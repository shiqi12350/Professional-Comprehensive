<script setup>

import { RouterLink, RouterView, useRouter} from 'vue-router';
import axios from "axios";
import userInfo from '@/stores/user';
import {storeToRefs } from "pinia";
import loginVue from './components/login.vue';
import {onMounted, ref} from 'vue'
import {watch} from 'vue'
import {User,Grid,View,Cellphone} from '@element-plus/icons'

const router = useRouter()

//get vuestore userID to Determine whether to log in or not 
const user = userInfo();
const {userID} = storeToRefs(user);

//登录
const loginIn = ref(false);

const systemTime = ref()

let showLoginPage = function(){
  loginIn.value = !loginIn.value
}
user.$subscribe((args, state) => {
  loginIn.value = !loginIn.value
})

<<<<<<< HEAD
const logout = () => {  
      user.reset();  
       //alert('您已成功退出登录！');  
    };  


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
function turnToPurchaseRate(){
  router.push('/purchaseRate')
}
function turnToRedemptionRate(){
  router.push('/redemptionRate')
}
function turnToComparison(){
  router.push('/comparison')
}
=======
>>>>>>> 1b87634bb98133f625c1d830d2a01209a2926791
async function changeTime(time) {
  const api_url = 'http://8.130.119.249:14103/api/v1/TradeManagement/Modification_time'
  const queryString = `?isoInstant=${time}`
  const url = api_url + queryString
  const response = await axios.post(url)
  console.log(response)
  // if(response.status === 200) {
  //   this.$message({
  //     message: '修改成功',
  //     type: 'success'
  //   });
  //   this.inputRemark = ''
  // } else {
  //   this.$message.error('修改失败')
  //   this.inputRemark = ''
  // }
  this.getTime()
}

async function getTime(){
  const url = "http://8.130.119.249:14103/api/v1/TradeManagement/Get_time"
  const response = await axios.get(url)
  console.log(response)
  const date = new Date(response.data)
  systemTime.value = date.toISOString().split('T')[0]
}
onMounted(()=>{
  getTime()
})
</script>

<template>
  <el-container>
<<<<<<< HEAD
    <el-header>
      <el-row>
        <el-col :span="8">
          <el-page-header @click="goBack" content="基金交易系统"></el-page-header>
        </el-col>
        <el-col :span="4">
          {{"userName"}}
        </el-col>
        <el-col :span="8">
          <el-button v-if="userID === ''" @click="showLoginPage">登录</el-button>
          <p v-else>
            <el-button @click="logout">退出</el-button> {{userID}}
          </p>
          <div class="login" v-if="loginIn">
            <login-vue></login-vue>
          </div>
        </el-col>
        <el-col :span="2">
          <el-button @click="changeTime(1)">修改至一天后</el-button>
        </el-col>
        <el-col :span="2">
          <el-button @click="changeTime(31)">修改至一月后</el-button>
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
=======
    <el-aside>
      <el-menu
          class="main-menu"
          background-color="#5E7BFFFF"
          text-color="#fff"
          active-text-color="#FF804CFF"
          unique-opened=true
          router
      >
        <el-sub-menu>
          <template #title>
            <el-icon><User/></el-icon>
            <span class="menuHeader">用户管理</span>
>>>>>>> 1b87634bb98133f625c1d830d2a01209a2926791
          </template>
          <el-menu-item-group>
            <el-menu-item index="/userInfo">信息修改</el-menu-item>
            <el-menu-item index="/tradeHistory">交易历史查看</el-menu-item>
            <el-menu-item index="/accountManage">持仓管理</el-menu-item>
          </el-menu-item-group>
        </el-sub-menu>
        <el-sub-menu>
          <template #title>
            <el-icon><Grid/></el-icon>
            <span class="menuHeader">基金信息</span>
          </template>
          <el-menu-item-group>
            <el-menu-item index="/investList">基金查看</el-menu-item>
            <el-menu-item index="/classificationList">分类排行</el-menu-item>
          </el-menu-item-group>
        </el-sub-menu>
        <el-sub-menu>
          <template #title>
            <el-icon><View/></el-icon>
            <span class="menuHeader">基金研究</span>
          </template>
          <el-menu-item-group>
            <el-menu-item index="/stockInfo">深度资料</el-menu-item>
            <el-menu-item index="/purchasePage">基金购买</el-menu-item>
          </el-menu-item-group>
        </el-sub-menu>
        <el-sub-menu>
          <template #title>
            <el-icon><Cellphone/></el-icon>
            <span class="menuHeader">基金工具</span>
          </template>
          <el-menu-item-group>
            <el-menu-item index="/purchaseRate">基金计算器</el-menu-item>
            <el-menu-item index="/comparison">基金比较</el-menu-item>
          </el-menu-item-group>
        </el-sub-menu>
      </el-menu>
    </el-aside>
    <el-main>
      <el-container>
        <el-header class="stockWebHeader">
          <el-row>
            <el-col :span="4">

            </el-col>
            <el-col :span="8">
              <div class="stockWebName">基金交易系统</div>
            </el-col>

            <el-col :span="6">
              <div class="showTime">当前系统时：{{systemTime}}</div>
            </el-col>
            <el-col :span="2">
              <el-button v-if="userID === ''" @click="showLoginPage" class="loginIcon">
                <img src="@/resource/login.png" class="loginPic">
              </el-button>
              <p v-else>{{userID}}</p>
              <div class="login" v-if="loginIn">
                <login-vue></login-vue>
              </div>
            </el-col>
            <el-col :span="2">
              <el-button @click="changeTime(1)" class="changeDateButton">
                <img src="@/resource/changeOneDay.png" class="changeDate">
              </el-button>
            </el-col>
            <el-col :span="2">
              <el-button @click="changeTime(31)" class="changeDateButton">
                <img src="@/resource/changeOneMonth.png" class="changeDate">
              </el-button>
            </el-col>
          </el-row>
        </el-header>
        <el-main>
          <router-view />
        </el-main>
      </el-container>
    </el-main>
  </el-container>
</template>


<style>
.login{
  z-index: 10;
  position: absolute;
  left: calc(50vw - 300px);
  top: calc(50vh - 250px);
  width: 450px;
  height: 400px;
  padding: 10px;
  background-color: white;
  box-shadow: 1px 1px 7px 1px #ccc;
  border-radius: 10px;
}

body {
  background-color: #eef1ff;
}

.main-menu{
  border-right: 0;
  border-radius: 10px;
}

.menuHeader{
  font-family:"AlimamaFangYuan",serif;
  font-weight: bold;
  font-size:18px;
}

.stockWebHeader{
  background-color: #ffffff;
}

.stockWebName{
  margin-top:5px;
  font-family:"AlimamaFangYuan",serif;
  font-size:38px;
}
.showTime{
  margin-top:15px;
}
.loginIcon{
  margin-top:10px
}
.loginPic{
  height:20px;
}
.changeDate{
  height:20px;
}
.changeDateButton{
  margin-top:10px
}
</style>