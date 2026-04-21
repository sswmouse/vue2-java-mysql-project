package com.example.demo.controller;

import com.example.demo.dto.PetDTO;
import com.example.demo.service.PetService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 宠物Controller
 */
@RestController
@RequestMapping("/api/pets")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class PetController {

    private final PetService petService;

    /**
     * 获取所有宠物
     */
    @GetMapping
    public ResponseEntity<List<PetDTO>> getAllPets() {
        List<PetDTO> pets = petService.getAllPets();
        return ResponseEntity.ok(pets);
    }

    /**
     * 根据ID获取宠物
     */
    @GetMapping("/{id}")
    public ResponseEntity<PetDTO> getPetById(@PathVariable Long id) {
        PetDTO pet = petService.getPetById(id);
        if (pet == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(pet);
    }

    /**
     * 根据年份获取宠物
     */
    @GetMapping("/year/{year}")
    public ResponseEntity<List<PetDTO>> getPetsByYear(@PathVariable Integer year) {
        List<PetDTO> pets = petService.getPetsByYear(year);
        return ResponseEntity.ok(pets);
    }

    /**
     * 根据类型获取宠物
     */
    @GetMapping("/type/{type}")
    public ResponseEntity<List<PetDTO>> getPetsByType(@PathVariable String type) {
        List<PetDTO> pets = petService.getPetsByType(type);
        return ResponseEntity.ok(pets);
    }

    /**
     * 获取所有年份
     */
    @GetMapping("/years")
    public ResponseEntity<List<Integer>> getAllYears() {
        List<Integer> years = petService.getAllYears();
        return ResponseEntity.ok(years);
    }

    /**
     * 搜索宠物
     */
    @GetMapping("/search")
    public ResponseEntity<List<PetDTO>> searchPets(@RequestParam String name) {
        List<PetDTO> pets = petService.searchPets(name);
        return ResponseEntity.ok(pets);
    }

    /**
     * 穿戴宠物
     */
    @PostMapping("/{petId}/equip/{characterId}")
    public ResponseEntity<Void> equipPet(
            @PathVariable Long petId,
            @PathVariable Long characterId) {
        petService.equipPet(characterId, petId);
        return ResponseEntity.ok().build();
    }

    /**
     * 卸下宠物
     */
    @DeleteMapping("/{petId}/unequip/{characterId}")
    public ResponseEntity<Void> unequipPet(
            @PathVariable Long petId,
            @PathVariable Long characterId) {
        petService.unequipPet(characterId, petId);
        return ResponseEntity.ok().build();
    }

    /**
     * 获取角色已穿戴的宠物ID列表
     */
    @GetMapping("/equipped/{characterId}")
    public ResponseEntity<List<Long>> getEquippedPetIds(@PathVariable Long characterId) {
        List<Long> equippedIds = petService.getEquippedPetIds(characterId);
        return ResponseEntity.ok(equippedIds);
    }

    /**
     * 创建宠物
     */
    @PostMapping
    public ResponseEntity<PetDTO> createPet(@RequestBody PetDTO petDTO) {
        PetDTO created = petService.createPet(petDTO);
        return ResponseEntity.ok(created);
    }

    /**
     * 更新宠物
     */
    @PutMapping("/{id}")
    public ResponseEntity<PetDTO> updatePet(
            @PathVariable Long id,
            @RequestBody PetDTO petDTO) {
        PetDTO updated = petService.updatePet(id, petDTO);
        return ResponseEntity.ok(updated);
    }

    /**
     * 删除宠物
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePet(@PathVariable Long id) {
        petService.deletePet(id);
        return ResponseEntity.ok().build();
    }
}