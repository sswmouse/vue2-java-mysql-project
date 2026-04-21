package com.example.demo.service.impl;

import com.example.demo.dto.TitleDTO;
import com.example.demo.model.CharacterTitle;
import com.example.demo.model.Title;
import com.example.demo.repository.CharacterTitleRepository;
import com.example.demo.repository.TitleRepository;
import com.example.demo.service.TitleService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 称号Service实现
 */
@Service
@RequiredArgsConstructor
public class TitleServiceImpl implements TitleService {

    private final TitleRepository titleRepository;
    private final CharacterTitleRepository characterTitleRepository;

    @Override
    public List<TitleDTO> getAllTitles() {
        return titleRepository.findAllOrderByYearAndFameDesc().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public TitleDTO getTitleById(Long id) {
        return titleRepository.findById(id)
                .map(this::convertToDTO)
                .orElse(null);
    }

    @Override
    public List<TitleDTO> getTitlesByYear(Integer year) {
        return titleRepository.findByYear(year).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<TitleDTO> getTitlesByType(String type) {
        return titleRepository.findByType(type).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<Integer> getAllYears() {
        return titleRepository.findDistinctYears();
    }

    @Override
    public List<TitleDTO> searchTitles(String name) {
        return titleRepository.findByNameContaining(name).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public void equipTitle(Long characterId, Long titleId) {
        // 先卸下该角色之前穿戴的称号
        characterTitleRepository.deleteByCharacterId(characterId);
        // 穿戴新称号
        CharacterTitle characterTitle = new CharacterTitle();
        characterTitle.setCharacterId(characterId);
        characterTitle.setTitleId(titleId);
        characterTitle.setIsEquipped(true);
        characterTitleRepository.save(characterTitle);
    }

    @Override
    @Transactional
    public void unequipTitle(Long characterId, Long titleId) {
        characterTitleRepository.deleteByCharacterIdAndTitleId(characterId, titleId);
    }

    @Override
    public List<Long> getEquippedTitleIds(Long characterId) {
        return characterTitleRepository.findByCharacterId(characterId).stream()
                .map(CharacterTitle::getTitleId)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public TitleDTO createTitle(TitleDTO titleDTO) {
        Title title = new Title();
        updateTitleFromDTO(title, titleDTO);
        Title saved = titleRepository.save(title);
        return convertToDTO(saved);
    }

    @Override
    @Transactional
    public TitleDTO updateTitle(Long id, TitleDTO titleDTO) {
        Title title = titleRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("称号不存在"));
        updateTitleFromDTO(title, titleDTO);
        Title saved = titleRepository.save(title);
        return convertToDTO(saved);
    }

    @Override
    @Transactional
    public void deleteTitle(Long id) {
        Title title = titleRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("称号不存在"));
        titleRepository.delete(title);
    }

    private void updateTitleFromDTO(Title title, TitleDTO dto) {
        if (dto.getName() != null) title.setName(dto.getName());
        if (dto.getYear() != null) title.setYear(dto.getYear());
        if (dto.getType() != null) title.setType(dto.getType());
        if (dto.getSubType() != null) title.setSubType(dto.getSubType());
        if (dto.getFourStats() != null) title.setFourStats(dto.getFourStats());
        if (dto.getThreeAttack() != null) title.setThreeAttack(dto.getThreeAttack());
        if (dto.getCritRate() != null) title.setCritRate(dto.getCritRate());
        if (dto.getElementalBonus() != null) title.setElementalBonus(dto.getElementalBonus());
        if (dto.getAttackSpeed() != null) title.setAttackSpeed(dto.getAttackSpeed());
        if (dto.getAttackStrength() != null) title.setAttackStrength(dto.getAttackStrength());
        if (dto.getSkillBonus() != null) title.setSkillBonus(dto.getSkillBonus());
        if (dto.getSkillBonusLevel() != null) title.setSkillBonusLevel(dto.getSkillBonusLevel());
        if (dto.getFame() != null) title.setFame(dto.getFame());
        if (dto.getDescription() != null) title.setDescription(dto.getDescription());
        if (dto.getIsActive() != null) title.setIsActive(dto.getIsActive());
    }

    @Override
    public TitleDTO convertToDTO(Title title) {
        TitleDTO dto = new TitleDTO();
        dto.setId(title.getId());
        dto.setName(title.getName());
        dto.setYear(title.getYear());
        dto.setType(title.getType());
        dto.setSubType(title.getSubType());
        dto.setFourStats(title.getFourStats());
        dto.setThreeAttack(title.getThreeAttack());
        dto.setCritRate(title.getCritRate());
        dto.setElementalBonus(title.getElementalBonus());
        dto.setAttackSpeed(title.getAttackSpeed());
        dto.setAttackStrength(title.getAttackStrength());
        dto.setSkillBonus(title.getSkillBonus());
        dto.setSkillBonusLevel(title.getSkillBonusLevel());
        dto.setFame(title.getFame());
        dto.setDescription(title.getDescription());
        dto.setIsActive(title.getIsActive());
        return dto;
    }
}