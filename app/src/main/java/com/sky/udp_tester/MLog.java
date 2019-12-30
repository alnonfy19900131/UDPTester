package com.sky.udp_tester;

import android.util.Log;

public class MLog {
    private static final boolean SHOW_LOG = true;

    public static void i(String tag, String msg){
        if(SHOW_LOG){
            Log.i(tag, msg);
        }
    }

    public static void d(String tag, String msg){
        if (SHOW_LOG){
            Log.d(tag, msg);
        }
    }

}
