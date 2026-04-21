package com.example.demo.repository;

import com.example.demo.model.Pet;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 宠物Repository
 */
@Repository
public interface PetRepository extends JpaRepository<Pet, Long> {

    /**
     * 查询所有宠物（按年份和名望倒序）
     */
    @Query("SELECT p FROM Pet p ORDER BY p.year DESC, p.fame DESC")
    List<Pet> findAllOrderByYearAndFameDesc();

    /**
     * 根据年份查询（按名望倒序）
     */
    @Query("SELECT p FROM Pet p WHERE p.year = :year ORDER BY p.fame DESC")
    List<Pet> findByYear(@Param("year") Integer year);

    /**
     * 根据类型查询（按年份和名望倒序）
     */
    @Query("SELECT p FROM Pet p WHERE p.type = :type ORDER BY p.year DESC, p.fame DESC")
    List<Pet> findByType(@Param("type") String type);

    /**
     * 查询所有启用的宠物（按年份和名望倒序）
     */
    @Query("SELECT p FROM Pet p WHERE p.isActive = true ORDER BY p.year DESC, p.fame DESC")
    List<Pet> findByIsActiveTrue();

    /**
     * 根据名称模糊查询（按年份和名望倒序）
     */
    @Query("SELECT p FROM Pet p WHERE p.name LIKE %:name% ORDER BY p.year DESC, p.fame DESC")
    List<Pet> findByNameContaining(@Param("name") String name);

    /**
     * 获取所有年份（去重）
     */
    @Query("SELECT DISTINCT p.year FROM Pet p ORDER BY p.year DESC")
    List<Integer> findDistinctYears();
}