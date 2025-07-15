package com.example.hello;

import java.util.List;
import java.util.Optional;
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

    public List<CrudTestDto> search(String field, String keyword) {
        List<CrudTest> list;

        switch (field) {
            case "title":
                list = repository.findByTypeDataContainingIgnoreCaseOrderByIdDesc(keyword);
                break;
            case "content":
                list = repository.findByTextDataContainingIgnoreCaseOrderByIdDesc(keyword);
                break;
            default: // 'all'
                list = repository
                .findByTypeDataContainingIgnoreCaseOrTextDataContainingIgnoreCaseOrderByIdDesc(keyword, keyword);
        }
        
        return list.stream().map(this::toDto).toList();

    }

    /* ⭐ entity → dto 변환 전용 메서드 */
    private CrudTestDto toDto(CrudTest entity) {
        CrudTestDto dto = new CrudTestDto();
        dto.setId(entity.getId());
        dto.setTypeData(entity.getTypeData());
        dto.setTextData(entity.getTextData());
        dto.setCreatedAt(entity.getCreatedAt());
        return dto;
    }

    public Optional<CrudTestDto> findOne(Long id) {
        // return repository.findById(id)      // Optional<CrudTest>
        //                  .map(this::toDto); // Optional<CrudTestDto>

        System.out.println("findOne 요청됨 id = " + id);
        Optional<CrudTest> entityOpt = repository.findById(id);
        if (entityOpt.isEmpty()) {
            System.out.println("해당 ID의 데이터 없음!");
        }
        return entityOpt.map(this::toDto);
    }

    public Optional<CrudTestDto> update(Long id, CrudTestDto dto) {
        return repository.findById(id).map(entity -> {
            entity.setTypeData(dto.getTypeData());
            entity.setTextData(dto.getTextData());
            CrudTest saved = repository.save(entity);
            return toDto(saved);
        });
    }
}
