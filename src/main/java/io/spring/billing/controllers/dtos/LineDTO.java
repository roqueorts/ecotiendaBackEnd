package io.spring.billing.controllers.dtos;

import io.spring.billing.entities.Product;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LineDTO implements BillingDTO {
    private Long id;
    private Integer quantity;
    private ProductDTO product;
    private AuditDTO audit;
}
