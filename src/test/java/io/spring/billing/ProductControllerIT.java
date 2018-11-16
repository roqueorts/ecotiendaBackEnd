package io.spring.billing;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.springtestdbunit.DbUnitTestExecutionListener;
import io.spring.billing.controllers.dtos.ProductDTO;
import io.spring.billing.entities.Product;
import io.spring.billing.repositories.ProductRepository;
import lombok.extern.java.Log;
import org.json.JSONException;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.skyscreamer.jsonassert.JSONAssert;
import org.skyscreamer.jsonassert.JSONCompareMode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.*;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;
import org.springframework.test.context.support.DirtiesContextTestExecutionListener;
import org.springframework.test.context.transaction.TransactionalTestExecutionListener;

@Log
@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestExecutionListeners({DependencyInjectionTestExecutionListener.class,
        DirtiesContextTestExecutionListener.class,
        TransactionalTestExecutionListener.class,
        DbUnitTestExecutionListener.class})
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class ProductControllerIT {

    @Autowired
    private TestRestTemplate testRestTemplate;

    @Autowired
    private ProductRepository repository;

    private ObjectMapper mapper = new ObjectMapper();

    @Before
    public void init() {
        log.info("Removing all data at init...");
        this.clearData();
    }

    @After
    public void finish() {
        log.info("Removing all data at finish...");
        this.clearData();
    }

    private void clearData() {
        this.repository.deleteAll();
    }

    @Test
    public void testFindAll() throws JSONException {
        // Arrange
        final Product saved1 = this.repository.save(this.buildProduct("name-1", 10.0));
        final Product saved2 = this.repository.save(this.buildProduct("name-2", 20.0));
        final Product saved3 = this.repository.save(this.buildProduct("name-3", 30.0));
        final Product saved4 = this.repository.save(this.buildProduct("name-4", 40.0));

        // Act
        ResponseEntity<String> exchange = testRestTemplate.exchange("/product", HttpMethod.GET, new HttpEntity<>(new HttpHeaders()), String.class);

        // Assert
        Assert.assertEquals(HttpStatus.OK, exchange.getStatusCode());
        JSONAssert.assertEquals("[{'id':" + +saved1.getId() + ",'name':'name-1','price':10.0},{'id':" + +saved2.getId() + ",'name':'name-2','price':20.0},{'id':" + +saved3.getId() + ",'name':'name-3','price':30.0},{'id':" + +saved4.getId() + ",'name':'name-4','price':40.0}]",
                exchange.getBody(),
                JSONCompareMode.LENIENT);
    }

    @Test
    public void testFindOne() throws JSONException {
        // Arrange
        final Product saved = this.repository.save(this.buildProduct("name-1", 10.0));

        // Act
        ResponseEntity<String> exchange = testRestTemplate.exchange("/product/" + saved.getId(), HttpMethod.GET, new HttpEntity<>(new HttpHeaders()), String.class);

        // Assert
        Assert.assertEquals(HttpStatus.OK, exchange.getStatusCode());
        JSONAssert.assertEquals("{'id':" + +saved.getId() + ",'name':'name-1','price':10.0}",
                exchange.getBody(), JSONCompareMode.LENIENT);
    }

    @Test
    public void testCreate() throws JSONException, JsonProcessingException {
        // Arrange
        final ProductDTO productDTO = this.buildProductDTO("name-1", 10.0);

        // Act
        ResponseEntity<String> exchange = testRestTemplate.exchange("/product", HttpMethod.POST, this.getPostRequest(productDTO), String.class);

        // Assert
        Assert.assertEquals(HttpStatus.ACCEPTED, exchange.getStatusCode());
        JSONAssert.assertEquals("{'name':'name-1','price':10.0}",
                exchange.getBody(), JSONCompareMode.LENIENT);
    }

    @Test
    public void testUpdate() throws JSONException, JsonProcessingException {
        // Arrange
        final Product saved = this.repository.save(this.buildProduct("name-1", 10.0));
        final ProductDTO productDTO = this.buildProductDTO("name-changed", 99.0);
        productDTO.setId(saved.getId());

        // Act
        ResponseEntity<String> exchange = testRestTemplate.exchange("/product/" + saved.getId(), HttpMethod.PUT, this.getPostRequest(productDTO), String.class);

        // Arrange
        Assert.assertEquals(HttpStatus.ACCEPTED, exchange.getStatusCode());
        JSONAssert.assertEquals("{'id':" + +saved.getId() + ",'name':'name-changed','price':99.0}",
                exchange.getBody(), JSONCompareMode.LENIENT);
    }

    @Test
    public void testRemove() {
        // Arrange
        final Product saved = this.repository.save(this.buildProduct("name-1", 10.0));

        // Act
        ResponseEntity<String> exchange = testRestTemplate.exchange("/product/" + saved.getId(), HttpMethod.DELETE, new HttpEntity<>(new HttpHeaders()), String.class);

        // Assert
        Assert.assertEquals(HttpStatus.OK, exchange.getStatusCode());
    }

    private HttpEntity<String> getPostRequest(final Object object) throws JsonProcessingException {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        return new HttpEntity<>(mapper.writeValueAsString(object), headers);
    }

    private Product buildProduct(final String name, final Double price) {
        final Product product = new Product();
        product.setName(name);
        product.setPrice(price);
        return product;
    }

    private ProductDTO buildProductDTO(final String name, final Double price) {
        final ProductDTO productDTO = new ProductDTO();
        productDTO.setName(name);
        productDTO.setPrice(price);
        return productDTO;
    }
}
