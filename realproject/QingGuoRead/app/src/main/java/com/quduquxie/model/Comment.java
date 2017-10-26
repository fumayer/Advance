package com.quduquxie.model;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created on 17/2/20.
 * Created by crazylei.
 */

public class Comment implements Serializable {
    public String id;
    public String content;
    public int sn;
    public int status;
    public int is_author;
    public long create_time;
    public int like_count;
    public CommentUser sender;
    public ArrayList<CommentReply> replies;

    @Override
    public boolean equals(Object o) {
        if (o == this) {
            return true;
        } else if (!(o instanceof Comment)) {
            return false;
        } else {
            Comment comment = (Comment) o;
            return comment.id.equals(this.id);
        }
    }
}
