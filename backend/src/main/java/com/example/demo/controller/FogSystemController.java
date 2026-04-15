package com.example.demo.controller;

import com.example.demo.model.FogSystem;
import com.example.demo.service.FogSystemService;
import com.example.demo.service.impl.FogSystemServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/fogs")
public class FogSystemController {

    @Autowired
    private FogSystemService fogSystemService;

    /**
     * 获取当前用户所有跨区的迷雾数据
     */
    @GetMapping
    public ResponseEntity<?> getAllFogs(
            @RequestParam(required = false, defaultValue = "1") Long userId) {
        List<FogSystem> fogs = fogSystemService.getAllFogsByUserId(userId);

        // 如果没有数据，初始化所有跨区
        if (fogs.isEmpty()) {
            fogs = fogSystemService.initAllRegions(userId);
        }

        // 转换为 Map 格式，key 为跨区名称
        Map<String, Integer> fogData = new HashMap<>();
        for (FogSystem fog : fogs) {
            fogData.put(fog.getRegion(), fog.getFogLevel());
        }

        return ResponseEntity.ok(fogData);
    }

    /**
     * 获取指定跨区的迷雾数据
     */
    @GetMapping("/{region}")
    public ResponseEntity<?> getFogByRegion(
            @PathVariable String region,
            @RequestParam(required = false, defaultValue = "1") Long userId) {
        FogSystem fog = fogSystemService.getFogByUserIdAndRegion(userId, region)
                .orElse(null);

        if (fog == null) {
            // 不存在则创建默认30级
            fog = fogSystemService.saveFogLevel(userId, region, 30);
        }

        return ResponseEntity.ok(fog);
    }

    /**
     * 更新指定跨区的迷雾等级
     */
    @PutMapping("/{region}")
    public ResponseEntity<?> updateFogLevel(
            @PathVariable String region,
            @RequestBody Map<String, Object> request) {
        Long userId = request.get("userId") != null
                ? Long.valueOf(request.get("userId").toString()) : 1L;
        Integer level = request.get("level") != null
                ? Integer.valueOf(request.get("level").toString()) : 30;

        if (level == null) {
            return ResponseEntity.badRequest().body("等级不能为空");
        }

        FogSystem fog = fogSystemService.saveFogLevel(userId, region, level);
        return ResponseEntity.ok(fog);
    }

    /**
     * 批量初始化所有跨区
     */
    @PostMapping("/init")
    public ResponseEntity<?> initAllRegions(
            @RequestParam(required = false, defaultValue = "1") Long userId) {
        List<FogSystem> fogs = fogSystemService.initAllRegions(userId);
        return ResponseEntity.ok(fogs);
    }

    /**
     * 获取所有跨区列表
     */
    @GetMapping("/regions")
    public ResponseEntity<?> getRegions() {
        return ResponseEntity.ok(FogSystemServiceImpl.getRegions());
    }
}
