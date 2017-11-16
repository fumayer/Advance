package com.shortmeet.www.entity.video;

import java.util.List;

/**
 * Created by Fenglingyue on 2017/11/8.
 */

public class ShowZanHeadsEntity {

    /**
     * code : 0
     * msg : SUCCESS
     * data : {"list":[{"id":"5_33","account_id":"5","vod_id":"33","add_time":"2017-11-08 13:47:48","table_name":"thumb_up20171101","vod_account_id":"5","nickname":"shortmeet395045","img":"无路径","content":"无"},{"id":"20_33","account_id":"20","vod_id":"33","add_time":"2017-11-08 11:48:54","table_name":"th","vod_account_id":"5","nickname":"","img":"","content":null}],"has_next":false,"next_page":1}
     */

    private int code;
    private String msg;
    /**
     * list : [{"id":"5_33","account_id":"5","vod_id":"33","add_time":"2017-11-08 13:47:48","table_name":"thumb_up20171101","vod_account_id":"5","nickname":"shortmeet395045","img":"无路径","content":"无"},{"id":"20_33","account_id":"20","vod_id":"33","add_time":"2017-11-08 11:48:54","table_name":"th","vod_account_id":"5","nickname":"","img":"","content":null}]
     * has_next : false
     * next_page : 1
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
        private boolean has_next;
        private int next_page;
        /**
         * id : 5_33
         * account_id : 5
         * vod_id : 33
         * add_time : 2017-11-08 13:47:48
         * table_name : thumb_up20171101
         * vod_account_id : 5
         * nickname : shortmeet395045
         * img : 无路径
         * content : 无
         */

        private List<ListEntity> list;

        public void setHas_next(boolean has_next) {
            this.has_next = has_next;
        }

        public void setNext_page(int next_page) {
            this.next_page = next_page;
        }

        public void setList(List<ListEntity> list) {
            this.list = list;
        }

        public boolean isHas_next() {
            return has_next;
        }

        public int getNext_page() {
            return next_page;
        }

        public List<ListEntity> getList() {
            return list;
        }

        public static class ListEntity {
            private String id;
            private String account_id;
            private String vod_id;
            private String add_time;
            private String table_name;
            private String vod_account_id;
            private String nickname;
            private String img;
            private String content;

            public void setId(String id) {
                this.id = id;
            }

            public void setAccount_id(String account_id) {
                this.account_id = account_id;
            }

            public void setVod_id(String vod_id) {
                this.vod_id = vod_id;
            }

            public void setAdd_time(String add_time) {
                this.add_time = add_time;
            }

            public void setTable_name(String table_name) {
                this.table_name = table_name;
            }

            public void setVod_account_id(String vod_account_id) {
                this.vod_account_id = vod_account_id;
            }

            public void setNickname(String nickname) {
                this.nickname = nickname;
            }

            public void setImg(String img) {
                this.img = img;
            }

            public void setContent(String content) {
                this.content = content;
            }

            public String getId() {
                return id;
            }

            public String getAccount_id() {
                return account_id;
            }

            public String getVod_id() {
                return vod_id;
            }

            public String getAdd_time() {
                return add_time;
            }

            public String getTable_name() {
                return table_name;
            }

            public String getVod_account_id() {
                return vod_account_id;
            }

            public String getNickname() {
                return nickname;
            }

            public String getImg() {
                return img;
            }

            public String getContent() {
                return content;
            }
        }
    }
}
