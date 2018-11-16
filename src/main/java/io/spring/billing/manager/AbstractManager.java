package io.spring.billing.manager;

import io.spring.billing.entities.BillingEntity;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.Optional;

public abstract class AbstractManager<T extends BillingEntity> {

    public abstract CrudRepository<T, Long> getRepository();

    public List<T> findAll() {
        return (List<T>) this.getRepository().findAll();
    }

    public T save(final T element) {
        return this.getRepository().save(element);
    }

    public void delete(final T element) {
        this.getRepository().delete(element);
    }

    public T get(final long id) {
        return this.getRepository().findById(id).orElse(null);
    }

}
