package com.rainwood.eurobusiness.ui.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.ImageView;

import androidx.annotation.NonNull;

import com.rainwood.eurobusiness.R;
import com.rainwood.eurobusiness.base.BaseActivity;
import com.rainwood.eurobusiness.domain.WarnStockBean;
import com.rainwood.eurobusiness.json.JsonParser;
import com.rainwood.eurobusiness.okhttp.HttpResponse;
import com.rainwood.eurobusiness.okhttp.OnHttpListener;
import com.rainwood.eurobusiness.request.RequestPost;
import com.rainwood.eurobusiness.ui.adapter.WarnRepertoryAdapter;
import com.rainwood.tools.refresh.DaisyRefreshLayout;
import com.rainwood.tools.viewinject.ViewById;
import com.rainwood.tools.widget.MeasureListView;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @Author: a797s
 * @Date: 2020/2/20
 * @Desc: 预警库存
 */
public class WarnRepertoryActivity extends BaseActivity implements View.OnClickListener, OnHttpListener {
    @Override
    protected int getLayoutId() {
        return R.layout.activity_warn_repertory;
    }

    @ViewById(R.id.iv_back)
    private ImageView pageBack;
    @ViewById(R.id.lv_content_list)
    private MeasureListView contentList;
    @ViewById(R.id.drl_refresh)
    private DaisyRefreshLayout mRefreshLayout;

    private List<WarnStockBean> mCopyList = new ArrayList<>();
    private final int INITIAL_SIZE = 0x101;
    private final int REFRESH_SIZE = 0x102;
    private static int pageCount = 0;

    @Override
    protected void initView() {
        pageBack.setOnClickListener(this);

        Message msg = new Message();
        msg.what = REFRESH_SIZE;
        mHandler.sendMessage(msg);
    }

    @Override
    protected void initData() {
        super.initData();
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
            super.handleMessage(msg);
            switch (msg.what) {
                case INITIAL_SIZE:
                    mRefreshLayout.setRefreshing(false);
                    mRefreshLayout.setLoadMore(false);
                    WarnRepertoryAdapter repertoryAdapter = new WarnRepertoryAdapter(WarnRepertoryActivity.this, mCopyList);
                    contentList.setAdapter(repertoryAdapter);
                    repertoryAdapter.setOnClickItem(position -> {
                        // toast("点击了：" + position);
                        Intent intent = new Intent(WarnRepertoryActivity.this, WarnRepertoryDetailActivity.class);
                        intent.putExtra("warnId", mCopyList.get(position).getId());
                        startActivity(intent);
                    });
                    break;
                case REFRESH_SIZE:
                    //上拉加载
                    mRefreshLayout.setOnLoadMoreListener(() -> {
                        pageCount++;
                        RequestPost.warnStockList(String.valueOf(pageCount), "", WarnRepertoryActivity.this);
                    });
                    // 下拉刷新
                    mRefreshLayout.setOnRefreshListener(() -> {
                        pageCount = 0;
                        mCopyList = new ArrayList<>();
                        RequestPost.warnStockList(String.valueOf(pageCount), "", WarnRepertoryActivity.this);
                    });
                    // 第一次进来的时候刷新
                    mRefreshLayout.setOnAutoLoadListener(() -> {
                        // default query new
                        RequestPost.warnStockList(String.valueOf(pageCount), "", WarnRepertoryActivity.this);
                    });
                    mRefreshLayout.autoRefresh();
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
                // 获取预警库存列表
                if (result.url().contains("wxapi/v1/stock.php?type=getWarnStocklist")) {
                    List<WarnStockBean> stockList = JsonParser.parseJSONArray(WarnStockBean.class, JsonParser.parseJSONObject(body.get("data")).get("stocklist"));
                    if (stockList != null) {
                        mCopyList.addAll(stockList);
                        Message msg = new Message();
                        msg.what = INITIAL_SIZE;
                        mHandler.sendMessage(msg);
                    }
                }
            } else {
                toast(body.get("data"));
            }
            if (getDialog() != null)
                dismissLoading();
        }
    }
}
