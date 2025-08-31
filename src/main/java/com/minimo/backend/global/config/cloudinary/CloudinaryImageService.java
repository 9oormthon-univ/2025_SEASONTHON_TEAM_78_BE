package com.minimo.backend.global.config.cloudinary;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class CloudinaryImageService implements ImageStorageService {
    private final Cloudinary cloudinary;

    @Override
    public String uploadCertificationImage(Long challengeId, Long userId, MultipartFile file) {
        if (file == null || file.isEmpty()) {
            throw new IllegalArgumentException("이미지 파일이 비어 있습니다.");
        }
        try {
            String publicId = "minimo/certifications/challenge_" + challengeId
                    + "/user_" + userId + "/" + System.currentTimeMillis();

            Map<?, ?> result = cloudinary.uploader().upload(file.getBytes(), ObjectUtils.asMap(
                    "public_id", publicId,
                    "folder", "minimo/certifications",
                    "overwrite", true,
                    "resource_type", "image"
            ));
            Object secureUrl = result.get("secure_url");
            if (secureUrl == null) throw new IllegalStateException("Cloudinary secure_url 누락");
            return secureUrl.toString();
        } catch (IOException e) {
            throw new IllegalStateException("이미지 업로드 실패", e);
        }
    }
}
