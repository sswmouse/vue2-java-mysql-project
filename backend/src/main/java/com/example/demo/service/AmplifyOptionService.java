package com.example.demo.service;

import com.example.demo.model.AmplifyOption;

import java.util.List;

/**
 * 增幅选项服务接口
 */
public interface AmplifyOptionService {

    /**
     * 获取所有增幅选项
     */
    List<AmplifyOption> getAllOptions();

    /**
     * 根据ID获取增幅选项
     */
    AmplifyOption getOptionById(Long id);

    /**
     * 根据增幅类型获取选项
     */
    List<AmplifyOption> getOptionsByType(String amplifyType);

    /**
     * 创建增幅选项
     */
    AmplifyOption createOption(AmplifyOption option);

    /**
     * 更新增幅选项
     */
    AmplifyOption updateOption(Long id, AmplifyOption option);

    /**
     * 删除增幅选项
     */
    void deleteOption(Long id);

    /**
     * 批量创建增幅选项
     */
    List<AmplifyOption> batchCreateOptions(List<AmplifyOption> options);
}
