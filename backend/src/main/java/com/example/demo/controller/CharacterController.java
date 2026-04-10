package com.example.demo.controller;

import com.example.demo.dto.CharacterDTO;
import com.example.demo.service.CharacterService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 角色Controller
 */
@RestController
@RequestMapping("/characters")
@RequiredArgsConstructor
public class CharacterController {

    private final CharacterService characterService;

    /**
     * 根据用户ID获取角色列表
     */
    @GetMapping("/user/{userId}")
    public ResponseEntity<List<CharacterDTO>> getCharactersByUserId(@PathVariable Long userId) {
        List<CharacterDTO> characters = characterService.getCharactersByUserId(userId);
        return ResponseEntity.ok(characters);
    }

    /**
     * 根据ID获取角色
     */
    @GetMapping("/{id}")
    public ResponseEntity<CharacterDTO> getCharacterById(@PathVariable Long id) {
        CharacterDTO character = characterService.getCharacterById(id);
        return ResponseEntity.ok(character);
    }

    /**
     * 创建角色
     */
    @PostMapping
    public ResponseEntity<CharacterDTO> createCharacter(@Validated @RequestBody CharacterDTO characterDTO) {
        CharacterDTO createdCharacter = characterService.createCharacter(characterDTO);
        return new ResponseEntity<>(createdCharacter, HttpStatus.CREATED);
    }

    /**
     * 更新角色
     */
    @PutMapping("/{id}")
    public ResponseEntity<CharacterDTO> updateCharacter(@PathVariable Long id, @Validated @RequestBody CharacterDTO characterDTO) {
        CharacterDTO updatedCharacter = characterService.updateCharacter(id, characterDTO);
        return ResponseEntity.ok(updatedCharacter);
    }

    /**
     * 删除角色
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCharacter(@PathVariable Long id) {
        characterService.deleteCharacter(id);
        return ResponseEntity.noContent().build();
    }
}
