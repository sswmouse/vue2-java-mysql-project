package com.example.demo.repository;

import com.example.demo.model.EquipmentItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 装备详情Repository
 * 提供装备详情数据的数据库操作方法
 *
 * @author Claude Code
 * @version 1.0
 * @since 2026-04-17
 */
@Repository
public interface EquipmentItemRepository extends JpaRepository<EquipmentItem, Long> {

    /**
     * 根据套装ID查找所有装备
     */
    List<EquipmentItem> findBySetIdOrderBySortOrderAsc(Long setId);

    /**
     * 根据套装ID和部位类型查找装备
     */
    List<EquipmentItem> findBySetIdAndSlotType(Long setId, String slotType);

    /**
     * 根据套装ID和部位名称查找装备
     */
    EquipmentItem findBySetIdAndSlotName(Long setId, String slotName);

    /**
     * 根据部位类型查找所有装备
     */
    List<EquipmentItem> findBySlotTypeOrderBySetIdAsc(String slotType);

    /**
     * 根据套装ID统计装备数量
     */
    long countBySetId(Long setId);

    /**
     * 判断套装是否有装备数据
     */
    boolean existsBySetId(Long setId);

    /**
     * 根据套装ID删除所有装备
     */
    void deleteBySetId(Long setId);

    /**
     * 获取所有防具装备（按套装ID和排序顺序）
     */
    @Query("SELECT e FROM EquipmentItem e WHERE e.slotType = 'armor' ORDER BY e.setId, e.sortOrder")
    List<EquipmentItem> findAllArmorItems();

    /**
     * 获取所有首饰装备（按套装ID和排序顺序）
     */
    @Query("SELECT e FROM EquipmentItem e WHERE e.slotType = 'jewelry' ORDER BY e.setId, e.sortOrder")
    List<EquipmentItem> findAllJewelryItems();

    /**
     * 获取所有特殊装备（按套装ID和排序顺序）
     */
    @Query("SELECT e FROM EquipmentItem e WHERE e.slotType = 'special' ORDER BY e.setId, e.sortOrder")
    List<EquipmentItem> findAllSpecialItems();
}
