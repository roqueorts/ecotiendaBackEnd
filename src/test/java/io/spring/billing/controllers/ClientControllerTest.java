package io.spring.billing.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.spring.billing.controllers.dtos.AuditDTO;
import io.spring.billing.controllers.dtos.ClientDTO;
import io.spring.billing.entities.Audit;
import io.spring.billing.entities.Client;
import io.spring.billing.manager.ClientManager;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.modelmapper.ModelMapper;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.ArrayList;
import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
public class ClientControllerTest {

    @InjectMocks
    private ClientControllerImpl controller;

    @Mock
    private ClientManager mockManager;

    @Mock
    private ModelMapper mockModelMapper;

    private MockMvc mockMvc;

    private ObjectMapper mapper;

    @Before
    public void setup() {
        this.mockMvc = MockMvcBuilders.standaloneSetup(controller).build();
        this.mapper = new ObjectMapper();
    }

    @Test
    public void testGetAllNoContent() throws Exception {
        // Arrange
        Mockito.when(mockManager.findAll()).thenReturn(null);

        // Act
        ResultActions perform = mockMvc.perform(get("/client"));

        // Assert
        perform.andExpect(status().isOk());
    }

    @Test
    public void testGetAllWithContent() throws Exception {
        // Arrange
        Client client_1 = new Client();
        client_1.setId(1L);
        client_1.setAudit(new Audit());

        Client client_2 = new Client();
        client_2.setId(2L);
        client_2.setAudit(new Audit());

        List<Client> clients = new ArrayList<>();
        clients.add(client_1);
        clients.add(client_2);

        ClientDTO clientDTO_1 = new ClientDTO();
        clientDTO_1.setId(1L);
        clientDTO_1.setAudit(new AuditDTO());

        ClientDTO clientDTO_2 = new ClientDTO();
        clientDTO_2.setId(2L);
        clientDTO_2.setAudit(new AuditDTO());

        List<ClientDTO> clientDTOs = new ArrayList<>();
        clientDTOs.add(clientDTO_1);
        clientDTOs.add(clientDTO_2);

        Mockito.when(mockManager.findAll()).thenReturn(clients);
        Mockito.when(mockModelMapper.map(client_1, ClientDTO.class)).thenReturn(clientDTO_1);
        Mockito.when(mockModelMapper.map(client_2, ClientDTO.class)).thenReturn(clientDTO_2);

        // Act
        ResultActions perform = mockMvc.perform(get("/client"));

        // Assert
        perform.andExpect(status().isOk());
        perform.andExpect(content().json(mapper.writeValueAsString(clientDTOs)));
    }

    @Test
    public void testFindById() throws Exception {
        // Arrange
        Client client = new Client();
        client.setId(1L);
        client.setAudit(new Audit());

        ClientDTO clientDTO = new ClientDTO();
        clientDTO.setId(1L);
        clientDTO.setAudit(new AuditDTO());

        Mockito.when(mockManager.get(1L)).thenReturn(client);
        Mockito.when(mockModelMapper.map(client, ClientDTO.class)).thenReturn(clientDTO);

        // Act
        ResultActions perform = mockMvc.perform(get("/client/1"));

        // Assert
        perform.andExpect(status().isOk());
        perform.andExpect(content().json(mapper.writeValueAsString(clientDTO)));
    }

    @Test
    public void testCreate() throws Exception {
        // Arrange
        Client client = new Client();
        client.setName("name");
        client.setSurname("surname");
        client.setEmail("email");

        ClientDTO clientDTO = new ClientDTO();
        clientDTO.setId(1L);
        clientDTO.setName("name");
        clientDTO.setSurname("surname");
        clientDTO.setEmail("email");
        clientDTO.setAudit(new AuditDTO());

        Mockito.when(mockModelMapper.map(Mockito.any(ClientDTO.class), Mockito.eq(Client.class))).thenReturn(client);
        Mockito.when(mockManager.save(client)).thenReturn(client);
        Mockito.when(mockModelMapper.map(client, ClientDTO.class)).thenReturn(clientDTO);

        // Act
        ResultActions perform = mockMvc.perform(post("/client")
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(clientDTO)));

        // Assert
        perform.andExpect(status().isAccepted());
        perform.andExpect(content().json(mapper.writeValueAsString(clientDTO)));
    }

    @Test
    public void testUpdate() throws Exception {
        // Arrange
        Client client = new Client();
        client.setName("name");
        client.setSurname("surname");
        client.setEmail("email");

        ClientDTO clientDTO = new ClientDTO();
        clientDTO.setId(1L);
        clientDTO.setName("name");
        clientDTO.setSurname("surname");
        clientDTO.setEmail("email");
        clientDTO.setAudit(new AuditDTO());

        Mockito.when(mockModelMapper.map(Mockito.any(ClientDTO.class), Mockito.eq(Client.class))).thenReturn(client);
        Mockito.when(mockManager.save(client)).thenReturn(client);
        Mockito.when(mockModelMapper.map(client, ClientDTO.class)).thenReturn(clientDTO);

        // Act
        ResultActions perform = mockMvc.perform(put("/client/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(clientDTO)));

        // Assert
        perform.andExpect(status().isAccepted());
        perform.andExpect(content().json(mapper.writeValueAsString(clientDTO)));
    }

    @Test
    public void testDelete() throws Exception {
        // Act
        ResultActions perform = mockMvc.perform(delete("/client/1"));

        // Assert
        perform.andExpect(status().isOk());
        Mockito.verify(mockManager, Mockito.times(1)).delete(Mockito.any());

    }


}
