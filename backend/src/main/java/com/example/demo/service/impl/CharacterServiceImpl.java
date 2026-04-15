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

        // 核心输出属性
        if (characterDTO.getTechniqueAttack() != null) existingCharacter.setTechniqueAttack(characterDTO.getTechniqueAttack());
        if (characterDTO.getAttackBonus() != null) existingCharacter.setAttackBonus(characterDTO.getAttackBonus());
        if (characterDTO.getBuffBonus() != null) existingCharacter.setBuffBonus(characterDTO.getBuffBonus());
        if (characterDTO.getSimulateDamage() != null) existingCharacter.setSimulateDamage(characterDTO.getSimulateDamage());
        if (characterDTO.getStatAttack() != null) existingCharacter.setStatAttack(characterDTO.getStatAttack());

        // 伤害词缀
        if (characterDTO.getFinalDamage() != null) existingCharacter.setFinalDamage(characterDTO.getFinalDamage());
        if (characterDTO.getWhiteCritDamage() != null) existingCharacter.setWhiteCritDamage(characterDTO.getWhiteCritDamage());
        if (characterDTO.getYellowCritDamage() != null) existingCharacter.setYellowCritDamage(characterDTO.getYellowCritDamage());
        if (characterDTO.getCritDamageBonus() != null) existingCharacter.setCritDamageBonus(characterDTO.getCritDamageBonus());
        if (characterDTO.getSkillLvBonus() != null) existingCharacter.setSkillLvBonus(characterDTO.getSkillLvBonus());
        if (characterDTO.getSkillAttackBonus() != null) existingCharacter.setSkillAttackBonus(characterDTO.getSkillAttackBonus());

        // 属性抗性
        if (characterDTO.getLightResistance() != null) existingCharacter.setLightResistance(characterDTO.getLightResistance());
        if (characterDTO.getFireResistance() != null) existingCharacter.setFireResistance(characterDTO.getFireResistance());
        if (characterDTO.getIceResistance() != null) existingCharacter.setIceResistance(characterDTO.getIceResistance());
        if (characterDTO.getDarkResistance() != null) existingCharacter.setDarkResistance(characterDTO.getDarkResistance());

        // 奶系BUFF字段
        if (characterDTO.getBuffLevel() != null) existingCharacter.setBuffLevel(characterDTO.getBuffLevel());
        if (characterDTO.getBuffStrength() != null) existingCharacter.setBuffStrength(characterDTO.getBuffStrength());
        if (characterDTO.getBuffIntelligence() != null) existingCharacter.setBuffIntelligence(characterDTO.getBuffIntelligence());

        // HP/MP
        if (characterDTO.getHpMax() != null) existingCharacter.setHpMax(characterDTO.getHpMax());
        if (characterDTO.getMpMax() != null) existingCharacter.setMpMax(characterDTO.getMpMax());

        // 防御
        if (characterDTO.getPhysicalDefense() != null) existingCharacter.setPhysicalDefense(characterDTO.getPhysicalDefense());
        if (characterDTO.getMagicalDefense() != null) existingCharacter.setMagicalDefense(characterDTO.getMagicalDefense());

        // 暴击/命中/回避
        if (characterDTO.getCritRate() != null) existingCharacter.setCritRate(characterDTO.getCritRate());
        if (characterDTO.getHitRate() != null) existingCharacter.setHitRate(characterDTO.getHitRate());
        if (characterDTO.getAvoidance() != null) existingCharacter.setAvoidance(characterDTO.getAvoidance());

        // 三速
        if (characterDTO.getAttackSpeed() != null) existingCharacter.setAttackSpeed(characterDTO.getAttackSpeed());
        if (characterDTO.getCastingSpeed() != null) existingCharacter.setCastingSpeed(characterDTO.getCastingSpeed());
        if (characterDTO.getMoveSpeed() != null) existingCharacter.setMoveSpeed(characterDTO.getMoveSpeed());

        // 恢复量
        if (characterDTO.getHpRecovery() != null) existingCharacter.setHpRecovery(characterDTO.getHpRecovery());
        if (characterDTO.getMpRecovery() != null) existingCharacter.setMpRecovery(characterDTO.getMpRecovery());

        // 物理/魔法暴击率
        if (characterDTO.getPhysicalCritRate() != null) existingCharacter.setPhysicalCritRate(characterDTO.getPhysicalCritRate());
        if (characterDTO.getMagicalCritRate() != null) existingCharacter.setMagicalCritRate(characterDTO.getMagicalCritRate());

        // 装备和誓约系统
        if (characterDTO.getEquipmentData() != null) existingCharacter.setEquipmentData(characterDTO.getEquipmentData());
        if (characterDTO.getOathMode() != null) existingCharacter.setOathMode(characterDTO.getOathMode());
        if (characterDTO.getOathAttribute() != null) existingCharacter.setOathAttribute(characterDTO.getOathAttribute());
        if (characterDTO.getEnhancementData() != null) existingCharacter.setEnhancementData(characterDTO.getEnhancementData());

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

        // DNF同步相关字段
        dto.setFameValue(character.getFameValue());
        dto.setServerName(character.getServerName());
        dto.setLevel(character.getLevel());
        dto.setAdventureName(character.getAdventureName());
        dto.setEquipmentSummary(character.getEquipmentSummary());
        dto.setSyncStatus(character.getSyncStatus());
        dto.setSyncErrorMsg(character.getSyncErrorMsg());
        if (character.getLastSyncTime() != null) {
            dto.setLastSyncTime(character.getLastSyncTime().toString());
        }

        // 计算最高属强
        Integer maxElement = 0;
        if (character.getLightElement() != null) maxElement = Math.max(maxElement, character.getLightElement());
        if (character.getFireElement() != null) maxElement = Math.max(maxElement, character.getFireElement());
        if (character.getIceElement() != null) maxElement = Math.max(maxElement, character.getIceElement());
        if (character.getDarkElement() != null) maxElement = Math.max(maxElement, character.getDarkElement());
        dto.setMaxElement(maxElement);

        // 核心输出属性
        dto.setTechniqueAttack(character.getTechniqueAttack());
        dto.setAttackBonus(character.getAttackBonus());
        dto.setBuffBonus(character.getBuffBonus());
        dto.setSimulateDamage(character.getSimulateDamage());
        dto.setStatAttack(character.getStatAttack());

        // 伤害词缀
        dto.setFinalDamage(character.getFinalDamage());
        dto.setWhiteCritDamage(character.getWhiteCritDamage());
        dto.setYellowCritDamage(character.getYellowCritDamage());
        dto.setCritDamageBonus(character.getCritDamageBonus());
        dto.setSkillLvBonus(character.getSkillLvBonus());
        dto.setSkillAttackBonus(character.getSkillAttackBonus());

        // 属性抗性
        dto.setLightResistance(character.getLightResistance());
        dto.setFireResistance(character.getFireResistance());
        dto.setIceResistance(character.getIceResistance());
        dto.setDarkResistance(character.getDarkResistance());

        // 奶系BUFF字段
        dto.setBuffLevel(character.getBuffLevel());
        dto.setBuffStrength(character.getBuffStrength());
        dto.setBuffIntelligence(character.getBuffIntelligence());

        // HP/MP
        dto.setHpMax(character.getHpMax());
        dto.setMpMax(character.getMpMax());

        // 防御
        dto.setPhysicalDefense(character.getPhysicalDefense());
        dto.setMagicalDefense(character.getMagicalDefense());

        // 暴击/命中/回避
        dto.setCritRate(character.getCritRate());
        dto.setHitRate(character.getHitRate());
        dto.setAvoidance(character.getAvoidance());

        // 三速
        dto.setAttackSpeed(character.getAttackSpeed());
        dto.setCastingSpeed(character.getCastingSpeed());
        dto.setMoveSpeed(character.getMoveSpeed());

        // 恢复量
        dto.setHpRecovery(character.getHpRecovery());
        dto.setMpRecovery(character.getMpRecovery());

        // 物理/魔法暴击率
        dto.setPhysicalCritRate(character.getPhysicalCritRate());
        dto.setMagicalCritRate(character.getMagicalCritRate());

        // 装备和誓约系统
        dto.setEquipmentData(character.getEquipmentData());
        dto.setOathMode(character.getOathMode());
        dto.setOathAttribute(character.getOathAttribute());
        dto.setEnhancementData(character.getEnhancementData());

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
