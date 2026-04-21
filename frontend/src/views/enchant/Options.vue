<!--
 * @Description: 附魔选项管理页面
 * @Author: Claude Code
 * @Date: 2026-04-20
 * @LastEditTime: 2026-04-20 17:09:01
 * @FilePath: /vue2-java-mysql-project/frontend/src/views/enchant/Options.vue
-->
<template>
    <div class="enchant-options-page">
        <!-- 页面标题 -->
        <div class="page-header">
            <h2 class="page-title">
                <i class="el-icon-setting" />
                附魔选项管理
            </h2>
        </div>

        <!-- 筛选和操作栏 -->
        <div class="filter-bar">
            <div class="filter-left">
                <el-select
                    v-model="filterPart"
                    placeholder="选择部位"
                    clearable
                    class="filter-select"
                    size="small"
                    @change="handleFilterChange"
                >
                    <el-option
                        v-for="part in equipmentParts"
                        :key="part"
                        :label="part"
                        :value="part"
                    />
                </el-select>
                <el-select
                    v-model="filterPackage"
                    placeholder="礼包类型"
                    clearable
                    class="filter-select"
                    size="small"
                    @change="handleFilterChange"
                >
                    <el-option
                        v-for="pkg in packageTypes"
                        :key="pkg"
                        :label="pkg"
                        :value="pkg"
                    />
                </el-select>
                <el-button
                    type="text"
                    icon="el-icon-refresh"
                    @click="handleFilterChange"
                />
            </div>
            <div class="filter-right">
                <el-button
                    type="primary"
                    icon="el-icon-plus"
                    size="small"
                    @click="handleAdd"
                >
                    添加选项
                </el-button>
            </div>
        </div>

        <!-- 数据表格 -->
        <el-table
            :data="options"
            class="options-table"
            size="small"
        >
            <el-table-column
                prop="equipmentPart"
                label="部位"
                min-width="60"
            />
            <el-table-column
                prop="name"
                label="名称"
                min-width="120"
                show-overflow-tooltip
            />
            <el-table-column
                prop="packageType"
                label="礼包类型"
                min-width="90"
            />
            <el-table-column
                prop="attributeValue"
                label="属性值"
                min-width="170"
                show-overflow-tooltip
            />
            <el-table-column
                prop="fame"
                label="名望"
                min-width="55"
                align="center"
            />
            <el-table-column
                label="操作"
                min-width="100"
                fixed="right"
            >
                <template slot-scope="{ row }">
                    <el-button
                        type="text"
                        size="small"
                        @click="handleEdit(row)"
                    >
                        编辑
                    </el-button>
                    <el-button
                        type="text"
                        size="small"
                        class="text-danger"
                        @click="handleDelete(row)"
                    >
                        删除
                    </el-button>
                </template>
            </el-table-column>
        </el-table>

        <!-- 分页器 -->
        <div class="pagination-wrapper">
            <el-pagination
                :current-page="pagination.current"
                :page-sizes="[10, 20, 50, 100]"
                :page-size="pagination.size"
                :total="pagination.total"
                layout="total, sizes, prev, pager, next, jumper"
                background
                @size-change="handleSizeChange"
                @current-change="handleCurrentChange"
            />
        </div>

        <!-- 编辑弹窗 -->
        <el-dialog
            :visible.sync="dialogVisible"
            :title="isEdit ? '编辑选项' : '添加选项'"
            width="500px"
            class="edit-dialog"
            append-to-body
        >
            <el-form
                ref="form"
                :model="form"
                label-width="90px"
                size="small"
            >
                <el-form-item
                    label="装备部位"
                    prop="equipmentPart"
                    :rules="[{ required: true, message: '请选择部位' }]"
                >
                    <el-select
                        v-model="form.equipmentPart"
                        placeholder="选择部位"
                    >
                        <el-option
                            v-for="part in equipmentParts"
                            :key="part"
                            :label="part"
                            :value="part"
                        />
                    </el-select>
                </el-form-item>
                <el-form-item
                    label="名称"
                    prop="name"
                    :rules="[{ required: true, message: '请输入名称' }]"
                >
                    <el-input
                        v-model="form.name"
                        placeholder="如：金秋宝珠(技攻+30)"
                    />
                </el-form-item>
                <el-form-item label="礼包类型">
                    <el-select
                        v-model="form.packageType"
                        placeholder="选择礼包类型"
                    >
                        <el-option
                            v-for="pkg in packageTypes"
                            :key="pkg"
                            :label="pkg"
                            :value="pkg"
                        />
                    </el-select>
                </el-form-item>
                <el-form-item label="属性值">
                    <div class="multi-value-input">
                        <div
                            v-for="(val, idx) in attributeValues"
                            :key="idx"
                            class="value-row"
                        >
                            <el-input
                                v-model="attributeValues[idx]"
                                placeholder="如：+30 或 +8%"
                                class="value-input"
                            />
                            <el-button
                                circle
                                icon="el-icon-plus"
                                class="add-btn"
                                @click="addAttributeValue"
                            />
                            <el-button
                                v-if="attributeValues.length > 1"
                                circle
                                icon="el-icon-minus"
                                class="remove-btn"
                                @click="removeAttributeValue(idx)"
                            />
                        </div>
                    </div>
                </el-form-item>
                <el-form-item label="名望">
                    <el-input
                        v-model.number="form.fame"
                        type="number"
                        placeholder="输入名望值"
                    />
                </el-form-item>
                <el-form-item label="备注">
                    <el-input
                        v-model="form.remark"
                        type="textarea"
                        :rows="2"
                    />
                </el-form-item>
            </el-form>
            <div slot="footer">
                <el-button
                    size="small"
                    @click="dialogVisible = false"
                >
                    取消
                </el-button>
                <el-button
                    type="primary"
                    size="small"
                    :loading="saving"
                    @click="handleSave"
                >
                    保存
                </el-button>
            </div>
        </el-dialog>
    </div>
</template>

<script>
import enchantApi, { EQUIPMENT_PARTS, PACKAGE_TYPES } from '@/api/enchant'

/**
 * 附魔选项管理页面
 */
export default {
    name: 'EnchantOptions',

    data() {
        return {
            // 数据列表
            options: [],
            // 筛选
            filterPart: '',
            filterPackage: '',
            // 弹窗状态
            dialogVisible: false,
            isEdit: false,
            saving: false,
            // 分页配置
            pagination: {
                current: 1,
                size: 10,
                total: 0
            },
            // 多值属性输入
            attributeValues: [''],
            // 表单数据
            form: {
                id: null,
                equipmentPart: '',
                name: '',
                packageType: '',
                attributeValue: '',
                fame: null,
                remark: ''
            }
        }
    },

    computed: {
        // 12个装备部位
        equipmentParts() {
            return EQUIPMENT_PARTS
        },

        // 礼包类型列表
        packageTypes() {
            return PACKAGE_TYPES
        }
    },

    mounted() {
        this.loadOptions()
    },

    methods: {
        /**
         * 筛选条件变化时重置页码并重新加载
         */
        handleFilterChange() {
            this.pagination.current = 1
            this.loadOptions()
        },

        /**
         * 每页条数变化
         */
        handleSizeChange(size) {
            this.pagination.size = size
            this.pagination.current = 1
            this.loadOptions()
        },

        /**
         * 页码变化
         */
        handleCurrentChange(current) {
            this.pagination.current = current
            this.loadOptions()
        },

        /**
         * 添加属性值输入框
         */
        addAttributeValue() {
            this.attributeValues.push('')
        },

        /**
         * 移除属性值输入框
         */
        removeAttributeValue(index) {
            this.attributeValues.splice(index, 1)
        },

        /**
         * 校验属性值
         */
        validateAttributeValues() {
            const values = this.attributeValues.filter(v => v.trim() !== '')
            if (values.length === 0) {
                this.$message.error('请至少输入一个属性值')
                return false
            }
            // 检查重复
            const uniqueValues = [...new Set(values)]
            if (uniqueValues.length !== values.length) {
                this.$message.error('属性值不能重复')
                return false
            }
            return true
        },

        /**
         * 加载选项列表（分页）
         */
        async loadOptions() {
            try {
                const res = await this.$request({
                    url: enchantApi.enchantOptionsPage,
                    method: 'get',
                    params: {
                        current: this.pagination.current,
                        size: this.pagination.size,
                        equipmentPart: this.filterPart || null,
                        packageType: this.filterPackage || null
                    }
                })
                // 解析分页响应
                if (res && res.records) {
                    this.options = res.records
                    this.pagination.total = res.total || 0
                    this.pagination.current = res.current || 1
                    this.pagination.size = res.size || 10
                } else if (Array.isArray(res)) {
                    // 兼容非分页响应
                    this.options = res
                    this.pagination.total = res.length
                } else {
                    this.options = []
                    this.pagination.total = 0
                }
            } catch (error) {
                console.error('加载失败:', error)
                this.$message.error('加载失败')
            }
        },

        /**
         * 添加选项
         */
        handleAdd() {
            this.isEdit = false
            this.attributeValues = ['']
            this.form = {
                id: null,
                equipmentPart: '',
                name: '',
                packageType: '',
                attributeValue: '',
                fame: null,
                remark: ''
            }
            this.dialogVisible = true
        },

        /**
         * 编辑选项
         */
        handleEdit(row) {
            this.isEdit = true
            this.form = { ...row }
            // 解析属性值为数组
            if (row.attributeValue) {
                this.attributeValues = row.attributeValue.split(',').map(v => v.trim())
            } else {
                this.attributeValues = ['']
            }
            this.dialogVisible = true
        },

        /**
         * 保存选项
         */
        async handleSave() {
            try {
                await this.$refs.form.validate()
                // 校验属性值
                if (!this.validateAttributeValues()) {
                    return
                }
                this.saving = true

                // 将多值属性合并为逗号分隔字符串
                const submitData = {
                    ...this.form,
                    attributeValue: this.attributeValues.filter(v => v.trim() !== '').join(',')
                }

                if (this.isEdit) {
                    await this.$request({
                        url: enchantApi.enchantOptionsById(this.form.id),
                        method: 'put',
                        data: submitData
                    })
                } else {
                    await this.$request({
                        url: enchantApi.enchantOptions,
                        method: 'post',
                        data: submitData
                    })
                }

                await this.loadOptions()
                this.dialogVisible = false
                this.$message.success('保存成功')
            } catch (error) {
                if (error !== false) {
                    console.error('保存失败:', error)
                    this.$message.error('保存失败')
                }
            } finally {
                this.saving = false
            }
        },

        /**
         * 删除选项
         */
        handleDelete(row) {
            this.$confirm('确认删除该选项？', '提示', {
                type: 'warning'
            }).then(async () => {
                try {
                    await this.$request({
                        url: enchantApi.enchantOptionsById(row.id),
                        method: 'delete'
                    })
                    await this.loadOptions()
                    this.$message.success('删除成功')
                } catch (error) {
                    console.error('删除失败:', error)
                    this.$message.error('删除失败')
                }
            }).catch(() => { })
        }
    }
}
</script>

<style lang="less" scoped>
@import '@/assets/styles/variables.less';

.enchant-options-page {
    padding: @spacing-xl;
    min-height: 100%;
    background: linear-gradient(180deg, var(--theme-bg-dark) 0%, var(--theme-primary) 100%);

    .page-header {
        margin-bottom: @spacing-xl;
        background: var(--theme-bg-card);
        padding: @spacing-lg;;
        border-radius: @border-radius-lg;
        border: 1px solid var(--theme-border);

        .page-title {
            font-family: @font-heading, sans-serif;
            font-size: 24px;
            font-weight: @font-weight-bold;
            color: var(--theme-accent);
            margin: 0;
            display: flex;
            align-items: center;
            gap: @spacing-sm;

            i {
                font-size: 28px;
            }
        }
    }

    .filter-bar {
        display: flex;
        justify-content: space-between;
        align-items: center;
        margin-bottom: @spacing-lg;

        .filter-left {
            display: flex;
            gap: @spacing-md;
            align-items: center;

            .filter-select {
                width: 150px;
            }
        }
    }

    .options-table {
        .text-muted {
            color: var(--theme-text-secondary);
        }

        .text-danger {
            color: var(--theme-danger);
        }
    }

    // 分页器样式
    .pagination-wrapper {
        display: flex;
        justify-content: flex-end;
        margin-top: @spacing-lg;
        padding: @spacing-md 0;

        /deep/ .el-pagination {
            background: var(--theme-bg-card);
            padding: 8px 16px;
            border-radius: @border-radius-md;

            .btn-prev,
            .btn-next,
            .el-pager li {
                background: var(--theme-bg-hover);
                color: var(--theme-text-secondary);
                border: 1px solid var(--theme-border);
                height: 28px;
                line-height: 26px;

                &:hover {
                    color: var(--theme-accent);
                    border-color: var(--theme-accent);
                }
            }

            .el-pager li.is-active {
                background: var(--theme-secondary);
                color: var(--theme-text-primary);
                border-color: var(--theme-secondary);
            }

            .el-pagination__total {
                color: var(--theme-text-secondary);
            }

            .el-pagination__sizes {
                .el-input__inner {
                    background: var(--theme-bg-hover);
                    color: var(--theme-text-secondary);
                    border-color: var(--theme-border);

                    &:hover {
                        border-color: var(--theme-accent);
                    }
                }
            }

            .el-pagination__jump {
                color: var(--theme-text-secondary);

                .el-pagination__editor {
                    background: var(--theme-bg-hover);
                    color: var(--theme-text-primary);
                    border-color: var(--theme-border);

                    &:focus {
                        border-color: var(--theme-accent);
                    }
                }
            }
        }
    }

    .multi-value-input {
        display: flex;
        flex-direction: column;
        gap: 8px;
        width: auto;
        max-width: 320px;

        .value-row {
            display: flex;
            align-items: center;
            gap: 8px;
            width: 100%;
            max-width: 280px;

            .value-input {
                flex: 1;
                min-width: 0;

                /deep/ .el-input__inner {
                    padding-right: 30px;
                }
            }

            .add-btn,
            .remove-btn {
                width: 28px;
                height: 28px;
                min-width: 28px;
                padding: 0;
                display: inline-flex;
                align-items: center;
                justify-content: center;
                border: 1px solid var(--theme-border);
                border-radius: 50%;
                background: var(--theme-bg-hover);
                transition: all @transition-fast;
                flex-shrink: 0;

                i {
                    font-size: 12px;
                }

                &:hover {
                    border-color: var(--theme-accent);
                }
            }

            .add-btn {
                color: var(--theme-accent);

                &:hover {
                    background: rgba(255, 215, 0, 0.15);
                }
            }

            .remove-btn {
                color: var(--theme-danger);

                &:hover {
                    background: rgba(245, 108, 108, 0.15);
                }
            }
        }
    }
}

.edit-dialog {
    .el-select,
    .el-input,
    .el-textarea {
        width: 225px;
        margin: 10px 10px 0 0;
    }
}
</style>
