<template>
  <div>
    <div style="margin: 10px 0">
      <el-carousel type="card" height="300px" :interval="10000">
        <el-carousel-item v-for="item in imgs" :key="item">
          <img :src="item" alt="" style="width: 100%">
        </el-carousel-item>
      </el-carousel>
    </div>
    <el-input
        style="width: 280px"
        placeholder="请输入商品名称"
        suffix-icon="el-icon-search"
        v-model="name"
    ></el-input>
    <el-button class="ml-5" type="primary" @click="load"
    >搜索
    </el-button
    >
    <el-button type="warning" @click="reset">重置</el-button>
    <div style="margin: 10px 0">
      <el-row :gutter="10">
        <el-col :span="6" v-for="item in files" :key="item.sid" style="margin-bottom: 10px">
          <div class="MyDiv" @click="buy(item)">
            <!--图片-->
            <img class="MyImg" :src="item.simage" alt="图片暂时无法显示" style="width: 100%">
            <!--价格和销量-->
            <div style="padding: 10px">
              <span style="color: red;font-size: 20px">￥{{ item.sprices - item.sdiscount }}</span>
              <span style="color:gray;margin-left: 8px">￥<s>{{ item.sprices }}</s></span>
              <span style="color:gray;float: right"><i>{{ item.stimessold }}</i>+人付款</span>
            </div>
            <!--产品名称-->
            <div class="MyText" style="">{{ item.sname }}</div>
            <!--生产商,还没-->
            <el-link type="primary"><i class="el-icon-s-shop" style="margin-right: 4px"></i>{{ item.sproducer }}
            </el-link>
          </div>
        </el-col>
      </el-row>
      <div style="padding: 10px 0">
        <el-pagination
            @size-change="handleSizeChange"
            @current-change="handleCurrentChange"
            :current-page="this.pageNum"
            :page-sizes="[12, 24, 36, 48]"
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
  name: "home",
  data() {
    return {
      imgs: [
        'https://gdp.alicdn.com/imgextra/i1/2200674901717/O1CN01Ac09211OYQjQOEwl4_!!2200674901717.jpg',
        'https://gdp.alicdn.com/imgextra/i2/2200674901717/O1CN01xXFFT61OYQjR09zZ4_!!2200674901717.jpg',
        'https://gdp.alicdn.com/imgextra/i1/2200674901717/O1CN0104FXcx1OYQjYr8yxT_!!2200674901717.jpg '
      ],
      files: [],
      pageNum: 1,
      pageSize: 12,
      total: 0,
      name: ""
    }
  },
  created() {
    this.load()
  },
  methods: {
    load() {
      axios.get("/shoes/page", {
        params: {
          pageNum: this.pageNum,
          pageSize: this.pageSize,
          name: this.name
        }
      }).then(res => {
        this.files = res.data.data.records
        // console.log(this.files)
        this.total = res.data.data.total
      })
    },
    buy(file) {
      this.$router.push("/front/buy")
      localStorage.setItem("file", JSON.stringify(file))
      // console.log(file)
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
    }
  }
}
</script>

<style>
.MyDiv {
  overflow: hidden;
  border: 1px solid gray;
  padding-bottom: 10px
}

.MyDiv:hover {
  border: 1px solid red;
  cursor: pointer;
}

.MyText {
  color: gray;
  padding: 10px;
  line-height: 40px;
  display: -webkit-box;
  -webkit-line-clamp: 2;
  -webkit-box-orient: vertical;
  overflow: hidden;
}

.MyImg {

  transition: all 0.6s;
}

.MyImg:hover {
  transform: scale(1.1);
}
</style>
