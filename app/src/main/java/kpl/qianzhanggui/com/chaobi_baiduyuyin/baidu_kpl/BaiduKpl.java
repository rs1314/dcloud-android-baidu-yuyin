package kpl.qianzhanggui.com.chaobi_baiduyuyin.baidu_kpl;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import java.util.Map;

import io.dcloud.application.DCloudApplication;
import kpl.qianzhanggui.com.chaobi_baiduyuyin.InitBaidu;
import kpl.qianzhanggui.com.chaobi_baiduyuyin.baidu.AutoCheck;
import kpl.qianzhanggui.com.chaobi_baiduyuyin.baidu.BaiDuYuYinConfig;
import kpl.qianzhanggui.com.chaobi_baiduyuyin.baidu_kpl.listener.IRecogListener;
import kpl.qianzhanggui.com.chaobi_baiduyuyin.baidu_kpl.listener.MessageStatusRecogListener;
import kpl.qianzhanggui.com.chaobi_baiduyuyin.baidu_kpl.listener.MyRecognizer;
import kpl.qianzhanggui.com.chaobi_baiduyuyin.baidu_kpl.params.OfflineRecogParams;

/**
 * @author Rs  529811807@qq.com
 * @date 2018/11/9
 */
public class BaiduKpl {

    private StringBuffer checkPermissionMessage = new StringBuffer();
    private MyRecognizer myRecognizer;
    private IRecogListener listener;
    private Context dCloudApplication;

    public void onCreate(Context dCloudApplication) {
        listener = new MessageStatusRecogListener();
        // 可以传入IRecogListener的实现类，也可以如SDK，传入EventListener实现类
        // 如果传入IRecogListener类，在RecogEventAdapter为您做了大多数的json解析。
        myRecognizer = new MyRecognizer(dCloudApplication, listener); // this是Activity或其它Context类
        if (BaiDuYuYinConfig.enableOffline) {
            // 集成步骤 1.3（可选）加载离线资源。offlineParams是固定值，复制到您的代码里即可
            Map<String, Object> offlineParams = OfflineRecogParams.fetchOfflineParams();
            myRecognizer.loadOfflineEngine(offlineParams);
        }
        this.dCloudApplication =dCloudApplication;
    }

    public Context getdCloudApplication() {
        return dCloudApplication;
    }

    public void setdCloudApplication(Context dCloudApplication) {
        this.dCloudApplication = dCloudApplication;
    }

    public MyRecognizer getMyRecognizer() {
        return myRecognizer;
    }

    public void setMyRecognizer(MyRecognizer myRecognizer) {
        this.myRecognizer = myRecognizer;
    }

    /**
     * 开始录音，点击“开始”按钮后调用。
     */
    public void start(Context context) {
       check(context);
        // DEMO集成步骤2.1 拼接识别参数： 此处params可以打印出来，直接写到你的代码里去，最终的json一致即可。
        final Map<String, Object> params = BaiDuYuYinConfig.getParams();
        // params 也可以根据文档此处手动修改，参数会以json的格式在界面和logcat日志中打印

        // 复制此段可以自动检测常规错误
        (new AutoCheck(dCloudApplication, new Handler() {
            public void handleMessage(Message msg) {
                if (msg.what == 100) {
                    AutoCheck autoCheck = (AutoCheck) msg.obj;
                    synchronized (autoCheck) {
                        String message = autoCheck.obtainErrorMessage(); // autoCheck.obtainAllMessage();
                        checkPermissionMessage.append(message + "\n");
                        ; // 可以用下面一行替代，在logcat中查看代码
                        // Log.w("AutoCheckMessage", message);
                    }
                }
            }
        }, BaiDuYuYinConfig.enableOffline)).checkAsr(params);

        // 这里打印出params， 填写至您自己的app中，直接调用下面这行代码即可。
        myRecognizer.start(params);
    }


    /**
     * 开始录音后，手动点击“停止”按钮。
     * SDK会识别不会再识别停止后的录音。
     */
    public void stop() {
        // DEMO集成步骤4 (可选） 停止录音
        myRecognizer.stop();
    }

    /**
     * 开始录音后，手动点击“取消”按钮。
     * SDK会取消本次识别，回到原始状态。
     */
    public void cancel() {
        // DEMO集成步骤5 (可选） 取消本次识别
        myRecognizer.cancel();
    }

    public void onDestroy(Context contexttemp) {
        check(contexttemp);
        // DEMO集成步骤3 释放资源
        // 如果之前调用过myRecognizer.loadOfflineEngine()， release()里会自动调用释放离线资源
        myRecognizer.release();
    }

    public void check(Context contexttemp){
        BaiduKpl baiduKpl = InitBaidu.baiduKpl;
        baiduKpl.setdCloudApplication(contexttemp);
    }
}
