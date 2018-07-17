package com.lib.var.network;

import com.lib.var.network.callback.LogCallback;
import okhttp3.Cache;
import okhttp3.Headers;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;

import javax.net.ssl.*;
import java.io.File;
import java.io.InputStream;
import java.security.KeyStore;
import java.security.SecureRandom;
import java.security.cert.CertificateFactory;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * 配置管理器
 *
 * @author var.
 * @date 18-7-16.
 */
public class ConfigManager {

    /* 拦截器集合 */
    private List<Interceptor> its;
    /* 缓存 */
    private Cache cache;
    /* 证书管理器 */
    private TrustManager manager;

    /**
     * 构造函数封装
     */
    ConfigManager() {
        this.its = new ArrayList<>();
    }

    /**
     * 设置接口IP地址
     *
     * @param host 域名或IP地址
     * @return 返回当前对象
     */
    public ConfigManager host(String host) {
        Network.setBaseUrl(host);
        return this;
    }

    /**
     * 设置基础请求对象类型
     *
     * @param baseReq 类的Class
     * @param <T>     泛型
     * @return 返回当前对象
     */
    public <T> ConfigManager baseRequest(Class<T> baseReq) {
        Network.setBaseReq(baseReq);
        return this;
    }

    /**
     * 设置日志打印接口
     *
     * @param callback LogCallback对象
     * @return 返回当前对象
     */
    public ConfigManager log(LogCallback callback) {
        Network.setLog(callback);
        return this;
    }

    /**
     * 添加请求头
     *
     * @param name  请求头名称
     * @param value 请求头参数
     * @return 返回当前对象
     */
    public ConfigManager addHeader(String name, String value) {
        if (Network.getHeaders() == null) {
            Network.setHeaders(new Headers.Builder().add(name, value).build());
        } else {
            Network.setHeaders(Network.getHeaders().newBuilder().add(name, value).build());
        }
        return this;
    }

    /**
     * 添加拦截器
     *
     * @param it 拦截器对象
     * @return 返回当前对象
     */
    public ConfigManager addInterceptor(Interceptor it) {
        if (Network.getClient() != null) {
            Network.setClient(Network.getClient().newBuilder().addInterceptor(it).build());
        } else {
            this.its.add(it);
        }
        return this;
    }

    /**
     * 添加缓存
     *
     * @param path 缓存目录
     * @param size 缓存大小
     * @return 返回当前对象
     */
    public ConfigManager cache(String path, int size) {
        Cache cache = new Cache(new File(path), size);
        if (Network.getClient() == null) {
            this.cache = cache;
        } else {
            Network.setClient(Network.getClient().newBuilder().cache(cache).build());
        }
        return this;
    }

    /**
     * 启用HTTP协议
     */
    public void http() {
        Network.setClient(this.httpClient());
    }

    /**
     * 启用HTTPS协议
     *
     * @param certificates 证书输入流
     */
    public void https(InputStream... certificates) {
        SSLSocketFactory factory = null;
        try {
            factory = this.getSSLSocketFactory(certificates);
        } catch (Exception e) {
            e.printStackTrace();
        }
        Network.setClient(this.httpsClient(factory, manager));
    }

    /**
     * 创建HTTP客户端
     *
     * @return 返回一个OkHttpClient对象
     */
    private OkHttpClient httpClient() {
        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        /* 超时设置 */
        builder.connectTimeout(20, TimeUnit.SECONDS);
        builder.writeTimeout(20, TimeUnit.SECONDS);
        builder.readTimeout(20, TimeUnit.SECONDS);
        /* 域名/Session验证 */
        builder.hostnameVerifier((hostname, session) -> true);
        /* 缓存设置 */
        if (this.cache != null) {
            builder.cache(this.cache);
        }
        /* 拦截器添加 */
        if (this.its.size() > 0) {
            for (Interceptor it : this.its) {
                builder.addInterceptor(it);
            }
            this.its.clear();
        }
        return builder.build();
    }

    /**
     * 用证书创建KeyStore并获取SSLSocketFactory对象
     *
     * @param certificates 证书
     * @return 如果创建失败则返回null, 反之则返回一个SSLSocketFactory对象
     */
    private SSLSocketFactory getSSLSocketFactory(InputStream... certificates) throws Exception {
        /*使用自己的证书创建一个KeyStore*/
        CertificateFactory cerFactory = CertificateFactory.getInstance("X.509");
        KeyStore keyStore = KeyStore.getInstance(KeyStore.getDefaultType());
        keyStore.load(null);

        /*遍历证书*/
        int index = 0;
        for (InputStream cer : certificates) {
            String alias = Integer.toString(index++);
            keyStore.setCertificateEntry(alias, cerFactory.generateCertificate(cer));
            if (cer != null) cer.close();
        }

        /*创建管理器,只信任我们自己创建的KeyStore*/
        SSLContext sslContext = SSLContext.getInstance("TLS");
        TrustManagerFactory trustManagerFactory = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
        trustManagerFactory.init(keyStore);
        sslContext.init(null, trustManagerFactory.getTrustManagers(), new SecureRandom());
        this.manager = trustManagerFactory.getTrustManagers()[0];
        return sslContext.getSocketFactory();
    }

    /**
     * 添加HTTPS支持
     *
     * @param factory 加密工厂
     * @param manager 证书管理器
     * @return 返回支持HTTPS的OkHttpClient
     */
    private OkHttpClient httpsClient(SSLSocketFactory factory, TrustManager manager) {
        OkHttpClient client;
        if (Network.getClient() == null) {
            client = this.httpClient();
        } else {
            client = Network.getClient();
        }
        return client.newBuilder().sslSocketFactory(factory, (X509TrustManager) manager).build();
    }
}
