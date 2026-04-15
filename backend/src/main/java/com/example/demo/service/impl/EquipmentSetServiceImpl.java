package com.example.demo.service.impl;

import com.example.demo.model.EquipmentSet;
import com.example.demo.repository.EquipmentSetRepository;
import com.example.demo.service.EquipmentSetService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

/**
 * 装备套装服务实现类
 */
@Service
public class EquipmentSetServiceImpl implements EquipmentSetService {

    @Autowired
    private EquipmentSetRepository equipmentSetRepository;

    /**
     * 12套装备数据
     */
    private static final List<EquipmentSet> DEFAULT_SETS = Arrays.asList(
            createSet(1, "理想之黄金乡套", 5.0f, 5000),
            createSet(2, "龙战八荒套", 5.5f, 5500),
            createSet(3, "潜影暗袭套", 6.0f, 6000),
            createSet(4, "造化自然套", 6.5f, 6500),
            createSet(5, "天命者的气运套", 7.0f, 7000),
            createSet(6, "究极能量套", 7.5f, 7500),
            createSet(7, "混沌净化套", 8.0f, 8000),
            createSet(8, "诸神黄昏之女武神套", 8.5f, 8500),
            createSet(9, "青丘灵珠套", 9.0f, 9000),
            createSet(10, "精灵国度套", 9.5f, 9500),
            createSet(11, "冥思者的领域套", 10.0f, 10000),
            createSet(12, "群猎美学套", 10.5f, 10500)
    );

    private static EquipmentSet createSet(int index, String name, float techAttack, int attackBonus) {
        EquipmentSet set = new EquipmentSet();
        set.setIndexNum(index);
        set.setName(name);
        set.setTechAttackBonus(techAttack);
        set.setAttackBonus(attackBonus);
        set.setDescription(name);
        return set;
    }

    @Override
    public List<EquipmentSet> getAllSets() {
        List<EquipmentSet> sets = equipmentSetRepository.findAllByOrderByIndexNumAsc();
        if (sets.isEmpty()) {
            return initEquipmentSets();
        }
        return sets;
    }

    @Override
    public EquipmentSet getSetByIndex(Integer indexNum) {
        return equipmentSetRepository.findByIndexNum(indexNum);
    }

    @Override
    public EquipmentSet getSetById(Long id) {
        Optional<EquipmentSet> set = equipmentSetRepository.findById(id);
        return set.orElse(null);
    }

    @Override
    @Transactional
    public List<EquipmentSet> initEquipmentSets() {
        for (EquipmentSet set : DEFAULT_SETS) {
            EquipmentSet existing = equipmentSetRepository.findByIndexNum(set.getIndexNum());
            if (existing == null) {
                equipmentSetRepository.save(set);
            }
        }
        return equipmentSetRepository.findAllByOrderByIndexNumAsc();
    }
}
