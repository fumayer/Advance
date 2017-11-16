package com.shortmeet.www.entity.percenalCenter;

/**
 * Created by Fenglingyue on 2017/10/11.
 */

public class LoginEntity {


    /**
     * code : 0
     * msg : SUCCESS
     * data : {"account_id":"5","phone":"13716221350","img":"无路径","sex":"2","area":"北京","nickname":"shortmeet395045","birthday":"","content":"无","reg_time":"2017-10-27 16:16:29","sessionid":"uou630hj4h45te7oikqtb8gfs7","session_time":1511694925}
     */

    private int code;
    private String msg;
    /**
     * account_id : 5
     * phone : 13716221350
     * img : 无路径
     * sex : 2
     * area : 北京
     * nickname : shortmeet395045
     * birthday :
     * content : 无
     * reg_time : 2017-10-27 16:16:29
     * sessionid : uou630hj4h45te7oikqtb8gfs7
     * session_time : 1511694925
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
