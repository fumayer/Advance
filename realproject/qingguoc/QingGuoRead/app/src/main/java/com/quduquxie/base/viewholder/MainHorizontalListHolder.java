package com.quduquxie.base.viewholder;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.quduquxie.R;
import com.quduquxie.base.adapter.HorizontalListAdapter;
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

public class MainHorizontalListHolder extends RecyclerView.ViewHolder {

    @BindView(R.id.main_mode_head)
    public View main_mode_head;
    @BindView(R.id.main_mode_head_title)
    TextView main_mode_head_title;
    @BindView(R.id.main_mode_head_more)
    TextView main_mode_head_more;

    @BindView(R.id.main_mode_head_detailed)
    public View main_mode_head_detailed;
    @BindView(R.id.main_mode_head_detailed_title)
    TextView main_mode_head_detailed_title;
    @BindView(R.id.main_mode_head_detailed_desc)
    TextView main_mode_head_detailed_desc;
    @BindView(R.id.main_mode_head_detailed_more)
    TextView main_mode_head_detailed_more;

    @BindView(R.id.main_horizontal_list)
    RecyclerView main_horizontal_list;

    private ArrayList<Book> books = new ArrayList<>();

    private LinearLayoutManager linearLayoutManager;
    private HorizontalListAdapter horizontalListAdapter;

    public MainHorizontalListHolder(View view) {
        super(view);
        ButterKnife.bind(this, view);
    }

    public void initializeView(Context context, MainContentItem mainContentItem, BookListener bookListener) {
        if (mainContentItem != null && !TextUtils.isEmpty(mainContentItem.title) && mainContentItem.bookList != null && mainContentItem.bookList.size() > 0) {
            checkTitleState(mainContentItem);

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

                horizontalListAdapter = new HorizontalListAdapter(context, books, bookListener);

                main_horizontal_list.setAdapter(horizontalListAdapter);
                main_horizontal_list.setLayoutManager(linearLayoutManager);
            }
        }
    }

    private void checkTitleState(MainContentItem mainContentItem) {

        String title = mainContentItem.title;

        if ("大家在读".equals(title) || "编辑在读".equals(title) || "新书推荐".equals(title)) {

            main_mode_head.setVisibility(View.GONE);
            main_mode_head_detailed.setVisibility(View.VISIBLE);

            initializeHeadDetailedView(mainContentItem);
        } else {

            main_mode_head.setVisibility(View.VISIBLE);
            main_mode_head_detailed.setVisibility(View.GONE);

            initializeHeadView(mainContentItem);
        }
    }

    private void initializeHeadView(MainContentItem mainContentItem) {

        main_mode_head.setTag(R.id.click_object, mainContentItem);

        main_mode_head_title.setText(TextUtils.isEmpty(mainContentItem.title) ? "精选推荐" : mainContentItem.title);

        if (!TextUtils.isEmpty(mainContentItem.uri)) {
            main_mode_head_more.setVisibility(View.VISIBLE);
        } else {
            main_mode_head_title.setVisibility(View.GONE);
        }
    }

    private void initializeHeadDetailedView(MainContentItem mainContentItem) {

        main_mode_head_detailed.setTag(R.id.click_object, mainContentItem);

        main_mode_head_detailed_title.setText(TextUtils.isEmpty(mainContentItem.title) ? "精选推荐" : mainContentItem.title);

        main_mode_head_detailed_desc.setText(TextUtils.isEmpty(mainContentItem.editor) ? "编辑精选的推荐哦~" : mainContentItem.editor);

        if (!TextUtils.isEmpty(mainContentItem.uri)) {
            main_mode_head_detailed_more.setVisibility(View.VISIBLE);
        } else {
            main_mode_head_detailed_more.setVisibility(View.GONE);
        }
    }

    public void recycle() {

    }
}