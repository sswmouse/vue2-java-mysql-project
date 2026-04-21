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
}