package com.quduquxie.model;

import java.io.Serializable;

/**
 * Created on 17/2/28.
 * Created by crazylei.
 */

public class Review implements Serializable {
    public String id;
    public int type;
    public long create_time;
    public CommentUser sender;
    public CommentUser receiver;
    public Fiction book;
    public Comment comments;
    public CommentReply replies;
}