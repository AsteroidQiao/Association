<template>

  <div style="width: 70%;margin-top: 60px;margin-left: 15%">
<!--    <el-tag type="danger" size="large">忘记密码</el-tag>-->
    <el-steps :active="active" finish-status="success">
      <el-step title="步骤 1" description="请回答密保问题"></el-step>
      <el-step title="步骤 2" description="请重新设置密码"></el-step>
      <el-step title="步骤 3" description="返回登录"></el-step>
    </el-steps>
    <!--    step0密保问题-->
    <div v-if="active===0" style="margin-top: 80px">
      <el-link type="primary" size="large" disabled>亲爱的用户:{{ this.account }}</el-link>
      <el-divider></el-divider>
      <el-link type="danger" size="large" disabled>请回答:{{ this.question }}</el-link>
      <el-divider></el-divider>
      <el-input v-model="uanswer" size="large" placeholder="在此处回答密保问题"></el-input>
      <el-button style="margin-top: 12px;" @click="next">下一步</el-button>
    </div>
    <!--    step1设置密码-->
    <div v-if="active===1" style="margin-top: 80px">
      <el-card style="width: 550px;">
        <el-form label-position="left" label-width="120px" size="small" :model="form" :rules="rules" ref="pass">
          <el-form-item label="新密码" prop="newPassword">
            <el-input v-model="form.newPassword" autocomplete="off" show-password></el-input>
          </el-form-item>
          <el-form-item label="确认新密码" prop="confirmPassword">
            <el-input v-model="form.confirmPassword" autocomplete="off" show-password></el-input>
          </el-form-item>
          <el-form-item>
            <el-button type="primary" @click="active=0">上一步</el-button>
            <el-button type="primary" @click="save">下一步</el-button>
          </el-form-item>
        </el-form>
      </el-card>
    </div>
    <!--    step2设置密码-->
    <div v-if="active===2" style="margin-top: 80px">
      <el-link type="primary" size="large" disabled>用户{{ this.account }}修改密码成功！</el-link>
      <el-divider></el-divider>
      <el-form>
        <el-form-item>
          <el-button style="margin-top: 12px;" @click="active=1">上一步</el-button>
          <el-button style="margin-top: 12px;" @click="$router.push('/login')">返回登录</el-button>
        </el-form-item>
      </el-form>
    </div>
  </div>
</template>

<script>
export default {
  name: "forget",
  data() {
    return {
      active: 0,
      account: "",
      question: "",
      answer: "",
      uanswer: "",
      form: {},
      rules: {
        username: [
          {required: true, message: '请输入用户名', trigger: 'blur'},
          {min: 2, max: 10, message: '长度在 2 到 5 个字符', trigger: 'blur'}
        ],
        password: [
          {required: true, message: '请输入密码', trigger: 'blur'},
          {min: 1, max: 20, message: '长度在 1 到 20 个字符', trigger: 'blur'}
        ],
        newPassword: [
          { required: true, message: '请输入新密码', trigger: 'blur' },
          { min: 3, message: '长度不少于3位', trigger: 'blur' }
        ],
        confirmPassword: [
          { required: true, message: '请输入密码', trigger: 'blur' },
          { min: 3, message: '长度不少于3位', trigger: 'blur' }
        ],
      }
    }
  },
  mounted() {
    this.load()
  },
  methods: {
    load() {
      this.account = this.$route.query.account
      this.getUser()
      console.log(this.account)
    },
    getUser() {
      axios.post("/UserControl/getUser?uaccount=" + this.account).then(res => {
        this.question = res.data.data.upwdask
        this.answer = res.data.data.uwdans
        console.log(this.question)
      })
    },
    save() {
      this.$refs.pass.validate((valid) => {
        if (valid) {
          if (this.form.newPassword !== this.form.confirmPassword) {
            this.$message.error("2次输入的新密码不相同")
            return false
          }
          axios.post("/UserControl/UserUpdatepwd2?uaccount="+this.account+"&newpwd="+this.form.newPassword).then(res => {
            if (res.data.code === 200) {
              this.$message.success(res.data.msg)
              this.active=2
            } else {
              this.$message.error(res.data.msg)
            }
          })
        }
      })
    },
    next() {
      if (this.uanswer === "") {
        this.$message.warning("请输入密保问题")
      } else if (this.uanswer !== this.answer) {
        this.$message.error("回答错误，请重试")
      } else {
        this.$message.success("回答正确")
        this.active = 1
      }
    }
  }
}
</script>

<style scoped>

</style>