<script>
import axios from "axios";
import * as echarts from "echarts";
import { useStockInfoStore } from '@/stores/stockInfoStore.js'
export  default {
  setup(){
    const stockInfoStore = useStockInfoStore()
    return {
      stockId: stockInfoStore.stockId
    }
  },
  data(){
    return{
      array_data:[],
      StockID:this.stockId,
      Alpha:'',
      Beta:'',
      Sharpe:'',
      averageRiskReturn:'',
      annualizedReturn:'',
      score:null,
      predictNav:[],
    }
  },
  methods:{
    async getNavData(StockID) {
      const response = await axios.post(
          "http://api.tushare.pro",
          {
            api_name: "fund_nav",
            token: "91a073e655c1849334691d5f5c71a518ff5468891554887fad2afca0",
            params: {"ts_code": StockID}
          }
      );
      for (let i = 20; i >= 0; i--) {
        this.array_data.push(response.data.data.items[i][8])
      }
      console.log(this.array_data)
    },
    async AI_predict(array_data) {
      console.log("调用ai预测api")
      console.log(array_data)
      const data = {
        model_name: 1,
        lookback: 20,
        future_days: 7,
        array: [1.8233, 1.8125, 1.8312, 1.8305, 1.8098, 1.8198, 1.7881, 1.7687, 1.776, 1.7938, 1.825, 1.837, 1.8058, 1.7871, 1.7994, 1.8119999999999998, 1.8027, 1.7705, 1.7839, 1.8126, 1.8068, 1.8172, 1.819, 1.8045, 1.7932, 1.7574, 1.7691, 1.7242, 1.6955, 1.714, 1.7235, 1.7327, 1.7244, 1.7289, 1.7548, 1.7489, 1.7269, 1.7357, 1.6980000000000002, 1.6959, 1.682, 1.695, 1.6851],
      };
      const response = await axios.post('http://127.0.0.1:14106/LSTM_prediction', data, {
        headers: {
          'Content-Type': 'application/json'
        }
      });
      this.predictNav = response.data.Prediction
      console.log("预测数字："+this.predictNav[0])
      console.log(response)
      this.initChart()
    },
    async getScore() {
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
    async getFeature() {
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
      this.initRadarChart()
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
    },
    initChart() {
      let PredictChart = echarts.init(this.$refs.PredictChart)
      let xData = ['1','2','3','4','5','6','7']
      let data = [Number(this.predictNav[0]),Number(this.predictNav[1]),Number(this.predictNav[2]),Number(this.predictNav[3]),Number(this.predictNav[4]),Number(this.predictNav[5]),Number(this.predictNav[6])]
      let options = {
        xAxis: {
          type: "category",
          data: xData
        },
        yAxis: {
          type: "value",
          scale: true
        },
        series: [{
          data,
          type: "line",
        }]
      };
      PredictChart.setOption(options)
    },
    initRadarChart(){
      let radarChart = echarts.init(this.$refs.radarChart)
      let options={
        title:{
          text:"五维图"
        },
        radar:[{
          indicator:[
            {name:"Alpha",max:0.001},
            {name:"Beta",max:0.01},
            {name:"Sharpe",max:1},
            {name:"平均风险收益率(%)",max:0.1},
            {name:"年化收益率(%)",max:10}
          ]
        }],
        series:[
          {
            type:"radar",
            data:[
              {
                value:[Number(this.Alpha),Number(this.Beta),Number(this.Sharpe),Number(this.averageRiskReturn),Number(this.annualizedReturn)]
              }
            ]
          }
        ]
      }
      radarChart.setOption(options)
    }
  },
  mounted() {
    this.getNavData(this.StockID)
    this.AI_predict(this.array_data)
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
      <el-col :span="8">
        <div class="prediction"> 当前基金评价：
          <el-rate v-model="score"></el-rate>
        </div>
        <br><br><br><br>
<!--        <el-card class="box-card">-->
<!--          <template #header>-->
<!--            <div class="card-header">-->
<!--              <span>基金指标分析</span>-->
<!--            </div>-->
<!--          </template>-->
<!--          <div class="text item">{{ 'Alpha:' + this.Alpha }}</div>-->
<!--          <div class="text item">{{ 'Beta:' + this.Beta }}</div>-->
<!--          <div class="text item">{{ 'Sharpe:' + this.Sharpe }}</div>-->
<!--          <div class="text item">{{ '平均风险收益率(%):' + this.averageRiskReturn }}</div>-->
<!--          <div class="text item">{{ '年化收益率(%):' + this.annualizedReturn }}</div>-->
<!--        </el-card>-->
        <div ref="radarChart" id="radarChart"></div>
      </el-col>
      <el-col :span="4"></el-col>
      <el-col :span="12">
        <div ref="PredictChart" id="PredictChart"></div>
      </el-col>
    </el-row>
  </el-main>
</template>

<style>
.prediction {
  margin-top: 50px;
  margin-left: 30px;
}

.box-card {
  margin-top: 100px;
  margin-left: 30px;
  width: 400px;
}
#PredictChart{
  width: 500px;
  height: 400px;
}
#radarChart{
  width:500px;
  height:400px;
}
</style>