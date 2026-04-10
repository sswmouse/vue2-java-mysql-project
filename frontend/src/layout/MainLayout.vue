<!--
 * @Description: 主布局组件，包含左侧导航栏、右侧内容区、右上角用户头像
 * @Author: Claude Code
 * @Date: 2026-04-10
 * @LastEditTime: 2026-04-10 17:20:12
 * @FilePath: /vue2-java-mysql-project/frontend/src/layout/MainLayout.vue
 -->
<template>
    <el-container class="main-layout">
        <!-- 左侧导航栏 -->
        <el-aside
            width="200px"
            class="sidebar"
        >
            <div class="logo">
                <h2>DNF助手</h2>
            </div>
            <el-menu
                :default-active="activeMenu"
                :default-openeds="openedMenus"
                router
                background-color="#304156"
                text-color="#bfcbd9"
                active-text-color="#409EFF"
                class="sidebar-menu"
            >
                <el-menu-item index="/">
                    <i class="el-icon-s-home" />
                    <span slot="title">首页</span>
                </el-menu-item>
                <el-menu-item index="/users">
                    <i class="el-icon-user-solid" />
                    <span slot="title">用户管理</span>
                </el-menu-item>
                <el-menu-item index="/role">
                    <i class="el-icon-user" />
                    <span slot="title">角色</span>
                </el-menu-item>
                <el-menu-item index="/equipment">
                    <i class="el-icon-suitcase" />
                    <span slot="title">装备</span>
                </el-menu-item>
                <el-menu-item index="/fashion">
                    <i class="el-icon-star-off" />
                    <span slot="title">时装</span>
                </el-menu-item>
                <el-menu-item index="/enhancement">
                    <i class="el-icon-top" />
                    <span slot="title">增幅</span>
                </el-menu-item>
                <el-menu-item index="/enchantment">
                    <i class="el-icon-magic-stick" />
                    <span slot="title">附魔</span>
                </el-menu-item>
                <el-menu-item index="/badge">
                    <i class="el-icon-medal" />
                    <span slot="title">徽章</span>
                </el-menu-item>
                <el-menu-item index="/title">
                    <i class="el-icon-crown" />
                    <span slot="title">称号</span>
                </el-menu-item>
                <el-menu-item index="/pet">
                    <i class="el-icon-dog" />
                    <span slot="title">宠物</span>
                </el-menu-item>
                <el-menu-item index="/mist">
                    <i class="el-icon-cloudy" />
                    <span slot="title">迷雾</span>
                </el-menu-item>
            </el-menu>
        </el-aside>

        <!-- 右侧主内容区 -->
        <el-container class="main-container">
            <!-- 顶部导航栏 -->
            <el-header class="header">
                <div class="header-right">
                    <el-dropdown
                        trigger="click"
                        @command="handleUserCommand"
                        @visible-change="handleDropdownShow"
                    >
                        <div
                            class="user-avatar"
                            @mouseenter="handleAvatarHover"
                            @mouseleave="handleAvatarLeave"
                        >
                            <el-avatar
                                size="medium"
                                :src="userAvatar"
                                class="user-avatar-img"
                            >
                                {{ userInitials }}
                            </el-avatar>
                            <span class="user-name">{{ userName }}</span>
                            <i class="el-icon-arrow-down el-icon--right" />
                        </div>
                        <el-dropdown-menu
                            slot="dropdown"
                            class="user-dropdown"
                        >
                            <el-dropdown-item command="profile">
                                <i class="el-icon-user" />
                                个人中心
                            </el-dropdown-item>
                            <el-dropdown-item
                                command="settings"
                                divided
                            >
                                <i class="el-icon-setting" />
                                设置
                            </el-dropdown-item>
                            <el-dropdown-item command="logout">
                                <i class="el-icon-switch-button" />
                                退出登录
                            </el-dropdown-item>
                        </el-dropdown-menu>
                    </el-dropdown>
                </div>
            </el-header>

            <!-- 主要内容区域 -->
            <el-main class="content">
                <router-view />
            </el-main>
        </el-container>
    </el-container>
</template>

<script>
import {
    logoGlow,
    logoFloat,
    menuItemHover,
    menuItemLeave,
    buttonHover,
    buttonLeave,
    dropdownExpand,
    dropdownCollapse
} from '@/utils/animations'

export default {
    name: 'MainLayout',
    data() {
        return {
            activeMenu: '/',
            openedMenus: [],
            userName: '管理员',
            userAvatar: '',
            userInitials: '管',
            logoAnimation: null,
            dropdownVisible: false
        }
    },
    mounted() {
        this.initAnimations()
        this.setupMenuAnimations()
    },
    beforeDestroy() {
        this.cleanupAnimations()
    },
    watch: {
        $route: {
            immediate: true,
            handler(route) {
                this.activeMenu = route.path
            }
        }
    },
    methods: {
        initAnimations() {
            // 初始化Logo动画
            const logoElement = this.$el.querySelector('.logo h2')
            if (logoElement) {
                this.logoAnimation = logoGlow(logoElement)
                logoFloat(logoElement)
            }
        },
        cleanupAnimations() {
            // 清理动画
            if (this.logoAnimation) {
                this.logoAnimation.kill()
            }
        },
        setupMenuAnimations() {
            // 设置菜单项动画
            const menuItems = this.$el.querySelectorAll('.el-menu-item')
            menuItems.forEach(item => {
                item.addEventListener('mouseenter', (e) => {
                    menuItemHover(e.currentTarget)
                })
                item.addEventListener('mouseleave', (e) => {
                    menuItemLeave(e.currentTarget)
                })
            })
        },
        handleUserCommand(command) {
            switch (command) {
                case 'profile':
                    this.$message.info('打开个人中心')
                    break
                case 'settings':
                    this.$message.info('打开设置')
                    break
                case 'logout':
                    this.$confirm('确定要退出登录吗?', '提示', {
                        confirmButtonText: '确定',
                        cancelButtonText: '取消',
                        type: 'warning'
                    }).then(() => {
                        this.$message.success('已退出登录')
                        // 实际项目中这里应该跳转到登录页
                    }).catch(() => { })
                    break
                default:
                    break
            }
        },
        handleDropdownShow() {
            this.dropdownVisible = true
            const dropdown = this.$el.querySelector('.el-dropdown-menu')
            if (dropdown) {
                dropdownExpand(dropdown)
            }
        },
        handleDropdownHide() {
            this.dropdownVisible = false
            const dropdown = this.$el.querySelector('.el-dropdown-menu')
            if (dropdown) {
                dropdownCollapse(dropdown)
            }
        },
        handleAvatarHover(event) {
            buttonHover(event.currentTarget)
        },
        handleAvatarLeave(event) {
            buttonLeave(event.currentTarget)
        }
    }
}
</script>

<style lang="less" scoped>
// 导入DNF主题变量和混合函数
@import '@/assets/styles/variables.less';
@import '@/assets/styles/mixins.less';

.main-layout {
    height: 100vh;
    overflow: hidden;
    background: @dnf-bg-dark;

    .sidebar {
        background: @gradient-dark-blue;
        transition: all @transition-normal;
        position: relative;
        z-index: 100;
        box-shadow: @dnf-shadow-lg;

        .logo {
            height: 70px;
            display: flex;
            align-items: center;
            justify-content: center;
            padding: @spacing-md;
            border-bottom: 1px solid @dnf-border-dark;
            background: linear-gradient(180deg, #1a2d4a, #0f172a);
            position: relative;
            overflow: hidden;

            // 简化的背景光效
            &::before {
                content: '';
                position: absolute;
                top: 50%;
                left: 50%;
                transform: translate(-50%, -50%);
                width: 150%;
                height: 150%;
                background: radial-gradient(circle, rgba(255, 215, 0, 0.08) 0%, transparent 50%);
                pointer-events: none;
            }

            h2 {
                margin: 0;
                font-size: @font-size-2xl;
                font-weight: 900;
                letter-spacing: 3px;
                position: relative;
                padding: @spacing-sm @spacing-lg;
                border-radius: @border-radius-md;
                background: linear-gradient(180deg, rgba(15, 23, 42, 0.95), rgba(26, 45, 74, 0.95));
                border: 2px solid #ffd700;
                color: #ffd700;
                // 多层发光效果 + 黑色描边增强对比度
                text-shadow:
                    0 0 8px rgba(255, 215, 0, 0.9),
                    0 0 16px rgba(255, 215, 0, 0.7),
                    0 0 24px rgba(255, 215, 0, 0.5),
                    2px 2px 0 #000000,
                    -1px -1px 0 #000000,
                    1px -1px 0 #000000,
                    -1px 1px 0 #000000;
                cursor: default;
                user-select: none;
                z-index: 10;
                animation: logo-glow 3s ease-in-out infinite alternate;

                // 顶部高光层 - 增强金属质感
                &::before {
                    content: 'DNF助手';
                    position: absolute;
                    top: -1px;
                    left: 0;
                    padding: @spacing-sm @spacing-lg;
                    background: linear-gradient(180deg,
                        rgba(255, 255, 255, 0.8) 0%,
                        rgba(255, 223, 0, 0.9) 20%,
                        rgba(255, 215, 0, 0.6) 40%,
                        transparent 100%);
                    -webkit-background-clip: text;
                    background-clip: text;
                    -webkit-text-fill-color: transparent;
                    pointer-events: none;
                    z-index: 1;
                }

                // 外发光层
                &::after {
                    content: '';
                    position: absolute;
                    top: -2px;
                    left: -2px;
                    right: -2px;
                    bottom: -2px;
                    background: linear-gradient(135deg, #ffd700, #ffec8b, #ffd700);
                    border-radius: @border-radius-md;
                    z-index: -1;
                    opacity: 0.5;
                    filter: blur(6px);
                    animation: logo-border-glow 3s ease-in-out infinite alternate;
                }
            }
        }

        .sidebar-menu {
            border-right: none;
            height: calc(100vh - 70px);
            overflow-y: auto;
            background: transparent;
            .custom-scrollbar(6px, transparent, @dnf-primary-purple);

            &:not(.el-menu--collapse) {
                width: 200px;
            }

            .el-menu-item {
                position: relative;
                margin: @spacing-xs @spacing-md;
                border-radius: @border-radius-md;
                transition: all @transition-normal;
                border-left: 3px solid transparent;

                i {
                    color: @dnf-primary-gold;
                    font-size: @font-size-lg;
                }

                span {
                    font-weight: @font-weight-medium;
                }

                &:hover {
                    background: fade(@dnf-primary-purple, 20%) !important;
                    transform: translateX(5px);
                    border-left: 3px solid @dnf-primary-gold;

                    i {
                        transform: scale(1.2);
                    }
                }

                &.is-active {
                    background: fade(@dnf-primary-purple, 30%) !important;
                    color: @dnf-primary-gold !important;
                    border-left: 3px solid @dnf-primary-gold;
                    box-shadow: @dnf-shadow-purple;

                    &::after {
                        content: '';
                        position: absolute;
                        right: 10px;
                        top: 50%;
                        transform: translateY(-50%);
                        width: 8px;
                        height: 8px;
                        background: @dnf-primary-gold;
                        border-radius: 50%;
                        box-shadow: 0 0 10px @dnf-primary-gold;
                    }
                }
            }
        }
    }

    .main-container {
        display: flex;
        flex-direction: column;

        .header {
            display: flex;
            align-items: center;
            justify-content: flex-end;
            padding: 0 @spacing-xl;
            background: @dnf-primary-blue;
            border-bottom: 1px solid @dnf-border-dark;
            height: 70px;
            box-shadow: @dnf-shadow-md;
            position: relative;
            z-index: 90;

            &::before {
                content: '';
                position: absolute;
                top: 0;
                left: 0;
                right: 0;
                bottom: 0;
                background: linear-gradient(90deg, transparent, fade(@dnf-primary-purple, 20%), transparent);
                opacity: 0.5;
            }

            .header-right {
                position: relative;
                z-index: 1;

                .user-avatar {
                    display: flex;
                    align-items: center;
                    cursor: pointer;
                    padding: @spacing-sm @spacing-md;
                    border-radius: @border-radius-lg;
                    transition: all @transition-normal;
                    background: fade(@dnf-bg-card, 30%);
                    border: 1px solid fade(@dnf-primary-gold, 20%);
                    position: relative;
                    overflow: hidden;

                    &::before {
                        content: '';
                        position: absolute;
                        top: 0;
                        left: 0;
                        right: 0;
                        bottom: 0;
                        background: @gradient-purple-gold;
                        opacity: 0;
                        transition: opacity @transition-normal;
                        z-index: -1;
                    }

                    &:hover {
                        border-color: @dnf-primary-gold;
                        box-shadow: @dnf-shadow-gold;

                        &::before {
                            opacity: 0.1;
                        }

                        .user-avatar-img {
                            transform: scale(1.1);
                            box-shadow: 0 0 15px @dnf-primary-gold;
                        }

                        .user-name {
                            color: @dnf-primary-gold;
                        }

                        .el-icon-arrow-down {
                            transform: rotate(180deg);
                            color: @dnf-primary-gold;
                        }
                    }

                    .user-avatar-img {
                        transition: all @transition-normal;
                        border: 2px solid fade(@dnf-primary-gold, 50%);
                        background: @gradient-gold-text;
                        font-weight: @font-weight-bold;
                    }

                    .user-name {
                        margin-left: @spacing-md;
                        margin-right: @spacing-sm;
                        font-size: @font-size-md;
                        font-weight: @font-weight-medium;
                        color: @dnf-text-primary;
                        transition: color @transition-normal;
                    }

                    .el-icon-arrow-down {
                        transition: all @transition-normal;
                        color: @dnf-text-secondary;
                    }
                }

                .user-dropdown {
                    background: @dnf-bg-card;
                    border: 1px solid @dnf-border-dark;
                    border-radius: @border-radius-lg;
                    box-shadow: @dnf-shadow-xl;
                    padding: @spacing-sm 0;
                    margin-top: @spacing-sm;

                    &::before {
                        content: '';
                        position: absolute;
                        top: -5px;
                        right: 20px;
                        width: 10px;
                        height: 10px;
                        background: @dnf-bg-card;
                        border-top: 1px solid @dnf-border-dark;
                        border-left: 1px solid @dnf-border-dark;
                        transform: rotate(45deg);
                    }

                    .el-dropdown-menu__item {
                        color: @dnf-text-secondary;
                        padding: @spacing-sm @spacing-xl;
                        font-weight: @font-weight-medium;
                        transition: all @transition-fast;

                        i {
                            margin-right: @spacing-sm;
                            color: @dnf-primary-purple;
                        }

                        &:hover {
                            background: @dnf-bg-hover;
                            color: @dnf-text-primary;
                            transform: translateX(5px);

                            i {
                                color: @dnf-primary-gold;
                            }
                        }

                        &.is-divided {
                            position: relative;

                            &::before {
                                content: '';
                                position: absolute;
                                top: 0;
                                left: @spacing-xl;
                                right: @spacing-xl;
                                height: 1px;
                                background: @dnf-border-dark;
                            }
                        }
                    }
                }
            }
        }

        .content {
            padding: @spacing-xl;
            background: @dnf-bg-dark;
            overflow-y: auto;
            flex: 1;
            position: relative;

            &::before {
                content: '';
                position: absolute;
                top: 0;
                left: 0;
                right: 0;
                height: 1px;
                background: @gradient-purple-gold;
                opacity: 0.3;
            }

            .custom-scrollbar();
        }
    }
}

// 响应式调整
@media (max-width: @breakpoint-md) {
    .main-layout {
        .sidebar {
            width: 60px !important;

            .logo {
                h2 {
                    font-size: 12px;
                    padding: 6px 6px;
                    letter-spacing: 1px;
                    text-shadow:
                        0 0 6px rgba(255, 215, 0, 0.9),
                        0 0 12px rgba(255, 215, 0, 0.7),
                        2px 2px 0 #000000,
                        -1px -1px 0 #000000,
                        1px -1px 0 #000000,
                        -1px 1px 0 #000000;
                }
            }

            .sidebar-menu {
                .el-menu-item {
                    span {
                        display: none;
                    }

                    i {
                        margin-right: 0;
                    }
                }
            }
        }

        .main-container {
            .header {
                padding: 0 @spacing-md;

                .user-avatar {
                    .user-name {
                        display: none;
                    }
                }
            }

            .content {
                padding: @spacing-md;
            }
        }
    }
}

// 动画类
.logo-animation {
    animation: logo-glow 3s ease-in-out infinite;
}

// Logo发光动画
@keyframes logo-glow {
    0% {
        text-shadow:
            0 0 5px fade(@dnf-primary-gold, 80%),
            0 0 10px fade(@dnf-primary-gold, 60%),
            0 0 20px fade(@dnf-primary-gold, 40%),
            0 0 30px fade(@dnf-primary-gold, 20%),
            0 2px 4px rgba(0, 0, 0, 0.8);
    }
    100% {
        text-shadow:
            0 0 10px fade(@dnf-secondary-gold, 100%),
            0 0 20px fade(@dnf-secondary-gold, 80%),
            0 0 30px fade(@dnf-primary-gold, 60%),
            0 0 40px fade(@dnf-primary-gold, 40%),
            0 2px 4px rgba(0, 0, 0, 0.8);
    }
}

// Logo边框发光动画
@keyframes logo-border-glow {
    0% {
        opacity: 0.4;
        filter: blur(6px);
    }
    100% {
        opacity: 0.7;
        filter: blur(10px);
    }
}

// Logo背景脉动动画
@keyframes logo-bg-pulse {
    0%, 100% {
        opacity: 0.1;
        transform: scale(1);
    }
    50% {
        opacity: 0.2;
        transform: scale(1.1);
    }
}

.menu-item-animation {
    transition: all 0.3s cubic-bezier(0.68, -0.55, 0.265, 1.55);
}

.avatar-animation {
    transition: all 0.3s cubic-bezier(0.68, -0.55, 0.265, 1.55);
}
</style>