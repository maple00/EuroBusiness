package com.rainwood.eurobusiness.ui.activity;

import android.annotation.SuppressLint;
import android.os.Build;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.rainwood.eurobusiness.R;
import com.rainwood.eurobusiness.base.BaseActivity;
import com.rainwood.eurobusiness.common.Contants;
import com.rainwood.eurobusiness.domain.ClientBean;
import com.rainwood.eurobusiness.domain.GoodsRankingBean;
import com.rainwood.eurobusiness.domain.StoreBean;
import com.rainwood.eurobusiness.json.JsonParser;
import com.rainwood.eurobusiness.okhttp.HttpResponse;
import com.rainwood.eurobusiness.okhttp.OnHttpListener;
import com.rainwood.eurobusiness.request.RequestPost;
import com.rainwood.eurobusiness.ui.adapter.ClientRankingAdapter;
import com.rainwood.eurobusiness.ui.adapter.SalesLeaderAdapter;
import com.rainwood.eurobusiness.ui.adapter.StoreRankingAdapter;
import com.rainwood.eurobusiness.utils.ListUtils;
import com.rainwood.tools.viewinject.ViewById;
import com.rainwood.tools.widget.MeasureListView;

import java.util.List;
import java.util.Map;

/**
 * @Author: a797s
 * @Date: 2020/2/13
 * @Desc: 销售排行榜
 */
public class SalesLeaderActivity extends BaseActivity implements View.OnClickListener, OnHttpListener {

    @Override
    protected int getLayoutId() {
        return R.layout.activity_sales_leader;
    }

    @ViewById(R.id.iv_back)
    private ImageView pageBack;
    @ViewById(R.id.tv_title)
    private TextView pageTitle;
    @ViewById(R.id.lv_goods_list)
    private MeasureListView goodsList;
    @ViewById(R.id.ll_goods)
    private LinearLayout goodsLine;

    // 批发商
    @ViewById(R.id.ll_sales)
    private LinearLayout sales;
    @ViewById(R.id.tv_goods)
    private TextView goods;
    @ViewById(R.id.tv_stores)
    private TextView stores;
    @ViewById(R.id.tv_custom)
    private TextView custom;
    @ViewById(R.id.ll_client)
    private LinearLayout client;
    @ViewById(R.id.tv_type_name)
    private TextView typeName;

    // mHandler
    private final int INITIAL_SIZE = 0x101;
    private final int STORE_SIZE = 0x102;
    private final int CLIENT_SIZE = 0x103;

    // 商品排行榜
    private List<GoodsRankingBean> mList;
    // 门店排行榜
    private List<StoreBean> mStoreList;
    // 客户排行榜
    private List<ClientBean> mClientList;

    @Override
    protected void initView() {
        pageBack.setOnClickListener(this);
        pageBack.setImageResource(R.drawable.icon_white_page_back);
        pageTitle.setTextColor(getResources().getColor(R.color.white));

        // 默认
        goodsLine.setVisibility(View.VISIBLE);
        client.setVisibility(View.GONE);

        if (Contants.CHOOSE_MODEL_SIZE == 114) {
            pageTitle.setText("销售排行榜");
            sales.setVisibility(View.VISIBLE);
            goods.setOnClickListener(this);
            custom.setOnClickListener(this);
            stores.setOnClickListener(this);
            goods.setText("商品");
            stores.setText("门店");
            custom.setText("客户");
        }else {
            pageTitle.setText("商品销售排行榜");
            sales.setVisibility(View.GONE);
        }
    }


    @Override
    protected void initData() {
        // request  -- 门店端
        showLoading("loading");
        if (Contants.CHOOSE_MODEL_SIZE == 12) {
            request("");
        }
        // 批发商端
        if (Contants.CHOOSE_MODEL_SIZE == 114) {
            // 默认请求商品
            request("goods");
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_back:
                finish();
                break;
            case R.id.tv_goods:                     // 商品ranking
                goodsLine.setVisibility(View.VISIBLE);
                client.setVisibility(View.GONE);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    setTopUI(R.drawable.shape_radius_yellow_left_4, R.drawable.shape_radius_yellow_full_empry_4, R.drawable.shape_radius_yellow_right_empty_4, R.color.black01, R.color.orange25, R.color.orange25);
                }
                // request goods
                showLoading("loading");
                request("goods");
                break;
            case R.id.tv_stores:                    // 门店raking
                goodsLine.setVisibility(View.GONE);
                client.setVisibility(View.VISIBLE);
                typeName.setText("门店名称");
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    setTopUI(R.drawable.shape_radius_yellow_left_empty_4, R.drawable.shape_radius_yellow_full_4, R.drawable.shape_radius_yellow_right_empty_4, R.color.orange25, R.color.black01, R.color.orange25);
                }
                // request store
                showLoading("loading");
                request("store");
                break;
            case R.id.tv_custom:                    // 客户ranking
                goodsLine.setVisibility(View.GONE);
                client.setVisibility(View.VISIBLE);
                typeName.setText("客户名称");
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    setTopUI(R.drawable.shape_radius_yellow_left_empty_4, R.drawable.shape_radius_yellow_full_empry_4, R.drawable.shape_radius_yellow_right_4, R.color.orange25, R.color.orange25, R.color.black01);
                }
                // request client
                showLoading("loading");
                request("client");
                break;
        }
    }

    /**
     * 查看批发商的销售排行榜
     */
    private void setTopUI(int p, int p2, int p3, int p4, int p5, int p6) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            goods.setBackground(getDrawable(p));
            stores.setBackground(getDrawable(p2));
            custom.setBackground(getDrawable(p3));
        }
        goods.setTextColor(getResources().getColor(p4));
        stores.setTextColor(getResources().getColor(p5));
        custom.setTextColor(getResources().getColor(p6));
    }

    /**
     * request
     */
    private void request(String type) {
        RequestPost.saleRanking(type, this);
    }

    @SuppressLint("HandlerLeak")
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(@NonNull Message msg) {
            switch (msg.what) {
                case INITIAL_SIZE:               // 默认查询的是商品ranking
                    if (Contants.CHOOSE_MODEL_SIZE == 12) {              // 门店端
                        SalesLeaderAdapter leaderAdapter = new SalesLeaderAdapter(SalesLeaderActivity.this, mList);
                        goodsList.setAdapter(leaderAdapter);
                    }
                    if (Contants.CHOOSE_MODEL_SIZE == 114) {             // 批发商端
                        SalesLeaderAdapter leaderAdapter = new SalesLeaderAdapter(SalesLeaderActivity.this, mList);
                        goodsList.setAdapter(leaderAdapter);
                    }
                    break;
                case STORE_SIZE:            // 门店ranking
                    Log.d(TAG, "mStoreList -- " + mStoreList.toString());
                    StoreRankingAdapter storeRankingAdapter = new StoreRankingAdapter(SalesLeaderActivity.this, mStoreList);
                    goodsList.setAdapter(storeRankingAdapter);
                    break;
                case CLIENT_SIZE:           // 客户ranking
                    ClientRankingAdapter clientRankingAdapter = new ClientRankingAdapter(SalesLeaderActivity.this, mClientList);
                    goodsList.setAdapter(clientRankingAdapter);
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
                if (result.url().contains("wxapi/v1/statistics.php?type=getSaleRank")) {       // 销售排行榜
                    String data = body.get("data");
                    Log.d(TAG, "data --- " + data);
                    Map<String, String> map = JsonParser.parseJSONObject(body.get("data"));
                    // 商品列表
                    if ("goods".equals(map.get("type")) || "".equals(map.get("type"))) {
                        mList = JsonParser.parseJSONArray(GoodsRankingBean.class, JsonParser.parseJSONObject(body.get("data")).get("datalist"));
                        for (int i = 0; i < mList.size(); i++) {
                            mList.get(i).setRaking(String.valueOf(i+1));
                        }
                        Message msg = new Message();
                        msg.what = INITIAL_SIZE;
                        mHandler.sendMessage(msg);
                    }
                    // 门店列表
                    if ("store".equals(map.get("type"))) {
                        mStoreList = JsonParser.parseJSONArray(StoreBean.class, JsonParser.parseJSONObject(body.get("data")).get("datalist"));
                        for (int i = 0; i < ListUtils.getSize(mStoreList); i++) {
                            mStoreList.get(i).setRanking(String.valueOf(i+1));
                        }
                        Message msg = new Message();
                        msg.what = STORE_SIZE;
                        mHandler.sendMessage(msg);
                    }
                    // 客户列表
                    if ("client".equals(map.get("type"))) {
                        mClientList = JsonParser.parseJSONArray(ClientBean.class, JsonParser.parseJSONObject(body.get("data")).get("datalist"));
                        for (int i = 0; i < ListUtils.getSize(mClientList); i++) {
                            mClientList.get(i).setRanking(String.valueOf(i+1));
                        }
                        Message msg = new Message();
                        msg.what = CLIENT_SIZE;
                        mHandler.sendMessage(msg);
                    }

                }
            } else {
                toast(body.get("warn"));
            }
            dismissLoading();
        }
    }
}
