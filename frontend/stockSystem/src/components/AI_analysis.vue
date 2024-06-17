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
      color:['#d8fcfa', '#a9d6fd', '#d8fcfa', '#a9d6fd', '#d8fcfa'],
      array_data:[1.8233, 1.8125, 1.8312, 1.8305, 1.8098, 1.8198, 1.7881, 1.7687, 1.776, 1.7938, 1.825, 1.837, 1.8058, 1.7871, 1.7994, 1.8119999999999998, 1.8027, 1.7705, 1.7839, 1.8126, 1.8068, 1.8172, 1.819, 1.8045, 1.7932, 1.7574, 1.7691, 1.7242, 1.6955, 1.714, 1.7235, 1.7327, 1.7244, 1.7289, 1.7548, 1.7489, 1.7269, 1.7357, 1.6980000000000002, 1.6959, 1.682, 1.695, 1.6851],
      StockID:this.stockId,
      Alpha:'',
      Beta:'',
      Sharpe:'',
      averageRiskReturn:'',
      annualizedReturn:'',
      score:null,
      predictNav:[],
      allData:[]
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
    async getScore() {
      const apiUrl = 'http://localhost:14105/predict'
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
      const apiUrl = "http://localhost:14105/get_fund_info";
      const queryString = `?fund_code=${this.StockID}`
      const url = apiUrl + queryString
      console.log(url)
      const response = await axios.get(url);
      console.log(response)
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
    initRadarChart(){
      let radarChart = echarts.init(this.$refs.radarChart)
      let options={
        title:{
          text:"基金能力五维显示",
          style: {
            textAlign: "center"
          }
        },
        radar:[{
          indicator:[
            {name:"Alpha",max:0.005597,min:-0.00567},
            {name:"Beta",max:2.732936225,min:-4.79113},
            {name:"Sharpe",max:5.101323,min:-0.073011285},
            {name:"平均风险收益率(%)",max:1.082191 ,min:-0.58113},
            {name:"年化收益率(%)",max:5061.553026 ,min:-87.90064599}
          ],
          axisName:{
            fontSize:14,
            color:'#8d0000'
          },
          splitArea:{
            areaStyle:{
              color:this.color
            }
          }
        }],
        series:[
          {
            type:"radar",
            data:[
              {
                value:[Number(this.Alpha),Number(this.Beta),Number(this.Sharpe),Number(this.averageRiskReturn),Number(this.annualizedReturn)]
              }
            ],
            areaStyle:{
              color:'#ef3939'
            }
          }
        ]
      }
      radarChart.setOption(options)
    }
  },
  mounted() {
    this.getNavData(this.StockID)
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
        <el-card class="radar">
          <div class="predictionStock">{{this.StockID}}</div>
          <div class="prediction"> 当前基金评价：
            <el-rate v-model="score"></el-rate>
          </div>
          <el-divider></el-divider>
          <div ref="radarChart" id="radarChart"></div>
        </el-card>
      </el-col>
      <el-col :span="4"></el-col>
      <el-col :span="12">
        <el-card class="predictChart">
          <div>在基金评价中，以下指标用于评估基金的绩效和风险：</div>
          <div>
            <div class="expressionTitle">
              Alpha：
            </div>
            <div>
              Alpha表示基金的超额收益，是指基金在扣除市场基准收益后的剩余收益。
              正的Alpha值意味着基金的表现优于市场基准，而负的Alpha值则意味着基金表现逊于市场基准。
              Alpha通常用来衡量基金经理的选股能力和投资决策的效果。
            </div>
          </div>
          <div>
            <div class="expressionTitle">
              Beta：
            </div>
            <div>
              Beta用于衡量基金相对于市场基准的波动性。
              Beta值为1表示基金的波动性与市场基准一致；Beta值大于1表示基金波动性大于市场，意味着基金的风险较高；Beta值小于1表示基金波动性小于市场，意味着基金的风险较低。
              Beta值帮助投资者了解基金对市场波动的敏感度。
              通过综合使用这些指标，投资者可以更全面地了解基金的风险、收益以及基金经理的表现，从而做出更明智的投资决策。
            </div>
          </div>
          <div>
            <div class="expressionTitle">
              Sharpe Ratio（夏普比率）：
            </div>
            <div>
              Sharpe比率用于衡量基金每单位风险所获得的超额回报。
              公式为：(基金的平均收益率 - 无风险利率) / 基金的标准差。
              较高的Sharpe比率表示基金在承担相同风险的情况下，提供了更高的回报，反之亦然。
            </div>
          </div>
          <div>
            <div class="expressionTitle">
              平均风险收益率(%)：
            </div>
            <div>
              平均风险收益率是基金在一段时间内的平均收益率，通常以年化百分比表示。
              它可以用来衡量基金在特定风险水平下的表现。
            </div>
          </div>
          <div>
            <div class="expressionTitle">
              年化收益率(%)：
            </div>
            <div>
              年化收益率是将基金的总收益率转换为年度收益率，以便于不同时间段之间的比较。
              公式为：年化收益率 = (1 + 总收益率)^(1/年数) - 1。
            </div>
          </div>
        </el-card>
      </el-col>
    </el-row>
  </el-main>
</template>

<style>
.prediction {
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
.radar{
  width:550px
}
.predictChart{
  width:500px;
}
.prediction{
  font-size:30px;
}
.predictionStock{
  text-align:center;
  font-size:20px;
}

</style>