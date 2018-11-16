package io.spring.billing.repositories;

import io.spring.billing.entities.Line;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

public interface LineRepository extends CrudRepository<Line, Long> {

    @Query("select sum(l.quantity) from Line l where l.product.id = ?1")
    long findAmountSold(Long productId);

}
