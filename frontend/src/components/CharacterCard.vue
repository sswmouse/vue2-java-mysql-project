<!--
 * @Description: 角色卡片组件
 * @Author: Claude Code
 * @Date: 2026-04-10
 * @LastEditTime: 2026-04-11 02:12:56
 * @FilePath: /vue2-java-mysql-project/frontend/src/components/CharacterCard.vue
 -->
<template>
    <div
        class="character-card-container"
        :class="{ flipped: isFlipped }"
        @click="handleCardClick"
    >
        <div class="character-card">
            <!-- 卡片正面 - 角色外观 -->
            <div class="card-face card-front">
                <div class="card-image">
                    <img
                        v-if="character.avatarUrl"
                        :src="character.avatarUrl"
                        :alt="character.characterName"
                    >
                    <div
                        v-else
                        class="placeholder-image"
                    >
                        <i class="el-icon-user" />
                    </div>
                </div>
                <div class="card-title">
                    <h3>{{ character.characterName }}</h3>
                    <p class="character-type">
                        {{ character.characterType ? character.characterType.gender + character.characterType.className
                            : '' }} - {{ character.characterType ? character.characterType.jobName : '' }}
                    </p>
                </div>
                <div class="card-hint">
                    <i class="el-icon-refresh-right" />
                    <span>点击查看详情</span>
                </div>
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
            isFlipped: false
        }
    },
    methods: {
        handleCardClick() {
            this.isFlipped = !this.isFlipped
        }
    }
}
</script>

<style lang="less" scoped>
.character-card-container {
    perspective: 1000px;
    cursor: pointer;
    width: 100%;
    max-width: 240px;
    margin: 0 auto;

    .character-card {
        position: relative;
        width: 100%;
        height: 360px;
        transform-style: preserve-3d;
        transform: translateY(-8px) scale(1.02);
        box-shadow: 0 20px 40px rgba(0, 0, 0, 0.4),
            0 8px 16px rgba(0, 0, 0, 0.3);
        transition: transform 0.6s cubic-bezier(0.4, 0, 0.2, 1), box-shadow 0.3s ease;
        animation: breath 3s ease-in-out infinite;

        .card-face {
            position: absolute;
            width: 100%;
            height: 100%;
            backface-visibility: hidden;
            border-radius: 16px;
            overflow: hidden;
            box-shadow: 0 10px 30px rgba(0, 0, 0, 0.3),
                0 4px 8px rgba(0, 0, 0, 0.2);
        }

        .card-front {
            background: linear-gradient(145deg, #2a2a3e 0%, #1a1a2e 100%);
            border: 3px solid #ffd700;
            display: flex;
            flex-direction: column;

            .card-image {
                flex: 1;
                display: flex;
                align-items: center;
                justify-content: center;
                background: linear-gradient(180deg, rgba(255, 215, 0, 0.1) 0%, transparent 100%);

                img {
                    width: 100%;
                    height: 100%;
                    object-fit: cover;
                }

                .placeholder-image {
                    width: 100px;
                    height: 100px;
                    border-radius: 50%;
                    background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
                    display: flex;
                    align-items: center;
                    justify-content: center;

                    i {
                        font-size: 50px;
                        color: white;
                    }
                }
            }

            .card-title {
                padding: 16px;
                text-align: center;
                background: linear-gradient(180deg, transparent 0%, rgba(0, 0, 0, 0.5) 100%);

                h3 {
                    margin: 0 0 6px 0;
                    font-size: 16px;
                    color: #ffd700;
                    text-shadow: 0 2px 4px rgba(0, 0, 0, 0.5);
                }

                .character-type {
                    margin: 0;
                    font-size: 11px;
                    color: #a0a0b0;
                }
            }

            .card-hint {
                padding: 6px 12px;
                display: flex;
                align-items: center;
                justify-content: center;
                gap: 6px;
                font-size: 11px;
                color: #ffd700;
                background: rgba(255, 215, 0, 0.1);

                i {
                    animation: pulse 2s infinite;
                }
            }
        }

        .card-back {
            transform: rotateY(180deg);
        }
    }

    &.flipped {
        .character-card {
            transform: rotateY(180deg) translateY(-8px) scale(1.02);
            animation: breath-flipped 3s ease-in-out infinite;
        }
    }

    &:hover .character-card {
        box-shadow: 0 25px 50px rgba(0, 0, 0, 0.5),
            0 12px 24px rgba(0, 0, 0, 0.4);
    }

    &.flipped:hover .character-card {
        box-shadow: 0 25px 50px rgba(0, 0, 0, 0.5),
            0 12px 24px rgba(0, 0, 0, 0.4);
    }
}

@keyframes pulse {

    0%,
    100% {
        opacity: 1;
    }

    50% {
        opacity: 0.5;
    }
}

@keyframes shake {

    0%,
    100% {
        transform: translateY(-8px) scale(1.02) rotateZ(-0.5deg);
    }

    50% {
        transform: translateY(-8px) scale(1.02) rotateZ(0.5deg);
    }
}

@keyframes shake-flipped {

    0%,
    100% {
        transform: rotateY(180deg) translateY(-8px) scale(1.02) rotateZ(-0.5deg);
    }

    50% {
        transform: rotateY(180deg) translateY(-8px) scale(1.02) rotateZ(0.5deg);
    }
}

@keyframes breath {

    0%,
    100% {
        transform: translateY(-8px) scale(1.02);
    }

    50% {
        transform: translateY(-12px) scale(1.02);
    }
}

@keyframes breath-flipped {

    0%,
    100% {
        transform: rotateY(180deg) translateY(-8px) scale(1.02);
    }

    50% {
        transform: rotateY(180deg) translateY(-12px) scale(1.02);
    }
}

/* 滚动条样式 */
.card-content::-webkit-scrollbar {
    width: 4px;
}

.card-content::-webkit-scrollbar-track {
    background: rgba(255, 255, 255, 0.05);
    border-radius: 2px;
}

.card-content::-webkit-scrollbar-thumb {
    background: rgba(255, 215, 0, 0.3);
    border-radius: 2px;

    &:hover {
        background: rgba(255, 215, 0, 0.5);
    }
}

</style>
