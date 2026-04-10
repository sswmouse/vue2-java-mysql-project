<!--
 * @Description: 用户管理组件
 * @Author: System
 * @Date: 2026-04-10
 * @LastEditTime: 2026-04-10
 * @FilePath: /frontend/src/views/Users.vue
 -->
<template>
    <div class="users">
        <h1>用户管理</h1>

        <div class="user-form">
            <el-form
                ref="userForm"
                :model="form"
                :rules="rules"
                label-width="100px"
            >
                <el-form-item
                    label="用户名"
                    prop="username"
                >
                    <el-input
                        v-model="form.username"
                        placeholder="请输入用户名"
                    />
                </el-form-item>
                <el-form-item
                    label="邮箱"
                    prop="email"
                >
                    <el-input
                        v-model="form.email"
                        placeholder="请输入邮箱"
                    />
                </el-form-item>
                <el-form-item
                    label="密码"
                    prop="password"
                >
                    <el-input
                        v-model="form.password"
                        type="password"
                        placeholder="请输入密码"
                    />
                </el-form-item>
                <el-form-item>
                    <el-button
                        type="primary"
                        @click="handleSubmit"
                    >
                        提交
                    </el-button>
                    <el-button @click="resetForm">
                        重置
                    </el-button>
                </el-form-item>
            </el-form>
        </div>

        <div class="user-table">
            <el-table
                :data="users"
                style="width: 100%"
            >
                <el-table-column
                    prop="id"
                    label="ID"
                    width="80"
                />
                <el-table-column
                    prop="username"
                    label="用户名"
                />
                <el-table-column
                    prop="email"
                    label="邮箱"
                />
                <el-table-column
                    label="操作"
                    width="200"
                >
                    <template slot-scope="scope">
                        <el-button
                            size="small"
                            @click="handleEdit(scope.row)"
                        >
                            编辑
                        </el-button>
                        <el-button
                            size="small"
                            type="danger"
                            @click="handleDelete(scope.row)"
                        >
                            删除
                        </el-button>
                    </template>
                </el-table-column>
            </el-table>
        </div>

        <el-dialog
            title="编辑用户"
            :visible.sync="dialogVisible"
        >
            <el-form
                :model="editForm"
                label-width="100px"
            >
                <el-form-item label="用户名">
                    <el-input v-model="editForm.username" />
                </el-form-item>
                <el-form-item label="邮箱">
                    <el-input v-model="editForm.email" />
                </el-form-item>
                <el-form-item>
                    <el-button
                        type="primary"
                        @click="updateUser"
                    >
                        更新
                    </el-button>
                    <el-button @click="dialogVisible = false">
                        取消
                    </el-button>
                </el-form-item>
            </el-form>
        </el-dialog>
    </div>
</template>

<script>
import api from '@/api'

export default {
    name: 'Users',
    data() {
        return {
            users: [],
            form: {
                username: '',
                email: '',
                password: ''
            },
            editForm: {
                id: '',
                username: '',
                email: ''
            },
            dialogVisible: false,
            rules: {
                username: [
                    { required: true, message: '请输入用户名', trigger: 'blur' },
                    { min: 3, max: 20, message: '长度在 3 到 20 个字符', trigger: 'blur' }
                ],
                email: [
                    { required: true, message: '请输入邮箱地址', trigger: 'blur' },
                    { type: 'email', message: '请输入正确的邮箱地址', trigger: 'blur' }
                ],
                password: [
                    { required: true, message: '请输入密码', trigger: 'blur' },
                    { min: 6, message: '密码长度不能小于6位', trigger: 'blur' }
                ]
            }
        }
    },
    mounted() {
        this.loadUsers()
    },
    methods: {
        async loadUsers() {
            try {
                const data = await this.$request({
                    url: api.user.list,
                    method: 'get'
                })
                this.users = data || []
            } catch (error) {
                console.error('加载用户列表失败:', error)
                // 如果没有后端API，显示示例数据
                this.users = [
                    { id: 1, username: 'admin', email: 'admin@example.com' },
                    { id: 2, username: 'user1', email: 'user1@example.com' }
                ]
            }
        },
        handleSubmit() {
            this.$refs.userForm.validate(async (valid) => {
                if (valid) {
                    try {
                        await this.$request({
                            url: api.user.create,
                            method: 'post',
                            data: this.form
                        })
                        this.$message.success('创建用户成功')
                        this.resetForm()
                        this.loadUsers()
                    } catch (error) {
                        console.error('创建用户失败:', error)
                        this.$message.error('创建用户失败')
                    }
                } else {
                    return false
                }
            })
        },
        resetForm() {
            this.$refs.userForm.resetFields()
        },
        handleEdit(user) {
            this.editForm = { ...user }
            this.dialogVisible = true
        },
        async updateUser() {
            try {
                await this.$request({
                    url: api.user.update.replace(':id', this.editForm.id),
                    method: 'put',
                    data: this.editForm
                })
                this.$message.success('更新用户成功')
                this.dialogVisible = false
                this.loadUsers()
            } catch (error) {
                console.error('更新用户失败:', error)
                this.$message.error('更新用户失败')
            }
        },
        async handleDelete(user) {
            this.$confirm('确认删除该用户吗?', '提示', {
                confirmButtonText: '确定',
                cancelButtonText: '取消',
                type: 'warning'
            }).then(async () => {
                try {
                    await this.$request({
                        url: api.user.delete.replace(':id', user.id),
                        method: 'delete'
                    })
                    this.$message.success('删除用户成功')
                    this.loadUsers()
                } catch (error) {
                    console.error('删除用户失败:', error)
                    this.$message.error('删除用户失败')
                }
            }).catch(() => {
                // 取消删除
            })
        }
    }
}
</script>

<style lang="less" scoped>
.users {
    padding: 20px;
    max-width: 1000px;
    margin: 0 auto;

    .user-form {
        margin: 30px 0;
        padding: 20px;
        background-color: #f8f9fa;
        border-radius: 8px;
        box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
    }

    .user-table {
        margin-top: 30px;
    }
}
</style>