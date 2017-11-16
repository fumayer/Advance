package com.shortmeet.www.entity.percenalCenter;

/**
 * Created by Fenglingyue on 2017/10/27.
 */

public class VisitorLoginEntity {


    /**
     * code : 0
     * msg : SUCCESS
     * data : {"account_id":"100000025","username":"sm100000026","phone":"","img":"","area":"","create_time":"2017-10-27 10:02:50","sessionid":"dh83pgmk5p45od4lmcqv8l4cp7","session_time":1511667061}
     */

    private int code;
    private String msg;
    /**
     * account_id : 100000025
     * username : sm100000026
     * phone :
     * img :
     * area :
     * create_time : 2017-10-27 10:02:50
     * sessionid : dh83pgmk5p45od4lmcqv8l4cp7
     * session_time : 1511667061
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

}
