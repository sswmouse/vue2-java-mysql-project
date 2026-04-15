package com.example.demo.repository;

import com.example.demo.model.FogSystem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface FogSystemRepository extends JpaRepository<FogSystem, Long> {

    /**
     * 查询用户在指定跨区的迷雾数据
     */
    Optional<FogSystem> findByUserIdAndRegion(Long userId, String region);

    /**
     * 查询用户在所有跨区的迷雾数据
     */
    List<FogSystem> findByUserId(Long userId);
}
