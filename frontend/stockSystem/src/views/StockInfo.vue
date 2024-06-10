<script>
import * as echarts from 'echarts';
import {useRouter} from "vue-router";
import axios from "axios";
export default {
  data(){
    return{
    }
  },
  methods:{
    async getFundNav(){
      console.log(1)
      const response = await axios.post(
          "http://api.tushare.pro",
          {
            api_name: "fund_nav",
            token: "91a073e655c1849334691d5f5c71a518ff5468891554887fad2afca0",
            params:{"ts_code":"021406.OF",
                    "start_date":"20200101",
                    "end_date":"20200107"}
          }
      )
      console.log(2)
      console.log(response)
    }
  },
  mounted(){
    this.getFundNav()
  },
  setup() {
    const router = useRouter()
    router.push('/StockInfo/dataCatalog_basicInfo')
    function goBack() {
      router.push('/')
    }
    return {
      goBack
    }
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
#stockInfoChart{
  width:500px;
  height:400px;
  border:1px solid red;
}
.StockInfoTable{
  width:280px;
  height:500px;
  border:1px solid red;
}
.ai{
  width:250px;
  height:200px;
  border:1px solid red;
  margin-bottom: 20px;
}
.ref{
  width:250px;
  height:350px;
  border:1px solid red;
}
.StockName{
  font-weight: bold;
  font-size: 20px;
  text-align: center;
  margin-bottom: 20px;
}
.box-card{
  width:230px;
  margin-left:10px;
}
.card-header{
  height:15px;
}
</style>