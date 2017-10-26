package com.quduquxie.base.module.main.fragment.mine;

import com.quduquxie.base.BasePresenter;
import com.quduquxie.base.BaseView;
import com.quduquxie.model.User;

/**
 * Created on 17/7/17.
 * Created by crazylei.
 */

public interface MineInterface {

    interface Presenter extends BasePresenter {

        void refreshUserInformation();
    }

    interface View extends BaseView {
        void initUserInformation(User user);

        void initCommentCountView(int comment_count, int reply_count);

        void showToastMessage(String message);

        void showProgressDialog();

        void hideProgressDialog();

        void startLoginActivity();

    }
}