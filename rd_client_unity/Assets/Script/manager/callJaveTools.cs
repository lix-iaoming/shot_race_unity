using System.Collections;
using System.Collections.Generic;
using UnityEngine;

public class callJaveTools
{
    private const string JAVA_CLASS_NAME = "com.unity3d.player.UnityPlayer";
    private static callJaveTools _mInstance = null;

    public static callJaveTools getInstance()
    {
        if (_mInstance == null)
        {
            _mInstance = new callJaveTools();

        }
        return _mInstance;
    }

    private void CallJavaFunc(string javaFuncName, params object[] args)
    {
        try
        {
            //获取到AndroidJavaClass，至于这里为什么调用这个类，我也不是很清楚
            using (AndroidJavaClass jc = new AndroidJavaClass(JAVA_CLASS_NAME))
            {
                //获取到Activity
                using (AndroidJavaObject jo = jc.GetStatic<AndroidJavaObject>("currentActivity"))
                {
                    Debug.Log("shuifeng: AndroidJavaObject  0 ");
                    //调用Java方法
                    jo.Call(javaFuncName, args);
                    Debug.Log("shuifeng: AndroidJavaObject  1 ");

                }
                Debug.Log("shuifeng: AndroidJavaObject  2 ");
            }
        }
        catch (System.Exception ex)
        {
            Debug.Log("shuifeng: AndroidJavaObject  3 ");
            Debug.Log("shuifeng callSdk error:" + ex.Message);
        }
    }

  
    public void onCallJavaMenthod(string javaFuncName, params object[] args)
    {

        Debug.Log("shuifeng: javaFuncName "+  javaFuncName + args);
        CallJavaFunc(javaFuncName, args);
    }
}
