package com.shortmeet.www.entity.percenalCenter;

/**
 * Created by Fenglingyue on 2017/10/26.
 */

public class CreateVisitorEntity {
    /**
     * code : 0
     * msg : SUCCESS
     * data : {"account_id":100000016,"username":"sm100000017","usertype":null,"password":"03a5712fbb9ea503e18244ee23f21a44","img":"","area":"","nickname":"","birthday":"","content":"","create_time":"2017-10-26 14:29:13","sessionid":"8bu4h6c81c94v58gdg0fd0o075","session_time":1511591353}
     */

    private int code;
    private String msg;
    /**
     * account_id : 100000016
     * username : sm100000017
     * usertype : null
     * password : 03a5712fbb9ea503e18244ee23f21a44
     * img :
     * area :
     * nickname :
     * birthday :
     * content :
     * create_time : 2017-10-26 14:29:13
     * sessionid : 8bu4h6c81c94v58gdg0fd0o075
     * session_time : 1511591353
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
