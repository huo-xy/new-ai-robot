<template>
    <Layout>
        <!-- 主内容 -->
        <template #main-content>
            <div class="h-screen flex flex-col overflow-y-auto" ref="chatContainer">
                <!-- 聊天记录区域 -->
                <div class="flex-1 max-w-3xl mx-auto pb-24 pt-4 px-4 w-full">
                    <template v-for="(chat, index) in chatList" :key="index">
                        <!-- 用户提问消息 靠右 -->
                        <div v-if="chat.role == 'user'" class="flex justify-end mb-4">
                            <div class="question-container">
                                <p>{{ chat.content }}</p>
                            </div>
                        </div>

                        <!-- 大模型回复消息（左） -->
                        <div v-else class="flex mb-4">
                            <div class="shrink-0 mr-3">
                                <div class="w-8 h-8 rounded-full flex items-center justify-center border border-gray-200">
                                    <SvgIcon name="deepseek-logo" customCss="w-5 h-5"></SvgIcon>
                                </div>
                            </div>
                            <!-- 回复的内容 -->
                            <div class="p-1 max-w-[90%] mb-2">
                                <LoadingDots v-if="chat.loading" />
                                <!-- 推理过程展示 -->
                                <div v-if="chat.reasoning" class=" text-gray-500 mb-5">
                                    <div class="mb-1 flex items-center cursor-pointer" @click="toggleReasoning(chat)">
                                        深度思考
                                        <SvgIcon name="down-arrow" :customCss="`w-5 h-5 inline ${chat.collapseReasoning ? 'rotate-180' : ''}`"></SvgIcon>
                                    </div>
                                    <StreamMarkdownRender v-if="!chat.collapseReasoning" customCss="px-2 border-l-2 border-gray-200 text-gray-500!" :content="chat.reasoning" />
                                </div>

                                <!-- 正式回答 -->
                                <StreamMarkdownRender :content="chat.content" />
                            </div>
                        </div>
                    </template>
                </div>
                <!-- 提问输入框 -->
                <ChatInputBox 
                v-model="message" 
                containerClass="sticky max-w-3xl mx-auto bg-white bottom-8 left-0 w-full"
                @sendMessage="sendMessage"
                /> 
            </div>
        </template>
    </Layout>
</template>

<script setup>
import { findChatMessagePageList } from '@/api/chat';
import ChatInputBox from '@/components/ChatInputBox.vue';
import LoadingDots from '@/components/LoadingDots.vue';
import StreamMarkdownRender from '@/components/StreamMarkdownRender.vue';
import SvgIcon from '@/components/SvgIcon.vue';
import Layout from '@/layouts/Layout.vue';
import { useChatStore } from '@/stores/chatStore';
import { fetchEventSource } from '@microsoft/fetch-event-source';
import { ref, onBeforeMount, nextTick, onMounted, onBeforeUnmount, handleError, watch } from 'vue'
import { useRoute } from 'vue-router';

const toggleReasoning = (chat) => {
    chat.collapseReasoning = !chat.collapseReasoning
}

const route = useRoute()

const chatStore = useChatStore()

const userMessage = ref('')

const message = ref(history.state?.firstMessage || '')
const textareaRef = ref(null)

const chatId = ref(route.params.chatId || null)

// 聊天容器引用
const chatContainer = ref(null)

// SSE 连接
let eventSource = null;

const chatList = ref([])

const hasMore = ref(true)

const sendMessage = async (payload) => {
  // 校验发送的消息不能为空
  if (!message.value.trim()) return

  console.log('选中的模型:', payload.selectedModel)
  console.log('是否联网:', payload.isNetworkSearch)

  // 将用户发送的消息添加到 chatList 聊天列表中
  const userMessage = message.value.trim()
  chatList.value.push({ role: 'user', content: userMessage })

  // 点击发送按钮后，清空输入框
  message.value = ''

  // 添加一个占位的回复消息
  chatList.value.push({ role: 'assistant', content: '', reasoning: '' ,loading: true})

  try {
    // 构建请求体
    const requestBody = {
      message: userMessage,
      chatId: chatId.value,
      modelName: payload.selectedModel?.name,
      networkSearch: payload.isNetworkSearch
    }

    // 响应的回答
    let responseText = ''
    let reasoningText = ''
    // 获取最后一条消息
    const lastMessage = chatList.value[chatList.value.length - 1]

    const controller = new AbortController()
    const signal = controller.signal

    fetchEventSource('http://localhost:8081/chat/completion', {
      method: 'POST',
      signal: signal,
      headers: {
        'Content-Type': 'application/json',
      },
      body: JSON.stringify(requestBody),
      openWhenHidden: true,
      onmessage(msg) {
        if (msg.event === '') {
          if (lastMessage.loading) {
            lastMessage.loading = false;
          }  
          // 解析 JSON
          let parseJson = JSON.parse(msg.data)

          // 处理推理过程
          if (parseJson.reasoning) {
            reasoningText += parseJson.reasoning
            lastMessage.reasoning = reasoningText
          }

          // 处理正常回复
          if (parseJson.v) {
            reasoningText += parseJson.v
            lastMessage.content = reasoningText
          }

          // 滚动到底部
          scrollToBottom()
        }
        else if (msg.event === 'close') {
          console.log('-- sse close')
          controller.abort();
        }
      },
      onerror(err) {
        throw err // 必须 throw 才能停止 
      }
    })


  } catch (error) {
    console.error('发送消息错误: ', error)
    // 提示用户 “请求出错”
    const lastMessage = chatList.value[chatList.value.length - 1]
    chatList.value[chatList.value.length - 1].content = '抱歉，请求出错了，请稍后重试。'
    lastMessage.loading = false
    // 滚动到底部
    scrollToBottom()
  }

}

console.log('首页传递过来的消息：', history.state?.firstMessage);


// 关闭 SSE
const closeSSE = () => {
    if (eventSource) {
        eventSource.close()
        eventSource = null
    }
}

// 滚动到底部
const scrollToBottom = async () => {
    await nextTick() // 等待 vue.js 完成 DOM 更新
    if (chatContainer.value) {
        const container = chatContainer.value;
        container.scrollTop = container.scrollHeight;
    }
}

onMounted(() => {

    // 加载历史消息
    loadHistoryMessages()

    // 为聊天容器添加滚动事件监听器
    if (chatContainer.value) {
        chatContainer.value.addEventListener('scroll', handleScroll);
    }

    const firstMessage = history.state?.firstMessage
    // 检查跳转路由，是否有初始消息
    if (firstMessage) {
        message.value = firstMessage
        // 发送消息
        sendMessage({
            selectedModel: chatStore.selectedModel,
            isNetworkSearch: chatStore.isNetworkSearchSelected
        })

        // 发送消息后清楚 history.state 中的 firstMessage
        if (history.replaceState) {
            const newState = { ...history.state }
            delete newState.firstMessage
            history.replaceState(newState, document.title)
        }
    }
})

const current = ref(1)

const size = ref(3)

const isLoadingMore = ref(false)

const loadHistoryMessages = async () => {
    findChatMessagePageList(current.value, size.value, chatId.value).then((res) => {
        // 无论成功失败，请求完成后都需要重置加载状态
        isLoadingMore.value = false

        if (res.data.success) {
                const historyMessages = res.data.data

                hasMore.value = res.data.pages > current.value

                if (historyMessages && historyMessages.length > 0) {
                // 历史消息添加到聊天列表顶部
                chatList.value = [...historyMessages, ...chatList.value]

                if (current.value === 1) {
                    scrollToBottom()
                }
            }
        }

        // 确保加载历史消息时自动滚到底部
        if (current.value === 1) {
            scrollToBottom()
        }
    }).catch((error) => {
        console.error('加载历史消息失败:', error)
        isLoadingMore.value = false
    })
}

// 组件卸载时自动关闭连接
onBeforeUnmount(() => {
    closeSSE()

    // 移除滚动事件
    if (chatContainer.value) {
        chatContainer.value.removeEventListener('scroll', handleScroll);
    }
})

// 监听滚动事件
const handleScroll = () => {
    if (chatContainer.value) {
        const scrollTop = chatContainer.value.scrollTop
        const scrollHeight = chatContainer.value.scrollHeight

        console.log('=== 滚动事件日志 ===');
        console.log('scrollTop:', scrollTop);
        console.log('scrollHeight:', scrollHeight)
        console.log('isLoadingMore:', isLoadingMore.value);
        console.log('hasMore:', hasMore.value);

        if (scrollTop < 50 && hasMore.value) {
            console.log('=== 触发加载更多历史消息 ===');
            loadMoreHistoryMessages()
        }
    }
}

// 加载更多历史消息
const loadMoreHistoryMessages = async () => {
    console.log('=== 开始加载更多历史消息 ===');
    console.log('当前页码:', current.value);

    // 双重检查
    if (!hasMore.value) {
        console.log('=== 没有更多历史消息，不在请求 ===');
        return
    }

    if (isLoadingMore.value) {
        console.log('=== 已有加载请求正在进行中，不在发送新请求 ===');
        return
    }

    isLoadingMore.value = true
    
    // 计算下一页页码
    const nextPageNo = current.value + 1
    console.log('=== 计算下一下页码 ===', nextPageNo);
    
    // 保存当前页码用于恢复错误
    const currentTemp = current.value
    // 当前需要请求的页码
    current.value = nextPageNo

    try {
        await loadHistoryMessages();
    } catch (error) {
        current.value = currentTemp;
    }
}

watch(() => route.params.chatId, (newChatId) => {
    if (newChatId) {
        chatId.value = newChatId
        chatList.value = []
        current.value = 1
        loadHistoryMessages()
    }
})

</script>

<style scoped>
.question-container {
    font-size: 16px;
    line-height: 28px;
    color: #262626;
    padding: calc((44px - 28px) / 2) 20px;
    box-sizing: border-box;
    white-space: pre-wrap;
    word-break: break-word;
    background-color: #eff6ff;
    border-radius: 14px;
    max-width: calc(100% - 48px);
    position: relative;
}

.overflow-y-auto {
    scrollbar-color: rgba(0,0,0,0.2) transparent;
}
</style>