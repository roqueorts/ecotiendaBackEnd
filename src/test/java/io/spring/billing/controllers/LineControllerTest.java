package io.spring.billing.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.spring.billing.controllers.dtos.AuditDTO;
import io.spring.billing.controllers.dtos.LineDTO;
import io.spring.billing.entities.Audit;
import io.spring.billing.entities.Line;
import io.spring.billing.manager.LineManager;
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
public class LineControllerTest {

    @InjectMocks
    private LineControllerImpl controller;

    @Mock
    private LineManager mockManager;

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
        ResultActions perform = mockMvc.perform(get("/line"));

        // Assert
        perform.andExpect(status().isOk());
    }

    @Test
    public void testGetAllWithContent() throws Exception {
        // Arrange
        Line line_1 = new Line();
        line_1.setId(1L);
        line_1.setAudit(new Audit());

        Line line_2 = new Line();
        line_2.setId(2L);
        line_2.setAudit(new Audit());

        List<Line> lines = new ArrayList<>();
        lines.add(line_1);
        lines.add(line_2);

        LineDTO lineDTO_1 = new LineDTO();
        lineDTO_1.setId(1L);
        lineDTO_1.setAudit(new AuditDTO());

        LineDTO lineDTO_2 = new LineDTO();
        lineDTO_2.setId(2L);
        lineDTO_2.setAudit(new AuditDTO());

        List<LineDTO> lineDTOs = new ArrayList<>();
        lineDTOs.add(lineDTO_1);
        lineDTOs.add(lineDTO_2);

        Mockito.when(mockManager.findAll()).thenReturn(lines);
        Mockito.when(mockModelMapper.map(line_1, LineDTO.class)).thenReturn(lineDTO_1);
        Mockito.when(mockModelMapper.map(line_2, LineDTO.class)).thenReturn(lineDTO_2);

        // Act
        ResultActions perform = mockMvc.perform(get("/line"));

        // Assert
        perform.andExpect(status().isOk());
        perform.andExpect(content().json(mapper.writeValueAsString(lineDTOs)));
    }

    @Test
    public void testFindById() throws Exception {
        // Arrange
        Line line = new Line();
        line.setId(1L);
        line.setAudit(new Audit());

        LineDTO lineDTO = new LineDTO();
        lineDTO.setId(1L);
        lineDTO.setAudit(new AuditDTO());

        Mockito.when(mockManager.get(1L)).thenReturn(line);
        Mockito.when(mockModelMapper.map(line, LineDTO.class)).thenReturn(lineDTO);

        // Act
        ResultActions perform = mockMvc.perform(get("/line/1"));

        // Assert
        perform.andExpect(status().isOk());
        perform.andExpect(content().json(mapper.writeValueAsString(lineDTO)));
    }

    @Test
    public void testCreate() throws Exception {
        // Arrange
        Line line = new Line();
        line.setQuantity(1);

        LineDTO lineDTO = new LineDTO();
        lineDTO.setId(1L);
        lineDTO.setQuantity(1);
        lineDTO.setAudit(new AuditDTO());

        Mockito.when(mockModelMapper.map(Mockito.any(LineDTO.class), Mockito.eq(Line.class))).thenReturn(line);
        Mockito.when(mockManager.save(line)).thenReturn(line);
        Mockito.when(mockModelMapper.map(line, LineDTO.class)).thenReturn(lineDTO);

        // Act
        ResultActions perform = mockMvc.perform(post("/line")
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(lineDTO)));

        // Assert
        perform.andExpect(status().isAccepted());
        perform.andExpect(content().json(mapper.writeValueAsString(lineDTO)));
    }

    @Test
    public void testUpdate() throws Exception {
        // Arrange
        Line line = new Line();
        line.setQuantity(1);

        LineDTO lineDTO = new LineDTO();
        lineDTO.setId(1L);
        lineDTO.setQuantity(1);
        lineDTO.setAudit(new AuditDTO());

        Mockito.when(mockModelMapper.map(Mockito.any(LineDTO.class), Mockito.eq(Line.class))).thenReturn(line);
        Mockito.when(mockManager.save(line)).thenReturn(line);
        Mockito.when(mockModelMapper.map(line, LineDTO.class)).thenReturn(lineDTO);

        // Act
        ResultActions perform = mockMvc.perform(put("/line/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(lineDTO)));

        // Assert
        perform.andExpect(status().isAccepted());
        perform.andExpect(content().json(mapper.writeValueAsString(lineDTO)));
    }

}
