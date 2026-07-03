<template>
    <div class="markdown-container" :class="props.customCss">
        <div v-html="renderContent"></div>
    </div>
</template>

<script setup>
import { ref, watch, nextTick } from 'vue'
import MarkdownIt from 'markdown-it';
import hljs from 'highlight.js';
import 'highlight.js/styles/github.css'
import markdownItHighlightJs from 'markdown-it-highlightjs'
import { message } from 'ant-design-vue';

// 定义一个 content 字段，用于父组件传入 markdown 文本
const props = defineProps({
    content: {
        type: String,
        default: ''
    },
    customCss: {
        type: String,
        default: ''
    }
})

// 解析后的HTML
const renderContent = ref('')

// 初始化 MarkdownIt
const md = new MarkdownIt({
    html: true,  // 允许解析 HTML 标签
    xhtmlOut: true, // 输出符合 XHTML 规范的标签
    linkify: true, //自动将文本中的 URL 转换为可点击的链接
    typographer: true, // 启动排版优化
    breaks: true, // 将单个换行符转换为<br>
    langPrefix: 'language-' // 代码块的语言类名前缀
})

md.use(markdownItHighlightJs, {
    hljs,
    auto: true, // 自动检测
    code: true // 高亮内联代码
})

// 保存默认代码块渲染规则
const defaultRender = md.renderer.rules.fence || function(tokens, idx, options, env, renderer) {
    return renderer.renderToken(tokens, idx, options)
}

// 重写
md.renderer.rules.fence = function (tokens, idx, options, env, renderer) {
    const token = tokens[idx]

    const info = token.info ? md.utils.unescapeAll(token.info).trim() : ''
    let langName = ''

    if (info) {
        const langCode = info.split(/\s+/g)[0]
        langName = langCode.toLowerCase() // 转换为小写
    }

    const originalContent = defaultRender(tokens, idx, options, env, renderer)

    // 拼装最终的 HTML
    let finalContent = `<div class="code-block-wrapper">
        <div class="code-header">
        `
    if (langName) {
        finalContent += `<div class="code-language-label">${langName}</div>`
    }

    const codeContent = token.content

    const codeId = `code-${Math.random().toString(36).substr(2, 9)}`

    return finalContent += `
        <button class="copy-code-btn" onclick="copyCode('${codeId}')">
            <svg t="1750068080826" class="copy-icon" viewBox="0 0 1024 1024" version="1.1" xmlns="http://www.w3.org/2000/svg" p-id="1529" 
            width="15" height="15"><path d="M761.088 715.3152a38.7072 38.7072 0 0 1 0-77.4144 37.4272 37.4272 0 0 0 37.4272-37.4272V265.0112a37.4272 37.4272 0 0 0-37.4272-37.4272H425.6256a37.4272 37.4272 0 0 0-37.4272 37.4272 38.7072 38.7072 0 1 1-77.4144 0 115.0976 115.0976 0 0 1 114.8416-114.8416h335.4624a115.0976 115.0976 0 0 1 114.8416 114.8416v335.4624a115.0976 115.0976 0 0 1-114.8416 114.8416z" p-id="1530"></path><path d="M589.4656 883.0976H268.1856a121.1392 121.1392 0 0 1-121.2928-121.2928v-322.56a121.1392 121.1392 0 0 1 121.2928-121.344h321.28a121.1392 121.1392 0 0 1 121.2928 121.2928v322.56c1.28 67.1232-54.1696 121.344-121.2928 121.344zM268.1856 395.3152a43.52 43.52 0 0 0-43.8784 43.8784v322.56a43.52 43.52 0 0 0 43.8784 43.8784h321.28a43.52 43.52 0 0 0 43.8784-43.8784v-322.56a43.52 43.52 0 0 0-43.8784-43.8784z" p-id="1531"></path></svg>
            <span class="copy-text">复制</span>
        </button>
        </div>
        <div class="code-content" id="${codeId}" data-code="${encodeURIComponent(codeContent)}">
            ${originalContent}
        </div>
    </div>`
}

const setupCopyFunction = () => {
    if (!window.copyCode) {
        window.copyCode = async (codeId) => {
            try {
                const codeElement = document.getElementById(codeId)
                if (!codeElement) return // 不存在溢出

                const codeContent = decodeURIComponent(codeElement.getAttribute('data-code'))

                await navigator.clipboard.writeText(codeContent)

                const btn = codeElement.parentElement.querySelector('.copy-code-btn')
                if (btn) {
                    const originalIcon = btn.querySelector('.copy-icon').innerHTML

                    btn.querySelector('.copy-icon').innerHTML = `<path d="M912 190h-69.9c-9.8 0-19.1 4.5-25.1 12.2L404.7 724.5 207 474c-6.1-7.7-15.3-12.2-25.1-12.2H112c-6.7 0-10.4 7.7-6.3 12.9L357.1 864c12.6 16.1 35.5 16.1 48.1 0L918.3 202.9c4.1-5.2 0.4-12.9-6.3-12.9z" p-id="4582"></path>`

                    btn.classList.add('copied')

                    message.success('复制成功')

                    setTimeout(() => {
                        btn.querySelector('.copy-icon').innerHTML = originalIcon
                        btn.classList.remove('copied')
                    }, 1000)
                }
            } catch (err) {
                console.error('复制失败:', err)
            }
        }
    }
}


// 监听 content 字段，流式更新处理
watch(() => props.content, (newVal) => {
    if (newVal) {
        const html = md.render(newVal)
        renderContent.value = html

        nextTick(() => {
            setupCopyFunction()
        })
    }
}, { immediate: true })
</script>

<style scoped>
.markdown-container {
    width: 100%;
    line-height: 24px;
    color: rgb(64, 64, 64);
}

:deep(.markdown-container > p:first-child),
:deep(p:first-child) {
    margin-top: 0;
}

:deep(h1), :deep(h2), :deep(h3), :deep(h4), :deep(h5), :deep(h6) {
    font-weight: 600;
    margin: calc(1.143 * 16px) 0 calc(1.143 * 12px) 0;
}

:deep(h1) {
    font-size: 1.5em;
    margin-top: 1.2em;
    margin-bottom: 0.7em;
    line-height: 1.5;
}

:deep(h2) {
    font-size: 1.3em;
    margin-top: 1.1em;
    margin-bottom: 0.6em;
    line-height: 1.5;
}

:deep(h3) {
    font-size: calc(1.143 * 16px);
    line-height: 1.5;
}

:deep(p) {
    line-height: 1.7;
    margin: calc(1.143 * 12px) 0;
    font-size: calc(1.143 * 14px);
}

:deep(ul) {
    list-style: disc;
    margin-top: 0.6em;
    margin-bottom: 0.9em;
    padding-left: 2em;
}

:deep(ol) {
    list-style: decimal;
    margin-top: 0.6em;
    margin-bottom: 0.9em;
    padding-left: 2em;
}

:deep(li) {
    margin-bottom: 0.5em;
    line-height: 1.7;
}

:deep(ol li::marker) {
    line-height: calc(1.143 * 25px);
    color: rgb(139, 139, 139);
}

:deep(ul li::marker) {
    color: rgb(139, 139, 139);
}

:deep(ul ul) {
    list-style: circle;
    margin-top: 0.3em;
    margin-bottom: 0.3em;
}

:deep(ul ul ul) {
    list-style: square;
}

:deep(.code-block-wrapper) {
    margin: 1em 0;
    border-radius: 14px;
    overflow: hidden;
    background-color: #f6f8fa;
}

:deep(.code-header) {
    display: flex;
    justify-content: space-between;
    align-items: center;
    background-color: #f5f5f5;
    padding: 8px 12px;
}

:deep(.code-language-label) {
    color: rgb(82 82 82);
    margin-left: 8px;
    font-size: 12px;
    line-height: 18px;
}

:deep(.hljs) {
    background: transparent !important;
    padding: 0 !important;
}

:deep(.copy-code-btn) {
    display: flex;
    align-items: center;
    gap: 4px;
    background: transparent;
    border-radius: 12px;
    padding: 0 8px;
    color: #586069;
    font-size: 12px;
    height: 28px;
    cursor: pointer;
    transition: all 0.2s ease;
}

:deep(.copy-code-btn.copied .copy-icon) {
    fill: #22c55e
}

:deep(.copy-code-btn:hover) {
    background-color: rgb(0 0 0 / 4%);
}

:deep(.copy-icon) {
    fill: currentColor;
    flex-shrink: 0;
}

:deep(.copy-text) {
    white-space: nowrap;
}

:deep(pre) {
    background-color: #fafafa;
    padding: 1em;
    border-radius: 5px;
    overflow-x: auto;
    max-width: 100%;
    white-space: pre;
    word-wrap: normal;
}

:deep(:not(pre) > code) {
    font-size: 0.875em;
    font-weight: 600;
    background-color: #ececec;
    border-radius: 4px;
    padding: 0.15em 0.3em;
    margin: 0 0.2em;
}

:deep(pre > code) {
    font-size: 0.875em;
    background-color: transparent;
    padding: 0;
    border-radius: 0;
    font-weight: normal;
    color: #333;
    display: block;
    width: 100%;
}

:deep(a) {
    color: #4d6bfe;
    text-decoration: none;
}

:deep(a:hover) {
    text-decoration: underline;
}

:deep(blockquote) {
    border-left: 4px solid #e5e5e5;
    padding-left: 1em;
    margin: 1em 0;
    color: #666;
}

:deep(table) {
    border-collapse: collapse;
    width: 100%;
    margin: 1em 0;
    font-size: 0.95em;
}

:deep(th), :deep(td) {
    border: 1px solid #e5e5e5;
    padding: 0.6em;
    text-align: left;
}

:deep(th) {
    background-color: #f5f5f5;
}

:deep(hr) {
    background-color: rgb(229, 229, 229);
    margin: 1.5em 0;
    height: 1px;
    border: none;
}

:deep(h1 + p)
:deep(h2 + p)
:deep(h3 + p) {
    margin-top: 0.5em;
}

:deep(p + ul)
:deep(p + ol) {
    margin-top: 0.5em;
}

:deep(ul + p)
:deep(ol + p) {
    margin-top: 0.7em;
}
</style>