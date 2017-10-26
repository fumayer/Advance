package com.quduquxie.wifi.view;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.baidu.mobstat.StatService;
import com.quduquxie.Constants;
import com.quduquxie.R;
import com.quduquxie.base.util.TypefaceUtil;
import com.quduquxie.util.StatServiceUtils;
import com.quduquxie.view.BaseActivity;
import com.quduquxie.wifi.WiFiTransportInterface;
import com.quduquxie.wifi.presenter.WiFiTransportPresenter;

import java.net.InetAddress;
import java.text.MessageFormat;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class WiFiTransportActivity extends BaseActivity implements WiFiTransportInterface.View {

    private WiFiTransportPresenter wifiTransportPresenter;

    @BindView(R.id.common_head_back)
    public ImageView common_head_back;
    @BindView(R.id.common_head_title)
    public TextView common_head_title;
    @BindView(R.id.wifi_transport_prompt)
    public TextView wifi_transport_prompt;
    @BindView(R.id.wifi_transport_address)
    public TextView wifi_transport_address;
    @BindView(R.id.wifi_transport_name)
    public TextView wifi_transport_name;
    @BindView(R.id.wifi_transport_message)
    public TextView wifi_transport_message;
    @BindView(R.id.wifi_transport_result_text)
    public TextView wifi_transport_result_text;

    public static final int TRANSPORT_SUCCESS = 0x51;
    public static final int TRANSPORT_ERROR = 0x52;

    private Typeface typeface_song_depict;

    public UIHandler uiHandler = new UIHandler();

    class UIHandler extends Handler {
        @Override
        public void handleMessage(Message message) {
            super.handleMessage(message);
            String name = (String) message.obj;
            switch (message.what) {
                case TRANSPORT_SUCCESS:
                    StatServiceUtils.statWiFiTransport(WiFiTransportActivity.this, name);
                    showTransportSuccessView(name);
                    break;
                case TRANSPORT_ERROR:
                    showTransportErrorView(name);
                    break;
            }
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {
            setContentView(R.layout.layout_wifi_transport);
        } catch (Resources.NotFoundException exception) {
            exception.printStackTrace();
        }

        ButterKnife.bind(this);

        typeface_song_depict = TypefaceUtil.loadTypeface(this, TypefaceUtil.TYPEFACE_SONG_DEPICT);

        initView();

        wifiTransportPresenter = new WiFiTransportPresenter(this, this, uiHandler);
        wifiTransportPresenter.init();
    }

    private void initView() {
        if (common_head_title != null) {
            common_head_title.setText("WI-FI传书");
            common_head_title.setTypeface(typeface_song_depict);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        StatService.onResume(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        StatService.onPause(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        this.wifiTransportPresenter.recyclerData();
    }

    @Override
    public void setPresenter(WiFiTransportInterface.Presenter wifiTransportPresenter) {
        this.wifiTransportPresenter = (WiFiTransportPresenter) wifiTransportPresenter;
    }

    @Override
    public Context getContext() {
        return WiFiTransportActivity.this;
    }

    @Override
    public void showWiFiInformation(String name) {
        if (wifi_transport_name != null) {
            wifi_transport_name.setText(MessageFormat.format("已连接WiFi名称为{0}", name));
            wifi_transport_name.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void hideWiFiInformation() {
        if (wifi_transport_name != null) {
            wifi_transport_name.setVisibility(View.GONE);
        }
    }

    @Override
    public void showWiFiStatusError() {
        if (wifi_transport_address != null) {
            wifi_transport_address.setText(R.string.wifi_transport_address_empty);
        }
    }

    @Override
    public void showWiFiStatusSuccess(InetAddress inetAddress) {
        if (wifi_transport_address != null) {
            wifi_transport_address.setText("http://" + inetAddress.getHostAddress() + ":" +  Constants.wifi_transport_port + "/");
        }
    }

    @OnClick({R.id.common_head_back})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.common_head_back:
                finish();
                break;
        }
    }

    public void showTransportSuccessView(String result) {
        if (wifi_transport_result_text.getVisibility() != View.VISIBLE) {
            wifi_transport_result_text.setVisibility(View.VISIBLE);
        }
        if (result.length() > 10) {
            result = result.substring(0, 10) + "...";
        }
        wifi_transport_result_text.setTextColor(Color.parseColor("#4D91D0"));
        wifi_transport_result_text.setText("《" + result + "》" + "接收成功！");
    }

    public void showTransportErrorView(String result) {
        if (wifi_transport_result_text.getVisibility() != View.VISIBLE) {
            wifi_transport_result_text.setVisibility(View.VISIBLE);
        }
        if (result.length() > 10) {
            result = result.substring(0, 10) + "...";
        }
        wifi_transport_result_text.setTextColor(Color.parseColor("#F44336"));
        wifi_transport_result_text.setText("《" + result + "》" + "接收失败！");
    }
}
