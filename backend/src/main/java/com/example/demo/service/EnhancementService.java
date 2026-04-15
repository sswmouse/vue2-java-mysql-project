package com.example.demo.service;

import com.example.demo.dto.*;

/**
 * 打造系统服务接口
 */
public interface EnhancementService {

    /**
     * 获取角色全部打造数据
     */
    EnhancementDataDTO getEnhancementData(Long characterId);

    /**
     * 更新增幅数据
     */
    void updateAmplify(Long characterId, AmplifyDTO amplifyDTO);

    /**
     * 更新附魔数据
     */
    void updateEnchant(Long characterId, EnchantDTO enchantDTO);

    /**
     * 更新称号数据
     */
    void updateTitle(Long characterId, TitleDTO titleDTO);

    /**
     * 更新宠物数据
     */
    void updatePet(Long characterId, PetDTO petDTO);

    /**
     * 更新装备数据
     */
    void updateEquipment(Long characterId, EquipmentDTO equipmentDTO);

    /**
     * 更新徽章数据
     */
    void updateEmblem(Long characterId, EmblemDTO emblemDTO);
}
