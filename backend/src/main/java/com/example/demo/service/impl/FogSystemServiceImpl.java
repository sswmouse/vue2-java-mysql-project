package com.example.demo.service.impl;

import com.example.demo.model.FogSystem;
import com.example.demo.repository.FogSystemRepository;
import com.example.demo.service.FogSystemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class FogSystemServiceImpl implements FogSystemService {

    @Autowired
    private FogSystemRepository fogSystemRepository;

    /**
     * 8个跨区列表
     */
    private static final List<String> REGIONS = Arrays.asList(
            "跨一", "跨二", "跨三A", "跨三B", "跨四", "跨五", "跨六", "跨七"
    );

    /**
     * 默认迷雾等级
     */
    private static final Integer DEFAULT_LEVEL = 30;

    @Override
    public Optional<FogSystem> getFogByUserIdAndRegion(Long userId, String region) {
        return fogSystemRepository.findByUserIdAndRegion(userId, region);
    }

    @Override
    public List<FogSystem> getAllFogsByUserId(Long userId) {
        return fogSystemRepository.findByUserId(userId);
    }

    @Override
    @Transactional
    public FogSystem saveFogLevel(Long userId, String region, Integer level) {
        // 限制等级范围 1-30
        level = Math.max(1, Math.min(30, level));

        Optional<FogSystem> existingFog = fogSystemRepository.findByUserIdAndRegion(userId, region);

        if (existingFog.isPresent()) {
            // 更新现有记录
            FogSystem fog = existingFog.get();
            fog.setFogLevel(level);
            return fogSystemRepository.save(fog);
        } else {
            // 创建新记录
            FogSystem fog = new FogSystem();
            fog.setUserId(userId);
            fog.setRegion(region);
            fog.setFogLevel(level);
            return fogSystemRepository.save(fog);
        }
    }

    @Override
    @Transactional
    public List<FogSystem> initAllRegions(Long userId) {
        return REGIONS.stream()
                .map(region -> {
                    Optional<FogSystem> existingFog = fogSystemRepository.findByUserIdAndRegion(userId, region);
                    if (existingFog.isPresent()) {
                        return existingFog.get();
                    } else {
                        FogSystem fog = new FogSystem();
                        fog.setUserId(userId);
                        fog.setRegion(region);
                        fog.setFogLevel(DEFAULT_LEVEL);
                        return fogSystemRepository.save(fog);
                    }
                })
                .collect(Collectors.toList());
    }

    /**
     * 获取所有跨区列表
     */
    public static List<String> getRegions() {
        return REGIONS;
    }
}
