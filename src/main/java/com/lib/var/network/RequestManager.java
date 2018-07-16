package com.lib.var.network;

import com.lib.var.network.callback.NetCallback;

/**
 * 请求配置管理器
 *
 * @author var.
 * @date 18-7-16.
 */
public class RequestManager {

    /* 请求地址 */
    private String url;
    /* 请求参数 */
    private Object data;

    /**
     * 构造方法封装
     */
    RequestManager() {
    }

    /**
     * 设置请求地址
     *
     * @param url 请求地址字符串
     * @return 返回当前对象
     */
    public RequestManager url(String url) {
        this.url = url;
        return this;
    }

    /**
     * 设置请求参数
     *
     * @param data 参数数据
     * @return 返回当前对象
     */
    public RequestManager params(Object data) {
        this.data = data;
        return this;
    }

    /**
     * 设置POST请求回调并发起请求
     *
     * @param callback 回调接口
     * @param <T>      泛型
     */
    public <T> void post(NetCallback<T> callback) {

    }

    /**
     * 设置GET请求回调并发起请求
     *
     * @param callback 回调接口
     * @param <T>      泛型
     */
    public <T> void get(NetCallback<T> callback) {

    }

    /**
     * 下载方法
     *
     * @return 返回DownloadManager对象
     */
    public DownloadManager download() {
        return new DownloadManager(this);
    }

    /**
     * 上传方法
     *
     * @return 返回UploadManager对象
     */
    public UploadManager upload() {
        return new UploadManager(this);
    }
}
