package com.example.demo.repository;

import com.example.demo.model.TitleOption;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 称号选项数据仓库
 */
@Repository
public interface TitleOptionRepository extends JpaRepository<TitleOption, Long> {

    /**
     * 根据赛季查询称号
     */
    List<TitleOption> findBySeasonOrderBySortOrderAsc(String season);

    /**
     * 查询最新赛季的称号
     */
    List<TitleOption> findByIsLatestTrueOrderBySortOrderAsc();

    /**
     * 根据称号类型查询
     */
    List<TitleOption> findByTitleTypeOrderBySortOrderAsc(String titleType);

    /**
     * 查询所有赛季
     */
    @Query("SELECT DISTINCT t.season FROM TitleOption t ORDER BY t.season DESC")
    List<String> findDistinctSeasons();

    /**
     * 查询有技能攻击力的称号
     */
    List<TitleOption> findByHasTechniqueAttackTrueOrderBySortOrderAsc();
}
