<!--
 * @Description: 无权限页面
 * @Author: Claude Code
 * @Date: 2026-04-14
-->
<template>
    <div class="no-permission-page">
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
            <!-- 警告图标 -->
            <div class="warning-icon">
                <i class="el-icon-warning-outline" />
            </div>

            <!-- 标题 -->
            <h1 class="title">
                权限不足
            </h1>
            <p class="subtitle">
                你没有权限访问该页面
            </p>

            <!-- 提示信息 -->
            <div class="tips">
                <p>请联系管理员获取相应权限</p>
                <p class="hint">
                    或者返回首页继续浏览
                </p>
            </div>

            <!-- 按钮组 -->
            <div class="button-group">
                <el-button
                    type="primary"
                    class="back-btn"
                    @click="goBack"
                >
                    返回首页
                </el-button>
                <el-button
                    class="logout-btn"
                    @click="handleLogout"
                >
                    退出登录
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
    name: 'NoPermission',
    methods: {
        goBack() {
            this.$router.push('/')
        },
        handleLogout() {
            this.$store.dispatch('auth/logout')
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

.no-permission-page {
    position: fixed;
    top: 0;
    left: 0;
    width: 100%;
    height: 100%;
    display: flex;
    align-items: center;
    justify-content: center;
    background: linear-gradient(135deg, #1a0a1a 0%, #2a1a2e 50%, #1a1a3e 100%);
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
        background: linear-gradient(135deg, #ff4444, #ff8800, #ffcc00);
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
        background: #ff4444;
        top: -100px;
        right: -100px;
        animation: pulse 4s ease-in-out infinite;
    }

    .glow-2 {
        width: 300px;
        height: 300px;
        background: #ff8800;
        bottom: -50px;
        left: -50px;
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
    background: rgba(40, 20, 50, 0.9);
    border: 2px solid rgba(255, 68, 68, 0.5);
    border-radius: 20px;
    box-shadow: 0 0 60px rgba(255, 68, 68, 0.3),
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

// 警告图标
.warning-icon {
    font-size: 80px;
    color: #ff4444;
    margin-bottom: 24px;
    animation: pulse-icon 2s ease-in-out infinite;

    i {
        text-shadow: 0 0 30px rgba(255, 68, 68, 0.8);
    }
}

@keyframes pulse-icon {
    0%,
    100% {
        transform: scale(1);
        filter: brightness(1);
    }

    50% {
        transform: scale(1.1);
        filter: brightness(1.2);
    }
}

// 标题
.title {
    font-size: 36px;
    font-weight: 700;
    color: #ffffff;
    margin: 0 0 12px;
    text-shadow: 0 0 20px rgba(255, 68, 68, 0.5);
}

.subtitle {
    font-size: 18px;
    color: rgba(255, 255, 255, 0.7);
    margin: 0 0 32px;
}

// 提示信息
.tips {
    margin-bottom: 36px;

    p {
        margin: 8px 0;
        font-size: 14px;
        color: rgba(255, 255, 255, 0.6);
    }

    .hint {
        font-size: 12px;
        color: rgba(255, 255, 255, 0.4);
    }
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

    .logout-btn {
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
            border-color: #ff4444;
            color: #ff4444;
            background: rgba(255, 68, 68, 0.1);
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
