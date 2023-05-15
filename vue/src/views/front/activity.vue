<template>
  <div>
    <div style="margin: 10px 0"></div>
    <el-backtop></el-backtop>
    <el-backtop :bottom="100" @click="">
      <div
          style="{
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
        placeholder="请输入活动名称"
        suffix-icon="el-icon-search"
        @keyup.native.enter="load"
        v-model="name"
    ></el-input>
    <el-button class="ml-5" type="primary" @click="load">搜索</el-button>
    <el-button type="warning" @click="reset">重置</el-button>
    <h2>全部校内活动</h2>
    <p>活动列表</p>
    <div style="margin: 10px 0">
      <el-row :gutter="10">
        <el-col :span="setting.span" v-for="item in files" :key="item.id" style="margin-bottom: 10px">
          <el-card shadow="hover">
            <div @click="attend(item)">
              <!--特色图片-->
              <img v-if="setting.span===4" class="MyImg1" :src="item.url" alt="图片暂时无法显示">
              <img v-else-if="setting.span===6" class="MyImg2" :src="item.url" alt="图片暂时无法显示">
              <img v-else-if="setting.span===8" class="MyImg3" :src="item.url" alt="图片暂时无法显示">
              <!--活动时间-->
              <div style="padding: 10px">
                <span style="color: red;font-size: 15px">{{ item.starttime }} 至 {{ item.endtime }}</span>
              </div>
              <!--活动名称-->
              <div class="MyText" style="">{{ item.name }}</div>
            </div>
            <!--举办社团-->
            <el-link type="primary" @click="toClub(item)">
              <i class="el-icon-s-promotion" style="margin-right: 4px"></i>{{ item.association }}
            </el-link>
          </el-card>
        </el-col>
      </el-row>
    </div>
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
</template>

<script>
export default {
  name: "activity",
  data() {
    return {
      files: {},
      club: {},
      activity: [],
      pageSize: 8,
      total: 0,
      pageNum: 1,
      name: '',
      setting: localStorage.getItem("setting") ? JSON.parse(localStorage.getItem("setting")) : {},
      carousel: [],
      images: [],
    }
  },
  created() {
    this.load()
  },
  methods: {
    load() {
      //获取热门活动
      axios.get("/activity/page", {
        params: {
          pageNum: this.pageNum,
          pageSize: this.pageSize,
          name: this.name,
          pass: 1
        }
      }).then(res => {
        if (res.data.code === 200) {
          this.files = res.data.data.records
          for (const data of this.files) {
            data.starttime = this.formatDate(data.starttime, 'yyyy.MM.dd');
            data.endtime = this.formatDate(data.endtime, 'yyyy.MM.dd');
          }
          this.total = res.data.data.total
        }
      })
    },
    toClub(item) {
      axios.get('/association/name?name=' + item.association).then(res => {
        if (res.data.code === 200) {
          let club = res.data.data
          localStorage.setItem("club", JSON.stringify(club))
          this.$router.push('/front/club/' + club.id)
        }
      })
    },
    formatDate(inputDate, fmt) {
      let date = new Date(inputDate)
      if (/(y+)/.test(fmt)) {
        fmt = fmt.replace(RegExp.$1, (date.getFullYear() + '').substr(4 - RegExp.$1.length))
      }
      let o = {
        'M+': date.getMonth() + 1,
        'd+': date.getDate(),
        'h+': date.getHours(),
        'm+': date.getMinutes(),
        's+': date.getSeconds()
      }
      for (let k in o) {
        if (new RegExp(`(${k})`).test(fmt)) {
          let str = o[k] + ''
          fmt = fmt.replace(RegExp.$1, (RegExp.$1.length === 1) ? str : this.padLeftZero(str))
        }
      }
      return fmt
    },
    padLeftZero(str) {
      return ('00' + str).substr(str.length)
    },
    attend(item) {
      this.$router.push('/front/activityDetail/' + item.acid)
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
  height: 60px;
  line-height: 40px;
  display: -webkit-box;
  -webkit-line-clamp: 2;
  -webkit-box-orient: vertical;
  overflow: hidden;
}

.MyImg {
  width: 100%;
  height: 140px;
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
.MyImage {
  width: 100px;
  height: 100px;
  transition: all 0.6s;
}

.MyImage:hover {
  transform: scale(1.1);
}
</style>
