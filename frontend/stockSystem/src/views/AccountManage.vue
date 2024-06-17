<template>
    <el-card>
      <template #header>
        <el-row>
          <el-col :span="2">
            <div class="accountTitle">
              <img src="@/resource/account.png" class="accountPic">
            </div>
          </el-col>
          <el-col :span="22">
            <div class="accountName">持仓管理</div>
          </el-col>
        </el-row>
      </template>
      <el-card>
        <el-button type="primary"  @click="bindBankCardDialogVisible = true">绑定</el-button>
        <el-table :data="bankCardList" @row-click="handleRowClick" style="margin-top: 20px">
          <el-table-column prop="bankCard" label="银行卡号"></el-table-column>
          <el-table-column label="操作">
            <template #default="{ row }">
              <el-button type="primary" @click="dismissBankCard(row.bankCard)">解绑</el-button>
            </template>
          </el-table-column>
        </el-table>
      </el-card>
    </el-card>

  
    <!--绑定银行卡输入框-->
    <el-dialog title="绑定银行卡" v-model="bindBankCardDialogVisible">
      <el-form :model="bindBankCardForm" label-width="120px">
        <el-form-item label="银行卡号">
          <el-input v-model="bindBankCardForm.bankCard"></el-input>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="bindBankCard">确认绑定</el-button>
        </el-form-item>
      </el-form>
    </el-dialog>
  
    <el-card>
    <el-table :data="portionInfo" style="width: 100%">
        <el-table-column prop="fundID" label="基金ID"></el-table-column>
        <el-table-column prop="amount" label="持仓份额"></el-table-column>
        <el-table-column prop="close" label="收盘价"></el-table-column>
        <el-table-column prop="pct_chg" label="涨跌幅"></el-table-column>
    </el-table>
    </el-card>
  
  </template>
  
  <script>
  import axios from 'axios';
  
  export default {
    data() {
      return {
        formData: {
          userID: '450303200308052030',
          bankCard: ''
        },
        bankCardList: [],
        bindBankCardDialogVisible: false,
        bindBankCardForm: {
          bankCard: ''
        },
        pagedBankCardList: [],
  
        //绑定
        portionInfo: []
      };
    },
    async created() {
      await this.getBankCard();
    },
    methods: {
      async getBankCard() {
        try {
          const response = await axios.get('http://8.130.119.249:14100/api/v1/accountManagement/getBankCardList', {
            params: {
              userID: this.formData.userID
            }
          });
          console.log(response)
          if (response.data.resultCode === 1) {
            this.formData.bankCard = response.data.bankCardList[0];
            this.bankCardList.push({ bankCard: response.data.bankCardList[0] });
          } else {
            alert('查询失败，错误代码：' + response.data.resultCode);
          }
        } catch (error) {
          alert('查询失败，错误信息：' + error.message);
        }
      },
      async dismissBankCard(cardID) {
        try {
          const response = await axios.get('http://8.130.119.249:14100/api/v1/accountManagement/dismissBankCard', {
            params:{
              userID: this.formData.userID,
              cardID: cardID
            }
          });
          if (response.data.resultCode === 1) {
            alert('解绑成功');
            this.bankCardList = this.bankCardList.filter(item => item.bankCard !== cardID);
          } else {
            alert('解绑失败，错误代码：' + response.data.resultCode);
          }
        } catch (error) {
          alert('解绑错误：' + error.message);
        }
      },
      async bindBankCard() {
        try {
          const response = await axios.get('http://8.130.119.249:14100/api/v1/accountManagement/bindBankCard', {
            params:{
              userID: this.formData.userID,
              cardID: this.bindBankCardForm.bankCard,
              SPK: 'oA5XHdFaJ9'
            }
          });
          console.log(response)
          if (response.data.resultCode === 1) {
            alert('银行卡绑定成功');
            this.bankCardList.push({ bankCard: this.bindBankCardForm.bankCard });
            this.bindBankCardDialogVisible = false;
            this.bindBankCardForm.bankCard = '';
          } else {
            alert('绑定失败，错误代码：' + response.data.resultCode);
          }
        } catch (error) {
          alert('绑定失败，错误信息：' + error.message);
        }
      },
      async handleRowClick(row) {
        await this.getPortionInfo(row.bankCard);
        //仅使用行对应的卡号调用getPortionInfo，目的是便利修改份额后刷新portionInfo
      },
      async getPortionInfo(cardID) {
        this.portionInfo = [];
        const response = await axios.get('http://8.130.119.249:14100/api/v1/accountManagement/getAllPortion', {
          params: {
            cardID: cardID
          }
        });
        if (response.data.resultCode === 1) {
          this.portionInfo = response.data.info;
          for(let i = 0; i < this.portionInfo.length; i++){

            const response2 = await axios.post(
                "http://api.tushare.pro",
                {
                  api_name: "fund_daily",
                  token: "91a073e655c1849334691d5f5c71a518ff5468891554887fad2afca0",
                  params: {
                    "ts_code": this.portionInfo[i].fundID,
                    "start_date": "20200101",
                    "end_date": "20200107"
                  }
                }
            );
            this.portionInfo[i].close= response2.data.data.items[0][5];
            this.portionInfo[i].pct_chg= response2.data.data.items[0][8];
          }
        }
      },
  },
};
</script>

<style>
.el-card{
  margin-top:10px;
}
.accountPic{
   width:40px;
   margin-top:10px;
   margin-left:10px;
 }
.accountName{
  margin-top:15px;
  font-size:30px;
}
.accountTitle{
  width:250px;
  height:60px;
  background-color: #fff;
}
</style>
