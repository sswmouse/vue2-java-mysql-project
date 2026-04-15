<!--
 * @Description: 首页组件 - DNF沉浸式风格
 * @Author: Claude Code
 * @Date: 2026-04-14
 * @LastEditTime: 2026-04-14 11:38:01
 * @FilePath: /vue2-java-mysql-project/frontend/src/views/Home.vue
-->
<template>
    <div class="home">
        <!-- 动态粒子背景 -->
        <div class="bg-particles">
            <div
                v-for="i in 20"
                :key="i"
                class="particle"
                :class="`particle-${(i % 3) + 1}`"
                :style="{
                    left: `${Math.random() * 100}%`,
                    top: `${Math.random() * 100}%`,
                    animationDelay: `${Math.random() * 8}s`,
                    animationDuration: `${8 + Math.random() * 6}s`
                }"
            />
            <!-- 背景光晕 -->
            <div class="glow-orb glow-orb-1" />
            <div class="glow-orb glow-orb-2" />
            <div class="glow-orb glow-orb-3" />
        </div>

        <!-- 英雄区域 -->
        <div class="hero-section">
            <div class="hero-content">
                <!-- Logo区域 -->
                <div class="logo-container">
                    <div class="logo-text">
                        <span class="logo-letter logo-d">D</span>
                        <span class="logo-letter logo-n">N</span>
                        <span class="logo-letter logo-f">F</span>
                    </div>
                    <div class="logo-subtitle">
                        <span class="subtitle-line" />
                        <span class="subtitle-text">DUNGEON FANTASY</span>
                        <span class="subtitle-line" />
                    </div>
                </div>

                <!-- 主标题 -->
                <h1
                    ref="mainTitle"
                    class="hero-title"
                >
                    游戏管理系统
                </h1>

                <!-- 副标题 -->
                <p class="hero-subtitle">
                    基于 Vue2 + Spring Boot + MySQL 的沉浸式游戏化管理平台
                </p>
            </div>

            <!-- 装饰线条 -->
            <div class="hero-decoration">
                <div class="deco-line deco-left" />
                <div class="deco-diamond" />
                <div class="deco-line deco-right" />
            </div>
        </div>

        <!-- 技术栈展示 -->
        <div class="tech-stack-section">
            <div class="section-header">
                <h2>技术架构</h2>
                <div class="section-line" />
            </div>

            <div class="tech-grid">
                <div
                    v-for="(tech, index) in techStack"
                    :key="index"
                    class="tech-item"
                >
                    <div class="tech-icon">
                        <i :class="tech.icon" />
                    </div>
                    <h3>{{ tech.title }}</h3>
                    <p>{{ tech.description }}</p>
                </div>
            </div>
        </div>

        <!-- 功能卡片网格 -->
        <div class="feature-section">
            <div class="section-header">
                <h2>核心功能</h2>
                <div class="section-line" />
            </div>

            <div class="feature-grid">
                <div
                    v-for="(feature, index) in features"
                    :key="index"
                    class="feature-card"
                    :class="`feature-${index + 1}`"
                >
                    <div class="feature-glow" />
                    <div class="feature-icon">
                        <i :class="feature.icon" />
                    </div>
                    <h3>{{ feature.title }}</h3>
                    <p>{{ feature.description }}</p>
                    <div class="feature-tags">
                        <span
                            v-for="tag in feature.tags"
                            :key="tag"
                            class="feature-tag"
                        >{{ tag }}</span>
                    </div>
                </div>
            </div>
        </div>

        <!-- 状态信息 -->
        <div class="status-section">
            <div class="status-card">
                <div class="status-header">
                    <i class="el-icon-monitor" />
                    <span>运行环境</span>
                </div>
                <div class="status-list">
                    <div class="status-item">
                        <span class="status-label">前端服务</span>
                        <span class="status-value status-frontend">http://localhost:8080</span>
                    </div>
                    <div class="status-item">
                        <span class="status-label">后端API</span>
                        <span class="status-value status-backend">http://localhost:8081</span>
                    </div>
                    <div class="status-item">
                        <span class="status-label">数据库</span>
                        <span class="status-value status-db">MySQL @ localhost:3306</span>
                    </div>
                </div>
            </div>
        </div>
    </div>
</template>

<script>
import gsap from 'gsap'
import { ScrollTrigger } from 'gsap/ScrollTrigger'

gsap.registerPlugin(ScrollTrigger)

export default {
    name: 'Home',
    data() {
        return {
            timeline: null,
            techStack: [
                {
                    icon: 'el-icon-monitor',
                    title: '前端框架',
                    description: 'Vue 2.6 + Vue Router + Vuex 完整生态'
                },
                {
                    icon: 'el-icon-data-analysis',
                    title: 'UI组件库',
                    description: 'Element UI 深度定制，霓虹暗色主题'
                },
                {
                    icon: 'el-icon-box',
                    title: '构建工具',
                    description: 'Vue CLI 5 + LESS 预处理器'
                },
                {
                    icon: 'el-icon-connection',
                    title: '后端服务',
                    description: 'Spring Boot 2.7 + RESTful API'
                },
                {
                    icon: 'el-icon-coin',
                    title: '数据库',
                    description: 'MySQL 8.0 关系型数据库'
                },
                {
                    icon: 'el-icon-magic-stick',
                    title: '动画引擎',
                    description: 'GSAP + ScrollTrigger 流畅动效'
                }
            ],
            features: [
                {
                    icon: 'el-icon-user',
                    title: '角色管理',
                    description: '创建、编辑、删除游戏角色，支持拖拽排序和3D翻转卡片展示',
                    tags: ['CRUD', '拖拽排序', '3D翻转']
                },
                {
                    icon: 'el-icon-s-shop',
                    title: '装备系统',
                    description: '管理角色装备穿戴，装备强化与附魔系统',
                    tags: ['强化', '附魔', '套装']
                },
                {
                    icon: 'el-icon-cloudy',
                    title: '时装系统',
                    description: '角色外观时装管理，支持多种风格分类',
                    tags: ['外观', '染色', '幻化']
                },
                {
                    icon: 'el-icon-data-line',
                    title: '宠物系统',
                    description: '宠物捕捉、养成与技能配置',
                    tags: ['养成', '技能', '进化']
                },
                {
                    icon: 'el-icon-medal',
                    title: '成就徽章',
                    description: '记录玩家成就，解锁专属荣誉徽章',
                    tags: ['成就', '荣誉', '排名']
                },
                {
                    icon: 'el-icon-moon',
                    title: '迷雾大陆',
                    description: '探索未知的地图区域，解锁新剧情',
                    tags: ['探索', '剧情', '地图']
                }
            ]
        }
    },
    mounted() {
        // 禁用GSAP动画，确保内容直接可见
        // this.initAnimations()
        // this.setupScrollAnimations()
    },
    beforeDestroy() {
        if (this.timeline) {
            this.timeline.kill()
        }
        ScrollTrigger.getAll().forEach(trigger => trigger.kill())
    },
    methods: {
        initAnimations() {
            this.timeline = gsap.timeline({
                defaults: {
                    duration: 1,
                    ease: 'power3.out'
                }
            })

            // Logo字母依次出现
            this.timeline.from('.logo-letter', {
                y: 100,
                opacity: 0,
                scale: 0,
                rotation: -30,
                stagger: 0.2,
                ease: 'back.out(1.7)'
            })

            // Logo副标题
            this.timeline.from('.logo-subtitle', {
                y: 30,
                opacity: 0,
                scaleX: 0,
                ease: 'power2.out'
            }, '-=0.4')

            // 主标题
            this.timeline.from('.hero-title', {
                y: 60,
                opacity: 0,
                scale: 0.9,
                ease: 'power2.out'
            }, '-=0.6')

            // 副标题
            this.timeline.from('.hero-subtitle', {
                y: 30,
                opacity: 0
            }, '-=0.4')

            // 装饰元素
            this.timeline.from('.hero-decoration', {
                scaleX: 0,
                opacity: 0
            }, '-=0.4')

            // 技术栈
            this.timeline.from('.tech-item', {
                y: 40,
                opacity: 0,
                stagger: 0.1,
                scale: 0.9
            }, '-=0.4')

            // 功能卡片
            this.timeline.from('.feature-card', {
                y: 60,
                opacity: 0,
                scale: 0.85,
                stagger: 0.12,
                rotationY: 15
            }, '-=0.3')

            // 状态卡片
            this.timeline.from('.status-card', {
                y: 40,
                opacity: 0,
                scale: 0.95
            }, '-=0.2')
        },

        setupScrollAnimations() {
            // 滚动动画已禁用，避免干扰页面布局
            // Logo视差效果
            // gsap.to('.hero-section', {
            //     scrollTrigger: {
            //         trigger: '.home',
            //         start: 'top top',
            //         end: 'bottom top',
            //         scrub: 1
            //     },
            //     y: 80,
            //     opacity: 0.6
            // })

            // // 标题呼吸效果
            // gsap.to('.hero-title', {
            //     scrollTrigger: {
            //         trigger: '.hero-title',
            //         start: 'top 80%',
            //         end: 'bottom 20%',
            //         scrub: 1
            //     },
            //     textShadow: '0 0 40px rgba(200, 168, 50, 0.8), 0 0 80px rgba(74, 16, 144, 0.4)'
            // })

            // // 功能卡片滚动进入
            // gsap.utils.toArray('.feature-card').forEach((card, i) => {
            //     gsap.from(card, {
            //         scrollTrigger: {
            //             trigger: card,
            //             start: 'top 85%',
            //             end: 'bottom 20%',
            //             toggleActions: 'play none none reverse'
            //         },
            //         y: 80,
            //         opacity: 0,
            //         scale: 0.85,
            //         rotationY: 20,
            //         duration: 1,
            //         ease: 'power3.out',
            //         delay: i * 0.1
            //     })
            // })

            // // 背景粒子视差
            // gsap.to('.bg-particles', {
            //     scrollTrigger: {
            //         trigger: '.home',
            //         start: 'top top',
            //         end: 'bottom bottom',
            //         scrub: 0.5
            //     },
            //     y: 200
            // })
        }
    }
}
</script>

<style lang="less" scoped>
@import '@/assets/styles/variables.less';
@import '@/assets/styles/mixins.less';

.home {
    position: relative;
    min-height: 100%;
    height: 100%;
    overflow-x: hidden;
    background: var(--theme-bg-dark);
    padding: 0;
}

// ============================================
// 粒子背景
// ============================================

.bg-particles {
    position: absolute;
    top: 0;
    left: 0;
    right: 0;
    bottom: 0;
    pointer-events: none;
    z-index: 0;
    overflow: hidden;
}

.particle {
    position: absolute;
    border-radius: 50%;
    pointer-events: none;
}

.particle-1 {
    width: 4px;
    height: 4px;
    background: var(--theme-secondary);
    box-shadow: 0 0 10px var(--theme-secondary);
    animation: particle-drift-1 12s ease-in-out infinite;
}

.particle-2 {
    width: 3px;
    height: 3px;
    background: var(--theme-accent);
    box-shadow: 0 0 8px var(--theme-accent);
    animation: particle-drift-2 15s ease-in-out infinite;
}

.particle-3 {
    width: 5px;
    height: 5px;
    background: var(--theme-info);
    box-shadow: 0 0 12px var(--theme-info);
    animation: particle-drift-3 10s ease-in-out infinite;
}

.glow-orb {
    position: absolute;
    border-radius: 50%;
    filter: blur(80px);
    pointer-events: none;
}

.glow-orb-1 {
    width: 600px;
    height: 600px;
    background: radial-gradient(circle, var(--theme-secondary) 0%, transparent 70%);
    opacity: 0.15;
    top: -200px;
    left: -100px;
    animation: glow-pulse 8s ease-in-out infinite;
}

.glow-orb-2 {
    width: 500px;
    height: 500px;
    background: radial-gradient(circle, var(--theme-accent) 0%, transparent 70%);
    opacity: 0.1;
    top: 30%;
    right: -150px;
    animation: glow-pulse 10s ease-in-out infinite;
    animation-delay: 2s;
}

.glow-orb-3 {
    width: 400px;
    height: 400px;
    background: radial-gradient(circle, var(--theme-info) 0%, transparent 70%);
    opacity: 0.08;
    bottom: 20%;
    left: 20%;
    animation: glow-pulse 12s ease-in-out infinite;
    animation-delay: 4s;
}

// ============================================
// 英雄区域
// ============================================

.hero-section {
    position: relative;
    min-height: 70vh;
    display: flex;
    flex-direction: column;
    align-items: center;
    justify-content: center;
    padding: @spacing-2xl @spacing-xl;
    z-index: 1;

    &::before {
        content: '';
        position: absolute;
        top: 0;
        left: 0;
        right: 0;
        bottom: 0;
        background: radial-gradient(ellipse at center top, var(--theme-secondary) 0%, transparent 60%);
        opacity: 0.1;
        pointer-events: none;
    }
}

.hero-content {
    text-align: center;
    max-width: 900px;
}

.logo-container {
    margin-bottom: @spacing-2xl;
}

.logo-text {
    font-size: 96px;
    font-weight: 900;
    letter-spacing: 16px;
    display: flex;
    justify-content: center;
    gap: 12px;
    margin-bottom: @spacing-lg;
    position: relative;

    .logo-letter {
        display: inline-block;
        animation: logo-float 6s ease-in-out infinite;
        position: relative;

        &::after {
            content: '';
            position: absolute;
            bottom: -12px;
            left: 0;
            right: 0;
            height: 4px;
            border-radius: 2px;
            animation: title-underline 3s ease-in-out infinite;
        }
    }

    .logo-d {
        color: @dnf-secondary-purple;
        text-shadow: 0 0 30px fade(@dnf-secondary-purple, 0.8), 0 0 60px fade(@dnf-secondary-purple, 0.4);
        animation-delay: 0s;

        &::after {
            background: @dnf-secondary-purple;
            box-shadow: 0 0 15px fade(@dnf-secondary-purple, 0.8);
        }
    }

    .logo-n {
        color: @dnf-info;
        text-shadow: 0 0 30px fade(@dnf-info, 0.8), 0 0 60px fade(@dnf-info, 0.4);
        animation-delay: 0.15s;

        &::after {
            background: linear-gradient(90deg, @dnf-secondary-purple, @dnf-info);
        }
    }

    .logo-f {
        color: @dnf-primary-gold;
        text-shadow: 0 0 30px fade(@dnf-primary-gold, 0.8), 0 0 60px fade(@dnf-primary-gold, 0.4);
        animation-delay: 0.3s;

        &::after {
            background: @dnf-primary-gold;
            box-shadow: 0 0 15px fade(@dnf-primary-gold, 0.8);
        }
    }
}

.logo-subtitle {
    display: flex;
    align-items: center;
    justify-content: center;
    gap: @spacing-lg;

    .subtitle-line {
        width: 80px;
        height: 1px;
        background: linear-gradient(90deg, transparent, var(--theme-accent), transparent);
    }

    .subtitle-text {
        font-family: @font-display, 'Orbitron', sans-serif;
        font-size: @font-size-sm;
        letter-spacing: 0.4em;
        color: var(--theme-accent);
        text-shadow: 0 0 20px var(--theme-accent);
        opacity: 0.5;
    }
}

.hero-title {
    font-family: @font-display, 'Orbitron', sans-serif;
    font-size: @font-size-3xl;
    font-weight: 700;
    letter-spacing: 0.08em;
    color: var(--theme-accent);
    text-shadow: 0 0 15px var(--theme-accent);
    opacity: 0.6;
    margin-bottom: @spacing-xl;
    position: relative;
    padding: 0 @spacing-lg;
}

.hero-subtitle {
    font-family: @font-body, 'Quicksand', sans-serif;
    font-size: @font-size-xl;
    color: var(--theme-text-secondary);
    max-width: 600px;
    margin: 0 auto;
    line-height: @line-height-relaxed;
    letter-spacing: 0.02em;
}

.hero-decoration {
    display: flex;
    align-items: center;
    justify-content: center;
    gap: @spacing-lg;
    margin-top: @spacing-3xl;
    opacity: 0.6;

    .deco-line {
        width: 150px;
        height: 1px;
        background: linear-gradient(90deg, transparent, var(--theme-accent));
    }

    .deco-left {
        transform: rotate(180deg);
    }

    .deco-right {
        background: linear-gradient(90deg, var(--theme-accent), transparent);
    }

    .deco-diamond {
        width: 12px;
        height: 12px;
        background: var(--theme-accent);
        transform: rotate(45deg);
        box-shadow: 0 0 15px var(--theme-accent);
    }
}

// ============================================
// 技术栈区域
// ============================================

.tech-stack-section {
    position: relative;
    z-index: 1;
    padding: @spacing-2xl @spacing-xl;
    max-width: 1200px;
    margin: 0 auto;
}

.section-header {
    text-align: center;
    margin-bottom: @spacing-2xl;

    h2 {
        font-family: @font-display, 'Orbitron', sans-serif;
        font-size: @font-size-2xl;
        font-weight: 700;
        letter-spacing: 0.15em;
        text-transform: uppercase;
        color: var(--theme-accent);
        text-shadow: 0 0 20px var(--theme-accent);
        opacity: 0.6;
        margin-bottom: @spacing-md;
    }

    .section-line {
        width: 200px;
        height: 2px;
        margin: 0 auto;
        background: linear-gradient(90deg, transparent, var(--theme-accent), var(--theme-secondary), transparent);
        border-radius: 1px;
    }
}

.tech-grid {
    display: grid;
    grid-template-columns: repeat(auto-fit, minmax(280px, 1fr));
    gap: @spacing-xl;

    .tech-item {
        background: var(--theme-bg-card);
        border: 1px solid var(--theme-border);
        border-radius: @border-radius-xl;
        padding: @spacing-xl;
        text-align: center;
        transition: all @transition-smooth;
        position: relative;
        overflow: hidden;
        opacity: 1 !important;
        visibility: visible !important;

        &::before {
            content: '';
            position: absolute;
            top: 0;
            left: 0;
            right: 0;
            height: 2px;
            background: linear-gradient(90deg, var(--theme-info), var(--theme-secondary));
            opacity: 0;
            transition: opacity @transition-normal;
        }

        &:hover {
            border-color: var(--theme-secondary);
            transform: translateY(-6px);
            box-shadow: 0 8px 32px var(--theme-secondary);

            &::before {
                opacity: 1;
            }

            .tech-icon {
                transform: scale(1.15) rotate(5deg);
                color: var(--theme-secondary);
                filter: drop-shadow(0 0 15px var(--theme-secondary));
            }
        }

        .tech-icon {
            font-size: 48px;
            color: var(--theme-accent);
            margin-bottom: @spacing-lg;
            transition: all @transition-smooth;
            filter: drop-shadow(0 0 8px var(--theme-accent));
        }

        h3 {
            font-family: @font-display, 'Orbitron', sans-serif;
            font-size: @font-size-lg;
            font-weight: 600;
            letter-spacing: 0.1em;
            color: var(--theme-accent);
            text-shadow: 0 0 15px var(--theme-accent);
            margin-bottom: @spacing-sm;
        }

        p {
            font-size: @font-size-sm;
            color: var(--theme-text-primary);
            line-height: @line-height-relaxed;
        }
    }
}

// ============================================
// 功能卡片区域
// ============================================

.feature-section {
    position: relative;
    z-index: 1;
    padding: @spacing-2xl @spacing-xl;
    max-width: 1200px;
    margin: 0 auto;
}

.feature-grid {
    display: grid;
    grid-template-columns: repeat(auto-fit, minmax(340px, 1fr));
    gap: @spacing-xl;
}

.feature-card {
    background: var(--theme-bg-card);
    border: 1px solid var(--theme-border);
    border-radius: @border-radius-xl;
    backdrop-filter: blur(10px);
    padding: @spacing-2xl;
    position: relative;
    overflow: hidden;
    transition: all @transition-smooth;
    opacity: 1 !important;
    visibility: visible !important;

    &::before {
        content: '';
        position: absolute;
        top: 0;
        left: 0;
        right: 0;
        height: 3px;
        background: linear-gradient(90deg, var(--theme-secondary), var(--theme-accent), var(--theme-info));
        opacity: 0;
        transition: opacity @transition-normal;
    }

    .feature-glow {
        position: absolute;
        top: -50%;
        left: -50%;
        width: 200%;
        height: 200%;
        background: radial-gradient(circle, var(--theme-secondary) 0%, transparent 50%);
        pointer-events: none;
        opacity: 0;
        transition: opacity @transition-smooth;
    }

    &:hover {
        transform: translateY(-8px) scale(1.02);
        box-shadow: 0 20px 40px rgba(0, 0, 0, 0.3),
            0 0 50px var(--theme-secondary);

        &::before {
            opacity: 1;
        }

        .feature-glow {
            opacity: 1;
        }

        .feature-icon {
            transform: scale(1.2) rotate(10deg);
            color: var(--theme-accent);
            filter: drop-shadow(0 0 20px var(--theme-accent));
        }

        h3 {
            color: var(--theme-accent);
            text-shadow: 0 0 15px var(--theme-accent);
        }
    }

    .feature-icon {
        font-size: 52px;
        color: var(--theme-secondary);
        margin-bottom: @spacing-xl;
        transition: all @transition-smooth;
        filter: drop-shadow(0 0 10px var(--theme-secondary));
    }

    h3 {
        font-family: @font-display, 'Orbitron', sans-serif;
        font-size: @font-size-xl;
        font-weight: 600;
        letter-spacing: 0.08em;
        color: var(--theme-text-primary);
        margin-bottom: @spacing-md;
        transition: all @transition-smooth;
    }

    p {
        font-size: @font-size-md;
        color: var(--theme-text-secondary);
        line-height: @line-height-relaxed;
        margin-bottom: @spacing-xl;
    }

    .feature-tags {
        display: flex;
        flex-wrap: wrap;
        gap: @spacing-sm;

        .feature-tag {
            display: inline-block;
            padding: 4px 12px;
            background: var(--theme-bg-hover);
            border: 1px solid var(--theme-border);
            border-radius: @border-radius-full;
            font-size: @font-size-xs;
            color: var(--theme-text-secondary);
            font-weight: @font-weight-medium;
            letter-spacing: 0.05em;
            transition: all @transition-fast;

            &:hover {
                background: var(--theme-secondary);
                border-color: var(--theme-secondary);
                color: var(--theme-text-primary);
                box-shadow: 0 0 10px var(--theme-secondary);
            }
        }
    }
}

// ============================================
// 状态信息区域
// ============================================

.status-section {
    position: relative;
    z-index: 1;
    padding: @spacing-2xl @spacing-xl;
    max-width: 600px;
    margin: 0 auto;
}

.status-card {
    background: var(--theme-bg-card);
    border: 1px solid var(--theme-border);
    border-radius: @border-radius-xl;
    backdrop-filter: blur(10px);
    padding: @spacing-2xl;

    .status-header {
        display: flex;
        align-items: center;
        gap: @spacing-md;
        margin-bottom: @spacing-xl;
        padding-bottom: @spacing-lg;
        border-bottom: 1px solid var(--theme-border);

        i {
            font-size: 28px;
            color: var(--theme-info);
            filter: drop-shadow(0 0 10px var(--theme-info));
        }

        span {
            font-family: @font-display, 'Orbitron', sans-serif;
            font-size: @font-size-lg;
            font-weight: 600;
            letter-spacing: 0.1em;
            color: var(--theme-info);
            text-shadow: 0 0 10px var(--theme-info);
        }
    }

    .status-list {
        display: flex;
        flex-direction: column;
        gap: @spacing-lg;
    }

    .status-item {
        display: flex;
        justify-content: space-between;
        align-items: center;
        padding: @spacing-md @spacing-lg;
        background: var(--theme-bg-hover);
        border-radius: @border-radius-lg;
        border: 1px solid var(--theme-border);
        transition: all @transition-smooth;

        &:hover {
            border-color: var(--theme-info);
            background: var(--theme-bg-hover);
            opacity: 0.95;
        }

        .status-label {
            font-size: @font-size-sm;
            color: var(--theme-text-secondary);
            letter-spacing: 0.05em;
        }

        .status-value {
            font-family: @font-mono, 'Share Tech Mono', monospace;
            font-size: @font-size-sm;
            font-weight: 600;
            letter-spacing: 0.02em;
        }

        .status-frontend {
            color: var(--theme-success);
            text-shadow: 0 0 8px var(--theme-success);
        }

        .status-backend {
            color: var(--theme-info);
            text-shadow: 0 0 8px var(--theme-info);
        }

        .status-db {
            color: var(--theme-accent);
            text-shadow: 0 0 8px var(--theme-accent);
        }
    }
}

// ============================================
// 响应式设计
// ============================================

@media (max-width: @breakpoint-lg) {
    .logo-text {
        font-size: 72px;
        letter-spacing: 10px;
        gap: 8px;
    }

    .hero-title {
        font-size: @font-size-2xl;
    }

    .feature-grid {
        grid-template-columns: 1fr;
    }
}

@media (max-width: @breakpoint-md) {
    .hero-section {
        min-height: 70vh;
        padding: @spacing-2xl @spacing-md;
    }

    .logo-text {
        font-size: 56px;
        letter-spacing: 6px;
        gap: 4px;
    }

    .logo-subtitle .subtitle-line {
        width: 40px;
    }

    .hero-title {
        font-size: @font-size-xl;
    }

    .hero-subtitle {
        font-size: @font-size-md;
    }

    .tech-grid {
        grid-template-columns: 1fr;
    }

    .feature-grid {
        grid-template-columns: 1fr;
    }

    .hero-decoration .deco-line {
        width: 60px;
    }
}

@media (max-width: @breakpoint-sm) {
    .logo-text {
        font-size: 40px;
        letter-spacing: 4px;
    }

    .hero-title {
        font-size: @font-size-lg;
        padding: 0;
    }

    .hero-subtitle {
        font-size: @font-size-sm;
    }

    .status-card .status-item {
        flex-direction: column;
        align-items: flex-start;
        gap: @spacing-xs;
    }
}

// ============================================
// 动画
// ============================================

@keyframes title-underline {

    0%,
    100% {
        opacity: 0.6;
        transform: scaleX(0.8);
    }

    50% {
        opacity: 1;
        transform: scaleX(1);
    }
}
</style>
