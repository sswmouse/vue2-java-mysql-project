package com.example.demo.controller;

import com.example.demo.dto.CharacterAmplifyDTO;
import com.example.demo.service.CharacterAmplifyService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 角色增幅配置Controller
 */
@RestController
@RequestMapping("/api/character-amplify")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class CharacterAmplifyController {

    private final CharacterAmplifyService characterAmplifyService;

    /**
     * 获取角色所有增幅配置
     */
    @GetMapping("/{characterId}")
    public ResponseEntity<List<CharacterAmplifyDTO>> getByCharacterId(@PathVariable Long characterId) {
        List<CharacterAmplifyDTO> amplifies = characterAmplifyService.getByCharacterId(characterId);
        return ResponseEntity.ok(amplifies);
    }

    /**
     * 保存或更新增幅配置
     */
    @PostMapping("/{characterId}")
    public ResponseEntity<CharacterAmplifyDTO> save(
            @PathVariable Long characterId,
            @RequestBody CharacterAmplifyDTO dto) {
        CharacterAmplifyDTO result = characterAmplifyService.save(characterId, dto);
        return ResponseEntity.ok(result);
    }

    /**
     * 删除角色指定部位的增幅配置
     */
    @DeleteMapping("/{characterId}/{equipmentPart}")
    public ResponseEntity<Void> deleteByPart(
            @PathVariable Long characterId,
            @PathVariable String equipmentPart) {
        characterAmplifyService.deleteByPart(characterId, equipmentPart);
        return ResponseEntity.ok().build();
    }

    /**
     * 批量保存增幅配置
     */
    @PostMapping("/{characterId}/batch")
    public ResponseEntity<List<CharacterAmplifyDTO>> batchSave(
            @PathVariable Long characterId,
            @RequestBody List<CharacterAmplifyDTO> dtos) {
        List<CharacterAmplifyDTO> results = characterAmplifyService.batchSave(characterId, dtos);
        return ResponseEntity.ok(results);
    }
}
