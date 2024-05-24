<script>
import * as echarts from "echarts";

export default{
  data(){
    return{
      userName:'',
      stockName:'11',
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
}
</script>

<template>
  <el-container>
    <el-header>
      <el-row>
        <el-col :span="16">
          <el-page-header @back="goBack" content="基金交易系统"></el-page-header>
        </el-col>
        <el-col :span="8">
          {{this.userName}}
        </el-col>
      </el-row>
    </el-header>

    <el-main>
      <el-row>
        <el-col :span="16">
          <div class="stockBriefInfo">
            <div>当前基金：{{this.stockName}}</div>
            <el-row>
              <el-col :span="15">
                <el-row>
                  <el-col :span="12">
                    <div>单位净值：</div>
                    <div>2.3882</div>
                  </el-col>
                  <el-col :span="12">
                    <div>累计净值：</div>
                    <div>2.4082</div>
                  </el-col>
                </el-row>
              </el-col>
              <el-col :span="9">
                <el-descriptions  >
                  <el-descriptions-item label="类型">{{ this.stockCategory }}</el-descriptions-item>
                  <el-descriptions-item label="规模">{{ this.stockScale }}</el-descriptions-item>
                  <el-descriptions-item label="成立日">{{ this.establishmentDate }}</el-descriptions-item>
                  <el-descriptions-item label="管理人">{{ this.custodian }}</el-descriptions-item>
                </el-descriptions>
              </el-col>
            </el-row>
            <el-row>
              <el-col :span="8">
                <div>近一月</div>
                <div>11.23%</div>
              </el-col>
              <el-col :span="8">
                <div>近三月</div>
                <div>34.44%</div>
              </el-col>
              <el-col :span="8">
                <div>近一年</div>
                <div>14.08%</div>
              </el-col>
            </el-row>
          </div>
        </el-col>
        <el-col :span="8">
          <div class="purchaseForm">
            <el-row>
              <el-col :span="8">
                <div>交易状态:</div>
              </el-col>
              <el-col :span="8">
                <div>{{this.ifSubscribe}}</div>
              </el-col>
              <el-col :span="8">
                <div>{{this.ifRedeem}}</div>
              </el-col>
            </el-row>
            <el-row>
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
            <el-row>
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
          <el-table  :data="tableData">
            <el-table-column prop="date" label="日期" > </el-table-column>
            <el-table-column prop="unitNetWorth" label="单位净值" > </el-table-column>
            <el-table-column prop="increaseRate" label="增长率" > </el-table-column>
          </el-table>
        </el-col>
      </el-row>
    </el-main>
    <router-link to="/">back</router-link>
  </el-container>
</template>

<style scoped>
.stockBriefInfo{
  width:800px;
  height:300px;
  border:1px solid red;
}
.purchaseForm{
  width:400px;
  height:300px;
  border:1px solid red;
}
#stockInfoChart{
  width:500px;
  height:500px;
  border:1px solid red;
}
</style>