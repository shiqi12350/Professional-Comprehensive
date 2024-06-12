<template>
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
  
    <p slot="header" style=" height:40px; line-height:40px; font-weight: bold; background-color: #f2f2f2; color: #333; text-align: center;">计算结果</p>
    <p>赎回手续费：{{ Math.round(result.redemptionFee * 100) / 100 }} 元</p>
    <p>后端申购手续费：{{ Math.round(result.backendPurchaseFee * 100) / 100  }} 元</p>
    <p>实际所得金额：{{ Math.round(result.actualAmount * 100) / 100 }} 元</p>

  </template>
  
  <script>
  import axios from "axios";

  export default {
    data() {
      return {
        formData: {
          redemptionShares: null,
          redemptionNetValue: null,
          redemptionRate: null,
          purchaseNetValue: null,
          backendPurchaseRate: null
        },
        result: {
          redemptionFee: null,
          backendPurchaseFee: null,
          actualAmount: null
        },
        selectedOrder: null,
        orderOptions: [],

      };
    },
    methods: {
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
        this.formData.redemptionShares = this.selectedOrder.share;
        this.formData.redemptionRate = this.selectedOrder.Rate;

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
    this.formData.redemptionNetValue = unitNav;
  } catch (error) {
    console.error('Error fetching fund nav:', error);
  }
},
      calculate() {
        this.result.redemptionFee = (this.formData.redemptionShares * this.formData.redemptionNetValue * this.formData.redemptionRate) / 100;
        if (this.formData.purchaseNetValue && this.formData.backendPurchaseRate) {
          this.result.backendPurchaseFee = (this.formData.redemptionShares * this.formData.purchaseNetValue * this.formData.backendPurchaseRate / 100) / (1 + (this.formData.backendPurchaseRate / 100));
        } else {
          this.result.backendPurchaseFee = 0;
        }
        this.result.actualAmount = this.formData.redemptionShares * this.formData.redemptionNetValue - this.result.redemptionFee - this.result.backendPurchaseFee;
      }
    },
    mounted() {
      this.fetchOrders('450303200308052030');
    }
  };
  </script>

  <style>
  .el-select{
    width:450px;
  }
  .el-input{
  width:300px;
}
  </style>