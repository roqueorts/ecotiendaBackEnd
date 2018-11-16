package io.spring.billing.controllers;

import io.spring.billing.controllers.dtos.LineDTO;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequestMapping("/line")
public interface LineController {

    @GetMapping()
    ResponseEntity<List<LineDTO>> findAll();

    @GetMapping("/{id}")
    ResponseEntity<LineDTO> findById(final @PathVariable("id") Long id);

    @PostMapping()
    ResponseEntity<LineDTO> create(final @RequestBody LineDTO dto);

    @PutMapping("/{id}")
    ResponseEntity<LineDTO> update(final @PathVariable("id") Long id, final @RequestBody LineDTO dto);

}
