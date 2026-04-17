package com.example.demo.controller;

import com.example.demo.model.AmplifyOption;
import com.example.demo.service.AmplifyOptionService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 增幅选项控制器
 */
@RestController
@RequestMapping("/api/amplify-options")
public class AmplifyOptionController {

    private final AmplifyOptionService service;

    public AmplifyOptionController(AmplifyOptionService service) {
        this.service = service;
    }

    /**
     * 获取所有增幅选项
     */
    @GetMapping
    public ResponseEntity<List<AmplifyOption>> getAllOptions() {
        return ResponseEntity.ok(service.getAllOptions());
    }

    /**
     * 根据ID获取增幅选项
     */
    @GetMapping("/{id}")
    public ResponseEntity<AmplifyOption> getOptionById(@PathVariable Long id) {
        AmplifyOption option = service.getOptionById(id);
        if (option == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(option);
    }

    /**
     * 根据增幅类型获取选项
     */
    @GetMapping("/type/{type}")
    public ResponseEntity<List<AmplifyOption>> getOptionsByType(@PathVariable String type) {
        return ResponseEntity.ok(service.getOptionsByType(type));
    }

    /**
     * 创建增幅选项
     */
    @PostMapping
    public ResponseEntity<AmplifyOption> createOption(@RequestBody AmplifyOption option) {
        return ResponseEntity.ok(service.createOption(option));
    }

    /**
     * 更新增幅选项
     */
    @PutMapping("/{id}")
    public ResponseEntity<AmplifyOption> updateOption(@PathVariable Long id, @RequestBody AmplifyOption option) {
        AmplifyOption updated = service.updateOption(id, option);
        if (updated == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(updated);
    }

    /**
     * 删除增幅选项
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteOption(@PathVariable Long id) {
        service.deleteOption(id);
        return ResponseEntity.ok().build();
    }

    /**
     * 批量创建增幅选项
     */
    @PostMapping("/batch")
    public ResponseEntity<List<AmplifyOption>> batchCreateOptions(@RequestBody List<AmplifyOption> options) {
        return ResponseEntity.ok(service.batchCreateOptions(options));
    }
}
