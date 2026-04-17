package com.example.demo.service.impl;

import com.example.demo.dto.StageDTO;
import com.example.demo.model.StageConfig;
import com.example.demo.repository.StageConfigRepository;
import com.example.demo.service.StageConfigService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

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

    /**
     * 默认阶段配置数据
     */
    private static final List<StageConfig> DEFAULT_STAGES = Arrays.asList(
            createStage("稀有1", 1, 750, 0, 0, "minor"),
            createStage("稀有2", 2, 835, 1000, 1000, "minor"),
            createStage("稀有3", 3, 920, 2000, 2000, "minor"),
            createStage("稀有4", 4, 1005, 3000, 3000, "minor"),
            createStage("稀有5", 5, 1090, 4000, 4000, "major"),
            createStage("神器1", 6, 1200, 5500, 5500, "minor"),
            createStage("神器2", 7, 1285, 6500, 6500, "minor"),
            createStage("神器3", 8, 1370, 7500, 7500, "minor"),
            createStage("神器4", 9, 1455, 8500, 8500, "minor"),
            createStage("神器5", 10, 1540, 9500, 9500, "major"),
            createStage("传说1", 11, 1650, 11000, 11000, "minor"),
            createStage("传说2", 12, 1735, 12000, 12000, "minor"),
            createStage("传说3", 13, 1820, 13000, 13000, "minor"),
            createStage("传说4", 14, 1905, 14000, 14000, "minor"),
            createStage("传说5", 15, 1990, 15000, 15000, "major"),
            createStage("史诗1", 16, 2100, 16500, 16500, "minor"),
            createStage("史诗2", 17, 2185, 17500, 17500, "minor"),
            createStage("史诗3", 18, 2270, 18500, 18500, "minor"),
            createStage("史诗4", 19, 2355, 19500, 19500, "minor"),
            createStage("史诗5", 20, 2440, 20500, 20500, "major"),
            createStage("太初", 21, 2550, 22000, 22000, "major")
    );

    private static StageConfig createStage(String stage, int order, int threshold, int fameBonus, int cumulativeFame, String stageType) {
        return StageConfig.builder()
                .stage(stage)
                .stageOrder(order)
                .threshold(threshold)
                .fameBonus(fameBonus)
                .cumulativeFame(cumulativeFame)
                .stageType(stageType)
                .build();
    }

    @Override
    public List<StageConfig> getAllStages() {
        List<StageConfig> stages = stageConfigRepository.findAllByOrderByStageOrderAsc();
        if (stages.isEmpty()) {
            initStageConfigs();
            return stageConfigRepository.findAllByOrderByStageOrderAsc();
        }
        return stages;
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

    @Override
    public List<StageConfig> getActivatedStages(Integer points) {
        if (points == null || points < 750) {
            return Arrays.asList();
        }

        StageConfig currentStage = getCurrentStage(points);
        if (currentStage == null) {
            return Arrays.asList();
        }

        return stageConfigRepository.findAllByOrderByStageOrderAsc().stream()
                .filter(stage -> stage.getStageOrder() <= currentStage.getStageOrder())
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public void initStageConfigs() {
        for (StageConfig stage : DEFAULT_STAGES) {
            if (!stageConfigRepository.existsByStage(stage.getStage())) {
                stageConfigRepository.save(stage);
                log.info("阶段[{}]初始化完成", stage.getStage());
            }
        }
        log.info("阶段配置数据初始化完成，共{}个阶段", DEFAULT_STAGES.size());
    }

    @Override
    public List<StageDTO> getAllStageDTOs() {
        List<StageConfig> stages = getAllStages();
        return stages.stream().map(this::convertToDTO).collect(Collectors.toList());
    }

    /**
     * 将StageConfig转换为StageDTO
     */
    private StageDTO convertToDTO(StageConfig stage) {
        return StageDTO.builder()
                .id(stage.getId())
                .stage(stage.getStage())
                .stageOrder(stage.getStageOrder())
                .threshold(stage.getThreshold())
                .fameBonus(stage.getFameBonus())
                .cumulativeFame(stage.getCumulativeFame())
                .stageType(stage.getStageType())
                .rarity(stage.getRarity())
                .subStage(stage.getSubStage())
                .isLastStage(stage.isLastStage())
                .build();
    }
}
