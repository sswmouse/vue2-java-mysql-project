package com.example.demo.controller;

import com.example.demo.dto.CardTemplateDTO;
import com.example.demo.dto.CharacterCardDTO;
import com.example.demo.service.CardService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

/**
 * 卡片系统Controller
 */
@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class CardController {

    private final CardService cardService;

    /**
     * 获取所有卡片模板
     */
    @GetMapping("/cards/templates")
    public ResponseEntity<List<CardTemplateDTO>> getAllTemplates() {
        List<CardTemplateDTO> templates = cardService.getAllTemplates();
        return ResponseEntity.ok(templates);
    }

    /**
     * 获取角色的所有卡片
     */
    @GetMapping("/characters/{characterId}/cards")
    public ResponseEntity<List<CharacterCardDTO>> getCharacterCards(@PathVariable Long characterId) {
        List<CharacterCardDTO> cards = cardService.getCharacterCards(characterId);
        return ResponseEntity.ok(cards);
    }

    /**
     * 创建卡片
     */
    @PostMapping("/characters/{characterId}/cards")
    public ResponseEntity<CharacterCardDTO> createCard(
            @PathVariable Long characterId,
            @RequestBody CharacterCardDTO cardDTO) {
        CharacterCardDTO created = cardService.createCard(characterId, cardDTO);
        return ResponseEntity.ok(created);
    }

    /**
     * 更新卡片
     */
    @PutMapping("/cards/{cardId}")
    public ResponseEntity<CharacterCardDTO> updateCard(
            @PathVariable Long cardId,
            @RequestBody CharacterCardDTO cardDTO) {
        CharacterCardDTO updated = cardService.updateCard(cardId, cardDTO);
        return ResponseEntity.ok(updated);
    }

    /**
     * 删除卡片
     */
    @DeleteMapping("/cards/{cardId}")
    public ResponseEntity<Void> deleteCard(@PathVariable Long cardId) {
        cardService.deleteCard(cardId);
        return ResponseEntity.noContent().build();
    }

    /**
     * 设为默认卡片
     */
    @PutMapping("/cards/{cardId}/default")
    public ResponseEntity<Void> setDefaultCard(@PathVariable Long cardId) {
        cardService.setDefaultCard(cardId);
        return ResponseEntity.ok().build();
    }
}
