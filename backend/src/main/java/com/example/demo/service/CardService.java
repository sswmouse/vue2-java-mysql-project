package com.example.demo.service;

import com.example.demo.dto.CardTemplateDTO;
import com.example.demo.dto.CharacterCardDTO;
import java.util.List;

/**
 * 卡片系统服务接口
 */
public interface CardService {

    /**
     * 获取所有卡片模板
     */
    List<CardTemplateDTO> getAllTemplates();

    /**
     * 获取角色的所有卡片
     */
    List<CharacterCardDTO> getCharacterCards(Long characterId);

    /**
     * 创建卡片
     */
    CharacterCardDTO createCard(Long characterId, CharacterCardDTO cardDTO);

    /**
     * 更新卡片
     */
    CharacterCardDTO updateCard(Long cardId, CharacterCardDTO cardDTO);

    /**
     * 删除卡片
     */
    void deleteCard(Long cardId);

    /**
     * 设为默认卡片
     */
    void setDefaultCard(Long cardId);
}
