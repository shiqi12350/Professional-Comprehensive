<script>
import axios from "axios";
import * as echarts from 'echarts';
import {useStockInfoStore} from "@/stores/stockInfoStore.js";
export default {
  setup(){
    const stockInfoStore = useStockInfoStore()
    return {
      stockId: stockInfoStore.stockId
    }
  },
  data(){
    return{
      StockID:this.stockId,
      StartTime:"20240531",
      NavList:[],
      pageList:[],
      nav_date:[],
      currentPage: 1,
      pageSize: 5,
      total_netasset:[],
      tableData:[],
      thisYear:null,
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
      console.log(this.NavList)
      console.log(this.nav_date)
      console.log(this.total_netasset)
      this.initChart()
      this.pagination()
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
    },
    pagination() {
      const pageCount = Math.ceil(this.NavList.length / this.pageSize)

      for(let i = 0; i < pageCount; i++) {
        this.pageList.push(
            this.NavList.slice(i * this.pageSize, (i + 1) * this.pageSize)
        )
      }
    },
    handleCurrentChange(page) {
      this.currentPage = page
      this.tableData = this.pageList[page - 1]
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
        <el-menu-item
            index="/StockInfo/dataCatalog_basicInfo"
            :params="{stockId: StockID}"
        >基本信息</el-menu-item>
        <el-menu-item
            index="/StockInfo/dataCatalog_navHistory"
            :params="{stockId: StockID}"
        >净值记录</el-menu-item>
      </el-sub-menu>
      <el-menu-item
          index="/StockInfo/RankInfo"
          :params="{stockId: StockID}"
      >排名信息</el-menu-item>
      <el-menu-item
          index="/StockInfo/AI_analysis"
          :params="{stockId: StockID}"
      >AI分析</el-menu-item>
      <el-menu-item
          index="/StockInfo/UserFeedback"
          :params="{stockId: StockID}"
      >用户反馈</el-menu-item>
    </el-menu>
  </el-header>
  <el-main>
    <el-row>
      <el-col :span="6">
        <el-table
          :data="tableData"
          :size="small"
        >
          <el-table-column prop="nav_date" label="日期" width="100" ></el-table-column>
          <el-table-column prop="total_netasset" label="净值" width="120" ></el-table-column>
        </el-table>
        <el-pagination
            small
            layout="prev, pager, next"
            :current-page="currentPage"
            :page-size="pageSize"
            :total="25"
            @current-change="handleCurrentChange">

        </el-pagination>
      </el-col>
      <el-col :span="18">
        <div ref="stockNavChart" id="stockNavChart"></div>
      </el-col>
    </el-row>
  </el-main>
</template>

<style>
#stockNavChart{
  width:900px;
  height:550px;
}
</style>