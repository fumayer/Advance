package com.aiwue.iview;

import com.aiwue.model.Note;

import java.util.List;

/**
 *  主页中的笔记列表接口
 * Created by Yibao on 2017年4月12日14:36:55
 * Copyright (c) 2017 aiwue.com All rights reserved
 */
public interface INoteListView {
    void onGetNoteListSuccess(Boolean success, String err, List<Note> response);

}
