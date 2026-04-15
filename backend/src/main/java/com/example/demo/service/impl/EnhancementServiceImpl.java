package com.example.demo.service.impl;

import com.example.demo.dto.*;
import com.example.demo.model.Character;
import com.example.demo.repository.CharacterRepository;
import com.example.demo.service.EnhancementService;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class EnhancementServiceImpl implements EnhancementService {

    private final CharacterRepository characterRepository;
    private final ObjectMapper objectMapper;

    @Override
    public EnhancementDataDTO getEnhancementData(Long characterId) {
        Character character = characterRepository.findById(characterId)
                .orElseThrow(() -> new RuntimeException("角色不存在"));

        EnhancementDataDTO dataDTO = new EnhancementDataDTO();

        try {
            if (character.getAmplifyData() != null) {
                dataDTO.setAmplify(objectMapper.readValue(character.getAmplifyData(), AmplifyDTO.class));
            }
            if (character.getEnchantData() != null) {
                dataDTO.setEnchant(objectMapper.readValue(character.getEnchantData(), EnchantDTO.class));
            }
            if (character.getTitleData() != null) {
                dataDTO.setTitle(objectMapper.readValue(character.getTitleData(), TitleDTO.class));
            }
            if (character.getPetData() != null) {
                dataDTO.setPet(objectMapper.readValue(character.getPetData(), PetDTO.class));
            }
            if (character.getEquipmentData() != null) {
                dataDTO.setEquipment(objectMapper.readValue(character.getEquipmentData(), EquipmentDTO.class));
            }
            if (character.getEmblemData() != null) {
                dataDTO.setEmblem(objectMapper.readValue(character.getEmblemData(), EmblemDTO.class));
            }
        } catch (Exception e) {
            throw new RuntimeException("解析打造数据失败", e);
        }

        return dataDTO;
    }

    @Override
    @Transactional
    public void updateAmplify(Long characterId, AmplifyDTO amplifyDTO) {
        Character character = characterRepository.findById(characterId)
                .orElseThrow(() -> new RuntimeException("角色不存在"));
        try {
            character.setAmplifyData(objectMapper.writeValueAsString(amplifyDTO));
            characterRepository.save(character);
        } catch (Exception e) {
            throw new RuntimeException("保存增幅数据失败", e);
        }
    }

    @Override
    @Transactional
    public void updateEnchant(Long characterId, EnchantDTO enchantDTO) {
        Character character = characterRepository.findById(characterId)
                .orElseThrow(() -> new RuntimeException("角色不存在"));
        try {
            character.setEnchantData(objectMapper.writeValueAsString(enchantDTO));
            characterRepository.save(character);
        } catch (Exception e) {
            throw new RuntimeException("保存附魔数据失败", e);
        }
    }

    @Override
    @Transactional
    public void updateTitle(Long characterId, TitleDTO titleDTO) {
        Character character = characterRepository.findById(characterId)
                .orElseThrow(() -> new RuntimeException("角色不存在"));
        try {
            character.setTitleData(objectMapper.writeValueAsString(titleDTO));
            characterRepository.save(character);
        } catch (Exception e) {
            throw new RuntimeException("保存称号数据失败", e);
        }
    }

    @Override
    @Transactional
    public void updatePet(Long characterId, PetDTO petDTO) {
        Character character = characterRepository.findById(characterId)
                .orElseThrow(() -> new RuntimeException("角色不存在"));
        try {
            character.setPetData(objectMapper.writeValueAsString(petDTO));
            characterRepository.save(character);
        } catch (Exception e) {
            throw new RuntimeException("保存宠物数据失败", e);
        }
    }

    @Override
    @Transactional
    public void updateEquipment(Long characterId, EquipmentDTO equipmentDTO) {
        Character character = characterRepository.findById(characterId)
                .orElseThrow(() -> new RuntimeException("角色不存在"));
        try {
            character.setEquipmentData(objectMapper.writeValueAsString(equipmentDTO));
            characterRepository.save(character);
        } catch (Exception e) {
            throw new RuntimeException("保存装备数据失败", e);
        }
    }

    @Override
    @Transactional
    public void updateEmblem(Long characterId, EmblemDTO emblemDTO) {
        Character character = characterRepository.findById(characterId)
                .orElseThrow(() -> new RuntimeException("角色不存在"));
        try {
            character.setEmblemData(objectMapper.writeValueAsString(emblemDTO));
            characterRepository.save(character);
        } catch (Exception e) {
            throw new RuntimeException("保存徽章数据失败", e);
        }
    }
}
