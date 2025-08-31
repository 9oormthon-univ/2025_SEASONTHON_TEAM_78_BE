package com.minimo.backend.global.config.cloudinary;

import org.springframework.web.multipart.MultipartFile;

public interface ImageStorageService {

    String uploadCertificationImage(Long challengeId,
                                    Long userId,
                                    MultipartFile file);
}
