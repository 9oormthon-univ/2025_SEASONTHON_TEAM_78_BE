package com.minimo.backend.certification.repository;

import com.minimo.backend.certification.dto.response.CertificationFeedResponse;
import com.minimo.backend.global.response.GlobalPageResponse;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface CertificationRepositoryCustom {
    GlobalPageResponse<CertificationFeedResponse> findCertificationFeed(Long userId, Pageable pageable);
}
