import { fileURLToPath, URL } from 'node:url'

import { defineConfig } from 'vite'
import vue from '@vitejs/plugin-vue'

// https://vitejs.dev/config/
export default defineConfig({
  // proxy: {
  //   '/api': {
  //     target: 'http://8.130.119.249:14103',
  //     changeOrigin: true,
  //     PathRewrite:{
  //       '^/api': '/'
  //     }
  //   },
  // },
  plugins: [
    vue(),
  ],
  resolve: {
    alias: {
      '@': fileURLToPath(new URL('./src', import.meta.url))
    }
  }
})
