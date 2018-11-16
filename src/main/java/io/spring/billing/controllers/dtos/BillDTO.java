package io.spring.billing.controllers.dtos;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class BillDTO implements BillingDTO {
    private Long id;
    private String description;
    private String observation;
    private List<LineDTO> lines;
    private AuditDTO audit;

}
