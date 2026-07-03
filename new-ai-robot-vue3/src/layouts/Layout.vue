<template>
    <div class="h-screen flex overflow-hidden overflow-x-hidden">
        <!-- 左边栏 -->
        <Sidebar
            :sidebarOpen="sidebarOpen"
            @toggle-sidebar="toggleSidebar"
            />

        <div :class="sidebarOpen ? 'ml-64' : 'ml-0'" class="flex flex-col flex-1 transition-all duration-300">
            <slot name="main-content"></slot>
        </div>

        <!-- 提示文字 -->
        <div v-if="showFooterText" 
            :class="sidebarOpen ? 'ml-64' : 'ml-0'" 
            class="bg-white flex fixed bottom-0 left-0 right-0 items-center justify-center text-xs text-gray-400 transition-all duration-300 py-2">
        内容由 AI 生成，请仔细甄别
    </div>
    </div>
</template>

<script setup>
import { ref } from 'vue';
import Sidebar from '@/components/Sidebar.vue';

const props = defineProps({
    showFooterText: {
        type: Boolean,
        default: true
    }
})

const sidebarOpen = ref(true)

const toggleSidebar = () => {
    sidebarOpen.value = !sidebarOpen.value;
}

</script>
