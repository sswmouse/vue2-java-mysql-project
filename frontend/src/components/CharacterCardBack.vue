<!--
 * @Description: 角色卡片背面组件 - 炉石传说风格
 * @Author: Claude Code
 * @Date: 2026-04-11
 * @LastEditTime: 2026-04-11 02:13:28
 * @FilePath: /vue2-java-mysql-project/frontend/src/components/CharacterCardBack.vue
 -->
<template>
    <div class="card-back-image-container">
        <!-- 卡片背面图片 -->
        <img
            :src="cardBackImage"
            :alt="'卡片背面'"
            class="card-back-image"
        />
    </div>
</template>

<script>
export default {
    name: 'CharacterCardBack',
    props: {
        character: {
            type: Object,
            required: true
        }
    },
    computed: {
        cardBackImage() {
            // 使用相对路径导入图片
            return require('@/assets/img/cardBack.png')
        }
    }
}
</script>

<style lang="less" scoped>
.card-back-image-container {
    width: 100%;
    height: 100%;
    position: relative;
    border-radius: 16px;
    overflow: hidden;

    .card-back-image {
        width: 100%;
        height: 100%;
        object-fit: cover;
        border-radius: 16px;
    }
}
</style>
