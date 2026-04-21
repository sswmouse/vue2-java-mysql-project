package com.example.demo.service.impl;

import com.example.demo.dto.PetDTO;
import com.example.demo.model.CharacterPet;
import com.example.demo.model.Pet;
import com.example.demo.repository.CharacterPetRepository;
import com.example.demo.repository.PetRepository;
import com.example.demo.service.PetService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 宠物Service实现
 */
@Service
@RequiredArgsConstructor
public class PetServiceImpl implements PetService {

    private final PetRepository petRepository;
    private final CharacterPetRepository characterPetRepository;

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
    @Transactional
    public void equipPet(Long characterId, Long petId) {
        // 先卸下该角色之前穿戴的宠物
        characterPetRepository.deleteByCharacterId(characterId);
        // 穿戴新宠物
        CharacterPet characterPet = new CharacterPet();
        characterPet.setCharacterId(characterId);
        characterPet.setPetId(petId);
        characterPet.setIsEquipped(true);
        characterPetRepository.save(characterPet);
    }

    @Override
    @Transactional
    public void unequipPet(Long characterId, Long petId) {
        characterPetRepository.deleteByCharacterIdAndPetId(characterId, petId);
    }

    @Override
    public List<Long> getEquippedPetIds(Long characterId) {
        return characterPetRepository.findByCharacterId(characterId).stream()
                .map(CharacterPet::getPetId)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public PetDTO createPet(PetDTO petDTO) {
        Pet pet = new Pet();
        updatePetFromDTO(pet, petDTO);
        Pet saved = petRepository.save(pet);
        return convertToDTO(saved);
    }

    @Override
    @Transactional
    public PetDTO updatePet(Long id, PetDTO petDTO) {
        Pet pet = petRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("宠物不存在"));
        updatePetFromDTO(pet, petDTO);
        Pet saved = petRepository.save(pet);
        return convertToDTO(saved);
    }

    @Override
    @Transactional
    public void deletePet(Long id) {
        Pet pet = petRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("宠物不存在"));
        petRepository.delete(pet);
    }

    private void updatePetFromDTO(Pet pet, PetDTO dto) {
        if (dto.getName() != null) pet.setName(dto.getName());
        if (dto.getYear() != null) pet.setYear(dto.getYear());
        if (dto.getType() != null) pet.setType(dto.getType());
        if (dto.getFourStats() != null) pet.setFourStats(dto.getFourStats());
        if (dto.getFourStatsAlt() != null) pet.setFourStatsAlt(dto.getFourStatsAlt());
        if (dto.getCritRate() != null) pet.setCritRate(dto.getCritRate());
        if (dto.getElementalBonus() != null) pet.setElementalBonus(dto.getElementalBonus());
        if (dto.getAttackSpeed() != null) pet.setAttackSpeed(dto.getAttackSpeed());
        if (dto.getMpBonus() != null) pet.setMpBonus(dto.getMpBonus());
        if (dto.getSkillLevelRange() != null) pet.setSkillLevelRange(dto.getSkillLevelRange());
        if (dto.getGainBonus() != null) pet.setGainBonus(dto.getGainBonus());
        if (dto.getAttackStrength() != null) pet.setAttackStrength(dto.getAttackStrength());
        if (dto.getWhiteDamage() != null) pet.setWhiteDamage(dto.getWhiteDamage());
        if (dto.getExtraYellow() != null) pet.setExtraYellow(dto.getExtraYellow());
        if (dto.getThreeAttack() != null) pet.setThreeAttack(dto.getThreeAttack());
        if (dto.getCdReduction() != null) pet.setCdReduction(dto.getCdReduction());
        if (dto.getFame() != null) pet.setFame(dto.getFame());
        if (dto.getDescription() != null) pet.setDescription(dto.getDescription());
        if (dto.getIsActive() != null) pet.setIsActive(dto.getIsActive());
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