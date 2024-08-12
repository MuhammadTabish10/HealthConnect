package com.healthconnect.baseservice.controller;

import com.healthconnect.baseservice.service.GenericService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

public abstract class GenericController<U> {

    private static final String ID = "/{id}";
    private static final String IS_ACTIVE = "isActive";

    private final GenericService<U> genericService;

    protected GenericController(GenericService<U> genericService) {
        this.genericService = genericService;
    }

    @PostMapping
    public ResponseEntity<U> create(@RequestBody U dto) {
        U createdDto = genericService.save(dto);
        return new ResponseEntity<>(createdDto, HttpStatus.CREATED);
    }

    @GetMapping(ID)
    public ResponseEntity<U> getById(@PathVariable Long id) {
        U dto = genericService.getById(id);
        return new ResponseEntity<>(dto, HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<List<U>> getAll(@RequestParam(value = IS_ACTIVE) Boolean isActive) {
        List<U> dtos = genericService.getAll(isActive);
        return new ResponseEntity<>(dtos, HttpStatus.OK);
    }

    @PutMapping(ID)
    public ResponseEntity<U> update(@PathVariable Long id, @RequestBody U dto) {
        U updatedDto = genericService.update(dto, id);
        return new ResponseEntity<>(updatedDto, HttpStatus.OK);
    }

    @DeleteMapping(ID)
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        genericService.delete(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
