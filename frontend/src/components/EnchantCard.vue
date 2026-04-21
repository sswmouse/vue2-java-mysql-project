<!--
 * @Description: 附魔卡片组件
 * @Author: Claude Code
 * @Date: 2026-04-20
 * @LastEditTime: 2026-04-20
 * @FilePath: /vue2-java-mysql-project/frontend/src/components/EnchantCard.vue
-->
<template>
    <div
        class="enchant-card"
        :class="{ 'not-enchanted': !enchantData }"
        @click="handleClick"
    >
        <!-- 卡片光晕 -->
        <div class="card-glow" />

        <!-- 卡片主体 -->
        <div class="card-content">
            <!-- 顶部装饰 -->
            <div class="card-corner top-left" />
            <div class="card-corner top-right" />

            <!-- 部位图标 -->
            <div class="part-icon">
                <!-- 武器 - 剑 -->
                <svg
                    v-if="partName === '武器'"
                    class="part-svg"
                    viewBox="0 0 24 24"
                    fill="none"
                    stroke="currentColor"
                    stroke-width="2"
                >
                    <path d="M14.5 17.5L3 6V3h3l11.5 11.5M12 12L6.5 6.5M5 14l-2 2 5 5 2-2M18 3l3 3-9 9-3-3 9-9z" />
                </svg>
                <!-- 头肩 - 头盔 -->
                <svg
                    v-else-if="partName === '头肩'"
                    class="part-svg"
                    viewBox="0 0 24 24"
                    fill="none"
                    stroke="currentColor"
                    stroke-width="2"
                >
                    <path d="M12 2C6.48 2 2 6.48 2 12v2c0 1.1.9 2 2 2h1v-6H4v-1c0-4.42 3.58-8 8-8s8 3.58 8 8v1h-1v6h1c1.1 0 2-.9 2-2v-2c0-5.52-4.48-10-10-10z" />
                    <path d="M9 14v2c0 1.66 1.34 3 3 3s3-1.34 3-3v-2" />
                </svg>
                <!-- 上衣 - 衣服 -->
                <svg
                    v-else-if="partName === '上衣'"
                    class="part-svg"
                    viewBox="0 0 24 24"
                    fill="none"
                    stroke="currentColor"
                    stroke-width="2"
                >
                    <path d="M6 2L2 6v14c0 1.1.9 2 2 2h16c1.1 0 2-.9 2-2V6l-4-4H6zM6 4h12l2 2H4l2-2zM4 8h16v10H4V8z" />
                </svg>
                <!-- 下装 - 裤子 -->
                <svg
                    v-else-if="partName === '下装'"
                    class="part-svg"
                    viewBox="0 0 24 24"
                    fill="none"
                    stroke="currentColor"
                    stroke-width="2"
                >
                    <path d="M5 4h14v4l-2 14h-4l-1-8-1 8H7L5 8V4z" />
                </svg>
                <!-- 鞋 - 鞋子 -->
                <svg
                    v-else-if="partName === '鞋'"
                    class="part-svg"
                    viewBox="0 0 24 24"
                    fill="none"
                    stroke="currentColor"
                    stroke-width="2"
                >
                    <path d="M4 18h16c1.1 0 2-.9 2-2v-2c0-2.21-1.79-4-4-4h-2l-2-3H6c-2.21 0-4 1.79-4 4v5c0 1.1.9 2 2 2z" />
                    <path d="M6 13h4l2 3h2c1.1 0 2 .9 2 2" />
                </svg>
                <!-- 腰带 - 腰带 -->
                <svg
                    v-else-if="partName === '腰带'"
                    class="part-svg"
                    viewBox="0 0 24 24"
                    fill="none"
                    stroke="currentColor"
                    stroke-width="2"
                >
                    <rect
                        x="2"
                        y="8"
                        width="20"
                        height="8"
                        rx="2"
                    />
                    <circle
                        cx="12"
                        cy="12"
                        r="2"
                    />
                    <path d="M2 10h2M2 14h2M20 10h2M20 14h2" />
                </svg>
                <!-- 项链 - 项链 -->
                <svg
                    v-else-if="partName === '项链'"
                    class="part-svg"
                    viewBox="0 0 24 24"
                    fill="none"
                    stroke="currentColor"
                    stroke-width="2"
                >
                    <circle
                        cx="12"
                        cy="14"
                        r="4"
                    />
                    <path d="M12 2v4M6 6l2 2M18 6l-2 2M8 10c0-2.21 1.79-4 4-4s4 1.79 4 4" />
                </svg>
                <!-- 手镯 - 手镯 -->
                <svg
                    v-else-if="partName === '手镯'"
                    class="part-svg"
                    viewBox="0 0 24 24"
                    fill="none"
                    stroke="currentColor"
                    stroke-width="2"
                >
                    <circle
                        cx="12"
                        cy="12"
                        r="8"
                    />
                    <circle
                        cx="12"
                        cy="12"
                        r="5"
                    />
                    <path d="M12 4v2M12 18v2M4 12h2M18 12h2" />
                </svg>
                <!-- 戒指 - 戒指 -->
                <svg
                    v-else-if="partName === '戒指'"
                    class="part-svg"
                    viewBox="0 0 24 24"
                    fill="none"
                    stroke="currentColor"
                    stroke-width="2"
                >
                    <circle
                        cx="12"
                        cy="12"
                        r="6"
                    />
                    <path d="M12 6v2M12 16v2M6 12h2M16 12h2" />
                    <path
                        d="M12 8l1 1-1 1-1-1z"
                        fill="currentColor"
                    />
                </svg>
                <!-- 辅助装备 - 魔法棒 -->
                <svg
                    v-else-if="partName === '辅助装备'"
                    class="part-svg"
                    viewBox="0 0 24 24"
                    fill="none"
                    stroke="currentColor"
                    stroke-width="2"
                >
                    <path d="M15 4l6 6-9 9-6-6 9-9z" />
                    <path d="M6 13l4 4" />
                    <path d="M2 22l3-3" />
                    <circle
                        cx="15"
                        cy="9"
                        r="1"
                        fill="currentColor"
                    />
                </svg>
                <!-- 魔法石 - 宝石 -->
                <svg
                    v-else-if="partName === '魔法石'"
                    class="part-svg"
                    viewBox="0 0 24 24"
                    fill="none"
                    stroke="currentColor"
                    stroke-width="2"
                >
                    <path d="M12 2l8 6-8 14-8-14 8-6z" />
                    <path d="M4 8h16" />
                    <path d="M8 8l4 10 4-10" />
                </svg>
                <!-- 耳环 - 耳环 -->
                <svg
                    v-else-if="partName === '耳环'"
                    class="part-svg"
                    viewBox="0 0 24 24"
                    fill="none"
                    stroke="currentColor"
                    stroke-width="2"
                >
                    <circle
                        cx="12"
                        cy="5"
                        r="2"
                    />
                    <path d="M12 7v3" />
                    <circle
                        cx="12"
                        cy="14"
                        r="5"
                    />
                    <circle
                        cx="12"
                        cy="14"
                        r="2"
                    />
                </svg>
                <!-- 默认图标 -->
                <svg
                    v-else
                    class="part-svg"
                    viewBox="0 0 24 24"
                    fill="none"
                    stroke="currentColor"
                    stroke-width="2"
                >
                    <rect
                        x="3"
                        y="3"
                        width="18"
                        height="18"
                        rx="2"
                    />
                    <path d="M9 9h6v6H9z" />
                </svg>
            </div>

            <!-- 部位名称 -->
            <h3 class="part-name">
                {{ partName }}
            </h3>

            <!-- 附魔信息 -->
            <div
                v-if="enchantData && enchantData.enchantName"
                class="enchant-info"
            >
                <div class="enchant-name">
                    {{ enchantData.enchantName }}
                </div>
                <div class="enchant-attr">
                    {{ enchantData.attributeValue }}
                </div>
                <div
                    v-if="enchantData.fame"
                    class="enchant-fame"
                >
                    名望+{{ enchantData.fame }}
                </div>
            </div>
            <div
                v-else
                class="enchant-empty"
            >
                未附魔
            </div>

            <!-- 右下角装饰 -->
            <div class="card-corner bottom-left" />
            <div class="card-corner bottom-right" />
        </div>
    </div>
</template>

<script>
/**
 * 附魔卡片组件
 * 用于展示单个装备部位的附魔状态
 */
export default {
    name: 'EnchantCard',

    props: {
        // 部位名称
        partName: {
            type: String,
            required: true
        },
        // 附魔数据
        enchantData: {
            type: Object,
            default: null
        }
    },

    methods: {
        handleClick() {
            this.$emit('click', {
                partName: this.partName,
                enchantData: this.enchantData
            })
        }
    }
}
</script>

<style lang="less" scoped>
@import '@/assets/styles/variables.less';

.enchant-card {
    position: relative;
    width: 100%;
    height: 180px;
    border-radius: @border-radius-lg;
    transition: all @transition-smooth;
    cursor: pointer;

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
        transform: translateY(-5px);

        .card-glow {
            opacity: 1;
        }

        .card-content {
            border-color: var(--theme-accent);
            box-shadow: 0 0 25px var(--theme-accent);
        }
    }

    &.not-enchanted {
        .card-content {
            border-style: dashed;
            opacity: 0.7;
        }

        &:hover .card-content {
            opacity: 1;
        }
    }

    .card-content {
        position: relative;
        width: 100%;
        height: 100%;
        background: var(--theme-bg-card);
        border: 2px solid var(--theme-border);
        border-radius: @border-radius-lg;
        padding: @spacing-md;
        display: flex;
        flex-direction: column;
        align-items: center;
        overflow: hidden;
        transition: all @transition-smooth;

        .card-corner {
            position: absolute;
            width: 14px;
            height: 14px;
            border-color: var(--theme-accent);
            border-style: solid;
            opacity: 0.4;

            &.top-left {
                top: 6px;
                left: 6px;
                border-width: 2px 0 0 2px;
            }

            &.top-right {
                top: 6px;
                right: 6px;
                border-width: 2px 2px 0 0;
            }

            &.bottom-left {
                bottom: 6px;
                left: 6px;
                border-width: 0 0 2px 2px;
            }

            &.bottom-right {
                bottom: 6px;
                right: 6px;
                border-width: 0 2px 2px 0;
            }
        }

        .part-icon {
            width: 48px;
            height: 48px;
            background: linear-gradient(145deg, var(--theme-bg-hover) 0%, var(--theme-bg-dark) 100%);
            border: 2px solid var(--theme-border);
            border-radius: 50%;
            display: flex;
            align-items: center;
            justify-content: center;
            margin-bottom: @spacing-sm;

            .part-svg {
                width: 28px;
                height: 28px;
                color: var(--theme-accent);
            }
        }

        .part-name {
            font-family: @font-heading, sans-serif;
            font-size: 15px;
            font-weight: @font-weight-semibold;
            color: var(--theme-accent);
            margin: 0 0 @spacing-xs 0;
        }

        .enchant-info {
            text-align: center;

            .enchant-name {
                font-size: 13px;
                color: var(--theme-text-primary);
                margin-bottom: 4px;
            }

            .enchant-attr {
                font-size: 12px;
                color: var(--theme-info);
            }

            .enchant-fame {
                font-size: 11px;
                color: var(--theme-accent);
                margin-top: 4px;
            }
        }

        .enchant-empty {
            font-size: 13px;
            color: var(--theme-text-secondary);
        }
    }
}
</style>
