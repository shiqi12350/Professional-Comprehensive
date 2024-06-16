import './assets/main.css'
import './assets/font.css'
import ElementPlus from 'element-plus';
import 'element-plus/dist/index.css';
import * as echarts from 'echarts';

import { createApp } from 'vue'
import { createPinia } from 'pinia'

createPinia()

import App from './App.vue'
import router from './router'

const app = createApp(App)

app.use(createPinia())
app.use(router)
app.use(ElementPlus)
app.mount('#app')
