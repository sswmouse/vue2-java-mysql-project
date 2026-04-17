package com.example.demo.service.impl;

import com.example.demo.dto.EquipmentItemDTO;
import com.example.demo.model.EquipmentItem;
import com.example.demo.model.EquipmentSet;
import com.example.demo.repository.EquipmentItemRepository;
import com.example.demo.repository.EquipmentSetRepository;
import com.example.demo.service.EquipmentItemService;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

/**
 * 装备详情服务实现类
 * 提供装备详情相关的业务操作实现
 *
 * @author Claude Code
 * @version 1.0
 * @since 2026-04-17
 */
@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class EquipmentItemServiceImpl implements EquipmentItemService {

    private final EquipmentItemRepository equipmentItemRepository;
    private final EquipmentSetRepository equipmentSetRepository;
    private final ObjectMapper objectMapper = new ObjectMapper();

    /**
     * 装备品级积分映射
     */
    private static final Map<String, Integer> RARITY_POINTS = new LinkedHashMap<>();
    static {
        RARITY_POINTS.put("稀有", 65);
        RARITY_POINTS.put("神器", 115);
        RARITY_POINTS.put("传说", 165);
        RARITY_POINTS.put("史诗", 215);
        RARITY_POINTS.put("太初", 265);
        RARITY_POINTS.put("黑牙史诗", 165);
        RARITY_POINTS.put("黑牙太初", 215);
    }

    /**
     * 装备品级名望映射
     */
    private static final Map<String, Integer> RARITY_FAME = new LinkedHashMap<>();
    static {
        RARITY_FAME.put("稀有", 1000);
        RARITY_FAME.put("神器", 1100);
        RARITY_FAME.put("传说", 1200);
        RARITY_FAME.put("史诗", 1300);
        RARITY_FAME.put("太初", 1400);
        RARITY_FAME.put("黑牙史诗", 1500);
        RARITY_FAME.put("黑牙太初", 1600);
    }

    @Override
    public List<EquipmentItem> getItemsBySetId(Long setId) {
        return equipmentItemRepository.findBySetIdOrderBySortOrderAsc(setId);
    }

    @Override
    public List<EquipmentItem> getItemsBySetIdAndSlotType(Long setId, String slotType) {
        return equipmentItemRepository.findBySetIdAndSlotType(setId, slotType);
    }

    @Override
    public EquipmentItem getItemBySetIdAndSlotName(Long setId, String slotName) {
        return equipmentItemRepository.findBySetIdAndSlotName(setId, slotName);
    }

    @Override
    public EquipmentItem getItemById(Long id) {
        return equipmentItemRepository.findById(id).orElse(null);
    }

    @Override
    public EquipmentItem saveItem(EquipmentItem item) {
        return equipmentItemRepository.save(item);
    }

    @Override
    public List<EquipmentItem> saveItems(List<EquipmentItem> items) {
        return equipmentItemRepository.saveAll(items);
    }

    @Override
    public void deleteItem(Long id) {
        equipmentItemRepository.deleteById(id);
    }

    @Override
    public void deleteItemsBySetId(Long setId) {
        equipmentItemRepository.deleteBySetId(setId);
    }

    @Override
    public Integer getPointsByRarity(EquipmentItem item, String rarity) {
        if (item.getRarityPoints() != null) {
            try {
                Map<String, Integer> pointsMap = objectMapper.readValue(
                        item.getRarityPoints(),
                        new TypeReference<Map<String, Integer>>() {}
                );
                return pointsMap.getOrDefault(rarity, 0);
            } catch (Exception e) {
                log.error("解析rarityPoints失败: {}", item.getRarityPoints(), e);
            }
        }
        return RARITY_POINTS.getOrDefault(rarity, 0);
    }

    @Override
    public Integer getFameByRarity(EquipmentItem item, String rarity) {
        if (item.getRarityFame() != null) {
            try {
                Map<String, Integer> fameMap = objectMapper.readValue(
                        item.getRarityFame(),
                        new TypeReference<Map<String, Integer>>() {}
                );
                return fameMap.getOrDefault(rarity, 0);
            } catch (Exception e) {
                log.error("解析rarityFame失败: {}", item.getRarityFame(), e);
            }
        }
        return RARITY_FAME.getOrDefault(rarity, 0);
    }

    @Override
    public Map<String, Object> getStatsByRarity(EquipmentItem item, String rarity) {
        if (item.getRarityStats() != null) {
            try {
                Map<String, Map<String, Object>> statsMap = objectMapper.readValue(
                        item.getRarityStats(),
                        new TypeReference<Map<String, Map<String, Object>>>() {}
                );
                Map<String, Object> stats = statsMap.get(rarity);
                if (stats != null) {
                    return stats;
                }
            } catch (Exception e) {
                log.error("解析rarityStats失败: {}", item.getRarityStats(), e);
            }
        }
        // 返回默认属性
        return getDefaultStats(rarity);
    }

    @Override
    public EquipmentItemDTO getItemDTOById(Long itemId, String rarity, String setName) {
        EquipmentItem item = getItemById(itemId);
        if (item == null) {
            return null;
        }

        String effectiveRarity = rarity != null ? rarity : "史诗";
        Map<String, Object> stats = getStatsByRarity(item, effectiveRarity);
        Integer points = getPointsByRarity(item, effectiveRarity);
        Integer fame = getFameByRarity(item, effectiveRarity);
        String[] availableRarities = getAvailableRarities(item);

        // 转换积分和名望Map
        Map<String, Integer> pointsMap = new LinkedHashMap<>();
        Map<String, Integer> fameMap = new LinkedHashMap<>();
        for (String r : availableRarities) {
            pointsMap.put(r, getPointsByRarity(item, r));
            fameMap.put(r, getFameByRarity(item, r));
        }

        return EquipmentItemDTO.builder()
                .id(item.getId())
                .setId(item.getSetId())
                .setName(setName)
                .slotType(item.getSlotType())
                .slotName(item.getSlotName())
                .itemName(item.getItemName())
                .currentRarity(effectiveRarity)
                .stats(stats)
                .points(points)
                .fame(fame)
                .rarityDisplayName(effectiveRarity)
                .sortOrder(item.getSortOrder())
                .availableRarities(availableRarities)
                .rarityPointsMap(pointsMap)
                .rarityFameMap(fameMap)
                .build();
    }

    @Override
    public String[] getAvailableRarities(EquipmentItem item) {
        // 根据部位类型返回可用的品级
        if (EquipmentItem.SLOT_TYPE_JEWELRY.equals(item.getSlotType())) {
            return new String[]{"稀有", "神器", "传说", "史诗", "太初", "黑牙史诗", "黑牙太初"};
        } else {
            // 防具和特殊装备最高为史诗
            return new String[]{"稀有", "神器", "传说", "史诗"};
        }
    }

    /**
     * 获取默认属性
     */
    private Map<String, Object> getDefaultStats(String rarity) {
        Map<String, Object> stats = new LinkedHashMap<>();
        int multiplier = getRarityMultiplier(rarity);
        stats.put("str", 50 * multiplier);
        stats.put("int", 50 * multiplier);
        stats.put("crit", 5 * multiplier);
        stats.put("elementalBonus", 15 * multiplier);
        return stats;
    }

    /**
     * 获取品级倍率
     */
    private int getRarityMultiplier(String rarity) {
        switch (rarity) {
            case "神器": return 2;
            case "传说": return 3;
            case "史诗": return 4;
            case "太初": return 5;
            case "黑牙史诗": return 3;
            case "黑牙太初": return 4;
            default: return 1;
        }
    }

    @Override
    public void initEquipmentItems() {
        log.info("开始初始化12套装备的装备详情数据...");

        // 从数据库获取所有套装，使用实际的setId
        List<EquipmentSet> sets = equipmentSetRepository.findAllByOrderByIndexNumAsc();
        if (sets.isEmpty()) {
            log.warn("没有找到任何套装数据，请先初始化套装数据");
            return;
        }

        for (EquipmentSet set : sets) {
            Long setId = set.getId();
            String setName = set.getName();
            List<EquipmentItem> items = new ArrayList<>();

            // 防具5件
            items.add(createItem(setId, "armor", "头肩", setName + "·裁决之冕", 1));
            items.add(createItem(setId, "armor", "胸甲", setName + "·永恒胸甲", 2));
            items.add(createItem(setId, "armor", "腰带", setName + "·缚魂腰带", 3));
            items.add(createItem(setId, "armor", "下装", setName + "·冥想护腿", 4));
            items.add(createItem(setId, "armor", "鞋", setName + "·疾风之靴", 5));

            // 首饰3件
            items.add(createItem(setId, "jewelry", "项链", setName + "·灵魂项链", 6));
            items.add(createItem(setId, "jewelry", "手镯", setName + "·暗影手镯", 7));
            items.add(createItem(setId, "jewelry", "戒指", setName + "·冥想戒指", 8));

            // 特殊装备3件
            items.add(createItem(setId, "special", "辅助装备", setName + "·灵魂石", 9));
            items.add(createItem(setId, "special", "魔法石", setName + "·虚空魔石", 10));
            items.add(createItem(setId, "special", "耳环", setName + "·深渊耳环", 11));

            // 检查是否已存在数据
            if (!equipmentItemRepository.existsBySetId(setId)) {
                equipmentItemRepository.saveAll(items);
                log.info("套装[{}]装备数据初始化完成，共{}件", setName, items.size());
            } else {
                log.info("套装[{}]已存在数据，跳过初始化", setName);
            }
        }

        log.info("12套装备详情数据初始化完成");
    }

    /**
     * 创建装备实例
     */
    private EquipmentItem createItem(Long setId, String slotType, String slotName, String itemName, int sortOrder) {
        return EquipmentItem.builder()
                .setId(setId)
                .slotType(slotType)
                .slotName(slotName)
                .itemName(itemName)
                .rarityStats(createRarityStatsJson(slotType))
                .rarityPoints(createRarityPointsJson(slotType))
                .rarityFame(createRarityFameJson(slotType))
                .sortOrder(sortOrder)
                .build();
    }

    /**
     * 创建品级属性JSON
     */
    private String createRarityStatsJson(String slotType) {
        try {
            Map<String, Map<String, Object>> stats = new LinkedHashMap<>();
            stats.put("稀有", createStatsMap(1));
            stats.put("神器", createStatsMap(2));
            stats.put("传说", createStatsMap(3));
            stats.put("史诗", createStatsMap(4));

            if (EquipmentItem.SLOT_TYPE_JEWELRY.equals(slotType)) {
                stats.put("太初", createStatsMap(5));
                stats.put("黑牙史诗", createBlackStatsMap(3));
                stats.put("黑牙太初", createBlackStatsMap(4));
            }

            return objectMapper.writeValueAsString(stats);
        } catch (Exception e) {
            log.error("创建rarityStats JSON失败", e);
            return "{}";
        }
    }

    /**
     * 创建属性Map
     */
    private Map<String, Object> createStatsMap(int multiplier) {
        Map<String, Object> stats = new LinkedHashMap<>();
        stats.put("str", 50 * multiplier);
        stats.put("int", 50 * multiplier);
        stats.put("crit", 5 * multiplier);
        stats.put("elementalBonus", 15 * multiplier);
        return stats;
    }

    /**
     * 创建黑牙属性Map
     */
    private Map<String, Object> createBlackStatsMap(int multiplier) {
        Map<String, Object> stats = createStatsMap(multiplier);
        stats.put("specialBonus", true);
        return stats;
    }

    /**
     * 创建品级积分JSON
     * 防具/特殊装备最高为史诗，首饰最高为太初
     */
    private String createRarityPointsJson(String slotType) {
        try {
            Map<String, Integer> points = new LinkedHashMap<>();
            points.put("稀有", 65);
            points.put("神器", 115);
            points.put("传说", 165);
            points.put("史诗", 215);

            if (EquipmentItem.SLOT_TYPE_JEWELRY.equals(slotType)) {
                points.put("太初", 265);
                points.put("黑牙史诗", 165);
                points.put("黑牙太初", 215);
            }

            return objectMapper.writeValueAsString(points);
        } catch (Exception e) {
            log.error("创建rarityPoints JSON失败", e);
            return "{}";
        }
    }

    /**
     * 创建品级名望JSON
     */
    private String createRarityFameJson(String slotType) {
        try {
            Map<String, Integer> fame = new LinkedHashMap<>();
            fame.put("稀有", 1000);
            fame.put("神器", 1100);
            fame.put("传说", 1200);
            fame.put("史诗", 1300);

            if (EquipmentItem.SLOT_TYPE_JEWELRY.equals(slotType)) {
                fame.put("太初", 1400);
                fame.put("黑牙史诗", 1500);
                fame.put("黑牙太初", 1600);
            }

            return objectMapper.writeValueAsString(fame);
        } catch (Exception e) {
            log.error("创建rarityFame JSON失败", e);
            return "{}";
        }
    }

    @Override
    public Integer calculateTotalPoints(Long setId, Map<String, String> itemRarities) {
        List<EquipmentItem> items = getItemsBySetId(setId);
        int totalPoints = 0;

        for (EquipmentItem item : items) {
            String rarity = itemRarities.get(item.getSlotName());
            if (rarity != null) {
                totalPoints += getPointsByRarity(item, rarity);
            }
        }

        return totalPoints;
    }

    @Override
    public Integer calculateTotalFame(Long setId, Map<String, String> itemRarities) {
        List<EquipmentItem> items = getItemsBySetId(setId);
        int totalFame = 0;

        for (EquipmentItem item : items) {
            String rarity = itemRarities.get(item.getSlotName());
            if (rarity != null) {
                totalFame += getFameByRarity(item, rarity);
            }
        }

        return totalFame;
    }
}
