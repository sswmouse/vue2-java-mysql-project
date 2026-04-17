<!--
 * @Description: 装备管理页面 - 展示12套装备卡片
 * @Author: Claude Code
 * @Date: 2026-04-17
 * @LastEditTime: 2026-04-17
 * @FilePath: /vue2-java-mysql-project/frontend/src/views/Equipment.vue
-->
<template>
    <div class="equipment-page">
        <!-- 页面标题 -->
        <div class="page-header">
            <h2 class="page-title">
                <i class="el-icon-suitcase-1" />
                装备套装管理
            </h2>
            <p class="page-subtitle">
                管理角色的115级重力之泉装备套装，查看积分和阶段进度
            </p>
        </div>

        <!-- 加载状态 -->
        <div
            v-loading="loading"
            class="loading-container"
        >
            <!-- 套装卡片网格 -->
            <div class="equipment-grid">
                <equipment-set-card
                    v-for="set in equipmentSets"
                    :key="set.id"
                    :set-data="set"
                    :is-active="activeSetId === set.id"
                    :loading="set.loading"
                    @click="handleSetClick"
                />
            </div>

            <!-- 空状态 -->
            <div
                v-if="!loading && equipmentSets.length === 0"
                class="empty-state"
            >
                <i class="el-icon-suitcase" />
                <p>暂无装备套装数据</p>
                <el-button
                    type="primary"
                    @click="loadEquipmentSets"
                >
                    刷新数据
                </el-button>
            </div>
        </div>

        <!-- 角色选择 -->
        <div class="character-selector">
            <el-select
                v-model="selectedCharacterId"
                placeholder="选择角色"
                class="character-select"
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

        <!-- 装备详情弹窗 -->
        <equipment-detail-dialog
            :visible.sync="dialogVisible"
            :set-data="selectedSetData"
            @calculate="handleCalculate"
            @save="handleSave"
            @close="handleDialogClose"
        />
    </div>
</template>

<script>
import equipmentApi from '@/api/equipment'
import EquipmentSetCard from '@/components/EquipmentSetCard.vue'
import EquipmentDetailDialog from '@/components/EquipmentDetailDialog.vue'

/**
 * 装备管理页面
 * 展示12套装备套装卡片，支持查看详情和配置
 */
export default {
    name: 'Equipment',

    components: {
        EquipmentSetCard,
        EquipmentDetailDialog
    },

    data() {
        return {
            // 加载状态
            loading: false,
            // 套装列表
            equipmentSets: [],
            // 角色列表
            characters: [],
            // 选中的角色ID
            selectedCharacterId: null,
            // 弹窗显示状态
            dialogVisible: false,
            // 选中的套装数据
            selectedSetData: {
                id: 0,
                setName: '',
                setIndex: 0,
                items: [],
                currentPoints: 0,
                currentStage: ''
            },
            // 当前激活的套装ID
            activeSetId: null,
            // 角色装备配置
            characterEquipmentConfig: {}
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
            await this.loadEquipmentSets()
            if (this.selectedCharacterId) {
                await this.loadCharacterEquipmentConfig()
            }
        },

        /**
         * 加载角色列表
         */
        async loadCharacters() {
            try {
                // 获取当前用户ID
                const userId = this.$store.getters.userId
                if (!userId) return

                const res = await this.$request({
                    url: `/api/characters/user/${userId}`,
                    method: 'get'
                })

                if (res.data && Array.isArray(res.data)) {
                    this.characters = res.data
                    // 默认选中第一个角色
                    if (this.characters.length > 0 && !this.selectedCharacterId) {
                        this.selectedCharacterId = this.characters[0].id
                    }
                }
            } catch (error) {
                console.error('加载角色列表失败:', error)
            }
        },

        /**
         * 加载装备套装列表
         */
        async loadEquipmentSets() {
            this.loading = true
            try {
                const res = await this.$request({
                    url: equipmentApi.list,
                    method: 'get'
                })

                // res 本身就是要的数组，不需要 res.data
                if (Array.isArray(res)) {
                    // 直接使用后端数据
                    this.equipmentSets = res
                } else if (res.data && Array.isArray(res.data)) {
                    // 兼容包装格式 { data: [...] }
                    this.equipmentSets = res.data
                } else {
                    // 如果后端没有数据，使用默认的12套
                    this.equipmentSets = this.getDefaultEquipmentSets()
                }

                // 应用角色的装备配置
                this.applyCharacterConfig()
            } catch (error) {
                console.error('加载装备套装列表失败:', error)
                // 使用默认数据作为兜底
                this.equipmentSets = this.getDefaultEquipmentSets()
            } finally {
                this.loading = false
            }
        },

        /**
         * 加载角色装备配置
         */
        async loadCharacterEquipmentConfig() {
            try {
                const res = await this.$request({
                    url: `/api/character-equipment/${this.selectedCharacterId}`,
                    method: 'get'
                })

                if (res.data) {
                    this.characterEquipmentConfig = res.data
                    this.applyCharacterConfig()
                }
            } catch (error) {
                console.error('加载角色装备配置失败:', error)
            }
        },

        /**
         * 应用角色装备配置到套装列表
         */
        applyCharacterConfig() {
            if (!this.characterEquipmentConfig.setId) return

            const configSetId = this.characterEquipmentConfig.setId
            this.equipmentSets = this.equipmentSets.map(set => {
                if (set.id === configSetId) {
                    return {
                        ...set,
                        currentPoints: this.characterEquipmentConfig.currentPoints || 0,
                        currentStage: this.characterEquipmentConfig.currentStage || ''
                    }
                }
                return set
            })
        },

        /**
         * 获取默认的12套装备数据（仅用于兜底）
         * @returns {Array} 默认套装列表
         */
        getDefaultEquipmentSets() {
            return [
                { id: 1, setName: '潜影暗袭', setIndex: 1, setType: 'normal', currentPoints: 0, currentStage: '', items: [] },
                { id: 2, setName: '精灵国度', setIndex: 2, setType: 'normal', currentPoints: 0, currentStage: '', items: [] },
                { id: 3, setName: '理想之黄金乡', setIndex: 3, setType: 'normal', currentPoints: 0, currentStage: '', items: [] },
                { id: 4, setName: '龙战八荒', setIndex: 4, setType: 'normal', currentPoints: 0, currentStage: '', items: [] },
                { id: 5, setName: '混沌净化', setIndex: 5, setType: 'normal', currentPoints: 0, currentStage: '', items: [] },
                { id: 6, setName: '天命者的气运', setIndex: 6, setType: 'normal', currentPoints: 0, currentStage: '', items: [] },
                { id: 7, setName: '究极能量', setIndex: 7, setType: 'normal', currentPoints: 0, currentStage: '', items: [] },
                { id: 8, setName: '造化自然', setIndex: 8, setType: 'normal', currentPoints: 0, currentStage: '', items: [] },
                { id: 9, setName: '诸神黄昏之女武神', setIndex: 9, setType: 'normal', currentPoints: 0, currentStage: '', items: [] },
                { id: 10, setName: '青丘灵珠', setIndex: 10, setType: 'normal', currentPoints: 0, currentStage: '', items: [] },
                { id: 11, setName: '群猎美学', setIndex: 11, setType: 'normal', currentPoints: 0, currentStage: '', items: [] },
                { id: 12, setName: '冥思者的魔力领域', setIndex: 12, setType: 'special', currentPoints: 0, currentStage: '', items: [] }
            ]
        },

        /**
         * 处理套装卡片点击
         * @param {Object} setData 套装数据
         */
        async handleSetClick(setData) {
            this.activeSetId = setData.id
            this.selectedSetData = { ...setData }

            // 如果还没有加载装备详情，尝试加载
            if (!setData.items || setData.items.length === 0) {
                await this.loadSetDetail(setData.id)
            }

            this.dialogVisible = true
        },

        /**
         * 加载套装详情
         * @param {number} setId 套装ID
         */
        async loadSetDetail(setId) {
            // 标记加载状态
            const setIndex = this.equipmentSets.findIndex(s => s.id === setId)
            if (setIndex !== -1) {
                this.$set(this.equipmentSets, setIndex, { ...this.equipmentSets[setIndex], loading: true })
            }

            try {
                const res = await this.$request({
                    url: equipmentApi.detail(setId),
                    method: 'get'
                })

                // res 就是要的数据对象，不需要 res.data
                if (res && res.setInfo) {
                    // 将后端返回的数据结构转换为前端需要的格式
                    const setDetailData = {
                        id: res.setInfo.id,
                        setName: res.setInfo.setName,
                        setIndex: res.setInfo.setIndex,
                        setType: res.setInfo.setType,
                        items: [
                            ...(res.items.armorItems || []),
                            ...(res.items.jewelryItems || []),
                            ...(res.items.specialItems || [])
                        ],
                        stages: res.effects || []
                    }

                    // 更新套装数据
                    const setIndex = this.equipmentSets.findIndex(s => s.id === setId)
                    if (setIndex !== -1) {
                        this.equipmentSets[setIndex] = {
                            ...this.equipmentSets[setIndex],
                            ...setDetailData,
                            loading: false
                        }
                    }

                    // 更新选中数据
                    if (this.selectedSetData.id === setId) {
                        this.selectedSetData = {
                            ...this.selectedSetData,
                            ...setDetailData
                        }
                    }
                }
            } catch (error) {
                console.error('加载套装详情失败:', error)
                this.$message.error('加载套装详情失败')
            }
        },

        /**
         * 处理角色切换
         * @param {number} characterId 角色ID
         */
        async handleCharacterChange(characterId) {
            this.selectedCharacterId = characterId
            await this.loadCharacterEquipmentConfig()
        },

        /**
         * 处理积分计算
         * @param {Object} data 计算数据
         */
        handleCalculate(data) {
            console.log('计算数据:', data)
            // 可以在这里更新UI或发送请求
        },

        /**
         * 处理保存
         * @param {Object} data 保存数据
         */
        async handleSave(data) {
            try {
                // 保存到后端
                await this.$request({
                    url: `/api/character-equipment/${this.selectedCharacterId}`,
                    method: 'post',
                    data: {
                        setId: data.setId,
                        setName: data.setName,
                        currentPoints: data.totalPoints,
                        currentStage: data.currentStage.name,
                        items: data.items
                    }
                })

                // 更新本地数据
                const setIndex = this.equipmentSets.findIndex(s => s.id === data.setId)
                if (setIndex !== -1) {
                    this.equipmentSets[setIndex] = {
                        ...this.equipmentSets[setIndex],
                        currentPoints: data.totalPoints,
                        currentStage: data.currentStage.name
                    }
                }

                this.$message.success('保存成功')
            } catch (error) {
                console.error('保存失败:', error)
                this.$message.error('保存失败')
            }
        },

        /**
         * 处理弹窗关闭
         */
        handleDialogClose() {
            this.activeSetId = null
        }
    }
}
</script>

<style lang="less" scoped>
@import '@/assets/styles/variables.less';

.equipment-page {
    padding: @spacing-xl;
    min-height: 100%;
    background: linear-gradient(180deg, @dnf-bg-dark 0%, @dnf-primary-dark 100%);

    // 页面标题
    .page-header {
        margin-bottom: @spacing-xl;

        .page-title {
            font-family: @font-heading, sans-serif;
            font-size: 24px;
            font-weight: @font-weight-bold;
            color: @dnf-primary-gold;
            margin: 0 0 @spacing-xs 0;
            display: flex;
            align-items: center;
            gap: @spacing-sm;

            i {
                font-size: 28px;
            }
        }

        .page-subtitle {
            font-size: 14px;
            color: @dnf-text-muted;
            margin: 0;
        }
    }

    // 加载容器
    .loading-container {
        min-height: 400px;
    }

    // 套装卡片网格 - 4x3布局
    .equipment-grid {
        display: grid;
        grid-template-columns: repeat(4, 1fr);
        gap: @spacing-lg;
        margin-bottom: @spacing-xl;

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

    // 空状态
    .empty-state {
        display: flex;
        flex-direction: column;
        align-items: center;
        justify-content: center;
        padding: @spacing-3xl;
        color: @dnf-text-muted;

        i {
            font-size: 64px;
            margin-bottom: @spacing-lg;
            opacity: 0.5;
        }

        p {
            font-size: 16px;
            margin-bottom: @spacing-lg;
        }
    }

    // 角色选择器
    .character-selector {
        display: flex;
        justify-content: flex-end;
        margin-bottom: @spacing-lg;

        .character-select {
            width: 200px;

            .char-level {
                float: right;
                color: @dnf-text-muted;
                font-size: 12px;
            }
        }
    }
}
</style>
