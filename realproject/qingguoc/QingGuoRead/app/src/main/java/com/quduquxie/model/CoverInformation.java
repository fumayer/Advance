package com.quduquxie.model;

import com.quduquxie.base.bean.Book;

import java.io.Serializable;

/**
 * Created on 17/2/20.
 * Created by crazylei.
 */

public class CoverInformation implements Serializable {
    public int type;
    public Book book;
    public Comment comment;
    public int comment_count;
}
