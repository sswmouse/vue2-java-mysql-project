<template>
    <div class="emblem-page">
        <div class="form-section">
            <h4>徽章品级</h4>
            <el-select
                v-model="form.emblemGrade"
                placeholder="选择品级"
                @change="handleChange"
            >
                <el-option
                    label="稀有"
                    value="稀有"
                />
                <el-option
                    label="神器"
                    value="神器"
                />
                <el-option
                    label="传说"
                    value="传说"
                />
                <el-option
                    label="史诗"
                    value="史诗"
                />
                <el-option
                    label="神圣"
                    value="神圣"
                />
            </el-select>
        </div>

        <div class="form-section">
            <h4>星徽等级</h4>
            <el-input-number
                v-model="form.starLevel"
                :min="0"
                :max="5"
                @change="handleChange"
            />
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
    name: 'EmblemPage',
    props: {
        characterId: {
            type: Number,
            required: true
        },
        emblemData: {
            type: Object,
            default: () => ({})
        }
    },
    data() {
        return {
            saving: false,
            form: {
                emblemGrade: '神器',
                starLevel: 0,
                emblemSlots: {}
            }
        }
    },
    watch: {
        emblemData: {
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
                    url: apiEnhancement.updateEmblem(this.characterId),
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
.emblem-page {
    padding: 20px;

    .form-section {
        margin-bottom: 24px;

        h4 {
            margin-bottom: 12px;
        }
    }

    .actions {
        margin-top: 32px;
    }
}
</style>
