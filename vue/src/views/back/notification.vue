<template>
  <el-form ref="form" :model="form" :rules="rules" label-width="130px" label-position="left">
    <el-form-item label="请选择通知对象" prop="uid">
      <el-select v-model="form.uid" filterable placeholder="请选择" style="margin-left: -800px">
        <el-option v-for="item in users" :key="item.uid" :label="item.uaccount" :value="item.uid"></el-option>
      </el-select>
    </el-form-item>
    <el-form-item label="请输入通知标题" prop="title">
      <el-input v-model="form.title"></el-input>
    </el-form-item>
    <el-form-item label="通知内容（200字以内)" prop="msg">
      <el-input type="textarea" :rows="4" v-model="form.msg"></el-input>
    </el-form-item>
    <el-form-item label="通知类型" prop="type">
      <el-select v-model="form.type" filterable placeholder="请选择" style="margin-left: -800px">
        <el-option v-for="item in options" :key="item.label" :value="item.value"></el-option>
      </el-select>
    </el-form-item>
    <el-form-item label="路由跳转(选填)">
      <el-input v-model="form.route"></el-input>
    </el-form-item>
    <el-form-item>
      <el-button type="primary" @click="onSubmit('form')">立即创建</el-button>
      <el-button @click="form={}">取消</el-button>
    </el-form-item>
  </el-form>
</template>

<script>
export default {
  name: "notification",
  data() {
    return {
      form: {},
      users: [],
      options: [
        {
          value: '部落',
          label: '部落'
        },
        {
          value: '活动',
          label: '活动'
        },
        {
          value: '系统',
          label: '系统'
        }
      ],
      rules: {
        uid: [
          {required: true, message: '请选择通知对象', trigger: 'blur'},
        ],
        type: [
          {required: true, message: '请选择通知类型', trigger: 'blur'},
        ],
        title: [
          {required: true, message: '请输入通知标题', trigger: 'blur'},
          {min: 3, max: 25, message: '长度在 3 到 25 个字符', trigger: 'blur'}
        ],
        msg: [
          {required: true, message: '请输入通知内容', trigger: 'blur'},
          {min: 3, max: 255, message: '长度在 3 到 255 个字符', trigger: 'blur'}
        ],
      },
    }
  },
  mounted() {
    axios.get('/UserController/findAll').then(res => {
      if (res.data.code === 200) {
        this.users = res.data.data
      }
    })
  },
  methods: {
    onSubmit(formName) {
      this.$refs[formName].validate((valid) => {
        if (valid) {
          axios.post("/notification/save" , this.form).then(res => {
            if (res.data.code === 200) {
              this.$notify.success("已成功发布通知")
            } else
              this.$notify.error(res.data.msg)
          })
        } else {
          this.$notify.error('error submit!!');
          return false;
        }
      });
    },
  }
}
</script>

<style scoped>

</style>