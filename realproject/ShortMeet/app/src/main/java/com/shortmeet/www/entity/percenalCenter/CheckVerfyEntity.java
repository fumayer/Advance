package com.shortmeet.www.entity.percenalCenter;

/**
 * Created by Fenglingyue on 2017/10/31.
 */

public class CheckVerfyEntity {

    /**
     * code : 0
     * msg : SUCCESS
     * data : {"prev_phone":"13716221350","prev_code":285871}
     */

    private int code;
    private String msg;
    /**
     * prev_phone : 13716221350
     * prev_code : 285871
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
        private String prev_phone;
        private int prev_code;

        public void setPrev_phone(String prev_phone) {
            this.prev_phone = prev_phone;
        }

        public void setPrev_code(int prev_code) {
            this.prev_code = prev_code;
        }

        public String getPrev_phone() {
            return prev_phone;
        }

        public int getPrev_code() {
            return prev_code;
        }
    }
}
