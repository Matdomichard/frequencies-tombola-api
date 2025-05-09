package com.frequencies.tombola.dto.helloasso;

import com.frequencies.tombola.dto.PlayerDto;
import lombok.Data;

import java.time.OffsetDateTime;
import java.util.List;

@Data
public class HelloAssoOrdersResponse {
    private List<PaymentWrapper> data;
    private Pagination pagination;

    /**
     * Shared meta information used by OrderDto, PaymentWrapper and RefundOperation.
     */
    @Data
    public static class Meta {
        private OffsetDateTime createdAt;
        private OffsetDateTime updatedAt;
    }

    @Data
    public static class PaymentWrapper {
        private OrderDto order;
        private HelloAssoPayer payer;          // ← renamed from `player` to `payer`
        private List<ItemDto> items;
        private OffsetDateTime cashOutDate;
        private String cashOutState;
        private String paymentReceiptUrl;
        private String fiscalReceiptUrl;
        private Long id;
        private Double amount;
        private Double amountTip;
        private OffsetDateTime date;
        private String paymentMeans;
        private Integer installmentNumber;
        private String state;
        private String type;
        private Meta meta;
        private String paymentOffLineMean;
        private List<RefundOperation> refundOperations;
    }

    @Data
    public static class OrderDto {
        private Long id;
        private OffsetDateTime date;
        private String formSlug;
        private String formType;
        private String organizationName;
        private String organizationSlug;
        private String organizationType;
        private Boolean organizationIsUnderColucheLaw;
        private Long checkoutIntentId;
        private Meta meta;
    }

    @Data
    public static class PayerDto {
        private String email;
        private String address;
        private String city;
        private String zipCode;
        private String country;
        private String company;
        private OffsetDateTime dateOfBirth;
        private String firstName;
        private String lastName;
    }

    @Data
    public static class ItemDto {
        private Double shareAmount;
        private Double shareItemAmount;
        private Double shareOptionsAmount;
        private Long id;
        private Double amount;
        private String type;
        private Double initialAmount;
        private String state;
        private String name;
    }

    @Data
    public static class RefundOperation {
        private Long id;
        private Double amount;
        private Double amountTip;
        private String status;
        private Meta meta;
    }

    @Data
    public static class Pagination {
        private Integer pageSize;
        private Integer totalCount;
        private Integer pageIndex;
        private Integer totalPages;
        private String continuationToken;
    }

}
