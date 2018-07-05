package com.timer.hyunjae.instafollow.common;

import android.util.Log;

import com.timer.hyunjae.instafollow.config.Config;


public class log
{
    public static void vlog(int type, String msg) {
        if (type == 1) {
            if (Config.RELEASE == false) {
                Log.e(Config.TAG, msg);
            }
        } else if (type == 2) {
            Log.e(Config.TAG, msg);
        }
    }
}
