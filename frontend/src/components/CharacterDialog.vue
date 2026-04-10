<!--
 * @Description: 角色添加/编辑弹窗组件
 * @Author: Claude Code
 * @Date: 2026-04-10
 * @LastEditTime: 2026-04-10
 * @FilePath: /frontend/src/components/CharacterDialog.vue
 -->
<template>
    <el-dialog
        :title="dialogTitle"
        :visible.sync="visible"
        :close-on-click-modal="false"
        width="700px"
        @close="handleClose"
    >
        <el-form
            ref="characterForm"
            :model="form"
            :rules="rules"
            label-width="120px"
            class="character-form"
        >
            <!-- 角色名称 -->
            <el-form-item label="角色名称" prop="characterName">
                <el-input
                    v-model="form.characterName"
                    placeholder="请输入角色名称"
                    maxlength="20"
                    show-word-limit
                />
            </el-form-item>

            <!-- 角色类型 - 联级选择器 -->
            <el-form-item label="角色类型" prop="characterTypeId">
                <el-cascader
                    v-model="selectedCharacterType"
                    :options="characterTypeOptions"
                    :props="cascaderProps"
                    placeholder="请选择角色类型"
                    @change="handleCharacterTypeChange"
                    style="width: 100%"
                />
            </el-form-item>

            <!-- 职业性质 -->
            <el-form-item label="职业性质" prop="jobNature">
                <el-select
                    v-model="form.jobNature"
                    placeholder="请选择职业性质"
                    :disabled="!isSpecialJob"
                    style="width: 100%"
                >
                    <el-option label="输出" value="输出" />
                    <el-option label="奶系" value="奶系" />
                </el-select>
                <div v-if="!isSpecialJob" class="form-tip">
                    根据选择的角色类型自动确定
                </div>
            </el-form-item>

            <!-- 职业属性 -->
            <el-form-item label="职业属性" prop="jobAttribute">
                <el-select
                    v-model="form.jobAttribute"
                    placeholder="请选择职业属性"
                    :disabled="!isSpecialJob"
                    style="width: 100%"
                >
                    <el-option
                        v-for="attr in jobAttributeOptions"
                        :key="attr"
                        :label="attr"
                        :value="attr"
                    />
                </el-select>
                <div v-if="!isSpecialJob" class="form-tip">
                    根据选择的角色类型自动确定
                </div>
            </el-form-item>

            <!-- 四维 -->
            <div class="form-section-title" v-if="showFourDimensionalSection">四维属性</div>
            <el-row :gutter="16">
                <el-col v-if="showStrength" :span="12">
                    <el-form-item label="力量" prop="strength">
                        <el-input-number
                            v-model="form.strength"
                            :min="0"
                            :max="99999"
                            controls-position="right"
                            style="width: 100%"
                        />
                    </el-form-item>
                </el-col>
                <el-col v-if="showIntelligence" :span="12">
                    <el-form-item label="智力" prop="intelligence">
                        <el-input-number
                            v-model="form.intelligence"
                            :min="0"
                            :max="99999"
                            controls-position="right"
                            style="width: 100%"
                        />
                    </el-form-item>
                </el-col>
            </el-row>
            <el-row :gutter="16">
                <el-col v-if="showSpirit" :span="12">
                    <el-form-item label="精神" prop="spirit">
                        <el-input-number
                            v-model="form.spirit"
                            :min="0"
                            :max="99999"
                            controls-position="right"
                            style="width: 100%"
                        />
                    </el-form-item>
                </el-col>
                <el-col v-if="showVitality" :span="12">
                    <el-form-item label="体力" prop="vitality">
                        <el-input-number
                            v-model="form.vitality"
                            :min="0"
                            :max="99999"
                            controls-position="right"
                            style="width: 100%"
                        />
                    </el-form-item>
                </el-col>
            </el-row>

            <!-- 三攻 -->
            <div class="form-section-title" v-if="showThreeAttackSection">三攻属性</div>
            <el-row :gutter="16">
                <el-col v-if="showPhysicalAttack" :span="8">
                    <el-form-item label="物攻" prop="physicalAttack">
                        <el-input-number
                            v-model="form.physicalAttack"
                            :min="0"
                            :max="999999"
                            controls-position="right"
                            style="width: 100%"
                        />
                    </el-form-item>
                </el-col>
                <el-col v-if="showMagicalAttack" :span="8">
                    <el-form-item label="魔攻" prop="magicalAttack">
                        <el-input-number
                            v-model="form.magicalAttack"
                            :min="0"
                            :max="999999"
                            controls-position="right"
                            style="width: 100%"
                        />
                    </el-form-item>
                </el-col>
                <el-col v-if="showIndependentAttack" :span="8">
                    <el-form-item label="独立" prop="independentAttack">
                        <el-input-number
                            v-model="form.independentAttack"
                            :min="0"
                            :max="999999"
                            controls-position="right"
                            style="width: 100%"
                        />
                    </el-form-item>
                </el-col>
            </el-row>

            <!-- 属强 -->
            <div class="form-section-title">属强属性</div>
            <el-row :gutter="16">
                <el-col :span="6">
                    <el-form-item label="光属强" prop="lightElement">
                        <el-input-number
                            v-model="form.lightElement"
                            :min="0"
                            :max="999"
                            controls-position="right"
                            style="width: 100%"
                        />
                    </el-form-item>
                </el-col>
                <el-col :span="6">
                    <el-form-item label="火属强" prop="fireElement">
                        <el-input-number
                            v-model="form.fireElement"
                            :min="0"
                            :max="999"
                            controls-position="right"
                            style="width: 100%"
                        />
                    </el-form-item>
                </el-col>
                <el-col :span="6">
                    <el-form-item label="冰属强" prop="iceElement">
                        <el-input-number
                            v-model="form.iceElement"
                            :min="0"
                            :max="999"
                            controls-position="right"
                            style="width: 100%"
                        />
                    </el-form-item>
                </el-col>
                <el-col :span="6">
                    <el-form-item label="暗属强" prop="darkElement">
                        <el-input-number
                            v-model="form.darkElement"
                            :min="0"
                            :max="999"
                            controls-position="right"
                            style="width: 100%"
                        />
                    </el-form-item>
                </el-col>
            </el-row>

            <!-- 角色外观URL -->
            <el-form-item label="角色外观" prop="avatarUrl">
                <el-input
                    v-model="form.avatarUrl"
                    placeholder="请输入角色外观图片URL"
                />
            </el-form-item>
        </el-form>

        <div slot="footer" class="dialog-footer">
            <el-button @click="handleClose">取消</el-button>
            <el-button type="primary" @click="handleSubmit" :loading="submitting">
                确定
            </el-button>
        </div>
    </el-dialog>
</template>

<script>
import api from '@/api'

export default {
    name: 'CharacterDialog',
    props: {
        // 是否显示弹窗
        visible: {
            type: Boolean,
            default: false
        },
        // 编辑的角色数据（新增时为null）
        character: {
            type: Object,
            default: null
        }
    },
    data() {
        return {
            form: {
                userId: 1, // 默认绑定admin账户
                characterTypeId: null,
                characterName: '',
                jobNature: '',
                jobAttribute: '',
                strength: 0,
                intelligence: 0,
                spirit: 0,
                vitality: 0,
                physicalAttack: 0,
                magicalAttack: 0,
                independentAttack: 0,
                lightElement: 300,
                fireElement: 300,
                iceElement: 300,
                darkElement: 300,
                avatarUrl: ''
            },
            selectedCharacterType: [],
            characterTypeOptions: [],
            cascaderProps: {
                value: 'value',
                label: 'label',
                children: 'children',
                emitPath: false
            },
            isSpecialJob: false,
            submitting: false,
            rules: {
                characterName: [
                    { required: true, message: '请输入角色名称', trigger: 'blur' },
                    { min: 2, max: 20, message: '长度在 2 到 20 个字符', trigger: 'blur' }
                ],
                characterTypeId: [
                    { required: true, message: '请选择角色类型', trigger: 'change' }
                ],
                jobNature: [
                    { required: true, message: '请选择职业性质', trigger: 'change' }
                ],
                jobAttribute: [
                    { required: true, message: '请选择职业属性', trigger: 'change' }
                ]
            }
        }
    },
    computed: {
        dialogTitle() {
            return this.character ? '编辑角色' : '创建角色'
        },
        jobAttributeOptions() {
            if (this.form.jobNature === '输出') {
                return ['物理百分比', '魔法百分比', '物理固伤', '魔法固伤']
            } else if (this.form.jobNature === '奶系') {
                return ['智力', '精神', '体力']
            }
            return []
        },
        // 职业属性类型判断
        isPhysical() {
            return this.form.jobAttribute && this.form.jobAttribute.includes('物理')
        },
        isMagical() {
            return this.form.jobAttribute && this.form.jobAttribute.includes('魔法')
        },
        isPercent() {
            return this.form.jobAttribute && this.form.jobAttribute.includes('百分比')
        },
        isFixedDamage() {
            return this.form.jobAttribute && this.form.jobAttribute.includes('固伤')
        },
        // 字段显示控制
        showStrength() {
            return this.isPhysical
        },
        showIntelligence() {
            return this.isMagical
        },
        showSpirit() {
            return false // 精神字段不需要显示
        },
        showVitality() {
            return false // 体力字段不需要显示
        },
        showPhysicalAttack() {
            return this.isPhysical && this.isPercent
        },
        showMagicalAttack() {
            return this.isMagical && this.isPercent
        },
        showIndependentAttack() {
            return this.isFixedDamage
        },
        // 部分显示控制
        showFourDimensionalSection() {
            return this.showStrength || this.showIntelligence || this.showSpirit || this.showVitality
        },
        showThreeAttackSection() {
            return this.showPhysicalAttack || this.showMagicalAttack || this.showIndependentAttack
        }
    },
    watch: {
        visible(val) {
            if (val) {
                this.loadCharacterTypes()
                if (this.character) {
                    this.initForm()
                } else {
                    this.resetForm()
                }
            }
        }
    },
    methods: {
        async loadCharacterTypes() {
            try {
                const data = await this.$request({
                    url: api.character.characterType.cascade,
                    method: 'get'
                })
                this.characterTypeOptions = data || []
            } catch (error) {
                console.error('加载角色类型失败:', error)
                this.$message.error('加载角色类型失败')
            }
        },

        handleCharacterTypeChange(value) {
            this.form.characterTypeId = value

            // 查找选中的角色类型信息
            const findCharacterType = (options, typeId) => {
                for (const option of options) {
                    if (option.children) {
                        const found = option.children.find(child => child.value === typeId)
                        if (found) return found
                    }
                }
                return null
            }

            const characterType = findCharacterType(this.characterTypeOptions, value)
            if (characterType) {
                this.isSpecialJob = characterType.specialCase
                if (!this.isSpecialJob) {
                    this.form.jobNature = characterType.jobNature
                    this.form.jobAttribute = characterType.jobAttribute
                }
            }
        },

        initForm() {
            this.form = {
                ...this.form,
                ...this.character,
                userId: this.character.userId || 1
            }

            // 设置联级选择器的值
            if (this.character.characterTypeId) {
                this.selectedCharacterType = [this.character.characterTypeId]
                this.handleCharacterTypeChange(this.character.characterTypeId)
            }
        },

        resetForm() {
            this.form = {
                userId: 1,
                characterTypeId: null,
                characterName: '',
                jobNature: '',
                jobAttribute: '',
                strength: 0,
                intelligence: 0,
                spirit: 0,
                vitality: 0,
                physicalAttack: 0,
                magicalAttack: 0,
                independentAttack: 0,
                lightElement: 300,
                fireElement: 300,
                iceElement: 300,
                darkElement: 300,
                avatarUrl: ''
            }
            this.selectedCharacterType = []
            this.isSpecialJob = false
            if (this.$refs.characterForm) {
                this.$refs.characterForm.clearValidate()
            }
        },

        handleSubmit() {
            this.$refs.characterForm.validate(async (valid) => {
                if (valid) {
                    this.submitting = true
                    try {
                        if (this.character) {
                            // 更新
                            await this.$request({
                                url: api.character.update(this.character.id),
                                method: 'put',
                                data: this.form
                            })
                            this.$message.success('更新角色成功')
                        } else {
                            // 创建
                            await this.$request({
                                url: api.character.create,
                                method: 'post',
                                data: this.form
                            })
                            this.$message.success('创建角色成功')
                        }
                        this.$emit('success')
                        this.handleClose()
                    } catch (error) {
                        console.error('提交失败:', error)
                        this.$message.error('提交失败，请重试')
                    } finally {
                        this.submitting = false
                    }
                }
            })
        },

        handleClose() {
            this.resetForm()
            this.$emit('update:visible', false)
        }
    }
}
</script>

<style lang="less" scoped>
.character-form {
    .form-section-title {
        margin: 24px 0 12px 0;
        padding: 10px 12px;
        border-left: 4px solid #409eff;
        background-color: #f5f7fa;
        color: #303133;
        font-size: 15px;
        font-weight: 600;
        border-radius: 4px;
    }

    .form-tip {
        margin-top: 4px;
        font-size: 12px;
        color: #909399;
    }

    /deep/ .el-form-item {
        margin-bottom: 18px;
    }

    /deep/ .el-form-item:last-child {
        margin-bottom: 0;
    }
}

.dialog-footer {
    text-align: right;
}
</style>
