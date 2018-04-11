package com.example.sunjie.knowledgepointshareproject.abstract_factory;

import com.example.sunjie.knowledgepointshareproject.abstract_factory.camera.Camera;
import com.example.sunjie.knowledgepointshareproject.abstract_factory.screen.Screen;

/**
 * Created by sunjie on 2018/4/2.
 */

public interface PhoneAbstractFactory {
    Screen installScreen();
    Camera installCamera();
}
