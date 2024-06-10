<script>
import axios from "axios";
export default {
  data(){
    return{
      StockNum:1,
      IndexNum:1,
      BlendNum:1,
      BondNum:1,
      StockFund:[],  /*股票型*/
      IndexFund:[],  /*货币市场型*/
      BlendFund:[],  /*混合型*/
      BondFund:[],   /*债券型*/
    }
  },
  methods:{
    async getStockList(num){
      console.log("当前股票列表" + num)
      const response = await axios.post(
          "http://api.tushare.pro",
          {
            api_name: "fund_basic",
            token: "91a073e655c1849334691d5f5c71a518ff5468891554887fad2afca0"
          }
      );
      const stocks = response.data.data.items.filter(item => {
        return item[19] === '股票型';
      });
      for(let i = num; i < num + 5 ; i++){
        const item = stocks[i];
        const tsCode = item[0];
        const Name = item[1];
        const status = item[18];
        /*获取单位净值*/
        const response2 = await axios.post(
            "http://api.tushare.pro",
            {
              api_name: "fund_nav",
              token: "91a073e655c1849334691d5f5c71a518ff5468891554887fad2afca0",
              params:{"ts_code":tsCode}
            }
        )
        const unitNav = response2.data.data.items[0][3];
        this.StockFund.push({
          ts_code: tsCode,
          name:Name,
          status: status,
          unit_nav: unitNav
        });
      }
    },
    async getIndexList(num){
      const response = await axios.post(
          "http://api.tushare.pro",
          {
            api_name: "fund_basic",
            token: "91a073e655c1849334691d5f5c71a518ff5468891554887fad2afca0"
          }
      );
      const stocks = response.data.data.items.filter(item => {
        return item[19] === '货币型';
      });
      for(let i = num; i < num + 5 ; i++){
        const item = stocks[i];
        const tsCode = item[0];
        const Name = item[1];
        const status = item[18];
        /*获取单位净值*/
        const response2 = await axios.post(
            "http://api.tushare.pro",
            {
              api_name: "fund_nav",
              token: "91a073e655c1849334691d5f5c71a518ff5468891554887fad2afca0",
              params:{"ts_code":tsCode}
            }
        )
        const unitNav = response2.data.data.items[0][3];
        this.IndexFund.push({
          ts_code: tsCode,
          name:Name,
          status: status,
          unit_nav: unitNav
        });
      }
    },
    async getBlendList(num){
      const response = await axios.post(
          "http://api.tushare.pro",
          {
            api_name: "fund_basic",
            token: "91a073e655c1849334691d5f5c71a518ff5468891554887fad2afca0"
          }
      );
      const stocks = response.data.data.items.filter(item => {
        return item[19] === '混合型';
      });
      for(let i = num; i < num + 5 ; i++){
        const item = stocks[i];
        const tsCode = item[0];
        const Name = item[1];
        const status = item[18];
        /*获取单位净值*/
        const response2 = await axios.post(
            "http://api.tushare.pro",
            {
              api_name: "fund_nav",
              token: "91a073e655c1849334691d5f5c71a518ff5468891554887fad2afca0",
              params:{"ts_code":tsCode}
            }
        )
        const unitNav = response2.data.data.items[0][3];
        this.BlendFund.push({
          ts_code: tsCode,
          name:Name,
          status: status,
          unit_nav: unitNav
        });
      }
    },
    async getBondList(num){
      const response = await axios.post(
          "http://api.tushare.pro",
          {
            api_name: "fund_basic",
            token: "91a073e655c1849334691d5f5c71a518ff5468891554887fad2afca0"
          }
      );
      const stocks = response.data.data.items.filter(item => {
        return item[19] === '债券型';
      });
      for(let i = num; i < num + 5 ; i++){
        const item = stocks[i];
        const tsCode = item[0];
        const Name = item[1];
        const status = item[18];
        /*获取单位净值*/
        const response2 = await axios.post(
            "http://api.tushare.pro",
            {
              api_name: "fund_nav",
              token: "91a073e655c1849334691d5f5c71a518ff5468891554887fad2afca0",
              params:{"ts_code":tsCode}
            }
        )
        const unitNav = response2.data.data.items[0][3];
        this.BondFund.push({
          ts_code: tsCode,
          name:Name,
          status: status,
          unit_nav: unitNav
        });
      }
    },
    getStockByPage(currentPage){
      this.StockFund = []
      this.StockNum = currentPage
      this.getStockList(this.StockNum - 1)
    },
    handleStockChange(currentPage) {
      this.StockNum = currentPage
      this.getStockByPage(this.StockNum)
    },
    getIndexByPage(currentPage){
      this.IndexFund = []
      this.IndexNum = currentPage
      this.getIndexList(this.IndexNum - 1)
    },
    handleIndexChange(currentPage) {
      this.IndexNum = currentPage
      this.getIndexByPage(this.IndexNum)
    },
    getBlendByPage(currentPage){
      this.BlendFund = []
      this.BlendNum = currentPage
      this.getBlendList(this.BlendNum - 1)
    },
    handleBlendChange(currentPage) {
      this.BlendNum = currentPage
      this.getBlendByPage(this.BlendNum)
    },
    getBondByPage(currentPage){
      this.BondFund = []
      this.BondNum = currentPage
      this.getBondList(this.BondNum - 1)
    },
    handleBondChange(currentPage) {
      this.BondNum = currentPage
      this.getBondByPage(this.BondNum)
    }

  },
  mounted(){
    this.getStockList(0)
    this.getBondList(0)
    this.getBlendList(0)
    this.getIndexList(0)
  }
}

</script>

<template>
  <el-row>
    <el-col :span="12">
      <div>股票型</div>
      <el-table
        :data="StockFund">
        <el-table-column prop="ts_code" label="基金代码" width="100"> </el-table-column>
        <el-table-column prop="name" label="基金名称" width="200"> </el-table-column>
        <el-table-column prop="unit_nav" label="单位净值" width="100"> </el-table-column>
        <el-table-column prop="status" label="申购状态" width="90"> </el-table-column>
        <el-table-column fixed="right" label="操作" width="100">
          <template #default="scope">
            <el-button type="primary">详情</el-button>
          </template>
        </el-table-column>
      </el-table>
      <el-pagination
          :current-page="StockNum"
          @current-change="handleStockChange"
          :page-size="50"
          :total="250">
      </el-pagination>
    </el-col>
    <el-col :span="12">
      <div>混合型</div>
      <el-table
      :data="BlendFund">
        <el-table-column prop="ts_code" label="基金代码" width="100"> </el-table-column>
        <el-table-column prop="name" label="基金名称" width="200"> </el-table-column>
        <el-table-column prop="unit_nav" label="单位净值" width="100"> </el-table-column>
        <el-table-column prop="status" label="申购状态" width="90"> </el-table-column>
        <el-table-column fixed="right" label="操作" width="100">
          <template #default="scope">
            <el-button type="primary">详情</el-button>
          </template>
        </el-table-column>
      </el-table>
      <el-pagination
          :current-page="BlendNum"
          @current-change="handleBlendChange"
          :page-size="50"
          :total="250">
      </el-pagination>
    </el-col>
  </el-row>
  <el-row>
    <el-col :span="12">
      <div>债券型</div>
      <el-table
      :data="BondFund">
        <el-table-column prop="ts_code" label="基金代码" width="100"> </el-table-column>
        <el-table-column prop="name" label="基金名称" width="200"> </el-table-column>
        <el-table-column prop="unit_nav" label="单位净值" width="100"> </el-table-column>
        <el-table-column prop="status" label="申购状态" width="90"> </el-table-column>
        <el-table-column fixed="right" label="操作" width="100">
          <template #default="scope">
            <el-button type="primary">详情</el-button>
          </template>
        </el-table-column>
      </el-table>
      <el-pagination
          :current-page="BondNum"
          @current-change="handleBondChange"
          :page-size="50"
          :total="250">
      </el-pagination>
    </el-col>
    <el-col :span="12">
      <div>货币型</div>
      <el-table
      :data="IndexFund">
        <el-table-column prop="ts_code" label="基金代码" width="100"> </el-table-column>
        <el-table-column prop="name" label="基金名称" width="200"> </el-table-column>
        <el-table-column prop="unit_nav" label="单位净值" width="100"> </el-table-column>
        <el-table-column prop="status" label="申购状态" width="90"> </el-table-column>
        <el-table-column fixed="right" label="操作" width="100">
          <template #default="scope">
            <el-button type="primary">详情</el-button>
          </template>
        </el-table-column>
      </el-table>
      <el-pagination
          :current-page="IndexNum"
          @current-change="handleIndexChange"
          :page-size="50"
          :total="250">
      </el-pagination>
    </el-col>
  </el-row>
</template>

<style scoped>

</style>