package io.spring.billing.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.spring.billing.controllers.dtos.AuditDTO;
import io.spring.billing.controllers.dtos.BillDTO;
import io.spring.billing.entities.Audit;
import io.spring.billing.entities.Bill;
import io.spring.billing.manager.BillManager;
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
public class BillControllerTest {

    @InjectMocks
    private BillControllerImpl controller;

    @Mock
    private BillManager mockManager;

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
        ResultActions perform = mockMvc.perform(get("/bill"));

        // Assert
        perform.andExpect(status().isOk());
    }

    @Test
    public void testGetAllWithContent() throws Exception {
        // Arrange
        Bill bill_1 = new Bill();
        bill_1.setId(1L);
        bill_1.setAudit(new Audit());

        Bill bill_2 = new Bill();
        bill_2.setId(2L);
        bill_2.setAudit(new Audit());

        List<Bill> bills = new ArrayList<>();
        bills.add(bill_1);
        bills.add(bill_2);

        BillDTO billDTO_1 = new BillDTO();
        billDTO_1.setId(1L);
        billDTO_1.setAudit(new AuditDTO());

        BillDTO billDTO_2 = new BillDTO();
        billDTO_2.setId(2L);
        billDTO_2.setAudit(new AuditDTO());

        List<BillDTO> billDTOs = new ArrayList<>();
        billDTOs.add(billDTO_1);
        billDTOs.add(billDTO_2);

        Mockito.when(mockManager.findAll()).thenReturn(bills);
        Mockito.when(mockModelMapper.map(bill_1, BillDTO.class)).thenReturn(billDTO_1);
        Mockito.when(mockModelMapper.map(bill_2, BillDTO.class)).thenReturn(billDTO_2);

        // Act
        ResultActions perform = mockMvc.perform(get("/bill"));

        // Assert
        perform.andExpect(status().isOk());
        perform.andExpect(content().json(mapper.writeValueAsString(billDTOs)));
    }

    @Test
    public void testFindById() throws Exception {
        // Arrange
        Bill bill = new Bill();
        bill.setId(1L);
        bill.setAudit(new Audit());

        BillDTO billDTO = new BillDTO();
        billDTO.setId(1L);
        billDTO.setAudit(new AuditDTO());

        Mockito.when(mockManager.get(1L)).thenReturn(bill);
        Mockito.when(mockModelMapper.map(bill, BillDTO.class)).thenReturn(billDTO);

        // Act
        ResultActions perform = mockMvc.perform(get("/bill/1"));

        // Assert
        perform.andExpect(status().isOk());
        perform.andExpect(content().json(mapper.writeValueAsString(billDTO)));
    }

    @Test
    public void testCreate() throws Exception {
        // Arrange
        Bill bill = new Bill();
        bill.setDescription("description");
        bill.setObservation("observation");

        BillDTO billDTO = new BillDTO();
        billDTO.setId(1L);
        billDTO.setDescription("description");
        billDTO.setObservation("observation");
        billDTO.setAudit(new AuditDTO());

        Mockito.when(mockModelMapper.map(Mockito.any(BillDTO.class), Mockito.eq(Bill.class))).thenReturn(bill);
        Mockito.when(mockManager.save(bill)).thenReturn(bill);
        Mockito.when(mockModelMapper.map(bill, BillDTO.class)).thenReturn(billDTO);

        // Act
        ResultActions perform = mockMvc.perform(post("/bill")
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(billDTO)));

        // Assert
        perform.andExpect(status().isAccepted());
        perform.andExpect(content().json(mapper.writeValueAsString(billDTO)));
    }

    @Test
    public void testUpdate() throws Exception {
        // Arrange
        Bill bill = new Bill();
        bill.setDescription("description");
        bill.setObservation("observation");

        BillDTO billDTO = new BillDTO();
        billDTO.setId(1L);
        billDTO.setDescription("description");
        billDTO.setObservation("observation");
        billDTO.setAudit(new AuditDTO());

        Mockito.when(mockModelMapper.map(Mockito.any(BillDTO.class), Mockito.eq(Bill.class))).thenReturn(bill);
        Mockito.when(mockManager.save(bill)).thenReturn(bill);
        Mockito.when(mockModelMapper.map(bill, BillDTO.class)).thenReturn(billDTO);

        // Act
        ResultActions perform = mockMvc.perform(put("/bill/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(billDTO)));

        // Assert
        perform.andExpect(status().isAccepted());
        perform.andExpect(content().json(mapper.writeValueAsString(billDTO)));
    }

    @Test
    public void testDelete() throws Exception {
        // Act
        ResultActions perform = mockMvc.perform(delete("/bill/1"));

        // Assert
        perform.andExpect(status().isOk());
        Mockito.verify(mockManager, Mockito.times(1)).delete(Mockito.any());

    }


}
