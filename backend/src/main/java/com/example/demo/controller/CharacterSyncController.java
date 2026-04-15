package com.example.demo.controller;

import com.example.demo.model.Character;
import com.example.demo.repository.CharacterRepository;
import com.example.demo.service.DnfSyncService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

/**
 * DNF角色同步接口
 */
@RestController
@RequestMapping("/api/character-sync")
@RequiredArgsConstructor
public class CharacterSyncController {

    private final DnfSyncService dnfSyncService;
    private final CharacterRepository characterRepository;

    /**
     * 同步单个角色
     * POST /api/character-sync/sync/{characterId}
     */
    @PostMapping("/sync/{characterId}")
    public ResponseEntity<Map<String, Object>> syncCharacter(
            @PathVariable Long characterId,
            @AuthenticationPrincipal UserDetails userDetails) {

        Map<String, Object> response = new HashMap<>();

        try {
            // 验证角色所属用户
            Character character = characterRepository.findById(characterId)
                    .orElseThrow(() -> new RuntimeException("角色不存在"));

            // 简单的用户验证（实际项目中应该更严格）
            if (userDetails != null) {
                // 获取当前用户ID进行验证
                // 暂时跳过详细验证，直接同步
            }

            Map<String, Object> result = dnfSyncService.syncCharacter(characterId);
            return ResponseEntity.ok(result);

        } catch (Exception e) {
            response.put("success", false);
            response.put("message", e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
    }

    /**
     * 同步用户所有角色
     * POST /api/character-sync/sync-all
     */
    @PostMapping("/sync-all")
    public ResponseEntity<Map<String, Object>> syncAllCharacters(
            @AuthenticationPrincipal UserDetails userDetails) {

        Map<String, Object> response = new HashMap<>();

        try {
            // 从认证信息获取用户ID
            // 需要根据实际的用户表结构来获取
            // 暂时返回错误，后续需要实现
            response.put("success", false);
            response.put("message", "需要实现用户认证获取");
            return ResponseEntity.badRequest().body(response);

        } catch (Exception e) {
            response.put("success", false);
            response.put("message", e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
    }

    /**
     * 根据冒险团名称查询角色
     * GET /api/character-sync/query?serverId=1&adventureName=xxx
     */
    @GetMapping("/query")
    public ResponseEntity<Map<String, Object>> queryByAdventure(
            @RequestParam String serverId,
            @RequestParam String adventureName) {

        try {
            Map<String, Object> result = dnfSyncService.fetchCharactersByAdventure(serverId, adventureName);
            return ResponseEntity.ok(result);

        } catch (Exception e) {
            Map<String, Object> response = new HashMap<>();
            response.put("success", false);
            response.put("message", e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
    }

    /**
     * 获取角色同步状态
     * GET /api/character-sync/status/{characterId}
     */
    @GetMapping("/status/{characterId}")
    public ResponseEntity<Map<String, Object>> getSyncStatus(@PathVariable Long characterId) {

        Map<String, Object> response = new HashMap<>();

        try {
            Character character = characterRepository.findById(characterId)
                    .orElseThrow(() -> new RuntimeException("角色不存在"));

            response.put("success", true);
            response.put("data", Map.of(
                    "characterId", character.getId(),
                    "characterName", character.getCharacterName(),
                    "syncStatus", character.getSyncStatus(),
                    "lastSyncTime", character.getLastSyncTime() != null ?
                            character.getLastSyncTime().toString() : "从未同步",
                    "errorMsg", character.getSyncErrorMsg() != null ?
                            character.getSyncErrorMsg() : ""
            ));

            return ResponseEntity.ok(response);

        } catch (Exception e) {
            response.put("success", false);
            response.put("message", e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
    }
}
