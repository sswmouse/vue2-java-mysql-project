package com.example.demo.service.impl;

import com.example.demo.dto.PageResult;
import com.example.demo.model.EnchantOption;
import com.example.demo.repository.EnchantOptionRepository;
import com.example.demo.service.EnchantOptionService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

/**
 * 附魔宝珠选项服务实现
 */
@Service
@Transactional
public class EnchantOptionServiceImpl implements EnchantOptionService {

    private final EnchantOptionRepository repository;

    public EnchantOptionServiceImpl(EnchantOptionRepository repository) {
        this.repository = repository;
    }

    @Override
    public List<EnchantOption> getAllOptions() {
        return repository.findAll();
    }

    @Override
    public PageResult<EnchantOption> getOptionsPage(int current, int size, String equipmentPart, String packageType) {
        // Spring Data JPA 页码从0开始，前端从1开始
        Pageable pageable = PageRequest.of(current - 1, size, Sort.by(Sort.Direction.DESC, "id"));

        Page<EnchantOption> page;
        if (equipmentPart != null && !equipmentPart.isEmpty() && packageType != null && !packageType.isEmpty()) {
            page = repository.findByEquipmentPartAndPackageType(equipmentPart, packageType, pageable);
        } else if (equipmentPart != null && !equipmentPart.isEmpty()) {
            page = repository.findByEquipmentPart(equipmentPart, pageable);
        } else if (packageType != null && !packageType.isEmpty()) {
            page = repository.findByPackageType(packageType, pageable);
        } else {
            page = repository.findAll(pageable);
        }

        return new PageResult<>(page.getContent(), page.getTotalElements(), current, size);
    }

    @Override
    public EnchantOption getOptionById(Long id) {
        return repository.findById(id).orElse(null);
    }

    @Override
    public List<EnchantOption> getOptionsByPart(String equipmentPart) {
        return repository.findByEquipmentPartOrderBySortOrderAsc(equipmentPart);
    }

    @Override
    public List<EnchantOption> getOptionsByPackage(String packageType) {
        return repository.findByPackageTypeOrderBySortOrderAsc(packageType);
    }

    @Override
    public List<String> getAllParts() {
        return repository.findDistinctEquipmentParts();
    }

    @Override
    public List<String> getAllPackages() {
        return repository.findDistinctPackageTypes();
    }

    @Override
    public Map<String, List<EnchantOption>> getOptionsGroupedByPart() {
        List<EnchantOption> allOptions = repository.findAll();
        return allOptions.stream()
                .collect(Collectors.groupingBy(
                        EnchantOption::getEquipmentPart,
                        LinkedHashMap::new,
                        Collectors.toList()
                ));
    }

    @Override
    public EnchantOption createOption(EnchantOption option) {
        return repository.save(option);
    }

    @Override
    public EnchantOption updateOption(Long id, EnchantOption option) {
        Optional<EnchantOption> existing = repository.findById(id);
        if (existing.isPresent()) {
            EnchantOption entity = existing.get();
            entity.setEquipmentPart(option.getEquipmentPart());
            entity.setName(option.getName());
            entity.setPackageType(option.getPackageType());
            entity.setAttributeValue(option.getAttributeValue());
            entity.setSortOrder(option.getSortOrder());
            entity.setFame(option.getFame());
            entity.setRemark(option.getRemark());
            return repository.save(entity);
        }
        return null;
    }

    @Override
    public void deleteOption(Long id) {
        repository.deleteById(id);
    }

    @Override
    public List<EnchantOption> batchCreateOptions(List<EnchantOption> options) {
        return repository.saveAll(options);
    }
}
