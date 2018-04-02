package com.example.sunjie.knowledgepointshareproject.abstract_factory;

import com.example.sunjie.knowledgepointshareproject.abstract_factory.camera.Camera;
import com.example.sunjie.knowledgepointshareproject.abstract_factory.camera.OneCamera;
import com.example.sunjie.knowledgepointshareproject.abstract_factory.screen.Screen;
import com.example.sunjie.knowledgepointshareproject.abstract_factory.screen.SmallScreen;

/**
 * Created by sunjie on 2018/4/2.
 */

public class XiaoMiPhone implements PhoneAbstractFactory {

    @Override
    public Screen installScreen() {
        return new SmallScreen();
    }

    @Override
    public Camera installCamera() {
        return new OneCamera();
    }
}
