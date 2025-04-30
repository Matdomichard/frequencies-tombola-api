package com.frequencies.tombola.dto.helloasso;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class HelloAssoFormIdDto {
    private Long id;
    private Long tombolaId;
    private String formSlug;
    private String formType;
}
