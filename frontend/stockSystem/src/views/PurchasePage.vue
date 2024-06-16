<script>
import * as echarts from "echarts";
import {useRouter} from "vue-router";
import axios from "axios";
import Md5 from 'js-md5'
import { useStockInfoStore } from '@/stores/stockInfoStore.js'

export default{
  methods:{
    async getStockData(stockID){
      console.log("获取基金信息")
      const response = await axios.post(
          "http://api.tushare.pro",
          {
            api_name: "fund_basic",
            token: "91a073e655c1849334691d5f5c71a518ff5468891554887fad2afca0",
            params:{"ts_code":stockID}
          }
      );
      console.log(this.ts_code)
      console.log(response)
      this.stockName = response.data.data.items[0][1]
      this.fund_type = response.data.data.items[0][4]
      this.list_date = response.data.data.items[0][7]
      this.issue_date = response.data.data.items[0][8]
      this.issue_amount = response.data.data.items[0][10]
      this.min_amount = response.data.data.items[0][15]
      this.purc_startdate = response.data.data.items[0][22]
      this.redm_startdate = response.data.data.items[0][23]
      const response2 = await axios.post(
          "http://api.tushare.pro",
          {
            api_name: "fund_share",
            token: "91a073e655c1849334691d5f5c71a518ff5468891554887fad2afca0",
            params:{"ts_code":stockID}
          }
      );
      console.log(response2)
    },
    async getNavData(StockID){
      const response = await axios.post(
          "http://api.tushare.pro",
          {
            api_name: "fund_nav",
            token: "91a073e655c1849334691d5f5c71a518ff5468891554887fad2afca0",
            params:{"ts_code":StockID,}
          }
      );
      for( let i = 6; i >=0 ; i--){
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
    async purchase(){
      console.log("调用购买")
      // const params = {
      //   TraderID:String(this.form.IDnumber),
      //   fundID:String(this.ts_code),
      //   tradeAmount:Number(this.form.tradeAmount),
      //   cardID:String(this.form.cardID),
      //   passWord:Md5(this.form.passWord,32)
      // }
      let queryString;
      let api_url;
      if(this.investWay==="purchase"){
        queryString = `?TraderID=${this.form.IDnumber}&fundID=${this.ts_code}&tradeAmount=${this.form.tradeAmount}&cardID=${this.form.cardID}&passWord=${Md5(this.form.passWord,32)}`
        api_url = "http://8.130.119.249:14103/api/v1/TradeManagement/Subscription"
      } else if(this.detailedInvestWay ==="weekly"){
        queryString = `?TraderID=${this.form.IDnumber}&fundID=${this.ts_code}&tradeAmount=${this.form.tradeAmount}&cardID=${this.form.cardID}&passWord=${Md5(this.form.passWord,32)}&cronExpression=0+0+0+?+*+${this.investWeeklyWay}`
        api_url = "http://8.130.119.249:14103/api/v1/TradeManagement/schedule"
      } else if(this.detailedInvestWay ==="monthly"){
        queryString = `?TraderID=${this.form.IDnumber}&fundID=${this.ts_code}&tradeAmount=${this.form.tradeAmount}&cardID=${this.form.cardID}&passWord=${Md5(this.form.passWord,32)}&cronExpression=0+0+0+${this.investMonthlyWay}+*+?`
        api_url = "http://8.130.119.249:14103/api/v1/TradeManagement/schedule"
      }
      console.log(Md5(this.form.passWord,32))
      const url = api_url+queryString
      console.log(url)
      const response = await axios.post(url)
      console.log(response)
      if(response.status === 200) {
        this.$message({
          message: '购买成功',
          type: 'success'
        });
        this.form.IDnumber = ''
        this.form.tradeAmount = ''
        this.form.cardID = ''
        this.form.passWord = ''
        this.investWay = 'purchase'
        this.detailedInvestWay = ''
        this.investWeeklyWay = ''
        this.investMonthlyWay = ''
      } else {
        this.$message.error('购买失败')
        this.form.IDnumber = ''
        this.form.tradeAmount = ''
        this.form.cardID = ''
        this.form.passWord = ''
        this.investWay = 'purchase'
        this.detailedInvestWay = ''
        this.investWeeklyWay = ''
        this.investMonthlyWay = ''
      }
    },
    initChart() {
      let NavChart = echarts.init(this.$refs.NavChart)
      let xData = this.nav_date
      let data = this.total_netasset
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
      NavChart.setOption(options)
    },

  },
  data() {
    return {
      userName: '',
      SearchStock:'',
      ifSubscribe: '开放申购',
      ifRedeem: '开放赎回',
      ts_code: this.stockId,
      stockName: '',
      fund_type: '',
      list_date: '',
      issue_date: '',
      issue_amount: '',
      min_amount: '',
      purc_startdate: '',
      redm_startdate: '',
      form: {
        IDnumber: '',
        tradeAmount: '',
        cardID: '',
        passWord: '',
      },
      investWay: 'purchase',
      detailedInvestWay: '',
      investWeeklyWay:'',
      investMonthlyWay:'',
      NavList: [],
      nav_date: [],
      total_netasset: [],
      date:'',
      options:[
        {
          value:'weekly',
          label:"每周定投"
        },
        {
          value:'monthly',
          label:"每月定投"
        },
      ],
      weeklyOption:[
        {
          value:'MON',
          label:"周一"
        },
        {
          value:'TUE',
          label:"周二"
        },
        {
          value:'WED',
          label:"周三"
        },
        {
          value:'THU',
          label:"周四"
        },
        {
          value:'FRI',
          label:"周五"
        },
        {
          value:'SAT',
          label:"周六"
        },
        {
          value:'SUN',
          label:"周日"
        },
      ],
    }
  },
  mounted() {
    this.getStockData(this.ts_code)
    this.getNavData(this.ts_code)
  },
  setup() {
    const stockInfoStore = useStockInfoStore()
    return {
      stockId: stockInfoStore.stockId,
    }
  }
}
</script>

<template>
  <el-card>
    <template #header>
      <el-row>
        <el-col :span="2">
          <div class="purchaseTitle">
            <img src="@/resource/purchase.png" class="purchasePic">
          </div>
        </el-col>
        <el-col :span="22">
          <div class="purchaseName">基金购买</div>
        </el-col>
      </el-row>
    </template>
    <el-container>
      <el-main>
        <el-row>
          <el-col :span="8">
            <el-descriptions
                :title="'当前基金名:' + stockName"
                :column="2">
              <el-descriptions-item label="基金代码">{{ this.ts_code }}</el-descriptions-item>
              <el-descriptions-item label="投资类型">{{ this.fund_type }}</el-descriptions-item>
              <el-descriptions-item label="上市时间">{{ this.list_date }}</el-descriptions-item>
              <el-descriptions-item label="发行日期">{{ this.issue_date }}</el-descriptions-item>
              <el-descriptions-item label="发行份额(亿)">{{ this.issue_amount }}</el-descriptions-item>
              <el-descriptions-item label="起点金额(万元)">{{ this.min_amount }}</el-descriptions-item>
              <el-descriptions-item label="日常申购起始日">{{ this.purc_startdate }}</el-descriptions-item>
              <el-descriptions-item label="日常赎回起始日">{{ this.redm_startdate }}</el-descriptions-item>
            </el-descriptions>
            <div ref="NavChart" id="NavChart"></div>
          </el-col>
          <el-col :span="16">
            <el-form class="investForm" ref="form" :model="form" label-width="350px">
              <el-form-item label="交易人身份证号码">
                <el-row>
                  <el-input v-model="form.IDnumber"></el-input>
                </el-row>
              </el-form-item>
              <el-form-item label="交易金额">
                <el-row>
                  <el-input v-model="form.tradeAmount"></el-input>
                </el-row>
              </el-form-item>
              <el-form-item label="交易银行卡号">
                <el-row>
                  <el-input v-model="form.cardID"></el-input>
                </el-row>
              </el-form-item>
              <el-form-item label="银行卡密码">
                <el-row>
                  <el-input v-model="form.passWord"></el-input>
                </el-row>
              </el-form-item>
              <el-form-item label="投资方式">
                <el-radio v-model="investWay" label="purchase">购买</el-radio>
                <el-radio v-model="investWay" label="invest">定投</el-radio>
              </el-form-item>
              <el-form-item label="选择定投方法" v-if="investWay === 'invest'">
                <el-select
                    v-model="detailedInvestWay"
                    placeholder="请选择"
                >
                  <el-option
                      v-for="item in options"
                      :key="item.value"
                      :label="item.label"
                      :value="item.value">
                  </el-option>
                </el-select>
              </el-form-item>
              <el-form-item label="具体定投日期" v-if="detailedInvestWay === 'weekly'">
                <el-select
                    v-model="investWeeklyWay"
                    placeholder="请选择"
                >
                  <el-option
                      v-for="item in weeklyOption"
                      :key="item.value"
                      :label="item.label"
                      :value="item.value">
                  </el-option>
                </el-select>
              </el-form-item>
              <el-form-item label="具体定投日期" v-if="detailedInvestWay === 'monthly'">
                <el-row>
                  <el-col :span="4">
                    每月
                  </el-col>
                  <el-col :span="12">
                    <el-input v-model="investMonthlyWay" v-if="detailedInvestWay === 'monthly'"  placeholder="请输入日期">
                    </el-input>
                  </el-col>
                  <el-col :span="8">
                    日
                  </el-col>
                </el-row>
              </el-form-item>
              <el-form-item>
                <el-button type="primary" @click="purchase">创建订单</el-button>
                <el-button>取消</el-button>
              </el-form-item>
            </el-form>
          </el-col>
        </el-row>
      </el-main>
    </el-container>
  </el-card>

</template>

<style>
#NavChart {
  width: 500px;
  height: 350px;
}

.investForm {
  margin-top: 50px;
}
.purchasePic{
  width:40px;
  margin-top:10px;
  margin-left:10px;
}
.purchaseName{
  margin-top:15px;
  font-size:30px;
}
.purchaseTitle{
  width:250px;
  height:60px;
  background-color: #fff;
}
</style>