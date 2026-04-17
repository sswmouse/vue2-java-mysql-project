package com.example.demo.controller;

import com.example.demo.model.EnchantOption;
import com.example.demo.service.EnchantOptionService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * 附魔宝珠选项控制器
 */
@RestController
@RequestMapping("/api/enchant-options")
public class EnchantOptionController {

    private final EnchantOptionService service;

    public EnchantOptionController(EnchantOptionService service) {
        this.service = service;
    }

    /**
     * 获取所有附魔选项
     */
    @GetMapping
    public ResponseEntity<List<EnchantOption>> getAllOptions() {
        return ResponseEntity.ok(service.getAllOptions());
    }

    /**
     * 根据ID获取附魔选项
     */
    @GetMapping("/{id}")
    public ResponseEntity<EnchantOption> getOptionById(@PathVariable Long id) {
        EnchantOption option = service.getOptionById(id);
        if (option == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(option);
    }

    /**
     * 根据装备部位获取附魔选项
     */
    @GetMapping("/part/{part}")
    public ResponseEntity<List<EnchantOption>> getOptionsByPart(@PathVariable String part) {
        return ResponseEntity.ok(service.getOptionsByPart(part));
    }

    /**
     * 根据礼包类型获取附魔选项
     */
    @GetMapping("/package/{packageType}")
    public ResponseEntity<List<EnchantOption>> getOptionsByPackage(@PathVariable String packageType) {
        return ResponseEntity.ok(service.getOptionsByPackage(packageType));
    }

    /**
     * 获取最新赛季的附魔选项
     */
    @GetMapping("/latest")
    public ResponseEntity<List<EnchantOption>> getLatestOptions() {
        return ResponseEntity.ok(service.getLatestOptions());
    }

    /**
     * 获取所有装备部位
     */
    @GetMapping("/parts")
    public ResponseEntity<List<String>> getAllParts() {
        return ResponseEntity.ok(service.getAllParts());
    }

    /**
     * 获取所有礼包类型
     */
    @GetMapping("/packages")
    public ResponseEntity<List<String>> getAllPackages() {
        return ResponseEntity.ok(service.getAllPackages());
    }

    /**
     * 按部位分组的所有选项
     */
    @GetMapping("/grouped")
    public ResponseEntity<Map<String, List<EnchantOption>>> getOptionsGroupedByPart() {
        return ResponseEntity.ok(service.getOptionsGroupedByPart());
    }

    /**
     * 创建附魔选项
     */
    @PostMapping
    public ResponseEntity<EnchantOption> createOption(@RequestBody EnchantOption option) {
        return ResponseEntity.ok(service.createOption(option));
    }

    /**
     * 更新附魔选项
     */
    @PutMapping("/{id}")
    public ResponseEntity<EnchantOption> updateOption(@PathVariable Long id, @RequestBody EnchantOption option) {
        EnchantOption updated = service.updateOption(id, option);
        if (updated == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(updated);
    }

    /**
     * 删除附魔选项
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteOption(@PathVariable Long id) {
        service.deleteOption(id);
        return ResponseEntity.ok().build();
    }

    /**
     * 批量创建附魔选项
     */
    @PostMapping("/batch")
    public ResponseEntity<List<EnchantOption>> batchCreateOptions(@RequestBody List<EnchantOption> options) {
        return ResponseEntity.ok(service.batchCreateOptions(options));
    }
}
