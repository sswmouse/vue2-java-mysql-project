package com.example.demo.controller;

import com.example.demo.dto.TitleDTO;
import com.example.demo.service.TitleService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * 称号Controller
 */
@RestController
@RequestMapping("/api/titles")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class TitleController {

    private final TitleService titleService;

    /**
     * 获取所有称号
     */
    @GetMapping
    public ResponseEntity<List<TitleDTO>> getAllTitles() {
        List<TitleDTO> titles = titleService.getAllTitles();
        return ResponseEntity.ok(titles);
    }

    /**
     * 根据ID获取称号
     */
    @GetMapping("/{id}")
    public ResponseEntity<TitleDTO> getTitleById(@PathVariable Long id) {
        TitleDTO title = titleService.getTitleById(id);
        if (title == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(title);
    }

    /**
     * 根据年份获取称号
     */
    @GetMapping("/year/{year}")
    public ResponseEntity<List<TitleDTO>> getTitlesByYear(@PathVariable Integer year) {
        List<TitleDTO> titles = titleService.getTitlesByYear(year);
        return ResponseEntity.ok(titles);
    }

    /**
     * 根据类型获取称号
     */
    @GetMapping("/type/{type}")
    public ResponseEntity<List<TitleDTO>> getTitlesByType(@PathVariable String type) {
        List<TitleDTO> titles = titleService.getTitlesByType(type);
        return ResponseEntity.ok(titles);
    }

    /**
     * 获取所有年份
     */
    @GetMapping("/years")
    public ResponseEntity<List<Integer>> getAllYears() {
        List<Integer> years = titleService.getAllYears();
        return ResponseEntity.ok(years);
    }

    /**
     * 搜索称号
     */
    @GetMapping("/search")
    public ResponseEntity<List<TitleDTO>> searchTitles(@RequestParam String name) {
        List<TitleDTO> titles = titleService.searchTitles(name);
        return ResponseEntity.ok(titles);
    }

    /**
     * 穿戴称号
     */
    @PostMapping("/{titleId}/equip/{characterId}")
    public ResponseEntity<Void> equipTitle(
            @PathVariable Long titleId,
            @PathVariable Long characterId) {
        titleService.equipTitle(characterId, titleId);
        return ResponseEntity.ok().build();
    }

    /**
     * 卸下称号
     */
    @DeleteMapping("/{titleId}/unequip/{characterId}")
    public ResponseEntity<Void> unequipTitle(
            @PathVariable Long titleId,
            @PathVariable Long characterId) {
        titleService.unequipTitle(characterId, titleId);
        return ResponseEntity.ok().build();
    }

    /**
     * 获取角色已穿戴的称号ID列表
     */
    @GetMapping("/equipped/{characterId}")
    public ResponseEntity<List<Long>> getEquippedTitleIds(@PathVariable Long characterId) {
        List<Long> equippedIds = titleService.getEquippedTitleIds(characterId);
        return ResponseEntity.ok(equippedIds);
    }

    /**
     * 创建称号
     */
    @PostMapping
    public ResponseEntity<TitleDTO> createTitle(@RequestBody TitleDTO titleDTO) {
        TitleDTO created = titleService.createTitle(titleDTO);
        return ResponseEntity.ok(created);
    }

    /**
     * 更新称号
     */
    @PutMapping("/{id}")
    public ResponseEntity<TitleDTO> updateTitle(
            @PathVariable Long id,
            @RequestBody TitleDTO titleDTO) {
        TitleDTO updated = titleService.updateTitle(id, titleDTO);
        return ResponseEntity.ok(updated);
    }

    /**
     * 删除称号
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTitle(@PathVariable Long id) {
        titleService.deleteTitle(id);
        return ResponseEntity.ok().build();
    }
}