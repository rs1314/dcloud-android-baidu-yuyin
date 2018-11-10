package kpl.qianzhanggui.com.chaobi_baiduyuyin;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;

import io.dcloud.application.DCloudApplication;
import io.dcloud.common.DHInterface.StandardFeature;
import kpl.qianzhanggui.com.chaobi_baiduyuyin.baidu.BaiDuYuYinUtils;
import kpl.qianzhanggui.com.chaobi_baiduyuyin.baidu_kpl.BaiduKpl;
import kpl.qianzhanggui.com.chaobi_baiduyuyin.baidu_kpl.listener.MyRecognizer;

/**
 * @author Rs  529811807@qq.com
 * @date 2018/11/10
 */
public class InitBaidu extends StandardFeature {
    public static  BaiDuYuYinUtils baiDuYuYinUtils;
    public static  BaiduKpl baiduKpl;
    public static MyRecognizer myRecognizer;
    public  int init = 0;   // 0是自己写    1 是demo代码
    /**
     * init baidu语音
     * @param pContext
     * @param pSavedInstanceState
     * @param pRuntimeArgs
     */
    public void onStart(Context pContext, Bundle pSavedInstanceState, String[] pRuntimeArgs) {
        Log.e("init baidu_yuyin","init baidu_yuYIn");
        int i = init;
        /**
         * 如果需要在应用启动时进行初始化，可以继承这个方法，并在properties.xml文件的service节点添加扩展插件的注册即可触发onStart方法
         * */

        if (pContext instanceof DCloudApplication) {
            DCloudApplication dCloudApplication = (DCloudApplication) pContext;
            if(i == 0){
                baiduKpl = new BaiduKpl();
                baiduKpl.onCreate(dCloudApplication);
                this.myRecognizer = baiduKpl.getMyRecognizer();
            }else {
                baiDuYuYinUtils = new BaiDuYuYinUtils();
                baiDuYuYinUtils.onCreate(dCloudApplication);
            }
        }
    }
}
