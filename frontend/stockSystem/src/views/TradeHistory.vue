<template>
    <div>
      <el-table
        v-if="currentPageOrdersType1.length > 0"
        :data="currentPageOrdersType1"
        style="margin-bottom: 20px"
        border
        stripe
      >
        <el-table-column prop="ordernumber" label="Order Number"></el-table-column>
        <el-table-column prop="company" label="Company"></el-table-column>
        <el-table-column prop="share" label="Share"></el-table-column>
        <el-table-column prop="SubmissionAmount" label="Submission Amount"></el-table-column>
        <el-table-column prop="SubmissionTime" label="Submission Time"></el-table-column>
        <el-table-column prop="ConfirmTime" label="Confirm Time"></el-table-column>
      </el-table>
  
      <el-pagination
        @current-change="handleCurrentChangeType1"
        :current-page="currentPageType1"
        :page-size="5"
        :total="totalOrdersType1.length"
        layout="prev, pager, next"
      ></el-pagination>
  
      <el-table
        v-if="currentPageOrdersType2.length > 0"
        :data="currentPageOrdersType2"
        style="margin-bottom: 20px"
        border
        stripe
      >
        <el-table-column prop="ordernumber" label="Order Number"></el-table-column>
        <el-table-column prop="company" label="Company"></el-table-column>
        <el-table-column prop="share" label="Share"></el-table-column>
        <el-table-column prop="SubmissionAmount" label="Submission Amount"></el-table-column>
        <el-table-column prop="SubmissionTime" label="Submission Time"></el-table-column>
        <el-table-column prop="ConfirmTime" label="Confirm Time"></el-table-column>
      </el-table>
  
      <el-pagination
        @current-change="handleCurrentChangeType2"
        :current-page="currentPageType2"
        :page-size="5"
        :total="totalOrdersType2.length"
        layout="prev, pager, next"
      ></el-pagination>
    </div>
  </template>
  
  <script>
  import axios from 'axios';
  
  export default {
    data() {
      return {
        totalOrdersType1: [],
        totalOrdersType2: [],
        currentPageOrdersType1: [],
        currentPageOrdersType2: [],
        currentPageType1: 1,
        currentPageType2: 1,
        userID: '450303200308052030',
        apiUrl: 'http://8.130.119.249:14103/api/v1/TradeManagement/Getorder'
      };
    },
    created() {
      this.fetchTradeHistory();
    },
    methods: {
      fetchTradeHistory() {
        axios.get(this.apiUrl, {
          params: {
            TraderID: this.userID
          }
        })
        .then(response => {
          this.totalOrdersType1 = response.data.filter(order => order.ordertype === '1');
          this.totalOrdersType2 = response.data.filter(order => order.ordertype === '2');
          this.paginateOrders();
        })
        .catch(error => {
          console.error('Error fetching trade history:', error);
        });
      },
      paginateOrders() {
        const startIndexType1 = (this.currentPageType1 - 1) * 5;
        this.currentPageOrdersType1 = this.totalOrdersType1.slice(startIndexType1, startIndexType1 + 5);
  
        const startIndexType2 = (this.currentPageType2 - 1) * 5;
        this.currentPageOrdersType2 = this.totalOrdersType2.slice(startIndexType2, startIndexType2 + 5);
      },
      handleCurrentChangeType1(page) {
        this.currentPageType1 = page;
        this.paginateOrders();
      },
      handleCurrentChangeType2(page) {
        this.currentPageType2 = page;
        this.paginateOrders();
      }
    }
  };
  </script>