package com.example.demo.service;

import com.example.demo.dto.TitleDTO;
import com.example.demo.model.Title;

import java.util.List;

/**
 * 称号Service接口
 */
public interface TitleService {

    /**
     * 获取所有称号
     */
    List<TitleDTO> getAllTitles();

    /**
     * 根据ID获取称号
     */
    TitleDTO getTitleById(Long id);

    /**
     * 根据年份获取称号
     */
    List<TitleDTO> getTitlesByYear(Integer year);

    /**
     * 根据类型获取称号
     */
    List<TitleDTO> getTitlesByType(String type);

    /**
     * 获取所有年份
     */
    List<Integer> getAllYears();

    /**
     * 根据名称模糊搜索
     */
    List<TitleDTO> searchTitles(String name);

    /**
     * 穿戴称号到角色
     */
    void equipTitle(Long characterId, Long titleId);

    /**
     * 卸下角色称号
     */
    void unequipTitle(Long characterId, Long titleId);

    /**
     * 获取角色已穿戴的称号ID列表
     */
    List<Long> getEquippedTitleIds(Long characterId);

    /**
     * 创建称号
     */
    TitleDTO createTitle(TitleDTO titleDTO);

    /**
     * 更新称号
     */
    TitleDTO updateTitle(Long id, TitleDTO titleDTO);

    /**
     * 删除称号
     */
    void deleteTitle(Long id);

    /**
     * 转换为DTO
     */
    TitleDTO convertToDTO(Title title);
}