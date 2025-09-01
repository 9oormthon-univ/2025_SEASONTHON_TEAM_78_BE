package com.minimo.backend.global.config.cloudinary;

import io.jsonwebtoken.io.IOException;
import org.springframework.web.multipart.MultipartFile;

import java.util.Map;

public interface CloudinaryImageService {

    public Map upload(MultipartFile file);
    void delete(String publicId);
}
