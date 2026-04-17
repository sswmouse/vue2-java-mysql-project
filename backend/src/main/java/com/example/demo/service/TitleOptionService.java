package com.example.demo.service;

import com.example.demo.model.TitleOption;

import java.util.List;
import java.util.Map;

/**
 * 称号选项服务接口
 */
public interface TitleOptionService {

    /**
     * 获取所有称号选项
     */
    List<TitleOption> getAllOptions();

    /**
     * 根据ID获取称号选项
     */
    TitleOption getOptionById(Long id);

    /**
     * 根据赛季获取称号
     */
    List<TitleOption> getOptionsBySeason(String season);

    /**
     * 获取最新赛季的称号
     */
    List<TitleOption> getLatestOptions();

    /**
     * 获取所有赛季
     */
    List<String> getAllSeasons();

    /**
     * 按赛季分组的所有选项
     */
    Map<String, List<TitleOption>> getOptionsGroupedBySeason();

    /**
     * 创建称号选项
     */
    TitleOption createOption(TitleOption option);

    /**
     * 更新称号选项
     */
    TitleOption updateOption(Long id, TitleOption option);

    /**
     * 删除称号选项
     */
    void deleteOption(Long id);

    /**
     * 批量创建称号选项
     */
    List<TitleOption> batchCreateOptions(List<TitleOption> options);
}
