package com.quduquxie.base.bean;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created on 17/7/20.
 * Created by crazylei.
 */

public class MainContentInformation implements Serializable {

    public int index;
    public String name;
    public String more_uri;
    public String editor;
    public ArrayList<Book> books_data;
    public ArrayList<Category> category_data;
    public ArrayList<Direct> direct_data;
    public ArrayList<Banner> banner_data;
}