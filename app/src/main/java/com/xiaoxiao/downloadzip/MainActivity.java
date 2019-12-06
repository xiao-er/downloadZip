package com.xiaoxiao.downloadzip;

import android.Manifest;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Build;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.os.Message;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.PermissionChecker;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Map;
import java.util.TreeMap;

public class MainActivity extends AppCompatActivity {

    private ServiceConnection sc;
    private LoadingDialog loadingDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        TextView download = findViewById(R.id.download);
        loadingDialog = new LoadingDialog(this);
        download.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadingDialog.show();
                downloadZIP();
            }
        });
    }


    /**
     * 下载热更新需要的文件
     */
    private void downloadZIP() {
        String[] permission = {
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.READ_EXTERNAL_STORAGE

        };
        if (Build.VERSION.SDK_INT >= 23) {
            if (ActivityCompat.checkSelfPermission(MainActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    != PermissionChecker.PERMISSION_GRANTED
                    && ActivityCompat.checkSelfPermission(MainActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE)
                    != PermissionChecker.PERMISSION_GRANTED
                    ) {
                ActivityCompat.requestPermissions(MainActivity.this, permission, 101);
            } else {
                startDownload();
            }
        } else {
            startDownload();
        }

    }


    private void startDownload() {
        sc = new ServiceConnection() {
            @Override
            public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
                DownloadZipService.Binder binder = (DownloadZipService.Binder) iBinder;
                binder.getService().setUpdateProgressListner(new CallBackListener() {
                    @Override

                    public void CallBack(int code, Object object) {
                        Message msg = new Message();
                        msg.what = 1;
                        handler.sendMessage(msg);
                    }
                });
            }

            @Override
            public void onServiceDisconnected(ComponentName componentName) {
            }
        };
        Intent intent = new Intent(MainActivity.this, DownloadZipService.class);
        intent.putExtra("zipurl", "https://github.com/xiao-er/SelectCitys/archive/master.zip");
        if (sc != null) {
            MainActivity.this.bindService(intent, sc, Context.BIND_AUTO_CREATE);
        }
    }


    Handler handler = new Handler(Looper.getMainLooper()) {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            loadingDialog.dismiss();
            Toast.makeText(MainActivity.this, "下载完成", Toast.LENGTH_SHORT).show();
        }
    };
}
