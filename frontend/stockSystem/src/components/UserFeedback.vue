<script>

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
        <el-menu-item index="/StockInfo/dataCatalog_basicInfo" >基本信息</el-menu-item>
        <el-menu-item index="/StockInfo/dataCatalog_navHistory">净值记录</el-menu-item>
      </el-sub-menu>
      <el-menu-item index="/StockInfo/RankInfo">排名信息</el-menu-item>
      <el-menu-item index="/StockInfo/AI_analysis">AI分析</el-menu-item>
      <el-menu-item index="/StockInfo/UserFeedback">用户反馈</el-menu-item>
    </el-menu>
  </el-header>
</template>

<style>

</style>