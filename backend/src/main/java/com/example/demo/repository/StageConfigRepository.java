package com.example.demo.repository;

import com.example.demo.model.StageConfig;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * 阶段配置Repository
 * 提供阶段配置数据的数据库操作方法
 *
 * @author Claude Code
 * @version 1.0
 * @since 2026-04-17
 */
@Repository
public interface StageConfigRepository extends JpaRepository<StageConfig, Long> {

    /**
     * 根据阶段名称查找
     */
    Optional<StageConfig> findByStage(String stage);

    /**
     * 根据阶段顺序查找
     */
    Optional<StageConfig> findByStageOrder(Integer stageOrder);

    /**
     * 获取所有阶段（按顺序排序）
     */
    List<StageConfig> findAllByOrderByStageOrderAsc();

    /**
     * 获取所有大阶段（跨品级阶段）
     */
    List<StageConfig> findByStageTypeOrderByStageOrderAsc(String stageType);

    /**
     * 根据触发积分阈值查找最近达成的阶段
     * 使用原生查询以支持LIMIT语法
     */
    @Query(value = "SELECT * FROM stage_configs WHERE threshold <= :points ORDER BY stage_order DESC LIMIT 1", nativeQuery = true)
    Optional<StageConfig> findCurrentStageByPoints(Integer points);

    /**
     * 根据积分获取下一个阶段
     * 使用原生查询以支持LIMIT语法
     */
    @Query(value = "SELECT * FROM stage_configs WHERE threshold > :points ORDER BY stage_order ASC LIMIT 1", nativeQuery = true)
    Optional<StageConfig> findNextStageByPoints(Integer points);

    /**
     * 获取所有阶段数量
     */
    long countByStageType(String stageType);

    /**
     * 判断阶段是否存在
     */
    boolean existsByStage(String stage);

    /**
     * 根据品级获取所有阶段
     * @param rarity 品级名称（稀有/神器/传说/史诗/太初）
     */
    @Query("SELECT s FROM StageConfig s WHERE s.stage LIKE :rarity || '%' ORDER BY s.stageOrder")
    List<StageConfig> findByRarity(String rarity);
}
