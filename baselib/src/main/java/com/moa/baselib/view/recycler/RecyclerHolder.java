package com.moa.baselib.view.recycler;

import android.content.Context;
import androidx.annotation.ColorRes;
import androidx.annotation.NonNull;
import androidx.annotation.StringRes;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;
import android.util.SparseArray;
import android.view.View;

import com.moa.baselib.R;
import com.moa.baselib.utils.AppUtils;


/**
 * ViewHolder 功能简单封装
 * <p>
 * Created by：wangjian on 2019/1/8 17:30
 */
public abstract class RecyclerHolder<D> extends RecyclerView.ViewHolder {
    
    private SparseArray<View> views;
    private RecyclerAdapter mAdapter;
    protected Context mContext;
    protected D mData;

    public RecyclerHolder(@NonNull View itemView) {
        super(itemView);
        // 设置tag供点击事件的时候获取当前对象
        mContext = itemView.getContext();
        itemView.setTag(this);
        views = new SparseArray<>();
        initSelector();
        initView();
    }

    public RecyclerHolder(@NonNull View itemView, RecyclerAdapter mAdapter) {
        this(itemView);
        this.mAdapter = mAdapter;
    }

    protected <V extends View> V findViewById(int viewId){
        View view = views.get(viewId);
        if(view == null){
            view = itemView.findViewById(viewId);
            views.put(viewId,view);
        }
        return (V) view;
    }

    public String getString(@StringRes int stringResId){
        return mContext.getString(stringResId);
    }

    public int getColor(@ColorRes int colorResId){
        return ContextCompat.getColor(mContext, colorResId);
    }

    /**
     * 设置item点击事件监听
     */
    public void setOnClickListener(View.OnClickListener onClickListener){
        this.itemView.setOnClickListener(onClickListener);
    }

    /**
     * 设置item长按事件监听
     */
    public void setOnLongClickListener(View.OnLongClickListener onLongClickListener){
        this.itemView.setOnLongClickListener(onLongClickListener);
    }

    /**
     * 初始化item view bg selector
     */
    protected void initSelector(){
        itemView.setBackground(AppUtils.getDrawable(itemView.getContext(), R.drawable.tt_list_item_selector));
    }

    public RecyclerAdapter getAdapter() {
        return mAdapter;
    }

    public void setAdapter(RecyclerAdapter mAdapter) {
        this.mAdapter = mAdapter;
    }

    /**
     * 初始化view组件
     */
    public void initView(){}
    
    /**
     * 数据和view组件进行绑定
     *
     * @param data 数据源
     */
    public void bindData(D data){
        this.mData = data;
    }

    public void unbind(){
        views.clear();
    }
    
}
