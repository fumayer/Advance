package com.quduquxie.communal.utils;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.net.http.SslError;
import android.os.Build;
import android.preference.PreferenceManager;
import android.text.TextUtils;
import android.view.View;
import android.webkit.CookieManager;
import android.webkit.CookieSyncManager;
import android.webkit.SslErrorHandler;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.orhanobut.logger.Logger;
import com.quduquxie.base.util.NetworkUtil;

/**
 * Created on 17/1/10.
 * Created by crazylei.
 */

public class CustomWebClient extends WebViewClient {

    private Context context;
    private WebView webView;

    private ErrorCallBack errorCallBack;
    private StartedCallBack startedCallBack;
    private FinishedCallBack finishedCallBack;
    private OverrideCallback overrideCallback;

    private WebSettings webSettings;

    private int load_error_code = 0;

    private int page_load_start = 0;

    private String host;

    private String cookie;

    private String userAgent;

    private CookieManager cookieManager;
    private SharedPreferences sharedPreferences;

    public CustomWebClient(Context context, WebView webView, String host) {
        super();
        this.host = host;
        this.context = context;
        this.webView = webView;

        this.sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);

        CookieSyncManager.createInstance(context);
        cookieManager = CookieManager.getInstance();
        cookieManager.setAcceptCookie(true);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            cookieManager.setAcceptThirdPartyCookies(webView, true);
        }
    }

    @Override
    public boolean shouldOverrideUrlLoading(WebView webView, String url) {
        Logger.d("ShouldOverrideUrlLoading: " + url);
        return super.shouldOverrideUrlLoading(webView, url);
    }

    @Override
    public void onPageStarted(WebView webView, String url, Bitmap bitmap) {
        Logger.d("onPageStarted: " + url);
        insertCookie(url);
        if (overrideCallback != null) {
            overrideCallback.onOverride(url);
        }
        page_load_start++;
        if (startedCallBack != null) {
            startedCallBack.onLoadStarted(url);
        }
        if (webView != null && page_load_start == 1) {
            webView.setVisibility(View.GONE);
        } else if (load_error_code != 0 && errorCallBack != null) {
            if (webView != null) {
                webView.stopLoading();
                webView.clearView();
                webView.setVisibility(View.GONE);
            }
            errorCallBack.onErrorReceived();
        }
        super.onPageStarted(webView, url, bitmap);
    }

    @Override
    public void onPageFinished(WebView webView, String url) {
        Logger.d("onPageFinished: " + url);
        //保存新的cookie
        saveCookie(url);
        if (load_error_code == 0 && finishedCallBack != null) {
            finishedCallBack.onLoadFinished();
            if (webView != null) {
                webView.setVisibility(View.VISIBLE);
            }
            if (webSettings != null) {
                webSettings.setBlockNetworkImage(false);
            }
        } else if (load_error_code != 0 && errorCallBack != null) {
            if (webView != null) {
                webView.clearView();
                webView.stopLoading();
                webView.setVisibility(View.GONE);
            }
            errorCallBack.onErrorReceived();
        }
        super.onPageFinished(webView, url);
    }

    private void saveCookie(String url) {
        cookie = cookieManager.getCookie(url);
        sharedPreferences.edit().putString(host, cookie).apply();
        Logger.d("保存Cookie: " + url + "  ::  " + cookie);
    }

    private void insertCookie(String url) {
        if (!TextUtils.isEmpty(cookie)) {
            cookieManager.setCookie(url, cookie);
            CookieSyncManager.createInstance(context).sync();
            Logger.d("插入Cookie: " + url + "  ::  " + cookie);
        } else {
            if (!TextUtils.isEmpty(host)) {
                cookie = sharedPreferences.getString(host, "");
                if (!TextUtils.isEmpty(cookie)) {
                    cookieManager.setCookie(url, cookie);
                    CookieSyncManager.createInstance(context).sync();
                    Logger.d("插入本地存储Cookie: " + url + "  ::  " + cookie);
                }
            }
        }
    }

    @Override
    public void onReceivedError(WebView webView, int errorCode, String description, String failingUrl) {
        load_error_code = errorCode;
        if (webView != null) {
            webView.clearView();
            webView.stopLoading();
        }
        super.onReceivedError(webView, errorCode, description, failingUrl);
    }

    @Override
    public void onReceivedSslError(WebView webView, SslErrorHandler sslErrorHandler, SslError sslError) {
        Logger.d("onReceivedSslError");
        sslErrorHandler.proceed();
    }

    public interface ErrorCallBack {
        void onErrorReceived();
    }

    public void setErrorAction(ErrorCallBack errorCallBack) {
        this.errorCallBack = errorCallBack;
    }

    public interface StartedCallBack {
        void onLoadStarted(String url);
    }

    public void setStartedAction(StartedCallBack startedCallBack) {
        this.startedCallBack = startedCallBack;
    }

    public interface FinishedCallBack {
        void onLoadFinished();
    }

    public void setFinishedAction(FinishedCallBack finishedCallBack) {
        this.finishedCallBack = finishedCallBack;
    }

    public interface OverrideCallback {
        void onOverride(String url);
    }

    public void setOverrideCallback(OverrideCallback overrideCallback) {
        this.overrideCallback = overrideCallback;
    }

    @SuppressLint("SetJavaScriptEnabled")
    public void setWebSettings() {
        String appCachePath = null;
        String databasePath = null;
        if (webView != null && webSettings == null) {
            webView.setScrollBarStyle(WebView.SCROLLBARS_INSIDE_OVERLAY);
            webSettings = webView.getSettings();
        }

        if (context != null && context.getCacheDir() != null && context.getDir("databases", 0) != null) {
            try {
                appCachePath = context.getCacheDir().getAbsolutePath();
                databasePath = context.getDir("databases", 0).getPath();
            } catch (NullPointerException e) {
                e.printStackTrace();
            }
        }
        if (webSettings != null && appCachePath != null)
            webSettings.setAppCachePath(appCachePath);
        if (webSettings != null && databasePath != null) {
            webSettings.setDatabasePath(databasePath);
        }
        if (webSettings != null) {
            if (NetworkUtil.loadNetworkType(context) == NetworkUtil.NETWORK_NONE) {
                webSettings.setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);
            } else {
                webSettings.setCacheMode(WebSettings.LOAD_DEFAULT);
            }
            webSettings.setAppCacheEnabled(true);
            webSettings.setDatabaseEnabled(true);
            webSettings.setAllowFileAccess(true);

            webSettings.setAppCacheMaxSize(1024 * 1024 * 8);

            webSettings.setNeedInitialFocus(false);
            webSettings.setSupportMultipleWindows(false);

            try {
                webSettings.setJavaScriptEnabled(true);
            } catch (Exception exception) {
                exception.printStackTrace();
            }

            webSettings.setJavaScriptCanOpenWindowsAutomatically(false);
            webSettings.setLoadsImagesAutomatically(true);
            webSettings.setRenderPriority(WebSettings.RenderPriority.HIGH);

            webSettings.setBlockNetworkImage(false);
            webSettings.setLoadWithOverviewMode(true);
            webSettings.setBuiltInZoomControls(true);

            if (TextUtils.isEmpty(userAgent)) {
                userAgent = webSettings.getUserAgentString();
                userAgent = userAgent + (" QuDuQuXie/") + ("v_1.1.1 ")+ ("NetType/") + ("WIFI");

                webSettings.setUserAgentString(userAgent);

            } else {
                webSettings.setUserAgentString(userAgent);
            }
        }
    }

    public void doClear() {
        if (load_error_code != 0) {
            load_error_code = 0;
        }
    }
}
