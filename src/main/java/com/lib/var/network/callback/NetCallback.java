package com.lib.var.network.callback;

/**
 * 网络请求回调
 *
 * @author var.
 * @date 18-7-16.
 */
public interface NetCallback<T> {

    /**
     * 请求异常时触发
     *
     * @param msg 异常信息
     */
    void onError(Object msg);

    /**
     * 请求成功时触发,此方法在主线程中调用
     *
     * @param t 反序列化对象
     */
    void onSuccess(T t);
}
