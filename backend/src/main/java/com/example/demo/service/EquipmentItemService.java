package com.example.demo.service;

import com.example.demo.model.EquipmentItem;

import java.util.List;
import java.util.Map;

/**
 * 装备详情服务接口
 * 提供装备详情相关的业务操作方法
 *
 * @author Claude Code
 * @version 1.0
 * @since 2026-04-17
 */
public interface EquipmentItemService {

    /**
     * 根据套装ID获取所有装备
     */
    List<EquipmentItem> getItemsBySetId(Long setId);

    /**
     * 根据套装ID和部位类型获取装备
     */
    List<EquipmentItem> getItemsBySetIdAndSlotType(Long setId, String slotType);

    /**
     * 根据套装ID和部位名称获取装备
     */
    EquipmentItem getItemBySetIdAndSlotName(Long setId, String slotName);

    /**
     * 根据ID获取装备
     */
    EquipmentItem getItemById(Long id);

    /**
     * 保存装备
     */
    EquipmentItem saveItem(EquipmentItem item);

    /**
     * 批量保存装备
     */
    List<EquipmentItem> saveItems(List<EquipmentItem> items);

    /**
     * 删除装备
     */
    void deleteItem(Long id);

    /**
     * 根据套装ID删除所有装备
     */
    void deleteItemsBySetId(Long setId);

    /**
     * 获取指定品级的积分
     * @param item 装备
     * @param rarity 品级
     * @return 积分
     */
    Integer getPointsByRarity(EquipmentItem item, String rarity);

    /**
     * 获取指定品级的名望
     * @param item 装备
     * @param rarity 品级
     * @return 名望
     */
    Integer getFameByRarity(EquipmentItem item, String rarity);

    /**
     * 初始化12套装备的装备详情数据
     */
    void initEquipmentItems();

    /**
     * 计算套装总积分
     * @param setId 套装ID
     * @param itemRarities 每件装备的品级 Map<slotName, rarity>
     * @return 总积分
     */
    Integer calculateTotalPoints(Long setId, Map<String, String> itemRarities);

    /**
     * 计算套装总名望
     * @param setId 套装ID
     * @param itemRarities 每件装备的品级 Map<slotName, rarity>
     * @return 总名望
     */
    Integer calculateTotalFame(Long setId, Map<String, String> itemRarities);
}
