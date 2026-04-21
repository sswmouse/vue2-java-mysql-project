<!--
 * @Description: 增幅管理页面 - 卡片形式展示12个部位
 * @Author: Claude Code
 * @Date: 2026-04-17
 * @LastEditTime: 2026-04-21
 * @FilePath: /frontend/src/views/Enhancement.vue
-->
<template>
    <div class="enhancement-page">
        <!-- 页面标题 -->
        <div class="page-header">
            <div class="header-left">
                <h2 class="page-title">
                    <i class="el-icon-magic-stick" />
                    增幅管理
                </h2>
            </div>
            <div class="header-right">
                <el-button
                    size="small"
                    class="quick-set-btn"
                    @click="showQuickSetDialog"
                >
                    <i class="el-icon-setting" />
                    一键设置
                </el-button>
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
            <div class="amplify-grid">
                <amplify-card
                    v-for="part in equipmentParts"
                    :key="part"
                    :part-name="part"
                    :amplify-data="getAmplifyData(part)"
                    @click="handleCardClick"
                />
            </div>
        </div>

        <!-- 选择弹窗 -->
        <amplify-select-dialog
            :visible.sync="dialogVisible"
            :part-name="selectedPart"
            :current-data="currentData"
            @select="handleSelect"
        />

        <!-- 一键设置弹窗 -->
        <el-dialog
            title="一键设置增幅"
            :visible.sync="quickSetDialogVisible"
            width="400px"
            class="quick-set-dialog"
            append-to-body
        >
            <div class="quick-set-content">
                <p class="quick-set-tip">将所有非武器部位设置为：</p>
                <div class="quick-set-form">
                    <el-radio-group v-model="quickSetType">
                        <el-radio label="增幅">增幅</el-radio>
                        <el-radio label="强化">强化</el-radio>
                    </el-radio-group>
                    <el-select
                        v-model="quickSetLevel"
                        placeholder="选择等级"
                        class="quick-set-level"
                    >
                        <el-option
                            v-for="level in 20"
                            :key="level"
                            :label="`+${level}`"
                            :value="level"
                        />
                    </el-select>
                </div>
                <p class="quick-set-hint">（武器部位不会修改）</p>
            </div>
            <div slot="footer">
                <el-button @click="quickSetDialogVisible = false">取消</el-button>
                <el-button
                    type="warning"
                    @click="handleQuickSet"
                >
                    确认设置
                </el-button>
            </div>
        </el-dialog>
    </div>
</template>

<script>
import amplifyApi, { EQUIPMENT_PARTS } from '@/api/amplify'
import AmplifyCard from '@/components/AmplifyCard.vue'
import AmplifySelectDialog from '@/components/AmplifySelectDialog.vue'

/**
 * 增幅管理页面
 * 展示12个装备部位卡片，支持查看和配置增幅/强化
 */
export default {
    name: 'Enhancement',

    components: {
        AmplifyCard,
        AmplifySelectDialog
    },

    data() {
        return {
            // 加载状态
            loading: false,
            // 角色列表
            characters: [],
            // 选中的角色ID
            selectedCharacterId: null,
            // 角色增幅配置
            characterAmplifies: [],
            // 弹窗状态
            dialogVisible: false,
            // 选中的部位
            selectedPart: '',
            // 当前数据
            currentData: null,
            // 一键设置弹窗
            quickSetDialogVisible: false,
            quickSetType: '增幅',
            quickSetLevel: 10
        }
    },

    computed: {
        // 12个装备部位
        equipmentParts() {
            return EQUIPMENT_PARTS
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
            if (this.selectedCharacterId) {
                await this.loadCharacterAmplifies()
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
         * 加载角色增幅配置
         */
        async loadCharacterAmplifies() {
            if (!this.selectedCharacterId) return

            this.loading = true
            try {
                const res = await this.$request({
                    url: amplifyApi.characterAmplify(this.selectedCharacterId),
                    method: 'get'
                })

                this.characterAmplifies = Array.isArray(res) ? res : (res.data || [])
            } catch (error) {
                console.error('加载角色增幅配置失败:', error)
                this.characterAmplifies = []
            } finally {
                this.loading = false
            }
        },

        /**
         * 获取部位的增幅数据
         */
        getAmplifyData(partName) {
            return this.characterAmplifies.find(a => a.equipmentPart === partName)
        },

        /**
         * 处理角色切换
         */
        async handleCharacterChange(characterId) {
            this.selectedCharacterId = characterId
            await this.loadCharacterAmplifies()
        },

        /**
         * 处理卡片点击
         */
        handleCardClick({ partName, amplifyData }) {
            this.selectedPart = partName
            this.currentData = amplifyData
            this.dialogVisible = true
        },

        /**
         * 处理增幅选择
         */
        async handleSelect({ partName, amplifyData }) {
            try {
                if (amplifyData) {
                    // 保存增幅配置
                    await this.$request({
                        url: amplifyApi.characterAmplify(this.selectedCharacterId),
                        method: 'post',
                        data: {
                            equipmentPart: partName,
                            amplifyType: amplifyData.amplifyType,
                            amplifyLevel: amplifyData.amplifyLevel
                        }
                    })
                } else {
                    // 删除增幅配置
                    await this.$request({
                        url: amplifyApi.characterAmplifyByPart(this.selectedCharacterId, partName),
                        method: 'delete'
                    })
                }

                // 重新加载配置
                await this.loadCharacterAmplifies()
                this.$message.success('保存成功')
            } catch (error) {
                console.error('保存失败:', error)
                this.$message.error('保存失败')
            }
        },

        /**
         * 显示一键设置弹窗
         */
        showQuickSetDialog() {
            this.quickSetDialogVisible = true
        },

        /**
         * 处理一键设置
         */
        async handleQuickSet() {
            if (!this.quickSetLevel) {
                this.$message.warning('请选择增幅等级')
                return
            }

            try {
                // 获取非武器的11个部位
                const nonWeaponParts = EQUIPMENT_PARTS.filter(part => part !== '武器')

                // 批量保存
                const promises = nonWeaponParts.map(part =>
                    this.$request({
                        url: amplifyApi.characterAmplify(this.selectedCharacterId),
                        method: 'post',
                        data: {
                            equipmentPart: part,
                            amplifyType: this.quickSetType,
                            amplifyLevel: this.quickSetLevel
                        }
                    })
                )

                await Promise.all(promises)

                // 重新加载配置
                await this.loadCharacterAmplifies()
                this.quickSetDialogVisible = false
                this.$message.success('一键设置成功')
            } catch (error) {
                console.error('一键设置失败:', error)
                this.$message.error('一键设置失败')
            }
        }
    }
}
</script>

<style lang="less" scoped>
@import '@/assets/styles/variables.less';

.enhancement-page {
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

        .quick-set-btn {
            margin-right: 12px;
            border: 1px solid var(--theme-border);
            background: var(--theme-bg-hover);
            color: var(--theme-text-secondary);
            transition: all 0.2s;

            i {
                margin-right: 4px;
            }

            &:hover {
                border-color: var(--theme-primary);
                color: var(--theme-primary);
                background: rgba(123, 47, 255, 0.1);
            }
        }
    }

    .loading-container {
        min-height: 400px;
    }

    .amplify-grid {
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

.quick-set-dialog {
    /deep/ .el-dialog {
        background: linear-gradient(145deg, @dnf-bg-card 0%, @dnf-primary-dark 100%);
        border: 2px solid @dnf-border-light;
        border-radius: @border-radius-xl;

        .el-dialog__header {
            padding: @spacing-lg;
            border-bottom: 1px solid @dnf-border-dark;

            .el-dialog__title {
                color: @dnf-secondary-gold;
                font-family: @font-heading, sans-serif;
                font-size: 18px;
            }
        }

        .el-dialog__body {
            padding: @spacing-lg;
        }
    }

    .quick-set-content {
        .quick-set-tip {
            font-size: 14px;
            color: @dnf-text-primary;
            margin: 0 0 @spacing-md 0;
        }

        .quick-set-form {
            display: flex;
            align-items: center;
            gap: @spacing-md;

            .quick-set-level {
                width: 120px;
            }
        }

        .quick-set-hint {
            font-size: 12px;
            color: @dnf-text-muted;
            margin: @spacing-sm 0 0 0;
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
