package com.rainwood.eurobusiness.ui.activity;

import android.annotation.SuppressLint;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.rainwood.eurobusiness.R;
import com.rainwood.eurobusiness.base.BaseActivity;
import com.rainwood.eurobusiness.domain.SaleStaticsBean;
import com.rainwood.eurobusiness.json.JsonParser;
import com.rainwood.eurobusiness.okhttp.HttpResponse;
import com.rainwood.eurobusiness.okhttp.OnHttpListener;
import com.rainwood.eurobusiness.request.RequestPost;
import com.rainwood.eurobusiness.ui.adapter.SaleStaticAdapter;
import com.rainwood.tools.viewinject.ViewById;
import com.rainwood.tools.widget.MeasureListView;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @Author: a797s
 * @Date: 2020/2/13
 * @Desc:
 */
public class SaleStaticsActivity extends BaseActivity implements View.OnClickListener, OnHttpListener {

    @Override
    protected int getLayoutId() {
        return R.layout.activity_sales_statics;
    }

    @ViewById(R.id.iv_back)
    private ImageView pageBack;
    @ViewById(R.id.tv_title)
    private TextView pageTitle;
    @ViewById(R.id.lv_modules_list)
    private MeasureListView moduleList;

    // handler
    private final int INITIAL_SIZE = 0x101;

    @Override
    protected void initView() {
        pageBack.setOnClickListener(this);
        pageBack.setImageResource(R.drawable.icon_white_page_back);
        pageTitle.setText("订单销售统计");
        pageTitle.setTextColor(getResources().getColor(R.color.white));
    }

    @Override
    protected void initData() {
        mList = new ArrayList<>();
        for (String title : titles) {
            SaleStaticsBean saleStatics = new SaleStaticsBean();
            saleStatics.setTitle(title);
            mList.add(saleStatics);
        }
        // request
        RequestPost.saleTotal(this);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.iv_back) {
            finish();
        }
    }

    @SuppressLint("HandlerLeak")
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(@NonNull Message msg) {
            if (msg.what == INITIAL_SIZE) {
                SaleStaticAdapter staticAdapter = new SaleStaticAdapter(SaleStaticsActivity.this, mList);
                moduleList.setAdapter(staticAdapter);
            }
        }
    };

    private List<SaleStaticsBean> mList;
    private String[] titles = {"客户数", "订单数", "销售额"};

    @Override
    public void onHttpFailure(HttpResponse result) {

    }

    @Override
    public void onHttpSucceed(HttpResponse result) {
        Map<String, String> body = JsonParser.parseJSONObject(result.body());
        if (body != null) {
            if ("1".equals(body.get("code"))) {
                if (result.url().contains("wxapi/v1/statistics.php?type=getTotal")) {               // 销售量统计表
                    Map<String, String> info = JsonParser.parseJSONObject(JsonParser.parseJSONObject(body.get("data")).get("info"));
                    Log.d(TAG, "info --- " + JsonParser.parseJSONObject(body.get("data")));
                    for (int i = 0; i < mList.size(); i++) {
                        switch (i){
                            case 0:             // 客户数量
                                // 今日新增客户数
                                mList.get(i).setToday(info.get("toDayPeople"));
                                // 本月新增客户数
                                mList.get(i).setMonth(info.get("toYearPeople"));
                                break;
                            case 1:             // 订单数量
                                mList.get(i).setToday(info.get("toDayOrder"));
                                mList.get(i).setMonth(info.get("toYearOrder"));
                                break;
                            case 2:             // 销售额
                                mList.get(i).setToday(info.get("toDayMoney"));
                                mList.get(i).setMonth(info.get("toYearMoney"));
                                break;
                        }
                    }
                    Message msg = new Message();
                    msg.what = INITIAL_SIZE;
                    mHandler.sendMessage(msg);
                }
            }else {
                toast(body.get("warn"));
            }
        }
    }
}
