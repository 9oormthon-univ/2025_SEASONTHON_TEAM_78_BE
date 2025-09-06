package com.minimo.backend.certification.repository;

import com.minimo.backend.certification.domain.QCertification;
import com.minimo.backend.certification.domain.QReaction;
import com.minimo.backend.certification.dto.response.CertificationFeedResponse;
import com.minimo.backend.certification.dto.response.ReactionCountResponse;
import com.minimo.backend.global.response.GlobalPageResponse;
import com.querydsl.core.Tuple;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RequiredArgsConstructor // 생성자 주입을 위해 변경
public class CertificationRepositoryCustomImpl implements CertificationRepositoryCustom {

    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public GlobalPageResponse<CertificationFeedResponse> findCertificationFeed(Long userId, Pageable pageable) {
        QCertification c = QCertification.certification;

        // 페이징을 적용하여 Certification의 기본 정보만 조회 (Join 없음)
        List<CertificationFeedResponse> content = jpaQueryFactory
                .select(Projections.constructor(
                        CertificationFeedResponse.class,
                        c.id,
                        c.title,
                        c.content,
                        c.imageUrl,
                        c.user.id,
                        c.user.nickname,
                        c.user.picture,
                        Expressions.constant(0),
                        Expressions.nullExpression(List.class)
                ))
                .from(c)
                .orderBy(c.createdAt.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        // 조회된 인증글이 없다면 바로 반환
        if (content.isEmpty()) {
            // PageImpl을 사용하여 비어있는 Page 객체 생성
            Page<CertificationFeedResponse> emptyPage = new PageImpl<>(List.of(), pageable, 0);
            return GlobalPageResponse.create(emptyPage); // 기존 create 메소드 사용
        }

        // 인증글 ID 리스트 추출
        List<Long> certificationIds = content.stream()
                .map(CertificationFeedResponse::getCertificationId)
                .toList();

        QReaction r = QReaction.reaction;

        // 조회된 인증글 ID에 해당하는 모든 Reaction 정보를 한 번에 조회
        List<Tuple> reactionTuples = jpaQueryFactory
                .select(r.certification.id, r.emojiType, r.count())
                .from(r)
                .where(r.certification.id.in(certificationIds))
                .groupBy(r.certification.id, r.emojiType)
                .fetch();

        // 인증글 ID를 key로, ReactionCountResponse 리스트를 value로 하는 Map 생성
        Map<Long, List<ReactionCountResponse>> reactionMap = reactionTuples.stream()
                .collect(Collectors.groupingBy(
                        t -> t.get(r.certification.id),
                        Collectors.mapping(
                                t -> new ReactionCountResponse(t.get(r.emojiType), t.get(r.count()).intValue()),
                                Collectors.toList()
                        )
                ));

        //  메모리에서 데이터 조합 (Reaction 정보 주입)
        content.forEach(feed -> {
            List<ReactionCountResponse> reactionCounts = reactionMap.getOrDefault(feed.getCertificationId(), List.of());
            int totalReactions = reactionCounts.stream()
                    .mapToInt(ReactionCountResponse::getCount)
                    .sum();
            feed.setReactionCounts(reactionCounts);
            feed.setTotalReactions(totalReactions);
        });

        // 전체 카운트 쿼리
        Long total = jpaQueryFactory.select(c.count()).from(c).fetchOne();
        if (total == null) total = 0L;

        Page<CertificationFeedResponse> page = new PageImpl<>(content, pageable, total);

        return GlobalPageResponse.create(page);
    }
}