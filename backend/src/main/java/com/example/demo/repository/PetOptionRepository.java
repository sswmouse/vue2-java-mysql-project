package com.example.demo.repository;

import com.example.demo.model.PetOption;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 宠物选项数据仓库
 */
@Repository
public interface PetOptionRepository extends JpaRepository<PetOption, Long> {

    /**
     * 根据赛季查询宠物
     */
    List<PetOption> findBySeasonOrderBySortOrderAsc(String season);

    /**
     * 查询最新赛季的宠物
     */
    List<PetOption> findByIsLatestTrueOrderBySortOrderAsc();

    /**
     * 根据宠物类型查询
     */
    List<PetOption> findByPetTypeOrderBySortOrderAsc(String petType);

    /**
     * 查询所有赛季
     */
    @Query("SELECT DISTINCT p.season FROM PetOption p ORDER BY p.season DESC")
    List<String> findDistinctSeasons();

    /**
     * 查询有专属技能的宠物
     */
    List<PetOption> findByHasSkillTrueOrderBySortOrderAsc();
}
