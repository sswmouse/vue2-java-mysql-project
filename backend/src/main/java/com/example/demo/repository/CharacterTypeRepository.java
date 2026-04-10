package com.example.demo.repository;

import com.example.demo.model.CharacterType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 角色类型Repository
 */
@Repository
public interface CharacterTypeRepository extends JpaRepository<CharacterType, Long> {

    /**
     * 根据职业分类查询角色类型
     */
    List<CharacterType> findByClassName(String className);

    /**
     * 根据性别和职业分类查询角色类型
     */
    List<CharacterType> findByGenderAndClassName(String gender, String className);

}
