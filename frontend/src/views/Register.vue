<!--
 * @Description: 注册页面 - DNF霓虹风格
 * @Author: Claude Code
 * @Date: 2026-04-14
-->
<template>
    <div class="register-page">
        <!-- 背景粒子效果 -->
        <div class="particles">
            <div
                v-for="i in 20"
                :key="i"
                class="particle"
                :style="getParticleStyle(i)"
            />
        </div>

        <!-- 背景装饰 -->
        <div class="bg-decoration">
            <div class="glow glow-1" />
            <div class="glow glow-2" />
            <div class="glow glow-3" />
        </div>

        <!-- 注册卡片 -->
        <div class="register-card">
            <!-- Logo区域 -->
            <div class="logo-area">
                <div class="logo">
                    <span class="logo-d">D</span>
                    <span class="logo-n">N</span>
                    <span class="logo-f">F</span>
                </div>
                <h1 class="title">
                    冒险者注册
                </h1>
                <p class="subtitle">
                    开启你的阿拉德冒险之旅
                </p>
            </div>
            <!-- 注册表单 -->
            <el-form
                ref="registerForm"
                :model="registerForm"
                :rules="rules"
                class="register-form"
                @submit.native.prevent="handleRegister"
            >
                <el-form-item prop="username">
                    <div class="input-wrapper">
                        <i class="el-icon-user" />
                        <el-input
                            v-model="registerForm.username"
                            placeholder="请输入用户名（3-20字符）"
                            prefix-icon="none"
                            autocomplete="off"
                        />
                    </div>
                </el-form-item>

                <el-form-item prop="phone">
                    <div class="input-wrapper">
                        <i class="el-icon-mobile-phone" />
                        <el-input
                            v-model="registerForm.phone"
                            placeholder="请输入手机号"
                            prefix-icon="none"
                            autocomplete="off"
                        />
                    </div>
                </el-form-item>

                <el-form-item prop="password">
                    <div class="input-wrapper">
                        <i class="el-icon-lock" />
                        <el-input
                            v-model="registerForm.password"
                            :type="showPassword ? 'text' : 'password'"
                            placeholder="请输入密码（至少6位）"
                            prefix-icon="none"
                            autocomplete="off"
                        >
                            <i
                                slot="suffix"
                                :class="showPassword ? 'el-icon-view' : 'el-icon-close'"
                                class="password-toggle"
                                @click="showPassword = !showPassword"
                            />
                        </el-input>
                    </div>
                </el-form-item>

                <el-form-item prop="confirmPassword">
                    <div class="input-wrapper">
                        <i class="el-icon-lock" />
                        <el-input
                            v-model="registerForm.confirmPassword"
                            :type="showPassword ? 'text' : 'password'"
                            placeholder="请确认密码"
                            prefix-icon="none"
                            autocomplete="off"
                        />
                    </div>
                </el-form-item>

                <el-form-item>
                    <el-button
                        type="primary"
                        :loading="loading"
                        class="register-btn"
                        native-type="submit"
                    >
                        <span v-if="!loading">注册</span>
                        <span v-else>注册中...</span>
                    </el-button>
                </el-form-item>
            </el-form>

            <!-- 登录链接 -->
            <div class="login-link">
                <span>已有账号？</span>
                <router-link to="/login">
                    立即登录
                </router-link>
            </div>

            <!-- 提示信息 -->
            <div class="tips">
                <i class="el-icon-info" />
                <span>注册成功后，你将获得"普通冒险者"身份</span>
            </div>
        </div>

        <!-- 底部信息 -->
        <div class="footer-info">
            <p>DNF管理系统 - 地下城与勇士</p>
        </div>
    </div>
</template>

<script>
import api from '@/api'

export default {
    name: 'Register',
    data() {
        // 验证确认密码
        const validateConfirmPassword = (rule, value, callback) => {
            if (value !== this.registerForm.password) {
                callback(new Error('两次输入的密码不一致'))
            } else {
                callback()
            }
        }

        return {
            registerForm: {
                username: '',
                phone: '',
                password: '',
                confirmPassword: ''
            },
            rules: {
                username: [
                    { required: true, message: '请输入用户名', trigger: 'blur' },
                    { min: 2, max: 20, message: '用户名长度在2-20个字符之间', trigger: 'blur' }
                ],
                phone: [
                    { required: true, message: '请输入手机号', trigger: 'blur' },
                    { pattern: /^1[3-9]\d{9}$/, message: '请输入正确的手机号', trigger: 'blur' }
                ],
                password: [
                    { required: true, message: '请输入密码', trigger: 'blur' },
                    { min: 6, message: '密码长度至少6位', trigger: 'blur' }
                ],
                confirmPassword: [
                    { required: true, message: '请确认密码', trigger: 'blur' },
                    { validator: validateConfirmPassword, trigger: 'blur' }
                ]
            },
            loading: false,
            showPassword: false
        }
    },
    methods: {
        async handleRegister() {
            this.$refs.registerForm.validate(async valid => {
                if (!valid) return

                this.loading = true
                try {
                    await this.$request({
                        url: api.auth.register,
                        method: 'post',
                        data: {
                            username: this.registerForm.username,
                            phone: this.registerForm.phone,
                            password: this.registerForm.password
                        }
                    })

                    this.$message.success('注册成功！请登录')
                    this.$router.push('/login')
                } catch (error) {
                    console.error('注册失败:', error)
                    this.$message.error(error.message || '注册失败，请稍后重试')
                } finally {
                    this.loading = false
                }
            })
        },
        getParticleStyle() {
            const size = Math.random() * 6 + 2
            return {
                width: size + 'px',
                height: size + 'px',
                left: Math.random() * 100 + '%',
                top: Math.random() * 100 + '%',
                animationDelay: Math.random() * 5 + 's',
                animationDuration: (Math.random() * 10 + 10) + 's'
            }
        }
    }
}
</script>

<style lang="less" scoped>
@import '@/assets/styles/variables.less';

.register-page {
    position: fixed;
    top: 0;
    left: 0;
    width: 100%;
    height: 100%;
    display: flex;
    align-items: center;
    justify-content: center;
    background: linear-gradient(135deg, #0a0a1a 0%, #1a1a2e 50%, #16213e 100%);
    overflow: hidden;
}

// 粒子效果
.particles {
    position: absolute;
    top: 0;
    left: 0;
    width: 100%;
    height: 100%;
    overflow: hidden;
    pointer-events: none;

    .particle {
        position: absolute;
        background: linear-gradient(135deg, #7b2fff, #00b4ff, #ffd700);
        border-radius: 50%;
        opacity: 0.6;
        animation: float-particle linear infinite;
    }
}

@keyframes float-particle {
    0% {
        transform: translateY(100vh) scale(0);
        opacity: 0;
    }

    10% {
        opacity: 0.6;
    }

    90% {
        opacity: 0.6;
    }

    100% {
        transform: translateY(-100vh) scale(1);
        opacity: 0;
    }
}

// 背景装饰
.bg-decoration {
    position: absolute;
    top: 0;
    left: 0;
    width: 100%;
    height: 100%;
    pointer-events: none;

    .glow {
        position: absolute;
        border-radius: 50%;
        filter: blur(100px);
        opacity: 0.3;
    }

    .glow-1 {
        width: 400px;
        height: 400px;
        background: #ffd700;
        top: -100px;
        right: -100px;
        animation: pulse 4s ease-in-out infinite;
    }

    .glow-2 {
        width: 300px;
        height: 300px;
        background: #7b2fff;
        bottom: -50px;
        left: -50px;
        animation: pulse 4s ease-in-out infinite 2s;
    }

    .glow-3 {
        width: 200px;
        height: 200px;
        background: #00b4ff;
        top: 50%;
        left: 50%;
        transform: translate(-50%, -50%);
        animation: pulse 4s ease-in-out infinite 1s;
    }
}

@keyframes pulse {

    0%,
    100% {
        opacity: 0.2;
        transform: scale(1);
    }

    50% {
        opacity: 0.4;
        transform: scale(1.1);
    }
}

// 注册卡片
.register-card {
    position: relative;
    width: 440px;
    padding: 48px 40px;
    background: rgba(26, 39, 68, 0.9);
    border: 1px solid rgba(255, 215, 0, 0.3);
    border-radius: 20px;
    box-shadow: 0 0 60px rgba(255, 215, 0, 0.2),
        0 20px 60px rgba(0, 0, 0, 0.5);
    backdrop-filter: blur(10px);

    &::before {
        content: '';
        position: absolute;
        top: -1px;
        left: -1px;
        right: -1px;
        bottom: -1px;
        border-radius: 20px;
        background: linear-gradient(135deg, #ffd700, #ff8c00, #7b2fff, #ffd700);
        background-size: 300% 300%;
        z-index: -1;
        animation: gradient-rotate 3s linear infinite;
        opacity: 0.5;
    }
}

@keyframes gradient-rotate {
    0% {
        background-position: 0% 50%;
    }

    50% {
        background-position: 100% 50%;
    }

    100% {
        background-position: 0% 50%;
    }
}

// Logo区域
.logo-area {
    text-align: center;
    margin-bottom: 36px;

    .logo {
        font-size: 36px;
        font-weight: 900;
        letter-spacing: 4px;
        display: flex;
        justify-content: center;
        gap: 4px;
        margin-bottom: 16px;

        .logo-d {
            color: #7b2fff;
            text-shadow: 0 0 20px rgba(123, 47, 255, 0.8);
            animation: neon-pulse 2s ease-in-out infinite;
        }

        .logo-n {
            color: #00b4ff;
            text-shadow: 0 0 20px rgba(0, 180, 255, 0.8);
            animation: neon-pulse 2s ease-in-out infinite 0.3s;
        }

        .logo-f {
            color: #ffd700;
            text-shadow: 0 0 20px rgba(255, 215, 0, 0.8);
            animation: neon-pulse 2s ease-in-out infinite 0.6s;
        }
    }

    @keyframes neon-pulse {

        0%,
        100% {
            filter: brightness(1);
        }

        50% {
            filter: brightness(1.3);
        }
    }

    .title {
        font-size: 24px;
        font-weight: 600;
        color: #ffffff;
        margin: 0 0 8px;
        text-shadow: 0 0 20px rgba(255, 255, 255, 0.3);
    }

    .subtitle {
        font-size: 14px;
        color: rgba(255, 255, 255, 0.5);
        margin: 0;
    }
}

// 注册表单
.register-form {
    .input-wrapper {
        position: relative;
        display: flex;
        align-items: center;

        >i {
            position: absolute;
            left: 16px;
            color: #ffd700;
            font-size: 16px;
            z-index: 1;
        }

        ::v-deep .el-input {
            .el-input__inner {
                height: 48px;
                padding-left: 44px;
                background: rgba(13, 21, 37, 0.8);
                border: 2px solid rgba(255, 215, 0, 0.3);
                border-radius: 12px;
                color: #ffffff;
                font-size: 14px;
                transition: all 0.3s ease;

                &::placeholder {
                    color: rgba(255, 255, 255, 0.4);
                }

                &:focus {
                    border-color: #ffd700;
                    box-shadow: 0 0 20px rgba(255, 215, 0, 0.4);
                    outline: none;
                }
            }
        }

        .password-toggle {
            cursor: pointer;
            color: rgba(255, 255, 255, 0.5);
            transition: color 0.3s ease;
            margin-right: 12px;

            &:hover {
                color: #ffd700;
            }
        }
    }

    ::v-deep .el-form-item {
        margin-bottom: 20px;

        .el-form-item__error {
            color: #ff6b6b;
            font-size: 12px;
            padding-top: 4px;
        }
    }

    .register-btn {
        width: 100%;
        height: 48px;
        font-size: 16px;
        font-weight: 600;
        border: none;
        border-radius: 12px;
        background: linear-gradient(135deg, #ffd700 0%, #ff8c00 100%);
        color: #1a1a2e;
        cursor: pointer;
        transition: all 0.3s ease;
        box-shadow: 0 4px 20px rgba(255, 215, 0, 0.4);

        &:hover {
            background: linear-gradient(135deg, #ffe033 0%, #ffa31a 100%);
            box-shadow: 0 6px 30px rgba(255, 215, 0, 0.6);
            transform: translateY(-2px);
        }

        &:active {
            transform: translateY(0);
        }
    }
}

// 登录链接
.login-link {
    text-align: center;
    margin-top: 16px;
    font-size: 14px;
    color: rgba(255, 255, 255, 0.6);

    a {
        color: #7b2fff;
        text-decoration: none;
        margin-left: 8px;
        transition: all 0.3s ease;
        font-weight: 600;

        &:hover {
            color: #9a4fff;
            text-shadow: 0 0 10px rgba(123, 47, 255, 0.6);
        }
    }
}

// 提示信息
.tips {
    display: flex;
    align-items: center;
    justify-content: center;
    gap: 8px;
    margin-top: 24px;
    padding: 12px;
    background: rgba(123, 47, 255, 0.1);
    border: 1px solid rgba(123, 47, 255, 0.3);
    border-radius: 8px;

    i {
        color: #7b2fff;
        font-size: 16px;
    }

    span {
        font-size: 12px;
        color: rgba(255, 255, 255, 0.7);
    }
}

// 底部信息
.footer-info {
    position: absolute;
    bottom: 24px;
    text-align: center;

    p {
        margin: 0;
        font-size: 12px;
        color: rgba(255, 255, 255, 0.3);
    }
}
</style>
