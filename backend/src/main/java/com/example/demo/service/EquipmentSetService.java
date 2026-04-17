package com.example.demo.service;

import com.example.demo.dto.EquipmentSetDetailDTO;
import com.example.demo.dto.EquipmentSetDTO;
import com.example.demo.model.EquipmentSet;

import java.util.List;

/**
 * 装备套装服务接口
 *
 * @author Claude Code
 * @version 1.0
 * @since 2026-04-17
 */
public interface EquipmentSetService {

    /**
     * 获取所有套装（按序号排序）
     */
    List<EquipmentSet> getAllSets();

    /**
     * 根据序号获取套装
     */
    EquipmentSet getSetByIndex(Integer indexNum);

    /**
     * 根据ID获取套装
     */
    EquipmentSet getSetById(Long id);

    /**
     * 初始化12套装备数据
     */
    List<EquipmentSet> initEquipmentSets();

    /**
     * 获取套装详情（包含装备列表和阶段效果）
     * @param setId 套装ID
     * @param itemRarities 每件装备的品级 Map<slotName, rarity>
     * @return 套装详情DTO
     */
    EquipmentSetDetailDTO getSetWithItems(Long setId, java.util.Map<String, String> itemRarities);

    /**
     * 获取套装列表DTO（包含当前积分和阶段信息）
     * @param itemRarities 每件装备的品级 Map<setId, Map<slotName, rarity>>
     * @return 套装列表DTO
     */
    List<EquipmentSetDTO> getAllSetsWithPoints(java.util.Map<Long, java.util.Map<String, String>> itemRarities);
}
