import axios from "@/axios";

export function findMarkdownFilePageList(current, size, fileName, startDate, endDate) {
    return axios.post("/customer-service/file/list", {current, size, fileName, startDate, endDate })
}

// export function uploadMarkdownFile(form) {
//     return axios.post("/customer-service/md/upload", form)
// }

export function deleteMarkdownFile(id) {
    return axios.post("/customer-service/file/delete", { id })
}

export function updateMarkdownFile(record) {
    return axios.post("/customer-service/file/update", record)
}

export function uploadFileChunk(form) {
    return axios.post("/customer-service/file/upload-chunk", form)
}

// 合并问答文件分片
export function mergeFileChunk(fileMd5, timeout = 30000) {
    return axios.post("/customer-service/file/merge-chunk", { fileMd5 }, {
        timeout: timeout // 自定义超时间，默认 30s
    })
}

// 检查文件是否存在（秒传）
export function checkFile(fileMd5) {
    return axios.post("/customer-service/file/check", { fileMd5 })
}