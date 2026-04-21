<!--
 * @Description: 角色卡片背面组件
 * @Author: Claude Code
 * @Date: 2026-04-11
 * @FilePath: /vue2-java-mysql-project/frontend/src/components/CharacterCardBack.vue
 -->
<template>
    <div class="card-back-container">
        <!-- 卡片背面图片 -->
        <div class="card-back-image-container">
            <img
                :src="cardBackImage"
                :alt="'卡片背面'"
                class="card-back-image"
            >
        </div>

        <!-- 操作按钮 -->
        <div class="card-back-actions">
        </div>
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
            return require('@/assets/img/cardBack.png')
        }
    },
    methods: {
        handleEdit() {
            this.$emit('edit', this.character)
        }
    }
}
</script>

<style lang="less" scoped>
.card-back-container {
    width: 100%;
    height: 100%;
    position: relative;
    border-radius: 16px;
    overflow: hidden;

    .card-back-image-container {
        width: 100%;
        height: 100%;
        border-radius: 16px;
        overflow: hidden;

        .card-back-image {
            width: 100%;
            height: 100%;
            object-fit: cover;
        }
    }

    .card-back-actions {
        position: absolute;
        bottom: 24px;
        left: 50%;
        transform: translateX(-50%);
        display: flex;
        gap: 16px;

        .action-btn {
            display: flex;
            flex-direction: column;
            align-items: center;
            justify-content: center;
            width: 70px;
            height: 70px;
            background: rgba(0, 0, 0, 0.6);
            backdrop-filter: blur(10px);
            border: 2px solid rgba(255, 255, 255, 0.2);
            border-radius: 16px;
            cursor: pointer;
            transition: all 0.3s ease;

            i {
                font-size: 24px;
                color: #fff;
                margin-bottom: 4px;
            }

            span {
                font-size: 12px;
                color: rgba(255, 255, 255, 0.9);
            }

            &:hover {
                transform: translateY(-4px);
                border-color: rgba(255, 255, 255, 0.5);
                box-shadow: 0 8px 24px rgba(0, 0, 0, 0.4);
            }
        }

        .edit-btn {
            &:hover {
                background: rgba(64, 158, 255, 0.7);
                border-color: #409eff;
            }
        }

        .delete-btn {
            &:hover {
                background: rgba(245, 108, 108, 0.7);
                border-color: #f56c6c;
            }
        }
    }
}
</style>
