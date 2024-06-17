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
      array_data:[1.8233, 1.8125, 1.8312, 1.8305, 1.8098, 1.8198, 1.7881, 1.7687, 1.776, 1.7938, 1.825, 1.837, 1.8058, 1.7871, 1.7994, 1.8119999999999998, 1.8027, 1.7705, 1.7839, 1.8126, 1.8068, 1.8172, 1.819, 1.8045, 1.7932, 1.7574, 1.7691, 1.7242, 1.6955, 1.714, 1.7235, 1.7327, 1.7244, 1.7289, 1.7548, 1.7489, 1.7269, 1.7357, 1.6980000000000002, 1.6959, 1.682, 1.695, 1.6851],
      NavList:[],
      pageList:[],
      nav_date:[],
      currentPage: 1,
      pageSize: 5,
      total_netasset:[],
      tableData:[],
      thisYear:null,
      listLoading:true,
      totalList:[],
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
          total_netasset:Number(response.data.data.items[i][8])
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
      this.pagination()
      this.handleCurrentChange(1)
      await this.AI_predict()
    },
    async AI_predict() {
      console.log("调用ai预测api")
      console.log(this.array_data)
      console.log(this.total_netasset)
      const data = {
        model_name: 2,
        lookback: 19,
        future_days: 7,
        array:this.total_netasset
      };
      const response = await axios.post('http://127.0.0.1:14106/LSTM_prediction', data, {
        headers: {
          'Content-Type': 'application/json'
        }
      });
      this.predictNav = response.data.Prediction
      this.allData = this.total_netasset
      for(let i = 0;i < 7;i++){
        this.allData.push(this.predictNav[i][0])
      }
      console.log("全部数据")
      console.log(this.allData)
      console.log("预测数字："+this.predictNav[0])
      console.log(response)
      this.initChart()
    },
    initChart(){
      let NavChart = echarts.init(this.$refs.stockNavChart)
      let xData = this.nav_date
      xData.push('future 1','future 2','future 3','future 4','future 5','future 6','future 7')
      let data = this.allData
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
      this.listLoading = true
      this.currentPage = page
      this.tableData = this.pageList[page - 1]
      this.listLoading = false
    }
  },
  mounted() {
    this.getNavData(this.StockID)

  }
}
</script>

<template>
  <el-card>
    <template #header>
      <el-row>
        <el-col :span="2">
          <div class="stockInfoTitle">
            <img src="@/resource/stockInfo.png" class="stockInfoPic">
          </div>
        </el-col>
        <el-col :span="22">
          <div class="stockInfoName">深度资料</div>
        </el-col>
      </el-row>
    </template>
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
              v-loading="listLoading"
          >
            <el-table-column prop="nav_date" label="日期"  ></el-table-column>
            <el-table-column prop="total_netasset" label="净值"></el-table-column>
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
  </el-card>
</template>

<style>
#stockNavChart{
  width:900px;
  height:550px;
}
.stockInfoPic{
  width:40px;
  margin-top:10px;
  margin-left:10px;
}
.stockInfoName{
  margin-top:15px;
  font-size:30px;
}
.stockInfoTitle{
  width:250px;
  height:60px;
  background-color: #fff;
}
</style>
