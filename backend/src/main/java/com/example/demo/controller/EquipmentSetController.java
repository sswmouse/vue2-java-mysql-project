package com.example.demo.controller;

import com.example.demo.dto.CalculatePointsDTO;
import com.example.demo.dto.EquipmentItemDTO;
import com.example.demo.dto.EquipmentSetDetailDTO;
import com.example.demo.dto.EquipmentSetDTO;
import com.example.demo.model.EquipmentItem;
import com.example.demo.model.EquipmentSet;
import com.example.demo.model.StageConfig;
import com.example.demo.service.EquipmentItemService;
import com.example.demo.service.EquipmentSetService;
import com.example.demo.service.StageConfigService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 装备套装Controller
 * 提供装备套装相关的REST API接口
 *
 * @author Claude Code
 * @version 1.0
 * @since 2026-04-17
 */
@RestController
@RequestMapping("/api/equipment-sets")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
@Slf4j
public class EquipmentSetController {

    private final EquipmentSetService equipmentSetService;
    private final EquipmentItemService equipmentItemService;
    private final StageConfigService stageConfigService;

    /**
     * 获取所有套装列表
     * GET /api/equipment-sets
     *
     * @param rarities 可选参数：套装ID对应的品级Map（JSON格式）
     * @return 套装列表
     */
    @GetMapping(produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<?> getAllSets(
            @RequestParam(required = false) String rarities) {
        List<EquipmentSetDTO> sets;

        if (rarities != null && !rarities.isEmpty()) {
            // 解析品级参数
            Map<Long, Map<String, String>> itemRarities = parseRarities(rarities);
            sets = equipmentSetService.getAllSetsWithPoints(itemRarities);
        } else {
            // 获取所有套装基本信息
            List<EquipmentSet> basicSets = equipmentSetService.getAllSets();
            sets = basicSets.stream().map(set -> EquipmentSetDTO.builder()
                    .id(set.getId())
                    .setName(set.getName())
                    .setIndex(set.getIndexNum())
                    .setType(set.getSetType())
                    .description(set.getDescription())
                    .techAttackBonus(set.getTechAttackBonus())
                    .attackBonus(set.getAttackBonus())
                    .currentPoints(0)
                    .currentStage(null)
                    .stageProgress(0.0)
                    .selectedItemCount(0)
                    .totalItemCount(11)
                    .createdAt(set.getCreatedAt())
                    .updatedAt(set.getUpdatedAt())
                    .build())
                    .collect(Collectors.toList());
        }

        return ResponseEntity.ok(sets);
    }

    /**
     * 获取套装详情
     * GET /api/equipment-sets/:id
     *
     * @param id 套装ID
     * @param rarities 可选参数：部位品级Map（JSON格式）
     * @return 套装详情
     */
    @GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<?> getSetDetail(
            @PathVariable Long id,
            @RequestParam(required = false) String rarities) {
        Map<String, String> itemRarities = new HashMap<>();
        if (rarities != null && !rarities.isEmpty()) {
            itemRarities = parseSlotRarities(rarities);
        }

        EquipmentSetDetailDTO detail = equipmentSetService.getSetWithItems(id, itemRarities);
        if (detail == null) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(detail);
    }

    /**
     * 根据序号获取套装
     * GET /api/equipment-sets/index/:indexNum
     *
     * @param indexNum 套装序号(1-12)
     * @return 套装信息
     */
    @GetMapping("/index/{indexNum}")
    public ResponseEntity<?> getSetByIndex(@PathVariable Integer indexNum) {
        EquipmentSet set = equipmentSetService.getSetByIndex(indexNum);
        if (set == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(set);
    }

    /**
     * 初始化套装数据
     * POST /api/equipment-sets/init
     *
     * @return 初始化后的套装列表
     */
    @PostMapping("/init")
    public ResponseEntity<?> initSets() {
        List<EquipmentSet> sets = equipmentSetService.initEquipmentSets();
        return ResponseEntity.ok(sets);
    }

    /**
     * 计算套装积分和阶段
     * POST /api/equipment-sets/calculate
     *
     * @param request 积分计算请求
     * @return 计算结果
     */
    @PostMapping("/calculate")
    public ResponseEntity<?> calculatePoints(@Valid @RequestBody CalculatePointsDTO.Request request) {
        // 获取套装信息
        EquipmentSet set = equipmentSetService.getSetById(request.getSetId());
        if (set == null) {
            return ResponseEntity.badRequest().body("套装不存在");
        }

        // 转换装备选择为Map
        Map<String, String> itemRarities = new HashMap<>();
        for (CalculatePointsDTO.ItemSelection item : request.getItems()) {
            if (item.getSlotName() != null && item.getRarity() != null) {
                itemRarities.put(item.getSlotName(), item.getRarity());
            }
        }

        // 计算总积分
        Integer totalPoints = equipmentItemService.calculateTotalPoints(request.getSetId(), itemRarities);

        // 获取当前阶段和下一阶段
        StageConfig currentStage = stageConfigService.getCurrentStage(totalPoints);
        Optional<StageConfig> nextStageOpt = stageConfigService.getNextStage(totalPoints);
        Double progress = stageConfigService.getStageProgress(totalPoints);

        // 获取已激活的阶段效果
        List<StageConfig> activatedStages = stageConfigService.getActivatedStages(totalPoints);
        List<CalculatePointsDTO.ActivatedEffect> activatedEffects = activatedStages.stream()
                .map(stage -> CalculatePointsDTO.ActivatedEffect.builder()
                        .stage(stage.getStage())
                        .stageOrder(stage.getStageOrder())
                        .threshold(stage.getThreshold())
                        .fameBonus(stage.getFameBonus())
                        .cumulativeFame(stage.getCumulativeFame())
                        .build())
                .collect(Collectors.toList());

        // 构建装备详情
        List<CalculatePointsDTO.ItemDetail> itemDetails = request.getItems().stream()
                .map(item -> {
                    EquipmentItem equip = equipmentItemService.getItemBySetIdAndSlotName(request.getSetId(), item.getSlotName());
                    if (equip == null) {
                        return null;
                    }
                    Integer points = equipmentItemService.getPointsByRarity(equip, item.getRarity());
                    Integer fame = equipmentItemService.getFameByRarity(equip, item.getRarity());

                    return CalculatePointsDTO.ItemDetail.builder()
                            .itemId(equip.getId())
                            .itemName(equip.getItemName())
                            .slotName(equip.getSlotName())
                            .slotType(equip.getSlotType())
                            .rarity(item.getRarity())
                            .points(points)
                            .fame(fame)
                            .build();
                })
                .filter(Objects::nonNull)
                .collect(Collectors.toList());

        // 构建响应
        CalculatePointsDTO.Response response = CalculatePointsDTO.Response.builder()
                .setId(set.getId())
                .setName(set.getName())
                .totalPoints(totalPoints)
                .currentStage(currentStage != null ? currentStage.getStage() : null)
                .nextStage(nextStageOpt.map(StageConfig::getStage).orElse(null))
                .progress(progress)
                .fameBonus(currentStage != null ? currentStage.getFameBonus() : 0)
                .cumulativeFame(currentStage != null ? currentStage.getCumulativeFame() : 0)
                .activatedEffects(activatedEffects)
                .itemDetails(itemDetails)
                .build();

        return ResponseEntity.ok(response);
    }

    /**
     * 解析套装品级参数
     * 格式：{setId: {slotName: rarity}}
     */
    @SuppressWarnings("unchecked")
    private Map<Long, Map<String, String>> parseRarities(String rarities) {
        try {
            com.fasterxml.jackson.databind.ObjectMapper mapper = new com.fasterxml.jackson.databind.ObjectMapper();
            return mapper.readValue(rarities, Map.class);
        } catch (Exception e) {
            log.error("解析rarities参数失败: {}", rarities, e);
            return new HashMap<>();
        }
    }

    /**
     * 解析部位品级参数
     * 格式：{slotName: rarity}
     */
    @SuppressWarnings("unchecked")
    private Map<String, String> parseSlotRarities(String rarities) {
        try {
            com.fasterxml.jackson.databind.ObjectMapper mapper = new com.fasterxml.jackson.databind.ObjectMapper();
            return mapper.readValue(rarities, Map.class);
        } catch (Exception e) {
            log.error("解析rarities参数失败: {}", rarities, e);
            return new HashMap<>();
        }
    }
}
