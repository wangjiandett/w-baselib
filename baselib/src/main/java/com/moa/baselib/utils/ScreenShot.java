/*
 *  Copyright (C) 2016-2017 浙江海宁山顶动力网络科技有限公司
 * 文件说明：
 *  <p>
 *  更新说明:
 *
 *  @author wangjian@xiaokebang.com
 *  @version 1.0.0
 *  @create 17-7-27 上午11:00
 *  @see <a href="http://www.top4s.net/">http://www.top4s.net/</a>
 */

package com.moa.baselib.utils;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Point;
import android.graphics.Rect;
import android.view.View;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * 截屏操作类
 * <p>
 * Created by：wangjian on 2017/7/27 11:00
 */
public class ScreenShot {

    /**
     * 截屏 去除状态栏，如果超过一屏幕可能无法截全，可使用{@link #takeViewShot}
     *
     * @param activity 当前界面
     * @return 截屏图片
     */
    public static Bitmap takeScreenShot(Activity activity) {
        // View是你需要截图的View
        View view = activity.getWindow().getDecorView();
        view.setDrawingCacheEnabled(true);
        view.buildDrawingCache();
        Bitmap b1 = view.getDrawingCache();

        // 获取状态栏高度
        Rect frame = new Rect();
        activity.getWindow().getDecorView().getWindowVisibleDisplayFrame(frame);
        int statusBarHeight = frame.top;

        Point point = new Point();
        activity.getWindowManager().getDefaultDisplay().getSize(point);
        // 获取屏幕长和高
        int width = point.x;
        int height = point.y;
        // 去掉标题栏
        Bitmap b = Bitmap.createBitmap(b1, 0, statusBarHeight, width, height - statusBarHeight);
        view.destroyDrawingCache();
        return b;
    }

    /**
     * view截图，可截取长图
     *
     * @param view 需要截图的view
     * @return 截图
     */
    public static Bitmap takeViewShot(View view) {
        Bitmap bitmap = Bitmap.createBitmap(view.getWidth(), view.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        canvas.translate(-view.getScrollX(), -view.getScrollY());
        view.draw(canvas);
        return bitmap;
    }

    private static void savePic(Bitmap b, File filePath) {
        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(filePath);
            b.compress(Bitmap.CompressFormat.PNG, 100, fos);
            fos.flush();
            fos.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 截屏保存到文件中，涉及到文件操作建议放到子线程中操作
     *
     * @param activity 当前界面
     * @param filePath 文件路径
     */
    public static void saveActivity2File(Activity activity, File filePath) {
        if (filePath == null) {
            return;
        }
        if (!filePath.getParentFile().exists()) {
            filePath.getParentFile().mkdirs();
        }
        savePic(ScreenShot.takeScreenShot(activity), filePath);
    }

    /**
     * 保存截图view到文件中，默认保存在sdcard中，涉及到文件操作建议放到子线程中操作
     *
     * @param view 要截图的view
     * @return 保存的文件
     */
    public static File saveView2File(View view){
        String path = Files.getExternalStorageFileDir();
        File pic = new File(path, "screen_shot_"+System.currentTimeMillis()+".png");
        savePic(takeViewShot(view), pic);
        return pic;
    }

    /**
     * 保存view截图到文件中，并保存到图库中，涉及到文件操作建议放到子线程中操作
     *
     * @param view 需要截图的view
     */
    public static File saveShot2FileAndGallery(View view){
        File file = saveView2File(view);
        FileUtil.saveImageToGallery(view.getContext(), file);
        return file;
    }



}
