package com.minimo.backend.global.response;


import lombok.Getter;
import org.springframework.data.domain.Page;

import java.util.List;

@Getter
public class GlobalPageResponse<T> {

    private final int pageNumber;

    private final int pageSize;

    private final long totalElements;

    private final int totalPages;

    private final String pageSort;

    private final List<T> pageContents;

    public GlobalPageResponse(
            int pageNumber,
            int pageSize,
            long totalElements,
            int totalPages,
            String pageSort,
            List<T> pageContents
    ) {
        this.pageNumber = pageNumber;
        this.pageSize = pageSize;
        this.totalElements = totalElements;
        this.totalPages = totalPages;
        this.pageSort = pageSort;
        this.pageContents = pageContents;
    }

    public static <T> GlobalPageResponse<T> create(Page<T> pageDto) {
        return new GlobalPageResponse<>(
                pageDto.getNumber(),
                pageDto.getSize(),
                pageDto.getTotalElements(),
                pageDto.getTotalPages(),
                pageDto.getSort().toString(),
                pageDto.getContent());
    }
}
