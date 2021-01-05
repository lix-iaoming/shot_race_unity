package com.csj.klccc;

import android.app.Activity;
import android.util.Log;


public class tools {
    private static tools _mInatance = null;
    private Activity _mActivity = null;
    public static tools getInstance(){
        if(_mInatance == null){
            _mInatance = new tools();
        }
        return _mInatance;
    }
    public void initialized(Activity mActivity){
        Log.i("shuifeng", "initialized: ");
        _mActivity = mActivity;
    }


    public void onClickMoreGame() {
        oppoSDKAgent.getInstance().moreGame();
    }
}
