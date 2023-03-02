<template>
  <div class="wrapper">
    <div class="login-form">
      <h2>注 册</h2>      <el-form :model="user" :rules="rules" ref="userForm">
        <el-form-item prop="username">
          <el-input placeholder="请输入账号" size="medium" style="margin: 5px 0" prefix-icon="el-icon-user"
                    v-model="user.name"></el-input>
        </el-form-item>
        <el-form-item prop="password">
          <el-input placeholder="请输入密码" size="medium" style="margin: 5px 0" prefix-icon="el-icon-lock" show-password
                    v-model="user.password"></el-input>
        </el-form-item>
        <el-form-item prop="confirmPassword">
          <el-input placeholder="请确认密码" size="medium" style="margin: 5px 0" prefix-icon="el-icon-lock" show-password
                    v-model="user.confirmPassword"></el-input>
        </el-form-item>
        <el-form-item style="margin: 5px 0; text-align: right">
          <el-button type="primary" size="medium" autocomplete="off" @click="register()">注册</el-button>
          <el-button type="warning" size="medium" autocomplete="off" @click="$router.push('/login')">返回登录</el-button>
        </el-form-item>
      </el-form>
    </div>
  </div>
</template>

<script>
export default {
  name: "register",
  data() {
    return {
      user: {},
      rules: {
        name: [
          {required: true, message: '请输入用户名', trigger: 'blur'},
          {min: 3, max: 10, message: '长度在 3 到 5 个字符', trigger: 'blur'}
        ],
        password: [
          {required: true, message: '请输入密码', trigger: 'blur'},
          {min: 1, max: 20, message: '长度在 1 到 20 个字符', trigger: 'blur'}
        ],
        confirmPassword: [
          {required: true, message: '请确认密码', trigger: 'blur'},
          {min: 1, max: 20, message: '长度在 1 到 20 个字符', trigger: 'blur'}
        ],
      }
    }
  },
  methods: {
    register() {
      this.$refs['userForm'].validate((valid) => {
        if (valid) {  // 表单校验合法
          if (this.user.password !== this.user.confirmPassword) {
            this.$message.error("两次输入的密码不一致")
            return false
          }
          console.log(this.user)
          axios.post("/UserController/UserRegister?uaccount=" + this.user.name + "&upwd=" + this.user.password + "&uemail=" + this.user.email).then(res => {
            if (res.data.code === 200) {
              this.$message.success("注册成功")
            } else {
              this.$message.error(res.data.msg)
            }
          })
        }
      });
    }
  }
}
</script>

<style>
.wrapper {
  height: 100vh;
  background-image: linear-gradient(to bottom right, #5dbe8a , #63bbd0);
  overflow: hidden;
}
.login-form {
  margin: 200px auto;
  width: 350px;
  height: 350px;
  padding: 20px;
  border-radius: 10px;
  display: flex;
  flex-direction: column;
  position: relative;
  background: inherit;
  overflow: hidden;
}
.login-form::before{
  content: "";
  width: calc(100% + 20px);
  height: calc(100% + 20px);
  position: absolute;
  top: -10px;
  left: -10px;
  overflow: hidden;
  background: inherit;
  box-shadow: inset 0 0 0 200px rgba(255, 255, 255, 0.4);
  filter: blur(5px);
}
.login-form h2 {
  color: black;
  margin: 15px 0;
  position: relative;
  text-align: center;
  font-size: 24px
}
</style>
