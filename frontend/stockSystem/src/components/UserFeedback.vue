<script>
import {useStockInfoStore} from "@/stores/stockInfoStore.js";
import axios from "axios";

export default{
  setup(){
    const stockInfoStore = useStockInfoStore()
    return {
      stockId: stockInfoStore.stockId
    }
  },
  methods:{
    async remark(){
      console.log("send remark")
      const api_url = "http://8.130.119.249:14102/api/v1/informationmaintenance/InsertNewComment"
      const queryString = `?fundCode=${this.StockID}&content=${this.inputRemark}`
      const url = api_url + queryString
      console.log(url)
      const response = await axios.get(url)
      if(response.status === 200) {
        this.$message({
          message: '评论发送成功',
          type: 'success'
        });
        this.inputRemark = ''
      } else {
        this.$message.error('评论发送失败')
        this.inputRemark = ''
      }
      console.log(response)
    },
    async gerRemark(){
      const api_url = "http://8.130.119.249:14102/api/v1/informationmaintenance/ObtainAllFundComment"
      const queryString = `?fundCode=021406.OF`
      const url = api_url+queryString
      const response = await axios.get(url)
      console.log(response.data.contentList[0].content)
      this.comments = response.data.contentList.map(item => {
        return {
          text: item.content
        }
      })
      console.log(this.comments)
      console.log(response)
    }
  },
  data(){
    return{
      StockID:this.stockId,
      UserName:'高程侠',
      inputRemark:'',
      comments: []
    }
  },
  mounted(){
    this.gerRemark()
  }
}
</script>

<template>
  <div class="forum">
    <el-container>
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
        <el-card
            v-for="comment in comments">
          <p>{{ comment.text }}</p>
        </el-card>
      </el-main>
      <el-footer>
        <el-row>
          <el-col :span="2">
            {{this.UserName}}
          </el-col>
          <el-col :span="22">
            <el-input v-model="inputRemark" placeholder="输入评论吧">
            </el-input>
            <div class="button-wrapper">
              <el-button
                  class="sendButton"
                  @click="remark"
                  type="primary">
                发送
              </el-button>
            </div>
          </el-col>
        </el-row>
      </el-footer>
    </el-container>
  </div>


</template>

<style>
.button-wrapper {
  text-align: right;
}
.forum {
  height: 80vh;
  display: flex;
  flex-direction: column;
}
</style>