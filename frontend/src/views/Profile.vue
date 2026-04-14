<!--
 * @Description: 个人中心页面 - DNF霓虹风格
 * @Author: Claude Code
 * @Date: 2026-04-14
-->
<template>
    <div class="profile-page">
        <!-- 页面头部 -->
        <div class="page-header">
            <div class="header-content">
                <div class="header-icon">
                    <i class="el-icon-user-solid" />
                </div>
                <div class="header-info">
                    <h1>个人中心</h1>
                    <p>管理你的账户信息和安全设置</p>
                </div>
            </div>
        </div>

        <!-- 主内容区 -->
        <div class="profile-content">
            <!-- 左侧：头像卡片 -->
            <div class="profile-sidebar">
                <div class="avatar-card">
                    <!-- 头像悬停编辑 -->
                    <div
                        class="avatar-preview"
                        @mouseenter="showEditIcon = true"
                        @mouseleave="showEditIcon = false"
                        @click="openAvatarDialog"
                    >
                        <div class="avatar-wrapper">
                            <img
                                v-if="form.avatar"
                                :src="form.avatar"
                                alt="头像"
                                class="avatar-img"
                            >
                            <span
                                v-else
                                class="avatar-placeholder"
                            >{{ userInitial }}</span>
                        </div>
                        <div class="avatar-glow" />

                        <!-- 编辑图标 -->
                        <div
                            v-show="showEditIcon"
                            class="edit-overlay"
                        >
                            <i class="el-icon-camera" />
                            <span>更换头像</span>
                        </div>
                    </div>

                    <h3 class="user-name">
                        {{ form.username || '冒险家' }}
                    </h3>
                    <p class="user-role">
                        <i :class="isAdmin ? 'el-icon-s-check' : 'el-icon-user'" />
                        {{ isAdmin ? '管理员' : '普通用户' }}
                    </p>
                </div>
            </div>

            <!-- 右侧：信息表单 -->
            <div class="profile-main">
                <!-- 基本信息 -->
                <div class="form-section">
                    <div class="section-header">
                        <i class="el-icon-info" />
                        <h2>基本信息</h2>
                    </div>

                    <div class="form-grid">
                        <div class="form-group">
                            <label>
                                <i class="el-icon-user" />
                                用户名
                            </label>
                            <div class="input-wrapper">
                                <input
                                    v-model="form.username"
                                    type="text"
                                    placeholder="请输入用户名"
                                    :disabled="!editFields.username"
                                >
                                <button
                                    class="btn-toggle"
                                    @click="toggleEdit('username')"
                                >
                                    <i :class="editFields.username ? 'el-icon-check' : 'el-icon-edit'" />
                                </button>
                            </div>
                            <div
                                v-if="usernameError"
                                class="field-error"
                            >
                                <i class="el-icon-warning" />
                                {{ usernameError }}
                            </div>
                        </div>

                        <div class="form-group">
                            <label>
                                <i class="el-icon-mobile-phone" />
                                手机号
                            </label>
                            <div class="input-wrapper">
                                <input
                                    v-model="form.phone"
                                    type="text"
                                    placeholder="请输入手机号"
                                    :disabled="!editFields.phone"
                                >
                                <button
                                    class="btn-toggle"
                                    @click="toggleEdit('phone')"
                                >
                                    <i :class="editFields.phone ? 'el-icon-check' : 'el-icon-edit'" />
                                </button>
                            </div>
                        </div>

                        <div class="form-group">
                            <label>
                                <i class="el-icon-postcard" />
                                用户ID
                            </label>
                            <div class="input-wrapper input-disabled">
                                <input
                                    :value="form.id"
                                    type="text"
                                    disabled
                                >
                            </div>
                        </div>

                        <div class="form-group">
                            <label>
                                <i class="el-icon-s-check" />
                                角色
                            </label>
                            <div class="input-wrapper input-disabled">
                                <input
                                    :value="isAdmin ? '管理员' : '普通用户'"
                                    type="text"
                                    disabled
                                >
                            </div>
                        </div>
                    </div>
                </div>

                <!-- 密码修改 -->
                <div class="form-section">
                    <div class="section-header">
                        <i class="el-icon-lock" />
                        <h2>修改密码</h2>
                    </div>

                    <div class="form-grid">
                        <div class="form-group full-width">
                            <label>
                                <i class="el-icon-key" />
                                新密码
                            </label>
                            <div class="input-wrapper">
                                <input
                                    v-model="passwordForm.newPassword"
                                    :type="showPassword ? 'text' : 'password'"
                                    placeholder="请输入新密码（至少6位）"
                                >
                                <button
                                    class="btn-toggle btn-eye"
                                    @click="showPassword = !showPassword"
                                >
                                    <i :class="showPassword ? 'el-icon-view' : 'el-icon-close-notification'" />
                                </button>
                            </div>
                        </div>

                        <div class="form-group full-width">
                            <label>
                                <i class="el-icon-key" />
                                确认密码
                            </label>
                            <div class="input-wrapper">
                                <input
                                    v-model="passwordForm.confirmPassword"
                                    :type="showPassword ? 'text' : 'password'"
                                    placeholder="请再次输入新密码"
                                >
                            </div>
                        </div>
                    </div>

                    <div
                        v-if="passwordForm.newPassword || passwordForm.confirmPassword"
                        class="password-tips"
                    >
                        <div
                            :class="['tip', { active: passwordForm.newPassword.length >= 6 }]"
                        >
                            <i class="el-icon-circle-check" />
                            密码至少6位
                        </div>
                        <div
                            :class="['tip', { active: passwordsMatch }]"
                        >
                            <i class="el-icon-circle-check" />
                            两次密码一致
                        </div>
                    </div>
                </div>

                <!-- 提交按钮 -->
                <div class="form-actions">
                    <button
                        class="btn-reset"
                        @click="resetForm"
                    >
                        <i class="el-icon-refresh-left" />
                        重置
                    </button>
                    <button
                        class="btn-save"
                        :class="{ loading: saving }"
                        :disabled="saving || !hasChanges"
                        @click="saveProfile"
                    >
                        <i
                            v-if="!saving"
                            class="el-icon-check"
                        />
                        <span
                            v-if="saving"
                            class="loading-spinner"
                        />
                        {{ saving ? '保存中...' : '保存修改' }}
                    </button>
                </div>
            </div>
        </div>

        <!-- 头像上传弹窗 -->
        <el-dialog
            title="更换头像"
            :visible.sync="avatarDialogVisible"
            width="600px"
            :close-on-click-modal="false"
            class="avatar-dialog"
        >
            <div class="avatar-upload-content">
                <!-- 已选择图片时显示预览 -->
                <div
                    v-if="tempAvatar"
                    class="selected-avatar-section"
                >
                    <div class="current-avatar-preview">
                        <img
                            :src="tempAvatar"
                            alt="已选头像"
                            class="preview-avatar-img"
                        >
                    </div>
                    <div class="avatar-actions">
                        <el-upload
                            ref="upload"
                            class="avatar-uploader-inline"
                            action="#"
                            :show-file-list="false"
                            :auto-upload="false"
                            :limit="1"
                            accept="image/*"
                            :on-change="handleAvatarChange"
                        >
                            <button
                                class="btn-change-avatar"
                                type="button"
                            >
                                <i class="el-icon-upload2" />
                                重新选择
                            </button>
                        </el-upload>
                    </div>
                </div>

                <!-- 已有头像且未选择新图片时显示当前头像 -->
                <div
                    v-else-if="form.avatar"
                    class="existing-avatar-section"
                >
                    <div class="current-avatar-preview">
                        <img
                            :src="form.avatar"
                            alt="当前头像"
                            class="preview-avatar-img"
                        >
                    </div>
                    <div class="avatar-actions">
                        <el-upload
                            ref="upload"
                            class="avatar-uploader-inline"
                            action="#"
                            :show-file-list="false"
                            :auto-upload="false"
                            :limit="1"
                            accept="image/*"
                            :on-change="handleAvatarChange"
                        >
                            <button
                                class="btn-change-avatar"
                                type="button"
                            >
                                <i class="el-icon-upload2" />
                                更换头像
                            </button>
                        </el-upload>
                        <button
                            class="btn-delete-avatar"
                            type="button"
                            @click="deleteAvatar"
                        >
                            <i class="el-icon-delete" />
                            删除头像
                        </button>
                    </div>
                </div>

                <!-- 无头像时显示上传区域 -->
                <el-upload
                    v-else
                    ref="upload"
                    class="avatar-uploader"
                    action="#"
                    :show-file-list="false"
                    :auto-upload="false"
                    :limit="1"
                    accept="image/*"
                    :on-change="handleAvatarChange"
                    drag
                >
                    <i class="el-icon-upload" />
                    <div class="el-upload__text">
                        将图片拖到此处，或<em>点击上传</em>
                    </div>
                    <div
                        slot="tip"
                        class="el-upload__tip"
                    >
                        支持 JPG、PNG 格式，建议尺寸 200x200
                    </div>
                </el-upload>
            </div>

            <div
                slot="footer"
                class="dialog-footer-buttons"
            >
                <button
                    class="btn-dialog-cancel"
                    @click="closeAvatarDialog"
                >
                    取消
                </button>
                <button
                    v-if="tempAvatar"
                    class="btn-dialog-confirm"
                    :disabled="uploading"
                    @click="confirmAvatar"
                >
                    {{ uploading ? '上传中...' : '确认更换' }}
                </button>
            </div>
        </el-dialog>
    </div>
</template>

<script>
export default {
    name: 'Profile',
    data() {
        return {
            form: {
                id: null,
                username: '',
                phone: '',
                avatar: ''
            },
            originalForm: {},
            editFields: {
                username: false,
                phone: false
            },
            usernameError: '',
            passwordForm: {
                newPassword: '',
                confirmPassword: ''
            },
            showPassword: false,
            saving: false,
            uploading: false,
            showEditIcon: false,
            avatarDialogVisible: false,
            tempAvatar: null,
            avatarFile: null
        }
    },
    computed: {
        user() {
            return this.$store.state.auth.user
        },
        isAdmin() {
            return this.$store.getters['auth/isAdmin']
        },
        userInitial() {
            const name = this.form.username || '冒'
            return name.charAt(0).toUpperCase()
        },
        passwordsMatch() {
            return this.passwordForm.newPassword === this.passwordForm.confirmPassword &&
                this.passwordForm.newPassword.length >= 6
        },
        hasChanges() {
            return JSON.stringify(this.form) !== JSON.stringify(this.originalForm) ||
                this.passwordForm.newPassword.length >= 6
        }
    },
    created() {
        this.loadUserData()
    },
    methods: {
        loadUserData() {
            if (this.user) {
                this.form = {
                    id: this.user.id,
                    username: this.user.username,
                    phone: this.user.phone,
                    avatar: this.user.avatar || ''
                }
                this.originalForm = { ...this.form }
            }
        },

        toggleEdit(field) {
            if (this.editFields[field]) {
                // 关闭编辑时验证
                if (field === 'username') {
                    const validation = this.validateUsername(this.form.username)
                    if (!validation.valid) {
                        this.usernameError = validation.message
                        return
                    }
                    this.usernameError = ''
                }
                this.editFields[field] = false
            } else {
                this.editFields[field] = true
            }
        },

        openAvatarDialog() {
            this.tempAvatar = null
            this.avatarFile = null
            this.avatarDialogVisible = true
        },

        closeAvatarDialog() {
            this.avatarDialogVisible = false
        },

        handleAvatarChange(file) {
            const isImage = file.raw.type.startsWith('image/')
            const isLt5M = file.raw.size / 1024 / 1024 < 5

            if (!isImage) {
                this.$message.error('只能上传图片文件')
                return
            }
            if (!isLt5M) {
                this.$message.error('图片大小不能超过 5MB')
                return
            }

            this.avatarFile = file.raw
            const reader = new FileReader()
            reader.onload = (e) => {
                this.tempAvatar = e.target.result
            }
            reader.readAsDataURL(file.raw)
        },

        deleteAvatar() {
            this.$msgbox({
                title: '确认删除',
                message: '确定要删除头像吗？删除后将恢复默认头像。',
                showCancelButton: true,
                confirmButtonText: '确定',
                cancelButtonText: '取消',
                type: 'warning',
                customClass: 'avatar-delete-confirm-dialog'
            }).then(() => {
                // 清空头像
                this.form.avatar = ''
                this.tempAvatar = null
                this.avatarFile = null
                this.avatarDialogVisible = false
                this.$message.success('头像已删除，保存后生效')
            }).catch(() => { })
        },

        async confirmAvatar() {
            if (!this.tempAvatar || !this.avatarFile) return

            this.uploading = true
            try {
                // 将图片转换为 Blob
                const blob = await this.dataURLtoBlob(this.tempAvatar)

                // 创建 FormData
                const formData = new FormData()
                formData.append('file', blob, 'avatar.png')

                // 上传到服务器
                const response = await this.$request.post('/upload/upload', formData, {
                    headers: {
                        'Content-Type': 'multipart/form-data'
                    }
                })

                // 获取图片URL
                const avatarUrl = response.url

                // 更新表单中的头像
                this.form.avatar = avatarUrl
                this.avatarDialogVisible = false
                this.$message.success('头像已更新，保存后生效')
            } catch (error) {
                console.error('上传头像失败:', error)
                this.$message.error(error.message || '上传头像失败')
            } finally {
                this.uploading = false
            }
        },

        dataURLtoBlob(dataURL) {
            return new Promise((resolve) => {
                const arr = dataURL.split(',')
                const mime = arr[0].match(/:(.*?);/)[1]
                const bstr = atob(arr[1])
                let n = bstr.length
                const u8arr = new Uint8Array(n)
                while (n--) {
                    u8arr[n] = bstr.charCodeAt(n)
                }
                resolve(new Blob([u8arr], { type: mime }))
            })
        },

        resetForm() {
            this.form = { ...this.originalForm }
            this.passwordForm = {
                newPassword: '',
                confirmPassword: ''
            }
            this.editFields = {
                username: false,
                phone: false
            }
            this.$message.info('已重置')
        },

        /**
     * 验证用户名
     * 规则：2-8个字符，只能是中文、英文、数字、下划线
     */
        validateUsername(username) {
            if (!username || !username.trim()) {
                return { valid: false, message: '用户名不能为空' }
            }
            const trimmed = username.trim()
            if (trimmed.length < 2 || trimmed.length > 8) {
                return { valid: false, message: '用户名长度需为2-8个字符' }
            }
            // 只允许中文、英文、数字、下划线
            const validPattern = /^[\u4e00-\u9fa5a-zA-Z0-9_]+$/
            if (!validPattern.test(trimmed)) {
                return { valid: false, message: '用户名只能包含中文、英文、数字和下划线' }
            }
            return { valid: true }
        },

        async saveProfile() {
            // 验证用户名
            if (this.form.username !== this.originalForm.username) {
                const usernameValidation = this.validateUsername(this.form.username)
                if (!usernameValidation.valid) {
                    this.$message.error(usernameValidation.message)
                    return
                }
            }

            // 验证密码
            if (this.passwordForm.newPassword) {
                if (this.passwordForm.newPassword.length < 6) {
                    this.$message.error('密码长度不能少于6位')
                    return
                }
                if (!this.passwordsMatch) {
                    this.$message.error('两次输入的密码不一致')
                    return
                }
            }

            this.saving = true
            try {
                const profileData = {}

                // 只发送修改过的字段
                if (this.form.username !== this.originalForm.username) {
                    profileData.username = this.form.username
                }
                if (this.form.phone !== this.originalForm.phone) {
                    profileData.phone = this.form.phone
                }
                if (this.form.avatar !== this.originalForm.avatar) {
                    profileData.avatar = this.form.avatar
                }
                if (this.passwordForm.newPassword && this.passwordsMatch) {
                    profileData.newPassword = this.passwordForm.newPassword
                }

                await this.$store.dispatch('auth/updateProfile', profileData)

                this.$message.success('资料更新成功')
                this.originalForm = { ...this.form }
                this.passwordForm = {
                    newPassword: '',
                    confirmPassword: ''
                }
                this.editFields = {
                    username: false,
                    phone: false
                }
            } catch (error) {
                console.error('更新资料失败:', error)
                this.$message.error(error.message || '更新资料失败')
            } finally {
                this.saving = false
            }
        }
    }
}
</script>

<style lang="less" scoped>
@import '@/assets/styles/variables.less';
@import '@/assets/styles/mixins.less';

.profile-page {
    min-height: 100%;
    padding: @spacing-xl;
    background: var(--theme-bg-dark);
}

// 页面头部
.page-header {
    margin-bottom: @spacing-xl;
    padding: @spacing-xl @spacing-2xl;
    background: var(--theme-bg-card);
    border: 1px solid var(--theme-border);
    border-radius: @border-radius-xl;
    position: relative;
    overflow: hidden;

    &::before {
        content: '';
        position: absolute;
        top: 0;
        left: 0;
        right: 0;
        height: 2px;
        background: var(--theme-accent);
    }

    .header-content {
        display: flex;
        align-items: center;
        gap: @spacing-lg;
    }

    .header-icon {
        width: 60px;
        height: 60px;
        background: var(--theme-secondary);
        border-radius: @border-radius-lg;
        display: flex;
        align-items: center;
        justify-content: center;
        box-shadow: 0 0 30px var(--theme-secondary);

        i {
            font-size: 28px;
            color: var(--theme-accent);
        }
    }

    .header-info {
        h1 {
            font-size: @font-size-2xl;
            font-weight: 700;
            color: var(--theme-accent);
            margin: 0 0 @spacing-xs;
            text-shadow: 0 0 20px var(--theme-accent);
        }

        p {
            color: var(--theme-text-secondary);
            margin: 0;
            font-size: @font-size-sm;
        }
    }
}

// 主内容区
.profile-content {
    display: grid;
    grid-template-columns: 280px 1fr;
    gap: @spacing-xl;

    @media (max-width: @breakpoint-lg) {
        grid-template-columns: 1fr;
    }
}

// 左侧头像卡片
.profile-sidebar {
    .avatar-card {
        background: var(--theme-bg-card);
        border: 1px solid var(--theme-border);
        border-radius: @border-radius-xl;
        padding: @spacing-2xl;
        text-align: center;
        position: sticky;
        top: @spacing-lg;

        .avatar-preview {
            position: relative;
            display: inline-block;
            margin-bottom: @spacing-lg;
            cursor: pointer;

            .avatar-wrapper {
                width: 140px;
                height: 140px;
                border-radius: 50%;
                overflow: hidden;
                border: 4px solid var(--theme-secondary);
                box-shadow: 0 0 30px var(--theme-secondary);
                display: flex;
                align-items: center;
                justify-content: center;
                background: var(--theme-secondary);
                position: relative;
                z-index: 1;
                transition: all 0.3s ease;

                .avatar-img {
                    width: 100%;
                    height: 100%;
                    object-fit: cover;
                }

                .avatar-placeholder {
                    font-size: 56px;
                    font-weight: 900;
                    color: var(--theme-accent);
                    text-shadow: 0 0 20px var(--theme-accent);
                }
            }

            .avatar-glow {
                position: absolute;
                top: 50%;
                left: 50%;
                transform: translate(-50%, -50%);
                width: 160px;
                height: 160px;
                background: radial-gradient(circle, var(--theme-secondary) 0%, transparent 70%);
                opacity: 0.4;
                border-radius: 50%;
                animation: pulse-glow 2s ease-in-out infinite;
            }

            // 悬停编辑覆盖层
            .edit-overlay {
                position: absolute;
                top: 0;
                left: 0;
                right: 0;
                bottom: 0;
                border-radius: 50%;
                background: rgba(0, 0, 0, 0.6);
                display: flex;
                flex-direction: column;
                align-items: center;
                justify-content: center;
                gap: @spacing-xs;
                z-index: 2;
                animation: fade-in 0.2s ease;

                i {
                    font-size: 28px;
                    color: var(--theme-accent);
                }

                span {
                    font-size: @font-size-sm;
                    color: var(--theme-text-primary);
                }
            }

            &:hover .avatar-wrapper {
                border-color: var(--theme-accent);
                transform: scale(1.02);
            }
        }

        .user-name {
            font-size: @font-size-xl;
            font-weight: 700;
            color: var(--theme-text-primary);
            margin: 0 0 @spacing-xs;
        }

        .user-role {
            color: var(--theme-secondary);
            font-size: @font-size-sm;
            margin: 0;
            display: flex;
            align-items: center;
            justify-content: center;
            gap: @spacing-xs;

            i {
                font-size: 14px;
            }
        }
    }
}

// 右侧表单
.profile-main {
    display: flex;
    flex-direction: column;
    gap: @spacing-xl;
}

.form-section {
    background: var(--theme-bg-card);
    border: 1px solid var(--theme-border);
    border-radius: @border-radius-xl;
    padding: @spacing-xl;

    .section-header {
        display: flex;
        align-items: center;
        gap: @spacing-sm;
        margin-bottom: @spacing-xl;
        padding-bottom: @spacing-md;
        border-bottom: 1px solid var(--theme-border);

        i {
            font-size: 20px;
            color: var(--theme-accent);
        }

        h2 {
            font-size: @font-size-lg;
            font-weight: 600;
            color: var(--theme-text-primary);
            margin: 0;
        }
    }
}

.form-grid {
    display: grid;
    grid-template-columns: repeat(2, 1fr);
    gap: @spacing-lg;

    @media (max-width: @breakpoint-md) {
        grid-template-columns: 1fr;
    }

    .form-group {
        &.full-width {
            grid-column: 1 / -1;
        }
    }
}

.form-group {
    label {
        display: flex;
        align-items: center;
        gap: @spacing-xs;
        font-size: @font-size-sm;
        color: var(--theme-text-secondary);
        margin-bottom: @spacing-sm;

        i {
            color: var(--theme-secondary);
        }
    }

    .input-wrapper {
        display: flex;
        align-items: center;
        background: var(--theme-bg-hover);
        border: 1px solid var(--theme-border);
        border-radius: @border-radius-md;
        overflow: hidden;
        transition: all 0.2s ease;

        &:focus-within {
            border-color: var(--theme-secondary);
            box-shadow: 0 0 15px var(--theme-secondary);
        }

        &.input-disabled {
            opacity: 0.6;
            background: var(--theme-bg-card);
        }

        input {
            flex: 1;
            background: transparent;
            border: none;
            outline: none;
            padding: @spacing-md @spacing-lg;
            font-size: @font-size-md;
            color: var(--theme-text-primary);

            &::placeholder {
                color: var(--theme-text-secondary);
            }

            &:disabled {
                color: var(--theme-text-secondary);
                cursor: not-allowed;
            }
        }

        .btn-toggle {
            padding: @spacing-sm @spacing-md;
            background: transparent;
            border: none;
            color: var(--theme-text-secondary);
            cursor: pointer;
            transition: all 0.2s ease;

            &:hover {
                color: var(--theme-secondary);
                background: var(--theme-secondary);
                opacity: 0.1;
            }

            &.btn-eye:hover {
                color: var(--theme-accent);
            }
        }
    }
    .field-error {
        display: flex;
        align-items: center;
        gap: 4px;
        margin-top: 6px;
        font-size: @font-size-xs;
        color: var(--theme-danger);

        i {
            font-size: 12px;
        }
    }
}

.password-tips {
    display: flex;
    gap: @spacing-lg;
    margin-top: @spacing-md;

    .tip {
        display: flex;
        align-items: center;
        gap: @spacing-xs;
        font-size: @font-size-xs;
        color: var(--theme-text-secondary);

        i {
            font-size: 12px;
        }

        &.active {
            color: var(--theme-success);
        }
    }
}

// 提交按钮
.form-actions {
    display: flex;
    justify-content: flex-end;
    gap: @spacing-md;
    padding-top: @spacing-lg;

    button {
        display: flex;
        align-items: center;
        gap: @spacing-sm;
        padding: @spacing-md @spacing-2xl;
        font-size: @font-size-md;
        font-weight: 600;
        border-radius: @border-radius-md;
        cursor: pointer;
        transition: all 0.2s ease;
    }

    .btn-reset {
        background: transparent;
        border: 1px solid var(--theme-border);
        color: var(--theme-text-secondary);

        &:hover {
            border-color: var(--theme-secondary);
            color: var(--theme-secondary);
        }
    }

    .btn-save {
        background: var(--theme-secondary);
        border: none;
        color: var(--theme-accent);
        box-shadow: 0 4px 20px var(--theme-secondary);

        &:hover:not(:disabled) {
            transform: translateY(-2px);
            box-shadow: 0 6px 30px var(--theme-secondary);
        }

        &:disabled {
            opacity: 0.5;
            cursor: not-allowed;
        }

        .loading-spinner {
            width: 14px;
            height: 14px;
            border: 2px solid transparent;
            border-top-color: var(--theme-accent);
            border-radius: 50%;
            animation: spin 0.8s linear infinite;
        }
    }
}

// 头像上传弹窗样式
.avatar-upload-content {
    display: flex;
    flex-direction: column;
    align-items: center;
    gap: @spacing-xl;
}

.upload-area-wrapper {
    width: 100%;
    display: flex;
    justify-content: center;
}

.avatar-uploader {
    width: 100%;
    max-width: 400px;

    :deep(.el-upload) {
        border: 2px dashed var(--theme-secondary);
        border-radius: @border-radius-lg;
        cursor: pointer;
        position: relative;
        overflow: hidden;
        transition: all 0.3s ease;
        width: 100%;
        display: flex;
        justify-content: center;

        &:hover {
            border-color: var(--theme-secondary);
        }
    }

    :deep(.el-upload-dragger) {
        background: var(--theme-bg-hover);
        border: none;
        padding: @spacing-2xl @spacing-xl;
        width: 100%;
        display: flex;
        flex-direction: column;
        align-items: center;
        justify-content: center;

        &:hover {
            background: var(--theme-bg-card);
        }
    }

    :deep(.el-icon-upload) {
        font-size: 48px;
        color: var(--theme-secondary);
        margin-bottom: @spacing-md;
    }

    :deep(.el-upload__text) {
        color: var(--theme-text-secondary);
        font-size: @font-size-sm;
        text-align: center;

        em {
            color: var(--theme-accent);
            font-style: normal;
        }
    }

    :deep(.el-upload__tip) {
        color: var(--theme-text-secondary);
        font-size: @font-size-xs;
        margin-top: @spacing-sm;
        text-align: center;
    }
}

// 已有头像时的编辑区域
.existing-avatar-section {
    display: flex;
    flex-direction: column;
    align-items: center;
    gap: @spacing-xl;
    padding: @spacing-xl;
    background: var(--theme-bg-hover);
    border: 1px solid var(--theme-border);
    border-radius: @border-radius-lg;
}

.current-avatar-preview {
    .preview-avatar-img {
        width: 120px;
        height: 120px;
        border-radius: 50%;
        object-fit: cover;
        border: 4px solid var(--theme-secondary);
        box-shadow: 0 0 30px var(--theme-secondary);
    }
}

.avatar-actions {
    display: flex;
    gap: @spacing-md;
}

.avatar-uploader-inline {
    :deep(.el-upload) {
        cursor: pointer;
    }
}

.btn-change-avatar {
    display: flex;
    align-items: center;
    gap: @spacing-xs;
    padding: @spacing-sm @spacing-xl;
    background: var(--theme-secondary);
    border: none;
    border-radius: @border-radius-md;
    color: var(--theme-accent);
    font-size: @font-size-sm;
    font-weight: 600;
    cursor: pointer;
    transition: all 0.2s ease;

    i {
        font-size: 14px;
    }

    &:hover {
        transform: translateY(-2px);
        box-shadow: 0 4px 20px var(--theme-secondary);
    }
}

.btn-delete-avatar {
    display: flex;
    align-items: center;
    gap: @spacing-xs;
    padding: @spacing-sm @spacing-xl;
    background: transparent;
    border: 1px solid var(--theme-danger);
    border-radius: @border-radius-md;
    color: var(--theme-danger);
    font-size: @font-size-sm;
    font-weight: 600;
    cursor: pointer;
    transition: all 0.2s ease;

    i {
        font-size: 14px;
    }

    &:hover {
        background: var(--theme-danger);
        border-color: var(--theme-danger);
        opacity: 0.1;
        transform: translateY(-2px);
    }
}

// 已选择图片预览区域
.selected-avatar-section {
    display: flex;
    flex-direction: column;
    align-items: center;
    gap: @spacing-xl;
    padding: @spacing-xl;
    background: var(--theme-bg-hover);
    border: 1px solid var(--theme-border);
    border-radius: @border-radius-lg;
}

// 弹窗底部按钮
.dialog-footer-buttons {
    display: flex;
    justify-content: center;
    align-items: center;
    gap: @spacing-md;
}

.btn-dialog-cancel {
    padding: @spacing-sm @spacing-xl;
    background: transparent;
    border: 1px solid var(--theme-border);
    border-radius: @border-radius-md;
    color: var(--theme-text-secondary);
    cursor: pointer;
    transition: all 0.2s ease;

    &:hover {
        border-color: var(--theme-secondary);
        color: var(--theme-secondary);
    }
}

.btn-dialog-reupload {
    padding: @spacing-sm @spacing-xl;
    background: transparent;
    border: 1px solid var(--theme-info);
    border-radius: @border-radius-md;
    color: var(--theme-info);
    cursor: pointer;
    transition: all 0.2s ease;

    &:hover {
        border-color: var(--theme-info);
        background: var(--theme-info);
        opacity: 0.1;
    }
}

.btn-dialog-confirm {
    padding: @spacing-sm @spacing-xl;
    background: var(--theme-secondary);
    border: none;
    border-radius: @border-radius-md;
    color: var(--theme-accent);
    font-weight: 600;
    cursor: pointer;
    transition: all 0.2s ease;

    &:hover:not(:disabled) {
        transform: translateY(-2px);
        box-shadow: 0 4px 20px var(--theme-secondary);
    }

    &:disabled {
        opacity: 0.5;
        cursor: not-allowed;
    }
}

// 动画
@keyframes pulse-glow {
    0%, 100% {
        transform: translate(-50%, -50%) scale(1);
        opacity: 0.6;
    }
    50% {
        transform: translate(-50%, -50%) scale(1.1);
        opacity: 0.8;
    }
}

@keyframes fade-in {
    from {
        opacity: 0;
    }
    to {
        opacity: 1;
    }
}

@keyframes spin {
    to {
        transform: rotate(360deg);
    }
}

// 头像上传弹窗 - DNF风格覆盖
::v-deep .avatar-dialog {
    .el-dialog {
        background: @dnf-bg-card !important;
        border: 1px solid @dnf-secondary-purple !important;
        border-radius: @border-radius-xl !important;
        box-shadow: 0 0 50px rgba(123, 47, 255, 0.4) !important;
        overflow: hidden;

        &__header {
            background: linear-gradient(135deg, rgba(123, 47, 255, 0.2) 0%, rgba(0, 180, 255, 0.1) 100%) !important;
            border-bottom: 1px solid rgba(123, 47, 255, 0.3) !important;
            padding: @spacing-xl !important;
            position: relative;

            &::before {
                content: '';
                position: absolute;
                top: 0;
                left: 0;
                right: 0;
                height: 2px;
                background: linear-gradient(90deg, @dnf-secondary-purple, @dnf-primary-gold, @dnf-primary-blue);
            }

            .el-dialog__title {
                color: @dnf-primary-gold !important;
                font-size: @font-size-lg !important;
                font-weight: 700 !important;
                font-family: 'Orbitron', sans-serif;
            }

            .el-dialog__headerbtn {
                position: absolute !important;
                top: 12px !important;
                right: 16px !important;

                .el-dialog__close {
                    color: @dnf-text-muted !important;
                    font-size: 16px !important;

                    &:hover {
                        color: @dnf-danger !important;
                        transform: rotate(90deg);
                    }
                }
            }
        }

        &__body {
            background: @dnf-bg-card !important;
            padding: @spacing-xl !important;
            color: @dnf-text-secondary !important;
        }

        &__footer {
            background: rgba(13, 21, 37, 0.5) !important;
            border-top: 1px solid rgba(123, 47, 255, 0.2) !important;
            padding: @spacing-lg @spacing-xl !important;
        }
    }
}
</style>
