package com.example.demo.controller;

import com.example.demo.model.PetOption;
import com.example.demo.service.PetOptionService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * 宠物选项控制器
 */
@RestController
@RequestMapping("/api/pet-options")
public class PetOptionController {

    private final PetOptionService service;

    public PetOptionController(PetOptionService service) {
        this.service = service;
    }

    /**
     * 获取所有宠物选项
     */
    @GetMapping
    public ResponseEntity<List<PetOption>> getAllOptions() {
        return ResponseEntity.ok(service.getAllOptions());
    }

    /**
     * 根据ID获取宠物选项
     */
    @GetMapping("/{id}")
    public ResponseEntity<PetOption> getOptionById(@PathVariable Long id) {
        PetOption option = service.getOptionById(id);
        if (option == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(option);
    }

    /**
     * 根据赛季获取宠物
     */
    @GetMapping("/season/{season}")
    public ResponseEntity<List<PetOption>> getOptionsBySeason(@PathVariable String season) {
        return ResponseEntity.ok(service.getOptionsBySeason(season));
    }

    /**
     * 获取最新赛季的宠物
     */
    @GetMapping("/latest")
    public ResponseEntity<List<PetOption>> getLatestOptions() {
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
    public ResponseEntity<Map<String, List<PetOption>>> getOptionsGroupedBySeason() {
        return ResponseEntity.ok(service.getOptionsGroupedBySeason());
    }

    /**
     * 创建宠物选项
     */
    @PostMapping
    public ResponseEntity<PetOption> createOption(@RequestBody PetOption option) {
        return ResponseEntity.ok(service.createOption(option));
    }

    /**
     * 更新宠物选项
     */
    @PutMapping("/{id}")
    public ResponseEntity<PetOption> updateOption(@PathVariable Long id, @RequestBody PetOption option) {
        PetOption updated = service.updateOption(id, option);
        if (updated == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(updated);
    }

    /**
     * 删除宠物选项
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteOption(@PathVariable Long id) {
        service.deleteOption(id);
        return ResponseEntity.ok().build();
    }

    /**
     * 批量创建宠物选项
     */
    @PostMapping("/batch")
    public ResponseEntity<List<PetOption>> batchCreateOptions(@RequestBody List<PetOption> options) {
        return ResponseEntity.ok(service.batchCreateOptions(options));
    }
}
