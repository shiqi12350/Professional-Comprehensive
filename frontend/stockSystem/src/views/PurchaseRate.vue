<template>
  <el-card>
    <template #header>
      <el-row>
        <el-col :span="2">
          <div class="calculatorTitle">
            <img src="@/resource/calculator.png" class="calculatorPic">
          </div>
        </el-col>
        <el-col :span="22">
          <div class="calculatorName">基金计算器</div>
        </el-col>
      </el-row>
    </template>
    <el-row>
      <el-col :span="10">
        <p slot="header" style="height:40px; line-height:40px; font-weight: bold; background-color: #f2f2f2; color: #333; text-align: center;">认/申购费率计算</p>
        <el-form :model="purchaseFormData" label-width="100px">

          <!--基金ID查询-->
          <el-form-item label="基金代码">
            <el-input v-model="fundId" placeholder="申购基金编号"></el-input>
            <el-button size="small" @click="getFundNetValue">查询</el-button>
          </el-form-item>

          <el-form-item label="申购金额">
            <el-input v-model="purchaseFormData.purchaseAmount" placeholder="请输入金额(元)"></el-input>
          </el-form-item>
          <el-form-item label="单位净值">
            <el-input v-model="purchaseFormData.netValue" placeholder="请输入净值(元)"></el-input>
          </el-form-item>
          <el-form-item label="申购费率">
            <el-input v-model="purchaseFormData.purchaseRate" placeholder="请输入费率(%)"></el-input>
          </el-form-item>
          <el-form-item>
            <el-button type="primary" @click="calculatePurchase">计算</el-button>
          </el-form-item>
        </el-form>
      </el-col>
      <el-col :span="4">

      </el-col>
      <el-col :span="10">
        <p slot="header" style=" height:40px; line-height:40px; font-weight: bold; background-color: #f2f2f2; color: #333; text-align: center;">计算结果</p>
        <p>申购手续费：{{ purchaseResult.purchaseFee }} 元</p>
        <p>申购成交份额：{{ purchaseResult.purchaseShares }} 份</p>
      </el-col>
    </el-row>

    <el-row>
      <el-col :span="10">
        <p slot="header" style="height:40px; line-height:40px; font-weight: bold; background-color: #f2f2f2; color: #333; text-align: center;">赎回费率计算</p>
        <el-form :model="redeemFormData" label-width="120px">

          <!--订单选择-->
          <el-form-item label="选择订单">
            <el-select v-model="selectedOrder" placeholder="请选择订单" value-key="ordernumber" @change="handleOrderChange">
              <el-option
                  v-for="order in orderOptions"
                  :key="order.ordernumber"
                  :label="`单号：${order.ordernumber}&nbsp|&nbsp基金：${order.fundid} &nbsp|&nbsp份额：${order.share.toFixed(2)}`"
                  :value="order"
              ></el-option>
            </el-select>
          </el-form-item>

          <el-form-item label="赎回份额">
            <el-input v-model.number="redeemFormData.redemptionShares"></el-input>&nbsp元
          </el-form-item>
          <el-form-item label="赎回净值">
            <el-input v-model.number="redeemFormData.redemptionNetValue"></el-input>&nbsp元
          </el-form-item>
          <el-form-item label="赎回费率 ">
            <el-input v-model.number="redeemFormData.redemptionRate"></el-input>&nbsp%
          </el-form-item>
          <el-form-item label="申购净值">
            <el-input v-model.number="redeemFormData.purchaseNetValue"></el-input>&nbsp元
          </el-form-item>
          <el-form-item label="后端申购费率">
            <el-input v-model.number="redeemFormData.backendPurchaseRate"></el-input>&nbsp%
          </el-form-item>
          <el-form-item>
            <el-button type="primary" @click="calculateRedeem">计算</el-button>
          </el-form-item>
        </el-form>
      </el-col>
      <el-col :span="4"></el-col>
      <el-col :span="10">
        <p slot="header" style=" height:40px; line-height:40px; font-weight: bold; background-color: #f2f2f2; color: #333; text-align: center;">计算结果</p>
        <p>赎回手续费：{{ Math.round(redeemResult.redemptionFee * 100) / 100 }} 元</p>
        <p>后端申购手续费：{{ Math.round(redeemResult.backendPurchaseFee * 100) / 100 }} 元</p>
        <p>实际所得金额：{{ Math.round(redeemResult.actualAmount * 100) / 100 }} 元</p>
      </el-col>
    </el-row>
  </el-card>
</template>

<script>
 import axios from "axios";

export default {
  data() {
    return {
      purchaseFormData: {
        purchaseAmount: null,
        netValue: null,
        purchaseRate: null
      },
      fundId: '', //输入的基金ID
      purchaseResult: {
        purchaseFee: null,
        purchaseShares: null
      },
      redeemFormData: {
        redemptionShares: null,
        redemptionNetValue: null,
        redemptionRate: null,
        purchaseNetValue: null,
        backendPurchaseRate: null
      },
      redeemResult: {
        redemptionFee: null,
        backendPurchaseFee: null,
        actualAmount: null
      },
      selectedOrder: null,
      orderOptions: [],
    };
  },
  methods: {
    calculatePurchase() {
      const purchaseAmount = parseFloat(this.purchaseFormData.purchaseAmount);
      const netValue = parseFloat(this.purchaseFormData.netValue);
      const purchaseRate = parseFloat(this.purchaseFormData.purchaseRate)*0.01;

      const purchaseFee = (purchaseAmount / (1 + purchaseRate)) * purchaseRate;
      const purchaseShares = (purchaseAmount / (1 + purchaseRate)) / netValue;

      this.purchaseResult.purchaseFee = purchaseFee.toFixed(2);
      this.purchaseResult.purchaseShares = purchaseShares.toFixed(2);
    },
    async getFundNetValue() {
      try {
        const response = await axios.post(
          "http://api.tushare.pro",
          {
            api_name: "fund_nav",
            token: "91a073e655c1849334691d5f5c71a518ff5468891554887fad2afca0",
            params: {
              "ts_code": this.fundId,
              "start_date": "20200101",
              "end_date": "20200107"
            }
          }
        );
        const unitNav = response.data.data.items[0][3];
        this.purchaseFormData.netValue = unitNav;
      } catch (error) {
        console.error('Error fetching fund nav:', error);
      }
    },
    async fetchOrders(traderId) {
      try {
        const response = await fetch(`http://8.130.119.249:14103/api/v1/TradeManagement/Getorder?TraderID=${traderId}`);
        const data = await response.json();
        this.orderOptions = data;
      } catch (error) {
        console.error('Error fetching orders:', error);
      }
    },
    handleOrderChange() {
      this.redeemFormData.redemptionShares = this.selectedOrder.share;
      this.redeemFormData.redemptionRate = this.selectedOrder.Rate;

      this.getFundNav(this.selectedOrder.fundid);
    },
    async getFundNav(fundId) {
      try {
        const response = await axios.post(
            "http://api.tushare.pro",
            {
              api_name: "fund_nav",
              token: "91a073e655c1849334691d5f5c71a518ff5468891554887fad2afca0",
              params: {
                "ts_code": fundId,
                "start_date": "20200101",
                "end_date": "20200107"
              }
            }
        );
        const unitNav = response.data.data.items[0][3];
        this.redeemFormData.redemptionNetValue = unitNav;
      } catch (error) {
        console.error('Error fetching fund nav:', error);
      }
    },
    calculateRedeem() {
      this.redeemResult.redemptionFee = (this.redeemFormData.redemptionShares * this.redeemFormData.redemptionNetValue * this.redeemFormData.redemptionRate) / 100;
      if (this.redeemFormData.purchaseNetValue && this.redeemFormData.backendPurchaseRate) {
        this.redeemResult.backendPurchaseFee = (this.redeemFormData.redemptionShares * this.redeemFormData.purchaseNetValue * this.redeemFormData.backendPurchaseRate / 100) / (1 + (this.redeemFormData.backendPurchaseRate / 100));
      } else {
        this.redeemResult.backendPurchaseFee = 0;
      }
      this.redeemResult.actualAmount = this.redeemFormData.redemptionShares * this.redeemFormData.redemptionNetValue - this.redeemResult.redemptionFee - this.redeemResult.backendPurchaseFee;
    }
  },
  mounted() {
    this.fetchOrders('450303200308052030');
  }
};
</script>

<style scoped>
.el-card__body {
  padding: 20px;
}
.el-input{
  width:200px;
}
.calculatorPic{
  width:40px;
  margin-top:10px;
  margin-left:10px;
}
.calculatorName{
  margin-top:15px;
  font-size:30px;
}
.calculatorTitle{
  width:250px;
  height:60px;
  background-color: #fff;
}
</style>