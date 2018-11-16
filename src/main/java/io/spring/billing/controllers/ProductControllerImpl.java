package io.spring.billing.controllers;

import io.spring.billing.controllers.dtos.ProductDTO;
import io.spring.billing.entities.Product;
import io.spring.billing.manager.AbstractManager;
import io.spring.billing.manager.ProductManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class ProductControllerImpl extends AbstractController<Product, ProductDTO, Long> implements ProductController {

    @Autowired
    private ProductManager manager;

    public ProductControllerImpl() {
        super(Product.class, ProductDTO.class);
    }

    @Override
    protected ProductManager getManager() {
        return this.manager;
    }

    // @WTF
    public ResponseEntity<List<ProductDTO>> findAll() {
        return new ResponseEntity<>(convertToDTOList(getManager().findAll()), HttpStatus.OK);
    }

}
