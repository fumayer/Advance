package com.shortmeet.www.entity.percenalCenter;

/**
 * Created by Fenglingyue on 2017/10/9.
 */

public class PerfectUserInfoEntity {

    /**
     * code : 0
     * msg : SUCCESS
     * data : {"account_id":"5","phone":"13716221350","img":"无路径","sex":"2","area":"北京","nickname":"shortmeet395045","birthday":"","content":"无","reg_time":"2017-10-27 16:16:29","sessionid":"s0h6lfdut72n9upg8g47fdkm76","session_time":"2017-11-26 16:34:04"}
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
     * sessionid : s0h6lfdut72n9upg8g47fdkm76
     * session_time : 2017-11-26 16:34:04
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
