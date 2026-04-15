package com.example.demo.controller;

import com.example.demo.dto.*;
import com.example.demo.service.EnhancementService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * 打造系统Controller
 */
@RestController
@RequestMapping("/api/characters/{characterId}/enhancement")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class EnhancementController {

    private final EnhancementService enhancementService;

    /**
     * 获取角色全部打造数据
     */
    @GetMapping
    public ResponseEntity<EnhancementDataDTO> getEnhancementData(@PathVariable Long characterId) {
        EnhancementDataDTO data = enhancementService.getEnhancementData(characterId);
        return ResponseEntity.ok(data);
    }

    /**
     * 更新增幅数据
     */
    @PutMapping("/amplify")
    public ResponseEntity<Void> updateAmplify(
            @PathVariable Long characterId,
            @RequestBody AmplifyDTO amplifyDTO) {
        enhancementService.updateAmplify(characterId, amplifyDTO);
        return ResponseEntity.ok().build();
    }

    /**
     * 更新附魔数据
     */
    @PutMapping("/enchant")
    public ResponseEntity<Void> updateEnchant(
            @PathVariable Long characterId,
            @RequestBody EnchantDTO enchantDTO) {
        enhancementService.updateEnchant(characterId, enchantDTO);
        return ResponseEntity.ok().build();
    }

    /**
     * 更新称号数据
     */
    @PutMapping("/title")
    public ResponseEntity<Void> updateTitle(
            @PathVariable Long characterId,
            @RequestBody TitleDTO titleDTO) {
        enhancementService.updateTitle(characterId, titleDTO);
        return ResponseEntity.ok().build();
    }

    /**
     * 更新宠物数据
     */
    @PutMapping("/pet")
    public ResponseEntity<Void> updatePet(
            @PathVariable Long characterId,
            @RequestBody PetDTO petDTO) {
        enhancementService.updatePet(characterId, petDTO);
        return ResponseEntity.ok().build();
    }

    /**
     * 更新装备数据
     */
    @PutMapping("/equipment")
    public ResponseEntity<Void> updateEquipment(
            @PathVariable Long characterId,
            @RequestBody EquipmentDTO equipmentDTO) {
        enhancementService.updateEquipment(characterId, equipmentDTO);
        return ResponseEntity.ok().build();
    }

    /**
     * 更新徽章数据
     */
    @PutMapping("/emblem")
    public ResponseEntity<Void> updateEmblem(
            @PathVariable Long characterId,
            @RequestBody EmblemDTO emblemDTO) {
        enhancementService.updateEmblem(characterId, emblemDTO);
        return ResponseEntity.ok().build();
    }
}
