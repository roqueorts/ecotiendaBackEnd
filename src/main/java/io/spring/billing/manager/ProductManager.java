package io.spring.billing.manager;

import io.spring.billing.entities.Product;
import io.spring.billing.repositories.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductManager extends AbstractManager<Product> {

    private ProductRepository repository;

    @Autowired
    public ProductManager(final ProductRepository repository) {
        this.repository = repository;
    }

    @Override
    public ProductRepository getRepository() {
        return this.repository;
    }

    public List<Product> findByPriceGreaterThanEqual(final Double price) {
        return this.repository.findByPriceGreaterThanEqual(price);
    }

    public List<Product> findByName(final String term) {
        return this.repository.findByName(term);
    }
}
