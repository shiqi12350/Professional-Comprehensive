<template>
  <el-card>
    <template #header>
      <el-row>
        <el-col :span="2">
          <div class="tradeHistoryTitle">
            <img src="@/resource/tradeHistory.png" class="tradeHistoryPic">
          </div>
        </el-col>
        <el-col :span="22">
          <div class="tradeHistoryName">交易历史</div>
        </el-col>
      </el-row>
    </template>
    <div>
      <div class="tradeHistoryTableName">申购历史</div>
      <el-table
          v-if="currentPageOrdersType1.length > 0"
          :data="currentPageOrdersType1"
          style="margin-bottom: 20px"
          border
          stripe
      >
        <el-table-column prop="ordernumber" label="订单号"></el-table-column>
        <el-table-column prop="company" label="基金代码"></el-table-column>
        <el-table-column prop="share" label="申购份额"></el-table-column>
        <el-table-column prop="SubmissionAmount" label="下单金额"></el-table-column>
        <el-table-column prop="SubmissionTime" label="提交日期"></el-table-column>
        <el-table-column prop="ConfirmTime" label="确认日期"></el-table-column>
        <el-table-column fixed="right" label="操作" >
          <template #default="scope">
            <el-button @click="handleRevocationPurchase(scope.row)" type="text" size="mini">撤销</el-button>
            <el-button @click="handleRedeem(scope.row)" type="text" size="small">赎回</el-button>
          </template>
        </el-table-column>
      </el-table>

      <el-pagination
          @current-change="handleCurrentChangeType1"
          :current-page="currentPageType1"
          :page-size="5"
          :total="totalOrdersType1.length"
          layout="prev, pager, next"
      ></el-pagination>
      <br>
      <div  class="tradeHistoryTableName">赎回历史</div>
      <el-table
          v-if="currentPageOrdersType2.length > 0"
          :data="currentPageOrdersType2"
          style="margin-bottom: 20px"
          border
          stripe
      >
        <el-table-column prop="ordernumber" label="订单号"></el-table-column>
        <el-table-column prop="company" label="基金代码"></el-table-column>
        <el-table-column prop="share" label="赎回份额"></el-table-column>
        <el-table-column prop="SubmissionAmount" label="赎回金额"></el-table-column>
        <el-table-column prop="SubmissionTime" label="提交日期"></el-table-column>
        <el-table-column prop="ConfirmTime" label="确认日期"></el-table-column>
        <el-table-column fixed="right" label="操作" >
          <template #default="scope">
            <el-button @click="handleRevocationRedeem(scope.row)" type="text" size="small">撤销</el-button>
          </template>
        </el-table-column>
      </el-table>

      <el-pagination
          @current-change="handleCurrentChangeType2"
          :current-page="currentPageType2"
          :page-size="5"
          :total="totalOrdersType2.length"
          layout="prev, pager, next"
      ></el-pagination>
    </div>
  </el-card>

  </template>
  
  <script>
  import axios from 'axios';
  
  export default {
    data() {
      return {
        totalOrdersType1: [],
        totalOrdersType2: [],
        currentPageOrdersType1: [],
        currentPageOrdersType2: [],
        currentPageType1: 1,
        currentPageType2: 1,
        userID: '450303200308052030',
        apiUrl: 'http://8.130.119.249:14103/api/v1/TradeManagement/Getorder'
      };
    },
    created() {
      this.fetchTradeHistory();
    },
    methods: {
      fetchTradeHistory() {
        this.totalOrdersType1 = []
        this.totalOrdersType2 = []
        this.currentPageOrdersType1 = []
        this.currentPageOrdersType2 = []
        axios.get(this.apiUrl, {
          params: {
            TraderID: this.userID
          }
        })
        .then(response => {
          console.log("response")
          console.log(response.data)
          this.totalOrdersType1 = response.data.filter(order => (order.ordertype === '1' && order.orderstate !== 0));
          this.totalOrdersType2 = response.data.filter(order => (order.ordertype === '2' && order.orderstate !== 0));

          this.paginateOrders();
        })
        .catch(error => {
          console.error('Error fetching trade history:', error);
        });
      },
      paginateOrders() {
        const startIndexType1 = (this.currentPageType1 - 1) * 5;
        this.currentPageOrdersType1 = this.totalOrdersType1.slice(startIndexType1, startIndexType1 + 5);
  
        const startIndexType2 = (this.currentPageType2 - 1) * 5;
        this.currentPageOrdersType2 = this.totalOrdersType2.slice(startIndexType2, startIndexType2 + 5);
      },
      handleCurrentChangeType1(page) {
        this.currentPageType1 = page;
        this.paginateOrders();
      },
      handleCurrentChangeType2(page) {
        this.currentPageType2 = page;
        this.paginateOrders();
      },
      async handleRevocationPurchase(row) {
        console.log(row)
        this.$confirm('此操作将撤销已购买/赎回基金!', '提示', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'warning',
        }).then(async () => {
              const api_url = "http://8.130.119.249:14103/api/v1/TradeManagement/CancelTrade"
              const queryString = `?Ordernum=${row.ordernumber}&OrderType=1`
              const url = api_url + queryString
              console.log(url)
              const response = await axios.post(url)
              console.log(response)
              if(response.status === 200) {
                this.$message({
                  type: 'success',
                  message: '撤销成功!'
                })
              }
        }).catch(() => {
              this.$message({
                type: 'info',
                message: '已取消操作',
              })
            })
        this.fetchTradeHistory()
      },
      async handleRevocationRedeem(row) {
        console.log(row)
        this.$confirm('此操作将撤销已购买/赎回基金!', '提示', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'warning',
        }).then(async () => {
          const api_url = "http://8.130.119.249:14103/api/v1/TradeManagement/CancelTrade"
          const queryString = `?Ordernum=${row.ordernumber}&OrderType=2`
          const url = api_url + queryString
          console.log(url)
          const response = await axios.post(url)
          console.log(response)
          if(response.status === 200) {
            this.$message({
              type: 'success',
              message: '撤销成功!'
            })
          }
        }).catch(() => {
          this.$message({
            type: 'info',
            message: '已取消操作',
          })
        })
        this.fetchTradeHistory()
      },
      async handleRedeem(row){
        this.$confirm('此操作将赎回已购买基金!', '提示', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'warning',
        })
            .then(async () => {
              const api_url = "http://8.130.119.249:14103/api/v1/TradeManagement/Redemption"
              const queryString = `?Ordernum=${row.ordernumber}`
              const url = api_url + queryString
              const response = await axios.post(url)
              console.log(response)
              if(response.status === 200) {
                this.$message({
                  type: 'success',
                  message: '赎回成功!'
                })
              }
            })
            .catch(() => {
              this.$message({
                type: 'info',
                message: '已取消操作',
              })
            })
        this.fetchTradeHistory()
      }
    }
  };
  </script>

<style>
.tradeHistoryPic{
  width:40px;
  margin-top:10px;
  margin-left:10px;
}
.tradeHistoryName{
  margin-top:15px;
  font-size:30px;
}
.tradeHistoryTitle{
  width:250px;
  height:60px;
  background-color: #fff;
}
.tradeHistoryTableName{
  font-size:20px;
  margin-left:15px;
  margin-bottom:5px;
}
</style>