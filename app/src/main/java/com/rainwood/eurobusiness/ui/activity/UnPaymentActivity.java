package com.rainwood.eurobusiness.ui.activity;

import android.annotation.SuppressLint;
import android.os.Handler;
import android.os.Message;
import android.text.Html;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.rainwood.eurobusiness.R;
import com.rainwood.eurobusiness.base.BaseActivity;
import com.rainwood.eurobusiness.common.Contants;
import com.rainwood.eurobusiness.domain.CommonUIBean;
import com.rainwood.eurobusiness.domain.UnCompleteBean;
import com.rainwood.eurobusiness.json.JsonParser;
import com.rainwood.eurobusiness.okhttp.HttpResponse;
import com.rainwood.eurobusiness.okhttp.OnHttpListener;
import com.rainwood.eurobusiness.request.RequestPost;
import com.rainwood.eurobusiness.ui.adapter.UnPayAdapter;
import com.rainwood.tools.common.FontDisplayUtil;
import com.rainwood.tools.viewinject.ViewById;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @Author: shearson
 * @Time: 2020/2/24 21:24
 * @Desc: 未收款项 和未付款项
 */
public class UnPaymentActivity extends BaseActivity implements View.OnClickListener, OnHttpListener {

    @Override
    protected int getLayoutId() {
        return R.layout.activity_un_payment;
    }

    @ViewById(R.id.iv_back)
    private ImageView pageBack;
    @ViewById(R.id.tv_title)
    private TextView pageTitle;
    @ViewById(R.id.tv_all_money)
    private TextView allMoney;
    @ViewById(R.id.lv_content_list)
    private ListView contentList;

    private List<CommonUIBean> mList;
    private final int INITIAL_SIZE = 0x101;
    private String totalAmount = "";         // 合计金额

    @SuppressLint("SetTextI18n")
    @Override
    protected void initView() {
        pageBack.setOnClickListener(this);
        if (Contants.CHOOSE_MODEL_SIZE == 115) {         // 未付款项
            pageTitle.setText("未付款项");
            // request
            showLoading("");
            RequestPost.UnPaylist(this);
        }
        if (Contants.CHOOSE_MODEL_SIZE == 116) {        // 未收款项
            pageTitle.setText("未收款项");
            // request
            showLoading("");
            RequestPost.UnRecList(this);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_back:
                finish();
                break;
        }
    }

    @SuppressLint("HandlerLeak")
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(@NonNull Message msg) {
            switch (msg.what) {
                case INITIAL_SIZE:
                    allMoney.setText(Html.fromHtml("<font color='" + R.color.textColor
                            + "' size='" + FontDisplayUtil.dip2px(UnPaymentActivity.this, 13) + "'>合计：</font>"
                            + "<font color='" + R.color.red30 + "'size='" + FontDisplayUtil.dip2px(UnPaymentActivity.this, 17)
                            + "'><b>" + totalAmount + "</b></font>"));

                    UnPayAdapter payAdapter = new UnPayAdapter(UnPaymentActivity.this, mList);
                    contentList.setAdapter(payAdapter);
                    break;
            }
        }
    };

    @Override
    public void onHttpFailure(HttpResponse result) {

    }

    @Override
    public void onHttpSucceed(HttpResponse result) {
        Map<String, String> body = JsonParser.parseJSONObject(result.body());
        if (body != null) {
            if ("1".equals(body.get("code"))) {
                if (result.url().contains("wxapi/v1/statistics.php?type=getReclist")) {       // 未收款项
                    List<UnCompleteBean> unCompleteList = JsonParser.parseJSONArray(UnCompleteBean.class, JsonParser.parseJSONObject(body.get("data")).get("datalist"));
                    totalAmount = JsonParser.parseJSONObject(body.get("data")).get("info");
                    // data
                    mList = new ArrayList<>();
                    if (unCompleteList != null) {
                        for (UnCompleteBean unCompleteBean : unCompleteList) {
                            CommonUIBean common = new CommonUIBean();
                            common.setTitle(unCompleteBean.getName());
                            common.setShowText(unCompleteBean.getMoney());
                            mList.add(common);
                        }
                    }

                }

                if (result.url().contains("wxapi/v1/statistics.php?type=getPayclist")) {         // 未付款项
                    List<UnCompleteBean> unCompleteList = JsonParser.parseJSONArray(UnCompleteBean.class, JsonParser.parseJSONObject(body.get("data")).get("datalist"));
                    totalAmount = JsonParser.parseJSONObject(body.get("data")).get("info");
                    mList = new ArrayList<>();
                    if (unCompleteList != null) {
                        for (UnCompleteBean unCompleteBean : unCompleteList) {
                            CommonUIBean common = new CommonUIBean();
                            common.setTitle(unCompleteBean.getName());
                            common.setShowText(unCompleteBean.getMoney());
                            mList.add(common);
                        }
                    }
                }
                Message msg = new Message();
                msg.what = INITIAL_SIZE;
                mHandler.sendMessage(msg);
            } else {
                toast(body.get("warn"));
            }
            dismissLoading();
        }
    }
}
