<script>
import axios from "axios";
import { useStockInfoStore } from '@/stores/stockInfoStore.js'
export default {
  setup(){
    const stockInfoStore = useStockInfoStore()
    return {
      stockId: stockInfoStore.stockId
    }
  },
  data() {
    return {
      StockID: this.stockId ,
      StockInfo: [{
        ts_code: '',
        name: '',
        management: '',
        custodian: '',
        fund_type: '',
        found_date: '',
        m_fee: '',
        c_fee: '',
        p_value: '',
        benchmark: '',
        status: '',
        invest_type: '',
        purc_startdate: '',
        redm_startdate: '',
        market: '',
        trade_date: '',
        fd_share: '',
        ann_date: '',
        nav_date: '',
        unit_nav: '',
        accum_nav: '',
        accum_div: '',
        net_asset: '',
        total_netasset: '',
        adj_nav: '',
        imp_anndate: '',
        base_date: '',
        div_proc: '',
        record_date: '',
        ex_date: '',
        pay_date: '',
        earpay_date: '',
        net_ex_date: '',
        div_cash: '',
        base_unit: '',
        ear_distr: '',
        ear_amount: '',
        account_date: '',
        base_year: '',
        mkv: '',
        stk_mkv_ratio: '',
        stk_float_ratio: '',
      }]
    }
  },
  methods: {
    async getStockList(StockID) {
      const response = await axios.post(
          "http://api.tushare.pro",
          {
            api_name: "fund_basic",
            token: "91a073e655c1849334691d5f5c71a518ff5468891554887fad2afca0",
            params: {"ts_code": StockID}
          }
      );
      console.log(response.data.data.items[0])
      this.StockInfo.ts_code = response.data.data.items[0][0]
      this.StockInfo.name = response.data.data.items[0][1]
      this.StockInfo.management = response.data.data.items[0][2]
      this.StockInfo.custodian = response.data.data.items[0][3]
      this.StockInfo.fund_type = response.data.data.items[0][4]
      this.StockInfo.found_date = response.data.data.items[0][5]
      this.StockInfo.m_fee = response.data.data.items[0][11]
      this.StockInfo.c_fee = response.data.data.items[0][12]
      this.StockInfo.p_value = response.data.data.items[0][14]
      this.StockInfo.benchmark = response.data.data.items[0][17]
      this.StockInfo.status = response.data.data.items[0][18]
      this.StockInfo.invest_type = response.data.data.items[0][19]
      this.StockInfo.purc_startdate = response.data.data.items[0][22]
      this.StockInfo.redm_startdate = response.data.data.items[0][23]
      this.StockInfo.market = response.data.data.items[0][24]
    },
    async getNavData(StockID) {
      const response = await axios.post(
          "http://api.tushare.pro",
          {
            api_name: "fund_nav",
            token: "91a073e655c1849334691d5f5c71a518ff5468891554887fad2afca0",
            params: {"ts_code": StockID}
          }
      );
      console.log(response)
      this.StockInfo.ann_date = response.data.data.items[0][1]
      this.StockInfo.nav_date = response.data.data.items[0][2]
      this.StockInfo.unit_nav = response.data.data.items[0][3]
      this.StockInfo.accum_nav = response.data.data.items[0][4]
      this.StockInfo.accum_div = response.data.data.items[0][5]
      this.StockInfo.net_asset = response.data.data.items[0][6]
      this.StockInfo.total_netasset = response.data.data.items[0][7]
      this.StockInfo.adj_nav = response.data.data.items[0][8]
    },
    async getShareData(StockID) {
      const response = await axios.post(
          "http://api.tushare.pro",
          {
            api_name: "fund_share",
            token: "91a073e655c1849334691d5f5c71a518ff5468891554887fad2afca0",
            params: {"ts_code": StockID}
          }
      );
      console.log(response)
      this.StockInfo.trade_date = response.data.data.items[0][1]
      this.StockInfo.fd_share = response.data.data.items[0][2]
    },
    async getDivData(StockID) {
      const response = await axios.post(
          "http://api.tushare.pro",
          {
            api_name: "fund_div",
            token: "91a073e655c1849334691d5f5c71a518ff5468891554887fad2afca0",
            params: {"ts_code": StockID}
          }
      );
      console.log(response)
      this.StockInfo.imp_anndate = response.data.data.items[0][2]
      this.StockInfo.base_date = response.data.data.items[0][3]
      this.StockInfo.div_proc = response.data.data.items[0][4]
      this.StockInfo.record_date = response.data.data.items[0][5]
      this.StockInfo.ex_date = response.data.data.items[0][6]
      this.StockInfo.pay_date = response.data.data.items[0][7]
      this.StockInfo.earpay_date = response.data.data.items[0][8]
      this.StockInfo.net_ex_date = response.data.data.items[0][9]
      this.StockInfo.div_cash = response.data.data.items[0][10]
      this.StockInfo.base_unit = response.data.data.items[0][11]
      this.StockInfo.ear_distr = response.data.data.items[0][12]
      this.StockInfo.ear_amount = response.data.data.items[0][13]
      this.StockInfo.account_date = response.data.data.items[0][14]
      this.StockInfo.base_year = response.data.data.items[0][15]
    },
    async getPortfolioData(StockID) {
      const response = await axios.post(
          "http://api.tushare.pro",
          {
            api_name: "fund_portfolio",
            token: "91a073e655c1849334691d5f5c71a518ff5468891554887fad2afca0",
            params: {"ts_code": StockID}
          }
      );
      console.log(response)
      this.StockInfo.mkv = response.data.data.items[0][4]
      this.StockInfo.stk_mkv_ratio = response.data.data.items[0][6]
      this.StockInfo.stk_float_ratio = response.data.data.items[0][7]
    }
  },
  mounted() {
    this.getStockList(this.StockID)
    this.getShareData(this.StockID)
    this.getNavData(this.StockID)
    this.getDivData(this.StockID)
    this.getPortfolioData(this.StockID)
  }
}
</script>

<template>
  <el-card>
    <template #header>
      <el-row>
        <el-col :span="2">
          <div class="stockInfoTitle">
            <img src="@/resource/stockInfo.png" class="stockInfoPic">
          </div>
        </el-col>
        <el-col :span="22">
          <div class="stockInfoName">深度资料</div>
        </el-col>
      </el-row>
    </template>
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
      <el-descriptions
          class="margin-top"
          title="基金基本信息"
          :column="2"
          border
      >
        <el-descriptions-item label="基金代码">{{ this.StockInfo.ts_code }}</el-descriptions-item>
        <el-descriptions-item label="简称">{{ this.StockInfo.name }}</el-descriptions-item>
        <el-descriptions-item label="管理人">{{ this.StockInfo.management }}</el-descriptions-item>
        <el-descriptions-item label="托管人">{{ this.StockInfo.custodian }}</el-descriptions-item>
        <el-descriptions-item label="投资类型">{{ this.StockInfo.fund_type }}</el-descriptions-item>
        <el-descriptions-item label="成立日期">{{ this.StockInfo.found_date }}</el-descriptions-item>
        <el-descriptions-item label="管理费">{{ this.StockInfo.m_fee }}</el-descriptions-item>
        <el-descriptions-item label="托管费">{{ this.StockInfo.c_fee }}</el-descriptions-item>
        <el-descriptions-item label="面值">{{ this.StockInfo.p_value }}</el-descriptions-item>
        <el-descriptions-item label="业绩比较基准">{{ this.StockInfo.benchmark }}</el-descriptions-item>
        <el-descriptions-item label="存续状态D摘牌 I发行 L已上市">{{ this.StockInfo.status }}</el-descriptions-item>
        <el-descriptions-item label="投资风格">{{ this.StockInfo.invest_type }}</el-descriptions-item>
        <el-descriptions-item label="日常申购起始日">{{ this.StockInfo.purc_startdate }}</el-descriptions-item>
        <el-descriptions-item label="日常赎回起始日">{{ this.StockInfo.redm_startdate }}</el-descriptions-item>
        <el-descriptions-item label="E场内O场外">{{ this.StockInfo.market }}</el-descriptions-item>


      </el-descriptions>
      <br><br>
      <el-descriptions
          class="margin-top"
          title="基金规模数据"
          :column="2"
          :size="size"
          border>
        <el-descriptions-item label="交易（变动）日期">{{ this.StockInfo.trade_date }}</el-descriptions-item>
        <el-descriptions-item label="基金份额（万）">{{ this.StockInfo.fd_share }}</el-descriptions-item>
      </el-descriptions>
      <br><br>
      <el-descriptions
          class="margin-top"
          title="基金净值数据"
          :column="2"
          :size="size"
          border>
        <el-descriptions-item label="公告日期">{{ this.StockInfo.ann_date }}</el-descriptions-item>
        <el-descriptions-item label="净值日期">{{ this.StockInfo.nav_date }}</el-descriptions-item>
        <el-descriptions-item label="单位净值">{{ this.StockInfo.unit_nav }}</el-descriptions-item>
        <el-descriptions-item label="累计净值">{{ this.StockInfo.accum_nav }}</el-descriptions-item>
        <el-descriptions-item label="累计分红">{{ this.StockInfo.accum_div }}</el-descriptions-item>
        <el-descriptions-item label="资产净值">{{ this.StockInfo.net_asset }}</el-descriptions-item>
        <el-descriptions-item label="合计资产净值">{{ this.StockInfo.total_netasset }}</el-descriptions-item>
        <el-descriptions-item label="复权单位净值">{{ this.StockInfo.adj_nav }}</el-descriptions-item>
      </el-descriptions>
      <el-descriptions
          class="margin-top"
          title="基金分红"
          :column="2"
          :size="size"
          border>
        <el-descriptions-item label="公告日期">{{ this.StockInfo.ann_date }}</el-descriptions-item>
        <el-descriptions-item label="分红实施公告日">{{ this.StockInfo.imp_anndate }}</el-descriptions-item>
        <el-descriptions-item label="分配收益基准日">{{ this.StockInfo.base_date }}</el-descriptions-item>
        <el-descriptions-item label="方案进度">{{ this.StockInfo.div_proc }}</el-descriptions-item>
        <el-descriptions-item label="权益登记日">{{ this.StockInfo.record_date }}</el-descriptions-item>
        <el-descriptions-item label="除息日">{{ this.StockInfo.ex_date }}</el-descriptions-item>
        <el-descriptions-item label="派息日">{{ this.StockInfo.earpay_date }}</el-descriptions-item>
        <el-descriptions-item label="净值除权日">{{ this.StockInfo.net_ex_date }}</el-descriptions-item>
        <el-descriptions-item label="每股派息(元)">{{ this.StockInfo.div_cash }}</el-descriptions-item>
        <el-descriptions-item label="基准基金份额(万份)">{{ this.StockInfo.base_unit }}</el-descriptions-item>
        <el-descriptions-item label="可分配收益(元)">{{ this.StockInfo.ear_distr }}</el-descriptions-item>
        <el-descriptions-item label="收益分配金额(元)">{{ this.StockInfo.ear_amount }}</el-descriptions-item>
        <el-descriptions-item label="红利再投资到账日">{{ this.StockInfo.account_date }}</el-descriptions-item>
        <el-descriptions-item label="份额基准年度">{{ this.StockInfo.base_year }}</el-descriptions-item>
      </el-descriptions>
      <el-descriptions
          class="margin-top"
          title="基金持仓"
          :column="2"
          :size="size"
          border>
        <el-descriptions-item label="公告日期">{{ this.StockInfo.ann_date }}</el-descriptions-item>
        <el-descriptions-item label="截止日期">{{ this.StockInfo.end_date }}</el-descriptions-item>
        <el-descriptions-item label="股票代码">{{ this.StockInfo.symbol }}</el-descriptions-item>
        <el-descriptions-item label="持有股票市值(元)">{{ this.StockInfo.mkv }}</el-descriptions-item>
        <el-descriptions-item label="持有股票数量（股）">{{ this.StockInfo.amount }}</el-descriptions-item>
        <el-descriptions-item label="占股票市值比">{{ this.StockInfo.stk_mkv_ratio }}</el-descriptions-item>
        <el-descriptions-item label="占流通股本比例">{{ this.StockInfo.stk_float_ratio }}</el-descriptions-item>
      </el-descriptions>
    </el-main>
  </el-card>

</template>

<style>
.stockInfoPic{
  width:40px;
  margin-top:10px;
  margin-left:10px;
}
.stockInfoName{
  margin-top:15px;
  font-size:30px;
}
.stockInfoTitle{
  width:250px;
  height:60px;
  background-color: #fff;
}
</style>