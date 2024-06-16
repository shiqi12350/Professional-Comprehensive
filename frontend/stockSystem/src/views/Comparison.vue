<script>
import axios from "axios";
import * as echarts from 'echarts'
export default{
  data(){
    return{
      StockID:'',
      StockDataset:[]
    }
  },
  methods:{
    deleteRow(index) {
      this.StockDataset.splice(index, 1)
      console.log(this.StockDataset)
      this.updateChart()
    },

    async addStock(StockID){
      let stockInfo = {
        ts_code:'',
        name:'',
        p_value:'',
        fd_share:'',
        nav_date:[],
        total_netasset:[],
        pct_chg:'',
        amount:''
      }
      console.log("start adding items")
      const response = await axios.post(
          "http://api.tushare.pro",
          {
            api_name: "fund_basic",
            token: "91a073e655c1849334691d5f5c71a518ff5468891554887fad2afca0",
            params:{"ts_code":StockID}
          }
      );
      console.log(response)
      stockInfo.ts_code = response.data.data.items[0][0]
      stockInfo.name = response.data.data.items[0][1]
      stockInfo.p_value = response.data.data.items[0][14]
      console.log(stockInfo)
      const response2 = await axios.post(
          "http://api.tushare.pro",
          {
            api_name: "fund_share",
            token: "91a073e655c1849334691d5f5c71a518ff5468891554887fad2afca0",
            params:{"ts_code":StockID}
          }
      );
      stockInfo.fd_share = response2.data.data.items[0][2]
      console.log(stockInfo)
      const response3 = await axios.post(
          "http://api.tushare.pro",
          {
            api_name: "fund_nav",
            token: "91a073e655c1849334691d5f5c71a518ff5468891554887fad2afca0",
            params:{"ts_code":StockID}
          }
      );
      for( let i = 25; i >=0 ; i--){
        stockInfo.nav_date.push({
          nav_date:response3.data.data.items[i][2],
        })
        stockInfo.total_netasset.push({
          total_netasset:response3.data.data.items[i][8]
        })
      }
      console.log(stockInfo)
      const response4 = await axios.post(
          "http://api.tushare.pro",
          {
            api_name: "fund_daily",
            token: "91a073e655c1849334691d5f5c71a518ff5468891554887fad2afca0",
            params:{"ts_code":StockID}
          }
      );
      stockInfo.pct_chg = response4.data.data.items[0][8]
      stockInfo.amount = response4.data.data.items[0][10]
      console.log(stockInfo)
      const item = {
        ts_code: stockInfo.ts_code,
        name: stockInfo.name,
        p_value:stockInfo.p_value,
        fd_share:stockInfo.fd_share,
        nav_date:stockInfo.nav_date,
        total_netasset:stockInfo.total_netasset,
        pct_chg:stockInfo.pct_chg,
        amount:stockInfo.amount

      }
      this.StockDataset.push(item)
      console.log(this.StockDataset)
      this.updateChart()
    },
    updateChart(){
      let chart = echarts.init(this.$refs.compareChart)
      let option = {
        title:{
          text:"净值走势对比图"
        },
        tooltip: {
          trigger: 'axis'
        },
        xAxis:{
          type:"category",
          data: this.StockDataset[0].nav_date.map(item => item.nav_date)
        },
        yAxis:{
          type:"value",
          scale:true
        },
        series:[]
      }
      option.series = []
      this.StockDataset.forEach(item => {
        let data = item.total_netasset.map(net => net.total_netasset)
        option.series.push({
          name: item.name,
          type: 'line',
          data: data
        })
      })
      console.log(option.series)
      chart.setOption(option)
    }
  }
}
</script>

<template>
  <el-card>
    <template #header>
      <el-row>
        <el-col :span="2">
          <div class="comparisonTitle">
            <img src="@/resource/comparison.png" class="comparisonPic">
          </div>
        </el-col>
        <el-col :span="22">
          <div class="comparisonName">基金比较</div>
        </el-col>
      </el-row>
    </template>
    <el-row>
      <el-col :span="4">
        <el-input placeholder="请输入内容" v-model="StockID" > </el-input>
      </el-col>
      <el-col :span="2">
        <el-button @click="addStock(this.StockID)">添加基金</el-button>
      </el-col>
      <el-col :span="18"></el-col>
    </el-row>
    <el-table
        :data="StockDataset">
      <el-table-column  prop="ts_code" label="基金代码" ></el-table-column>
      <el-table-column  prop="name" label="基金名称" ></el-table-column>
      <el-table-column  prop="p_value" label="面值" ></el-table-column>
      <el-table-column  prop="fd_share" label="	基金份额（万）" ></el-table-column>
      <el-table-column  prop="pct_chg" label="涨跌幅(%)" ></el-table-column>
      <el-table-column  prop="amount" label="成交额(千元)" ></el-table-column>
      <el-table-column fixed="right" label="操作" width="120">
        <template #default="scope">
          <el-button
              @click.prevent="deleteRow(scope.$index)"
              type="text"
              size="small"
          >
            移除
          </el-button>
        </template>
      </el-table-column>
    </el-table>
    <div ref="compareChart" id="compareChart"></div>
  </el-card>

</template>

<style>
#compareChart{
  width:800px;
  height:500px;
}
.comparisonPic{
  width:40px;
  margin-top:10px;
  margin-left:10px;
}
.comparisonName{
  margin-top:15px;
  font-size:30px;
}
.comparisonTitle{
  width:250px;
  height:60px;
  background-color: #fff;
}
</style>