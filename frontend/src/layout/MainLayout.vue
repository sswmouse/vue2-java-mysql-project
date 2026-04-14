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
                        <span>{{ userInitial }}</span>
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
                                <span>{{ userInitial }}</span>
                            </div>
                            <span class="user-name">{{ username }}</span>
                            <i class="el-icon-arrow-down" />
                        </div>
                        <el-dropdown-menu
                            slot="dropdown"
                            class="user-dropdown-menu"
                        >
                            <!-- 只有admin角色才显示用户管理 -->
                            <el-dropdown-item
                                v-if="isAdmin"
                                @click.native="goToUsers"
                            >
                                <i class="el-icon-user" />
                                用户管理
                            </el-dropdown-item>
                            <el-dropdown-item>
                                <i class="el-icon-user-solid" />
                                个人中心
                            </el-dropdown-item>
                            <el-dropdown-item>
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
            menuItems: [
                { path: '/', title: '首页', icon: 'el-icon-s-home', roles: ['admin', 'user'] },
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
        username() {
            return (this.$store.state.user && this.$store.state.user.username) || 'Admin'
        },
        userRole() {
            return (this.$store.state.user && this.$store.state.user.role) || 'admin'
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
        currentPageTitle() {
            const current = this.menuItems.find(item => item.path === this.$route.path)
            return current ? current.title : 'DNF管理系统'
        },
        visibleMenuItems() {
            const currentRole = this.userRole
            return this.menuItems.filter(item => item.roles.includes(currentRole))
        }
    },
    mounted() {
        this.updateTime()
        setInterval(this.updateTime, 1000)
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
        handleLogout() {
            this.$confirm('确定要退出登录吗？', '提示', {
                confirmButtonText: '确定',
                cancelButtonText: '取消',
                type: 'warning'
            }).then(() => {
                this.$message.success('已退出登录')
                this.$store.commit('SET_USER', null)
                // 实际项目中这里应该跳转到登录页
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
    min-height: 100vh;
    background: @dnf-bg-dark;
}

// ============================================
// 侧边栏
// ============================================

.sidebar {
    width: 180px;
    background: #1a2744;
    border-right: 1px solid #2a3a58;
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
    background: #2a3a58;
    border: 1px solid #3a4a68;
    border-radius: 50%;
    display: flex;
    align-items: center;
    justify-content: center;
    cursor: pointer;
    z-index: 10;
    transition: all 0.2s ease;

    i {
        font-size: 14px;
        color: #8899aa;
        transition: all 0.2s ease;
    }

    &:hover {
        background: #3a4a68;

        i {
            color: #ffd700;
        }
    }
}

// Logo区域
.sidebar-header {
    padding: 16px 12px;
    text-align: center;
    border-bottom: 1px solid #2a3a58;
    background: linear-gradient(180deg, #1e2d4a 0%, #1a2744 100%);
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
    color: #b8c4d8;
    text-decoration: none;
    transition: all 0.2s ease;
    font-size: 13px;
    white-space: nowrap;

    i {
        font-size: 16px;
        width: 20px;
        color: #8899aa;
        transition: all 0.2s ease;
        flex-shrink: 0;
    }

    .nav-text {
        transition: opacity 0.2s ease;
    }

    &:hover {
        background: rgba(123, 47, 255, 0.15);
        color: #ffffff;
        transform: translateX(2px);

        i {
            color: #7b2fff;
        }
    }

    &.active {
        background: rgba(123, 47, 255, 0.25);
        color: #ffd700;
        border-left: 3px solid #ffd700;
        margin-left: 5px;
        padding-left: 13px;

        i {
            color: #ffd700;
        }
    }
}

// 用户信息
.sidebar-footer {
    padding: 12px;
    border-top: 1px solid #2a3a58;
    background: #151f38;

    .user-info {
        display: flex;
        align-items: center;
        gap: 10px;

        .user-avatar {
            width: 36px;
            height: 36px;
            background: linear-gradient(135deg, #7b2fff, #4a1090);
            border-radius: 8px;
            display: flex;
            align-items: center;
            justify-content: center;
            flex-shrink: 0;

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
                color: #ffffff;
                white-space: nowrap;
                overflow: hidden;
                text-overflow: ellipsis;
            }

            .user-role {
                font-size: 10px;
                color: #7b2fff;
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
    background: #0d1525;
    border-bottom: 1px solid #1a2744;
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
            color: #ffd700;
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
    background: rgba(13, 21, 37, 0.8);
    border: 1px solid #1a2744;
    border-radius: 6px;

    i {
        font-size: 14px;
        color: #00b4ff;
    }

    span {
        font-family: monospace;
        font-size: 12px;
        color: #8899aa;
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
    background: rgba(13, 21, 37, 0.8);
    border: 1px solid #1a2744;
    border-radius: 6px;
    transition: all 0.2s ease;

    &:hover {
        border-color: #7b2fff;
        background: rgba(123, 47, 255, 0.1);
    }

    .user-avatar-small {
        width: 28px;
        height: 28px;
        background: linear-gradient(135deg, #7b2fff, #4a1090);
        border-radius: 6px;
        display: flex;
        align-items: center;
        justify-content: center;

        span {
            font-size: 12px;
            font-weight: 700;
            color: #ffffff;
        }
    }

    .user-name {
        font-size: 12px;
        color: #ffffff;
    }

    i {
        font-size: 12px;
        color: #8899aa;
    }
}

// 下拉菜单样式
::v-deep .user-dropdown-menu {
    background: #1a2744;
    border: 1px solid #2a3a58;
    border-radius: 8px;
    padding: 4px;

    .el-dropdown-menu__item {
        padding: 8px 16px;
        border-radius: 4px;
        color: #b8c4d8;
        font-size: 12px;

        i {
            margin-right: 8px;
            color: #7b2fff;
        }

        &:hover {
            background: rgba(123, 47, 255, 0.15);
            color: #ffffff;
        }

        &.el-dropdown-menu__item--divided {
            border-top: 1px solid #2a3a58;
            margin-top: 4px;
        }
    }
}

// 页面内容 - 修复滚动问题
.page-content {
    flex: 1;
    overflow-y: auto;
    overflow-x: hidden;
    position: relative;

    &::-webkit-scrollbar {
        width: 8px;
    }

    &::-webkit-scrollbar-track {
        background: #0d1525;
    }

    &::-webkit-scrollbar-thumb {
        background: #2a3a58;
        border-radius: 4px;

        &:hover {
            background: #3a4a68;
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
