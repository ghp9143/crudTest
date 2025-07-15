package com.example.hello;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest; 
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;
import org.springframework.web.bind.annotation.PutMapping;


@RestController
@RequestMapping("/crudTest")  
public class CrudTestController {
    private final CrudTestService crudTestService;

    @Autowired
    private CrudTestRepository crudTestRepository;

    public CrudTestController(CrudTestService crudTestService) {
        this.crudTestService = crudTestService;
    }

    @GetMapping("")
    public List<CrudTestDto> findAll() {
        return crudTestService.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<CrudTestDto> findOne(@PathVariable("id") Long id) {
        Optional<CrudTestDto> dto = crudTestService.findOne(id);
        return dto.map(ResponseEntity::ok)
                  .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping("/")
    public ResponseEntity<CrudTestDto> saveAndReturnOne(@RequestBody CrudTestDto dto) {
        CrudTestDto savedDto = crudTestService.saveAndReturn(dto);
        return ResponseEntity.ok(savedDto);
    }

    @DeleteMapping("/{id}")
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

    @GetMapping("/search")
    public List<CrudTestDto> search(
        @RequestParam String field,
        @RequestParam String keyword) {
            return crudTestService.search(field, keyword);
        }


    @PutMapping("/{id}")
    public ResponseEntity<CrudTestDto> update(@PathVariable("id") Long id, @RequestBody CrudTestDto dto) {
        Optional<CrudTestDto> updated = crudTestService.update(id, dto);
        return updated.map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }

    // 전체 목록 페이징
    @GetMapping("/paged")
        public ResponseEntity<?> getPagedList(@RequestParam("page") int page,
                                      @RequestParam("size") int size) {
        Page<CrudTestDto> pageResult = crudTestService.findAllPaged(PageRequest.of(page, size));

        // JSON으로 보낼 맵 만들기
        Map<String, Object> response = new HashMap<>();
        response.put("content", pageResult.getContent());         // 실제 데이터 리스트
        response.put("totalPages", pageResult.getTotalPages());   // 총 페이지 수
        response.put("totalElements", pageResult.getTotalElements()); // 총 데이터 수
        response.put("pageNumber", pageResult.getNumber());        // 현재 페이지 번호
        response.put("pageSize", pageResult.getSize());            // 페이지당 데이터 수

        return ResponseEntity.ok(response);  // 이걸 JSON으로 보내!
    }

    // 검색 + 페이징
    @GetMapping("/searchPaged")
    public Page<CrudTestDto> getSearchPaged(@RequestParam String field,
                                            @RequestParam String keyword,
                                            @RequestParam int page,
                                            @RequestParam int size) {
        return crudTestService.searchPaged(field, keyword,
                                           PageRequest.of(page, size));
    }
}

