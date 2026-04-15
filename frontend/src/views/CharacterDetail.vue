<!--
 * @Description: 角色详情页面 - 角色打造总览
 * @Author: Claude Code
 * @Date: 2026-04-15
-->
<template>
    <div class="character-detail-page">
        <enhancement-layout
            :character="character"
            :active-tab="activeTab"
        >
            <!-- 总览 -->
            <template #overview>
                <div class="overview-content">
                    <h3>角色打造总览</h3>
                    <el-descriptions
                        :column="2"
                        border
                    >
                        <el-descriptions-item label="增幅">
                            +{{ enhancementData.amplify?.level || 0 }}
                            {{ enhancementData.amplify?.attribute || '' }}
                        </el-descriptions-item>
                        <el-descriptions-item label="武器">
                            {{ enhancementData.amplify?.isWeaponReinforce ? '强化' : '增幅' }}
                            +{{ enhancementData.amplify?.weaponLevel || 0 }}
                        </el-descriptions-item>
                        <el-descriptions-item label="属强">
                            {{ enhancementData.enchant?.elementType || '无' }}
                            +{{ enhancementData.enchant?.elementValue || 0 }}
                        </el-descriptions-item>
                        <el-descriptions-item label="称号">
                            {{ enhancementData.title?.name || '未设置' }}
                        </el-descriptions-item>
                        <el-descriptions-item label="宠物">
                            {{ enhancementData.pet?.pet || '未设置' }}
                        </el-descriptions-item>
                        <el-descriptions-item label="装备套装">
                            {{ enhancementData.equipment?.setName || '未设置' }}
                        </el-descriptions-item>
                    </el-descriptions>
                </div>
            </template>

            <!-- 增幅 -->
            <template #amplify>
                <amplify-page
                    :character-id="characterId"
                    :amplify-data="enhancementData.amplify || {}"
                    @saved="loadEnhancementData"
                />
            </template>

            <!-- 附魔 -->
            <template #enchant>
                <enchant-page
                    :character-id="characterId"
                    :enchant-data="enhancementData.enchant || {}"
                    @saved="loadEnhancementData"
                />
            </template>

            <!-- 称号 -->
            <template #title>
                <title-page
                    :character-id="characterId"
                    :title-data="enhancementData.title || {}"
                />
            </template>

            <!-- 宠物 -->
            <template #pet>
                <pet-page
                    :character-id="characterId"
                    :pet-data="enhancementData.pet || {}"
                />
            </template>

            <!-- 装备 -->
            <template #equipment>
                <equipment-page
                    :character-id="characterId"
                    :equipment-data="enhancementData.equipment || {}"
                />
            </template>

            <!-- 徽章 -->
            <template #emblem>
                <emblem-page
                    :character-id="characterId"
                    :emblem-data="enhancementData.emblem || {}"
                />
            </template>

            <!-- 卡片 -->
            <template #cards>
                <card-manager
                    :character-id="characterId"
                    :character="character"
                />
            </template>
        </enhancement-layout>
    </div>
</template>

<script>
import api from '@/api'
import apiEnhancement from '@/api/enhancement'
import EnhancementLayout from '@/components/EnhancementLayout.vue'
import AmplifyPage from '@/views/Amplify.vue'
import EnchantPage from '@/views/Enchant.vue'
import TitlePage from '@/views/TitlePage.vue'
import PetPage from '@/views/PetPage.vue'
import EquipmentPage from '@/views/EquipmentPage.vue'
import EmblemPage from '@/views/EmblemPage.vue'
import CardManager from '@/views/CardManager.vue'

export default {
    name: 'CharacterDetail',
    components: {
        EnhancementLayout,
        AmplifyPage,
        EnchantPage,
        TitlePage,
        PetPage,
        EquipmentPage,
        EmblemPage,
        CardManager
    },
    data() {
        return {
            characterId: null,
            character: {},
            activeTab: 'overview',
            enhancementData: {}
        }
    },
    mounted() {
        this.characterId = parseInt(this.$route.params.id)
        this.loadCharacter()
        this.loadEnhancementData()
    },
    methods: {
        async loadCharacter() {
            try {
                const data = await this.$request({
                    url: api.character.detail(this.characterId),
                    method: 'get'
                })
                this.character = data
            } catch (error) {
                console.error('加载角色失败:', error)
            }
        },
        async loadEnhancementData() {
            try {
                const data = await this.$request({
                    url: apiEnhancement.getEnhancementData(this.characterId),
                    method: 'get'
                })
                this.enhancementData = data || {}
            } catch (error) {
                console.error('加载打造数据失败:', error)
            }
        }
    }
}
</script>

<style lang="less" scoped>
.character-detail-page {
    min-height: 100%;
    background: var(--theme-bg-dark);
}
</style>
