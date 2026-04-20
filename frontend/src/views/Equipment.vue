<!--
 * @Description: 装备管理页面 - 展示12套装备卡片
 * @Author: Claude Code
 * @Date: 2026-04-17
 * @LastEditTime: 2026-04-18
 * @FilePath: /vue2-java-mysql-project/frontend/src/views/Equipment.vue
-->
<template>
    <div class="equipment-page">
        <!-- 页面标题 -->
        <div class="page-header">
            <div class="header-left">
                <h2 class="page-title">
                    <i class="el-icon-suitcase-1" />
                    装备套装管理
                </h2>
                <p class="page-subtitle">
                    管理角色的115级重力之泉装备套装，查看积分和阶段进度，支持多套装备独立保存进度
                </p>
            </div>
            <div class="header-right">
                <!-- 角色选择 -->
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
            <!-- 套装卡片网格 -->
            <div class="equipment-grid">
                <equipment-set-card
                    v-for="set in equipmentSets"
                    :key="set.id"
                    :set-data="set"
                    :is-active="activeSetId === set.id"
                    :is-equipped="set.isEquipped"
                    :loading="set.loading"
                    @equip="handleEquip"
                    @edit="handleEdit"
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
import equipmentApi, { RARITY_MAP } from '@/api/equipment'
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
                currentStage: '',
                isEquipped: false,
                savedItems: []
            },
            // 当前激活的套装ID
            activeSetId: null,
            // 角色所有装备配置列表
            characterEquipmentList: []
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
                await this.loadAllCharacterEquipment()
            }
        },

        /**
         * 加载角色列表
         */
        async loadCharacters() {
            try {
                // 获取当前用户ID (auth模块是命名空间模块)
                const userId = this.$store.getters['auth/userId']
                if (!userId) return

                const res = await this.$request({
                    url: `/api/characters/user/${userId}`,
                    method: 'get'
                })

                // 处理响应：可能是数组，也可能是 { data: [...] } 格式
                const characters = Array.isArray(res) ? res : (res.data || [])
                if (Array.isArray(characters)) {
                    this.characters = characters
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
                this.applyCharacterConfigs()
            } catch (error) {
                console.error('加载装备套装列表失败:', error)
                // 使用默认数据作为兜底
                this.equipmentSets = this.getDefaultEquipmentSets()
            } finally {
                this.loading = false
            }
        },

        /**
         * 加载角色所有装备配置
         */
        async loadAllCharacterEquipment() {
            try {
                const res = await this.$request({
                    url: `/api/character-equipment/all/${this.selectedCharacterId}`,
                    method: 'get'
                })

                // res 是数组，包含角色所有套装配置
                if (Array.isArray(res)) {
                    this.characterEquipmentList = res
                } else if (res.data && Array.isArray(res.data)) {
                    this.characterEquipmentList = res.data
                } else {
                    this.characterEquipmentList = []
                }
                this.applyCharacterConfigs()
            } catch (error) {
                console.error('加载角色装备配置失败:', error)
                this.characterEquipmentList = []
            }
        },

        /**
         * 加载单个套装配置
         */
        async loadSingleCharacterEquipment(setId) {
            try {
                const res = await this.$request({
                    url: `/api/character-equipment/${this.selectedCharacterId}/${setId}`,
                    method: 'get'
                })

                if (res && res.setId) {
                    // 更新列表中的配置
                    const index = this.characterEquipmentList.findIndex(c => c.setId === setId)
                    if (index !== -1) {
                        this.characterEquipmentList[index] = res
                    } else {
                        this.characterEquipmentList.push(res)
                    }
                    this.applyCharacterConfigs()
                    return res
                }
                return null
            } catch (error) {
                console.error('加载套装配置失败:', error)
                return null
            }
        },

        /**
         * 应用角色所有装备配置到套装列表
         */
        applyCharacterConfigs() {
            this.equipmentSets = this.equipmentSets.map(set => {
                const config = this.characterEquipmentList.find(c => c.setId === set.id)
                if (config) {
                    return {
                        ...set,
                        currentPoints: config.currentPoints || 0,
                        currentStage: config.currentStage || '',
                        isEquipped: config.isEquipped || false,
                        savedItems: config.items || []
                    }
                }
                return {
                    ...set,
                    currentPoints: 0,
                    currentStage: '',
                    isEquipped: false,
                    savedItems: []
                }
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
         * 处理编辑套装 - 打开弹窗
         * @param {Object} setData 套装数据
         */
        async handleEdit(setData) {
            this.activeSetId = setData.id

            // 加载角色的装备配置
            await this.loadSingleCharacterEquipment(setData.id)

            // 如果还没有加载装备详情，尝试加载
            if (!setData.items || setData.items.length === 0) {
                await this.loadSetDetail(setData.id)
            }

            // 从列表中找到最新的套装数据（包含所有items）
            const latestSetData = this.equipmentSets.find(s => s.id === setData.id) || setData

            // 从 characterEquipmentList 中获取该套装的配置
            const setConfig = this.characterEquipmentList.find(c => c.setId === setData.id)

            // 构建选中数据
            this.selectedSetData = {
                ...latestSetData,
                savedItems: setConfig ? (setConfig.items || []) : [],
                isEquipped: setConfig ? (setConfig.isEquipped || false) : false
            }

            // 确保 items 数组存在
            if (!this.selectedSetData.items) {
                this.selectedSetData.items = []
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
                        this.$set(this.equipmentSets, setIndex, {
                            ...this.equipmentSets[setIndex],
                            ...setDetailData,
                            loading: false
                        })
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
            await this.loadAllCharacterEquipment()
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
                // 获取品级名称（如"稀有"），用于卡片颜色显示
                const rarityKey = data.currentStage.key
                const rarityName = RARITY_MAP[rarityKey] || ''

                // 保存到后端
                await this.$request({
                    url: `/api/character-equipment/${this.selectedCharacterId}`,
                    method: 'post',
                    data: {
                        setId: data.setId,
                        setName: data.setName,
                        currentPoints: data.totalPoints,
                        currentStage: rarityName,
                        items: data.items
                    }
                })

                // 重新加载角色所有装备配置以更新卡片显示
                await this.loadAllCharacterEquipment()

                this.$message.success('保存成功')
            } catch (error) {
                console.error('保存失败:', error)
                this.$message.error('保存失败')
            }
        },

        /**
         * 处理穿戴/卸下装备切换 - 通过卡片点击触发
         * @param {Object} data 套装数据
         */
        async handleEquip(data) {
            // 如果当前已穿戴，则卸下；否则穿戴
            const isCurrentlyEquipped = data.isEquipped

            try {
                if (isCurrentlyEquipped) {
                    // 卸下装备
                    await this.$request({
                        url: `/api/character-equipment/${this.selectedCharacterId}/unequip/${data.id}`,
                        method: 'post'
                    })
                    this.$message.success('卸下成功')
                } else {
                    // 穿戴装备
                    await this.$request({
                        url: `/api/character-equipment/${this.selectedCharacterId}/equip/${data.id}`,
                        method: 'post'
                    })
                    this.$message.success('穿戴成功')
                }

                // 重新加载角色所有装备配置以更新卡片显示
                await this.loadAllCharacterEquipment()

                // 更新选中数据的穿戴状态
                this.selectedSetData.isEquipped = !isCurrentlyEquipped
            } catch (error) {
                console.error('操作失败:', error)
                this.$message.error('操作失败')
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
        display: flex;
        justify-content: space-between;
        align-items: flex-start;
        margin-bottom: @spacing-xl;

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

        // 角色选择器
        .character-select {
            min-width: 160px;

            .char-level {
                float: right;
                color: @dnf-text-muted;
                font-size: 12px;
            }
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
}

// 下拉框样式覆盖 - 需要全局作用域
/deep/ .el-select-dropdown {
    background: @dnf-bg-card;
    border-color: @dnf-border-light;

    .el-select-dropdown__item {
        color: @dnf-text-primary;

        &:hover {
            background: @dnf-bg-hover;
        }

        &.selected {
            color: @dnf-primary-gold;
        }
    }
}
</style>
