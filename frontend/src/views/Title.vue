<!--
 * @Description: 称号管理页面
 * @Author: Claude Code
 * @Date: 2026-04-21
 * @FilePath: /vue2-java-mysql-project/frontend/src/views/Title.vue
-->
<template>
    <div class="title-page">
        <!-- 页面标题 -->
        <div class="page-header">
            <div class="header-left">
                <h2 class="page-title">
                    <i class="el-icon-postcard" />
                    称号管理
                </h2>
            </div>
            <div class="header-right">
                <el-select
                    v-model="selectedYear"
                    placeholder="选择年份"
                    size="small"
                    clearable
                    class="year-select"
                    @change="handleYearChange"
                >
                    <el-option
                        v-for="year in years"
                        :key="year"
                        :label="`${year}年`"
                        :value="year"
                    />
                </el-select>
                <el-select
                    v-model="selectedCharacterId"
                    placeholder="选择角色"
                    size="small"
                    class="character-select"
                    @change="handleCharacterChange"
                >
                    <el-option
                        v-for="char in characters"
                        :key="char.id"
                        :label="char.characterName"
                        :value="char.id"
                    >
                        <span>{{ char.characterName }}</span>
                        <span class="char-level">LV.{{ char.level }}</span>
                    </el-option>
                </el-select>
            </div>
        </div>

        <!-- 称号列表 -->
        <div
            v-loading="loading"
            class="title-list"
        >
            <el-table
                :data="paginatedTitles"
                border
                stripe
                style="width: 100%"
            >
                <el-table-column
                    prop="name"
                    label="称号名称"
                    width="180"
                    fixed
                    show-overflow-tooltip
                >
                    <template slot-scope="scope">
                        <span class="title-name">{{ scope.row.name }}</span>
                        <el-tag
                            v-if="scope.row.type === '至尊'"
                            type="danger"
                            size="mini"
                        >
                            至尊
                        </el-tag>
                    </template>
                </el-table-column>
                <el-table-column
                    prop="year"
                    label="年份"
                    width="80"
                    align="center"
                    show-overflow-tooltip
                />
                <el-table-column
                    prop="type"
                    label="类型"
                    width="80"
                    align="center"
                    show-overflow-tooltip
                >
                    <template slot-scope="scope">
                        <el-tag
                            :type="getTypeTagType(scope.row.type)"
                            size="small"
                        >
                            {{ scope.row.type }}
                        </el-tag>
                    </template>
                </el-table-column>
                <el-table-column
                    label="属性"
                    min-width="400"
                    show-overflow-tooltip
                >
                    <template slot-scope="scope">
                        <span
                            v-if="scope.row.fourStats"
                            class="stat-item"
                        >四维+{{ scope.row.fourStats }}</span>
                        <span
                            v-if="scope.row.threeAttack"
                            class="stat-item"
                        >三攻+{{ scope.row.threeAttack }}</span>
                        <span
                            v-if="scope.row.critRate"
                            class="stat-item"
                        >暴击+{{ scope.row.critRate }}%</span>
                        <span
                            v-if="scope.row.elementalBonus"
                            class="stat-item"
                        >属强+{{ scope.row.elementalBonus }}</span>
                        <span
                            v-if="scope.row.attackSpeed"
                            class="stat-item"
                        >三速+{{ scope.row.attackSpeed }}%</span>
                        <span
                            v-if="scope.row.attackStrength"
                            class="stat-item"
                        >攻强+{{ scope.row.attackStrength }}%</span>
                        <span
                            v-if="scope.row.skillBonus"
                            class="stat-item skill"
                        >{{ scope.row.skillBonus }}</span>
                    </template>
                </el-table-column>
                <el-table-column
                    prop="fame"
                    label="名望"
                    width="80"
                    align="center"
                    show-overflow-tooltip
                />
                <el-table-column
                    label="操作"
                    width="80"
                    fixed="right"
                    align="center"
                >
                    <template slot-scope="scope">
                        <el-button
                            v-if="isEquipped(scope.row.id)"
                            type="success"
                            size="mini"
                            disabled
                        >
                            已穿戴
                        </el-button>
                        <el-button
                            v-else
                            type="primary"
                            size="mini"
                            @click="equipTitle(scope.row)"
                        >
                            穿戴
                        </el-button>
                    </template>
                </el-table-column>
            </el-table>

            <!-- 分页 -->
            <div class="pagination-wrapper">
                <el-pagination
                    :current-page="currentPage"
                    :page-sizes="[10, 20, 50, 100]"
                    :page-size="pageSize"
                    :total="filteredTitles.length"
                    layout="total, sizes, prev, pager, next, jumper"
                    @size-change="handleSizeChange"
                    @current-change="handleCurrentChange"
                />
            </div>
        </div>
    </div>
</template>

<script>
import titleApi from '@/api/title'

/**
 * 称号管理页面
 */
export default {
    name: 'Title',

    data() {
        return {
            loading: false,
            titles: [],
            years: [],
            selectedYear: null,
            characters: [],
            selectedCharacterId: null,
            equippedTitleIds: [],
            currentPage: 1,
            pageSize: 10
        }
    },

    computed: {
        filteredTitles() {
            if (!this.selectedYear) return this.titles
            return this.titles.filter(t => t.year === this.selectedYear)
        },
        paginatedTitles() {
            const start = (this.currentPage - 1) * this.pageSize
            const end = start + this.pageSize
            return this.filteredTitles.slice(start, end)
        }
    },

    mounted() {
        this.loadTitles()
        this.loadYears()
        this.loadCharacters()
    },

    methods: {
        /**
         * 加载称号列表
         */
        async loadTitles() {
            this.loading = true
            try {
                const res = await this.$request({
                    url: titleApi.getAll,
                    method: 'get'
                })
                this.titles = Array.isArray(res) ? res : (res.data || [])
            } catch (error) {
                console.error('加载称号列表失败:', error)
            } finally {
                this.loading = false
            }
        },

        /**
         * 加载年份列表
         */
        async loadYears() {
            try {
                const res = await this.$request({
                    url: titleApi.getYears,
                    method: 'get'
                })
                this.years = Array.isArray(res) ? res : (res.data || [])
            } catch (error) {
                console.error('加载年份列表失败:', error)
            }
        },

        /**
         * 加载角色列表
         */
        async loadCharacters() {
            try {
                const userId = this.$store.getters['auth/userId']
                if (!userId) return

                const res = await this.$request({
                    url: `/api/characters/user/${userId}`,
                    method: 'get'
                })

                const characters = Array.isArray(res) ? res : (res.data || [])
                if (Array.isArray(characters)) {
                    this.characters = characters
                    if (this.characters.length > 0 && !this.selectedCharacterId) {
                        this.selectedCharacterId = this.characters[0].id
                    }
                }
            } catch (error) {
                console.error('加载角色列表失败:', error)
            }
        },

        /**
         * 处理年份切换
         */
        handleYearChange() {
            this.currentPage = 1
        },

        /**
         * 处理每页数量变化
         */
        handleSizeChange(val) {
            this.pageSize = val
            this.currentPage = 1
        },

        /**
         * 处理页码变化
         */
        handleCurrentChange(val) {
            this.currentPage = val
        },

        /**
         * 处理角色切换
         */
        async handleCharacterChange() {
            // 加载角色已穿戴称号
        },

        /**
         * 获取类型标签颜色
         */
        getTypeTagType(type) {
            const typeMap = {
                '春节': 'warning',
                '五一': 'success',
                '金秋': 'danger'
            }
            return typeMap[type] || 'info'
        },

        /**
         * 检查是否已穿戴
         */
        isEquipped(titleId) {
            return this.equippedTitleIds.includes(titleId)
        },

        /**
         * 穿戴称号
         */
        async equipTitle() {
            this.$message.info('穿戴功能开发中')
        }
    }
}
</script>

<style lang="less" scoped>
@import '@/assets/styles/variables.less';

.title-page {
    padding: @spacing-xl;
    min-height: 100%;
    background: linear-gradient(180deg, var(--theme-bg-dark) 0%, var(--theme-primary) 100%);

    .page-header {
        display: flex;
        justify-content: space-between;
        align-items: flex-start;
        margin-bottom: @spacing-xl;
        background: var(--theme-bg-card);
        padding: @spacing-lg;
        border-radius: @border-radius-lg;
        border: 1px solid var(--theme-border);

        .header-left {
            flex: 1;
        }

        .header-right {
            display: flex;
            gap: @spacing-md;
            flex-shrink: 0;

            .year-select {
                width: 120px;
            }

            .character-select {
                min-width: 160px;

                .char-level {
                    float: right;
                    color: var(--theme-text-secondary);
                    font-size: 12px;
                }
            }
        }

        .page-title {
            font-family: @font-heading, sans-serif;
            font-size: 24px;
            font-weight: @font-weight-bold;
            color: var(--theme-accent);
            margin: 0 0 @spacing-xs 0;
            display: flex;
            align-items: center;
            gap: @spacing-sm;

            i {
                font-size: 28px;
            }
        }
    }

    .title-list {
        background: var(--theme-bg-card);
        border-radius: @border-radius-lg;
        border: 1px solid var(--theme-border);
        padding: @spacing-lg;

        .title-name {
            font-weight: @font-weight-semibold;
            white-space: nowrap;
        }

        .title-stats {
            display: flex;
            flex-wrap: nowrap;
            gap: 4px;
            overflow: hidden;

            .stat-item {
                display: inline-block;
                padding: 2px 6px;
                background: var(--theme-bg-hover);
                border-radius: @border-radius-sm;
                font-size: 12px;
                color: var(--theme-text-secondary);
                white-space: nowrap;

                &.skill {
                    color: var(--theme-info);
                    background: rgba(0, 180, 255, 0.1);
                }
            }
        }

        .fame-value {
            font-weight: @font-weight-bold;
            color: var(--theme-accent);
            white-space: nowrap;
        }

        .pagination-wrapper {
            margin-top: @spacing-lg;
            text-align: right;

            /deep/ .el-pagination {
                font-weight: normal;
            }

            /deep/ .btn-prev,
            /deep/ .btn-next,
            /deep/ .el-pager li {
                height: 28px;
                line-height: 28px;
                margin: 0 2px;
            }
        }
    }
}
</style>