package com.jyt.baseapp.util;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.media.MediaMetadataRetriever;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.os.Handler;
import android.os.StatFs;
import android.provider.MediaStore;
import android.support.design.widget.TabLayout;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.jyt.baseapp.App;
import com.jyt.baseapp.api.Const;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.reflect.Field;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author LinWei on 2018/5/4 16:09
 */
public class BaseUtil {
    public static Context getContext(){
        return App.getContext();
    }
    public static Handler getHandle(){
        return App.getHandler();
    }
    public static int geMainThreadId(){
        return App.getMainThreadid();
    }



    /**
     * 获取字符串
     * @param id
     * @return
     */
    public static String getString(int id){
        return getContext().getResources().getString(id);
    }

    /**
     * 获取字符串数组

     */
    public static String[] getStringArray(int id){
        return getContext().getResources().getStringArray(id);
    }
    //获取图片
    public static Drawable getDrawable(int id){
        return getContext().getResources().getDrawable(id);
    }

    public static int  getColor(int id){
        return getContext().getResources().getColor(id);
    }

    //根据id获取颜色的状态选择器
    public static ColorStateList getColorStateList(int id){
        return getContext().getResources().getColorStateList(id);
    }

    ////////////////////////加载资源文件//////////////////////////////

    //返回具体的像素值，保存的是dp，返回的是px。
    public static int getDime(int id) {
        return getContext().getResources().getDimensionPixelSize(id);}

    //dp转px
    public static int dip2px(float dip){
        float density=getContext().getResources().getDisplayMetrics().density;//设备密度
        return (int) (dip*density+0.5f);//加0.5f是为了四合五入
    }

    //px转dp
    public static float px2dip(float px){
        float density=getContext().getResources().getDisplayMetrics().density;//设备密度
        return px/density;
    }

    //加载布局文件
    public static View inflate(int id){
        return View.inflate(getContext(),id,null);
    }

    //判断是否在主线程运行
    public static boolean isRunOnUiThread(){
        int nowid=android.os.Process.myTid();
        if (nowid==geMainThreadId()){
            return true;
        }else {
            return false;
        }
    }

    //运行在主线程的方法
    public static void runOnUiThread(Runnable r){
        if (isRunOnUiThread()){
            r.run();//在主线程,直接运行
        }else {
            getHandle().post(r);//在子线程，借助handle，运行在主线程
        }
    }

    public static int currentapiVersion=android.os.Build.VERSION.SDK_INT;//获取api版本好


    /**
     * 返回系统当前时间
     * @return
     */
    public static String currentTime(String type){
        Date date=new Date();
        SimpleDateFormat format=new SimpleDateFormat(type);//"yyyy-MM-dd HH:mm:ss"
        return format.format(date);
    }

    //时间戳转换为规格时间
    public static String TransTime(String time){
        if (TextUtils.isEmpty(time)){
            return "";
        }else {
            SimpleDateFormat format=new SimpleDateFormat("yyyy-MM-dd");//"yyyy-MM-dd HH:mm:ss"
            return format.format(Long.parseLong(time));
        }

    }

    public static Date TransDate(String time){
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        try {
            return format.parse(time);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }


    public static String TransTime(long time){
        SimpleDateFormat format=new SimpleDateFormat("yyyy-MM-dd");//"yyyy-MM-dd HH:mm:ss"
        return format.format(time);

    }

    public static void e(Object obj){
        if (obj instanceof Integer){
            Log.e("@#",""+obj);
        }else {
            Log.e("@#",obj.toString());
        }
    }

    /**
     * 返回当前程序版本名
     */
    public static String getAppVersionName() {
        String versionName = "";
        int versioncode=0;
        try {
            // ---get the package info---
            PackageManager pm = getContext().getPackageManager();
            PackageInfo pi = pm.getPackageInfo( getContext().getPackageName(), 0);
            versionName = pi.versionName;
            versioncode = pi.versionCode;
            if (versionName == null || versionName.length() <= 0) {
                return "";
            }
        } catch (Exception e) {
            Log.e("VersionInfo", "Exception", e);
        }
        return versionName;
    }

    public static String getRealFilePath( final Context context, final Uri uri ) {
        if ( null == uri ) return null;
        final String scheme = uri.getScheme();
        String data = null;
        if ( scheme == null )
            data = uri.getPath();
        else if ( ContentResolver.SCHEME_FILE.equals( scheme ) ) {
            data = uri.getPath();
        } else if ( ContentResolver.SCHEME_CONTENT.equals( scheme ) ) {
            Cursor cursor = context.getContentResolver().query( uri, new String[] { MediaStore.Images.ImageColumns.DATA }, null, null, null );
            if ( null != cursor ) {
                if ( cursor.moveToFirst() ) {
                    int index = cursor.getColumnIndex( MediaStore.Images.ImageColumns.DATA );
                    if ( index > -1 ) {
                        data = cursor.getString( index );
                    }
                }
                cursor.close();
            }
        }
        return data;
    }


    /*SharedPreferences保存*/
    private static SharedPreferences sp = getContext().getSharedPreferences("config",Context.MODE_PRIVATE);
    private static SharedPreferences.Editor editor=sp.edit();
    public static void  setSpNumInt(String key,int value)
    {
        editor.putInt(key,value).commit();
    }
    public static void  setSpString(String key,String value) {
        editor.putString(key, value).commit();
    }
    public static void  setSpBoolean(String key,boolean value){
        editor.putBoolean(key, value).commit();
    }

    public static int getSpInt(String key){
        return sp.getInt(key,-1);
    }
    public static String getSpString(String key){
        return sp.getString(key, null);
    }
    public static boolean getSpBoolean(String key){
        return sp.getBoolean(key, false);
    }
    /*--------------------------------------------------------------------------------------------*/

    /**
     * 吐司
     * @param object
     */
    public static void makeText(final Object object){
        new Handler(getContext().getMainLooper()).post(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(getContext(),object.toString(),Toast.LENGTH_SHORT).show();
            }
        });
    }


    /**
     * 判断手机号码的格式是否符合标准
     * @param value
     * @return
     */
    public static boolean checkCellphone(String value) {
        String regExp = "^[1]([3][0-9]{1}|59|58|88|89)[0-9]{8}$";
        Pattern p = Pattern.compile(regExp);
        Matcher m = p.matcher(value);
        return m.find();//boolean
    }

    /**
     * 获得机身可用内存
     *
     * @return
     */
    public static long getRomAvailableSizeNum() {
        File path = Environment.getDataDirectory();
        StatFs stat = new StatFs(path.getPath());
        long blockSize = stat.getBlockSize();
        long availableBlocks = stat.getAvailableBlocks();
        return availableBlocks*blockSize;
    }

    /**
     * 判断是否有SD卡
     * @return
     */
    public static boolean hasSdcard(){
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)){
            return true;
        }else {
            return false;
        }
    }

    /**
     * 返回屏幕宽度
     * @return
     */
    public static int getScannerWidth(){
        Resources resources = getContext().getResources();
        DisplayMetrics dm = resources.getDisplayMetrics();
        return dm.widthPixels;
    }

    /**
     * 返回屏幕高度
     * @return
     */
    public static int getScannerHeight(){
        Resources resources = getContext().getResources();
        DisplayMetrics dm = resources.getDisplayMetrics();
        float density = dm.density;

        return dm.heightPixels;
    }

    /**
     * 返回屏幕密度
     * @return
     */
    public static float getScannerDensity(){
        Resources resources = getContext().getResources();
        DisplayMetrics dm = resources.getDisplayMetrics();
        return dm.density;
    }

    /**
     * 修改导航条大小
     * @param context
     * @param tabs
     * @param leftDip 左边距
     * @param rightDip 右边距
     */
    public static void setIndicator(Context context, TabLayout tabs, int leftDip, int rightDip) {
        Class<?> tabLayout = tabs.getClass();
        Field tabStrip = null;
        try {
            tabStrip = tabLayout.getDeclaredField("mTabStrip");
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        }

        tabStrip.setAccessible(true);
        LinearLayout ll_tab = null;
        try {
            ll_tab = (LinearLayout) tabStrip.get(tabs);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }

        int left = (int) (getDisplayMetrics(context).density * leftDip);
        int right = (int) (getDisplayMetrics(context).density * rightDip);

        for (int i = 0; i < ll_tab.getChildCount(); i++) {
            View child = ll_tab.getChildAt(i);
            child.setPadding(0, 0, 0, 0);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.MATCH_PARENT, 1);
            params.leftMargin = left;
            params.rightMargin = right;
            child.setLayoutParams(params);
            child.invalidate();
        }
    }
    public static DisplayMetrics getDisplayMetrics(Context context) {
        DisplayMetrics metric = new DisplayMetrics();
        ((Activity) context).getWindowManager().getDefaultDisplay().getMetrics(metric);
        return metric;
    }

    /**
     * 获取网络视频第一帧的图片
     * @param url
     * @param width
     * @param height
     * @return
     */
    public static Bitmap createVideoThumbnail(String url, int width, int height) {
        Bitmap bitmap = null;
        MediaMetadataRetriever retriever = new MediaMetadataRetriever();
        int kind = MediaStore.Video.Thumbnails.MINI_KIND;
        try {
            if (Build.VERSION.SDK_INT >= 14) {
                retriever.setDataSource(url, new HashMap<String, String>());
            } else {
                retriever.setDataSource(url);
            }
            bitmap = retriever.getFrameAtTime();
        } catch (IllegalArgumentException ex) {
            // Assume this is a corrupt video file
        } catch (RuntimeException ex) {
            // Assume this is a corrupt video file.
        } finally {
            try {
                retriever.release();
            } catch (RuntimeException ex) {
                // Ignore failures while cleaning up.
            }
        }
        if (kind == MediaStore.Images.Thumbnails.MICRO_KIND && bitmap != null) {
            bitmap = ThumbnailUtils.extractThumbnail(bitmap, width, height, ThumbnailUtils.OPTIONS_RECYCLE_INPUT);
        }
        return bitmap;
    }

    public static File saveBitmap(Context context, Bitmap mBitmap) {
        String IN_PATH = "/dskqxt/pic/";
        String savePath;
        File filePic;
        if (Environment.getExternalStorageState().equals(
                Environment.MEDIA_MOUNTED)) {
            savePath = Const.mMainFile;
        } else {
            savePath = context.getApplicationContext().getFilesDir()
                    .getAbsolutePath()
                    + IN_PATH;
        }
        try {
            filePic = new File(savePath + "videoTest" + ".jpg");
            if (!filePic.exists()) {
                filePic.getParentFile().mkdirs();
                filePic.createNewFile();
            }
            FileOutputStream fos = new FileOutputStream(filePic);
            mBitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos);
            fos.flush();
            fos.close();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return null;
        }

        return filePic;
    }




}
