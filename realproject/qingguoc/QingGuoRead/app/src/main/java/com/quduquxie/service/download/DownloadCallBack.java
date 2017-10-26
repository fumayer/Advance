package com.quduquxie.service.download;

/**
 * Created on 17/4/19.
 * Created by crazylei.
 */

public interface DownloadCallBack {

    void onTaskStart(String id);

    void onChapterDownFinish(String id, int sequence);

    void onChapterDownFailed(String id, int sequence);

    void setDownloadProgress(int progress);

    void onTaskFinish(String id);

    void onOffLineFinish();

    void refreshUI();
}
