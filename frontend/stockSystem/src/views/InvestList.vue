  <script>
  import { useRouter } from 'vue-router';
  import axios from "axios";
  export default {
    data(){
      return{
        Username:'null',
        tableData:[],
        currentPage: 1,
        input:'',
        ts_code: '',
      }
    },
    setup() {
      const router = useRouter()
      function turnToCorrespondingStock() {
        router.push('/StockInfo')
      }
      return {
        turnToCorrespondingStock
      }
    },
    mounted() {
      this.getStockList(0)
    },
    methods: {
      async getStockList(num){
        const response = await axios.post(
            "http://api.tushare.pro",
            {
                api_name: "fund_basic",
                token: "91a073e655c1849334691d5f5c71a518ff5468891554887fad2afca0"
            }
        );
        const funds = [];

        for(let i = num; i < num + 10; i++) {
          const item = response.data.data.items[i];

          const fund = {
            tsCode: item[0],
            name: item[1],
            management: item[2],
            foundDate: item[5],
            status: item[18],
            investType: item[19]
          };

          funds.push(fund);
        }

        this.tableData = funds
        console.log(funds)
      },
      getStockListByPage(currentPage){
        this.currentPage = currentPage
        this.getStockList(this.currentPage - 1)
      },
      handleCurrentChange(currentPage) {
        this.currentPage = currentPage
        this.getStockListByPage(currentPage)
      },
      async searchByTsCode(){
        const response = await axios.post(
            "http://api.tushare.pro",
            {
              api_name: "fund_basic",
              token: "91a073e655c1849334691d5f5c71a518ff5468891554887fad2afca0",
              params:{"ts_code": this.ts_code}
            }
        );
        const fund = {
          tsCode: response.data.data.items[0][0],
          name: response.data.data.items[0][1],
          management: response.data.data.items[0][2],
          foundDate: response.data.data.items[0][5],
          status: response.data.data.items[0][18],
          investType: response.data.data.items[0][19]
        }
        this.tableData = [fund]
      },
      clearTsCode() {
        this.ts_code = ''
        this.tableData = []
        console.log(this.currentPage)
        this.getStockList(this.currentPage)
      }
    }
  }
  </script>

  <template>
    <el-container>
      <el-main>
        <el-row>
          <el-col :span="16"></el-col>
          <el-col :span="6">
            <el-input v-model="ts_code" placeholder="请输入基金代码"></el-input>
          </el-col>
          <el-col :span="2">
            <el-button
                clearable:true
                @click="searchByTsCode"
                @clear="clearTsCode">搜索</el-button>
          </el-col>
        </el-row>
        <el-table
            :data="tableData"
            :width="1060"
        >
          <el-table-column prop="tsCode" label="基金代码" width="100"> </el-table-column>
          <el-table-column prop="name" label="基金名称" width="250"> </el-table-column>
          <el-table-column prop="management" label="管理人" width="150"> </el-table-column>
          <el-table-column prop="foundDate" label="成立日期" width="150"> </el-table-column>
          <el-table-column prop="investType" label="投资风格" width="100"> </el-table-column>
          <el-table-column prop="status" label="状态" width="100"> </el-table-column>
          <el-table-column fixed="right" label="操作" width="200">
            <template #default="scope">
              <el-button type="primary" @click="turnToCorrespondingStock">详情</el-button>
              <el-button type="danger">买入</el-button>
            </template>
          </el-table-column>
        </el-table>
        <el-pagination
            :current-page="currentPage"
            @current-change="handleCurrentChange"
            :page-size="10"
            :total="1000">
        </el-pagination>
      </el-main>
    </el-container>
  </template>

  <style>

  </style>