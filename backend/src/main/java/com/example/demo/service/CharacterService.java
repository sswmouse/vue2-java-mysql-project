package com.example.demo.service;

import com.example.demo.dto.CharacterDTO;

import java.util.List;

/**
 * 角色Service接口
 */
public interface CharacterService {

    /**
     * 根据用户ID获取角色列表
     */
    List<CharacterDTO> getCharactersByUserId(Long userId);

    /**
     * 根据ID获取角色
     */
    CharacterDTO getCharacterById(Long id);

    /**
     * 创建角色
     */
    CharacterDTO createCharacter(CharacterDTO characterDTO);

    /**
     * 更新角色
     */
    CharacterDTO updateCharacter(Long id, CharacterDTO characterDTO);

    /**
     * 删除角色
     */
    void deleteCharacter(Long id);
}
