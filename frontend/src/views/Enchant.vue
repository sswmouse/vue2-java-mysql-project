<template>
    <div class="enchant-page">
        <div class="current-status">
            <h3>当前附魔状态</h3>
            <p>主属性: {{ enchantData.elementType || '无' }} +{{ enchantData.elementValue || 0 }}</p>
        </div>

        <!-- 属强类型和数值 -->
        <div class="form-section">
            <h4>属性强化</h4>
            <el-select
                v-model="form.elementType"
                placeholder="选择属性"
                @change="handleChange"
            >
                <el-option
                    label="光属性"
                    value="光"
                />
                <el-option
                    label="火属性"
                    value="火"
                />
                <el-option
                    label="冰属性"
                    value="冰"
                />
                <el-option
                    label="暗属性"
                    value="暗"
                />
            </el-select>
            <el-input-number
                v-model="form.elementValue"
                :min="0"
                :max="999"
                class="element-input"
                @change="handleChange"
            />
        </div>

        <!-- 预设宝珠选择 -->
        <div class="form-section">
            <h4>防具附魔</h4>
            <el-form label-width="100px">
                <el-form-item label="头肩">
                    <el-select
                        v-model="form.head"
                        placeholder="选择宝珠"
                        @change="handleChange"
                    >
                        <el-option
                            label="冲击之魂头肩宝珠"
                            value="冲击之魂头肩宝珠"
                        />
                        <el-option
                            label="使徒宝珠"
                            value="使徒宝珠"
                        />
                    </el-select>
                </el-form-item>
                <el-form-item label="上衣/下装">
                    <el-select
                        v-model="form.chest"
                        placeholder="选择宝珠"
                        @change="handleChange"
                    >
                        <el-option
                            label="使徒希洛克宝珠"
                            value="使徒希洛克宝珠"
                        />
                    </el-select>
                </el-form-item>
                <el-form-item label="鞋/腰带/肩膀">
                    <el-select
                        v-model="form.shoes"
                        placeholder="选择宝珠"
                        @change="handleChange"
                    >
                        <el-option
                            label="格斗场冠军宝珠"
                            value="格斗场冠军宝珠"
                        />
                    </el-select>
                </el-form-item>
            </el-form>
        </div>

        <!-- 首饰附魔 -->
        <div class="form-section">
            <h4>首饰附魔</h4>
            <el-form label-width="100px">
                <el-form-item label="项链">
                    <el-select
                        v-model="form.necklace"
                        placeholder="选择宝珠"
                        @change="handleChange"
                    >
                        <el-option
                            label="光之主人宝珠"
                            value="光之主人宝珠"
                        />
                        <el-option
                            label="火之主人宝珠"
                            value="火之主人宝珠"
                        />
                        <el-option
                            label="冰之主人宝珠"
                            value="冰之主人宝珠"
                        />
                        <el-option
                            label="暗之主人宝珠"
                            value="暗之主人宝珠"
                        />
                    </el-select>
                </el-form-item>
                <el-form-item label="手镯">
                    <el-select
                        v-model="form.bracelet"
                        placeholder="选择宝珠"
                        @change="handleChange"
                    >
                        <el-option
                            label="光之主人宝珠"
                            value="光之主人宝珠"
                        />
                    </el-select>
                </el-form-item>
                <el-form-item label="戒指">
                    <el-select
                        v-model="form.ring"
                        placeholder="选择宝珠"
                        @change="handleChange"
                    >
                        <el-option
                            label="冰之主人宝珠"
                            value="冰之主人宝珠"
                        />
                    </el-select>
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
    name: 'EnchantPage',
    props: {
        characterId: {
            type: Number,
            required: true
        },
        enchantData: {
            type: Object,
            default: () => ({})
        }
    },
    data() {
        return {
            saving: false,
            form: {
                weapon: '',
                head: '',
                chest: '',
                legs: '',
                shoes: '',
                necklace: '',
                bracelet: '',
                ring: '',
                subEquip: '',
                magicStone: '',
                auxiliary: '',
                elementType: '冰',
                elementValue: 0
            }
        }
    },
    watch: {
        enchantData: {
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
                    url: apiEnhancement.updateEnchant(this.characterId),
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
.enchant-page {
    padding: 20px;

    .form-section {
        margin-bottom: 24px;

        h4 {
            margin-bottom: 12px;
        }

        .element-input {
            margin-left: 12px;
        }
    }

    .actions {
        margin-top: 32px;
    }
}
</style>
