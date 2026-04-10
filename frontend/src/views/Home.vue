<!--
 * @Description: 首页组件
 * @Author: System
 * @Date: 2026-04-10
 * @LastEditTime: 2026-04-10 17:23:54
 * @FilePath: /vue2-java-mysql-project/frontend/src/views/Home.vue
 -->
<template>
    <div class="home">
        <!-- Logo区域 -->
        <div class="logo-section">
            <div class="logo-container">
                <div class="logo-text">
                    <span class="logo-d">D</span>
                    <span class="logo-n">N</span>
                    <span class="logo-f">F</span>
                </div>
                <div class="logo-subtitle">
                    地下城与勇士风格
                </div>
            </div>
        </div>

        <!-- 主标题 -->
        <h1 ref="mainTitle">
            欢迎使用Vue2 + Java + MySQL全栈项目
        </h1>

        <!-- 描述区域 -->
        <div
            ref="description"
            class="description"
        >
            <p>这是一个完整的全栈项目示例，包含：</p>
            <ul>
                <li>前端：Vue2 + Element UI + Vue Router + Vuex</li>
                <li>后端：Java Spring Boot + MySQL</li>
                <li>数据库：MySQL</li>
                <li>测试：Jest + Puppeteer端到端测试</li>
                <li>部署：Docker容器化支持</li>
            </ul>
            <p>点击上方导航栏的"用户管理"来管理用户数据。</p>
        </div>

        <!-- 功能卡片网格 -->
        <div class="feature-grid">
            <div
                ref="featureCard1"
                class="feature-card"
            >
                <div class="feature-icon">
                    <i class="el-icon-monitor" />
                </div>
                <h3 class="feature-title">
                    现代前端技术
                </h3>
                <p class="feature-description">
                    基于Vue2生态系统，包含路由、状态管理、UI组件库
                </p>
            </div>
            <div
                ref="featureCard2"
                class="feature-card"
            >
                <div class="feature-icon">
                    <i class="el-icon-data-line" />
                </div>
                <h3 class="feature-title">
                    强大后端支持
                </h3>
                <p class="feature-description">
                    Spring Boot + MySQL，提供RESTful API和数据库管理
                </p>
            </div>
            <div
                ref="featureCard3"
                class="feature-card"
            >
                <div class="feature-icon">
                    <i class="el-icon-setting" />
                </div>
                <h3 class="feature-title">
                    完整开发工具链
                </h3>
                <p class="feature-description">
                    包含代码规范、测试框架、构建工具和部署配置
                </p>
            </div>
            <div
                ref="featureCard4"
                class="feature-card"
            >
                <div class="feature-icon">
                    <i class="el-icon-guide" />
                </div>
                <h3 class="feature-title">
                    DNF主题设计
                </h3>
                <p class="feature-description">
                    地下城与勇士风格界面，暗色主题与华丽特效
                </p>
            </div>
        </div>

        <!-- 状态卡片 -->
        <div
            ref="statusCard"
            class="status"
        >
            <el-alert
                title="项目状态"
                type="info"
                :closable="false"
            >
                <p>前端运行在: <strong>http://localhost:8080</strong></p>
                <p>后端API运行在: <strong>http://localhost:8081</strong></p>
                <p>数据库: <strong>MySQL @ localhost:3306</strong></p>
            </el-alert>
        </div>
    </div>
</template>

<script>
import gsap from 'gsap'
import { ScrollTrigger } from 'gsap/ScrollTrigger'

export default {
    name: 'Home',
    data() {
        return {
            timeline: null
        }
    },
    mounted() {
        // 注册ScrollTrigger插件
        gsap.registerPlugin(ScrollTrigger)

        // 创建主时间轴
        this.timeline = gsap.timeline({
            defaults: {
                duration: 1.2,
                ease: 'power3.out'
            }
        })

        // Logo区域动画 - 字母依次出现
        this.timeline.from('.logo-text span', {
            y: 60,
            opacity: 0,
            scale: 0,
            rotation: 180,
            stagger: 0.3,
            ease: 'back.out(1.7)'
        })

        // Logo副标题动画
        this.timeline.from('.logo-subtitle', {
            y: 30,
            opacity: 0,
            scale: 0.8,
            ease: 'elastic.out(1, 0.5)'
        }, '-=0.4')

        // 标题动画
        this.timeline.from('.home h1', {
            y: 50,
            opacity: 0,
            scale: 0.8,
            rotationX: 15
        }, '-=0.2')

        // 描述区域动画
        this.timeline.from('.description', {
            y: 40,
            opacity: 0,
            scale: 0.95
        }, '-=0.6')

        // 列表项交错动画
        this.timeline.from('.description li', {
            y: 30,
            opacity: 0,
            stagger: 0.2
        }, '-=0.4')

        // 功能卡片网格动画
        this.timeline.from('.feature-card', {
            y: 50,
            opacity: 0,
            scale: 0.85,
            stagger: 0.15,
            rotationY: 20
        }, '-=0.4')

        // 状态卡片动画
        this.timeline.from('.status', {
            y: 40,
            opacity: 0,
            scale: 0.95
        }, '-=0.4')

        // 设置滚动触发动画
        this.setupScrollAnimations()
    },
    beforeDestroy() {
        // 清理动画实例
        if (this.timeline) {
            this.timeline.kill()
        }
        ScrollTrigger.getAll().forEach(trigger => trigger.kill())
    },
    methods: {
        setupScrollAnimations() {
            // Logo区域视差效果
            gsap.to('.logo-section', {
                scrollTrigger: {
                    trigger: '.home',
                    start: 'top top',
                    end: 'bottom top',
                    scrub: true,
                    markers: false
                },
                y: 50,
                opacity: 0.8,
                scale: 0.95
            })

            // 为描述区域添加滚动触发动画
            gsap.from('.description', {
                scrollTrigger: {
                    trigger: '.description',
                    start: 'top 85%',
                    end: 'bottom 20%',
                    toggleActions: 'play none none reverse',
                    markers: false
                },
                y: 40,
                opacity: 0,
                rotationX: 10,
                duration: 1.2,
                ease: 'power3.out'
            })

            // 描述区域列表项交错动画
            gsap.from('.description li', {
                scrollTrigger: {
                    trigger: '.description',
                    start: 'top 75%',
                    end: 'bottom 20%',
                    toggleActions: 'play none none reverse',
                    markers: false
                },
                y: 30,
                opacity: 0,
                stagger: 0.15,
                duration: 0.8,
                ease: 'back.out(1.5)'
            })

            // 为功能卡片添加滚动触发动画
            gsap.from('.feature-card', {
                scrollTrigger: {
                    trigger: '.feature-grid',
                    start: 'top 90%',
                    end: 'bottom 20%',
                    toggleActions: 'play none none reverse',
                    markers: false,
                    scrub: 0.5
                },
                y: 60,
                opacity: 0,
                scale: 0.85,
                rotationY: 30,
                stagger: 0.2,
                duration: 1,
                ease: 'power3.out'
            })

            // 为状态卡片添加滚动触发动画
            gsap.from('.status', {
                scrollTrigger: {
                    trigger: '.status',
                    start: 'top 85%',
                    end: 'bottom 20%',
                    toggleActions: 'play none none reverse',
                    markers: false
                },
                y: 40,
                opacity: 0,
                scale: 0.9,
                duration: 1.2,
                ease: 'elastic.out(1, 0.5)'
            })

            // 标题浮动效果
            gsap.to('h1', {
                scrollTrigger: {
                    trigger: 'h1',
                    start: 'top 80%',
                    end: 'bottom 20%',
                    scrub: true,
                    markers: false
                },
                y: -20,
                scale: 1.05,
                textShadow: '0 0 30px rgba(212, 175, 55, 0.7)'
            })

            // Logo区域悬停效果增强
            const logoLetters = document.querySelectorAll('.logo-text span')
            logoLetters.forEach((letter, index) => {
                letter.addEventListener('mouseenter', () => {
                    gsap.to(letter, {
                        scale: 1.4,
                        y: -15,
                        rotation: 15,
                        color: index === 0 ? '#8a2be2' : index === 1 ? '#3b82f6' : '#ffd700',
                        duration: 0.3,
                        ease: 'power2.out'
                    })
                    // 添加粒子效果
                    this.createParticles(letter, index)
                })
                letter.addEventListener('mouseleave', () => {
                    gsap.to(letter, {
                        scale: 1,
                        y: 0,
                        rotation: 0,
                        color: index === 0 ? '#6a11cb' : index === 1 ? '#1a3a5f' : '#d4af37',
                        duration: 0.5,
                        ease: 'elastic.out(1, 0.5)'
                    })
                })
            })
        },
        createParticles(element, index) {
            const colors = ['#8a2be2', '#3b82f6', '#ffd700']
            const color = colors[index]
            for (let i = 0; i < 8; i++) {
                const particle = document.createElement('div')
                particle.style.position = 'absolute'
                particle.style.width = '4px'
                particle.style.height = '4px'
                particle.style.backgroundColor = color
                particle.style.borderRadius = '50%'
                particle.style.pointerEvents = 'none'
                particle.style.zIndex = '1000'

                const rect = element.getBoundingClientRect()
                const x = rect.left + rect.width / 2
                const y = rect.top + rect.height / 2

                particle.style.left = `${x}px`
                particle.style.top = `${y}px`

                document.body.appendChild(particle)

                gsap.to(particle, {
                    x: (Math.random() - 0.5) * 100,
                    y: (Math.random() - 0.5) * 100,
                    opacity: 0,
                    scale: 0,
                    duration: 0.8,
                    ease: 'power2.out',
                    onComplete: () => {
                        document.body.removeChild(particle)
                    }
                })
            }
        }
    }
}
</script>

<style lang="less" scoped>
// 导入DNF主题变量和混合函数
@import '@/assets/styles/variables.less';
@import '@/assets/styles/mixins.less';

.home {
    padding: @spacing-xl;
    animation: page-fade-in 0.6s ease-out;

    // Logo区域样式
    .logo-section {
        text-align: center;
        margin-bottom: @spacing-3xl;
        position: relative;
        overflow: hidden;
        padding: @spacing-xl 0;

        .logo-container {
            position: relative;
            display: inline-block;
        }

        .logo-text {
            font-size: 72px;
            font-weight: @font-weight-bold;
            letter-spacing: 8px;
            position: relative;
            display: flex;
            justify-content: center;
            gap: 16px;
            margin-bottom: @spacing-md;

            .logo-d, .logo-n, .logo-f {
                display: inline-block;
                position: relative;
                text-shadow: 0 0 20px @dnf-primary-purple;
                animation: logo-float 6s ease-in-out infinite;

                &::after {
                    content: '';
                    position: absolute;
                    bottom: -10px;
                    left: 0;
                    right: 0;
                    height: 4px;
                    background: @gradient-purple-gold;
                    border-radius: 2px;
                    transform: scaleX(0);
                    transform-origin: center;
                    animation: logo-line 3s ease-in-out infinite;
                }
            }

            .logo-d {
                color: @dnf-primary-purple;
                animation-delay: 0s;
            }

            .logo-n {
                color: @dnf-primary-blue;
                animation-delay: 0.2s;
            }

            .logo-f {
                color: @dnf-primary-gold;
                animation-delay: 0.4s;
            }
        }

        .logo-subtitle {
            color: @dnf-text-secondary;
            font-size: @font-size-lg;
            letter-spacing: 4px;
            text-transform: uppercase;
            position: relative;
            display: inline-block;
            padding: @spacing-sm @spacing-lg;
            background: fade(@dnf-primary-dark, 70%);
            border-radius: @border-radius-full;
            border: 1px solid fade(@dnf-primary-gold, 30%);

            &::before, &::after {
                content: '';
                position: absolute;
                top: 50%;
                width: 40px;
                height: 1px;
                background: @gradient-purple-gold;
            }

            &::before {
                right: 100%;
                margin-right: @spacing-md;
            }

            &::after {
                left: 100%;
                margin-left: @spacing-md;
            }
        }
    }

    h1 {
        text-align: center;
        margin-bottom: @spacing-2xl;
        font-size: @font-size-3xl;
        font-weight: @font-weight-bold;
        .gold-gradient-text();
        text-shadow: 0 0 20px fade(@dnf-primary-gold, 50%);
        position: relative;
        padding-bottom: @spacing-lg;

        &::after {
            content: '';
            position: absolute;
            bottom: 0;
            left: 50%;
            transform: translateX(-50%);
            width: 200px;
            height: 4px;
            background: @gradient-purple-gold;
            border-radius: 2px;
            animation: title-underline 3s ease-in-out infinite;
        }
    }

    .description {
        text-align: center;
        max-width: 800px;
        margin: @spacing-2xl auto;
        padding: @spacing-xl;
        .dnf-card();
        .gradient-border(@gradient-purple-gold, 3px);
        position: relative;
        overflow: hidden;
        background: fade(@dnf-primary-dark, 80%);
        backdrop-filter: blur(10px);
        -webkit-backdrop-filter: blur(10px);

        &::before {
            content: '';
            position: absolute;
            top: 0;
            left: 0;
            right: 0;
            bottom: 0;
            background: @gradient-purple-gold;
            opacity: 0.1;
            z-index: -1;
        }

        p {
            color: @dnf-text-secondary;
            font-size: @font-size-lg;
            margin-bottom: @spacing-lg;
            line-height: @line-height-relaxed;

            &:first-child {
                color: @dnf-text-primary;
                font-size: @font-size-xl;
                font-weight: @font-weight-bold;
                .gold-gradient-text();
                text-shadow: 0 0 15px fade(@dnf-primary-gold, 50%);
                margin-bottom: @spacing-xl;
            }
        }

        ul {
            margin: @spacing-lg auto;
            padding: 0;
            list-style: none;
            max-width: 600px;
        }

        li {
            margin: @spacing-md 0;
            padding: @spacing-md @spacing-lg;
            background: fade(@dnf-primary-blue, 20%);
            border-radius: @border-radius-md;
            border-left: 4px solid @dnf-primary-purple;
            color: @dnf-text-secondary;
            transition: all @transition-normal;
            text-align: left;
            position: relative;
            overflow: hidden;

            &::before {
                content: '';
                position: absolute;
                top: 0;
                left: 0;
                right: 0;
                bottom: 0;
                background: @gradient-blue-purple;
                opacity: 0;
                transition: opacity @transition-normal;
                z-index: -1;
            }

            &:hover {
                background: fade(@dnf-primary-purple, 10%);
                transform: translateX(8px);
                border-left-color: @dnf-primary-gold;
                color: @dnf-text-primary;

                &::before {
                    opacity: 0.2;
                }
            }
        }
    }

    .status {
        max-width: 600px;
        margin: @spacing-2xl auto;
        text-align: left;

        .el-alert {
            .dnf-card();
            .gradient-border(@gradient-blue-purple, 2px);
            background: fade(@dnf-primary-dark, 70%);
            backdrop-filter: blur(8px);
            -webkit-backdrop-filter: blur(8px);
            position: relative;
            overflow: hidden;

            &::before {
                content: '';
                position: absolute;
                top: -2px;
                left: -2px;
                right: -2px;
                bottom: -2px;
                background: @gradient-blue-purple;
                border-radius: @border-radius-lg + 2px;
                opacity: 0.3;
                z-index: -1;
            }

            .el-alert__title {
                color: @dnf-primary-gold;
                font-weight: @font-weight-bold;
                font-size: @font-size-lg;
                .gold-gradient-text();
                margin-bottom: @spacing-md;
            }

            p {
                color: @dnf-text-secondary;
                margin: @spacing-sm 0;
                font-size: @font-size-md;

                strong {
                    color: @dnf-primary-gold;
                    font-weight: @font-weight-bold;
                    text-shadow: 0 0 8px fade(@dnf-primary-gold, 50%);
                }
            }
        }
    }

    // 功能卡片网格
    .feature-grid {
        display: grid;
        grid-template-columns: repeat(auto-fit, minmax(280px, 1fr));
        gap: @spacing-xl;
        margin: @spacing-3xl 0;

        .feature-card {
            .dnf-card();
            .gradient-border(@gradient-blue-purple, 2px);
            padding: @spacing-2xl @spacing-xl;
            text-align: center;
            transition: all @transition-normal;
            position: relative;
            overflow: hidden;
            background: fade(@dnf-primary-dark, 70%);
            backdrop-filter: blur(8px);
            -webkit-backdrop-filter: blur(8px);

            &::before {
                content: '';
                position: absolute;
                top: 0;
                left: 0;
                right: 0;
                bottom: 0;
                background: @gradient-blue-purple;
                opacity: 0;
                transition: opacity @transition-normal;
                z-index: -1;
            }

            &::after {
                content: '';
                position: absolute;
                top: 0;
                left: -100%;
                width: 100%;
                height: 100%;
                background: linear-gradient(
                    90deg,
                    transparent,
                    fade(@dnf-primary-gold, 20%),
                    transparent
                );
                transition: left @transition-slow;
            }

            &:hover {
                transform: translateY(-15px) scale(1.03);
                box-shadow: @dnf-shadow-purple, 0 20px 40px fade(@dnf-primary-purple, 30%);

                &::before {
                    opacity: 0.15;
                }

                &::after {
                    left: 100%;
                }

                .feature-icon {
                    transform: scale(1.3) rotate(10deg);
                    color: @dnf-primary-gold;
                    filter: drop-shadow(0 0 12px @dnf-primary-gold);
                }

                .feature-title {
                    .gold-gradient-text();
                    text-shadow: 0 0 10px fade(@dnf-primary-gold, 50%);
                }

                .feature-description {
                    color: @dnf-text-light;
                }
            }

            .feature-icon {
                font-size: 56px;
                color: @dnf-primary-purple;
                margin-bottom: @spacing-lg;
                transition: all @transition-normal;
                filter: drop-shadow(0 0 8px fade(@dnf-primary-purple, 50%));
            }

            .feature-title {
                color: @dnf-text-primary;
                font-size: @font-size-xl;
                font-weight: @font-weight-bold;
                margin-bottom: @spacing-md;
                transition: all @transition-normal;
            }

            .feature-description {
                color: @dnf-text-secondary;
                font-size: @font-size-md;
                line-height: @line-height-relaxed;
                transition: color @transition-normal;
            }
        }
    }
}

// 响应式调整
@media (max-width: @breakpoint-md) {
    .home {
        padding: @spacing-md;

        .logo-section {
            margin-bottom: @spacing-2xl;
            padding: @spacing-lg 0;

            .logo-text {
                font-size: 48px;
                letter-spacing: 4px;
                gap: 8px;
                flex-wrap: wrap;
                justify-content: center;
            }

            .logo-subtitle {
                font-size: @font-size-md;
                letter-spacing: 2px;
                padding: @spacing-xs @spacing-md;

                &::before, &::after {
                    width: 20px;
                }
            }
        }

        h1 {
            font-size: @font-size-2xl;
            margin-bottom: @spacing-xl;
            padding-bottom: @spacing-md;

            &::after {
                width: 150px;
            }
        }

        .description {
            padding: @spacing-lg;
            margin: @spacing-xl auto;

            p {
                font-size: @font-size-md;

                &:first-child {
                    font-size: @font-size-lg;
                }
            }

            li {
                padding: @spacing-sm @spacing-md;
                font-size: @font-size-sm;
            }
        }

        .feature-grid {
            grid-template-columns: 1fr;
            gap: @spacing-md;
            margin: @spacing-2xl 0;

            .feature-card {
                padding: @spacing-xl @spacing-lg;

                .feature-icon {
                    font-size: 42px;
                }

                .feature-title {
                    font-size: @font-size-lg;
                }

                .feature-description {
                    font-size: @font-size-sm;
                }
            }
        }

        .status {
            margin: @spacing-2xl auto;

            .el-alert {
                padding: @spacing-md;

                p {
                    font-size: @font-size-sm;
                }
            }
        }
    }
}

// 小屏幕手机
@media (max-width: @breakpoint-sm) {
    .home {
        padding: @spacing-sm;

        .logo-section {
            .logo-text {
                font-size: 36px;
                letter-spacing: 2px;
                gap: 4px;
            }

            .logo-subtitle {
                font-size: @font-size-sm;
                letter-spacing: 1px;
                padding: @spacing-xs @spacing-sm;
            }
        }

        h1 {
            font-size: @font-size-xl;
            margin-bottom: @spacing-lg;
        }

        .description {
            padding: @spacing-md;

            p {
                font-size: @font-size-sm;

                &:first-child {
                    font-size: @font-size-md;
                }
            }

            li {
                padding: @spacing-xs @spacing-sm;
                font-size: @font-size-xs;
            }
        }

        .feature-grid {
            .feature-card {
                padding: @spacing-lg @spacing-md;
            }
        }
    }
}

// 动画定义
@keyframes page-fade-in {
    from {
        opacity: 0;
        transform: translateY(20px);
    }
    to {
        opacity: 1;
        transform: translateY(0);
    }
}

// Logo浮动动画
@keyframes logo-float {
    0%, 100% {
        transform: translateY(0);
    }
    50% {
        transform: translateY(-15px);
    }
}

// Logo下划线动画
@keyframes logo-line {
    0%, 100% {
        transform: scaleX(0);
    }
    50% {
        transform: scaleX(1);
    }
}

// Logo发光动画
@keyframes logo-glow {
    0%, 100% {
        text-shadow: 0 0 20px @dnf-primary-purple;
    }
    50% {
        text-shadow: 0 0 30px @dnf-primary-purple, 0 0 40px @dnf-primary-gold;
    }
}

// 标题下划线动画
@keyframes title-underline {
    0%, 100% {
        width: 200px;
        opacity: 1;
    }
    50% {
        width: 300px;
        opacity: 0.8;
    }
}
</style>