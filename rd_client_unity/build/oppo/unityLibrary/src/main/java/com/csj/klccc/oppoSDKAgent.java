package com.csj.klccc;

import android.app.Activity;
import android.content.Context;
import android.graphics.Point;
import android.os.Build;
import android.support.annotation.Dimension;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.heytap.msp.mobad.api.InitParams;
import com.heytap.msp.mobad.api.MobAdManager;
import com.heytap.msp.mobad.api.ad.BannerAd;
import com.heytap.msp.mobad.api.ad.InterstitialAd;
import com.heytap.msp.mobad.api.ad.RewardVideoAd;
import com.heytap.msp.mobad.api.listener.IBannerAdListener;
import com.heytap.msp.mobad.api.listener.IInterstitialAdListener;
import com.heytap.msp.mobad.api.listener.IRewardVideoAdListener;
import com.nearme.game.sdk.GameCenterSDK;
import com.nearme.game.sdk.callback.GameExitCallback;
import com.unity3d.player.R;
import com.unity3d.player.UnityPlayer;
import com.unity3d.player.UnityPlayerActivity;

import org.json.JSONException;
import org.json.JSONObject;

public class oppoSDKAgent {
    private static oppoSDKAgent _mInatance = null;
    private Activity _mActivity = null;
//    private RelativeLayout mAdContainer;

    private  int adsKay; //这是哪里调起的广告


   private  final  int  ADS_TYPE_BANNER = 1; //banner 广告
    private  final  int  ADS_TYPE_INSERT = 2;   //插屏广告
    private  final  int  ADS_TYPE_VOID = 3;    //激励视频
    private  final  int  ADS_TYPE_OPEN = 4;    //开屏广告



    private BannerAd mBannerAd = null;     //banner
    private InterstitialAd mInterstitialAd = null; //插屏
    private RewardVideoAd mRewardVideoAd = null;   //视频激励

    private String APP_ID = "30431679";
    private String BANNER_ADS_ID = "273294";
    private String INSERT_ADS_ID = "273295";
    private String VIDEO_ADS_ID = "273298";
    private String OPEN_ADS_ID = "273296";


    public static oppoSDKAgent getInstance(){
        if(_mInatance == null){
            _mInatance = new oppoSDKAgent();
        }
        return _mInatance;
    }
    public void initialized(Activity mActivity){
//        mAdContainer = (RelativeLayout) mActivity.findViewById(R.id.ad_container);
        Log.i("shuifeng", "initialized: ");
        _mActivity = mActivity;
        String appSecret = "25a2b9f742cb46d1b42f5908cd257096";
        GameCenterSDK.init(appSecret, _mActivity);


        InitParams initParams = new InitParams.Builder()
                .setDebug(true) //true 打开 SDK 日志，当应用发布 Release 版本时，必须注释掉这行代码的调用，或者设为 false
                .build();
/**  * 调用这行代码初始化广告 SDK
 */
        MobAdManager.getInstance().init(mActivity, APP_ID, initParams);

//        initVideoAds();
//        initBannerAds();
//        initInsertAds();
    }
    public void pay(){
//        GameCenterSDK.getInstance().doSinglePay(this, payInfo,
//                new SinglePayCallback() {
//                    // add OPPO 支付成功处理逻辑~
//                    public void onSuccess(String resultMsg) {
//                        Toast.makeText(DemoActivity.this, "支付成功",Toast.LENGTH_SHORT).show();}
//                    // add OPPO 支付失败处理逻辑~
//                    public void onFailure(String resultMsg, int resultCode) {
//                        if (PayResponse.CODE_CANCEL != resultCode) {
//                            Toast.makeText(DemoActivity.this, "支付失败",Toast.LENGTH_SHORT).show();
//                        } else { // 取消支付处理
//                            Toast.makeText(DemoActivity.this, "支付取消",Toast.LENGTH_SHORT).show();
//                        }}
//                    /* bySelectSMSPay 为true表示回调来自于支付渠道列表选择短信支付，false表示没有
//                    网络等非主动选择短信支付时候的回调 */
//                    public void onCallCarrierPay(PayInfo payInfo, boolean bySelectSMSPay) {
//// add 运营商支付逻辑~
//        Toast.makeText(DemoActivity.this, "运营商支付",Toast.LENGTH_SHORT).show();
//    }
//});
    }
    public void gameExitGame(){
        Log.d("shuifeng", "sdk gameExitGame：");
        GameCenterSDK.getInstance().onExit(_mActivity,  new myGameExitCallback());
    }

    class myGameExitCallback implements GameExitCallback {

        @Override
        public void exitGame() {
            /**
             * 在你的应用程序进程退出时，调用该方法释放 SDK 资源
             * */
//            MobAdManager.getInstance().exit(_mActivity);

            Log.i("shuifeng", "exitGame: ");
            _mActivity.finish();
        }
    }
    public void moreGame(){
        Log.i("shuifeng", "moreGame: 1");
        GameCenterSDK.getInstance().jumpLeisureSubject();
        Log.i("shuifeng", "moreGame: 2");

    }

    public void showAds(int _type_, int _adKey_) {
            switch (_type_) {
                case ADS_TYPE_BANNER: {
//                    mFrameLayout = _mActivity.getWindow().getDecorView().findViewById(android.R.id.content);

                }break;
                case ADS_TYPE_INSERT: {
                    mInterstitialAd.showAd();
                }break;
                case ADS_TYPE_VOID: {
                    adsKay = _adKey_;
                    mRewardVideoAd.showAd();

                }break;
                case ADS_TYPE_OPEN: {

                }break;
                default: {
                    Log.e("shuifeng", "showAds: "+ "not support ads type is " + _type_);
                }
            }
    }

    void initBannerAds() {
        if ( mBannerAd == null ) {
            mBannerAd = new BannerAd(_mActivity, BANNER_ADS_ID);
            mBannerAd.setAdListener(new myIBannerAdListener());
        }
        mBannerAd.loadAd();

    }

    void initInsertAds() {
        if (mInterstitialAd == null) {
            mInterstitialAd = new InterstitialAd(_mActivity, INSERT_ADS_ID);
            mInterstitialAd.setAdListener(new myIInterstitialAdListener());
        }
        mInterstitialAd.loadAd();
    }

    void initVideoAds() {
        if (mRewardVideoAd == null) {
            mRewardVideoAd = new RewardVideoAd(_mActivity, VIDEO_ADS_ID, new myINativeAdvanceLoadListener());
            Log.i("shuifeng", "initVideoAds: ");
        }
        mRewardVideoAd.loadAd();
    }

    void  onAdsReady(int _adstype_) {
        Thread myThread=new Thread(){//创建子线程
            @Override
            public void run() {
                try{
                    Log.i("shuifeng", "onAdsReady:  _adstype_ is " + _adstype_);
                    UnityPlayer.UnitySendMessage("Canvas", "onAdsReady", ""+_adstype_);
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        };
        myThread.start();//启动线程


    }

    void onAdsFinish(int _code_) {

        String tempStr = _code_ + "," + adsKay;

        Thread myThread=new Thread(){//创建子线程
            @Override
            public void run() {
                try{
                    Log.i("shuifeng", "onAdsReady:  _adstype_ is " + tempStr);
                    UnityPlayer.UnitySendMessage("Canvas", "isReworldVideoComplete", tempStr);
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        };
        myThread.start();//启动线程
    }

    public void loadAdsAgain(int _adsType_) {
        switch (_adsType_) {
            case ADS_TYPE_BANNER : {
                if (mBannerAd == null) {
                    initBannerAds();
                }
                mBannerAd.loadAd();
            }break;
            case ADS_TYPE_INSERT : {
                if (mInterstitialAd == null) {
                    initInsertAds();
                }
                mInterstitialAd.loadAd();

            }break;
            case ADS_TYPE_VOID : {
                if (mRewardVideoAd == null) {
                    initVideoAds();
                }
                mRewardVideoAd.loadAd();
            }break;
        }
    }

    //banner 的监听
    class myIBannerAdListener implements IBannerAdListener {

        @Override
        public void onAdReady() {
            Log.i("shuifeng", "Banner onAdReady:  ");
            onAdsReady(ADS_TYPE_BANNER);
            View adView = mBannerAd.getAdView();
            //挂载上去
            if (null != adView) {
                Display display =  _mActivity.getWindowManager().getDefaultDisplay();
                Point size = new Point();
                display.getSize(size);
                FrameLayout mFrameLayout  = (FrameLayout) _mActivity.getWindow().getDecorView().findViewById(android.R.id.content);
                int tempHeight = size.y;

                //Build.VERSION.SDK;
                float tempRatio =(float)size.x / size.y;
                Log.e("shuifeng", "onAdReady: tempRatio == "+ tempRatio );
                Log.e("shuifeng", "onAdReady: size.x == "+ size.x + "  size.y  == " + size.y );
                if (tempRatio < 0.5) {
                    tempHeight = size.y +10;
                } else {
                    tempHeight = size.y - 100;
                }

                ViewGroup.MarginLayoutParams tvParams = new FrameLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT, 1);
                tvParams.setMargins(0, tempHeight, 0, 0);
                mFrameLayout.addView(adView, tvParams);
                Log.e("shuifeng", "onAdReady: phoneSdkVersion == "+ tempHeight + "  size.y  == " + size.y );




                //int tempWidth = size.x /2 - 150;

//                RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
//                layoutParams.addRule(RelativeLayout.ALIGN_PARENT_TOP);
//                Display display =  _mActivity.getWindowManager( ).getDefaultDisplay();
//                Point size = new Point();
//                display.getSize(size);
//
            }
        }

        @Override
        public void onAdClose() {
            Log.i("shuifeng", "Banner onAdClose:  ");

        }

        @Override
        public void onAdShow() {
            Log.i("shuifeng", "Banner onAdShow:  ");

        }

        @Override
        public void onAdFailed(String s) {
            Log.i("shuifeng", "Banner onAdFailed: " );
        }

        @Override
        public void onAdFailed(int code,String err) {
            Log.i("shuifeng", "Banner onAdShow:  code =  "+ code + "   err is "+ err );

        }

        @Override
        public void onAdClick() {
            Log.i("shuifeng", "Banner  onAdClick: ");
        }
    }
    // 插屏的监听
    class myIInterstitialAdListener implements IInterstitialAdListener {

        @Override
        public void onAdReady() {
            Log.i("shuifeng", "Interstitial  onAdReady: ");
            onAdsReady(ADS_TYPE_INSERT);
        }

        @Override
        public void onAdClose() {
            Log.i("shuifeng", "Interstitial  onAdClose: ");
        }

        @Override
        public void onAdShow() {
            Log.i("shuifeng", "Interstitial  onAdShow: ");
        }

        @Override
        public void onAdFailed(String s) {
            Log.i("shuifeng", "Interstitial  onAdFailed: " + s);
        }

        @Override
        public void onAdFailed(int code, String err) {
            Log.i("shuifeng", "Interstitial onAdFailed:  code =  "+ code + "   err is "+ err );
        }

        @Override
        public void onAdClick() {
            Log.i("shuifeng", "Interstitial onAdClick ");
        }
    }
    //视屏激励的监听
    class myINativeAdvanceLoadListener implements IRewardVideoAdListener {

        @Override
        public void onAdSuccess() {
            Log.e("shuifeng", "video onAdSuccess: ");
            onAdsReady(ADS_TYPE_VOID);
        }

        @Override
        public void onAdFailed(String s) {

        }

        @Override
        public void onAdFailed(int code, String retStr) {

            Log.e("shuifeng", "video onAdFailed: code is "+ code + "retstr is " + retStr );
        }

        @Override
        public void onAdClick(long l) {
            Log.e("shuifeng", "video onAdClick:  ");
        }

        @Override
        public void onVideoPlayStart() {
            Log.e("shuifeng", "video onVideoPlayStart:  ");
        }

        @Override
        public void onVideoPlayComplete() {
            Log.e("shuifeng", "video  onVideoPlayComplete: ");

            //通知unity 发奖励
        }

        @Override
        public void onVideoPlayError(String s) {
            Log.e("shuifeng", "video onVideoPlayError: "+ s);

            onAdsFinish(-1);
            //通知unity 不发奖励
        }

        @Override
        public void onVideoPlayClose(long l) {
            Log.e("shuifeng", "video onVideoPlayClose: ");
            onAdsFinish(-2);
        }

        @Override
        public void onLandingPageOpen() {
            Log.e("shuifeng", "video onLandingPageOpen: ");
        }

        @Override
        public void onLandingPageClose() {
            Log.e("shuifeng", "video onLandingPageClose: ");
        }

        @Override
        public void onReward(Object... objects) {
            //            奖励发放
            Log.e("shuifeng", "video onReward: ");
            onAdsFinish(0);
            //通知unity 发奖励
        }
    }



}


/*
 implementation 'com.android.support:appcompat-v7:26.0.0'
    implementation 'com.squareup.wire:wire-runtime:2.2.0'
    implementation 'pl.droidsonroids.gif:android-gif-drawable:1.2.18'



    public  void onClickMoreGameBtn() {
        oppoSDKAgent.getInstance().moreGame();
    }

     public void showAds(String _str_) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                String[] tempSreArray = _str_.split(",");
                Log.d("shuifeng", "showAds: " + _str_);
                int adType = Integer.parseInt(tempSreArray[0]);
                int adKey = Integer.parseInt(tempSreArray[1]);
                oppoSDKAgent.getInstance().showAds(adType, adKey);

            }
        });
    }

    public void loadAgain(String _adsType_) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                int tempIndex = Integer.parseInt(_adsType_);
                oppoSDKAgent.getInstance().loadAdsAgain(tempIndex);
            }
        });
    }

}

     if(keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_UP){
        oppoSDKAgent.getInstance().gameExitGame();
    }

    oppoSDKAgent.getInstance().initialized(this);

    <?xml version="1.0" encoding="utf-8"?>
<!-- GENERATED BY UNITY. REMOVE THIS COMMENT TO PREVENT OVERWRITING WHEN EXPORTING AGAIN-->
<manifest xmlns:android="http://schemas.android.com/apk/res/android" package="com.unity3d.player" xmlns:tools="http://schemas.android.com/tools">

  <supports-gl-texture android:name="GL_KHR_texture_compression_astc_ldr" />
  <uses-permission android:name="android.permission.INTERNET" />
  <uses-feature android:name="android.hardware.touchscreen" android:required="false" />
  <uses-feature android:name="android.hardware.touchscreen.multitouch" android:required="false" />
  <uses-feature android:name="android.hardware.touchscreen.multitouch.distinct" android:required="false" />

  <!--SDK 可选权限配置开始；建议应用配置定位权限，可以提升应用的广告收益-->
  <uses-permission android:name="android.permission.ACCESS_COARSE_LOCATION" /> <!--如果应用需要精准定位的话加上该权限-->
  <uses-permission android:name="android.permission.ACCESS_FINE_LOCATION" /> <!--Android Q 上如果应用需要精准定位的话加上该权限-->
  <uses-permission android:name="android.permission.ACCESS_BACKGROUND_LOCATION" /> <!--SDK 可选权限配置结束-->

  <uses-permission android:name="android.permission.READ_PHONE_STATE" />
  <uses-permission android:name="android.permission.WRITE_EXTERNAL_STORAGE" />
  <uses-permission android:name="android.permission.WRITE_CALENDAR" />
  <uses-permission android:name="android.permission.ACCESS_FINE_LOCATION" />




  <application>

    <activity
        android:name="com.unity3d.player.UnityPlayerActivity"
        android:theme="@style/UnityThemeSelector"
        android:screenOrientation="sensorPortrait"
        android:launchMode="singleTask"
        android:requestLegacyExternalStorage="true"
        android:networkSecurityConfig="@xml/network_security_config"
        android:configChanges="mcc|mnc|locale|touchscreen|keyboard|keyboardHidden|navigation|orientation|screenLayout|uiMode|screenSize|smallestScreenSize|fontScale|layoutDirection|density"
        android:hardwareAccelerated="false">
      <!--        android:theme="@style/Theme.notAnimation" -->

    </activity>

    <activity
        android:name="com.csj.klccc.mySplashActivity"
        android:screenOrientation="sensorPortrait"
        android:configChanges="orientation|keyboardHidden|screenSize"
        android:label="@string/app_name"
        tools:ignore="DuplicateActivity">
      <intent-filter>
        <action android:name="android.intent.action.MAIN" />
        <category android:name="android.intent.category.LAUNCHER" />
      </intent-filter>
      <meta-data android:name="unityplayer.UnityActivity" android:value="true" />
      <meta-data android:name="android.notch_support" android:value="true" />

    </activity>
    <meta-data android:name="unity.splash-mode" android:value="0" />
    <meta-data android:name="unity.splash-enable" android:value="True" />
    <meta-data android:name="notch.config" android:value="portrait|landscape" />
    <meta-data android:name="unity.build-id" android:value="964d2ee4-45a2-4000-9c81-7da5bac52ec4" />


    <meta-data android:name="debug_mode" android:value="false" /> <!-- 调试开关，发布时候设置false -->
    <meta-data android:name="is_offline_game" android:value="true" /> <!-- true:单机游戏 false:网游 -->
    <meta-data android:name="app_key" android:value="202e28aeadb2440eaad32df22caf5822" /> <!--appKey,务必换成 游戏自己的参数 -->
    <uses-library android:name="org.apache.http.legacy" android:required="false"/> <!--9.0及以上设备可能需要 -->



    <provider
        android:name="com.opos.mobad.provider.MobAdGlobalProvider"
        android:authorities="${applicationId}.MobAdGlobalProvider"
        android:exported="false" />
    <provider
        android:name="com.bytedance.sdk.openadsdk.multipro.TTMultiProvider"
        android:authorities="${applicationId}.TTMultiProvider"
        android:exported="false" />
    <provider
        android:name="com.bytedance.sdk.openadsdk.TTFileProvider"
        android:authorities="${applicationId}.TTFileProvider"
        android:exported="false"
        android:grantUriPermissions="true">
      <meta-data android:name="android.support.FILE_PROVIDER_PATHS"
          android:resource="@xml/file_paths" />
    </provider>
    <provider
        android:name="android.support.v4.content.FileProvider"
        android:authorities="${applicationId}.fileprovider"
        android:exported="false"
        android:grantUriPermissions="true">
      <meta-data
          android:name="android.support.FILE_PROVIDER_PATHS"
          android:resource="@xml/gdt_file_path" />
    </provider>
    <provider
        android:name="com.heytap.msp.mobad.api.MobFileProvider"
        android:authorities="${applicationId}.MobFileProvider"
        android:exported="false"
        android:grantUriPermissions="true">
      <meta-data
          android:name="android.support.FILE_PROVIDER_PATHS"
          android:resource="@xml/mobad_provider_paths" />
    </provider>

  </application>
  <uses-feature android:glEsVersion="0x00020000" />


</manifest>


 */

