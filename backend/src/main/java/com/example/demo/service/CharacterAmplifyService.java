package com.example.demo.service;

import com.example.demo.dto.CharacterAmplifyDTO;

import java.util.List;

/**
 * 角色增幅配置服务接口
 */
public interface CharacterAmplifyService {

    /**
     * 获取角色所有增幅配置
     */
    List<CharacterAmplifyDTO> getByCharacterId(Long characterId);

    /**
     * 保存或更新角色增幅配置
     */
    CharacterAmplifyDTO save(Long characterId, CharacterAmplifyDTO dto);

    /**
     * 删除角色指定部位的增幅配置
     */
    void deleteByPart(Long characterId, String equipmentPart);

    /**
     * 批量保存角色增幅配置
     */
    List<CharacterAmplifyDTO> batchSave(Long characterId, List<CharacterAmplifyDTO> dtos);
}
