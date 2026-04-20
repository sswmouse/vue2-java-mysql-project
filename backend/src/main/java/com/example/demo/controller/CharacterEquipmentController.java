package com.example.demo.controller;

import com.example.demo.dto.CharacterEquipmentDTO;
import com.example.demo.service.CharacterEquipmentService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 角色装备配置Controller
 * 提供角色装备配置相关的REST API接口
 *
 * @author Claude Code
 * @version 1.0
 * @since 2026-04-18
 */
@RestController
@RequestMapping("/api/character-equipment")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
@Slf4j
public class CharacterEquipmentController {

    private final CharacterEquipmentService characterEquipmentService;

    /**
     * 获取角色所有装备配置（包含12套的进度）
     * GET /api/character-equipment/all/{characterId}
     *
     * @param characterId 角色ID
     * @return 角色所有装备配置列表
     */
    @GetMapping(value = "/all/{characterId}", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<?> getAllCharacterEquipment(@PathVariable Long characterId) {
        List<CharacterEquipmentDTO> list = characterEquipmentService.getAllCharacterEquipment(characterId);
        return ResponseEntity.ok(list);
    }

    /**
     * 获取角色当前穿戴的装备配置
     * GET /api/character-equipment/equipped/{characterId}
     *
     * @param characterId 角色ID
     * @return 当前穿戴的装备配置
     */
    @GetMapping(value = "/equipped/{characterId}", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<?> getEquippedEquipment(@PathVariable Long characterId) {
        CharacterEquipmentDTO dto = characterEquipmentService.getEquippedEquipment(characterId);
        return ResponseEntity.ok(dto);
    }

    /**
     * 获取角色单个装备套装配置
     * GET /api/character-equipment/{characterId}/{setId}
     *
     * @param characterId 角色ID
     * @param setId 套装ID
     * @return 装备配置DTO
     */
    @GetMapping(value = "/{characterId}/{setId}", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<?> getCharacterEquipment(@PathVariable Long characterId, @PathVariable Long setId) {
        CharacterEquipmentDTO dto = characterEquipmentService.getCharacterEquipment(characterId, setId);
        return ResponseEntity.ok(dto);
    }

    /**
     * 保存角色装备配置
     * POST /api/character-equipment/{characterId}
     *
     * @param characterId 角色ID
     * @param dto 装备配置DTO
     * @return 保存后的配置
     */
    @PostMapping(value = "/{characterId}", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<?> saveCharacterEquipment(
            @PathVariable Long characterId,
            @RequestBody CharacterEquipmentDTO dto) {
        try {
            CharacterEquipmentDTO saved = characterEquipmentService.saveCharacterEquipment(characterId, dto);
            return ResponseEntity.ok(saved);
        } catch (Exception e) {
            log.error("保存角色装备配置失败", e);
            return ResponseEntity.badRequest().body("保存失败: " + e.getMessage());
        }
    }

    /**
     * 穿戴装备
     * POST /api/character-equipment/{characterId}/equip/{setId}
     *
     * @param characterId 角色ID
     * @param setId 套装ID
     * @return 穿戴后的装备配置
     */
    @PostMapping(value = "/{characterId}/equip/{setId}", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<?> equipSet(@PathVariable Long characterId, @PathVariable Long setId) {
        try {
            CharacterEquipmentDTO saved = characterEquipmentService.equipSet(characterId, setId);
            return ResponseEntity.ok(saved);
        } catch (Exception e) {
            log.error("穿戴装备失败", e);
            return ResponseEntity.badRequest().body("穿戴失败: " + e.getMessage());
        }
    }

    /**
     * 卸下装备
     * POST /api/character-equipment/{characterId}/unequip/{setId}
     *
     * @param characterId 角色ID
     * @param setId 套装ID
     * @return 卸下后的装备配置
     */
    @PostMapping(value = "/{characterId}/unequip/{setId}", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<?> unequipSet(@PathVariable Long characterId, @PathVariable Long setId) {
        try {
            CharacterEquipmentDTO saved = characterEquipmentService.unequipSet(characterId, setId);
            return ResponseEntity.ok(saved);
        } catch (Exception e) {
            log.error("卸下装备失败", e);
            return ResponseEntity.badRequest().body("卸下失败: " + e.getMessage());
        }
    }
}
