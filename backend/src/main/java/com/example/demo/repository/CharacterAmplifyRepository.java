package com.example.demo.repository;

import com.example.demo.model.CharacterAmplify;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * 角色增幅配置数据仓库
 */
@Repository
public interface CharacterAmplifyRepository extends JpaRepository<CharacterAmplify, Long> {

    /**
     * 根据角色ID获取所有增幅配置
     */
    List<CharacterAmplify> findAllByCharacterId(Long characterId);

    /**
     * 根据角色ID和装备部位获取增幅配置
     */
    Optional<CharacterAmplify> findByCharacterIdAndEquipmentPart(Long characterId, String equipmentPart);

    /**
     * 删除角色指定部位的增幅配置
     */
    void deleteByCharacterIdAndEquipmentPart(Long characterId, String equipmentPart);

    /**
     * 删除角色所有增幅配置
     */
    void deleteAllByCharacterId(Long characterId);
}
