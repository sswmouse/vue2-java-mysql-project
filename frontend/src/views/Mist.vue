<template>
    <div class="mist-page">
        <!-- 顶部区域 -->
        <div class="page-header">
            <!-- 左侧：跨区选择 -->
            <div class="region-selector">
                <span class="region-label">跨区:</span>
                <el-dropdown
                    trigger="click"
                    @command="handleRegionChange"
                >
                    <span class="region-button">
                        {{ currentRegion }}
                        <i class="el-icon-arrow-down el-icon--right" />
                    </span>
                    <el-dropdown-menu slot="dropdown">
                        <el-dropdown-item
                            v-for="region in regions"
                            :key="region"
                            :command="region"
                        >
                            {{ region }}
                        </el-dropdown-item>
                    </el-dropdown-menu>
                </el-dropdown>
            </div>

            <!-- 中间：标题 -->
            <div class="page-title">
                <span class="title-text">迷雾融合</span>
            </div>

            <!-- 右侧：占位 -->
            <div class="header-right" />
        </div>

        <!-- 主内容区域 -->
        <div class="mist-content">
            <!-- 迷雾融合卡片 -->
            <div class="mist-card fog-fusion-card">
                <div class="card-inner">
                    <!-- 等级大数字 -->
                    <div class="level-display">
                        <span class="level-number">{{ fogData.level }}</span>
                    </div>

                    <!-- 进度条区域 -->
                    <div class="progress-bar-area">
                        <div class="progress-bar">
                            <div
                                class="progress-fill"
                                :style="{ width: progressPercent + '%' }"
                            >
                                <div class="progress-glow" />
                            </div>
                        </div>
                        <span class="progress-text">{{ progressPercent }}%</span>
                    </div>

                    <!-- 等级控制按钮 -->
                    <div class="level-controls">
                        <button
                            class="level-btn btn-min"
                            :disabled="fogData.level <= 1"
                            title="回到1级"
                            @click="handleLevelMin"
                        >
                            <span>&lt;&lt;</span>
                        </button>
                        <button
                            class="level-btn btn-minus"
                            :disabled="fogData.level <= 1"
                            title="等级-1"
                            @click="handleLevelDecrease"
                        >
                            <span>-</span>
                        </button>
                        <button
                            class="level-btn btn-plus"
                            :disabled="fogData.level >= fogData.maxLevel"
                            title="等级+1"
                            @click="handleLevelIncrease"
                        >
                            <span>+</span>
                        </button>
                        <button
                            class="level-btn btn-max"
                            :disabled="fogData.level >= fogData.maxLevel"
                            title="满级"
                            @click="handleLevelMax"
                        >
                            <span>&gt;&gt;</span>
                        </button>
                    </div>

                    <!-- 属性展示区域 -->
                    <div class="attributes-area">
                        <div class="attr-item">
                            <span class="attr-label">冒险家名望：</span>
                            <span class="attr-value">+{{ fogData.fameValue }}</span>
                        </div>
                        <div class="attr-item">
                            <span class="attr-label">技能伤害：</span>
                            <span class="attr-value">+{{ fogData.techAttack }}%</span>
                        </div>
                        <div class="attr-item">
                            <span class="attr-label">增益量：</span>
                            <span class="attr-value">+{{ fogData.buffBonus }}</span>
                        </div>
                        <div class="attr-item">
                            <span class="attr-label">四维：</span>
                            <span class="attr-value">+{{ fogData.fourStats }}</span>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>
</template>

<script>
import axios from 'axios'

// 迷雾等级属性表（1-30级完整数据）
const FOG_LEVEL_DATA = {
    1:  { fame: 1000, tech: 10.0, buff: 2400, fourStats: 0 },
    2:  { fame: 1050, tech: 10.5, buff: 2450, fourStats: 0 },
    3:  { fame: 1100, tech: 11.0, buff: 2500, fourStats: 0 },
    4:  { fame: 1150, tech: 11.5, buff: 2550, fourStats: 0 },
    5:  { fame: 1250, tech: 12.5, buff: 2650, fourStats: 30 },
    6:  { fame: 1300, tech: 13.0, buff: 2700, fourStats: 30 },
    7:  { fame: 1350, tech: 13.5, buff: 2750, fourStats: 30 },
    8:  { fame: 1400, tech: 14.0, buff: 2800, fourStats: 30 },
    9:  { fame: 1450, tech: 14.5, buff: 2850, fourStats: 30 },
    10: { fame: 1600, tech: 16.0, buff: 3000, fourStats: 80 },
    11: { fame: 1650, tech: 16.5, buff: 3050, fourStats: 80 },
    12: { fame: 1700, tech: 17.0, buff: 3100, fourStats: 80 },
    13: { fame: 1750, tech: 17.5, buff: 3150, fourStats: 80 },
    14: { fame: 1800, tech: 18.0, buff: 3200, fourStats: 80 },
    15: { fame: 1900, tech: 19.0, buff: 3300, fourStats: 110 },
    16: { fame: 1950, tech: 19.5, buff: 3350, fourStats: 110 },
    17: { fame: 2000, tech: 20.0, buff: 3400, fourStats: 110 },
    18: { fame: 2050, tech: 20.5, buff: 3450, fourStats: 110 },
    19: { fame: 2100, tech: 21.0, buff: 3500, fourStats: 110 },
    20: { fame: 2250, tech: 22.5, buff: 3650, fourStats: 160 },
    21: { fame: 2300, tech: 23.0, buff: 3700, fourStats: 160 },
    22: { fame: 2350, tech: 23.5, buff: 3750, fourStats: 160 },
    23: { fame: 2400, tech: 24.0, buff: 3800, fourStats: 160 },
    24: { fame: 2450, tech: 24.5, buff: 3850, fourStats: 160 },
    25: { fame: 2550, tech: 25.5, buff: 3950, fourStats: 190 },
    26: { fame: 2600, tech: 26.0, buff: 4000, fourStats: 190 },
    27: { fame: 2650, tech: 26.5, buff: 4050, fourStats: 190 },
    28: { fame: 2700, tech: 27.0, buff: 4100, fourStats: 190 },
    29: { fame: 2750, tech: 27.5, buff: 4150, fourStats: 190 },
    30: { fame: 3000, tech: 30.0, buff: 4400, fourStats: 250 }
}

export default {
    name: 'Mist',
    data() {
        return {
            // 当前跨区
            currentRegion: '跨六',
            regions: [
                '跨一', '跨二', '跨三A', '跨三B',
                '跨四', '跨五', '跨六', '跨七'
            ],

            // 迷雾数据
            fogData: {
                level: 30,
                maxLevel: 30,
                fameValue: 3000,
                techAttack: 30.0,
                buffBonus: 4400,
                fourStats: 250
            },

            // 用户ID（从localStorage获取）
            userId: 1,

            // 所有跨区的迷雾数据缓存
            fogCache: {}
        }
    },
    computed: {
        // 进度百分比
        progressPercent() {
            return Math.floor((this.fogData.level / this.fogData.maxLevel) * 100)
        }
    },
    created() {
        // 获取用户ID
        const user = JSON.parse(localStorage.getItem('user') || '{}')
        this.userId = user.id || 1

        // 初始化加载
        this.loadAllFogData()
    },
    methods: {
        // 根据等级获取属性数据
        getLevelData(level) {
            return FOG_LEVEL_DATA[level] || FOG_LEVEL_DATA[1]
        },

        // 加载所有跨区的迷雾数据
        async loadAllFogData() {
            try {
                const response = await axios.get(`/api/fogs?userId=${this.userId}`)
                this.fogCache = response.data

                // 如果当前跨区没有数据，设置默认值30
                if (!this.fogCache[this.currentRegion]) {
                    this.fogCache[this.currentRegion] = 30
                }

                // 更新当前跨区的数据显示
                this.updateCurrentFogDisplay()
            } catch (error) {
                console.error('加载迷雾数据失败:', error)
                // 初始化默认值
                this.initDefaultFogData()
            }
        },

        // 更新当前跨区的显示数据
        updateCurrentFogDisplay() {
            const level = this.fogCache[this.currentRegion] || 30
            this.fogData.level = level
            this.calculateFogStats()
        },

        // 初始化默认迷雾数据
        initDefaultFogData() {
            this.regions.forEach(region => {
                this.fogCache[region] = 30
            })
            this.updateCurrentFogDisplay()
        },

        // 跨区切换
        async handleRegionChange(region) {
            this.currentRegion = region
            this.updateCurrentFogDisplay()

            // 确保该跨区有数据
            if (!this.fogCache[region]) {
                await this.createFogData(region)
            }
        },

        // 创建跨区迷雾数据
        async createFogData() {
            try {
                await axios.post(`/api/fogs?userId=${this.userId}`)
                await this.loadAllFogData()
            } catch (error) {
                console.error('创建迷雾数据失败:', error)
            }
        },

        // 等级减少
        async handleLevelDecrease() {
            if (this.fogData.level <= 1) return
            this.fogData.level--
            this.calculateFogStats()
            await this.saveFogData()
        },

        // 等级增加
        async handleLevelIncrease() {
            if (this.fogData.level >= this.fogData.maxLevel) return
            this.fogData.level++
            this.calculateFogStats()
            await this.saveFogData()
        },

        // 回到1级
        async handleLevelMin() {
            if (this.fogData.level <= 1) return
            this.fogData.level = 1
            this.calculateFogStats()
            await this.saveFogData()
        },

        // 满级
        async handleLevelMax() {
            if (this.fogData.level >= this.fogData.maxLevel) return
            this.fogData.level = this.fogData.maxLevel
            this.calculateFogStats()
            await this.saveFogData()
        },

        // 保存迷雾数据到服务器
        async saveFogData() {
            try {
                await axios.put(`/api/fogs/${this.currentRegion}`, {
                    userId: this.userId,
                    level: this.fogData.level
                })
                // 更新缓存
                this.fogCache[this.currentRegion] = this.fogData.level
            } catch (error) {
                console.error('保存迷雾数据失败:', error)
            }
        },

        // 根据等级计算属性（使用查表法）
        calculateFogStats() {
            const levelData = this.getLevelData(this.fogData.level)
            this.fogData.fameValue = levelData.fame
            this.fogData.techAttack = levelData.tech
            this.fogData.buffBonus = levelData.buff
            this.fogData.fourStats = levelData.fourStats
        }
    }
}
</script>

<style lang="less" scoped>
@import '@/assets/styles/variables.less';
@import '@/assets/styles/mixins.less';

// ============================================
// 页面基础样式
// ============================================

.mist-page {
    padding: 24px;
    min-height: 100%;
    height: 100%;
    background: var(--theme-bg-dark);
    overflow-x: hidden;
    overflow-y: auto;
    position: relative;

    &::before {
        content: '';
        position: absolute;
        top: 0;
        left: 0;
        right: 0;
        bottom: 0;
        background:
            radial-gradient(ellipse at 50% 30%, rgba(30, 80, 180, 0.12) 0%, transparent 50%);
        pointer-events: none;
        z-index: 0;
    }
}

// ============================================
// 顶部区域
// ============================================

.page-header {
    display: flex;
    justify-content: space-between;
    align-items: center;
    padding: 16px 24px;
    background: var(--theme-bg-card);
    border: 1px solid var(--theme-border);
    border-radius: 12px;
    margin-bottom: 32px;
    position: relative;
    z-index: 1;

    .region-selector {
        display: flex;
        align-items: center;
        gap: 12px;

        .region-label {
            font-size: 14px;
            color: var(--theme-text-secondary);
        }

        .region-button {
            display: flex;
            align-items: center;
            gap: 8px;
            padding: 8px 16px;
            background: var(--theme-bg-hover);
            border: 1px solid var(--theme-border);
            border-radius: 8px;
            color: var(--theme-text-primary);
            font-size: 14px;
            cursor: pointer;
            transition: all 0.3s ease;

            &:hover {
                border-color: var(--theme-secondary);
            }
        }
    }

    .page-title {
        position: absolute;
        left: 50%;
        transform: translateX(-50%);

        .title-text {
            font-size: 22px;
            font-weight: 600;
            color: var(--theme-text-primary);
            letter-spacing: 0.1em;
        }
    }

    .header-right {
        width: 120px;
    }
}

// ============================================
// 主内容区域
// ============================================

.mist-content {
    display: flex;
    justify-content: center;
    position: relative;
    z-index: 1;
}

// ============================================
// 迷雾融合卡片
// ============================================

.mist-card {
    background: linear-gradient(180deg, #0c1525 0%, #0a1018 100%);
    border: 2px solid #1a3a6c;
    border-radius: 12px;
    overflow: hidden;
    width: 100%;
    max-width: 420px;
    box-shadow:
        0 0 30px rgba(30, 100, 200, 0.15),
        inset 0 1px 0 rgba(255, 255, 255, 0.05);
}

.fog-fusion-card {
    .card-inner {
        padding: 32px 24px;
    }

    // 等级大数字
    .level-display {
        text-align: center;
        margin-bottom: 20px;

        .level-number {
            font-size: 72px;
            font-weight: 900;
            color: #4a9eff;
            text-shadow:
                0 0 20px rgba(74, 158, 255, 0.6),
                0 0 40px rgba(74, 158, 255, 0.3),
                0 2px 4px rgba(0, 0, 0, 0.5);
            letter-spacing: -0.02em;
            line-height: 1;
        }
    }

    // 进度条区域
    .progress-bar-area {
        display: flex;
        align-items: center;
        gap: 12px;
        margin-bottom: 24px;

        .progress-bar {
            flex: 1;
            height: 16px;
            background: #141d2e;
            border: 1px solid #1e2a40;
            border-radius: 8px;
            overflow: hidden;
            position: relative;

            .progress-fill {
                height: 100%;
                background: linear-gradient(90deg, #1e5dc9 0%, #4a9eff 50%, #1e5dc9 100%);
                border-radius: 7px;
                transition: width 0.3s ease;
                position: relative;
                overflow: hidden;

                .progress-glow {
                    position: absolute;
                    top: 0;
                    left: 0;
                    right: 0;
                    height: 50%;
                    background: linear-gradient(180deg, rgba(255, 255, 255, 0.25) 0%, transparent 100%);
                }
            }
        }

        .progress-text {
            font-family: @font-mono, 'Share Tech Mono', monospace;
            font-size: 14px;
            color: #6a8aca;
            min-width: 45px;
            text-align: right;
        }
    }

    // 等级控制按钮
    .level-controls {
        display: flex;
        justify-content: center;
        gap: 8px;
        margin-bottom: 28px;

        .level-btn {
            width: 52px;
            height: 40px;
            border: 1px solid #2a4a7a;
            border-radius: 6px;
            background: linear-gradient(180deg, #141d2e 0%, #0c1520 100%);
            color: #8ab4ff;
            font-size: 16px;
            font-weight: bold;
            cursor: pointer;
            transition: all 0.2s ease;
            display: flex;
            align-items: center;
            justify-content: center;

            &:hover:not(:disabled) {
                background: linear-gradient(180deg, #1e2a40 0%, #141d2e 100%);
                border-color: #4a7ac0;
                box-shadow: 0 0 10px rgba(74, 158, 255, 0.2);
            }

            &:active:not(:disabled) {
                background: linear-gradient(180deg, #0c1520 0%, #141d2e 100%);
                transform: scale(0.98);
            }

            &:disabled {
                opacity: 0.35;
                cursor: not-allowed;
            }

            &.btn-min {
                font-size: 14px;
                color: #6a8aaa;
            }

            &.btn-minus {
                color: #ff8a6a;
            }

            &.btn-plus {
                color: #6aff8a;
            }

            &.btn-max {
                font-size: 14px;
                color: #ffaa6a;
            }
        }
    }

    // 属性展示区域
    .attributes-area {
        display: flex;
        flex-direction: column;
        gap: 12px;
        background: rgba(10, 16, 24, 0.8);
        border: 1px solid #1a2535;
        border-radius: 8px;
        padding: 20px 16px;

        .attr-item {
            display: flex;
            justify-content: center;
            align-items: baseline;
            gap: 4px;

            .attr-label {
                font-size: 15px;
                color: #8a9bb0;
            }

            .attr-value {
                font-family: @font-mono, 'Share Tech Mono', monospace;
                font-size: 18px;
                color: #c8d4e8;
                font-weight: 500;
            }
        }
    }
}
</style>
