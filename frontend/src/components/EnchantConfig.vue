<!--
 * @Description: 附魔配置组件 - 从后端加载选项数据
 * @Author: Claude Code
 * @Date: 2026-04-17
-->
<template>
    <div class="enchant-config">
        <!-- 当前状态 -->
        <div class="current-status">
            <h3>当前附魔状态</h3>
            <p>主属性: {{ form.elementType || '无' }} +{{ form.elementValue || 0 }}</p>
        </div>

        <!-- 属强类型和数值 -->
        <div class="form-section">
            <h4>属性强化</h4>
            <el-select
                v-model="form.elementType"
                placeholder="选择属性"
                class="element-select"
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
                <el-option
                    label="全属性"
                    value="全"
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

        <!-- 防具附魔 -->
        <div class="form-section">
            <h4>防具附魔</h4>
            <el-form
                label-width="100px"
                class="enchant-form"
            >
                <el-form-item label="头肩">
                    <el-select
                        v-model="form.head"
                        placeholder="选择宝珠"
                        clearable
                        @change="handleChange"
                    >
                        <el-option-group
                            v-for="(optionList, packageType) in groupedOptions.head"
                            :key="packageType"
                            :label="packageType"
                        >
                            <el-option
                                v-for="opt in optionList"
                                :key="opt.id"
                                :label="opt.name"
                                :value="opt.name"
                            />
                        </el-option-group>
                    </el-select>
                </el-form-item>
                <el-form-item label="上衣/下装">
                    <el-select
                        v-model="form.chest"
                        placeholder="选择宝珠"
                        clearable
                        @change="handleChange"
                    >
                        <el-option-group
                            v-for="(optionList, packageType) in groupedOptions.chest"
                            :key="packageType"
                            :label="packageType"
                        >
                            <el-option
                                v-for="opt in optionList"
                                :key="opt.id"
                                :label="opt.name"
                                :value="opt.name"
                            />
                        </el-option-group>
                    </el-select>
                </el-form-item>
                <el-form-item label="鞋/腰带">
                    <el-select
                        v-model="form.shoes"
                        placeholder="选择宝珠"
                        clearable
                        @change="handleChange"
                    >
                        <el-option-group
                            v-for="(optionList, packageType) in groupedOptions.shoes"
                            :key="packageType"
                            :label="packageType"
                        >
                            <el-option
                                v-for="opt in optionList"
                                :key="opt.id"
                                :label="opt.name"
                                :value="opt.name"
                            />
                        </el-option-group>
                    </el-select>
                </el-form-item>
                <el-form-item label="腰带">
                    <el-select
                        v-model="form.legs"
                        placeholder="选择宝珠"
                        clearable
                        @change="handleChange"
                    >
                        <el-option-group
                            v-for="(optionList, packageType) in groupedOptions.legs"
                            :key="packageType"
                            :label="packageType"
                        >
                            <el-option
                                v-for="opt in optionList"
                                :key="opt.id"
                                :label="opt.name"
                                :value="opt.name"
                            />
                        </el-option-group>
                    </el-select>
                </el-form-item>
            </el-form>
        </div>

        <!-- 首饰附魔 -->
        <div class="form-section">
            <h4>首饰附魔</h4>
            <el-form
                label-width="100px"
                class="enchant-form"
            >
                <el-form-item label="项链">
                    <el-select
                        v-model="form.necklace"
                        placeholder="选择宝珠"
                        clearable
                        @change="handleChange"
                    >
                        <el-option-group
                            v-for="(optionList, packageType) in groupedOptions.necklace"
                            :key="packageType"
                            :label="packageType"
                        >
                            <el-option
                                v-for="opt in optionList"
                                :key="opt.id"
                                :label="opt.name"
                                :value="opt.name"
                            />
                        </el-option-group>
                    </el-select>
                </el-form-item>
                <el-form-item label="手镯">
                    <el-select
                        v-model="form.bracelet"
                        placeholder="选择宝珠"
                        clearable
                        @change="handleChange"
                    >
                        <el-option-group
                            v-for="(optionList, packageType) in groupedOptions.bracelet"
                            :key="packageType"
                            :label="packageType"
                        >
                            <el-option
                                v-for="opt in optionList"
                                :key="opt.id"
                                :label="opt.name"
                                :value="opt.name"
                            />
                        </el-option-group>
                    </el-select>
                </el-form-item>
                <el-form-item label="戒指">
                    <el-select
                        v-model="form.ring"
                        placeholder="选择宝珠"
                        clearable
                        @change="handleChange"
                    >
                        <el-option-group
                            v-for="(optionList, packageType) in groupedOptions.ring"
                            :key="packageType"
                            :label="packageType"
                        >
                            <el-option
                                v-for="opt in optionList"
                                :key="opt.id"
                                :label="opt.name"
                                :value="opt.name"
                            />
                        </el-option-group>
                    </el-select>
                </el-form-item>
            </el-form>
        </div>

        <!-- 特殊装备附魔 -->
        <div class="form-section">
            <h4>特殊装备附魔</h4>
            <el-form
                label-width="100px"
                class="enchant-form"
            >
                <el-form-item label="辅助装备">
                    <el-select
                        v-model="form.subEquip"
                        placeholder="选择宝珠"
                        clearable
                        @change="handleChange"
                    >
                        <el-option-group
                            v-for="(optionList, packageType) in groupedOptions.subEquip"
                            :key="packageType"
                            :label="packageType"
                        >
                            <el-option
                                v-for="opt in optionList"
                                :key="opt.id"
                                :label="opt.name"
                                :value="opt.name"
                            />
                        </el-option-group>
                    </el-select>
                </el-form-item>
                <el-form-item label="魔法石">
                    <el-select
                        v-model="form.magicStone"
                        placeholder="选择宝珠"
                        clearable
                        @change="handleChange"
                    >
                        <el-option-group
                            v-for="(optionList, packageType) in groupedOptions.magicStone"
                            :key="packageType"
                            :label="packageType"
                        >
                            <el-option
                                v-for="opt in optionList"
                                :key="opt.id"
                                :label="opt.name"
                                :value="opt.name"
                            />
                        </el-option-group>
                    </el-select>
                </el-form-item>
                <el-form-item label="耳环">
                    <el-select
                        v-model="form.auxiliary"
                        placeholder="选择宝珠"
                        clearable
                        @change="handleChange"
                    >
                        <el-option-group
                            v-for="(optionList, packageType) in groupedOptions.auxiliary"
                            :key="packageType"
                            :label="packageType"
                        >
                            <el-option
                                v-for="opt in optionList"
                                :key="opt.id"
                                :label="opt.name"
                                :value="opt.name"
                            />
                        </el-option-group>
                    </el-select>
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
    name: 'EnchantConfig',
    props: {
        characterId: {
            type: Number,
            required: true
        },
        enchantData: {
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
                elementType: '全',
                elementValue: 0
            }
        }
    },
    computed: {
        // 将后端数据转换为前端需要的格式
        groupedOptions() {
            const result = {}
            const partMapping = {
                '头肩': 'head',
                '上衣': 'chest',
                '下装': 'chest',
                '鞋': 'shoes',
                '腰带': 'legs',
                '项链': 'necklace',
                '手镯': 'bracelet',
                '戒指': 'ring',
                '辅助装备': 'subEquip',
                '魔法石': 'magicStone',
                '耳环': 'auxiliary'
            }

            for (const [part, options] of Object.entries(this.options)) {
                const mappedPart = partMapping[part]
                if (mappedPart) {
                    if (!result[mappedPart]) {
                        result[mappedPart] = {}
                    }
                    options.forEach(opt => {
                        const packageType = opt.packageType || '其他'
                        if (!result[mappedPart][packageType]) {
                            result[mappedPart][packageType] = []
                        }
                        result[mappedPart][packageType].push(opt)
                    })
                }
            }
            return result
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
.enchant-config {
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

        .element-select {
            width: 150px;
        }

        .element-input {
            margin-left: 12px;
        }

        .enchant-form {
            .el-select {
                width: 280px;
            }
        }
    }

    .actions {
        margin-top: 32px;
    }
}
</style>
