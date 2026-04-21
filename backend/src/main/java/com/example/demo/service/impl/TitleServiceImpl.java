package com.example.demo.service.impl;

import com.example.demo.dto.TitleDTO;
import com.example.demo.model.Title;
import com.example.demo.repository.TitleRepository;
import com.example.demo.service.TitleService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 称号Service实现
 */
@Service
@RequiredArgsConstructor
public class TitleServiceImpl implements TitleService {

    private final TitleRepository titleRepository;

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