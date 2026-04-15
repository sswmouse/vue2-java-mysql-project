<!--
 * @Description: 角色卡片组件 - 霓虹卡牌风格
 * @Author: Claude Code
 * @Date: 2026-04-14
 * @LastEditTime: 2026-04-14 11:38:05
 * @FilePath: /vue2-java-mysql-project/frontend/src/components/CharacterCard.vue
-->
<template>
    <div
        class="character-card-container"
        :class="{ flipped: isFlipped }"
        @click="handleCardClick"
        @mouseenter="handleMouseEnter"
        @mouseleave="handleMouseLeave"
        @dblclick="handleDoubleClick"
    >
        <!-- 卡牌光晕 -->
        <div class="card-glow" />
        <div class="card-glow-inner" />

        <!-- 卡牌主体 -->
        <div class="character-card">
            <!-- 卡片正面 - 角色外观 -->
            <div class="card-face card-front">
                <!-- 顶部装饰线 -->
                <div class="card-corner top-left" />
                <div class="card-corner top-right" />

                <!-- 卡片图片区域 -->
                <div class="card-image">
                    <div class="image-frame">
                        <img
                            v-if="character.avatarUrl"
                            :src="character.avatarUrl"
                            :alt="character.characterName"
                        >
                        <div
                            v-else
                            class="placeholder-image"
                        >
                            <div class="placeholder-inner">
                                <i class="el-icon-user-solid" />
                            </div>
                        </div>
                    </div>
                    <!-- 图片底部渐变遮罩 -->
                    <div class="image-overlay" />
                </div>

                <!-- 角色信息 -->
                <div class="card-info">
                    <h3 class="character-name">
                        {{ character.characterName }}
                    </h3>
                    <p class="character-type">
                        {{ character.characterType ? character.characterType.gender + character.characterType.className
                            : '' }}
                    </p>
                    <p class="character-job">
                        {{ character.characterType ? character.characterType.jobName : '' }}
                    </p>
                </div>

                <!-- 底部装饰 -->
                <div class="card-footer">
                    <div class="card-level">
                        <span class="level-label">LV</span>
                        <span class="level-value">{{ character.level || 1 }}</span>
                    </div>
                    <div class="card-hint">
                        <i class="el-icon-refresh-right" />
                        <span>查看详情</span>
                    </div>
                </div>

                <!-- 右下角装饰 -->
                <div class="card-corner bottom-left" />
                <div class="card-corner bottom-right" />
            </div>

            <!-- 卡片背面 - 炉石传说风格 -->
            <div class="card-face card-back">
                <character-card-back :character="character" />
            </div>
        </div>
    </div>
</template>

<script>
import CharacterCardBack from './CharacterCardBack.vue'

export default {
    name: 'CharacterCard',
    components: {
        CharacterCardBack
    },
    props: {
        character: {
            type: Object,
            required: true
        }
    },
    data() {
        return {
            isFlipped: false,
            isHovering: false
        }
    },
    methods: {
        handleCardClick() {
            this.isFlipped = !this.isFlipped
        },
        handleMouseEnter() {
            this.isHovering = true
        },
        handleMouseLeave() {
            this.isHovering = false
        },
        handleDoubleClick() {
            // 双击跳转到角色详情页
            this.$router.push(`/character/${this.character.id}`)
        }
    }
}
</script>

<style lang="less" scoped>
@import '@/assets/styles/variables.less';
@import '@/assets/styles/mixins.less';

.character-card-container {
    perspective: 1200px;
    cursor: pointer;
    width: 100%;
    max-width: 260px;
    margin: 0 auto;
    position: relative;
}

// ============================================
// 卡牌光晕效果
// ============================================

.card-glow {
    position: absolute;
    top: 50%;
    left: 50%;
    transform: translate(-50%, -50%);
    width: 120%;
    height: 120%;
    background: radial-gradient(ellipse at center,
            fade(@dnf-secondary-purple, 0.25) 0%,
            fade(@dnf-primary-gold, 0.1) 30%,
            transparent 70%);
    border-radius: 50%;
    filter: blur(30px);
    opacity: 0;
    transition: opacity 0.5s ease;
    pointer-events: none;
    z-index: -1;

    .character-card-container:hover & {
        opacity: 1;
        background: radial-gradient(ellipse at center,
                var(--theme-secondary) 0%,
                var(--theme-accent) 30%,
                transparent 70%);
    }
}

.card-glow-inner {
    position: absolute;
    top: 50%;
    left: 50%;
    transform: translate(-50%, -50%);
    width: 100%;
    height: 100%;
    background: radial-gradient(ellipse at center,
            var(--theme-info) 0%,
            transparent 60%);
    border-radius: 50%;
    filter: blur(20px);
    opacity: 0;
    transition: opacity 0.5s ease;
    pointer-events: none;
    z-index: -1;

    .character-card-container:hover & {
        opacity: 1;
        background: radial-gradient(ellipse at center,
                var(--theme-info) 0%,
                transparent 60%);
    }
}

.character-card-container:hover,
.character-card-container.flipped {
    .card-glow {
        animation: glow-pulse 3s ease-in-out infinite;
    }

    .card-glow-inner {
        animation: glow-pulse 4s ease-in-out infinite;
        animation-delay: 1s;
    }
}

// ============================================
// 卡牌主体
// ============================================

.character-card {
    position: relative;
    width: 100%;
    height: 380px;
    transform-style: preserve-3d;
    transform: translateY(-10px);
    transition: transform 0.6s cubic-bezier(0.4, 0, 0.2, 1), box-shadow 0.3s ease;
    animation: card-breathe 4s ease-in-out infinite;

    .card-face {
        position: absolute;
        width: 100%;
        height: 100%;
        backface-visibility: hidden;
        border-radius: @border-radius-xl;
        overflow: hidden;
        box-shadow:
            0 15px 35px rgba(0, 0, 0, 0.5),
            0 5px 15px rgba(0, 0, 0, 0.3),
            inset 0 1px 0 rgba(255, 255, 255, 0.1);
    }
}

// ============================================
// 卡片正面
// ============================================

.card-front {
    background: var(--theme-bg-card);
    border: 2px solid var(--theme-border);
    display: flex;
    flex-direction: column;

    // 四角装饰
    .card-corner {
        position: absolute;
        width: 20px;
        height: 20px;
        border-color: var(--theme-accent);
        border-style: solid;
        opacity: 0.5;
        transition: opacity 0.3s ease;
        z-index: 5;

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

    // 卡片图片
    .card-image {
        flex: 1;
        position: relative;
        overflow: hidden;

        .image-frame {
            width: 100%;
            height: 100%;
            display: flex;
            align-items: center;
            justify-content: center;
            position: relative;

            img {
                width: 100%;
                height: 100%;
                object-fit: cover;
                transition: transform 0.5s ease;
            }

            .placeholder-image {
                width: 100%;
                height: 100%;
                background: var(--theme-bg-hover);
                display: flex;
                align-items: center;
                justify-content: center;

                .placeholder-inner {
                    width: 100px;
                    height: 100px;
                    background: var(--theme-secondary);
                    border-radius: @border-radius-xl;
                    display: flex;
                    align-items: center;
                    justify-content: center;
                    box-shadow:
                        0 0 30px var(--theme-secondary),
                        inset 0 0 20px rgba(255, 255, 255, 0.1);
                    animation: logo-pulse 3s ease-in-out infinite;

                    i {
                        font-size: 50px;
                        color: white;
                        filter: drop-shadow(0 0 10px rgba(255, 255, 255, 0.5));
                    }
                }
            }
        }

        .image-overlay {
            position: absolute;
            bottom: 0;
            left: 0;
            right: 0;
            height: 60%;
            background: linear-gradient(180deg,
                    transparent 0%,
                    var(--theme-bg-dark) 100%);
            pointer-events: none;
        }
    }

    &:hover {
        border-color: var(--theme-accent);

        .card-image img {
            transform: scale(1.05);
        }
    }

    // 角色信息
    .card-info {
        padding: @spacing-md @spacing-lg;
        text-align: center;
        background: var(--theme-bg-dark);
        position: relative;

        &::before {
            content: '';
            position: absolute;
            top: 0;
            left: 20%;
            right: 20%;
            height: 1px;
            background: var(--theme-accent);
        }

        .character-name {
            font-family: @font-display, 'Orbitron', sans-serif;
            font-size: 18px;
            font-weight: 700;
            letter-spacing: 0.1em;
            color: var(--theme-accent);
            text-shadow: 0 0 15px var(--theme-accent);
            opacity: 0.6;
            margin: 0 0 @spacing-xs 0;
        }

        .character-type {
            font-size: 12px;
            color: var(--theme-info);
            margin: 0 0 @spacing-xs 0;
            letter-spacing: 0.05em;
        }

        .character-job {
            font-size: 11px;
            color: var(--theme-text-secondary);
            margin: 0;
            letter-spacing: 0.03em;
        }
    }

    // 底部装饰
    .card-footer {
        display: flex;
        justify-content: space-between;
        align-items: center;
        padding: @spacing-sm @spacing-lg @spacing-md;
        background: var(--theme-bg-dark);

        .card-level {
            display: flex;
            align-items: baseline;
            gap: 4px;

            .level-label {
                font-family: @font-display, 'Orbitron', sans-serif;
                font-size: 10px;
                font-weight: 700;
                color: var(--theme-text-secondary);
                letter-spacing: 0.1em;
            }

            .level-value {
                font-family: @font-display, 'Orbitron', sans-serif;
                font-size: 16px;
                font-weight: 700;
                color: var(--theme-success);
                text-shadow: 0 0 10px var(--theme-success);
            }
        }

        .card-hint {
            display: flex;
            align-items: center;
            gap: 4px;
            font-size: 11px;
            color: var(--theme-accent);
            opacity: 0.7;
            transition: opacity 0.3s ease;

            i {
                animation: pulse-dots 2s infinite;
            }

            &:hover {
                opacity: 1;
            }
        }
    }
}

// ============================================
// 卡片背面
// ============================================

.card-back {
    transform: rotateY(180deg);
}

// ============================================
// 翻转状态
// ============================================

.character-card-container.flipped {
    .character-card {
        transform: rotateY(180deg) translateY(-10px);
        animation: card-breathe-flipped 4s ease-in-out infinite;
    }
}

.character-card-container:hover:not(.flipped) .character-card {
    transform: translateY(-15px) scale(1.02);
    box-shadow:
        0 30px 60px rgba(0, 0, 0, 0.5),
        0 15px 30px rgba(0, 0, 0, 0.3);
    animation: card-hover 0.4s ease-out forwards;
}

.character-card-container.flipped:hover .character-card {
    box-shadow:
        0 30px 60px rgba(0, 0, 0, 0.5),
        0 15px 30px rgba(0, 0, 0, 0.3);
}

// ============================================
// 动画
// ============================================

@keyframes card-breathe {

    0%,
    100% {
        transform: translateY(-10px) rotateX(0deg);
    }

    50% {
        transform: translateY(-14px) rotateX(1deg);
    }
}

@keyframes card-breathe-flipped {

    0%,
    100% {
        transform: rotateY(180deg) translateY(-10px);
    }

    50% {
        transform: rotateY(180deg) translateY(-14px);
    }
}

@keyframes card-hover {
    0% {
        transform: translateY(-10px) scale(1.02);
    }

    100% {
        transform: translateY(-15px) scale(1.03);
    }
}

// ============================================
// 滚动条样式
// ============================================

.card-content::-webkit-scrollbar {
    width: 4px;
}

.card-content::-webkit-scrollbar-track {
    background: var(--theme-bg-hover);
    border-radius: 2px;
}

.card-content::-webkit-scrollbar-thumb {
    background: var(--theme-accent);
    border-radius: 2px;
    opacity: 0.3;

    &:hover {
        opacity: 0.5;
    }
}
</style>
