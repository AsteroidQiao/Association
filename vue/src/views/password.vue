<template>
  <el-card style="width: 500px;">
    <el-form label-position="left" label-width="120px" size="small" :model="form" :rules="rules" ref="pass">

      <el-form-item label="原密码" prop="password">
        <el-input v-model="form.password" autocomplete="off" show-password></el-input>
      </el-form-item>
      <el-form-item label="新密码" prop="newPassword">
        <el-input v-model="form.newPassword" autocomplete="off" show-password></el-input>
      </el-form-item>
      <el-form-item label="确认新密码" prop="confirmPassword">
        <el-input v-model="form.confirmPassword" autocomplete="off" show-password></el-input>
      </el-form-item>
      <el-form-item>
        <el-button type="primary" @click="save">确 定</el-button>
      </el-form-item>
    </el-form>
  </el-card>
</template>

<script>
export default {
  name: "Password",
  data() {
    return {
      form: {},
      user: localStorage.getItem("user") ? JSON.parse(localStorage.getItem("user")) : {},
      rules: {
        password: [
          {required: true, message: '请输入原密码', trigger: 'blur'},
          {min: 3, message: '长度不少于3位', trigger: 'blur'}
        ],
        newPassword: [
          {required: true, message: '请输入新密码', trigger: 'blur'},
          {min: 3, message: '长度不少于3位', trigger: 'blur'}
        ],
        confirmPassword: [
          {required: true, message: '请输入密码', trigger: 'blur'},
          {min: 3, message: '长度不少于3位', trigger: 'blur'}
        ],
      }
    }
  },
  created() {
    this.form.username = this.user.username
  },
  methods: {
    save() {
      this.$refs.pass.validate((valid) => {
        if (valid) {
          if (this.form.newPassword !== this.form.confirmPassword) {
            this.$message.error("2次输入的新密码不相同")
            return false
          }
          axios.post("/UserController/UserUpdatepwd?uaccount=" + this.user.uaccount + "&oldpwd=" + this.form.password + "&newpwd=" + this.form.newPassword).then(res => {
            if (res.data.code === 200) {
              this.$notify.success(res.data.msg)
              this.$store.commit("logout")
            } else {
              this.$message.error(res.data.msg)
            }
          })
        }
      })
    },
  }
}
</script>

<style scoped>

</style>
