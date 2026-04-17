package com.example.demo.service;

import com.example.demo.dto.StageDTO;
import com.example.demo.model.StageConfig;

import java.util.List;
import java.util.Optional;

/**
 * 阶段配置服务接口
 * 提供阶段配置相关的业务操作方法
 *
 * @author Claude Code
 * @version 1.0
 * @since 2026-04-17
 */
public interface StageConfigService {

    /**
     * 获取所有阶段配置（按顺序排序）
     */
    List<StageConfig> getAllStages();

    /**
     * 根据ID获取阶段配置
     */
    StageConfig getStageById(Long id);

    /**
     * 根据阶段名称获取阶段配置
     */
    Optional<StageConfig> getStageByName(String stage);

    /**
     * 根据阶段顺序获取阶段配置
     */
    Optional<StageConfig> getStageByOrder(Integer stageOrder);

    /**
     * 根据积分获取当前阶段
     * @param points 当前积分
     * @return 当前阶段（积分不足返回null）
     */
    StageConfig getCurrentStage(Integer points);

    /**
     * 根据积分获取下一阶段
     * @param points 当前积分
     * @return 下一阶段（已达最高阶段返回null）
     */
    Optional<StageConfig> getNextStage(Integer points);

    /**
     * 获取阶段进度
     * @param points 当前积分
     * @return 进度百分比
     */
    Double getProgress(Integer points);

    /**
     * 获取阶段到下一阶段的进度
     * @param points 当前积分
     * @return 当前阶段内进度百分比
     */
    Double getStageProgress(Integer points);

    /**
     * 获取达到指定阶段还需要多少积分
     * @param points 当前积分
     * @param targetStage 目标阶段
     * @return 还需积分（负数表示已超过）
     */
    Integer getPointsToNextStage(Integer points, String targetStage);

    /**
     * 获取指定品级的所有阶段
     * @param rarity 品级名称
     * @return 该品级的所有阶段
     */
    List<StageConfig> getStagesByRarity(String rarity);

    /**
     * 获取所有大阶段（跨品级阶段）
     */
    List<StageConfig> getMajorStages();

    /**
     * 判断是否为最后阶段
     * @param stageName 阶段名称
     * @return 是否为最后阶段
     */
    boolean isLastStage(String stageName);

    /**
     * 获取已激活的阶段列表
     * @param points 当前积分
     * @return 已激活的阶段列表
     */
    List<StageConfig> getActivatedStages(Integer points);

    /**
     * 初始化阶段配置数据
     */
    void initStageConfigs();

    /**
     * 获取所有阶段DTO列表
     * @return 阶段DTO列表
     */
    List<StageDTO> getAllStageDTOs();
}
