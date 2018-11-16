package io.spring.billing.entities;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Embeddable;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import java.util.Date;

@Getter
@Setter
@Embeddable
public class Audit {

    private Date createOn;

    private Date updateOn;

    @PrePersist
    public void prePersist() {
        this.createOn = new Date();
    }

    @PreUpdate
    public void preUpdate() {
        this.updateOn = new Date();
    }

}
