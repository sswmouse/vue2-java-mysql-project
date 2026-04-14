/* global describe, beforeAll, test, expect, page */
describe('Vue应用首页测试', () => {
    beforeAll(async () => {
    // 访问首页
        await page.goto('http://localhost:8099')
        // 等待页面完全加载
        await page.waitForSelector('#app')
    })

    test('页面应该正确加载', async () => {
    // 验证页面标题
        const title = await page.title()
        expect(title).toBeTruthy()

        // 验证应用容器存在
        const appContainer = await page.$('#app')
        expect(appContainer).toBeTruthy()
    })

    test('应该显示欢迎信息或主要内容', async () => {
    // 等待主要内容区域加载
        await page.waitForSelector('.main-content', { timeout: 5000 })
            .catch(() => console.log('没有找到.main-content选择器，尝试其他选择器'))

        // 检查页面是否有可见文本
        const pageText = await page.evaluate(() => document.body.innerText)
        expect(pageText.length).toBeGreaterThan(10)
    })

    test('导航菜单应该正常工作', async () => {
    // 检查导航链接
        const navLinks = await page.$$('a, .nav-link, .router-link')
        expect(navLinks.length).toBeGreaterThan(0)

        // 如果有导航到用户页的链接，测试点击
        const userLinks = await page.$$('a[href*="user"], a[href*="User"]')
        if (userLinks.length > 0) {
            await userLinks[0].click()
            await new Promise(resolve => setTimeout(resolve, 1000))

            // 验证URL变化
            const currentUrl = await page.url()
            expect(currentUrl).toMatch(/user/i)

            // 返回首页
            await page.goBack()
            await page.waitForSelector('#app')
        }
    })

    test('页面应该没有明显的JavaScript错误', async () => {
    // 监听页面错误
        const errors = []
        page.on('pageerror', (error) => {
            errors.push(error.message)
        })

        // 刷新页面触发可能的错误
        await page.reload()
        await page.waitForSelector('#app')

        expect(errors).toHaveLength(0)
    })
})