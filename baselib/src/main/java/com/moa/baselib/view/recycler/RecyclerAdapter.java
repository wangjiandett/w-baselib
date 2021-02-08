package com.moa.baselib.view.recycler;

import androidx.annotation.LayoutRes;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

/**
 * 封装对RecyclerView adapter,支持empty view显示，item的点击长按事件。如下使用下拉刷新，上拉加载，等更多功能请参看 {@link RefreshRecyclerAdapter}
 * 如果嵌套使用NestedScrollView可以使RecyclerView自适应高度，并且设置以下属性
 * RecyclerView.setHasFixedSize(true);
 * RecyclerView.setNestedScrollingEnabled(false);
 * <p>
 * Created by：wangjian on 2019/1/8 17:29
 */
public abstract class RecyclerAdapter<D extends IData> extends RecyclerView.Adapter<RecyclerHolder> implements
        View.OnClickListener, View.OnLongClickListener {

    /**
     * empty holder时显示的view类型
     */
    public static final int ITEM_TYPE_EMPTY = -1;

    /**
     * empty view显示的状态
     */
    protected EmptyData mEmptyData = EmptyData.getLoading();

    /**
     * empty view是否可用
     */
    protected boolean isEmptyViewEnable = false;

    /**
     * item 点击事件回调函数
     */
    private OnItemClickListener<D> onItemClickListener;
    /**
     * item 长按事件回调函数
     */
    private OnItemLongClickListener<D> onItemLongClickListener;

    /**
     * item单击监听
     *
     * @param <D> 数据模型
     */
    public interface OnItemClickListener<D> {
        /**
         * item点击回调
         *
         * @param view     点击的view，即holder中的itemView
         * @param position 点击的位置
         * @param item     回调item数据
         */
        void onItemClick(View view, int position, D item);
    }

    /**
     * item长按监听
     *
     * @param <D> 数据模型
     */
    public interface OnItemLongClickListener<D> {
        /**
         * 长按item回调
         *
         * @param view     长按的view，即holder中的itemView
         * @param position 点击的位置
         * @param item     回调item数据
         */
        void onItemLongClick(View view, int position, D item);
    }

    /**
     * 设置item单击事件回调函数
     *
     * @param onItemClickListener 回调函数
     */
    public void setOnItemClickListener(OnItemClickListener<D> onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    /**
     * 设置item长按事件回调函数
     *
     * @param onItemLongClickListener 回调函数
     */
    public void setOnItemLongClickListener(OnItemLongClickListener<D> onItemLongClickListener) {
        this.onItemLongClickListener = onItemLongClickListener;
    }

    /**
     * empty view是否可用
     *
     * @return true 可用，false 不可以
     */
    public boolean isEmptyViewEnable() {
        return isEmptyViewEnable;
    }

    /**
     * 设置empty view是否可用
     *
     * @param enable true 可用，false 不可用
     */
    public void setEmptyViewEnable(boolean enable) {
        this.isEmptyViewEnable = enable;
    }

    /**
     * 获取当前empty view显示状态 {@link Status}
     *
     * @return 当前empty view显示状态
     */
    public EmptyData getEmptyViewStatus() {
        return mEmptyData;
    }

    /**
     * 更新empty view状态
     *
     * @param emptyStatus 当前empty view显示的状态
     */
    public void setEmptyViewStatus(EmptyData emptyStatus) {
        if (this.mEmptyData != emptyStatus && isEmptyViewEnable()) {
            this.mEmptyData = emptyStatus;
            for (int position = 0; position < getItemCount(); position++) {
                if (getItemViewType(position) == ITEM_TYPE_EMPTY) {
                    notifyItemChanged(position);
                    return;
                }
            }
        }
    }


    /**
     * 在empty holder点击时执行刷新操作，回调函数
     */
    public void onReload() {
        // 更新empty view的状态为正在加载中...
        setEmptyViewStatus(EmptyData.getLoading());
    }

    //----------------------start-------以下对数据集合操作-----------------------------------

    /**
     * 数据源
     */
    private List<D> mDataList;

    public List<D> getData() {
        if (mDataList == null) {
            mDataList = new ArrayList<>();
        }
        return mDataList;
    }

    public void setData(List<D> data) {
        if (data == null) {
            data = new ArrayList<>();
        }
        this.mDataList = data;
    }

    public void remove(D item) {
        int index = getData().indexOf(item);
        remove(index);
    }

    public void remove(int index) {
        getData().remove(index);
        notifyItemRemoved(index);
    }

    public void add(D item, int index) {
        getData().add(index, item);
        notifyItemChanged(index);
    }

    public void add(D item) {
        getData().add(item);
        int index = getData().indexOf(item);
        notifyItemChanged(index);
    }

    public void addAll(List<D> dataList) {
        if (dataList == null || dataList.size() == 0) {
            return;
        }
        getData().addAll(dataList);
        notifyItemRangeChanged(getData().size() - dataList.size(), getData().size());
    }

    public void addAll(int index, List<D> dataList) {
        if (dataList == null || dataList.size() == 0) {
            return;
        }
        getData().addAll(index, dataList);
        notifyItemRangeChanged(index, index + dataList.size());
    }

    public void clear() {
        getData().clear();
        notifyDataSetChanged();
    }

    public int getItemPosition(D item){
        return getData().indexOf(item);
    }

    //---------------------end--------对数据集合操作-----------------------------------

    /**
     * 加载item需要显示的view
     *
     * @param viewGroup parent view
     * @param resId     布局id
     * @return view
     */
    public View inflateView(ViewGroup viewGroup, @LayoutRes int resId) {
        // 此处获取item view需要传入viewGroup，否则item view布局不居中
        return LayoutInflater.from(viewGroup.getContext()).inflate(resId, viewGroup, false);
    }

    public D getItem(int position) {
        if (getData().size() > position && position > -1) {
            return getData().get(position);
        }
        return null;
    }

    /**
     * 获取真实数据长度
     */
    public int getDataCount() {
        return getData().size();
    }

    @Override
    public int getItemCount() {
        // count = 0时，引入empty view
        return getDataCount() != 0 ? getDataCount() : (isEmptyViewEnable ? 1 : 0);
    }

    @Override
    public int getItemViewType(int position) {
        // 数据为空时
        if (isEmptyViewEnable && getDataCount() == 0 && position == 0) {
            return ITEM_TYPE_EMPTY;
        } else if (getData().size() > position && position > -1) {
            D d = getData().get(position);
            if (d != null) {
                return d.getType();
            }
        }

        return super.getItemViewType(position);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerHolder holder, int position) {
        if (getItemViewType(position) == ITEM_TYPE_EMPTY) {
            holder.bindData(mEmptyData);
        } else {
            onBindViewHolder(getItem(position), position, holder);
            bindListener(holder);
        }
    }

    protected void onBindViewHolder(D data, int position, RecyclerHolder<D> holder) {
        holder.bindData(data);
    }

    protected void bindListener(RecyclerHolder holder) {
        if (onItemClickListener != null) {
            holder.setOnClickListener(this);
        }

        if (onItemLongClickListener != null) {
            holder.setOnLongClickListener(this);
        }
    }

    @NonNull
    @Override
    public RecyclerHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        if (viewType == ITEM_TYPE_EMPTY && isEmptyViewEnable) {
            return getEmptyViewHolder(viewGroup);
        }
        return getViewHolder(viewGroup, viewType);
    }

    protected RecyclerHolder getEmptyViewHolder(ViewGroup viewGroup) {
        return null;
    }

    /**
     * 返回对应的holder，此处viewType可由{@link IData#getType()}}中自行定义
     *
     * @param viewGroup viewGroup
     * @param viewType  自定义的viewType
     * @return RecyclerHolder
     */
    protected abstract RecyclerHolder<D> getViewHolder(ViewGroup viewGroup, int viewType);

    /**
     * 获取view在adapter中的position
     *
     * @return view在adapter中的position
     */
    private int getViewPosition(View view) {
        int position = 0;
        Object tag = view.getTag();
        if (tag instanceof RecyclerHolder) {
            RecyclerHolder vh = (RecyclerHolder) tag;
            position = vh.getAdapterPosition();
        }

        return position;
    }

    @Override
    public void onClick(View v) {
        int position = getViewPosition(v);
        onItemClickListener.onItemClick(v, position, getItem(position));
    }

    @Override
    public boolean onLongClick(View v) {
        int position = getViewPosition(v);
        onItemLongClickListener.onItemLongClick(v, position, getItem(position));
        return true;
    }


    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
        // 布局是GridLayoutManager时，控制header和footer显示一行
        RecyclerView.LayoutManager manager = recyclerView.getLayoutManager();
        if (manager instanceof GridLayoutManager) {
            final GridLayoutManager gridManager = ((GridLayoutManager) manager);
            gridManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
                @Override
                public int getSpanSize(int position) {
                    return (isFullItemViewType(position))
                            ? gridManager.getSpanCount() : 1;
                }
            });
        }
    }

    @Override
    public void onViewAttachedToWindow(@NonNull RecyclerHolder holder) {
        super.onViewAttachedToWindow(holder);
        ViewGroup.LayoutParams lp = holder.itemView.getLayoutParams();
        // 布局是StaggeredGridLayoutManager时，控制header和footer显示一行
        if (lp instanceof StaggeredGridLayoutManager.LayoutParams) {
            StaggeredGridLayoutManager.LayoutParams p = (StaggeredGridLayoutManager.LayoutParams) lp;
            if (isFullItemViewType(holder.getLayoutPosition())) {
                p.setFullSpan(true);
            } else {
                p.setFullSpan(false);
            }
        }
    }

    /**
     * 当前item是否显示独占一行或一列
     *
     * @param position 对应的位置
     * @return
     */
    protected boolean isFullItemViewType(int position) {
        return (getItemViewType(position) == ITEM_TYPE_EMPTY);
    }
}
