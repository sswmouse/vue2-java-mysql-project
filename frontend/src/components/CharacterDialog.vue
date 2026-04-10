<!--
 * @Description: 角色添加/编辑弹窗组件
 * @Author: Claude Code
 * @Date: 2026-04-10
 * @LastEditTime: 2026-04-11 01:33:20
 * @FilePath: /vue2-java-mysql-project/frontend/src/components/CharacterDialog.vue
 -->
<template>
    <el-dialog
        :title="dialogTitle"
        :visible.sync="visible"
        :close-on-click-modal="false"
        width="600px"
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
            <el-form-item
                label="角色名称"
                prop="characterName"
            >
                <el-input
                    v-model="form.characterName"
                    placeholder="请输入角色名称"
                    maxlength="20"
                    show-word-limit
                />
            </el-form-item>

            <!-- 角色类型 - 联级选择器 -->
            <el-form-item
                label="角色类型"
                prop="characterTypeId"
            >
                <el-cascader
                    v-model="selectedCharacterType"
                    :options="characterTypeOptions"
                    :props="cascaderProps"
                    placeholder="请选择角色类型"
                    style="width: 100%"
                    @change="handleCharacterTypeChange"
                />
            </el-form-item>

            <!-- 职业性质 -->
            <el-form-item
                label="职业性质"
                prop="jobNature"
            >
                <el-select
                    v-model="form.jobNature"
                    placeholder="请选择职业性质"
                    style="width: 100%"
                >
                    <el-option
                        label="输出"
                        value="输出"
                    />
                    <el-option
                        label="奶系"
                        value="奶系"
                    />
                </el-select>
            </el-form-item>

            <!-- 职业属性 -->
            <el-form-item
                label="职业属性"
                prop="jobAttribute"
            >
                <el-select
                    v-model="form.jobAttribute"
                    placeholder="请选择职业属性"
                    style="width: 100%"
                >
                    <el-option
                        v-for="attr in jobAttributeOptions"
                        :key="attr"
                        :label="attr"
                        :value="attr"
                    />
                </el-select>
            </el-form-item>

            <el-row :gutter="16">
                <el-col
                    v-if="showStrength"
                    :span="12"
                >
                    <el-form-item
                        label="力量"
                        prop="strength"
                    >
                        <el-input
                            v-model="form.strength"
                            type="number"
                            :min="0"
                            :max="99999"
                            placeholder="请输入力量值"
                            :disabled="!canEditAttributes"
                            style="width: 100%"
                        />
                    </el-form-item>
                </el-col>
                <el-col
                    v-if="showIntelligence"
                    :span="12"
                >
                    <el-form-item
                        label="智力"
                        prop="intelligence"
                    >
                        <el-input
                            v-model="form.intelligence"
                            type="number"
                            :min="0"
                            :max="99999"
                            placeholder="请输入智力值"
                            :disabled="!canEditAttributes"
                            style="width: 100%"
                        />
                    </el-form-item>
                </el-col>
                <el-col
                    v-if="showSpirit"
                    :span="12"
                >
                    <el-form-item
                        label="精神"
                        prop="spirit"
                    >
                        <el-input
                            v-model="form.spirit"
                            type="number"
                            :min="0"
                            :max="99999"
                            placeholder="请输入精神值"
                            :disabled="!canEditAttributes"
                            style="width: 100%"
                        />
                    </el-form-item>
                </el-col>
                <el-col
                    v-if="showVitality"
                    :span="12"
                >
                    <el-form-item
                        label="体力"
                        prop="vitality"
                    >
                        <el-input
                            v-model="form.vitality"
                            type="number"
                            :min="0"
                            :max="99999"
                            placeholder="请输入体力值"
                            :disabled="!canEditAttributes"
                            style="width: 100%"
                        />
                    </el-form-item>
                </el-col>
                <el-col
                    v-if="showPhysicalAttack"
                    :span="12"
                >
                    <el-form-item
                        label="物攻"
                        prop="physicalAttack"
                    >
                        <el-input
                            v-model="form.physicalAttack"
                            type="number"
                            :min="0"
                            :max="99999"
                            placeholder="请输入物攻值"
                            :disabled="!canEditAttributes"
                            style="width: 100%"
                        />
                    </el-form-item>
                </el-col>
                <el-col
                    v-if="showMagicalAttack"
                    :span="12"
                >
                    <el-form-item
                        label="魔攻"
                        prop="magicalAttack"
                    >
                        <el-input
                            v-model="form.magicalAttack"
                            type="number"
                            :min="0"
                            :max="99999"
                            placeholder="请输入魔攻值"
                            :disabled="!canEditAttributes"
                            style="width: 100%"
                        />
                    </el-form-item>
                </el-col>
                <el-col
                    v-if="showIndependentAttack"
                    :span="12"
                >
                    <el-form-item
                        label="独立"
                        prop="independentAttack"
                    >
                        <el-input
                            v-model="form.independentAttack"
                            type="number"
                            :min="0"
                            :max="99999"
                            placeholder="请输入独立攻击值"
                            :disabled="!canEditAttributes"
                            style="width: 100%"
                        />
                    </el-form-item>
                </el-col>
            </el-row>

            <!-- 属强 -->
            <el-row
                v-if="showElementSection"
                :gutter="16"
            >
                <el-col :span="12">
                    <el-form-item
                        label="属强值"
                        prop="elementValue"
                    >
                        <el-input
                            v-model="form.elementValue"
                            type="number"
                            :min="0"
                            :max="999"
                            placeholder="请输入属强值"
                            :disabled="!canEditAttributes"
                            style="width: 100%"
                        />
                    </el-form-item>
                </el-col>
                <el-col :span="12">
                    <el-form-item
                        label="属强属性"
                        prop="elementType"
                    >
                        <el-select
                            v-model="form.elementType"
                            placeholder="请选择属强属性"
                            :disabled="!canEditAttributes"
                            style="width: 100%"
                        >
                            <el-option
                                label="光"
                                value="light"
                            />
                            <el-option
                                label="火"
                                value="fire"
                            />
                            <el-option
                                label="冰"
                                value="ice"
                            />
                            <el-option
                                label="暗"
                                value="dark"
                            />
                        </el-select>
                    </el-form-item>
                </el-col>
            </el-row>

            <!-- 角色外观 -->
            <el-form-item
                label="角色外观"
                prop="avatarUrl"
            >
                <div class="avatar-url-container">
                    <el-input
                        v-model="form.avatarUrl"
                        placeholder="请输入图片URL地址"
                        clearable
                    >
                        <template slot="prepend">
                            <i class="el-icon-link" />
                        </template>
                    </el-input>
                    <div class="url-tip">
                        请输入完整的图片URL地址，例如：https://example.com/avatar.jpg
                    </div>
                    <div
                        v-if="form.avatarUrl"
                        class="avatar-preview"
                    >
                        <div class="preview-label">预览：</div>
                        <img
                            :src="form.avatarUrl"
                            class="avatar-preview-image"
                            @error="handleImageError"
                        >
                    </div>
                </div>
            </el-form-item>
        </el-form>

        <div
            slot="footer"
            class="dialog-footer"
        >
            <el-button @click="handleClose">
                取消
            </el-button>
            <el-button
                type="primary"
                :loading="submitting"
                @click="handleSubmit"
            >
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
                elementValue: 300,
                elementType: 'light',
                lightElement: 0,
                fireElement: 0,
                iceElement: 0,
                darkElement: 0,
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
        isHealer() {
            return this.form.jobNature === '奶系'
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
            // 奶系职业显示力量，输出职业根据物理/魔法判断
            return this.isHealer || this.isPhysical
        },
        showIntelligence() {
            // 奶系职业显示智力，输出职业根据物理/魔法判断
            return this.isHealer || this.isMagical
        },
        showSpirit() {
            // 奶系职业显示精神
            return this.isHealer
        },
        showVitality() {
            // 奶系职业显示体力
            return this.isHealer
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
        },
        canEditAttributes() {
            return this.form.jobAttribute && this.form.jobAttribute.length > 0
        },
        // 属强部分显示控制
        showElementSection() {
            // 奶系职业不显示属强
            return this.selectedCharacterType.length > 0 && !this.isHealer
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
            // 将后端的属强字段映射到前端的elementValue和elementType
            const { lightElement, fireElement, iceElement, darkElement, ...characterData } = this.character

            // 确定当前属强类型和值
            let elementValue = 0
            let elementType = 'light'

            // 找到值最大的属强字段
            const elementMap = [
                { type: 'light', value: lightElement || 0 },
                { type: 'fire', value: fireElement || 0 },
                { type: 'ice', value: iceElement || 0 },
                { type: 'dark', value: darkElement || 0 }
            ]

            const maxElement = elementMap.reduce((max, curr) =>
                curr.value > max.value ? curr : max
            )

            elementValue = maxElement.value
            elementType = maxElement.type

            this.form = {
                ...this.form,
                ...characterData,
                userId: this.character.userId || 1,
                elementValue,
                elementType,
                lightElement: lightElement || 0,
                fireElement: fireElement || 0,
                iceElement: iceElement || 0,
                darkElement: darkElement || 0
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
                elementValue: 300,
                elementType: 'light',
                lightElement: 0,
                fireElement: 0,
                iceElement: 0,
                darkElement: 0,
                avatarUrl: ''
            }
            this.selectedCharacterType = []
            this.isSpecialJob = false
            if (this.$refs.characterForm) {
                this.$refs.characterForm.clearValidate()
            }
        },

        handleImageError() {
            this.$message.warning('图片加载失败，请检查URL地址')
        },
        prepareSubmitData() {
            const submitData = { ...this.form }

            // 根据选择的属强类型设置对应的属强字段
            const elementValue = parseInt(this.form.elementValue) || 0
            const elementType = this.form.elementType || 'light'

            // 重置所有属强字段为0
            submitData.lightElement = 0
            submitData.fireElement = 0
            submitData.iceElement = 0
            submitData.darkElement = 0

            // 设置对应类型的属强值
            switch (elementType) {
                case 'light':
                    submitData.lightElement = elementValue
                    break
                case 'fire':
                    submitData.fireElement = elementValue
                    break
                case 'ice':
                    submitData.iceElement = elementValue
                    break
                case 'dark':
                    submitData.darkElement = elementValue
                    break
            }

            // 移除前端自定义的字段，避免后端接收不到
            delete submitData.elementValue
            delete submitData.elementType

            return submitData
        },
        handleSubmit() {
            this.$refs.characterForm.validate(async (valid) => {
                if (valid) {
                    this.submitting = true
                    try {
                        const submitData = this.prepareSubmitData()
                        if (this.character) {
                            // 更新
                            await this.$request({
                                url: api.character.update(this.character.id),
                                method: 'put',
                                data: submitData
                            })
                            this.$message.success('更新角色成功')
                        } else {
                            // 创建
                            await this.$request({
                                url: api.character.create,
                                method: 'post',
                                data: submitData
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
        position: relative;

        &__error {
            position: absolute;
            z-index: 2000;
            background: #ef4444;
            color: #ffffff !important;
            padding: 4px 8px;
            border-radius: 4px;
            font-size: 12px;
            line-height: 1.5;
            box-shadow: 0 2px 4px rgba(0, 0, 0, 0.2);
            white-space: nowrap;
            top: 100%;
            left: 0;
            margin-top: 2px;

            &:before {
                content: '';
                position: absolute;
                bottom: 100%;
                left: 16px;
                border-width: 4px;
                border-style: solid;
                border-color: transparent transparent #ef4444 transparent;
            }
        }
    }

    /deep/ .el-form-item:last-child {
        margin-bottom: 0;
    }

    /* 隐藏数字输入框的上下箭头 */
    /deep/ input[type="number"]::-webkit-inner-spin-button,
    /deep/ input[type="number"]::-webkit-outer-spin-button {
        -webkit-appearance: none;
        appearance: none;
        margin: 0;
    }

    /deep/ input[type="number"] {
        -moz-appearance: textfield;
        appearance: textfield;
    }

    /deep/ .el-input .el-input__count .el-input__count-inner {
        background-color: transparent !important;
        color: #909399;
    }


    /deep/ .el-row {
        margin-bottom: 10px;
    }

    /deep/.el-input__inner {
        border-color: #475569 !important;
    }
}

.dialog-footer {
    text-align: right;

    .el-button {
        min-width: 100px;
        font-weight: 600;
        transition: all 0.3s ease;

        &:first-child {
            // 取消按钮
            background: #2d3748;
            border-color: #475569;
            color: #e2e8f0;

            &:hover {
                background: #4a5568;
                border-color: #6a11cb;
                color: #ffffff;
                transform: translateY(-2px);
                box-shadow: 0 4px 12px rgba(0, 0, 0, 0.4);
            }

            &:active {
                transform: translateY(0);
            }
        }

        &:last-child {

            // 确定按钮
            &:hover {
                transform: translateY(-2px);
                box-shadow: 0 4px 12px rgba(106, 17, 203, 0.3);
            }

            &:active {
                transform: translateY(0);
            }
        }
    }
}

/deep/ .el-dialog {
    background: #1e293b;
    border: 1px solid #1e293b;
    border-radius: 12px;
    box-shadow: 0 20px 25px -5px rgba(0, 0, 0, 0.6);

    &__header {
        background: rgba(26, 58, 95, 0.3);
        border-bottom: 1px solid #1e293b;
        padding: 24px;
        border-radius: 12px 12px 0 0;

        .el-dialog__title {
            color: #ffffff;
            font-weight: 700;
            font-size: 20px;
        }

        .el-dialog__headerbtn {
            position: absolute !important;
            top: 7px;

            .el-dialog__close {
                color: #b0b7c3;

                &:hover {
                    color: #ffffff;
                }
            }
        }
    }

    &__body {
        padding: 32px;
        color: #b0b7c3;
    }

    &__footer {
        background: rgba(15, 23, 42, 0.3);
        border-top: 1px solid #1e293b;
        padding: 24px;
        border-radius: 0 0 12px 12px;
    }
}

// 角色外观样式
.avatar-input-mode {
    margin-bottom: 16px;

    .el-radio-group {
        width: 100%;
    }

    .el-radio-button {
        flex: 1;
        text-align: center;

        &__inner {
            width: 100%;
        }
    }
}

.avatar-upload-container {
    .avatar-uploader {
        /deep/ .el-upload {
            border: 1px dashed #475569;
            border-radius: 8px;
            cursor: pointer;
            position: relative;
            overflow: hidden;
            width: 120px;
            height: 120px;
            transition: all 0.3s ease;

            &:hover {
                border-color: #6a11cb;
                transform: translateY(-2px);
                box-shadow: 0 4px 12px rgba(106, 17, 203, 0.2);
            }
        }

        .avatar-uploader-icon {
            font-size: 32px;
            color: #8a94a6;
            width: 120px;
            height: 120px;
            line-height: 120px;
            text-align: center;
        }

        .avatar {
            width: 120px;
            height: 120px;
            display: block;
            object-fit: cover;
            border-radius: 6px;
        }
    }

    .upload-tip {
        margin-top: 8px;
        font-size: 12px;
        color: #8a94a6;
        line-height: 1.5;
    }
}

.avatar-url-container {
    .el-input {
        margin-bottom: 12px;

        /deep/ .el-input-group__prepend {
            background: #2d3748;
            border-color: #475569;
            color: #b0b7c3;
        }
    }

    .url-tip {
        font-size: 12px;
        color: #8a94a6;
        margin-bottom: 16px;
        line-height: 1.5;
    }

    .avatar-preview {
        .preview-label {
            font-size: 14px;
            color: #b0b7c3;
            margin-bottom: 8px;
            font-weight: 500;
        }

        .avatar-preview-image {
            width: 120px;
            height: 120px;
            border-radius: 8px;
            object-fit: cover;
            border: 1px solid #475569;
            background: #1e293b;
        }
    }
}

// 级联选择器面板样式
/deep/ .el-cascader-panel,
/deep/ .el-cascader__dropdown {
    background: #1e293b !important;
    border: 1px solid #1e293b !important;

    .el-cascader-menu {
        background: #1e293b !important;
        border-right: 1px solid #475569 !important;

        &:last-child {
            border-right: none !important;
        }
    }
}

// 下拉选择器输入框样式
/deep/ .el-select,
/deep/ .el-cascader {
    .el-input__inner {
        background: #1e293b !important;
        border-color: #475569 !important;
        color: #b0b7c3 !important;

        &:focus {
            border-color: #6a11cb !important;
            box-shadow: 0 0 0 3px rgba(106, 17, 203, 0.2) !important;
        }
    }

    .el-input__suffix {
        .el-select__caret {
            color: #b0b7c3 !important;
        }
    }
}
</style>
<style lang="less">
// 下拉选择器和级联选择器弹窗样式
.el-cascader__dropdown,
.el-select-dropdown {
    background: #1e293b !important;
    border: 1px solid #6a11cb !important;
    box-shadow: 0 0 0 3px rgba(106, 17, 203, 0.2) !important;
    // box-shadow: 0 10px 15px -3px rgba(0, 0, 0, 0.5) !important;

    .el-select-dropdown__item,
    .el-cascader-node,
    .el-cascader-menu__item {
        color: #b0b7c3 !important;

        &:hover,
        &:focus {
            background: #2d3748 !important;
            color: #ffffff !important;
        }

        &.selected,
        &.is-active,
        &.el-cascader-node--selectable.in-active-path {
            color: #d4af37 !important;
            background: rgba(212, 175, 55, 0.1) !important;
        }
    }

    .el-select-dropdown__item.hover,
    .el-select-dropdown__item:hover {
        background: #2d3748 !important;
    }
    .el-cascader-menu {
        border-right: solid 1px #6a11cb;
    }
}
.el-popper .popper__arrow::after {
    top: 0 !important;
    border-bottom-color: #2d3748 !important;
}
</style>
