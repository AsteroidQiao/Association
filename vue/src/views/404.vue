<template>
  <div class="bg">
    <!--<img src="../assets/404.jpg">-->
    <div class="contains">
      <h1>404</h1>
      <h3>UHOH!页面丢失</h3>
      <p>
        您所寻找的页面不存在。你可以点击下面的按钮，<br>
        返回主页。
      </p>
      <!--如果是管理员或教师返回后台主界面-->
      <div v-if="user.urole==='管理员'||user.urole==='教师'">
        <el-button type="primary" plain @click="$router.push('/back/association')">点击返回</el-button>
      </div>
      <!--如果是游客或学生返回前台主界面-->
      <div v-else>
        <el-button class="btn" type="primary" plain @click="$router.push('/front/home')">点击返回</el-button>
      </div>

    </div>
  </div>
</template>

<script>
export default {
  name: "notfound",
  created() {
    this.user = localStorage.getItem('user') ? JSON.parse(localStorage.getItem('user')) : {}
  },
  data() {
    return {
      user: {},
    }
  }
}
</script>

<style scoped>
.bg {
  position: absolute;
  overflow: hidden;
  width: 100%;
  height: 100%;
  background: url("../assets/404.png") fixed no-repeat;
  background-size: cover;
}

.contains {
  margin-left: 60%;
  margin-top: 10%;
}

.contains h1 {
  font-size: 160px;
  margin-top: -10%;
}

.contains h3 {
  font-size: 50px;
  margin-top: 10%;
}

.btn {
  width: 200px;
  margin-top: 10px;
}

p {
  font-size: 20px;
  margin-top: 1%;
}
</style>
<!--<div id="Layer1" style=" ">-->
<!--<img src="pictures/background.jpg" width="100%" height="100%"/>-->