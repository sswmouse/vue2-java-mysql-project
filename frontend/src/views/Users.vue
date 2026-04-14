<!--
 * @Description: 用户管理组件 - DNF霓虹风格
 * @Author: Claude Code
 * @Date: 2026-04-14
-->
<template>
    <div class="users-page">
        <!-- 页面头部 -->
        <div class="page-header">
            <div class="header-left">
                <div class="header-icon">
                    <i class="el-icon-user-solid" />
                </div>
                <div class="header-info">
                    <h1>用户管理</h1>
                    <p>管理系统用户账户与权限</p>
                </div>
            </div>
            <div class="header-right">
                <div class="user-count">
                    <span class="count-number">{{ users.length }}</span>
                    <span class="count-label">位用户</span>
                </div>
            </div>
        </div>

        <!-- 搜索栏 -->
        <div class="search-bar">
            <div class="search-wrapper">
                <i class="el-icon-search" />
                <input
                    v-model="searchKeyword"
                    type="text"
                    placeholder="搜索用户名或手机号..."
                    class="search-input"
                    @input="handleSearch"
                >
                <i
                    v-if="searchKeyword"
                    class="el-icon-circle-close clear-btn"
                    @click="searchKeyword = ''"
                />
            </div>
        </div>

        <!-- 用户列表 -->
        <div
            v-loading="loading"
            class="users-container"
        >
            <!-- 空状态 -->
            <div
                v-if="!loading && filteredUsers.length === 0"
                class="empty-state"
            >
                <i class="el-icon-user" />
                <p v-if="searchKeyword">
                    未找到匹配的用户
                </p>
                <p v-else>
                    暂无用户数据
                </p>
            </div>

            <!-- 用户卡片列表 -->
            <div
                v-else
                class="users-grid"
            >
                <div
                    v-for="user in filteredUsers"
                    :key="user.id"
                    class="user-card"
                >
                    <div class="card-main">
                        <div class="user-avatar">
                            {{ user.username.charAt(0).toUpperCase() }}
                        </div>
                        <div class="user-info">
                            <div class="user-name">
                                {{ user.username }}
                            </div>
                            <div class="user-phone">
                                <i class="el-icon-mobile-phone" />
                                {{ user.phone }}
                            </div>
                        </div>
                        <div :class="['user-role', `role-${user.role}`]">
                            <i :class="user.role === 'admin' ? 'el-icon-s-check' : 'el-icon-user'" />
                            {{ user.role === 'admin' ? '管理员' : '普通用户' }}
                        </div>
                    </div>
                    <div class="card-actions">
                        <button
                            class="btn-action btn-edit"
                            @click="handleEditRole(user)"
                        >
                            <i class="el-icon-edit" />
                            修改角色
                        </button>
                    </div>
                </div>
            </div>
        </div>

        <!-- 角色编辑对话框 -->
        <div
            :class="['modal-overlay', { 'is-visible': roleDialogVisible }]"
            @click.self="roleDialogVisible = false"
        >
            <div class="modal-dialog">
                <div class="modal-header">
                    <h3>修改用户角色</h3>
                    <button
                        class="btn-close"
                        @click="roleDialogVisible = false"
                    >
                        <i class="el-icon-close" />
                    </button>
                </div>
                <div class="modal-body">
                    <div class="user-preview">
                        <div class="preview-avatar">
                            {{ roleEditForm.username.charAt(0).toUpperCase() }}
                        </div>
                        <div class="preview-info">
                            <span class="preview-name">{{ roleEditForm.username }}</span>
                            <span class="preview-phone">{{ roleEditForm.phone }}</span>
                        </div>
                    </div>
                    <div class="form-group">
                        <label>用户角色</label>
                        <div class="role-options">
                            <label class="role-option">
                                <input
                                    v-model="roleEditForm.role"
                                    type="radio"
                                    name="role"
                                    value="admin"
                                >
                                <span class="option-box option-admin">
                                    <i class="el-icon-s-check" />
                                    <span>管理员</span>
                                </span>
                            </label>
                            <label class="role-option">
                                <input
                                    v-model="roleEditForm.role"
                                    type="radio"
                                    name="role"
                                    value="user"
                                >
                                <span class="option-box option-user">
                                    <i class="el-icon-user" />
                                    <span>普通用户</span>
                                </span>
                            </label>
                        </div>
                    </div>
                </div>
                <div class="modal-footer">
                    <button
                        class="btn-cancel"
                        @click="roleDialogVisible = false"
                    >
                        取消
                    </button>
                    <button
                        class="btn-confirm"
                        @click="updateUserRole"
                    >
                        确认修改
                    </button>
                </div>
            </div>
        </div>
    </div>
</template>

<script>
export default {
    name: 'Users',
    data() {
        return {
            users: [],
            searchKeyword: '',
            roleEditForm: {
                id: '',
                username: '',
                phone: '',
                role: ''
            },
            roleDialogVisible: false,
            loading: false
        }
    },
    computed: {
        filteredUsers() {
            if (!this.searchKeyword) {
                return this.users
            }
            const keyword = this.searchKeyword.toLowerCase()
            return this.users.filter(user =>
                user.username.toLowerCase().includes(keyword) ||
                (user.phone && user.phone.includes(keyword))
            )
        }
    },
    mounted() {
        this.loadUsers()
    },
    methods: {
        async loadUsers() {
            this.loading = true
            try {
                const { default: api } = await import('@/api')
                const data = await this.$request({
                    url: api.user.list,
                    method: 'get'
                })
                this.users = data || []
            } catch (error) {
                console.error('加载用户列表失败:', error)
                this.$message.error('加载用户列表失败')
            } finally {
                this.loading = false
            }
        },

        handleSearch() {
            // 搜索通过 computed 属性 filteredUsers 自动处理
        },

        handleEditRole(user) {
            this.roleEditForm = { ...user }
            this.roleDialogVisible = true
        },

        async updateUserRole() {
            try {
                const { default: api } = await import('@/api')
                await this.$request({
                    url: `${api.auth.updateRole}/${this.roleEditForm.id}/role`,
                    method: 'put',
                    data: { role: this.roleEditForm.role }
                })
                this.$message.success('角色修改成功')
                this.roleDialogVisible = false
                this.loadUsers()
            } catch (error) {
                console.error('修改角色失败:', error)
                this.$message.error('修改角色失败')
            }
        }
    }
}
</script>

<style lang="less" scoped>
@import '@/assets/styles/variables.less';
@import '@/assets/styles/mixins.less';

// ============================================
// 页面容器
// ============================================

.users-page {
    position: relative;
    min-height: 100%;
    padding: @spacing-xl;
    background: linear-gradient(135deg, #0d1525 0%, #1a2744 50%, #16213e 100%);
    box-sizing: border-box;
}

// ============================================
// 页面头部
// ============================================

.page-header {
    display: flex;
    justify-content: space-between;
    align-items: center;
    padding: @spacing-xl @spacing-2xl;
    margin-bottom: @spacing-xl;
    background: linear-gradient(135deg, rgba(123, 47, 255, 0.15) 0%, rgba(0, 180, 255, 0.08) 100%);
    border: 1px solid rgba(123, 47, 255, 0.3);
    border-radius: 16px;
    backdrop-filter: blur(10px);

    .header-left {
        display: flex;
        align-items: center;
        gap: @spacing-lg;
    }

    .header-icon {
        width: 56px;
        height: 56px;
        background: linear-gradient(135deg, #7b2fff, #5a1fd4);
        border-radius: 12px;
        display: flex;
        align-items: center;
        justify-content: center;
        box-shadow: 0 0 20px rgba(123, 47, 255, 0.4);

        i {
            font-size: 28px;
            color: #ffffff;
        }
    }

    .header-info {
        h1 {
            font-size: 24px;
            font-weight: 600;
            color: #ffd700;
            margin: 0 0 4px;
            text-shadow: 0 0 10px rgba(255, 215, 0, 0.5);
        }

        p {
            font-size: 13px;
            color: rgba(255, 255, 255, 0.5);
            margin: 0;
        }
    }

    .header-right {
        .user-count {
            display: flex;
            align-items: baseline;
            gap: 6px;
            padding: 8px 20px;
            background: rgba(255, 215, 0, 0.1);
            border: 1px solid rgba(255, 215, 0, 0.3);
            border-radius: 20px;

            .count-number {
                font-size: 28px;
                font-weight: 700;
                color: #ffd700;
                font-family: 'Orbitron', monospace;
            }

            .count-label {
                font-size: 14px;
                color: rgba(255, 215, 0, 0.7);
            }
        }
    }
}

// ============================================
// 搜索栏
// ============================================

.search-bar {
    margin-bottom: @spacing-xl;
}

.search-wrapper {
    position: relative;
    display: flex;
    align-items: center;
    max-width: 400px;

    > i {
        position: absolute;
        left: 16px;
        font-size: 16px;
        color: rgba(255, 255, 255, 0.4);
    }

    .search-input {
        width: 100%;
        height: 44px;
        padding: 0 44px;
        background: rgba(13, 21, 37, 0.8);
        border: 1px solid rgba(123, 47, 255, 0.3);
        border-radius: 10px;
        color: #ffffff;
        font-size: 14px;
        outline: none;
        transition: all 0.3s ease;

        &::placeholder {
            color: rgba(255, 255, 255, 0.4);
        }

        &:focus {
            border-color: #7b2fff;
            box-shadow: 0 0 15px rgba(123, 47, 255, 0.3);
        }
    }

    .clear-btn {
        position: absolute;
        right: 12px;
        cursor: pointer;
        color: rgba(255, 255, 255, 0.4);
        font-size: 14px;
        transition: color 0.2s;

        &:hover {
            color: #ffd700;
        }
    }
}

// ============================================
// 用户容器
// ============================================

.users-container {
    min-height: 200px;
}

// 空状态
.empty-state {
    display: flex;
    flex-direction: column;
    align-items: center;
    justify-content: center;
    padding: 80px 20px;
    background: rgba(255, 255, 255, 0.03);
    border: 2px dashed rgba(123, 47, 255, 0.3);
    border-radius: 16px;

    i {
        font-size: 64px;
        color: rgba(255, 255, 255, 0.2);
        margin-bottom: 16px;
    }

    p {
        font-size: 16px;
        color: rgba(255, 255, 255, 0.5);
        margin: 0;
    }
}

// ============================================
// 用户卡片网格
// ============================================

.users-grid {
    display: grid;
    grid-template-columns: repeat(auto-fill, minmax(320px, 1fr));
    gap: @spacing-lg;
}

.user-card {
    background: linear-gradient(135deg, rgba(26, 39, 68, 0.9) 0%, rgba(13, 21, 37, 0.9) 100%);
    border: 1px solid rgba(123, 47, 255, 0.25);
    border-radius: 14px;
    padding: @spacing-xl;
    transition: all 0.3s ease;

    &:hover {
        border-color: rgba(123, 47, 255, 0.5);
        box-shadow: 0 8px 32px rgba(123, 47, 255, 0.15);
        transform: translateY(-2px);
    }

    .card-main {
        display: flex;
        align-items: center;
        gap: @spacing-lg;
        margin-bottom: @spacing-lg;
    }

    .user-avatar {
        width: 48px;
        height: 48px;
        background: linear-gradient(135deg, #7b2fff, #4a1090);
        border-radius: 10px;
        display: flex;
        align-items: center;
        justify-content: center;
        font-size: 20px;
        font-weight: 700;
        color: #ffffff;
        flex-shrink: 0;
    }

    .user-info {
        flex: 1;
        min-width: 0;

        .user-name {
            font-size: 16px;
            font-weight: 600;
            color: #ffffff;
            margin-bottom: 4px;
            white-space: nowrap;
            overflow: hidden;
            text-overflow: ellipsis;
        }

        .user-phone {
            display: flex;
            align-items: center;
            gap: 6px;
            font-size: 13px;
            color: rgba(255, 255, 255, 0.5);

            i {
                font-size: 14px;
                color: #00b4ff;
            }
        }
    }

    .user-role {
        display: flex;
        align-items: center;
        gap: 6px;
        padding: 6px 12px;
        border-radius: 20px;
        font-size: 12px;
        font-weight: 600;
        white-space: nowrap;

        i {
            font-size: 14px;
        }

        &.role-admin {
            background: rgba(255, 215, 0, 0.15);
            border: 1px solid rgba(255, 215, 0, 0.4);
            color: #ffd700;
        }

        &.role-user {
            background: rgba(0, 180, 255, 0.15);
            border: 1px solid rgba(0, 180, 255, 0.4);
            color: #00b4ff;
        }
    }

    .card-actions {
        display: flex;
        gap: @spacing-sm;
    }

    .btn-action {
        flex: 1;
        padding: 10px 16px;
        border-radius: 8px;
        font-size: 13px;
        font-weight: 500;
        cursor: pointer;
        transition: all 0.2s ease;
        display: flex;
        align-items: center;
        justify-content: center;
        gap: 6px;

        i {
            font-size: 14px;
        }

        &.btn-edit {
            background: rgba(255, 215, 0, 0.1);
            border: 1px solid rgba(255, 215, 0, 0.3);
            color: #ffd700;

            &:hover {
                background: rgba(255, 215, 0, 0.2);
                border-color: #ffd700;
                box-shadow: 0 0 15px rgba(255, 215, 0, 0.2);
            }
        }
    }
}

// ============================================
// 模态框
// ============================================

.modal-overlay {
    position: fixed;
    top: 0;
    left: 0;
    right: 0;
    bottom: 0;
    background: rgba(0, 0, 0, 0.7);
    backdrop-filter: blur(4px);
    display: flex;
    align-items: center;
    justify-content: center;
    z-index: 1000;
    opacity: 0;
    visibility: hidden;
    transition: all 0.3s ease;

    &.is-visible {
        opacity: 1;
        visibility: visible;
    }
}

.modal-dialog {
    width: 90%;
    max-width: 420px;
    background: linear-gradient(135deg, #1a2744 0%, #0d1525 100%);
    border: 1px solid rgba(123, 47, 255, 0.4);
    border-radius: 16px;
    box-shadow: 0 20px 60px rgba(0, 0, 0, 0.5);
    transform: scale(0.9);
    transition: transform 0.3s ease;

    .modal-overlay.is-visible & {
        transform: scale(1);
    }
}

.modal-header {
    display: flex;
    justify-content: space-between;
    align-items: center;
    padding: @spacing-xl @spacing-2xl;
    border-bottom: 1px solid rgba(123, 47, 255, 0.2);

    h3 {
        font-size: 18px;
        font-weight: 600;
        color: #ffd700;
        margin: 0;
    }

    .btn-close {
        width: 32px;
        height: 32px;
        background: transparent;
        border: 1px solid rgba(255, 255, 255, 0.2);
        border-radius: 8px;
        color: rgba(255, 255, 255, 0.6);
        cursor: pointer;
        display: flex;
        align-items: center;
        justify-content: center;
        transition: all 0.2s;

        &:hover {
            border-color: #ff6b6b;
            color: #ff6b6b;
        }
    }
}

.modal-body {
    padding: @spacing-2xl;
}

.user-preview {
    display: flex;
    align-items: center;
    gap: @spacing-lg;
    padding: @spacing-lg;
    background: rgba(13, 21, 37, 0.5);
    border: 1px solid rgba(123, 47, 255, 0.2);
    border-radius: 12px;
    margin-bottom: @spacing-xl;

    .preview-avatar {
        width: 52px;
        height: 52px;
        background: linear-gradient(135deg, #ffd700, #ff8c00);
        border-radius: 10px;
        display: flex;
        align-items: center;
        justify-content: center;
        font-size: 22px;
        font-weight: 700;
        color: #0d1525;
    }

    .preview-info {
        display: flex;
        flex-direction: column;
        gap: 4px;

        .preview-name {
            font-size: 16px;
            font-weight: 600;
            color: #ffffff;
        }

        .preview-phone {
            font-size: 13px;
            color: rgba(255, 255, 255, 0.5);
        }
    }
}

.form-group {
    label {
        display: block;
        font-size: 14px;
        font-weight: 500;
        color: rgba(255, 255, 255, 0.7);
        margin-bottom: @spacing-md;
    }
}

.role-options {
    display: flex;
    gap: @spacing-md;
}

.role-option {
    flex: 1;
    cursor: pointer;

    input {
        display: none;

        &:checked + .option-box {
            border-color: #7b2fff;
            background: rgba(123, 47, 255, 0.2);
        }

        &:checked + .option-admin {
            border-color: #ffd700;
            background: rgba(255, 215, 0, 0.2);
        }
    }

    .option-box {
        display: flex;
        flex-direction: column;
        align-items: center;
        gap: 8px;
        padding: @spacing-xl @spacing-lg;
        background: rgba(13, 21, 37, 0.5);
        border: 2px solid rgba(123, 47, 255, 0.3);
        border-radius: 12px;
        transition: all 0.2s ease;

        i {
            font-size: 24px;
        }

        span {
            font-size: 14px;
            font-weight: 600;
        }

        &.option-admin {
            border-color: rgba(255, 215, 0, 0.3);

            i, span {
                color: #ffd700;
            }
        }

        &.option-user {
            i, span {
                color: #00b4ff;
            }
        }
    }

    &:hover .option-box {
        border-color: rgba(123, 47, 255, 0.6);
    }
}

.modal-footer {
    display: flex;
    justify-content: flex-end;
    gap: @spacing-md;
    padding: @spacing-lg @spacing-2xl @spacing-xl;
    border-top: 1px solid rgba(123, 47, 255, 0.2);

    button {
        padding: 10px 24px;
        border-radius: 8px;
        font-size: 14px;
        font-weight: 500;
        cursor: pointer;
        transition: all 0.2s ease;
    }

    .btn-cancel {
        background: transparent;
        border: 1px solid rgba(255, 255, 255, 0.2);
        color: rgba(255, 255, 255, 0.7);

        &:hover {
            border-color: rgba(255, 255, 255, 0.4);
            color: #ffffff;
        }
    }

    .btn-confirm {
        background: linear-gradient(135deg, #ffd700 0%, #ff8c00 100%);
        border: none;
        color: #0d1525;
        font-weight: 600;

        &:hover {
            box-shadow: 0 0 20px rgba(255, 215, 0, 0.4);
        }
    }
}

// ============================================
// 响应式
// ============================================

@media (max-width: @breakpoint-md) {
    .users-page {
        padding: @spacing-md;
    }

    .page-header {
        flex-direction: column;
        gap: @spacing-lg;
        text-align: center;
        padding: @spacing-lg;

        .header-left {
            flex-direction: column;
        }

        .header-right .user-count {
            width: 100%;
            justify-content: center;
        }
    }

    .search-wrapper {
        max-width: 100%;
    }

    .users-grid {
        grid-template-columns: 1fr;
    }

    .user-card {
        .card-main {
            flex-wrap: wrap;
        }

        .user-role {
            margin-left: auto;
        }
    }

    .role-options {
        flex-direction: column;
    }
}
</style>
