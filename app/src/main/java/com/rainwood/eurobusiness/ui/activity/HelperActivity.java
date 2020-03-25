package com.rainwood.eurobusiness.ui.activity;

import android.annotation.SuppressLint;
import android.os.Build;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.webkit.CookieManager;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.TextView;

import com.rainwood.eurobusiness.R;
import com.rainwood.eurobusiness.base.BaseActivity;
import com.rainwood.eurobusiness.common.Contants;
import com.rainwood.eurobusiness.domain.HelpCenterBean;
import com.rainwood.eurobusiness.json.JsonParser;
import com.rainwood.eurobusiness.okhttp.HttpResponse;
import com.rainwood.eurobusiness.okhttp.OnHttpListener;
import com.rainwood.eurobusiness.request.RequestPost;
import com.rainwood.tools.viewinject.ViewById;

import java.util.List;
import java.util.Map;

/**
 * @Author: a797s
 * @Date: 2020/2/7
 * @Desc: 帮助中心
 */
public class HelperActivity extends BaseActivity implements View.OnClickListener, OnHttpListener {

    @Override
    protected int getLayoutId() {
        return R.layout.activity_helper;
    }

    @ViewById(R.id.iv_back)
    private ImageView pageBack;
    @ViewById(R.id.tv_title)
    private TextView pageTitle;
    @ViewById(R.id.wv_content)
    private WebView mWebView;

    @Override
    protected void initView() {
        pageBack.setOnClickListener(this);
        pageTitle.setText("帮助中心");
        // request
        showLoading("loading");
        RequestPost.helpCenter(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_back:
                finish();
                break;
        }
    }

    @Override
    public void onHttpFailure(HttpResponse result) {

    }

    @Override
    public void onHttpSucceed(HttpResponse result) {
        Map<String, String> body = JsonParser.parseJSONObject(result.body());
        if (body != null) {
            if ("1".equals(body.get("code"))) {
                // 门店或开票信息
                if (result.url().contains("wxapi/v1/index.php?type=getHelp")) {
                    List<HelpCenterBean> dataList = JsonParser.parseJSONArray(HelpCenterBean.class, JsonParser.parseJSONObject(body.get("data")).get("list"));
                    // 如果问题为空则必然是图片，如果图片为空，则必然是文字
                    for (HelpCenterBean helpData : dataList) {
                        if (TextUtils.isEmpty(helpData.getWord())){     // 必然是图片
                            content.append("<img src=\"").append(helpData.getImg()).append("\" alt=\"").append(helpData.getId()).append("\"").append("/>\r\n");
                        }else {
                            content.append(helpData.getWord()).append("\r\n");
                        }
                    }
                    initWebView();
                    Log.d(TAG, "webView === " + content);
                }
            } else {
                toast(body.get("data"));
            }
            dismissLoading();
        }
    }

    /**
     * web解析
     */
    @SuppressLint("SetJavaScriptEnabled")
    private void initWebView() {
        WebSettings settings = mWebView.getSettings();
        // 设置WebView支持JavaScript
        settings.setJavaScriptEnabled(true);
        //支持自动适配
        settings.setUseWideViewPort(true);
        settings.setLoadWithOverviewMode(true);
        settings.setSupportZoom(false);  //支持放大缩小
        settings.setBuiltInZoomControls(false); //显示缩放按钮
        settings.setBlockNetworkImage(true);// 把图片加载放在最后来加载渲染
        settings.setAllowFileAccess(true); // 允许访问文件
        settings.setSaveFormData(true);
        settings.setGeolocationEnabled(true);
        settings.setDomStorageEnabled(true);
        settings.setJavaScriptCanOpenWindowsAutomatically(true);/// 支持通过JS打开新窗口
        settings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.NARROW_COLUMNS);
        settings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);
        settings.setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);
        //设置不让其跳转浏览器
        mWebView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                return false;
            }
        });
        // 添加客户端支持
        mWebView.setWebChromeClient(new WebChromeClient());
        // mWebView.loadUrl(TEXTURL);
        //不加这个图片显示不出来
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP) {
            mWebView.getSettings().setMixedContentMode(WebSettings.MIXED_CONTENT_ALWAYS_ALLOW);
        }
        mWebView.getSettings().setBlockNetworkImage(false);
        //允许cookie 不然有的网站无法登陆
        CookieManager mCookieManager = CookieManager.getInstance();
        mCookieManager.setAcceptCookie(true);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            mCookieManager.setAcceptThirdPartyCookies(mWebView, true);
        }
        // Log.d(TAG, " --- " + startStr + content + endStr);
        mWebView.loadDataWithBaseURL(null, startStr + content + endStr,
                "text/html", "utf-8", null);
    }

    private StringBuffer content = new StringBuffer();
    private String startStr = "<html>\n" +
            "    <head>\n" +
            "        <meta name=\"viewport\" content=\"width=device-width, initial-scale=1\">\n" +
            "        <style>img{max-width: 100%; width:100%; margin:15px 0;height:auto;}*{margin:0px;}</style>\n" +
            "    </head>\n" +
            "    <body>\n" +
            "    <p style=\"text-align: left;\">";
    private String endStr = "</p>\n" + "</body>\n" + "</html>";

}
