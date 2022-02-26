package com.exzork.simpedarku.utils;

import android.content.res.Resources;

public class Utils {
    public static int getDP(int px){
        return (int) (px * (Resources.getSystem().getDisplayMetrics().density));
    }
}
