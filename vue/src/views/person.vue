<template>
  <el-card style="width: 500px;">
    <el-form  label-width="80px" size="small">
      <el-upload
          class="avatar-uploader"
          :action="'http://' + serverIp +':9090/file/upload'"
          :show-file-list="false"
          :on-success="handleAvatarSuccess"
          :before-upload="beforeAvatarUpload"
      >
        <img v-if="form.url" :src="form.url" class="avatar">
        <i v-else class="el-icon-plus avatar-uploader-icon"></i>
      </el-upload>

      <el-form-item label="用户名">
        <el-input v-model="form.uaccount" disabled autocomplete="off"></el-input>
      </el-form-item>
      <el-form-item label="昵称">
        <el-input v-model="form.uname" autocomplete="off"></el-input>
      </el-form-item>
      <el-form-item label="性别">
        <el-input v-model="form.gender" autocomplete="off"></el-input>
      </el-form-item>
      <el-form-item label="学号">
        <el-input v-model="form.num" autocomplete="off"></el-input>
      </el-form-item>
      <el-form-item label="学院">
        <el-input v-model="form.college" autocomplete="off"></el-input>
      </el-form-item>
      <el-form-item label="专业">
        <el-input v-model="form.major" autocomplete="off"></el-input>
      </el-form-item>
      <el-form-item label="邮箱">
        <el-input v-model="form.email" autocomplete="off"></el-input>
      </el-form-item>
      <el-form-item label="电话">
        <el-input v-model="form.phone" autocomplete="off"></el-input>
      </el-form-item>
      <el-form-item label="个签">
        <el-input type="textarea" v-model="form.singn" autocomplete="off"></el-input>
      </el-form-item>

      <el-form-item>
        <el-button type="primary" @click="save">确 定</el-button>
      </el-form-item>
    </el-form>
  </el-card>
</template>

<script>
import {serverIp} from "../../public/config";

export default {
  name: "Person",
  data() {
    return {
      serverIp: serverIp,
      form: {},
      user: "",
    }
  },
  created() {
    this.user = localStorage.getItem("user")
        ? JSON.parse(localStorage.getItem("user"))
        : {};
    this.getUser()
  },
  methods: {
    getUser() {
      console.log(this.user.uaccount)
      axios.get("/UserController/" +this.user.uid).then(res => {
        this.form = res.data.data;
      })
    },
    save() {
      console.log(this.form)
      axios.post("/UserController/UserUpdate?user=", this.form).then(res => {
        if (res.data.code === 200) {
          this.$message.success(res.data.msg)
          // 触发父级更新User的方法
          this.$emit("load")
          // 更新浏览器存储的用户信息
          console.log(res)
          localStorage.setItem("user", JSON.stringify(res.data.data))
        } else {
          this.$message.error("保存失败")
        }
      })
    },
    handleAvatarSuccess(resp) {
      this.form.avatarUrl = resp
    },
    beforeAvatarUpload(file) {
      const isJPG = file.type === 'image/jpeg';
      const isLt2M = file.size / 1024 / 1024 < 2;
      if (!isJPG) {
        this.$message.error('上传头像图片只能是JPG格式!');
      }
      if (!isLt2M) {
        this.$message.error('上传头像图片大小不能超过 2MB!');
      }
      return isJPG && isLt2M;
    }
  }
}
</script>

<style>
.avatar-uploader {
  text-align: center;
  padding-bottom: 10px;
}

.avatar-uploader .el-upload {
  border: 1px dashed #d9d9d9;
  border-radius: 6px;
  cursor: pointer;
  position: relative;
  overflow: hidden;
}

.avatar-uploader .el-upload:hover {
  border-color: #409EFF;
}

.avatar-uploader-icon {
  font-size: 28px;
  color: #8c939d;
  width: 138px;
  height: 138px;
  line-height: 138px;
  text-align: center;
}

.avatar {
  width: 138px;
  height: 138px;
  display: block;
}
</style>
