<!--
 * @Description: 应用根组件
 * @Author: System
 * @Date: 2026-04-10
 * @LastEditTime: 2026-04-10
 * @FilePath: /frontend/src/App.vue
 -->
<template>
    <div id="app">
        <div id="nav">
            <router-link to="/">
                首页
            </router-link> |
            <router-link to="/users">
                用户管理
            </router-link>
        </div>
        <router-view />
    </div>
</template>

<script>
export default {
    name: 'App'
}
</script>

<style lang="less" scoped>
#app {
    font-family: Avenir, Helvetica, Arial, sans-serif;
    -webkit-font-smoothing: antialiased;
    -moz-osx-font-smoothing: grayscale;
    text-align: center;
    color: #2c3e50;
    margin-top: 60px;

    #nav {
        padding: 30px;

        a {
            font-weight: bold;
            color: #2c3e50;
        }

        a.router-link-exact-active {
            color: #42b983;
        }
    }
}
</style>