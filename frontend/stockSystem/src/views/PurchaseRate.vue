<template>
    <el-row>
      <el-col :span="10">
        <p slot="header" style="height:40px; line-height:40px; font-weight: bold; background-color: #f2f2f2; color: #333; text-align: center;">认/申购费率计算</p>
        <el-form :model="formData" label-width="100px">

          <!--基金ID查询-->
          <el-form-item label="基金代码">
            <el-input v-model="fundId" placeholder="申购基金编号"></el-input>
            <el-button size="small" @click="getFundNetValue">查询</el-button>
          </el-form-item>

          <el-form-item label="申购金额">
            <el-input v-model="formData.purchaseAmount" placeholder="请输入金额(元)"></el-input>
          </el-form-item>
          <el-form-item label="单位净值">
            <el-input v-model="formData.netValue" placeholder="请输入净值(元)"></el-input>
          </el-form-item>
          <el-form-item label="申购费率">
            <el-input v-model="formData.purchaseRate" placeholder="请输入费率(%)"></el-input>
          </el-form-item>
          <el-form-item>
            <el-button type="primary" @click="calculate">计算</el-button>
          </el-form-item>
        </el-form>
      </el-col>
      <el-col :span="4">

      </el-col>
      <el-col :span="10">
        <p slot="header" style=" height:40px; line-height:40px; font-weight: bold; background-color: #f2f2f2; color: #333; text-align: center;">计算结果</p>
        <p>申购手续费：{{ result.purchaseFee }} 元</p>
        <p>申购成交份额：{{ result.purchaseShares }} 份</p>
      </el-col>
    </el-row>

    <el-row>
      <el-col :span="10">
        <p slot="header" style="height:40px; line-height:40px; font-weight: bold; background-color: #f2f2f2; color: #333; text-align: center;">赎回费率计算</p>
        <el-form :model="formData" label-width="120px">

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
            <el-input v-model.number="formData.redemptionShares"></el-input>&nbsp元
          </el-form-item>
          <el-form-item label="赎回净值">
            <el-input v-model.number="formData.redemptionNetValue"></el-input>&nbsp元
          </el-form-item>
          <el-form-item label="赎回费率 ">
            <el-input v-model.number="formData.redemptionRate"></el-input>&nbsp%
          </el-form-item>
          <el-form-item label="申购净值">
            <el-input v-model.number="formData.purchaseNetValue"></el-input>&nbsp元
          </el-form-item>
          <el-form-item label="后端申购费率">
            <el-input v-model.number="formData.backendPurchaseRate"></el-input>&nbsp%
          </el-form-item>
          <el-form-item>
            <el-button type="primary" @click="calculate">计算</el-button>
          </el-form-item>
        </el-form>
      </el-col>
      <el-col :span="4"></el-col>
      <el-col :span="10">
        <p slot="header" style=" height:40px; line-height:40px; font-weight: bold; background-color: #f2f2f2; color: #333; text-align: center;">计算结果</p>
        <p>赎回手续费：{{ Math.round(result.redemptionFee * 100) / 100 }} 元</p>
        <p>后端申购手续费：{{ Math.round(result.backendPurchaseFee * 100) / 100  }} 元</p>
        <p>实际所得金额：{{ Math.round(result.actualAmount * 100) / 100 }} 元</p>
      </el-col>
    </el-row>

 
</template>

<script>
 import axios from "axios";

export default {
  data() {
    return {
      formData: {
        purchaseAmount: null,
        netValue: null,
        purchaseRate: null
      },
      fundId: '', //输入的基金ID
      result: {
        purchaseFee: null,
        purchaseShares: null
      }
    };
  },
  methods: {
    calculate() {
      const purchaseAmount = parseFloat(this.formData.purchaseAmount);
      const netValue = parseFloat(this.formData.netValue);
      const purchaseRate = parseFloat(this.formData.purchaseRate)*0.01;

      const purchaseFee = (purchaseAmount / (1 + purchaseRate)) * purchaseRate;
      const purchaseShares = (purchaseAmount / (1 + purchaseRate)) / netValue;

      this.result.purchaseFee = purchaseFee.toFixed(2);
      this.result.purchaseShares = purchaseShares.toFixed(2);
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
        this.formData.netValue = unitNav;
      } catch (error) {
        console.error('Error fetching fund nav:', error);
      }
    },
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
</style>