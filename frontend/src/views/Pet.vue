<!--
 * @Description: 宠物管理页面 - 支持角色选择
 * @Author: Claude Code
 * @Date: 2026-04-17
-->
<template>
    <div class="pet-page-wrapper">
        <div class="page-header">
            <h1 class="page-title">宠物管理</h1>
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

        <!-- 宠物配置 -->
        <div
            v-if="selectedCharacterId"
            class="config-section"
        >
            <pet-config
                :character-id="selectedCharacterId"
                :pet-data="petData"
                :options="petOptions"
                @saved="onSaved"
            />
        </div>

        <!-- 未选择角色时的提示 -->
        <div
            v-else
            class="empty-tip"
        >
            <el-empty description="请选择角色以查看和编辑宠物配置">
                <el-icon
                    class="empty-icon"
                    size="60"
                >
                    <el-icon-dog />
                </el-icon>
            </el-empty>
        </div>
    </div>
</template>

<script>
import api from '@/api'
import PetConfig from '@/components/PetConfig.vue'

export default {
    name: 'PetPageWrapper',
    components: {
        PetConfig
    },
    data() {
        return {
            characterList: [],
            selectedCharacterId: null,
            petData: {},
            petOptions: {}
        }
    },
    created() {
        this.loadCharacters()
        this.loadPetOptions()
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
        async loadPetOptions() {
            try {
                const data = await this.$request({
                    url: api.petOption.getGrouped,
                    method: 'get'
                })
                this.petOptions = data || {}
            } catch (error) {
                console.error('加载宠物选项失败:', error)
                this.petOptions = {}
            }
        },
        async onCharacterChange(characterId) {
            if (!characterId) {
                this.petData = {}
                return
            }
            try {
                const data = await this.$request({
                    url: api.enhancement.getEnhancementData(characterId),
                    method: 'get'
                })
                this.petData = data?.pet || {}
            } catch (error) {
                console.error('加载宠物数据失败:', error)
                this.petData = {}
            }
        },
        onSaved() {
            // 可选：刷新数据
        }
    }
}
</script>

<style lang="less" scoped>
.pet-page-wrapper {
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
