package com.frequencies.tombola.dto.helloasso;

import lombok.Data;
import java.util.List;

@Data
public class HelloAssoFormsResponse {
    private List<HelloAssoFormDto> data;
    private Pagination pagination;

    @Data
    public static class Pagination {
        private Integer pageSize;
        private Integer totalCount;
        private Integer pageIndex;
        private Integer totalPages;
        private String continuationToken;
    }
}
