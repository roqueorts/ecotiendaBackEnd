package io.spring.billing.controllers;

import io.spring.billing.controllers.dtos.BillDTO;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequestMapping("/bill")
public interface BillController {

    @GetMapping()
    ResponseEntity<List<BillDTO>> findAll();

    @GetMapping("/{id}")
    ResponseEntity<BillDTO> findById(final @PathVariable("id") Long id);

    @PostMapping()
    ResponseEntity<BillDTO> create(final @RequestBody BillDTO dto);

    @PutMapping("/{id}")
    ResponseEntity<BillDTO> update(final @PathVariable("id") Long id, final @RequestBody BillDTO dto);

    @DeleteMapping("/{id}")
    void remove(final @PathVariable("id") Long id);

}
