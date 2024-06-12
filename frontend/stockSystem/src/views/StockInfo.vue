<script>
import axios from "axios";

export  default {
  data(){
    return{
      array_data:[],
      StockID:'000005.OF',
      Alpha:'',
      Beta:'',
      Sharpe:'',
      averageRiskReturn:'',
      annualizedReturn:'',
      score:null,
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
      console.log("调用ai预测api")
      console.log(array_data)
      const data = {
        model_name: 1,
        lookback: 20,
        future_days: 7,
        array: [1.8233, 1.8125, 1.8312, 1.8305, 1.8098, 1.8198, 1.7881, 1.7687, 1.776, 1.7938, 1.825, 1.837, 1.8058, 1.7871, 1.7994, 1.8119999999999998, 1.8027, 1.7705, 1.7839, 1.8126, 1.8068, 1.8172, 1.819, 1.8045, 1.7932, 1.7574, 1.7691, 1.7242, 1.6955, 1.714, 1.7235, 1.7327, 1.7244, 1.7289, 1.7548, 1.7489, 1.7269, 1.7357, 1.6980000000000002, 1.6959, 1.682, 1.695, 1.6851],
      };
      const response = await axios.post('http://8.130.119.249:14106/LSTM_prediction', data, {
        headers: {
          'Content-Type': 'application/json'
        }
      });
      console.log(response)
    },
    async getScore(){
      const apiUrl = 'http://8.130.119.249:14105/predict'
      const requestData = {
        features: [
          Number(this.Alpha),
          Number(this.Beta),
          Number(this.Sharpe),
          Number(this.averageRiskReturn),
          Number(this.annualizedReturn)
        ]
      }
      try {
        const response = await axios.post(apiUrl, requestData)
        console.log(response)
        this.score = this.transformScore(response.data.prediction)
        console.log(this.score)
      } catch (err) {
        console.error(err)
      }
      console.log("测试结束");
    },
    async getHistory(){
      const API_URL = 'http://8.130.119.249:14103/api/v1/TradeManagement/Getorder'
      const traderId = '450303200308052030'
      try {
        const queryString = `?TraderID=${traderId}`
        const url = API_URL + queryString
        const response = await axios.get(url)
        console.log(response)
      } catch (error) {
        console.error(error)
      }
    },
    async getFeature(){
      console.log("getFeature")
      const apiUrl = "http://8.130.119.249:14105/get_fund_info";

      const response = await axios.get(apiUrl, {
        params: {
          fund_code: this.StockID
        }
      });
      this.Alpha = response.data.Alpha
      this.Beta = response.data.Beta
      this.Sharpe = response.data.Sharpe
      const data = Object.assign({}, response.data);
      this.averageRiskReturn = data['平均风险收益率(%)'];
      this.annualizedReturn = data['年化收益率(%)'];
    },
    transformScore(stars) {
      //星级映射表
      const map = {
        '★★★★★': 5,
        '★★★★': 4,
        '★★★': 3,
        '★★': 2,
        '★': 1,
      }

      return map[stars]
    }
  },
  mounted() {
    // this.getNavData(this.StockID)
    this.AI_predict(this.array_data)
    // this.getHistory()
    this.getFeature()
    this.getScore()
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
    <el-row>
      <el-col :span="8">
        <div class = "prediction"> 当前基金评价：
          <el-rate v-model="score"></el-rate>
        </div>
        <el-card class="box-card">
          <template #header>
            <div class="card-header">
              <span>基金指标分析</span>
            </div>
          </template>
          <div class="text item">{{'Alpha:' + this.Alpha }}</div>
          <div class="text item">{{'Beta:' + this.Beta }}</div>
          <div class="text item">{{'Sharpe:' + this.Sharpe }}</div>
          <div class="text item">{{'平均风险收益率(%):' + this.averageRiskReturn }}</div>
          <div class="text item">{{'年化收益率(%):' + this.annualizedReturn }}</div>
        </el-card>
      </el-col>
      <el-col ::span="16">

      </el-col>
    </el-row>
  </el-main>
</template>

<style>
.prediction{
  margin-top:50px;
  margin-left:30px;
}
.box-card{
  margin-top:100px;
  margin-left:30px;
  width:400px;
}
</style>