package com.example.demo.repository;

import com.example.demo.model.CharacterCard;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.util.List;

public interface CharacterCardRepository extends JpaRepository<CharacterCard, Long> {
    List<CharacterCard> findByCharacterId(Long characterId);
    List<CharacterCard> findByCharacterIdAndIsDefaultTrue(Long characterId);

    @Modifying
    @Query("UPDATE CharacterCard c SET c.isDefault = false WHERE c.characterId = :characterId")
    void clearDefaultByCharacterId(@Param("characterId") Long characterId);
}
