<!--
 * @Description: 装备Tooltip组件 - 悬停显示装备详情
 * @Author: Claude Code
 * @Date: 2026-04-17
 * @LastEditTime: 2026-04-17
 * @FilePath: /vue2-java-mysql-project/frontend/src/components/EquipmentTooltip.vue
-->
<template>
    <div
        class="equipment-tooltip"
        :class="[positionClass, { visible: show }]"
        :style="tooltipStyle"
        @click.stop
    >
        <!-- 标题区域 -->
        <div class="tooltip-header">
            <h4 class="item-name">
                {{ itemData.itemName || '未知装备' }}
            </h4>
            <div class="rarity-badge">
                <span
                    class="rarity-text"
                    :style="{ color: currentRarityColor }"
                >
                    {{ currentRarityName }}
                </span>
                <i class="el-icon-arrow-down" />
            </div>
        </div>

        <!-- 基础信息 -->
        <div class="tooltip-info">
            <div class="info-row">
                <span class="info-label">部位</span>
                <span class="info-value">{{ itemData.slotName || '未知' }}</span>
            </div>
            <div class="info-row">
                <span class="info-label">所属</span>
                <span class="info-value">{{ setName || '未知套装' }}</span>
            </div>
        </div>

        <!-- 属性列表 -->
        <div
            v-if="currentStats && Object.keys(currentStats).length > 0"
            class="tooltip-stats"
        >
            <div
                v-for="(value, key) in currentStats"
                :key="key"
                class="stat-row"
            >
                <span class="stat-name">{{ getStatName(key) }}</span>
                <span class="stat-value">+{{ value }}{{ getStatUnit(key) }}</span>
            </div>
        </div>

        <!-- 品级选择 -->
        <div class="rarity-selector">
            <div class="selector-label">
                品级选择
            </div>
            <div class="rarity-buttons">
                <button
                    v-for="rarity in availableRarities"
                    :key="rarity.key"
                    class="rarity-btn"
                    :class="{ active: rarity.key === selectedRarity }"
                    :style="rarity.key === selectedRarity ? { background: rarity.color, borderColor: rarity.color } : {}"
                    @click="handleRarityChange(rarity.key)"
                >
                    {{ rarity.name }}
                </button>
            </div>
        </div>

        <!-- 底部统计 -->
        <div class="tooltip-footer">
            <div class="footer-stat">
                <span class="footer-label">名望</span>
                <span class="footer-value fame">{{ currentFame }}</span>
            </div>
            <div class="footer-stat">
                <span class="footer-label">积分</span>
                <span class="footer-value points">+{{ currentPoints }}</span>
            </div>
        </div>
    </div>
</template>

<script>
import {
    RARITY_MAP,
    RARITY_COLORS,
    POINTS_MAP,
    FAME_MAP
} from '@/api/equipment'

/**
 * 装备Tooltip组件
 * 用于悬停显示装备详情，支持品级切换
 */
export default {
    name: 'EquipmentTooltip',

    props: {
        // 装备数据
        itemData: {
            type: Object,
            required: true,
            default: () => ({
                id: 0,
                itemName: '',
                slotName: '',
                setName: '',
                rarityStats: {},
                rarityPoints: {},
                rarityFame: {}
            })
        },
        // 装备所属套装名称
        setName: {
            type: String,
            default: ''
        },
        // 是否显示
        show: {
            type: Boolean,
            default: false
        },
        // 位置
        position: {
            type: String,
            default: 'top', // top, bottom, left, right
            validator: (value) => ['top', 'bottom', 'left', 'right'].includes(value)
        },
        // 偏移量
        offset: {
            type: Object,
            default: () => ({ x: 0, y: 0 })
        }
    },

    data() {
        return {
            // 选中的品级
            selectedRarity: 'mythic'
        }
    },

    computed: {
        /**
         * 位置样式类
         */
        positionClass() {
            return `position-${this.position}`
        },

        /**
         * Tooltip样式
         */
        tooltipStyle() {
            return {
                transform: `translate(${this.offset.x}px, ${this.offset.y}px)`
            }
        },

        /**
         * 当前品级中文名
         */
        currentRarityName() {
            return RARITY_MAP[this.selectedRarity] || '史诗'
        },

        /**
         * 当前品级颜色
         */
        currentRarityColor() {
            return RARITY_COLORS[this.selectedRarity] || RARITY_COLORS.mythic
        },

        /**
         * 当前品级属性
         */
        currentStats() {
            if (!this.itemData.rarityStats) return {}
            const stats = this.itemData.rarityStats[this.selectedRarity]
            return stats || {}
        },

        /**
         * 当前品级积分
         */
        currentPoints() {
            if (this.itemData.rarityPoints && this.itemData.rarityPoints[this.selectedRarity] !== undefined) {
                return this.itemData.rarityPoints[this.selectedRarity]
            }
            return POINTS_MAP[this.selectedRarity] || 0
        },

        /**
         * 当前品级名望
         */
        currentFame() {
            if (this.itemData.rarityFame && this.itemData.rarityFame[this.selectedRarity] !== undefined) {
                return this.itemData.rarityFame[this.selectedRarity]
            }
            return FAME_MAP[this.selectedRarity] || 0
        },

        /**
         * 可用品级列表
         */
        availableRarities() {
            // 首饰支持太初、黑牙史诗、黑牙太初
            const isJewelry = this.itemData.slotType === 'jewelry'

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
        }
    },

    watch: {
        // 当装备数据变化时，重置品级
        itemData: {
            handler() {
                // 默认选中史诗
                this.selectedRarity = 'mythic'
            },
            deep: true
        }
    },

    methods: {
        /**
         * 处理品级切换
         * @param {string} rarityKey 品级Key
         */
        handleRarityChange(rarityKey) {
            this.selectedRarity = rarityKey
            this.$emit('rarity-change', rarityKey)
        },

        /**
         * 获取属性名称
         * @param {string} key 属性Key
         * @returns {string} 属性中文名
         */
        getStatName(key) {
            const statNames = {
                str: '力量',
                int: '智力',
                vit: '体力',
                spr: '精神',
                allStats: '四维',
                crit: '暴击率',
                critDamage: '暴击伤害',
                attackPower: '攻击强化',
                magicPower: '魔法强化',
                elementDamage: '属性强化',
                attackSpeed: '攻击速度',
                castSpeed: '施放速度',
                moveSpeed: '移动速度',
                hp: 'HP',
                mp: 'MP'
            }
            return statNames[key] || key
        },

        /**
         * 获取属性单位
         * @param {string} key 属性Key
         * @returns {string} 属性单位
         */
        getStatUnit(key) {
            const percentStats = ['crit', 'critDamage', 'attackSpeed', 'castSpeed', 'moveSpeed']
            return percentStats.includes(key) ? '%' : ''
        }
    }
}
</script>

<style lang="less" scoped>
@import '@/assets/styles/variables.less';

.equipment-tooltip {
    position: absolute;
    width: 280px;
    background: linear-gradient(
        145deg,
        rgba(13, 25, 48, 0.98) 0%,
        rgba(10, 20, 40, 0.98) 100%
    );
    border: 2px solid @dnf-border-light;
    border-radius: @border-radius-lg;
    padding: @spacing-md;
    box-shadow:
        0 8px 32px rgba(0, 0, 0, 0.6),
        0 0 20px rgba(74, 16, 144, 0.2);
    z-index: @z-tooltip;
    opacity: 0;
    visibility: hidden;
    transition: all @transition-fast;
    pointer-events: auto;

    &.visible {
        opacity: 1;
        visibility: visible;
    }

    // 位置样式
    &.position-top {
        bottom: calc(100% + 12px);
        left: 50%;
        transform: translateX(-50%) translateY(10px);

        &.visible {
            transform: translateX(-50%) translateY(0);
        }
    }

    &.position-bottom {
        top: calc(100% + 12px);
        left: 50%;
        transform: translateX(-50%) translateY(-10px);

        &.visible {
            transform: translateX(-50%) translateY(0);
        }
    }

    &.position-left {
        right: calc(100% + 12px);
        top: 50%;
        transform: translateY(-50%) translateX(-10px);

        &.visible {
            transform: translateY(-50%) translateX(0);
        }
    }

    &.position-right {
        left: calc(100% + 12px);
        top: 50%;
        transform: translateY(-50%) translateX(10px);

        &.visible {
            transform: translateY(-50%) translateX(0);
        }
    }

    // 标题区域
    .tooltip-header {
        display: flex;
        justify-content: space-between;
        align-items: center;
        padding-bottom: @spacing-sm;
        border-bottom: 1px solid @dnf-border-dark;
        margin-bottom: @spacing-sm;

        .item-name {
            font-family: @font-heading, sans-serif;
            font-size: 15px;
            font-weight: @font-weight-semibold;
            color: @dnf-text-primary;
            margin: 0;
            letter-spacing: 0.03em;
        }

        .rarity-badge {
            display: flex;
            align-items: center;
            gap: 4px;

            .rarity-text {
                font-size: 12px;
                font-weight: @font-weight-bold;
            }

            i {
                font-size: 12px;
                color: @dnf-text-muted;
            }
        }
    }

    // 基础信息
    .tooltip-info {
        padding: @spacing-xs 0;

        .info-row {
            display: flex;
            justify-content: space-between;
            font-size: 12px;
            margin-bottom: 4px;

            .info-label {
                color: @dnf-text-muted;
            }

            .info-value {
                color: @dnf-text-secondary;
            }
        }
    }

    // 属性列表
    .tooltip-stats {
        padding: @spacing-sm 0;
        border-top: 1px solid @dnf-border-dark;
        border-bottom: 1px solid @dnf-border-dark;
        margin: @spacing-sm 0;

        .stat-row {
            display: flex;
            justify-content: space-between;
            font-size: 12px;
            margin-bottom: 4px;

            &:last-child {
                margin-bottom: 0;
            }

            .stat-name {
                color: @dnf-text-secondary;
            }

            .stat-value {
                color: @dnf-success;
                font-family: @font-mono, monospace;
                font-weight: @font-weight-medium;
            }
        }
    }

    // 品级选择
    .rarity-selector {
        padding: @spacing-sm 0;

        .selector-label {
            font-size: 11px;
            color: @dnf-text-muted;
            margin-bottom: @spacing-xs;
        }

        .rarity-buttons {
            display: flex;
            flex-wrap: wrap;
            gap: 4px;

            .rarity-btn {
                padding: 4px 8px;
                font-size: 11px;
                font-weight: @font-weight-medium;
                color: @dnf-text-secondary;
                background: @dnf-bg-hover;
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
                    box-shadow: 0 2px 8px rgba(0, 0, 0, 0.3);
                }
            }
        }
    }

    // 底部统计
    .tooltip-footer {
        display: flex;
        justify-content: space-around;
        padding-top: @spacing-sm;
        border-top: 1px solid @dnf-border-dark;

        .footer-stat {
            display: flex;
            flex-direction: column;
            align-items: center;

            .footer-label {
                font-size: 10px;
                color: @dnf-text-muted;
                margin-bottom: 2px;
            }

            .footer-value {
                font-family: @font-mono, monospace;
                font-size: 14px;
                font-weight: @font-weight-bold;

                &.fame {
                    color: @dnf-info;
                }

                &.points {
                    color: @dnf-secondary-gold;
                }
            }
        }
    }
}
</style>
