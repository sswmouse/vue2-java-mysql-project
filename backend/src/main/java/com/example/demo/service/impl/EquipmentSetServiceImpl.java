package com.example.demo.service.impl;

import com.example.demo.dto.EquipmentItemDTO;
import com.example.demo.dto.EquipmentSetDetailDTO;
import com.example.demo.dto.EquipmentSetDTO;
import com.example.demo.model.EquipmentItem;
import com.example.demo.model.EquipmentSet;
import com.example.demo.model.StageConfig;
import com.example.demo.repository.EquipmentSetRepository;
import com.example.demo.service.EquipmentItemService;
import com.example.demo.service.EquipmentSetService;
import com.example.demo.service.StageConfigService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

/**
 * 装备套装服务实现类
 *
 * @author Claude Code
 * @version 1.0
 * @since 2026-04-17
 */
@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class EquipmentSetServiceImpl implements EquipmentSetService {

    private final EquipmentSetRepository equipmentSetRepository;
    private final EquipmentItemService equipmentItemService;
    private final StageConfigService stageConfigService;

    /**
     * 12套装备数据
     */
    private static final List<EquipmentSet> DEFAULT_SETS = Arrays.asList(
            createSet(1, "理想之黄金乡套", 5.0f, 5000),
            createSet(2, "龙战八荒套", 5.5f, 5500),
            createSet(3, "潜影暗袭套", 6.0f, 6000),
            createSet(4, "造化自然套", 6.5f, 6500),
            createSet(5, "天命者的气运套", 7.0f, 7000),
            createSet(6, "究极能量套", 7.5f, 7500),
            createSet(7, "混沌净化套", 8.0f, 8000),
            createSet(8, "诸神黄昏之女武神套", 8.5f, 8500),
            createSet(9, "青丘灵珠套", 9.0f, 9000),
            createSet(10, "精灵国度套", 9.5f, 9500),
            createSet(11, "冥思者的领域套", 10.0f, 10000),
            createSet(12, "群猎美学套", 10.5f, 10500)
    );

    private static EquipmentSet createSet(int index, String name, float techAttack, int attackBonus) {
        EquipmentSet set = new EquipmentSet();
        set.setIndexNum(index);
        set.setSetIndex(index);  // 设置setIndex字段
        set.setName(name);       // 设置name字段
        set.setSetName(name);    // 设置setName字段
        set.setTechAttackBonus(techAttack);
        set.setAttackBonus(attackBonus);
        set.setDescription(name);
        return set;
    }

    @Override
    public List<EquipmentSet> getAllSets() {
        List<EquipmentSet> sets = equipmentSetRepository.findAllByOrderByIndexNumAsc();
        if (sets.isEmpty()) {
            return initEquipmentSets();
        }
        return sets;
    }

    @Override
    public EquipmentSet getSetByIndex(Integer indexNum) {
        return equipmentSetRepository.findByIndexNum(indexNum);
    }

    @Override
    public EquipmentSet getSetById(Long id) {
        Optional<EquipmentSet> set = equipmentSetRepository.findById(id);
        return set.orElse(null);
    }

    @Override
    @Transactional
    public List<EquipmentSet> initEquipmentSets() {
        for (EquipmentSet set : DEFAULT_SETS) {
            EquipmentSet existing = equipmentSetRepository.findByIndexNum(set.getIndexNum());
            if (existing == null) {
                equipmentSetRepository.save(set);
            }
        }
        return equipmentSetRepository.findAllByOrderByIndexNumAsc();
    }

    @Override
    public EquipmentSetDetailDTO getSetWithItems(Long setId, Map<String, String> itemRarities) {
        EquipmentSet set = getSetById(setId);
        if (set == null) {
            return null;
        }

        // 获取套装的所有装备
        List<EquipmentItem> allItems = equipmentItemService.getItemsBySetId(setId);

        // 按部位类型分组
        List<EquipmentItem> armorItems = allItems.stream()
                .filter(item -> EquipmentItem.SLOT_TYPE_ARMOR.equals(item.getSlotType()))
                .sorted(Comparator.comparing(EquipmentItem::getSortOrder))
                .collect(Collectors.toList());

        List<EquipmentItem> jewelryItems = allItems.stream()
                .filter(item -> EquipmentItem.SLOT_TYPE_JEWELRY.equals(item.getSlotType()))
                .sorted(Comparator.comparing(EquipmentItem::getSortOrder))
                .collect(Collectors.toList());

        List<EquipmentItem> specialItems = allItems.stream()
                .filter(item -> EquipmentItem.SLOT_TYPE_SPECIAL.equals(item.getSlotType()))
                .sorted(Comparator.comparing(EquipmentItem::getSortOrder))
                .collect(Collectors.toList());

        // 转换装备为DTO
        List<EquipmentItemDTO> armorDTOs = armorItems.stream()
                .map(item -> convertToDTO(item, itemRarities.get(item.getSlotName()), set.getName()))
                .collect(Collectors.toList());

        List<EquipmentItemDTO> jewelryDTOs = jewelryItems.stream()
                .map(item -> convertToDTO(item, itemRarities.get(item.getSlotName()), set.getName()))
                .collect(Collectors.toList());

        List<EquipmentItemDTO> specialDTOs = specialItems.stream()
                .map(item -> convertToDTO(item, itemRarities.get(item.getSlotName()), set.getName()))
                .collect(Collectors.toList());

        // 获取当前积分和阶段
        Integer totalPoints = equipmentItemService.calculateTotalPoints(setId, itemRarities);
        StageConfig currentStage = stageConfigService.getCurrentStage(totalPoints);
        Optional<StageConfig> nextStageOpt = stageConfigService.getNextStage(totalPoints);
        Double progress = stageConfigService.getStageProgress(totalPoints);

        // 获取阶段效果列表
        List<StageConfig> allStages = stageConfigService.getAllStages();
        List<EquipmentSetDetailDTO.StageEffect> effects = allStages.stream()
                .map(stage -> EquipmentSetDetailDTO.StageEffect.builder()
                        .stage(stage.getStage())
                        .stageOrder(stage.getStageOrder())
                        .threshold(stage.getThreshold())
                        .fameBonus(stage.getFameBonus())
                        .cumulativeFame(stage.getCumulativeFame())
                        .stageType(stage.getStageType())
                        .isActivated(currentStage != null && stage.getStageOrder() <= currentStage.getStageOrder())
                        .build())
                .collect(Collectors.toList());

        // 构建套装基本信息
        EquipmentSetDetailDTO.SetBasicInfo setBasicInfo = EquipmentSetDetailDTO.SetBasicInfo.builder()
                .id(set.getId())
                .setName(set.getName())
                .setIndex(set.getIndexNum())
                .setType(set.getSetType())
                .description(set.getDescription())
                .techAttackBonus(set.getTechAttackBonus())
                .attackBonus(set.getAttackBonus())
                .build();

        // 构建装备信息
        EquipmentSetDetailDTO.ItemsInfo itemsInfo = EquipmentSetDetailDTO.ItemsInfo.builder()
                .armorItems(armorDTOs)
                .jewelryItems(jewelryDTOs)
                .specialItems(specialDTOs)
                .build();

        // 构建积分信息
        EquipmentSetDetailDTO.PointsInfo pointsInfo = EquipmentSetDetailDTO.PointsInfo.builder()
                .totalPoints(totalPoints)
                .currentStage(currentStage != null ? currentStage.getStage() : null)
                .nextStage(nextStageOpt.map(StageConfig::getStage).orElse(null))
                .progress(progress)
                .fameBonus(currentStage != null ? currentStage.getFameBonus() : 0)
                .build();

        return EquipmentSetDetailDTO.builder()
                .setInfo(setBasicInfo)
                .items(itemsInfo)
                .effects(effects)
                .pointsInfo(pointsInfo)
                .build();
    }

    @Override
    public List<EquipmentSetDTO> getAllSetsWithPoints(Map<Long, Map<String, String>> itemRarities) {
        List<EquipmentSet> sets = getAllSets();
        return sets.stream().map(set -> {
            Map<String, String> rarities = itemRarities.getOrDefault(set.getId(), new HashMap<>());
            Integer totalPoints = equipmentItemService.calculateTotalPoints(set.getId(), rarities);
            StageConfig currentStage = stageConfigService.getCurrentStage(totalPoints);
            Optional<StageConfig> nextStageOpt = stageConfigService.getNextStage(totalPoints);
            Double stageProgress = stageConfigService.getStageProgress(totalPoints);

            return EquipmentSetDTO.builder()
                    .id(set.getId())
                    .setName(set.getName())
                    .setIndex(set.getIndexNum())
                    .setType(set.getSetType())
                    .description(set.getDescription())
                    .techAttackBonus(set.getTechAttackBonus())
                    .attackBonus(set.getAttackBonus())
                    .currentPoints(totalPoints)
                    .currentStage(currentStage != null ? currentStage.getStage() : null)
                    .stageProgress(stageProgress)
                    .nextStage(nextStageOpt.map(StageConfig::getStage).orElse(null))
                    .pointsToNextStage(nextStageOpt.map(s -> s.getThreshold() - totalPoints).orElse(0))
                    .fameBonus(currentStage != null ? currentStage.getFameBonus() : 0)
                    .selectedItemCount(rarities.size())
                    .totalItemCount(11)
                    .createdAt(set.getCreatedAt())
                    .updatedAt(set.getUpdatedAt())
                    .build();
        }).collect(Collectors.toList());
    }

    /**
     * 将EquipmentItem转换为DTO
     */
    private EquipmentItemDTO convertToDTO(EquipmentItem item, String rarity, String setName) {
        String effectiveRarity = rarity != null ? rarity : "史诗";
        Map<String, Object> stats = equipmentItemService.getStatsByRarity(item, effectiveRarity);
        Integer points = equipmentItemService.getPointsByRarity(item, effectiveRarity);
        Integer fame = equipmentItemService.getFameByRarity(item, effectiveRarity);
        String[] availableRarities = equipmentItemService.getAvailableRarities(item);

        // 转换积分和名望Map
        Map<String, Integer> pointsMap = new LinkedHashMap<>();
        Map<String, Integer> fameMap = new LinkedHashMap<>();
        for (String r : availableRarities) {
            pointsMap.put(r, equipmentItemService.getPointsByRarity(item, r));
            fameMap.put(r, equipmentItemService.getFameByRarity(item, r));
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
}
