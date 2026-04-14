module.exports = {
    presets: [
        '@vue/cli-plugin-babel/preset'
    ],
    plugins: [
    // 支持可选链操作符 (?.)
        '@babel/plugin-proposal-optional-chaining'
    ]
}
