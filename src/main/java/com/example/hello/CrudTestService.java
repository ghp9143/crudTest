package com.example.hello;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

@Service
public class CrudTestService {
    
    private final CrudTestRepository repository;

    public CrudTestService(CrudTestRepository repository) {
        this.repository = repository;
    }

    public void save(CrudTestDto dto) {
        CrudTest entity = new CrudTest();
        entity.setTypeData(dto.getTypeData());
        entity.setTextData(dto.getTextData());
        repository.save(entity);
    }

    public List<CrudTestDto> findAll() {
        return repository.findAll().stream().map(entity -> {
            CrudTestDto dto = new CrudTestDto();
            dto.setTypeData(entity.getTypeData());
            dto.setTextData(entity.getTextData());
            return dto;
        }).collect(Collectors.toList());
    }
}
