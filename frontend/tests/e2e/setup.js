// E2E测试全局配置
jest.setTimeout(30000)

// 测试前等待应用完全加载
beforeAll(async () => {
  // 可以在这里添加全局测试前置条件
})

// 每个测试后清理 - 暂时禁用，可能干扰jest-puppeteer
// afterEach(async () => {
//   // 清除浏览器缓存或重置状态
//   const pages = await browser.pages()
//   for (const page of pages) {
//     if (page.url() !== 'about:blank') {
//       await page.close()
//     }
//   }
// })

// 所有测试结束后清理 - jest-puppeteer会自动处理
// afterAll(async () => {
//   await browser.close()
// })