package com.moa.baselib.utils;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.text.format.Formatter;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.FileNameMap;
import java.net.URLConnection;
import java.util.HashMap;
import java.util.Locale;

/**
 * 文件相关操作
 *
 * @author wangjian
 * @date 2018/12/17
 */
public class FileUtil {

    /**
     * 读取文件到字符串
     *
     * @param path 文件路径
     * @return 文件内容
     */
    public static String readFile(String path) {
        byte[] databytes = readFileBytes(path);
        if (databytes != null) {
            return new String(databytes);
        }
        return null;
    }

    /**
     * 读取文件到数组中
     *
     * @param path 文件路径
     * @return 文件内容
     */
    public static byte[] readFileBytes(String path) {
        InputStream is = getFileInputStream(path);
        if (is != null) {
            BufferedInputStream bis = new BufferedInputStream(is);
            ByteArrayOutputStream buffer = new ByteArrayOutputStream();
            //We create an array of bytes
            byte[] data = new byte[4096];
            int current = 0;
            try {
                while ((current = bis.read(data, 0, data.length)) != -1) {
                    buffer.write(data, 0, current);
                }
                return buffer.toByteArray();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    /**
     * 读取文件到流中
     *
     * @param path 文件路径
     * @return 文件流
     */
    public static InputStream getFileInputStream(String path) {
        InputStream inputstream = null;
        if (!TextUtils.isEmpty(path)) {
            return null;
        }

        try {
            File pfile = new File(path);
            if (pfile.exists() && (!pfile.isDirectory())) {
                inputstream = new FileInputStream(pfile);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return inputstream;
    }

    /**
     * 从res文件夹中的raw 文件夹中获取文件并读取数据
     *
     * @param context context
     * @param rawId   资源id
     * @return 字符串
     */
    public static String readStringFromRaw(Context context, int rawId) {
        String result = "";
        try {
            InputStream in = context.getResources().openRawResource(rawId);
            //获取文件的字节数
            int lenght = in.available();
            //创建byte数组
            byte[] buffer = new byte[lenght];
            //将文件中的数据读到byte数组中
            in.read(buffer);
            result = new String(buffer, "UTF-8");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    /**
     * 从assets 文件夹中获取文件并读取数据
     *
     * @param context
     * @param fileName
     * @return
     */
    public static String readStringAssets(Context context, String fileName) {
        String result = "";
        try {
            InputStream in = context.getResources().getAssets().open(fileName);
            //获取文件的字节数
            int length = in.available();
            //创建byte数组
            byte[] buffer = new byte[length];
            //将文件中的数据读到byte数组中
            in.read(buffer);
            result = new String(buffer, "UTF-8");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }


    /**
     * 由于assets中的文件不能直接使用，需要将其复制出来在使用
     *
     * @param context  context
     * @param filename 文件名
     * @param desFile  copy到的文件
     */
    public static void copyAssetsFile(Context context, String filename, File desFile) {
        try {
            InputStream in = context.getResources().getAssets().open(filename);
            writeBytesToFile(in, desFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 从SD卡文件夹中获取文件并读取数据
     *
     * @param filePath 文件路径
     */
    public static String readStringFromFile(String filePath) throws IOException {
        File f = null;
        f = new File(filePath);//这是对应文件路径全名
        StringBuilder fileData = new StringBuilder();
        InputStream in = null;
        try {
            in = new BufferedInputStream(new FileInputStream(f));
        } catch (FileNotFoundException e3) {
            e3.printStackTrace();
        }

        BufferedReader reader = new BufferedReader(new InputStreamReader(in, "UTF-8"));
        char[] buf = new char[4096];
        int numRead = 0;
        while ((numRead = reader.read(buf)) != -1) {
            String readData = String.valueOf(buf, 0, numRead);
            fileData.append(readData);
        }
        reader.close();
        return fileData.toString();

    }

    public static void writeBytesToFile(InputStream is, File file) {
        FileOutputStream fos = null;
        try {
            byte[] data = new byte[4096];
            int nbread = 0;
            fos = new FileOutputStream(file);
            while ((nbread = is.read(data)) > -1) {
                fos.write(data, 0, nbread);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            CloseUtils.closeIO(fos);
        }
    }

    /**
     * 遍历删除文件夹中的所有文件
     *
     * @param logFiles
     */
    public static void deleteFiles(File[] logFiles) {
        if (logFiles != null) {
            for (File file : logFiles) {
                if (file != null && file.exists()) {
                    file.delete();
                }
            }
        }
    }

    /**
     * 递归删除
     */
    public static void recursionDeleteFile(File file) {
        if (file.isFile()) {
            file.delete();
            return;
        }
        if (file.isDirectory()) {
            File[] childFile = file.listFiles();
            if (childFile == null || childFile.length == 0) {
                file.delete();
                return;
            }
            for (File f : childFile) {
                recursionDeleteFile(f);
            }
            file.delete();
        }
    }

    /**
     * 创建文件
     *
     * @param path 文件名 以“/”开头表示绝对路径
     * @return 文件File
     */
    public static File createFile(String path, String name) {
        File file = new File(path, name);
        try {
            if (!file.exists()) {
                File parent = file.getParentFile();
                if (!parent.exists()) {
                    parent.mkdirs();
                }
                file.createNewFile();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return file;
    }

    /**
     * 创建文件夹
     *
     * @param path    父类路径
     * @param dirName 文件夹名
     * @return 创建后的文件夹
     */
    public static File createDir(String path, String dirName) {
        File file = new File(path, dirName);
        try {
            if (!file.exists()) {
                file.mkdirs();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return file;
    }

    /**
     * copy文件
     *
     * @param src 输入文件源
     * @param dst 输出文件
     * @throws IOException
     */
    public static void copy(File src, File dst) throws IOException {
        InputStream in = new FileInputStream(src);
        OutputStream out = new FileOutputStream(dst);

        byte[] buf = new byte[4096];
        int len;
        while ((len = in.read(buf)) > 0) {
            out.write(buf, 0, len);
        }
        in.close();
        out.close();
    }

    /**
     * 文件重命名
     *
     * @param oldFile 原文件
     * @param newName 新文件名
     * @return 新文件
     */
    public static File renameFile(File oldFile, String newName) {
        if(oldFile != null){
            // String oldName = oldFile.getName();
            String path = oldFile.getParent();
            File newFile = new File(path, newName);
            if(newFile.exists()){
                newFile.delete();
            }
            oldFile.renameTo(newFile);
            return newFile;
        }
        return null;
    }

    /**
     * 图片保存到文件中
     *
     * @param mBitmap  图片
     * @param filepath 路径
     * @param filename 文件名
     * @return 是否保存成功
     */
    public static boolean saveBitmapToFile(Bitmap mBitmap, String filepath, String filename) {
        if (null == mBitmap || TextUtils.isEmpty(filepath)) {
            return false;
        }

        boolean result = true;
        File file = createFile(filepath, filename);
        try {
            FileOutputStream fos = new FileOutputStream(file);
            result = mBitmap.compress(Bitmap.CompressFormat.PNG, 100, fos);
            fos.flush();
            fos.close();
        } catch (IOException e) {
            result = false;
        }
        return result;
    }

    /**
     * 递归删除文件或者文件夹，
     *
     * @param file
     * @return
     */
    public static boolean deleteFileRecursively(File file) {
        if (null == file || !file.exists()) {
            return true;
        }

        boolean result = true;
        if (file.isFile()) {
            result &= file.delete();
        }
        if (file.isDirectory()) {
            File[] childFile = file.listFiles();
            // 删除空文件夹
            if (childFile == null || childFile.length == 0) {
                result &= file.delete();
            } else {
                // 删除子文件夹
                for (File f : childFile) {
                    result &= deleteFileRecursively(f);
                }
                result &= file.delete();
            }
        }
        return result;
    }


    public static boolean writeInputStream2File(InputStream inputStream, String path) {
        boolean result = true;
        BufferedInputStream bufferedInputStream = null;
        FileOutputStream fileOutputStream = null;
        BufferedOutputStream bufferedOutputStream = null;

        try {
            bufferedInputStream = new BufferedInputStream(inputStream);
            fileOutputStream = new FileOutputStream(new File(path));
            bufferedOutputStream = new BufferedOutputStream(fileOutputStream);

            byte[] buffer = new byte[4096 * 2];
            int bytesRead = 0;
            while ((bytesRead = bufferedInputStream.read(buffer, 0, buffer.length)) != -1) {
                bufferedOutputStream.write(buffer, 0, bytesRead);
            }
            bufferedOutputStream.flush();
        } catch (Exception e) {
            result = false;
        } finally {
            CloseUtils.closeIO(bufferedInputStream, inputStream, fileOutputStream, bufferedOutputStream);
        }
        return result;
    }

    /**
     * 保存字节到文件中
     *
     * @param bfile    字节数组
     * @param filePath 文件路径
     * @param filename 文件名
     */
    public static void saveByte2File(byte[] bfile, String filePath, String filename) {
        BufferedOutputStream bos = null;
        FileOutputStream fos = null;
        File file = null;
        try {
            file = createFile(filePath, filename);
            fos = new FileOutputStream(file);
            bos = new BufferedOutputStream(fos);
            bos.write(bfile);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            CloseUtils.closeIO(bos, fos);
        }
    }

    /**
     * 格式化文件大小
     *
     * @param context
     * @param size    文件长度
     * @return 格式化后的文件大小
     */
    public static String formatFileSize(Context context, long size) {
        return Formatter.formatFileSize(context, size);
    }


    /**
     * 调用手机系统打开对应文件
     *
     * @param file
     */
    public static void openFile(File file, Context context) {
        if (null != file && file.exists() && null != context) {
            Intent intent = new Intent();
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.setAction(Intent.ACTION_VIEW);
            String type = getMimeTypeFromFile(file);
            intent.setDataAndType(Uri.fromFile(file), type);
            context.startActivity(intent);
        }
    }

    /**
     * 保存图片到图片库，文件不能在隐私目录，否则无法显示，必须是以 Environment.getExternalStorageDirectory() 方法的返回值开头
     *
     * @param context context
     * @param saveFile 要保存的文件
     * @return 保存的文件
     */
    public static File saveImageToGallery(Context context, File saveFile) {
        if (saveFile != null && saveFile.exists()) {
            // 其次把文件插入到系统图库
            try {
                MediaStore.Images.Media.insertImage(context.getContentResolver(), saveFile.getAbsolutePath(),
                        saveFile.getName(), null);
                // 最后通知图库更新
                // 如果图库文件较多，防止扫描长时间不能访问，可以指定扫描目录
                String mimeType = getMimeType(saveFile);
                String[] paths = new String[]{saveFile.getAbsolutePath()};
                // 扫描文件，此处mimeType必须要填写，否则扫描无效
                // 参考地址
                // https://blog.csdn.net/weixin_34072458/article/details/88836444?utm_medium=distribute.pc_relevant_t0.none-task-blog-BlogCommendFromMachineLearnPai2-1.edu_weight&depth_1-utm_source=distribute.pc_relevant_t0.none-task-blog-BlogCommendFromMachineLearnPai2-1.edu_weight
                MediaScannerConnection.scanFile(context, paths, new String[]{mimeType}, (path, uri) -> LogUtils.d(path));
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }

        return saveFile;
    }

    public static String getMimeType(File file){
        FileNameMap fileNameMap = URLConnection.getFileNameMap();
        String type = fileNameMap.getContentTypeFor(file.getName());
        return type;
    }

    /**
     * 写入msg到文件中
     *
     * @param msg      消息内容
     * @param fileName 文件名
     * @param append   是否追加
     */
    public static void writeMsg2File(String msg, String fileName, boolean append) {
        File file = Files.getLogFile(fileName);

        if (file.exists() && file.canWrite()) {
            try {
                FileOutputStream os = new FileOutputStream(file, append);
                os.write(System.getProperty("line.separator").getBytes());
                try {
                    os.write(msg.getBytes("UTF-8"));
                } catch (UnsupportedEncodingException ex) {
                    ex.printStackTrace();
                    os.write(msg.getBytes());
                }
                os.write(System.getProperty("line.separator").getBytes());
                os.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 使用自定义方法获得文件的MIME类型
     */
    public static String getMimeTypeFromFile(File file) {
        String type = "*/*";
        String fName = file.getName();
        //获取后缀名前的分隔符"."在fName中的位置。
        int dotIndex = fName.lastIndexOf(".");
        if (dotIndex > 0) {
            //获取文件的后缀名
            String end = fName.substring(dotIndex, fName.length()).toLowerCase(Locale.getDefault());
            //在MIME和文件类型的匹配表中找到对应的MIME类型。
            HashMap<String, String> map = getMimeMap();
            if (!TextUtils.isEmpty(end) && map.keySet().contains(end)) {
                type = map.get(end);
            }
        }
        return type;
    }

    /**
     * 常用"文件扩展名—MIME类型"匹配表。
     */
    private static HashMap<String, String> getMimeMap() {
        HashMap<String, String> mapSimple = new HashMap<>();
        mapSimple.put(".3gp", "video/3gpp");
        mapSimple.put(".apk", "application/vnd.android.package-archive");
        mapSimple.put(".asf", "video/x-ms-asf");
        mapSimple.put(".avi", "video/x-msvideo");
        mapSimple.put(".bin", "application/octet-stream");
        mapSimple.put(".bmp", "image/bmp");
        mapSimple.put(".c", "text/plain");
        mapSimple.put(".chm", "application/x-chm");
        mapSimple.put(".class", "application/octet-stream");
        mapSimple.put(".conf", "text/plain");
        mapSimple.put(".cpp", "text/plain");
        mapSimple.put(".doc", "application/msword");
        mapSimple.put(".docx", "application/msword");
        mapSimple.put(".exe", "application/octet-stream");
        mapSimple.put(".gif", "image/gif");
        mapSimple.put(".gtar", "application/x-gtar");
        mapSimple.put(".gz", "application/x-gzip");
        mapSimple.put(".h", "text/plain");
        mapSimple.put(".htm", "text/html");
        mapSimple.put(".html", "text/html");
        mapSimple.put(".jar", "application/java-archive");
        mapSimple.put(".java", "text/plain");
        mapSimple.put(".jpeg", "image/jpeg");
        mapSimple.put(".jpg", "image/jpeg");
        mapSimple.put(".js", "application/x-javascript");
        mapSimple.put(".log", "text/plain");
        mapSimple.put(".m3u", "audio/x-mpegurl");
        mapSimple.put(".m4a", "audio/mp4a-latm");
        mapSimple.put(".m4b", "audio/mp4a-latm");
        mapSimple.put(".m4p", "audio/mp4a-latm");
        mapSimple.put(".m4u", "video/vnd.mpegurl");
        mapSimple.put(".m4v", "video/x-m4v");
        mapSimple.put(".mov", "video/quicktime");
        mapSimple.put(".mp2", "audio/x-mpeg");
        mapSimple.put(".mp3", "audio/x-mpeg");
        mapSimple.put(".mp4", "video/mp4");
        mapSimple.put(".mpc", "application/vnd.mpohun.certificate");
        mapSimple.put(".mpe", "video/mpeg");
        mapSimple.put(".mpeg", "video/mpeg");
        mapSimple.put(".mpg", "video/mpeg");
        mapSimple.put(".mpg4", "video/mp4");
        mapSimple.put(".mpga", "audio/mpeg");
        mapSimple.put(".msg", "application/vnd.ms-outlook");
        mapSimple.put(".ogg", "audio/ogg");
        mapSimple.put(".pdf", "application/pdf");
        mapSimple.put(".png", "image/png");
        mapSimple.put(".pps", "application/vnd.ms-powerpoint");
        mapSimple.put(".ppt", "application/vnd.ms-powerpoint");
        mapSimple.put(".pptx", "application/vnd.ms-powerpoint");
        mapSimple.put(".prop", "text/plain");
        mapSimple.put(".rar", "application/x-rar-compressed");
        mapSimple.put(".rc", "text/plain");
        mapSimple.put(".rmvb", "audio/x-pn-realaudio");
        mapSimple.put(".rtf", "application/rtf");
        mapSimple.put(".sh", "text/plain");
        mapSimple.put(".tar", "application/x-tar");
        mapSimple.put(".tgz", "application/x-compressed");
        mapSimple.put(".txt", "text/plain");
        mapSimple.put(".wav", "audio/x-wav");
        mapSimple.put(".wma", "audio/x-ms-wma");
        mapSimple.put(".wmv", "audio/x-ms-wmv");
        mapSimple.put(".wps", "application/vnd.ms-works");
        mapSimple.put(".xml", "text/plain");
        mapSimple.put(".xls", "application/vnd.ms-excel");
        mapSimple.put(".xlsx", "application/vnd.ms-excel");
        mapSimple.put(".z", "application/x-compress");
        mapSimple.put(".zip", "application/zip");
        mapSimple.put("", "*/*");
        return mapSimple;
    }


    /**
     * 安装intent 需要安装权限 {@link android.Manifest.permission#REQUEST_INSTALL_PACKAGES}
     *
     * @return
     */
    public Intent installIntent(Context context, File file) {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        if (file.exists() && file.isFile()) {
            FileProvider7.setIntentDataAndType(context, intent, "application/vnd.android.package-archive", file);
        }
        return intent;
    }
}
