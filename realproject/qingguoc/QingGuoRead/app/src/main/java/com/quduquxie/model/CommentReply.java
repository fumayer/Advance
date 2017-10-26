package com.quduquxie.model;

import java.io.Serializable;

/**
 * Created on 17/2/20.
 * Created by crazylei.
 */

public class CommentReply implements Serializable {
    public int sn;
    public CommentUser sender;
    public CommentUser receiver;
    public int status;
    public String content;
    public long create_time;

    public int item_type = 0;

    @Override
    public String toString() {
        return "CommentReply{" +
                "sn=" + sn +
                ", sender=" + sender +
                ", receiver=" + receiver +
                ", status=" + status +
                ", content='" + content + '\'' +
                ", create_time=" + create_time +
                '}';
    }
}
