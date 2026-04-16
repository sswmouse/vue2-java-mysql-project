module.exports = {
    devServer: {
        port: 8099,
        proxy: {
            '/api': {
                target: 'http://localhost:3000',
                changeOrigin: true
            },
            '/auth': {
                target: 'http://localhost:3000',
                changeOrigin: true
            },
            '/uploads': {
                target: 'http://localhost:3000',
                changeOrigin: true
            }
        }
    }
}