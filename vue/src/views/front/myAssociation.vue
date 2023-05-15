<template>
  <div>
    <div style="margin: 10px 0"></div>
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
    <el-input
        style="width: 280px"
        placeholder="请输入社团名称"
        suffix-icon="el-icon-search"
        v-model="name"
        @keydown.native.enter="load"
    ></el-input>
    <el-button type="primary" @click="load">搜索</el-button>
    <el-button type="warning" @click="reset">重置</el-button>
    <h2>我加入的部落</h2>
    <p>部落列表</p>
    <div style="margin: 10px 0">
      <el-row :gutter="10">
        <el-col :span="8" v-for="item in files" :key="item.id" style="margin-bottom: 10px">
          <div @click="toClub(item)">
            <el-card shadow="hover">
              <!--社团图标-->
              <img class="MyImage" :src="item.icon" alt="图片暂时无法显示">
              <!--社团名称-->
              <div style="padding: 10px;color: red;font-size: 20px;margin-top: -100px; margin-left: 40%">
                {{ item.name }}
              </div>
              <!--部落简介-->
              <div class="MyText" style="margin-top: -20px; margin-left: 40%">{{ item.info }}</div>
            </el-card>
          </div>
        </el-col>
      </el-row>
    </div>
    <div style="padding: 10px 0">
      <el-pagination
          @size-change="handleSizeChange"
          @current-change="handleCurrentChange"
          :current-page="this.pageNum"
          :page-sizes="[3, 6, 9, 12, 24]"
          :page-size="pageSize"
          layout="total, sizes, prev, pager, next, jumper"
          :total="total">
      </el-pagination>
    </div>
  </div>
</template>
<script>
export default {
  name: "association",
  data() {
    return {
      files: {},
      pageSize: 6,
      total: 0,
      pageNum: 1,
      name: '',
      user: {},
    }
  },
  created() {
    this.user = localStorage.getItem('user') ? JSON.parse(localStorage.getItem('user')) : {}
    this.load()
  },
  methods: {
    load() {
      axios.get("/association/myAssociation", {
        params: {
          pageNum: this.pageNum,
          pageSize: this.pageSize,
          name: this.name,
          uid: this.user.uid
        }
      }).then(res => {
        this.files = res.data.data.records
        console.log(this.files)
        console.log(this.name)
        this.total = res.data.data.total
      })
    },
    reset() {
      this.name = "";
      this.load();
    },
    handleSizeChange(pageSize) {
      this.pageSize = pageSize
      this.load()
    },
    handleCurrentChange(pageNum) {
      this.pageNum = pageNum
      this.load()
    },
    toClub(item) {
      localStorage.setItem("club", JSON.stringify(item))
      this.$router.push('/front/club/' + item.id)
    }
  },

}
</script>

<style scoped>
.contain {
  width: 100%;
  height: 150px;
  overflow: hidden;
  border: 2px solid #8fb2c9;
  padding-bottom: 10px
}

.MyText {
  color: gray;
  padding: 10px;
  height: 60px;
  line-height: 40px;
  display: -webkit-box;
  -webkit-line-clamp: 2;
  -webkit-box-orient: vertical;
  overflow: hidden;
}

.MyImage {
  width: 100px;
  height: 100px;
  transition: all 0.6s;
}

.MyImage:hover {
  transform: scale(1.1);
}

.MyImg1 {
  width: 100%;
  height: 70px;
  transition: all 0.6s;
}

.MyImg2 {
  width: 100%;
  height: 140px;
  transition: all 0.6s;
}

.MyImg3 {
  width: 100%;
  height: 180px;
  transition: all 0.6s;
}

.MyImg1:hover {
  transform: scale(1.1);
}

.MyImg2:hover {
  transform: scale(1.1);
}

.MyImg3:hover {
  transform: scale(1.1);
}

.name {
  text-align: center;
}

.position {
  width: 400px;
  margin-top: -20px;
  margin-left: 130px;
  text-align: left;
}

.info {
  margin-left: 10px;
  text-align: left;
  color: gray;
  padding: 10px;
  line-height: 40px;
  display: -webkit-box;
  -webkit-line-clamp: 2;
  -webkit-box-orient: vertical;
  overflow: hidden;
}

</style>