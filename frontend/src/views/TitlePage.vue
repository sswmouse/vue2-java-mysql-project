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
                <el-option-group label="2026年">
                    <el-option
                        label="永恒的偏爱者"
                        value="永恒的偏爱者"
                    />
                    <el-option
                        label="永恒的开拓者"
                        value="永恒的开拓者"
                    />
                    <el-option
                        label="永恒的守护者"
                        value="永恒的守护者"
                    />
                </el-option-group>
                <el-option-group label="2025年金秋">
                    <el-option
                        label="无上荣光"
                        value="无上荣光"
                    />
                    <el-option
                        label="完美主义"
                        value="完美主义"
                    />
                    <el-option
                        label="至高之炎"
                        value="至高之炎"
                    />
                    <el-option
                        label="至阴之寒"
                        value="至阴之寒"
                    />
                    <el-option
                        label="至圣之雷"
                        value="至圣之雷"
                    />
                    <el-option
                        label="至幽之暗"
                        value="至幽之暗"
                    />
                </el-option-group>
                <el-option-group label="2025年春节">
                    <el-option
                        label="TX.创造者"
                        value="TX.创造者"
                    />
                    <el-option
                        label="TX.毁灭者"
                        value="TX.毁灭者"
                    />
                    <el-option
                        label="TX.洞察者"
                        value="TX.洞察者"
                    />
                    <el-option
                        label="TX.封印者"
                        value="TX.封印者"
                    />
                </el-option-group>
                <el-option-group label="2025年耕耘">
                    <el-option
                        label="大自然的守护者"
                        value="大自然的守护者"
                    />
                    <el-option
                        label="大自然的歌者"
                        value="大自然的歌者"
                    />
                    <el-option
                        label="大自然的银行"
                        value="大自然的银行"
                    />
                </el-option-group>
                <el-option-group label="2024年金秋">
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
                    <el-option
                        label="永恒的独裁者"
                        value="永恒的独裁者"
                    />
                    <el-option
                        label="创造之神"
                        value="创造之神"
                    />
                </el-option-group>
                <el-option-group label="2024年春节">
                    <el-option
                        label="龙年神选"
                        value="龙年神选"
                    />
                    <el-option
                        label="龙袍拥有者"
                        value="龙袍拥有者"
                    />
                    <el-option
                        label="龙拳至尊"
                        value="龙拳至尊"
                    />
                </el-option-group>
                <el-option-group label="2023年金秋">
                    <el-option
                        label="集卡大师"
                        value="集卡大师"
                    />
                    <el-option
                        label="称号大师"
                        value="称号大师"
                    />
                    <el-option
                        label="最强装扮"
                        value="最强装扮"
                    />
                </el-option-group>
                <el-option-group label="其他">
                    <el-option
                        label="未获得"
                        value=""
                    />
                </el-option-group>
            </el-select>
        </div>

        <div class="form-section">
            <h4>称号年份</h4>
            <el-select
                v-model="form.year"
                placeholder="选择年份"
                class="year-select"
                @change="handleChange"
            >
                <el-option
                    label="2026年"
                    value="2026"
                />
                <el-option
                    label="2025金秋"
                    value="2025金秋"
                />
                <el-option
                    label="2025春节"
                    value="2025春节"
                />
                <el-option
                    label="2025耕耘"
                    value="2025耕耘"
                />
                <el-option
                    label="2024金秋"
                    value="2024金秋"
                />
                <el-option
                    label="2024春节"
                    value="2024春节"
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
            color: var(--theme-text-primary);
            font-weight: 600;
        }

        .title-select,
        .year-select {
            width: 300px;
        }
    }

    .actions {
        margin-top: 32px;
    }
}
</style>
