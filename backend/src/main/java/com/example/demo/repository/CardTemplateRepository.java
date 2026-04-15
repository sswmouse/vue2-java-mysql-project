package com.example.demo.repository;

import com.example.demo.model.CardTemplate;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;

public interface CardTemplateRepository extends JpaRepository<CardTemplate, Long> {
    List<CardTemplate> findAll();
    Optional<CardTemplate> findByTemplateCode(String templateCode);
    Optional<CardTemplate> findByIsDefaultTrue();
}
