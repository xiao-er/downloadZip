package com.xiaoxiao.downloadzip;


import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;


/**
 * @author: 潇潇
 * @create on:  2018/10/19
 * @describe:下载热更新需要的文件，存在sd卡
 */

public class DownloadZipService extends Service {

    private String mDownloadUrl;//zip的下载路径
    private String filePath = Constants.BLACKTECH_HOT_UPDATE_FILE_PATH;//文件下载路径
    private String fileName = "YJHtml.zip";//zip文件名
    CallBackListener updateProgressListner;

    public void setUpdateProgressListner(CallBackListener updateProgressListner) {
        this.updateProgressListner = updateProgressListner;
    }

    @Override
    public void onCreate() {
        super.onCreate();
    }


    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (intent == null) {
//            notifyMsg("温馨提醒", "文件下载失败", 0);
            stopSelf();
        }
        mDownloadUrl = intent.getStringExtra("zipurl");//获取下载APK的链接
        downloadFile(mDownloadUrl);//下载APK
        return super.onStartCommand(intent, flags, startId);
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        if (intent == null) {
//            notifyMsg("温馨提醒", "文件下载失败", 0);
            stopSelf();
        }
        mDownloadUrl = intent.getStringExtra("zipurl");//获取下载zip的链接
        downloadFile(mDownloadUrl);//下载APK
        return new Binder();
    }

    public class Binder extends android.os.Binder {
        public DownloadZipService getService() {
            return DownloadZipService.this;
        }
    }


    /**
     * 下载zip文件
     *
     * @param url
     */
    private void downloadFile(final String url) {
        OkHttpClient.Builder builder = new OkHttpClient.Builder().connectTimeout(20, TimeUnit.SECONDS)
                .writeTimeout(5, TimeUnit.SECONDS)
                .readTimeout(5, TimeUnit.SECONDS);
        Request request = new Request.Builder().url(url).build();
        builder.build().newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                InputStream is = null;
                byte[] buf = new byte[4096];
                int len = 0;
                FileOutputStream fos = null;
                // 储存下载文件的目录
                String savePath = Tools.isExistDir_html(filePath);
                try {

                    is = response.body().byteStream();
                    long total = response.body().contentLength();
//                    File file = new File(savePath, getNameFromUrl(url));
                    File file = new File(savePath, getNameFromUrl(fileName));
                    fos = new FileOutputStream(file);
                    long sum = 0;
                    while ((len = is.read(buf)) != -1) {
                        fos.write(buf, 0, len);
                        sum += len;
                        int progress = (int) (sum * 1.0f / total * 100);
                        // 下载中

                    }
                    fos.flush();
                } catch (Exception e) {
                    //
                    e.printStackTrace();
                } finally {
                    try {
                        if (is != null)
                            is.close();
                    } catch (IOException e) {
                    }
                    try {
                        if (fos != null)
                            fos.close();
                    } catch (IOException e) {
                    }
                }
                // 下载完成,解压zip
                //下载完成，先删除热更新的过时数据，并解压最新的热更新数据
                if (updateProgressListner!=null){
                    updateProgressListner.CallBack(200, null);
                }
                deleteOldData();
            }
        });
    }

    /**
     * 首先删除热更新的过时数据，并解压最新的热更新数据
     */
    private void deleteOldData() {
        String dirPath = Constants.BLACKTECH_HOT_UPDATE_FILE_PATH;
        List<File> files = Tools.getFilesInDirectory(dirPath);
        for (File file : files) {
            String fileName = file.getName();
            if (fileName.endsWith(Constants.UXUE_TEMP_FILE_SUFFIX)) {
//                String type = fileName.substring(0, fileName.indexOf("_"));
//                CommandUtils.deleteDirectory(dirPath + fileName);
                Tools.deleteDirectory(dirPath);
                try {
                    ZipFileUtil.upZipFile(file, dirPath);
                    file.delete();
                } catch (Exception e) {
                    e.printStackTrace();
//                    Logcat.e("解压文件失败：" + file.getPath());
                }
            }
        }
    }


    private String getNameFromUrl(String url) {
        return url.substring(url.lastIndexOf("/") + 1);
    }

}
