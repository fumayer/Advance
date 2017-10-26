package com.quduquxie.base.bean;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created on 17/7/20.
 * Created by crazylei.
 */

public class MainContentItem implements Serializable {

    public int type;

    public String uri;

    public String title;

    public String editor;

    public Book book;

    public Direct direct;

    public Category category;

    public ArrayList<Book> bookList;

    public ArrayList<Direct> directs;

    public ArrayList<Banner> bannerList;

    public ArrayList<Category> categories;
}