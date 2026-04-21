package com.example.demo.service;

import com.example.demo.dto.PetDTO;
import com.example.demo.model.Pet;

import java.util.List;

/**
 * 宠物Service接口
 */
public interface PetService {

    /**
     * 获取所有宠物
     */
    List<PetDTO> getAllPets();

    /**
     * 根据ID获取宠物
     */
    PetDTO getPetById(Long id);

    /**
     * 根据年份获取宠物
     */
    List<PetDTO> getPetsByYear(Integer year);

    /**
     * 根据类型获取宠物
     */
    List<PetDTO> getPetsByType(String type);

    /**
     * 获取所有年份
     */
    List<Integer> getAllYears();

    /**
     * 根据名称模糊搜索
     */
    List<PetDTO> searchPets(String name);

    /**
     * 转换为DTO
     */
    PetDTO convertToDTO(Pet pet);
}