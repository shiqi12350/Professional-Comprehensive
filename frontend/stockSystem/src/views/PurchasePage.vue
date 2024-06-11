<script>
import * as echarts from "echarts";
import {useRouter} from "vue-router";
import axios from "axios";

export default{
  methods:{
    async getStockData(stockID){
      const response = await axios.post(
          "http://api.tushare.pro",
          {
            api_name: "fund_basic",
            token: "91a073e655c1849334691d5f5c71a518ff5468891554887fad2afca0",
            params:{"ts_code":stockID}
          }
      );
      this.stockName = response.data.data.items[0][1]
      this.fund_type = response.data.data.items[0][4]
      this.list_date = response.data.data.items[0][7]
      this.issue_date = response.data.data.items[0][8]
      this.issue_amount = response.data.data.items[0][10]
      this.min_amount = response.data.data.items[0][15]
      this.purc_startdate = response.data.data.items[0][22]
      this.redm_startdate = response.data.data.items[0][23]
      console.log(response)
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
    initChart(){
      let NavChart = echarts.init(this.$refs.NavChart)
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
        }]
      };
      NavChart.setOption(options)
    },

  },
  data(){
    return{
      userName:'',
      ifSubscribe:'开放申购',
      ifRedeem:'开放赎回',
      ts_code:"562340.SH",
      stockName:'',
      fund_type:'',
      list_date:'',
      issue_date:'',
      issue_amount:'',
      min_amount:'',
      purc_startdate:'',
      redm_startdate:'',
      form:{
        IDnumber:'',
        tradeAmount:'',
        cardID:'',
        passWord:'',
      },
      investWay:'purchase',
      NavList:[],
      nav_date:[],
      total_netasset:[],
    }
  },
  mounted(){
    this.getStockData(this.ts_code)
    this.getNavData(this.ts_code)
  },
  setup() {
    const router = useRouter()
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
  <el-container>
    <el-main>
      <el-row>
        <el-col :span="15"></el-col>
        <el-col :span="6">
          <el-input v-model="input" placeholder="请输入搜索的基金名称"></el-input>
        </el-col>
        <el-col :span="2">
          <el-button>搜索</el-button>
        </el-col>
      </el-row>
      <br>
      <el-row>
        <el-col :span="8">
          <el-descriptions
              :title="'当前基金名:' + stockName"
              :column="2">
            <el-descriptions-item label="基金代码">{{ this.ts_code}}</el-descriptions-item>
            <el-descriptions-item label="投资类型">{{ this.fund_type}}</el-descriptions-item>
            <el-descriptions-item label="上市时间">{{ this.list_date}}</el-descriptions-item>
            <el-descriptions-item label="发行日期">{{ this.issue_date}}</el-descriptions-item>
            <el-descriptions-item label="发行份额(亿)">{{ this.issue_amount}}</el-descriptions-item>
            <el-descriptions-item label="起点金额(万元)">{{ this.min_amount}}</el-descriptions-item>
            <el-descriptions-item label="日常申购起始日">{{ this.purc_startdate}}</el-descriptions-item>
            <el-descriptions-item label="日常赎回起始日">{{ this.redm_startdate}}</el-descriptions-item>
          </el-descriptions>
          <div ref="NavChart" id="NavChart"></div>
        </el-col>
        <el-col :span="16">
          <el-form class ="investForm"  ref="form" :model="form" label-width="350px">
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
            <el-form-item v-if="investWay === 'invest'">
              <el-input v-model="investPlan">定投的方法</el-input>
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
</template>

<style>
#NavChart{
  width:500px;
  height:350px;
}
.investForm{
  margin-top:50px;
}
</style>