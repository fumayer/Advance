package com.quduquxie.base.widget.dialog;

/**
 * Created on 17/7/18.
 * Created by crazylei.
 */

public interface ShelfOptionDialogListener {

    void startWiFiTransportActivity();

    void startLocalFilesActivity();

    void startDownloadManagerActivity();

    void changeShelfDisplayMode();

    void cancelShelfOptionDialog();
}