<script>
import axios from "axios";
import * as echarts from 'echarts';
export default {
  data(){
    return{
      StockID:"021406.OF",
      StartTime:"20240531",
      NavList:[],
      nav_date:[],
      total_netasset:[],
    }
  },
  methods:{
    async getNavData(StockID){
      const response = await axios.post(
          "http://api.tushare.pro",
          {
            api_name: "fund_nav",
            token: "91a073e655c1849334691d5f5c71a518ff5468891554887fad2afca0",
            params:{"ts_code":StockID}
          }
      );
      for( let i = 25; i >=0 ; i--){
        this.NavList.push({
          nav_date:response.data.data.items[i][2],
          total_netasset:response.data.data.items[i][8]
        })
      }
      this.nav_date = this.NavList.map(item => {
        return item.nav_date
      })
      this.total_netasset = this.NavList.map(item => {
        return item.total_netasset
      })
      console.log(this.nav_date)
      console.log(this.total_netasset)
      this.initChart()
    },
    async getTimeNavData(StockID,DuringTime){
      const response = await axios.post(
          "http://api.tushare.pro",
          {
            api_name: "fund_nav",
            token: "91a073e655c1849334691d5f5c71a518ff5468891554887fad2afca0",
            params:{"ts_code":StockID,
                    "nav_date":DuringTime}
          }
      );
      console.log(response)
    },
    initChart(){
      let NavChart = echarts.init(this.$refs.stockNavChart)
      let xData = this.nav_date
      let data = this.total_netasset
      let options={
        xAxis:{
          type:"category",
          data:xData
        },
        yAxis:{
          type:"value",
          scale:true
        },
        series:[{
          data,
          type:"line",
          areaStyle:{
            color:'pink'
          },
          emphasis: {
            itemStyle: {
              // 高亮时点的颜色。
              color: 'blue'
            },
            label: {
              show: true,
            }
          },
          markPoint : {
            data : [
              {
                type : 'max' ,
                name : ' 最大值 '
              }, {
                type : 'min' ,
                name : ' 最小值 '
              }
            ]
          },
          markLine:{
            data:[
              {
                type:'average',
                name:'平均值'
              }
            ]
          }
        }]
      };
      NavChart.setOption(options)
    }
  },
  mounted() {
    this.getNavData(this.StockID)
    this.getTimeNavData(this.StockID,this.StartTime)
  }
}
</script>

<template>
  <el-header>
    <el-menu
        :default-active="activeIndex"
        class="stockInfoMenu"
        mode="horizontal"
        background-color="#545c64"
        text-color="#fff"
        active-text-color="#ffd04b"
        router
    >
      <el-sub-menu index="1">
        <template #title>数据目录</template>
        <el-menu-item index="/StockInfo/dataCatalog_basicInfo" >基本信息</el-menu-item>
        <el-menu-item index="/StockInfo/dataCatalog_navHistory">净值记录</el-menu-item>
      </el-sub-menu>
      <el-menu-item index="/StockInfo/RankInfo">排名信息</el-menu-item>
      <el-menu-item index="/StockInfo/AI_analysis">AI分析</el-menu-item>
      <el-menu-item index="/StockInfo/UserFeedback">用户反馈</el-menu-item>
    </el-menu>
  </el-header>
  <el-main>
    <div ref="stockNavChart" id="stockNavChart"></div>
  </el-main>
</template>

<style>
#stockNavChart{
  width:1000px;
  height:550px;
  margin-left:100px
}
</style>