<!--
 * @Description: 装备套装卡片组件
 * @Author: Claude Code
 * @Date: 2026-04-17
 * @LastEditTime: 2026-04-17
 * @FilePath: /vue2-java-mysql-project/frontend/src/components/EquipmentSetCard.vue
-->
<template>
    <div
        class="equipment-set-card"
        :class="{ 'is-active': isActive }"
        @click="handleClick"
    >
        <!-- 卡片光晕 -->
        <div class="card-glow" />

        <!-- 卡片主体 -->
        <div class="card-content">
            <!-- 顶部装饰线 -->
            <div class="card-corner top-left" />
            <div class="card-corner top-right" />

            <!-- 套装图标区域 -->
            <div class="set-icon-wrapper">
                <div class="set-icon">
                    <!-- 根据套装类型显示不同图标 -->
                    <svg
                        v-if="setData.setType === 'special'"
                        viewBox="0 0 64 64"
                        class="icon-svg"
                    >
                        <circle
                            cx="32"
                            cy="32"
                            r="28"
                            fill="none"
                            stroke="currentColor"
                            stroke-width="2"
                        />
                        <path
                            d="M32 12 L44 24 L44 40 L32 52 L20 40 L20 24 Z"
                            fill="currentColor"
                            opacity="0.3"
                        />
                        <circle
                            cx="32"
                            cy="32"
                            r="8"
                            fill="currentColor"
                        />
                    </svg>
                    <svg
                        v-else
                        viewBox="0 0 64 64"
                        class="icon-svg"
                    >
                        <rect
                            x="8"
                            y="8"
                            width="48"
                            height="48"
                            rx="8"
                            fill="none"
                            stroke="currentColor"
                            stroke-width="2"
                        />
                        <path
                            d="M20 20 L32 32 L20 44"
                            fill="none"
                            stroke="currentColor"
                            stroke-width="3"
                            stroke-linecap="round"
                            stroke-linejoin="round"
                        />
                        <path
                            d="M32 32 L44 44"
                            fill="none"
                            stroke="currentColor"
                            stroke-width="3"
                            stroke-linecap="round"
                        />
                    </svg>
                </div>
                <!-- 套装序号 -->
                <div class="set-index">
                    {{ setData.setIndex || 1 }}
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

            <!-- 底部装饰 -->
            <div class="card-footer">
                <span class="footer-hint">
                    <i class="el-icon-view" />
                    点击查看详情
                </span>
            </div>

            <!-- 右下角装饰 -->
            <div class="card-corner bottom-left" />
            <div class="card-corner bottom-right" />
        </div>

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
                activatedEffects: []
            })
        },
        // 是否为当前激活套装
        isActive: {
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
        }
    },

    methods: {
        /**
         * 处理卡片点击事件
         */
        handleClick() {
            this.$emit('click', this.setData)
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
    cursor: pointer;
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
            border-color: @dnf-primary-gold;
            box-shadow: @dnf-shadow-gold;
        }
    }

    &.is-active {
        .card-content {
            border-color: @dnf-secondary-gold;
            box-shadow: @dnf-shadow-gold;
        }
    }

    // 卡片主体
    .card-content {
        position: relative;
        width: 100%;
        height: 100%;
        background: @gradient-card;
        border: 2px solid @dnf-border-dark;
        border-radius: @border-radius-lg;
        padding: @spacing-lg;
        display: flex;
        flex-direction: column;
        align-items: center;
        overflow: hidden;
        transition: all @transition-smooth;

        // 四角装饰
        .card-corner {
            position: absolute;
            width: 16px;
            height: 16px;
            border-color: @dnf-primary-gold;
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
                    @dnf-bg-hover 0%,
                    @dnf-primary-dark 100%
                );
                border: 2px solid @dnf-border-light;
                border-radius: @border-radius-md;
                transition: all @transition-smooth;

                .icon-svg {
                    width: 48px;
                    height: 48px;
                    color: @dnf-secondary-gold;
                    transition: all @transition-smooth;
                }
            }

            &:hover .set-icon {
                border-color: @dnf-primary-gold;
                box-shadow: @dnf-shadow-gold;

                .icon-svg {
                    transform: scale(1.1);
                    filter: drop-shadow(0 0 8px @dnf-primary-gold);
                }
            }

            // 套装序号
            .set-index {
                position: absolute;
                top: -8px;
                right: -8px;
                width: 24px;
                height: 24px;
                background: linear-gradient(135deg, @dnf-secondary-purple 0%, @dnf-primary-purple 100%);
                border-radius: 50%;
                display: flex;
                align-items: center;
                justify-content: center;
                font-size: 12px;
                font-weight: @font-weight-bold;
                color: white;
                box-shadow: 0 2px 8px rgba(0, 0, 0, 0.3);
            }
        }

        // 套装名称
        .set-name {
            font-family: @font-heading, sans-serif;
            font-size: 16px;
            font-weight: @font-weight-semibold;
            color: @dnf-text-primary;
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
                    color: @dnf-text-muted;
                    margin-bottom: 2px;
                }

                .stat-value {
                    font-family: @font-mono, monospace;
                    font-size: 14px;
                    font-weight: @font-weight-bold;
                    color: @dnf-secondary-gold;
                }

                .stage-value {
                    font-size: 12px;
                    color: @dnf-info;
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
                background: linear-gradient(135deg, @dnf-success 0%, @dnf-info 100%);
                border-radius: 50%;
                display: flex;
                align-items: center;
                justify-content: center;
                font-size: 10px;
                font-weight: @font-weight-bold;
                color: white;
                box-shadow: 0 2px 6px rgba(0, 229, 160, 0.3);

                &.more {
                    background: linear-gradient(135deg, @dnf-secondary-purple 0%, @dnf-primary-purple 100%);
                    box-shadow: 0 2px 6px rgba(123, 47, 255, 0.3);
                }
            }
        }

        // 底部装饰
        .card-footer {
            margin-top: auto;
            padding-top: @spacing-sm;

            .footer-hint {
                display: flex;
                align-items: center;
                gap: 4px;
                font-size: 11px;
                color: @dnf-text-muted;
                opacity: 0.7;
                transition: opacity 0.3s ease;

                i {
                    font-size: 12px;
                }
            }
        }

        &:hover .footer-hint {
            opacity: 1;
            color: @dnf-primary-gold;
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
            color: @dnf-secondary-gold;
        }
    }
}
</style>
