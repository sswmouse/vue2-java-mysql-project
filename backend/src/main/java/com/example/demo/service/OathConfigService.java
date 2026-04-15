package com.example.demo.service;

import com.example.demo.model.OathConfig;

import java.util.List;

/**
 * 誓约配置服务接口
 */
public interface OathConfigService {

    /**
     * 获取所有誓约配置
     */
    List<OathConfig> getAllConfigs();

    /**
     * 根据模式获取誓约配置
     */
    OathConfig getConfigByMode(String oathMode);

    /**
     * 初始化誓约配置数据
     */
    List<OathConfig> initOathConfigs();
}
