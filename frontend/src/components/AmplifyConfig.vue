<!--
 * @Description: 增幅配置组件
 * @Author: Claude Code
 * @Date: 2026-04-17
-->
<template>
    <div class="amplify-config">
        <!-- 当前状态 -->
        <div class="current-status">
            <h3>当前增幅状态</h3>
            <p>增幅等级: +{{ form.level || 0 }} {{ form.attribute || '' }}</p>
            <p>武器: {{ form.isWeaponReinforce ? '强化' : '增幅' }} +{{ form.weaponLevel || 0 }}</p>
        </div>

        <!-- 增幅等级选择 -->
        <div class="form-section">
            <h4>增幅等级</h4>
            <div class="level-selector">
                <el-radio-group
                    v-model="form.level"
                    @change="handleChange"
                >
                    <el-radio-button :label="10">
                        +10
                    </el-radio-button>
                    <el-radio-button :label="11">
                        +11
                    </el-radio-button>
                    <el-radio-button :label="12">
                        +12
                    </el-radio-button>
                    <el-radio-button :label="13">
                        +13
                    </el-radio-button>
                    <el-radio-button :label="14">
                        +14
                    </el-radio-button>
                    <el-radio-button :label="15">
                        +15
                    </el-radio-button>
                    <el-radio-button :label="16">
                        +16
                    </el-radio-button>
                    <el-radio-button :label="17">
                        +17
                    </el-radio-button>
                </el-radio-group>
            </div>
        </div>

        <!-- 增幅属性选择 -->
        <div class="form-section">
            <h4>增幅属性</h4>
            <el-radio-group
                v-model="form.attribute"
                @change="handleChange"
            >
                <el-radio label="力量">
                    力量
                </el-radio>
                <el-radio label="智力">
                    智力
                </el-radio>
                <el-radio label="体力">
                    体力
                </el-radio>
                <el-radio label="精神">
                    精神
                </el-radio>
            </el-radio-group>
        </div>

        <!-- 武器强化 -->
        <div class="form-section">
            <h4>武器强化/增幅</h4>
            <el-radio-group
                v-model="form.isWeaponReinforce"
                @change="handleChange"
            >
                <el-radio :label="true">
                    强化
                </el-radio>
                <el-radio :label="false">
                    增幅
                </el-radio>
            </el-radio-group>
            <el-input-number
                v-model="form.weaponLevel"
                :min="0"
                :max="40"
                class="weapon-level-input"
                @change="handleChange"
            />
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
    name: 'AmplifyConfig',
    props: {
        characterId: {
            type: Number,
            required: true
        },
        amplifyData: {
            type: Object,
            default: () => ({})
        }
    },
    data() {
        return {
            saving: false,
            form: {
                level: 0,
                attribute: '力量',
                isReinforce: false,
                weaponLevel: 0,
                isWeaponReinforce: true
            }
        }
    },
    watch: {
        amplifyData: {
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
                    url: apiEnhancement.updateAmplify(this.characterId),
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
.amplify-config {
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

        .weapon-level-input {
            margin-top: 12px;
        }
    }

    .actions {
        margin-top: 32px;
    }
}
</style>
