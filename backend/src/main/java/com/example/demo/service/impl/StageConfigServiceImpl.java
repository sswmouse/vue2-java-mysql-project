package com.example.demo.service.impl;

import com.example.demo.model.StageConfig;
import com.example.demo.repository.StageConfigRepository;
import com.example.demo.service.StageConfigService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

/**
 * 阶段配置服务实现类
 * 提供阶段配置相关的业务操作实现
 *
 * @author Claude Code
 * @version 1.0
 * @since 2026-04-17
 */
@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class StageConfigServiceImpl implements StageConfigService {

    private final StageConfigRepository stageConfigRepository;

    /**
     * 太初阶段积分上限（满分）
     */
    private static final int MAX_POINTS = 2550;

    @Override
    public List<StageConfig> getAllStages() {
        return stageConfigRepository.findAllByOrderByStageOrderAsc();
    }

    @Override
    public StageConfig getStageById(Long id) {
        return stageConfigRepository.findById(id).orElse(null);
    }

    @Override
    public Optional<StageConfig> getStageByName(String stage) {
        return stageConfigRepository.findByStage(stage);
    }

    @Override
    public Optional<StageConfig> getStageByOrder(Integer stageOrder) {
        return stageConfigRepository.findByStageOrder(stageOrder);
    }

    @Override
    public StageConfig getCurrentStage(Integer points) {
        if (points == null || points < 750) {
            return null;
        }
        return stageConfigRepository.findCurrentStageByPoints(points).orElse(null);
    }

    @Override
    public Optional<StageConfig> getNextStage(Integer points) {
        if (points == null) {
            return Optional.empty();
        }
        return stageConfigRepository.findNextStageByPoints(points);
    }

    @Override
    public Double getProgress(Integer points) {
        if (points == null || points <= 0) {
            return 0.0;
        }
        return Math.min(100.0, (points * 100.0) / MAX_POINTS);
    }

    @Override
    public Double getStageProgress(Integer points) {
        if (points == null || points < 750) {
            return 0.0;
        }

        StageConfig currentStage = getCurrentStage(points);
        if (currentStage == null) {
            return 0.0;
        }

        StageConfig current = currentStage;

        // 如果是太初阶段（最后阶段），返回100%
        if (current.isLastStage()) {
            return 100.0;
        }

        // 获取下一阶段
        Optional<StageConfig> nextStage = getNextStage(points);
        if (nextStage.isEmpty()) {
            return 100.0;
        }

        StageConfig next = nextStage.get();

        // 计算当前阶段内进度
        int currentThreshold = current.getThreshold();
        int nextThreshold = next.getThreshold();
        int thresholdRange = nextThreshold - currentThreshold;

        if (thresholdRange <= 0) {
            return 100.0;
        }

        int pointsInStage = points - currentThreshold;
        return Math.min(100.0, (pointsInStage * 100.0) / thresholdRange);
    }

    @Override
    public Integer getPointsToNextStage(Integer points, String targetStage) {
        if (points == null || targetStage == null) {
            return null;
        }

        Optional<StageConfig> target = getStageByName(targetStage);
        if (target.isEmpty()) {
            return null;
        }

        int targetThreshold = target.get().getThreshold();
        return targetThreshold - points;
    }

    @Override
    public List<StageConfig> getStagesByRarity(String rarity) {
        if (rarity == null) {
            return getAllStages();
        }
        return stageConfigRepository.findByRarity(rarity);
    }

    @Override
    public List<StageConfig> getMajorStages() {
        return stageConfigRepository.findByStageTypeOrderByStageOrderAsc(StageConfig.STAGE_TYPE_MAJOR);
    }

    @Override
    public boolean isLastStage(String stageName) {
        return StageConfig.STAGE_PRIMAL.equals(stageName);
    }
}
