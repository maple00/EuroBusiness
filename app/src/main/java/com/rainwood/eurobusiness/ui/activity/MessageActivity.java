package com.rainwood.eurobusiness.ui.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.rainwood.eurobusiness.R;
import com.rainwood.eurobusiness.base.BaseActivity;
import com.rainwood.eurobusiness.domain.MessageBean;
import com.rainwood.eurobusiness.json.JsonParser;
import com.rainwood.eurobusiness.okhttp.HttpResponse;
import com.rainwood.eurobusiness.okhttp.OnHttpListener;
import com.rainwood.eurobusiness.request.RequestPost;
import com.rainwood.eurobusiness.ui.adapter.MessageListAdapter;
import com.rainwood.tools.refresh.DaisyRefreshLayout;
import com.rainwood.tools.viewinject.ViewById;
import com.rainwood.tools.widget.MeasureListView;

import java.util.List;
import java.util.Map;

/**
 * @Author: shearson
 * @Time: 2020/3/1 18:09
 * @Desc: 消息 msg
 */
public final class MessageActivity extends BaseActivity implements View.OnClickListener, OnHttpListener {

    @Override
    protected int getLayoutId() {
        return R.layout.activity_message;
    }

    @ViewById(R.id.iv_back)
    private ImageView pageBack;
    @ViewById(R.id.tv_title)
    private TextView pageTitle;
    @ViewById(R.id.mlv_message_list)
    private MeasureListView messageList;
    @ViewById(R.id.drl_refresh)
    private DaisyRefreshLayout mRefreshLayout;
    private List<MessageBean> mList;
    // mHandler
    private final int REFRESH_SIZE = 0x101;
    private final int INITIAL_SIZE = 0x102;

    @Override
    protected void initView() {
        pageBack.setOnClickListener(this);
        pageTitle.setText("消息");
    }

    @Override
    protected void onResume() {
        super.onResume();
        // request
        // showLoading("loading");
        Message msg = new Message();
        msg.what = REFRESH_SIZE;
        mHandler.sendMessage(msg);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_back:
                finish();
                break;
        }
    }

    private static int pageCount = 0;

    @SuppressLint("HandlerLeak")
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(@NonNull Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case REFRESH_SIZE:
                    //上拉加载
                    mRefreshLayout.setOnLoadMoreListener(() -> {
                        pageCount++;
                        // request
                        RequestPost.getMessageList(String.valueOf(pageCount),MessageActivity.this);
                    });
                    // 下拉刷新
                    mRefreshLayout.setOnRefreshListener(() -> {
                        pageCount = 0;
                        RequestPost.getMessageList(String.valueOf(pageCount),MessageActivity.this);
                    });
                    // 第一次进来的时候刷新
                    mRefreshLayout.setOnAutoLoadListener(() -> RequestPost.getMessageList("0", MessageActivity.this));
                    mRefreshLayout.autoRefresh();
                    break;
                case INITIAL_SIZE:
                    MessageListAdapter listAdapter = new MessageListAdapter(MessageActivity.this, mList);
                    messageList.setAdapter(listAdapter);
                    listAdapter.setOnClickItem(position -> {
                        //toast("查看详情");
                        //openActivity(MessageDetailActivity.class);
                        Intent intent = new Intent(MessageActivity.this, MessageDetailActivity.class);
                        intent.putExtra("id", mList.get(position).getId());
                        startActivity(intent);
                    });
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
                // 获取消息列表
                if (result.url().contains("wxapi/v1/index.php?type=getMessagelist")) {
                    Log.d(TAG, "=== body ==== " + body.get("data"));
                    mList = JsonParser.parseJSONArray(MessageBean.class, JsonParser.parseJSONObject(body.get("data")).get("messagelist"));
                    // initial --- msg list
                    mRefreshLayout.setRefreshing(false);
                    mRefreshLayout.setLoadMore(false);
                    Message msgContent = new Message();
                    msgContent.what = INITIAL_SIZE;
                    mHandler.sendMessage(msgContent);
                }
            } else {
                toast(body.get("warn"));
            }
            //dismissLoading();
        }
    }
}
