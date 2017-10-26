package com.quduquxie.service.download;

/**
 * Created on 17/4/19.
 * Created by crazylei.
 */

public enum DownloadState {

    NO_START("NO_START"),
    DOWNLOADING("DOWNLOADING"),
    WAITING("WAITING"),
    PAUSED("PAUSED"),
    REFRESH("REFRESH"),
    FINISH("FINISH");

    private String state;

    DownloadState(String state) {
        this.state = state;
    }
}
