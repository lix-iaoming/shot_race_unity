using System.Collections;
using System.Collections.Generic;
using UnityEngine;
using UnityEngine.SceneManagement;
using System;
using UnityEngine.Advertisements;
using GoogleMobileAds.Api;

public class AdManager : MonoBehaviour
{

    public enum ChannelName
    {
        Fridaybox,
        TapGamesFever,
        Petmaster
    }

    public bool forreward;

    public bool[] adsReady = new bool[5];

    private static AdManager _instance = null;

    public static AdManager getInstance() {
        if (_instance == null) {
            _instance = new AdManager();
        }
        return _instance;
    }


    void Awake()
    {

        for (int i = 0; i < 5; ++i) {

            adsReady[i] = false;
        }

    }



    // Use this for initialization
    void Start()
    {

    }

    void onInterstitialEvent(object sender, System.EventArgs args)
    {

    }
    void onInterstitialCloseEvent(object sender, System.EventArgs args)
    {
    }
    void onBannerEvent(string eventName, string msg)
    {

    }
    void onadclosed(object sender, System.EventArgs args)
    {

    }
    void onRewardedVideoEvent(object sender, Reward args)
    {

    }
    public void showBannerAd()
    {
  
    }
    public void loadInterstitial()
    {

    }
    public void showInterstitial()
    {
      
    }
    public void loadRewardVideo()
    {
       
    }
    public void showRewardVideo()
    {

       
    }
    public void hideBannerAd()
    {
    }
}