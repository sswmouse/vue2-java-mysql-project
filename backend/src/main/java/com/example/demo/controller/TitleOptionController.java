package com.example.demo.controller;

import com.example.demo.model.TitleOption;
import com.example.demo.service.TitleOptionService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * 称号选项控制器
 */
@RestController
@RequestMapping("/api/title-options")
public class TitleOptionController {

    private final TitleOptionService service;

    public TitleOptionController(TitleOptionService service) {
        this.service = service;
    }

    /**
     * 获取所有称号选项
     */
    @GetMapping
    public ResponseEntity<List<TitleOption>> getAllOptions() {
        return ResponseEntity.ok(service.getAllOptions());
    }

    /**
     * 根据ID获取称号选项
     */
    @GetMapping("/{id}")
    public ResponseEntity<TitleOption> getOptionById(@PathVariable Long id) {
        TitleOption option = service.getOptionById(id);
        if (option == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(option);
    }

    /**
     * 根据赛季获取称号
     */
    @GetMapping("/season/{season}")
    public ResponseEntity<List<TitleOption>> getOptionsBySeason(@PathVariable String season) {
        return ResponseEntity.ok(service.getOptionsBySeason(season));
    }

    /**
     * 获取最新赛季的称号
     */
    @GetMapping("/latest")
    public ResponseEntity<List<TitleOption>> getLatestOptions() {
        return ResponseEntity.ok(service.getLatestOptions());
    }

    /**
     * 获取所有赛季
     */
    @GetMapping("/seasons")
    public ResponseEntity<List<String>> getAllSeasons() {
        return ResponseEntity.ok(service.getAllSeasons());
    }

    /**
     * 按赛季分组的所有选项
     */
    @GetMapping("/grouped")
    public ResponseEntity<Map<String, List<TitleOption>>> getOptionsGroupedBySeason() {
        return ResponseEntity.ok(service.getOptionsGroupedBySeason());
    }

    /**
     * 创建称号选项
     */
    @PostMapping
    public ResponseEntity<TitleOption> createOption(@RequestBody TitleOption option) {
        return ResponseEntity.ok(service.createOption(option));
    }

    /**
     * 更新称号选项
     */
    @PutMapping("/{id}")
    public ResponseEntity<TitleOption> updateOption(@PathVariable Long id, @RequestBody TitleOption option) {
        TitleOption updated = service.updateOption(id, option);
        if (updated == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(updated);
    }

    /**
     * 删除称号选项
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteOption(@PathVariable Long id) {
        service.deleteOption(id);
        return ResponseEntity.ok().build();
    }

    /**
     * 批量创建称号选项
     */
    @PostMapping("/batch")
    public ResponseEntity<List<TitleOption>> batchCreateOptions(@RequestBody List<TitleOption> options) {
        return ResponseEntity.ok(service.batchCreateOptions(options));
    }
}
