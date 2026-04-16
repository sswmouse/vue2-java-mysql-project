<template>
    <div class="pet-page">
        <div class="form-section">
            <h4>当前宠物</h4>
            <el-select
                v-model="form.pet"
                placeholder="选择宠物"
                class="pet-select"
                @change="handleChange"
            >
                <el-option-group label="2026年春节">
                    <el-option
                        label="永恒的神宠"
                        value="永恒的神宠"
                    />
                    <el-option
                        label="永恒的灵宠"
                        value="永恒的灵宠"
                    />
                </el-option-group>
                <el-option-group label="2025年金秋">
                    <el-option
                        label="至高之炎宠"
                        value="至高之炎宠"
                    />
                    <el-option
                        label="至阴之寒宠"
                        value="至阴之寒宠"
                    />
                    <el-option
                        label="至圣之雷宠"
                        value="至圣之雷宠"
                    />
                    <el-option
                        label="至幽之暗宠"
                        value="至幽之暗宠"
                    />
                </el-option-group>
                <el-option-group label="2025年春节">
                    <el-option
                        label="TX.创造者宠"
                        value="TX.创造者宠"
                    />
                    <el-option
                        label="TX.毁灭者宠"
                        value="TX.毁灭者宠"
                    />
                </el-option-group>
                <el-option-group label="2025年耕耘">
                    <el-option
                        label="大自然守护宠"
                        value="大自然守护宠"
                    />
                    <el-option
                        label="大自然歌者宠"
                        value="大自然歌者宠"
                    />
                </el-option-group>
                <el-option-group label="2024年金秋">
                    <el-option
                        label="无上荣光宠"
                        value="无上荣光宠"
                    />
                    <el-option
                        label="完美主义宠"
                        value="完美主义宠"
                    />
                </el-option-group>
                <el-option-group label="2024年春节">
                    <el-option
                        label="龙年神宠"
                        value="龙年神宠"
                    />
                    <el-option
                        label="龙年灵宠"
                        value="龙年灵宠"
                    />
                    <el-option
                        label="龙年祥宠"
                        value="龙年祥宠"
                    />
                </el-option-group>
                <el-option-group label="2023年金秋">
                    <el-option
                        label="兔年神宠"
                        value="兔年神宠"
                    />
                    <el-option
                        label="兔年灵宠"
                        value="兔年灵宠"
                    />
                </el-option-group>
                <el-option-group label="常驻宠物">
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
                    <el-option
                        label="迷你伊甸"
                        value="迷你伊甸"
                    />
                    <el-option
                        label="迷你伊尔"
                        value="迷你伊尔"
                    />
                </el-option-group>
                <el-option-group label="无宠物">
                    <el-option
                        label="无宠物"
                        value=""
                    />
                </el-option-group>
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
            color: var(--theme-text-primary);
            font-weight: 600;
        }

        .pet-select {
            width: 300px;
        }
    }

    .actions {
        margin-top: 32px;
    }
}
</style>
