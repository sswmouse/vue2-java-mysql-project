<template>
    <div class="card-manager">
        <!-- 工具栏 -->
        <div class="toolbar">
            <el-button
                type="primary"
                icon="el-icon-plus"
                @click="handleCreate"
            >
                生成卡片
            </el-button>
        </div>

        <!-- 卡片列表 -->
        <div class="card-list">
            <el-row :gutter="20">
                <el-col
                    v-for="card in cards"
                    :key="card.id"
                    :xs="24"
                    :sm="12"
                    :md="8"
                    :lg="6"
                >
                    <div
                        class="card-item"
                        :class="{ 'is-default': card.isDefault }"
                    >
                        <!-- 卡片预览 -->
                        <div
                            class="card-preview"
                            :class="'template-' + card.templateCode"
                        >
                            <div class="card-content">
                                <div class="avatar-section">
                                    <img
                                        v-if="character.avatarUrl"
                                        :src="character.avatarUrl"
                                        class="avatar"
                                    >
                                    <div
                                        v-else
                                        class="avatar-placeholder"
                                    >
                                        <i class="el-icon-user" />
                                    </div>
                                </div>
                                <h3 class="character-name">
                                    {{ character.characterName }}
                                </h3>
                                <p class="character-info">
                                    Lv.{{ character.level }} | {{ character.jobNature }}
                                </p>
                                <div class="stats-grid">
                                    <div class="stat-item">
                                        <span class="stat-label">名望</span>
                                        <span class="stat-value">{{ character.fameValue || 0 }}</span>
                                    </div>
                                    <div class="stat-item">
                                        <span class="stat-label">增幅</span>
                                        <span class="stat-value">+{{ enhancementData.amplify?.level || 0 }}</span>
                                    </div>
                                    <div class="stat-item">
                                        <span class="stat-label">属强</span>
                                        <span class="stat-value">{{ enhancementData.enchant?.elementValue || 0 }}</span>
                                    </div>
                                    <div class="stat-item">
                                        <span class="stat-label">装备</span>
                                        <span class="stat-value">{{ getEquipmentShortName() }}</span>
                                    </div>
                                </div>
                            </div>
                            <div class="template-badge">
                                {{ card.templateName }}
                            </div>
                        </div>

                        <!-- 操作按钮 -->
                        <div class="card-actions">
                            <span class="card-name">{{ card.cardName || '未命名' }}</span>
                            <div class="action-buttons">
                                <el-button
                                    v-if="!card.isDefault"
                                    size="mini"
                                    @click="handleSetDefault(card.id)"
                                >
                                    设为默认
                                </el-button>
                                <el-button
                                    v-if="card.isDefault"
                                    size="mini"
                                    type="success"
                                    plain
                                >
                                    默认卡片
                                </el-button>
                                <el-button
                                    size="mini"
                                    type="danger"
                                    @click="handleDelete(card.id)"
                                >
                                    删除
                                </el-button>
                            </div>
                        </div>
                    </div>
                </el-col>
            </el-row>

            <!-- 空状态 -->
            <div
                v-if="cards.length === 0"
                class="empty-state"
            >
                <i class="el-icon-picture-outline" />
                <p>还没有生成卡片</p>
                <p>点击"生成卡片"按钮创建你的角色卡片</p>
            </div>
        </div>

        <!-- 创建卡片弹窗 -->
        <el-dialog
            title="生成卡片"
            :visible.sync="dialogVisible"
            width="600px"
        >
            <el-form label-width="100px">
                <el-form-item label="卡片名称">
                    <el-input
                        v-model="form.cardName"
                        placeholder="给你的卡片起个名字"
                    />
                </el-form-item>
                <el-form-item label="选择模板">
                    <el-radio-group v-model="form.templateId">
                        <el-radio
                            v-for="template in templates"
                            :key="template.id"
                            :label="template.id"
                            class="template-option"
                        >
                            <div class="template-info">
                                <span>{{ template.templateName }}</span>
                            </div>
                        </el-radio>
                    </el-radio-group>
                </el-form-item>
            </el-form>
            <span slot="footer">
                <el-button @click="dialogVisible = false">取消</el-button>
                <el-button
                    type="primary"
                    :loading="saving"
                    @click="handleSubmit"
                >生成</el-button>
            </span>
        </el-dialog>
    </div>
</template>

<script>
import apiCard from '@/api/card'
import apiEnhancement from '@/api/enhancement'

export default {
    name: 'CardManager',
    props: {
        characterId: {
            type: Number,
            required: true
        },
        character: {
            type: Object,
            required: true
        }
    },
    data() {
        return {
            cards: [],
            templates: [],
            enhancementData: {},
            dialogVisible: false,
            saving: false,
            form: {
                cardName: '',
                templateId: null,
                isDefault: true
            }
        }
    },
    mounted() {
        this.loadCards()
        this.loadTemplates()
        this.loadEnhancementData()
    },
    methods: {
        getEquipmentShortName() {
            const name = this.enhancementData.equipment?.setName
            return name ? name.slice(0, 4) : '无'
        },
        async loadCards() {
            try {
                const data = await this.$request({
                    url: apiCard.getCharacterCards(this.characterId),
                    method: 'get'
                })
                this.cards = data || []
            } catch (error) {
                console.error('加载卡片失败:', error)
            }
        },
        async loadTemplates() {
            try {
                const data = await this.$request({
                    url: apiCard.templates,
                    method: 'get'
                })
                this.templates = data || []
                // 默认选中第一个模板
                if (this.templates.length > 0 && !this.form.templateId) {
                    const defaultTemplate = this.templates.find(t => t.isDefault) || this.templates[0]
                    this.form.templateId = defaultTemplate.id
                }
            } catch (error) {
                console.error('加载模板失败:', error)
            }
        },
        async loadEnhancementData() {
            try {
                const data = await this.$request({
                    url: apiEnhancement.getEnhancementData(this.characterId),
                    method: 'get'
                })
                this.enhancementData = data || {}
            } catch (error) {
                console.error('加载打造数据失败:', error)
            }
        },
        handleCreate() {
            this.dialogVisible = true
        },
        async handleSubmit() {
            if (!this.form.templateId) {
                this.$message.warning('请选择模板')
                return
            }
            this.saving = true
            try {
                await this.$request({
                    url: apiCard.createCard(this.characterId),
                    method: 'post',
                    data: this.form
                })
                this.$message.success('卡片生成成功')
                this.dialogVisible = false
                this.form.cardName = ''
                this.loadCards()
            } catch (error) {
                this.$message.error('生成失败')
            } finally {
                this.saving = false
            }
        },
        async handleSetDefault(cardId) {
            try {
                await this.$request({
                    url: apiCard.setDefault(cardId),
                    method: 'put'
                })
                this.$message.success('设置成功')
                this.loadCards()
            } catch (error) {
                this.$message.error('设置失败')
            }
        },
        async handleDelete(cardId) {
            try {
                await this.$confirm('确定要删除这张卡片吗?', '提示', {
                    type: 'warning'
                })
                await this.$request({
                    url: apiCard.deleteCard(cardId),
                    method: 'delete'
                })
                this.$message.success('删除成功')
                this.loadCards()
            } catch (error) {
                if (error !== 'cancel') {
                    this.$message.error('删除失败')
                }
            }
        }
    }
}
</script>

<style lang="less" scoped>
.card-manager {
    padding: 20px;

    .toolbar {
        margin-bottom: 24px;
    }

    .card-list {
        .card-item {
            margin-bottom: 24px;
            border-radius: 12px;
            overflow: hidden;
            background: var(--theme-bg-card);
            transition: transform 0.3s, box-shadow 0.3s;

            &:hover {
                transform: translateY(-4px);
                box-shadow: 0 8px 24px rgba(0, 0, 0, 0.3);
            }

            &.is-default {
                border: 2px solid var(--theme-accent);
            }
        }

        .card-preview {
            height: 280px;
            padding: 20px;
            background: linear-gradient(135deg, #1a1a2e, #16213e);
            color: #fff;
            position: relative;

            .card-content {
                text-align: center;
            }

            .avatar-section {
                margin-bottom: 12px;

                .avatar {
                    width: 80px;
                    height: 80px;
                    border-radius: 50%;
                    border: 3px solid rgba(255, 255, 255, 0.3);
                }

                .avatar-placeholder {
                    width: 80px;
                    height: 80px;
                    border-radius: 50%;
                    background: rgba(255, 255, 255, 0.1);
                    margin: 0 auto;
                    display: flex;
                    align-items: center;
                    justify-content: center;
                    font-size: 32px;
                }
            }

            .character-name {
                margin: 8px 0;
                font-size: 20px;
                color: #ffd700;
            }

            .character-info {
                margin: 4px 0;
                font-size: 12px;
                color: rgba(255, 255, 255, 0.7);
            }

            .stats-grid {
                display: grid;
                grid-template-columns: repeat(2, 1fr);
                gap: 8px;
                margin-top: 16px;
                padding: 12px;
                background: rgba(0, 0, 0, 0.2);
                border-radius: 8px;

                .stat-item {
                    text-align: center;

                    .stat-label {
                        display: block;
                        font-size: 11px;
                        color: rgba(255, 255, 255, 0.6);
                    }

                    .stat-value {
                        font-size: 16px;
                        font-weight: bold;
                    }
                }
            }

            .template-badge {
                position: absolute;
                top: 10px;
                right: 10px;
                padding: 4px 8px;
                background: rgba(0, 0, 0, 0.5);
                border-radius: 4px;
                font-size: 11px;
            }
        }

        .card-actions {
            padding: 12px 16px;
            display: flex;
            justify-content: space-between;
            align-items: center;

            .card-name {
                font-size: 14px;
                color: var(--theme-text-secondary);
            }

            .action-buttons {
                display: flex;
                gap: 8px;
            }
        }

        .empty-state {
            text-align: center;
            padding: 60px 20px;
            color: var(--theme-text-secondary);

            i {
                font-size: 64px;
                margin-bottom: 16px;
            }

            p {
                margin: 8px 0;
            }
        }
    }

    .template-option {
        display: block;
        margin-bottom: 12px;

        .template-info {
            display: inline-block;
            margin-left: 8px;
        }
    }
}
</style>
