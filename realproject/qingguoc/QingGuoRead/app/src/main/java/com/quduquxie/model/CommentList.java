package com.quduquxie.model;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created on 17/2/20.
 * Created by crazylei.
 */

public class CommentList implements Serializable {
    public int count;
    public ArrayList<Comment> comments;
    public ArrayList<Comment> hot_comments;
}
