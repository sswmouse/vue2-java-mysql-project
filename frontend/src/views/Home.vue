<template>
  <div class="home">
    <h1>欢迎使用Vue2 + Java + MySQL项目</h1>
    <div class="description">
      <p>这是一个完整的全栈项目示例，包含：</p>
      <ul>
        <li>前端：Vue2 + Element UI + Vue Router + Vuex</li>
        <li>后端：Java Spring Boot + MySQL</li>
        <li>数据库：MySQL</li>
      </ul>
      <p>点击上方导航栏的"用户管理"来管理用户数据。</p>
    </div>
    <div class="status">
      <el-alert title="项目状态" type="info" :closable="false">
        <p>前端运行在: <strong>http://localhost:8080</strong></p>
        <p>后端API运行在: <strong>http://localhost:8081</strong></p>
      </el-alert>
    </div>
  </div>
</template>

<script>
export default {
  name: 'Home'
}
</script>

<style scoped>
.home {
  padding: 20px;
}

.description {
  text-align: left;
  max-width: 800px;
  margin: 30px auto;
  padding: 20px;
  background-color: #f8f9fa;
  border-radius: 8px;
  box-shadow: 0 2px 4px rgba(0,0,0,0.1);
}

.description ul {
  margin: 10px 0 10px 20px;
}

.description li {
  margin: 5px 0;
}

.status {
  max-width: 600px;
  margin: 30px auto;
  text-align: left;
}
</style>