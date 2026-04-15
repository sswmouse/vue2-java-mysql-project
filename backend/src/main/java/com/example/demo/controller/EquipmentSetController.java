package com.example.demo.controller;

import com.example.demo.model.EquipmentSet;
import com.example.demo.service.EquipmentSetService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 装备套装Controller
 */
@RestController
@RequestMapping("/api/equipment-sets")
@CrossOrigin(origins = "*")
public class EquipmentSetController {

    @Autowired
    private EquipmentSetService equipmentSetService;

    /**
     * 获取所有套装
     */
    @GetMapping
    public ResponseEntity<?> getAllSets() {
        List<EquipmentSet> sets = equipmentSetService.getAllSets();
        return ResponseEntity.ok(sets);
    }

    /**
     * 根据序号获取套装
     */
    @GetMapping("/{indexNum}")
    public ResponseEntity<?> getSetByIndex(@PathVariable Integer indexNum) {
        EquipmentSet set = equipmentSetService.getSetByIndex(indexNum);
        if (set == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(set);
    }

    /**
     * 初始化套装数据
     */
    @PostMapping("/init")
    public ResponseEntity<?> initSets() {
        List<EquipmentSet> sets = equipmentSetService.initEquipmentSets();
        return ResponseEntity.ok(sets);
    }
}
