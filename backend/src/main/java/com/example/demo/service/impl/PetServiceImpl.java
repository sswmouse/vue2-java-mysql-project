package com.example.demo.service.impl;

import com.example.demo.dto.PetDTO;
import com.example.demo.model.Pet;
import com.example.demo.repository.PetRepository;
import com.example.demo.service.PetService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 宠物Service实现
 */
@Service
@RequiredArgsConstructor
public class PetServiceImpl implements PetService {

    private final PetRepository petRepository;

    @Override
    public List<PetDTO> getAllPets() {
        return petRepository.findAllOrderByYearAndFameDesc().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public PetDTO getPetById(Long id) {
        return petRepository.findById(id)
                .map(this::convertToDTO)
                .orElse(null);
    }

    @Override
    public List<PetDTO> getPetsByYear(Integer year) {
        return petRepository.findByYear(year).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<PetDTO> getPetsByType(String type) {
        return petRepository.findByType(type).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<Integer> getAllYears() {
        return petRepository.findDistinctYears();
    }

    @Override
    public List<PetDTO> searchPets(String name) {
        return petRepository.findByNameContaining(name).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public PetDTO convertToDTO(Pet pet) {
        PetDTO dto = new PetDTO();
        dto.setId(pet.getId());
        dto.setName(pet.getName());
        dto.setYear(pet.getYear());
        dto.setType(pet.getType());
        dto.setFourStats(pet.getFourStats());
        dto.setFourStatsAlt(pet.getFourStatsAlt());
        dto.setCritRate(pet.getCritRate());
        dto.setElementalBonus(pet.getElementalBonus());
        dto.setAttackSpeed(pet.getAttackSpeed());
        dto.setMpBonus(pet.getMpBonus());
        dto.setSkillLevelRange(pet.getSkillLevelRange());
        dto.setGainBonus(pet.getGainBonus());
        dto.setAttackStrength(pet.getAttackStrength());
        dto.setWhiteDamage(pet.getWhiteDamage());
        dto.setExtraYellow(pet.getExtraYellow());
        dto.setThreeAttack(pet.getThreeAttack());
        dto.setCdReduction(pet.getCdReduction());
        dto.setFame(pet.getFame());
        dto.setDescription(pet.getDescription());
        dto.setIsActive(pet.getIsActive());
        return dto;
    }
}