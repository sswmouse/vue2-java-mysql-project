package com.example.demo.controller;

import com.example.demo.dto.CharacterTypeDTO;
import com.example.demo.service.CharacterTypeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 角色类型Controller
 */
@RestController
@RequestMapping("/api/character-types")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class CharacterTypeController {

    private final CharacterTypeService characterTypeService;

    /**
     * 获取所有角色类型
     */
    @GetMapping
    public ResponseEntity<List<CharacterTypeDTO>> getAllCharacterTypes() {
        List<CharacterTypeDTO> characterTypes = characterTypeService.getAllCharacterTypes();
        return ResponseEntity.ok(characterTypes);
    }

    /**
     * 根据ID获取角色类型
     */
    @GetMapping("/{id}")
    public ResponseEntity<CharacterTypeDTO> getCharacterTypeById(@PathVariable Long id) {
        CharacterTypeDTO characterType = characterTypeService.getCharacterTypeById(id);
        return ResponseEntity.ok(characterType);
    }

    /**
     * 获取用于联级选择器的角色类型数据
     */
    @GetMapping("/cascade")
    public ResponseEntity<List<CharacterTypeDTO>> getCharacterTypesForCascade() {
        List<CharacterTypeDTO> characterTypes = characterTypeService.getCharacterTypesForCascade();
        return ResponseEntity.ok(characterTypes);
    }
}
