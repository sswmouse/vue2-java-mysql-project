package com.example.demo.service;

import com.example.demo.model.FogSystem;
import java.util.List;
import java.util.Optional;

public interface FogSystemService {

    /**
     * 获取用户在指定跨区的迷雾数据
     */
    Optional<FogSystem> getFogByUserIdAndRegion(Long userId, String region);

    /**
     * 获取用户在所有跨区的迷雾数据
     */
    List<FogSystem> getAllFogsByUserId(Long userId);

    /**
     * 创建或更新迷雾等级
     */
    FogSystem saveFogLevel(Long userId, String region, Integer level);

    /**
     * 批量初始化用户的所有跨区迷雾数据（默认30级）
     */
    List<FogSystem> initAllRegions(Long userId);

    /**
     * 获取用户在指定跨区的迷雾名望
     * @param userId 用户ID
     * @param region 跨区名称（如"跨六"）
     * @return 迷雾名望值
     */
    Integer getFameByRegion(Long userId, String region);

    /**
     * 获取用户所有跨区的迷雾总名望
     */
    Integer getTotalFame(Long userId);
}
