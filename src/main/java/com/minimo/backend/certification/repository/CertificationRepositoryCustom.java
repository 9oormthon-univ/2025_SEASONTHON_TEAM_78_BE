package com.minimo.backend.certification.repository;

import com.minimo.backend.certification.dto.response.CertificationFeedResponse;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface CertificationRepositoryCustom {
    List<CertificationFeedResponse> findCertificationFeed(Long userId, Pageable pageable);
}
