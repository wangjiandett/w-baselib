package com.moa.baselib.view.recycler;

/**
 * Recycler view 加载状态
 */
public class Status {

    /**
     * 未使用或未传递的code时使用
     */
    public static final int UNUSED = 0x111;
    /**
     * 加载成功
     */
    public static final int SUCCESS = 0x100;
    /**
     * 加载失败
     */
    public static final int FAIL = 0x101;
    /**
     * 加载完成，没有更多数据了
     */
    public static final int FINISH = 0x102;
    /**
     * 正在加载
     */
    public static final int LOADING = 0x103;

}
