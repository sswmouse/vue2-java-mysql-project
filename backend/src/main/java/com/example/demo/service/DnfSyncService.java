package com.example.demo.service;

import java.util.Map;

/**
 * DNF角色信息同步服务接口
 */
public interface DnfSyncService {

    /**
     * 根据角色名称查询DNF角色信息
     * @param serverId 区服ID
     * @param roleName 角色名称
     * @return 角色信息
     */
    Map<String, Object> fetchCharacterInfo(String serverId, String roleName);

    /**
     * 根据冒险团名称获取名下所有角色
     * @param serverId 区服ID
     * @param adventureName 冒险团名称
     * @return 角色列表
     */
    Map<String, Object> fetchCharactersByAdventure(String serverId, String adventureName);

    /**
     * 手动同步单个角色
     * @param characterId 角色ID
     * @return 同步结果
     */
    Map<String, Object> syncCharacter(Long characterId);

    /**
     * 同步用户所有角色
     * @param userId 用户ID
     * @return 同步结果
     */
    Map<String, Object> syncUserCharacters(Long userId);

    /**
     * 每日自动同步（定时任务调用）
     */
    void dailySyncAllUsers();
}
