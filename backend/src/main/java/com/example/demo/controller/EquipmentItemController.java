package com.example.demo.controller;

import com.example.demo.dto.EquipmentItemDTO;
import com.example.demo.model.EquipmentItem;
import com.example.demo.model.EquipmentSet;
import com.example.demo.service.EquipmentItemService;
import com.example.demo.service.EquipmentSetService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 装备详情Controller
 * 提供单件装备相关的REST API接口
 *
 * @author Claude Code
 * @version 1.0
 * @since 2026-04-17
 */
@RestController
@RequestMapping("/api/equipment-items")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
@Slf4j
public class EquipmentItemController {

    private final EquipmentItemService equipmentItemService;
    private final EquipmentSetService equipmentSetService;

    /**
     * 获取装备详情
     * GET /api/equipment-items/:id
     *
     * @param id 装备ID
     * @param rarity 可选参数：品级（默认史诗）
     * @return 装备详情
     */
    @GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<?> getItemById(
            @PathVariable Long id,
            @RequestParam(required = false, defaultValue = "史诗") String rarity) {
        EquipmentItem item = equipmentItemService.getItemById(id);
        if (item == null) {
            return ResponseEntity.notFound().build();
        }

        // 获取套装名称
        EquipmentSet set = equipmentSetService.getSetById(item.getSetId());
        String setName = set != null ? set.getName() : "";

        EquipmentItemDTO dto = equipmentItemService.getItemDTOById(id, rarity, setName);
        return ResponseEntity.ok(dto);
    }

    /**
     * 获取套装的所有装备
     * GET /api/equipment-items/set/:setId
     *
     * @param setId 套装ID
     * @param rarity 可选参数：品级（默认史诗）
     * @return 装备列表
     */
    @GetMapping(value = "/set/{setId}", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<?> getItemsBySetId(
            @PathVariable Long setId,
            @RequestParam(required = false, defaultValue = "史诗") String rarity) {
        List<EquipmentItem> items = equipmentItemService.getItemsBySetId(setId);
        if (items.isEmpty()) {
            return ResponseEntity.ok(items);
        }

        // 获取套装名称
        EquipmentSet set = equipmentSetService.getSetById(setId);
        String setName = set != null ? set.getName() : "";

        List<EquipmentItemDTO> dtos = items.stream()
                .map(item -> equipmentItemService.getItemDTOById(item.getId(), rarity, setName))
                .collect(Collectors.toList());

        return ResponseEntity.ok(dtos);
    }

    /**
     * 根据部位类型获取装备
     * GET /api/equipment-items/slot-type/:slotType
     *
     * @param slotType 部位类型：armor/jewelry/special
     * @param setId 套装ID
     * @param rarity 可选参数：品级（默认史诗）
     * @return 装备列表
     */
    @GetMapping("/slot-type/{slotType}")
    public ResponseEntity<?> getItemsBySlotType(
            @PathVariable String slotType,
            @RequestParam Long setId,
            @RequestParam(required = false, defaultValue = "史诗") String rarity) {
        List<EquipmentItem> items = equipmentItemService.getItemsBySetIdAndSlotType(setId, slotType);

        // 获取套装名称
        EquipmentSet set = equipmentSetService.getSetById(setId);
        String setName = set != null ? set.getName() : "";

        List<EquipmentItemDTO> dtos = items.stream()
                .map(item -> equipmentItemService.getItemDTOById(item.getId(), rarity, setName))
                .collect(Collectors.toList());

        return ResponseEntity.ok(dtos);
    }

    /**
     * 初始化装备数据
     * POST /api/equipment-items/init
     *
     * @return 初始化结果
     */
    @PostMapping("/init")
    public ResponseEntity<?> initItems() {
        equipmentItemService.initEquipmentItems();
        return ResponseEntity.ok("装备数据初始化完成");
    }

    /**
     * 批量获取装备积分
     * POST /api/equipment-items/points
     *
     * @param request 请求体，包含装备ID和品级列表
     * @return 装备积分映射
     */
    @PostMapping("/points")
    public ResponseEntity<?> getPointsBatch(@RequestBody Map<String, Object> request) {
        @SuppressWarnings("unchecked")
        List<Map<String, String>> items = (List<Map<String, String>>) request.get("items");

        if (items == null || items.isEmpty()) {
            return ResponseEntity.badRequest().body("装备列表不能为空");
        }

        Map<String, Map<String, Object>> result = items.stream()
                .collect(Collectors.toMap(
                        item -> item.getOrDefault("slotName", ""),
                        item -> {
                            Long itemId = Long.valueOf(item.getOrDefault("itemId", "0"));
                            String rarity = item.getOrDefault("rarity", "史诗");
                            EquipmentItem equip = equipmentItemService.getItemById(itemId);
                            if (equip == null) {
                                return Map.of("points", 0, "fame", 0);
                            }
                            Integer points = equipmentItemService.getPointsByRarity(equip, rarity);
                            Integer fame = equipmentItemService.getFameByRarity(equip, rarity);
                            return Map.of("points", points, "fame", fame);
                        }
                ));

        return ResponseEntity.ok(result);
    }
}
