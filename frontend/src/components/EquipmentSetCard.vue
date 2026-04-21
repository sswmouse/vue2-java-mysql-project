<!--
 * @Description: 装备套装卡片组件
 * @Author: Claude Code
 * @Date: 2026-04-17
 * @LastEditTime: 2026-04-18
 * @FilePath: /vue2-java-mysql-project/frontend/src/components/EquipmentSetCard.vue
-->
<template>
    <div
        class="equipment-set-card"
        :class="{
            'is-active': isActive,
            'is-equipped': isEquipped,
            [stageClass]: stageClass
        }"
    >
        <!-- 卡片光晕 -->
        <div
            class="card-glow"
            :style="stageGlowColor && {
                background: `radial-gradient(ellipse at center, ${stageGlowColor}33 0%, ${stageGlowColor}11 40%, transparent 70%)`
            }"
        />

        <!-- 卡片主体 - 点击切换穿戴状态 -->
        <div
            class="card-content"
            @click="handleEquipClick"
        >
            <!-- 穿戴状态标识 -->
            <div
                v-if="isEquipped"
                class="equipped-badge"
            >
                <i class="el-icon-check" />
                穿戴中
            </div>

            <!-- 顶部装饰线 -->
            <div class="card-corner top-left" />
            <div class="card-corner top-right" />

            <!-- 套装图标区域 -->
            <div class="set-icon-wrapper">
                <div class="set-icon">
                    <!-- 根据套装ID显示不同图标 -->
                    <svg
                        viewBox="0 0 64 64"
                        class="icon-svg"
                    >
                        <!-- 通用底框 -->
                        <path
                            d="M32 4 L56 16 L56 48 L32 60 L8 48 L8 16 Z"
                            fill="url(#iconGradient)"
                            stroke="#c9a959"
                            stroke-width="1.5"
                        />
                        <path
                            d="M32 12 L48 20 L48 44 L32 52 L16 44 L16 20 Z"
                            fill="none"
                            stroke="#e6c866"
                            stroke-width="1"
                            opacity="0.5"
                        />
                        <circle
                            cx="32"
                            cy="10"
                            r="3"
                            fill="#e6c866"
                        />

                        <!-- 套装1: 潜影暗袭 - 匕首 -->
                        <g v-if="setData.setIndex === 1">
                            <path
                                d="M20 44 L32 12 L44 44"
                                fill="none"
                                stroke="#8b5cf6"
                                stroke-width="3"
                                stroke-linecap="round"
                            />
                            <path
                                d="M32 12 L32 20"
                                fill="none"
                                stroke="#a78bfa"
                                stroke-width="2"
                            />
                            <circle
                                cx="32"
                                cy="46"
                                r="4"
                                fill="#8b5cf6"
                            />
                        </g>

                        <!-- 套装2: 精灵国度 - 树叶 -->
                        <g v-else-if="setData.setIndex === 2">
                            <path
                                d="M32 14 C20 20 20 40 32 50 C44 40 44 20 32 14"
                                fill="#22c55e"
                                opacity="0.8"
                            />
                            <path
                                d="M32 20 L32 44"
                                stroke="#16a34a"
                                stroke-width="2"
                            />
                            <path
                                d="M26 30 L32 34 L38 30"
                                fill="none"
                                stroke="#16a34a"
                                stroke-width="1.5"
                            />
                        </g>

                        <!-- 套装3: 理想之黄金乡 - 金色 -->
                        <g v-else-if="setData.setIndex === 3">
                            <circle
                                cx="32"
                                cy="32"
                                r="14"
                                fill="none"
                                stroke="#fbbf24"
                                stroke-width="2"
                            />
                            <path
                                d="M32 20 L35 28 L44 28 L37 34 L40 44 L32 38 L24 44 L27 34 L20 28 L29 28 Z"
                                fill="#fbbf24"
                                opacity="0.9"
                            />
                        </g>

                        <!-- 套装4: 龙战八荒 - 龙角 -->
                        <g v-else-if="setData.setIndex === 4">
                            <path
                                d="M18 46 L28 20 L32 28"
                                fill="none"
                                stroke="#ef4444"
                                stroke-width="3"
                                stroke-linecap="round"
                            />
                            <path
                                d="M46 46 L36 20 L32 28"
                                fill="none"
                                stroke="#ef4444"
                                stroke-width="3"
                                stroke-linecap="round"
                            />
                            <path
                                d="M26 40 L38 40"
                                stroke="#dc2626"
                                stroke-width="2"
                            />
                        </g>

                        <!-- 套装5: 混沌净化 - 漩涡 -->
                        <g v-else-if="setData.setIndex === 5">
                            <path
                                d="M32 18 C40 18 46 24 46 32 C46 40 40 46 32 46 C24 46 18 40 18 32 C18 24 24 18 32 18"
                                fill="none"
                                stroke="#06b6d4"
                                stroke-width="2"
                                stroke-dasharray="4 2"
                            />
                            <path
                                d="M32 22 C38 22 42 26 42 32 C42 38 38 42 32 42"
                                fill="none"
                                stroke="#22d3ee"
                                stroke-width="2"
                            />
                            <circle
                                cx="32"
                                cy="32"
                                r="6"
                                fill="#06b6d4"
                                opacity="0.6"
                            />
                        </g>

                        <!-- 套装6: 天命者的气运 - 命运轮 -->
                        <g v-else-if="setData.setIndex === 6">
                            <circle
                                cx="32"
                                cy="32"
                                r="14"
                                fill="none"
                                stroke="#a855f7"
                                stroke-width="2"
                            />
                            <circle
                                cx="32"
                                cy="32"
                                r="8"
                                fill="none"
                                stroke="#c084fc"
                                stroke-width="1.5"
                            />
                            <path
                                d="M32 18 L32 22 M32 42 L32 46 M18 32 L22 32 M42 32 L46 32"
                                stroke="#a855f7"
                                stroke-width="2"
                            />
                            <circle
                                cx="32"
                                cy="32"
                                r="3"
                                fill="#e879f9"
                            />
                        </g>

                        <!-- 套装7: 究极能量 - 闪电 -->
                        <g v-else-if="setData.setIndex === 7">
                            <path
                                d="M36 14 L24 32 L34 32 L28 50 L44 30 L34 30 L40 14 Z"
                                fill="#facc15"
                                stroke="#eab308"
                                stroke-width="1"
                            />
                        </g>

                        <!-- 套装8: 造化自然 - 山川 -->
                        <g v-else-if="setData.setIndex === 8">
                            <path
                                d="M16 46 L28 24 L36 34 L44 20 L48 46 Z"
                                fill="#14b8a6"
                                opacity="0.8"
                            />
                            <path
                                d="M20 46 L30 30 L38 38 L44 28 L46 46 Z"
                                fill="#0d9488"
                                opacity="0.6"
                            />
                            <circle
                                cx="40"
                                cy="18"
                                r="4"
                                fill="#facc15"
                                opacity="0.8"
                            />
                        </g>

                        <!-- 套装9: 诸神黄昏之女武神 - 矛与盾 -->
                        <g v-else-if="setData.setIndex === 9">
                            <path
                                d="M26 18 L26 48 L32 42 L38 48 L38 18"
                                fill="none"
                                stroke="#f97316"
                                stroke-width="2"
                            />
                            <path
                                d="M18 36 L46 36"
                                stroke="#fb923c"
                                stroke-width="2"
                            />
                            <circle
                                cx="32"
                                cy="22"
                                r="4"
                                fill="#f97316"
                            />
                        </g>

                        <!-- 套装10: 青丘灵珠 - 狐狸 -->
                        <g v-else-if="setData.setIndex === 10">
                            <ellipse
                                cx="32"
                                cy="36"
                                rx="10"
                                ry="8"
                                fill="#f472b6"
                                opacity="0.8"
                            />
                            <path
                                d="M22 32 L18 18 L26 28"
                                fill="#ec4899"
                            />
                            <path
                                d="M42 32 L46 18 L38 28"
                                fill="#ec4899"
                            />
                            <circle
                                cx="28"
                                cy="34"
                                r="2"
                                fill="#be185d"
                            />
                            <circle
                                cx="36"
                                cy="34"
                                r="2"
                                fill="#be185d"
                            />
                        </g>

                        <!-- 套装11: 群猎美学 - 弓箭 -->
                        <g v-else-if="setData.setIndex === 11">
                            <path
                                d="M20 18 C20 18 20 46 20 46"
                                fill="none"
                                stroke="#84cc16"
                                stroke-width="2"
                            />
                            <path
                                d="M20 18 L44 32 L20 46"
                                fill="none"
                                stroke="#a3e635"
                                stroke-width="2"
                            />
                            <path
                                d="M18 32 L26 32"
                                stroke="#84cc16"
                                stroke-width="3"
                            />
                            <path
                                d="M44 30 L52 32 L44 34"
                                fill="#84cc16"
                            />
                        </g>

                        <!-- 套装12: 冥思者的魔力领域 - 魔法阵 -->
                        <g v-else-if="setData.setIndex === 12">
                            <circle
                                cx="32"
                                cy="32"
                                r="14"
                                fill="none"
                                stroke="#818cf8"
                                stroke-width="1.5"
                                stroke-dasharray="3 2"
                            />
                            <circle
                                cx="32"
                                cy="32"
                                r="10"
                                fill="none"
                                stroke="#a5b4fc"
                                stroke-width="1"
                            />
                            <path
                                d="M32 22 L35 30 L44 32 L35 34 L32 42 L29 34 L20 32 L29 30 Z"
                                fill="#818cf8"
                                opacity="0.7"
                            />
                            <circle
                                cx="32"
                                cy="32"
                                r="4"
                                fill="#c7d2fe"
                            />
                        </g>

                        <!-- 默认图标 -->
                        <g v-else>
                            <path
                                d="M32 20 L40 32 L32 44 L24 32 Z"
                                fill="none"
                                stroke="#c9a959"
                                stroke-width="2"
                            />
                            <path
                                d="M32 24 L36 32 L32 40 L28 32 Z"
                                fill="#c9a959"
                                opacity="0.6"
                            />
                        </g>

                        <!-- 渐变定义 -->
                        <defs>
                            <linearGradient
                                id="iconGradient"
                                x1="0%"
                                y1="0%"
                                x2="100%"
                                y2="100%"
                            >
                                <stop
                                    offset="0%"
                                    style="stop-color:#1a1a2e"
                                />
                                <stop
                                    offset="100%"
                                    style="stop-color:#16213e"
                                />
                            </linearGradient>
                        </defs>
                    </svg>
                </div>
            </div>

            <!-- 套装名称 -->
            <h3 class="set-name">
                {{ setData.setName || '未知套装' }}
            </h3>

            <!-- 套装积分和阶段 -->
            <div class="set-stats">
                <div
                    v-if="setData.currentPoints"
                    class="stat-item points"
                >
                    <span class="stat-label">积分</span>
                    <span class="stat-value">{{ setData.currentPoints }}</span>
                </div>
                <div
                    v-if="setData.currentStage"
                    class="stat-item stage"
                >
                    <span class="stat-label">阶段</span>
                    <span class="stat-value stage-value">{{ setData.currentStage }}</span>
                </div>
            </div>

            <!-- 已激活的套装效果图标 -->
            <div
                v-if="activatedEffects && activatedEffects.length > 0"
                class="activated-effects"
            >
                <div
                    v-for="(effect, index) in activatedEffects.slice(0, 3)"
                    :key="index"
                    class="effect-badge"
                    :title="effect"
                >
                    {{ index + 1 }}
                </div>
                <div
                    v-if="activatedEffects.length > 3"
                    class="effect-badge more"
                >
                    +{{ activatedEffects.length - 3 }}
                </div>
            </div>

            <!-- 右下角装饰 -->
            <div class="card-corner bottom-left" />
            <div class="card-corner bottom-right" />
        </div>

        <!-- 编辑按钮 - 悬浮在卡片右下角内侧 -->
        <el-button
            class="edit-btn"
            type="primary"
            icon="el-icon-edit"
            circle
            size="small"
            @click.stop="handleEditClick"
        />

        <!-- 加载状态 -->
        <div
            v-if="loading"
            class="loading-overlay"
        >
            <i class="el-icon-loading" />
        </div>
    </div>
</template>

<script>
import { RARITY_COLORS, KEY_TO_RARITY_MAP } from '@/api/equipment'

/**
 * 装备套装卡片组件
 * 用于展示单个装备套装的卡片视图
 */
export default {
    name: 'EquipmentSetCard',

    props: {
        // 套装数据
        setData: {
            type: Object,
            required: true,
            default: () => ({
                id: 0,
                setName: '',
                setIndex: 1,
                setType: 'normal',
                currentPoints: 0,
                currentStage: '',
                isEquipped: false,
                activatedEffects: []
            })
        },
        // 是否为当前激活套装
        isActive: {
            type: Boolean,
            default: false
        },
        // 是否穿戴
        isEquipped: {
            type: Boolean,
            default: false
        },
        // 加载状态
        loading: {
            type: Boolean,
            default: false
        }
    },

    computed: {
        /**
         * 获取已激活的套装效果列表
         */
        activatedEffects() {
            return this.setData.activatedEffects || []
        },

        /**
         * 根据套装阶段获取卡片样式类
         */
        stageClass() {
            const stageName = this.setData.currentStage
            if (!stageName) return ''

            // 尝试直接匹配品级名称
            let rarityKey = KEY_TO_RARITY_MAP[stageName]
            if (rarityKey) {
                return `stage-${rarityKey}`
            }

            // 尝试从完整阶段名中提取品级名称（如"稀有1阶段" -> "稀有"）
            const match = stageName.match(/^(稀有|神器|传说|史诗|太初|黑牙史诗|黑牙太初)/)
            if (match) {
                rarityKey = KEY_TO_RARITY_MAP[match[1]]
                if (rarityKey) {
                    return `stage-${rarityKey}`
                }
            }

            return ''
        },

        /**
         * 根据套装阶段获取光晕颜色
         */
        stageGlowColor() {
            const stageName = this.setData.currentStage
            if (!stageName) return null

            // 尝试直接匹配品级名称
            let rarityKey = KEY_TO_RARITY_MAP[stageName]
            if (rarityKey && RARITY_COLORS[rarityKey]) {
                return RARITY_COLORS[rarityKey]
            }

            // 尝试从完整阶段名中提取品级名称
            const match = stageName.match(/^(稀有|神器|传说|史诗|太初|黑牙史诗|黑牙太初)/)
            if (match) {
                rarityKey = KEY_TO_RARITY_MAP[match[1]]
                if (rarityKey && RARITY_COLORS[rarityKey]) {
                    return RARITY_COLORS[rarityKey]
                }
            }

            return null
        }
    },

    methods: {
        /**
         * 处理穿戴点击事件 - 卡片主体点击
         */
        handleEquipClick() {
            this.$emit('equip', this.setData)
        },

        /**
         * 处理编辑点击事件 - 编辑按钮点击
         */
        handleEditClick() {
            this.$emit('edit', this.setData)
        }
    }
}
</script>

<style lang="less" scoped>
@import '@/assets/styles/variables.less';

.equipment-set-card {
    position: relative;
    width: 100%;
    height: 260px;
    border-radius: @border-radius-lg;
    transition: all @transition-smooth;

    // 卡片光晕
    .card-glow {
        position: absolute;
        top: 50%;
        left: 50%;
        transform: translate(-50%, -50%);
        width: 120%;
        height: 120%;
        background: radial-gradient(
            ellipse at center,
            fade(@dnf-secondary-purple, 0.15) 0%,
            fade(@dnf-primary-gold, 0.05) 30%,
            transparent 70%
        );
        border-radius: 50%;
        filter: blur(30px);
        opacity: 0;
        transition: opacity 0.5s ease;
        pointer-events: none;
        z-index: -1;
    }

    &:hover {
        transform: translateY(-8px);

        .card-glow {
            opacity: 1;
        }

        .card-content {
            border-color: var(--theme-accent);
            box-shadow: 0 0 25px var(--theme-accent);
        }
    }

    &.is-active {
        .card-content {
            border-color: var(--theme-accent);
            box-shadow: 0 0 25px var(--theme-accent);
        }
    }

    // 穿戴状态样式
    &.is-equipped {
        .card-glow {
            opacity: 1;
            background: radial-gradient(
                ellipse at center,
                fade(@dnf-success, 0.2) 0%,
                fade(@dnf-secondary-gold, 0.1) 30%,
                transparent 70%
            );
        }

        .card-content {
            border-color: var(--theme-success);
            box-shadow: 0 0 20px rgba(0, 229, 160, 0.3);
        }
    }

    // 穿戴状态标识
    .equipped-badge {
        position: absolute;
        top: 8px;
        right: 8px;
        background: linear-gradient(135deg, var(--theme-success) 0%, var(--theme-info) 100%);
        color: white;
        padding: 4px 8px;
        border-radius: @border-radius-sm;
        font-size: 11px;
        font-weight: @font-weight-bold;
        display: flex;
        align-items: center;
        gap: 4px;
        z-index: 5;
        box-shadow: 0 2px 8px rgba(0, 229, 160, 0.4);

        i {
            font-size: 12px;
        }
    }

    // 编辑按钮 - 定位在卡片右下角内侧
    .edit-btn {
        position: absolute;
        bottom: 12px;
        right: 12px;
        background: linear-gradient(135deg, var(--theme-secondary) 0%, var(--theme-primary) 100%);
        border: 1px solid fade(@dnf-border-light, 50%);
        z-index: 6;
        box-shadow: 0 2px 8px rgba(123, 47, 255, 0.4);
        transition: all @transition-smooth;
        opacity: 0.8;

        &:hover {
            opacity: 1;
            transform: scale(1.1);
            box-shadow: 0 4px 16px rgba(123, 47, 255, 0.6);
        }
    }

    // 卡片主体
    .card-content {
        position: relative;
        width: 100%;
        height: 100%;
        background: var(--theme-bg-card);
        border: 2px solid var(--theme-border);
        border-radius: @border-radius-lg;
        padding: @spacing-lg;
        display: flex;
        flex-direction: column;
        align-items: center;
        overflow: hidden;
        transition: all @transition-smooth;
        cursor: pointer;

        // 四角装饰
        .card-corner {
            position: absolute;
            width: 16px;
            height: 16px;
            border-color: var(--theme-accent);
            border-style: solid;
            opacity: 0.5;
            transition: opacity 0.3s ease;

            &.top-left {
                top: 8px;
                left: 8px;
                border-width: 2px 0 0 2px;
            }

            &.top-right {
                top: 8px;
                right: 8px;
                border-width: 2px 2px 0 0;
            }

            &.bottom-left {
                bottom: 8px;
                left: 8px;
                border-width: 0 0 2px 2px;
            }

            &.bottom-right {
                bottom: 8px;
                right: 8px;
                border-width: 0 2px 2px 0;
            }
        }

        &:hover .card-corner {
            opacity: 1;
        }

        // 套装图标
        .set-icon-wrapper {
            position: relative;
            margin-bottom: @spacing-md;

            .set-icon {
                width: 72px;
                height: 72px;
                display: flex;
                align-items: center;
                justify-content: center;
                background: linear-gradient(
                    145deg,
                    var(--theme-bg-hover) 0%,
                    var(--theme-bg-dark) 100%
                );
                border: 2px solid var(--theme-border);
                border-radius: @border-radius-md;
                transition: all @transition-smooth;

                .icon-svg {
                    width: 48px;
                    height: 48px;
                    color: var(--theme-accent);
                    transition: all @transition-smooth;
                }
            }

            &:hover .set-icon {
                border-color: var(--theme-accent);
                box-shadow: 0 0 25px var(--theme-accent);

                .icon-svg {
                    transform: scale(1.1);
                    filter: drop-shadow(0 0 8px var(--theme-accent));
                }
            }
        }

        // 套装名称
        .set-name {
            font-family: @font-heading, sans-serif;
            font-size: 16px;
            font-weight: @font-weight-semibold;
            color: var(--theme-text-primary);
            text-align: center;
            margin: 0 0 @spacing-sm 0;
            letter-spacing: 0.05em;
            line-height: 1.3;
            max-width: 100%;
            overflow: hidden;
            text-overflow: ellipsis;
            white-space: nowrap;
        }

        // 套装积分和阶段
        .set-stats {
            display: flex;
            gap: @spacing-md;
            margin-bottom: @spacing-sm;

            .stat-item {
                display: flex;
                flex-direction: column;
                align-items: center;

                .stat-label {
                    font-size: 10px;
                    color: var(--theme-text-secondary);
                    margin-bottom: 2px;
                }

                .stat-value {
                    font-family: @font-mono, monospace;
                    font-size: 14px;
                    font-weight: @font-weight-bold;
                    color: var(--theme-accent);
                }

                .stage-value {
                    font-size: 12px;
                    color: var(--theme-info);
                }
            }
        }

        // 已激活效果
        .activated-effects {
            display: flex;
            gap: 4px;
            margin-top: @spacing-xs;

            .effect-badge {
                width: 20px;
                height: 20px;
                background: linear-gradient(135deg, var(--theme-success) 0%, var(--theme-info) 100%);
                border-radius: 50%;
                display: flex;
                align-items: center;
                justify-content: center;
                font-size: 10px;
                font-weight: @font-weight-bold;
                color: white;
                box-shadow: 0 2px 6px rgba(0, 229, 160, 0.3);

                &.more {
                    background: linear-gradient(135deg, var(--theme-secondary) 0%, var(--theme-primary) 100%);
                    box-shadow: 0 2px 6px rgba(123, 47, 255, 0.3);
                }
            }
        }
    }

    // 加载状态
    .loading-overlay {
        position: absolute;
        top: 0;
        left: 0;
        right: 0;
        bottom: 0;
        background: rgba(7, 9, 26, 0.8);
        border-radius: @border-radius-lg;
        display: flex;
        align-items: center;
        justify-content: center;
        z-index: 10;

        i {
            font-size: 32px;
            color: var(--theme-accent);
        }
    }

    // 稀有阶段 - 紫色
    &.stage-rare {
        .card-content {
            border-color: @rarity-rare;

            .set-icon {
                border-color: @rarity-rare;
                box-shadow: 0 0 15px rgba(168, 85, 247, 0.3);
            }

            .stage-value {
                color: @rarity-rare;
            }
        }

        &:hover .card-content {
            border-color: @rarity-rare;
            box-shadow: 0 0 20px rgba(168, 85, 247, 0.4);
        }
    }

    // 神器阶段 - 红色
    &.stage-legendary {
        .card-content {
            border-color: @rarity-legendary;

            .set-icon {
                border-color: @rarity-legendary;
                box-shadow: 0 0 15px rgba(239, 68, 68, 0.3);
            }

            .stage-value {
                color: @rarity-legendary;
            }
        }

        &:hover .card-content {
            border-color: @rarity-legendary;
            box-shadow: 0 0 20px rgba(239, 68, 68, 0.4);
        }
    }

    // 传说阶段 - 橙色
    &.stage-epic {
        .card-content {
            border-color: @rarity-epic;

            .set-icon {
                border-color: @rarity-epic;
                box-shadow: 0 0 15px rgba(249, 115, 22, 0.3);
            }

            .stage-value {
                color: @rarity-epic;
            }
        }

        &:hover .card-content {
            border-color: @rarity-epic;
            box-shadow: 0 0 20px rgba(249, 115, 22, 0.4);
        }
    }

    // 史诗阶段 - 金色
    &.stage-mythic {
        .card-content {
            border-color: @rarity-mythic;

            .set-icon {
                border-color: @rarity-mythic;
                box-shadow: 0 0 15px rgba(251, 191, 36, 0.3);
            }

            .stage-value {
                color: @rarity-mythic;
            }
        }

        &:hover .card-content {
            border-color: @rarity-mythic;
            box-shadow: 0 0 20px rgba(251, 191, 36, 0.4);
        }
    }

    // 太初阶段 - 蓝色渐变
    &.stage-primal {
        .card-content {
            border-color: @rarity-primal;

            .set-icon {
                border-color: @rarity-primal;
                background: linear-gradient(145deg, var(--theme-bg-hover) 0%, var(--theme-bg-dark) 100%),
                            linear-gradient(135deg, rgba(96, 165, 250, 0.2) 0%, rgba(29, 78, 216, 0.2) 100%);
                box-shadow: 0 0 15px rgba(96, 165, 250, 0.3);
            }

            .stage-value {
                color: @rarity-primal;
                text-shadow: 0 0 10px rgba(96, 165, 250, 0.5);
            }
        }

        &:hover .card-content {
            border-color: @rarity-primal;
            box-shadow: 0 0 25px rgba(96, 165, 250, 0.5);
        }
    }

    // 黑牙史诗阶段 - 深红色
    &.stage-blackEpic {
        .card-content {
            border-color: @rarity-blackEpic;

            .set-icon {
                border-color: @rarity-blackEpic;
                box-shadow: 0 0 15px rgba(220, 38, 38, 0.3);
            }

            .stage-value {
                color: @rarity-blackEpic;
            }
        }

        &:hover .card-content {
            border-color: @rarity-blackEpic;
            box-shadow: 0 0 20px rgba(220, 38, 38, 0.4);
        }
    }

    // 黑牙太初阶段 - 深蓝色
    &.stage-blackPrimal {
        .card-content {
            border-color: @rarity-blackPrimal;

            .set-icon {
                border-color: @rarity-blackPrimal;
                box-shadow: 0 0 15px rgba(29, 78, 216, 0.3);
            }

            .stage-value {
                color: @rarity-blackPrimal;
            }
        }

        &:hover .card-content {
            border-color: @rarity-blackPrimal;
            box-shadow: 0 0 20px rgba(29, 78, 216, 0.4);
        }
    }
}
</style>
