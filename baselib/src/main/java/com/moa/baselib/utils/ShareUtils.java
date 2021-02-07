package com.moa.baselib.utils;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;

import com.moa.baselib.R;

import java.io.File;
import java.util.ArrayList;

/**
 * 调用系统分享
 *
 * @author wangjian
 * Created on 2020/9/3 17:26
 */
public class ShareUtils {


    public static final String QQ_PACKAGE_NAME = "com.tencent.mobileqq";
    public static final String WX_PACKAGE_NAME = "com.tencent.mm";

    /**
     * 分享文本到qq
     *
     * @param shareText 分享内容
     */
    public static void shareText2QQ(Context mContext, String shareText) {
        Intent intent = shareTextIntent(mContext, shareText);
        intent.setPackage(QQ_PACKAGE_NAME);
        mContext.startActivity(intent);
    }

    /**
     * 分享文件到qq
     *
     * @param file 文件
     */
    public static void shareFile2QQ(Context mContext, File file) {
        Intent intent = shareFileIntent(mContext, file);
        intent.setPackage(QQ_PACKAGE_NAME);
        mContext.startActivity(intent);
    }

    /**
     * 分享文本到微信
     *
     * @param shareText 分享内容
     */
    public static void shareText2WX(Context mContext, String shareText) {
        Intent intent = shareTextIntent(mContext, shareText);
        intent.setPackage(WX_PACKAGE_NAME);
        mContext.startActivity(intent);
    }

    /**
     * 分享文件到微信
     *
     * @param file 文件
     */
    public static void shareFile2WX(Context mContext, File file) {
        Intent intent = shareFileIntent(mContext, file);
        intent.setPackage(WX_PACKAGE_NAME);
        mContext.startActivity(intent);
    }

    /**
     * 调用系统分享文本
     *
     * @param shareText 文本
     */
    public static void shareText(Context mContext, String shareText) {
        Intent intent = shareTextIntent(mContext, shareText);
        shareBySystemChoose(mContext, intent);
    }

    /**
     * 调用系统分享文件
     *
     * @param file 文件
     */
    public static void shareFile(Context mContext, File file) {
        Intent intent = shareFileIntent(mContext, file);
        shareBySystemChoose(mContext, intent);
    }

    /**
     * 调用系统分享多文件
     *
     * @param files 文件列表
     */
    public static void shareFiles(Context mContext, ArrayList<java.io.File> files) {
        Intent intent = shareFilesIntent(mContext, files);
        shareBySystemChoose(mContext, intent);
    }

    /**
     * 文本分享intent
     *
     * @param shareText 分享内容
     * @return intent
     */
    private static Intent shareTextIntent(Context context, String shareText) {
        Intent textIntent = new Intent(Intent.ACTION_SEND);
        textIntent.setType("text/plain");
        textIntent.putExtra(Intent.EXTRA_TEXT, shareText);
        return textIntent;
    }

    /**
     * 调用系统分享文件
     */
    private static Intent shareFileIntent(Context mContext, File file) {
        Intent sendIntent = new Intent();
        sendIntent.setAction(Intent.ACTION_SEND);
        sendIntent.putExtra(Intent.EXTRA_STREAM, FileProvider7.getUriForFile(mContext, file));
        sendIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        sendIntent.setType(FileUtil.getMimeTypeFromFile(file));
        return sendIntent;
    }

    /**
     * 分享多文件 intent，目前微信和qq不支持多文件分享
     *
     * @param files 文件列表
     * @return intent
     */
    private static Intent shareFilesIntent(Context mContext, ArrayList<java.io.File> files) {
        ArrayList<Uri> uris = new ArrayList<>();
        for (File file : files) {
            uris.add(FileProvider7.getUriForFile(mContext, file));
        }

        Intent sendIntent = new Intent();
        sendIntent.setAction(Intent.ACTION_SEND_MULTIPLE);
        sendIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        sendIntent.setType(FileUtil.getMimeTypeFromFile(files.get(0)));
        sendIntent.putParcelableArrayListExtra(Intent.EXTRA_STREAM, uris);
        return sendIntent;
    }

    private static void shareBySystemChoose(Context mContext, Intent sendIntent) {
        try {
            Intent chooserIntent = Intent.createChooser(sendIntent, mContext.getString(R.string.tt_share));
            if (chooserIntent == null) {
                return;
            }
            mContext.startActivity(sendIntent);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
