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
    <el-page-header @back="$router.go(-1)" content="">
    </el-page-header>
    <h2>活动详情</h2>
    <div style="margin: 10px 0">
      <el-carousel type="card" height="300px" :interval="4000">
        <el-carousel-item v-for="item in images" :key="item.pid">
          <img :src="item.url" alt="图片显示错误" style="width: 100%"/>
        </el-carousel-item>
      </el-carousel>
    </div>
    <el-tabs v-model="activeName" :stretch="true" @tab-click="handleClick">
      <el-tab-pane label="活动详情" name="first">
        <el-card style="height: 180px">
          <!--社团图标-->
          <img class="MyImage" :src="club.icon" alt="图片暂时无法显示">
          <!--社团名称-->
          <div style="padding:10px;color: red; margin-top: -100px; margin-left:180px;font-size: 20px">{{
              club.name
            }}
          </div>
          <!--社团简介-->
          <div style="margin-top: 10px; margin-left:180px;">校级、社团与协会、{{ club.college }}</div>
        </el-card>
        <el-divider></el-divider>
        <h2>活动名称</h2>
        <div>
          {{ detail.name }}
        </div>
        <el-divider></el-divider>
        <h2>活动简介</h2>
        <div>
          {{ detail.info }}
        </div>
        <el-divider></el-divider>
        <h2>参与须知</h2>
        活动相关信息及参与需求<br><br>
        {{ detail.notice }}
        <el-divider></el-divider>
        <!--<h2>综合评分 </h2>-->
        <!--该活动综合评分<br>-->
        <h2>花絮故事</h2>
        分享你的活动精彩记录<br>
        <el-divider></el-divider>
      </el-tab-pane>
      <el-tab-pane label="活动信息" name="second">
        <h2>时间范围</h2>
        <p>报名时间<span style="margin-left:300px ">{{ detail.enrolltime }}——{{ detail.enrollendtime }}</span></p>
        <p>活动时间<span style="margin-left:300px ">{{ detail.starttime }}——{{ detail.endtime }}</span></p>
        <el-divider></el-divider>
        <h2>活动奖项</h2>
        <el-row :gutter="10">
          <el-col :span="8" v-for="item in award" :key="item.id" style="margin-bottom: 10px">
            <el-card>
              <!--奖项名称-->
              <div>奖项名称:&nbsp;&nbsp;{{ item.name }}
              </div>
              <!--奖项数量-->
              <div>奖项名额:&nbsp;&nbsp;{{ item.count }}</div>
            </el-card>
          </el-col>
        </el-row>
        <el-divider></el-divider>
        <h2>活动学分</h2>
        <el-row :gutter="10">
          <el-col :span="8" v-for="item in credit" :key="item.id" style="margin-bottom: 10px">
            <el-card>
              <!--奖项名称-->
              <div>学时:&nbsp;&nbsp;{{ item.num }}
              </div>
              <!--奖项数量-->
              <div>名额:&nbsp;&nbsp;{{ item.count }}</div>
            </el-card>
          </el-col>
        </el-row>
        <el-divider></el-divider>
      </el-tab-pane>
      <el-tab-pane label="活动人员" name="third">
        <h2>负责人</h2>
        <el-col :span="24" v-for="item in organizer" :key="item.id" style="margin-bottom: 10px">
          <el-card style="height: 100px">
            <!--用户头像-->
            <img class="MyImage2" :src="item.url" alt="图片暂时无法显示">
            <!--用户名-->
            <div style="padding:10px;font-weight: bold; margin-top: -70px; margin-left:90px;font-size: 15px">{{
                item.uaccount
              }}
            </div>
            <!--所在学院-->
            <div style="margin-top: 3px; margin-left:90px;">河南师范大学 · {{ item.college }}</div>
          </el-card>
          <el-divider></el-divider>
        </el-col>
        <h2>报名人员</h2>
        <el-col :span="24" v-for="item in enrolls" :key="item.uid" v-if="item.en===1" style="margin-bottom: 10px">
          <el-card style="height: 100px">
            <!--用户头像-->
            <img class="MyImage2" :src="item.url" alt="图片暂时无法显示">
            <!--用户名-->
            <div style="padding:10px;font-weight: bold; margin-top: -70px; margin-left:90px;font-size: 15px">{{
                item.uaccount
              }}
            </div>
            <!--所在学院-->
            <div style="margin-top: 3px; margin-left:90px;">河南师范大学 · {{ item.college }}</div>
            <!--报名状态-->
            <div class="en2">
              <span style="color: white">已录取</span></div>
          </el-card>
          <el-divider></el-divider>
        </el-col>
        <el-col :span="24" v-for="item in enrolls" :key="item.uid" v-if="item.en===0" style="margin-bottom: 10px">
          <el-card style="height: 100px">
            <!--用户头像-->
            <img class="MyImage2" :src="item.url" alt="图片暂时无法显示">
            <!--用户名-->
            <div style="padding:10px;font-weight: bold; margin-top: -70px; margin-left:90px;font-size: 15px">{{
                item.uaccount
              }}
            </div>
            <!--所在学院-->
            <div style="margin-top: 3px; margin-left:90px;">河南师范大学 · {{ item.college }}</div>
            <!--报名状态-->
            <div class="en">
              <span style="color: white">已报名</span></div>
          </el-card>
          <el-divider></el-divider>
        </el-col>
      </el-tab-pane>
    </el-tabs>
    <div>
      <el-row>
        <el-col :span="12">
          <el-statistic v-if="deadline!==0" :value="deadline" time-indices title="倒计时">
            <template slot="suffix"><span style="color: #63bbd0">报名即将开始</span></template>
          </el-statistic>
          <el-statistic v-if="deadline2!==0" :value="deadline2" time-indices title="倒计时">
            <template slot="suffix"><span style="color: orange">报名进行中</span></template>
          </el-statistic>
          <div v-if="deadline===0&&deadline2===0">
            <span style="color: red">报名已结束</span>
          </div>
        </el-col>
        <el-divider direction="vertical"></el-divider>
        <el-col :span="12">
          <el-button type="primary" round v-if="deadline2!==0" @click="join()" style="margin-left: 60px">报名活动
          </el-button>
          <el-button type="primary" round v-if="deadline!==0" style="margin-left: 60px" disabled>报名活动</el-button>
          <el-button type="success" round v-if="isOrgan" @click="enroll()">人员录取</el-button>
        </el-col>
      </el-row>
      <div style="margin-top: 50px"></div>
    </div>
  </div>
</template>

<script>
export default {
  data() {
    return {
      activeName: 'second',
      club: {},
      detail: {},
      id: 0,
      isOrgan: false,
      user: {},
      images: [],
      enrolls: [],
      enables: [],
      award: [],
      credit: [],
      organizer: [],
      deadline: 0,
      deadline2: 0,
    };
  },
  mounted() {
    let url = window.location.toString()
    let arr = url.split('/')
    this.id = arr.pop()
    this.user = localStorage.getItem('user') ? JSON.parse(localStorage.getItem('user')) : {}
    this.load()
  },
  methods: {
    load() {
      //获取活动详情
      axios.get('/activity/id?id=' + this.id).then(res => {
        if (res.data.code === 200) {
          this.detail = res.data.data
          //计算活动时间
          this.computeTime()
          //获取图片
          axios.get('/picture/findByName?name=' + this.detail.name).then(res => {
            if (res.data.code === 200) {
              this.images = res.data.data
            }
          })
          //获取活动奖励
          axios.get('/award/findAllByName?name=' + this.detail.name).then(res => {
            if (res.data.code === 200) {
              this.award = res.data.data
            }
          })
          //获取活动学分
          axios.get('/credit/findAllByName?name=' + this.detail.name).then(res => {
            if (res.data.code === 200) {
              this.credit = res.data.data
            }
          })
          //获取活动负责人
          axios.get('/organizer/findAllByName?name=' + this.detail.name).then(res => {
            if (res.data.code === 200) {
              for (const re of res.data.data) {
                axios.get('/UserController/uaccount?uaccount=' + re.user).then(res2 => {
                  if (res2.data.code === 200) {
                    this.organizer.push(res2.data.data)
                    if (res2.data.data.uaccount === this.user.uaccount) {
                      this.isOrgan = true
                    }
                  }
                })
              }
            }
          })
          //获取该活动所属社团
          axios.get('/association/name?name=' + this.detail.association).then(res => {
            if (res.data.code === 200) {
              this.club = res.data.data
            }
          })
        }
      })
      //获取报名人员
      axios.get('/enroll/findAllByAcid?acid=' + this.id).then(res => {
        if (res.data.code === 200) {
          this.enrolls = res.data.data
          //获取报名状态
          axios.get('/enroll/findAllById?acid=' + this.id).then(res => {
            if (res.data.code === 200) {
              this.enables = res.data.data
              //  将录取状态赋给user
              for (let i = 0; i < this.enables.length; i++) {
                for (let j = 0; j < this.enrolls.length; j++) {
                  //判断uid是否相同
                  if (this.enables[i].uid === this.enrolls[j].uid) {
                    this.enrolls[j].en = this.enables[i].en
                  }
                }
              }
            }
          })
        }
      })
    },
    reload() {
      window.location.reload()
    },
    enroll() {
      this.$router.push('/front/enroll/' + this.id)
    },
    join() {
      //  报名活动
      let enroll = {}
      enroll.acid = this.id
      enroll.uid = this.user.uid
      axios.post("/enroll/save", enroll).then(res => {
        if (res.data.code === 200) {
          this.$notify.success(res.data.msg)
          window.location.reload()
        } else this.$notify.warning(res.data.msg)
      })
    },
    computeTime() {
      // 验证秒杀开始时间是否过期
      // 1.还未开始
      if (new Date() <= new Date(this.detail.enrolltime)) {
        this.deadline = (new Date(this.detail.enrolltime)).getTime()
      }
      // 2.在开始和结束时间范围内
      if (new Date() >= new Date(this.detail.enrolltime) && new Date() <= new Date(this.detail.enrollendtime)) {
        this.deadline2 = (new Date(this.detail.enrollendtime)).getTime()
      }
      // 3.活动已结束
      if (new Date() >= new Date(this.detail.enrollendtime)) {

      }
    },
    handleClick(tab, event) {
      // console.log(tab, event);
    }
  }
}
</script>

<style scoped>
.MyImage {
  width: 100px;
  height: 100px;
  border-radius: 50px;
  transition: all 0.6s;
}

.MyImage:hover {
  transform: scale(1.1);
}

.MyImage2 {
  width: 60px;
  height: 60px;
  border-radius: 50px;
  transition: all 0.6s;
}

.MyImage2:hover {
  transform: scale(1.1);
}

.en {
  background-color: #63bbd0;
  border-radius: 5px;
  width: 80px;
  height: 26px;
  text-align: center;
  margin-left: 80%;
  margin-top: -30px
}

.en2 {
  background-color: #34b640;
  border-radius: 5px;
  width: 80px;
  height: 26px;
  text-align: center;
  margin-left: 80%;
  margin-top: -30px
}
</style>