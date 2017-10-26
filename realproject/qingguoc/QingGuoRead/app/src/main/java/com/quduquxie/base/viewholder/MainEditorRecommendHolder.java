package com.quduquxie.base.viewholder;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.quduquxie.R;
import com.quduquxie.base.module.main.activity.adapter.MainEditorRecommendAdapter;
import com.quduquxie.base.bean.Book;
import com.quduquxie.base.bean.MainContentItem;
import com.quduquxie.base.listener.BookListener;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created on 17/7/20.
 * Created by crazylei.
 */

public class MainEditorRecommendHolder extends RecyclerView.ViewHolder {

    @BindView(R.id.main_mode_head_detailed)
    public View main_mode_head_detailed;
    @BindView(R.id.main_mode_head_detailed_title)
    TextView main_mode_head_detailed_title;
    @BindView(R.id.main_mode_head_detailed_desc)
    TextView main_mode_head_detailed_desc;
    @BindView(R.id.main_mode_head_detailed_more)
    TextView main_mode_head_detailed_more;
    @BindView(R.id.main_editor_recommend)
    RecyclerView main_editor_recommend;

    private ArrayList<Book> books = new ArrayList<>();

    private LinearLayoutManager linearLayoutManager;
    private MainEditorRecommendAdapter mainEditorRecommendAdapter;

    public MainEditorRecommendHolder(View view) {
        super(view);
        ButterKnife.bind(this, view);
    }

    public void initializeView(Context context, MainContentItem mainContentItem, BookListener bookListener) {
        if (mainContentItem != null && mainContentItem.bookList != null && mainContentItem.bookList.size() > 0) {

            main_mode_head_detailed_title.setText(TextUtils.isEmpty(mainContentItem.title) ? "主编今日推荐" : mainContentItem.title);

            main_mode_head_detailed_desc.setText(TextUtils.isEmpty(mainContentItem.editor) ? "主编说：今天给大家准备了很多书哦～" : mainContentItem.editor);

            main_mode_head_detailed_more.setVisibility(TextUtils.isEmpty(mainContentItem.uri) ? View.GONE : View.VISIBLE);

            if (books == null) {
                books = new ArrayList<>();
            } else {
                books.clear();
            }

            for (Book book : mainContentItem.bookList) {
                books.add(book);
            }

            if (!books.isEmpty()) {

                linearLayoutManager = new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false);

                mainEditorRecommendAdapter = new MainEditorRecommendAdapter(context, books, bookListener);

                main_editor_recommend.setAdapter(mainEditorRecommendAdapter);
                main_editor_recommend.setLayoutManager(linearLayoutManager);
            }
        }
    }

    public void recycle() {

        if (books != null) {
            books.clear();
        }

        if (mainEditorRecommendAdapter != null) {
            mainEditorRecommendAdapter.recycle();
        }
    }
}