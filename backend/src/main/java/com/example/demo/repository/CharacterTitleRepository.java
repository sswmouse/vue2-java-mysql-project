package com.example.demo.repository;

import com.example.demo.model.CharacterTitle;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * 角色称号关联Repository
 */
@Repository
public interface CharacterTitleRepository extends JpaRepository<CharacterTitle, Long> {

    /**
     * 根据角色ID查询所有称号
     */
    List<CharacterTitle> findByCharacterId(Long characterId);

    /**
     * 根据角色ID和称号ID查询
     */
    Optional<CharacterTitle> findByCharacterIdAndTitleId(Long characterId, Long titleId);

    /**
     * 删除角色指定称号
     */
    void deleteByCharacterIdAndTitleId(Long characterId, Long titleId);

    /**
     * 删除角色所有称号
     */
    void deleteByCharacterId(Long characterId);
}