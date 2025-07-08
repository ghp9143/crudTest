package com.example.hello;

import java.util.List;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class CrudTestController {
    private final CrudTestService crudTestService;

    public CrudTestController(CrudTestService crudTestService) {
        this.crudTestService = crudTestService;
    }

    @PostMapping("/crudTest")
    public List<CrudTestDto> saveAndReturnList(@RequestBody CrudTestDto dto) {
        crudTestService.save(dto);
        return crudTestService.findAll();
    }

}
 