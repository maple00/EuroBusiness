package com.rainwood.eurobusiness.ui.activity;

import android.annotation.SuppressLint;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.rainwood.eurobusiness.R;
import com.rainwood.eurobusiness.base.BaseActivity;
import com.rainwood.eurobusiness.domain.CommonUIBean;
import com.rainwood.eurobusiness.json.JsonParser;
import com.rainwood.eurobusiness.okhttp.HttpResponse;
import com.rainwood.eurobusiness.okhttp.OnHttpListener;
import com.rainwood.eurobusiness.request.RequestPost;
import com.rainwood.eurobusiness.ui.adapter.StoresListAdapter;
import com.rainwood.eurobusiness.utils.ListUtils;
import com.rainwood.tools.viewinject.ViewById;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @Author: a797s
 * @Date: 2020/2/7
 * @Desc: 门店信息
 */
public class StoresActivity extends BaseActivity implements View.OnClickListener, OnHttpListener {

    @Override
    protected int getLayoutId() {
        return R.layout.activity_stores;
    }

    @ViewById(R.id.iv_back)
    private ImageView pageBack;
    @ViewById(R.id.tv_title)
    private TextView pageTitle;
    @ViewById(R.id.lv_stores)
    private ListView storesInfos;

    private final int INITIAL_SIZE = 0x101;

    private List<CommonUIBean> mList;
    private String[] titles = {"门店名称", "联系人", "邮箱", "电话", "所在地区", "详细地址", "税号(P.IVA)", "税号(C.F)", "备注"};

    @Override
    protected void initView() {
        pageBack.setOnClickListener(this);
        pageTitle.setText("门店信息");

        // request
        showLoading("loading");
        RequestPost.storeOrInvoice(this);
    }

    @Override
    protected void initData() {
        mList = new ArrayList<>();
        for (String title : titles) {
            CommonUIBean commonUI = new CommonUIBean();
            commonUI.setTitle(title);
            mList.add(commonUI);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_back:
                //openActivity(PersonalFragment.class);
                finish();
                break;
        }
    }

    @SuppressLint("HandlerLeak")
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(@NonNull Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case INITIAL_SIZE:
                    StoresListAdapter adapter = new StoresListAdapter(StoresActivity.this, mList);
                    storesInfos.setAdapter(adapter);
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
                // 门店或开票信息
                if (result.url().contains("wxapi/v1/index.php?type=getStoreInfo")) {
                    Map<String, String> data = JsonParser.parseJSONObject(JsonParser.parseJSONObject(body.get("data")).get("info"));
                    if (data != null)
                        for (int i = 0; i < ListUtils.getSize(mList); i++) {
                            switch (i) {
                                case 0:
                                    mList.get(i).setShowText(data.get("id"));
                                    break;
                                case 1:
                                    mList.get(i).setShowText(data.get("contactName"));
                                    break;
                                case 2:
                                    mList.get(i).setShowText(data.get("email"));
                                    break;
                                case 3:
                                    mList.get(i).setShowText(data.get("tel"));
                                    break;
                                case 4:
                                    mList.get(i).setShowText(data.get("region"));
                                    break;
                                case 5:
                                    mList.get(i).setShowText(data.get("address"));
                                    break;
                                case 6:
                                    mList.get(i).setShowText(data.get("companyTax"));
                                    break;
                                case 7:
                                    mList.get(i).setShowText(data.get("taxNum"));
                                    break;
                                case 8:
                                    mList.get(i).setShowText(data.get("text"));
                                    break;
                            }
                        }
                    Message msg = new Message();
                    msg.what = INITIAL_SIZE;
                    mHandler.sendMessage(msg);
                }
            } else {
                toast(body.get("warn"));
            }
            dismissLoading();
        }
    }
}
