<!--
 * @Description: 时装管理页面
 * @Author: Claude Code
 * @Date: 2026-04-10
 * @LastEditTime: 2026-04-10
 * @FilePath: /frontend/src/views/Fashion.vue
 -->
<template>
    <div class="fashion-page">
        <div class="iframe-container">
            <iframe
                src="https://dnf.qq.com/act/a20260114cloth/index.html"
                frameborder="0"
                allowfullscreen
            />
        </div>
    </div>
</template>

<script>
export default {
    name: 'Fashion'
}
</script><style lang="less" scoped>
.fashion-page {
    height: calc(100vh - 50px);
    padding: 0;

    .iframe-container {
        width: 100%;
        height: 100%;

        iframe {
            width: 100%;
            height: 100%;
            display: block;
            border: none;
        }
    }
}
</style>