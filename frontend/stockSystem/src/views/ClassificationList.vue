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
      stockLoading:true,
      indexLoading:true,
      blendLoading:true,
      bondLoading:true
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
        this.stockLoading = false
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
        this.indexLoading = false
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
        this.blendLoading = false
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
        this.bondLoading = false
      }
    },
    getStockByPage(currentPage){
      this.stockLoading = true
      this.StockFund = []
      this.StockNum = currentPage
      this.getStockList(this.StockNum - 1)
    },
    handleStockChange(currentPage) {
      this.StockNum = currentPage
      this.getStockByPage(this.StockNum)
    },
    getIndexByPage(currentPage){
      this.indexLoading = true
      this.IndexFund = []
      this.IndexNum = currentPage
      this.getIndexList(this.IndexNum - 1)
    },
    handleIndexChange(currentPage) {
      this.IndexNum = currentPage
      this.getIndexByPage(this.IndexNum)
    },
    getBlendByPage(currentPage){
      this.blendLoading = true
      this.BlendFund = []
      this.BlendNum = currentPage
      this.getBlendList(this.BlendNum - 1)
    },
    handleBlendChange(currentPage) {
      this.BlendNum = currentPage
      this.getBlendByPage(this.BlendNum)
    },
    getBondByPage(currentPage){
      this.bondLoading = true
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
  <el-card>
    <template #header>
      <el-row>
        <el-col :span="2">
          <div class="classificationTitle">
            <img src="@/resource/classification.png" class="classificationPic">
          </div>
        </el-col>
        <el-col :span="22">
          <div class="classificationName">分类排行</div>
        </el-col>
      </el-row>
    </template>
    <el-row>
      <el-col :span="12">
        <el-card class="stockCard">
          <template #header>
            <div class="cardHeader">
              <img src="@/resource/股票.png" class="headerPic">
              <span>股票型</span>
            </div>
          </template>
          <el-table
              :data="StockFund"
              size="small"
              stripe="true"
              v-loading="stockLoading">
            <el-table-column prop="ts_code" label="基金代码" width="100"> </el-table-column>
            <el-table-column prop="name" label="基金名称" width="180"> </el-table-column>
            <el-table-column prop="unit_nav" label="单位净值" width="80"> </el-table-column>
            <el-table-column prop="status" label="申购状态" width="80"> </el-table-column>
            <el-table-column fixed="right" label="操作" width="100">
              <template #default="scope">
                <el-button type="primary" plain>详情</el-button>
              </template>
            </el-table-column>
          </el-table>
          <el-pagination
              :current-page="StockNum"
              @current-change="handleStockChange"
              :page-size="50"
              :total="250">
          </el-pagination>
        </el-card>
      </el-col>
      <el-col :span="12">
        <el-card class="blendCard">
          <template #header>
            <div class="cardHeader">
              <img src="@/resource/团队.png" class="headerPic">
              <span>混合型</span>
            </div>
          </template>
          <el-table
              :data="BlendFund"
              size="small"
              stripe="true"
              v-loading="blendLoading">
            <el-table-column prop="ts_code" label="基金代码" width="100"> </el-table-column>
            <el-table-column prop="name" label="基金名称" width="180"> </el-table-column>
            <el-table-column prop="unit_nav" label="单位净值" width="80"> </el-table-column>
            <el-table-column prop="status" label="申购状态" width="80"> </el-table-column>
            <el-table-column fixed="right" label="操作" width="100">
              <template #default="scope">
                <el-button type="primary" plain>详情</el-button>
              </template>
            </el-table-column>
          </el-table>
          <el-pagination
              :current-page="BlendNum"
              @current-change="handleBlendChange"
              :page-size="50"
              :total="250">
          </el-pagination>
        </el-card>
      </el-col>
    </el-row>
    <el-row>
      <el-col :span="12">
        <el-card class="bondCard">
          <template #header>
            <div class="cardHeader">
              <img src="@/resource/债券.png" class="headerPic">
              <span>债券型</span>
            </div>
          </template>
          <el-table
              :data="BondFund"
              size="small"
              stripe="true"
              v-loading="bondLoading">
            <el-table-column prop="ts_code" label="基金代码" width="100"> </el-table-column>
            <el-table-column prop="name" label="基金名称" width="180"> </el-table-column>
            <el-table-column prop="unit_nav" label="单位净值" width="80"> </el-table-column>
            <el-table-column prop="status" label="申购状态" width="80"> </el-table-column>
            <el-table-column fixed="right" label="操作" width="100">
              <template #default="scope">
                <el-button type="primary" plain>详情</el-button>
              </template>
            </el-table-column>
          </el-table>
          <el-pagination
              :current-page="BondNum"
              @current-change="handleBondChange"
              :page-size="50"
              :total="250">
          </el-pagination>
        </el-card>
      </el-col>
      <el-col :span="12">
        <el-card class="indexCard">
          <template #header>
            <div class="cardHeader">
              <img src="@/resource/人民币.png" class="headerPic">
              <span>货币型</span>
            </div>
          </template>
          <el-table
              :data="IndexFund"
              size="small"
              stripe="true"
              v-loading="indexLoading">
            <el-table-column prop="ts_code" label="基金代码" width="100"> </el-table-column>
            <el-table-column prop="name" label="基金名称" width="180"> </el-table-column>
            <el-table-column prop="unit_nav" label="单位净值" width="80"> </el-table-column>
            <el-table-column prop="status" label="申购状态" width="80"> </el-table-column>
            <el-table-column fixed="right" label="操作" width="100">
              <template #default="scope">
                <el-button type="primary" plain>详情</el-button>
              </template>
            </el-table-column>
          </el-table>
          <el-pagination
              :current-page="IndexNum"
              @current-change="handleIndexChange"
              :page-size="50"
              :total="250">
          </el-pagination>
        </el-card>
      </el-col>
    </el-row>
  </el-card>
</template>

<style>
.stockCard,.blendCard,.bondCard,.indexCard{
  margin-left:10px;
  margin-bottom:5px;
}

.cardHeader{
  font-family:'AlimamaFangYuan',serif;
  margin-bottom:10px;
  font-size:20px;
}

.headerPic{
  width:40px;
  height:40px;
  margin-right:40px
}
.classificationPic{
  width:40px;
  margin-top:10px;
  margin-left:10px;
}
.classificationName{
  margin-top:15px;
  font-size:30px;
}
.classificationTitle{
  width:250px;
  height:60px;
  background-color: #fff;
}
</style>