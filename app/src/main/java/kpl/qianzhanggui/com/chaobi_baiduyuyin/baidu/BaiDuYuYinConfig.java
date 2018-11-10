package kpl.qianzhanggui.com.chaobi_baiduyuyin.baidu;



import java.util.LinkedHashMap;
import java.util.Map;

/**
 * @author Rs  529811807@qq.com
 * @date 2018/11/9
 */
public class BaiDuYuYinConfig {
    private static  Map<String, Object> params = new LinkedHashMap<String, Object>();
    /*
     * 本Activity中是否需要调用离线命令词功能。根据此参数，判断是否需要调用SDK的ASR_KWS_LOAD_ENGINE事件
     */
    public static final boolean enableOffline = true;
    /**
     *  设置各种默认参数
     */
    public static Map<String, Object> getParams(){

        /**
         * 离线命令词及在线识别混合，在线优先
         */
        params.put("decoder",2);

        params.put("accept-audio-volume",false);
        /**
         * 开启本地语义解释（不支持英语）
         */
        params.put("nlu","enable");
        /**
         * 普通话搜索模型 + 在线语义
         */
        params.put("pid",15361);

        return params;
    }

}
