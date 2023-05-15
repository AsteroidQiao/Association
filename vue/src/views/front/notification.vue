<template>
  <div style="padding: 10px">
    <el-badge :value="val" :hidden="hidden" class="item">
      <el-button @click="examine('')">全部</el-button>
    </el-badge>
    <el-badge :value="val1" :hidden="hidden1" class="item">
      <el-button @click="examine('活动')">活动</el-button>
    </el-badge>
    <el-badge :value="val2" :hidden="hidden2" class="item" type="primary">
      <el-button @click="examine('部落')">部落</el-button>
    </el-badge>
    <el-badge :value="val3" :hidden="hidden3" class="item" type="warning">
      <el-button @click="examine('系统')">系统</el-button>
    </el-badge>
    <el-row>
      <el-col :span="24" v-for="item in notifications" :key="item.id" v-if="item.isread===0"
              style="margin-bottom: 10px">
        <div style="margin-top: 10px">
          <el-card shadow="hover" @click="$router.push(item.route)">
            <!--通知标题-->
            <h4>{{ item.title }}</h4>
            <!--通知内容-->
            <div @click="read(item)">{{ item.msg }}</div>
            <!--状态-->
            <div class="en" style="margin-bottom:25px;">
              <span style="color: white">未读</span>
            </div>
          </el-card>
        </div>
        <el-divider></el-divider>
      </el-col>
      <el-col :span="24" v-for="item in notifications" :key="item.id" v-if="item.isread===1"
              style="margin-bottom: 10px">

        <div style="margin-top: 10px">
          <el-card shadow="hover">
            <!--通知标题-->
            <h4>{{ item.title }}</h4>
            <!--通知内容-->
            <div @click="read(item)">{{ item.msg }}</div>
            <!--状态-->
            <div class="en2">
              <span style="color: white">已读</span>
            </div>
            <!--已读 可删除通知-->
            <div style="margin-top: -27px;margin-bottom:20px;margin-left: 90%">
              <el-button type="danger" icon="el-icon-delete" size="mini" @click="handleDelete(item)">删除</el-button>
            </div>
          </el-card>
        </div>
        <el-divider></el-divider>
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
</template>

<script>
export default {
  name: "notification",
  data() {
    return {
      user: {},
      notifications: [],
      pageNum: 1,
      pageSize: 6,
      total: 0,
      name: '',
      val: 0,
      val1: 0,
      val2: 0,
      val3: 0,
      hidden: false,
      hidden1: false,
      hidden2: false,
      hidden3: false,
    }
  },
  mounted() {
    this.user = localStorage.getItem('user') ? JSON.parse(localStorage.getItem('user')) : null
    this.load()
  },
  methods: {
    //通过用户id和活动类型，获取相应通知
    load() {
      axios.get('/notification/page', {
        params: {
          pageNum: this.pageNum,
          pageSize: this.pageSize,
          name: this.name,
          uid: this.user.uid
        }
      }).then(res => {
        if (res.data.code === 200) {
          this.notifications = res.data.data.records
          this.total = res.data.data.total
        }
      })
      //获取所有通知已读和未读
      axios.get('/notification/findAll?uid=' + this.user.uid).then(res => {
        if (res.data.code === 200) {
          let notice = res.data.data
          this.val = 0
          this.val1 = 0
          this.val2 = 0
          this.val3 = 0
          for (let i = 0; i < notice.length; i++) {
            if (notice[i].isread === 0) {
              this.val++
              if (notice[i].type === '活动') {
                this.val1++
              } else if (notice[i].type === '部落') {
                this.val2++
              } else if (notice[i].type === '系统') {
                this.val3++
              }
            }
          }
          //  如果通知已读,则隐藏角标
          if (this.val === 0) {
            this.hidden = true
          }
          if (this.val1 === 0) {
            this.hidden1 = true
          }
          if (this.val2 === 0) {
            this.hidden2 = true
          }
          if (this.val3 === 0) {
            this.hidden3 = true
          }
        }
      })
    },
    examine(name) {
      //获取通知类型
      this.name = name
      this.load()
    },
    read(item) {
      //修改isread为已读
      item.isread = 1
      axios.post('/notification/save', item).then(res => {
        if (res.data.code === 200) {
          this.load()
        }
      })
      if (item.route !== null && item.route !== '' && item.route !== undefined)
        window.location.assign(item.route)
    },
    handleDelete(row) {
      row.isdelete = 1
      axios.post('/notification/save', row).then(res => {
        if (res.data.code === 200) {
          this.$notify.success("删除成功")
          this.load()
        }
      })
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

<style scoped>
.item {
  margin-top: 10px;
  margin-right: 40px;
}

.en {
  background-color: #fc3714;
  border-radius: 5px;
  width: 80px;
  height: 26px;
  text-align: center;
  margin-left: 80%;
  margin-top: -30px
}

.en2 {
  background-color: #8da4fc;
  border-radius: 5px;
  width: 80px;
  height: 26px;
  text-align: center;
  margin-left: 80%;
  margin-top: -30px
}
</style>