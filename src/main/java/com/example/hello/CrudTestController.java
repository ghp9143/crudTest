package com.example.hello;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class CrudTestController {
    private final CrudTestService crudTestService;

    @Autowired
    private CrudTestRepository crudTestRepository;

    public CrudTestController(CrudTestService crudTestService) {
        this.crudTestService = crudTestService;
    }

    @GetMapping("/crudTest")
    public List<CrudTestDto> findAll() {
        return crudTestService.findAll();
    }

    @PostMapping("/crudTest")
    public CrudTestDto saveAndReturnOne(@RequestBody CrudTestDto dto) {
        return crudTestService.saveAndReturn(dto);
    }

    @DeleteMapping("/crudTest/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        crudTestRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }

}

