<script>
import axios from "axios";
import {useStockInfoStore} from "@/stores/stockInfoStore.js";

export default{
  setup(){
    const stockInfoStore = useStockInfoStore()
    return {
      stockId: stockInfoStore.stockId
    }
  },
  data(){
    return{
      openChecked: true,
      closeChecked: false,
      changeChecked: false,
      pct_chgChecked: false,
      volChecked: false,
      amountChecked: false,
      date:"20240607",
      rankList:[],
      sortedList:[],
      StockID:this.stockId,
      trade_date:"",
      open:"",
      close:"",
      change:"",
      pct_chg:"",
      vol:"",
      amount:"",
    }
  },
  methods:{
    async getData(ts_code,trade_date){
      const response = await axios.post(
          "http://api.tushare.pro",
          {
            api_name: "fund_daily",
            token: "91a073e655c1849334691d5f5c71a518ff5468891554887fad2afca0",
            params:{"trade_date":trade_date}
          }
      );
      for(let i = 0;i < 1000;i++)
      {
        const rankData = response.data.data.items[i]
        const data = {
          ts_code: rankData[0],
          trade_date: rankData[1],
          open:rankData[2],
          close:rankData[5],
          change:rankData[7],
          pct_chg:rankData[8],
          vol:rankData[9],
          amount:rankData[10],
        }
        this.rankList.push(data);
      }
      console.log(this.rankList)
      const response2 = await axios.post(
          "http://api.tushare.pro",
          {
            api_name: "fund_daily",
            token: "91a073e655c1849334691d5f5c71a518ff5468891554887fad2afca0",
            params:{"ts_code":ts_code}
          }
      );
      console.log(response)
      this.trade_date = response2.data.data.items[0][1]
      this.open = response2.data.data.items[0][2]
      this.close = response2.data.data.items[0][5]
      this.change = response2.data.data.items[0][7]
      this.pct_chg = response2.data.data.items[0][8]
      this.vol = response2.data.data.items[0][9]
      this.amount = response2.data.data.items[0][10]
      this.sortBy('open')
    },
    indexMethod(index) {
      return index + 1
    },
    sortBy(field) {
      Object.keys(this).forEach(key => {
        if(key.includes('Checked')) {
          this[key] = false
        }
      })

      // 根据field动态设置tag
      let name = field + 'Checked'
      this[name] = true
      // 拷贝一份数据
      this.sortedList = [...this.rankList]

      this.sortedList.sort((a, b) => {
        return b[field] - a[field]
      })

      this.sortedList = this.sortedList.slice(0, 10)
    }
  },
  mounted(){
    this.getData(this.StockID,this.date)
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
    <el-descriptions title="当前基金信息" direction="vertical" :column="8" border>
      <el-descriptions-item label="基金代码">{{this.StockID}}</el-descriptions-item>
      <el-descriptions-item label="交易日期">{{this.trade_date}}</el-descriptions-item>
      <el-descriptions-item label="开盘价(元)">{{this.open}}</el-descriptions-item>
      <el-descriptions-item label="收盘价(元)">{{this.close}}</el-descriptions-item>
      <el-descriptions-item label="涨跌额(元)">{{this.change}}</el-descriptions-item>
      <el-descriptions-item label="涨跌幅(%)">{{this.pct_chg}}</el-descriptions-item>
      <el-descriptions-item label="成交量(手)">{{this.vol}}</el-descriptions-item>
      <el-descriptions-item label="成交额(千元)">{{this.amount}}</el-descriptions-item>
    </el-descriptions>
    <br>
    <el-check-tag @click="sortBy('open')"  :checked="openChecked" >按开盘价</el-check-tag>
    <el-check-tag @click="sortBy('close')"  :checked="closeChecked" >按收盘价</el-check-tag>
    <el-check-tag @click="sortBy('change')" :checked="changeChecked" >按涨跌额</el-check-tag>
    <el-check-tag @click="sortBy('pct_chg')" :checked="pct_chgChecked" >按涨跌幅</el-check-tag>
    <el-check-tag @click="sortBy('vol')" :checked="volChecked" >按成交量</el-check-tag>
    <el-check-tag @click="sortBy('amount')" :checked="amountChecked">按成交额</el-check-tag>
    <el-table
      :data="sortedList">
      <el-table-column type="index" :index="indexMethod"> </el-table-column>
      <el-table-column prop="ts_code" label="基金代码" width="150"> </el-table-column>
      <el-table-column prop="trade_date" label="交易日期" width="150"></el-table-column>
      <el-table-column prop="open" label="开盘价(元)" width="100"></el-table-column>
      <el-table-column prop="close" label="收盘价(元)" width="100"></el-table-column>
      <el-table-column prop="change" label="涨跌额(元)" width="100"></el-table-column>
      <el-table-column prop="pct_chg" label="涨跌幅(%)" width="100"></el-table-column>
      <el-table-column prop="vol" label="成交量(手)" width="150"></el-table-column>
      <el-table-column prop="amount" label="成交额(千元)" width="180"></el-table-column>
    </el-table>
  </el-main>
</template>

<style>
.el-check-tag{
  margin-left:50px
}
</style>