package com.shortmeet.www.entity.video;

/**
 * Created by Fenglingyue on 2017/11/9.
 */

public class ReplyEntity {

    /**
     * code : 0
     * msg : SUCCESS
     * data : {"reply_id":"19"}
     */

    private int code;
    private String msg;
    /**
     * reply_id : 19
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
        private String reply_id;

        public void setReply_id(String reply_id) {
            this.reply_id = reply_id;
        }

        public String getReply_id() {
            return reply_id;
        }
    }
}
