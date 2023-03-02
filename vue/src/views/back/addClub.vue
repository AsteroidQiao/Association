<template>
  <div>
    <el-form ref="form" :model="form" :rules="rules" label-width="130px" label-position="left">
      <el-form-item label="请输入社团名称" prop="name">
        <el-input v-model="form.name"></el-input>
      </el-form-item>
      <el-form-item label="上传社团头像">
      </el-form-item>
      <el-upload
          class="avatar-uploader"
          :action="'http://' + serverIp +':9090/file/upload'"
          :show-file-list="false"
          :on-success="handleAvatarSuccess"
          :auto-upload="true"
          :before-upload="beforeAvatarUpload"
      >
        <img v-if="form.icon" :src="form.icon" class="avatar">
        <i v-else class="el-icon-plus avatar-uploader-icon"></i>
        <div slot="tip" class="el-upload__tip">上传jpg/png文件，大小不要超过10M</div>
      </el-upload>
      <el-form-item label="绑定学院" prop="college">
        <el-select v-model="form.college" filterable placeholder="请选择" style="margin-left: -940px">
          <el-option
              v-for="item in options"
              :key="item.cid"
              :value="item.cname">
          </el-option>
        </el-select>
      </el-form-item>
      <el-form-item label="社团简介（200字以内）">
        <el-input type="textarea" v-model="form.info"></el-input>
      </el-form-item>
      <el-form-item>
        <el-button type="primary" @click="onSubmit('form')">立即创建</el-button>
        <el-button>取消</el-button>
      </el-form-item>
    </el-form>
  </div>
</template>

<script>
import {serverIp} from "../../../public/config";

export default {
  name: "addClub",
  data() {
    return {
      rules: {
        name: [
          {required: true, message: '请输入社团名称', trigger: 'blur'},
          {min: 3, max: 25, message: '长度在 3 到 25 个字符', trigger: 'blur'}
        ],
        college: [
          {required: true, message: '请绑定学院', trigger: 'blur'},
        ],
      },
      serverIp: serverIp,
      value: '',
      options: [],
      form: {
        name: '',
        icon: '',
        info: '',
        uid: 0,
        uid2: 0,
        college: '',
        notice: '',
      },
    }
  },

  created() {
    axios.get('/college/').then((res => {
      this.options = res.data.data
    }))
  },
  methods: {
    onSubmit(formName) {
      this.$refs[formName].validate((valid) => {
        if (valid) {
          axios.post("/association/insert?name=" + this.form.name + '&info=' + this.form.info + '&icon=' + this.form.icon + '&college=' + this.form.college).then(res => {
            if (res.data.code === 200) {
              this.$message.success(res.data.msg)
              this.$router.push('/back/association')
            } else
              this.$message.error(res.data.msg)
          })
        } else {
          console.log('error submit!!');
          return false;
        }
      });

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
    },
  }
}

</script>

<style>
.avatar {
  width: 300px;
  height: 300px;
}

.avatar-uploader .el-upload {
  border: 1px dashed #409EFF;
  border-radius: 6px;
  cursor: pointer;
  position: relative;
  overflow: hidden;
}

.avatar-uploader .el-upload:hover {
  border-color: darkorange;
}

.avatar-uploader-icon {
  font-size: 28px;
  color: #8c939d;
  width: 178px;
  height: 178px;
  line-height: 178px;
  text-align: center;
}

.avatar {
  width: 178px;
  height: 178px;
  display: block;
}
</style>