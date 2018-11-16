package io.spring.billing.repositories;

import com.github.springtestdbunit.DbUnitTestExecutionListener;
import com.github.springtestdbunit.annotation.DatabaseSetup;
import io.spring.billing.entities.Line;
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
public class LineRepositoryTest {

    @Autowired
    private LineRepository repository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private BillRepository billRepository;

    @Test
    @DatabaseSetup("/db/billing.xml")
    public void testFindAll() {
        // Act
        List<Line> all = (List<Line>) this.repository.findAll();

        // Assert
        Assert.assertEquals(6, all.size());
    }

    @Test
    @DatabaseSetup("/db/billing.xml")
    public void testGetAmountSold() {
        // Act
        long amount = this.repository.findAmountSold(1L);

        // Assert
        Assert.assertEquals(6, amount);
    }

    @Test
    @DatabaseSetup("/db/billing.xml")
    public void testSave() {
        // Arrange
        final Line line = new Line();
        line.setQuantity(2);
        line.setProduct(this.productRepository.findById(1L).get());
        line.setBill(this.billRepository.findById(1L).get());

        // Act
        this.repository.save(line);

        // Assert
        List<Line> all = (List<Line>) this.repository.findAll();
        Assert.assertEquals(7, all.size());
    }

    @Test
    @DatabaseSetup("/db/billing.xml")
    public void testDelete() {
        // Arrange
        final Line line = this.repository.findById(1L).get();

        // Act
        this.repository.delete(line);

        // Assert
        List<Line> all = (List<Line>) this.repository.findAll();
        Assert.assertEquals(5, all.size());
    }

}
