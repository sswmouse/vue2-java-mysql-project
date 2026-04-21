<!--
 * @Description: 附魔管理页面 - 支持角色选择
 * @Author: Claude Code
 * @Date: 2026-04-17
-->
<template>
    <div class="enchantment-page">
        <div class="page-header">
            <h1 class="page-title">
                附魔管理
            </h1>
        </div>

        <!-- 角色选择器 -->
        <div class="character-selector">
            <el-select
                v-model="selectedCharacterId"
                placeholder="请选择角色"
                filterable
                @change="onCharacterChange"
            >
                <el-option
                    v-for="char in characterList"
                    :key="char.id"
                    :label="char.characterName"
                    :value="char.id"
                >
                    <span style="float: left">{{ char.characterName }}</span>
                    <span style="float: right; color: #8492a6; font-size: 13px">{{ char.serverName }}</span>
                </el-option>
            </el-select>
        </div>

        <!-- 附魔配置 -->
        <div
            v-if="selectedCharacterId"
            class="config-section"
        >
            <enchant-config
                :character-id="selectedCharacterId"
                :enchant-data="enchantData"
                :options="enchantOptions"
                @saved="onSaved"
            />
        </div>

        <!-- 未选择角色时的提示 -->
        <div
            v-else
            class="empty-tip"
        >
            <el-empty description="请选择角色以查看和编辑附魔配置">
                <el-icon
                    class="empty-icon"
                    size="60"
                >
                    <el-icon-magic-stick />
                </el-icon>
            </el-empty>
        </div>
    </div>
</template>

<script>
import api from '@/api'
import EnchantConfig from '@/components/EnchantConfig.vue'

export default {
    name: 'Enchantment',
    components: {
        EnchantConfig
    },
    data() {
        return {
            characterList: [],
            selectedCharacterId: null,
            enchantData: {},
            enchantOptions: {}
        }
    },
    created() {
        this.loadCharacters()
        this.loadEnchantOptions()
    },
    methods: {
        async loadCharacters() {
            try {
                const data = await this.$request({
                    url: api.character.list,
                    method: 'get'
                })
                this.characterList = data || []
            } catch (error) {
                console.error('加载角色列表失败:', error)
            }
        },
        async loadEnchantOptions() {
            try {
                const data = await this.$request({
                    url: api.enchantOption.getGrouped,
                    method: 'get'
                })
                this.enchantOptions = data || {}
            } catch (error) {
                console.error('加载附魔选项失败:', error)
                this.enchantOptions = {}
            }
        },
        async onCharacterChange(characterId) {
            if (!characterId) {
                this.enchantData = {}
                return
            }
            try {
                const data = await this.$request({
                    url: api.enhancement.getEnhancementData(characterId),
                    method: 'get'
                })
                this.enchantData = data?.enchant || {}
            } catch (error) {
                console.error('加载附魔数据失败:', error)
                this.enchantData = {}
            }
        },
        onSaved() {
            // 可选：刷新数据
        }
    }
}
</script>

<style lang="less" scoped>
.enchantment-page {
    padding: 20px;
    min-height: 100%;
    background: var(--theme-bg-dark);

    .page-header {
        margin-bottom: 24px;

        h1 {
            margin: 0;
            color: var(--theme-text-primary);
            font-size: 24px;
        }
    }

    .character-selector {
        margin-bottom: 24px;

        .el-select {
            width: 300px;
        }
    }

    .config-section {
        margin-top: 20px;
    }

    .empty-tip {
        margin-top: 60px;
        text-align: center;

        .empty-icon {
            color: var(--theme-text-secondary);
        }
    }
}
</style>
