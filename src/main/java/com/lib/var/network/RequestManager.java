package com.lib.var.network;

import com.lib.var.network.callback.NetCallback;
import com.lib.var.network.utils.JSON;
import io.reactivex.Single;
import okhttp3.*;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;

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
    private String log;

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
        this.url = Network.getBaseUrl() + url;
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
     * @param clazz    反序列化泛型对象类型
     * @param <T>      泛型
     */
    public <T> void post(Class<T> clazz, NetCallback<T> callback) {
        if (callback == null) {
            this.I("NetCallback not set!");
        }
        Object params = this.fromClass(this.data);
        Request.Builder builder = new Request.Builder();
        builder.url(this.url);
        builder.headers(Network.getHeaders());
        String json;
        if (params == null) {
            json = JSON.toJson(this.data);
        } else {
            json = JSON.toJson(params);
        }
        builder.post(RequestBody.create(MediaType.parse("application/json; charset=utf-8"), json));
        Request request = builder.build();
        log = "\nrequest method: \tPOST\ndata type: \tJSON\nrequest url: \t" + this.url + "\nrequest data: \t" + json;
        this.I(">>>>>>>>REQUEST START>>>>>>>>" + log);
        Network.getClient().newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                error(callback, "网络链接失败!");
                E(">>>>>>>>REQUEST ERROR>>>>>>>>" + log, e);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                verifyResponse(clazz, callback, response);
            }
        });
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

    /**
     * 通过基础请求对象类型创建请求参数对象
     *
     * @param data 请求数据
     * @return 返回创建的对象
     */
    private Object fromClass(Object data) {
        Object params = null;
        try {
            if (Network.getBaseReq() != null) {
                params = Network.getBaseReq().getConstructor(Object.class).newInstance(data);
            } else {
                this.I("base request object not set!");
            }
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
            this.E("create base request object fail!", e);
        }
        return params;
    }

    /**
     * 提交错误信息
     *
     * @param callback 回调接口
     * @param str      错误信息
     * @param <T>      泛型
     */
    private <T> void error(NetCallback<T> callback, String str) {
        if (callback != null) {
            Single.just(str)
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(callback::onError);
        }
    }

    /**
     * 提交成功信息
     *
     * @param callback 回调接口
     * @param t        反序列化对象
     * @param <T>      泛型
     */
    private <T> void success(NetCallback<T> callback, T t) {
        if (callback != null) {
            Single.just(t)
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(callback::onSuccess);
        }
    }

    /**
     * 打印操作信息
     *
     * @param log 日志信息
     */
    private void I(String log) {
        if (Network.getLog() != null) {
            Network.getLog().i(log);
        }
    }

    /**
     * 打印错误信息
     *
     * @param log       日志信息
     * @param throwable 异常信息
     */
    private void E(String log, Throwable throwable) {
        if (Network.getLog() != null) {
            Network.getLog().e(log, throwable);
        }
    }

    /**
     * 检查返回
     *
     * @param clazz    反序列化泛型对象类型
     * @param callback 请求回调
     * @param response 返回对象
     * @param <T>      泛型
     */
    private <T> void verifyResponse(Class<T> clazz, NetCallback<T> callback, Response response) throws IOException {
        if (response.isSuccessful()) {
            ResponseBody body = response.body();
            if (body != null) {
                String json = body.string();
                if (json != null && json.length() != 0) {
                    I(">>>>>>>>RESPONSE DONE>>>>>>>>" + log + "\nresponse data: \t" + json);
                    T t = JSON.toObject(json, clazz);
                    if (t != null) {
                        success(callback, t);
                    } else {
                        error(callback, "内部错误!");
                    }
                } else {
                    error(callback, "请求服务器失败!");
                    E(">>>>>>>>RESPONSE ERROR>>>>>>>>" + log, new Exception("ResponseBody is no data!"));
                }
            } else {
                error(callback, "请求服务器失败!");
                E(">>>>>>>>RESPONSE ERROR>>>>>>>>" + log, new Exception("ResponseBody is null!"));
            }
        } else {
            error(callback, "请求服务器失败!");
            E(">>>>>>>>RESPONSE ERROR>>>>>>>>" + log, new Exception("response not success!"));
        }
    }
}
