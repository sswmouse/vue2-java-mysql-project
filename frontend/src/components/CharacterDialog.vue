<!--
 * @Description: 角色添加/编辑弹窗组件
 * @Author: Claude Code
 * @Date: 2026-04-10
 * @LastEditTime: 2026-04-21
 * @FilePath: /vue2-java-mysql-project/frontend/src/components/CharacterDialog.vue
 -->
<template>
    <el-dialog
        :title="dialogTitle"
        :visible.sync="visible"
        :close-on-click-modal="false"
        width="500px"
        @close="handleClose"
    >
        <el-form
            ref="characterForm"
            :model="form"
            :rules="rules"
            label-width="100px"
            class="character-form"
        >
            <!-- 角色名称 -->
            <el-form-item
                label="角色名称"
                prop="characterName"
            >
                <el-input
                    v-model="form.characterName"
                    placeholder="请输入角色名称"
                    maxlength="20"
                    show-word-limit
                />
            </el-form-item>

            <!-- 角色类型 - 联级选择器 -->
            <el-form-item
                label="角色类型"
                prop="characterTypeId"
            >
                <el-cascader
                    v-model="selectedCharacterType"
                    :options="characterTypeOptions"
                    :props="cascaderProps"
                    placeholder="请选择角色类型"
                    style="width: 100%"
                    @change="handleCharacterTypeChange"
                />
            </el-form-item>

            <!-- 角色等级 -->
            <el-form-item label="角色等级">
                <el-input-number
                    v-model="form.level"
                    :min="1"
                    :max="115"
                    style="width: 100%"
                />
            </el-form-item>

            <!-- 角色外观 -->
            <el-form-item
                label="角色外观"
            >
                <div class="avatar-url-container">
                    <el-input
                        v-model="form.avatarUrl"
                        placeholder="请输入图片URL地址"
                        clearable
                    >
                        <template slot="prepend">
                            <i class="el-icon-link" />
                        </template>
                    </el-input>
                    <div class="url-tip">
                        请输入完整的图片URL地址，例如：https://example.com/avatar.jpg
                    </div>
                    <div
                        v-if="form.avatarUrl"
                        class="avatar-preview"
                    >
                        <div class="preview-label">
                            预览：
                        </div>
                        <img
                            :src="form.avatarUrl"
                            class="avatar-preview-image"
                            @error="handleImageError"
                        >
                    </div>
                </div>
            </el-form-item>
        </el-form>

        <div
            slot="footer"
            class="dialog-footer"
        >
            <el-button @click="handleClose">
                取消
            </el-button>
            <el-button
                type="primary"
                :loading="submitting"
                @click="handleSubmit"
            >
                确定
            </el-button>
        </div>
    </el-dialog>
</template>

<script>
import api from '@/api'

export default {
    name: 'CharacterDialog',
    props: {
        visible: {
            type: Boolean,
            default: false
        },
        character: {
            type: Object,
            default: null
        }
    },
    data() {
        return {
            form: {
                userId: null,
                characterTypeId: null,
                characterName: '',
                level: 115,
                avatarUrl: ''
            },
            selectedCharacterType: [],
            characterTypeOptions: [],
            cascaderProps: {
                value: 'value',
                label: 'label',
                children: 'children',
                emitPath: false
            },
            submitting: false,
            rules: {
                characterName: [
                    { required: true, message: '请输入角色名称', trigger: 'blur' },
                    { min: 2, max: 20, message: '长度在 2 到 20 个字符', trigger: 'blur' }
                ],
                characterTypeId: [
                    { required: true, message: '请选择角色类型', trigger: 'change' }
                ]
            }
        }
    },
    computed: {
        dialogTitle() {
            return this.character ? '编辑角色' : '创建角色'
        }
    },
    watch: {
        visible(val) {
            if (val) {
                this.loadCharacterTypes()
                if (this.character) {
                    this.initForm()
                } else {
                    this.resetForm()
                }
            }
        }
    },
    methods: {
        async loadCharacterTypes() {
            try {
                const data = await this.$request({
                    url: api.character.characterType.cascade,
                    method: 'get'
                })
                this.characterTypeOptions = data || []
            } catch (error) {
                console.error('加载角色类型失败:', error)
                this.$message.error('加载角色类型失败')
            }
        },

        handleCharacterTypeChange(value) {
            this.form.characterTypeId = value
        },

        initForm() {
            this.form = {
                userId: this.character.userId || this.$store.getters.userId,
                characterTypeId: this.character.characterTypeId,
                characterName: this.character.characterName,
                level: this.character.level || 115,
                avatarUrl: this.character.avatarUrl || ''
            }
            if (this.character.characterTypeId) {
                this.selectedCharacterType = [this.character.characterTypeId]
            }
        },

        resetForm() {
            this.form = {
                userId: this.$store.getters.userId,
                characterTypeId: null,
                characterName: '',
                level: 115,
                avatarUrl: ''
            }
            this.selectedCharacterType = []
            if (this.$refs.characterForm) {
                this.$refs.characterForm.clearValidate()
            }
        },

        handleImageError() {
            this.$message.warning('图片加载失败，请检查URL地址')
        },

        handleSubmit() {
            this.$refs.characterForm.validate(async (valid) => {
                if (valid) {
                    this.submitting = true
                    try {
                        if (!this.form.userId) {
                            this.form.userId = this.$store.getters.userId
                        }
                        if (this.character) {
                            await this.$request({
                                url: api.character.update(this.character.id),
                                method: 'put',
                                data: this.form
                            })
                            this.$message.success('更新角色成功')
                        } else {
                            await this.$request({
                                url: api.character.create,
                                method: 'post',
                                data: this.form
                            })
                            this.$message.success('创建角色成功')
                        }
                        this.$emit('success')
                        this.handleClose()
                    } catch (error) {
                        console.error('提交失败:', error)
                        this.$message.error('提交失败，请重试')
                    } finally {
                        this.submitting = false
                    }
                }
            })
        },

        handleClose() {
            this.resetForm()
            this.$emit('update:visible', false)
        }
    }
}
</script>

<style lang="less" scoped>
.character-form {
    /deep/ .el-form-item {
        margin-bottom: 20px;
        position: relative;

        &__error {
            position: absolute;
            z-index: 2000;
            background: var(--theme-danger);
            color: #ffffff !important;
            padding: 4px 8px;
            border-radius: 4px;
            font-size: 12px;
            line-height: 1.5;
            box-shadow: 0 2px 4px rgba(0, 0, 0, 0.2);
            white-space: nowrap;
            top: 100%;
            left: 0;
            margin-top: 2px;

            &:before {
                content: '';
                position: absolute;
                bottom: 100%;
                left: 16px;
                border-width: 4px;
                border-style: solid;
                border-color: transparent transparent var(--theme-danger) transparent;
            }
        }
    }

    /deep/ .el-input__inner {
        border-color: var(--theme-border) !important;
        background: var(--theme-bg-hover) !important;
        color: var(--theme-text-primary) !important;

        &:focus {
            border-color: var(--theme-primary) !important;
        }
    }

    /deep/ .el-input .el-input__count .el-input__count-inner {
        background-color: transparent !important;
        color: var(--theme-text-secondary);
    }
}

.dialog-footer {
    text-align: right;

    .el-button {
        min-width: 100px;
        font-weight: 600;
        transition: all 0.3s ease;

        &:first-child {
            background: var(--theme-bg-hover);
            border-color: var(--theme-border);
            color: var(--theme-text-primary);

            &:hover {
                background: var(--theme-bg-hover);
                border-color: var(--theme-primary);
                color: var(--theme-text-primary);
            }
        }

        &:last-child:hover {
            transform: translateY(-2px);
            box-shadow: 0 4px 12px var(--theme-primary);
        }
    }
}

/deep/ .el-dialog {
    background: var(--theme-bg-card);
    border: 1px solid var(--theme-border);
    border-radius: 12px;
    box-shadow: 0 20px 25px -5px rgba(0, 0, 0, 0.6);

    &__header {
        background: var(--theme-bg-hover);
        border-bottom: 1px solid var(--theme-border);
        padding: 20px;
        border-radius: 12px 12px 0 0;

        .el-dialog__title {
            color: var(--theme-text-primary);
            font-weight: 700;
            font-size: 18px;
        }

        .el-dialog__headerbtn {
            position: absolute !important;
            top: 16px;

            .el-dialog__close {
                color: var(--theme-text-secondary);

                &:hover {
                    color: var(--theme-text-primary);
                }
            }
        }
    }

    &__body {
        padding: 24px;
        color: var(--theme-text-secondary);
    }

    &__footer {
        background: var(--theme-bg-hover);
        border-top: 1px solid var(--theme-border);
        padding: 16px 24px;
        border-radius: 0 0 12px 12px;
    }
}

.avatar-url-container {
    .el-input {
        margin-bottom: 8px;

        /deep/ .el-input-group__prepend {
            background: var(--theme-bg-hover);
            border-color: var(--theme-border);
            color: var(--theme-text-secondary);
        }
    }

    .url-tip {
        font-size: 12px;
        color: var(--theme-text-secondary);
        margin-bottom: 12px;
        line-height: 1.5;
    }

    .avatar-preview {
        .preview-label {
            font-size: 13px;
            color: var(--theme-text-secondary);
            margin-bottom: 8px;
        }

        .avatar-preview-image {
            width: 100px;
            height: 100px;
            border-radius: 8px;
            object-fit: cover;
            border: 1px solid var(--theme-border);
            background: var(--theme-bg-hover);
        }
    }
}
</style>
<style lang="less">
/deep/ .el-cascader,
/deep/ .el-select {
    .el-input__inner {
        background: var(--theme-bg-hover) !important;
        border-color: var(--theme-border) !important;
        color: var(--theme-text-primary) !important;

        &:focus {
            border-color: var(--theme-primary) !important;
            box-shadow: 0 0 0 3px var(--theme-primary) !important;
        }
    }

    .el-input__suffix {
        .el-select__caret {
            color: var(--theme-text-secondary) !important;
        }
    }
}

// 级联选择器面板样式
.el-cascader-panel,
.el-cascader__dropdown {
    background: var(--theme-bg-card) !important;
    border: 1px solid var(--theme-border) !important;

    .el-cascader-menu {
        background: var(--theme-bg-card) !important;
        border-right: 1px solid var(--theme-border) !important;

        &:last-child {
            border-right: none !important;
        }
    }
}

// 下拉选择器和级联选择器弹窗样式
.el-cascader__dropdown,
.el-select-dropdown {
    background: var(--theme-bg-card) !important;
    border: 1px solid var(--theme-primary) !important;
    box-shadow: 0 0 0 3px var(--theme-primary) !important;

    .el-select-dropdown__item,
    .el-cascader-node,
    .el-cascader-menu__item {
        color: var(--theme-text-secondary) !important;

        &:hover,
        &:focus {
            background: var(--theme-bg-hover) !important;
            color: var(--theme-text-primary) !important;
        }

        &.selected,
        &.is-active,
        &.el-cascader-node--selectable.in-active-path {
            color: var(--theme-accent) !important;
            background: var(--theme-bg-card) !important;
        }
    }

    .el-select-dropdown__item.hover,
    .el-select-dropdown__item:hover {
        background: var(--theme-bg-hover) !important;
    }

    .el-cascader-menu {
        border-right: solid 1px var(--theme-primary);
    }
}

.el-popper .popper__arrow::after {
    top: 0 !important;
    border-bottom-color: var(--theme-bg-hover) !important;
}
</style>
