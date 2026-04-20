package com.example.demo.service;

import com.example.demo.dto.CharacterEquipmentDTO;
import com.example.demo.model.CharacterEquipment;
import com.example.demo.repository.CharacterEquipmentRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * 角色装备配置服务实现类
 *
 * @author Claude Code
 * @version 1.0
 * @since 2026-04-18
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class CharacterEquipmentServiceImpl implements CharacterEquipmentService {

    private final CharacterEquipmentRepository characterEquipmentRepository;
    private final ObjectMapper objectMapper;

    @Override
    public List<CharacterEquipmentDTO> getAllCharacterEquipment(Long characterId) {
        List<CharacterEquipment> entities = characterEquipmentRepository.findAllByCharacterId(characterId);
        return entities.stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    public CharacterEquipmentDTO getCharacterEquipment(Long characterId, Long setId) {
        Optional<CharacterEquipment> opt = characterEquipmentRepository.findByCharacterIdAndSetId(characterId, setId);
        if (opt.isEmpty()) {
            return CharacterEquipmentDTO.builder()
                    .setId(setId)
                    .currentPoints(0)
                    .currentStage(null)
                    .isEquipped(false)
                    .items(Collections.emptyList())
                    .build();
        }
        return toDTO(opt.get());
    }

    @Override
    public CharacterEquipmentDTO getEquippedEquipment(Long characterId) {
        Optional<CharacterEquipment> opt = characterEquipmentRepository.findByCharacterIdAndIsEquipped(characterId, true);
        if (opt.isEmpty()) {
            return null;
        }
        return toDTO(opt.get());
    }

    @Override
    @Transactional
    public CharacterEquipmentDTO saveCharacterEquipment(Long characterId, CharacterEquipmentDTO dto) {
        // 查找或创建实体
        CharacterEquipment entity = characterEquipmentRepository
                .findByCharacterIdAndSetId(characterId, dto.getSetId())
                .orElse(CharacterEquipment.builder().characterId(characterId).build());

        // 转换装备配置为JSON
        String configJson = serializeEquipmentConfig(dto.getItems());

        // 更新字段
        entity.setSetId(dto.getSetId());
        entity.setSetName(dto.getSetName());
        entity.setSetIndex(dto.getSetIndex());
        entity.setCurrentPoints(dto.getCurrentPoints());
        entity.setCurrentStage(dto.getCurrentStage());
        entity.setEquipmentConfig(configJson);

        // 如果保存时没有指定isEquipped，保持原值
        if (dto.getIsEquipped() != null) {
            entity.setIsEquipped(dto.getIsEquipped());
        }

        // 保存
        CharacterEquipment saved = characterEquipmentRepository.save(entity);
        return toDTO(saved);
    }

    @Override
    @Transactional
    public CharacterEquipmentDTO equipSet(Long characterId, Long setId) {
        // 清除角色所有装备的穿戴状态（无论记录是否存在都要清除）
        characterEquipmentRepository.clearEquippedByCharacterId(characterId);

        // 查找或创建实体 - 只设置必要的字段，保留已有的进度数据
        CharacterEquipment entity = characterEquipmentRepository
                .findByCharacterIdAndSetId(characterId, setId)
                .orElseGet(() -> CharacterEquipment.builder()
                        .characterId(characterId)
                        .setId(setId)
                        .currentPoints(0)
                        .currentStage(null)
                        .isEquipped(true)
                        .equipmentConfig("[]")
                        .build());

        // 只更新穿戴状态，保留已有的进度数据
        entity.setIsEquipped(true);

        // 保存
        CharacterEquipment saved = characterEquipmentRepository.save(entity);
        return toDTO(saved);
    }

    @Override
    @Transactional
    public CharacterEquipmentDTO unequipSet(Long characterId, Long setId) {
        Optional<CharacterEquipment> opt = characterEquipmentRepository.findByCharacterIdAndSetId(characterId, setId);
        if (opt.isEmpty()) {
            return CharacterEquipmentDTO.builder()
                    .setId(setId)
                    .isEquipped(false)
                    .build();
        }

        CharacterEquipment entity = opt.get();
        entity.setIsEquipped(false);
        CharacterEquipment saved = characterEquipmentRepository.save(entity);
        return toDTO(saved);
    }

    /**
     * 将实体转换为DTO
     */
    private CharacterEquipmentDTO toDTO(CharacterEquipment entity) {
        List<CharacterEquipmentDTO.ItemConfig> items = parseEquipmentConfig(entity.getEquipmentConfig());
        return CharacterEquipmentDTO.builder()
                .setId(entity.getSetId())
                .setName(entity.getSetName())
                .setIndex(entity.getSetIndex())
                .currentPoints(entity.getCurrentPoints())
                .currentStage(entity.getCurrentStage())
                .isEquipped(entity.getIsEquipped() != null ? entity.getIsEquipped() : false)
                .items(items)
                .build();
    }

    /**
     * 解析装备配置JSON
     */
    private List<CharacterEquipmentDTO.ItemConfig> parseEquipmentConfig(String json) {
        if (json == null || json.isEmpty()) {
            return Collections.emptyList();
        }
        try {
            return objectMapper.readValue(json, new TypeReference<List<CharacterEquipmentDTO.ItemConfig>>() {});
        } catch (JsonProcessingException e) {
            log.error("解析装备配置失败: {}", json, e);
            return Collections.emptyList();
        }
    }

    /**
     * 序列化装备配置为JSON
     */
    private String serializeEquipmentConfig(List<CharacterEquipmentDTO.ItemConfig> items) {
        if (items == null || items.isEmpty()) {
            return "[]";
        }
        try {
            return objectMapper.writeValueAsString(items);
        } catch (JsonProcessingException e) {
            log.error("序列化装备配置失败", e);
            return "[]";
        }
    }
}
