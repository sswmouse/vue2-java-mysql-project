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
            <el-form label-width="100px">
                <el-form-item label="头肩">
                    <el-select
                        v-model="form.head"
                        placeholder="选择宝珠"
                        @change="handleChange"
                    >
                        <el-option-group label="金秋套">
                            <el-option
                                label="金秋宝珠(技攻+30)"
                                value="金秋头肩宝珠(技攻+30)"
                            />
                            <el-option
                                label="金秋宝珠(技攻+4%)"
                                value="金秋头肩宝珠(技攻+4%)"
                            />
                            <el-option
                                label="金秋宝珠(暴击+8%)"
                                value="金秋头肩宝珠(暴击+8%)"
                            />
                            <el-option
                                label="金秋宝珠(三速+10%)"
                                value="金秋头肩宝珠(三速+10%)"
                            />
                        </el-option-group>
                        <el-option-group label="春节套">
                            <el-option
                                label="春节宝珠(技攻+30)"
                                value="春节头肩宝珠(技攻+30)"
                            />
                            <el-option
                                label="春节宝珠(技攻+4%)"
                                value="春节头肩宝珠(技攻+4%)"
                            />
                            <el-option
                                label="春节宝珠(暴击+8%)"
                                value="春节头肩宝珠(暴击+8%)"
                            />
                        </el-option-group>
                        <el-option-group label="其他">
                            <el-option
                                label="使徒宝珠(暴击+5%)"
                                value="使徒头肩宝珠(暴击+5%)"
                            />
                            <el-option
                                label="代号希望宝珠"
                                value="代号希望头肩宝珠"
                            />
                            <el-option
                                label="未附魔"
                                value=""
                            />
                        </el-option-group>
                    </el-select>
                </el-form-item>
                <el-form-item label="上衣/下装">
                    <el-select
                        v-model="form.chest"
                        placeholder="选择宝珠"
                        @change="handleChange"
                    >
                        <el-option-group label="金秋套">
                            <el-option
                                label="金秋上衣(力量/智力+70)"
                                value="金秋上衣宝珠(力智+70)"
                            />
                            <el-option
                                label="金秋下装(力量/智力+70)"
                                value="金秋下装宝珠(力智+70)"
                            />
                            <el-option
                                label="金秋上衣(体力/精神+65)"
                                value="金秋上衣宝珠(体精+65)"
                            />
                        </el-option-group>
                        <el-option-group label="春节套">
                            <el-option
                                label="春节上衣(力量/智力+60)"
                                value="春节上衣宝珠(力智+60)"
                            />
                            <el-option
                                label="春节下装(力量/智力+60)"
                                value="春节下装宝珠(力智+60)"
                            />
                        </el-option-group>
                        <el-option-group label="CP武器">
                            <el-option
                                label="CP上衣(技攻+5%)"
                                value="CP上衣宝珠(技攻+5%)"
                            />
                            <el-option
                                label="CP下装(技攻+5%)"
                                value="CP下装宝珠(技攻+5%)"
                            />
                        </el-option-group>
                        <el-option-group label="其他">
                            <el-option
                                label="使徒希洛克宝珠"
                                value="使徒希洛克宝珠"
                            />
                            <el-option
                                label="未附魔"
                                value=""
                            />
                        </el-option-group>
                    </el-select>
                </el-form-item>
                <el-form-item label="鞋/腰带">
                    <el-select
                        v-model="form.shoes"
                        placeholder="选择宝珠"
                        @change="handleChange"
                    >
                        <el-option-group label="金秋套">
                            <el-option
                                label="金秋鞋/腰带(力量/智力+55)"
                                value="金秋鞋/腰带宝珠(力智+55)"
                            />
                            <el-option
                                label="金秋鞋/腰带(体力/精神+50)"
                                value="金秋鞋/腰带宝珠(体精+50)"
                            />
                        </el-option-group>
                        <el-option-group label="春节套">
                            <el-option
                                label="春节鞋/腰带(力量/智力+45)"
                                value="春节鞋/腰带宝珠(力智+45)"
                            />
                            <el-option
                                label="春节鞋/腰带(移动+10%)"
                                value="春节鞋/腰带宝珠(移动+10%)"
                            />
                        </el-option-group>
                        <el-option-group label="其他">
                            <el-option
                                label="格斗场冠军宝珠"
                                value="格斗场冠军宝珠"
                            />
                            <el-option
                                label="未附魔"
                                value=""
                            />
                        </el-option-group>
                    </el-select>
                </el-form-item>
                <el-form-item label="腰带">
                    <el-select
                        v-model="form.legs"
                        placeholder="选择宝珠"
                        @change="handleChange"
                    >
                        <el-option-group label="金秋套">
                            <el-option
                                label="金秋腰带(力量/智力+55)"
                                value="金秋腰带宝珠(力智+55)"
                            />
                        </el-option-group>
                        <el-option-group label="春节套">
                            <el-option
                                label="春节腰带(力量/智力+45)"
                                value="春节腰带宝珠(力智+45)"
                            />
                        </el-option-group>
                        <el-option-group label="其他">
                            <el-option
                                label="未附魔"
                                value=""
                            />
                        </el-option-group>
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
                        <el-option-group label="单属强">
                            <el-option
                                label="光之主人宝珠(光强+25)"
                                value="光之主人宝珠(光强+25)"
                            />
                            <el-option
                                label="火之主人宝珠(火强+25)"
                                value="火之主人宝珠(火强+25)"
                            />
                            <el-option
                                label="冰之主人宝珠(冰强+25)"
                                value="冰之主人宝珠(冰强+25)"
                            />
                            <el-option
                                label="暗之主人宝珠(暗强+25)"
                                value="暗之主人宝珠(暗强+25)"
                            />
                        </el-option-group>
                        <el-option-group label="双属强">
                            <el-option
                                label="属性强化+20(光/火)"
                                value="光火双属强+20"
                            />
                            <el-option
                                label="属性强化+20(冰/暗)"
                                value="冰暗双属强+20"
                            />
                            <el-option
                                label="属性强化+22(光/火)"
                                value="光火双属强+22"
                            />
                            <el-option
                                label="属性强化+22(冰/暗)"
                                value="冰暗双属强+22"
                            />
                        </el-option-group>
                        <el-option-group label="全属强">
                            <el-option
                                label="全属性强化+18"
                                value="全属强+18"
                            />
                            <el-option
                                label="全属性强化+20"
                                value="全属强+20"
                            />
                            <el-option
                                label="全属性强化+23"
                                value="全属强+23"
                            />
                        </el-option-group>
                        <el-option-group label="金秋/春节">
                            <el-option
                                label="金秋首饰(属强+25)"
                                value="金秋首饰宝珠(属强+25)"
                            />
                            <el-option
                                label="金秋首饰(属强+30)"
                                value="金秋首饰宝珠(属强+30)"
                            />
                            <el-option
                                label="春节首饰(属强+23)"
                                value="春节首饰宝珠(属强+23)"
                            />
                        </el-option-group>
                        <el-option-group label="其他">
                            <el-option
                                label="未附魔"
                                value=""
                            />
                        </el-option-group>
                    </el-select>
                </el-form-item>
                <el-form-item label="手镯">
                    <el-select
                        v-model="form.bracelet"
                        placeholder="选择宝珠"
                        @change="handleChange"
                    >
                        <el-option-group label="单属强">
                            <el-option
                                label="光之主人宝珠(光强+25)"
                                value="光之主人手镯宝珠(光强+25)"
                            />
                            <el-option
                                label="火之主人宝珠(火强+25)"
                                value="火之主人手镯宝珠(火强+25)"
                            />
                            <el-option
                                label="冰之主人宝珠(冰强+25)"
                                value="冰之主人手镯宝珠(冰强+25)"
                            />
                            <el-option
                                label="暗之主人宝珠(暗强+25)"
                                value="暗之主人手镯宝珠(暗强+25)"
                            />
                        </el-option-group>
                        <el-option-group label="双属强">
                            <el-option
                                label="属性强化+22(光/火)"
                                value="光火双属强手镯+22"
                            />
                            <el-option
                                label="属性强化+22(冰/暗)"
                                value="冰暗双属强手镯+22"
                            />
                        </el-option-group>
                        <el-option-group label="全属强">
                            <el-option
                                label="全属性强化+18"
                                value="全属强手镯+18"
                            />
                            <el-option
                                label="全属性强化+20"
                                value="全属强手镯+20"
                            />
                        </el-option-group>
                        <el-option-group label="其他">
                            <el-option
                                label="未附魔"
                                value=""
                            />
                        </el-option-group>
                    </el-select>
                </el-form-item>
                <el-form-item label="戒指">
                    <el-select
                        v-model="form.ring"
                        placeholder="选择宝珠"
                        @change="handleChange"
                    >
                        <el-option-group label="单属强">
                            <el-option
                                label="光之主人宝珠(光强+25)"
                                value="光之主人戒指宝珠(光强+25)"
                            />
                            <el-option
                                label="火之主人宝珠(火强+25)"
                                value="火之主人戒指宝珠(火强+25)"
                            />
                            <el-option
                                label="冰之主人宝珠(冰强+25)"
                                value="冰之主人戒指宝珠(冰强+25)"
                            />
                            <el-option
                                label="暗之主人宝珠(暗强+25)"
                                value="暗之主人戒指宝珠(暗强+25)"
                            />
                        </el-option-group>
                        <el-option-group label="双属强">
                            <el-option
                                label="属性强化+22(光/火)"
                                value="光火双属强戒指+22"
                            />
                            <el-option
                                label="属性强化+22(冰/暗)"
                                value="冰暗双属强戒指+22"
                            />
                        </el-option-group>
                        <el-option-group label="全属强">
                            <el-option
                                label="全属性强化+18"
                                value="全属强戒指+18"
                            />
                            <el-option
                                label="全属性强化+20"
                                value="全属强戒指+20"
                            />
                        </el-option-group>
                        <el-option-group label="其他">
                            <el-option
                                label="未附魔"
                                value=""
                            />
                        </el-option-group>
                    </el-select>
                </el-form-item>
            </el-form>
        </div>

        <!-- 特殊装备附魔 -->
        <div class="form-section">
            <h4>特殊装备附魔</h4>
            <el-form label-width="100px">
                <el-form-item label="辅助装备">
                    <el-select
                        v-model="form.subEquip"
                        placeholder="选择宝珠"
                        @change="handleChange"
                    >
                        <el-option-group label="金秋套">
                            <el-option
                                label="金秋辅助(力量/智力+70)"
                                value="金秋辅助装备宝珠(力智+70)"
                            />
                            <el-option
                                label="金秋辅助(体力/精神+65)"
                                value="金秋辅助装备宝珠(体精+65)"
                            />
                            <el-option
                                label="金秋辅助(徽章栏)"
                                value="金秋辅助装备宝珠(徽章栏)"
                            />
                        </el-option-group>
                        <el-option-group label="春节套">
                            <el-option
                                label="春节辅助(力量/智力+60)"
                                value="春节辅助装备宝珠(力智+60)"
                            />
                            <el-option
                                label="春节辅助(体力/精神+55)"
                                value="春节辅助装备宝珠(体精+55)"
                            />
                        </el-option-group>
                        <el-option-group label="其他">
                            <el-option
                                label="时空石宝珠"
                                value="时空石辅助装备宝珠"
                            />
                            <el-option
                                label="未附魔"
                                value=""
                            />
                        </el-option-group>
                    </el-select>
                </el-form-item>
                <el-form-item label="魔法石">
                    <el-select
                        v-model="form.magicStone"
                        placeholder="选择宝珠"
                        @change="handleChange"
                    >
                        <el-option-group label="属强">
                            <el-option
                                label="光属性强化+20"
                                value="光强魔法石+20"
                            />
                            <el-option
                                label="火属性强化+20"
                                value="火强魔法石+20"
                            />
                            <el-option
                                label="冰属性强化+20"
                                value="冰强魔法石+20"
                            />
                            <el-option
                                label="暗属性强化+20"
                                value="暗强魔法石+20"
                            />
                            <el-option
                                label="全属性强化+18"
                                value="全属强魔法石+18"
                            />
                            <el-option
                                label="全属性强化+20"
                                value="全属强魔法石+20"
                            />
                        </el-option-group>
                        <el-option-group label="金秋/春节">
                            <el-option
                                label="金秋魔法石(属强+25)"
                                value="金秋魔法石宝珠(属强+25)"
                            />
                            <el-option
                                label="春节魔法石(属强+23)"
                                value="春节魔法石宝珠(属强+23)"
                            />
                        </el-option-group>
                        <el-option-group label="其他">
                            <el-option
                                label="使徒安图恩宝珠"
                                value="使徒安图恩魔法石宝珠"
                            />
                            <el-option
                                label="未附魔"
                                value=""
                            />
                        </el-option-group>
                    </el-select>
                </el-form-item>
                <el-form-item label="耳环">
                    <el-select
                        v-model="form.auxiliary"
                        placeholder="选择宝珠"
                        @change="handleChange"
                    >
                        <el-option-group label="金秋套">
                            <el-option
                                label="金秋耳环(力量/智力+70)"
                                value="金秋耳环宝珠(力智+70)"
                            />
                            <el-option
                                label="金秋耳环(体力/精神+65)"
                                value="金秋耳环宝珠(体精+65)"
                            />
                        </el-option-group>
                        <el-option-group label="春节套">
                            <el-option
                                label="春节耳环(力量/智力+60)"
                                value="春节耳环宝珠(力智+60)"
                            />
                            <el-option
                                label="春节耳环(体力/精神+55)"
                                value="春节耳环宝珠(体精+55)"
                            />
                        </el-option-group>
                        <el-option-group label="其他">
                            <el-option
                                label="四维+100宝珠"
                                value="四维+100耳环宝珠"
                            />
                            <el-option
                                label="四维+125宝珠"
                                value="四维+125耳环宝珠"
                            />
                            <el-option
                                label="使徒罗特斯宝珠"
                                value="使徒罗特斯耳环宝珠"
                            />
                            <el-option
                                label="未附魔"
                                value=""
                            />
                        </el-option-group>
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
                elementType: '全',
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

    .current-status {
        background: var(--theme-bg-card);
        border-radius: 8px;
        padding: 16px 20px;
        margin-bottom: 24px;
        border: 1px solid var(--theme-border);

        h3 {
            margin: 0 0 8px 0;
            color: var(--theme-text-primary);
        }

        p {
            margin: 0;
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

        .element-input {
            margin-left: 12px;
        }

        .el-select {
            width: 300px;
        }
    }

    .actions {
        margin-top: 32px;
    }
}
</style>
