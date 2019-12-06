package com.xiaoxiao.downloadzip;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Enumeration;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

/**
 * @author: 潇潇
 * @create on:  2019/12/6
 * @describe:解压 zip 文件
 */

public class ZipFileUtil {

    /**
     * 解压缩功能.
     * 将zipFile文件解压到folderPath目录下.
     *
     * @throws Exception
     */
    public static int upZipFile(File zipFile, String folderPath) throws Exception {
        try {
            ZipFile zfile = new ZipFile(zipFile);
            Enumeration zList = zfile.entries();
            ZipEntry ze = null;
            byte[] buf = new byte[1024];
            while (zList.hasMoreElements()) {
                ze = (ZipEntry) zList.nextElement();
                if (ze.isDirectory()) {
                    //Logcat.d("upZipFile", "ze.getName() = " + ze.getName());
                    String dirstr = folderPath + ze.getName();
                    //dirstr.trim();
                    dirstr = new String(dirstr.getBytes("8859_1"), "GB2312");
                    //Logcat.d("upZipFile", "str = " + dirstr);
                    File f = new File(dirstr);
                    f.mkdir();
                    continue;
                }
                //Logcat.d("upZipFile", "ze.getName() = " + ze.getName());
                OutputStream os = new BufferedOutputStream(new FileOutputStream(getRealFileName(folderPath, ze.getName())));
                InputStream is = new BufferedInputStream(zfile.getInputStream(ze));
                int readLen = 0;
                while ((readLen = is.read(buf, 0, 1024)) != -1) {
                    os.write(buf, 0, readLen);
                }
                is.close();
                os.close();
            }
            zfile.close();

        } catch (IOException e) {
            e.printStackTrace();
        }

        return 0;
    }

    /**
     * 给定根目录，返回一个相对路径所对应的实际文件名.
     *
     * @param baseDir     指定根目录
     * @param absFileName 相对路径名，来自于ZipEntry中的name
     * @return java.io.File 实际的文件
     */
    public static File getRealFileName(String baseDir, String absFileName) {
        String[] dirs = absFileName.split("/");
        String lastDir = baseDir;
        if (dirs.length > 1) {
            for (int i = 0; i < dirs.length - 1; i++) {
                lastDir += (dirs[i] + "/");
                File dir = new File(lastDir);
                if (!dir.exists()) {
                    dir.mkdirs();

                }
            }
            File ret = new File(lastDir, dirs[dirs.length - 1]);
            return ret;
        } else {

            return new File(baseDir, absFileName);

        }

    }
}
