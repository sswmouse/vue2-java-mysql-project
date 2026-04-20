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
                <i :class="partIcon" />
            </div>

            <!-- 部位名称 -->
            <h3 class="part-name">{{ partName }}</h3>

            <!-- 附魔信息 -->
            <div
                v-if="enchantData && enchantData.enchantName"
                class="enchant-info"
            >
                <div class="enchant-name">{{ enchantData.enchantName }}</div>
                <div class="enchant-attr">{{ enchantData.attributeValue }}</div>
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

    computed: {
        // 根据部位名称返回对应图标
        partIcon() {
            const iconMap = {
                '武器': 'el-icon-suitcase-1',
                '头肩': 'el-icon-user',
                '上衣': 'el-icon-suitcase',
                '下装': 'el-icon-document',
                '鞋': 'el-icon-shoes',
                '腰带': 'el-icon-bangzhu',
                '项链': 'el-icon-star-on',
                '手镯': 'el-icon-circle-check',
                '戒指': 'el-icon-circle-close',
                '辅助装备': 'el-icon-magic-stick',
                '魔法石': 'el-icon-gem',
                '耳环': 'el-icon-headset'
            }
            return iconMap[this.partName] || 'el-icon-box'
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
            border-color: @dnf-primary-gold;
            box-shadow: @dnf-shadow-gold;
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
        background: @gradient-card;
        border: 2px solid @dnf-border-dark;
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
            border-color: @dnf-primary-gold;
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
            background: linear-gradient(145deg, @dnf-bg-hover 0%, @dnf-primary-dark 100%);
            border: 2px solid @dnf-border-light;
            border-radius: 50%;
            display: flex;
            align-items: center;
            justify-content: center;
            margin-bottom: @spacing-sm;

            i {
                font-size: 24px;
                color: @dnf-secondary-gold;
            }
        }

        .part-name {
            font-family: @font-heading, sans-serif;
            font-size: 15px;
            font-weight: @font-weight-semibold;
            color: @dnf-primary-gold;
            margin: 0 0 @spacing-xs 0;
        }

        .enchant-info {
            text-align: center;

            .enchant-name {
                font-size: 13px;
                color: @dnf-text-primary;
                margin-bottom: 4px;
            }

            .enchant-attr {
                font-size: 12px;
                color: @dnf-info;
            }
        }

        .enchant-empty {
            font-size: 13px;
            color: @dnf-text-muted;
        }
    }
}
</style>
