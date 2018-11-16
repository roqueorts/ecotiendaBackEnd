package io.spring.billing.controllers.dtos;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class ClientDTO implements BillingDTO {
    private Long id;
    private String name;
    private String surname;
    private String email;
    private List<BillDTO> bills;
    private AuditDTO audit;
}
