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
    <el-card>
      <el-page-header @back="$router.go(-1)" content="">
      </el-page-header>
      <h2>社团详情</h2>
      <div class="contains">
        <h2>{{ this.form.name }}</h2>
        <p>#ID:{{ this.form.id }}# &nbsp;#校级#&nbsp; #河南师范大学#</p>
        <p>#社团与协会#&nbsp;#{{ this.form.college }}#</p>
        <p>社团图标：
          <img :src="this.form.icon" class="icon"/>
        </p>
        <el-popover
            placement="right"
            title="标题"
            width="200"
            v-model="visible"
            trigger="click">
          <el-result icon="success" title="报名成功" subTitle="请等待审核">
            <template slot="extra">
              <el-button type="primary" size="medium" @click="visible = false">返回</el-button>
            </template>
          </el-result>
          <el-button slot="reference" round style="margin-left: 0" type="primary" @click="join(form.id)">加入社团
          </el-button>
        </el-popover>
        <el-button type="success" round v-if="isOrgan" @click="manage()">人员录取</el-button>
        <el-divider></el-divider>
        <h2>社团成员</h2>
        <p>当前社团共{{ this.count }}位成员</p>
        <div style="margin: 10px 0">
          <el-row :gutter="10">
            <el-col :span="6" v-for="item in users" :key="item.uid" style="margin-bottom: 10px">
              <div @click="">
                <el-card>
                  <!--用户头像-->
                  <img class="MyImage" :src="item.url" alt="图片暂时无法显示">
                  <!--用户名-->
                  <div style="padding: 10px">
                    <span style="color: red;font-size: 20px">{{ item.uaccount }}</span>
                  </div>
                </el-card>
              </div>
            </el-col>
          </el-row>
        </div>
        <el-divider></el-divider>
        <h2>社团公告</h2>
        <div v-if="form.notice">
          <el-card shadow="hover" style="width: 400px;height: 300px">{{ form.notice }}</el-card>
          <p>收到1条社团公告</p>
        </div>
        <div v-else>
          <el-card shadow="hover" style="width: 400px;height: 300px">暂无公告</el-card>
          <p>收到0条社团公告</p>
        </div>
        <el-divider></el-divider>
        <h2>社团简介</h2>
        <p>{{ this.form.info }}</p>
        <el-divider></el-divider>
        <h2>社团活动</h2>
        <div style="margin: 10px 0">
          <el-row :gutter="10">
            <el-col :span="6" v-for="item in files" :key="item.id" style="margin-bottom: 10px">
              <el-card>
                <div @click="attend(item)">
                  <!--活动图片-->
                  <img class="MyImg" :src="item.url" alt="图片暂时无法显示">
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
      </div>
    </el-card>
  </div>
</template>

<script>
export default {
  name: "club",
  data() {
    return {
      id: 0,
      visible: false,
      form: {},
      users: {},
      user: {},
      isOrgan: false,
      files: {},
      count: 0,
    }
  },
  mounted() {
    let url = window.location.toString()
    let arr = url.split('/')
    this.id = arr.pop()
    this.user = localStorage.getItem("user") ? JSON.parse(localStorage.getItem("user")) : {};
    this.load()
  },
  methods: {
    load() {
      //获取社团信息
      axios.get('/association/id?id=' + this.id).then(res => {
        console.log(res.data.data)
        console.log("res.data.data")
        if (res.data.code === 200) {
          this.form = res.data.data
          //获取社团成员
          axios.get('/relation/findAll?name=' + this.form.name).then(res => {
            if (res.data.code === 200) {
              this.users = res.data.data
              for (const re of this.users) {
                this.count++
              }
            }
          })
          //  获取社团活动
          axios.get("/activity/findByClub?club=" + this.form.name).then(res => {
            console.log('jfdsa')
            console.log(this.form.name)
            console.log(res.data.code)
            if (res.data.code === 200) {
              this.files = res.data.data
              console.log(res)
              for (const data of this.files) {
                data.starttime = this.formatDate(data.starttime, 'yyyy.MM.dd');
                data.endtime = this.formatDate(data.endtime, 'yyyy.MM.dd');
              }
            }
          })
        }
      })
      //是否是社团负责人
      axios.get('/relation/isOrgan?aid=' + this.id + '&uid=' + this.user.uid).then(res => {
        if (res.data.code === 200) {
          this.isOrgan = res.data.data
        }
      })
    },
    join(id) {
      let relation = {}
      relation.uid = this.user.uid
      relation.aid = id
      axios.post('relation/save', relation).then(res => {
        if (res.data.code === 200) {
          this.$notify.success(res.data.msg)
        } else this.$notify.warning(res.data.msg)
      })
    },
    manage() {
      this.$router.push('/front/manage/' + this.id)
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
  },

}
</script>

<style scoped>
.contains {

}

.contains h2, p {
  text-align: left;
}

.icon {
  margin-right: 7px;
  transition: all 0.6s;
  width: 80px;
  border-radius: 50%;
  position: relative;
  top: 10px;
  right: 5px;
}

.MyImage {
  width: 100px;
  height: 100px;
  border-radius: 50px;
  transition: all 0.6s;
}

.MyImage:hover {
  transform: scale(1.1);
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
</style>