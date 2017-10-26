package com.quduquxie.base.module.main.fragment.content.util;

import android.text.TextUtils;

import com.quduquxie.base.module.main.activity.adapter.MainContentAdapter;
import com.quduquxie.base.bean.Book;
import com.quduquxie.base.bean.Direct;
import com.quduquxie.base.bean.MainContent;
import com.quduquxie.base.bean.MainContentInformation;
import com.quduquxie.base.bean.MainContentItem;
import com.quduquxie.base.module.main.fragment.content.view.ContentFragment;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created on 17/7/20.
 * Created by crazylei.
 */

public class ContentUtil {

    private static HashMap<String, Integer> titleMap = new HashMap<String, Integer>() {
        {
            put("banner", 0x80);
            put("主编今日推荐", 0x81);
            put("大神推荐", 0x82);
            put("4大分类", 0x83);
            put("跳转选项", 0x84);
            put("本周TOP", 0x85);
            put("大家在读", 0x86);
            put("编辑在读", 0x86);
            put("新书推荐", 0x86);
            put("编辑推荐", 0x86);
            put("今日精选推荐", 0x87);
            put("今日热评", 0x87);
            put("今日更新最多", 0x87);
            put("豪门精选", 0x87);
            put("穿越精选", 0x87);
            put("玄幻精选", 0x87);
            put("都市精选", 0x87);
            put("分类", 0x88);
        }
    };

    public static ArrayList<MainContentItem> encapsulationMainContent(MainContent mainContent, int type) {

        ArrayList<MainContentItem> mainContentItems = new ArrayList<>();

        MainContentItem mainContentItem;

        for (MainContentInformation mainContentInformation : mainContent.data) {

            if (mainContentInformation != null && !TextUtils.isEmpty(mainContentInformation.name) && titleMap.containsKey(mainContentInformation.name)) {
                int key = titleMap.get(mainContentInformation.name);

                switch (key) {
                    case 0x80:
                        if (mainContentInformation.banner_data != null && mainContentInformation.banner_data.size() > 0) {
                            mainContentItem = new MainContentItem();
                            mainContentItem.type = MainContentAdapter.TYPE_BANNER;
                            mainContentItem.title = mainContentInformation.name;
                            mainContentItem.bannerList = mainContentInformation.banner_data;

                            mainContentItems.add(mainContentItem);
                        }
                        break;
                    case 0x81:
                        if (mainContentInformation.books_data != null && mainContentInformation.books_data.size() > 0) {
                            mainContentItem = new MainContentItem();
                            mainContentItem.type = MainContentAdapter.TYPE_EDITOR_RECOMMEND;
                            mainContentItem.uri = mainContentInformation.more_uri;
                            mainContentItem.title = mainContentInformation.name;
                            mainContentItem.editor = mainContentInformation.editor;
                            mainContentItem.bookList = mainContentInformation.books_data;

                            mainContentItems.add(mainContentItem);

                            mainContentItems.add(createMainContentItem(MainContentAdapter.TYPE_FILLER_SHADOW));
                        }
                        break;
                    case 0x82:
                        if (mainContentInformation.books_data != null && mainContentInformation.books_data.size() > 0) {
                            mainContentItem = new MainContentItem();
                            mainContentItem.type = MainContentAdapter.TYPE_AUTHOR_RECOMMEND;
                            mainContentItem.title = mainContentInformation.name;
                            mainContentItem.editor = mainContentInformation.editor;
                            mainContentItem.bookList = mainContentInformation.books_data;

                            mainContentItems.add(mainContentItem);

                            mainContentItems.add(createMainContentItem(MainContentAdapter.TYPE_FILLER_SHADOW));
                        }
                        break;
                    case 0x83:
                        if (mainContentInformation.category_data != null && mainContentInformation.category_data.size() > 0) {
                            mainContentItem = new MainContentItem();
                            mainContentItem.type = MainContentAdapter.TYPE_SELECTED_CATEGORY;
                            mainContentItem.title = mainContentInformation.name;
                            mainContentItem.categories = mainContentInformation.category_data;

                            mainContentItems.add(mainContentItem);

                            mainContentItems.add(createMainContentItem(MainContentAdapter.TYPE_FILLER_SHADOW));
                        }
                        break;
                    case 0x84:

                        if (mainContentInformation.direct_data != null && mainContentInformation.direct_data.size() > 0) {
                            if (type == ContentFragment.TYPE_SELECTED) {
                                mainContentItem = new MainContentItem();
                                mainContentItem.type = MainContentAdapter.TYPE_SELECTED_JUMP;
                                mainContentItem.title = mainContentInformation.name;
                                mainContentItem.directs = mainContentInformation.direct_data;

                                mainContentItems.add(mainContentItem);

                                mainContentItems.add(createMainContentItem(MainContentAdapter.TYPE_FILLER_SHADOW));
                            } else if (type == ContentFragment.TYPE_LIBRARY) {

                                for (Direct direct : mainContentInformation.direct_data) {
                                    mainContentItem = new MainContentItem();
                                    mainContentItem.type = MainContentAdapter.TYPE_LIBRARY_JUMP;
                                    mainContentItem.title = mainContentInformation.name;
                                    mainContentItem.direct = direct;
                                    mainContentItems.add(mainContentItem);
                                }
                            }
                        }

                        break;
                    case 0x85:
                        if (mainContentInformation.books_data != null && mainContentInformation.books_data.size() > 0) {

                            mainContentItems.add(createMainContentItem(MainContentAdapter.TYPE_MODULE_HEAD, mainContentInformation.name, mainContentInformation.more_uri));

                            mainContentItem = new MainContentItem();
                            mainContentItem.uri = mainContentInformation.more_uri;
                            mainContentItem.type = MainContentAdapter.TYPE_CHANNEL_TOP;
                            mainContentItem.title = mainContentInformation.name;
                            mainContentItem.bookList = mainContentInformation.books_data;

                            mainContentItems.add(mainContentItem);

                            mainContentItems.add(createMainContentItem(MainContentAdapter.TYPE_PROMPT_SEARCH));
                        }
                        break;
                    case 0x86:
                        if (mainContentInformation.books_data != null && mainContentInformation.books_data.size() > 0) {

                            int count = 0;
                            for (Book book : mainContentInformation.books_data) {
                                count += book.follow_count;
                            }

                            mainContentItem = new MainContentItem();
                            mainContentItem.type = MainContentAdapter.TYPE_HORIZONTAL_LIST;
                            mainContentItem.uri = mainContentInformation.more_uri;
                            mainContentItem.title = mainContentInformation.name;
                            mainContentItem.bookList = mainContentInformation.books_data;

                            if ("大家在读".equals(mainContentInformation.name)) {
                                mainContentItem.editor = "今日有" + count + "书友一起读书";
                            } else if ("编辑在读".equals(mainContentInformation.name)) {
                                mainContentItem.editor = "编辑陪你一起看";
                            } else if ("新书推荐".equals(mainContentInformation.name)) {
                                mainContentItem.editor = "发现最佳新秀作品";
                            } else {
                                mainContentItem.editor = mainContentInformation.editor;
                            }

                            mainContentItems.add(mainContentItem);

                            if ("大家在读".equals(mainContentInformation.name) || "编辑在读".equals(mainContentInformation.name)) {
                                mainContentItems.add(createMainContentItem(MainContentAdapter.TYPE_FILLER_DIVIDER));
                            } else {
                                mainContentItems.add(createMainContentItem(MainContentAdapter.TYPE_FILLER_SHADOW));
                            }
                        }
                        break;
                    case 0x87:
                        if (mainContentInformation.books_data != null && mainContentInformation.books_data.size() > 0) {

                            int size = mainContentInformation.books_data.size();

                            mainContentItems.add(createMainContentItem(MainContentAdapter.TYPE_MODULE_HEAD, mainContentInformation.name, mainContentInformation.more_uri));

                            for (int i = 0; i < mainContentInformation.books_data.size(); i++) {
                                Book book = mainContentInformation.books_data.get(i);
                                mainContentItems.add(createMainContentItem(MainContentAdapter.TYPE_BOOK_VERTICAL, book));

                                if (i == size - 1) {

                                    if ("今日更新最多".equals(mainContentInformation.name)) {
                                        mainContentItems.add(createMainContentItem(MainContentAdapter.TYPE_PROMPT_SEARCH));
                                    } else {
                                        mainContentItems.add(createMainContentItem(MainContentAdapter.TYPE_FILLER_SHADOW));
                                    }
                                }
                            }

                        }
                        break;
                    case 0x88:
                        if (mainContentInformation.category_data != null && mainContentInformation.category_data.size() > 0) {
                            for (int i = 0; i < mainContentInformation.category_data.size(); i++) {
                                mainContentItem = new MainContentItem();
                                mainContentItem.title = mainContent.name;
                                mainContentItem.category = mainContentInformation.category_data.get(i);

                                if (i < 3) {
                                    mainContentItem.type = MainContentAdapter.TYPE_LIBRARY_CATEGORY_HOT;
                                } else {
                                    mainContentItem.type = MainContentAdapter.TYPE_LIBRARY_CATEGORY;
                                }

                                mainContentItems.add(mainContentItem);
                            }
                        }
                        break;
                }
            }
        }

        return mainContentItems;
    }

    private static MainContentItem createMainContentItem(int type) {
        MainContentItem mainContentItem = new MainContentItem();
        mainContentItem.type = type;
        return mainContentItem;
    }

    private static MainContentItem createMainContentItem(int type, Book book) {
        MainContentItem mainContentItem = new MainContentItem();
        mainContentItem.type = type;
        mainContentItem.book = book;
        return mainContentItem;
    }

    private static MainContentItem createMainContentItem(int type, String title, String uri) {
        MainContentItem mainContentItem = new MainContentItem();
        mainContentItem.uri = uri;
        mainContentItem.type = type;
        mainContentItem.title = title;
        return mainContentItem;
    }
}