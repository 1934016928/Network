import com.lib.var.network.Network;
import com.lib.var.network.callback.NetCallback;

/**
 * 程序入口点
 *
 * @author var.
 * @date 18-7-16.
 */
public class Main {

    public static void main(String[] args) {
        /* 网络请求配置 */
        Network.config()
                .host("http://192.168.0.129:80")
                .addHeader("Content-Type", "Application/json")
                .addHeader("Content-Type", "Application/json")
                .addHeader("Content-Type", "Application/json")
                .addHeader("Content-Type", "Application/json")
                .addHeader("Content-Type", "Application/json")
                .addInterceptor(null)
                .addInterceptor(null)
                .addInterceptor(null)
                .addInterceptor(null)
                .addInterceptor(null)
                .baseRequest(Main.class)
                .baseResponse(Main.class)
                .cache("/sdcard/cache", 10 * 1024 * 1024)
                .http();
        /* .https(null) */
        /* 发起请求 */
        Network.execute()
                .url("/user/login")
                .params("{\"name\":\"root\"}")
                .post(new NetCallback<Main>() {
                    @Override
                    public void onError(Throwable throwable, Object msg) {

                    }

                    @Override
                    public void onSuccess(Main main) {

                    }

                    @Override
                    public void onUpdate(Main main) {

                    }
                });
    }
}
