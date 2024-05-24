<script >

export default {
  data(){
    return{
      Username:'null',
      tableData:[{
        id:'000001',
        name:'汇丰晋信',
        yes_unit_net_worth:2.3882,
        yes_cumulative_net_worth:2.4082,
        to_unit_net_worth:2.3356,
        to_cumulative_net_worth:2.3556,
        daily_increment:0.0526,
        state:'开放申购'
      }],
      input:'',
    }
  },
}
</script>

<template>
  <el-container>
    <el-header>
      <el-row>
        <el-col :span="16">
          <el-page-header @back="goBack" content="基金交易系统"></el-page-header>
        </el-col>
        <el-col :span="8">
          {{this.Username}}
        </el-col>
      </el-row>
    </el-header>
    <el-main>
      <el-row>
        <el-col :span="16">
          <el-collapse v-model="activeNames" @change="handleChange">
            <el-collapse-item title="筛选" name="1">
              <div class="stock_category">
                <el-tag type="success">股票型</el-tag>
                <el-tag type="info">混合型</el-tag>
                <el-tag type="warning">债券型</el-tag>
                <el-tag type="danger">指数型</el-tag>
              </div>
            </el-collapse-item>
          </el-collapse>
        </el-col>
        <el-col :span="6">
          <el-input v-model="input" placeholder="请输入搜索内容"></el-input>
        </el-col>
        <el-col :span="2">
          <el-button>搜索</el-button>
        </el-col>
      </el-row>
      <el-table
          :data="tableData"
      >
        <el-table-column prop="id" label="基金代码" width="150"> </el-table-column>
        <el-table-column prop="name" label="基金名称" width="150"> </el-table-column>
        <el-table-column label="昨日">
          <el-table-column prop="yes_unit_net_worth" label="单位净值" width="150"> </el-table-column>
          <el-table-column prop="yes_cumulative_net_worth" label="累计净值" width="150"> </el-table-column>
        </el-table-column>
        <el-table-column label="今日">
          <el-table-column prop="to_unit_net_worth" label="单位净值" width="150"> </el-table-column>
          <el-table-column prop="to_cumulative_net_worth" label="累计净值" width="150"> </el-table-column>
        </el-table-column>
        <el-table-column prop="daily_increment" label="日增长值" width="150"> </el-table-column>
        <el-table-column prop="state" label="申购状态" width="150"> </el-table-column>
        <el-table-column fixed="right" label="操作" >
          <template #default="scope">
            <el-button type="primary">详情</el-button>
            <el-button type="danger">买入</el-button>
          </template>
        </el-table-column>
      </el-table>
    </el-main>
    <el-footer>
      <el-pagination background layout="prev, pager, next" :total="5">
      </el-pagination>
    </el-footer>
  </el-container>
  <router-link to="/">back</router-link>
</template>

<style scoped>

</style>