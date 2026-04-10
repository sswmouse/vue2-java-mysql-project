package com.example.demo.service.impl;

import com.example.demo.dto.CharacterTypeDTO;
import com.example.demo.model.CharacterType;
import com.example.demo.repository.CharacterTypeRepository;
import com.example.demo.service.CharacterTypeService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 角色类型Service实现类
 */
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CharacterTypeServiceImpl implements CharacterTypeService {

    private final CharacterTypeRepository characterTypeRepository;

    @Override
    public List<CharacterTypeDTO> getAllCharacterTypes() {
        List<CharacterType> characterTypes = characterTypeRepository.findAll();
        return characterTypes.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public CharacterTypeDTO getCharacterTypeById(Long id) {
        CharacterType characterType = characterTypeRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("角色类型不存在"));
        return convertToDTO(characterType);
    }

    @Override
    public CharacterTypeDTO getCharacterTypeFullInfo(Long id) {
        CharacterType characterType = characterTypeRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("角色类型不存在"));
        return convertToDTO(characterType);
    }

    @Override
    public List<CharacterTypeDTO> getCharacterTypesForCascade() {
        List<CharacterType> allTypes = characterTypeRepository.findAll();

        // 按性别和职业分类分组
        Map<String, List<CharacterType>> grouped = allTypes.stream()
                .collect(Collectors.groupingBy(type ->
                        type.getGender() + type.getClassName()
                ));

        List<CharacterTypeDTO> result = new ArrayList<>();

        for (Map.Entry<String, List<CharacterType>> entry : grouped.entrySet()) {
            String key = entry.getKey();
            List<CharacterType> types = entry.getValue();

            if (!types.isEmpty()) {
                CharacterType first = types.get(0);
                CharacterTypeDTO parentDTO = new CharacterTypeDTO();
                parentDTO.setLabel(first.getGender() + first.getClassName());
                parentDTO.setValue(first.getGender() + first.getClassName());

                List<CharacterTypeDTO> children = types.stream()
                        .map(type -> {
                            CharacterTypeDTO childDTO = convertToDTO(type);
                            childDTO.setValue(type.getId().toString());
                            return childDTO;
                        })
                        .collect(Collectors.toList());

                parentDTO.setChildren(children);
                result.add(parentDTO);
            }
        }

        return result;
    }

    private CharacterTypeDTO convertToDTO(CharacterType characterType) {
        CharacterTypeDTO dto = new CharacterTypeDTO();
        dto.setId(characterType.getId());
        dto.setGender(characterType.getGender());
        dto.setClassName(characterType.getClassName());
        dto.setJobName(characterType.getJobName());
        dto.setJobNature(characterType.getJobNature());
        dto.setJobAttribute(characterType.getJobAttribute());
        dto.setSpecialCase(characterType.getSpecialCase());
        dto.setLabel(characterType.getJobName());
        return dto;
    }
}
