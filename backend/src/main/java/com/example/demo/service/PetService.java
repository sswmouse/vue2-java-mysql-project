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
     * 穿戴宠物到角色
     */
    void equipPet(Long characterId, Long petId);

    /**
     * 卸下角色宠物
     */
    void unequipPet(Long characterId, Long petId);

    /**
     * 获取角色已穿戴的宠物ID列表
     */
    List<Long> getEquippedPetIds(Long characterId);

    /**
     * 创建宠物
     */
    PetDTO createPet(PetDTO petDTO);

    /**
     * 更新宠物
     */
    PetDTO updatePet(Long id, PetDTO petDTO);

    /**
     * 删除宠物
     */
    void deletePet(Long id);

    /**
     * 转换为DTO
     */
    PetDTO convertToDTO(Pet pet);
}