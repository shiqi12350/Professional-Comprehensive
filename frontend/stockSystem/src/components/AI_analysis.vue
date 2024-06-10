<script>
import axios from "axios";

export  default {
  data(){
    return{
      array_data:[],
      StockID:'021406.OF',
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
      for(let i = 20;i >=0;i--){
        this.array_data.push(response.data.data.items[i][8])
      }
    },

    async AI_predict(array_data){
      const params = {
        model_name: 1,
        lookback: 20,
        future_days: 7,
        array: array_data
      }
      try {
        const response = await axios.post('/LSTM_prediction', params)
        console.log(response)
      } catch (error) {
        console.error(error)
      }
    },

  },
  mounted() {
    this.getNavData(this.StockID)
    this.AI_predict(this.array_data)
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
</template>

<style>

</style>