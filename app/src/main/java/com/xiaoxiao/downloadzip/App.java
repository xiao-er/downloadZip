package com.xiaoxiao.downloadzip;

import android.app.Application;
import android.content.Context;

/**
 * @author: 潇潇
 * @create on:  2019/12/6
 * @describe:DOTO
 */

public class App extends Application {

    private static Context context;
    @Override
    public void onCreate() {
        super.onCreate();
        context = this.getApplicationContext();
    }



    public static Context getContext() {
        return context;
    }

}
