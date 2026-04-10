/*
 * @Description: GSAP动画工具库
 * @Author: Claude Code
 * @Date: 2026-04-10
 * @LastEditTime: 2026-04-10
 * @FilePath: /frontend/src/utils/animations.js
 */

import { gsap } from 'gsap'

// ============================================
// 页面过渡动画
// ============================================

/**
 * 页面淡入动画
 * @param {HTMLElement} element - 要动画的元素
 * @param {number} duration - 动画时长（秒）
 * @param {number} delay - 动画延迟（秒）
 * @returns {Promise} 动画完成的Promise
 */
export const pageFadeIn = (element, duration = 0.5, delay = 0) => {
    return new Promise((resolve) => {
        gsap.fromTo(element,
            {
                opacity: 0,
                y: 30,
                scale: 0.95
            },
            {
                opacity: 1,
                y: 0,
                scale: 1,
                duration,
                delay,
                ease: 'power2.out',
                onComplete: resolve
            }
        )
    })
}

/**
 * 页面淡出动画
 * @param {HTMLElement} element - 要动画的元素
 * @param {number} duration - 动画时长（秒）
 * @returns {Promise} 动画完成的Promise
 */
export const pageFadeOut = (element, duration = 0.3) => {
    return new Promise((resolve) => {
        gsap.to(element,
            {
                opacity: 0,
                y: -20,
                scale: 0.98,
                duration,
                ease: 'power2.in',
                onComplete: resolve
            }
        )
    })
}

/**
 * 页面滑动进入动画
 * @param {HTMLElement} element - 要动画的元素
 * @param {string} direction - 滑动方向：'left', 'right', 'top', 'bottom'
 * @param {number} duration - 动画时长（秒）
 * @returns {Promise} 动画完成的Promise
 */
export const pageSlideIn = (element, direction = 'left', duration = 0.6) => {
    const positions = {
        left: { x: -100, y: 0 },
        right: { x: 100, y: 0 },
        top: { x: 0, y: -50 },
        bottom: { x: 0, y: 50 }
    }

    const from = positions[direction] || positions.left

    return new Promise((resolve) => {
        gsap.fromTo(element,
            {
                opacity: 0,
                x: from.x,
                y: from.y
            },
            {
                opacity: 1,
                x: 0,
                y: 0,
                duration,
                ease: 'power3.out',
                onComplete: resolve
            }
        )
    })
}

/**
 * 页面缩放进入动画
 * @param {HTMLElement} element - 要动画的元素
 * @param {number} duration - 动画时长（秒）
 * @returns {Promise} 动画完成的Promise
 */
export const pageZoomIn = (element, duration = 0.5) => {
    return new Promise((resolve) => {
        gsap.fromTo(element,
            {
                opacity: 0,
                scale: 0.8,
                rotationY: 10
            },
            {
                opacity: 1,
                scale: 1,
                rotationY: 0,
                duration,
                ease: 'back.out(1.7)',
                onComplete: resolve
            }
        )
    })
}

/**
 * 交错显示元素列表
 * @param {Array|NodeList} elements - 元素数组或NodeList
 * @param {number} stagger - 交错延迟（秒）
 * @param {number} duration - 每个元素的动画时长（秒）
 * @returns {Promise} 动画完成的Promise
 */
export const staggerFadeIn = (elements, stagger = 0.1, duration = 0.3) => {
    return new Promise((resolve) => {
        gsap.fromTo(elements,
            {
                opacity: 0,
                y: 20
            },
            {
                opacity: 1,
                y: 0,
                duration,
                stagger,
                ease: 'power2.out',
                onComplete: resolve
            }
        )
    })
}

// ============================================
// 交互反馈动画
// ============================================

/**
 * 按钮悬停动画
 * @param {HTMLElement} element - 按钮元素
 * @param {number} duration - 动画时长（秒）
 * @returns {gsap.core.Tween} GSAP动画实例
 */
export const buttonHover = (element, duration = 0.2) => {
    return gsap.to(element, {
        scale: 1.05,
        boxShadow: '0 10px 25px rgba(106, 17, 203, 0.3)',
        duration,
        ease: 'power2.out'
    })
}

/**
 * 按钮离开动画
 * @param {HTMLElement} element - 按钮元素
 * @param {number} duration - 动画时长（秒）
 * @returns {gsap.core.Tween} GSAP动画实例
 */
export const buttonLeave = (element, duration = 0.2) => {
    return gsap.to(element, {
        scale: 1,
        boxShadow: '0 2px 8px rgba(0, 0, 0, 0.1)',
        duration,
        ease: 'power2.out'
    })
}

/**
 * 按钮点击动画
 * @param {HTMLElement} element - 按钮元素
 * @returns {gsap.core.Tween} GSAP动画实例
 */
export const buttonClick = (element) => {
    return gsap.to(element, {
        scale: 0.95,
        duration: 0.1,
        yoyo: true,
        repeat: 1
    })
}

/**
 * 卡片悬停动画
 * @param {HTMLElement} element - 卡片元素
 * @param {number} duration - 动画时长（秒）
 * @returns {gsap.core.Tween} GSAP动画实例
 */
export const cardHover = (element, duration = 0.3) => {
    return gsap.to(element, {
        y: -8,
        scale: 1.02,
        boxShadow: '0 20px 40px rgba(106, 17, 203, 0.25)',
        duration,
        ease: 'power2.out'
    })
}

/**
 * 卡片离开动画
 * @param {HTMLElement} element - 卡片元素
 * @param {number} duration - 动画时长（秒）
 * @returns {gsap.core.Tween} GSAP动画实例
 */
export const cardLeave = (element, duration = 0.3) => {
    return gsap.to(element, {
        y: 0,
        scale: 1,
        boxShadow: '0 4px 12px rgba(0, 0, 0, 0.15)',
        duration,
        ease: 'power2.out'
    })
}

/**
 * 输入框焦点动画
 * @param {HTMLElement} element - 输入框元素
 * @returns {gsap.core.Tween} GSAP动画实例
 */
export const inputFocus = (element) => {
    return gsap.to(element, {
        boxShadow: '0 0 0 3px rgba(106, 17, 203, 0.2)',
        borderColor: '#6a11cb',
        duration: 0.2,
        ease: 'power2.out'
    })
}

/**
 * 输入框失去焦点动画
 * @param {HTMLElement} element - 输入框元素
 * @returns {gsap.core.Tween} GSAP动画实例
 */
export const inputBlur = (element) => {
    return gsap.to(element, {
        boxShadow: 'none',
        borderColor: '#334155',
        duration: 0.2,
        ease: 'power2.out'
    })
}

// ============================================
// 菜单和导航动画
// ============================================

/**
 * 侧边栏展开动画
 * @param {HTMLElement} element - 侧边栏元素
 * @param {number} duration - 动画时长（秒）
 * @returns {Promise} 动画完成的Promise
 */
export const sidebarExpand = (element, duration = 0.4) => {
    return new Promise((resolve) => {
        gsap.fromTo(element,
            {
                x: -200,
                opacity: 0
            },
            {
                x: 0,
                opacity: 1,
                duration,
                ease: 'power3.out',
                onComplete: resolve
            }
        )
    })
}

/**
 * 侧边栏收起动画
 * @param {HTMLElement} element - 侧边栏元素
 * @param {number} duration - 动画时长（秒）
 * @returns {Promise} 动画完成的Promise
 */
export const sidebarCollapse = (element, duration = 0.3) => {
    return new Promise((resolve) => {
        gsap.to(element,
            {
                x: -200,
                opacity: 0,
                duration,
                ease: 'power3.in',
                onComplete: resolve
            }
        )
    })
}

/**
 * 下拉菜单展开动画
 * @param {HTMLElement} element - 下拉菜单元素
 * @returns {Promise} 动画完成的Promise
 */
export const dropdownExpand = (element) => {
    return new Promise((resolve) => {
        gsap.fromTo(element,
            {
                scaleY: 0,
                opacity: 0,
                transformOrigin: 'top'
            },
            {
                scaleY: 1,
                opacity: 1,
                duration: 0.3,
                ease: 'back.out(1.7)',
                onComplete: resolve
            }
        )
    })
}

/**
 * 下拉菜单收起动画
 * @param {HTMLElement} element - 下拉菜单元素
 * @returns {Promise} 动画完成的Promise
 */
export const dropdownCollapse = (element) => {
    return new Promise((resolve) => {
        gsap.to(element,
            {
                scaleY: 0,
                opacity: 0,
                duration: 0.2,
                ease: 'power2.in',
                onComplete: resolve
            }
        )
    })
}

/**
 * 菜单项悬停动画
 * @param {HTMLElement} element - 菜单项元素
 * @returns {gsap.core.Tween} GSAP动画实例
 */
export const menuItemHover = (element) => {
    return gsap.to(element, {
        x: 5,
        backgroundColor: 'rgba(106, 17, 203, 0.2)',
        duration: 0.2,
        ease: 'power2.out'
    })
}

/**
 * 菜单项离开动画
 * @param {HTMLElement} element - 菜单项元素
 * @returns {gsap.core.Tween} GSAP动画实例
 */
export const menuItemLeave = (element) => {
    return gsap.to(element, {
        x: 0,
        backgroundColor: 'transparent',
        duration: 0.2,
        ease: 'power2.out'
    })
}

// ============================================
// Logo和品牌动画
// ============================================

/**
 * Logo发光动画
 * @param {HTMLElement} element - Logo元素
 * @returns {gsap.core.Timeline} GSAP时间线实例
 */
export const logoGlow = (element) => {
    const timeline = gsap.timeline({ repeat: -1, yoyo: true })

    timeline.to(element, {
        filter: 'drop-shadow(0 0 10px rgba(212, 175, 55, 0.7))',
        textShadow: '0 0 15px rgba(212, 175, 55, 0.8)',
        duration: 1.5,
        ease: 'power2.inOut'
    }).to(element, {
        filter: 'drop-shadow(0 0 5px rgba(212, 175, 55, 0.3))',
        textShadow: '0 0 8px rgba(212, 175, 55, 0.4)',
        duration: 1.5,
        ease: 'power2.inOut'
    })

    return timeline
}

/**
 * Logo浮动动画
 * @param {HTMLElement} element - Logo元素
 * @returns {gsap.core.Timeline} GSAP时间线实例
 */
export const logoFloat = (element) => {
    const timeline = gsap.timeline({ repeat: -1, yoyo: true })

    timeline.to(element, {
        y: -5,
        rotation: 1,
        duration: 3,
        ease: 'power1.inOut'
    }).to(element, {
        y: 5,
        rotation: -1,
        duration: 3,
        ease: 'power1.inOut'
    })

    return timeline
}

/**
 * Logo脉动动画
 * @param {HTMLElement} element - Logo元素
 * @returns {gsap.core.Timeline} GSAP时间线实例
 */
export const logoPulse = (element) => {
    const timeline = gsap.timeline({ repeat: -1, yoyo: true })

    timeline.to(element, {
        scale: 1.1,
        opacity: 0.9,
        duration: 2,
        ease: 'power1.inOut'
    })

    return timeline
}

// ============================================
// 通知和提示动画
// ============================================

/**
 * 通知进入动画
 * @param {HTMLElement} element - 通知元素
 * @returns {Promise} 动画完成的Promise
 */
export const notificationEnter = (element) => {
    return new Promise((resolve) => {
        gsap.fromTo(element,
            {
                x: 100,
                opacity: 0
            },
            {
                x: 0,
                opacity: 1,
                duration: 0.4,
                ease: 'back.out(1.7)',
                onComplete: resolve
            }
        )
    })
}

/**
 * 通知退出动画
 * @param {HTMLElement} element - 通知元素
 * @returns {Promise} 动画完成的Promise
 */
export const notificationExit = (element) => {
    return new Promise((resolve) => {
        gsap.to(element,
            {
                x: 100,
                opacity: 0,
                duration: 0.3,
                ease: 'power2.in',
                onComplete: resolve
            }
        )
    })
}

/**
 * 成功提示动画
 * @param {HTMLElement} element - 成功提示元素
 * @returns {gsap.core.Timeline} GSAP时间线实例
 */
export const successAnimation = (element) => {
    const timeline = gsap.timeline()

    timeline.fromTo(element,
        {
            scale: 0,
            rotation: -180
        },
        {
            scale: 1,
            rotation: 0,
            duration: 0.5,
            ease: 'back.out(1.7)'
        }
    ).to(element, {
        scale: 1.2,
        duration: 0.2,
        ease: 'power2.out',
        yoyo: true,
        repeat: 1
    })

    return timeline
}

/**
 * 错误提示动画
 * @param {HTMLElement} element - 错误提示元素
 * @returns {gsap.core.Timeline} GSAP时间线实例
 */
export const errorAnimation = (element) => {
    const timeline = gsap.timeline()

    timeline.fromTo(element,
        {
            scale: 0,
            rotation: 180
        },
        {
            scale: 1,
            rotation: 0,
            duration: 0.4,
            ease: 'back.out(1.7)'
        }
    ).to(element, {
        x: 10,
        duration: 0.1,
        ease: 'power2.out',
        yoyo: true,
        repeat: 3
    })

    return timeline
}

// ============================================
// 加载状态动画
// ============================================

/**
 * 骨架屏加载动画
 * @param {HTMLElement} element - 骨架屏元素
 * @returns {gsap.core.Timeline} GSAP时间线实例
 */
export const skeletonLoading = (element) => {
    const timeline = gsap.timeline({ repeat: -1 })

    timeline.to(element, {
        backgroundPosition: '200px 0',
        duration: 1.5,
        ease: 'none'
    })

    return timeline
}

/**
 * 旋转加载动画
 * @param {HTMLElement} element - 加载指示器元素
 * @returns {gsap.core.Timeline} GSAP时间线实例
 */
export const spinLoading = (element) => {
    const timeline = gsap.timeline({ repeat: -1 })

    timeline.to(element, {
        rotation: 360,
        duration: 1,
        ease: 'none'
    })

    return timeline
}

/**
 * 脉动加载动画
 * @param {Array|NodeList} elements - 加载点元素数组
 * @returns {gsap.core.Timeline} GSAP时间线实例
 */
export const pulseLoading = (elements) => {
    const timeline = gsap.timeline({ repeat: -1 })

    elements.forEach((dot, index) => {
        timeline.to(dot, {
            scale: 1.5,
            opacity: 0.7,
            duration: 0.5,
            ease: 'power2.inOut',
            delay: index * 0.1
        }, 0)
    })

    timeline.to(elements, {
        scale: 1,
        opacity: 1,
        duration: 0.5,
        ease: 'power2.inOut'
    }, 0.5)

    return timeline
}

// ============================================
// 游戏风格特效
// ============================================

/**
 * 伤害数字弹出动画
 * @param {HTMLElement} element - 伤害数字元素
 * @param {number} damage - 伤害值
 * @returns {Promise} 动画完成的Promise
 */
export const damagePopup = (element, damage) => {
    element.textContent = damage

    return new Promise((resolve) => {
        gsap.fromTo(element,
            {
                y: 0,
                opacity: 0,
                scale: 0.5
            },
            {
                y: -60,
                opacity: 1,
                scale: 1,
                duration: 0.2,
                ease: 'power2.out'
            }
        ).to(element, {
            y: -100,
            opacity: 0,
            scale: 0.8,
            duration: 0.8,
            ease: 'power2.in',
            delay: 0.2,
            onComplete: resolve
        })
    })
}

/**
 * 暴击特效动画
 * @param {HTMLElement} element - 暴击特效元素
 * @returns {Promise} 动画完成的Promise
 */
export const criticalHit = (element) => {
    return new Promise((resolve) => {
        gsap.fromTo(element,
            {
                scale: 1,
                filter: 'brightness(1)'
            },
            {
                scale: 1.5,
                filter: 'brightness(2)',
                duration: 0.1,
                ease: 'power2.out'
            }
        ).to(element, {
            scale: 1,
            filter: 'brightness(1)',
            duration: 0.4,
            ease: 'power2.in',
            onComplete: resolve
        })
    })
}

/**
 * 获得物品闪光动画
 * @param {HTMLElement} element - 物品元素
 * @returns {Promise} 动画完成的Promise
 */
export const itemObtained = (element) => {
    return new Promise((resolve) => {
        gsap.fromTo(element,
            {
                boxShadow: '0 0 0 0 rgba(212, 175, 55, 0)'
            },
            {
                boxShadow: '0 0 30px 10px rgba(212, 175, 55, 0.7)',
                duration: 0.3,
                ease: 'power2.out'
            }
        ).to(element, {
            boxShadow: '0 0 0 0 rgba(212, 175, 55, 0)',
            duration: 0.7,
            ease: 'power2.in',
            onComplete: resolve
        })
    })
}

/**
 * 升级光芒动画
 * @param {HTMLElement} element - 升级元素
 * @returns {Promise} 动画完成的Promise
 */
export const levelUp = (element) => {
    return new Promise((resolve) => {
        const overlay = document.createElement('div')
        overlay.style.position = 'absolute'
        overlay.style.top = '0'
        overlay.style.left = '0'
        overlay.style.right = '0'
        overlay.style.bottom = '0'
        overlay.style.background = 'linear-gradient(135deg, #6a11cb, #d4af37)'
        overlay.style.zIndex = '-1'
        overlay.style.opacity = '0'

        element.style.position = 'relative'
        element.appendChild(overlay)

        gsap.to(overlay, {
            opacity: 1,
            duration: 0.5,
            ease: 'power2.out'
        }).to(overlay, {
            opacity: 0,
            duration: 0.5,
            ease: 'power2.in',
            delay: 0.5,
            onComplete: () => {
                element.removeChild(overlay)
                resolve()
            }
        })
    })
}

// ============================================
// 工具函数
// ============================================

/**
 * 创建动画时间线
 * @returns {gsap.core.Timeline} GSAP时间线实例
 */
export const createTimeline = () => {
    return gsap.timeline()
}

/**
 * 停止所有动画
 * @param {HTMLElement} element - 要停止动画的元素
 */
export const stopAnimations = (element) => {
    gsap.killTweensOf(element)
}

/**
 * 暂停所有动画
 */
export const pauseAnimations = () => {
    gsap.globalTimeline.pause()
}

/**
 * 恢复所有动画
 */
export const resumeAnimations = () => {
    gsap.globalTimeline.resume()
}

/**
 * 检查是否支持减少动画
 * @returns {boolean} 是否支持减少动画
 */
export const prefersReducedMotion = () => {
    return window.matchMedia('(prefers-reduced-motion: reduce)').matches
}

/**
 * 安全执行动画（考虑减少动画设置）
 * @param {Function} animationFunc - 动画函数
 * @param {HTMLElement} element - 动画元素
 * @param {...any} args - 动画参数
 * @returns {Promise|gsap.core.Tween|null} 动画结果或null
 */
export const safeAnimation = (animationFunc, element, ...args) => {
    if (prefersReducedMotion()) {
        return null
    }
    return animationFunc(element, ...args)
}

export default {
    // 页面过渡动画
    pageFadeIn,
    pageFadeOut,
    pageSlideIn,
    pageZoomIn,
    staggerFadeIn,

    // 交互反馈动画
    buttonHover,
    buttonLeave,
    buttonClick,
    cardHover,
    cardLeave,
    inputFocus,
    inputBlur,

    // 菜单和导航动画
    sidebarExpand,
    sidebarCollapse,
    dropdownExpand,
    dropdownCollapse,
    menuItemHover,
    menuItemLeave,

    // Logo和品牌动画
    logoGlow,
    logoFloat,
    logoPulse,

    // 通知和提示动画
    notificationEnter,
    notificationExit,
    successAnimation,
    errorAnimation,

    // 加载状态动画
    skeletonLoading,
    spinLoading,
    pulseLoading,

    // 游戏风格特效
    damagePopup,
    criticalHit,
    itemObtained,
    levelUp,

    // 工具函数
    createTimeline,
    stopAnimations,
    pauseAnimations,
    resumeAnimations,
    prefersReducedMotion,
    safeAnimation
}