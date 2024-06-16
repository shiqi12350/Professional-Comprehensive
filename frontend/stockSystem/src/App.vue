<script setup>

import { RouterLink, RouterView, useRouter} from 'vue-router';
import axios from "axios";
import userInfo from '@/stores/user';
import {storeToRefs } from "pinia";
import loginVue from './components/login.vue';
import {ref} from 'vue'
import {watch} from 'vue'
import {User,Grid,View,Cellphone} from '@element-plus/icons'

const router = useRouter()

//get vuestore userID to Determine whether to log in or not 
const user = userInfo();
const {userID} = storeToRefs(user);

//登录
const loginIn = ref(false);
let showLoginPage = function(){
  loginIn.value = !loginIn.value
}
user.$subscribe((args, state) => {
  loginIn.value = !loginIn.value
})

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
  const url2 = "http://8.130.119.249:14103/api/v1/TradeManagement/Get_time"
  const response2 = await axios.get(url2)
  console.log(response2)

}
</script>

<template>
  <el-container>
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
          <p v-else>{{userID}}</p>
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
            <el-menu-item index="/purchaseRate">申购计算</el-menu-item>
            <el-menu-item index="/redemptionRate">赎回计算</el-menu-item>
            <el-menu-item index="/comparison">基金比较</el-menu-item>
          </el-menu-item-group>
        </el-sub-menu>
        </el-menu>
      </el-aside>
      <el-main>
          <router-view />
      </el-main>
    </el-container>
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

</style>