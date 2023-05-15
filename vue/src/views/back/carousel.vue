<template>
  <div>
    <el-popover
        placement="center"
        width="500"
        v-model="view"
        trigger="click">
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
        <el-form-item label="跳转路由">
          <el-input v-model="form.route" autocomplete="off"></el-input>
        </el-form-item>
        <el-form-item label="描述信息">
          <el-input v-model="form.info" autocomplete="off"></el-input>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" autocomplete="off" @click="addCarousel">确定</el-button>
          <el-button autocomplete="off" @click="cancel">取消</el-button>
        </el-form-item>
      </el-form>
    </el-popover>
    <el-button style="margin-right: 90%;margin-bottom: 10px" type="primary" icon="el-icon-upload" @click="add">
      添加轮播图
    </el-button>
    <el-card >
      <el-table
          :data="carousel"
          style="width: 100%">
        <el-table-column
            label="缩略图"
            align="center"
            width="200">
          <template slot-scope="scope">
            <el-image style="width: 160px; height: 90px" :src="carousel[scope.$index].url" fit="contain"
                      :preview-src-list="images"/>
          </template>
        </el-table-column>
        <el-table-column
            prop="route"
            label="跳转路由"
            align="center"
            width="180">
        </el-table-column>
        <el-table-column
            prop="info"
            align="center"
            label="描述">
        </el-table-column>
        <el-table-column
            align="center"
            label="操作">
          <template slot-scope="scope">
            <el-popconfirm placement="top" width="180" @confirm="handleDelete(scope.$index, scope.row)"
                           title="确定删除这张轮播图吗？">
              <el-button slot="reference" type="danger" circle icon="el-icon-delete" style="margin-right: 0">
              </el-button>
            </el-popconfirm>
            <el-button type="primary" icon="el-icon-edit" circle @click="editCarousel( scope.row)"></el-button>
          </template>
        </el-table-column>
      </el-table>
    </el-card>
  </div>
</template>

<script>
import {serverIp} from '../../../public/config';

export default {
  name: "carousel",
  data() {
    return {
      form: {},
      carousel: [],
      images: [],
      serverIp: serverIp,
      view: false
    }
  },
  mounted() {
    this.load()
  },
  methods: {
    load() {
      axios.get("carousel/findAllAdmin").then(res => {
        //获取轮播图列表赋值给table
        if (res.data.code === 200) {
          this.carousel = res.data.data
          for (let image in this.carousel) {
            this.images.push(this.carousel[image].url)
          }
        } else
          this.$notify.error('操作失败')
      })
    },
    add() {
      this.form = {}
      this.view = true
    },
    addCarousel() {
      //添加轮播图
      if (this.form.url === undefined || this.form.url === '') {
        this.$notify.warning("请添加轮播图！")
      } else {
        axios.post('/carousel/save', this.form).then(res => {
          if (res.data.code === 200) {
            //添加成功，提示信息，form清空，重新加载table数据
            this.$notify.success("操作成功")
            this.view = false
            this.form = {}
            this.load()
          } else
            this.$notify.error("操作失败")
        })
      }

    },
    cancel() {
      this.view = false
    },
    editCarousel(row) {
      this.view = true
      console.log(row)
      axios.get('/carousel/' + row.id).then(res => {
        if (res.data.code === 200) {
          this.form = res.data.data
        }
      })
    },
    handleDelete(index, row) {
      //删除轮播图
      axios.delete('/carousel/' + row.id).then(res => {
        if (res.data.code === 200) {
          this.$notify.success("删除成功")
          this.load()
        } else
          this.$notify.error('操作失败')
      })
    },
    handleAvatarSuccess(resp) {
      this.form.url = resp
      console.log(resp)
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