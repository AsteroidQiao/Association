<template>
  <div>
    <el-table :data="files"
              row-key="mid"
              style="width: 100%"
              :tree-props="{children: 'children', hasChildren: 'hasChildren'}">
      <el-table-column
          label="专业ID"
          prop="mid">
      </el-table-column>
      <el-table-column
          label="绑定学院"
      >
        <template slot-scope="scope">
          <el-select v-model="files[scope.$index].cname" filterable placeholder="请选择" :disabled="edit">
            <el-option
                v-for="item in options"
                :key="item.cname"
                :value="item.cname">
            </el-option>
          </el-select>
        </template>
      </el-table-column>
      <el-table-column
          label="专业名称"
      >
        <template slot-scope="scope">
          <el-input v-model="files[scope.$index].major" :disabled="edit"></el-input>
        </template>
      </el-table-column>
      <el-table-column
          align="right">
        <template slot="header" slot-scope="scope">
          <div style="">
            <el-input
                v-model="name"
                size="mini"
                suffix-icon="el-icon-search"
                placeholder="输入关键字搜索"
                @keyup.native.enter="load"
                style="width: 180px"/>
            <el-button type="primary" size="mini" @click="load">搜索</el-button>
            <el-button type="warning" size="mini" @click="reset">重置</el-button>
          </div>

        </template>

        <template slot-scope="scope">
          <el-popconfirm
              confirm-button-text='确认'
              cancel-button-text='取消'
              icon="el-icon-info"
              icon-color="red"
              @cancel="cancel(scope.row)"
              @confirm="handleDelete(scope.row)"
              title="确定删除该专业吗？请确认操作！"
          >
            <el-button
                slot="reference"
                size="mini"
                type="danger"
                icon="el-icon-delete"
            >删除
            </el-button>
          </el-popconfirm>
          <el-button
              v-if="edit"
              size="mini"
              icon="el-icon-edit"
              style="margin-left: 10px"
              @click="handleEdit(scope.$index, scope.row)">编辑
          </el-button>
          <el-button
              v-else
              size="mini"
              icon="el-icon-circle-check"
              style="margin-left: 10px"
              type="success"
              @click="handleSave(scope.$index, scope.row)">保存
          </el-button>
          <el-button
              size="mini"
              type="primary"
              icon="el-icon-circle-plus-outline"
              @click="handleAdd(scope.$index, scope.row)">新增
          </el-button>
        </template>
      </el-table-column>
    </el-table>
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
  name: "major",
  data() {
    return {
      options: [],
      college: '',
      edit: true,
      files: [],
      pageSize: 8,
      total: 0,
      pageNum: 1,
      name: '',
      major: {},
      url: '',
      user: {}
    }
  },

  created() {
    this.load()
  },
  methods: {
    load() {
      axios.get("/major/page", {
        params: {
          pageNum: this.pageNum,
          pageSize: this.pageSize,
          name: this.name
        }
      }).then(res => {
        this.files = res.data.data.records
        this.total = res.data.data.total
      })
      axios.get('/college/').then((res => {
        this.options = res.data.data
      }))
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
    handleEdit(index, row) {
      row.edit = false
      // this.edit = false
      // console.log(this.files)
    },
    handleSave(index, row) {
      this.edit = true
      //判断当前是否改变
      this.major.mid = row.mid
      this.major.cname = row.cname
      this.major.major = row.major
      axios.post('/major', this.major).then((res => {
        if (res.data.code === 200) {
          this.$notify.success(res.data.msg)
          this.load()
        } else {
          this.$notify.warning(res.data.msg)
        }
      }))
    },
    handleAdd() {
      this.files.push({})
      this.edit = false
    },
    handleDelete(row) {
      axios.delete('/major/' + row.mid).then((res => {
        if (res.data.code === 200) {
          this.$notify.success("删除成功")
        }
      }))
      this.load()
      this.load()
    },
    cancel(row) {
      console.log(row);
    }
  },
}
</script>

<style scoped>

</style>