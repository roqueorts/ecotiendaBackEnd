package io.spring.billing.controllers.dtos;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ProductDTO implements BillingDTO {
    private Long id;
    private String name;
    private Double price;
    private AuditDTO audit;
}
