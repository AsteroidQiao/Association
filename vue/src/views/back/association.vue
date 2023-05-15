<template>
  <div>
    <el-input
        style="width: 280px"
        placeholder="请输入社团名称"
        suffix-icon="el-icon-search"
        v-model="name"
        @keydown.native.enter="load"
    ></el-input>
    <el-button class="ml-5" type="primary" @click="load">搜索</el-button>
    <el-button type="warning" @click="reset">重置</el-button>

    <div style="margin: 10px 0">
      <el-row :gutter="10">
        <el-col :span="12" v-for="item in files" :key="item.name" style="margin-bottom: 10px">
          <div class="contain" @click="toClub(item)">
            <img class="MyImg" :src="item.icon" alt="图片暂时无法显示">
            <h2 class="name">{{ item.name }}</h2>
            <div class="position">
              <span class="info">{{ item.info }}</span>
            </div>
          </div>
        </el-col>
      </el-row>
      <div style="padding: 10px 0">
        <el-pagination
            @size-change="handleSizeChange"
            @current-change="handleCurrentChange"
            :current-page="this.pageNum"
            :page-sizes="[4, 8, 12, 24]"
            :page-size="pageSize"
            layout="total, sizes, prev, pager, next, jumper"
            :total="total">
        </el-pagination>
      </div>
    </div>
  </div>
</template>

<script>
export default {
  name: "association",
  data() {
    return {
      files: {},
      pageSize: 4,
      total: 0,
      pageNum: 1,
      name: '',
    }
  },
  created() {
    this.load()
  },
  methods: {
    load() {
      axios.get("/association/page", {
        params: {
          pageNum: this.pageNum,
          pageSize: this.pageSize,
          name: this.name
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
    toClub(club) {
      localStorage.setItem("club", JSON.stringify(club))
      console.log(club)
      this.$router.push('/back/club/'+club.id)
    }
  },

}
</script>

<style scoped>
.contain {
  width: 630px;
  height: 150px;
  overflow: hidden;
  border: 2px solid #8fb2c9;
  padding-bottom: 10px
}

.MyImg {
  width: 130px;
  height: 130px;
  margin-top: 10px;
  margin-left: -304px;
  position: absolute;
  flex: 1;
  transition: all 0.6s;
}

.MyImg:hover {
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