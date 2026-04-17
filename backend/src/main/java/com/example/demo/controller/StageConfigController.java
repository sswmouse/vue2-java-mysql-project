package com.example.demo.controller;

import com.example.demo.model.StageConfig;
import com.example.demo.service.StageConfigService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 阶段配置Controller
 * 提供阶段配置相关的REST API接口
 *
 * @author Claude Code
 * @version 1.0
 * @since 2026-04-17
 */
@RestController
@RequestMapping("/api/stage-configs")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
@Slf4j
public class StageConfigController {

    private final StageConfigService stageConfigService;

    /**
     * 获取所有阶段配置
     * GET /api/stage-configs
     *
     * @return 阶段配置列表
     */
    @GetMapping(produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<?> getAllStages() {
        List<StageConfig> stages = stageConfigService.getAllStages();
        return ResponseEntity.ok(stages);
    }

    /**
     * 根据积分获取当前阶段
     * GET /api/stage-configs/current
     *
     * @param points 当前积分
     * @return 当前阶段
     */
    @GetMapping(value = "/current", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<?> getCurrentStage(@RequestParam Integer points) {
        StageConfig stage = stageConfigService.getCurrentStage(points);
        return ResponseEntity.ok(stage);
    }

    /**
     * 初始化阶段配置数据
     * POST /api/stage-configs/init
     *
     * @return 初始化结果
     */
    @PostMapping("/init")
    public ResponseEntity<?> initStages() {
        stageConfigService.initStageConfigs();
        return ResponseEntity.ok("阶段配置初始化完成");
    }
}
