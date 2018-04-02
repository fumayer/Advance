package com.example.sunjie.knowledgepointshareproject.abstract_factory.camera;

import com.example.sunjie.knowledgepointshareproject.LogUtil;

/**
 * Created by sunjie on 2018/4/2.
 */

public class OneCamera implements Camera {
    @Override
    public void takePhoto() {
        LogUtil.e("OneCamera","10-----takePhoto--->单摄像头拍照还行");
    }
}
