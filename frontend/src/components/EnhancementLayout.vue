<template>
    <div class="enhancement-layout">
        <!-- 角色信息头部 -->
        <div class="character-header">
            <div class="character-info">
                <img
                    v-if="character.avatarUrl"
                    :src="character.avatarUrl"
                    class="avatar"
                >
                <div
                    v-else
                    class="avatar-placeholder"
                >
                    <i class="el-icon-user" />
                </div>
                <div class="info-text">
                    <h2>{{ character.characterName }}</h2>
                    <p>Lv.{{ character.level }} | 名望: {{ character.fameValue }}</p>
                    <p>{{ character.jobNature }} | {{ character.jobAttribute }}</p>
                </div>
            </div>
            <el-button @click="handleBack">
                返回角色列表
            </el-button>
        </div>

        <!-- Tab导航 -->
        <el-tabs
            v-model="activeTab"
            @tab-click="handleTabClick"
        >
            <el-tab-pane
                label="总览"
                name="overview"
            >
                <slot name="overview" />
            </el-tab-pane>
            <el-tab-pane
                label="增幅"
                name="amplify"
            >
                <slot name="amplify" />
            </el-tab-pane>
            <el-tab-pane
                label="附魔"
                name="enchant"
            >
                <slot name="enchant" />
            </el-tab-pane>
            <el-tab-pane
                label="称号"
                name="title"
            >
                <slot name="title" />
            </el-tab-pane>
            <el-tab-pane
                label="宠物"
                name="pet"
            >
                <slot name="pet" />
            </el-tab-pane>
            <el-tab-pane
                label="装备"
                name="equipment"
            >
                <slot name="equipment" />
            </el-tab-pane>
            <el-tab-pane
                label="卡片"
                name="cards"
            >
                <slot name="cards" />
            </el-tab-pane>
        </el-tabs>
    </div>
</template>

<script>
export default {
    name: 'EnhancementLayout',
    props: {
        character: {
            type: Object,
            required: true
        },
        activeTab: {
            type: String,
            default: 'overview'
        }
    },
    methods: {
        handleBack() {
            this.$router.push('/role')
        },
        handleTabClick(tab) {
            this.$emit('tab-change', tab.name)
        }
    }
}
</script>

<style lang="less" scoped>
.enhancement-layout {
    padding: 24px;

    .character-header {
        display: flex;
        justify-content: space-between;
        align-items: center;
        padding: 20px;
        background: var(--theme-bg-card);
        border-radius: 12px;
        margin-bottom: 24px;

        .character-info {
            display: flex;
            align-items: center;
            gap: 16px;

            .avatar {
                width: 80px;
                height: 80px;
                border-radius: 50%;
                object-fit: cover;
            }

            .avatar-placeholder {
                width: 80px;
                height: 80px;
                border-radius: 50%;
                background: var(--theme-bg-dark);
                display: flex;
                align-items: center;
                justify-content: center;
                font-size: 32px;
                color: var(--theme-text-secondary);
            }

            .info-text {
                h2 {
                    margin: 0 0 8px 0;
                    color: var(--theme-accent);
                }
                p {
                    margin: 4px 0;
                    color: var(--theme-text-secondary);
                }
            }
        }
    }
}
</style>
