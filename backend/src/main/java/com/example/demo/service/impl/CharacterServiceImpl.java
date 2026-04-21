package com.example.demo.service.impl;

import com.example.demo.dto.CharacterDTO;
import com.example.demo.dto.CharacterTypeDTO;
import com.example.demo.model.CharacterTitle;
import com.example.demo.model.CharacterPet;
import com.example.demo.model.CharacterEnchant;
import com.example.demo.model.CharacterAmplify;
import com.example.demo.model.Title;
import com.example.demo.model.Pet;
import com.example.demo.repository.*;
import com.example.demo.service.CharacterService;
import com.example.demo.service.CharacterTypeService;
import com.example.demo.service.FogSystemService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
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
    private final CharacterTitleRepository characterTitleRepository;
    private final CharacterPetRepository characterPetRepository;
    private final CharacterEnchantRepository characterEnchantRepository;
    private final CharacterAmplifyRepository characterAmplifyRepository;
    private final TitleRepository titleRepository;
    private final PetRepository petRepository;
    private final FogSystemService fogSystemService;

    @Override
    @Transactional(readOnly = true)
    public List<CharacterDTO> getCharactersByUserId(Long userId) {
        List<com.example.demo.model.Character> characters = characterRepository.findByUserId(userId);
        return characters.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public CharacterDTO getCharacterById(Long id) {
        com.example.demo.model.Character character = characterRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("角色不存在"));
        return convertToDTO(character);
    }

    @Override
    @Transactional
    public CharacterDTO createCharacter(CharacterDTO characterDTO) {
        // 验证角色类型是否存在
        characterTypeRepository.findById(characterDTO.getCharacterTypeId())
                .orElseThrow(() -> new RuntimeException("角色类型不存在"));

        com.example.demo.model.Character character = new com.example.demo.model.Character();
        character.setUserId(characterDTO.getUserId());
        character.setCharacterTypeId(characterDTO.getCharacterTypeId());
        character.setCharacterName(characterDTO.getCharacterName());
        character.setAvatarUrl(characterDTO.getAvatarUrl());
        character.setLevel(characterDTO.getLevel() != null ? characterDTO.getLevel() : 115);

        com.example.demo.model.Character savedCharacter = characterRepository.save(character);
        return convertToDTO(savedCharacter);
    }

    @Override
    @Transactional
    public CharacterDTO updateCharacter(Long id, CharacterDTO characterDTO) {
        com.example.demo.model.Character existingCharacter = characterRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("角色不存在"));

        // 验证角色类型是否存在
        if (characterDTO.getCharacterTypeId() != null) {
            characterTypeRepository.findById(characterDTO.getCharacterTypeId())
                    .orElseThrow(() -> new RuntimeException("角色类型不存在"));
        }

        // 只更新传入的字段
        if (characterDTO.getCharacterName() != null) {
            existingCharacter.setCharacterName(characterDTO.getCharacterName());
        }
        if (characterDTO.getCharacterTypeId() != null) {
            existingCharacter.setCharacterTypeId(characterDTO.getCharacterTypeId());
        }
        if (characterDTO.getAvatarUrl() != null) {
            existingCharacter.setAvatarUrl(characterDTO.getAvatarUrl());
        }
        if (characterDTO.getLevel() != null) {
            existingCharacter.setLevel(characterDTO.getLevel());
        }

        // 以下字段可选更新
        if (characterDTO.getJobNature() != null) existingCharacter.setJobNature(characterDTO.getJobNature());
        if (characterDTO.getJobAttribute() != null) existingCharacter.setJobAttribute(characterDTO.getJobAttribute());
        if (characterDTO.getStrength() != null) existingCharacter.setStrength(characterDTO.getStrength());
        if (characterDTO.getIntelligence() != null) existingCharacter.setIntelligence(characterDTO.getIntelligence());
        if (characterDTO.getSpirit() != null) existingCharacter.setSpirit(characterDTO.getSpirit());
        if (characterDTO.getVitality() != null) existingCharacter.setVitality(characterDTO.getVitality());
        if (characterDTO.getPhysicalAttack() != null) existingCharacter.setPhysicalAttack(characterDTO.getPhysicalAttack());
        if (characterDTO.getMagicalAttack() != null) existingCharacter.setMagicalAttack(characterDTO.getMagicalAttack());
        if (characterDTO.getIndependentAttack() != null) existingCharacter.setIndependentAttack(characterDTO.getIndependentAttack());
        if (characterDTO.getLightElement() != null) existingCharacter.setLightElement(characterDTO.getLightElement());
        if (characterDTO.getFireElement() != null) existingCharacter.setFireElement(characterDTO.getFireElement());
        if (characterDTO.getIceElement() != null) existingCharacter.setIceElement(characterDTO.getIceElement());
        if (characterDTO.getDarkElement() != null) existingCharacter.setDarkElement(characterDTO.getDarkElement());

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

        com.example.demo.model.Character updatedCharacter = characterRepository.save(existingCharacter);
        return convertToDTO(updatedCharacter);
    }

    @Override
    @Transactional
    public void deleteCharacter(Long id) {
        com.example.demo.model.Character character = characterRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("角色不存在"));
        characterRepository.delete(character);
    }

    private CharacterDTO convertToDTO(com.example.demo.model.Character character) {
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

        // 计算名望值：称号 + 宠物 + 附魔 + 增幅 + 迷雾
        AtomicInteger totalFame = new AtomicInteger(0);

        // 称号名望
        List<CharacterTitle> equippedTitles = characterTitleRepository.findByCharacterId(character.getId());
        for (CharacterTitle ct : equippedTitles) {
            if (Boolean.TRUE.equals(ct.getIsEquipped())) {
                titleRepository.findById(ct.getTitleId()).ifPresent(title -> {
                    totalFame.addAndGet(title.getFame() != null ? title.getFame() : 0);
                });
            }
        }

        // 宠物名望
        List<CharacterPet> equippedPets = characterPetRepository.findByCharacterId(character.getId());
        for (CharacterPet cp : equippedPets) {
            if (Boolean.TRUE.equals(cp.getIsEquipped())) {
                petRepository.findById(cp.getPetId()).ifPresent(pet -> {
                    totalFame.addAndGet(pet.getFame() != null ? pet.getFame() : 0);
                });
            }
        }

        // 附魔名望
        List<CharacterEnchant> enchants = characterEnchantRepository.findAllByCharacterId(character.getId());
        for (CharacterEnchant enchant : enchants) {
            totalFame.addAndGet(enchant.getFame() != null ? enchant.getFame() : 0);
        }

        // 增幅名望
        List<CharacterAmplify> amplifies = characterAmplifyRepository.findAllByCharacterId(character.getId());
        for (CharacterAmplify amplify : amplifies) {
            totalFame.addAndGet(amplify.getFame() != null ? amplify.getFame() : 0);
        }

        // 迷雾名望（根据角色所在跨区获取迷雾名望，暂用跨六作为默认值）
        try {
            String region = character.getServerName();
            totalFame.addAndGet(fogSystemService.getFameByRegion(character.getUserId(), region));
        } catch (Exception e) {
            // 迷雾未激活，忽略
        }

        dto.setFameValue(totalFame.get());

        // DNF同步相关字段
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

    private com.example.demo.model.Character convertToEntity(CharacterDTO dto) {
        com.example.demo.model.Character character = new com.example.demo.model.Character();
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
