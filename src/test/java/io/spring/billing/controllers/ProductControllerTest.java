package io.spring.billing.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.spring.billing.controllers.dtos.AuditDTO;
import io.spring.billing.controllers.dtos.ProductDTO;
import io.spring.billing.entities.Audit;
import io.spring.billing.entities.Product;
import io.spring.billing.manager.ProductManager;
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
public class ProductControllerTest {

    @InjectMocks
    private ProductControllerImpl controller;

    @Mock
    private ProductManager mockManager;

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
        ResultActions perform = mockMvc.perform(get("/product"));

        // Assert
        perform.andExpect(status().isOk());
    }

    @Test
    public void testGetAllWithContent() throws Exception {
        // Arrange
        Product product_1 = new Product();
        product_1.setId(1L);
        product_1.setAudit(new Audit());

        Product product_2 = new Product();
        product_2.setId(2L);
        product_2.setAudit(new Audit());

        List<Product> products = new ArrayList<>();
        products.add(product_1);
        products.add(product_2);

        ProductDTO productDTO_1 = new ProductDTO();
        productDTO_1.setId(1L);
        productDTO_1.setAudit(new AuditDTO());

        ProductDTO productDTO_2 = new ProductDTO();
        productDTO_2.setId(2L);
        productDTO_2.setAudit(new AuditDTO());

        List<ProductDTO> productDTOs = new ArrayList<>();
        productDTOs.add(productDTO_1);
        productDTOs.add(productDTO_2);

        Mockito.when(mockManager.findAll()).thenReturn(products);
        Mockito.when(mockModelMapper.map(product_1, ProductDTO.class)).thenReturn(productDTO_1);
        Mockito.when(mockModelMapper.map(product_2, ProductDTO.class)).thenReturn(productDTO_2);

        // Act
        ResultActions perform = mockMvc.perform(get("/product"));

        // Assert
        perform.andExpect(status().isOk());
        perform.andExpect(content().json(mapper.writeValueAsString(productDTOs)));
    }

    @Test
    public void testFindById() throws Exception {
        // Arrange
        Product product = new Product();
        product.setId(1L);
        product.setAudit(new Audit());

        ProductDTO productDTO = new ProductDTO();
        productDTO.setId(1L);
        productDTO.setAudit(new AuditDTO());

        Mockito.when(mockManager.get(1L)).thenReturn(product);
        Mockito.when(mockModelMapper.map(product, ProductDTO.class)).thenReturn(productDTO);

        // Act
        ResultActions perform = mockMvc.perform(get("/product/1"));

        // Assert
        perform.andExpect(status().isOk());
        perform.andExpect(content().json(mapper.writeValueAsString(productDTO)));
    }

    @Test
    public void testCreate() throws Exception {
        // Arrange
        Product product = new Product();
        product.setName("name");
        product.setPrice(10.0);

        ProductDTO productDTO = new ProductDTO();
        productDTO.setId(1L);
        productDTO.setName("name");
        productDTO.setPrice(10.0);
        productDTO.setAudit(new AuditDTO());

        Mockito.when(mockModelMapper.map(Mockito.any(ProductDTO.class), Mockito.eq(Product.class))).thenReturn(product);
        Mockito.when(mockManager.save(product)).thenReturn(product);
        Mockito.when(mockModelMapper.map(product, ProductDTO.class)).thenReturn(productDTO);

        // Act
        ResultActions perform = mockMvc.perform(post("/product")
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(productDTO)));

        // Assert
        perform.andExpect(status().isAccepted());
        perform.andExpect(content().json(mapper.writeValueAsString(productDTO)));
    }

    @Test
    public void testUpdate() throws Exception {
        // Arrange
        Product product = new Product();
        product.setName("name");
        product.setPrice(10.0);

        ProductDTO productDTO = new ProductDTO();
        productDTO.setId(1L);
        productDTO.setName("name");
        productDTO.setPrice(10.0);
        productDTO.setAudit(new AuditDTO());

        Mockito.when(mockModelMapper.map(Mockito.any(ProductDTO.class), Mockito.eq(Product.class))).thenReturn(product);
        Mockito.when(mockManager.save(product)).thenReturn(product);
        Mockito.when(mockModelMapper.map(product, ProductDTO.class)).thenReturn(productDTO);

        // Act
        ResultActions perform = mockMvc.perform(put("/product/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(productDTO)));

        // Assert
        perform.andExpect(status().isAccepted());
        perform.andExpect(content().json(mapper.writeValueAsString(productDTO)));
    }

    @Test
    public void testDelete() throws Exception {
        // Act
        ResultActions perform = mockMvc.perform(delete("/product/1"));

        // Assert
        perform.andExpect(status().isOk());
        Mockito.verify(mockManager, Mockito.times(1)).delete(Mockito.any());

    }


}
