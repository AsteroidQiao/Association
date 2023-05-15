<template>
  <div style="margin-top: 10px">
    <div style="float: right;margin: 10px">
      <el-input
          style="width: 240px;margin-right: 10px"
          placeholder="输入活动名称搜索"
          suffix-icon="el-icon-search"
          @keyup.native.enter="load"
          v-model="name">
      </el-input>
      <el-select v-model="pass"
                 @change="load"
                 clearable
                 filterable placeholder="请选择">
        <el-option label="未通过" value="0"></el-option>
        <el-option label="通过" value="1"></el-option>
        <el-option label="全部" value="2"></el-option>
      </el-select>
      <!--<el-input-->
      <!--    style="width: 240px;margin-right: 10px"-->
      <!--    placeholder="输入学院搜索"-->
      <!--    suffix-icon="el-icon-search"-->
      <!--    @keyup.native.enter="load"-->
      <!--    v-model="">-->
      <!--</el-input>-->
      <!--<el-input-->
      <!--    style="width: 240px;margin-right: 10px"-->
      <!--    placeholder="输入学号搜索"-->
      <!--    suffix-icon="el-icon-search"-->
      <!--    @keyup.native.enter="load"-->
      <!--    v-model="">-->
      <!--</el-input>-->
      <el-button class="ml-5" type="primary" @click="load">搜索</el-button>
      <el-button type="warning" @click="reset">重置</el-button>
    </div>
    <el-table :data="tableData" stripe style="width: 100%">
      <el-table-column
          label="序号"
          type=index
          width="55">
      </el-table-column>
      <el-table-column label="活动图片">
        <template slot-scope="scope">
          <el-image style="width: 100px" :src="tableData[scope.$index].url"></el-image>
        </template>
      </el-table-column>
      <el-table-column prop="name" label="活动名称"></el-table-column>
      <el-table-column prop="association" label="主办社团"></el-table-column>
      <el-table-column label="报名时间">
        <template slot-scope="scope">
          {{ tableData[scope.$index].enrolltime }}—{{ tableData[scope.$index].enrollendtime }}
        </template>
      </el-table-column>
      <el-table-column label="活动时间">
        <template slot-scope="scope">
          {{ tableData[scope.$index].starttime }}—{{ tableData[scope.$index].endtime }}
        </template>
      </el-table-column>
      <el-table-column label="查看录取人员">
        <template slot-scope="scope">
          <el-button type="success" size="small" round @click="enroll(scope.row)">点击查看详情</el-button>
        </template>
      </el-table-column>
      <el-table-column label="查看详情">
        <template slot-scope="scope">
          <el-button type="primary" size="small" round @click="detail(scope.row)">点击查看详情</el-button>
        </template>
      </el-table-column>
      <el-table-column label="是否通过" width="150">
        <template slot-scope="scope">
          <el-switch
              active-color="#13ce66"
              v-model="tableData[scope.$index].pass"
              @change="changePass(scope.row,tableData[scope.$index].pass)"
              :active-value=1
              :inactive-value=0>
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
  data() {
    return {
      tableData: [],
      pageSize: 8,
      total: 0,
      pageNum: 1,
      name: '',
      pass: 2
    }
  },
  mounted() {
    this.load()
  },
  methods: {
    load() {
      axios.get("/activity/page", {
        params: {
          pageNum: this.pageNum,
          pageSize: this.pageSize,
          name: this.name,
          pass: this.pass
          // num: this.num,
          // college: this.college,
          // max: this.max,
          // min: this.min
        }
      }).then(res => {
        if (res.data.code === 200) {
          this.tableData = res.data.data.records
          console.log(res.data.data.records)
          this.total = res.data.data.total
        }
      })
    },
    detail(row) {
      this.$router.push("/back/activityDetail/"+row.acid)
    },
    enroll(row) {
      console.log(row)
      this.$router.push('/back/enroll/' + row.acid)
    },
    reset() {
      this.name = "";
      this.college = ""
      this.load();
    },
    update(row) {
      axios.post("/activity/update", row).then(res => {
        if (res.data.code === 200) {
          this.$notify.success("修改成功")
          this.load();
        } else this.$notify.error("修改失败")
      })
    },
    changePass(row, pass) {
      console.log(row)
      console.log(pass)
      row.pass = pass
      this.update(row)
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