package com.example.demo.service.impl;

import com.example.demo.dto.CharacterEnchantDTO;
import com.example.demo.model.CharacterEnchant;
import com.example.demo.repository.CharacterEnchantRepository;
import com.example.demo.service.CharacterEnchantService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 角色附魔配置服务实现类
 *
 * @author Claude Code
 * @version 1.0
 * @since 2026-04-20
 */
@Service
@RequiredArgsConstructor
public class CharacterEnchantServiceImpl implements CharacterEnchantService {

    private final CharacterEnchantRepository repository;

    @Override
    public List<CharacterEnchantDTO> getAllByCharacterId(Long characterId) {
        return repository.findAllByCharacterId(characterId)
                .stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    public CharacterEnchantDTO getByCharacterIdAndPart(Long characterId, String equipmentPart) {
        return repository.findByCharacterIdAndEquipmentPart(characterId, equipmentPart)
                .map(this::toDTO)
                .orElse(null);
    }

    @Override
    @Transactional
    public CharacterEnchantDTO save(Long characterId, CharacterEnchantDTO dto) {
        CharacterEnchant entity = repository.findByCharacterIdAndEquipmentPart(characterId, dto.getEquipmentPart())
                .orElse(CharacterEnchant.builder().characterId(characterId).build());

        entity.setEquipmentPart(dto.getEquipmentPart());
        entity.setEnchantOptionId(dto.getEnchantOptionId());
        entity.setEnchantName(dto.getEnchantName());
        entity.setAttributeValue(dto.getAttributeValue());
        entity.setPackageType(dto.getPackageType());
        entity.setFame(dto.getFame());

        return toDTO(repository.save(entity));
    }

    @Override
    @Transactional
    public List<CharacterEnchantDTO> saveAll(Long characterId, List<CharacterEnchantDTO> dtos) {
        return dtos.stream()
                .map(dto -> save(characterId, dto))
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public void delete(Long characterId, String equipmentPart) {
        repository.deleteByCharacterIdAndEquipmentPart(characterId, equipmentPart);
    }

    @Override
    @Transactional
    public void deleteAllByCharacterId(Long characterId) {
        repository.deleteAllByCharacterId(characterId);
    }

    private CharacterEnchantDTO toDTO(CharacterEnchant entity) {
        return CharacterEnchantDTO.builder()
                .id(entity.getId())
                .characterId(entity.getCharacterId())
                .equipmentPart(entity.getEquipmentPart())
                .enchantOptionId(entity.getEnchantOptionId())
                .enchantName(entity.getEnchantName())
                .attributeValue(entity.getAttributeValue())
                .packageType(entity.getPackageType())
                .fame(entity.getFame())
                .build();
    }
}
