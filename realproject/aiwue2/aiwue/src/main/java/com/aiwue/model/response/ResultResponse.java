package com.aiwue.model.response;

import java.io.Serializable;

/**
 * Created by Administrator on 2016/4/14.
 */
public class ResultResponse<T> implements Serializable {
    public T result;
    private int totalNum;
    private int pageSize;
    private int totalPage;
    private int pageNum;
    private String emsg;
    private String ecode;
    public static final String ECODE_SUCCESS="000000";
    public static final String ECODE_NO_RESULT="120005";

    public T getResult() {
        return result;
    }

    public void setResult(T result) {
        this.result = result;
    }

    public int getTotalNum() {
        return totalNum;
    }

    public void setTotalNum(int totalNum) {
        this.totalNum = totalNum;
    }

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    public int getTotalPage() {
        return totalPage;
    }

    public void setTotalPage(int totalPage) {
        this.totalPage = totalPage;
    }

    public int getPageNum() {
        return pageNum;
    }

    public void setPageNum(int pageNum) {
        this.pageNum = pageNum;
    }

    public String getEmsg() {
        return emsg;
    }

    public void setEmsg(String emsg) {
        this.emsg = emsg;
    }

    public String getEcode() {
        return ecode;
    }

    public void setEcode(String ecode) {
        this.ecode = ecode;
    }

    //    public ResultResponse(String more, String _message, T result) {
//        has_more = more;
//        message = _message;
//        data = result;
//    }


}
