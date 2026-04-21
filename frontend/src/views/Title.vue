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
                <el-button
                    type="primary"
                    size="small"
                    @click="handleAdd"
                >
                    <i class="el-icon-plus" />添加
                </el-button>
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
                    min-width="180"
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
                    min-width="80"
                    show-overflow-tooltip
                />
                <el-table-column
                    prop="type"
                    label="类型"
                    min-width="80"
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
                    min-width="80"
                    show-overflow-tooltip
                />
                <el-table-column
                    label="操作"
                    min-width="180"
                    fixed="right"
                    align="center"
                >
                    <template slot-scope="scope">
                        <el-button
                            type="text"
                            size="small"
                            @click="handleEdit(scope.row)"
                        >
                            编辑
                        </el-button>
                        <el-button
                            type="text"
                            size="small"
                            class="text-danger"
                            @click="handleDelete(scope.row)"
                        >
                            删除
                        </el-button>
                        <el-button
                            v-if="isEquipped(scope.row.id)"
                            type="warning"
                            size="mini"
                            @click="unequipTitle(scope.row)"
                        >
                            卸下
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

        <!-- 编辑对话框 -->
        <el-dialog
            :title="dialogTitle"
            :visible.sync="dialogVisible"
            width="600px"
            @close="resetForm"
        >
            <el-form
                ref="form"
                :model="form"
                label-width="100px"
            >
                <el-form-item
                    label="称号名称"
                    prop="name"
                >
                    <el-input
                        v-model="form.name"
                        placeholder="请输入称号名称"
                    />
                </el-form-item>
                <el-form-item
                    label="年份"
                    prop="year"
                >
                    <el-input-number
                        v-model="form.year"
                        :min="2000"
                        :max="2100"
                        placeholder="年份"
                    />
                </el-form-item>
                <el-form-item
                    label="类型"
                    prop="type"
                >
                    <el-select
                        v-model="form.type"
                        placeholder="选择类型"
                    >
                        <el-option
                            label="春节"
                            value="春节"
                        />
                        <el-option
                            label="五一"
                            value="五一"
                        />
                        <el-option
                            label="金秋"
                            value="金秋"
                        />
                        <el-option
                            label="至尊"
                            value="至尊"
                        />
                        <el-option
                            label="耕耘"
                            value="耕耘"
                        />
                    </el-select>
                </el-form-item>
                <el-form-item
                    label="四维"
                    prop="fourStats"
                >
                    <el-input-number
                        v-model="form.fourStats"
                        :min="0"
                        placeholder="四维属性"
                    />
                </el-form-item>
                <el-form-item
                    label="三攻"
                    prop="threeAttack"
                >
                    <el-input-number
                        v-model="form.threeAttack"
                        :min="0"
                        placeholder="三攻属性"
                    />
                </el-form-item>
                <el-form-item
                    label="暴击"
                    prop="critRate"
                >
                    <el-input-number
                        v-model="form.critRate"
                        :min="0"
                        placeholder="暴击率%"
                    />
                </el-form-item>
                <el-form-item
                    label="属强"
                    prop="elementalBonus"
                >
                    <el-input-number
                        v-model="form.elementalBonus"
                        :min="0"
                        placeholder="属强"
                    />
                </el-form-item>
                <el-form-item
                    label="三速"
                    prop="attackSpeed"
                >
                    <el-input-number
                        v-model="form.attackSpeed"
                        :min="0"
                        placeholder="三速%"
                    />
                </el-form-item>
                <el-form-item
                    label="攻强"
                    prop="attackStrength"
                >
                    <el-input-number
                        v-model="form.attackStrength"
                        :min="0"
                        placeholder="攻强%"
                    />
                </el-form-item>
                <el-form-item
                    label="名望"
                    prop="fame"
                >
                    <el-input-number
                        v-model="form.fame"
                        :min="0"
                        placeholder="名望值"
                    />
                </el-form-item>
            </el-form>
            <span
                slot="footer"
                class="dialog-footer"
            >
                <el-button @click="dialogVisible = false">取消</el-button>
                <el-button
                    type="primary"
                    :loading="saving"
                    @click="handleSave"
                >保存</el-button>
            </span>
        </el-dialog>
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
            saving: false,
            titles: [],
            years: [],
            selectedYear: null,
            characters: [],
            selectedCharacterId: null,
            equippedTitleIds: [],
            currentPage: 1,
            pageSize: 10,
            dialogVisible: false,
            dialogTitle: '添加称号',
            isEdit: false,
            editingId: null,
            form: {
                name: '',
                year: new Date().getFullYear(),
                type: '',
                fourStats: 0,
                threeAttack: 0,
                critRate: 0,
                elementalBonus: 0,
                attackSpeed: 0,
                attackStrength: 0,
                fame: 0
            }
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
            if (this.selectedCharacterId) {
                this.loadEquippedTitles()
            }
        },

        /**
         * 加载角色已穿戴的称号
         */
        async loadEquippedTitles() {
            try {
                const res = await this.$request({
                    url: titleApi.getEquipped(this.selectedCharacterId),
                    method: 'get'
                })
                this.equippedTitleIds = Array.isArray(res) ? res : (res.data || [])
            } catch (error) {
                console.error('加载已穿戴称号失败:', error)
                this.equippedTitleIds = []
            }
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
         * 添加称号
         */
        handleAdd() {
            this.dialogTitle = '添加称号'
            this.isEdit = false
            this.editingId = null
            this.resetForm()
            this.dialogVisible = true
        },

        /**
         * 编辑称号
         */
        handleEdit(row) {
            this.dialogTitle = '编辑称号'
            this.isEdit = true
            this.editingId = row.id
            this.form = {
                name: row.name,
                year: row.year,
                type: row.type,
                fourStats: row.fourStats || 0,
                threeAttack: row.threeAttack || 0,
                critRate: row.critRate || 0,
                elementalBonus: row.elementalBonus || 0,
                attackSpeed: row.attackSpeed || 0,
                attackStrength: row.attackStrength || 0,
                fame: row.fame || 0
            }
            this.dialogVisible = true
        },

        /**
         * 删除称号
         */
        handleDelete(row) {
            this.$confirm(`确定要删除称号「${row.name}」吗？`, '提示', {
                confirmButtonText: '确定',
                cancelButtonText: '取消',
                type: 'warning'
            }).then(async () => {
                try {
                    await this.$request({
                        url: titleApi.delete(row.id),
                        method: 'delete'
                    })
                    this.$message.success('删除成功')
                    this.loadTitles()
                } catch (error) {
                    console.error('删除称号失败:', error)
                    this.$message.error('删除失败')
                }
            }).catch(() => {})
        },

        /**
         * 保存称号
         */
        async handleSave() {
            if (!this.form.name) {
                this.$message.warning('请输入称号名称')
                return
            }
            this.saving = true
            try {
                if (this.isEdit) {
                    await this.$request({
                        url: titleApi.update(this.editingId),
                        method: 'put',
                        data: this.form
                    })
                    this.$message.success('更新成功')
                } else {
                    await this.$request({
                        url: titleApi.create,
                        method: 'post',
                        data: this.form
                    })
                    this.$message.success('添加成功')
                }
                this.dialogVisible = false
                this.loadTitles()
            } catch (error) {
                console.error('保存称号失败:', error)
                this.$message.error('保存失败')
            } finally {
                this.saving = false
            }
        },

        /**
         * 重置表单
         */
        resetForm() {
            this.form = {
                name: '',
                year: new Date().getFullYear(),
                type: '',
                fourStats: 0,
                threeAttack: 0,
                critRate: 0,
                elementalBonus: 0,
                attackSpeed: 0,
                attackStrength: 0,
                fame: 0
            }
        },

        /**
         * 穿戴称号
         */
        async equipTitle(row) {
            if (!this.selectedCharacterId) {
                this.$message.warning('请先选择角色')
                return
            }
            try {
                await this.$request({
                    url: titleApi.equip(row.id, this.selectedCharacterId),
                    method: 'post'
                })
                this.$message.success(`已穿戴称号「${row.name}」`)
                this.loadEquippedTitles()
            } catch (error) {
                console.error('穿戴称号失败:', error)
                this.$message.error('穿戴称号失败')
            }
        },

        /**
         * 卸下称号
         */
        async unequipTitle(row) {
            if (!this.selectedCharacterId) {
                this.$message.warning('请先选择角色')
                return
            }
            try {
                await this.$request({
                    url: titleApi.unequip(row.id, this.selectedCharacterId),
                    method: 'delete'
                })
                this.$message.success(`已卸下称号「${row.name}」`)
                this.loadEquippedTitles()
            } catch (error) {
                console.error('卸下称号失败:', error)
                this.$message.error('卸下称号失败')
            }
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