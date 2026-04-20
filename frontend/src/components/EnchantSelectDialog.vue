<!--
 * @Description: 附魔选择弹窗组件
 * @Author: Claude Code
 * @Date: 2026-04-20
 * @LastEditTime: 2026-04-20
 * @FilePath: /vue2-java-mysql-project/frontend/src/components/EnchantSelectDialog.vue
-->
<template>
    <el-dialog
        :visible.sync="dialogVisible"
        :title="`选择 ${partName} 附魔`"
        width="600px"
        class="enchant-select-dialog"
        append-to-body
    >
        <div class="dialog-content">
            <!-- 选项列表 - 按礼包类型分组 -->
            <el-radio-group v-model="selectedOptionId">
                <div
                    v-for="group in groupedOptions"
                    :key="group.packageType"
                    class="option-group"
                >
                    <div class="group-title">
                        {{ group.packageType }}
                    </div>
                    <div class="group-options">
                        <el-radio
                            v-for="option in group.options"
                            :key="option.id"
                            :label="option.id"
                            class="option-item"
                        >
                            <span class="option-name">{{ option.name }}</span>
                            <span class="option-attr">{{ option.attributeValue }}</span>
                            <span
                                v-if="option.fame"
                                class="option-fame"
                            >名望+{{ option.fame }}</span>
                        </el-radio>
                    </div>
                </div>

                <!-- 未附魔选项 -->
                <div class="option-group">
                    <div class="group-title">
                        无附魔
                    </div>
                    <div class="group-options">
                        <el-radio
                            :label="0"
                            class="option-item"
                        >
                            <span class="option-name">未附魔</span>
                        </el-radio>
                    </div>
                </div>
            </el-radio-group>
        </div>

        <div slot="footer">
            <el-button @click="handleCancel">
                取消
            </el-button>
            <el-button
                type="primary"
                @click="handleConfirm"
            >
                确定
            </el-button>
        </div>
    </el-dialog>
</template>

<script>
/**
 * 附魔选择弹窗组件
 */
export default {
    name: 'EnchantSelectDialog',

    props: {
        visible: {
            type: Boolean,
            default: false
        },
        partName: {
            type: String,
            default: ''
        },
        options: {
            type: Array,
            default: () => []
        },
        currentOptionId: {
            type: [Number, String],
            default: null
        }
    },

    data() {
        return {
            selectedOptionId: null
        }
    },

    computed: {
        dialogVisible: {
            get() {
                return this.visible
            },
            set(val) {
                this.$emit('update:visible', val)
            }
        },

        // 按礼包类型分组选项
        groupedOptions() {
            const groups = {}
            this.options.forEach(option => {
                const packageType = option.packageType || '其他'
                if (!groups[packageType]) {
                    groups[packageType] = []
                }
                groups[packageType].push(option)
            })
            return Object.entries(groups).map(([packageType, options]) => ({
                packageType,
                options
            }))
        }
    },

    watch: {
        visible(val) {
            if (val) {
                this.selectedOptionId = this.currentOptionId || 0
            }
        }
    },

    methods: {
        handleCancel() {
            this.dialogVisible = false
        },

        handleConfirm() {
            if (this.selectedOptionId === 0) {
                // 未附魔
                this.$emit('select', {
                    partName: this.partName,
                    enchantData: null
                })
            } else {
                const option = this.options.find(o => o.id === this.selectedOptionId)
                this.$emit('select', {
                    partName: this.partName,
                    enchantData: option
                })
            }
            this.dialogVisible = false
        }
    }
}
</script>

<style lang="less" scoped>
@import '@/assets/styles/variables.less';

.enchant-select-dialog {
    /deep/ .el-dialog {
        background: linear-gradient(145deg, @dnf-bg-card 0%, @dnf-primary-dark 100%);
        border: 2px solid @dnf-border-light;
        border-radius: @border-radius-xl;

        .el-dialog__header {
            padding: @spacing-lg;
            border-bottom: 1px solid @dnf-border-dark;

            .el-dialog__title {
                color: @dnf-primary-gold;
                font-family: @font-heading, sans-serif;
                font-size: 18px;
            }

            .el-dialog__headerbtn .el-dialog__close {
                color: @dnf-text-muted;

                &:hover {
                    color: @dnf-primary-gold;
                }
            }
        }

        .el-dialog__body {
            padding: @spacing-lg;
            max-height: 500px;
            overflow-y: auto;
        }

        .el-dialog__footer {
            padding: @spacing-md @spacing-lg;
            border-top: 1px solid @dnf-border-dark;
        }
    }

    .dialog-content {
        .option-group {
            margin-bottom: @spacing-lg;

            .group-title {
                font-size: 13px;
                font-weight: @font-weight-semibold;
                color: @dnf-secondary-gold;
                margin-bottom: @spacing-sm;
                padding-left: @spacing-sm;
                border-left: 3px solid @dnf-secondary-gold;
            }

            .group-options {
                .option-item {
                    display: flex;
                    align-items: center;
                    width: 100%;
                    padding: @spacing-sm @spacing-md;
                    margin-bottom: @spacing-xs;
                    border-radius: @border-radius-sm;
                    transition: background 0.2s;

                    &:hover {
                        background: @dnf-bg-hover;
                    }

                    .option-name {
                        flex: 1;
                        color: @dnf-text-primary;
                    }

                    .option-attr {
                        color: @dnf-info;
                        font-size: 13px;
                    }

                    .option-fame {
                        color: @dnf-secondary-gold;
                        font-size: 12px;
                        margin-left: @spacing-sm;
                    }

                    /deep/ .el-radio__label {
                        display: flex;
                        justify-content: space-between;
                        width: 100%;
                    }

                    /deep/ .el-radio__input.is-checked + .el-radio__label {
                        color: @dnf-primary-gold;
                    }
                }
            }
        }
    }
}
</style>
