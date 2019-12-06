package com.xiaoxiao.downloadzip;

import android.os.Environment;

/**
 * @author: 潇潇
 * @create on:  2019/12/6
 * @describe:DOTO
 */

public class Constants {
    public static final String MAIN_FILE_PATH = Environment.getExternalStorageDirectory() + "/com.xiaoxiao.downloadZip/";
    //zip下载后解压文件存放目录
    public static final String BLACKTECH_HOT_UPDATE_FILE_PATH = MAIN_FILE_PATH + "hot_update_file/";
    // 下载的缓存文件后缀名
    public static final String UXUE_TEMP_FILE_SUFFIX = ".zip";

}
