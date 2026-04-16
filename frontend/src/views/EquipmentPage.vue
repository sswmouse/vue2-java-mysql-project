<template>
    <div class="equipment-page">
        <div class="form-section">
            <h4>选择装备套装</h4>
            <el-select
                v-model="form.setName"
                placeholder="选择套装"
                class="equipment-select"
                @change="handleSetChange"
            >
                <el-option
                    label="理想之黄金乡套"
                    value="理想之黄金乡套"
                />
                <el-option
                    label="龙战八荒套"
                    value="龙战八荒套"
                />
                <el-option
                    label="潜影暗袭套"
                    value="潜影暗袭套"
                />
                <el-option
                    label="造化自然套"
                    value="造化自然套"
                />
                <el-option
                    label="天命者的气运套"
                    value="天命者的气运套"
                />
                <el-option
                    label="究极能量套"
                    value="究极能量套"
                />
                <el-option
                    label="混沌净化套"
                    value="混沌净化套"
                />
                <el-option
                    label="诸神黄昏之女武神套"
                    value="诸神黄昏之女武神套"
                />
                <el-option
                    label="青丘灵珠套"
                    value="青丘灵珠套"
                />
                <el-option
                    label="精灵国度套"
                    value="精灵国度套"
                />
                <el-option
                    label="冥思者的领域套"
                    value="冥思者的领域套"
                />
                <el-option
                    label="群猎美学套"
                    value="群猎美学套"
                />
            </el-select>
        </div>

        <div class="form-section">
            <h4>套装评分</h4>
            <el-input-number
                v-model="form.setScore"
                :min="0"
                :max="999"
                @change="handleChange"
            />
        </div>

        <div class="form-section">
            <h4>武器信息</h4>
            <el-form label-width="100px">
                <el-form-item label="武器名称">
                    <el-input
                        v-model="form.weaponName"
                        placeholder="武器名称"
                        @change="handleChange"
                    />
                </el-form-item>
                <el-form-item label="武器品级">
                    <el-select
                        v-model="form.weaponGrade"
                        @change="handleChange"
                    >
                        <el-option
                            label="普通"
                            value="普通"
                        />
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
                            label="太初"
                            value="太初"
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
    name: 'EquipmentPage',
    props: {
        characterId: {
            type: Number,
            required: true
        },
        equipmentData: {
            type: Object,
            default: () => ({})
        }
    },
    data() {
        return {
            saving: false,
            form: {
                setName: '',
                setIndex: 0,
                setScore: 0,
                weaponName: '',
                weaponGrade: '太初'
            }
        }
    },
    watch: {
        equipmentData: {
            handler(newData) {
                if (newData) {
                    if (newData.setName) this.form.setName = newData.setName
                    if (newData.setScore) this.form.setScore = newData.setScore
                    if (newData.pieces && newData.pieces.weapon) {
                        this.form.weaponName = newData.pieces.weapon.name || ''
                        this.form.weaponGrade = newData.pieces.weapon.grade || '太初'
                    }
                }
            },
            immediate: true
        }
    },
    methods: {
        handleSetChange() {
            const setIndexMap = {
                '理想之黄金乡套': 1,
                '龙战八荒套': 2,
                '潜影暗袭套': 3,
                '造化自然套': 4,
                '天命者的气运套': 5,
                '究极能量套': 6,
                '混沌净化套': 7,
                '诸神黄昏之女武神套': 8,
                '青丘灵珠套': 9,
                '精灵国度套': 10,
                '冥思者的领域套': 11,
                '群猎美学套': 12
            }
            this.form.setIndex = setIndexMap[this.form.setName] || 0
            this.handleChange()
        },
        handleChange() {
            this.$emit('update', this.form)
        },
        async handleSave() {
            this.saving = true
            try {
                const saveData = {
                    setName: this.form.setName,
                    setIndex: this.form.setIndex,
                    setScore: this.form.setScore,
                    pieces: {
                        weapon: {
                            name: this.form.weaponName,
                            grade: this.form.weaponGrade
                        }
                    }
                }
                await this.$request({
                    url: apiEnhancement.updateEquipment(this.characterId),
                    method: 'put',
                    data: saveData
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
.equipment-page {
    padding: 20px;

    .form-section {
        margin-bottom: 24px;

        h4 {
            margin-bottom: 12px;
        }

        .equipment-select {
            width: 300px;
        }
    }

    .actions {
        margin-top: 32px;
    }
}
</style>
