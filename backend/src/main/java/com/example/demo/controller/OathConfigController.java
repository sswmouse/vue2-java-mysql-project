package com.example.demo.controller;

import com.example.demo.model.OathConfig;
import com.example.demo.service.OathConfigService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 誓约配置Controller
 */
@RestController
@RequestMapping("/api/oath-configs")
@CrossOrigin(origins = "*")
public class OathConfigController {

    @Autowired
    private OathConfigService oathConfigService;

    /**
     * 获取所有誓约配置
     */
    @GetMapping
    public ResponseEntity<?> getAllConfigs() {
        List<OathConfig> configs = oathConfigService.getAllConfigs();
        return ResponseEntity.ok(configs);
    }

    /**
     * 根据模式获取誓约配置
     */
    @GetMapping("/{oathMode}")
    public ResponseEntity<?> getConfigByMode(@PathVariable String oathMode) {
        OathConfig config = oathConfigService.getConfigByMode(oathMode);
        if (config == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(config);
    }

    /**
     * 初始化誓约配置数据
     */
    @PostMapping("/init")
    public ResponseEntity<?> initConfigs() {
        List<OathConfig> configs = oathConfigService.initOathConfigs();
        return ResponseEntity.ok(configs);
    }
}
