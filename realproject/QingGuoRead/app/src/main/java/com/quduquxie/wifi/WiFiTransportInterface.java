package com.quduquxie.wifi;

import android.content.Context;

import com.quduquxie.BasePresenter;
import com.quduquxie.BaseView;

import java.net.InetAddress;

public interface WiFiTransportInterface {

    interface Presenter extends BasePresenter {

        void recyclerData();
    }

    interface View extends BaseView<Presenter> {

        Context getContext();

        void showWiFiInformation(String name);

        void hideWiFiInformation();

        void showWiFiStatusError();

        void showWiFiStatusSuccess(InetAddress inetAddress);

    }
}
