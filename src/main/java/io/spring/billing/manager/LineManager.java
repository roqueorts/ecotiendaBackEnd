package io.spring.billing.manager;

import io.spring.billing.entities.Line;
import io.spring.billing.repositories.LineRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class LineManager extends AbstractManager<Line> {

    private LineRepository repository;

    @Autowired
    public LineManager(final LineRepository repository) {
        this.repository = repository;
    }

    @Override
    public LineRepository getRepository() {
        return this.repository;
    }

    public long findAmountSold(final Long productId) {
        return this.repository.findAmountSold(productId);
    }
}
