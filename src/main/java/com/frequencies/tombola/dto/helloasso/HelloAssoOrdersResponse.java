package com.frequencies.tombola.dto.helloasso;

import lombok.Data;

import java.util.List;

/**
 * Wrapper for the list of orders returned by the HelloAsso API.
 */
@Data
public class HelloAssoOrdersResponse {
    private List<HelloAssoOrder> data;
}
