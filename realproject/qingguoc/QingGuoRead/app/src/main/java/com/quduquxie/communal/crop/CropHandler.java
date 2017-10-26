package com.quduquxie.communal.crop;

import android.content.Intent;
import android.net.Uri;

public interface CropHandler {

    void onPhotoCropped(Uri uri);

    void onCompressed(Uri uri);

    void onCancel();

    void onFailed(String message);

    void handleIntent(Intent intent, int requestCode);

    CropParams getCropParams();
}
