<!--
 * @Description: 角色管理页面
 * @Author: Claude Code
 * @Date: 2026-04-10
 * @LastEditTime: 2026-04-10
 * @FilePath: /frontend/src/views/Role.vue
 -->
<template>
    <div class="role-page">
        <div class="page-header">
            <h1>我的角色</h1>
            <el-button
                type="primary"
                icon="el-icon-plus"
                @click="handleCreate"
            >
                创建角色
            </el-button>
        </div>

        <div
            v-loading="loading"
            class="role-content"
        >
            <!-- 空状态 -->
            <div
                v-if="!loading && characters.length === 0"
                class="empty-state"
            >
                <i class="el-icon-user" />
                <p>还没有角色</p>
                <p>点击右上角"创建角色"按钮开始吧</p>
            </div>

            <!-- 角色卡片网格 -->
            <div
                v-else-if="!loading"
                class="character-grid"
            >
                <character-card
                    v-for="character in characters"
                    :key="character.id"
                    :character="character"
                />
            </div>
        </div>

        <!-- 创建/编辑角色弹窗 -->
        <character-dialog
            :visible.sync="dialogVisible"
            :character="editingCharacter"
            @success="loadCharacters"
        />
    </div>
</template>

<script>
import api from '@/api'
import CharacterCard from '@/components/CharacterCard.vue'
import CharacterDialog from '@/components/CharacterDialog.vue'

export default {
    name: 'Role',
    components: {
        CharacterCard,
        CharacterDialog
    },
    data() {
        return {
            loading: false,
            characters: [],
            dialogVisible: false,
            editingCharacter: null,
            // 默认绑定admin账户（userId = 1）
            currentUserId: 1
        }
    },
    mounted() {
        this.loadCharacters()
    },
    methods: {
        async loadCharacters() {
            this.loading = true
            try {
                const data = await this.$request({
                    url: api.character.list(this.currentUserId),
                    method: 'get'
                })
                this.characters = data || []
            } catch (error) {
                console.error('加载角色列表失败:', error)
                this.$message.error('加载角色列表失败')
                this.characters = []
            } finally {
                this.loading = false
            }
        },

        handleCreate() {
            this.editingCharacter = null
            this.dialogVisible = true
        }
    }
}
</script>

<style lang="less" scoped>
.role-page {
    padding: 24px;
    min-height: calc(100vh - 60px);
    background: linear-gradient(135deg, #1a1a2e 0%, #16213e 50%, #0f3460 100%);

    .page-header {
        display: flex;
        justify-content: space-between;
        align-items: center;
        margin-bottom: 32px;
        padding: 0 8px;

        h1 {
            margin: 0;
            font-size: 28px;
            font-weight: 600;
            color: #ffffff;
            text-shadow: 0 2px 8px rgba(0, 0, 0, 0.3);
        }

        .el-button {
            padding: 12px 24px;
            font-size: 14px;
            font-weight: 500;
            border-radius: 8px;
            background: linear-gradient(135deg, #ffd700 0%, #ffaa00 100%);
            border: none;
            color: #1a1a2e;
            box-shadow: 0 4px 12px rgba(255, 215, 0, 0.3);

            &:hover {
                background: linear-gradient(135deg, #ffe033 0%, #ffbb33 100%);
                box-shadow: 0 6px 16px rgba(255, 215, 0, 0.4);
            }

            &:active {
                transform: translateY(1px);
            }

            i {
                font-size: 16px;
            }
        }
    }

    .role-content {
        .empty-state {
            display: flex;
            flex-direction: column;
            align-items: center;
            justify-content: center;
            padding: 80px 20px;
            background: rgba(255, 255, 255, 0.05);
            border-radius: 16px;
            border: 2px dashed rgba(255, 255, 255, 0.2);

            i {
                font-size: 80px;
                color: rgba(255, 255, 255, 0.3);
                margin-bottom: 20px;
            }

            p {
                margin: 8px 0;
                color: rgba(255, 255, 255, 0.6);

                &:first-of-type {
                    font-size: 18px;
                    font-weight: 500;
                    color: rgba(255, 255, 255, 0.8);
                }

                &:last-of-type {
                    font-size: 14px;
                    color: rgba(255, 255, 255, 0.4);
                }
            }
        }

        .character-grid {
            display: grid;
            grid-template-columns: repeat(auto-fill, minmax(280px, 1fr));
            gap: 24px;
            padding: 8px;

            @media (min-width: 1200px) {
                grid-template-columns: repeat(4, 1fr);
            }

            @media (min-width: 992px) and (max-width: 1199px) {
                grid-template-columns: repeat(3, 1fr);
            }

            @media (max-width: 991px) {
                grid-template-columns: repeat(2, 1fr);
            }

            @media (max-width: 576px) {
                grid-template-columns: 1fr;
            }
        }
    }
}
</style>
