package io.spring.billing;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.springtestdbunit.DbUnitTestExecutionListener;
import io.spring.billing.controllers.dtos.BillDTO;
import io.spring.billing.entities.Bill;
import io.spring.billing.repositories.BillRepository;
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
public class BillControllerIT {

    @Autowired
    private TestRestTemplate testRestTemplate;

    @Autowired
    private BillRepository repository;

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
        final Bill saved1 = this.repository.save(this.buildBill("description-1", "observation-1"));
        final Bill saved2 = this.repository.save(this.buildBill("description-2", "observation-2"));
        final Bill saved3 = this.repository.save(this.buildBill("description-3", "observation-3"));
        final Bill saved4 = this.repository.save(this.buildBill("description-4", "observation-4"));

        // Act
        ResponseEntity<String> exchange = testRestTemplate.exchange("/bill", HttpMethod.GET, new HttpEntity<>(new HttpHeaders()), String.class);

        // Assert
        Assert.assertEquals(HttpStatus.OK, exchange.getStatusCode());
        JSONAssert.assertEquals("[{'id':" + +saved1.getId() + ",'description':'description-1','observation':'observation-1'},{'id':" + +saved2.getId() + ",'description':'description-2','observation':'observation-2'},{'id':" + +saved3.getId() + ",'description':'description-3','observation':'observation-3'},{'id':" + +saved4.getId() + ",'description':'description-4','observation':'observation-4'}]",
                exchange.getBody(),
                JSONCompareMode.LENIENT);
    }

    @Test
    public void testFindOne() throws JSONException {
        // Arrange
        final Bill saved = this.repository.save(this.buildBill("description-1", "observation-1"));

        // Act
        ResponseEntity<String> exchange = testRestTemplate.exchange("/bill/" + saved.getId(), HttpMethod.GET, new HttpEntity<>(new HttpHeaders()), String.class);

        // Assert
        Assert.assertEquals(HttpStatus.OK, exchange.getStatusCode());
        JSONAssert.assertEquals("{'id':" + +saved.getId() + ",'description':'description-1','observation':'observation-1'}",
                exchange.getBody(), JSONCompareMode.LENIENT);
    }

    @Test
    public void testCreate() throws JSONException, JsonProcessingException {
        // Arrange
        final BillDTO billDTO = this.buildBillDTO("description-1", "observation-1");

        // Act
        ResponseEntity<String> exchange = testRestTemplate.exchange("/bill", HttpMethod.POST, this.getPostRequest(billDTO), String.class);

        // Assert
        Assert.assertEquals(HttpStatus.ACCEPTED, exchange.getStatusCode());
        JSONAssert.assertEquals("{'description':'description-1','observation':'observation-1'}",
                exchange.getBody(), JSONCompareMode.LENIENT);
    }

    @Test
    public void testUpdate() throws JSONException, JsonProcessingException {
        // Arrange
        final Bill saved = this.repository.save(this.buildBill("description-1", "observation-1"));
        final BillDTO billDTO = this.buildBillDTO("description-changed", "observation-changed");
        billDTO.setId(saved.getId());

        // Act
        ResponseEntity<String> exchange = testRestTemplate.exchange("/bill/" + saved.getId(), HttpMethod.PUT, this.getPostRequest(billDTO), String.class);

        // Arrange
        Assert.assertEquals(HttpStatus.ACCEPTED, exchange.getStatusCode());
        JSONAssert.assertEquals("{'id':" + +saved.getId() + ",'description':'description-changed','observation':'observation-changed'}",
                exchange.getBody(), JSONCompareMode.LENIENT);
    }

    @Test
    public void testRemove() {
        // Arrange
        final Bill saved = this.repository.save(this.buildBill("description-1", "observation-1"));

        // Act
        ResponseEntity<String> exchange = testRestTemplate.exchange("/bill/" + saved.getId(), HttpMethod.DELETE, new HttpEntity<>(new HttpHeaders()), String.class);

        // Assert
        Assert.assertEquals(HttpStatus.OK, exchange.getStatusCode());
    }

    private HttpEntity<String> getPostRequest(final Object object) throws JsonProcessingException {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        return new HttpEntity<>(mapper.writeValueAsString(object), headers);
    }

    private Bill buildBill(final String description, final String observation) {
        final Bill bill = new Bill();
        bill.setDescription(description);
        bill.setObservation(observation);
        return bill;
    }

    private BillDTO buildBillDTO(final String description, final String observation) {
        final BillDTO billDTO = new BillDTO();
        billDTO.setDescription(description);
        billDTO.setObservation(observation);
        return billDTO;
    }
}
