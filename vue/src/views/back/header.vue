<template>
  <div>
    <div style="flex: 1;text-align: left">
      <el-button type="primary" @click="$router.push('/front/home')">查看前台页面</el-button>
    </div>
    <div class="title">
      <h2>{{setting.title2}}</h2>
    </div>
    <div class="dropdown">
      <el-dropdown style="width: 150px; cursor: pointer; margin-right: 40px">
        <div style="display: inline-block">
          <img class="MyAvatar" :src="user.url" alt="头像"/>
          <span>{{ user.uaccount }}</span>
          <i class="el-icon-arrow-down" style="margin-left: 5px"></i>
        </div>
        <el-dropdown-menu slot="dropdown" style="width: 100px; text-align: center">
          <el-dropdown-item style="font-size: 14px; padding: 5px 0">
            <el-link type="primary" :underline="false" href="/back/person">个人中心</el-link>
          </el-dropdown-item>
          <el-dropdown-item style="font-size: 14px; padding: 5px 0">
            <el-link type="primary" :underline="false" href="/back/password">修改密码</el-link>
          </el-dropdown-item>
          <el-dropdown-item style="font-size: 14px; padding: 5px 0">
            <span style="text-decoration: none" @click="logout">退出</span>
          </el-dropdown-item>
        </el-dropdown-menu>
      </el-dropdown>
    </div>
  </div>
</template>

<script>
export default {
  name: "Header",
  data() {
    return {
      isCollapse: false,
      setting: localStorage.getItem("setting") ? JSON.parse(localStorage.getItem("setting")) : {},
      user: localStorage.getItem("user") ? JSON.parse(localStorage.getItem("user")) : {},
    }
  },
  methods: {
    logout() {
      this.$store.commit("logout");
      this.$message.success("退出成功");
    },
  },
}
</script>

<style scoped>


.MyAvatar {
  margin-right: 7px;
  transition: all 0.6s;
  width: 33px;
  border-radius: 50%;
  position: relative;
  top: 10px;
  right: 5px;
}
.title{
  margin-top: -80px;
}
.MyAvatar:hover {
  transform: scale(1.5);
}

.dropdown {
  width: 180px;
  margin-top: -80px;
  margin-left: 86%;
}

</style>