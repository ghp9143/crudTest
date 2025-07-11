package com.example.hello;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
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

        try {
            crudTestRepository.deleteById(id);
            return ResponseEntity.noContent().build();
        } catch (EmptyResultDataAccessException e) {
            return ResponseEntity.notFound().build();  
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/crudTest/search")
    public List<CrudTestDto> search(
        @RequestParam String field,
        @RequestParam String keyword) {
            return crudTestService.search(field, keyword);
        }

}

