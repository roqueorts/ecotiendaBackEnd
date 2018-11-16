package io.spring.billing.controllers;

import io.spring.billing.controllers.dtos.BillingDTO;
import io.spring.billing.entities.BillingEntity;
import io.spring.billing.manager.AbstractManager;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

import java.io.Serializable;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public abstract class AbstractController <E extends BillingEntity, DTO extends BillingDTO, ID extends Serializable> {

    @Autowired
    private ModelMapper modelMapper;

    private Class<E> eClass;
    private Class<DTO> dtoClass;

    protected abstract AbstractManager<E> getManager();

    protected DTO convertToDTO(final E e) {
        if (e == null) return null;
        return modelMapper.map(e, dtoClass);
    }

    protected E convertToEntity(final DTO dto) {
        if (dto == null) return null;
        return modelMapper.map(dto,eClass);
    }

    protected AbstractController(final Class<E> eClass, final Class<DTO> dtoClass) {
        this.eClass = eClass;
        this.dtoClass = dtoClass;
    }

    public ResponseEntity<DTO> findById(final @PathVariable("id") ID id) {
        return new ResponseEntity<>(convertToDTO(getManager().get((Long) id)), HttpStatus.OK);
    }

    public ResponseEntity<DTO> create(final @RequestBody DTO dto) {
        return new ResponseEntity<>(convertToDTO(getManager().save(convertToEntity(dto))), HttpStatus.ACCEPTED);
    }

    public ResponseEntity<DTO> update(final @PathVariable("id") ID id, final @RequestBody DTO dto) {
        final E e = convertToEntity(dto);
        return new ResponseEntity<>(convertToDTO(getManager().save(e)), HttpStatus.ACCEPTED);
    }

    public void remove(final @PathVariable("id") ID id) {
        getManager().delete(getManager().get((Long) id));
    }

    protected List<DTO> convertToDTOList(final List<E> list) {
        return Optional.ofNullable(list)
                .orElse(Collections.emptyList())
                .stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

}
