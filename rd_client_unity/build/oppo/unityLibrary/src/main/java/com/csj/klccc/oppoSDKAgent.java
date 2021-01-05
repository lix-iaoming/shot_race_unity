package com.csj.klccc;

import android.app.Activity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.FrameLayout;

import com.heytap.instant.upgrade.util.Constants;
import com.heytap.msp.mobad.api.InitParams;
import com.heytap.msp.mobad.api.MobAdManager;
import com.heytap.msp.mobad.api.ad.BannerAd;
import com.heytap.msp.mobad.api.ad.InterstitialAd;
import com.heytap.msp.mobad.api.ad.RewardVideoAd;
import com.heytap.msp.mobad.api.ad.SplashAd;
import com.heytap.msp.mobad.api.listener.IBannerAdListener;
import com.heytap.msp.mobad.api.listener.IInterstitialAdListener;
import com.heytap.msp.mobad.api.listener.INativeAdvanceLoadListener;
import com.heytap.msp.mobad.api.listener.IRewardVideoAdListener;
import com.heytap.msp.mobad.api.listener.ISplashAdListener;
import com.heytap.msp.mobad.api.params.INativeAdvanceData;
import com.heytap.msp.mobad.api.params.SplashAdParams;
import com.nearme.game.sdk.GameCenterSDK;
import com.nearme.game.sdk.callback.GameExitCallback;
import com.unity3d.player.UnityPlayer;

import java.util.List;

public class oppoSDKAgent {
    private static oppoSDKAgent _mInatance = null;
    private Activity _mActivity = null;
    private FrameLayout mFrameLayout; //广告展示的layout

    private  final  int  ADS_TYPE_BANNER = 1; //banner 广告
    private  final  int  ADS_TYPE_INSERT = 2;   //插屏广告
    private  final  int  ADS_TYPE_VOID = 3;    //激励视频
    private  final  int  ADS_TYPE_OPEN = 4;    //开屏广告


    private SplashAd mSplashAd = null; //开屏广告
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
        Log.i("shuifeng", "initialized: ");
        _mActivity = mActivity;
        String appSecret = "25a2b9f742cb46d1b42f5908cd257096";
        GameCenterSDK.init(appSecret, _mActivity);

        InitParams initParams = new InitParams.Builder()
                .setDebug(false)
                //true 打开 SDK 日志，当应用发布 Release 版本时，必须注释掉这行代码的调用，或者设为 false
                .build();
        /**
         * 调用这行代码初始化广告 SDK
         */
        MobAdManager.getInstance().init(_mActivity, APP_ID, initParams);

        initVideoAds();
        initBannerAds();
        initInsertAds();
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

        GameCenterSDK.getInstance().onExit(_mActivity,  new myGameExitCallback());
    }

    class myGameExitCallback implements GameExitCallback {

        @Override
        public void exitGame() {
            /**
             * 在你的应用程序进程退出时，调用该方法释放 SDK 资源
             * */
            MobAdManager.getInstance().exit(_mActivity);

            Log.i("shuifeng", "exitGame: ");
            _mActivity.finish();
        }
    }
    public void moreGame(){
        GameCenterSDK.getInstance().jumpLeisureSubject();
    }

    public void showAds(int _type_) {
            switch (_type_) {
                case ADS_TYPE_BANNER: {
                    if (mBannerAd == null ) {
                        initBannerAds();
                    }else  {

                        mFrameLayout = _mActivity.getWindow().getDecorView().findViewById(android.R.id.content);
                        View adView = mBannerAd.getAdView();
                        if (null != adView) {
                            mFrameLayout.addView(adView);
                        }
//                        mBannerAd.showAd();
                        //挂载上去
                    }
                }break;
                case ADS_TYPE_INSERT: {
                    if (mInterstitialAd == null) {
                        initInsertAds();
                    }
                    mInterstitialAd.showAd();
                }break;
                case ADS_TYPE_VOID: {
                    if (mRewardVideoAd == null || mRewardVideoAd.isReady() != true) {
                        initVideoAds();
                    }
                    mRewardVideoAd.showAd();

                }break;
                case ADS_TYPE_OPEN: {

                }break;
                default: {
                    Log.e("shuifeng", "showAds: "+ "not support ads type is " + _type_);
                }
            }
    }

    void initSplashAd() {
        if (mSplashAd == null) {
            try {
                /**
                 * SplashAd 初始化参数、这里可以设置获取广告最大超时时间，
                 * 广告下面半屏的应用标题+应用描述
                 * 注意：应用标题和应用描述是必传字段，不传将抛出异常
                 */
                SplashAdParams splashAdParams = new SplashAdParams.Builder()
                        .setFetchTimeout(3000)
                        .setTitle("快乐冲冲冲")
                        .setDesc("")
                        .build();
                /**
                 * 构造 SplashAd 对象
                 * 注意：构造函数传入的几个形参都不能为空，否则将抛出 NullPointerException 异常。
                 */
                mSplashAd = new SplashAd(_mActivity, OPEN_ADS_ID , new myISplashAdListener(), splashAdParams);
            } catch (Exception e) {
                mSplashAd = null;
                Log.w("shuifneg", "loadSplashAd err is ", e);
                /**
                 * 出错，直接 finish(),跳转应用主页面。
                 */
//                Intent intent = new Intent(this, MainActivity.class);
//                startActivity(intent);
//                finish();
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
        UnityPlayer.UnitySendMessage("admanager", "onAdsReady", ""+_adstype_);
    }



    void loadAdsAgain(int _adsType_) {
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



    //开屏的监听
    class myISplashAdListener implements ISplashAdListener {

        @Override
        public void onAdDismissed() {

        }

        @Override
        public void onAdShow() {

        }

        @Override
        public void onAdFailed(String s) {

        }

        @Override
        public void onAdFailed(int i, String s) {

        }

        @Override
        public void onAdClick() {

        }
    }
    //banner 的监听
    class myIBannerAdListener implements IBannerAdListener {

        @Override
        public void onAdReady() {
            Log.i("shuifeng", "onAdReady:  ");
            onAdsReady(ADS_TYPE_BANNER);
        }

        @Override
        public void onAdClose() {
            Log.i("shuifeng", "onAdClose:  ");

        }

        @Override
        public void onAdShow() {
            Log.i("shuifeng", "onAdShow:  ");

        }

        @Override
        public void onAdFailed(String s) {
            Log.i("shuifeng", "onAdFailed: " );
        }

        @Override
        public void onAdFailed(int code,String err) {
            Log.i("shuifeng", "onAdShow:  code = "+ code + "err is "+ err );

        }

        @Override
        public void onAdClick() {
            Log.i("shuifeng", "onAdClick: ");
        }
    }
    // 插屏的监听
    class myIInterstitialAdListener implements IInterstitialAdListener {

        @Override
        public void onAdReady() {
            onAdsReady(ADS_TYPE_INSERT);
        }

        @Override
        public void onAdClose() {

        }

        @Override
        public void onAdShow() {

        }

        @Override
        public void onAdFailed(String s) {

        }

        @Override
        public void onAdFailed(int i, String s) {

        }

        @Override
        public void onAdClick() {

        }
    }
    //视屏激励的监听
    class myINativeAdvanceLoadListener implements IRewardVideoAdListener {


        @Override
        public void onAdSuccess() {
            Log.e("shuifeng", "onAdSuccess: ");
            onAdsReady(ADS_TYPE_VOID);
        }

        @Override
        public void onAdFailed(String s) {

        }

        @Override
        public void onAdFailed(int code, String retStr) {
            Log.e("shuifeng", "onAdFailed: code is "+ code + "retstr is " + retStr );
        }

        @Override
        public void onAdClick(long l) {

        }

        @Override
        public void onVideoPlayStart() {

        }

        @Override
        public void onVideoPlayComplete() {
            Log.e("shuifeng", "onVideoPlayComplete: ");
            UnityPlayer.UnitySendMessage("admanager", "isReworldVideoComplete", "0");
            //通知unity 发奖励
        }

        @Override
        public void onVideoPlayError(String s) {
            Log.e("shuifeng", "onVideoPlayError: "+ s);
            UnityPlayer.UnitySendMessage("admanager", "isReworldVideoComplete", "-1");
            //通知unity 不发奖励
        }

        @Override
        public void onVideoPlayClose(long l) {
            Log.e("shuifeng", "onVideoPlayClose: ");
            UnityPlayer.UnitySendMessage("admanager", "isReworldVideoComplete", "-2");
        }

        @Override
        public void onLandingPageOpen() {

        }

        @Override
        public void onLandingPageClose() {

        }

        @Override
        public void onReward(Object... objects) {
            //            奖励发放
            Log.e("shuifeng", "onReward: ");
            UnityPlayer.UnitySendMessage("admanager", "isReworldVideoComplete", "0");
            //通知unity 发奖励
        }
    }





//        oppoSDKAgent.getInstance().initialized(this);
//        tools.getInstance().initialized(this);

//    if(keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_UP){
//        oppoSDKAgent.getInstance().gameExitGame();
//    }


//    public  void onClickMoreGameBtn() {
//        Log.d("shuifeng", "onClickMoreGameBtn: 1");
//        try {
//            UnityPlayer.UnitySendMessage("Canvas", "onClickConfirmBtn","");
//        } catch (Error err) {
//            Log.e("shuifeng", "onClickMoreGameBtn: "+ err);
//        }
//
//        Log.d("shuifeng", "onClickMoreGameBtn: 2");
//
////        tools.getInstance().onClickMoreGame();
//    }
//    public void showAds(String _adsType_) {
//        Log.d("shuifeng", "showAds: " + _adsType_);
//        int tempIndex = Integer.parseInt(_adsType_);
//        oppoSDKAgent.getInstance().showAds(tempIndex);
//    }

}


