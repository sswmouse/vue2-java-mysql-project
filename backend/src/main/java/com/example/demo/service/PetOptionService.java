package com.example.demo.service;

import com.example.demo.model.PetOption;

import java.util.List;
import java.util.Map;

/**
 * 宠物选项服务接口
 */
public interface PetOptionService {

    /**
     * 获取所有宠物选项
     */
    List<PetOption> getAllOptions();

    /**
     * 根据ID获取宠物选项
     */
    PetOption getOptionById(Long id);

    /**
     * 根据赛季获取宠物
     */
    List<PetOption> getOptionsBySeason(String season);

    /**
     * 获取最新赛季的宠物
     */
    List<PetOption> getLatestOptions();

    /**
     * 获取所有赛季
     */
    List<String> getAllSeasons();

    /**
     * 按赛季分组的所有选项
     */
    Map<String, List<PetOption>> getOptionsGroupedBySeason();

    /**
     * 创建宠物选项
     */
    PetOption createOption(PetOption option);

    /**
     * 更新宠物选项
     */
    PetOption updateOption(Long id, PetOption option);

    /**
     * 删除宠物选项
     */
    void deleteOption(Long id);

    /**
     * 批量创建宠物选项
     */
    List<PetOption> batchCreateOptions(List<PetOption> options);
}
