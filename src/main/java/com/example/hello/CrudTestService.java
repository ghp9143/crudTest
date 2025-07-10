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
            dto.setId(entity.getId());
            dto.setTypeData(entity.getTypeData());
            dto.setTextData(entity.getTextData());
            return dto;
        }).collect(Collectors.toList());
    }

    public CrudTestDto saveAndReturn(CrudTestDto dto) {
        CrudTest entity = new CrudTest();
        entity.setTypeData(dto.getTypeData());
        entity.setTextData(dto.getTextData());

        CrudTest saved = repository.save(entity); // 저장하고 반환

        CrudTestDto result = new CrudTestDto();
        result.setId(saved.getId()); // ← 저장된 ID
        result.setTypeData(saved.getTypeData());
        result.setTextData(saved.getTextData());

        return result;
    }
}
