<template>
  <div>
    <el-form ref="form" :model="form" label-width="100px" label-position="left">
      <el-row :gutter="60">
        <el-col :span="12">
          <el-form-item label="前台名称">
            <el-input v-model="form.title1"></el-input>
            <div>
              <span style="font:20px '幼圆'">选择预置颜色 或 自定义色值</span>
              <el-color-picker style="margin-left: 20px; margin-top: 20px" v-model="form.color1"></el-color-picker>
              <el-input v-model="form.color1" style="width: 90px" placeholder="RGB" @click=""></el-input>
              <div class="container">
                <div class="preview-box">
                  <div class="preview" style="background:#63bbd0;" @click="form.color1='#63bbd0'"></div>
                  <div class="name">霁青 (默认)</div>
                </div>
                <div class="preview-box">
                  <div class="preview" style="background:#fa7298;" @click="form.color1='#fa7298'"></div>
                  <div class="name">粉</div>
                </div>
                <div class="preview-box">
                  <div class="preview" style="background:#009688;" @click="form.color1='#009688'"></div>
                  <div class="name">水鸭青</div>
                </div>
                <div class="preview-box">
                  <div class="preview" style="background:#f1908c;" @click="form.color1='#f1908c'"></div>
                  <div class="name">珊瑚赫</div>
                </div>
                <div class="preview-box">
                  <div class="preview" style="background:#a6559d;" @click="form.color1='#a6559d'"></div>
                  <div class="name">紫蒲</div>
                </div>
                <div class="preview-box">
                  <div class="preview" style="background:#EE7959;" @click="form.color1='#EE7959'"></div>
                  <div class="name">缙云</div>
                </div>
                <div class="preview-box">
                  <div class="preview" style="background:#ff9700;" @click="form.color1='#ff9700'"></div>
                  <div class="name">鲜艳橙</div>
                </div>
                <div class="preview-box">
                  <div class="preview" style="background:#6E9BC5;" @click="form.color1='#6E9BC5'"></div>
                  <div class="name">挼蓝</div>
                </div>
                <div class="preview-box">
                  <div class="preview" style="background:#779649;" @click="form.color1='#779649'"></div>
                  <div class="name">碧山</div>
                </div>
                <div class="preview-box">
                  <div class="preview" style="background:#F0C2A2;" @click="form.color1='#F0C2A2'"></div>
                  <div class="name">扶光</div>
                </div>
              </div>
            </div>
          </el-form-item>
          <el-form-item label="效果预览" style="text-align: left">
            <div :style="{color:form.color1}" style="font:50px 'TechnicBold';">{{ form.title1 }}</div>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="平台图标"></el-form-item>
          <el-upload
              class="avatar-uploader" :action="'http://' + serverIp +':9090/gitee/upload'" :show-file-list="false"
              :on-success="handleAvatarSuccess" :auto-upload="true" :before-upload="beforeAvatarUpload">
            <img v-if="form.icon" :src="form.icon" class="avatar">
            <i v-else class="el-icon-plus avatar-uploader-icon"></i>
            <div slot="tip" class="el-upload__tip">上传jpg/png文件，大小不要超过10M</div>
          </el-upload>
          <el-form-item label="首页布局" style="margin-top: 110px; text-align: left">
            <el-radio-group v-model="form.span" style="display: flex" >
              <!--<el-image class="image" src="" @click="form.span=8"></el-image>-->
              <el-radio :label="8" class="radio">三排</el-radio>
              <!--<el-image class="image" src="" @click="form.span=6"></el-image>-->
              <el-radio :label="6" class="radio">四排</el-radio>
              <!--<el-image class="image" src="" @click="form.span=4"></el-image>-->
              <el-radio :label="4" class="radio">六排</el-radio>
            </el-radio-group>
          </el-form-item>
        </el-col>
      </el-row>
      <el-form-item>
        <el-button type="primary" @click="onSubmit('form')">点击保存</el-button>
      </el-form-item>

    </el-form>
  </div>
</template>

<script>
import {serverIp} from "../../../public/config";

export default {
  name: "page",
  data() {
    return {
      color: "red",
      serverIp: serverIp,
      options: [],
      form: {},
    }
  },

  created() {
    axios.post("/setting/select").then(res => {
      if (res.data.code === 200) {
        this.form = res.data.data;
      }
    })
  },
  methods: {
    onSubmit() {
      axios.post("/setting/save", this.form).then(res => {
        if (res.data.code === 200) {
          this.$notify.success(res.data.msg)
          localStorage.setItem('setting', JSON.stringify(this.form))
        }
      })
    },

    handleAvatarSuccess(resp) {
      this.form.icon = resp.data
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

.container {
  max-width: calc(100%);
}

.preview-box {
  width: max-content;
  width: -moz-max-content;
  display: inline-block;
}

.preview {
  width: 50px;
  height: 50px;
  margin: 20px 25px 5px 25px;
  line-height: 50px;
  color: #fff;
  font-size: 15px;
  text-align: center;
  display: inline-block;
  border-radius: 50px;
  transition: all 0.6s;
}

.preview:hover {
  transform: scale(1.2);
}

.name {
  width: 100px;
  color: #619ac3;
  text-align: center;
}
.image{
  width: 100px;
  height: 100px;
  margin-left: 30px;
}
.radio{
  text-align: center;
  margin-top: 120px;
  margin-left: -80px;

}

</style>