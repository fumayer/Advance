package com.shortmeet.www.entity.percenalCenter;

/**
 * Created by Fenglingyue on 2017/11/9.
 */

public class ZanCountMineFrgEntity {
    /**
     * code : 0
     * msg : SUCCESS
     * data : {"countNum":"2","myCount":0}
     */

    private int code;
    private String msg;
    /**
     * countNum : 2
     * myCount : 0
     */

    private DataEntity data;

    public void setCode(int code) {
        this.code = code;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public void setData(DataEntity data) {
        this.data = data;
    }

    public int getCode() {
        return code;
    }

    public String getMsg() {
        return msg;
    }

    public DataEntity getData() {
        return data;
    }

    public static class DataEntity {
        private String countNum;
        private int myCount;

        public void setCountNum(String countNum) {
            this.countNum = countNum;
        }

        public void setMyCount(int myCount) {
            this.myCount = myCount;
        }

        public String getCountNum() {
            return countNum;
        }

        public int getMyCount() {
            return myCount;
        }
    }
}
