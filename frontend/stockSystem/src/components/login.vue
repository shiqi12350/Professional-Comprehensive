<template>
    <div class="login_register_box">
      <div v-if="showBol" class="login_box">
          <el-form
              ref="ruleFormRef"
              style="max-width: 600px"
              :model="ruleForm"
              status-icon
              :rules="rules"
              label-width="auto"
              class="demo-ruleForm"
          >
              <el-form-item label="userPhone" prop="userPhone">
              <el-input v-model="ruleForm.userPhone" type="text" autocomplete="off" />
              </el-form-item>
              <el-form-item label="Password" prop="pass">
              <el-input
                  v-model="ruleForm.pass"
                  type="password"
                  autocomplete="off"
                  show-password
              />
              </el-form-item>
              <el-form-item>
              <el-button type="primary" @click="submitForm(ruleFormRef)">
                  登录
              </el-button>
              <el-button @click="resetForm(ruleFormRef)">重置</el-button>
              </el-form-item>
          </el-form>
      </div>
      <div v-else class="register_box">
          <el-form
              ref="ruleFormRef"
              style="max-width: 600px"
              :model="ruleFormRegister"
              status-icon
              :rules="rulesRegister"
              label-width="auto"
              class="demo-ruleForm"
          >
              <el-form-item label="userID" prop="userID">
              <el-input v-model="ruleFormRegister.userID" type="text" autocomplete="off" />
              </el-form-item>
              <el-form-item label="userName" prop="userName">
              <el-input
                  v-model="ruleFormRegister.userName"
                  type="text"
                  autocomplete="off"
              />
              </el-form-item>
              <el-form-item label="userPhone" prop="userPhone">
              <el-input v-model="ruleFormRegister.userPhone" type="text" autocomplete="off" />
              </el-form-item>
              <el-form-item label="password" prop="password">
              <el-input v-model="ruleFormRegister.password" type="password" show-password autocomplete="off" />
              </el-form-item>
              <el-form-item label="birth" prop="birth">
              <el-input v-model="ruleFormRegister.birth" type="text" autocomplete="off" />
              </el-form-item>
              <el-form-item label="email" prop="email">
              <el-input v-model="ruleFormRegister.email" type="text" autocomplete="off" />
              </el-form-item>
              <el-form-item>
              <el-button type="primary" @click="submitRegisterForm(ruleFormRef)">
                  注册
              </el-button>
              <el-button @click="resetRegisterForm(ruleFormRef)">重置</el-button>
              </el-form-item>
          </el-form>
      </div>
      <p @click="switchPage()" class="swi_btn">切换登录/注册</p>
    </div>
  </template>
  
  <script setup lang="ts">
  import { ElMessage } from 'element-plus'
  import md5 from 'js-md5';
  import { reactive, ref } from 'vue';
  import type { FormInstance, FormRules } from 'element-plus';
  import axios from 'axios';
  import userInfo from '@/stores/user';
  
  //用于修改vuestore的userID
  const user = userInfo();
  
  
  //切换登录还是注册页面
  const showBol = ref<boolean>(true);
  let switchPage = function(){
      showBol.value = !showBol.value
  }
  // 登录规则的响应方法
  const ruleFormRef = ref<FormInstance>()
  
  const validatePass = (rule: any, value: any, callback: any) => {
    if (value === '') {
      callback(new Error('Please input the password'))
    } else {
      //
      callback()
    }
  }
  const validateuserPhone = (rule: any, value: any, callback: any) => {
    if (value === '') {
      callback(new Error('Please input the userPhone'))
    } else {
      callback()
    }
  }
  
  // 登录表单
  const ruleForm = reactive({
    pass: '',
    userPhone: '',
  })
  
  // 登录规则
  const rules = reactive<FormRules<typeof ruleForm>>({
    pass: [{ validator: validatePass, trigger: 'blur' }],
    userPhone: [{ validator: validateuserPhone, trigger: 'blur' }],
  })
  
  //登录按钮
  const submitForm = (formEl: FormInstance | undefined) => {
    if (!formEl) return
    formEl.validate(async (valid) => {
      if (valid) {
        console.log('submit!')
        console.log(ruleForm.userPhone)
        console.log(ruleForm.pass)
        let data = `?userPhone=${ruleForm.userPhone}&password=${ruleForm.pass}`
        const response = await axios.get(
            "http://8.130.119.249:14100/api/v1/accountManagement/Login"+data
        );
        console.log(response);
        if(response.data.resultCode == 1){
          //登录成功
          ElMessage({
              message: '登录成功',
              type: 'success',
          })
          user.$patch({
            userID:response.data.userID
          })
        }else{
          //登录失败
          ElMessage.error('登录失败')
        }
        
      } else {
        console.log('error submit!')
      }
    })
  }
  
  // 登录重置按钮
  const resetRegisterForm = (formEl: FormInstance | undefined) => {
    if (!formEl) return
    formEl.resetFields()
  }
  
  
  //注册
  
  //注册表单
  const ruleFormRegister = reactive({
    userID:'',
    userName:'',
    userPhone:'',
    password:'',
    birth: '',
    email: '',
  })
  
  // 注册规则
  const validateuser = (rule: any, value: any, callback: any) => {
    if (value === '') {
      callback(new Error('Please input'))
    } else {
      callback()
    }
  }
  const rulesRegister = reactive<FormRules<typeof ruleForm>>({
    userID: [{ validator: validateuser, trigger: 'blur' }],
    userName: [{ validator: validateuser, trigger: 'blur' }],
    userPhone: [{ validator: validateuser, trigger: 'blur' }],
    password: [{ validator: validateuser, trigger: 'blur' }],
    birth: [{ validator: validateuser, trigger: 'blur' }],
    email: [{ validator: validateuser, trigger: 'blur' }]
  })
  
  
  
  // 注册按钮
  const submitRegisterForm = (formEl: FormInstance | undefined) => {
    if (!formEl) return
    formEl.validate(async (valid) => {
      if (valid) {
        console.log('submit!')
        
        let data = new URLSearchParams();
      data.append("userID",ruleFormRegister.userID)
      data.append("userName",ruleFormRegister.userName)
      data.append("userPhone",ruleFormRegister.userPhone)
      data.append("password",ruleFormRegister.password)
      data.append("birth",ruleFormRegister.birth)
      data.append("email",ruleFormRegister.email)
      const response = await axios.post(
          "http://8.130.119.249:14100/api/v1/accountManagement/Register",
          data,
      );
        console.log(response);
        if(response.data.resultCode == 1){
          //登录成功
          ElMessage({
              message: '注册成功',
              type: 'success',
          })
          user.$patch({
            userID:ruleFormRegister.userID
          })
        }else if(response.data.resultCode == -201){
          //注册失败
          ElMessage.error('身份证已存在')
        }else{
          //注册失败
          ElMessage.error('手机号已存在')
        }
        
      } else {
        console.log('error submit!')
      }
    })
  }
  
  // 注册重置按钮
  const resetForm = (formEl: FormInstance | undefined) => {
    if (!formEl) return
    formEl.resetFields()
  }
  </script>
  
  <style>
  .login_register_box{
      display: flex;
      flex-direction: column;
      justify-content: center;
      height: 100%;
      align-items: center;
  }
  .swi_btn{
      font-size: small;
  }
  .swi_btn:hover{
      color: red;
      cursor: pointer;
  }
  </style>