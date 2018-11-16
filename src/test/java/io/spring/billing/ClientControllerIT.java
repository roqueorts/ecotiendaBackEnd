package io.spring.billing;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.springtestdbunit.DbUnitTestExecutionListener;
import io.spring.billing.controllers.dtos.ClientDTO;
import io.spring.billing.entities.Client;
import io.spring.billing.repositories.ClientRepository;
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
public class ClientControllerIT {

    @Autowired
    private TestRestTemplate testRestTemplate;

    @Autowired
    private ClientRepository repository;

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
        final Client saved1 = this.repository.save(this.buildClient("name-1", "surname-1", "email-1"));
        final Client saved2 = this.repository.save(this.buildClient("name-2", "surname-2", "email-2"));
        final Client saved3 = this.repository.save(this.buildClient("name-3", "surname-3", "email-3"));
        final Client saved4 = this.repository.save(this.buildClient("name-4", "surname-4", "email-4"));

        // Act
        ResponseEntity<String> exchange = testRestTemplate.exchange("/client", HttpMethod.GET, new HttpEntity<>(new HttpHeaders()), String.class);

        // Assert
        Assert.assertEquals(HttpStatus.OK, exchange.getStatusCode());
        JSONAssert.assertEquals("[{'id':" + +saved1.getId() + ",'name':'name-1','surname':'surname-1','email':'email-1'},{'id':" + +saved2.getId() + ",'name':'name-2','surname':'surname-2','email':'email-2'},{'id':" + +saved3.getId() + ",'name':'name-3','surname':'surname-3','email':'email-3'},{'id':" + +saved4.getId() + ",'name':'name-4','surname':'surname-4','email':'email-4'}]",
                exchange.getBody(),
                JSONCompareMode.LENIENT);
    }

    @Test
    public void testFindOne() throws JSONException {
        // Arrange
        final Client saved = this.repository.save(this.buildClient("name-1", "surname-1", "email-1"));

        // Act
        ResponseEntity<String> exchange = testRestTemplate.exchange("/client/" + saved.getId(), HttpMethod.GET, new HttpEntity<>(new HttpHeaders()), String.class);

        // Assert
        Assert.assertEquals(HttpStatus.OK, exchange.getStatusCode());
        JSONAssert.assertEquals("{'id':" + +saved.getId() + ",'name':'name-1','surname':'surname-1','email':'email-1'}",
                exchange.getBody(), JSONCompareMode.LENIENT);
    }

    @Test
    public void testCreate() throws JSONException, JsonProcessingException {
        // Arrange
        final ClientDTO clientDTO = this.buildClientDTO("name-1", "surname-1", "email-1");

        // Act
        ResponseEntity<String> exchange = testRestTemplate.exchange("/client", HttpMethod.POST, this.getPostRequest(clientDTO), String.class);

        // Assert
        Assert.assertEquals(HttpStatus.ACCEPTED, exchange.getStatusCode());
        JSONAssert.assertEquals("{'name':'name-1','surname':'surname-1','email':'email-1'}",
                exchange.getBody(), JSONCompareMode.LENIENT);
    }

    @Test
    public void testUpdate() throws JSONException, JsonProcessingException {
        // Arrange
        final Client saved = this.repository.save(this.buildClient("name-1", "surname-1", "email-1"));
        final ClientDTO clientDTO = this.buildClientDTO("name-changed", "surname-changed", "email-changed");
        clientDTO.setId(saved.getId());

        // Act
        ResponseEntity<String> exchange = testRestTemplate.exchange("/client/" + saved.getId(), HttpMethod.PUT, this.getPostRequest(clientDTO), String.class);

        // Arrange
        Assert.assertEquals(HttpStatus.ACCEPTED, exchange.getStatusCode());
        JSONAssert.assertEquals("{'id':" + +saved.getId() + ",'name':'name-changed','surname':'surname-changed','email':'email-changed'}",
                exchange.getBody(), JSONCompareMode.LENIENT);
    }

    @Test
    public void testRemove() {
        // Arrange
        final Client saved = this.repository.save(this.buildClient("name-1", "surname-1", "email-1"));

        // Act
        ResponseEntity<String> exchange = testRestTemplate.exchange("/client/" + saved.getId(), HttpMethod.DELETE, new HttpEntity<>(new HttpHeaders()), String.class);

        // Assert
        Assert.assertEquals(HttpStatus.OK, exchange.getStatusCode());
    }

    private HttpEntity<String> getPostRequest(final Object object) throws JsonProcessingException {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        return new HttpEntity<>(mapper.writeValueAsString(object), headers);
    }

    private Client buildClient(final String name, final String surname, final String email) {
        final Client client = new Client();
        client.setName(name);
        client.setSurname(surname);
        client.setEmail(email);
        return client;
    }

    private ClientDTO buildClientDTO(final String name, final String surname, final String email) {
        final ClientDTO clientDTO = new ClientDTO();
        clientDTO.setName(name);
        clientDTO.setSurname(surname);
        clientDTO.setEmail(email);
        return clientDTO;
    }
}
