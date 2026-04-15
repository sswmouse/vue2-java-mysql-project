package com.example.demo.service.impl;

import com.example.demo.model.Character;
import com.example.demo.repository.CharacterRepository;
import com.example.demo.service.DnfSyncService;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDateTime;
import java.util.*;

/**
 * DNF角色信息同步服务实现类
 * 调用DNF助手API获取角色信息
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class DnfSyncServiceImpl implements DnfSyncService {

    private final CharacterRepository characterRepository;
    private final ObjectMapper objectMapper;
    private final RestTemplate restTemplate = new RestTemplate();

    // DNF API配置
    @Value("${dnf.api.base-url:https://dnf.qq.com}")
    private String apiBaseUrl;

    @Value("${dnf.api.role-query-url:/cp/a20220322role/index.html}")
    private String roleQueryUrl;

    @Value("${dnf.api.enabled:false}")
    private boolean apiEnabled;

    @Value("${dnf.api.mock-mode:true}")
    private boolean mockMode;

    // 区服ID映射（跨区）
    private static final Map<String, String> SERVER_ID_MAP = new LinkedHashMap<>();
    static {
        // 跨区
        SERVER_ID_MAP.put("跨一", "1");
        SERVER_ID_MAP.put("跨二", "2");
        SERVER_ID_MAP.put("跨三", "3");
        SERVER_ID_MAP.put("跨四", "4");
        SERVER_ID_MAP.put("跨五", "5");
        SERVER_ID_MAP.put("跨六大区", "6");
        // 传统大区
        SERVER_ID_MAP.put("广东一区", "10");
        SERVER_ID_MAP.put("广东三区", "12");
        SERVER_ID_MAP.put("广东四区", "13");
        SERVER_ID_MAP.put("广东五区", "14");
        SERVER_ID_MAP.put("广东六区", "15");
        SERVER_ID_MAP.put("广东七区", "16");
        SERVER_ID_MAP.put("广东八区", "17");
        SERVER_ID_MAP.put("广东十一区", "20");
        SERVER_ID_MAP.put("华北一区", "30");
        SERVER_ID_MAP.put("华北二区", "31");
        SERVER_ID_MAP.put("华北三区", "32");
        SERVER_ID_MAP.put("华东一区", "40");
        SERVER_ID_MAP.put("华东二区", "41");
        SERVER_ID_MAP.put("华东三区", "42");
        SERVER_ID_MAP.put("华东四区", "43");
        SERVER_ID_MAP.put("华东五区", "44");
        SERVER_ID_MAP.put("华中一区", "50");
        SERVER_ID_MAP.put("华中二区", "51");
        SERVER_ID_MAP.put("东北一区", "60");
        SERVER_ID_MAP.put("东北二区", "61");
        SERVER_ID_MAP.put("西南一区", "70");
        SERVER_ID_MAP.put("西南二区", "71");
        SERVER_ID_MAP.put("西北一区", "80");
        SERVER_ID_MAP.put("福建一区", "90");
        SERVER_ID_MAP.put("河南一区", "100");
        SERVER_ID_MAP.put("河南二区", "101");
        SERVER_ID_MAP.put("河南三区", "102");
        SERVER_ID_MAP.put("西南一区", "70");
        // 优先跨区
        SERVER_ID_MAP.put("内蒙古一区", "21");
        SERVER_ID_MAP.put("云南一区", "22");
        SERVER_ID_MAP.put("广西一区", "23");
        SERVER_ID_MAP.put("新疆一区", "24");
    }

    @Override
    public Map<String, Object> fetchCharacterInfo(String serverId, String roleName) {
        Map<String, Object> result = new HashMap<>();

        // Mock模式返回模拟数据
        if (mockMode) {
            log.info("Mock模式：模拟获取角色信息 serverId={}, roleName={}", serverId, roleName);
            return generateMockCharacterData(serverId, roleName);
        }

        try {
            // 构建请求参数
            Map<String, String> params = new HashMap<>();
            params.put("area", serverId);
            params.put("name", roleName);

            // 调用DNF API
            String response = callDnfApi(roleQueryUrl, params);

            if (response != null) {
                JsonNode data = objectMapper.readTree(response);
                result.put("success", true);
                result.put("data", parseCharacterData(data));
            } else {
                result.put("success", false);
                result.put("message", "API调用失败，请检查API配置");
            }
        } catch (Exception e) {
            log.error("获取角色信息失败: serverId={}, roleName={}", serverId, roleName, e);
            result.put("success", false);
            result.put("message", "获取角色信息失败: " + e.getMessage());
        }

        return result;
    }

    @Override
    public Map<String, Object> fetchCharactersByAdventure(String serverId, String adventureName) {
        Map<String, Object> result = new HashMap<>();

        if (mockMode) {
            log.info("Mock模式：模拟获取冒险团角色列表 serverId={}, adventureName={}", serverId, adventureName);
            return generateMockCharacterList(serverId, adventureName);
        }

        try {
            Map<String, String> params = new HashMap<>();
            params.put("area", serverId);
            params.put("keyword", adventureName);

            String response = callDnfApi(roleQueryUrl, params);

            if (response != null) {
                JsonNode data = objectMapper.readTree(response);
                List<Map<String, Object>> characters = parseCharacterList(data);
                result.put("success", true);
                result.put("data", characters);
            } else {
                result.put("success", false);
                result.put("message", "API调用失败");
            }
        } catch (Exception e) {
            log.error("获取冒险团角色列表失败: serverId={}, adventureName={}", serverId, adventureName, e);
            result.put("success", false);
            result.put("message", "获取冒险团角色列表失败: " + e.getMessage());
        }

        return result;
    }

    @Override
    @Transactional
    public Map<String, Object> syncCharacter(Long characterId) {
        Map<String, Object> result = new HashMap<>();
        LocalDateTime startTime = LocalDateTime.now();

        try {
            Character character = characterRepository.findById(characterId)
                    .orElseThrow(() -> new RuntimeException("角色不存在"));

            // 更新同步状态
            character.setSyncStatus("syncing");
            characterRepository.save(character);

            // 获取区服ID
            String serverId = getServerId(character.getServerName());
            if (serverId == null) {
                serverId = character.getServerName();
            }

            // 调用API获取最新数据
            Map<String, Object> apiResult = fetchCharacterInfo(
                    serverId,
                    character.getCharacterName()
            );

            if ((Boolean) apiResult.getOrDefault("success", false)) {
                @SuppressWarnings("unchecked")
                Map<String, Object> characterData = (Map<String, Object>) apiResult.get("data");

                // 更新角色数据
                updateCharacterFromApi(character, characterData);

                character.setSyncStatus("idle");
                character.setSyncErrorMsg(null);
                result.put("message", "同步成功");
            } else {
                character.setSyncStatus("error");
                character.setSyncErrorMsg((String) apiResult.get("message"));
                result.put("message", apiResult.get("message"));
            }

            character.setLastSyncTime(LocalDateTime.now());
            characterRepository.save(character);

            result.put("success", true);
            result.put("characterId", characterId);

            long duration = java.time.Duration.between(startTime, LocalDateTime.now()).toMillis();
            log.info("角色同步完成: id={}, name={}, duration={}ms",
                    characterId, character.getCharacterName(), duration);

        } catch (Exception e) {
            log.error("同步角色失败: characterId={}", characterId, e);
            result.put("success", false);
            result.put("message", "同步失败: " + e.getMessage());

            characterRepository.findById(characterId).ifPresent(character -> {
                character.setSyncStatus("error");
                character.setSyncErrorMsg(e.getMessage());
                characterRepository.save(character);
            });
        }

        return result;
    }

    @Override
    @Transactional
    public Map<String, Object> syncUserCharacters(Long userId) {
        Map<String, Object> result = new HashMap<>();
        List<Map<String, Object>> syncResults = new ArrayList<>();

        try {
            List<Character> characters = characterRepository.findByUserId(userId);

            for (Character character : characters) {
                if (character.getServerName() == null || character.getServerName().isEmpty()) {
                    continue;
                }

                Map<String, Object> syncResult = syncCharacter(character.getId());
                syncResults.add(syncResult);
            }

            result.put("success", true);
            result.put("total", characters.size());
            result.put("results", syncResults);
            result.put("message", String.format("成功同步 %d 个角色", syncResults.size()));

        } catch (Exception e) {
            log.error("同步用户角色失败: userId={}", userId, e);
            result.put("success", false);
            result.put("message", "同步失败: " + e.getMessage());
        }

        return result;
    }

    /**
     * 每日自动同步（凌晨2点执行）
     */
    @Override
    @Scheduled(cron = "0 0 2 * * ?")
    public void dailySyncAllUsers() {
        if (mockMode) {
            log.info("Mock模式已启用，跳过每日同步任务");
            return;
        }

        log.info("开始执行每日角色同步任务");

        try {
            List<Character> allCharacters = characterRepository.findAll();

            int successCount = 0;
            int failCount = 0;

            for (Character character : allCharacters) {
                if (character.getServerName() == null || character.getServerName().isEmpty()) {
                    continue;
                }

                try {
                    syncCharacter(character.getId());
                    successCount++;
                } catch (Exception e) {
                    failCount++;
                    log.warn("同步角色失败: id={}, name={}", character.getId(), character.getCharacterName());
                }

                Thread.sleep(500);
            }

            log.info("每日同步任务完成: 成功={}, 失败={}", successCount, failCount);

        } catch (Exception e) {
            log.error("每日同步任务执行失败", e);
        }
    }

    /**
     * 调用DNF API
     */
    private String callDnfApi(String url, Map<String, String> params) {
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            headers.set("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/120.0.0.0 Safari/537.36");
            headers.set("Referer", apiBaseUrl);

            HttpEntity<Map<String, String>> request = new HttpEntity<>(params, headers);

            ResponseEntity<String> response = restTemplate.exchange(
                    apiBaseUrl + url,
                    HttpMethod.POST,
                    request,
                    String.class
            );

            return response.getBody();
        } catch (Exception e) {
            log.error("API调用失败: url={}, params={}", url, params, e);
            return null;
        }
    }

    /**
     * 根据区服名称获取区服ID
     */
    private String getServerId(String serverName) {
        if (serverName == null || serverName.isEmpty()) {
            return null;
        }
        return SERVER_ID_MAP.get(serverName);
    }

    /**
     * 生成模拟角色数据（用于测试）
     */
    private Map<String, Object> generateMockCharacterData(String serverId, String roleName) {
        Map<String, Object> result = new HashMap<>();
        Random random = new Random();

        // 生成随机但合理的数值
        int level = 110 + random.nextInt(6); // 110-115
        int fame = 50000 + random.nextInt(100000); // 5万-15万名望

        result.put("success", true);
        result.put("data", Map.of(
                "characterName", roleName != null ? roleName : "测试角色",
                "level", level,
                "jobName", "剑魂",
                "fameValue", fame,
                "serverName", serverId,
                "status", Map.of(
                        "strength", 5000 + random.nextInt(2000),
                        "intelligence", 3000 + random.nextInt(1000),
                        "spirit", 4000 + random.nextInt(1500),
                        "vitality", 4500 + random.nextInt(1500)
                ),
                "attack", Map.of(
                        "physicalAttack", 80000 + random.nextInt(40000),
                        "magicalAttack", 60000 + random.nextInt(30000),
                        "independentAttack", 30000 + random.nextInt(10000)
                ),
                "elements", Map.of(
                        "lightElement", 300 + random.nextInt(100),
                        "fireElement", 280 + random.nextInt(100),
                        "iceElement", 250 + random.nextInt(100),
                        "darkElement", 260 + random.nextInt(100)
                ),
                "equipments", generateMockEquipments()
        ));

        return result;
    }

    /**
     * 生成模拟装备数据
     */
    private List<Map<String, Object>> generateMockEquipments() {
        List<Map<String, Object>> equipments = new ArrayList<>();
        String[] partNames = {"武器", "头肩", "上衣", "下装", "腰带", "鞋", "手镯", "项链", "戒指", "耳环", "辅助装备", "魔法石"};
        Random random = new Random();

        for (String part : partNames) {
            Map<String, Object> equip = new HashMap<>();
            equip.put("name", "自定义史诗装备");
            equip.put("part", part);
            equip.put("level", 437); // 固定值
            equip.put("enchant", "属强+30");
            equip.put("strengthen", 12 + random.nextInt(5));
            equip.put("increase", 8 + random.nextInt(4));
            equipments.add(equip);
        }

        return equipments;
    }

    /**
     * 生成模拟角色列表
     */
    private Map<String, Object> generateMockCharacterList(String serverId, String adventureName) {
        Map<String, Object> result = new HashMap<>();
        Random random = new Random();

        List<Map<String, Object>> characters = new ArrayList<>();
        String[] jobNames = {"剑魂", "红眼", "瞎子", "元素", "战法", "奶爸", "奶妈"};

        for (int i = 0; i < 5; i++) {
            Map<String, Object> character = new HashMap<>();
            character.put("characterName", adventureName + "_角色" + (i + 1));
            character.put("level", 110 + random.nextInt(6));
            character.put("jobName", jobNames[random.nextInt(jobNames.length)]);
            character.put("fameValue", 50000 + random.nextInt(100000));
            character.put("serverName", serverId);
            characters.add(character);
        }

        result.put("success", true);
        result.put("data", characters);
        return result;
    }

    /**
     * 解析角色数据
     */
    @SuppressWarnings("unchecked")
    private Map<String, Object> parseCharacterData(JsonNode data) {
        Map<String, Object> result = new HashMap<>();

        if (data == null) {
            return result;
        }

        // 尝试解析不同的响应格式
        JsonNode roleInfo = data.has("data") ? data.get("data") : data;

        result.put("characterName", getTextValue(roleInfo, "name"));
        result.put("level", getIntValue(roleInfo, "level"));
        result.put("jobName", getTextValue(roleInfo, "job"));
        result.put("fameValue", getIntValue(roleInfo, "fame"));
        result.put("serverName", getTextValue(roleInfo, "area"));

        // 四维属性 - 尝试多种字段名
        Map<String, Object> status = new HashMap<>();
        status.put("strength", getIntValue(roleInfo, "STR"));
        status.put("intelligence", getIntValue(roleInfo, "INT"));
        status.put("spirit", getIntValue(roleInfo, "SPI"));
        status.put("vitality", getIntValue(roleInfo, "VIT"));
        result.put("status", status);

        // 三攻
        Map<String, Object> attack = new HashMap<>();
        attack.put("physicalAttack", getIntValue(roleInfo, "PAD"));
        attack.put("magicalAttack", getIntValue(roleInfo, "MAD"));
        attack.put("independentAttack", getIntValue(roleInfo, "IED"));
        result.put("attack", attack);

        // 属强
        Map<String, Object> elements = new HashMap<>();
        elements.put("lightElement", getIntValue(roleInfo, "LGT"));
        elements.put("fireElement", getIntValue(roleInfo, "FIR"));
        elements.put("iceElement", getIntValue(roleInfo, "ICE"));
        elements.put("darkElement", getIntValue(roleInfo, "DARK"));
        result.put("elements", elements);

        // 装备概要
        result.put("equipments", parseEquipments(roleInfo.get("equip")));

        return result;
    }

    /**
     * 解析角色列表
     */
    private List<Map<String, Object>> parseCharacterList(JsonNode data) {
        List<Map<String, Object>> result = new ArrayList<>();

        if (data == null || !data.has("data") || !data.get("data").isArray()) {
            return result;
        }

        for (JsonNode role : data.get("data")) {
            Map<String, Object> roleMap = new HashMap<>();
            roleMap.put("characterName", getTextValue(role, "name"));
            roleMap.put("level", getIntValue(role, "level"));
            roleMap.put("jobName", getTextValue(role, "job"));
            roleMap.put("fameValue", getIntValue(role, "fame"));
            roleMap.put("serverName", getTextValue(role, "area"));
            result.add(roleMap);
        }

        return result;
    }

    /**
     * 解析装备信息
     */
    private List<Map<String, Object>> parseEquipments(JsonNode equipData) {
        List<Map<String, Object>> equipments = new ArrayList<>();

        if (equipData == null || !equipData.isArray()) {
            return equipments;
        }

        for (JsonNode equip : equipData) {
            Map<String, Object> equipMap = new HashMap<>();
            equipMap.put("name", getTextValue(equip, "name"));
            equipMap.put("part", getTextValue(equip, "pos"));
            equipMap.put("level", getIntValue(equip, "level"));
            equipMap.put("enchant", getTextValue(equip, "enchant"));
            equipMap.put("strengthen", getIntValue(equip, "strengthen"));
            equipMap.put("increase", getIntValue(equip, "increase"));
            equipments.add(equipMap);
        }

        return equipments;
    }

    /**
     * 从API数据更新角色信息
     */
    @SuppressWarnings("unchecked")
    private void updateCharacterFromApi(Character character, Map<String, Object> data) {
        // 更新基础信息
        if (data.containsKey("level")) {
            character.setLevel((Integer) data.get("level"));
        }
        if (data.containsKey("fameValue")) {
            character.setFameValue((Integer) data.get("fameValue"));
        }

        // 更新四维属性
        Map<String, Object> status = (Map<String, Object>) data.get("status");
        if (status != null) {
            if (status.containsKey("strength")) {
                character.setStrength((Integer) status.get("strength"));
            }
            if (status.containsKey("intelligence")) {
                character.setIntelligence((Integer) status.get("intelligence"));
            }
            if (status.containsKey("spirit")) {
                character.setSpirit((Integer) status.get("spirit"));
            }
            if (status.containsKey("vitality")) {
                character.setVitality((Integer) status.get("vitality"));
            }
        }

        // 更新三攻
        Map<String, Object> attack = (Map<String, Object>) data.get("attack");
        if (attack != null) {
            if (attack.containsKey("physicalAttack")) {
                character.setPhysicalAttack((Integer) attack.get("physicalAttack"));
            }
            if (attack.containsKey("magicalAttack")) {
                character.setMagicalAttack((Integer) attack.get("magicalAttack"));
            }
            if (attack.containsKey("independentAttack")) {
                character.setIndependentAttack((Integer) attack.get("independentAttack"));
            }
        }

        // 更新属强
        Map<String, Object> elements = (Map<String, Object>) data.get("elements");
        if (elements != null) {
            if (elements.containsKey("lightElement")) {
                character.setLightElement((Integer) elements.get("lightElement"));
            }
            if (elements.containsKey("fireElement")) {
                character.setFireElement((Integer) elements.get("fireElement"));
            }
            if (elements.containsKey("iceElement")) {
                character.setIceElement((Integer) elements.get("iceElement"));
            }
            if (elements.containsKey("darkElement")) {
                character.setDarkElement((Integer) elements.get("darkElement"));
            }
        }

        // 更新装备概要JSON
        List<Map<String, Object>> equipments = (List<Map<String, Object>>) data.get("equipments");
        if (equipments != null) {
            try {
                character.setEquipmentSummary(objectMapper.writeValueAsString(equipments));
            } catch (Exception e) {
                log.error("序列化装备信息失败", e);
            }
        }
    }

    private String getTextValue(JsonNode node, String field) {
        return node != null && node.has(field) ? node.get(field).asText() : "";
    }

    private Integer getIntValue(JsonNode node, String field) {
        return node != null && node.has(field) ? node.get(field).asInt() : 0;
    }
}
