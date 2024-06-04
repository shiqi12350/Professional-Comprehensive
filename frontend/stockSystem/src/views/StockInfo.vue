<script>
import * as echarts from 'echarts';
import {useRouter} from "vue-router";
export default {
  data(){
    return{
      Username:'汇丰晋信',
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
        type:"value",
        min: data[0] - 0.1
      },
      series:[{
        data,
        type:"line"
      }]
    };
    StockChart.setOption(options)
  },
  setup() {
    const router = useRouter()
    function goBack() {
      router.push('/')
    }
    return {
      goBack
    }
  }
}
</script>

<template>
  <el-main>
    <el-row>
      <el-col :span="6">
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
      <el-col :span="12">
        <div class="StockName">当前基金：{{Username}}</div>
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
                width="60">
            </el-table-column>

            <el-table-column
                label="基金名"
                prop="name"
                width="140">
            </el-table-column>

            <el-table-column
                fixed="right"
                label="操作"
                width="80">
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
</template>

<style>
#stockInfoChart{
  width:500px;
  height:400px;
  border:1px solid red;
}
.StockInfoTable{
  width:280px;
  height:500px;
  border:1px solid red;
}
.ai{
  width:250px;
  height:200px;
  border:1px solid red;
  margin-bottom: 20px;
}
.ref{
  width:250px;
  height:350px;
  border:1px solid red;
}
.StockName{
  font-weight: bold;
  font-size: 20px;
  text-align: center;
  margin-bottom: 20px;
}
.box-card{
  width:230px;
  margin-left:10px;
}
.card-header{
  height:15px;
}
</style>