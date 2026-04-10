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
import java.util.Set;
import java.util.HashMap;
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

        // 1. 收集每个职业分类的性别分布
        Map<String, Set<String>> classGenderMap = allTypes.stream()
                .collect(Collectors.groupingBy(CharacterType::getClassName,
                         Collectors.mapping(CharacterType::getGender, Collectors.toSet())));

        // 2. 按性别和职业分类分组，得到每个分组的职业列表
        Map<String, List<CharacterType>> grouped = allTypes.stream()
                .collect(Collectors.groupingBy(type ->
                        type.getGender() + type.getClassName()
                ));

        List<CharacterTypeDTO> result = new ArrayList<>();

        // 3. 定义排序顺序
        Map<String, Integer> classOrder = new HashMap<>();
        String[] orderedClasses = {"鬼剑士", "格斗家", "神枪手", "魔法师", "圣职者", "暗夜使者", "守护者", "魔枪士", "枪剑士", "弓箭手"};
        for (int i = 0; i < orderedClasses.length; i++) {
            classOrder.put(orderedClasses[i], i);
        }

        // 4. 构建父级DTO列表
        for (Map.Entry<String, List<CharacterType>> entry : grouped.entrySet()) {
            List<CharacterType> types = entry.getValue();
            if (!types.isEmpty()) {
                CharacterType first = types.get(0);
                String className = first.getClassName();
                String gender = first.getGender();

                // 判断该职业分类是否有男女两种性别
                Set<String> genders = classGenderMap.get(className);
                String label;
                if (genders != null && genders.size() > 1) {
                    // 有男女，标签带性别前缀
                    label = gender + className;
                } else {
                    // 只有一种性别，标签不带性别前缀
                    label = className;
                }

                CharacterTypeDTO parentDTO = new CharacterTypeDTO();
                parentDTO.setLabel(label);
                parentDTO.setValue(label);

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

        // 5. 排序：先按职业分类顺序，再按性别（男在前，女在后）
        result.sort((a, b) -> {
            // 提取职业分类和性别
            String aLabel = a.getLabel();
            String bLabel = b.getLabel();

            // 获取职业分类（去掉性别前缀）
            String aClass = extractClassName(aLabel, classGenderMap);
            String bClass = extractClassName(bLabel, classGenderMap);

            // 获取排序值
            Integer aOrder = classOrder.get(aClass);
            Integer bOrder = classOrder.get(bClass);

            // 如果职业分类不在排序列表中，放到最后
            if (aOrder == null) aOrder = Integer.MAX_VALUE;
            if (bOrder == null) bOrder = Integer.MAX_VALUE;

            // 先按职业分类顺序排序
            if (!aOrder.equals(bOrder)) {
                return aOrder.compareTo(bOrder);
            }

            // 同一职业分类，有男女的按性别排序（男在前，女在后）
            String aGender = extractGender(aLabel, aClass, classGenderMap);
            String bGender = extractGender(bLabel, bClass, classGenderMap);

            // 男在前，女在后
            if ("男".equals(aGender) && "女".equals(bGender)) return -1;
            if ("女".equals(aGender) && "男".equals(bGender)) return 1;

            return 0;
        });

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

    private String extractClassName(String label, Map<String, Set<String>> classGenderMap) {
        // 遍历职业分类，检查label是否匹配
        for (Map.Entry<String, Set<String>> entry : classGenderMap.entrySet()) {
            String className = entry.getKey();
            Set<String> genders = entry.getValue();
            if (genders.size() > 1) {
                // 有男女，label可能是"男"+className或"女"+className
                if (label.equals("男" + className) || label.equals("女" + className)) {
                    return className;
                }
            } else {
                // 只有一种性别，label就是className
                if (label.equals(className)) {
                    return className;
                }
            }
        }
        // 如果找不到，返回原label
        return label;
    }

    private String extractGender(String label, String className, Map<String, Set<String>> classGenderMap) {
        Set<String> genders = classGenderMap.get(className);
        if (genders != null && genders.size() > 1) {
            // 有男女，label以性别开头
            if (label.startsWith("男")) {
                return "男";
            } else if (label.startsWith("女")) {
                return "女";
            }
        }
        return "";
    }
}
