package cn.net.xinyi.xmjt.utils;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Environment;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Enumeration;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

import cn.net.xinyi.xmjt.R;

public class BaseUtil {

    public static final String TAG = "BaseUtil";


    /**
     * 网络检测
     *
     * @param context
     * @return
     */
    public static boolean isConnect(Context context) {
        ConnectivityManager connectivity = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivity != null) {
            NetworkInfo info = connectivity.getActiveNetworkInfo();
            if (info != null && info.isConnected()) {
                if (info.getState() == NetworkInfo.State.CONNECTED) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * sd卡是否插入
     *
     * @return
     */
    public static boolean sdCardExit() {
        return Environment.getExternalStorageState().equals(
                Environment.MEDIA_MOUNTED);
    }

    /**
     * 获取版本号
     *
     * @param context
     * @param packageurl
     * @return
     */
    public static int getVerCode(Context context, String packageurl) {
        int verCode = -1;
        try {
            verCode = context.getPackageManager().getPackageInfo(packageurl, 0).versionCode;
        } catch (NameNotFoundException e) {
        }
        return verCode;
    }

    /**
     * 获取名称
     *
     * @param context
     * @param packageurl
     * @return
     */
    public static String getVerName(Context context, String packageurl) {
        String verName ="";
        try {
            verName = context.getPackageManager().getPackageInfo(packageurl, 0).versionName;
        } catch (NameNotFoundException e) {
        }
        return verName;
    }




    /**
     * 显示对话框
     *
     * @param msg
     * @param context
     */
    public static void showDialog(String msg, Context context) {
        AlertDialog alert = null;
        if (null!=context){
            AlertDialog.Builder builder = new AlertDialog.Builder(context);
            builder.setMessage(msg)
                    .setCancelable(false)
                    .setPositiveButton(R.string.sure,
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                }
                            }).setCancelable(false);
             alert = builder.create();
             alert.show();
        }
    }

    /**
     * 从资源文件中读取文本内容
     *
     * @param context
     * @param resourceID
     * @return
     */
    public static String readTextFromResource(Context context, int resourceID) {
        InputStream is = context.getResources().openRawResource(resourceID);
        StringBuffer out = new StringBuffer();
        byte[] b = new byte[4096];
        try {
            for (int n; (n = is.read(b)) != -1; ) {
                out.append(new String(b, 0, n));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return out.toString();
    }

    /**
     * 加载本地图片
     *
     * @param url 本地图片文件物理地址
     * @return Drawable
     */
    @SuppressWarnings("deprecation")
    public static Drawable getLocalBitmap(String url) {
        try {
            FileInputStream fis = new FileInputStream(url);
            return new BitmapDrawable(BitmapFactory.decodeStream(fis));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Java文件操作 获取不带扩展名的文件名
     * <p/>
     * Created on: 2011-8-2 Author: nodeny
     */
    public static String getFileNameNoEx(String filename) {
        if ((filename != null) && (filename.length() > 0)) {
            int dot = filename.lastIndexOf('.');
            if ((dot > -1) && (dot < (filename.length()))) {
                return filename.substring(0, dot);
            }
        }
        return filename;
    }

    /**
     * Java文件操作 根据文件全路径获取文件名
     * <p/>
     * Created on: 2011-8-2 Author: nodeny
     */
    public static String getFileName(String filePath) {

        if ((filePath != null) && (filePath.length() > 0)) {
            int dot = filePath.lastIndexOf('/');
            if ((dot > -1) && (dot < (filePath.length() - 1))) {
                return filePath.substring(dot + 1);
            }
        }
        return filePath;
    }

    public static String getFileDir(String filePath) {
        if ((filePath != null) && (filePath.length() > 0)) {
            int dot = filePath.lastIndexOf('/');
            if ((dot > -1) && (dot < (filePath.length() - 1))) {
                return filePath.substring(0, dot + 1);
            }
        }
        return filePath;
    }

    public static String getFileEx(String fileName) {
        if ((fileName != null) && (fileName.length() > 0)) {
            int dot = fileName.lastIndexOf('.');
            if ((dot > -1) && (dot < (fileName.length() - 1))) {
                return fileName.substring(dot + 1);
            }
        }
        return fileName;
    }

    /**
     * 文件重命名
     * @param oldname 原来的文件名
     * @param newname 新文件名
     */
    public static boolean renameFile(String oldname, String newname) {
        if (!oldname.equals(newname)) {//新的文件名和以前文件名不同时,才有必要进行重命名
            File oldfile = new File(oldname);
            File newfile = new File(newname);
            if (!oldfile.exists()) {
                return false;//重命名文件不存在
            }
            if (newfile.exists()) {//若在该目录下已经有一个文件和新文件名相同，则不允许重命名
                System.out.println(newname + "已经存在！");
                return false;
            } else {
                oldfile.renameTo(newfile);
                return true;
            }
        } else {
            System.out.println("新文件名和旧文件名相同...");
            return false;
        }
    }

    /**
     * 从assets文件夹复制文件
     *
     * @param context
     * @param fileName
     * @param path
     * @return
     */
    public static boolean copyFileFromAssets(Context context, String fileName,
                                             String path) {
        boolean copyIsFinish = false;
        try {
            InputStream is = context.getAssets().open(fileName);
            File file = new File(path);
            file.createNewFile();
            FileOutputStream fos = new FileOutputStream(file);
            byte[] temp = new byte[1024];
            int i = 0;
            while ((i = is.read(temp)) > 0) {
                fos.write(temp, 0, i);
            }
            fos.close();
            is.close();
            copyIsFinish = true;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return copyIsFinish;
    }


    /**
     * 检查指定的应用程序是否安装
     *
     * @param context
     * @param packageName
     * @return
     */
    public static boolean isApkAvilible(Context context, String packageName) {
        final PackageManager packageManager = context.getPackageManager();
        // 获取所有已安装程序的包信息
        List<PackageInfo> pinfo = packageManager.getInstalledPackages(0);
        for (int i = 0; i < pinfo.size(); i++) {
            if (pinfo.get(i).packageName.equalsIgnoreCase(packageName))
                return true;
        }
        return false;
    }

    /**
     * 安装APK
     */
    public static void installApk(Context context, String apkName) {
        String[] files = null;
        try {// 遍历assest文件夹，读取压缩包及安装包
            files = context.getAssets().list("");
        } catch (IOException e) {
            e.printStackTrace();
        }
        for (int i = 0; i < files.length; i++) {
            String assetfileName = files[i];
            if (assetfileName.contains(apkName)) {// 判断是否为Apk
                String mApkName = assetfileName;
                String s = FileUtils.getDocFileDirPath() + "/";
                // 不能直接读取assets目录下的Apk信息，所以需要先将其拷贝出来
                final String apkcachePath = (new StringBuilder(s)).append(
                        apkName).toString();// 临时文件
                if (copyFileFromAssets(context, mApkName, apkcachePath)) {
                    File file = new File(apkcachePath);
                    Intent intent = FileUtils.getApkFileIntent(file);
                    context.startActivity(intent);
                    return;
                }
            }
        }
    }

    /**
     * 解压缩一个文件
     * @param zipFile 要解压的压缩文件
     * @param folderPath 解压缩的目标目录
     * @throws IOException 当解压缩过程出错时抛出
     */
    public static void upZipFile(File zipFile, String folderPath) throws IOException
    {
        File desDir = new File(folderPath);
        if(!desDir.exists())
        {
            desDir.mkdirs();
        }

        ZipFile zf = new ZipFile(zipFile);
        for (Enumeration<?> entries = zf.entries(); entries.hasMoreElements();)
        {
            ZipEntry entry = ((ZipEntry)entries.nextElement());
            InputStream in = zf.getInputStream(entry);
            String str = folderPath + File.separator + entry.getName();
            str = new String(str.getBytes("8859_1"), "GB2312");
            File desFile = new File(str);
            if (!desFile.exists())
            {
                File fileParentDir = desFile.getParentFile();
                if (!fileParentDir.exists())
                {
                    fileParentDir.mkdirs();
                }
                desFile.createNewFile();
            }
            OutputStream out = new FileOutputStream(desFile);
            byte buffer[] = new byte[1024];
            int realLength;
            while ((realLength = in.read(buffer)) > 0)
            {
                out.write(buffer, 0, realLength);
            }
            in.close();
            out.close();
        }
    }


    public static void showDialog(final Activity aty, final Class<?> intent1,final Class<?> intent2) {
        new AlertDialog.Builder(aty).setTitle("提示")
                .setMessage("采集数据成功!")
                .setNegativeButton ("查看详情",
                        new DialogInterface.OnClickListener() {

                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Intent intent = new Intent(aty,intent1);
                                aty.startActivity(intent);
                                aty.finish();
                            }
                        })
                .setPositiveButton("继续采集",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Intent intent = new Intent(aty,intent2);
                                aty.startActivity(intent);
                                aty.finish();
                            }
                        }).setCancelable(false).show();
    }

}
