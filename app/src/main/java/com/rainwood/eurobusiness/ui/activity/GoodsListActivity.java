package com.rainwood.eurobusiness.ui.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.rainwood.eurobusiness.R;
import com.rainwood.eurobusiness.base.BaseActivity;
import com.rainwood.eurobusiness.domain.GoodsListBean;
import com.rainwood.eurobusiness.json.JsonParser;
import com.rainwood.eurobusiness.okhttp.HttpResponse;
import com.rainwood.eurobusiness.okhttp.OnHttpListener;
import com.rainwood.eurobusiness.request.RequestPost;
import com.rainwood.eurobusiness.ui.adapter.GoodsListAdapter;
import com.rainwood.tools.view.ClearEditText;
import com.rainwood.tools.viewinject.ViewById;

import java.util.List;
import java.util.Map;

import static com.rainwood.eurobusiness.common.Contants.GOODS_NAME_REQUEST;

/**
 * @Author: a797s
 * @Date: 2020/4/8 10:39
 * @Desc: 商品列表activity
 */
public final class GoodsListActivity extends BaseActivity implements View.OnClickListener, OnHttpListener {

    @ViewById(R.id.iv_back)
    private ImageView mPageBack;
    @ViewById(R.id.ll_search)
    private LinearLayout search;
    @ViewById(R.id.cet_search)
    private ClearEditText cetSearch;
    @ViewById(R.id.tv_search_tips)
    private TextView searchTips;
    @ViewById(R.id.lv_goods_list)
    private ListView goodsList;

    // handler
    private final int INITIAL_SIZE = 0x101;
    private List<GoodsListBean> mGoodsListList;
    private String mGoodsTypeId;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_goods_list;
    }

    @Override
    protected void initView() {
        initEvents();
        // request -- 商品列表  -- 有商品分类id的时候，查询分类下的商品，没有则查询所有
        showLoading("");
        mGoodsTypeId = getIntent().getStringExtra("goodsTypeId");
        RequestPost.getGoodsList("", (mGoodsTypeId == null) || mGoodsTypeId.equals("请选择") ? "" : mGoodsTypeId, this);
    }

    private void initEvents() {
        mPageBack.setOnClickListener(this);
        search.setOnClickListener(this);
        searchTips.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_back:
                finish();
                break;
            case R.id.ll_search:
                cetSearch.setFocusable(true);
                cetSearch.setFocusableInTouchMode(true);
                break;
            case R.id.tv_search_tips:
                // request 搜索商品列表
                showLoading("");
                RequestPost.getGoodsList("", mGoodsTypeId == null ? "" : mGoodsTypeId, this);
                break;
        }
    }

    @SuppressLint("HandlerLeak")
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(@NonNull Message msg) {
            switch (msg.what) {
                case INITIAL_SIZE:
                    GoodsListAdapter goodsListAdapter = new GoodsListAdapter(GoodsListActivity.this, mGoodsListList);
                    goodsList.setAdapter(goodsListAdapter);
                    goodsListAdapter.setOnClickGoods(position -> {
                        for (GoodsListBean listBean : mGoodsListList) {
                            listBean.setSelected(false);
                        }
                        mGoodsListList.get(position).setSelected(true);
                        goodsListAdapter.notifyDataSetChanged();
                        // TODO: return
                        showLoading("");
                        postDelayed(() -> {
                            dismissLoading();
                            Intent intent = new Intent();
                            intent.putExtra("goods", mGoodsListList.get(position));
                            setResult(GOODS_NAME_REQUEST, intent);
                            finish();
                        }, 500);
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
                // 获取商品列表
                if (result.url().contains("wxapi/v1/goods.php?type=getGoods")) {
                    mGoodsListList = JsonParser.parseJSONArray(GoodsListBean.class, JsonParser.parseJSONObject(body.get("data")).get("goodslist"));

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
