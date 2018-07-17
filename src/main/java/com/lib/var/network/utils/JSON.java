package com.lib.var.network.utils;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.lib.var.network.Network;

/**
 * JSON操作工具类
 *
 * @author var.
 * @date 2018/7/17.
 */
public class JSON {

    /* Google JSON解析工具 */
    private static Gson gson = new Gson();

    /**
     * 对象序列化为JSON字符串
     *
     * @param object 对象
     * @return 返回序列化后的JSON字符串
     */
    public static String toJson(Object object) {
        return gson.toJson(object);
    }

    /**
     * JSON字符串反序列化为对象
     *
     * @param json  JSON字符串
     * @param clazz 反序列化类型
     * @param <T>   泛型
     * @return 返回反序列化的对象
     */
    public static <T> T toObject(String json, Class<T> clazz) {
        T type = null;
        try {
            type = gson.fromJson(json, clazz);
        } catch (JsonSyntaxException e) {
            if (Network.getLog() != null) {
                Network.getLog().e("JSON to object fail!", e);
            }
        }
        return type;
    }
}
