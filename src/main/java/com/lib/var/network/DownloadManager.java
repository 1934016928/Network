package com.lib.var.network;

/**
 * 下载管理器
 *
 * @author var.
 * @date 18-7-16.
 */
class DownloadManager {

    /* 请求管理器对象 */
    private RequestManager manager;

    /**
     * 构造方法封装
     *
     * @param manager 请求管理器对象
     */
    DownloadManager(RequestManager manager) {
        this.manager = manager;
    }
}
