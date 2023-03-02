<template>
  <div>
    <!--头部-->
    <div
        style="
        display: flex;
        height: 60px;
        line-height: 60px;
        border-bottom: 1px solid #eee;
      "
    >
      <div style="width: 300px; display: flex; padding-left: 30px">
        <div style="width: 60px">
          <img src="../../assets/icon.png" alt="图片暂时不能显示"
               style="width: 50px; position: relative; top: 5px; right: 0;border-radius: 10%;"
          />
        </div>
        <div style="flex: 1;color:#63bbd0;font: 50px 'TechnicBold'">Club</div>
      </div>
      <div style="flex:1;padding-left: 150px;">
        <el-menu
            :default-active="'/front/home'"
            class="el-menu-demo"
            mode="horizontal"
            active-text-color="#63bbd0"
            text-color="#31322c"
            router
        >
          <el-menu-item index="/front/home">首页</el-menu-item>
          <el-menu-item index="/front/activity">热门活动</el-menu-item>
          <el-menu-item index="/front/club">社团部落</el-menu-item>
          <el-menu-item index="/front/my-act">我的活动</el-menu-item>
          <el-menu-item index="/front/square">社区广场</el-menu-item>
          <el-menu-item index="/front/notification">通知信息</el-menu-item>
        </el-menu>
      </div>
      <div style="width: 200px">
        <div v-if="!user.uaccount" style="text-align: right; padding-right: 30px">
          <el-button @click="$router.push('/login')">登录</el-button>
          <el-button @click="$router.push('/register')">注册</el-button>
        </div>
        <div v-else>
          <div v-if="user.urole==='管理员'||user.urole==='教师'">
            <el-button type="primary" @click="$router.push('/back/association')">查看后台页面</el-button>
          </div>
          <div v-else>
            <el-dropdown style="width: 150px; cursor: pointer; text-align: right">
              <div style="display: inline-block">
                <img class="MyAvatar" :src="user.url" alt="头像"/>
                <span>{{ user.uaccount }}</span>
                <i class="el-icon-arrow-down" style="margin-left: 5px"></i>
              </div>
              <el-dropdown-menu slot="dropdown" style="width: 100px; text-align: center">
                <el-dropdown-item style="font-size: 14px; padding: 5px 0">
                  <el-link type="primary" :underline="false" href="/front/center/person">个人中心</el-link>
                </el-dropdown-item>
                <el-dropdown-item style="font-size: 14px; padding: 5px 0">
                  <el-link type="primary" :underline="false" href="/front/address">我的社团</el-link>
                </el-dropdown-item>
                <el-dropdown-item style="font-size: 14px; padding: 5px 0">
                  <span style="text-decoration: none" @click="logout">退出</span>
                </el-dropdown-item>
              </el-dropdown-menu>
            </el-dropdown>
          </div>
        </div>
      </div>
    </div>
    <!--主体-->
    <div style="width: 1000px; margin: 0 auto">
      <router-view/>
    </div>
  </div>

</template>

<script>


export default {
  name: "Front",
  data() {
    return {
      name: this.user = localStorage.getItem("user") ? JSON.parse(localStorage.getItem("user")) : {},
      user: "",
      pageNum: 1,
      pageSize: 12,
      total: 0,
    };
  },
  created() {
    this.load();
  },
  methods: {
    load() {
      this.user = localStorage.getItem("user")
          ? JSON.parse(localStorage.getItem("user"))
          : {};
    },
    logout() {
      this.$store.commit("logout");
      this.$message.success("退出成功");
    },

  },
};
</script>

<style>
body {

}

.item {
  display: inline-block;
  width: 100px;

  text-align: center;
  cursor: pointer;
}

.item a {
  color: #1e90ff;
}

.item:hover {
  background-color: LightPink;
}

a {
  text-decoration: none;
}

.MyAvatar {
  margin-right: 7px;
  transition: all 0.6s;
  width: 33px;
  border-radius: 50%;
  position: relative;
  top: 10px;
  right: 5px;

}

.MyAvatar:hover {
  transform: scale(1.5);
}
</style>
