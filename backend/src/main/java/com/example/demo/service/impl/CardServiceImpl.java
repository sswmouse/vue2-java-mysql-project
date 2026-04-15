package com.example.demo.service.impl;

import com.example.demo.dto.CardTemplateDTO;
import com.example.demo.dto.CharacterCardDTO;
import com.example.demo.model.CardTemplate;
import com.example.demo.model.Character;
import com.example.demo.model.CharacterCard;
import com.example.demo.repository.CardTemplateRepository;
import com.example.demo.repository.CharacterCardRepository;
import com.example.demo.repository.CharacterRepository;
import com.example.demo.service.CardService;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CardServiceImpl implements CardService {

    private final CardTemplateRepository cardTemplateRepository;
    private final CharacterCardRepository characterCardRepository;
    private final CharacterRepository characterRepository;
    private final ObjectMapper objectMapper;

    @Override
    public List<CardTemplateDTO> getAllTemplates() {
        return cardTemplateRepository.findAll().stream()
                .map(this::toTemplateDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<CharacterCardDTO> getCharacterCards(Long characterId) {
        return characterCardRepository.findByCharacterId(characterId).stream()
                .map(this::toCardDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public CharacterCardDTO createCard(Long characterId, CharacterCardDTO cardDTO) {
        Character character = characterRepository.findById(characterId)
                .orElseThrow(() -> new RuntimeException("角色不存在"));
        CardTemplate template = cardTemplateRepository.findById(cardDTO.getTemplateId())
                .orElseThrow(() -> new RuntimeException("模板不存在"));

        CharacterCard card = new CharacterCard();
        card.setCharacterId(characterId);
        card.setTemplateId(cardDTO.getTemplateId());
        card.setCardName(cardDTO.getCardName());
        card.setIsDefault(cardDTO.getIsDefault() != null && cardDTO.getIsDefault());

        // 生成卡片数据快照
        try {
            String cardData = objectMapper.writeValueAsString(buildCardSnapshot(character));
            card.setCardData(cardData);
        } catch (Exception e) {
            throw new RuntimeException("生成卡片数据失败", e);
        }

        // 如果设为默认，先清除其他默认
        if (card.getIsDefault()) {
            characterCardRepository.clearDefaultByCharacterId(characterId);
        }

        CharacterCard saved = characterCardRepository.save(card);
        return toCardDTO(saved);
    }

    @Override
    @Transactional
    public CharacterCardDTO updateCard(Long cardId, CharacterCardDTO cardDTO) {
        CharacterCard card = characterCardRepository.findById(cardId)
                .orElseThrow(() -> new RuntimeException("卡片不存在"));

        if (cardDTO.getCardName() != null) {
            card.setCardName(cardDTO.getCardName());
        }
        if (cardDTO.getTemplateId() != null) {
            card.setTemplateId(cardDTO.getTemplateId());
        }
        if (cardDTO.getIsDefault() != null && cardDTO.getIsDefault()) {
            characterCardRepository.clearDefaultByCharacterId(card.getCharacterId());
            card.setIsDefault(true);
        }

        CharacterCard saved = characterCardRepository.save(card);
        return toCardDTO(saved);
    }

    @Override
    @Transactional
    public void deleteCard(Long cardId) {
        characterCardRepository.deleteById(cardId);
    }

    @Override
    @Transactional
    public void setDefaultCard(Long cardId) {
        CharacterCard card = characterCardRepository.findById(cardId)
                .orElseThrow(() -> new RuntimeException("卡片不存在"));
        characterCardRepository.clearDefaultByCharacterId(card.getCharacterId());
        card.setIsDefault(true);
        characterCardRepository.save(card);
    }

    private CardTemplateDTO toTemplateDTO(CardTemplate template) {
        CardTemplateDTO dto = new CardTemplateDTO();
        dto.setId(template.getId());
        dto.setTemplateName(template.getTemplateName());
        dto.setTemplateCode(template.getTemplateCode());
        dto.setTemplateStyle(template.getTemplateStyle());
        dto.setPreviewImage(template.getPreviewImage());
        dto.setIsDefault(template.getIsDefault());
        return dto;
    }

    private CharacterCardDTO toCardDTO(CharacterCard card) {
        CharacterCardDTO dto = new CharacterCardDTO();
        dto.setId(card.getId());
        dto.setCharacterId(card.getCharacterId());
        dto.setTemplateId(card.getTemplateId());
        dto.setCardName(card.getCardName());
        dto.setCardData(card.getCardData());
        dto.setIsDefault(card.getIsDefault());
        dto.setCreatedAt(card.getCreatedAt() != null ? card.getCreatedAt().toString() : null);
        dto.setUpdatedAt(card.getUpdatedAt() != null ? card.getUpdatedAt().toString() : null);

        // 查询关联数据
        cardTemplateRepository.findById(card.getTemplateId()).ifPresent(template -> {
            dto.setTemplateName(template.getTemplateName());
            dto.setTemplateCode(template.getTemplateCode());
        });
        characterRepository.findById(card.getCharacterId()).ifPresent(character -> {
            dto.setCharacterName(character.getCharacterName());
        });

        return dto;
    }

    private Map<String, Object> buildCardSnapshot(Character character) {
        Map<String, Object> snapshot = new HashMap<>();
        snapshot.put("characterId", character.getId());
        snapshot.put("characterName", character.getCharacterName());
        snapshot.put("level", character.getLevel());
        snapshot.put("fameValue", character.getFameValue());
        snapshot.put("jobNature", character.getJobNature());
        snapshot.put("jobAttribute", character.getJobAttribute());
        snapshot.put("serverName", character.getServerName());
        snapshot.put("adventureName", character.getAdventureName());
        snapshot.put("avatarUrl", character.getAvatarUrl());
        return snapshot;
    }
}
