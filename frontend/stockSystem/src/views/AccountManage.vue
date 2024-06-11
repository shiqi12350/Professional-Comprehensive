<template>
    <div>
      <el-row>
        <el-col :span="7">
          <el-card>
            <!--bankcard section-->
            <el-table :data="bankCardList" style="width: 100%">
              <el-table-column prop="bankCardNumber" label="银行卡号"></el-table-column>
              <el-table-column label="操作">
                <template slot-scope="scope">
                  <el-button type="danger" @click="unbindBankCard(scope.row.bankCardNumber)">解绑</el-button>
                </template>
              </el-table-column>
            </el-table>
            <el-button type="primary" size="small" @click="bindBankCardDialogVisible = true">绑定</el-button>
  
            <el-dialog title="绑定银行卡" v-model="bindBankCardDialogVisible">
              <el-form ref="bindBankCardForm" label-width="80px">
                <el-form-item label="银行卡号">
                  <el-input v-model="newBankCardNumber"></el-input>
                </el-form-item>
              </el-form>
              <el-button type="primary" @click="bindBankCard">确认</el-button>
            </el-dialog>
          </el-card>
        </el-col>
  
        <el-col :span="17">
          <!--银行卡持仓修改部分-->
          <el-card>
        <el-form label-width="80px">
          <el-form-item label="选择银行卡">
            <el-select v-model="selectedBankCard" placeholder="请选择银行卡" @change="getFundHoldings">
              <el-option
                v-for="card in bankCardList"
                :key="card.bankCardNumber"
                :label="card.bankCardNumber"
                :value="card.bankCardNumber"
              ></el-option>
            </el-select>
          </el-form-item>
        </el-form>
  
        <el-table :data="fundHoldings" style="width: 100%">
          <el-table-column prop="fundID" label="基金代码"></el-table-column>
          <el-table-column prop="amount" label="持仓份额"></el-table-column>
          <el-table-column label="操作">
            <template slot-scope="scope">
              <el-input v-model="scope.row.newAmount" placeholder="输入新份额"></el-input>
              <el-button type="primary" @click="modifyFundHoldings(scope.row)">修改</el-button>
            </template>
          </el-table-column>
        </el-table>
      </el-card>
  
        </el-col>
      </el-row>
    </div>
  </template>
  
  <script>
  import axios from 'axios';
  
  export default {
    data() {
      return {
        userInfo: {
          userID: '450303200308052030',
          userName: '',
          userPhone: '',
          passWord: '',
          birth: '',
          email: '',
          level: ''
        },
        //银行卡部分
        bankCardList: [], 
        bindBankCardDialogVisible: false,//绑定部分
        newBankCardNumber: '',
  
        selectedBankCard: '',//持仓部分
        fundHoldings: []
      };
    },
    methods: {
      
      //银行卡绑定
      getBankCardList() {
        axios.get('http://8.130.119.249:14100/api/v1/bankcard/getBankCardList', {
          params: {
            userID: '450303200308052030'
          }
        }).then(response => {
          if (response.data.resultCode === 1) {
            this.bankCardList = response.data.bankCardList.map(cardNumber => ({ bankCardNumber: cardNumber }));
          } else {
            console.error('Error: Bank card list not trustworthy. Error Code: -101');
          }
        }).catch(error => {
          console.error('Error retrieving bank card list:', error);
        });
      },

      bindBankCard() {
        //axios.post('http://8.130.119.249:14100/api/v1/bankcard/bindBankCard',
        axios.post('http://8.130.119.249:14100/api/v1/accountManagement',  {
          userID: '450303200308052030',
          cardID: this.newBankCardNumber,
          SPK: 'oA5XHdFaJ9'
        }).then(response => {
          if (response.data.resultCode === 1) {
            alert('银行卡绑定成功');
            this.bindBankCardDialogVisible = false;
            this.getBankCardList();
          } else {
            console.error('Error binding bank card. Error Code:', response.data.resultCode);
          }
        }).catch(error => {
          console.error('Error binding bank card:', error);
        });
      },

      unbindBankCard(cardNumber) {
        axios.post('http://8.130.119.249:14100/api/v1/bankcard/dismissBankCard', {
          userID: '450303200308052030',
          cardID: cardNumber
        }).then(response => {
          if (response.data.resultCode === 1) {
            alert('银行卡解绑成功');
            this.getBankCardList();
          } else {
            console.error('Error unbinding bank card. Error Code:', response.data.resultCode);
          }
        }).catch(error => {
          console.error('Error unbinding bank card:', error);
        });
      },
      
      //持仓修改
      getFundHoldings() {
        axios.get('http://8.130.119.249:14100/api/v1/bankcard/getAllPortion', {
          params: {
            cardID: this.selectedBankCard
          }
        }).then(response => {
          if (response.data.resultCode === 1) {
            this.fundHoldings = response.data.info;
            this.fundHoldings.forEach(item => {
              item.newAmount = item.amount; // Initialize newAmount for each fund holding
            });
          } else {
            console.error('Error: Fund holdings not trustworthy. Error Code: -104');
          }
        }).catch(error => {
          console.error('Error retrieving fund holdings:', error);
        });
      },

      modifyFundHoldings(fund) {
        axios.post('http://8.130.119.249:14100/api/v1/bankcard/modifyPortion', {
          cardID: this.selectedBankCard,
          fundID: fund.fundID,
          amount: fund.newAmount
        }).then(response => {
          if (response.data.resultCode === 1) {
            alert('基金份额修改成功');
            this.getFundHoldings(); // Refresh fund holdings after modification
          } else {
            console.error('Error modifying fund holdings. Error Code:', response.data.resultCode);
          }
        }).catch(error => {
          console.error('Error modifying fund holdings:', error);
        });
      }
    },
    mounted() {
      this.getBankCardList();
    }
  };
  </script>
  
  <style>
   .el-form-item__label {
      font-size: 12px; /* 调整表单项标签字体大小 */
   }
  .el-input{
    font-size:10px;
  }
  .el-card{
    margin-left:6px;
  }
  .el-table{
    font-size:12px;
  }
  </style>
  