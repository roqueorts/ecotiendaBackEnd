package io.spring.billing.repositories;

import com.github.springtestdbunit.DbUnitTestExecutionListener;
import com.github.springtestdbunit.annotation.DatabaseSetup;
import io.spring.billing.entities.Client;
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
public class ClientRepositoryTest {

    @Autowired
    private ClientRepository repository;

    @Test
    @DatabaseSetup("/db/billing.xml")
    public void testFindAll() {
        // Act
        List<Client> all = (List<Client>) this.repository.findAll();

        // Assert
        Assert.assertEquals(5, all.size());
    }

    @Test
    @DatabaseSetup("/db/billing.xml")
    public void testFindByNameAndSurnameAllIgnoreCase() {
        // Act
        List<Client> all = this.repository.findByNameAndSurnameAllIgnoreCase("Paul", "MCCARTNEY");

        // Assert
        Assert.assertEquals(1, all.size());
    }

    @Test
    @DatabaseSetup("/db/billing.xml")
    public void testFindBySurnameOrderByNameAsc() {
        // Act
        List<Client> all = this.repository.findBySurnameOrderByNameDesc("lennon");

        // Assert
        Assert.assertEquals(2, all.size());
        Assert.assertEquals("julian", all.get(0).getName());
    }

    @Test
    @DatabaseSetup("/db/billing.xml")
    public void testFetchByIdWithBills() {
        // Act
        Client client = this.repository.fetchByIdWithBills(1L);

        // Assert
        Assert.assertNotNull(client);
        Assert.assertEquals(4, client.getBills().size());
    }

    @Test
    @DatabaseSetup("/db/billing.xml")
    public void testSave() {
        // Arrange
        final Client client = new Client();
        client.setName("a");
        client.setSurname("b");
        client.setEmail("c");

        // Act
        this.repository.save(client);

        // Assert
        List<Client> all = (List<Client>) this.repository.findAll();
        Assert.assertEquals(6, all.size());
    }

    @Test
    @DatabaseSetup("/db/billing.xml")
    public void testDelete() {
        // Arrange
        final Client client = this.repository.findById(1L).get();

        // Act
        this.repository.delete(client);

        // Assert
        List<Client> all = (List<Client>) this.repository.findAll();
        Assert.assertEquals(4, all.size());
    }

}
