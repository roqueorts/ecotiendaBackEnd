package io.spring.billing.repositories;

import io.spring.billing.entities.Client;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.List;

public interface ClientRepository extends PagingAndSortingRepository<Client, Long> {

    List<Client> findByNameAndSurnameAllIgnoreCase(String name, String surname);

    List<Client> findBySurnameOrderByNameDesc(String surname);

    @Query("select c from Client c left join fetch c.bills b where c.id=?1")
    Client fetchByIdWithBills(Long id);

}
