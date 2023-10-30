<template>
  <div class="container">
    <el-steps :active="active" simple>
      <el-step title="活动基本信息" icon="el-icon-edit"></el-step>
      <el-step title="添加活动图片" icon="el-icon-picture"></el-step>
      <el-step title="设置学分奖励" icon="el-icon-c-scale-to-original"></el-step>
    </el-steps>
    <div class="step1" v-if="active===1">
      <el-form ref="form" :model="form" label-position="left" label-width="100px" :rules="rules">
        <el-row :gutter="2">
          <el-col :span="12">
            <el-form-item label="主办方" prop="association">
              <el-select v-model="form.association" filterable placeholder="请选择"
                         style="margin-left: -120px" @click.native="clubChange">
                <el-option
                    v-for="item in association"
                    :key="item.name"
                    :label="item.name"
                    :value="item.name"
                >
                </el-option>
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="组织者" prop="organizers" @click.native="selectRes" style="margin-left: -100px">
              <el-popover
                  placement="center"
                  v-model="visible"
                  trigger="click">
                <el-table :data="users" @selection-change="handleSelectionChange">
                  <el-table-column type="selection" width="55"></el-table-column>
                  <el-table-column width="100" property="uaccount" label="姓名"></el-table-column>
                  <el-table-column width="100" property="gender" label="性别"></el-table-column>
                  <el-table-column width="180" property="college" sortable label="学院"></el-table-column>
                  <el-table-column width="140" property="num" sortable label="学号"></el-table-column>
                  <el-table-column width="120" property="phone" sortable label="电话"></el-table-column>
                </el-table>
                <el-input slot="reference" v-model="this.form.organizer" placeholder="请选择组织者"></el-input>
              </el-popover>
            </el-form-item>
          </el-col>
        </el-row>
        <el-form-item label="活动名称" prop="name">
          <el-input v-model="form.name"></el-input>
        </el-form-item>
        <el-form-item label="活动简介" prop="info">
          <el-input type="textarea" aria-multiline="true" :autosize="{ minRows: 2, maxRows: 5}"
                    v-model="form.info"></el-input>
        </el-form-item>
        <el-form-item label="参与须知" prop="notice">
          <el-input type="textarea" :autosize="{ minRows: 2, maxRows: 5}" v-model="form.notice"></el-input>
        </el-form-item>
        <el-row>
          <el-col :span="11">
            <el-form-item label="报名时间" prop="enrolltime">
              <div class="block">
                <el-date-picker
                    v-model="form.enrolltime"
                    type="datetime"
                    value-format="yyyy-MM-dd HH:mm:ss"
                    placeholder="选择开始时间"
                    default-time="12:00:00"
                    style="margin-left: -80px"
                >
                </el-date-picker>
              </div>
            </el-form-item>
          </el-col>
          <el-col :span="11">
            <el-form-item label="报名结束时间" label-width="200" prop="enrollendtime">
              <div class="block">
                <el-date-picker
                    v-model="form.enrollendtime"
                    type="datetime"
                    value-format="yyyy-MM-dd HH:mm:ss"
                    placeholder="选择结束时间"
                    default-time="12:00:00"
                >
                </el-date-picker>
              </div>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="11">
            <el-form-item label="活动时间" prop="starttime">
              <div class="block">
                <el-date-picker
                    v-model="form.starttime"
                    type="datetime"
                    value-format="yyyy-MM-dd HH:mm:ss"
                    placeholder="选择开始时间"
                    default-time="12:00:00"
                    style="margin-left: -80px">
                </el-date-picker>
              </div>
            </el-form-item>
          </el-col>
          <el-col :span="11">
            <el-form-item label="活动结束时间" prop="endtime" label-width="200">
              <div class="block">
                <el-date-picker
                    v-model="form.endtime"
                    type="datetime"
                    value-format="yyyy-MM-dd HH:mm:ss"
                    placeholder="选择结束时间"
                    default-time="12:00:00">
                </el-date-picker>
              </div>
            </el-form-item>
          </el-col>
        </el-row>
        <el-form-item>
          <el-button @click="clear">取消</el-button>
          <el-button type="primary" @click="next()">下一步</el-button>
        </el-form-item>
      </el-form>
    </div>
    <!--step2上传图片-->
    <div class="step2" v-if="active===2">
      <el-upload
          multiple
          :limit="5"
          :action="'http://'+  serverIp +':9090/gitee/upload'"
          list-type="picture-card"
          :file-list="imgs"
          :show-file-list="true"
          :on-success="handleSuccess"
          :on-preview="handlePictureCardPreview"
          :on-remove="handleRemove">
        <i class="el-icon-plus"></i>
      </el-upload>
      <el-dialog :visible.sync="dialogVisible">
        <img width="100%" :src="dialogImageUrl" alt="sdf">
      </el-dialog>
      <br/>
      <el-button @click="back">上一步</el-button>
      <el-button type="primary" @click="next">下一步</el-button>
    </div>
    <!--step3学分奖励-->
    <div class="step3" v-if="active===3">
      <el-row :gutter="10">
        <!--活动奖励设置-->
        <el-col :span="12">
          <el-card>
            <el-table :data="award" style="width: 100%">
              <el-table-column label="奖励名称" width="150" align="center">
                <template slot-scope="scope">
                  <el-input size="small" v-model="award[scope.$index].name"></el-input>
                </template>
              </el-table-column>
              <el-table-column label="名额" width="150" align="center">
                <template slot-scope="scope">
                  <el-input-number size="small" :min="0" v-model.number="award[scope.$index].count"></el-input-number>
                </template>
              </el-table-column>
              <el-table-column label="操作" align="center">
                <template slot-scope="scope">
                  <el-button
                      size="mini"
                      type="danger"
                      icon="el-icon-delete"
                      @click="handledelete(scope.$index, scope.row)"
                      style="margin-right: 0"
                  >删除
                  </el-button>
                </template>
              </el-table-column>
              <el-table-column
                  align="right">
                <template slot="header" slot-scope="scope">
                  <el-button
                      size="mini"
                      type="primary"
                      icon="el-icon-circle-plus-outline"
                      @click="handleAdd(scope.$index, scope.row)">新增
                  </el-button>
                </template>
              </el-table-column>
            </el-table>
          </el-card>
        </el-col>
        <el-col :span="12">
          <!--活动学分设置-->
          <el-card>
            <el-table :data="credit" style="width: 100%">
              <el-table-column label="学分数量" width="150" align="center">
                <template slot-scope="scope">
                  <el-input-number size="small" :min="0" v-model.number="credit[scope.$index].num"></el-input-number>
                </template>
              </el-table-column>
              <el-table-column label="名额" width="150" align="center">
                <template slot-scope="scope">
                  <el-input-number size="small" :min="0" v-model.number="credit[scope.$index].count"></el-input-number>
                </template>
              </el-table-column>
              <el-table-column label="操作" align="center">
                <template slot-scope="scope">
                  <el-button
                      size="mini"
                      type="danger"
                      icon="el-icon-delete"
                      @click="handledelete2(scope.$index, scope.row)"
                      style="margin-right: 0"
                  >删除
                  </el-button>
                </template>
              </el-table-column>
              <el-table-column
                  align="right">
                <template slot="header" slot-scope="scope">
                  <el-button
                      size="mini"
                      type="primary"
                      icon="el-icon-circle-plus-outline"
                      @click="handleAdd2(scope.$index, scope.row)">新增
                  </el-button>
                </template>
              </el-table-column>
            </el-table>
          </el-card>
        </el-col>
      </el-row>
      <br/>
      <el-button @click="back">上一步</el-button>
      <el-button type="primary" @click="commit()">发布活动</el-button>
    </div>
  </div>
</template>

<script>
import {serverIp} from "../../../public/config";

export default {
  data() {
    return {
      serverIp: serverIp,
      association: {},
      form: {},
      user: {},
      users: [],
      organ: [],
      rules: {
        association: [
          {required: true, message: '请输入主办方名称', trigger: 'blur'},
        ],
        organizers: [
          {required: true, message: '请选择组织者', trigger: 'blur'},
        ],
        name: [
          {required: true, message: '请输入活动名称', trigger: 'blur'},
          {min: 3, max: 20, message: '长度在 3 到 20 个字符', trigger: 'blur'}
        ],
        info: [
          {required: true, message: '请输入活动简介', trigger: 'blur'},
          {min: 5, max: 500, message: '长度在 50 到 500 个字符以内', trigger: 'blur'}
        ],
        notice: [
          {required: true, message: '请输入活动要求', trigger: 'blur'},
          {max: 500, message: '长度在 500 个字符以内', trigger: 'blur'}
        ],
        enrolltime: [
          {required: true, rigger: 'blur', validator: this.validateEnrollTime},
        ],
        enrollendtime: [
          {required: true, trigger: 'blur', validator: this.validateEnrollEndTime},
        ],
        starttime: [
          {required: true, trigger: 'blur', validator: this.validateStartTime},
        ],
        endtime: [
          {required: true, trigger: 'blur', validator: this.validateEndTime},
        ],

      },
      rules2: {
        name: [
          {required: true, message: '请输入奖项名称', trigger: 'blur'},
          {min: 3, max: 15, message: '长度在 3 到 15 个字符', trigger: 'blur'}
        ],
        count: [
          {required: true, message: '请设置奖项名额', trigger: 'blur'},
          {type: 'number', message: '数量必须为数字值'}
        ],
      },
      rules3: {
        num: [
          {required: true, message: '请输入学分数量', trigger: 'blur'},
          {type: 'number', message: '数量必须为数字值'}
        ],
        count: [
          {required: true, message: '请设置学分名额', trigger: 'blur'},
          {type: 'number', message: '数量必须为数字值'}
        ],
      },
      active: 1,
      uaccount: '',
      visible: false,
      award: [{
        name: '',
        count: 0,
        activity: ''
      }],
      credit: [{
        num: 0,
        count: 0,
        activity: ''
      }],
      dialogImageUrl: '',
      dialogVisible: false,
      imgs: [],
      fileList: [],
      picture: {},
    }
  },
  mounted() {
    this.user = localStorage.getItem('user') ? JSON.parse(localStorage.getItem('user')) : {}
    this.load()
  },
  methods: {
    load() {
      //获取我的社团
      axios.get("/association/myAssociation2?uid=" + this.user.uid).then(res => {
        if (res.data.code === 200) {
          this.association = res.data.data
        } else this.$notify.error(res.data.msg)
      })
    },
    //发布活动
    commit() {
      // console.log(this.form)
      // console.log(this.fileList[0])
      //绑定首张图片
      this.form.url = this.fileList[0].response
      axios.post("/activity/add", this.form).then(res => {
        if (res.data.code === 200) {
          //绑定负责人
          for (const user of this.organ) {
            let sent = {}
            sent.user = user
            sent.activity = this.form.name
            console.log(sent)
            axios.post('/organizer/save', sent)
          }
          // 上传图片集
          for (const fileListElement of this.fileList) {
            this.picture.url = fileListElement.response
            this.picture.name = this.form.name
            console.log(this.picture)
            axios.post("/picture/Add", this.picture)
          }
          //批量绑定学分奖励
          for (let i = 0; i < this.award.length; i++) {
            this.award[i].activity = this.form.name
          }
          for (let i = 0; i < this.credit.length; i++) {
            this.credit[i].activity = this.form.name
          }
          axios.post('/award/addBatch', this.award)
          axios.post('/credit/addBatch', this.credit)
          this.$notify.success(res.data.msg)
        } else {
          this.$notify.warning(res.data.msg)
        }
      })
    },
    handleAdd() {
      this.award.push({name: '', count: 0, activity: ''})
      console.log(this.award)
    },
    handledelete(index, row) {
      this.award.splice(index, 1)
    },
    handleAdd2() {
      this.credit.push({num: 0, count: 0, activity: ''})
      console.log(this.credit)
    },
    handledelete2(index, row) {
      this.credit.splice(index, 1)
    },
    //校验时间函数
    validateEnrollTime(rule, value, callback) {
      if (!value) {
        callback(new Error("请选择报名开始时间"));
      } else if (Date.parse(value) < Date.now()) {
        callback(new Error("开始时间必须大于当前时间"));
      }
      callback();
    },
    validateEnrollEndTime(rule, value, callback) {
      if (!value) {
        callback(new Error("请选择报名结束时间"));
      } else {
        if (!this.form.enrolltime) {
          callback(new Error("请选择活动开始时间！"))
        } else if (Date.parse(this.form.enrolltime) >= Date.parse(value)) {
          callback(new Error("结束时间必须大于开始时间！"))
        } else {
          callback();
        }
      }
      callback();
    },
    validateStartTime(rule, value, callback) {
      if (!value) {
        callback(new Error("请选择开始时间"));
      } else if (value < Date.now()) {
        callback(new Error("开始时间必须大于当前时间"));
      } else if (value < Date.parse(this.form.enrollendtime)) {
        callback(new Error("活动开始时间必须大于报名结束时间"));
      }
      callback();
    },
    validateEndTime(rule, value, callback) {
      if (!value) {
        callback(new Error("请选择结束时间"));
      } else {
        if (!this.form.starttime) {
          callback(new Error("请选择开始时间！"))
        } else if (Date.parse(this.form.starttime) >= Date.parse(value)) {
          callback(new Error("结束时间必须大于开始时间！"))
        } else {
          callback();
        }
      }
      callback();
    },
    //选择负责人
    selectRes() {
      //先选择社团才能选成员
      if (this.form.association === undefined) {
        this.$notify.error("请先选择社团！")
      } else {
        //获取所有用户
        axios.get("/relation/findAll?name=" + this.form.association).then(res => {
          if (res.data.code === 200) {
            this.users = res.data.data
            // console.log(this.users)
          } else {
            this.users = []
            this.$notify.error(res.data.msg)
          }
        })
      }
    },
    next() {
      if (this.active === 1) {
        // console.log("已进入===================")
        this.$refs['form'].validate((valid) => {
          // console.log(valid)
          // console.log("已进入2===================")
          if (valid) {
            this.active = this.active + 1
          }
        })
      } else this.active = this.active + 1
    },
    back() {
      this.active = this.active - 1;
      this.imgs = this.fileList
      // console.log("this.img==============")
      // console.log(this.imgs)
      for (const fileListElement of this.imgs) {
        fileListElement.url = fileListElement.response
      }
      // console.log("this.imgs")
      // console.log(this.imgs)
    },
    clear() {
      this.form = {}
    },

    clubChange() {
      console.log(this.association[0])
      if (this.association[0]=== undefined) {
        this.$notify.error("请先加入社团！")
      }
      console.log("fdsaads=========")
      this.organ = []
      this.form.organizer = ''
    },
    //删除照片
    handleRemove(file, fileList) {
      for (let i = 0; i < this.fileList.length; i++) {
        if (file === this.fileList[i]) {
          this.fileList.splice(i, 1)
        }
      }
      this.imgs = this.fileList
      // console.log(file, fileList);
      // console.log(this.imgs);
    },
    //照片上传成功
    handleSuccess(res, file, fileList) {
      // console.log(res)
      // console.log(file)
      // console.log(fileList)
      file.url = file.response.data
      this.fileList.push(file)
      // console.log(this.fileList)
    },
    //预览
    handlePictureCardPreview(file) {
      this.dialogImageUrl = file.response.data;
      this.dialogVisible = true;
    },
    //选择负责人改变触发
    handleSelectionChange(val) {
      // console.log(val);
      this.form.organizer = ''
      this.organ = []
      for (const valElement of val) {
        console.log(valElement)
        // console.log(valElement.uaccount)
        this.form.organizer += valElement.uaccount + "、"
        this.organ.push(valElement.uaccount)
      }
      console.log(this.organ)
      // console.log(val[valKey].uacount)
    },
  }
}
</script>

<style scoped>
.container {
  margin-top: 20px;
  text-align: center;
  width: 90%;
}

.step1 {
  margin-top: 20px;
}

.step2 {
  margin-top: 20px;
}

.step3 {
  margin-top: 20px;
}

.title3 {
  text-align: center;
  font: 20px '幼圆';
  font-weight: bold;
}

</style>