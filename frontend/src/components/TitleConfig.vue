<!--
 * @Description: 称号配置组件 - 从后端加载选项数据
 * @Author: Claude Code
 * @Date: 2026-04-17
-->
<template>
    <div class="title-config">
        <!-- 当前状态 -->
        <div class="current-status">
            <h3>当前称号</h3>
            <p>{{ form.name || '未设置' }}</p>
            <p v-if="form.year">
                年份: {{ form.year }}
            </p>
        </div>

        <!-- 称号选择 -->
        <div class="form-section">
            <h4>选择称号</h4>
            <el-select
                v-model="form.name"
                placeholder="选择称号"
                class="title-select"
                filterable
                @change="handleChange"
            >
                <el-option-group
                    v-for="(optionList, season) in groupedOptions"
                    :key="season"
                    :label="season"
                >
                    <el-option
                        v-for="opt in optionList"
                        :key="opt.id"
                        :label="opt.name"
                        :value="opt.name"
                    />
                </el-option-group>
            </el-select>
        </div>

        <!-- 称号年份 -->
        <div class="form-section">
            <h4>称号年份</h4>
            <el-select
                v-model="form.year"
                placeholder="选择年份"
                class="year-select"
                @change="handleChange"
            >
                <el-option
                    v-for="season in seasons"
                    :key="season"
                    :label="season"
                    :value="season"
                />
            </el-select>
        </div>

        <!-- 保存按钮 -->
        <div class="actions">
            <el-button
                type="primary"
                :loading="saving"
                @click="handleSave"
            >
                保存更改
            </el-button>
        </div>
    </div>
</template>

<script>
import apiEnhancement from '@/api/enhancement'

export default {
    name: 'TitleConfig',
    props: {
        characterId: {
            type: Number,
            required: true
        },
        titleData: {
            type: Object,
            default: () => ({})
        },
        options: {
            type: Object,
            default: () => ({})
        }
    },
    data() {
        return {
            saving: false,
            form: {
                name: '',
                year: ''
            }
        }
    },
    computed: {
        groupedOptions() {
            return this.options
        },
        seasons() {
            return Object.keys(this.options)
        }
    },
    watch: {
        titleData: {
            handler(newData) {
                if (newData) {
                    this.form = { ...this.form, ...newData }
                }
            },
            immediate: true,
            deep: true
        }
    },
    methods: {
        handleChange() {
            this.$emit('update', this.form)
        },
        async handleSave() {
            this.saving = true
            try {
                await this.$request({
                    url: apiEnhancement.updateTitle(this.characterId),
                    method: 'put',
                    data: this.form
                })
                this.$message.success('保存成功')
                this.$emit('saved', this.form)
            } catch (error) {
                this.$message.error('保存失败')
            } finally {
                this.saving = false
            }
        }
    }
}
</script>

<style lang="less" scoped>
.title-config {
    background: var(--theme-bg-card);
    border-radius: 8px;
    padding: 20px;
    border: 1px solid var(--theme-border);

    .current-status {
        background: var(--theme-bg-hover);
        border-radius: 8px;
        padding: 16px 20px;
        margin-bottom: 24px;

        h3 {
            margin: 0 0 8px 0;
            color: var(--theme-text-primary);
        }

        p {
            margin: 4px 0;
            color: var(--theme-text-secondary);
        }
    }

    .form-section {
        margin-bottom: 24px;

        h4 {
            margin-bottom: 12px;
            color: var(--theme-text-primary);
            font-weight: 600;
        }

        .title-select,
        .year-select {
            width: 300px;
        }
    }

    .actions {
        margin-top: 32px;
    }
}
</style>
