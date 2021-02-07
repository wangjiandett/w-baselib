package com.moa.rxdemo.utils;

import android.graphics.drawable.Drawable;
import android.text.Html;
import android.util.TypedValue;

import com.moa.baselib.utils.DisplayUtils;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

/**
 * 加载html中的image标签中图片，需要异步调用
 *
 * @author wangjian
 * Created on 2020/12/22 11:07
 */
public class MyImageGetter implements Html.ImageGetter {

    @Override
    public Drawable getDrawable(String source) {
        Drawable drawable = null;
        InputStream is = null;
        try {
            is = (InputStream) new URL(source).getContent();
            // source便是图片的路径，如果图片在本地，可以这样做
            // is = c.getResources().getAssets().open(source);
            TypedValue typedValue = new TypedValue();
            typedValue.density = TypedValue.DENSITY_DEFAULT;
            drawable = Drawable.createFromResourceStream(null, typedValue, is, "src");

            int windowW = (DisplayUtils.getWindowWidth()-DisplayUtils.dip2px(32)); // 因为textView 有左右16dp的边距
            int imageW = drawable.getIntrinsicWidth();
            int imageH = drawable.getIntrinsicHeight();
            // 缩放图片比例
            if(imageW > windowW){
                float percent = ((float) (DisplayUtils.getWindowWidth()-DisplayUtils.dip2px(32)) / drawable.getIntrinsicWidth());
                imageW = (int)(drawable.getIntrinsicWidth() * percent);
                imageH = (int)(drawable.getIntrinsicHeight() * percent);
            }

            drawable.setBounds(0, 0, imageW, imageH);
            return drawable;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
}

// public class HtmlRemoteImageGetter implements Html.ImageGetter {
//     Context c;
//     View container;
//     URI baseUri;
//
//     /**
//      * Construct the URLImageParser which will execute AsyncTask and refresh the container
//      */
//     public HtmlRemoteImageGetter(View t, Context c, String baseUrl) {
//         this.c = c;
//         this.container = t;
//         if (baseUrl != null) {
//             this.baseUri = URI.create(baseUrl);
//         }
//     }
//
//     public Drawable getDrawable(String source) {
//         UrlDrawable urlDrawable = new UrlDrawable();
//         ImageGetterAsyncTask asyncTask = new ImageGetterAsyncTask(urlDrawable);
//
//         asyncTask.execute(source);
//
//         // return reference to URLDrawable which will asynchronously load the image specified in the src tag
//         return urlDrawable;
//     }
//
//     public class ImageGetterAsyncTask extends AsyncTask<String, Void, Drawable> {
//         UrlDrawable urlDrawable;
//         String source;
//
//         public ImageGetterAsyncTask(UrlDrawable d) {
//             this.urlDrawable = d;
//         }
//         @Override
//         protected void onPreExecute() {
//             super.onPreExecute();
//         }
//
//         @Override
//         protected void onPostExecute(final Drawable result) {
//             if (result == null) {
//                 // Log.w(HtmlTextView.TAG, "Drawable result is null! (source: " + source + ")");
//                 return;
//             }
//             // set the correct bound according to the result from HTTP call
// //            urlDrawable.setBounds(0, 0, 0 + result.getIntrinsicWidth(), 0 + result.getIntrinsicHeight());
//
//             // change the reference of the current drawable to the result from the HTTP call
//             urlDrawable.drawable = result;
//
//             // redraw the image by invalidating the container
//             urlDrawable.invalidateSelf();
//             HtmlRemoteImageGetter.this.container.invalidate();
//             // important
//             // <span style="color:#ff0000;">((HtmlTextView)container).setText(((HtmlTextView) container).getText());</span>
//
//         }
//
//         @Override
//         protected Drawable doInBackground(String... params) {
//             source = params[0];
//             return fetchDrawable(source);
//         }
//         /**
//          * Get the Drawable from URL
//          */
//         public Drawable fetchDrawable(String urlString) {
//             try {
//                 InputStream is = fetch(urlString);
//                 Bitmap bitmap= BitmapFactory.decodeStream(is);
//                 Drawable drawable = new BitmapDrawable(c.getResources(), bitmap);
//                 int width = bitmap.getWidth();
//                 int height = bitmap.getHeight();
//
//                 int newWidth = width;
//                 int newHeight = height;
//
//                 if( width > container.getWidth() ) {
//                     newWidth = container.getWidth();
//                     newHeight = (newWidth * height) / width;
//                 }
//
//                 drawable.setBounds(0, 0, 0 + newWidth, 0 + newHeight);
//                 urlDrawable.setBounds(0, 0, newWidth, newHeight);
//                 return drawable;
//             } catch (Exception e) {
//                 return null;
//             }
//         }
//
//         private InputStream fetch(String urlString) throws IOException {
//             URL url;
//             if (baseUri != null) {
//                 url = baseUri.resolve(urlString).toURL();
//             } else {
//                 url = URI.create(urlString).toURL();
//             }
//
//             return (InputStream) url.getContent();
//         }
//     }
//
//     @SuppressWarnings("deprecation")
//     public class UrlDrawable extends BitmapDrawable {
//         protected Drawable drawable;
//
//         @Override
//         public void draw(Canvas canvas) {
//             // override the draw to facilitate refresh function later
//             if (drawable != null) {
//                 drawable.draw(canvas);
//             }
//         }
//     }
// }