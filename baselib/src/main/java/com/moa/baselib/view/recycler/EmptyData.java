package com.moa.baselib.view.recycler;

/**
 * Describe
 *
 * @author wangjian
 * Created on 2021/1/21 9:53
 */
public class EmptyData {

    public EmptyData(int code) {
        this.code = code;
    }

    public EmptyData(String msg) {
        this.msg = msg;
    }

    public EmptyData(int code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public int code;
    public String msg;


    public static EmptyData getLoading(){
        return new EmptyData(Status.LOADING);
    }

    public static EmptyData getSuccess(){
        return new EmptyData(Status.SUCCESS);
    }

    public static EmptyData getFail(){
        return new EmptyData(Status.FAIL);
    }

    public static EmptyData getFinish(){
        return new EmptyData(Status.FINISH);
    }

    public static EmptyData getUnUsed(){
        return new EmptyData(Status.UNUSED);
    }


}
