<!--
 * @Description: 动画路由组件，提供页面切换动画
 * @Author: Claude Code
 * @Date: 2026-04-10
 * @LastEditTime: 2026-04-10
 * @FilePath: /frontend/src/components/AnimatedRouter.vue
 -->
<template>
    <transition
        :css="false"
        @before-enter="beforeEnter"
        @enter="enter"
        @after-enter="afterEnter"
        @leave="leave"
        @after-leave="afterLeave"
        mode="out-in"
    >
        <router-view />
    </transition>
</template>

<script>
import {
    pageFadeIn,
    pageFadeOut,
    pageSlideIn,
    pageZoomIn,
    safeAnimation,
    prefersReducedMotion
} from '@/utils/animations'

export default {
    name: 'AnimatedRouter',
    data() {
        return {
            animationType: 'fade',
            animationDirection: 'left',
            animationEnabled: true,
            lastScrollPosition: 0
        }
    },
    created() {
        // 监听路由变化，根据路由元信息设置动画类型
        this.$router.beforeEach((to, from, next) => {
            // 保存离开页面的滚动位置
            this.lastScrollPosition = window.scrollY

            // 根据路由深度判断动画方向
            const toDepth = to.matched.length
            const fromDepth = from.matched.length
            this.animationDirection = toDepth > fromDepth ? 'right' : 'left'

            // 检查路由元信息中的动画设置
            if (to.meta && to.meta.animation) {
                this.animationType = to.meta.animation
            } else {
                // 默认根据路由变化选择动画
                this.animationType = this.getDefaultAnimation(to, from)
            }

            next()
        })

        // 检查是否支持减少动画
        this.animationEnabled = !prefersReducedMotion()
    },
    methods: {
        getDefaultAnimation(to, from) {
            // 根据路由路径决定默认动画
            const toPath = to.path
            const fromPath = from.path

            // 首页使用缩放动画
            if (toPath === '/' || fromPath === '/') {
                return 'zoom'
            }

            // 深层次路由使用滑动动画
            if (toPath.split('/').length > 2 || fromPath.split('/').length > 2) {
                return 'slide'
            }

            // 默认使用淡入淡出
            return 'fade'
        },
        beforeEnter(el) {
            // 动画开始前设置初始状态
            if (!this.animationEnabled) {
                return
            }

            el.style.opacity = 0
            el.style.transformOrigin = 'center'

            switch (this.animationType) {
                case 'slide':
                    el.style.transform = `translateX(${this.animationDirection === 'left' ? '100px' : '-100px'})`
                    break
                case 'zoom':
                    el.style.transform = 'scale(0.95)'
                    break
                default:
                    el.style.transform = 'translateY(20px)'
            }
        },
        enter(el, done) {
            // 执行进入动画
            if (!this.animationEnabled) {
                el.style.opacity = 1
                el.style.transform = 'none'
                done()
                return
            }

            let animationPromise

            switch (this.animationType) {
                case 'slide':
                    animationPromise = safeAnimation(pageSlideIn, el, this.animationDirection, 0.6)
                    break
                case 'zoom':
                    animationPromise = safeAnimation(pageZoomIn, el, 0.5)
                    break
                default:
                    animationPromise = safeAnimation(pageFadeIn, el, 0.5)
            }

            if (animationPromise) {
                animationPromise.then(done)
            } else {
                done()
            }
        },
        afterEnter(el) {
            // 动画完成后清理样式
            if (!this.animationEnabled) {
                return
            }

            el.style.opacity = ''
            el.style.transform = ''
            el.style.transformOrigin = ''

            // 恢复滚动位置
            window.scrollTo(0, this.lastScrollPosition)

            // 触发自定义事件，通知页面已进入
            this.$emit('page-entered', this.$route)
        },
        leave(el, done) {
            // 执行离开动画
            if (!this.animationEnabled) {
                done()
                return
            }

            const animationPromise = safeAnimation(pageFadeOut, el, 0.3)

            if (animationPromise) {
                animationPromise.then(done)
            } else {
                done()
            }
        },
        afterLeave(el) {
            // 离开动画完成后清理样式
            if (!this.animationEnabled) {
                return
            }

            el.style.opacity = ''
            el.style.transform = ''

            // 触发自定义事件，通知页面已离开
            this.$emit('page-left', this.$route)
        },
        // 公共方法：手动触发页面动画
        animatePageTransition(direction = 'forward') {
            const contentElement = document.querySelector('.content .el-main > *')
            if (!contentElement) return

            this.animationDirection = direction === 'forward' ? 'right' : 'left'

            if (this.animationEnabled) {
                safeAnimation(pageSlideIn, contentElement, this.animationDirection, 0.4)
            }
        }
    },
    computed: {
        // 当前路由的动画类
        animationClass() {
            if (!this.animationEnabled) return ''

            return `page-transition-${this.animationType}`
        }
    }
}
</script>

<style lang="less" scoped>
// 导入DNF主题变量
@import '@/assets/styles/variables.less';
@import '@/assets/styles/mixins.less';

// 路由过渡容器样式
::v-deep .page-transition-container {
    position: relative;
    width: 100%;
    height: 100%;

    .page-content {
        position: absolute;
        top: 0;
        left: 0;
        right: 0;
        bottom: 0;
        overflow-y: auto;
        .custom-scrollbar();
    }
}

// 动画性能优化
::v-deep .page-enter-active,
::v-deep .page-leave-active {
    will-change: transform, opacity;
    backface-visibility: hidden;
    perspective: 1000px;
}

// 页面加载指示器
.page-loading-indicator {
    position: fixed;
    top: 0;
    left: 0;
    right: 0;
    height: 3px;
    background: @gradient-purple-gold;
    z-index: 9999;
    transform-origin: left;
    animation: loadingProgress 0.5s ease-out;

    @keyframes loadingProgress {
        from {
            transform: scaleX(0);
        }
        to {
            transform: scaleX(1);
        }
    }
}

// 响应式调整
@media (max-width: @breakpoint-md) {
    ::v-deep .page-transition-container {
        .page-content {
            padding: @spacing-md;
        }
    }

    .page-loading-indicator {
        height: 2px;
    }
}

// 减少动画支持
@media (prefers-reduced-motion: reduce) {
    ::v-deep .page-enter-active,
    ::v-deep .page-leave-active {
        transition: none !important;
        animation: none !important;
    }

    .page-loading-indicator {
        display: none;
    }
}
</style>