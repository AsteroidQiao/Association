<template>
  <!--<el-badge :value="200" :max="99" class="item">-->
  <!--  <el-button size="small">评论</el-button>-->
  <!--</el-badge>-->
  <div style="margin-top: 10px">
    <div style="float: right;margin: 10px">
      <el-input
          style="width: 280px;margin-right: 10px"
          placeholder="输入用户名搜索"
          suffix-icon="el-icon-search"
          @keyup.native.enter="load"
          v-model="name">
      </el-input>
      <el-input
          style="width: 280px;margin-right: 10px"
          placeholder="输入学院搜索"
          suffix-icon="el-icon-search"
          @keyup.native.enter="load"
          v-model="college">
      </el-input>
      <el-select  v-model="role"
                  @change="load"
                  clearable
                 filterable placeholder="请选择">
        <el-option value="学生"></el-option>
        <el-option value="教师"></el-option>
        <el-option value="管理员"></el-option>
      </el-select>
      <!--<el-input-->
      <!--    style="width: 280px;margin-right: 10px"-->
      <!--    placeholder="输入角色搜索"-->
      <!--    suffix-icon="el-icon-search"-->
      <!--    @keyup.native.enter="load"-->
      <!--    v-model="role">-->
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
      <el-table-column label="用户头像">
        <template slot-scope="scope">
          <el-image style="width: 100px" :src="tableData[scope.$index].url"></el-image>
        </template>
      </el-table-column>
      <el-table-column prop="uaccount" label="用户名"></el-table-column>
      <el-table-column prop="college" label="学院"></el-table-column>
      <el-table-column prop="gender" label="性别"></el-table-column>
      <el-table-column label="角色" width="150">
        <template slot-scope="scope">
          <el-select v-if="tableData[scope.$index].urole==='管理员'" v-model="tableData[scope.$index].urole"
                     disabled></el-select>
          <el-select v-else v-model="tableData[scope.$index].urole"
                     @change="changeRole(tableData[scope.$index].urole,scope.row)"
                     filterable placeholder="请选择">
            <el-option value="学生"></el-option>
            <el-option value="教师"></el-option>
            <el-option value="管理员" disabled></el-option>
          </el-select>
        </template>
      </el-table-column>
      <el-table-column
          fixed="right"
          label="禁用">
        <template slot-scope="scope">
          <el-switch
              active-color="#13ce66"
              v-model="tableData[scope.$index].isdelete"
              @change="changeIsdelete(tableData[scope.$index].isdelete,scope.row)"
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
  name: "role",
  data() {
    return {
      role: '',
      pageSize: 8,
      total: 0,
      pageNum: 1,
      name: '',
      college: '',
      num: '',
      url: '',
      user: {},
      tableData: []
    }
  },
  mounted() {
    this.load()
  },
  methods: {
    load() {
      axios.get("/UserController/page", {
        params: {
          pageNum: this.pageNum,
          pageSize: this.pageSize,
          name: this.name,
          role: this.role,
          college: this.college,
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
    update(row) {
      axios.post("UserController/UserUpdate", row).then(res => {
        if (res.data.code === 200) {
          this.$notify.success("修改成功")
        } else this.$notify.error("修改失败")
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