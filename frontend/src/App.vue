<!--
 * @Description: 应用根组件
 * @Author: System
 * @Date: 2026-04-10
 * @LastEditTime: 2026-04-10 17:19:46
 * @FilePath: /vue2-java-mysql-project/frontend/src/App.vue
 -->
<template>
    <div
        id="app"
        class="dnf-theme"
    >
        <animated-router />
    </div>
</template>

<script>
import AnimatedRouter from '@/components/AnimatedRouter.vue'

export default {
    name: 'App',
    components: {
        AnimatedRouter
    },
    mounted() {
        // 添加全局样式类
        document.body.classList.add('dnf-theme')
        document.documentElement.classList.add('dnf-theme')

        // 读取页面动画设置
        const pageAnimations = localStorage.getItem('pageAnimations')
        if (pageAnimations === 'false') {
            document.body.classList.add('no-animations')
        }
    },
    beforeDestroy() {
        // 清理全局样式类
        document.body.classList.remove('dnf-theme')
        document.documentElement.classList.remove('dnf-theme')
    }
}
</script>

<style lang="less">
// Google Fonts 已在 index.html 中引入

// 导入全局样式
@import '@/assets/styles/index.less';

#app {
    font-family: 'Quicksand', -apple-system, BlinkMacSystemFont, 'Segoe UI', Roboto, sans-serif;
    -webkit-font-smoothing: antialiased;
    -moz-osx-font-smoothing: grayscale;
    font-feature-settings: 'liga' 1, 'kern' 1;
    height: 100vh;
    overflow: hidden;
    position: relative;

    // 添加主题背景装饰
    &::before {
        content: '';
        position: fixed;
        top: 0;
        left: 0;
        right: 0;
        bottom: 0;
        background:
            radial-gradient(circle at 20% 80%, var(--theme-secondary) 0%, transparent 50%),
            radial-gradient(circle at 80% 20%, var(--theme-accent) 0%, transparent 50%),
            var(--theme-bg-dark);
        opacity: 0.1;
        z-index: -1;
    }

    // 添加粒子效果装饰
    &::after {
        content: '';
        position: fixed;
        top: 0;
        left: 0;
        right: 0;
        bottom: 0;
        background-image:
            radial-gradient(2px 2px at 20px 30px, var(--theme-accent) 1px, transparent 0),
            radial-gradient(2px 2px at 40px 70px, var(--theme-secondary) 1px, transparent 0),
            radial-gradient(1px 1px at 60px 20px, var(--theme-text-primary) 1px, transparent 0),
            radial-gradient(1px 1px at 80px 50px, var(--theme-info) 1px, transparent 0),
            radial-gradient(2px 2px at 90px 80px, var(--theme-accent) 1px, transparent 0);
        background-size: 100px 100px;
        z-index: -1;
        opacity: 0.3;
        animation: mist-flow 20s linear infinite;
    }
}

// 全局滚动条样式
html,
body {
    .custom-scrollbar();
    margin: 0;
    padding: 0;
    overflow-x: hidden;
}

// 全局动画定义
@keyframes mist-flow {
    0% {
        background-position: 0 0;
    }

    100% {
        background-position: 100px 100px;
    }
}

// 减少动画支持
@media (prefers-reduced-motion: reduce) {
    #app::after {
        animation: none;
    }
}
</style>