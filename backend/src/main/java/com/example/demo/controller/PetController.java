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
}