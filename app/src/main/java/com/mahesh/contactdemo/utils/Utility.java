package com.mahesh.contactdemo.utils;

import android.content.Context;
import android.graphics.Typeface;
import android.text.Html;
import android.util.Log;
import android.widget.Button;

import com.mahesh.contactdemo.BuildConfig;
import com.mahesh.contactdemo.R;

import static android.view.View.GONE;

public class Utility {
    public static final boolean isDebugAPK= BuildConfig.DEBUG&&BuildConfig.BUILD_TYPE.trim().equalsIgnoreCase("debug");


    public static void showLog(String message){
        if(isDebugAPK)
            Log.w("showLog", message);
    }

    public static boolean chkNull(String str){
        return str!=null&&str.trim().length()>0&&!str.trim().equalsIgnoreCase("null");
    }

    public static String chkNull(String str, String def){
        return str!=null&&str.trim().length()>0&&!str.trim().equalsIgnoreCase("null")?str:def;
    }
    public static Long chknull(Long ll, Long def){
        return ll!=null?ll:def;
    }

    public static Double chknull(Double dd, Double def){
        return dd!=null?dd:def;
    }

    public static Float chknull(Float ff, Float def){
        return ff!=null?ff:def;
    }

    public static Integer chknull(Integer ii, Integer def){
        return ii!=null?ii:def;
    }

    public static Boolean chknull(Boolean bb, Boolean def){
        return bb!=null?bb:def;
    }

    public static String chknull(String str, String def){
        return (str!=null&&str.trim().length()>0&&!str.equalsIgnoreCase("null"))?str:def;
    }

}
