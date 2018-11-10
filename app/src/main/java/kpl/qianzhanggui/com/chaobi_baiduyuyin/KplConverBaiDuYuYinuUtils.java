package kpl.qianzhanggui.com.chaobi_baiduyuyin;


import android.util.Log;

import org.json.JSONArray;


import io.dcloud.common.DHInterface.IWebview;
import io.dcloud.common.DHInterface.StandardFeature;

import kpl.qianzhanggui.com.chaobi_baiduyuyin.baidu.CheckPermission;
import kpl.qianzhanggui.com.chaobi_baiduyuyin.baidu_kpl.BaiduKpl;
import kpl.qianzhanggui.com.chaobi_baiduyuyin.baidu_kpl.listener.MessageStatusRecogListener;
import kpl.qianzhanggui.com.chaobi_baiduyuyin.baidu_kpl.listener.MyRecognizer;
import kpl.qianzhanggui.com.chaobi_baiduyuyin.baidu_kpl.thread.ResultSendThread;

/**
 * 对前端传过来的base64进行处理
 *
 * @author Rs  529811807@qq.com
 * @date 2018/11/9
 */
public class KplConverBaiDuYuYinuUtils extends StandardFeature {
   private  int init = 0;   // 0是自己写    1 是demo代码

    public KplConverBaiDuYuYinuUtils(){
        BaiduKpl baiduKpl = InitBaidu.baiduKpl;
        MyRecognizer myRecognizer = InitBaidu.myRecognizer;
        baiduKpl.setMyRecognizer(myRecognizer);
    }




    /**
     *
     * start    baidu语音
     *
     * @param pWebview
     * @param array
     */
    public void kplbaidu_chaobi_yuyin(IWebview pWebview, JSONArray array) {
        int temp =init;
        Log.e("onstart", InitBaidu.baiduKpl + "");

        if(temp==0){
            MessageStatusRecogListener.pWebview =pWebview;
            MessageStatusRecogListener.array = array;
            InitBaidu.baiduKpl.start(pWebview.getContext());

          //  Thread resultThread = new Thread(new ResultSendThread(pWebview,array));
           // resultThread.start();
        }else{
            boolean checkPermission = CheckPermission.initPermission(pWebview.getActivity());
            if (checkPermission) {//有权限继续录制解析文件

                InitBaidu.baiDuYuYinUtils.onStart();
            } else {//没有权限的提示

            }
        }

    }

    /**
     * destory    baidu语音
     * @param pWebview
     * @param array
     */
    public void kplbaidu_chaobi_yuyin_ondestory(IWebview pWebview, JSONArray array) {
        Log.e("ondestory", InitBaidu.baiDuYuYinUtils + "");
        int temp =init;
        if(temp == 0){
            InitBaidu.baiduKpl.onDestroy(pWebview.getContext());
        }else{
            //停止发送
            InitBaidu.baiDuYuYinUtils.stop();
            //destory  baidu语音资源
            InitBaidu.baiDuYuYinUtils.onDestroy();
        }

    }

}
