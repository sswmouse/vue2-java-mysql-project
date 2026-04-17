package com.example.demo.repository;

import com.example.demo.model.EnchantOption;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 附魔宝珠选项数据仓库
 */
@Repository
public interface EnchantOptionRepository extends JpaRepository<EnchantOption, Long> {

    /**
     * 根据装备部位查询附魔选项
     */
    List<EnchantOption> findByEquipmentPartOrderBySortOrderAsc(String equipmentPart);

    /**
     * 根据所属礼包查询
     */
    List<EnchantOption> findByPackageTypeOrderBySortOrderAsc(String packageType);

    /**
     * 查询最新赛季的附魔选项
     */
    List<EnchantOption> findByIsLatestTrueOrderBySortOrderAsc();

    /**
     * 查询所有选项，按部位和排序顺序分组
     */
    @Query("SELECT DISTINCT e.equipmentPart FROM EnchantOption e ORDER BY e.equipmentPart")
    List<String> findDistinctEquipmentParts();

    /**
     * 查询所有礼包类型
     */
    @Query("SELECT DISTINCT e.packageType FROM EnchantOption e WHERE e.packageType IS NOT NULL")
    List<String> findDistinctPackageTypes();
}
