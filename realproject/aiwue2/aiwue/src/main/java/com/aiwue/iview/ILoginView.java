package com.aiwue.iview;

import com.aiwue.model.User;

/** 登录View 接口
 * Created by Yibao on 2017年4月13日09:57:53
 * Copyright (c) 2017 aiwue.com All rights reserved
 */
public interface ILoginView {
    void onLoginSuccess(Boolean success, String err, User response);
    void onSendVCodeSuccess(Boolean success, String err, Integer verifyId);

}
