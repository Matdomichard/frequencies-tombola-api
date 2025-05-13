package com.frequencies.tombola.dto.helloasso;

import lombok.Data;

import java.util.List;

@Data
public class HelloAssoItemListResponse {
    private List<HelloAssoItemDto> data;
    private Pagination pagination;

    @Data
    public static class Pagination {
        private String continuationToken;
    }
}

