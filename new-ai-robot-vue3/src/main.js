import './assets/main.css'

import { createApp } from 'vue'
import App from './App.vue'
// 导入路由
import router from './router'
// 注册SVG
import 'virtual:svg-icons-register'
// 全局管理 Pinia
import { createPinia } from 'pinia'
import piniaPluginPersistedstate from 'pinia-plugin-persistedstate'

const app = createApp(App)

const pinia = createPinia()

// 应用 Pinia
app.use(pinia)
// 应用路由
app.use(router)
app.mount('#app')
