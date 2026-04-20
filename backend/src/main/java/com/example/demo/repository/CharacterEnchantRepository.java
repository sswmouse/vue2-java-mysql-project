package com.example.demo.repository;

import com.example.demo.model.CharacterEnchant;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * 角色附魔配置数据仓库
 */
@Repository
public interface CharacterEnchantRepository extends JpaRepository<CharacterEnchant, Long> {

    /**
     * 根据角色ID获取所有附魔配置
     */
    List<CharacterEnchant> findAllByCharacterId(Long characterId);

    /**
     * 根据角色ID和装备部位获取附魔配置
     */
    Optional<CharacterEnchant> findByCharacterIdAndEquipmentPart(Long characterId, String equipmentPart);

    /**
     * 删除角色指定部位的附魔配置
     */
    void deleteByCharacterIdAndEquipmentPart(Long characterId, String equipmentPart);

    /**
     * 删除角色所有附魔配置
     */
    void deleteAllByCharacterId(Long characterId);
}
