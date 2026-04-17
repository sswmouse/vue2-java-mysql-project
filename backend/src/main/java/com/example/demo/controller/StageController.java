package com.example.demo.controller;

import com.example.demo.dto.StageDTO;
import com.example.demo.model.StageConfig;
import com.example.demo.service.StageConfigService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 阶段配置Controller
 * 提供阶段配置相关的REST API接口
 *
 * @author Claude Code
 * @version 1.0
 * @since 2026-04-17
 */
@RestController
@RequestMapping("/api/stages")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
@Slf4j
public class StageController {

    private final StageConfigService stageConfigService;

    /**
     * 获取所有阶段配置列表
     * GET /api/stages
     *
     * @return 阶段配置列表
     */
    @GetMapping
    public ResponseEntity<?> getAllStages() {
        List<StageDTO> stages = stageConfigService.getAllStageDTOs();
        return ResponseEntity.ok(stages);
    }

    /**
     * 获取阶段详情
     * GET /api/stages/:id
     *
     * @param id 阶段ID
     * @return 阶段配置
     */
    @GetMapping("/{id}")
    public ResponseEntity<?> getStageById(@PathVariable Long id) {
        StageConfig stage = stageConfigService.getStageById(id);
        if (stage == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(stage);
    }

    /**
     * 根据阶段名称获取阶段配置
     * GET /api/stages/name/:stage
     *
     * @param stage 阶段名称
     * @return 阶段配置
     */
    @GetMapping("/name/{stage}")
    public ResponseEntity<?> getStageByName(@PathVariable String stage) {
        return stageConfigService.getStageByName(stage)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    /**
     * 获取指定品级的所有阶段
     * GET /api/stages/rarity/:rarity
     *
     * @param rarity 品级名称：稀有/神器/传说/史诗/太初
     * @return 该品级的所有阶段
     */
    @GetMapping("/rarity/{rarity}")
    public ResponseEntity<?> getStagesByRarity(@PathVariable String rarity) {
        List<StageConfig> stages = stageConfigService.getStagesByRarity(rarity);
        return ResponseEntity.ok(stages);
    }

    /**
     * 根据积分获取当前阶段
     * GET /api/stages/current
     *
     * @param points 当前积分
     * @return 当前阶段配置
     */
    @GetMapping("/current")
    public ResponseEntity<?> getCurrentStage(@RequestParam Integer points) {
        StageConfig stage = stageConfigService.getCurrentStage(points);
        if (stage == null) {
            return ResponseEntity.ok().body(null);
        }
        return ResponseEntity.ok(stage);
    }

    /**
     * 获取阶段进度信息
     * GET /api/stages/progress
     *
     * @param points 当前积分
     * @return 进度信息
     */
    @GetMapping("/progress")
    public ResponseEntity<?> getProgress(@RequestParam Integer points) {
        Double progress = stageConfigService.getProgress(points);
        Double stageProgress = stageConfigService.getStageProgress(points);
        StageConfig currentStage = stageConfigService.getCurrentStage(points);

        return ResponseEntity.ok(java.util.Map.of(
                "totalProgress", progress,
                "stageProgress", stageProgress,
                "currentStage", currentStage != null ? currentStage.getStage() : null,
                "currentThreshold", currentStage != null ? currentStage.getThreshold() : 0
        ));
    }

    /**
     * 获取已激活的阶段列表
     * GET /api/stages/activated
     *
     * @param points 当前积分
     * @return 已激活的阶段列表
     */
    @GetMapping("/activated")
    public ResponseEntity<?> getActivatedStages(@RequestParam Integer points) {
        List<StageConfig> stages = stageConfigService.getActivatedStages(points);
        return ResponseEntity.ok(stages);
    }

    /**
     * 获取所有大阶段（跨品级阶段）
     * GET /api/stages/major
     *
     * @return 大阶段列表
     */
    @GetMapping("/major")
    public ResponseEntity<?> getMajorStages() {
        List<StageConfig> stages = stageConfigService.getMajorStages();
        return ResponseEntity.ok(stages);
    }

    /**
     * 初始化阶段配置数据
     * POST /api/stages/init
     *
     * @return 初始化结果
     */
    @PostMapping("/init")
    public ResponseEntity<?> initStages() {
        stageConfigService.initStageConfigs();
        List<StageConfig> stages = stageConfigService.getAllStages();
        return ResponseEntity.ok(java.util.Map.of(
                "message", "阶段配置初始化完成",
                "count", stages.size(),
                "stages", stages
        ));
    }
}
