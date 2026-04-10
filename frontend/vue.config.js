module.exports = {
    devServer: {
        port: 8099,
        proxy: {
            '/api': {
                target: 'http://localhost:3000',
                changeOrigin: true,
                pathRewrite: {
                    '^/api': ''
                }
            },
            '/uploads': {
                target: 'http://localhost:3000',
                changeOrigin: true
            }
        }
    }
}