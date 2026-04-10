package com.example.demo.service;

import com.example.demo.dto.CharacterTypeDTO;

import java.util.List;

/**
 * 角色类型Service接口
 */
public interface CharacterTypeService {

    /**
     * 获取所有角色类型
     */
    List<CharacterTypeDTO> getAllCharacterTypes();

    /**
     * 根据ID获取角色类型
     */
    CharacterTypeDTO getCharacterTypeById(Long id);

    /**
     * 获取用于联级选择器的角色类型数据
     * 格式：[{label: '男鬼剑士', value: '男鬼剑士', children: [...]}]
     */
    List<CharacterTypeDTO> getCharacterTypesForCascade();

    /**
     * 根据ID获取角色类型（返回完整信息）
     */
    CharacterTypeDTO getCharacterTypeFullInfo(Long id);
}
