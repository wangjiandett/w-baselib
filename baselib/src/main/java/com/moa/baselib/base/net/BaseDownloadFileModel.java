package com.moa.baselib.base.net;

import com.moa.baselib.base.net.mvp.ValueCallback;
import com.moa.baselib.utils.FileUtil;
import com.moa.baselib.utils.LogUtils;

import java.io.File;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.core.Observer;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.functions.Function;
import io.reactivex.rxjava3.schedulers.Schedulers;
import okhttp3.ResponseBody;

/**
 * 文件下载 base model
 */
public abstract class BaseDownloadFileModel implements ProgressResultListener {

    private Disposable mDisposable;
    private boolean isDownloading;
    protected ValueCallback<File> mCallback;

    public BaseDownloadFileModel() {
    }

    public BaseDownloadFileModel(ValueCallback<File> mCallback) {
        this.mCallback = mCallback;
    }

    /**
     * 监听下载进度，需要设置拦截器
     *
     * @param listener 监听下载进度
     */
    protected abstract Observable<ResponseBody> getDownloadObservable(ProgressResultListener listener);

    /**
     * 下载文件
     *
     * @param destFile 文件保存位置
     */
    public void download(final File destFile) {
        getDownloadObservable(this)
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .map(new ResultFilter(destFile))
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<File>() {

                    @Override
                    public void onSubscribe(Disposable d) {
                        LogUtils.d("download start");
                        mDisposable = d;
                        isDownloading = true;
                    }

                    @Override
                    public void onNext(File file) {
                        LogUtils.d("download file success: " + file.getAbsoluteFile());
                        isDownloading = false;
                        onSuccess(file);
                    }

                    @Override
                    public void onError(Throwable e) {
                        LogUtils.d(e);
                        isDownloading = false;
                        onFail(e.getMessage());
                    }

                    @Override
                    public void onComplete() {
                        LogUtils.d("onComplete");
                    }
                });
    }

    public void cancel() {
        if (this.mDisposable != null && !this.mDisposable.isDisposed()) {
            this.mDisposable.dispose();
            isDownloading = false;
        }
    }

    public boolean isDownloading() {
        return isDownloading;
    }

    private static class ResultFilter implements Function<ResponseBody, File> {

        private File desFile;

        public ResultFilter(File desFile) {
            this.desFile = desFile;
        }

        @Override
        public File apply(ResponseBody responseBody) throws Exception {
            if (desFile != null) {
                FileUtil.writeInputStream2File(responseBody.byteStream(), desFile.getAbsolutePath());
            }
            return desFile;
        }
    }


    /**
     * the success callback
     *
     * @param value the success value
     */
    protected abstract void onSuccess(File value);

    /**
     * the fail callback
     *
     * @param msg the fail message
     */
    protected abstract void onFail(String msg);

}
