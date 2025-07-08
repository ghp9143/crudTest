package com.example.hello;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CrudTestRepository extends JpaRepository<CrudTest, Long> {
    
}
