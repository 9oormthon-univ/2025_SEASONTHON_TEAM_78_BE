package com.minimo.backend.certification.repository;

import com.minimo.backend.certification.domain.QCertification;
import com.minimo.backend.certification.domain.QReaction;
import com.minimo.backend.certification.dto.response.CertificationFeedResponse;
import com.minimo.backend.global.response.GlobalPageResponse;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.util.List;

public class CertificationRepositoryCustomImpl implements CertificationRepositoryCustom {

    private final JPAQueryFactory jpaQueryFactory;

    public CertificationRepositoryCustomImpl(JPAQueryFactory jpaQueryFactory) {
        this.jpaQueryFactory = jpaQueryFactory;
    }

    @Override
    public GlobalPageResponse<CertificationFeedResponse> findCertificationFeed(Long userId, Pageable pageable) {
        QCertification c = QCertification.certification;
        QReaction r = QReaction.reaction;

        List<CertificationFeedResponse> content = jpaQueryFactory
                .select(Projections.bean(CertificationFeedResponse.class,
                        c.id.as("certificationId"),
                        c.title,
                        c.content,
                        c.imageUrl,
                        c.user.id.as("authorId"),
                        c.user.nickname.as("authorNickname"),
                        c.user.picture.as("authorPicture"),
                        r.countDistinct().intValue().as("totalReactions")
                ))
                .from(c)
                .leftJoin(r).on(r.certification.eq(c))
                .groupBy(c.id, c.title, c.content, c.imageUrl, c.user.id, c.user.nickname, c.user.picture)
                .orderBy(c.createdAt.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        // 전체 카운트
        Long total = jpaQueryFactory
                .select(c.count())
                .from(c)
                .fetchOne();

        if (total == null) {
            total = 0L; // NPE 방지
        }

        Page<CertificationFeedResponse> page = new PageImpl<>(content, pageable, total);

        return GlobalPageResponse.create(page);
    }
}