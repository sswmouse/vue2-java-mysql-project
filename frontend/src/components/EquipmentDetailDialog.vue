<!--
 * @Description: 装备详情弹窗组件
 * @Author: Claude Code
 * @Date: 2026-04-17
 * @LastEditTime: 2026-04-17
 * @FilePath: /vue2-java-mysql-project/frontend/src/components/EquipmentDetailDialog.vue
-->
<template>
    <el-dialog
        :visible.sync="dialogVisible"
        :title="dialogTitle"
        width="800px"
        :close-on-click-modal="false"
        class="equipment-detail-dialog"
        @close="handleClose"
    >
        <div
            v-loading="loading"
            class="dialog-content"
        >
            <!-- 当前阶段信息 -->
            <div class="current-stage">
                <div class="stage-label">
                    【当前阶段】
                </div>
                <div class="stage-name">
                    {{ currentStageInfo.stageName || '暂无阶段' }}
                </div>
                <div class="stage-points">
                    ({{ currentStageInfo.points || 0 }}分)
                </div>
                <div class="stage-fame">
                    额外名望: +{{ currentStageInfo.fameBonus || 0 }}
                </div>
            </div>

            <!-- 套装效果进度条 -->
            <div class="progress-section">
                <div class="section-title">
                    套装效果进度
                </div>
                <div class="progress-container">
                    <!-- 稀有阶段 -->
                    <div class="progress-row">
                        <span class="rarity-label rare">稀有</span>
                        <div class="progress-dots">
                            <div
                                v-for="i in 5"
                                :key="'rare-' + i"
                                class="progress-dot"
                                :class="{
                                    active: isStageActive('rare', i),
                                    current: isCurrentStage('rare', i)
                                }"
                                @click="handleStageClick('rare', i)"
                            >
                                <span class="dot-label">{{ i }}</span>
                            </div>
                        </div>
                    </div>

                    <!-- 神器阶段 -->
                    <div class="progress-row">
                        <span class="rarity-label legendary">神器</span>
                        <div class="progress-dots">
                            <div
                                v-for="i in 5"
                                :key="'legendary-' + i"
                                class="progress-dot"
                                :class="{
                                    active: isStageActive('legendary', i),
                                    current: isCurrentStage('legendary', i)
                                }"
                                @click="handleStageClick('legendary', i)"
                            >
                                <span class="dot-label">{{ i }}</span>
                            </div>
                        </div>
                    </div>

                    <!-- 传说阶段 -->
                    <div class="progress-row">
                        <span class="rarity-label epic">传说</span>
                        <div class="progress-dots">
                            <div
                                v-for="i in 5"
                                :key="'epic-' + i"
                                class="progress-dot"
                                :class="{
                                    active: isStageActive('epic', i),
                                    current: isCurrentStage('epic', i)
                                }"
                                @click="handleStageClick('epic', i)"
                            >
                                <span class="dot-label">{{ i }}</span>
                            </div>
                        </div>
                    </div>

                    <!-- 史诗阶段 -->
                    <div class="progress-row">
                        <span class="rarity-label mythic">史诗</span>
                        <div class="progress-dots">
                            <div
                                v-for="i in 5"
                                :key="'mythic-' + i"
                                class="progress-dot"
                                :class="{
                                    active: isStageActive('mythic', i),
                                    current: isCurrentStage('mythic', i)
                                }"
                                @click="handleStageClick('mythic', i)"
                            >
                                <span class="dot-label">{{ i }}</span>
                            </div>
                        </div>
                    </div>

                    <!-- 太初阶段 -->
                    <div class="progress-row">
                        <span class="rarity-label primal">太初</span>
                        <div class="progress-dots">
                            <div
                                class="progress-dot primal-dot"
                                :class="{
                                    active: isStageActive('primal', 1),
                                    current: isCurrentStage('primal', 1)
                                }"
                                @click="handleStageClick('primal', 1)"
                            >
                                <span class="dot-label">太初</span>
                            </div>
                        </div>
                    </div>
                </div>
            </div>

            <!-- 装备列表 -->
            <div class="equipment-sections">
                <!-- 防具 -->
                <div
                    v-if="armorItems.length > 0"
                    class="equipment-section"
                >
                    <div class="section-title">
                        防具
                    </div>
                    <div class="equipment-list">
                        <div
                            v-for="item in armorItems"
                            :key="item.id"
                            class="equipment-item"
                            @mouseenter="handleItemHover(item, $event)"
                            @mouseleave="handleItemLeave"
                        >
                            <div class="item-slot">
                                {{ item.slotName }}
                            </div>
                            <div class="item-rarity-selector">
                                <button
                                    v-for="rarity in getItemRarities(item)"
                                    :key="rarity.key"
                                    class="rarity-btn"
                                    :class="{ active: rarity.key === getItemRarity(item) }"
                                    :style="rarity.key === getItemRarity(item) ? { background: rarity.color } : {}"
                                    @click="handleItemRarityChange(item, rarity.key)"
                                >
                                    {{ rarity.name }}
                                </button>
                            </div>
                            <div class="item-stats">
                                <span class="stat fame">名望: {{ getItemFame(item) }}</span>
                                <span class="stat points">积分: +{{ getItemPoints(item) }}</span>
                            </div>
                        </div>
                    </div>
                </div>

                <!-- 首饰 -->
                <div
                    v-if="jewelryItems.length > 0"
                    class="equipment-section"
                >
                    <div class="section-title">
                        首饰
                    </div>
                    <div class="equipment-list">
                        <div
                            v-for="item in jewelryItems"
                            :key="item.id"
                            class="equipment-item"
                            @mouseenter="handleItemHover(item, $event)"
                            @mouseleave="handleItemLeave"
                        >
                            <div class="item-slot">
                                {{ item.slotName }}
                            </div>
                            <div class="item-rarity-selector">
                                <button
                                    v-for="rarity in getItemRarities(item)"
                                    :key="rarity.key"
                                    class="rarity-btn"
                                    :class="{ active: rarity.key === getItemRarity(item) }"
                                    :style="rarity.key === getItemRarity(item) ? { background: rarity.color } : {}"
                                    @click="handleItemRarityChange(item, rarity.key)"
                                >
                                    {{ rarity.name }}
                                </button>
                            </div>
                            <div class="item-stats">
                                <span class="stat fame">名望: {{ getItemFame(item) }}</span>
                                <span class="stat points">积分: +{{ getItemPoints(item) }}</span>
                            </div>
                        </div>
                    </div>
                </div>

                <!-- 特殊装备 -->
                <div
                    v-if="specialItems.length > 0"
                    class="equipment-section"
                >
                    <div class="section-title">
                        特殊装备
                    </div>
                    <div class="equipment-list">
                        <div
                            v-for="item in specialItems"
                            :key="item.id"
                            class="equipment-item"
                            @mouseenter="handleItemHover(item, $event)"
                            @mouseleave="handleItemLeave"
                        >
                            <div class="item-slot">
                                {{ item.slotName }}
                            </div>
                            <div class="item-rarity-selector">
                                <button
                                    v-for="rarity in getItemRarities(item)"
                                    :key="rarity.key"
                                    class="rarity-btn"
                                    :class="{ active: rarity.key === getItemRarity(item) }"
                                    :style="rarity.key === getItemRarity(item) ? { background: rarity.color } : {}"
                                    @click="handleItemRarityChange(item, rarity.key)"
                                >
                                    {{ rarity.name }}
                                </button>
                            </div>
                            <div class="item-stats">
                                <span class="stat fame">名望: {{ getItemFame(item) }}</span>
                                <span class="stat points">积分: +{{ getItemPoints(item) }}</span>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>

        <!-- Tooltip -->
        <equipment-tooltip
            ref="tooltip"
            :item-data="hoveredItem"
            :set-name="setData.setName"
            :show="tooltipVisible"
            position="right"
            :offset="{ x: 10, y: 0 }"
        />

        <div slot="footer">
            <el-button
                type="primary"
                @click="handleSave"
            >
                保存配置
            </el-button>
            <el-button @click="handleClose">
                关闭
            </el-button>
        </div>
    </el-dialog>
</template>

<script>
import {
    RARITY_COLORS,
    POINTS_MAP,
    FAME_MAP,
    KEY_TO_RARITY_MAP
} from '@/api/equipment'
import EquipmentTooltip from './EquipmentTooltip.vue'

/**
 * 装备详情弹窗组件
 * 显示套装详情、进度条和装备列表
 */
export default {
    name: 'EquipmentDetailDialog',

    components: {
        EquipmentTooltip
    },

    props: {
        // 是否显示
        visible: {
            type: Boolean,
            default: false
        },
        // 套装数据
        setData: {
            type: Object,
            required: true,
            default: () => ({
                id: 0,
                setName: '',
                setIndex: 0,
                items: [],
                currentPoints: 0,
                currentStage: '',
                stages: []
            })
        }
    },

    data() {
        return {
            loading: false,
            // 弹窗显示状态
            dialogVisible: false,
            // 各装备选中的品级
            itemRarities: {},
            // 悬停的装备
            hoveredItem: {},
            tooltipVisible: false,
            // 阶段数据
            stageData: []
        }
    },

    computed: {
        /**
         * 弹窗标题
         */
        dialogTitle() {
            const itemCount = this.activatedItemCount
            const totalItems = 11
            return `${this.setData.setName || '装备套装'}  积分: ${itemCount}/${totalItems}`
        },

        /**
         * 防具装备
         */
        armorItems() {
            return this.setData.items
                ? this.setData.items.filter(item => item.slotType === 'armor')
                : []
        },

        /**
         * 首饰装备
         */
        jewelryItems() {
            return this.setData.items
                ? this.setData.items.filter(item => item.slotType === 'jewelry')
                : []
        },

        /**
         * 特殊装备
         */
        specialItems() {
            return this.setData.items
                ? this.setData.items.filter(item => item.slotType === 'special')
                : []
        },

        /**
         * 当前已激活的件数
         */
        activatedItemCount() {
            return Object.keys(this.itemRarities).length
        },

        /**
         * 当前积分
         */
        totalPoints() {
            let points = 0
            for (const itemId in this.itemRarities) {
                const rarity = this.itemRarities[itemId]
                if (this.setData.items) {
                    const item = this.setData.items.find(i => i.id === parseInt(itemId))
                    if (item && item.rarityPoints && item.rarityPoints[rarity] !== undefined) {
                        points += item.rarityPoints[rarity]
                    } else {
                        points += POINTS_MAP[rarity] || 0
                    }
                }
            }
            return points
        },

        /**
         * 当前阶段信息
         */
        currentStageInfo() {
            const stage = this.getCurrentStage()
            return stage || { stageName: '暂无阶段', points: 0, fameBonus: 0 }
        }
    },

    watch: {
        visible(val) {
            this.dialogVisible = val
            if (val) {
                this.initData()
            }
        },
        dialogVisible(val) {
            this.$emit('update:visible', val)
        }
    },

    created() {
        // 初始化阶段数据
        this.initStages()
    },

    methods: {
        /**
         * 初始化数据
         */
        initData() {
            // 初始化各装备的品级
            if (this.setData.items) {
                this.setData.items.forEach(item => {
                    // 根据部位类型设置默认品级
                    // 防具/特殊装备默认史诗('mythic')，首饰默认太初('primal')
                    let defaultRarity = 'mythic'
                    if (item.slotType === 'jewelry') {
                        defaultRarity = 'primal'
                    }
                    this.$set(
                        this.itemRarities,
                        item.id,
                        defaultRarity
                    )
                })
            }
            this.$emit('calculate', this.getCalculationData())
        },

        /**
         * 初始化阶段数据
         */
        initStages() {
            // 定义21个阶段
            this.stageData = [
                // 稀有阶段
                { key: 'rare', sub: 1, name: '稀有1阶段', points: 750, fameBonus: 0 },
                { key: 'rare', sub: 2, name: '稀有2阶段', points: 835, fameBonus: 1000 },
                { key: 'rare', sub: 3, name: '稀有3阶段', points: 920, fameBonus: 2000 },
                { key: 'rare', sub: 4, name: '稀有4阶段', points: 1005, fameBonus: 3000 },
                { key: 'rare', sub: 5, name: '稀有5阶段', points: 1090, fameBonus: 4000 },
                // 神器阶段
                { key: 'legendary', sub: 1, name: '神器1阶段', points: 1200, fameBonus: 5500 },
                { key: 'legendary', sub: 2, name: '神器2阶段', points: 1285, fameBonus: 6500 },
                { key: 'legendary', sub: 3, name: '神器3阶段', points: 1370, fameBonus: 7500 },
                { key: 'legendary', sub: 4, name: '神器4阶段', points: 1455, fameBonus: 8500 },
                { key: 'legendary', sub: 5, name: '神器5阶段', points: 1540, fameBonus: 9500 },
                // 传说阶段
                { key: 'epic', sub: 1, name: '传说1阶段', points: 1650, fameBonus: 11000 },
                { key: 'epic', sub: 2, name: '传说2阶段', points: 1735, fameBonus: 12000 },
                { key: 'epic', sub: 3, name: '传说3阶段', points: 1820, fameBonus: 13000 },
                { key: 'epic', sub: 4, name: '传说4阶段', points: 1905, fameBonus: 14000 },
                { key: 'epic', sub: 5, name: '传说5阶段', points: 1990, fameBonus: 15000 },
                // 史诗阶段
                { key: 'mythic', sub: 1, name: '史诗1阶段', points: 2100, fameBonus: 16500 },
                { key: 'mythic', sub: 2, name: '史诗2阶段', points: 2185, fameBonus: 17500 },
                { key: 'mythic', sub: 3, name: '史诗3阶段', points: 2270, fameBonus: 18500 },
                { key: 'mythic', sub: 4, name: '史诗4阶段', points: 2355, fameBonus: 19500 },
                { key: 'mythic', sub: 5, name: '史诗5阶段', points: 2440, fameBonus: 20500 },
                // 太初阶段
                { key: 'primal', sub: 1, name: '太初阶段', points: 2550, fameBonus: 22000 }
            ]
        },

        /**
         * 获取当前阶段
         */
        getCurrentStage() {
            const points = this.totalPoints
            let currentStage = null

            for (const stage of this.stageData) {
                if (points >= stage.points) {
                    currentStage = stage
                } else {
                    break
                }
            }

            return currentStage
        },

        /**
         * 判断阶段是否已激活
         */
        isStageActive(rarity, sub) {
            const points = this.totalPoints
            const stage = this.stageData.find(s => s.key === rarity && s.sub === sub)
            if (!stage) return false
            return points >= stage.points
        },

        /**
         * 判断是否为当前阶段
         */
        isCurrentStage(rarity, sub) {
            const current = this.getCurrentStage()
            return current && current.key === rarity && current.sub === sub
        },

        /**
         * 处理阶段点击
         */
        handleStageClick(rarity, sub) {
            // 可以显示阶段详情
            const stage = this.stageData.find(s => s.key === rarity && s.sub === sub)
            if (stage) {
                this.$message.info(`${stage.name}: 需要 ${stage.points} 积分，当前 ${this.totalPoints} 积分`)
            }
        },

        /**
         * 获取装备可选品级
         */
        getItemRarities(item) {
            const isJewelry = item.slotType === 'jewelry'
            const rarities = [
                { key: 'rare', name: '稀有', color: RARITY_COLORS.rare },
                { key: 'legendary', name: '神器', color: RARITY_COLORS.legendary },
                { key: 'epic', name: '传说', color: RARITY_COLORS.epic },
                { key: 'mythic', name: '史诗', color: RARITY_COLORS.mythic }
            ]

            if (isJewelry) {
                rarities.push(
                    { key: 'primal', name: '太初', color: RARITY_COLORS.primal },
                    { key: 'blackEpic', name: '黑牙', color: RARITY_COLORS.blackEpic },
                    { key: 'blackPrimal', name: '黑牙太初', color: RARITY_COLORS.blackPrimal }
                )
            }

            return rarities
        },

        /**
         * 获取装备当前品级
         */
        getItemRarity(item) {
            if (this.itemRarities[item.id]) {
                return this.itemRarities[item.id]
            }
            // 防具/特殊装备默认史诗，首饰默认太初
            return item.slotType === 'jewelry' ? 'primal' : 'mythic'
        },

        /**
         * 将中文品级名转换为前端key
         */
        getRarityKey(chineseRarity) {
            const RARITY_KEY_MAP = {
                '稀有': 'rare',
                '神器': 'legendary',
                '传说': 'epic',
                '史诗': 'mythic',
                '太初': 'primal',
                '黑牙史诗': 'blackEpic',
                '黑牙太初': 'blackPrimal'
            }
            return RARITY_KEY_MAP[chineseRarity]
        },

        /**
         * 获取装备名望
         */
        getItemFame(item) {
            const rarity = this.getItemRarity(item)
            const chineseRarity = KEY_TO_RARITY_MAP[rarity] || rarity
            // 优先使用API返回的数据（rarityFameMap，中文键）
            if (item.rarityFameMap) {
                if (item.rarityFameMap[chineseRarity] !== undefined) {
                    return item.rarityFameMap[chineseRarity]
                }
            }
            return FAME_MAP[rarity] || 0
        },

        /**
         * 获取装备积分
         */
        getItemPoints(item) {
            const rarity = this.getItemRarity(item)
            const chineseRarity = KEY_TO_RARITY_MAP[rarity] || rarity
            // 优先使用API返回的数据（rarityPointsMap，中文键）
            if (item.rarityPointsMap) {
                if (item.rarityPointsMap[chineseRarity] !== undefined) {
                    return item.rarityPointsMap[chineseRarity]
                }
            }
            return POINTS_MAP[rarity] || 0
        },

        /**
         * 处理装备品级变化
         */
        handleItemRarityChange(item, rarity) {
            this.$set(this.itemRarities, item.id, rarity)
            this.$emit('calculate', this.getCalculationData())
        },

        /**
         * 处理装备悬停
         */
        handleItemHover(item) {
            this.hoveredItem = {
                ...item,
                currentRarity: this.getItemRarity(item)
            }
            this.tooltipVisible = true
        },

        /**
         * 处理装备离开
         */
        handleItemLeave() {
            this.tooltipVisible = false
        },

        /**
         * 获取计算数据
         */
        getCalculationData() {
            return {
                setId: this.setData.id,
                setName: this.setData.setName,
                totalPoints: this.totalPoints,
                currentStage: this.currentStageInfo,
                items: Object.entries(this.itemRarities).map(([itemId, rarity]) => ({
                    itemId: parseInt(itemId),
                    rarity
                }))
            }
        },

        /**
         * 处理关闭
         */
        handleClose() {
            this.dialogVisible = false
            this.$emit('close')
        },

        /**
         * 处理保存
         */
        handleSave() {
            this.$emit('save', this.getCalculationData())
            this.$message.success('保存成功')
            this.handleClose()
        }
    }
}
</script>

<style lang="less" scoped>
@import '@/assets/styles/variables.less';

.equipment-detail-dialog {
    /deep/ .el-dialog {
        background: linear-gradient(145deg, @dnf-bg-card 0%, @dnf-primary-dark 100%);
        border: 2px solid @dnf-border-light;
        border-radius: @border-radius-xl;

        .el-dialog__header {
            padding: @spacing-lg;
            padding-right: @spacing-3xl;
            border-bottom: 1px solid @dnf-border-dark;
            position: relative;

            .el-dialog__title {
                font-family: @font-heading, sans-serif;
                font-size: 18px;
                font-weight: @font-weight-semibold;
                color: @dnf-primary-gold;
            }

            .el-dialog__headerbtn {
                position: absolute;
                top: 10px;
                right: 10px;
                width: 24px;
                height: 24px;
                line-height: 24px;
                text-align: center;

                .el-dialog__close {
                    color: @dnf-text-muted;
                    font-size: 18px;
                    transition: color @transition-fast;

                    &:hover {
                        color: @dnf-primary-gold;
                    }
                }
            }
        }

        .el-dialog__body {
            padding: @spacing-lg;
            max-height: 70vh;
            overflow-y: auto;

            &::-webkit-scrollbar {
                width: 6px;
            }

            &::-webkit-scrollbar-track {
                background: @dnf-bg-hover;
                border-radius: 3px;
            }

            &::-webkit-scrollbar-thumb {
                background: @dnf-border-light;
                border-radius: 3px;

                &:hover {
                    background: @dnf-secondary-purple;
                }
            }
        }

        .el-dialog__footer {
            padding: @spacing-md @spacing-lg;
            border-top: 1px solid @dnf-border-dark;
        }
    }

    .dialog-content {
        // 当前阶段信息
        .current-stage {
            display: flex;
            align-items: center;
            gap: @spacing-md;
            padding: @spacing-md;
            background: linear-gradient(90deg, @dnf-bg-hover 0%, transparent 100%);
            border-left: 3px solid @dnf-secondary-gold;
            border-radius: @border-radius-sm;
            margin-bottom: @spacing-lg;

            .stage-label {
                font-size: 13px;
                color: @dnf-text-muted;
            }

            .stage-name {
                font-family: @font-heading, sans-serif;
                font-size: 16px;
                font-weight: @font-weight-semibold;
                color: @dnf-secondary-gold;
            }

            .stage-points {
                font-size: 12px;
                color: @dnf-text-secondary;
            }

            .stage-fame {
                margin-left: auto;
                font-size: 13px;
                color: @dnf-info;
            }
        }

        // 进度条区域
        .progress-section {
            margin-bottom: @spacing-lg;

            .section-title {
                font-size: 13px;
                font-weight: @font-weight-medium;
                color: @dnf-text-secondary;
                margin-bottom: @spacing-md;
                padding-left: @spacing-sm;
                border-left: 2px solid @dnf-secondary-purple;
            }

            .progress-container {
                background: @dnf-bg-hover;
                border-radius: @border-radius-md;
                padding: @spacing-md;
            }

            .progress-row {
                display: flex;
                align-items: center;
                margin-bottom: @spacing-sm;

                &:last-child {
                    margin-bottom: 0;
                }

                .rarity-label {
                    width: 48px;
                    font-size: 12px;
                    font-weight: @font-weight-medium;

                    &.rare { color: #6b7280; }
                    &.legendary { color: #3b82f6; }
                    &.epic { color: #f97316; }
                    &.mythic { color: #a855f7; }
                    &.primal { color: #fbbf24; }
                }

                .progress-dots {
                    display: flex;
                    gap: @spacing-xs;
                    flex: 1;

                    .progress-dot {
                        width: 40px;
                        height: 32px;
                        display: flex;
                        align-items: center;
                        justify-content: center;
                        background: @dnf-bg-card;
                        border: 2px solid @dnf-border-dark;
                        border-radius: @border-radius-sm;
                        cursor: pointer;
                        transition: all @transition-fast;

                        .dot-label {
                            font-size: 11px;
                            color: @dnf-text-muted;
                        }

                        &.active {
                            background: linear-gradient(135deg, @dnf-success 0%, @dnf-info 100%);
                            border-color: @dnf-success;
                            box-shadow: 0 0 10px rgba(0, 229, 160, 0.3);

                            .dot-label {
                                color: white;
                            }
                        }

                        &.current {
                            border-color: @dnf-secondary-gold;
                            box-shadow: 0 0 15px rgba(200, 168, 50, 0.4);
                            animation: pulse-glow 2s infinite;
                        }

                        &:hover:not(.active) {
                            border-color: @dnf-border-light;
                        }
                    }

                    .primal-dot {
                        width: 80px;
                    }
                }
            }
        }

        // 装备列表区域
        .equipment-sections {
            .equipment-section {
                margin-bottom: @spacing-lg;

                &:last-child {
                    margin-bottom: 0;
                }

                .section-title {
                    font-size: 13px;
                    font-weight: @font-weight-medium;
                    color: @dnf-text-secondary;
                    margin-bottom: @spacing-sm;
                    padding-left: @spacing-sm;
                    border-left: 2px solid @dnf-info;
                }

                .equipment-list {
                    background: @dnf-bg-hover;
                    border-radius: @border-radius-md;
                    padding: @spacing-sm;
                }

                .equipment-item {
                    display: flex;
                    align-items: center;
                    padding: @spacing-sm @spacing-md;
                    border-radius: @border-radius-sm;
                    transition: background @transition-fast;

                    &:hover {
                        background: @dnf-bg-active;
                    }

                    .item-slot {
                        width: 70px;
                        font-size: 13px;
                        color: @dnf-text-primary;
                        font-weight: @font-weight-medium;
                    }

                    .item-rarity-selector {
                        display: flex;
                        gap: 4px;
                        flex: 1;

                        .rarity-btn {
                            padding: 4px 8px;
                            font-size: 11px;
                            font-weight: @font-weight-medium;
                            color: @dnf-text-secondary;
                            background: @dnf-bg-card;
                            border: 1px solid @dnf-border-dark;
                            border-radius: @border-radius-sm;
                            cursor: pointer;
                            transition: all @transition-fast;

                            &:hover {
                                border-color: @dnf-border-light;
                                color: @dnf-text-primary;
                            }

                            &.active {
                                color: white;
                                border-color: transparent;
                                box-shadow: 0 2px 6px rgba(0, 0, 0, 0.3);
                            }
                        }
                    }

                    .item-stats {
                        display: flex;
                        gap: @spacing-md;

                        .stat {
                            font-size: 12px;
                            min-width: 70px;
                            text-align: right;

                            &.fame {
                                color: @dnf-info;
                            }

                            &.points {
                                color: @dnf-secondary-gold;
                                font-family: @font-mono, monospace;
                            }
                        }
                    }
                }
            }
        }
    }
}

@keyframes pulse-glow {
    0%, 100% {
        box-shadow: 0 0 15px rgba(200, 168, 50, 0.4);
    }
    50% {
        box-shadow: 0 0 25px rgba(200, 168, 50, 0.6);
    }
}
</style>
