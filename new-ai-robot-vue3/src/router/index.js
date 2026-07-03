import { createRouter, createWebHashHistory } from "vue-router";

// 统一声明
const routes = [
    {
        path: '/', //路由地址
        name: 'Index',
        component: () => import('@/views/Index.vue'), // 对应组件
        meta: {
            title: 'AI 机器人首页'
        }
    },
    {
        path: '/chat/:chatId',
        name: 'ChatPage',
        component: () => import('@/views/ChatPage.vue'),
        meta: {
            title: '对话聊天页'
        }
    },
    {
        path: '/customer-service/chat',
        name: 'CustomerServiceChatPage',
        component: () => import('@/views/CustomerServiceChatPage.vue'),
        meta: {
            title: '智能客服聊天页'
        }
    }
]

// 创建路由
const router = createRouter({
    // 指定路由模式 hash 是URL 用#标识
    history: createWebHashHistory(),
    // routes: routes 的缩写
    routes,
})

// ES6 模块导出语句
export default router