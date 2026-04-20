package com.example.demo.service;

import com.example.demo.dto.CharacterEnchantDTO;

import java.util.List;

/**
 * 角色附魔配置服务接口
 *
 * @author Claude Code
 * @version 1.0
 * @since 2026-04-20
 */
public interface CharacterEnchantService {

    /**
     * 获取角色所有附魔配置
     */
    List<CharacterEnchantDTO> getAllByCharacterId(Long characterId);

    /**
     * 获取角色指定部位的附魔配置
     */
    CharacterEnchantDTO getByCharacterIdAndPart(Long characterId, String equipmentPart);

    /**
     * 保存或更新角色附魔配置
     */
    CharacterEnchantDTO save(Long characterId, CharacterEnchantDTO dto);

    /**
     * 批量保存角色附魔配置
     */
    List<CharacterEnchantDTO> saveAll(Long characterId, List<CharacterEnchantDTO> dtos);

    /**
     * 删除角色指定部位的附魔配置
     */
    void delete(Long characterId, String equipmentPart);

    /**
     * 删除角色所有附魔配置
     */
    void deleteAllByCharacterId(Long characterId);
}
