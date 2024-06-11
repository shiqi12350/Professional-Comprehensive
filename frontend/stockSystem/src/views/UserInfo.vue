<template>
  <div>
        <el-card>
          <!-- User Information Section -->
          <el-form ref="userInfoForm" label-width="70px">
            <el-form-item label="身份证号">
              <el-input v-model="userInfo.userID" :disabled="true"></el-input>
            </el-form-item>
            <el-form-item label="姓名">
              <el-input v-model="userInfo.userName" :disabled="!isEditable"></el-input>
            </el-form-item>
            <el-form-item label="电话号码">
              <el-input v-model="userInfo.userPhone" :disabled="!isEditable"></el-input>
            </el-form-item>
            <el-form-item label="密码">
              <el-input v-model="userInfo.passWord" :disabled="!isEditable" type="password" @change="handlePasswordChange"></el-input>
            </el-form-item>
            <el-form-item label="确认密码" v-if="showConfirmPassword">
              <el-input v-model="confirmPassword" type="password"></el-input>
            </el-form-item>
            <el-form-item label="生日">
              <el-date-picker v-model="userInfo.birth" type="date" :disabled="!isEditable"></el-date-picker>
            </el-form-item>
            <el-form-item label="邮箱">
              <el-input v-model="userInfo.email" :disabled="!isEditable"></el-input>
            </el-form-item>
            <el-form-item label="等级">
              <el-input v-model="userInfo.level" :disabled="true"></el-input>
            </el-form-item>
            <el-button type="primary" size="small" @click="editUserInfo">{{ isEditable ? '保存' : '修改' }}</el-button>
          </el-form>
        </el-card>
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
      isEditable: false,
      showConfirmPassword: false,
      confirmPassword: '',   
    };
  },
  methods: {
    //用户信息
    getUserInfo() {
      axios.get('http://8.130.119.249:14100/api/v1/accountManagement/GetUserInfo', {
        params: {
          userID: '450303200308052030'
        }
      }).then(response => {
        if (response.data.resultCode === 1) {
          this.userInfo = response.data;
        } else {
          console.error('Error: Information not trustworthy. Error Code: -101');
        }
      }).catch(error => {
        console.error('Error retrieving user information:', error);
      });
    },
    getTransactionHistory() {
      axios.get('http://8.130.119.249:14103/api/v1/TradeManagement/Getorder', {
        params: {
          TraderID: '450303200308052030'
        }
      }).then(response => {
        this.transactionHistory = response.data;
      }).catch(error => {
        console.error('Error retrieving transaction history:', error);
      });
    },
    editUserInfo() {
      if (this.isEditable) {
        axios.get('http://8.130.119.249:14100/api/v1/accountManagement/ModifyUserInfo', {
          params: {
            userID: this.userInfo.userID,
            userName: this.userInfo.userName,
            userPhone: this.userInfo.userPhone,
            password: this.userInfo.passWord,
            birth: this.userInfo.birth,
            email: this.userInfo.email
          }
        }).then(response => {
          if (response.data.resultCode === 1) {
            alert('用户信息修改成功');
            this.isEditable = false;
            this.showConfirmPassword = false;
            this.confirmPassword = '';
          } else {
            console.error('Error: User ID does not exist. Error Code: -101');
          }
        }).catch(error => {
          console.error('Error modifying user information:', error);
        });
      } else {
        const password = prompt('请输入密码');
        if (password === this.userInfo.passWord) {
          this.isEditable = true;
        } else {
          alert('密码错误');
        }
      }
    },
    handlePasswordChange() {
      this.showConfirmPassword = true;
    },
  },
  mounted() {
    this.getUserInfo();
  }
};
</script>

<style>
 .el-form-item__label {
    font-size: 12px; /* 调整表单项标签字体大小 */
 }
.el-input{
  font-size:12px;
}
.el-card{
  margin-left:20px;
}
</style>

