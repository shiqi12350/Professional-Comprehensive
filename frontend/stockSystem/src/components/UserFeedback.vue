<script>
import {useStockInfoStore} from "@/stores/stockInfoStore.js";

export default{
  setup(){
    const stockInfoStore = useStockInfoStore()
    return {
      stockId: stockInfoStore.stockId
    }
  },
  methods:{
    remark(){
      console.log("send remark")
    }
  },
  data(){
    return{
      StockID:this.stockId,
      UserName:'高程侠',
      comments: [
        {
          id: 1,
          username: '张三',
          text: '评论一'
        },
        {
          id: 2,
          username: '李四',
          text: '评论二'
        }
      ]
    }
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
            v-for="comment in comments"
            :key="comment.id">

          <div>{{ comment.username }}</div>
          <p>{{ comment.text }}</p>

        </el-card>
      </el-main>
      <el-footer>
        <el-row>
          <el-col :span="2">
            {{this.UserName}}
          </el-col>
          <el-col :span="22">
            <el-input v-model="input" placeholder="输入评论吧">
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