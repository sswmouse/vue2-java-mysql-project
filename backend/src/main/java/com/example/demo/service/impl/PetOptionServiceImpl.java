package com.example.demo.service.impl;

import com.example.demo.model.PetOption;
import com.example.demo.repository.PetOptionRepository;
import com.example.demo.service.PetOptionService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

/**
 * 宠物选项服务实现
 */
@Service
@Transactional
public class PetOptionServiceImpl implements PetOptionService {

    private final PetOptionRepository repository;

    public PetOptionServiceImpl(PetOptionRepository repository) {
        this.repository = repository;
    }

    @Override
    public List<PetOption> getAllOptions() {
        return repository.findAll();
    }

    @Override
    public PetOption getOptionById(Long id) {
        return repository.findById(id).orElse(null);
    }

    @Override
    public List<PetOption> getOptionsBySeason(String season) {
        return repository.findBySeasonOrderBySortOrderAsc(season);
    }

    @Override
    public List<PetOption> getLatestOptions() {
        return repository.findByIsLatestTrueOrderBySortOrderAsc();
    }

    @Override
    public List<String> getAllSeasons() {
        return repository.findDistinctSeasons();
    }

    @Override
    public Map<String, List<PetOption>> getOptionsGroupedBySeason() {
        List<PetOption> allOptions = repository.findAll();
        return allOptions.stream()
                .collect(Collectors.groupingBy(
                        PetOption::getSeason,
                        LinkedHashMap::new,
                        Collectors.toList()
                ));
    }

    @Override
    public PetOption createOption(PetOption option) {
        return repository.save(option);
    }

    @Override
    public PetOption updateOption(Long id, PetOption option) {
        Optional<PetOption> existing = repository.findById(id);
        if (existing.isPresent()) {
            PetOption entity = existing.get();
            entity.setName(option.getName());
            entity.setSeason(option.getSeason());
            entity.setPetType(option.getPetType());
            entity.setAttributes(option.getAttributes());
            entity.setHasSkill(option.getHasSkill());
            entity.setSkillDescription(option.getSkillDescription());
            entity.setSortOrder(option.getSortOrder());
            entity.setIsLatest(option.getIsLatest());
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
    public List<PetOption> batchCreateOptions(List<PetOption> options) {
        return repository.saveAll(options);
    }
}
