package com.shortmeet.www.entity.percenalCenter;

import java.util.List;

/**
 * Created by Fenglingyue on 2017/10/19.
 */

public class MyWorkEntity {

    /**
     * code : 0
     * msg : SUCCESS
     * data : {"video":{"videos":[{"id":"32","account_id":"5","type":"1","activity_id":"1","tag":"活动","video_id":"59F342F19EB80721DB269EA7","title":"我的对对对v标题","duration":"","cover_url":"http://tanlanguiv2.oss-cn-beijing.aliyuncs.com/thumb/1f0c3476-b23f-4d8e-8465-a968d1016919.png","status":"1","media_type":"mp4","bitrate":"0.00","definition":"","encrypt":"1","play_url":"http://tlgshortmeeetspace.oss-cn-beijing.aliyuncs.com/vi/000C5A46E6B84C94ADBE6FB115451BEF","describe":"我的田娃","area":"北京","item_type":"2","create_time":"2017-10-27 22:30:09","is_show":"0","is_del":"0","is_adopt":"0","admin_id":"0","adopt_time":"0000-00-00 00:00:00"}],"type":0},"has_next":false,"next_page":1}
     */

    private int code;
    private String msg;
    /**
     * video : {"videos":[{"id":"32","account_id":"5","type":"1","activity_id":"1","tag":"活动","video_id":"59F342F19EB80721DB269EA7","title":"我的对对对v标题","duration":"","cover_url":"http://tanlanguiv2.oss-cn-beijing.aliyuncs.com/thumb/1f0c3476-b23f-4d8e-8465-a968d1016919.png","status":"1","media_type":"mp4","bitrate":"0.00","definition":"","encrypt":"1","play_url":"http://tlgshortmeeetspace.oss-cn-beijing.aliyuncs.com/vi/000C5A46E6B84C94ADBE6FB115451BEF","describe":"我的田娃","area":"北京","item_type":"2","create_time":"2017-10-27 22:30:09","is_show":"0","is_del":"0","is_adopt":"0","admin_id":"0","adopt_time":"0000-00-00 00:00:00"}],"type":0}
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
        /**
         * videos : [{"id":"32","account_id":"5","type":"1","activity_id":"1","tag":"活动","video_id":"59F342F19EB80721DB269EA7","title":"我的对对对v标题","duration":"","cover_url":"http://tanlanguiv2.oss-cn-beijing.aliyuncs.com/thumb/1f0c3476-b23f-4d8e-8465-a968d1016919.png","status":"1","media_type":"mp4","bitrate":"0.00","definition":"","encrypt":"1","play_url":"http://tlgshortmeeetspace.oss-cn-beijing.aliyuncs.com/vi/000C5A46E6B84C94ADBE6FB115451BEF","describe":"我的田娃","area":"北京","item_type":"2","create_time":"2017-10-27 22:30:09","is_show":"0","is_del":"0","is_adopt":"0","admin_id":"0","adopt_time":"0000-00-00 00:00:00"}]
         * type : 0
         */

        private VideoEntity video;
        private boolean has_next;
        private int next_page;

        public void setVideo(VideoEntity video) {
            this.video = video;
        }

        public void setHas_next(boolean has_next) {
            this.has_next = has_next;
        }

        public void setNext_page(int next_page) {
            this.next_page = next_page;
        }

        public VideoEntity getVideo() {
            return video;
        }

        public boolean isHas_next() {
            return has_next;
        }

        public int getNext_page() {
            return next_page;
        }

        public static class VideoEntity {
            private int type;
            /**
             * id : 32
             * account_id : 5
             * type : 1
             * activity_id : 1
             * tag : 活动
             * video_id : 59F342F19EB80721DB269EA7
             * title : 我的对对对v标题
             * duration :
             * cover_url : http://tanlanguiv2.oss-cn-beijing.aliyuncs.com/thumb/1f0c3476-b23f-4d8e-8465-a968d1016919.png
             * status : 1
             * media_type : mp4
             * bitrate : 0.00
             * definition :
             * encrypt : 1
             * play_url : http://tlgshortmeeetspace.oss-cn-beijing.aliyuncs.com/vi/000C5A46E6B84C94ADBE6FB115451BEF
             * describe : 我的田娃
             * area : 北京
             * item_type : 2
             * create_time : 2017-10-27 22:30:09
             * is_show : 0
             * is_del : 0
             * is_adopt : 0
             * admin_id : 0
             * adopt_time : 0000-00-00 00:00:00
             */

            private List<VideosEntity> videos;

            public void setType(int type) {
                this.type = type;
            }

            public void setVideos(List<VideosEntity> videos) {
                this.videos = videos;
            }

            public int getType() {
                return type;
            }

            public List<VideosEntity> getVideos() {
                return videos;
            }

            public static class VideosEntity {
                public static final int WORK = 0;
                public static final int DRAFT = 1;
                private String id;
                private String account_id;
                private String type;
                private String activity_id;
                private String tag;
                private String video_id;
                private String title;
                private String duration;
                private String cover_url;
                private String status;
                private String media_type;
                private String bitrate;
                private String definition;
                private String encrypt;
                private String play_url;
                private String describe;
                private String area;
                private String item_type;
                private String create_time;
                private String is_show;
                private String is_del;
                private String is_adopt;
                private String admin_id;
                private String adopt_time;

                public void setId(String id) {
                    this.id = id;
                }

                public void setAccount_id(String account_id) {
                    this.account_id = account_id;
                }

                public void setType(String type) {
                    this.type = type;
                }

                public void setActivity_id(String activity_id) {
                    this.activity_id = activity_id;
                }

                public void setTag(String tag) {
                    this.tag = tag;
                }

                public void setVideo_id(String video_id) {
                    this.video_id = video_id;
                }

                public void setTitle(String title) {
                    this.title = title;
                }

                public void setDuration(String duration) {
                    this.duration = duration;
                }

                public void setCover_url(String cover_url) {
                    this.cover_url = cover_url;
                }

                public void setStatus(String status) {
                    this.status = status;
                }

                public void setMedia_type(String media_type) {
                    this.media_type = media_type;
                }

                public void setBitrate(String bitrate) {
                    this.bitrate = bitrate;
                }

                public void setDefinition(String definition) {
                    this.definition = definition;
                }

                public void setEncrypt(String encrypt) {
                    this.encrypt = encrypt;
                }

                public void setPlay_url(String play_url) {
                    this.play_url = play_url;
                }

                public void setDescribe(String describe) {
                    this.describe = describe;
                }

                public void setArea(String area) {
                    this.area = area;
                }

                public void setItem_type(String item_type) {
                    this.item_type = item_type;
                }

                public void setCreate_time(String create_time) {
                    this.create_time = create_time;
                }

                public void setIs_show(String is_show) {
                    this.is_show = is_show;
                }

                public void setIs_del(String is_del) {
                    this.is_del = is_del;
                }

                public void setIs_adopt(String is_adopt) {
                    this.is_adopt = is_adopt;
                }

                public void setAdmin_id(String admin_id) {
                    this.admin_id = admin_id;
                }

                public void setAdopt_time(String adopt_time) {
                    this.adopt_time = adopt_time;
                }

                public String getId() {
                    return id;
                }

                public String getAccount_id() {
                    return account_id;
                }

                public String getType() {
                    return type;
                }

                public String getActivity_id() {
                    return activity_id;
                }

                public String getTag() {
                    return tag;
                }

                public String getVideo_id() {
                    return video_id;
                }

                public String getTitle() {
                    return title;
                }

                public String getDuration() {
                    return duration;
                }

                public String getCover_url() {
                    return cover_url;
                }

                public String getStatus() {
                    return status;
                }

                public String getMedia_type() {
                    return media_type;
                }

                public String getBitrate() {
                    return bitrate;
                }

                public String getDefinition() {
                    return definition;
                }

                public String getEncrypt() {
                    return encrypt;
                }

                public String getPlay_url() {
                    return play_url;
                }

                public String getDescribe() {
                    return describe;
                }

                public String getArea() {
                    return area;
                }

                public String getItem_type() {
                    return item_type;
                }

                public String getCreate_time() {
                    return create_time;
                }

                public String getIs_show() {
                    return is_show;
                }

                public String getIs_del() {
                    return is_del;
                }

                public String getIs_adopt() {
                    return is_adopt;
                }

                public String getAdmin_id() {
                    return admin_id;
                }

                public String getAdopt_time() {
                    return adopt_time;
                }
            }
        }
    }
}
