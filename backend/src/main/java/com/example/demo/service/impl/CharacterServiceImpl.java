package com.example.demo.service.impl;

import com.example.demo.dto.CharacterDTO;
import com.example.demo.dto.CharacterTypeDTO;
import com.example.demo.model.Character;
import com.example.demo.model.CharacterType;
import com.example.demo.repository.CharacterRepository;
import com.example.demo.repository.CharacterTypeRepository;
import com.example.demo.service.CharacterService;
import com.example.demo.service.CharacterTypeService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 角色Service实现类
 */
@Service
@RequiredArgsConstructor
public class CharacterServiceImpl implements CharacterService {

    private final CharacterRepository characterRepository;
    private final CharacterTypeRepository characterTypeRepository;
    private final CharacterTypeService characterTypeService;

    @Override
    @Transactional(readOnly = true)
    public List<CharacterDTO> getCharactersByUserId(Long userId) {
        List<Character> characters = characterRepository.findByUserId(userId);
        return characters.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public CharacterDTO getCharacterById(Long id) {
        Character character = characterRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("角色不存在"));
        return convertToDTO(character);
    }

    @Override
    @Transactional
    public CharacterDTO createCharacter(CharacterDTO characterDTO) {
        // 验证角色类型是否存在
        CharacterType characterType = characterTypeRepository.findById(characterDTO.getCharacterTypeId())
                .orElseThrow(() -> new RuntimeException("角色类型不存在"));

        Character character = convertToEntity(characterDTO);
        Character savedCharacter = characterRepository.save(character);
        return convertToDTO(savedCharacter);
    }

    @Override
    @Transactional
    public CharacterDTO updateCharacter(Long id, CharacterDTO characterDTO) {
        Character existingCharacter = characterRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("角色不存在"));

        // 验证角色类型是否存在
        CharacterType characterType = characterTypeRepository.findById(characterDTO.getCharacterTypeId())
                .orElseThrow(() -> new RuntimeException("角色类型不存在"));

        // 更新字段
        existingCharacter.setCharacterName(characterDTO.getCharacterName());
        existingCharacter.setJobNature(characterDTO.getJobNature());
        existingCharacter.setJobAttribute(characterDTO.getJobAttribute());
        existingCharacter.setStrength(characterDTO.getStrength());
        existingCharacter.setIntelligence(characterDTO.getIntelligence());
        existingCharacter.setSpirit(characterDTO.getSpirit());
        existingCharacter.setVitality(characterDTO.getVitality());
        existingCharacter.setPhysicalAttack(characterDTO.getPhysicalAttack());
        existingCharacter.setMagicalAttack(characterDTO.getMagicalAttack());
        existingCharacter.setIndependentAttack(characterDTO.getIndependentAttack());
        existingCharacter.setLightElement(characterDTO.getLightElement());
        existingCharacter.setFireElement(characterDTO.getFireElement());
        existingCharacter.setIceElement(characterDTO.getIceElement());
        existingCharacter.setDarkElement(characterDTO.getDarkElement());
        existingCharacter.setAvatarUrl(characterDTO.getAvatarUrl());

        Character updatedCharacter = characterRepository.save(existingCharacter);
        return convertToDTO(updatedCharacter);
    }

    @Override
    @Transactional
    public void deleteCharacter(Long id) {
        Character character = characterRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("角色不存在"));
        characterRepository.delete(character);
    }

    private CharacterDTO convertToDTO(Character character) {
        CharacterDTO dto = new CharacterDTO();
        dto.setId(character.getId());
        dto.setUserId(character.getUserId());
        dto.setCharacterTypeId(character.getCharacterTypeId());
        dto.setCharacterName(character.getCharacterName());
        dto.setJobNature(character.getJobNature());
        dto.setJobAttribute(character.getJobAttribute());
        dto.setStrength(character.getStrength());
        dto.setIntelligence(character.getIntelligence());
        dto.setSpirit(character.getSpirit());
        dto.setVitality(character.getVitality());
        dto.setPhysicalAttack(character.getPhysicalAttack());
        dto.setMagicalAttack(character.getMagicalAttack());
        dto.setIndependentAttack(character.getIndependentAttack());
        dto.setLightElement(character.getLightElement());
        dto.setFireElement(character.getFireElement());
        dto.setIceElement(character.getIceElement());
        dto.setDarkElement(character.getDarkElement());
        dto.setAvatarUrl(character.getAvatarUrl());

        // 计算最高属强
        Integer maxElement = 0;
        if (character.getLightElement() != null) maxElement = Math.max(maxElement, character.getLightElement());
        if (character.getFireElement() != null) maxElement = Math.max(maxElement, character.getFireElement());
        if (character.getIceElement() != null) maxElement = Math.max(maxElement, character.getIceElement());
        if (character.getDarkElement() != null) maxElement = Math.max(maxElement, character.getDarkElement());
        dto.setMaxElement(maxElement);

        // 获取角色类型信息
        try {
            CharacterTypeDTO characterTypeDTO = characterTypeService.getCharacterTypeFullInfo(character.getCharacterTypeId());
            dto.setCharacterType(characterTypeDTO);
        } catch (Exception e) {
            // 角色类型可能已被删除，忽略
        }

        return dto;
    }

    private Character convertToEntity(CharacterDTO dto) {
        Character character = new Character();
        character.setId(dto.getId());
        character.setUserId(dto.getUserId());
        character.setCharacterTypeId(dto.getCharacterTypeId());
        character.setCharacterName(dto.getCharacterName());
        character.setJobNature(dto.getJobNature());
        character.setJobAttribute(dto.getJobAttribute());
        character.setStrength(dto.getStrength());
        character.setIntelligence(dto.getIntelligence());
        character.setSpirit(dto.getSpirit());
        character.setVitality(dto.getVitality());
        character.setPhysicalAttack(dto.getPhysicalAttack());
        character.setMagicalAttack(dto.getMagicalAttack());
        character.setIndependentAttack(dto.getIndependentAttack());
        character.setLightElement(dto.getLightElement());
        character.setFireElement(dto.getFireElement());
        character.setIceElement(dto.getIceElement());
        character.setDarkElement(dto.getDarkElement());
        character.setAvatarUrl(dto.getAvatarUrl());
        return character;
    }
}
