<!--
 * @Description: 角色卡片组件
 * @Author: Claude Code
 * @Date: 2026-04-10
 * @LastEditTime: 2026-04-10
 * @FilePath: /frontend/src/components/CharacterCard.vue
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
                    />
                    <div v-else class="placeholder-image">
                        <i class="el-icon-user" />
                    </div>
                </div>
                <div class="card-title">
                    <h3>{{ character.characterName }}</h3>
                    <p class="character-type">
                        {{ character.characterType ? character.characterType.gender + character.characterType.className : '' }} - {{ character.characterType ? character.characterType.jobName : '' }}
                    </p>
                </div>
                <div class="card-hint">
                    <i class="el-icon-refresh-right" />
                    <span>点击查看详情</span>
                </div>
            </div>

            <!-- 卡片背面 - 角色信息 -->
            <div class="card-face card-back">
                <div class="card-header">
                    <h3>{{ character.characterName }}</h3>
                    <span class="job-nature" :class="character.jobNature === '奶系' ? 'healer' : 'dps'">
                        {{ character.jobNature }}
                    </span>
                </div>

                <div class="card-content">
                    <!-- 职业信息 -->
                    <div class="info-section">
                        <h4>职业信息</h4>
                        <div class="info-row">
                            <span>职业：</span>
                            <span>{{ character.characterType ? character.characterType.jobName : '' }}</span>
                        </div>
                        <div class="info-row">
                            <span>属性：</span>
                            <span>{{ character.jobAttribute }}</span>
                        </div>
                    </div>

                    <!-- 四维 -->
                    <div class="info-section" v-if="character.jobNature === '奶系'">
                        <h4>四维</h4>
                        <div class="stats-grid">
                            <div class="stat-item">
                                <span class="stat-label">智力</span>
                                <span class="stat-value">{{ character.intelligence || 0 }}</span>
                            </div>
                            <div class="stat-item">
                                <span class="stat-label">精神</span>
                                <span class="stat-value">{{ character.spirit || 0 }}</span>
                            </div>
                            <div class="stat-item">
                                <span class="stat-label">体力</span>
                                <span class="stat-value">{{ character.vitality || 0 }}</span>
                            </div>
                        </div>
                    </div>

                    <!-- 三攻 -->
                    <div class="info-section" v-if="character.jobNature === '输出'">
                        <h4>三攻</h4>
                        <div class="stats-grid">
                            <div class="stat-item">
                                <span class="stat-label">物攻</span>
                                <span class="stat-value">{{ character.physicalAttack || 0 }}</span>
                            </div>
                            <div class="stat-item">
                                <span class="stat-label">魔攻</span>
                                <span class="stat-value">{{ character.magicalAttack || 0 }}</span>
                            </div>
                            <div class="stat-item">
                                <span class="stat-label">独立</span>
                                <span class="stat-value">{{ character.independentAttack || 0 }}</span>
                            </div>
                        </div>
                    </div>

                    <!-- 属强 -->
                    <div class="info-section">
                        <h4>属强</h4>
                        <div class="element-grid">
                            <div class="element-item light">
                                <span class="element-label">光</span>
                                <span class="element-value">{{ character.lightElement || 0 }}</span>
                            </div>
                            <div class="element-item fire">
                                <span class="element-label">火</span>
                                <span class="element-value">{{ character.fireElement || 0 }}</span>
                            </div>
                            <div class="element-item ice">
                                <span class="element-label">冰</span>
                                <span class="element-value">{{ character.iceElement || 0 }}</span>
                            </div>
                            <div class="element-item dark">
                                <span class="element-label">暗</span>
                                <span class="element-value">{{ character.darkElement || 0 }}</span>
                            </div>
                        </div>
                    </div>

                    <!-- 最高属强 -->
                    <div class="info-section" v-if="character.maxElement > 0">
                        <div class="max-element">
                            <span>最高属强：</span>
                            <span class="max-value">{{ character.maxElement }}</span>
                        </div>
                    </div>
                </div>

                <div class="card-hint">
                    <i class="el-icon-refresh-left" />
                    <span>点击返回</span>
                </div>
            </div>
        </div>
    </div>
</template>

<script>
export default {
    name: 'CharacterCard',
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
    max-width: 280px;
    margin: 0 auto;

    .character-card {
        position: relative;
        width: 100%;
        height: 420px;
        transform-style: preserve-3d;
        transition: transform 0.6s cubic-bezier(0.4, 0, 0.2, 1);

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
                    width: 120px;
                    height: 120px;
                    border-radius: 50%;
                    background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
                    display: flex;
                    align-items: center;
                    justify-content: center;

                    i {
                        font-size: 60px;
                        color: white;
                    }
                }
            }

            .card-title {
                padding: 16px;
                text-align: center;
                background: linear-gradient(180deg, transparent 0%, rgba(0, 0, 0, 0.5) 100%);

                h3 {
                    margin: 0 0 8px 0;
                    font-size: 18px;
                    color: #ffd700;
                    text-shadow: 0 2px 4px rgba(0, 0, 0, 0.5);
                }

                .character-type {
                    margin: 0;
                    font-size: 12px;
                    color: #a0a0b0;
                }
            }

            .card-hint {
                padding: 8px 16px;
                display: flex;
                align-items: center;
                justify-content: center;
                gap: 6px;
                font-size: 12px;
                color: #ffd700;
                background: rgba(255, 215, 0, 0.1);

                i {
                    animation: pulse 2s infinite;
                }
            }
        }

        .card-back {
            background: linear-gradient(145deg, #1a1a2e 0%, #0f0f1a 100%);
            border: 3px solid #c0c0c0;
            transform: rotateY(180deg);
            display: flex;
            flex-direction: column;

            .card-header {
                padding: 16px;
                background: linear-gradient(180deg, rgba(192, 192, 192, 0.2) 0%, transparent 100%);
                display: flex;
                justify-content: space-between;
                align-items: center;

                h3 {
                    margin: 0;
                    font-size: 16px;
                    color: #c0c0c0;
                }

                .job-nature {
                    padding: 4px 12px;
                    border-radius: 12px;
                    font-size: 12px;
                    font-weight: bold;

                    &.dps {
                        background: linear-gradient(135deg, #ff6b6b 0%, #ee5a5a 100%);
                        color: white;
                    }

                    &.healer {
                        background: linear-gradient(135deg, #4ecdc4 0%, #44a08d 100%);
                        color: white;
                    }
                }
            }

            .card-content {
                flex: 1;
                overflow-y: auto;
                padding: 12px 16px;

                .info-section {
                    margin-bottom: 16px;

                    h4 {
                        margin: 0 0 8px 0;
                        font-size: 12px;
                        color: #ffd700;
                        text-transform: uppercase;
                        letter-spacing: 1px;
                    }

                    .info-row {
                        display: flex;
                        justify-content: space-between;
                        padding: 4px 0;
                        font-size: 13px;
                        color: #a0a0b0;

                        span:first-child {
                            color: #c0c0c0;
                        }

                        span:last-child {
                            color: #ffffff;
                            font-weight: 500;
                        }
                    }

                    .stats-grid {
                        display: grid;
                        grid-template-columns: repeat(3, 1fr);
                        gap: 8px;

                        .stat-item {
                            background: rgba(255, 255, 255, 0.05);
                            border-radius: 8px;
                            padding: 8px;
                            text-align: center;

                            .stat-label {
                                display: block;
                                font-size: 11px;
                                color: #a0a0b0;
                                margin-bottom: 4px;
                            }

                            .stat-value {
                                display: block;
                                font-size: 16px;
                                font-weight: bold;
                                color: #ffd700;
                            }
                        }
                    }

                    .element-grid {
                        display: grid;
                        grid-template-columns: repeat(4, 1fr);
                        gap: 6px;

                        .element-item {
                            background: rgba(255, 255, 255, 0.05);
                            border-radius: 8px;
                            padding: 8px 4px;
                            text-align: center;
                            border: 1px solid transparent;
                            transition: all 0.3s;

                            &.light {
                                border-color: rgba(255, 255, 0, 0.3);

                                &:hover {
                                    background: rgba(255, 255, 0, 0.1);
                                }
                            }

                            &.fire {
                                border-color: rgba(255, 100, 0, 0.3);

                                &:hover {
                                    background: rgba(255, 100, 0, 0.1);
                                }
                            }

                            &.ice {
                                border-color: rgba(0, 200, 255, 0.3);

                                &:hover {
                                    background: rgba(0, 200, 255, 0.1);
                                }
                            }

                            &.dark {
                                border-color: rgba(150, 0, 200, 0.3);

                                &:hover {
                                    background: rgba(150, 0, 200, 0.1);
                                }
                            }

                            .element-label {
                                display: block;
                                font-size: 11px;
                                color: #a0a0b0;
                                margin-bottom: 4px;
                            }

                            .element-value {
                                display: block;
                                font-size: 14px;
                                font-weight: bold;
                                color: #ffffff;
                            }
                        }
                    }

                    .max-element {
                        display: flex;
                        justify-content: center;
                        align-items: center;
                        gap: 8px;
                        padding: 8px;
                        background: rgba(255, 215, 0, 0.1);
                        border-radius: 8px;
                        font-size: 13px;
                        color: #a0a0b0;

                        .max-value {
                            font-size: 18px;
                            font-weight: bold;
                            color: #ffd700;
                        }
                    }
                }
            }

            .card-hint {
                padding: 8px 16px;
                display: flex;
                align-items: center;
                justify-content: center;
                gap: 6px;
                font-size: 12px;
                color: #c0c0c0;
                background: rgba(192, 192, 192, 0.1);

                i {
                    animation: pulse 2s infinite;
                }
            }
        }
    }

    &.flipped {
        .character-card {
            transform: rotateY(180deg);
        }
    }
}

@keyframes pulse {
    0%, 100% {
        opacity: 1;
    }
    50% {
        opacity: 0.5;
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
