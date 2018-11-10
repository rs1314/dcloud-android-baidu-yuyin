package kpl.qianzhanggui.com.chaobi_baiduyuyin.baidu_kpl.thread;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.Map;

import io.dcloud.common.DHInterface.IWebview;
import io.dcloud.common.util.JSUtil;
import kpl.qianzhanggui.com.chaobi_baiduyuyin.baidu_kpl.listener.MessageStatusRecogListener;

/**
 * @author Rs  529811807@qq.com
 * @date 2018/11/10
 */
public class ResultSendThread implements Runnable {
    private Object lock = MessageStatusRecogListener.lock;
    private IWebview pWebview;
    private JSONArray array;

    public ResultSendThread() {
    }



    public ResultSendThread(IWebview pWebview, JSONArray array) {
        this.pWebview = pWebview;
        this.array = array;
    }

    @Override
    public void run() {
        synchronized (lock) {
            try {
                this.lock.notify();
                String CallBackID = array.optString(0);
                JSONArray newArray = new JSONArray();
                Map<String, String> resultsMessageMap = MessageStatusRecogListener.resultsMessageMap;
                newArray.put(resultsMessageMap);
                // 调用方法将原生代码的执行结果返回给js层并触发相应的JS层回调函数
                JSUtil.execCallback(pWebview, CallBackID, newArray, JSUtil.OK, false);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
