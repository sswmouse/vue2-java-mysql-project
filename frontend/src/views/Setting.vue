<!--
 * @Description: 设置页面 - 主题风格切换
 * @Author: Claude Code
 * @Date: 2026-04-14
-->
<template>
    <div class="setting-page">
        <div class="setting-container">
            <!-- 页面标题 -->
            <div class="page-header">
                <h1 class="page-title">
                    <i class="el-icon-setting" /> 系统设置
                </h1>
            </div>

            <!-- 主题设置 -->
            <section class="setting-section">
                <div class="section-header">
                    <h2><i class="el-icon-magic-stick" /> 主题风格</h2>
                    <p>选择您喜欢的界面风格，每种主题都有独特的视觉体验</p>
                </div>

                <!-- 暗色主题 -->
                <div class="theme-category">
                    <h3 class="category-title">
                        <i class="el-icon-moon" /> 暗色主题
                    </h3>
                    <div class="theme-grid">
                        <div
                            v-for="theme in darkThemes"
                            :key="theme.id"
                            class="theme-card"
                            :class="{ active: currentTheme === theme.id }"
                            @click="selectTheme(theme.id)"
                        >
                            <!-- 主题预览 -->
                            <div
                                class="theme-preview"
                                :style="getPreviewStyle(theme)"
                            >
                                <div class="preview-header">
                                    <div class="preview-dots">
                                        <span class="dot dot-red" />
                                        <span class="dot dot-yellow" />
                                        <span class="dot dot-green" />
                                    </div>
                                    <div class="preview-title">
                                        DNF管理
                                    </div>
                                </div>
                                <div class="preview-sidebar">
                                    <div
                                        class="preview-nav"
                                        :style="{ background: theme.colors.primary }"
                                    />
                                    <div
                                        class="preview-nav"
                                        :style="{ background: theme.colors.secondary }"
                                    />
                                    <div
                                        class="preview-nav"
                                        :style="{ background: theme.colors.secondary }"
                                    />
                                </div>
                                <div class="preview-content">
                                    <div
                                        class="preview-card"
                                        :style="{
                                            background: theme.colors.bgCard,
                                            borderColor: theme.colors.primary
                                        }"
                                    />
                                    <div
                                        class="preview-card"
                                        :style="{
                                            background: theme.colors.bgCard,
                                            borderColor: 'transparent'
                                        }"
                                    />
                                </div>
                            </div>

                            <!-- 主题信息 -->
                            <div class="theme-info">
                                <div class="theme-name">
                                    <span>{{ theme.name }}</span>
                                    <i
                                        v-if="currentTheme === theme.id"
                                        class="el-icon-check"
                                    />
                                </div>
                                <p class="theme-desc">
                                    {{ theme.description }}
                                </p>
                                <div class="theme-colors">
                                    <span
                                        v-for="(color, key) in getMainColors(theme)"
                                        :key="key"
                                        class="color-dot"
                                        :style="{ background: color }"
                                        :title="color"
                                    />
                                </div>
                            </div>
                        </div>
                    </div>
                </div>

                <!-- 亮色主题 -->
                <div class="theme-category">
                    <h3 class="category-title">
                        <i class="el-icon-sunny" /> 亮色主题
                    </h3>
                    <div class="theme-grid">
                        <div
                            v-for="theme in lightThemes"
                            :key="theme.id"
                            class="theme-card"
                            :class="{ active: currentTheme === theme.id }"
                            @click="selectTheme(theme.id)"
                        >
                            <!-- 主题预览 -->
                            <div
                                class="theme-preview"
                                :style="getPreviewStyle(theme)"
                            >
                                <div class="preview-header light">
                                    <div class="preview-dots">
                                        <span class="dot dot-red" />
                                        <span class="dot dot-yellow" />
                                        <span class="dot dot-green" />
                                    </div>
                                    <div class="preview-title">
                                        DNF管理
                                    </div>
                                </div>
                                <div class="preview-sidebar">
                                    <div
                                        class="preview-nav"
                                        :style="{ background: theme.colors.primary }"
                                    />
                                    <div
                                        class="preview-nav"
                                        :style="{ background: theme.colors.secondary }"
                                    />
                                    <div
                                        class="preview-nav"
                                        :style="{ background: theme.colors.secondary }"
                                    />
                                </div>
                                <div class="preview-content">
                                    <div
                                        class="preview-card"
                                        :style="{
                                            background: theme.colors.bgCard,
                                            borderColor: theme.colors.primary
                                        }"
                                    />
                                    <div
                                        class="preview-card"
                                        :style="{
                                            background: theme.colors.bgCard,
                                            borderColor: 'transparent'
                                        }"
                                    />
                                </div>
                            </div>

                            <!-- 主题信息 -->
                            <div class="theme-info">
                                <div class="theme-name">
                                    <span>{{ theme.name }}</span>
                                    <i
                                        v-if="currentTheme === theme.id"
                                        class="el-icon-check"
                                    />
                                </div>
                                <p class="theme-desc">
                                    {{ theme.description }}
                                </p>
                                <div class="theme-colors">
                                    <span
                                        v-for="(color, key) in getMainColors(theme)"
                                        :key="key"
                                        class="color-dot"
                                        :style="{ background: color }"
                                        :title="color"
                                    />
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
            </section>

            <!-- 其他设置 -->
            <section class="setting-section">
                <div class="section-header">
                    <h2><i class="el-icon-s-tools" /> 界面偏好</h2>
                    <p>自定义您的界面显示选项</p>
                </div>

                <div class="setting-list">
                    <div class="setting-item">
                        <div class="setting-item-left">
                            <i class="el-icon-sunny" />
                            <div class="setting-item-info">
                                <span class="setting-item-title">侧边栏默认展开</span>
                                <span class="setting-item-desc">页面加载时侧边栏是否默认展开</span>
                            </div>
                        </div>
                        <el-switch
                            v-model="sidebarExpanded"
                            @change="savePreference('sidebarExpanded', sidebarExpanded)"
                        />
                    </div>

                    <div class="setting-item">
                        <div class="setting-item-left">
                            <i class="el-icon-magic-stick" />
                            <div class="setting-item-info">
                                <span class="setting-item-title">页面动画效果</span>
                                <span class="setting-item-desc">启用或禁用页面切换动画</span>
                            </div>
                        </div>
                        <el-switch
                            v-model="pageAnimations"
                            @change="savePreference('pageAnimations', pageAnimations)"
                        />
                    </div>

                    <div class="setting-item">
                        <div class="setting-item-left">
                            <i class="el-icon-bell" />
                            <div class="setting-item-info">
                                <span class="setting-item-title">消息通知</span>
                                <span class="setting-item-desc">接收系统消息和提醒</span>
                            </div>
                        </div>
                        <el-switch v-model="notifications" />
                    </div>
                </div>
            </section>

            <!-- 关于 -->
            <section class="setting-section">
                <div class="section-header">
                    <h2><i class="el-icon-info" /> 关于系统</h2>
                    <p>系统版本和相关信息</p>
                </div>

                <div class="about-card">
                    <div class="about-logo">
                        <span class="logo-d">D</span>
                        <span class="logo-n">N</span>
                        <span class="logo-f">F</span>
                    </div>
                    <div class="about-info">
                        <h3>DNF管理平台</h3>
                        <p>版本: 1.0.0</p>
                        <p>构建时间: 2026-04-14</p>
                    </div>
                </div>
            </section>
        </div>
    </div>
</template>

<script>
export default {
    name: 'Setting',
    data() {
        return {
            sidebarExpanded: localStorage.getItem('sidebarExpanded') !== 'false',
            pageAnimations: localStorage.getItem('pageAnimations') !== 'false',
            notifications: localStorage.getItem('notifications') !== 'false'
        }
    },
    computed: {
        darkThemes() {
            return this.$store.getters['theme/darkThemes']
        },
        lightThemes() {
            return this.$store.getters['theme/lightThemes']
        },
        currentTheme() {
            return this.$store.getters['theme/currentTheme']
        }
    },
    methods: {
        selectTheme(themeId) {
            this.$store.dispatch('theme/setTheme', themeId)
            this.$message({
                message: '主题切换成功',
                type: 'success',
                duration: 1500
            })
        },
        getPreviewStyle(theme) {
            return {
                background: theme.colors.bgDark,
                borderColor: theme.colors.primary
            }
        },
        getMainColors(theme) {
            return {
                primary: theme.colors.primary,
                secondary: theme.colors.secondary,
                accent: theme.colors.accent
            }
        },
        savePreference(key, value) {
            localStorage.setItem(key, value)

            // 触发设置变化事件
            window.dispatchEvent(new CustomEvent('setting-change', {
                detail: { key, value }
            }))

            // 页面动画特殊处理
            if (key === 'pageAnimations') {
                document.body.classList.toggle('no-animations', !value)
            }
        }
    }
}
</script>

<style lang="less" scoped>
@import '@/assets/styles/variables.less';

.setting-page {
    padding: 24px;
    min-height: 100%;
    animation: fadeIn 0.3s ease;
}

@keyframes fadeIn {
    from {
        opacity: 0;
        transform: translateY(10px);
    }
    to {
        opacity: 1;
        transform: translateY(0);
    }
}

.setting-container {
    max-width: 1200px;
    margin: 0 auto;
}

// 页面标题
.page-header {
    margin-bottom: 32px;
    padding: 24px 32px;
    background: var(--theme-bg-card);
    border: 1px solid var(--theme-border);
    border-radius: 12px;

    h1 {
        font-size: 28px;
        font-weight: 700;
        color: var(--theme-accent);
        margin: 0 0 8px;
        display: flex;
        align-items: center;
        gap: 12px;

        i {
            font-size: 28px;
        }
    }
}

// 设置区块
.setting-section {
    background: var(--theme-bg-card, @dnf-bg-card);
    border: 1px solid var(--theme-border, @dnf-border-light);
    border-radius: 12px;
    padding: 24px;
    margin-bottom: 24px;
    transition: all 0.3s ease;

    &:hover {
        border-color: var(--theme-primary, @dnf-secondary-purple);
        box-shadow: 0 0 30px rgba(123, 47, 255, 0.15);
    }
}

.section-header {
    margin-bottom: 24px;
    padding-bottom: 16px;
    border-bottom: 1px solid var(--theme-border, @dnf-border-dark);

    h2 {
        font-size: 18px;
        font-weight: 600;
        color: var(--theme-text-primary, @dnf-text-primary);
        margin: 0 0 8px;
        display: flex;
        align-items: center;
        gap: 10px;

        i {
            color: var(--theme-primary, @dnf-secondary-purple);
        }
    }

    p {
        color: var(--theme-text-secondary, @dnf-text-secondary);
        margin: 0;
        font-size: 13px;
    }
}

// 主题分类
.theme-category {
    margin-bottom: 32px;

    &:last-child {
        margin-bottom: 0;
    }
}

.category-title {
    font-size: 14px;
    font-weight: 600;
    color: var(--theme-text-secondary, @dnf-text-secondary);
    margin: 0 0 16px;
    display: flex;
    align-items: center;
    gap: 8px;

    i {
        color: var(--theme-accent, #ffd700);
    }
}

// 主题卡片网格
.theme-grid {
    display: grid;
    grid-template-columns: repeat(auto-fill, minmax(240px, 1fr));
    gap: 16px;
}

.theme-card {
    background: var(--theme-bg-dark, @dnf-bg-dark);
    border: 2px solid var(--theme-border, @dnf-border-dark);
    border-radius: 12px;
    overflow: hidden;
    cursor: pointer;
    transition: all 0.3s ease;

    &:hover {
        transform: translateY(-4px);
        box-shadow: 0 8px 30px rgba(0, 0, 0, 0.4);
    }

    &.active {
        border-color: var(--theme-accent, #ffd700);
        box-shadow: 0 0 20px rgba(255, 215, 0, 0.3);
    }
}

.theme-preview {
    height: 120px;
    border-bottom: 1px solid rgba(255, 255, 255, 0.1);
    position: relative;
    display: flex;
    flex-direction: column;
    padding: 8px;
}

.preview-header {
    display: flex;
    align-items: center;
    gap: 8px;
    margin-bottom: 8px;

    &.light {
        .preview-title {
            color: rgba(0, 0, 0, 0.7);
        }

        .dot {
            &.dot-red { background: #ff5f57; }
            &.dot-yellow { background: #ffbd2e; }
            &.dot-green { background: #28c840; }
        }
    }

    .preview-dots {
        display: flex;
        gap: 4px;

        .dot {
            width: 8px;
            height: 8px;
            border-radius: 50%;

            &.dot-red { background: #ff5f57; }
            &.dot-yellow { background: #ffbd2e; }
            &.dot-green { background: #28c840; }
        }
    }

    .preview-title {
        font-size: 9px;
        color: rgba(255, 255, 255, 0.7);
        font-weight: 600;
    }
}

.preview-sidebar {
    display: flex;
    flex-direction: column;
    gap: 4px;
    position: absolute;
    left: 8px;
    top: 36px;

    .preview-nav {
        width: 20px;
        height: 5px;
        border-radius: 3px;
        opacity: 0.6;
    }
}

.preview-content {
    flex: 1;
    display: flex;
    flex-direction: column;
    gap: 5px;
    margin-top: auto;
    padding-left: 36px;

    .preview-card {
        height: 16px;
        border-radius: 3px;
        border: 1px solid;
        opacity: 0.8;
    }
}

.theme-info {
    padding: 12px;
}

.theme-name {
    display: flex;
    align-items: center;
    justify-content: space-between;
    font-size: 14px;
    font-weight: 600;
    color: var(--theme-text-primary, @dnf-text-primary);
    margin-bottom: 4px;

    i {
        color: var(--theme-accent, #ffd700);
        font-size: 14px;
    }
}

.theme-desc {
    font-size: 11px;
    color: var(--theme-text-secondary, @dnf-text-secondary);
    margin: 0 0 8px;
}

.theme-colors {
    display: flex;
    gap: 6px;

    .color-dot {
        width: 14px;
        height: 14px;
        border-radius: 50%;
        border: 2px solid rgba(255, 255, 255, 0.2);
        cursor: pointer;
        transition: transform 0.2s ease;

        &:hover {
            transform: scale(1.2);
        }
    }
}

// 设置列表
.setting-list {
    display: flex;
    flex-direction: column;
    gap: 16px;
}

.setting-item {
    display: flex;
    align-items: center;
    justify-content: space-between;
    padding: 16px;
    background: var(--theme-bg-dark, @dnf-bg-dark);
    border: 1px solid var(--theme-border, @dnf-border-dark);
    border-radius: 8px;
    transition: all 0.2s ease;

    &:hover {
        border-color: var(--theme-primary, @dnf-secondary-purple);
        background: rgba(123, 47, 255, 0.1);
    }
}

.setting-item-left {
    display: flex;
    align-items: center;
    gap: 16px;

    > i {
        font-size: 24px;
        color: var(--theme-primary, @dnf-secondary-purple);
    }
}

.setting-item-info {
    display: flex;
    flex-direction: column;
    gap: 4px;

    .setting-item-title {
        font-size: 14px;
        font-weight: 500;
        color: var(--theme-text-primary, @dnf-text-primary);
    }

    .setting-item-desc {
        font-size: 12px;
        color: var(--theme-text-secondary, @dnf-text-secondary);
    }
}

// 关于卡片
.about-card {
    display: flex;
    align-items: center;
    gap: 24px;
    padding: 24px;
    background: var(--theme-bg-dark, @dnf-bg-dark);
    border: 1px solid var(--theme-border, @dnf-border-dark);
    border-radius: 8px;
}

.about-logo {
    display: flex;
    gap: 2px;
    font-size: 36px;
    font-weight: 900;
    letter-spacing: 2px;

    .logo-d {
        color: #7b2fff;
        text-shadow: 0 0 10px rgba(123, 47, 255, 0.8);
    }

    .logo-n {
        color: #00b4ff;
        text-shadow: 0 0 10px rgba(0, 180, 255, 0.8);
    }

    .logo-f {
        color: #ffd700;
        text-shadow: 0 0 10px rgba(255, 215, 0, 0.8);
    }
}

.about-info {
    h3 {
        font-size: 18px;
        font-weight: 600;
        color: var(--theme-text-primary, @dnf-text-primary);
        margin: 0 0 8px;
    }

    p {
        font-size: 13px;
        color: var(--theme-text-secondary, @dnf-text-secondary);
        margin: 4px 0;
    }
}

// 响应式
@media (max-width: @breakpoint-md) {
    .setting-page {
        padding: 16px;
    }

    .theme-grid {
        grid-template-columns: 1fr;
    }
}
</style>
