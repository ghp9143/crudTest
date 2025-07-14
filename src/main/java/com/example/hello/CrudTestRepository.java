package com.example.hello;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CrudTestRepository extends JpaRepository<CrudTest, Long> {
    // 제목(typeData)·내용(textData) 모두 조회
    List<CrudTest> findByTypeDataContainingIgnoreCaseOrTextDataContainingIgnoreCaseOrderByIdDesc(String t, String c);

    // 제목만
    List<CrudTest> findByTypeDataContainingIgnoreCaseOrderByIdDesc(String t);

    // 내용만
    List<CrudTest> findByTextDataContainingIgnoreCaseOrderByIdDesc(String c);
}
