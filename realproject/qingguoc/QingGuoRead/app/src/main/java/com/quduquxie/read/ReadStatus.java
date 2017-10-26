package com.quduquxie.read;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.view.ViewGroup;

import com.quduquxie.base.bean.Book;

import java.util.ArrayList;

public class ReadStatus {

    public Book book;
    public String book_id;
    public String book_name;
    public String book_author;
    public String image_book;
    public String book_source;

    public int offset;
    public int sequence;
    public int page_count;

    public int current_page;
    public int chapter_count;
    public String chapter_name;

    public int screen_width;
    public int screen_height;
    public float screen_density;
    public float screen_scaled_density;

    public boolean loading;
    public boolean menu_show;
    public int novel_progress;

    public boolean draw_foot_view;
    public ViewGroup novel_basePageView;

    public ArrayList<ArrayList<String>> line_list;
    public ArrayList<String> book_name_list;
    public ArrayList<String> chapter_name_list;

    private SharedPreferences sharedPreferences;

    private int auto_read_speed;
    private static final int default_auto_read_speed = 16;
    private static final String key_auto_read_speed = "auto_read_speed";

    public int comment_count = 0;

    public ReadStatus(Context context) {
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        auto_read_speed = sharedPreferences.getInt(key_auto_read_speed, default_auto_read_speed);
    }
}
