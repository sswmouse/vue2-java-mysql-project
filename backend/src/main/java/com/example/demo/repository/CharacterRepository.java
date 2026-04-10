package com.example.demo.repository;

import com.example.demo.model.Character;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 角色Repository
 */
@Repository
public interface CharacterRepository extends JpaRepository<Character, Long> {

    /**
     * 根据用户ID查询角色列表
     */
    List<Character> findByUserId(Long userId);

    /**
     * 根据用户ID和角色名称查询
     */
    List<Character> findByUserIdAndCharacterName(Long userId, String characterName);

    /**
     * 统计用户的角色数量
     */
    long countByUserId(Long userId);
}
