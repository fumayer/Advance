package com.shortmeet.www.entity.percenalCenter;

/**
 * Created by Fenglingyue on 2017/10/9.
 */

public class RegistEntity {


    /**
     * code : 0
     * msg : SUCCESS
     * data : {"account_id":100000033,"usertype":1,"phone":"18566165185","img":"","sex":"","area":"","nickname":"","birthday":"","content":"","reg_time":"2017-10-27 14:38:58","sessionid":"dhsb4r4e88343i092jlq65fbf3","session_time":1511678338}
     */

    private int code;
    private String msg;
    /**
     * account_id : 100000033
     * usertype : 1
     * phone : 18566165185
     * img :
     * sex :
     * area :
     * nickname :
     * birthday :
     * content :
     * reg_time : 2017-10-27 14:38:58
     * sessionid : dhsb4r4e88343i092jlq65fbf3
     * session_time : 1511678338
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
