<template>
  <div class="wrapper">
    <div class="login-form">
      <el-form :model="user" :rules="rules" ref="userForm">
        <h2>登录</h2>
        <el-form-item prop="username">
          <el-input size="medium" style="margin: 10px 0" placeholder="请输入用户名" prefix-icon="el-icon-user"
                    v-model="user.username"></el-input>
        </el-form-item>
        <el-form-item prop="password">
          <el-input size="medium" style="margin: 10px 0" placeholder="请输入密码" prefix-icon="el-icon-lock"
                    show-password
                    v-model="user.password"></el-input>
        </el-form-item>
        <el-form-item style="margin: 10px 0; text-align: right">
          <el-button type="warning" size="medium" autocomplete="off" @click="$router.push('/register')">注册</el-button>
          <el-button type="primary" size="medium" autocomplete="off" @click="login()">登录</el-button>
        </el-form-item>
        <el-form-item style="margin: 10px ; text-align: right">
          <el-radio v-model="radio" label="学生">学生</el-radio>
          <el-radio v-model="radio" label="教师">教师</el-radio>
          <el-radio v-model="radio" label="管理员">管理员</el-radio>
        </el-form-item>
        <el-form-item style="margin: 10px ; text-align: right">
          <el-link type="danger"  autocomplete="off" @click="forget(user.username)">忘记密码？点我重新设置密码</el-link>
        </el-form-item>
      </el-form>
    </div>
  </div>
</template>

<script>


export default {
  name: "login",
  data() {
    return {
      user: {},
      radio: '学生',
      rules: {
        username: [
          {required: true, message: '请输入用户名', trigger: 'blur'},
          {min: 2, max: 10, message: '长度在 2 到 5 个字符', trigger: 'blur'}
        ],
        password: [
          {required: true, message: '请输入密码', trigger: 'blur'},
          {min: 1, max: 20, message: '长度在 1 到 20 个字符', trigger: 'blur'}
        ],
      }
    }
  },

  methods: {
    login() {
      this.$refs['userForm'].validate((valid) => {
        if (valid) {  // 表单校验合法
          axios.post("/UserController/UserLogin?" + "uaccount=" + this.user.username + "&upwd=" + this.user.password+'&urole='+this.radio).then((res) => {
            console.log(res.data.code)
            if (res.data.code === 200) {
              localStorage.setItem("user", JSON.stringify(res.data.data))  // 存储用户信息到浏览器
              this.$notify.success(res.data.msg)
              if(res.data.data.urole==="学生")
              this.$router.push("/front/home")
              else
                this.$router.push("/back/association")
            } else {
              this.$message.error(res.data.msg)
            }
          })
        }
      });
    },
    forget(account) {
      this.$router.push({
        path: '/forget',
        query: {
          account: account
        }
      })
    }
  }
}
</script>

<style>
.wrapper {
  height: 100vh;
  background-image: linear-gradient(to bottom right, #5dbe8a, #63bbd0);
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

.login-form::before {
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
  margin: 20px 0;
  position: relative;
  text-align: center;
  font-size: 24px
}
</style>
