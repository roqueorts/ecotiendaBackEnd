package io.spring.billing.controllers;

import io.spring.billing.controllers.dtos.ClientDTO;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequestMapping("/client")
public interface ClientController {

    @GetMapping()
    ResponseEntity<List<ClientDTO>> findAll();

    @GetMapping("/{id}")
    ResponseEntity<ClientDTO> findById(final @PathVariable("id") Long id);

    @PostMapping()
    ResponseEntity<ClientDTO> create(final @RequestBody ClientDTO dto);

    @PutMapping("/{id}")
    ResponseEntity<ClientDTO> update(final @PathVariable("id") Long id, final @RequestBody ClientDTO dto);

    @DeleteMapping("/{id}")
    void remove(final @PathVariable("id") Long id);

}
