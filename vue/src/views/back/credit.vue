<template>
  <!--<el-badge :value="200" :max="99" class="item">-->
  <!--  <el-button size="small">评论</el-button>-->
  <!--</el-badge>-->
  <div style="margin-top: 10px">
    <div style="float: right;margin: 10px">
      <el-input
          style="width: 220px;margin-right: 10px"
          placeholder="输入用户名搜索"
          suffix-icon="el-icon-search"
          @keyup.native.enter="load"
          v-model="name">
      </el-input>
      <el-input
          style="width: 220px;margin-right: 10px"
          placeholder="输入学院搜索"
          suffix-icon="el-icon-search"
          @keyup.native.enter="load"
          v-model="college">
      </el-input>
      <el-input
          style="width: 220px;margin-right: 10px"
          placeholder="输入学号搜索"
          suffix-icon="el-icon-search"
          @keyup.native.enter="load"
          v-model="num">
      </el-input>
      学分在
      <el-input
          style="width: 80px;margin-right: 10px"
          placeholder="最小学分"
          @keyup.native.enter="load"
          v-model="min">
      </el-input>
      ~
      <el-input
          style="width: 80px;margin-right: 10px"
          placeholder="最大学分"
          @keyup.native.enter="load"
          v-model="max">
      </el-input>
      <el-button class="ml-5" size="medium" type="primary" @click="load">搜索</el-button>
      <el-button type="warning" size="medium" @click="reset">重置</el-button>
    </div>
    <el-table :data="tableData" stripe style="width: 100%">
      <el-table-column
          label="序号"
          type=index
          width="55">
      </el-table-column>
      <el-table-column label="用户头像">
        <template slot-scope="scope">
          <el-image style="width: 100px" :src="tableData[scope.$index].url"></el-image>
        </template>
      </el-table-column>
      <el-table-column prop="uaccount" label="用户名"></el-table-column>
      <el-table-column prop="college" label="学院"></el-table-column>
      <el-table-column prop="num" label="学号"></el-table-column>
      <el-table-column prop="gender" label="性别"></el-table-column>
      <el-table-column prop="credit" label="学分" width="150"></el-table-column>
      <el-table-column label="发放学分" width="150">
        <template slot-scope="scope">
          <el-popover
              placement="right"
              width="400"
              trigger="click">
            <el-form ref="form" :model="form" label-width="120px" label-position="left">
              <el-form-item label="发放学分">
                <el-input-number size="small" :min="0" v-model.number="form.count"></el-input-number>
              </el-form-item>
              <el-form-item label="发放学分原因">
                <el-input v-model="form.reason"></el-input>
              </el-form-item>
            </el-form>
            <el-button @click="addCredit(form.count,scope.row)">添加</el-button>
            <el-button slot="reference">点击添加</el-button>
          </el-popover>
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
  name: "credit",
  data() {
    return {
      form: {},
      pageSize: 8,
      total: 0,
      pageNum: 1,
      name: '',
      college: '',
      num: '',
      url: '',
      user: {},
      tableData: [],
      max: 5000,
      min: 0
    }
  },
  mounted() {
    this.load()
  },
  methods: {
    load() {
      axios.get("/UserController/credit", {
        params: {
          pageNum: this.pageNum,
          pageSize: this.pageSize,
          name: this.name,
          num: this.num,
          college: this.college,
          max: this.max,
          min: this.min
        }
      }).then(res => {
        if (res.data.code === 200) {
          this.tableData = res.data.data.records
          console.log(res.data.data.records)
          this.total = res.data.data.total
        }
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
    changeRole(role, row) {
      console.log(role)
      console.log(row)
      this.update(row)
    },
    addCredit(count, row) {
      this.form = {}
      console.log(count)
      console.log(row)
      row.credit = row.credit + count
      this.update(row)

    },
    update(row) {
      axios.post("UserController/UserUpdate", row).then(res => {
        if (res.data.code === 200) {
          this.$notify.success("添加成功")
          this.load();
        } else this.$notify.error("添加失败")
      })
    },
    changeIsdelete(isdelete, row) {
      console.log(isdelete)
      console.log(row)
      this.update(row)
    }
  }
}
</script>

<style scoped>

</style>