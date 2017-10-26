package com.quduquxie.module.comment;

import com.quduquxie.BasePresenter;
import com.quduquxie.BaseView;
import com.quduquxie.model.Review;

/**
 * Created on 17/3/3.
 * Created by crazylei.
 */

public interface CommentPostInterface {

    interface Presenter extends BasePresenter {

        void initParameter();

        void sentCommentReply(Review review, String content);
    }

    interface View extends BaseView<Presenter> {

        void initView();

        void showToast(String message);

        void finishActivity();

    }
}
