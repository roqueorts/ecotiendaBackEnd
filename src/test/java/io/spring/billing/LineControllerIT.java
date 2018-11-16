package io.spring.billing;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.springtestdbunit.DbUnitTestExecutionListener;
import io.spring.billing.controllers.dtos.LineDTO;
import io.spring.billing.entities.Line;
import io.spring.billing.repositories.LineRepository;
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
public class LineControllerIT {

    @Autowired
    private TestRestTemplate testRestTemplate;

    @Autowired
    private LineRepository repository;

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
        final Line saved1 = this.repository.save(this.buildLine(10));
        final Line saved2 = this.repository.save(this.buildLine(20));
        final Line saved3 = this.repository.save(this.buildLine(30));
        final Line saved4 = this.repository.save(this.buildLine(40));

        // Act
        ResponseEntity<String> exchange = testRestTemplate.exchange("/line", HttpMethod.GET, new HttpEntity<>(new HttpHeaders()), String.class);

        // Assert
        Assert.assertEquals(HttpStatus.OK, exchange.getStatusCode());
        JSONAssert.assertEquals("[{'id':" + +saved1.getId() + ",'quantity':10},{'id':" + +saved2.getId() + ",'quantity':20},{'id':" + +saved3.getId() + ",'quantity':30},{'id':" + +saved4.getId() + ",'quantity':40}]",
                exchange.getBody(),
                JSONCompareMode.LENIENT);
    }

    @Test
    public void testFindOne() throws JSONException {
        // Arrange
        final Line saved = this.repository.save(this.buildLine(10));

        // Act
        ResponseEntity<String> exchange = testRestTemplate.exchange("/line/" + saved.getId(), HttpMethod.GET, new HttpEntity<>(new HttpHeaders()), String.class);

        // Assert
        Assert.assertEquals(HttpStatus.OK, exchange.getStatusCode());
        JSONAssert.assertEquals("{'id':" + +saved.getId() + ",'quantity':10}",
                exchange.getBody(), JSONCompareMode.LENIENT);
    }

    @Test
    public void testCreate() throws JSONException, JsonProcessingException {
        // Arrange
        final LineDTO lineDTO = this.buildLineDTO(10);

        // Act
        ResponseEntity<String> exchange = testRestTemplate.exchange("/line", HttpMethod.POST, this.getPostRequest(lineDTO), String.class);

        // Assert
        Assert.assertEquals(HttpStatus.ACCEPTED, exchange.getStatusCode());
        JSONAssert.assertEquals("{'quantity':10}",
                exchange.getBody(), JSONCompareMode.LENIENT);
    }

    @Test
    public void testUpdate() throws JSONException, JsonProcessingException {
        // Arrange
        final Line saved = this.repository.save(this.buildLine(10));
        final LineDTO lineDTO = this.buildLineDTO(99);
        lineDTO.setId(saved.getId());

        // Act
        ResponseEntity<String> exchange = testRestTemplate.exchange("/line/" + saved.getId(), HttpMethod.PUT, this.getPostRequest(lineDTO), String.class);

        // Arrange
        Assert.assertEquals(HttpStatus.ACCEPTED, exchange.getStatusCode());
        JSONAssert.assertEquals("{'id':" + +saved.getId() + ",'quantity':99}",
                exchange.getBody(), JSONCompareMode.LENIENT);
    }

    private HttpEntity<String> getPostRequest(final Object object) throws JsonProcessingException {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        return new HttpEntity<>(mapper.writeValueAsString(object), headers);
    }

    private Line buildLine(final Integer quantity) {
        final Line line = new Line();
        line.setQuantity(quantity);
        return line;
    }

    private LineDTO buildLineDTO(final Integer quantity) {
        final LineDTO lineDTO = new LineDTO();
        lineDTO.setQuantity(quantity);
        return lineDTO;
    }
}
