package com.example.demo.repository;

import com.example.demo.model.CharacterPet;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * 角色宠物关联Repository
 */
@Repository
public interface CharacterPetRepository extends JpaRepository<CharacterPet, Long> {

    /**
     * 根据角色ID查询所有宠物
     */
    List<CharacterPet> findByCharacterId(Long characterId);

    /**
     * 根据角色ID和宠物ID查询
     */
    Optional<CharacterPet> findByCharacterIdAndPetId(Long characterId, Long petId);

    /**
     * 删除角色指定宠物
     */
    void deleteByCharacterIdAndPetId(Long characterId, Long petId);

    /**
     * 删除角色所有宠物
     */
    void deleteByCharacterId(Long characterId);
}