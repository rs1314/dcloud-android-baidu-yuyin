package kpl.qianzhanggui.com.chaobi_baiduyuyin.baidu_kpl.listener;

import android.os.Message;
import android.util.Log;

import com.baidu.speech.asr.SpeechConstant;

import org.json.JSONArray;

import java.util.LinkedHashMap;
import java.util.Map;

import io.dcloud.common.DHInterface.IWebview;
import io.dcloud.common.util.JSUtil;

/**
 * Created by fujiayi on 2017/6/16.
 */

public class MessageStatusRecogListener extends StatusRecogListener {
    public static Object lock = new Object();
    public static Map<String, String> resultsMessageMap = new LinkedHashMap<>();
    private static final String resuletsKey = "merged_res_semantic_form_kpl";
    private static final String resuletsKey_yuyi = "merged_res_semantic_form_kpl_yuyi";
    public static IWebview pWebview;
    public static JSONArray array;
    private long speechEndTime = 0;

    private boolean needTime = true;

    private static final String TAG = "MesStatusRecogListener";

    public MessageStatusRecogListener() {

    }

    static {
        resultsMessageMap.put(resuletsKey_yuyi, "-1");
    }


    @Override
    public void onAsrReady() {
        super.onAsrReady();
        speechEndTime = 0;
        sendStatusMessage(SpeechConstant.CALLBACK_EVENT_WAKEUP_READY, "引擎就绪，可以开始说话。");
    }

    @Override
    public void onAsrBegin() {
        super.onAsrBegin();
        sendStatusMessage(SpeechConstant.CALLBACK_EVENT_ASR_BEGIN, "检测到用户说话");
    }

    @Override
    public void onAsrEnd() {
        super.onAsrEnd();
        speechEndTime = System.currentTimeMillis();
        sendMessage("【asr.end事件】检测到用户说话结束");
    }

    @Override
    public void onAsrPartialResult(String[] results, RecogResult recogResult) {
        Log.e("onAsrPartialResult", results + "");
        Log.e("onAsrPartialResult", recogResult + "");

        sendStatusMessage(SpeechConstant.CALLBACK_EVENT_ASR_PARTIAL,
                "临时识别结果，结果是“" + results[0] + "”；原始json：" + recogResult.getOrigalJson());
        super.onAsrPartialResult(results, recogResult);
    }

    @Override
    public void onAsrFinalResult(String[] results, RecogResult recogResult) {
        Log.e("最好出来了", results + "");
        Log.e("最好出来了", recogResult + "");

        super.onAsrFinalResult(results, recogResult);
        String message = "识别结束，结果是”" + results[0] + "”";
        sendStatusMessage(SpeechConstant.CALLBACK_EVENT_ASR_PARTIAL,
                message + "；原始json：" + recogResult.getOrigalJson());
        if (speechEndTime > 0) {
            long currentTime = System.currentTimeMillis();
            long diffTime = currentTime - speechEndTime;
            message += "；说话结束到识别结束耗时【" + diffTime + "ms】" + currentTime;

        }
        Log.e("最好出来了", message);
        speechEndTime = 0;
        sendMessage(message, status, true);
    }

    @Override
    public void onAsrFinishError(int errorCode, int subErrorCode, String descMessage,
                                 RecogResult recogResult) {
        super.onAsrFinishError(errorCode, subErrorCode, descMessage, recogResult);
        String message = "【asr.finish事件】识别错误, 错误码：" + errorCode + " ," + subErrorCode + " ; " + descMessage;
        sendStatusMessage(SpeechConstant.CALLBACK_EVENT_ASR_PARTIAL, message);
        if (speechEndTime > 0) {
            long diffTime = System.currentTimeMillis() - speechEndTime;
            message += "。说话结束到识别结束耗时【" + diffTime + "ms】";
        }
        speechEndTime = 0;
        sendMessage(message, status, true);
        speechEndTime = 0;
    }


    @Override
    public void onAsrFinish(RecogResult recogResult) {
        Log.e("onAsrFinish", recogResult + "");
        super.onAsrFinish(recogResult);
        sendStatusMessage(SpeechConstant.CALLBACK_EVENT_ASR_FINISH, "识别一段话结束。如果是长语音的情况会继续识别下段话。");

    }

    /**
     * 长语音识别结束
     */
    @Override
    public void onAsrLongFinish() {
        super.onAsrLongFinish();
        sendStatusMessage(SpeechConstant.CALLBACK_EVENT_ASR_LONG_SPEECH, "长语音识别结束。");
    }


    /**
     * 使用离线命令词时，有该回调说明离线语法资源加载成功
     */
    @Override
    public void onOfflineLoaded() {
        sendStatusMessage(SpeechConstant.CALLBACK_EVENT_ASR_LOADED, "离线资源加载成功。没有此回调可能离线语法功能不能使用。");
    }

    /**
     * 使用离线命令词时，有该回调说明离线语法资源加载成功
     */
    @Override
    public void onOfflineUnLoaded() {
        sendStatusMessage(SpeechConstant.CALLBACK_EVENT_ASR_UNLOADED, "离线资源卸载成功。");
    }

    @Override
    public void onAsrExit() {
        super.onAsrExit();
        sendStatusMessage(SpeechConstant.CALLBACK_EVENT_ASR_EXIT, "识别引擎结束并空闲中");
    }

    private void sendStatusMessage(String eventName, String message) {
        message = "[" + eventName + "]" + message;
        sendMessage(message, status);
    }

    private void sendMessage(String message) {
        sendMessage(message, WHAT_MESSAGE_STATUS);
    }

    private void sendMessage(String message, int what) {
        sendMessage(message, what, false);
    }


    private void sendMessage(final String message, int what, boolean highlight) {
        if (needTime && what != STATUS_FINISHED) {
            //  message += "  ;time=" + System.currentTimeMillis();
        }

        Message msg = Message.obtain();
        msg.what = what;
        msg.arg1 = status;
        if (highlight) {
            msg.arg2 = 1;
        }
        msg.obj = message + "\n";
        //  put    String包含   asr.partial
        Log.e("数据是>>", message);
        if (message != null && message.contains("merged_res") && message.contains("semantic_form")) {


            resultsMessageMap.put(resuletsKey_yuyi, message);
            String CallBackID = array.optString(0);
            JSONArray newArray = new JSONArray();
            newArray.put(resultsMessageMap);
            // 调用方法将原生代码的执行结果返回给js层并触发相应的JS层回调函数
            JSUtil.execCallback(pWebview, CallBackID, newArray, JSUtil.OK, false);
        } else if (message != null && message.contains("错误码")) {
            String CallBackID = array.optString(0);
            JSONArray newArray = new JSONArray();
            newArray.put(resultsMessageMap);
            // 调用方法将原生代码的执行结果返回给js层并触发相应的JS层回调函数
            JSUtil.execCallback(pWebview, CallBackID, newArray, JSUtil.OK, false);
        }
    }

    @Override
    public void onAsrOnlineNluResult(String nluResult) {
        //这是最后的结果返回给前端           nluResult       map.put      包含两个    merged_res     semantic_form

        Log.e("nluResult", nluResult + "");
        if (nluResult != null && nluResult.contains("merged_res") && nluResult.contains("semantic_form")) {
            resultsMessageMap.put(resuletsKey, nluResult);
        }
        super.onAsrOnlineNluResult(nluResult);
        if (!nluResult.isEmpty()) {
            sendStatusMessage(SpeechConstant.CALLBACK_EVENT_ASR_PARTIAL, "原始语义识别结果json：" + nluResult);
        }
    }

}
