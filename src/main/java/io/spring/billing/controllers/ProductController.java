package io.spring.billing.controllers;

import io.spring.billing.controllers.dtos.ProductDTO;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequestMapping("/product")
@CrossOrigin(origins = "*")
public interface ProductController {

    @GetMapping()
    ResponseEntity<List<ProductDTO>> findAll();

    @GetMapping("/{id}")
    ResponseEntity<ProductDTO> findById(final @PathVariable("id") Long id);

    @PostMapping()
    ResponseEntity<ProductDTO> create(final @RequestBody ProductDTO dto);

    @PutMapping("/{id}")
    ResponseEntity<ProductDTO> update(final @PathVariable("id") Long id, final @RequestBody ProductDTO dto);

    @DeleteMapping("/{id}")
    void remove(final @PathVariable("id") Long id);

}
