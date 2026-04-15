<template>
    <div class="pet-page">
        <div class="form-section">
            <h4>当前宠物</h4>
            <el-select
                v-model="form.pet"
                placeholder="选择宠物"
                @change="handleChange"
            >
                <el-option
                    label="迷你铁齿狗"
                    value="迷你铁齿狗"
                />
                <el-option
                    label="迷你帕拉丁"
                    value="迷你帕拉丁"
                />
                <el-option
                    label="迷你亡魂阿震"
                    value="迷你亡魂阿震"
                />
            </el-select>
        </div>

        <div class="form-section">
            <h4>宠物装备</h4>
            <el-form label-width="120px">
                <el-form-item label="红色宠物装备">
                    <el-input
                        v-model="form.petEquipRed"
                        placeholder="属性描述"
                        @change="handleChange"
                    />
                </el-form-item>
                <el-form-item label="蓝色宠物装备">
                    <el-input
                        v-model="form.petEquipBlue"
                        placeholder="属性描述"
                        @change="handleChange"
                    />
                </el-form-item>
                <el-form-item label="绿色宠物装备">
                    <el-input
                        v-model="form.petEquipGreen"
                        placeholder="属性描述"
                        @change="handleChange"
                    />
                </el-form-item>
            </el-form>
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
    name: 'PetPage',
    props: {
        characterId: {
            type: Number,
            required: true
        },
        petData: {
            type: Object,
            default: () => ({})
        }
    },
    data() {
        return {
            saving: false,
            form: {
                pet: '',
                petEquipRed: '',
                petEquipBlue: '',
                petEquipGreen: ''
            }
        }
    },
    watch: {
        petData: {
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
                    url: apiEnhancement.updatePet(this.characterId),
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
.pet-page {
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
