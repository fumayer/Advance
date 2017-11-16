package com.shortmeet.www.entity.video;

/**
 * Created by Fenglingyue on 2017/11/14.
 */

public class AddCancelCareEntity {

    /**
     * code : 0
     * msg : SUCCESS
     * data : {"result_type":1}
     */

    private int code;
    private String msg;
    /**
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
        private int result_type;

        public void setResult_type(int result_type) {
            this.result_type = result_type;
        }

        public int getResult_type() {
            return result_type;
        }
    }
}
