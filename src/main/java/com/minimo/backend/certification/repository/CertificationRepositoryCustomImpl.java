package com.minimo.backend.certification.repository;

import com.minimo.backend.certification.domain.QCertification;
import com.minimo.backend.certification.domain.QReaction;
import com.minimo.backend.certification.dto.response.CertificationFeedResponse;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.data.domain.Pageable;

import java.util.List;

public class CertificationRepositoryCustomImpl implements CertificationRepositoryCustom {

    private final JPAQueryFactory jpaQueryFactory;

    public CertificationRepositoryCustomImpl(JPAQueryFactory jpaQueryFactory) {
        this.jpaQueryFactory = jpaQueryFactory;
    }

    @Override
    public List<CertificationFeedResponse> findCertificationFeed(Long userId, Pageable pageable) {
        QCertification c = QCertification.certification;
        QReaction r = QReaction.reaction;

        return jpaQueryFactory
                .select(Projections.bean(CertificationFeedResponse.class,
                        c.id.as("certificationId"),
                        c.title,
                        c.content,
                        c.imageUrl,
                        c.user.id.as("userId"),
                        c.user.nickname,
                        c.user.picture,
                        r.countDistinct().intValue().as("totalReactions")
                ))
                .from(c)
                .leftJoin(r).on(r.certification.eq(c))
                .groupBy(c.id, c.title, c.content, c.imageUrl, c.user.id, c.user.nickname, c.user.picture)
                .orderBy(c.createdAt.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();
    }
}