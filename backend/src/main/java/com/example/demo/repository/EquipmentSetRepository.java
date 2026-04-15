package com.example.demo.repository;

import com.example.demo.model.EquipmentSet;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 装备套装Repository
 */
@Repository
public interface EquipmentSetRepository extends JpaRepository<EquipmentSet, Long> {

    /**
     * 根据套装序号查找
     */
    EquipmentSet findByIndexNum(Integer indexNum);

    /**
     * 查找所有套装（按序号排序）
     */
    List<EquipmentSet> findAllByOrderByIndexNumAsc();

    /**
     * 根据套装类型查找
     */
    List<EquipmentSet> findBySetType(String setType);
}
