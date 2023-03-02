<template>
  <div>
    <el-container >
      <el-aside width="200px">
        <Aside/>
      </el-aside>
      <el-container>
        <el-header>
          <Header/>
        </el-header>
        <el-main>
          <Main/>
        </el-main>
        <el-footer> <a href="https://asteroidqiao.top/">@auth河南师范大学-项园</a></el-footer>
      </el-container>
    </el-container>
  </div>

</template>

<script>
import Aside from "@/views/back/aside";
import Header from "@/views/back/header";
import Main from "@/views/back/main";

export default {
  name: "back",
  components: {Main, Header, Aside},
  methods: {},
  data() {
    return {}
  }
}
</script>

<style scoped>
body {
  overflow: hidden
}

.el-header {
  background-color: #ffffff;
  color: #333;
  text-align: center;
  line-height: 60px;
}

.el-aside {
  background-color: #D3DCE6;
  color: #333;
  overflow: hidden;
  text-align: center;
}

.el-footer {
  background-color: #8fb2c9;
  color: #333;
  text-align: center;
}

.el-main {
  background-color: #E9EEF3;
  color: #f0945d;
  overflow: hidden;
  text-align: center;
}
a{
  font:20px  '幼圆' ;
  line-height: 60px;
  text-decoration: none;
}
body > .el-container {
  margin-bottom: 40px;
}

.el-container:nth-child(5) .el-aside,
.el-container:nth-child(6) .el-aside {
  line-height: 260px;
}

.el-container:nth-child(7) .el-aside {
  line-height: 320px;
}
</style>