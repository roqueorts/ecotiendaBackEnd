package io.spring.billing.repositories;

import com.github.springtestdbunit.DbUnitTestExecutionListener;
import com.github.springtestdbunit.annotation.DatabaseSetup;
import io.spring.billing.entities.Bill;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;
import org.springframework.test.context.support.DirtiesContextTestExecutionListener;
import org.springframework.test.context.transaction.TransactionalTestExecutionListener;

import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestExecutionListeners({DependencyInjectionTestExecutionListener.class,
        DirtiesContextTestExecutionListener.class,
        TransactionalTestExecutionListener.class,
        DbUnitTestExecutionListener.class})
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class BillRepositoryTest {

    @Autowired
    private BillRepository repository;

    @Autowired
    private ClientRepository clientRepository;

    @Test
    @DatabaseSetup("/db/billing.xml")
    public void testFindAll() {
        // Act
        List<Bill> all = (List<Bill>) this.repository.findAll();

        // Assert
        Assert.assertEquals(8, all.size());
    }

    @Test
    @DatabaseSetup("/db/billing.xml")
    public void testFindAllByClientId() {
        // Act
        List<Bill> all =this.repository.findAllByClientId(1L);

        // Assert
        Assert.assertEquals(4, all.size());
    }

    @Test
    @DatabaseSetup("/db/billing.xml")
    public void testFetchByIdWithClientWithLinesWithProduct() {
        // Act
        Bill bill =this.repository.fetchByIdWithClientWithLinesWithProduct(1L);

        // Assert
        Assert.assertEquals(1, (long)bill.getClient().getId());
        Assert.assertEquals(2, bill.getLines().size());
    }

    @Test
    @DatabaseSetup("/db/billing.xml")
    public void testSave() {
        // Arrange
        final Bill bill = new Bill();
        bill.setDescription("A");
        bill.setObservation("B");
        bill.setClient(this.clientRepository.findById(1L).get());

        // Act
        this.repository.save(bill);

        // Assert
        List<Bill> all = (List<Bill>) this.repository.findAll();
        Assert.assertEquals(9, all.size());
    }

    @Test
    @DatabaseSetup("/db/billing.xml")
    public void testDelete() {
        // Arrange
        final Bill bill = this.repository.findById(1L).get();

        // Act
        this.repository.delete(bill);

        // Assert
        List<Bill> all = (List<Bill>) this.repository.findAll();
        Assert.assertEquals(7, all.size());
    }

}
