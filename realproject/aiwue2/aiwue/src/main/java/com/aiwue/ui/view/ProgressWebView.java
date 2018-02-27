package com.aiwue.ui.view;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.util.AttributeSet;
import android.webkit.JavascriptInterface;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;

import com.aiwue.R;
import com.aiwue.utils.NetworkUtil;


/**
 * 带进度条的WebView
 */
public class ProgressWebView extends WebView {

    private ProgressBar progressbar;

    public ProgressWebView(Context context, AttributeSet attrs) {
        super(context, attrs);
        progressbar = new ProgressBar(context, null, android.R.attr.progressBarStyleHorizontal);
        progressbar.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, 5, 0, 0));
        Drawable drawable = context.getResources().getDrawable(R.drawable.progress_bar_states);
        progressbar.setProgressDrawable(drawable);
        addView(progressbar);
        setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                boolean isShould;
                if (NetworkUtil.isNetworkUrl(url)) {
                    view.loadUrl(url);
                    isShould = true;
                } else {
                    if (onHtmlEventListener != null)
                        onHtmlEventListener.onUriLoading(Uri.parse(url));
                    isShould = false;
                }
                return isShould;
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                view.loadUrl("javascript:window.local_obj.showSource('<head>'+" +
                        "document.getElementsByTagName('html')[0].innerHTML+'</head>');");
                super.onPageFinished(view, url);
            }
        });
        setWebChromeClient(new WebChromeClient());
        WebSettings settings = getSettings();
        settings.setJavaScriptEnabled(true);
        settings.setUseWideViewPort(true);
        settings.setLoadWithOverviewMode(true);
        settings.setCacheMode(WebSettings.LOAD_NO_CACHE);
        addJavascriptInterface(new InJavaScriptLocalObj(), "local_obj");
    }

    private OnHtmlEventListener onHtmlEventListener;

    public void setOnHtmlEventListener(OnHtmlEventListener onHtmlEventListener) {
        this.onHtmlEventListener = onHtmlEventListener;
    }

    public interface OnHtmlEventListener {
        void onFinished(String html);

        void onUriLoading(Uri uri);
    }

    class InJavaScriptLocalObj {
        @JavascriptInterface
        public void showSource(String html) {
//            Logger.i(html);
            if (onHtmlEventListener != null) onHtmlEventListener.onFinished(html);
        }
    }

    public class WebChromeClient extends android.webkit.WebChromeClient {
        @Override
        public void onProgressChanged(WebView view, int newProgress) {
            if (newProgress == 100) {
                progressbar.setVisibility(GONE);
            } else {
                if (progressbar.getVisibility() == GONE)
                    progressbar.setVisibility(VISIBLE);
                progressbar.setProgress(newProgress);
            }
            super.onProgressChanged(view, newProgress);
        }


    }

    @Override
    protected void onScrollChanged(int l, int t, int oldl, int oldt) {
        LayoutParams lp = (LayoutParams) progressbar.getLayoutParams();
        lp.x = l;
        lp.y = t;
        progressbar.setLayoutParams(lp);
        super.onScrollChanged(l, t, oldl, oldt);
    }
}