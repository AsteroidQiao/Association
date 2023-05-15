<template>
  <div>

    <el-backtop></el-backtop>
    <el-backtop :bottom="100" @click="">
      <div style="{
        height: 100%;
        width: 100%;
        background-color: #f2f5f6;
        box-shadow: 0 0 6px rgba(0,0,0, .12);
        text-align: center;
        line-height: 40px;
        color: #1989fa;
      }"
      >
        UP
      </div>
    </el-backtop>
    <el-page-header @back="$router.go(-1)" content=""></el-page-header>

    <h2>社团详情</h2>
    <el-form ref="form" :model="form" :rules="rules" label-width="130px" label-position="left">
      <el-form-item label="社团名称" prop="name">
        <el-input v-model="form.name"></el-input>
      </el-form-item>
      <el-form-item label="社团头像"></el-form-item>
      <el-upload
          class="avatar-uploader" :action="'http://' + serverIp +':9090/file/upload'" :show-file-list="false"
          :on-success="handleAvatarSuccess" :auto-upload="true" :before-upload="beforeAvatarUpload">
        <img v-if="form.icon" :src="form.icon" class="avatar">
        <i v-else class="el-icon-plus avatar-uploader-icon"></i>
        <div slot="tip" class="el-upload__tip">上传jpg/png文件，大小不要超过10M</div>
      </el-upload>
      <el-form-item label="绑定学院" prop="college">
        <el-select v-model="form.college" filterable placeholder="请选择" style="margin-left: -790px">
          <el-option v-for="item in options" :key="item.cid" :value="item.cname"></el-option>
        </el-select>
      </el-form-item>
      <el-form-item label="社团简介（200字以内）">
        <el-input type="textarea" v-model="form.info" placeholder="添加社团简介"></el-input>
      </el-form-item>
      <el-form-item label="社团公告">
        <el-input type="textarea" v-model="form.notice" placeholder="添加社团公告"></el-input>
      </el-form-item>
      <el-form-item>
        <el-button type="success"  @click="manage()">查看社员</el-button>
        <el-button type="primary" @click="onSubmit('form')">保存配置</el-button>
        <!--<el-button @click="$router.push('/back/association')">返回</el-button>-->
      </el-form-item>
    </el-form>
  </div>
</template>

<script>
import {serverIp} from "../../../public/config";

export default {
  name: "club",
  data() {
    return {
      serverIp: serverIp,
      club: {},
      form: {},
      options: [],
      rules: {
        name: [
          {required: true, message: '请输入社团名称', trigger: 'blur'},
          {min: 3, max: 25, message: '长度在 3 到 25 个字符', trigger: 'blur'}
        ],
        college: [
          {required: true, message: '请绑定学院', trigger: 'blur'},
        ],
      },
    }
  },
  mounted() {
    let url = window.location.toString()
    let arr = url.split('/')
    this.id = arr.pop()
    this.load()
  },
  methods: {
    load() {
      axios.get('/college/findAll').then((res => {
        this.options = res.data.data
      }))
      //获取社团详情
      axios.get('/association/id?id=' + this.id).then(res => {
        if (res.data.code === 200) {
          this.form = res.data.data
        }
      })
    },
    onSubmit(formName) {
      this.$refs[formName].validate((valid) => {
        if (valid) {
          axios.post("/association/save", this.form).then(res => {
            if (res.data.code === 200) {
              this.$notify.success(res.data.msg)
              // this.$router.push('/back/association')
            } else
              this.$notify.error(res.data.msg)
          })
        } else {
          console.log('error submit!!');
          return false;
        }
      });
    },
    manage() {
      console.log(this.form)
      this.$router.push('/back/manage/' + this.form.id)
    },
    handleAvatarSuccess(resp) {
      this.form.icon = resp
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
  },

}
</script>

<style>

.icon {
  margin-right: 7px;
  transition: all 0.6s;
  width: 80px;
  border-radius: 50%;
  position: relative;
  top: 10px;
  right: 5px;
}

avatar-uploader {
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