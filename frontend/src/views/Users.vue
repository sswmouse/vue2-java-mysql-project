<!--
 * @Description: 用户管理组件 - DNF霓虹风格
 * @Author: Claude Code
 * @Date: 2026-04-14
 * @LastEditTime: 2026-04-14 11:37:29
 * @FilePath: /vue2-java-mysql-project/frontend/src/views/Users.vue
-->
<template>
    <div class="users">
        <!-- 背景粒子 -->
        <div class="bg-particles">
            <div
                v-for="i in 10"
                :key="i"
                class="particle"
                :style="{
                    left: `${Math.random() * 100}%`,
                    top: `${Math.random() * 100}%`,
                    animationDelay: `${Math.random() * 6}s`,
                    animationDuration: `${6 + Math.random() * 4}s`
                }"
            />
        </div>

        <!-- 页面头部 -->
        <div class="page-header">
            <div class="header-content">
                <div class="header-icon">
                    <i class="el-icon-user-solid" />
                </div>
                <div class="header-text">
                    <h1>用户管理</h1>
                    <p>管理系统用户账户与权限</p>
                </div>
            </div>
            <div class="header-decoration">
                <div class="deco-line" />
                <div class="deco-diamond" />
            </div>
        </div>

        <!-- 主内容区 -->
        <div class="content-wrapper">
            <!-- 创建用户表单 -->
            <div class="form-card">
                <div class="card-header">
                    <div class="card-title">
                        <i class="el-icon-edit-outline" />
                        <span>创建新用户</span>
                    </div>
                </div>

                <div class="card-body">
                    <el-form
                        ref="userForm"
                        :model="form"
                        :rules="rules"
                        label-position="top"
                        class="user-form"
                    >
                        <div class="form-grid">
                            <el-form-item
                                label="用户名"
                                prop="username"
                                class="form-item"
                            >
                                <el-input
                                    v-model="form.username"
                                    placeholder="请输入用户名"
                                    prefix-icon="el-icon-user"
                                    clearable
                                />
                            </el-form-item>

                            <el-form-item
                                label="邮箱地址"
                                prop="email"
                                class="form-item"
                            >
                                <el-input
                                    v-model="form.email"
                                    placeholder="请输入邮箱"
                                    prefix-icon="el-icon-message"
                                    clearable
                                />
                            </el-form-item>

                            <el-form-item
                                label="密码"
                                prop="password"
                                class="form-item"
                            >
                                <el-input
                                    v-model="form.password"
                                    type="password"
                                    placeholder="请输入密码"
                                    prefix-icon="el-icon-lock"
                                    show-password
                                    clearable
                                />
                            </el-form-item>
                        </div>

                        <div class="form-actions">
                            <el-button
                                type="primary"
                                icon="el-icon-plus"
                                class="btn-create"
                                @click="handleSubmit"
                            >
                                创建用户
                            </el-button>
                            <el-button
                                icon="el-icon-refresh"
                                class="btn-reset"
                                @click="resetForm"
                            >
                                重置
                            </el-button>
                        </div>
                    </el-form>
                </div>
            </div>

            <!-- 用户列表 -->
            <div class="table-card">
                <div class="card-header">
                    <div class="card-title">
                        <i class="el-icon-s-order" />
                        <span>用户列表</span>
                    </div>
                    <div class="card-badge">
                        {{ users.length }} 位用户
                    </div>
                </div>

                <div class="card-body">
                    <el-table
                        v-loading="loading"
                        :data="users"
                        class="dnf-table"
                        stripe
                        :header-cell-style="{ background: 'linear-gradient(180deg, rgba(74, 16, 144, 0.3) 0%, rgba(13, 25, 48, 0.8) 100%)' }"
                        :row-class-name="tableRowClassName"
                    >
                        <el-table-column
                            prop="id"
                            label="ID"
                            width="100"
                            align="center"
                        >
                            <template slot-scope="scope">
                                <span class="id-badge">#{{ scope.row.id }}</span>
                            </template>
                        </el-table-column>

                        <el-table-column
                            prop="username"
                            label="用户名"
                            min-width="180"
                        >
                            <template slot-scope="scope">
                                <div class="user-cell">
                                    <div class="user-avatar">
                                        {{ scope.row.username.charAt(0).toUpperCase() }}
                                    </div>
                                    <span class="user-name">{{ scope.row.username }}</span>
                                </div>
                            </template>
                        </el-table-column>

                        <el-table-column
                            prop="email"
                            label="邮箱"
                            min-width="220"
                        >
                            <template slot-scope="scope">
                                <span class="email-cell">
                                    <i class="el-icon-message" />
                                    {{ scope.row.email }}
                                </span>
                            </template>
                        </el-table-column>

                        <el-table-column
                            label="操作"
                            width="200"
                            align="center"
                            fixed="right"
                        >
                            <template slot-scope="scope">
                                <div class="action-buttons">
                                    <el-button
                                        type="primary"
                                        size="small"
                                        icon="el-icon-edit"
                                        class="btn-edit"
                                        @click="handleEdit(scope.row)"
                                    >
                                        编辑
                                    </el-button>
                                    <el-button
                                        type="danger"
                                        size="small"
                                        icon="el-icon-delete"
                                        class="btn-delete"
                                        @click="handleDelete(scope.row)"
                                    >
                                        删除
                                    </el-button>
                                </div>
                            </template>
                        </el-table-column>
                    </el-table>
                </div>
            </div>
        </div>

        <!-- 编辑对话框 -->
        <el-dialog
            :visible.sync="dialogVisible"
            title="编辑用户"
            width="500px"
            class="edit-dialog"
            :close-on-click-modal="false"
        >
            <el-form
                :model="editForm"
                label-position="top"
            >
                <el-form-item label="用户名">
                    <el-input
                        v-model="editForm.username"
                        prefix-icon="el-icon-user"
                        placeholder="请输入用户名"
                    />
                </el-form-item>
                <el-form-item label="邮箱">
                    <el-input
                        v-model="editForm.email"
                        prefix-icon="el-icon-message"
                        placeholder="请输入邮箱"
                    />
                </el-form-item>
            </el-form>

            <div
                slot="footer"
                class="dialog-footer"
            >
                <el-button
                    icon="el-icon-close"
                    class="btn-cancel"
                    @click="dialogVisible = false"
                >
                    取消
                </el-button>
                <el-button
                    type="primary"
                    icon="el-icon-check"
                    class="btn-confirm"
                    @click="updateUser"
                >
                    确认更新
                </el-button>
            </div>
        </el-dialog>
    </div>
</template>

<script>
import gsap from 'gsap'

export default {
    name: 'Users',
    data() {
        return {
            users: [],
            form: {
                username: '',
                email: '',
                password: ''
            },
            editForm: {
                id: '',
                username: '',
                email: ''
            },
            dialogVisible: false,
            loading: false,
            rules: {
                username: [
                    { required: true, message: '请输入用户名', trigger: 'blur' },
                    { min: 3, max: 20, message: '用户名长度为 3-20 个字符', trigger: 'blur' }
                ],
                email: [
                    { required: true, message: '请输入邮箱地址', trigger: 'blur' },
                    { type: 'email', message: '请输入正确的邮箱格式', trigger: 'blur' }
                ],
                password: [
                    { required: true, message: '请输入密码', trigger: 'blur' },
                    { min: 6, message: '密码长度至少为 6 位', trigger: 'blur' }
                ]
            }
        }
    },
    mounted() {
        this.loadUsers()
        this.initAnimations()
    },
    methods: {
        initAnimations() {
            gsap.from('.page-header', {
                y: -40,
                opacity: 0,
                duration: 0.8,
                ease: 'power3.out'
            })

            gsap.from('.form-card', {
                y: 40,
                opacity: 0,
                duration: 0.8,
                delay: 0.2,
                ease: 'power3.out'
            })

            gsap.from('.table-card', {
                y: 40,
                opacity: 0,
                duration: 0.8,
                delay: 0.4,
                ease: 'power3.out'
            })
        },

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
                // 如果没有后端API，显示示例数据
                this.users = [
                    { id: 1, username: 'admin', email: 'admin@dnf-game.com' },
                    { id: 2, username: 'player_one', email: 'player1@dnf-game.com' },
                    { id: 3, username: 'warrior_king', email: 'warrior@dnf-game.com' }
                ]
            } finally {
                this.loading = false
            }
        },

        handleSubmit() {
            this.$refs.userForm.validate(async (valid) => {
                if (valid) {
                    try {
                        const { default: api } = await import('@/api')
                        await this.$request({
                            url: api.user.create,
                            method: 'post',
                            data: this.form
                        })
                        this.$message.success('用户创建成功')
                        this.resetForm()
                        this.loadUsers()
                    } catch (error) {
                        console.error('创建用户失败:', error)
                        this.$message.error('创建用户失败')
                    }
                }
            })
        },

        resetForm() {
            this.$refs.userForm.resetFields()
        },

        handleEdit(user) {
            this.editForm = { ...user }
            this.dialogVisible = true
        },

        async updateUser() {
            try {
                const { default: api } = await import('@/api')
                await this.$request({
                    url: api.user.update.replace(':id', this.editForm.id),
                    method: 'put',
                    data: this.editForm
                })
                this.$message.success('用户信息更新成功')
                this.dialogVisible = false
                this.loadUsers()
            } catch (error) {
                console.error('更新用户失败:', error)
                this.$message.error('更新用户失败')
            }
        },

        handleDelete(user) {
            this.$confirm(`确定要删除用户「${user.username}」吗？此操作不可撤销。`, '危险操作警告', {
                confirmButtonText: '确认删除',
                cancelButtonText: '取消',
                type: 'warning',
                customClass: 'delete-confirm-dialog'
            }).then(async () => {
                try {
                    const { default: api } = await import('@/api')
                    await this.$request({
                        url: api.user.delete.replace(':id', user.id),
                        method: 'delete'
                    })
                    this.$message.success('用户已删除')
                    this.loadUsers()
                } catch (error) {
                    console.error('删除用户失败:', error)
                    this.$message.error('删除用户失败')
                }
            }).catch(() => {
                // 取消删除
            })
        },
        tableRowClassName({ row, rowIndex }) {
            console.log('Row data:', row)
            return rowIndex % 2 === 0 ? 'even-row' : 'odd-row'
        }
    }
}
</script>

<style lang="less" scoped>
@import '@/assets/styles/variables.less';
@import '@/assets/styles/mixins.less';

.users {
    position: relative;
    min-height: 100%;
    padding: @spacing-xl;
    background: @dnf-bg-dark;
    overflow-x: hidden;
}

// ============================================
// 粒子背景
// ============================================

.bg-particles {
    position: fixed;
    top: 0;
    left: 0;
    right: 0;
    bottom: 0;
    pointer-events: none;
    z-index: 0;
}

.particle {
    position: absolute;
    width: 3px;
    height: 3px;
    background: @dnf-secondary-purple;
    border-radius: 50%;
    box-shadow: 0 0 8px @dnf-secondary-purple;
    animation: particle-drift-1 10s ease-in-out infinite;
}

// ============================================
// 页面头部
// ============================================

.page-header {
    position: relative;
    z-index: 1;
    padding: @spacing-2xl @spacing-xl;
    margin-bottom: @spacing-xl;
    background: linear-gradient(180deg, fade(@dnf-secondary-purple, 0.1) 0%, transparent 100%);
    border-bottom: 1px solid @dnf-border-dark;
    border-radius: @border-radius-xl;
}

.header-content {
    display: flex;
    align-items: center;
    gap: @spacing-xl;

    .header-icon {
        width: 72px;
        height: 72px;
        background: linear-gradient(135deg, @dnf-secondary-purple, @dnf-primary-purple);
        border-radius: @border-radius-xl;
        display: flex;
        align-items: center;
        justify-content: center;
        box-shadow:
            0 0 30px fade(@dnf-secondary-purple, 0.4),
            inset 0 0 20px rgba(255, 255, 255, 0.1);

        i {
            font-size: 36px;
            color: @dnf-text-primary;
            filter: drop-shadow(0 0 10px rgba(255, 255, 255, 0.5));
        }
    }

    .header-text {
        h1 {
            font-family: @font-display, 'Orbitron', sans-serif;
            font-size: @font-size-3xl;
            font-weight: 700;
            letter-spacing: 0.1em;
            .neon-gold-text();
            margin-bottom: @spacing-xs;
        }

        p {
            font-size: @font-size-md;
            color: @dnf-text-muted;
            letter-spacing: 0.05em;
        }
    }
}

.header-decoration {
    display: flex;
    align-items: center;
    gap: @spacing-md;
    margin-top: @spacing-lg;

    .deco-line {
        flex: 1;
        height: 1px;
        background: linear-gradient(90deg, @dnf-primary-gold, transparent);
    }

    .deco-diamond {
        width: 8px;
        height: 8px;
        background: @dnf-primary-gold;
        transform: rotate(45deg);
        box-shadow: 0 0 10px fade(@dnf-primary-gold, 0.6);
    }
}

// ============================================
// 内容区域
// ============================================

.content-wrapper {
    position: relative;
    z-index: 1;
    display: flex;
    flex-direction: column;
    gap: @spacing-xl;
    max-width: 1200px;
    margin: 0 auto;
}

// ============================================
// 卡片样式
// ============================================

.form-card,
.table-card {
    .glass-card();
    overflow: hidden;
}

.card-header {
    display: flex;
    justify-content: space-between;
    align-items: center;
    padding: @spacing-xl @spacing-2xl;
    background: linear-gradient(180deg, fade(@dnf-secondary-purple, 0.15) 0%, transparent 100%);
    border-bottom: 1px solid @dnf-border-dark;
}

.card-title {
    display: flex;
    align-items: center;
    gap: @spacing-md;
    font-family: @font-display, 'Orbitron', sans-serif;
    font-size: @font-size-lg;
    font-weight: 600;
    letter-spacing: 0.08em;
    color: @dnf-text-primary;

    i {
        font-size: 22px;
        color: @dnf-secondary-purple;
        filter: drop-shadow(0 0 8px fade(@dnf-secondary-purple, 0.6));
    }
}

.card-badge {
    display: inline-flex;
    align-items: center;
    padding: 6px 16px;
    background: fade(@dnf-primary-gold, 15%);
    border: 1px solid fade(@dnf-primary-gold, 40%);
    border-radius: @border-radius-full;
    font-family: @font-mono, 'Share Tech Mono', monospace;
    font-size: @font-size-sm;
    color: @dnf-primary-gold;
    text-shadow: 0 0 8px fade(@dnf-primary-gold, 0.4);
}

.card-body {
    padding: @spacing-2xl;
}

// ============================================
// 表单样式
// ============================================

.user-form {
    .form-grid {
        display: grid;
        grid-template-columns: repeat(auto-fit, minmax(280px, 1fr));
        gap: @spacing-xl;
        margin-bottom: @spacing-xl;
    }

    .form-item {
        margin-bottom: 0;

        ::v-deep .el-form-item__label {
            font-family: @font-display, 'Orbitron', sans-serif;
            font-size: @font-size-sm;
            font-weight: 500;
            letter-spacing: 0.08em;
            color: @dnf-text-secondary;
            text-transform: uppercase;
            margin-bottom: @spacing-sm;
        }

        ::v-deep .el-input__prefix {
            i {
                font-size: 18px;
                color: @dnf-secondary-purple;
                filter: drop-shadow(0 0 5px fade(@dnf-secondary-purple, 0.5));
            }
        }
    }

    .form-actions {
        display: flex;
        gap: @spacing-md;
        padding-top: @spacing-lg;
        border-top: 1px solid @dnf-border-dark;
    }
}

.btn-create {
    background: @gradient-blue-purple;
    border: none;
    font-weight: @font-weight-bold;
    letter-spacing: 0.05em;
    padding: 12px 28px;

    &:hover {
        box-shadow: 0 0 25px fade(@dnf-secondary-purple, 0.5);
        transform: translateY(-2px);
    }
}

.btn-reset {
    background: transparent;
    border: 1px solid @dnf-border-light;
    color: @dnf-text-secondary;

    &:hover {
        border-color: @dnf-secondary-purple;
        color: @dnf-secondary-purple;
    }
}

// ============================================
// 表格样式
// ============================================

.dnf-table {
    background: transparent;
    border: none;

    ::v-deep .el-table__header-wrapper th {
        background: linear-gradient(180deg, rgba(74, 16, 144, 0.3) 0%, rgba(13, 25, 48, 0.8) 100%);
        color: @dnf-text-primary;
        font-family: @font-display, 'Orbitron', sans-serif;
        font-weight: 600;
        letter-spacing: 0.08em;
        text-transform: uppercase;
        font-size: @font-size-sm;
        border-bottom: 1px solid @dnf-border-dark;
        padding: @spacing-lg @spacing-md;
    }

    ::v-deep .el-table__body-wrapper td {
        background: transparent;
        border-bottom: 1px solid fade(@dnf-border-dark, 50%);
        padding: @spacing-lg @spacing-md;
        transition: all @transition-fast;
    }

    ::v-deep .el-table__row {
        transition: all @transition-fast;

        &:hover>td {
            background: fade(@dnf-secondary-purple, 10%) !important;
        }

        &.even-row>td {
            background: fade(@dnf-primary-dark, 30%);
        }

        &.odd-row>td {
            background: transparent;
        }
    }
}

.id-badge {
    display: inline-flex;
    align-items: center;
    justify-content: center;
    min-width: 48px;
    padding: 4px 12px;
    background: fade(@dnf-secondary-purple, 20%);
    border: 1px solid fade(@dnf-secondary-purple, 40%);
    border-radius: @border-radius-full;
    font-family: @font-mono, 'Share Tech Mono', monospace;
    font-size: @font-size-sm;
    font-weight: 600;
    color: @dnf-secondary-purple;
}

.user-cell {
    display: flex;
    align-items: center;
    gap: @spacing-md;

    .user-avatar {
        width: 40px;
        height: 40px;
        background: linear-gradient(135deg, @dnf-secondary-purple, @dnf-primary-purple);
        border-radius: @border-radius-lg;
        display: flex;
        align-items: center;
        justify-content: center;
        font-family: @font-display, 'Orbitron', sans-serif;
        font-size: @font-size-lg;
        font-weight: 700;
        color: @dnf-text-primary;
        box-shadow: 0 0 15px fade(@dnf-secondary-purple, 0.3);
    }

    .user-name {
        font-weight: @font-weight-semibold;
        color: @dnf-text-primary;
        letter-spacing: 0.02em;
    }
}

.email-cell {
    display: flex;
    align-items: center;
    gap: @spacing-sm;
    color: @dnf-text-secondary;
    font-size: @font-size-sm;

    i {
        color: @dnf-info;
        font-size: 16px;
    }
}

.action-buttons {
    display: flex;
    gap: @spacing-sm;
    justify-content: center;
}

.btn-edit {
    background: fade(@dnf-info, 15%);
    border: 1px solid fade(@dnf-info, 40%);
    color: @dnf-info;
    font-size: @font-size-sm;

    &:hover {
        background: fade(@dnf-info, 25%);
        box-shadow: 0 0 15px fade(@dnf-info, 0.4);
    }
}

.btn-delete {
    background: fade(@dnf-danger, 15%);
    border: 1px solid fade(@dnf-danger, 40%);
    color: @dnf-danger;
    font-size: @font-size-sm;

    &:hover {
        background: fade(@dnf-danger, 25%);
        box-shadow: 0 0 15px fade(@dnf-danger, 0.4);
    }
}

// ============================================
// 对话框样式
// ============================================

.edit-dialog {
    ::v-deep .el-dialog {
        border-radius: @border-radius-2xl;
        overflow: hidden;
    }

    ::v-deep .el-dialog__header {
        padding: @spacing-xl @spacing-2xl;
    }

    ::v-deep .el-dialog__body {
        padding: @spacing-xl @spacing-2xl;
    }

    ::v-deep .el-dialog__footer {
        padding: @spacing-lg @spacing-2xl;
    }
}

.dialog-footer {
    display: flex;
    justify-content: flex-end;
    gap: @spacing-md;
}

.btn-cancel {
    background: transparent;
    border: 1px solid @dnf-border-light;
    color: @dnf-text-secondary;

    &:hover {
        border-color: @dnf-secondary-purple;
        color: @dnf-secondary-purple;
    }
}

.btn-confirm {
    background: @gradient-blue-purple;
    border: none;
    font-weight: @font-weight-bold;

    &:hover {
        box-shadow: 0 0 25px fade(@dnf-secondary-purple, 0.5);
    }
}

// ============================================
// 响应式设计
// ============================================

@media (max-width: @breakpoint-md) {
    .users {
        padding: @spacing-md;
    }

    .page-header {
        padding: @spacing-xl @spacing-md;
    }

    .header-content {
        flex-direction: column;
        text-align: center;

        .header-icon {
            width: 60px;
            height: 60px;

            i {
                font-size: 30px;
            }
        }

        .header-text h1 {
            font-size: @font-size-2xl;
        }
    }

    .card-body {
        padding: @spacing-xl @spacing-md;
    }

    .form-grid {
        grid-template-columns: 1fr !important;
    }

    .action-buttons {
        flex-direction: column;
    }
}
</style>
