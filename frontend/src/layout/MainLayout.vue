<!--
 * @Description: 主布局组件 - DNF霓虹风格
 * @Author: Claude Code
 * @Date: 2026-04-14
-->
<template>
    <div class="main-layout">
        <!-- 侧边栏 -->
        <aside
            class="sidebar"
            :class="{ collapsed: isCollapsed }"
        >
            <!-- 展开/收起按钮 -->
            <div
                class="sidebar-toggle"
                @click="toggleSidebar"
            >
                <i :class="isCollapsed ? 'el-icon-d-arrow-right' : 'el-icon-d-arrow-left'" />
            </div>

            <!-- Logo区域 -->
            <div class="sidebar-header">
                <div class="logo">
                    <span class="logo-d">D</span>
                    <span class="logo-n">N</span>
                    <span class="logo-f">F</span>
                </div>
                <span class="logo-text">DNF管理</span>
            </div>

            <!-- 导航菜单 -->
            <nav class="sidebar-nav">
                <router-link
                    v-for="item in visibleMenuItems"
                    :key="item.path"
                    :to="item.path"
                    class="nav-item"
                    :class="{ active: $route.path === item.path }"
                    :title="item.title"
                >
                    <i :class="item.icon" />
                    <span class="nav-text">{{ item.title }}</span>
                </router-link>
            </nav>

            <!-- 用户信息 -->
            <div class="sidebar-footer">
                <div class="user-info">
                    <div class="user-avatar">
                        <img
                            v-if="userAvatar"
                            :src="userAvatar"
                            class="avatar-img"
                        >
                        <span v-else>{{ userInitial }}</span>
                    </div>
                    <div class="user-details">
                        <span class="user-name">{{ username }}</span>
                        <span class="user-role">{{ userRoleText }}</span>
                    </div>
                </div>
            </div>
        </aside>

        <!-- 主内容区 -->
        <div class="main-content">
            <!-- 顶部栏 -->
            <header class="topbar">
                <div class="topbar-left">
                    <div class="page-title">
                        <h2>{{ currentPageTitle }}</h2>
                    </div>
                </div>

                <div class="topbar-right">
                    <!-- 时间显示 -->
                    <div class="time-display">
                        <i class="el-icon-time" />
                        <span>{{ currentTime }}</span>
                    </div>

                    <!-- 用户菜单 -->
                    <el-dropdown
                        trigger="click"
                        class="user-dropdown"
                    >
                        <div class="user-menu">
                            <div class="user-avatar-small">
                                <img
                                    v-if="userAvatar"
                                    :src="userAvatar"
                                    class="avatar-img"
                                >
                                <span v-else>{{ userInitial }}</span>
                            </div>
                            <span class="user-name">{{ username }}</span>
                            <i class="el-icon-arrow-down" />
                        </div>
                        <el-dropdown-menu slot="dropdown">
                            <!-- 只有admin角色才显示用户管理 -->
                            <el-dropdown-item
                                v-if="isAdmin"
                                @click.native="goToUsers"
                            >
                                <i class="el-icon-user" />
                                用户管理
                            </el-dropdown-item>
                            <el-dropdown-item
                                @click.native="goToProfile"
                            >
                                <i class="el-icon-user-solid" />
                                个人中心
                            </el-dropdown-item>
                            <el-dropdown-item
                                @click.native="goToSetting"
                            >
                                <i class="el-icon-setting" />
                                设置
                            </el-dropdown-item>
                            <el-dropdown-item
                                divided
                                @click.native="handleLogout"
                            >
                                <i class="el-icon-switch-button" />
                                退出登录
                            </el-dropdown-item>
                        </el-dropdown-menu>
                    </el-dropdown>
                </div>
            </header>

            <!-- 页面内容 -->
            <main class="page-content">
                <router-view />
            </main>
        </div>
    </div>
</template>

<script>
export default {
    name: 'MainLayout',
    data() {
        return {
            isCollapsed: false,
            currentTime: '',
            // 默认菜单配置（权限树加载前使用）
            defaultMenuItems: [
                { path: '/', title: '首页', icon: 'el-icon-s-home', roles: ['admin', 'user'] },
                { path: '/setting', title: '系统设置', icon: 'el-icon-setting', roles: ['admin', 'user'] },
                { path: '/role', title: '角色管理', icon: 'el-icon-s-custom', roles: ['admin', 'user'] },
                { path: '/equipment', title: '装备管理', icon: 'el-icon-suitcase', roles: ['admin', 'user'] },
                { path: '/fashion', title: '时装管理', icon: 'el-icon-shopping-bag-2', roles: ['admin', 'user'] },
                { path: '/enhancement', title: '强化系统', icon: 'el-icon-data-line', roles: ['admin', 'user'] },
                { path: '/enchantment', title: '附魔系统', icon: 'el-icon-magic-stick', roles: ['admin', 'user'] },
                { path: '/badge', title: '徽章系统', icon: 'el-icon-medal', roles: ['admin', 'user'] },
                { path: '/title', title: '称号系统', icon: 'el-icon-postcard', roles: ['admin', 'user'] },
                { path: '/pet', title: '宠物系统', icon: 'el-icon-cpu', roles: ['admin', 'user'] },
                { path: '/mist', title: '迷雾大陆', icon: 'el-icon-cloudy', roles: ['admin', 'user'] }
            ]
        }
    },
    computed: {
        // 从Vuex获取用户信息
        username() {
            return (this.$store.state.auth.user && this.$store.state.auth.user.username) || 'Admin'
        },
        userRole() {
            return (this.$store.state.auth.user && this.$store.state.auth.user.role) || 'admin'
        },
        isAdmin() {
            return this.userRole === 'admin'
        },
        userRoleText() {
            return this.isAdmin ? '管理员' : '普通用户'
        },
        userInitial() {
            const name = this.username
            return name ? name.charAt(0).toUpperCase() : 'A'
        },
        userAvatar() {
            return this.$store.state.auth.user && this.$store.state.auth.user.avatar
        },
        // 从Vuex获取权限树菜单
        menuTree() {
            return this.$store.getters['auth/menuTree']
        },
        // 当前页面标题
        currentPageTitle() {
            const current = this.visibleMenuItems.find(item => item.path === this.$route.path)
            return current ? current.title : 'DNF管理系统'
        },
        // 根据权限树或角色过滤可见菜单
        visibleMenuItems() {
            let items = []

            // 优先使用权限树
            if (this.menuTree && this.menuTree.length > 0) {
                items = this.menuTree
            } else {
                // 降级方案：根据角色过滤
                const currentRole = this.userRole
                items = this.defaultMenuItems.filter(item => item.roles.includes(currentRole))
            }

            // 始终排除个人中心（通过下拉菜单访问）
            return items.filter(item => item.path !== '/profile')
        }
    },
    mounted() {
        this.updateTime()
        setInterval(this.updateTime, 1000)

        // 初始化时如果需要，加载权限树
        if (this.$store.state.auth.token && !this.$store.state.auth.permissions) {
            this.$store.dispatch('auth/loadPermissions')
        }
    },
    methods: {
        toggleSidebar() {
            this.isCollapsed = !this.isCollapsed
        },
        updateTime() {
            const now = new Date()
            const hours = String(now.getHours()).padStart(2, '0')
            const minutes = String(now.getMinutes()).padStart(2, '0')
            const seconds = String(now.getSeconds()).padStart(2, '0')
            this.currentTime = `${hours}:${minutes}:${seconds}`
        },
        goToUsers() {
            this.$router.push('/users')
        },
        goToProfile() {
            this.$router.push('/profile')
        },
        goToSetting() {
            this.$router.push('/setting')
        },
        handleLogout() {
            this.$msgbox({
                title: '提示',
                message: '确定要退出登录吗？',
                showCancelButton: true,
                confirmButtonText: '确定',
                cancelButtonText: '取消',
                type: 'warning',
                customClass: 'logout-confirm-dialog'
            }).then(() => {
                // 使用Vuex auth模块清除登录状态
                this.$store.dispatch('auth/logout')
                this.$message.success('已退出登录')
                this.$router.push('/login')
            }).catch(() => { })
        }
    }
}
</script>

<style lang="less" scoped>
@import '@/assets/styles/variables.less';
@import '@/assets/styles/mixins.less';

.main-layout {
    display: flex;
    height: 100%;
    min-height: 100vh;
    background: var(--theme-bg-dark, @dnf-bg-dark);
}

// ============================================
// 侧边栏
// ============================================

.sidebar {
    width: 180px;
    background: var(--theme-bg-card, #1a2744);
    border-right: 1px solid var(--theme-border, #2a3a58);
    display: flex;
    flex-direction: column;
    position: relative;
    transition: width 0.3s ease;
    overflow: hidden;

    &.collapsed {
        width: 60px;

        .sidebar-header {
            .logo-text {
                display: none;
            }
        }

        .nav-item {
            justify-content: center;
            padding: 10px;

            .nav-text {
                display: none;
            }
        }

        .sidebar-footer {
            .user-details {
                display: none;
            }
        }
    }
}

// 展开/收起按钮
.sidebar-toggle {
    position: absolute;
    top: 50%;
    right: -14px;
    transform: translateY(-50%);
    width: 28px;
    height: 28px;
    background: var(--theme-bg-hover, #2a3a58);
    border: 1px solid var(--theme-border, #3a4a68);
    border-radius: 50%;
    display: flex;
    align-items: center;
    justify-content: center;
    cursor: pointer;
    z-index: 10;
    transition: all 0.2s ease;

    i {
        font-size: 14px;
        color: var(--theme-text-secondary, #8899aa);
        transition: all 0.2s ease;
    }

    &:hover {
        background: var(--theme-bg-hover, #3a4a68);

        i {
            color: var(--theme-accent, #ffd700);
        }
    }
}

// Logo区域
.sidebar-header {
    padding: 16px 12px;
    text-align: center;
    border-bottom: 1px solid var(--theme-border, #2a3a58);
    background: var(--theme-bg-card, linear-gradient(180deg, #1e2d4a 0%, #1a2744 100%));
    display: flex;
    flex-direction: column;
    align-items: center;
    gap: 6px;

    .logo {
        font-size: 22px;
        font-weight: 900;
        letter-spacing: 2px;
        display: flex;
        justify-content: center;
        gap: 1px;

        span {
            display: inline-block;
        }

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

    .logo-text {
        font-size: 11px;
        color: #8899aa;
        white-space: nowrap;
    }
}

// 导航菜单
.sidebar-nav {
    flex: 1;
    padding: 8px 0;
    overflow-y: auto;
    overflow-x: hidden;

    &::-webkit-scrollbar {
        width: 0;
    }
}

.nav-item {
    display: flex;
    align-items: center;
    gap: 10px;
    padding: 10px 16px;
    margin: 2px 8px;
    border-radius: 6px;
    color: var(--theme-text-secondary, #b8c4d8);
    text-decoration: none;
    transition: all 0.2s ease;
    font-size: 13px;
    white-space: nowrap;

    i {
        font-size: 16px;
        width: 20px;
        color: var(--theme-text-secondary, #8899aa);
        transition: all 0.2s ease;
        flex-shrink: 0;
    }

    .nav-text {
        transition: opacity 0.2s ease;
    }

    &:hover {
        background: rgba(123, 47, 255, 0.15);
        color: var(--theme-text-primary, #ffffff);
        transform: translateX(2px);

        i {
            color: var(--theme-primary, #7b2fff);
        }
    }

    &.active {
        background: rgba(123, 47, 255, 0.25);
        color: var(--theme-accent, #ffd700);
        border-left: 3px solid var(--theme-accent, #ffd700);
        margin-left: 5px;
        padding-left: 13px;

        i {
            color: var(--theme-accent, #ffd700);
        }
    }
}

// 用户信息
.sidebar-footer {
    padding: 12px;
    border-top: 1px solid var(--theme-border, #2a3a58);
    background: var(--theme-bg-dark, #151f38);

    .user-info {
        display: flex;
        align-items: center;
        gap: 10px;

        .user-avatar {
            width: 36px;
            height: 36px;
            background: linear-gradient(135deg, var(--theme-primary, #7b2fff), var(--theme-secondary, #4a1090));
            border-radius: 8px;
            display: flex;
            align-items: center;
            justify-content: center;
            flex-shrink: 0;
            overflow: hidden;

            .avatar-img {
                width: 100%;
                height: 100%;
                object-fit: cover;
            }

            span {
                font-size: 14px;
                font-weight: 700;
                color: #ffffff;
            }
        }

        .user-details {
            display: flex;
            flex-direction: column;
            gap: 2px;
            min-width: 0;

            .user-name {
                font-size: 12px;
                font-weight: 600;
                color: var(--theme-text-primary, #ffffff);
                white-space: nowrap;
                overflow: hidden;
                text-overflow: ellipsis;
            }

            .user-role {
                font-size: 10px;
                color: var(--theme-primary, #7b2fff);
            }
        }
    }
}

// ============================================
// 主内容区
// ============================================

.main-content {
    flex: 1;
    display: flex;
    flex-direction: column;
    overflow: hidden;
    position: relative;
    min-width: 0;
}

// 顶部栏
.topbar {
    height: 60px;
    background: var(--theme-bg-dark, #0d1525);
    border-bottom: 1px solid var(--theme-border, #1a2744);
    display: flex;
    justify-content: space-between;
    align-items: center;
    padding: 0 24px;
    position: relative;
    flex-shrink: 0;
}

.topbar-left {
    .page-title {
        h2 {
            font-size: 18px;
            font-weight: 600;
            color: var(--theme-accent, #ffd700);
            margin: 0;
            text-shadow: 0 0 10px rgba(255, 215, 0, 0.5);
        }
    }
}

.topbar-right {
    display: flex;
    align-items: center;
    gap: 16px;
}

.time-display {
    display: flex;
    align-items: center;
    gap: 6px;
    padding: 6px 12px;
    background: var(--theme-bg-card, rgba(13, 21, 37, 0.8));
    border: 1px solid var(--theme-border, #1a2744);
    border-radius: 6px;

    i {
        font-size: 14px;
        color: var(--theme-info, #00b4ff);
    }

    span {
        font-family: monospace;
        font-size: 12px;
        color: var(--theme-text-secondary, #8899aa);
    }
}

.user-dropdown {
    cursor: pointer;
}

.user-menu {
    display: flex;
    align-items: center;
    gap: 8px;
    padding: 6px 12px;
    background: var(--theme-bg-card, rgba(13, 21, 37, 0.8));
    border: 1px solid var(--theme-border, #1a2744);
    border-radius: 6px;
    transition: all 0.2s ease;

    &:hover {
        border-color: var(--theme-primary, #7b2fff);
        background: rgba(123, 47, 255, 0.1);
    }

    .user-avatar-small {
        width: 28px;
        height: 28px;
        background: linear-gradient(135deg, var(--theme-primary, #7b2fff), var(--theme-secondary, #4a1090));
        border-radius: 6px;
        display: flex;
        align-items: center;
        justify-content: center;
        overflow: hidden;

        .avatar-img {
            width: 100%;
            height: 100%;
            object-fit: cover;
        }

        span {
            font-size: 12px;
            font-weight: 700;
            color: #ffffff;
        }
    }

    .user-name {
        font-size: 12px;
        color: var(--theme-text-primary, #ffffff);
    }

    i {
        font-size: 12px;
        color: var(--theme-text-secondary, #8899aa);
    }
}

// 页面内容 - 修复滚动问题
.page-content {
    flex: 1;
    height: 0;
    min-height: 0;
    overflow-y: auto;
    overflow-x: hidden;
    position: relative;

    &::-webkit-scrollbar {
        width: 8px;
    }

    &::-webkit-scrollbar-track {
        background: var(--theme-bg-dark, #0d1525);
    }

    &::-webkit-scrollbar-thumb {
        background: var(--theme-border, #2a3a58);
        border-radius: 4px;

        &:hover {
            background: var(--theme-bg-hover, #3a4a68);
        }
    }
}

// ============================================
// 响应式设计
// ============================================

@media (max-width: @breakpoint-md) {
    .sidebar {
        width: 60px;

        .sidebar-toggle {
            display: none;
        }

        .sidebar-header {
            padding: 12px 8px;

            .logo {
                font-size: 16px;
                letter-spacing: 0;
                gap: 0;
            }

            .logo-text {
                display: none;
            }
        }

        .nav-item {
            justify-content: center;
            padding: 10px;

            .nav-text {
                display: none;
            }
        }

        .sidebar-footer {
            .user-details {
                display: none;
            }
        }
    }

    .topbar {
        padding: 0 12px;

        .user-menu .user-name {
            display: none;
        }
    }
}
</style>
