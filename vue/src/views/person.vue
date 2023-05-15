<template>
  <el-card style="width: 500px;">
    <el-form label-width="80px" size="small">
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
      <el-form-item label="学分">
        <el-input v-model="form.credit" disabled autocomplete="off"></el-input>
      </el-form-item>
      <el-form-item label="昵称">
        <el-input v-model="form.uname" autocomplete="off"></el-input>
      </el-form-item>
      <el-form-item label="性别">
        <el-select  v-model="form.gender"  filterable placeholder="请选择">
          <el-option value="男"></el-option>
          <el-option value="女"></el-option>
        </el-select>
      </el-form-item>
      <el-form-item label="学号">
        <el-input v-model="form.num" autocomplete="off"></el-input>
      </el-form-item>
      <el-form-item label="学院">
        <el-select v-model="form.college" filterable placeholder="请选择">
          <el-option
              v-for="item in college"
              :key="item.cname"
              :label="item.cname"
              :value="item.cname"
          >
          </el-option>
        </el-select>
      </el-form-item>
      <el-form-item label="专业">
        <el-select v-model="form.major" filterable placeholder="请选择" @click.native="searchMajor">
          <el-option
              v-for="item in major"
              :key="item.major"
              :label="item.major"
              :value="item.major"
          >
          </el-option>
        </el-select>
      </el-form-item>
      <el-form-item label="邮箱">
        <el-input v-model="form.email" autocomplete="off"></el-input>
      </el-form-item>
      <el-form-item label="电话">
        <el-input v-model="form.phone" autocomplete="off"></el-input>
      </el-form-item>
      <el-form-item label="个签">
        <el-input type="textarea" v-model="form.sign" autocomplete="off"></el-input>
      </el-form-item>
      <el-form-item label="密保问题">
      <el-popover
          placement="right"
          width="400"
          trigger="click">
        <el-card shadow="hover">
          <el-form label-width="80px" size="small">
            <el-form-item label="密保问题">
              <el-input v-model="form.issue" autocomplete="off"></el-input>
            </el-form-item>
            <el-form-item label="密保答案">
              <el-input v-model="form.answer" autocomplete="off"></el-input>
            </el-form-item>
          </el-form>
        </el-card>
        <el-button slot="reference" type="success">点击设置密保问题</el-button>
      </el-popover>
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
      college: {},
      major: {}
    }
  },
  created() {
    this.user = localStorage.getItem("user") ? JSON.parse(localStorage.getItem("user")) : {};
    this.load()
  },
  methods: {
    load() {
      //获取用户信息
      console.log(this.user.uaccount)
      axios.get("/UserController/" + this.user.uid).then(res => {
        this.form = res.data.data;
      })
      //所有角色只能选中未被假删除的学院
      axios.get('/college/findAll').then((res => {
        this.college = res.data.data
      }))
    },
    searchMajor() {
      //查找学院下的所有专业
      if (this.form.college === undefined || this.form.college === '' || this.form.college === null) {
        this.$notify.warning("请先选择学院")
      } else {
        axios.get('/major/findAllByCollege?college=' + this.form.college).then((res => {
          this.major = res.data.data
        }))
      }
    },
    save() {
      axios.post("/UserController/UserUpdate", this.form).then(res => {
        if (res.data.code === 200) {
          this.$message.success(res.data.msg)
          // 更新浏览器存储的用户信息
          axios.get('/UserController/' + this.form.uid).then(res => {
            if (res.data.code === 200) {
              res.data.data.token = this.user.token
              localStorage.setItem("user", JSON.stringify(res.data.data))
            }
          })
        } else {
          this.$message.error("保存失败")
        }
      })
    },
    handleAvatarSuccess(resp) {
      this.form.url = resp
    },
    beforeAvatarUpload(file) {
      const isJPGPNG = file.type === 'image/jpeg' || file.type === 'image/png';
      const isLt10M = file.size / 1024 / 1024 < 10;
      if (!isJPGPNG) {
        this.$message.error('上传头像图片只能是 JPG & PNG 格式!');
      }
      if (!isLt10M) {
        this.$message.error('上传头像图片大小不能超过 10MB!');
      }
      return isJPGPNG && isLt10M;
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
