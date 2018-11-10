package kpl.qianzhanggui.com.chaobi_baiduyuyin.baidu;

import android.Manifest;
import android.app.Activity;

import android.content.pm.PackageManager;

import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;


import java.util.ArrayList;



/**
 * @author Rs  529811807@qq.com
 * @date 2018/11/9
 */
public class CheckPermission   {




    /**
     * android 6.0 以上需要动态申请权限
     */
    public static boolean initPermission( Activity activity) {
        boolean flag =true;
        String permissions[] = {Manifest.permission.RECORD_AUDIO,
                Manifest.permission.ACCESS_NETWORK_STATE,
                Manifest.permission.INTERNET,
                Manifest.permission.READ_PHONE_STATE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE
        };

        ArrayList<String> toApplyList = new ArrayList<String>();

        for (String perm : permissions) {
            if (PackageManager.PERMISSION_GRANTED != ContextCompat.checkSelfPermission(activity, perm)) {
                toApplyList.add(perm);
                // 进入到这里代表没有权限.
                flag= false;
                break;
            }
        }
        String tmpList[] = new String[toApplyList.size()];
        if (!toApplyList.isEmpty()) {
            ActivityCompat.requestPermissions(activity, toApplyList.toArray(tmpList), 123);
        }
        return true;
    }

}
