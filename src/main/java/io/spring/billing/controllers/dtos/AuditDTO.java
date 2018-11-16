package io.spring.billing.controllers.dtos;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
public class AuditDTO {
    private Date createOn;
    private Date updateOn;
}
