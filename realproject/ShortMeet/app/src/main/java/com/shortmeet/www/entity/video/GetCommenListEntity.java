package com.shortmeet.www.entity.video;

import java.util.List;

/**
 * Created by Fenglingyue on 2017/11/8.
 */

public class GetCommenListEntity {

    /**
     * code : 0
     * msg : SUCCESS
     * data : {"list":[{"id":"18","c_account_id":"15","vod_account_id":"5","vod_id":"33","c_content":"斤斤计较","is_reply":"2","is_report":"2","comment_time":"1天前","parent_id":"0","nickname":"shortmeet2356","img":"无路径","comment_info":{"id":"","p_account_id":"","p_content":"","p_nickname":"","p_img":""},"type":false},{"id":"17","c_account_id":"20","vod_account_id":"5","vod_id":"33","c_content":"vvvvvv不不不","is_reply":"2","is_report":"2","comment_time":"1天前","parent_id":"0","nickname":"","img":"","comment_info":{"id":"","p_account_id":"","p_content":"","p_nickname":"","p_img":""},"type":false},{"id":"16","c_account_id":"10","vod_account_id":"5","vod_id":"33","c_content":"红7777777","is_reply":"2","is_report":"2","comment_time":"1天前","parent_id":"0","nickname":"shortmeet024587","img":"无路径","comment_info":{"id":"","p_account_id":"","p_content":"","p_nickname":"","p_img":""},"type":false},{"id":"15","c_account_id":"5","vod_account_id":"5","vod_id":"33","c_content":"红测试评论1","is_reply":"2","is_report":"2","comment_time":"1天前","parent_id":"0","nickname":"shortmeet395045","img":"无路径","comment_info":{"id":"","p_account_id":"","p_content":"","p_nickname":"","p_img":""},"type":false},{"id":"14","c_account_id":"15","vod_account_id":"5","vod_id":"33","c_content":"测试评论1uuuuuuu","is_reply":"2","is_report":"2","comment_time":"1天前","parent_id":"0","nickname":"shortmeet2356","img":"无路径","comment_info":{"id":"","p_account_id":"","p_content":"","p_nickname":"","p_img":""},"type":false},{"id":"13","c_account_id":"20","vod_account_id":"5","vod_id":"33","c_content":"测试评论ggggg","is_reply":"2","is_report":"2","comment_time":"1天前","parent_id":"0","nickname":"","img":"","comment_info":{"id":"","p_account_id":"","p_content":"","p_nickname":"","p_img":""},"type":false},{"id":"12","c_account_id":"10","vod_account_id":"5","vod_id":"33","c_content":"测试评论ooo","is_reply":"2","is_report":"2","comment_time":"1天前","parent_id":"0","nickname":"shortmeet024587","img":"无路径","comment_info":{"id":"","p_account_id":"","p_content":"","p_nickname":"","p_img":""},"type":false},{"id":"11","c_account_id":"5","vod_account_id":"5","vod_id":"33","c_content":"红领巾测试评论1测试评论1测试评论18","is_reply":"2","is_report":"2","comment_time":"1天前","parent_id":"0","nickname":"shortmeet395045","img":"无路径","comment_info":{"id":"","p_account_id":"","p_content":"","p_nickname":"","p_img":""},"type":false},{"id":"10","c_account_id":"15","vod_account_id":"5","vod_id":"33","c_content":"红领巾测试评论1测试评论9","is_reply":"2","is_report":"2","comment_time":"1天前","parent_id":"0","nickname":"shortmeet2356","img":"无路径","comment_info":{"id":"","p_account_id":"","p_content":"","p_nickname":"","p_img":""},"type":false},{"id":"9","c_account_id":"20","vod_account_id":"5","vod_id":"33","c_content":"红领巾测试评论1测试评论1测试评论1","is_reply":"2","is_report":"2","comment_time":"1天前","parent_id":"0","nickname":"","img":"","comment_info":{"id":"","p_account_id":"","p_content":"","p_nickname":"","p_img":""},"type":false}],"has_next":true,"next_page":1,"total_num":"15"}
     */

    private int code;
    private String msg;
    /**
     * list : [{"id":"18","c_account_id":"15","vod_account_id":"5","vod_id":"33","c_content":"斤斤计较","is_reply":"2","is_report":"2","comment_time":"1天前","parent_id":"0","nickname":"shortmeet2356","img":"无路径","comment_info":{"id":"","p_account_id":"","p_content":"","p_nickname":"","p_img":""},"type":false},{"id":"17","c_account_id":"20","vod_account_id":"5","vod_id":"33","c_content":"vvvvvv不不不","is_reply":"2","is_report":"2","comment_time":"1天前","parent_id":"0","nickname":"","img":"","comment_info":{"id":"","p_account_id":"","p_content":"","p_nickname":"","p_img":""},"type":false},{"id":"16","c_account_id":"10","vod_account_id":"5","vod_id":"33","c_content":"红7777777","is_reply":"2","is_report":"2","comment_time":"1天前","parent_id":"0","nickname":"shortmeet024587","img":"无路径","comment_info":{"id":"","p_account_id":"","p_content":"","p_nickname":"","p_img":""},"type":false},{"id":"15","c_account_id":"5","vod_account_id":"5","vod_id":"33","c_content":"红测试评论1","is_reply":"2","is_report":"2","comment_time":"1天前","parent_id":"0","nickname":"shortmeet395045","img":"无路径","comment_info":{"id":"","p_account_id":"","p_content":"","p_nickname":"","p_img":""},"type":false},{"id":"14","c_account_id":"15","vod_account_id":"5","vod_id":"33","c_content":"测试评论1uuuuuuu","is_reply":"2","is_report":"2","comment_time":"1天前","parent_id":"0","nickname":"shortmeet2356","img":"无路径","comment_info":{"id":"","p_account_id":"","p_content":"","p_nickname":"","p_img":""},"type":false},{"id":"13","c_account_id":"20","vod_account_id":"5","vod_id":"33","c_content":"测试评论ggggg","is_reply":"2","is_report":"2","comment_time":"1天前","parent_id":"0","nickname":"","img":"","comment_info":{"id":"","p_account_id":"","p_content":"","p_nickname":"","p_img":""},"type":false},{"id":"12","c_account_id":"10","vod_account_id":"5","vod_id":"33","c_content":"测试评论ooo","is_reply":"2","is_report":"2","comment_time":"1天前","parent_id":"0","nickname":"shortmeet024587","img":"无路径","comment_info":{"id":"","p_account_id":"","p_content":"","p_nickname":"","p_img":""},"type":false},{"id":"11","c_account_id":"5","vod_account_id":"5","vod_id":"33","c_content":"红领巾测试评论1测试评论1测试评论18","is_reply":"2","is_report":"2","comment_time":"1天前","parent_id":"0","nickname":"shortmeet395045","img":"无路径","comment_info":{"id":"","p_account_id":"","p_content":"","p_nickname":"","p_img":""},"type":false},{"id":"10","c_account_id":"15","vod_account_id":"5","vod_id":"33","c_content":"红领巾测试评论1测试评论9","is_reply":"2","is_report":"2","comment_time":"1天前","parent_id":"0","nickname":"shortmeet2356","img":"无路径","comment_info":{"id":"","p_account_id":"","p_content":"","p_nickname":"","p_img":""},"type":false},{"id":"9","c_account_id":"20","vod_account_id":"5","vod_id":"33","c_content":"红领巾测试评论1测试评论1测试评论1","is_reply":"2","is_report":"2","comment_time":"1天前","parent_id":"0","nickname":"","img":"","comment_info":{"id":"","p_account_id":"","p_content":"","p_nickname":"","p_img":""},"type":false}]
     * has_next : true
     * next_page : 1
     * total_num : 15
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
        private String total_num;
        /**
         * id : 18
         * c_account_id : 15
         * vod_account_id : 5
         * vod_id : 33
         * c_content : 斤斤计较
         * is_reply : 2
         * is_report : 2
         * comment_time : 1天前
         * parent_id : 0
         * nickname : shortmeet2356
         * img : 无路径
         * comment_info : {"id":"","p_account_id":"","p_content":"","p_nickname":"","p_img":""}
         * type : false
         */

        private List<ListEntity> list;

        public void setHas_next(boolean has_next) {
            this.has_next = has_next;
        }

        public void setNext_page(int next_page) {
            this.next_page = next_page;
        }

        public void setTotal_num(String total_num) {
            this.total_num = total_num;
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

        public String getTotal_num() {
            return total_num;
        }

        public List<ListEntity> getList() {
            return list;
        }

        public static class ListEntity {
            private String id;
            private String c_account_id;
            private String vod_account_id;
            private String vod_id;
            private String c_content;
            private String is_reply;
            private String is_report;
            private String comment_time;
            private String parent_id;
            private String nickname;
            private String img;
            /**
             * id :
             * p_account_id :
             * p_content :
             * p_nickname :
             * p_img :
             */

            private CommentInfoEntity comment_info;
            private boolean type;

            public void setId(String id) {
                this.id = id;
            }

            public void setC_account_id(String c_account_id) {
                this.c_account_id = c_account_id;
            }

            public void setVod_account_id(String vod_account_id) {
                this.vod_account_id = vod_account_id;
            }

            public void setVod_id(String vod_id) {
                this.vod_id = vod_id;
            }

            public void setC_content(String c_content) {
                this.c_content = c_content;
            }

            public void setIs_reply(String is_reply) {
                this.is_reply = is_reply;
            }

            public void setIs_report(String is_report) {
                this.is_report = is_report;
            }

            public void setComment_time(String comment_time) {
                this.comment_time = comment_time;
            }

            public void setParent_id(String parent_id) {
                this.parent_id = parent_id;
            }

            public void setNickname(String nickname) {
                this.nickname = nickname;
            }

            public void setImg(String img) {
                this.img = img;
            }

            public void setComment_info(CommentInfoEntity comment_info) {
                this.comment_info = comment_info;
            }

            public void setType(boolean type) {
                this.type = type;
            }

            public String getId() {
                return id;
            }

            public String getC_account_id() {
                return c_account_id;
            }

            public String getVod_account_id() {
                return vod_account_id;
            }

            public String getVod_id() {
                return vod_id;
            }

            public String getC_content() {
                return c_content;
            }

            public String getIs_reply() {
                return is_reply;
            }

            public String getIs_report() {
                return is_report;
            }

            public String getComment_time() {
                return comment_time;
            }

            public String getParent_id() {
                return parent_id;
            }

            public String getNickname() {
                return nickname;
            }

            public String getImg() {
                return img;
            }

            public CommentInfoEntity getComment_info() {
                return comment_info;
            }

            public boolean isType() {
                return type;
            }

            public static class CommentInfoEntity {
                private String id;
                private String p_account_id;
                private String p_content;
                private String p_nickname;
                private String p_img;

                public void setId(String id) {
                    this.id = id;
                }

                public void setP_account_id(String p_account_id) {
                    this.p_account_id = p_account_id;
                }

                public void setP_content(String p_content) {
                    this.p_content = p_content;
                }

                public void setP_nickname(String p_nickname) {
                    this.p_nickname = p_nickname;
                }

                public void setP_img(String p_img) {
                    this.p_img = p_img;
                }

                public String getId() {
                    return id;
                }

                public String getP_account_id() {
                    return p_account_id;
                }

                public String getP_content() {
                    return p_content;
                }

                public String getP_nickname() {
                    return p_nickname;
                }

                public String getP_img() {
                    return p_img;
                }
            }
        }
    }
}
