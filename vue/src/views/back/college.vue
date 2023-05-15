<template>
  <div>
    <el-table :data="files"
              style="width: 100%"
    >
      <el-table-column
          label="序号"
          type=index
      >
      </el-table-column>
      <el-table-column
          label="学院ID"
          prop="cid">
      </el-table-column>
      <el-table-column
          label="学院名称"
      >
        <template slot-scope="scope">
          <el-input v-model="files[scope.$index].cname" :disabled="files[scope.$index].edit?true:false"
                    style="width: 200px"></el-input>
        </template>
      </el-table-column>
      <el-table-column
          v-if="user.urole==='管理员'"
          label="是否假删除"
          align="center"
      >
        <template slot-scope="scope">
          <el-switch
              active-color="#13ce66"
              v-model="files[scope.$index].isdelete"
              :disabled="files[scope.$index].edit?true:false"
              :active-value=1
              :inactive-value=0
          >
          </el-switch>
          <!--<el-input v-model="files[scope.$index].isdelete" :disabled="files[scope.$index].edit"></el-input>-->
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
                style="width: 120px"/>
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
              title="删除学院会将该学院绑定的所有专业和社团一并删除，请确认操作！"
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
              v-if="files[scope.$index].edit"
              size="mini"
              icon="el-icon-edit"
              style="margin-left: 10px"
              @click="handleEdit(scope.$index, scope.row)">编辑
          </el-button>
          <el-button
              v-else
              size="mini"
              icon="el-icon-edit"
              style="margin-left: 10px"
              @click="handleCancel(scope.$index, scope.row)">取消
          </el-button>
          <el-button
              v-if="files[scope.$index].edit"
              size="mini"
              type="primary"
              icon="el-icon-circle-plus-outline"
              @click="handleAdd(scope.$index, scope.row)">新增
          </el-button>
          <el-button
              v-else
              size="mini"
              icon="el-icon-circle-check"
              style="margin-left: 10px"
              type="success"
              @click="handleSave(scope.$index, scope.row)">保存
          </el-button>
        </template>
      </el-table-column>
    </el-table>
    <div style="padding: 10px 0">
      <!--分页条-->
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
  name: "college",
  data() {
    return {
      edit: true,
      files: [],
      pageSize: 8,
      total: 0,
      pageNum: 1,
      name: '',
      college: {},
      url: '',
      user: {},
    }
  },
  created() {
    this.user = localStorage.getItem('user') ? JSON.parse(localStorage.getItem('user')) : {}
    this.load()
  },
  methods: {
    load() {
      if (this.user.urole === '管理员')
        this.url = 'pageAdmin'
      else if (this.user.urole === '教师') this.url = 'page'
      axios.get("/college/" + this.url, {
        params: {
          pageNum: this.pageNum,
          pageSize: this.pageSize,
          name: this.name
        }
      }).then(res => {
        this.files = res.data.data.records
        console.log(this.files)
        this.total = res.data.data.total
      })
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
      // console.log(this.files)
    },
    handleCancel(index, row) {
      // console.log(index)
      // console.log(row)
      // console.log(row.mid)
      //新增时取消就刷新页面
      if (row.mid === undefined)
        this.load()
      //正常取消改变编辑状态
      else
        row.edit = true
    },
    handleSave(index, row) {
      row.edit = true
      if (row.cname === "" || row.cname === null || row.cname === undefined) {
        this.$notify.error('学院名称不能为空！')
        this.load()
      } else {
        //判断当前是否改变
        this.college.cid = row.cid
        this.college.cname = row.cname
        this.college.isdelete = row.isdelete
        if (row.edit === true)
          this.college.edit = 1
        else
          this.college.edit = 0
        axios.post('/college', this.college).then((res => {
          if (res.data.code === 200) {
            this.$notify.success(res.data.msg)
            //放前面试试 --可以
            this.load()
          } else {
            this.$notify.warning(res.data.msg)
            this.load()
          }
        }))
      }
      //刷新页面 这个不好使
      // window.location.reload()
      //刷新页面 this.load()也不好使，可能是因为有axios，前面用这个还挺好使
      //this.load()
    },
    handleAdd() {
      this.files.push({})
      this.edit = false
    },
    handleDelete(row) {
      if (this.user.urole === '管理员') {
        axios.delete('/college/deleteAdmin/' + row.cid).then((res => {
          if (res.data.code === 200) {
            this.$notify.success("删除成功")
            this.load()
          }
        }))
      } else if (this.user.urole === '教师') {
        axios.delete('/college/' + row.cid).then((res => {
          if (res.data.code === 200) {
            this.$notify.success("删除成功")
            this.load()
          }
        }))
      }

    },
    cancel(row) {
      console.log(row);
    }
  },
}
</script>

<style scoped>

</style>