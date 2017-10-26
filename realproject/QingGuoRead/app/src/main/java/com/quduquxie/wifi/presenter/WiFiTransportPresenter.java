package com.quduquxie.wifi.presenter;

import android.content.Context;
import android.net.wifi.WifiManager;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.text.TextUtils;

import com.quduquxie.Constants;
import com.quduquxie.wifi.WiFiTransportInterface;
import com.quduquxie.wifi.service.WiFiTransportWebService;

import java.io.IOException;
import java.lang.ref.WeakReference;
import java.net.InetAddress;
import java.net.UnknownHostException;

public class WiFiTransportPresenter implements WiFiTransportInterface.Presenter {

    private static final String TAG = WiFiTransportPresenter.class.getSimpleName();

    private WiFiTransportInterface.View wifiTransportView;
    private WeakReference<Context> contextReference;
    private WiFiTransportWebService wiFiTransportWebService;

    private Handler handler;

    public WiFiTransportPresenter(@NonNull WiFiTransportInterface.View wifiTransportView, Context context, Handler handler) {
        this.wifiTransportView = wifiTransportView;
        this.wifiTransportView.setPresenter(this);
        this.contextReference = new WeakReference<>(context);
        this.handler = handler;
    }


    @Override
    public void init() {
        Context context = contextReference.get();
        InetAddress inetAddress;

        if (context == null) {
            context = wifiTransportView.getContext();
        }

        WifiManager wifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
        if (!wifiManager.isWifiEnabled()) {
            wifiTransportView.showWiFiStatusError();
        }

        int ipAddress = wifiManager.getConnectionInfo().getIpAddress();
        String wifiName = wifiManager.getConnectionInfo().getSSID();
        if (!TextUtils.isEmpty(wifiName)) {
            wifiTransportView.showWiFiInformation(wifiName);
        } else {
            wifiTransportView.hideWiFiInformation();
        }

        if (ipAddress == 0) {
            inetAddress = null;
        } else {
            inetAddress = initIPAddress(ipAddress);
        }

        if (inetAddress != null) {
            wifiTransportView.showWiFiStatusSuccess(inetAddress);
            try {
                if (wiFiTransportWebService == null) {
                    wiFiTransportWebService = new WiFiTransportWebService(inetAddress.getHostAddress(), Constants.wifi_transport_port, context, handler);
                    WiFiTransportWebService.assetManager = context.getAssets();
                    wiFiTransportWebService.start();
                } else {
                    wiFiTransportWebService.start();
                }

            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            wifiTransportView.showWiFiStatusError();
        }
    }

    public static InetAddress initIPAddress(int value) {
        byte[] bytes = new byte[4];
        for (int i = 0; i < 4; i++) {
            bytes[i] = fetchByteOfInt(value, i);
        }
        try {
            return InetAddress.getByAddress(bytes);
        } catch (UnknownHostException e) {
            return null;
        }
    }

    public static byte fetchByteOfInt(int value, int which) {
        int shift = which * 8;
        return (byte) (value >> shift);
    }

    @Override
    public void recyclerData() {
        if (wiFiTransportWebService != null) {
            wiFiTransportWebService.stop();
        }
    }
}
