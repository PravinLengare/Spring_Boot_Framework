package com.hoolistem.payload.common;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.Page;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class PaginatedResponse<T> {

    private List<T> data;
    private PaginationMeta meta;

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
    public static class PaginationMeta {
        private int currentPage;
        private int pageSize;
        private int totalPages;
        private long totalItems;
        private boolean hasNext;
        private boolean hasPrevious;
    }

    // A handy constructor to instantly convert Spring's Page<T> into this clean format
    public static <T> PaginatedResponse<T> from(Page<T> springPage) {
        PaginationMeta meta = PaginationMeta.builder()
                .currentPage(springPage.getNumber() + 1) // Convert 0-index back to 1-index for frontend
                .pageSize(springPage.getSize())
                .totalPages(springPage.getTotalPages())
                .totalItems(springPage.getTotalElements())
                .hasNext(springPage.hasNext())
                .hasPrevious(springPage.hasPrevious())
                .build();

        return PaginatedResponse.<T>builder()
                .data(springPage.getContent())
                .meta(meta)
                .build();
    }
}