<template>
    <div class="title-page">
        <div class="form-section">
            <h4>当前称号</h4>
            <el-select
                v-model="form.name"
                placeholder="选择称号"
                class="title-select"
                @change="handleChange"
            >
                <el-option
                    label="破坏之毁灭者"
                    value="破坏之毁灭者"
                />
                <el-option
                    label="超越极限者"
                    value="超越极限者"
                />
                <el-option
                    label="至高的神"
                    value="至高的神"
                />
            </el-select>
        </div>

        <div class="form-section">
            <h4>称号年份</h4>
            <el-select
                v-model="form.year"
                placeholder="选择年份"
                @change="handleChange"
            >
                <el-option
                    label="2024金秋"
                    value="2024金秋"
                />
                <el-option
                    label="2024耕耘"
                    value="2024耕耘"
                />
                <el-option
                    label="2023金秋"
                    value="2023金秋"
                />
            </el-select>
        </div>

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
    name: 'TitlePage',
    props: {
        characterId: {
            type: Number,
            required: true
        },
        titleData: {
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
    watch: {
        titleData: {
            handler(newData) {
                if (newData) {
                    this.form = { ...this.form, ...newData }
                }
            },
            immediate: true
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
.title-page {
    padding: 20px;

    .form-section {
        margin-bottom: 24px;

        h4 {
            margin-bottom: 12px;
        }

        .title-select {
            width: 300px;
        }
    }

    .actions {
        margin-top: 32px;
    }
}
</style>
