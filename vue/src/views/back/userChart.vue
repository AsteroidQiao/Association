<template>
  <div>
    <el-row>
      <el-col :span="12">
        <div id="userChart" style="width: 500px;height: 400px;"></div>
      </el-col>
      <el-col :span="12">
        <div id="userChartPie" style="width: 500px;height: 400px;">></div>
      </el-col>
    </el-row>
  </div>
</template>

<script>
import * as echarts from 'echarts';

export default {
  name: "userChart",
  mounted() {
    this.charts()
  },
  data() {
    return {}
  },
  methods: {
    charts() {
      var chartDom = document.getElementById('userChart');
      var myChart = echarts.init(chartDom);
      var option;
      var chartDomPie = document.getElementById('userChartPie');
      var myChartPie = echarts.init(chartDomPie);
      var optionPie;
      option = {
        title: {
          text: '各季度用户注册数量统计',
          subtext: '趋势图',
          left: 'center'
        },
        tooltip: {
          trigger: 'item',
        },
        legend: {
          orient: 'vertical',
          left: 'left'
        },
        xAxis: {
          type: 'category',
          data: ['第一季度', '第二季度', '第三季度', '第四季度']
        },
        yAxis: {
          type: 'value'
        },
        series: [
          {
            name: "折线图",
            data: [],
            type: 'line'
          },
          {
            name: "柱状图",
            data: [],
            type: 'bar'
          }
        ]
      };

      optionPie = {
        title: {
          text: '各季度用户注册数量统计',
          subtext: '比例图',
          left: 'center'
        },
        tooltip: {
          trigger: 'item',
          formatter: '{b} <br/>{a} : {c} '
        },
        legend: {
          orient: 'vertical',
          left: 'left'
        },
        series: [
          {
            name: '用户注册',
            type: 'pie',
            radius: '50%',
            label: {
              show: true,
              position:"inner",
              textStyle:{
                fontSize: 14
              },
              formatter:'{d}%',
            },
            data: [],
            emphasis: {
              itemStyle: {
                shadowBlur: 10,
                shadowOffsetX: 0,
                shadowColor: 'rgba(0, 0, 0, 0.5)'
              }
            }
          }
        ]
      };
      //获取每季度用户注册量
      axios.post("/charts/UserChart").then(res => {
        if (res.data.code === 200) {
          console.log(res.data.data)
          option.series[0].data = res.data.data
          option.series[1].data = res.data.data
          optionPie.series[0].data = [
            {value: res.data.data[0], name: '第一季度'},
            {value: res.data.data[1], name: '第二季度'},
            {value: res.data.data[2], name: '第三季度'},
            {value: res.data.data[3], name: '第四季度'},]

          //获取数据后setOption
          myChart.setOption(option)
          myChartPie.setOption(optionPie)
        }
      })

    }
  },
}
</script>

<style scoped>

</style>