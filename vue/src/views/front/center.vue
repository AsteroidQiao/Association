<template>
  <div>
    <el-row class="tac" :gutter="30">
      <el-col :span="4">
        <el-menu default-active="1" class="el-menu-vertical-demo">
          <el-menu-item index="1">
            <i class="el-icon-user-solid"></i>
            <span slot="title"><router-link to="/front/center/person">个人资料</router-link></span>
          </el-menu-item>
          <el-menu-item index="2">
            <i class="el-icon-lock"></i>
            <router-link to="/front/center/password">修改密码</router-link>
          </el-menu-item>
        </el-menu>
      </el-col>
      <el-col :span="20">
        <router-view/>
      </el-col>
    </el-row>
  </div>
</template>

<script>
export default {
  name: "Center"
}
</script>

<style scoped>

</style>