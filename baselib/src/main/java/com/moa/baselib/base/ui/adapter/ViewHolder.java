package com.moa.baselib.base.ui.adapter;

import android.content.Context;
import androidx.annotation.ColorRes;
import androidx.annotation.StringRes;
import androidx.core.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * holderAdapter 中使用的 viewHolder
 *
 * @param <T> 数据泛型
 */
public abstract class ViewHolder<T> {

    private long id;
    protected View mView;
    protected HolderAdapter<T> mAdapter;
    protected T mData;
    protected Context mContext;

    public ViewHolder() {
    }

    public ViewHolder(HolderAdapter<T> mAdapter) {
        this.mAdapter = mAdapter;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public abstract int getLayoutId();

    /**
     * adapter中回调的item初始化，因为是在adapter的缓存中回调的，
     * 此方法中不能进行item数据缓存等相关操作，会有缓存导致数据错乱
     * 如：this.mData = data，会导致数据错乱
     *
     * @param data item data
     */
    protected View init(T data, ViewGroup viewGroup, Context context) {
        this.mContext = context;
        mView = inflateLayout(context, getLayoutId(), viewGroup);
        initView(mView, data, context);
        return mView;
    }

    /**
     * 初始化数据，并缓存item data，可避免每次在此函数外调用，需要传参和对view设置tag，简化操作
     *
     * @param data item data
     */
    protected void initData(T data, int position, Context context){
        // 缓存item data
        this.mData = data;
        bindData(data, position, context);
    }

    public void unbind(boolean full) {}

    public View inflateLayout(Context context, int layoutId, ViewGroup parent) {
        // 此处使用LayoutInflater.inflate，传入parent
        // 防止使用View.inflate导致item顶层布局设置的布局参数无效
        return LayoutInflater.from(context).inflate(layoutId, parent, false);
    }

    @SuppressWarnings("unchecked")
    public <V extends View> V findViewById(View view, int viewId) {
        return (V) view.findViewById(viewId);
    }

    public String getString(@StringRes int stringResId) {
        return mContext.getString(stringResId);
    }

    public int getColor(@ColorRes int colorResId) {
        return ContextCompat.getColor(mContext, colorResId);
    }

    public void setOnClickListener(View.OnClickListener onClickListener, View... views) {
        for (View view : views) {
            if(view != null){
                view.setOnClickListener(onClickListener);
            }
        }
    }

    @SuppressWarnings("unchecked")
    public <V extends View> V findViewById(int viewId) {
        return (V) mView.findViewById(viewId);
    }

    public abstract void initView(View itemView, T data, Context context);

    public abstract void bindData(T data, int position, Context context);

}
