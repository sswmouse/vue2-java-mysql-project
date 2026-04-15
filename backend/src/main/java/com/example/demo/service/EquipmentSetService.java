package com.example.demo.service;

import com.example.demo.model.EquipmentSet;

import java.util.List;

/**
 * 装备套装服务接口
 */
public interface EquipmentSetService {

    /**
     * 获取所有套装（按序号排序）
     */
    List<EquipmentSet> getAllSets();

    /**
     * 根据序号获取套装
     */
    EquipmentSet getSetByIndex(Integer indexNum);

    /**
     * 根据ID获取套装
     */
    EquipmentSet getSetById(Long id);

    /**
     * 初始化12套装备数据
     */
    List<EquipmentSet> initEquipmentSets();
}
