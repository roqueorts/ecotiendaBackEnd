package io.spring.billing.manager;

import io.spring.billing.entities.Bill;
import io.spring.billing.repositories.BillRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BillManager extends AbstractManager<Bill> {

    private BillRepository repository;

    @Autowired
    public BillManager(final BillRepository repository) {
        this.repository = repository;
    }

    @Override
    public BillRepository getRepository() {
        return this.repository;
    }

    public Bill fetchByIdWithClientWithLinesWithProduct(final Long id) {
        return this.repository.fetchByIdWithClientWithLinesWithProduct(id);
    }

    public List<Bill> findAllByClientId(final Long id) {
        return this.repository.findAllByClientId(id);
    }
}
