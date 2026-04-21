package com.example.demo.service.impl;

import com.example.demo.dto.CharacterAmplifyDTO;
import com.example.demo.model.CharacterAmplify;
import com.example.demo.repository.CharacterAmplifyRepository;
import com.example.demo.service.CharacterAmplifyService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 角色增幅配置服务实现类
 */
@Service
@RequiredArgsConstructor
@Transactional
public class CharacterAmplifyServiceImpl implements CharacterAmplifyService {

    private final CharacterAmplifyRepository characterAmplifyRepository;

    /**
     * 增幅名望加成表
     * 增幅等级 -> { 武器名望, 防具/首饰/特殊装备名望 }
     */
    private static final int[][] FAME_TABLE = {
        {0, 0},      // 0级占位
        {13, 5},     // +1
        {26, 9},     // +2
        {38, 13},    // +3
        {51, 17},    // +4
        {64, 21},    // +5
        {76, 25},    // +6
        {89, 29},    // +7
        {152, 49},   // +8
        {171, 55},   // +9
        {240, 77},   // +10
        {472, 152},  // +11
        {757, 243},  // +12
        {820, 263},  // +13
        {883, 284},  // +14
        {947, 304}   // +15
    };

    @Override
    @Transactional(readOnly = true)
    public List<CharacterAmplifyDTO> getByCharacterId(Long characterId) {
        List<CharacterAmplify> amplifies = characterAmplifyRepository.findAllByCharacterId(characterId);
        return amplifies.stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    public CharacterAmplifyDTO save(Long characterId, CharacterAmplifyDTO dto) {
        // 检查是否已存在
        CharacterAmplify amplify = characterAmplifyRepository
                .findByCharacterIdAndEquipmentPart(characterId, dto.getEquipmentPart())
                .orElse(CharacterAmplify.builder()
                        .characterId(characterId)
                        .equipmentPart(dto.getEquipmentPart())
                        .build());

        // 更新数据
        amplify.setAmplifyType(dto.getAmplifyType());
        amplify.setAmplifyLevel(dto.getAmplifyLevel());

        // 计算名望
        int fame = calculateFame(dto.getAmplifyLevel(), dto.getEquipmentPart());
        amplify.setFame(fame);

        // 保存
        amplify = characterAmplifyRepository.save(amplify);
        return toDTO(amplify);
    }

    @Override
    public void deleteByPart(Long characterId, String equipmentPart) {
        characterAmplifyRepository.deleteByCharacterIdAndEquipmentPart(characterId, equipmentPart);
    }

    @Override
    public List<CharacterAmplifyDTO> batchSave(Long characterId, List<CharacterAmplifyDTO> dtos) {
        return dtos.stream()
                .map(dto -> save(characterId, dto))
                .collect(Collectors.toList());
    }

    /**
     * 根据增幅等级和部位类型计算名望
     */
    private int calculateFame(int level, String part) {
        if (level < 1 || level > 15) {
            return 0;
        }
        // 武器名望单独计算
        if ("武器".equals(part)) {
            return FAME_TABLE[level][0];
        }
        // 其他装备使用同一套名望
        return FAME_TABLE[level][1];
    }

    /**
     * 实体转DTO
     */
    private CharacterAmplifyDTO toDTO(CharacterAmplify amplify) {
        return CharacterAmplifyDTO.builder()
                .id(amplify.getId())
                .characterId(amplify.getCharacterId())
                .equipmentPart(amplify.getEquipmentPart())
                .amplifyType(amplify.getAmplifyType())
                .amplifyLevel(amplify.getAmplifyLevel())
                .fame(amplify.getFame())
                .build();
    }
}
