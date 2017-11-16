package com.shortmeet.www.entity.video;

/**
 * Created by Fenglingyue on 2017/11/7.
 */

public class ClickZanEntity {


    /**
     * code : 0
     * msg : SUCCESS
     * data : {"thumb_up_id":"5_33","table_name":"thumb_up20171101","result_type":1}
     */

    private int code;
    private String msg;
    /**
     * thumb_up_id : 5_33
     * table_name : thumb_up20171101
     * result_type : 1
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
        private String thumb_up_id;
        private String table_name;
        private int result_type;

        public void setThumb_up_id(String thumb_up_id) {
            this.thumb_up_id = thumb_up_id;
        }

        public void setTable_name(String table_name) {
            this.table_name = table_name;
        }

        public void setResult_type(int result_type) {
            this.result_type = result_type;
        }

        public String getThumb_up_id() {
            return thumb_up_id;
        }

        public String getTable_name() {
            return table_name;
        }

        public int getResult_type() {
            return result_type;
        }
    }
}
