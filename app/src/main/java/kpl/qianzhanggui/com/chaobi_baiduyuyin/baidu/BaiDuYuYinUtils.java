package kpl.qianzhanggui.com.chaobi_baiduyuyin.baidu;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.baidu.speech.EventListener;
import com.baidu.speech.EventManager;
import com.baidu.speech.EventManagerFactory;
import com.baidu.speech.asr.SpeechConstant;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

import io.dcloud.application.DCloudApplication;

/**
 * @author Rs  529811807@qq.com
 * @date 2018/11/9
 */
public class BaiDuYuYinUtils implements EventListener {
    private EventManager asr;
    private Context dCloudApplication;
    //检查数据是否合格回调字符串
    private StringBuffer callbackCheckMessage = new StringBuffer();

    /**
     * 1   初始化百度语音EventManager类
     * 通过工厂创建语音识别的事件管理器。注意识别事件管理器只能维持一个，
     * 请勿同时使用多个实例。即创建一个新的识别事件管理器后，之前的那个设置为null。并不再使用。
     */
    public void onCreate(Context dCloudApplication) {
        asr = EventManagerFactory.create(dCloudApplication, "asr");
        this.dCloudApplication = dCloudApplication;
        asr.registerListener(this); //  EventListener 中 onEvent方法
        if (BaiDuYuYinConfig.enableOffline) {
            loadOfflineEngine();
        }
    }

    /**
     * 开始录音，点击“开始”按钮后调用。
     */
    public void onStart() {
        //获取各种配置参数
        Map<String, Object> params = BaiDuYuYinConfig.getParams();
        // 复制此段可以自动检测常规错误,这里有一个参数需要返回给前端   callbackCheckMessage
        (new AutoCheck(dCloudApplication, new Handler() {
            public void handleMessage(Message msg) {
                if (msg.what == 100) {
                    AutoCheck autoCheck = (AutoCheck) msg.obj;
                    synchronized (autoCheck) {
                        String message = autoCheck.obtainErrorMessage(); // autoCheck.obtainAllMessage();
                        callbackCheckMessage.append(message + "\n");
                        ; // 可以用下面一行替代，在logcat中查看代码
                        // Log.w("AutoCheckMessage", message);
                    }
                }
            }
        }, BaiDuYuYinConfig.enableOffline)).checkAsr(params);
        //发送数据进行识别
        String json = new JSONObject(params).toString();
        asr.send(SpeechConstant.ASR_START, json, null, 0, 0);

    }


    /**
     * 点击停止按钮
     */
    public void stop() {
        asr.send(SpeechConstant.ASR_STOP, null, null, 0, 0); //
    }


    /**
     * 销毁数据连接，资源等
     */
    public void onDestroy() {
        asr.send(SpeechConstant.ASR_CANCEL, "{}", null, 0, 0);
        if (BaiDuYuYinConfig.enableOffline) {
            unloadOfflineEngine(); // 测试离线命令词请开启, 测试 ASR_OFFLINE_ENGINE_GRAMMER_FILE_PATH 参数时开启
        }

        // 必须与registerListener成对出现，否则可能造成内存泄露
        asr.unregisterListener(this);
    }

    /**
     * enableOffline设为true时，在onCreate中调用
     */
    private void loadOfflineEngine() {

        Map<String, Object> params = new LinkedHashMap<>();
        params.put(SpeechConstant.DECODER, 2);
        params.put(SpeechConstant.ASR_OFFLINE_ENGINE_GRAMMER_FILE_PATH, "assets://baidu_speech_grammar.bsg");
        params.putAll(fetchSlotDataParam());
        asr.send(SpeechConstant.ASR_KWS_LOAD_ENGINE, new JSONObject(params).toString(), null, 0, 0);
    }

    public static Map<String, Object> fetchSlotDataParam() {
        Map<String, Object> map = new HashMap<String, Object>();
        try {
            JSONObject json = new JSONObject();
            json.put("name", new JSONArray().put("妈妈").put("老伍"))
                    .put("appname", new JSONArray().put("手百").put("度秘"));
            map.put(SpeechConstant.SLOT_DATA, json);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return map;
    }


    /**
     * enableOffline为true时，在onDestory中调用，与loadOfflineEngine对应
     */
    private void unloadOfflineEngine() {
        asr.send(SpeechConstant.ASR_KWS_UNLOAD_ENGINE, null, null, 0, 0); //
    }


    /**
     * 2 .注册自己的输出事件类
     * SDK 中，需要实现EventListener的输出事件回调接口。该类需要处理SDK在识别过程中的回调事件。
     *
     * @param name
     * @param params
     * @param data
     * @param offset
     * @param length
     */
    @Override
    public void onEvent(String name, String params, byte[] data, int offset, int length) {
        String logTxt = "name: " + name;
        Log.e(">>>>>>>name>>", name);

        if (params != null && !params.isEmpty()) {
            logTxt += " ;params :" + params;
        }
        if (name.equals(SpeechConstant.CALLBACK_EVENT_ASR_PARTIAL)) {
            if (params.contains("\"nlu_result\"")) {
                if (length > 0 && data.length > 0) {
                    logTxt += ", 语义解析结果：" + new String(data, offset, length);
                }
            }
        } else if (data != null) {
            logTxt += " ;data length=" + data.length;
        }
        Log.e(">>>>>>>logTxt>>", logTxt);
    }
}
