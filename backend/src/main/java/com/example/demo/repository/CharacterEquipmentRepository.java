package com.example.demo.repository;

import com.example.demo.model.CharacterEquipment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * 角色装备配置Repository
 *
 * @author Claude Code
 * @version 1.0
 * @since 2026-04-18
 */
@Repository
public interface CharacterEquipmentRepository extends JpaRepository<CharacterEquipment, Long> {

    /**
     * 根据角色ID查找装备配置
     */
    Optional<CharacterEquipment> findByCharacterId(Long characterId);

    /**
     * 根据角色ID和套装ID查找装备配置
     */
    Optional<CharacterEquipment> findByCharacterIdAndSetId(Long characterId, Long setId);

    /**
     * 根据角色ID查找所有装备配置
     */
    List<CharacterEquipment> findAllByCharacterId(Long characterId);

    /**
     * 根据角色ID查找当前穿戴的装备配置
     */
    Optional<CharacterEquipment> findByCharacterIdAndIsEquipped(Long characterId, Boolean isEquipped);

    /**
     * 清除角色所有装备的穿戴状态
     */
    @Modifying
    @Query("UPDATE CharacterEquipment ce SET ce.isEquipped = false WHERE ce.characterId = :characterId AND ce.isEquipped = true")
    void clearEquippedByCharacterId(@Param("characterId") Long characterId);

    /**
     * 删除角色装备配置
     */
    void deleteByCharacterId(Long characterId);
}
