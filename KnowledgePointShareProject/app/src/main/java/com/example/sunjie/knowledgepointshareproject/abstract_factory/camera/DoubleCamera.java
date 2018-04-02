package com.example.sunjie.knowledgepointshareproject.abstract_factory.camera;

import com.example.sunjie.knowledgepointshareproject.LogUtil;

/**
 * Created by sunjie on 2018/4/2.
 */

public class DoubleCamera implements Camera {
    @Override
    public void takePhoto() {
        LogUtil.e("DoubleCamera","10-----takePhoto--->双摄像头拍照超清");
    }
}
