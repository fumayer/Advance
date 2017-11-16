package com.shortmeet.www.entity.video;

/**
 * Created by Fenglingyue on 2017/11/7.
 */

public class VideoDetailStatusEntity {

    /**
     * code : 0
     * msg : SUCCESS
     * data : {"type":1,"thumb_up_id":"5_33","table_name":"thumb_up20171101","countNum":"4","followStatus":false}
     */

    private int code;
    private String msg;
    /**
     * type : 1
     * thumb_up_id : 5_33
     * table_name : thumb_up20171101
     * countNum : 4
     * followStatus : false
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
        private int type;
        private String thumb_up_id;
        private String table_name;
        private String countNum;
        private boolean followStatus;

        public void setType(int type) {
            this.type = type;
        }

        public void setThumb_up_id(String thumb_up_id) {
            this.thumb_up_id = thumb_up_id;
        }

        public void setTable_name(String table_name) {
            this.table_name = table_name;
        }

        public void setCountNum(String countNum) {
            this.countNum = countNum;
        }

        public void setFollowStatus(boolean followStatus) {
            this.followStatus = followStatus;
        }

        public int getType() {
            return type;
        }

        public String getThumb_up_id() {
            return thumb_up_id;
        }

        public String getTable_name() {
            return table_name;
        }

        public String getCountNum() {
            return countNum;
        }

        public boolean isFollowStatus() {
            return followStatus;
        }
    }
}
