<script>
import * as echarts from 'echarts';
export default {
  data(){
    return{
      Username:'null',
      value1:5,
      tableData:[
        {
          name: '嘉实资源'
        },
        {
          name: '招商蓝筹'
        }
      ],
      commentVisible:false,
      AIVisible:false

    }
  },
  mounted(){
    let StockChart = echarts.init(this.$refs.stockInfoChart)
    let xData = ['5-14','5-15','5-16','5-17','5-18','5-19','5-20']
    let data = [0.8460,0.8444,0.8376,0.8218,0.8499,0.8523,0.8353]
    let options={
      xAxis:{
        type:"category",
        data:xData
      },
      yAxis:{
        type:"value"
      },
      series:[{
        data,
        type:"line"
      }]
    };
    StockChart.setOption(options)
  },

}
</script>

<template>
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
      <el-col :span="8">
        <div class="ai">
          <el-button type="text" @click="AIVisible=true">详情</el-button>
        </div>
        <div class="ref">
          <el-button type="text" @click="commentVisible=true">详情</el-button>
          <el-card class="box-card">
            <template #header>
              <div class="card-header">
                <span>用户1</span>
                <el-rate v-model="value1"></el-rate>
              </div>
            </template>
            <div>comment</div>
          </el-card>
        </div>
      </el-col>
      <el-col :span="10">
        <div>当前基金：{{}}</div>
        <div ref="stockInfoChart" id="stockInfoChart"></div>
      </el-col>
      <el-col :span="6">
        <div class="StockInfoTable">
          <el-table
              :data="tableData"
              title="涨幅排行">

            <el-table-column
                label="排名"
                prop="rank"
                type="index"
                width="80">
            </el-table-column>

            <el-table-column
                label="基金名"
                prop="name"
                width="250">
            </el-table-column>

            <el-table-column fixed="right" label="操作" width="120">
              <template #default="scope">
                <el-button
                    type="text"
                    size="small">
                  详情
                </el-button>
              </template>
            </el-table-column>

          </el-table>
        </div>
      </el-col>
    </el-row>
  </el-main>
  <el-dialog v-model="commentVisible">
    <el-card class="box-card">
      <template #header>
        <div class="card-header">
          <span>用户1</span>
          <el-rate v-model="value1"></el-rate>
        </div>
      </template>
      <div>comment</div>
    </el-card>
  </el-dialog>
  <el-dialog v-model="AIVisible"></el-dialog>
  <router-link to="/">back</router-link>
</template>

<style>
#stockInfoChart{
  width:500px;
  height:500px;
  border:1px solid red;
}
.StockInfoTable{
  width:300px;
  height:500px;
  border:1px solid red;
}
.ai{
  width:250px;
  height:200px;
  border:1px solid red;
}
.ref{
  width:250px;
  height:300px;
  border:1px solid red;
}
</style>