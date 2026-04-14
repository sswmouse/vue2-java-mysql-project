<!--
 * @Description: 宠物管理页面
 * @Author: Claude Code
 * @Date: 2026-04-10
 * @LastEditTime: 2026-04-10
 * @FilePath: /frontend/src/views/Pet.vue
 -->
<template>
    <div class="pet-page">
        <h1>宠物管理</h1>
        <div class="page-content">
            <el-card class="empty-card">
                <div class="empty-content">
                    <i class="el-icon-dog" />
                    <p>宠物管理页面内容正在开发中...</p>
                    <p>这里将展示宠物列表、宠物培养、宠物技能等功能</p>
                </div>
            </el-card>
        </div>
    </div>
</template>

<script>
export default {
    name: 'Pet'
}
</script>

<style lang="less" scoped>
.pet-page {
    padding: 20px;
    min-height: 100%;
    height: 100%;
    overflow-y: auto;

    .page-content {
        margin-top: 20px;

        .empty-card {
            text-align: center;
            padding: 40px;

            .empty-content {
                i {
                    font-size: 60px;
                    color: #909399;
                    margin-bottom: 20px;
                }

                p {
                    margin: 10px 0;
                    color: #606266;
                    font-size: 14px;

                    &:first-of-type {
                        font-size: 16px;
                        font-weight: bold;
                        color: #303133;
                    }
                }
            }
        }
    }
}
</style>