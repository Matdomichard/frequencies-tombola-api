package com.frequencies.tombola.dto.helloasso;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class HelloAssoItemDto {
    private Long id;

    // Jackson va remplir ce nested object depuis "payer": { "email": ..., "firstName": ..., ... }
    private Payer payer;

    private Double amount;

    // Rename to match the JSON "state"
    @JsonProperty("state")
    private String state;

    // if you ever need the "type"
    private String type;

    @Data
    public static class Payer {
        private String email;
        private String firstName;
        private String lastName;
    }

    // helper getters to simplify your service
    public String getPayerEmail() {
        return payer != null ? payer.getEmail() : null;
    }
    public String getPayerFirstName() {
        return payer != null ? payer.getFirstName() : null;
    }
    public String getPayerLastName() {
        return payer != null ? payer.getLastName() : null;
    }
}
