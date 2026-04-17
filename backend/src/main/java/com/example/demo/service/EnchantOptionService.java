package com.example.demo.service;

import com.example.demo.model.EnchantOption;

import java.util.List;
import java.util.Map;

/**
 * 附魔宝珠选项服务接口
 */
public interface EnchantOptionService {

    /**
     * 获取所有附魔选项
     */
    List<EnchantOption> getAllOptions();

    /**
     * 根据ID获取附魔选项
     */
    EnchantOption getOptionById(Long id);

    /**
     * 根据装备部位获取附魔选项
     */
    List<EnchantOption> getOptionsByPart(String equipmentPart);

    /**
     * 根据礼包类型获取附魔选项
     */
    List<EnchantOption> getOptionsByPackage(String packageType);

    /**
     * 获取最新赛季的附魔选项
     */
    List<EnchantOption> getLatestOptions();

    /**
     * 获取所有装备部位
     */
    List<String> getAllParts();

    /**
     * 获取所有礼包类型
     */
    List<String> getAllPackages();

    /**
     * 按部位分组的所有选项
     */
    Map<String, List<EnchantOption>> getOptionsGroupedByPart();

    /**
     * 创建附魔选项
     */
    EnchantOption createOption(EnchantOption option);

    /**
     * 更新附魔选项
     */
    EnchantOption updateOption(Long id, EnchantOption option);

    /**
     * 删除附魔选项
     */
    void deleteOption(Long id);

    /**
     * 批量创建附魔选项
     */
    List<EnchantOption> batchCreateOptions(List<EnchantOption> options);
}
