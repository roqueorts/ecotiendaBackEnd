package io.spring.billing.controllers;

import io.spring.billing.controllers.dtos.BillDTO;
import io.spring.billing.entities.Bill;
import io.spring.billing.manager.AbstractManager;
import io.spring.billing.manager.BillManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class BillControllerImpl extends AbstractController<Bill, BillDTO, Long> implements BillController {

    @Autowired
    private BillManager manager;

    public BillControllerImpl() {
        super(Bill.class, BillDTO.class);
    }

    @Override
    protected BillManager getManager() {
        return this.manager;
    }

    // @WTF
    public ResponseEntity<List<BillDTO>> findAll() {
        return new ResponseEntity<>(convertToDTOList(getManager().findAll()), HttpStatus.OK);
    }

}
