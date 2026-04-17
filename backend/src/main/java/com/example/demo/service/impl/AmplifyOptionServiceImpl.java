package com.example.demo.service.impl;

import com.example.demo.model.AmplifyOption;
import com.example.demo.repository.AmplifyOptionRepository;
import com.example.demo.service.AmplifyOptionService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

/**
 * 增幅选项服务实现
 */
@Service
@Transactional
public class AmplifyOptionServiceImpl implements AmplifyOptionService {

    private final AmplifyOptionRepository repository;

    public AmplifyOptionServiceImpl(AmplifyOptionRepository repository) {
        this.repository = repository;
    }

    @Override
    public List<AmplifyOption> getAllOptions() {
        return repository.findAll();
    }

    @Override
    public AmplifyOption getOptionById(Long id) {
        return repository.findById(id).orElse(null);
    }

    @Override
    public List<AmplifyOption> getOptionsByType(String amplifyType) {
        return repository.findByAmplifyTypeOrderByLevelAsc(amplifyType);
    }

    @Override
    public AmplifyOption createOption(AmplifyOption option) {
        return repository.save(option);
    }

    @Override
    public AmplifyOption updateOption(Long id, AmplifyOption option) {
        Optional<AmplifyOption> existing = repository.findById(id);
        if (existing.isPresent()) {
            AmplifyOption entity = existing.get();
            entity.setLevel(option.getLevel());
            entity.setAmplifyType(option.getAmplifyType());
            entity.setAttribute(option.getAttribute());
            entity.setAttributeValue(option.getAttributeValue());
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
    public List<AmplifyOption> batchCreateOptions(List<AmplifyOption> options) {
        return repository.saveAll(options);
    }
}
