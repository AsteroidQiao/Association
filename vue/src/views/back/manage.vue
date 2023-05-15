<template>
  <div style="margin-top: 10px">

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
    <el-page-header @back="$router.go(-1)" content=""></el-page-header>

    <div style="float: right;margin: 10px">
      <el-input
          style="width: 280px;margin-right: 10px"
          placeholder="输入用户名搜索"
          suffix-icon="el-icon-search"
          @keyup.native.enter="load"
          v-model="name">
      </el-input>

      <el-select v-model="enable"
                 @change="load"
                 clearable
                 filterable placeholder="请选择">
        <el-option label="全部" value="2"></el-option>
        <el-option label="通过" value="0"></el-option>
        <el-option label="未通过" value="1"></el-option>
      </el-select>
      <el-button class="ml-5" type="primary" @click="load">搜索</el-button>
      <el-button type="warning" @click="reset">重置</el-button>
    </div>
    <el-table :data="enrolls" stripe style="width: 100%">
      <el-table-column
          label="序号"
          type=index
          width="55">
      </el-table-column>
      <el-table-column label="用户头像">
        <template slot-scope="scope">
          <el-image style="width: 100px" :src="enrolls[scope.$index].url"></el-image>
        </template>
      </el-table-column>
      <el-table-column prop="uaccount" label="用户名"></el-table-column>
      <el-table-column prop="college" label="学院"></el-table-column>
      <el-table-column prop="gender" label="性别"></el-table-column>
      <el-table-column
          fixed="right"
          label="社员申请是否通过">
        <template slot-scope="scope">
          <el-switch
              active-color="#13ce66"
              v-model="enrolls[scope.$index].isdelete"
              @change="changeEnable(scope.row)"
              :active-value=0
              :inactive-value=1>
          </el-switch>
        </template>
      </el-table-column>
    </el-table>
    <div style="padding: 10px 0">
      <el-pagination
          @size-change="handleSizeChange"
          @current-change="handleCurrentChange"
          :current-page="pageNum"
          :page-sizes="[2,4,6,8,16]"
          :page-size="pageSize"
          layout="total, sizes, prev, pager, next, jumper"
          :total="total">
      </el-pagination>
    </div>
  </div>
</template>

<script>
export default {
  name: "mange",
  data() {
    return {
      user: {},
      id: 0,
      enable: 2,
      enrolls: [],
      enables: [],
      pageSize: 8,
      total: 0,
      pageNum: 1,
      name: '',
    }
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
      this.enrolls = []
      this.enables = []
      //获取全部社员
      axios.get('/relation/findAllByAidPage',{
        params:{
          pageNum: this.pageNum,
          pageSize: this.pageSize,
          name: this.name,
          aid: this.id,
          enable: this.enable,
        }
      }).then(res => {
        if (res.data.code === 200) {
          this.enrolls = res.data.data.records
          this.total = res.data.data.total
          //获取社团人员录取情况
          axios.get('/relation/findAllByAid?aid=' + this.id).then(res => {
            if (res.data.code === 200) {
              this.enables = res.data.data
              //  将录取状态赋给user
              for (let i = 0; i < this.enables.length; i++) {
                for (let j = 0; j < this.enrolls.length; j++) {
                  //判断uid是否相同
                  if (this.enables[i].uid === this.enrolls[j].uid) {
                    this.enrolls[j].isdelete = this.enables[i].isdelete
                  }
                }
              }
            }
          })
        }
        else this.$notify.error(res.data.msg)
      })
    },
    changeEnable(row) {
      let en = {}
      console.log(row.isdelete)
      en.uid = row.uid
      en.isdelete = row.isdelete
      en.aid = this.id
      axios.post('/relation/enroll', en).then(res => {
        if (res.data.code === 200) {
          if (en.isdelete === 0) {
            this.$notify.success("已成功录取")
          } else if (en.isdelete === 1) {
            this.$notify.success("已撤销录取")
          }
        } else this.$notify.error(res.data.msg)
      })
    },
    reset() {
      this.name = "";
      this.college = ""
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

<style scoped>

</style>