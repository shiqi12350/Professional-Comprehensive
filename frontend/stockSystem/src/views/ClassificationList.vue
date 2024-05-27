<script setup>

</script>

<template>
  <el-row>
    <el-col :span="12">
      <div>股票型</div>
      <el-table>
        <el-table-column prop="name" label="基金名称" width="150"> </el-table-column>
        <el-table-column prop="daily_increment" label="日增长值" width="150"> </el-table-column>
        <el-table-column prop="state" label="申购状态" width="100"> </el-table-column>
        <el-table-column fixed="right" label="操作" width="100">
          <template #default="scope">
            <el-button type="primary">详情</el-button>
          </template>
        </el-table-column>
      </el-table>
    </el-col>
    <el-col :span="12">
      <div>混合型</div>
      <el-table>
        <el-table-column prop="name" label="基金名称" width="150"> </el-table-column>
        <el-table-column prop="daily_increment" label="日增长值" width="150"> </el-table-column>
        <el-table-column prop="state" label="申购状态" width="100"> </el-table-column>
        <el-table-column fixed="right" label="操作" width="100">
          <template #default="scope">
            <el-button type="primary">详情</el-button>
          </template>
        </el-table-column>
      </el-table>
    </el-col>
  </el-row>
  <el-row>
    <el-col :span="12">
      <div>债券型</div>
      <el-table>
        <el-table-column prop="name" label="基金名称" width="150"> </el-table-column>
        <el-table-column prop="daily_increment" label="日增长值" width="150"> </el-table-column>
        <el-table-column prop="state" label="申购状态" width="100"> </el-table-column>
        <el-table-column fixed="right" label="操作" width="100">
          <template #default="scope">
            <el-button type="primary">详情</el-button>
          </template>
        </el-table-column>
      </el-table>
    </el-col>
    <el-col :span="12">
      <div>指数型</div>
      <el-table>
        <el-table-column prop="name" label="基金名称" width="150"> </el-table-column>
        <el-table-column prop="daily_increment" label="日增长值" width="150"> </el-table-column>
        <el-table-column prop="state" label="申购状态" width="100"> </el-table-column>
        <el-table-column fixed="right" label="操作" width="100">
          <template #default="scope">
            <el-button type="primary">详情</el-button>
          </template>
        </el-table-column>
      </el-table>
    </el-col>
  </el-row>
</template>

<style scoped>

</style>