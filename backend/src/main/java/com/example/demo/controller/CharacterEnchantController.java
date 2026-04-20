package com.example.demo.controller;

import com.example.demo.dto.CharacterEnchantDTO;
import com.example.demo.service.CharacterEnchantService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 角色附魔配置控制器
 *
 * @author Claude Code
 * @version 1.0
 * @since 2026-04-20
 */
@RestController
@RequestMapping("/api/character-enchant")
public class CharacterEnchantController {

    private final CharacterEnchantService service;

    public CharacterEnchantController(CharacterEnchantService service) {
        this.service = service;
    }

    /**
     * 获取角色所有附魔配置
     */
    @GetMapping("/{characterId}")
    public ResponseEntity<List<CharacterEnchantDTO>> getAllByCharacterId(@PathVariable Long characterId) {
        return ResponseEntity.ok(service.getAllByCharacterId(characterId));
    }

    /**
     * 获取角色指定部位的附魔配置
     */
    @GetMapping("/{characterId}/{equipmentPart}")
    public ResponseEntity<CharacterEnchantDTO> getByPart(
            @PathVariable Long characterId,
            @PathVariable String equipmentPart) {
        CharacterEnchantDTO dto = service.getByCharacterIdAndPart(characterId, equipmentPart);
        if (dto == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(dto);
    }

    /**
     * 保存角色附魔配置
     */
    @PostMapping("/{characterId}")
    public ResponseEntity<CharacterEnchantDTO> save(
            @PathVariable Long characterId,
            @RequestBody CharacterEnchantDTO dto) {
        return ResponseEntity.ok(service.save(characterId, dto));
    }

    /**
     * 批量保存角色附魔配置
     */
    @PostMapping("/{characterId}/batch")
    public ResponseEntity<List<CharacterEnchantDTO>> saveAll(
            @PathVariable Long characterId,
            @RequestBody List<CharacterEnchantDTO> dtos) {
        return ResponseEntity.ok(service.saveAll(characterId, dtos));
    }

    /**
     * 删除角色指定部位的附魔配置
     */
    @DeleteMapping("/{characterId}/{equipmentPart}")
    public ResponseEntity<Void> delete(
            @PathVariable Long characterId,
            @PathVariable String equipmentPart) {
        service.delete(characterId, equipmentPart);
        return ResponseEntity.ok().build();
    }
}
