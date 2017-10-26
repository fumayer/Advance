package com.quduquxie.model;

import com.quduquxie.base.bean.Book;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created on 17/2/20.
 * Created by crazylei.
 */

public class CommentItem implements Serializable {
    public int type;
    public int count;

    public Book book;
    public Comment comment;
    public ArrayList<Book> books;
}
