package com.example.demo.service.impl;

import com.example.demo.model.TitleOption;
import com.example.demo.repository.TitleOptionRepository;
import com.example.demo.service.TitleOptionService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

/**
 * 称号选项服务实现
 */
@Service
@Transactional
public class TitleOptionServiceImpl implements TitleOptionService {

    private final TitleOptionRepository repository;

    public TitleOptionServiceImpl(TitleOptionRepository repository) {
        this.repository = repository;
    }

    @Override
    public List<TitleOption> getAllOptions() {
        return repository.findAll();
    }

    @Override
    public TitleOption getOptionById(Long id) {
        return repository.findById(id).orElse(null);
    }

    @Override
    public List<TitleOption> getOptionsBySeason(String season) {
        return repository.findBySeasonOrderBySortOrderAsc(season);
    }

    @Override
    public List<TitleOption> getLatestOptions() {
        return repository.findByIsLatestTrueOrderBySortOrderAsc();
    }

    @Override
    public List<String> getAllSeasons() {
        return repository.findDistinctSeasons();
    }

    @Override
    public Map<String, List<TitleOption>> getOptionsGroupedBySeason() {
        List<TitleOption> allOptions = repository.findAll();
        return allOptions.stream()
                .collect(Collectors.groupingBy(
                        TitleOption::getSeason,
                        LinkedHashMap::new,
                        Collectors.toList()
                ));
    }

    @Override
    public TitleOption createOption(TitleOption option) {
        return repository.save(option);
    }

    @Override
    public TitleOption updateOption(Long id, TitleOption option) {
        Optional<TitleOption> existing = repository.findById(id);
        if (existing.isPresent()) {
            TitleOption entity = existing.get();
            entity.setName(option.getName());
            entity.setSeason(option.getSeason());
            entity.setTitleType(option.getTitleType());
            entity.setMainAttributes(option.getMainAttributes());
            entity.setHasTechniqueAttack(option.getHasTechniqueAttack());
            entity.setTechniqueAttackValue(option.getTechniqueAttackValue());
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
    public List<TitleOption> batchCreateOptions(List<TitleOption> options) {
        return repository.saveAll(options);
    }
}
