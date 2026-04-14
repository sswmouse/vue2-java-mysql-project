<!--
 * @Description: 登录页面 - DNF霓虹风格
 * @Author: Claude Code
 * @Date: 2026-04-14
-->
<template>
    <div class="login-page">
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

        <!-- 登录卡片 -->
        <div class="login-card">
            <!-- Logo区域 -->
            <div class="logo-area">
                <div class="logo">
                    <span class="logo-d">D</span>
                    <span class="logo-n">N</span>
                    <span class="logo-f">F</span>
                </div>
                <h1 class="title">
                    冒险者登录
                </h1>
                <p class="subtitle">
                    欢迎来到阿拉德大陆
                </p>
            </div>

            <!-- 登录表单 -->
            <el-form
                ref="loginForm"
                :model="loginForm"
                :rules="rules"
                class="login-form"
                @submit.native.prevent="handleLogin"
            >
                <el-form-item prop="username">
                    <div class="input-wrapper">
                        <i class="el-icon-user" />
                        <el-input
                            v-model="loginForm.username"
                            placeholder="请输入用户名"
                            prefix-icon="none"
                            autocomplete="off"
                        />
                    </div>
                </el-form-item>

                <el-form-item prop="password">
                    <div class="input-wrapper">
                        <i class="el-icon-lock" />
                        <el-input
                            v-model="loginForm.password"
                            :type="showPassword ? 'text' : 'password'"
                            placeholder="请输入密码"
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

                <el-form-item>
                    <div class="form-options">
                        <el-checkbox v-model="rememberUsername">
                            记住用户名
                        </el-checkbox>
                        <el-checkbox v-model="rememberPassword">
                            记住密码
                        </el-checkbox>
                    </div>
                </el-form-item>

                <el-form-item>
                    <el-button
                        type="primary"
                        :loading="loading"
                        class="login-btn"
                        native-type="submit"
                    >
                        <span v-if="!loading">登录</span>
                        <span v-else>登录中...</span>
                    </el-button>
                </el-form-item>
            </el-form>

            <!-- 注册链接 -->
            <div class="register-link">
                <span>还没有账号？</span>
                <router-link to="/register">
                    立即注册
                </router-link>
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
    name: 'Login',
    data() {
        return {
            loginForm: {
                username: '',
                password: ''
            },
            rules: {
                username: [
                    { required: true, message: '请输入用户名', trigger: 'blur' }
                ],
                password: [
                    { required: true, message: '请输入密码', trigger: 'blur' }
                ]
            },
            loading: false,
            showPassword: false,
            rememberUsername: localStorage.getItem('rememberUsername') === 'true',
            rememberPassword: localStorage.getItem('rememberPassword') === 'true'
        }
    },
    created() {
        // 恢复记住的用户名
        if (this.rememberUsername) {
            this.loginForm.username = localStorage.getItem('savedUsername') || ''
        }
        // 恢复记住的密码
        if (this.rememberPassword) {
            this.loginForm.password = localStorage.getItem('savedPassword') || ''
        }
    },
    methods: {
        async handleLogin() {
            this.$refs.loginForm.validate(async valid => {
                if (!valid) return

                this.loading = true
                try {
                    const response = await this.$request({
                        url: api.auth.login,
                        method: 'post',
                        data: this.loginForm
                    })

                    // 保存登录信息
                    this.saveLoginInfo()

                    // 登录成功，保存token和用户信息
                    const userData = {
                        id: response.userId,
                        username: response.username,
                        phone: response.phone,
                        role: response.role
                    }

                    // 使用Vuex auth模块登录
                    await this.$store.dispatch('auth/login', {
                        token: response.token,
                        user: userData
                    })

                    this.$message.success('登录成功，欢迎 ' + response.username + '！')
                    this.$router.push('/')
                } catch (error) {
                    console.error('登录失败:', error)
                    // 错误消息已在 http.js 拦截器中显示，这里不再重复显示
                } finally {
                    this.loading = false
                }
            })
        },
        saveLoginInfo() {
            if (this.rememberUsername) {
                localStorage.setItem('savedUsername', this.loginForm.username)
                localStorage.setItem('rememberUsername', 'true')
            } else {
                localStorage.removeItem('savedUsername')
                localStorage.removeItem('rememberUsername')
            }

            if (this.rememberPassword) {
                localStorage.setItem('savedPassword', this.loginForm.password)
                localStorage.setItem('rememberPassword', 'true')
            } else {
                localStorage.removeItem('savedPassword')
                localStorage.removeItem('rememberPassword')
            }
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

.login-page {
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
        background: #7b2fff;
        top: -100px;
        left: -100px;
        animation: pulse 4s ease-in-out infinite;
    }

    .glow-2 {
        width: 300px;
        height: 300px;
        background: #ffd700;
        bottom: -50px;
        right: -50px;
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

// 登录卡片
.login-card {
    position: relative;
    width: 420px;
    padding: 48px 40px;
    background: rgba(26, 39, 68, 0.9);
    border: 1px solid rgba(123, 47, 255, 0.3);
    border-radius: 20px;
    box-shadow: 0 0 60px rgba(123, 47, 255, 0.2),
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
        background: linear-gradient(135deg, #7b2fff, #ffd700, #00b4ff, #7b2fff);
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

// 登录表单
.login-form {
    .input-wrapper {
        position: relative;
        display: flex;
        align-items: center;

        >i {
            position: absolute;
            left: 16px;
            color: #7b2fff;
            font-size: 16px;
            z-index: 1;
        }

        ::v-deep .el-input {
            width: 100%;

            .el-input__inner {
                height: 48px;
                padding-left: 44px;
                padding-right: 44px;
                background: rgba(13, 21, 37, 0.8);
                border: 2px solid rgba(123, 47, 255, 0.3);
                border-radius: 12px;
                color: #ffffff;
                font-size: 14px;
                transition: all 0.3s ease;

                &::placeholder {
                    color: rgba(255, 255, 255, 0.4);
                }

                &:focus {
                    border-color: #7b2fff;
                    box-shadow: 0 0 20px rgba(123, 47, 255, 0.4);
                    outline: none;
                }
            }

            .el-input__suffix {
                right: 12px;
                display: flex;
                align-items: center;
            }

            .el-input__suffix-inner {
                display: flex;
                align-items: center;
            }
        }

        .password-toggle {
            cursor: pointer;
            color: rgba(255, 255, 255, 0.5);
            transition: color 0.3s ease;
            font-size: 16px;
            width: 24px;
            height: 24px;
            display: flex;
            align-items: center;
            justify-content: center;

            &:hover {
                color: #7b2fff;
            }
        }
    }

    ::v-deep .el-form-item {
        margin-bottom: 20px;
        position: relative;

        &:last-of-type {
            margin-bottom: 16px;
        }

        .el-form-item__error {
            color: #ff6b6b;
            font-size: 12px;
            padding-top: 4px;
            position: absolute;
            top: 100%;
            left: 0;
            z-index: 10;
        }
    }

    // 记住密码选项
    .form-options {
        display: flex;
        justify-content: space-between;
        align-items: center;
        padding: 0 4px;

        ::v-deep .el-checkbox {
            .el-checkbox__label {
                color: rgba(255, 255, 255, 0.6);
                font-size: 12px;
            }

            .el-checkbox__input.is-checked {
                .el-checkbox__inner {
                    background-color: #7b2fff;
                    border-color: #7b2fff;
                }
            }

            .el-checkbox__inner:hover {
                border-color: #7b2fff;
            }
        }
    }

    .login-btn {
        width: 100%;
        height: 48px;
        font-size: 16px;
        font-weight: 600;
        border: none;
        border-radius: 12px;
        background: linear-gradient(135deg, #7b2fff 0%, #5a1fd4 100%);
        color: #ffffff;
        cursor: pointer;
        transition: all 0.3s ease;
        box-shadow: 0 4px 20px rgba(123, 47, 255, 0.4);

        &:hover {
            background: linear-gradient(135deg, #8a3fff 0%, #6a2fe4 100%);
            box-shadow: 0 6px 30px rgba(123, 47, 255, 0.6);
            transform: translateY(-2px);
        }

        &:active {
            transform: translateY(0);
        }
    }
}

// 注册链接
.register-link {
    text-align: center;
    margin-top: 16px;
    font-size: 14px;
    color: rgba(255, 255, 255, 0.6);

    a {
        color: #ffd700;
        text-decoration: none;
        margin-left: 8px;
        transition: all 0.3s ease;
        font-weight: 600;

        &:hover {
            color: #ffe033;
            text-shadow: 0 0 10px rgba(255, 215, 0, 0.6);
        }
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
