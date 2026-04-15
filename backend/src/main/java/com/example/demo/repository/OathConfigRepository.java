package com.example.demo.repository;

import com.example.demo.model.OathConfig;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * 誓约配置Repository
 */
@Repository
public interface OathConfigRepository extends JpaRepository<OathConfig, Long> {

    /**
     * 根据誓约模式查找
     */
    Optional<OathConfig> findByOathMode(String oathMode);
}
