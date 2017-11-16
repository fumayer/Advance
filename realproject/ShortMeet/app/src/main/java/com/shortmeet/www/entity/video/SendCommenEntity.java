package com.shortmeet.www.entity.video;

/**
 * Created by Fenglingyue on 2017/11/8.
 */

public class SendCommenEntity {

    /**
     * code : 0
     * msg : SUCCESS
     * data : {"comment_id":"4"}
     */

    private int code;
    private String msg;
    /**
     * comment_id : 4
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
        private String comment_id;

        public void setComment_id(String comment_id) {
            this.comment_id = comment_id;
        }

        public String getComment_id() {
            return comment_id;
        }
    }
}
