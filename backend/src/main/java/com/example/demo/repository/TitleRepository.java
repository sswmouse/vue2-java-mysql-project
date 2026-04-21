package com.example.demo.repository;

import com.example.demo.model.Title;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 称号Repository
 */
@Repository
public interface TitleRepository extends JpaRepository<Title, Long> {

    /**
     * 查询所有称号（按年份和名望倒序）
     */
    @Query("SELECT t FROM Title t ORDER BY t.year DESC, t.fame DESC")
    List<Title> findAllOrderByYearAndFameDesc();

    /**
     * 根据年份查询（按名望倒序）
     */
    @Query("SELECT t FROM Title t WHERE t.year = :year ORDER BY t.fame DESC")
    List<Title> findByYear(@Param("year") Integer year);

    /**
     * 根据类型查询（按年份和名望倒序）
     */
    @Query("SELECT t FROM Title t WHERE t.type = :type ORDER BY t.year DESC, t.fame DESC")
    List<Title> findByType(@Param("type") String type);

    /**
     * 根据年份和类型查询（按名望倒序）
     */
    @Query("SELECT t FROM Title t WHERE t.year = :year AND t.type = :type ORDER BY t.fame DESC")
    List<Title> findByYearAndType(@Param("year") Integer year, @Param("type") String type);

    /**
     * 查询所有启用的称号（按年份和名望倒序）
     */
    @Query("SELECT t FROM Title t WHERE t.isActive = true ORDER BY t.year DESC, t.fame DESC")
    List<Title> findByIsActiveTrue();

    /**
     * 根据名称模糊查询（按年份和名望倒序）
     */
    @Query("SELECT t FROM Title t WHERE t.name LIKE %:name% ORDER BY t.year DESC, t.fame DESC")
    List<Title> findByNameContaining(@Param("name") String name);

    /**
     * 查询名望大于某值的称号（按名望倒序）
     */
    @Query("SELECT t FROM Title t WHERE t.fame >= :minFame ORDER BY t.year DESC, t.fame DESC")
    List<Title> findByMinFame(@Param("minFame") Integer minFame);

    /**
     * 获取所有年份（去重）
     */
    @Query("SELECT DISTINCT t.year FROM Title t ORDER BY t.year DESC")
    List<Integer> findDistinctYears();
}