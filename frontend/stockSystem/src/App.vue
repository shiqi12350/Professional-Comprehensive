<script setup>
import { RouterLink, RouterView, useRouter} from 'vue-router';
const router = useRouter()
function turnToInvestList() {
  router.push('/investList')
}
function turnToClassificationList() {
  router.push('/classificationList')
}
function turnToStockInfo() {
  router.push('/stockInfo')
}
function turnToPurchasePage() {
  router.push('/purchasePage')
}
function goBack() {
  router.push('/')
}

function turnToUserInfo() {
  router.push('/UserInfo')
}
function turnToTradeHistory() {
  router.push('/TradeHistory')
}
function turnToAccountManage() {
  router.push('/AccountManage')
}
</script>

<template>
  <el-container>
    <el-header>
      <el-row>
        <el-col :span="16">
          <el-page-header @click="goBack" content="基金交易系统"></el-page-header>
        </el-col>
        <el-col :span="8">
          {{"userName"}}
        </el-col>
      </el-row>
    </el-header>
    <el-container>
      <el-aside>
        <el-card class="UserFunc">
          <template #header>
            <div class="card-header">
              <span>用户管理</span>
            </div>
          </template>
          <el-row>
            <el-col :span="12">
              <el-button type="text" class="navButton" @click="turnToUserInfo">信息修改</el-button>
            </el-col>
            <el-col :span="12">
              <el-button type="text" class="navButton" @click="turnToTradeHistory">交易历史查看</el-button>
            </el-col>
            <el-col :span="12">
              <el-button type="text" class="navButton" @click="turnToAccountManage">持仓管理</el-button>
            </el-col>
          </el-row>
        </el-card>
        <el-card class="StockFunc">
          <template #header>
            <div class="card-header">
              <span>基金信息</span>
            </div>
          </template>
          <el-row>
            <el-col :span="12">
              <el-button type="text" class="navButton" @click="turnToInvestList">基金查看</el-button>
            </el-col>
            <el-col :span="12">
              <el-button type="text" class="navButton" @click="turnToClassificationList">分类排行</el-button>
            </el-col>
          </el-row>
        </el-card>
        <el-card class="DetailFunc">
          <template #header>
            <div class="card-header">
              <span>基金研究</span>
            </div>
          </template>
          <el-row>
            <el-col :span="12">
              <el-button type="text" class="navButton" @click="turnToStockInfo">深度资料</el-button>
            </el-col>
            <el-col :span="12">
              <el-button type="text" class="navButton" @click="turnToPurchasePage">基金购买</el-button>
            </el-col>
          </el-row>
        </el-card>
        <el-card class="CalculatorFunc">
          <template #header>
            <div class="card-header">
              <span>基金工具</span>
            </div>
          </template>
          <el-row>
            <el-col :span="12">
              <el-button type="text" class="navButton">收益计算</el-button>
            </el-col>
            <el-col :span="12">
              <el-button type="text" class="navButton">费率计算</el-button>
            </el-col>
          </el-row>
          <el-button type="text" class="navButton">基金比较</el-button>
        </el-card>
      </el-aside>
      <el-main>
        <router-view />
      </el-main>
    </el-container>
  </el-container>

</template>


<style>
.card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  font-weight: bold;
  font-size: 16px;

}
.text {
  font-size: 14px;
}

.item {
  margin-bottom: 18px;
}

.box-card {
  width: 200px;
}
.navButton{
  color: #333;
  padding: 8px 16px;

}
.navButton:hover {
  color:blue;
}
.UserFunc,.StockFunc,.DetailFunc {
  margin-bottom: 10px;
}
</style>