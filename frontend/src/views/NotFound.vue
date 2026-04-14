<!--
 * @Description: 404 页面
 * @Author: Claude Code
 * @Date: 2026-04-14
-->
<template>
    <div class="not-found-page">
        <!-- 背景粒子效果 -->
        <div class="particles">
            <div
                v-for="i in 15"
                :key="i"
                class="particle"
                :style="getParticleStyle(i)"
            />
        </div>

        <!-- 背景装饰 -->
        <div class="bg-decoration">
            <div class="glow glow-1" />
            <div class="glow glow-2" />
        </div>

        <!-- 内容区域 -->
        <div class="content">
            <!-- 404 图标 -->
            <div class="error-icon">
                <span class="error-code">404</span>
            </div>

            <!-- 标题 -->
            <h1 class="title">
                页面不存在
            </h1>
            <p class="subtitle">
                你访问的页面可能已被删除或地址错误
            </p>

            <!-- 按钮组 -->
            <div class="button-group">
                <el-button
                    type="primary"
                    class="back-btn"
                    @click="goHome"
                >
                    返回首页
                </el-button>
                <el-button
                    class="login-btn"
                    @click="goLogin"
                >
                    返回登录
                </el-button>
            </div>
        </div>

        <!-- 底部信息 -->
        <div class="footer-info">
            <p>DNF管理系统 - 地下城与勇士</p>
        </div>
    </div>
</template>

<script>
export default {
    name: 'NotFound',
    methods: {
        goHome() {
            // 如果已登录，返回首页；否则返回登录页
            if (this.$store.state.auth.token) {
                this.$router.push('/')
            } else {
                this.$router.push('/login')
            }
        },
        goLogin() {
            this.$router.push('/login')
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

.not-found-page {
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
        background: #00b4ff;
        bottom: -50px;
        right: -50px;
        animation: pulse 4s ease-in-out infinite 2s;
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

// 内容区域
.content {
    position: relative;
    text-align: center;
    padding: 60px 80px;
    background: rgba(26, 39, 68, 0.9);
    border: 2px solid rgba(123, 47, 255, 0.5);
    border-radius: 20px;
    box-shadow: 0 0 60px rgba(123, 47, 255, 0.3),
        0 20px 60px rgba(0, 0, 0, 0.5);
    backdrop-filter: blur(10px);
    animation: fadeIn 0.5s ease-out;
}

@keyframes fadeIn {
    from {
        opacity: 0;
        transform: translateY(20px);
    }

    to {
        opacity: 1;
        transform: translateY(0);
    }
}

// 404 图标
.error-icon {
    margin-bottom: 24px;

    .error-code {
        font-size: 100px;
        font-weight: 900;
        background: linear-gradient(135deg, #7b2fff, #00b4ff, #ffd700);
        -webkit-background-clip: text;
        -webkit-text-fill-color: transparent;
        background-clip: text;
        text-shadow: 0 0 40px rgba(123, 47, 255, 0.5);
        animation: pulse-icon 2s ease-in-out infinite;
    }
}

@keyframes pulse-icon {
    0%,
    100% {
        filter: brightness(1);
    }

    50% {
        filter: brightness(1.2);
    }
}

// 标题
.title {
    font-size: 36px;
    font-weight: 700;
    color: #ffffff;
    margin: 0 0 12px;
    text-shadow: 0 0 20px rgba(123, 47, 255, 0.5);
}

.subtitle {
    font-size: 16px;
    color: rgba(255, 255, 255, 0.6);
    margin: 0 0 36px;
}

// 按钮组
.button-group {
    display: flex;
    gap: 16px;
    justify-content: center;

    .back-btn {
        padding: 12px 32px;
        font-size: 14px;
        font-weight: 600;
        border: none;
        border-radius: 8px;
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
    }

    .login-btn {
        padding: 12px 32px;
        font-size: 14px;
        font-weight: 600;
        border: 2px solid rgba(255, 255, 255, 0.3);
        border-radius: 8px;
        background: transparent;
        color: rgba(255, 255, 255, 0.7);
        cursor: pointer;
        transition: all 0.3s ease;

        &:hover {
            border-color: #7b2fff;
            color: #7b2fff;
            background: rgba(123, 47, 255, 0.1);
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
