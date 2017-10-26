package com.quduquxie.base.listener;

import com.quduquxie.base.bean.Bookmark;

/**
 * Created on 17/7/28.
 * Created by crazylei.
 */

public interface BookmarkListener {

    void onClickedBookmark(Bookmark bookmark);

    void onLongClickedBookmark(Bookmark bookmark, int position);
}