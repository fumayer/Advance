package com.quduquxie.revise;

import com.quduquxie.BasePresenter;
import com.quduquxie.BaseView;

/**
 * Created on 16/12/5.
 * Created by crazylei.
 */

public interface ModifyQQInterface {

    interface Presenter extends BasePresenter {


    }

    interface View extends BaseView<Presenter> {

        void showToast(String message);

    }
}