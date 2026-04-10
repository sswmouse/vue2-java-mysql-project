module.exports = {
    launch: {
        headless: false, // 显示浏览器窗口以便调试
        defaultViewport: { width: 1280, height: 720 },
        args: ['--no-sandbox', '--disable-setuid-sandbox', '--window-size=1280,720']
    }
    // 使用已经运行在端口8099的前端开发服务器
    // server: false
}