package io.spring.billing.repositories;

import io.spring.billing.entities.Product;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface ProductRepository extends CrudRepository<Product, Long> {

    List<Product> findByPriceGreaterThanEqual(Double price);

    @Query("select p from Product p where p.name like %?1%")
    List<Product> findByName(String term);

}
