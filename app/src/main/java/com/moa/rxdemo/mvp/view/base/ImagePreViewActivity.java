package com.moa.rxdemo.mvp.view.base;

import com.moa.baselib.base.dispatcher.Runtimes;
import com.moa.baselib.base.ui.BaseActivity;
import com.moa.baselib.utils.FileUtil;
import com.moa.baselib.utils.Files;
import com.moa.baselib.utils.LogUtils;
import com.moa.rxdemo.R;
import com.moa.rxdemo.utils.ImageUtils;

import java.io.File;
import java.io.IOException;

import me.kareluo.intensify.image.IntensifyImageView;

/**
 * 图片预览界面
 * @author wangjian
 * Created on 2020/12/30 17:01
 */
public class ImagePreViewActivity extends BaseActivity {

    private File mFile = null;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_preview;
    }

    @Override
    protected void initView() {
        super.initView();
        setTitle("");
        // 使用参考地址
        // https://github.com/minetsh/IntensifyImageView
        IntensifyImageView imageView = (IntensifyImageView) findViewById(R.id.intensify_image);
        // 通过流设置
        // imageView.setImage(InputStream inputStream);
        // 通过文件设置
        // imageView.setImage(File file);
        // 通过文件路径设置
        // imageView.setImage(String path);

        getRightTv().setText(getString(R.string.save_result_img));
        getRightTv().setOnClickListener(v -> {
            if(mFile != null){
                File exFile = new File(Files.getExternalStorageFileDir(), mFile.getName());
                // copy到外存，并保存到图库，并分享
                try {
                    FileUtil.copy(mFile, exFile);
                    FileUtil.saveImageToGallery(this, exFile);
                    // ShareUtils.shareFile2QQ(activity, file)

                    Runtimes.postToMainThread(() -> showToast(R.string.save_result_img_success));
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });


        String url = getIntent().getStringExtra("url");
        ImageUtils.loadAsFile(this, url, file -> {
            mFile = file;
            LogUtils.d(file.getAbsolutePath());
            imageView.setImage(file);
        });
    }
}
