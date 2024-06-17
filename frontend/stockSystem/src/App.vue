<script setup>

import { RouterLink, RouterView, useRouter} from 'vue-router';
import axios from "axios";
import userInfo from '@/stores/user';
import {storeToRefs } from "pinia";
import loginVue from './components/login.vue';
import {onMounted, ref} from 'vue'
import {watch} from 'vue'
import {User,Grid,View,Cellphone} from '@element-plus/icons'
import Login from '@/components/login.vue'

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

const logout = () => {  
      user.reset();  
       //alert('您已成功退出登录！');  
    };


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

function closeLogin(data){
  console.log("调用closeLogin")
  loginIn.value = data
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
    <el-aside>
      <el-card class="userInfo">
        <el-row>
          <el-col :span="6">
            <img src="@/resource/randomUser.png" class="userImage" v-if="userID !== ''">
            <img src="@/resource/unknownUser.png" class="userImage" v-if="userID === ''">
          </el-col>
          <el-col :span="18">
            <div class="userName">用户{{userID}}</div>
            <div class="userName" v-if="userID !== ''">欢迎使用本系统！</div>
            <div class="userName" v-if="userID === ''">请先登录！</div>
            <div class="showTime">{{systemTime}}</div>
          </el-col>
        </el-row>
      </el-card>
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
            <el-col :span="3">
              <img src="@/resource/icon.png" class="icon">
            </el-col>
            <el-col :span="6">
              <div class="stockWebName">TJ基金交易系统</div>
            </el-col>
            <el-col :span="9">
            </el-col>
            <el-col :span="2">
<!--              <el-button v-if="userID === ''" @click="showLoginPage" class="loginIcon">-->
<!--                <img src="@/resource/login.png" class="loginPic">-->
<!--              </el-button>-->
<!--              <p v-else>{{userID}}</p>-->
<!--              <div class="login" v-if="loginIn">-->
<!--                <login-vue></login-vue>-->
<!--              </div>-->
              <el-button v-if="userID === ''" @click="showLoginPage" class="loginIcon">
                <img src="@/resource/login.png" class="loginPic">
              </el-button>
              <p v-else><el-button @click="logout" class="logoutIcon">
                <img src="@/resource/logout.png" class="logoutPic">
              </el-button>
              </p>
              <div class="login" v-if="loginIn">
                <Login @close="closeLogin"/>
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
  left: calc(50vw - 600px);
  top: calc(50vh - 250px);
  width: 800px;
  height: 500px;
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
.userName{
  font-size:20px;
  margin-top:5px;
}
.showTime{
  font-size:15px;
  margin-top:2px;
}
.loginIcon{
  margin-top:10px
}
.loginPic{
  height:20px;
}
.logoutIcon{
  margin-top:-5px;
}
.logoutPic{
  height:20px;
}
.changeDate{
  height:20px;
}
.changeDateButton{
  margin-top:10px
}
.icon{
  height:60px;
  margin-left:60px;
}
.userInfo{
  border-color: #333333;
  border-radius: 10px;
  margin-bottom:10px;
  color:white;
  background-color: #5E7BFFFF;
}
.userImage{
  width: 50px;
  height: 50px;
  object-fit: cover;
  border-radius: 50%;
}
.el-button{
  background-color: white;
  color: #8197fd;
  font-size:18px;
  border-width:2px;
}
</style>