<!--
 * @Description: 附魔管理页面 - 卡片形式展示12个部位
 * @Author: Claude Code
 * @Date: 2026-04-20
 * @LastEditTime: 2026-04-21 17:11:05
 * @FilePath: /vue2-java-mysql-project/frontend/src/views/Enchant.vue
-->
<template>
    <div class="enchant-page">
        <!-- 页面标题 -->
        <div class="page-header">
            <div class="header-left">
                <h2 class="page-title">
                    <i class="el-icon-magic-stick" />
                    附魔管理
                </h2>
            </div>
            <div class="header-right">
                <el-select
                    v-model="selectedCharacterId"
                    placeholder="选择角色"
                    class="character-select"
                    size="small"
                    @change="handleCharacterChange"
                >
                    <el-option
                        v-for="char in characters"
                        :key="char.id"
                        :label="char.characterName"
                        :value="char.id"
                    >
                        <span>{{ char.characterName }}</span>
                        <span class="char-level">LV.{{ char.level }}</span>
                    </el-option>
                </el-select>
            </div>
        </div>

        <!-- 加载状态 -->
        <div
            v-loading="loading"
            class="loading-container"
        >
            <!-- 卡片网格 -->
            <div class="enchant-grid">
                <enchant-card
                    v-for="part in equipmentParts"
                    :key="part"
                    :part-name="part"
                    :enchant-data="getEnchantData(part)"
                    @click="handleCardClick"
                />
            </div>
        </div>

        <!-- 选择弹窗 -->
        <enchant-select-dialog
            :visible.sync="dialogVisible"
            :part-name="selectedPart"
            :options="enchantOptions"
            :current-option-id="currentOptionId"
            @select="handleSelect"
        />
    </div>
</template>

<script>
import enchantApi, { EQUIPMENT_PARTS } from '@/api/enchant'
import EnchantCard from '@/components/EnchantCard.vue'
import EnchantSelectDialog from '@/components/EnchantSelectDialog.vue'

/**
 * 附魔管理页面
 * 展示12个装备部位卡片，支持查看和配置附魔
 */
export default {
    name: 'Enchant',

    components: {
        EnchantCard,
        EnchantSelectDialog
    },

    data() {
        return {
            // 加载状态
            loading: false,
            // 角色列表
            characters: [],
            // 选中的角色ID
            selectedCharacterId: null,
            // 角色附魔配置
            characterEnchants: [],
            // 所有附魔选项
            allEnchantOptions: [],
            // 弹窗状态
            dialogVisible: false,
            // 选中的部位
            selectedPart: '',
            // 当前选项ID
            currentOptionId: null
        }
    },

    computed: {
        // 12个装备部位
        equipmentParts() {
            return EQUIPMENT_PARTS
        },

        // 当前选中部位的附魔选项
        enchantOptions() {
            return this.allEnchantOptions.filter(
                opt => opt.equipmentPart === this.selectedPart
            )
        }
    },

    mounted() {
        this.init()
    },

    methods: {
        /**
         * 初始化页面
         */
        async init() {
            await this.loadCharacters()
            await this.loadAllEnchantOptions()
            if (this.selectedCharacterId) {
                await this.loadCharacterEnchants()
            }
        },

        /**
         * 加载角色列表
         */
        async loadCharacters() {
            try {
                const userId = this.$store.getters['auth/userId']
                if (!userId) return

                const res = await this.$request({
                    url: `/api/characters/user/${userId}`,
                    method: 'get'
                })

                const characters = Array.isArray(res) ? res : (res.data || [])
                if (Array.isArray(characters)) {
                    this.characters = characters
                    if (this.characters.length > 0 && !this.selectedCharacterId) {
                        this.selectedCharacterId = this.characters[0].id
                    }
                }
            } catch (error) {
                console.error('加载角色列表失败:', error)
            }
        },

        /**
         * 加载所有附魔选项
         */
        async loadAllEnchantOptions() {
            try {
                const res = await this.$request({
                    url: enchantApi.enchantOptions,
                    method: 'get'
                })

                this.allEnchantOptions = Array.isArray(res) ? res : (res.data || [])
            } catch (error) {
                console.error('加载附魔选项失败:', error)
                this.allEnchantOptions = []
            }
        },

        /**
         * 加载角色附魔配置
         */
        async loadCharacterEnchants() {
            if (!this.selectedCharacterId) return

            this.loading = true
            try {
                const res = await this.$request({
                    url: enchantApi.characterEnchant(this.selectedCharacterId),
                    method: 'get'
                })

                this.characterEnchants = Array.isArray(res) ? res : (res.data || [])
            } catch (error) {
                console.error('加载角色附魔配置失败:', error)
                this.characterEnchants = []
            } finally {
                this.loading = false
            }
        },

        /**
         * 获取部位的附魔数据
         */
        getEnchantData(partName) {
            return this.characterEnchants.find(e => e.equipmentPart === partName)
        },

        /**
         * 处理角色切换
         */
        async handleCharacterChange(characterId) {
            this.selectedCharacterId = characterId
            await this.loadCharacterEnchants()
        },

        /**
         * 处理卡片点击
         */
        handleCardClick({ partName, enchantData }) {
            this.selectedPart = partName
            this.currentOptionId = enchantData ? enchantData.enchantOptionId : null
            this.dialogVisible = true
        },

        /**
         * 处理附魔选择
         */
        async handleSelect({ partName, enchantData }) {
            try {
                if (enchantData) {
                    // 保存附魔配置
                    await this.$request({
                        url: enchantApi.characterEnchant(this.selectedCharacterId),
                        method: 'post',
                        data: {
                            equipmentPart: partName,
                            enchantOptionId: enchantData.id,
                            enchantName: enchantData.name,
                            attributeValue: enchantData.attributeValue,
                            packageType: enchantData.packageType,
                            fame: enchantData.fame
                        }
                    })
                } else {
                    // 删除附魔配置
                    await this.$request({
                        url: enchantApi.characterEnchantByPart(this.selectedCharacterId, partName),
                        method: 'delete'
                    })
                }

                // 重新加载配置
                await this.loadCharacterEnchants()
                this.$message.success('保存成功')
            } catch (error) {
                console.error('保存失败:', error)
                this.$message.error('保存失败')
            }
        }
    }
}
</script>

<style lang="less" scoped>
@import '@/assets/styles/variables.less';

.enchant-page {
    padding: @spacing-xl;
    min-height: 100%;
    background: linear-gradient(180deg, var(--theme-bg-dark) 0%, var(--theme-primary) 100%);

    .page-header {
        display: flex;
        justify-content: space-between;
        align-items: flex-start;
        margin-bottom: @spacing-xl;
        background: var(--theme-bg-card);
        padding: @spacing-lg;
        border-radius: @border-radius-lg;
        border: 1px solid var(--theme-border);

        .header-left {
            flex: 1;
        }

        .header-right {
            flex-shrink: 0;
            padding-top: @spacing-xs;
        }

        .page-title {
            font-family: @font-heading, sans-serif;
            font-size: 24px;
            font-weight: @font-weight-bold;
            color: var(--theme-accent);
            margin: 0 0 @spacing-xs 0;
            display: flex;
            align-items: center;
            gap: @spacing-sm;

            i {
                font-size: 28px;
            }
        }

        .character-select {
            min-width: 160px;

            .char-level {
                float: right;
                color: var(--theme-text-secondary);
                font-size: 12px;
            }
        }
    }

    .loading-container {
        min-height: 400px;
    }

    .enchant-grid {
        display: grid;
        grid-template-columns: repeat(4, 1fr);
        gap: @spacing-lg;

        @media (max-width: @breakpoint-xl) {
            grid-template-columns: repeat(3, 1fr);
        }

        @media (max-width: @breakpoint-lg) {
            grid-template-columns: repeat(2, 1fr);
        }

        @media (max-width: @breakpoint-sm) {
            grid-template-columns: 1fr;
        }
    }
}

/deep/ .el-select-dropdown {
    background: var(--theme-bg-card);
    border-color: var(--theme-border);

    .el-select-dropdown__item {
        color: var(--theme-text-primary);

        &:hover {
            background: var(--theme-bg-hover);
        }
    }
}
</style>
