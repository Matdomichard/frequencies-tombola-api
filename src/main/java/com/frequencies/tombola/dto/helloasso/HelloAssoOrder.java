package com.frequencies.tombola.dto.helloasso;

import lombok.Data;

import java.util.List;

/**
 * Represents a HelloAsso order (which may include multiple payers).
 */
@Data
public class HelloAssoOrder {
    private String state;
    private List<HelloAssoPayer> payers;
}
