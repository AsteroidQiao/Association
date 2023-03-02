<template>
  <div class="aside">
    <div class="menu">
      <el-menu
          :collapse="isCollapse"
          default-active="/back/association"
          class="el-menu-vertical-demo"
          unique-opened
          @open="handleOpen"
          @close="handleClose">
        <el-submenu index="/back/association">
          <template slot="title">
            <i class="el-icon-location"></i>
            <span>社团管理</span>
          </template>
          <el-menu-item-group>
            <el-menu-item index="/back/association" @click="$router.push('/back/association')">社团中心</el-menu-item>
            <el-menu-item index="/back/addClub" @click="$router.push('/back/addClub')">新增社团</el-menu-item>
          </el-menu-item-group>
        </el-submenu>
        <el-submenu index="/back/activity">
          <template slot="title">
            <i class="el-icon-menu"></i>
            <span>活动管理</span>
          </template>
          <el-menu-item-group>
            <el-menu-item index="/back/activity" @click="$router.push('/back/activity')">活动中心</el-menu-item>
            <el-menu-item index="/back/addActivity" @click="$router.push('/back/addActivity')">发布活动</el-menu-item>
          </el-menu-item-group>
        </el-submenu>
        <el-menu-item index="/back/role">
          <i class="el-icon-document"></i>
          <span slot="title">角色管理</span>
        </el-menu-item>
        <el-menu-item index="/back/score">
          <i class="el-icon-setting"></i>
          <span slot="title">学分管理</span>
        </el-menu-item>
        <el-menu-item index="/back/college">
          <i class="el-icon-setting"></i>
          <span slot="title" @click="$router.push('/back/college')">学院管理</span>
        </el-menu-item>
        <el-menu-item index="/back/file">
          <i class="el-icon-setting"></i>
          <span slot="title" @click="$router.push('/back/major')">专业管理</span>
        </el-menu-item>
      </el-menu>
    </div>
  </div>
</template>

<script>
export default {
  name: "Aside",
  data() {
    return {
      isCollapse: false,
    }
  },
  methods: {
    collapse() {
      this.isCollapse = this.isCollapse === false;
      localStorage.setItem('isCollapse', this.isCollapse)
      // console.log(this.isCollapse)
    },
    handleOpen(key, keyPath) {
      console.log(key,keyPath)
      this.$router.push(key)
    },
    handleClose(key, keyPath) {
      console.log(key, keyPath);
    }
  }
}
</script>

<style scoped>


</style>