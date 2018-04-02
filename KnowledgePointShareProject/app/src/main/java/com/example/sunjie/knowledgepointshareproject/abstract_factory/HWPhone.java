package com.example.sunjie.knowledgepointshareproject.abstract_factory;

import com.example.sunjie.knowledgepointshareproject.abstract_factory.camera.Camera;
import com.example.sunjie.knowledgepointshareproject.abstract_factory.camera.DoubleCamera;
import com.example.sunjie.knowledgepointshareproject.abstract_factory.screen.BigScreen;
import com.example.sunjie.knowledgepointshareproject.abstract_factory.screen.Screen;

/**
 * Created by sunjie on 2018/4/2.
 */

public class HWPhone implements PhoneAbstractFactory {
    @Override
    public Screen installScreen() {
        return new BigScreen();
    }

    @Override
    public Camera installCamera() {
        return new DoubleCamera();
    }
}
