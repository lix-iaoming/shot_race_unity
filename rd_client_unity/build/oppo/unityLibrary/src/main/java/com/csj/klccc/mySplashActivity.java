package com.csj.klccc;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;

import android.os.Bundle;

import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.util.Log;


import com.heytap.msp.mobad.api.InitParams;
import com.heytap.msp.mobad.api.MobAdManager;
import com.heytap.msp.mobad.api.ad.SplashAd;
import com.heytap.msp.mobad.api.listener.ISplashAdListener;
import com.heytap.msp.mobad.api.params.SplashAdParams;

import com.nearme.game.sdk.GameCenterSDK;
import com.unity3d.player.UnityPlayerActivity;

import java.util.ArrayList;
import java.util.List;

public class mySplashActivity extends Activity implements ISplashAdListener {
    private String APP_ID = "30431679";
    private String OPEN_ADS_ID = "273296";
    private static mySplashActivity _mInatance = null;
    private SplashAd mSplashAd = null; //开屏广告

    private List<String> mNeedRequestPMSList = new ArrayList<>();

    /**
     * @author taomaogan
     * @Date 2019/8/16
     **/
    private static final String TAG = "CloseSplashActivity";
    protected SplashAd vivoSplashAd;



    private int mStartedCount = 0;
    public  static mySplashActivity getInstance() {
        if (_mInatance == null) {
            _mInatance = new mySplashActivity();
        }
        return _mInatance;
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
       //oppoSDKAgent.getInstance().initialized(this);
//        String appSecret = "25a2b9f742cb46d1b42f5908cd257096";
//        GameCenterSDK.init(appSecret, this);

        InitParams initParams = new InitParams.Builder()
                .setDebug(true)
        //true 打开 SDK 日志，当应用发布 Release 版本时，必须注释掉这行代码的调用，或者设为 false
                        .build();
        /**
         * 调用这行代码初始化广告 SDK
         */
        MobAdManager.getInstance().init(this, APP_ID, initParams);

        checkAndRequestPermissions();
//        checkAndRequestPermissions();

        Log.i("shuifeng", "SplashAd java init get scheme param");
    }

    @Override
    protected void onDestroy() {
        MobAdManager.getInstance().exit(this);
        super.onDestroy();
    }

    protected void initSplashAd() {
        try {
            SplashAdParams splashAdParams = new SplashAdParams.Builder()
                    .setFetchTimeout(3000)
                    .setTitle("快乐冲冲冲")
                    .setDesc("冲就完事儿了")
                    .build();
//            mSplashAd =  mSplashAd = new SplashAd(this, OPEN_ADS_ID, new mySplashAdListener(), splashAdParams);
            mSplashAd =  mSplashAd = new SplashAd(this, OPEN_ADS_ID, this, splashAdParams);

        } catch (Exception err) {
            Log.e("shuifeng", "SplashAd initSplashAd: err  is " + err );
            mSplashAd = null;
            endActive();
        }
    }

    public void endActive() {
        try{
            Intent it=new Intent(getApplicationContext(), UnityPlayerActivity.class);//启动MainActivity
            startActivity(it);
            finish();//关闭当前活动
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @Override
    public void onAdDismissed() {
        Log.d("shuifeng", "SplashAd onADDismissed：");
        endActive();
    }
    @Override
    public void onAdShow() {
        Log.d("shuifeng", "SplashAd onAdShow：");
    }
    @Override
    public void onAdFailed(String s) {

    }
    @Override
    public void onAdFailed(int i, String s) {
        Log.d("shuifeng", "SplashAd onAdFailed：" + i + "  " + s);
        endActive();
    }
    @Override
    public void onAdClick() {
        Log.d("shuifeng", "SplashAd onAdClick：");
    }

//    //开屏广告
//    public class mySplashAdListener implements ISplashAdListener {
//        @Override
//        public void onAdDismissed() {
//            Log.d("shuifeng", "onADDismissed：");
//            endActive();
//        }
//        @Override
//        public void onAdShow() {
//
//        }
//        @Override
//        public void onAdFailed(String s) {
//
//        }
//        @Override
//        public void onAdFailed(int i, String s) {
//            Log.d("shuifeng", "onADDismissed：" + i + "  " + s);
//            endActive();
//        }
//        @Override
//        public void onAdClick() {
//
//        }
//    }

    /**
     * 申请 SDK 运行需要的权限
     * 注意：READ_PHONE_STATE、WRITE_EXTERNAL_STORAGE 两个权限是必须权限，没有这两个权限
     SDK 无法正常获得广告。
     * WRITE_CALENDAR、ACCESS_FINE_LOCATION 是两个可选权限；没有不影响 SDK 获取广告；但是
     如果应用申请到该权限，会显著提升应用的广告收益。
     */
    private void checkAndRequestPermissions() {
        /**
         * READ_PHONE_STATE、WRITE_EXTERNAL_STORAGE 两个权限是必须权限，没有这两个权限 SDK
         无法正常获得广告。
         */
        if (PackageManager.PERMISSION_GRANTED !=
                 ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE)) {
            mNeedRequestPMSList.add(Manifest.permission.READ_PHONE_STATE);
        }
        if (PackageManager.PERMISSION_GRANTED !=
                ActivityCompat.checkSelfPermission(this,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
            mNeedRequestPMSList.add(Manifest.permission.WRITE_EXTERNAL_STORAGE);
        }
        /**
         * WRITE_CALENDAR、ACCESS_FINE_LOCATION 是两个可选权限；没有不影响 SDK 获取广告；
         但是如果应用申请到该权限，会显著提升应用的广告收益。
         */
        if (PackageManager.PERMISSION_GRANTED !=
                ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_CALENDAR)) {
            mNeedRequestPMSList.add(Manifest.permission.WRITE_CALENDAR);
        }
        if (PackageManager.PERMISSION_GRANTED !=
                ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)) {
            mNeedRequestPMSList.add(Manifest.permission.ACCESS_FINE_LOCATION);
        }
        //
        if (0 == mNeedRequestPMSList.size()) {
            /**
             * 权限都已经有了，那么直接调用 SDK 请求广告。
             */
                initSplashAd();
            } else {
            /**
             * 有权限需要申请，主动申请。
             */
            String[] temp = new String[mNeedRequestPMSList.size()];
            mNeedRequestPMSList.toArray(temp);
            ActivityCompat.requestPermissions(this, temp, 100);
        }
    }
    /**
     * 处理权限申请的结果
     *
     * @param requestCode
     * @param permissions
     * @param grantResults
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[]
            permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            /**
             *处理 SDK 申请权限的结果。
             */
            case 100:
                if (hasNecessaryPMSGranted()) {
                    /**
                     * 应用已经获得 SDK 运行必须的 READ_PHONE_STATE、WRITE_EXTERNAL_STORAGE
                     两个权限，直接请求广告。
                     */
                    //initSplashAd();
                } else {
                    /**
                     * 如果用户没有授权，那么应该说明意图，引导用户去设置里面授权。
                     */
                    //endActive();
                }
                break;
            default:
                break;
        }
    }
    /**
     * 判断应用是否已经获得 SDK 运行必须的 READ_PHONE_STATE、WRITE_EXTERNAL_STORAGE 两个权
     限。
     * @return
     */
    private boolean hasNecessaryPMSGranted() {
        if (PackageManager.PERMISSION_GRANTED ==
                ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE)) {
            if (PackageManager.PERMISSION_GRANTED ==
                    ActivityCompat.checkSelfPermission(this,
                            Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                return true;
            }
        }
        return false;
    }
}


