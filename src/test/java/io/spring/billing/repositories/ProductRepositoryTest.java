package io.spring.billing.repositories;

import com.github.springtestdbunit.DbUnitTestExecutionListener;
import com.github.springtestdbunit.annotation.DatabaseSetup;
import io.spring.billing.entities.Product;
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
public class ProductRepositoryTest {

    @Autowired
    private ProductRepository repository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private BillRepository billRepository;

    @Test
    @DatabaseSetup("/db/billing.xml")
    public void testFindAll() {
        // Act
        List<Product> all = (List<Product>) this.repository.findAll();

        // Assert
        Assert.assertEquals(8, all.size());
    }

    @Test
    @DatabaseSetup("/db/billing.xml")
    public void testFindByPriceGreaterThanEqual() {
        // Act
        List<Product> all = this.repository.findByPriceGreaterThanEqual(100.0);

        // Assert
        Assert.assertEquals(3, all.size());
    }

    @Test
    @DatabaseSetup("/db/billing.xml")
    public void testFindByName() {
        // Act
        List<Product> all = this.repository.findByName("TRUM");

        // Assert
        Assert.assertEquals(0, all.size());
    }

    @Test
    @DatabaseSetup("/db/billing.xml")
    public void testSave() {
        // Arrange
        final Product product = new Product();
        product.setName("A");
        product.setPrice(100.0);

        // Act
        this.repository.save(product);

        // Assert
        List<Product> all = (List<Product>) this.repository.findAll();
        Assert.assertEquals(9, all.size());
    }

    @Test
    @DatabaseSetup("/db/billing.xml")
    public void testDelete() {
        // Arrange
        final Product product = this.repository.findById(3L).get();

        // Act
        this.repository.delete(product);

        // Assert
        List<Product> all = (List<Product>) this.repository.findAll();
        Assert.assertEquals(7, all.size());
    }

}
