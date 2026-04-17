<!--
 * @Description: 宠物配置组件 - 从后端加载选项数据
 * @Author: Claude Code
 * @Date: 2026-04-17
-->
<template>
    <div class="pet-config">
        <!-- 当前状态 -->
        <div class="current-status">
            <h3>当前宠物</h3>
            <p>{{ form.pet || '未设置' }}</p>
        </div>

        <!-- 宠物选择 -->
        <div class="form-section">
            <h4>选择宠物</h4>
            <el-select
                v-model="form.pet"
                placeholder="选择宠物"
                class="pet-select"
                filterable
                clearable
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

        <!-- 宠物装备 -->
        <div class="form-section">
            <h4>宠物装备</h4>
            <el-form
                label-width="120px"
                class="pet-equip-form"
            >
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
    name: 'PetConfig',
    props: {
        characterId: {
            type: Number,
            required: true
        },
        petData: {
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
                pet: '',
                petEquipRed: '',
                petEquipBlue: '',
                petEquipGreen: ''
            }
        }
    },
    computed: {
        groupedOptions() {
            return this.options
        }
    },
    watch: {
        petData: {
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
                    url: apiEnhancement.updatePet(this.characterId),
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
.pet-config {
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

        .pet-select {
            width: 300px;
        }

        .pet-equip-form {
            .el-input {
                width: 300px;
            }
        }
    }

    .actions {
        margin-top: 32px;
    }
}
</style>
