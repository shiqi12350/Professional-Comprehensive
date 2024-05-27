<script>
import * as echarts from "echarts";
import {useRouter} from "vue-router";

export default{
  data(){
    return{
      userName:'',
      stockName:'汇丰晋信',
      ifSubscribe:'开放申购',
      ifRedeem:'开放赎回',
      stockCategory:'股票型',
      stockScale:4.27,
      establishmentDate:'2018-10-22',
      custodian:'noname',
      tableData:[
        {
          date:'4-13',
          unitNetWorth:0.8353,
          increaseRate:'0.47%',
        },
        {
          date:'4-14',
          unitNetWorth:0.8523,
          increaseRate:'0.81%',
        },
        {
          date:'4-15',
          unitNetWorth:0.8499,
          increaseRate:'1.92%',
        },
        {
          date:'4-16',
          unitNetWorth:0.8218,
          increaseRate:'-3.31%',
        },
        {
          date:'4-17',
          unitNetWorth:0.8376,
          increaseRate:'-0.28%',
        },
        {
          date:'4-18',
          unitNetWorth:0.8444,
          increaseRate:'0.81%',
        },
        {
          date:'4-19',
          unitNetWorth:0.8460,
          increaseRate:'0.19%',
        }
      ]
    }
  },
  mounted(){
    let StockChart = echarts.init(this.$refs.stockInfoChart)
    let xData = ['5-14','5-15','5-16','5-17','5-18','5-19','5-20']
    let data = [0.8460,0.8444,0.8376,0.8218,0.8499,0.8523,0.8353]
    let options={
      xAxis:{
        type:"category",
        data:xData
      },
      yAxis:{
        type:"value"
      },
      series:[{
        data,
        type:"line"
      }]
    };
    StockChart.setOption(options)
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
        <el-col :span="16">
          <div class="stockBriefInfo">
            <div class="stockInfoTitle">当前基金：{{this.stockName}}</div>
            <el-row>
              <el-col :span="15">
                <el-row class="netWorthInfo">
                  <el-col :span="12">
                    <div class="netWorthName">单位净值：</div>
                    <div class="netWorthData">2.3882</div>
                  </el-col>
                  <el-col :span="12">
                    <div class="netWorthName">累计净值：</div>
                    <div class="netWorthData">2.4082</div>
                  </el-col>
                </el-row>
              </el-col>
              <el-col :span="9">
                <el-descriptions
                    :column="1">
                  <el-descriptions-item label="类型">{{ this.stockCategory }}</el-descriptions-item>
                  <el-descriptions-item label="规模">{{ this.stockScale }}</el-descriptions-item>
                  <el-descriptions-item label="成立日">{{ this.establishmentDate }}</el-descriptions-item>
                  <el-descriptions-item label="管理人">{{ this.custodian }}</el-descriptions-item>
                </el-descriptions>
              </el-col>
            </el-row>
            <el-row>
              <el-col :span="5">
                <div class="vagueTime">近一月</div>
                <div class="vagueRate">11.23%</div>
              </el-col>
              <el-col :span="5">
                <div class="vagueTime">近三月</div>
                <div class="vagueRate">34.44%</div>
              </el-col>
              <el-col :span="5">
                <div class="vagueTime">近一年</div>
                <div class="vagueRate">14.08%</div>
              </el-col>
            </el-row>
          </div>
        </el-col>
        <el-col :span="8">
          <div class="purchaseForm">
            <el-row>
                <el-col :span="8">
                  <div class="state">交易状态:</div>
                </el-col>
                <el-col :span="8">
                  <div class="openSubscribe">{{this.ifSubscribe}}</div>
                </el-col>
                <el-col :span="8">
                  <div class="openRedeem">{{this.ifRedeem}}</div>
                </el-col>
            </el-row>
            <el-row class="purchaseInput">
              <el-col :span="5">
                <div>金额：</div>
              </el-col>
              <el-col :span="12">
                <el-input v-model="input" placeholder="请输入内容"></el-input>
              </el-col>
              <el-col :span="7">
                <div>   元</div>
              </el-col>
            </el-row>
            <el-row class="purchaseOperation">
              <el-col :span="12">
                <el-button type="danger">购买</el-button>
              </el-col>
              <el-col :span="12">
                <el-button type="primary">定投</el-button>
              </el-col>
            </el-row>
          </div>
        </el-col>
      </el-row>
      <el-row>
        <el-col :span="15">
          <div ref="stockInfoChart" id="stockInfoChart"></div>
        </el-col>
        <el-col :span="9">
          <el-table
              :data="tableData">
            <el-table-column prop="date" label="日期" > </el-table-column>
            <el-table-column prop="unitNetWorth" label="单位净值" > </el-table-column>
            <el-table-column prop="increaseRate" label="增长率" > </el-table-column>
          </el-table>
        </el-col>
      </el-row>
    </el-main>
  </el-container>
</template>

<style scoped>
.stockBriefInfo{
  width:600px;
  height:230px;
  border:1px solid red;
  margin-bottom: 10px;
}
.purchaseForm{
  width:350px;
  height:230px;
  border:1px solid red;
}
#stockInfoChart{
  width:500px;
  height:300px;
  border:1px solid red;
}
.stockInfoTitle{
  margin-left:30px;
  font-size:30px;
}
.netWorthInfo{
  margin-left:20px;
  margin-top:40px;
}
.netWorthName{
  font-size:20px;
}
.netWorthData{
  margin-left:15px;
  color:red;
}
.vagueTime{
  margin-left:20px;
  font-size:20px;
}
.vagueRate{
  margin-left:25px;
  margin-bottom:30px;
  color:red;
}
.state{
  margin-left:20px;
  margin-top:10px;
}
.openSubscribe{
  margin-top:10px;
  color:red;
}
.openRedeem{
  margin-top:10px;
  color:blue;
}
.purchaseInput{
  margin-top:30px;
  margin-left:20px;
}
.purchaseOperation{
  margin-top:20px;
  margin-left:40px;
}
</style>