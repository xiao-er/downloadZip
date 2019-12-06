package com.xiaoxiao.downloadzip;

import android.app.Activity;
import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.NonNull;

/**
 * @author: 潇潇
 * @create on:  2019/12/6
 * @describe:DOTO
 */

public class LoadingDialog extends Dialog {
    public LoadingDialog(@NonNull Activity activity) {
//        super(activity);
        super(activity, R.style.Dialog_FS);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loading);
    }

}