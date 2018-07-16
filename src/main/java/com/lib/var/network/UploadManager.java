package com.lib.var.network;

/**
 * 上传管理器
 *
 * @author var.
 * @date 18-7-16.
 */
class UploadManager {

    /* 请求管理器对象 */
    private RequestManager manager;

    /**
     * 构造方法封装
     *
     * @param manager 上传管理器
     */
    UploadManager(RequestManager manager) {
        this.manager = manager;
    }
}
