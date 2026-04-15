package com.example.demo.service.impl;

import com.example.demo.model.OathConfig;
import com.example.demo.repository.OathConfigRepository;
import com.example.demo.service.OathConfigService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

/**
 * 誓约配置服务实现类
 */
@Service
public class OathConfigServiceImpl implements OathConfigService {

    @Autowired
    private OathConfigRepository oathConfigRepository;

    /**
     * 3种誓约模式配置
     */
    private static final List<OathConfig> DEFAULT_CONFIGS = Arrays.asList(
            createConfig("BASIC", "无限动力", "觉醒CD-30%，伤害均衡", "觉醒技能冷却时间减少30%，伤害均衡输出"),
            createConfig("MODE1", "压倒性的力量", "极端三觉伤害爆发", "三觉伤害极大提升，适合爆发输出"),
            createConfig("MODE2", "超载", "一二觉+80%/+96%，可刷新", "一觉+80%、二觉+96%伤害，可刷新CD")
    );

    private static OathConfig createConfig(String mode, String name, String attribute, String description) {
        OathConfig config = new OathConfig();
        config.setOathMode(mode);
        config.setOathName(name);
        config.setOathAttribute(attribute);
        config.setDescription(description);
        return config;
    }

    @Override
    public List<OathConfig> getAllConfigs() {
        List<OathConfig> configs = oathConfigRepository.findAll();
        if (configs.isEmpty()) {
            return initOathConfigs();
        }
        return configs;
    }

    @Override
    public OathConfig getConfigByMode(String oathMode) {
        Optional<OathConfig> config = oathConfigRepository.findByOathMode(oathMode);
        return config.orElse(null);
    }

    @Override
    @Transactional
    public List<OathConfig> initOathConfigs() {
        for (OathConfig config : DEFAULT_CONFIGS) {
            Optional<OathConfig> existing = oathConfigRepository.findByOathMode(config.getOathMode());
            if (!existing.isPresent()) {
                oathConfigRepository.save(config);
            }
        }
        return oathConfigRepository.findAll();
    }
}
