package com.lib.var.network.callback;

/**
 * 日志打印接口
 *
 * @author var.
 * @date 2018/7/17.
 */
public interface LogCallback {

    /**
     * 打印操作信息
     *
     * @param log 日志信息
     */
    void i(String log);

    /**
     * 打印错误信息
     *
     * @param log       日志信息
     * @param throwable 异常信息
     */
    void e(String log, Throwable throwable);
}
