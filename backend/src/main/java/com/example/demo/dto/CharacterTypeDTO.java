package com.example.demo.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import javax.validation.constraints.NotBlank;
import java.util.List;

/**
 * 角色类型DTO
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CharacterTypeDTO {

    private Long id;

    @NotBlank(message = "性别不能为空")
    private String gender;

    @NotBlank(message = "职业分类不能为空")
    private String className;

    @NotBlank(message = "具体职业不能为空")
    private String jobName;

    @NotBlank(message = "职业性质不能为空")
    private String jobNature;

    @NotBlank(message = "职业属性不能为空")
    private String jobAttribute;

    private Boolean specialCase;

    /**
     * 联级选择器使用的格式化标签
     */
    private String label;

    /**
     * 联级选择器使用的值
     */
    private String value;

    /**
     * 子选项（用于联级选择器）
     */
    private List<CharacterTypeDTO> children;
}
