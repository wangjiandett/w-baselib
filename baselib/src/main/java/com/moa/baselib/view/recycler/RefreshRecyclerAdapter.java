package com.moa.baselib.view.recycler;

import android.support.annotation.NonNull;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SimpleItemAnimator;
import android.view.ViewGroup;

import java.util.List;

/**
 * 支持显示header和footer的adapter，上拉加载和下拉刷新功能，如不需要这些功能可直接使用{@link RecyclerAdapter}
 *
 * @param <D> adapter中的数据泛型
 */
public abstract class RefreshRecyclerAdapter<D extends IData> extends RecyclerAdapter<D> {

    /**
     * 在enableHeader为true时设置head才有效
     */
    public static final int ITEM_TYPE_HEADER = -2;
    /**
     * 在enableFooter为true时设置head才有效
     */
    public static final int ITEM_TYPE_FOOTER = -3;

    // 使用系统的刷新组件
    private SwipeRefreshLayout mSwipeRefreshLayout;

    // 加载更多监听器
    private OnLoadMoreListener mOnLoadMoreListener;
    private RecyclerView mRecyclerView;

    // header需要显示的数据
    private Object mHeaderData;
    // 刷新状态
    private int mRefreshStatus = Status.LOADING;
    // 当前加载状态
    private int mLoadStatus = Status.LOADING;
    // 滚动到底部自动加载
    private boolean mAutoLoadMore;
    // 是否启用header view
    private boolean isHeaderViewEnable;
    // 是否启用footer view
    private boolean isFooterViewEnable;

    public RefreshRecyclerAdapter(RecyclerView recyclerView) {
        this.mRecyclerView = recyclerView;
        // 去除动画，防止更新footer view的时候闪烁，出现底部分割线
        RecyclerView.ItemAnimator animator = this.mRecyclerView.getItemAnimator();
        if (animator != null) {
            ((SimpleItemAnimator) animator).setSupportsChangeAnimations(false);
        }
    }

    /**
     * 设置是否滚动到底部自动加载
     */
    public void setAutoLoadMore(boolean mAutoLoadMore) {
        this.mAutoLoadMore = mAutoLoadMore;
    }

    /**
     * 返回header view是否可用
     */
    public boolean isHeaderViewEnable() {
        return isHeaderViewEnable;
    }

    /**
     * 设置header view是否可用
     */
    public void setHeaderViewEnable(boolean mEnableHeaderView) {
        this.isHeaderViewEnable = mEnableHeaderView;
    }

    /**
     * 返回footer view 是否可用
     */
    public boolean isFooterViewEnable() {
        return isFooterViewEnable;
    }

    /**
     * 设置footer view是否可用
     */
    public void setFooterViewEnable(boolean mEnableFooterView) {
        this.isFooterViewEnable = mEnableFooterView;
    }

    /**
     * 设置header要显示的数据，并更新
     */
    public void setHeaderData(Object headerData) {
        this.mHeaderData = headerData;
        notifyItemChanged(0);
    }

    @Override
    public int getItemViewType(int position) {
        // 数据为空时
        if (getDataCount() == 0) {
            if (isHeaderViewEnable && isEmptyViewEnable) {
                if (position == 0) {
                    return ITEM_TYPE_HEADER;
                } else if (position == 1) {
                    return ITEM_TYPE_EMPTY;
                }
            } else if (isEmptyViewEnable) {
                if (position == 0) {
                    return ITEM_TYPE_EMPTY;
                }
            } else if (isHeaderViewEnable) {
                if (position == 0) {
                    return ITEM_TYPE_HEADER;
                }
            }
        }
        // 数据不为空时
        else {
            // 头部
            if (isHeaderViewEnable && position == 0) {
                return ITEM_TYPE_HEADER;
            }
            // 尾部
            else if (isFooterViewEnable && position == getItemCount() - 1) {
                return ITEM_TYPE_FOOTER;
            }
        }

        // ITEM_TYPE_NORMAL 默认是0
        return super.getItemViewType(position);

    }

    @Override
    public int getItemCount() {
        if (getDataCount() == 0) {
            if (isHeaderViewEnable) {
                return super.getItemCount() + 1;
            } else {
                return super.getItemCount();
            }
        } else {
            if (isHeaderViewEnable && isFooterViewEnable) {
                return getDataCount() + 2;
            } else if (isHeaderViewEnable) {
                return getDataCount() + 1;
            } else if (isFooterViewEnable) {
                return getDataCount() + 1;
            } else {
                return super.getItemCount();
            }
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerHolder holder, int position) {
        if (getItemViewType(position) == ITEM_TYPE_FOOTER) {
            holder.bindData(mLoadStatus);
            // 底部footer view点击加载更多
            holder.setOnClickListener(v -> loadingMore());
            return;
        } else if (getItemViewType(position) == ITEM_TYPE_HEADER) {
            holder.bindData(mHeaderData);
            return;
        } else {
            position = getRealPosition(holder);
        }

        super.onBindViewHolder(holder, position);
    }

    @NonNull
    @Override
    public RecyclerHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        if (viewType == ITEM_TYPE_EMPTY && isEmptyViewEnable()) {
            return getEmptyViewHolder(viewGroup);
        } else if (viewType == ITEM_TYPE_FOOTER && isFooterViewEnable()) {
            return getFooterViewHolder(viewGroup);
        } else if (viewType == ITEM_TYPE_HEADER && isHeaderViewEnable()) {
            return getHeaderViewHolder(viewGroup);
        }
        return getViewHolder(viewGroup, viewType);
    }

    protected RecyclerHolder getHeaderViewHolder(ViewGroup viewGroup) {
        return null;
    }

    protected RecyclerHolder getFooterViewHolder(ViewGroup viewGroup) {
        return null;
    }

    public boolean isRefreshing() {
        return mSwipeRefreshLayout != null && mSwipeRefreshLayout.isRefreshing();
    }

    /**
     * 设置下拉刷新的view和监听器
     *
     * @param mSwipeRefreshLayout 刷新的view
     * @param onRefreshListener   刷新回调监听
     */
    public void setOnRefreshListener(SwipeRefreshLayout mSwipeRefreshLayout, SwipeRefreshLayout.OnRefreshListener onRefreshListener) {
        this.mSwipeRefreshLayout = mSwipeRefreshLayout;
        if (mSwipeRefreshLayout != null && onRefreshListener != null) {
            mSwipeRefreshLayout.setOnRefreshListener(() -> {
                setEmptyViewStatus(EmptyData.getLoading());
                onRefreshListener.onRefresh();
            });
        }
    }

    /**
     * 加载数据
     */
    public void onRefreshFail() {
        onRefreshFinish(null, true, EmptyData.getUnUsed());
    }

    public void onRefreshFinish(List<D> data) {
        onRefreshFinish(data, false, EmptyData.getUnUsed());
    }

    public void onRefreshFinish(List<D> data, int code) {
        onRefreshFinish(data, false, new EmptyData(code));
    }

    public void onRefreshFinish(List<D> data, boolean isFinish) {
        onRefreshFinish(data, isFinish, EmptyData.getUnUsed());
    }

    /**
     * 刷新加载失败
     */
    public void onRefreshFail(EmptyData data) {
        onRefreshFinish(null, true, data);
    }

    /**
     * 刷新完成回调
     * <p>
     * 1.data为空时加载失败
     * 2.data.size >= 0 时表示加载成功
     *
     * @param data     加载到的数据
     * @param isFinish 是否加载完所有数据
     */
    public void onRefreshFinish(List<D> data, boolean isFinish, EmptyData emptyData) {
        if (mSwipeRefreshLayout != null) {
            mSwipeRefreshLayout.setRefreshing(false);
        }

        if (data != null) {
            // 加载成功没有数据
            if (data.size() == 0) {
                mRefreshStatus = Status.FINISH;
                mEmptyData = EmptyData.getFinish();
                // 重置empty view的显示状态
                if (isEmptyViewEnable) {
                    setEmptyViewStatus(mEmptyData);
                }
            } else {
                // 加载成功且有数据
                mRefreshStatus = Status.SUCCESS;
            }
            setData(data);
            notifyDataSetChanged();
        } else {
            // 加载失败
            mRefreshStatus = Status.FAIL;
            // 未设置code时，默认使用FAIL，否则使用自定义code
            if (emptyData != null && emptyData.code == Status.UNUSED) {
                mEmptyData = EmptyData.getFail();
            } else {
                mEmptyData = emptyData;
            }

            // 重置empty view的显示状态
            if (isEmptyViewEnable) {
                setEmptyViewStatus(mEmptyData);
            }
        }

        // 更新底部加载更多按钮
        if (isFinish) {
            updateLoadingStatus(Status.FINISH);
        }
    }

    /**
     * 更新底部加载更多按钮样式和状态
     *
     * @param status 加载状态
     */
    private void updateLoadingStatus(int status) {
        this.mLoadStatus = status;
        // 在加载完成时，设置刷新可用
        if (!isLoading() && mSwipeRefreshLayout != null) {
            mSwipeRefreshLayout.setEnabled(true);
        }
        notifyItemChanged(getItemCount() - 1);
    }

    public boolean isLoading() {
        return mLoadStatus == Status.LOADING;
    }

    public void setOnLoadMoreListener(OnLoadMoreListener mOnLoadMoreListener) {
        this.mOnLoadMoreListener = mOnLoadMoreListener;
        this.mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {

            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                // 滚动到底部，自动加载
                if (mAutoLoadMore // 自动加载
                        && newState == RecyclerView.SCROLL_STATE_IDLE // 停止滑动
                        && isBottom(mRecyclerView) // 滚动到底部
                        && !isRefreshing()) {// 是否正在刷新
                    loadingMore();
                }
            }
        });
    }

    private void loadingMore() {
        // 正在加载和加载结束不回调事件
        if (mOnLoadMoreListener != null
                && mLoadStatus != Status.LOADING // 正在加载
                && mLoadStatus != Status.FINISH // 加载完成
                && !isRefreshing()) { // 没有正在刷新
            // 设置为正在加载
            updateLoadingStatus(Status.LOADING);

            // 加载中，禁用刷新
            if (mSwipeRefreshLayout != null) {
                mSwipeRefreshLayout.setEnabled(false);
            }
            // 回调加载
            mOnLoadMoreListener.onLoadMore();
        }
    }

    /**
     * 上拉加载数据失败
     */
    public void onLoadFail() {
        onLoadFinish(null, true);
    }

    /**
     * 上拉加载完成
     * 当 data == null 表示加载失败
     * 当 data.size > 0时表示加载成功
     *
     * @param data     加载到的数据
     * @param isFinish 是否加载完成，没有更多数据了，且仅在data != null 时有效
     */
    public void onLoadFinish(List<D> data, boolean isFinish) {
        if (data != null) {
            if (isFinish) {
                updateLoadingStatus(Status.FINISH);
            } else {
                updateLoadingStatus(Status.SUCCESS);
            }
            addAll(data);
        } else {
            updateLoadingStatus(Status.FAIL);
        }
    }

    /**
     * 获得holder在adapter中的位置，去除header
     */
    private int getRealPosition(RecyclerView.ViewHolder holder) {
        int position = holder.getAdapterPosition();
        return isHeaderViewEnable ? position - 1 : position;
    }

    /**
     * 是否滚动到底部
     */
    private boolean isBottom(RecyclerView recyclerView) {
        // computeVerticalScrollExtent()是当前屏幕显示的区域高度，
        // computeVerticalScrollOffset() 是当前屏幕之前滑过的距离，
        // computeVerticalScrollRange()是整个View控件的高度。

        //RecyclerView.canScrollVertically(1)的值表示是否能向上滚动，false表示已经滚动到底部
        //RecyclerView.canScrollVertically(-1)的值表示是否能向下滚动，false表示已经滚动到顶部
        if (recyclerView == null)
            return false;

        return !recyclerView.canScrollVertically(1);

//        if (recyclerView.computeVerticalScrollExtent() + recyclerView.computeVerticalScrollOffset() >= recyclerView.computeVerticalScrollRange())
//            return true;
//
//        return false;
    }

    @Override
    protected boolean isFullItemViewType(int position) {
        return super.isFullItemViewType(position)
                || (getItemViewType(position) == ITEM_TYPE_HEADER)
                || (getItemViewType(position) == ITEM_TYPE_FOOTER);
    }
}
