const puppeteer = require('puppeteer')

async function testBrowser() {
    let browser = null
    try {
        console.log('启动浏览器...')
        browser = await puppeteer.launch({
            headless: false,
            args: ['--no-sandbox', '--disable-setuid-sandbox']
        })

        console.log('浏览器已启动，版本:', await browser.version())

        const page = await browser.newPage()
        console.log('新页面已创建')

        await page.goto('http://localhost:8099')
        console.log('已访问 http://localhost:8099')

        const title = await page.title()
        console.log('页面标题:', title)

        // 截图保存
        await page.screenshot({ path: 'test-screenshot.png' })
        console.log('截图已保存到 test-screenshot.png')

        // 检查#app元素
        const appElement = await page.$('#app')
        if (appElement) {
            console.log('找到#app元素')
        } else {
            console.log('未找到#app元素')
        }

        console.log('测试成功!')
        return true
    } catch (error) {
        console.error('测试失败:', error)
        return false
    } finally {
        if (browser) {
            await browser.close()
            console.log('浏览器已关闭')
        }
    }
}

// 运行测试
testBrowser().then(success => {
    process.exit(success ? 0 : 1)
})