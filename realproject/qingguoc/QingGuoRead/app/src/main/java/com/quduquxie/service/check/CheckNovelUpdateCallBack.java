package com.quduquxie.service.check;

import com.quduquxie.model.v2.CheckUpdateResult;

public interface CheckNovelUpdateCallBack {
    void onSuccess(CheckUpdateResult checkUpdateResult);

    void onException(Exception exception);
}
