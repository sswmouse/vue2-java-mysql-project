package com.example.demo.service.impl;

import com.example.demo.model.FogSystem;
import com.example.demo.repository.FogSystemRepository;
import com.example.demo.service.FogSystemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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

    /**
     * 迷雾等级名望配置表（1-30级）
     */
    private static final Map<Integer, FogLevelData> FOG_LEVEL_DATA = new HashMap<>();
    static {
        FOG_LEVEL_DATA.put(1, new FogLevelData(1000, 10.0, 2400, 0));
        FOG_LEVEL_DATA.put(2, new FogLevelData(1050, 10.5, 2450, 0));
        FOG_LEVEL_DATA.put(3, new FogLevelData(1100, 11.0, 2500, 0));
        FOG_LEVEL_DATA.put(4, new FogLevelData(1150, 11.5, 2550, 0));
        FOG_LEVEL_DATA.put(5, new FogLevelData(1250, 12.5, 2650, 30));
        FOG_LEVEL_DATA.put(6, new FogLevelData(1300, 13.0, 2700, 30));
        FOG_LEVEL_DATA.put(7, new FogLevelData(1350, 13.5, 2750, 30));
        FOG_LEVEL_DATA.put(8, new FogLevelData(1400, 14.0, 2800, 30));
        FOG_LEVEL_DATA.put(9, new FogLevelData(1450, 14.5, 2850, 30));
        FOG_LEVEL_DATA.put(10, new FogLevelData(1600, 16.0, 3000, 80));
        FOG_LEVEL_DATA.put(11, new FogLevelData(1650, 16.5, 3050, 80));
        FOG_LEVEL_DATA.put(12, new FogLevelData(1700, 17.0, 3100, 80));
        FOG_LEVEL_DATA.put(13, new FogLevelData(1750, 17.5, 3150, 80));
        FOG_LEVEL_DATA.put(14, new FogLevelData(1800, 18.0, 3200, 80));
        FOG_LEVEL_DATA.put(15, new FogLevelData(1900, 19.0, 3300, 110));
        FOG_LEVEL_DATA.put(16, new FogLevelData(1950, 19.5, 3350, 110));
        FOG_LEVEL_DATA.put(17, new FogLevelData(2000, 20.0, 3400, 110));
        FOG_LEVEL_DATA.put(18, new FogLevelData(2050, 20.5, 3450, 110));
        FOG_LEVEL_DATA.put(19, new FogLevelData(2100, 21.0, 3500, 110));
        FOG_LEVEL_DATA.put(20, new FogLevelData(2250, 22.5, 3650, 160));
        FOG_LEVEL_DATA.put(21, new FogLevelData(2300, 23.0, 3700, 160));
        FOG_LEVEL_DATA.put(22, new FogLevelData(2350, 23.5, 3750, 160));
        FOG_LEVEL_DATA.put(23, new FogLevelData(2400, 24.0, 3800, 160));
        FOG_LEVEL_DATA.put(24, new FogLevelData(2450, 24.5, 3850, 160));
        FOG_LEVEL_DATA.put(25, new FogLevelData(2550, 25.5, 3950, 190));
        FOG_LEVEL_DATA.put(26, new FogLevelData(2600, 26.0, 4000, 190));
        FOG_LEVEL_DATA.put(27, new FogLevelData(2650, 26.5, 4050, 190));
        FOG_LEVEL_DATA.put(28, new FogLevelData(2700, 27.0, 4100, 190));
        FOG_LEVEL_DATA.put(29, new FogLevelData(2750, 27.5, 4150, 190));
        FOG_LEVEL_DATA.put(30, new FogLevelData(3000, 30.0, 4400, 250));
    }

    /**
     * 迷雾等级数据内部类
     */
    private static class FogLevelData {
        final int fame;
        final double techAttack;
        final int buffBonus;
        final int fourStats;

        FogLevelData(int fame, double techAttack, int buffBonus, int fourStats) {
            this.fame = fame;
            this.techAttack = techAttack;
            this.buffBonus = buffBonus;
            this.fourStats = fourStats;
        }
    }

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

    @Override
    public Integer getFameByRegion(Long userId, String region) {
        // 默认跨区
        if (region == null || region.isEmpty()) {
            region = "跨六";
        }

        Optional<FogSystem> fog = fogSystemRepository.findByUserIdAndRegion(userId, region);
        int level = fog.map(FogSystem::getFogLevel).orElse(DEFAULT_LEVEL);

        // 从配置表获取名望
        FogLevelData levelData = FOG_LEVEL_DATA.get(level);
        return levelData != null ? levelData.fame : 0;
    }

    @Override
    public Integer getTotalFame(Long userId) {
        List<FogSystem> fogs = fogSystemRepository.findByUserId(userId);
        return fogs.stream()
                .mapToInt(fog -> {
                    FogLevelData levelData = FOG_LEVEL_DATA.get(fog.getFogLevel());
                    return levelData != null ? levelData.fame : 0;
                })
                .sum();
    }
}
