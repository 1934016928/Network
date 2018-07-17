package com.lib.var.network;

import com.lib.var.network.callback.LogCallback;
import okhttp3.Headers;
import okhttp3.OkHttpClient;

/**
 * 网络访问功能提供
 *
 * @author var.
 * @date 18-7-16.
 */
public class Network {

    /* HTTP/HTTPS支持 */
    private static OkHttpClient client;
    /* 请求头 */
    private static Headers headers;
    /* 请求接口的域名或IP地址 */
    private static String baseUrl;
    /* 基础请求对象类型 */
    private static Class<?> baseReq;
    /* 日志打印接口 */
    private static LogCallback log;

    /**
     * 构造方法封装
     */
    Network() {
    }

    /**
     * 配置网络请求
     *
     * @return 返回一个ConfigManager配置管理器对象
     */
    public static ConfigManager config() {
        return new ConfigManager();
    }

    /**
     * 发起网络请求
     *
     * @return 返回一个RequestManager配置管理器
     */
    public static RequestManager execute() {
        return new RequestManager();
    }

    /**
     * 获取网络请求客户端
     *
     * @return 返回一个OkHttpClient对象
     */
    static OkHttpClient getClient() {
        return client;
    }

    /**
     * 设置网络请求客户端
     *
     * @param client 支持HTTP/HTTPS的OkHttpClient对象
     */
    static void setClient(OkHttpClient client) {
        Network.client = client;
    }

    /**
     * 获取请求域名或IP
     *
     * @return 返回String类型的字符串
     */
    static String getBaseUrl() {
        return baseUrl;
    }

    /**
     * 设置请求域名或IP
     *
     * @param baseUrl 包含IP或域名+端口的请求地址
     */
    static void setBaseUrl(String baseUrl) {
        Network.baseUrl = baseUrl;
    }

    /**
     * 获取基础请求对象
     *
     * @return 返回一个对象的Class
     */
    static Class<?> getBaseReq() {
        return baseReq;
    }

    /**
     * 设置基础请求对象
     *
     * @param baseReq 对象的Class
     */
    static void setBaseReq(Class<?> baseReq) {
        Network.baseReq = baseReq;
    }

    /**
     * 获取请求头
     *
     * @return 返回一个Headers对象
     */
    static Headers getHeaders() {
        return headers;
    }

    /**
     * 设置请求头
     *
     * @param headers Headers对象
     */
    static void setHeaders(Headers headers) {
        Network.headers = headers;
    }

    /**
     * 获取日志打印接口
     *
     * @return 返回LogCallback对象
     */
    public static LogCallback getLog() {
        return log;
    }

    /**
     * 设置日志打印接口
     *
     * @param log LogCallback对象
     */
    static void setLog(LogCallback log) {
        Network.log = log;
    }
}
