<!--
 * @Description: 附魔选项管理页面
 * @Author: Claude Code
 * @Date: 2026-04-20
 * @LastEditTime: 2026-04-20
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
                    @click="loadOptions"
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
            :data="filteredOptions"
            border
            stripe
            class="options-table"
            size="small"
        >
            <el-table-column
                prop="equipmentPart"
                label="部位"
                width="100"
            />
            <el-table-column
                prop="name"
                label="名称"
                min-width="180"
            />
            <el-table-column
                prop="packageType"
                label="礼包类型"
                width="100"
            />
            <el-table-column
                prop="attributeType"
                label="属性类型"
                width="90"
            />
            <el-table-column
                prop="attributeValue"
                label="属性值"
                width="100"
            />
            <el-table-column
                prop="isLatest"
                label="最新赛季"
                width="90"
                align="center"
            >
                <template slot-scope="{ row }">
                    <el-tag
                        v-if="row.isLatest"
                        type="success"
                        size="small"
                    >是</el-tag>
                    <span
                        v-else
                        class="text-muted"
                    >否</span>
                </template>
            </el-table-column>
            <el-table-column
                label="操作"
                width="120"
                fixed="right"
            >
                <template slot-scope="{ row }">
                    <el-button
                        type="text"
                        size="small"
                        @click="handleEdit(row)"
                    >编辑</el-button>
                    <el-button
                        type="text"
                        size="small"
                        class="text-danger"
                        @click="handleDelete(row)"
                    >删除</el-button>
                </template>
            </el-table-column>
        </el-table>

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
                <el-form-item label="属性类型">
                    <el-select
                        v-model="form.attributeType"
                        placeholder="选择属性类型"
                    >
                        <el-option
                            label="技攻"
                            value="技攻"
                        />
                        <el-option
                            label="力智"
                            value="力智"
                        />
                        <el-option
                            label="体精"
                            value="体精"
                        />
                        <el-option
                            label="暴击"
                            value="暴击"
                        />
                        <el-option
                            label="属强"
                            value="属强"
                        />
                        <el-option
                            label="四维"
                            value="四维"
                        />
                        <el-option
                            label="三速"
                            value="三速"
                        />
                    </el-select>
                </el-form-item>
                <el-form-item label="属性值">
                    <el-input
                        v-model="form.attributeValue"
                        placeholder="如：+30 或 +8%"
                    />
                </el-form-item>
                <el-form-item label="最新赛季">
                    <el-switch v-model="form.isLatest" />
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
                >取消</el-button>
                <el-button
                    type="primary"
                    size="small"
                    :loading="saving"
                    @click="handleSave"
                >保存</el-button>
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
            // 表单数据
            form: {
                id: null,
                equipmentPart: '',
                name: '',
                packageType: '',
                attributeType: '',
                attributeValue: '',
                isLatest: false,
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
        },

        // 筛选后的选项
        filteredOptions() {
            return this.options.filter(opt => {
                if (this.filterPart && opt.equipmentPart !== this.filterPart) {
                    return false
                }
                if (this.filterPackage && opt.packageType !== this.filterPackage) {
                    return false
                }
                return true
            })
        }
    },

    mounted() {
        this.loadOptions()
    },

    methods: {
        /**
         * 加载选项列表
         */
        async loadOptions() {
            try {
                const res = await this.$request({
                    url: enchantApi.enchantOptions,
                    method: 'get'
                })
                this.options = Array.isArray(res) ? res : (res.data || [])
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
            this.form = {
                id: null,
                equipmentPart: '',
                name: '',
                packageType: '',
                attributeType: '',
                attributeValue: '',
                isLatest: false,
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
            this.dialogVisible = true
        },

        /**
         * 保存选项
         */
        async handleSave() {
            try {
                await this.$refs.form.validate()
                this.saving = true

                if (this.isEdit) {
                    await this.$request({
                        url: enchantApi.enchantOptionsById(this.form.id),
                        method: 'put',
                        data: this.form
                    })
                } else {
                    await this.$request({
                        url: enchantApi.enchantOptions,
                        method: 'post',
                        data: this.form
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
            }).catch(() => {})
        }
    }
}
</script>

<style lang="less" scoped>
@import '@/assets/styles/variables.less';

.enchant-options-page {
    padding: @spacing-xl;

    .page-header {
        margin-bottom: @spacing-xl;

        .page-title {
            font-family: @font-heading, sans-serif;
            font-size: 24px;
            font-weight: @font-weight-bold;
            color: @dnf-primary-gold;
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
        background: @dnf-bg-card;

        .text-muted {
            color: @dnf-text-muted;
        }

        .text-danger {
            color: @dnf-danger;
        }
    }
}

.edit-dialog {
    /deep/ .el-dialog {
        background: linear-gradient(145deg, @dnf-bg-card 0%, @dnf-primary-dark 100%);

        .el-dialog__title {
            color: @dnf-primary-gold;
        }
    }
}
</style>
