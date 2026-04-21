<!--
 * @Description: 增幅选择弹窗组件
 * @Author: Claude Code
 * @Date: 2026-04-21
 * @FilePath: /frontend/src/components/AmplifySelectDialog.vue
-->
<template>
    <el-dialog
        :visible.sync="dialogVisible"
        :title="`选择 ${partName} 增幅/强化`"
        width="500px"
        class="amplify-select-dialog"
        append-to-body
    >
        <div class="dialog-content">
            <!-- 增幅类型选择 -->
            <div class="type-section">
                <div class="section-label">
                    类型
                </div>
                <el-radio-group v-model="selectedType">
                    <el-radio label="增幅">
                        增幅
                    </el-radio>
                    <el-radio label="强化">
                        强化
                    </el-radio>
                </el-radio-group>
            </div>

            <!-- 增幅等级选择 -->
            <div class="level-section">
                <div class="section-label">
                    等级
                </div>
                <el-select
                    v-model="selectedLevel"
                    placeholder="选择增幅/强化等级"
                    class="level-select"
                >
                    <el-option
                        v-for="level in amplifyLevels"
                        :key="level"
                        :label="`+${level}`"
                        :value="level"
                    >
                        <span class="level-label">+{{ level }}</span>
                        <span class="level-fame">{{ getFameDisplay(level) }}</span>
                    </el-option>
                </el-select>
            </div>

            <!-- 清除按钮 -->
            <div class="clear-section">
                <el-button
                    size="small"
                    @click="handleClear"
                >
                    清除增幅/强化
                </el-button>
            </div>
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
import { FAME_TABLE } from '@/api/amplify'

/**
 * 增幅选择弹窗组件
 */
export default {
    name: 'AmplifySelectDialog',

    props: {
        visible: {
            type: Boolean,
            default: false
        },
        partName: {
            type: String,
            default: ''
        },
        currentData: {
            type: Object,
            default: null
        }
    },

    data() {
        return {
            selectedType: '增幅',
            selectedLevel: null
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

        // 增幅等级列表 1-20
        amplifyLevels() {
            return Array.from({ length: 20 }, (_, i) => i + 1)
        }
    },

    watch: {
        visible(val) {
            if (val) {
                // 初始化数据
                if (this.currentData && this.currentData.amplifyLevel) {
                    this.selectedType = this.currentData.amplifyType || '增幅'
                    this.selectedLevel = this.currentData.amplifyLevel
                } else {
                    this.selectedType = '增幅'
                    this.selectedLevel = null
                }
            }
        }
    },

    methods: {
        /**
         * 获取名望显示
         */
        getFameDisplay(level) {
            const fameData = FAME_TABLE[level]
            if (!fameData) return ''
            // 武器和其他装备名望不同
            if (this.partName === '武器') {
                return `名望+${fameData.weapon}`
            }
            return `名望+${fameData.other}`
        },

        /**
         * 清除选择
         */
        handleClear() {
            this.selectedType = '增幅'
            this.selectedLevel = null
            this.$emit('select', {
                partName: this.partName,
                amplifyData: null
            })
            this.dialogVisible = false
        },

        /**
         * 取消
         */
        handleCancel() {
            this.dialogVisible = false
        },

        /**
         * 确认选择
         */
        handleConfirm() {
            if (!this.selectedLevel) {
                this.$message.warning('请选择增幅/强化等级')
                return
            }

            this.$emit('select', {
                partName: this.partName,
                amplifyData: {
                    amplifyType: this.selectedType,
                    amplifyLevel: this.selectedLevel
                }
            })
            this.dialogVisible = false
        }
    }
}
</script>

<style lang="less" scoped>
@import '@/assets/styles/variables.less';

.amplify-select-dialog {
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
            max-height: 450px;
            overflow-y: auto;
        }

        .el-dialog__footer {
            padding: @spacing-md @spacing-lg;
            border-top: 1px solid @dnf-border-dark;
        }
    }

    .dialog-content {
        .type-section {
            margin-bottom: @spacing-lg;

            .section-label {
                font-size: 13px;
                font-weight: @font-weight-semibold;
                color: @dnf-secondary-gold;
                margin-bottom: @spacing-sm;
            }
        }

        .level-section {
            .section-label {
                font-size: 13px;
                font-weight: @font-weight-semibold;
                color: @dnf-secondary-gold;
                margin-bottom: @spacing-sm;
            }

            .level-select {
                width: 100%;

                .el-select-dropdown__item {
                    display: flex;
                    justify-content: space-between;
                    align-items: center;
                    padding: 8px 12px;

                    .level-label {
                        font-weight: @font-weight-bold;
                        color: @dnf-text-primary;
                    }

                    .level-fame {
                        font-size: 12px;
                        color: @dnf-text-muted;
                    }
                }
            }
        }

        .clear-section {
            margin-top: @spacing-lg;
            padding-top: @spacing-md;
            border-top: 1px solid @dnf-border-dark;
            text-align: center;
        }
    }
}
</style>
