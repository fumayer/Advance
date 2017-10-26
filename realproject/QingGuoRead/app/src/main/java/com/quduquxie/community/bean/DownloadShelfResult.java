package com.quduquxie.community.bean;

import com.quduquxie.model.BookShelf;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created on 17/7/28.
 * Created by crazylei.
 */

public class DownloadShelfResult implements Serializable {
    public int count;
    public ArrayList<BookShelf> shelfs;
}