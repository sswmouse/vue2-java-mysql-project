package com.example.demo.service;

import com.example.demo.dto.CharacterEquipmentDTO;

import java.util.List;

/**
 * 角色装备配置服务接口
 *
 * @author Claude Code
 * @version 1.0
 * @since 2026-04-18
 */
public interface CharacterEquipmentService {

    /**
     * 获取角色所有装备配置（包含12套的进度）
     * @param characterId 角色ID
     * @return 角色所有装备配置列表
     */
    List<CharacterEquipmentDTO> getAllCharacterEquipment(Long characterId);

    /**
     * 获取角色单个装备套装配置
     * @param characterId 角色ID
     * @param setId 套装ID
     * @return 装备配置DTO
     */
    CharacterEquipmentDTO getCharacterEquipment(Long characterId, Long setId);

    /**
     * 获取角色当前穿戴的装备配置
     * @param characterId 角色ID
     * @return 当前穿戴的装备配置
     */
    CharacterEquipmentDTO getEquippedEquipment(Long characterId);

    /**
     * 保存角色装备配置
     * @param characterId 角色ID
     * @param dto 装备配置DTO
     * @return 保存后的DTO
     */
    CharacterEquipmentDTO saveCharacterEquipment(Long characterId, CharacterEquipmentDTO dto);

    /**
     * 穿戴装备
     * @param characterId 角色ID
     * @param setId 套装ID
     * @return 穿戴后的装备配置
     */
    CharacterEquipmentDTO equipSet(Long characterId, Long setId);

    /**
     * 卸下装备
     * @param characterId 角色ID
     * @param setId 套装ID
     * @return 卸下后的装备配置
     */
    CharacterEquipmentDTO unequipSet(Long characterId, Long setId);
}
