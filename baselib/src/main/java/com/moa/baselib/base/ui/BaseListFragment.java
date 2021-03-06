package com.moa.baselib.base.ui;

import android.content.Context;
import android.view.View;
import android.widget.AbsListView;

import com.moa.baselib.base.ui.adapter.HolderAdapter;
import com.moa.baselib.base.ui.adapter.ViewHolder;

import org.jetbrains.annotations.NotNull;


/**
 * 封装列表适配器功能，适用于ListView和GridView
 *
 * <p>
 * Created by：wangjian on 2018/12/22 16:32
 */
public abstract class BaseListFragment<T> extends BaseFragment {
    
    protected HolderAdapter<T> mHolderAdapter;
    
    @Override
    protected void initView(@NotNull View view) {
        super.initView(view);
    }
    
    protected void bindAdapter(AbsListView listView) {
        mHolderAdapter = getAdapter();
        listView.setAdapter(mHolderAdapter);
    }
    
    private final class DataListAdapter extends HolderAdapter<T> {
    
        private DataListAdapter(Context context) {
            super(context);
        }

        @Override
        protected ViewHolder<T> createHolder(int position, T obj) {
            return getViewHolder();
        }
    }
    
    protected HolderAdapter<T> getAdapter(){
        if(mHolderAdapter != null){
            return mHolderAdapter;
        }
        return new DataListAdapter(getActivity());
    }
    
    protected abstract ViewHolder<T> getViewHolder();
    
}
