package com.rainwood.eurobusiness.ui.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.bumptech.glide.Glide;
import com.rainwood.eurobusiness.R;
import com.rainwood.eurobusiness.base.BaseActivity;
import com.rainwood.eurobusiness.domain.CommonUIBean;
import com.rainwood.eurobusiness.domain.SpecificationBean;
import com.rainwood.eurobusiness.domain.WarinReBean;
import com.rainwood.eurobusiness.json.JsonParser;
import com.rainwood.eurobusiness.okhttp.HttpResponse;
import com.rainwood.eurobusiness.okhttp.OnHttpListener;
import com.rainwood.eurobusiness.request.RequestPost;
import com.rainwood.eurobusiness.ui.adapter.StoresListAdapter;
import com.rainwood.eurobusiness.ui.adapter.WranSizeAdapter;
import com.rainwood.eurobusiness.utils.ListUtils;
import com.rainwood.tools.viewinject.ViewById;
import com.rainwood.tools.widget.MeasureGridView;
import com.rainwood.tools.widget.MeasureListView;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @Author: a797s
 * @Date: 2020/2/21
 * @Desc:
 */
public class WarnRepertoryDetailActivity extends BaseActivity implements View.OnClickListener, OnHttpListener {

    private String mWarnId;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_warn_detail;
    }

    @ViewById(R.id.iv_back)
    private ImageView pageBack;
    @ViewById(R.id.tv_title)
    private TextView pageTitle;
    // content
    @ViewById(R.id.iv_img)
    private ImageView image;
    @ViewById(R.id.tv_name)
    private TextView name;
    @ViewById(R.id.tv_status)
    private TextView status;
    @ViewById(R.id.lv_goods_info)
    private MeasureListView goodsInfo;
    @ViewById(R.id.lv_params)
    private MeasureListView params;
    @ViewById(R.id.gv_warn_params)
    private MeasureGridView warnParams;
    @ViewById(R.id.btn_replenish)
    private Button replenish;

    private WarinReBean warinRe;
    private String[] goodsTitles = {"商品型号", "商品分类", "条码"};
    private String[] paramTitle = {"批发价", "商品规格", "零售价", "税率", "增值税", "最小起订量"};

    private final int DETAIL_SIZE = 0x1124;

    @Override
    protected void initView() {
        pageBack.setOnClickListener(this);
        pageTitle.setText("库存详情");
        replenish.setOnClickListener(this);

        // request
        showLoading("loading");
        mWarnId = getIntent().getStringExtra("warnId");
        RequestPost.warnStockDetail(mWarnId, this);
    }

    @Override
    protected void initData() {
        super.initData();
        warinRe = new WarinReBean();
        warinRe.setImgPath(null);
        warinRe.setName("西装外套式系缀扣连衣裙西装外套式");
        warinRe.setStatus("在售中");
        // 商品信息
        List<CommonUIBean> goodsList = new ArrayList<>();
        for (int i = 0; i < goodsTitles.length; i++) {
            CommonUIBean commonUI = new CommonUIBean();
            commonUI.setTitle(goodsTitles[i]);
            goodsList.add(commonUI);
        }
        warinRe.setCommonUIList(goodsList);
        // 商品规格参数
        List<SpecificationBean> sizeList = new ArrayList<>();
        warinRe.setSizeList(sizeList);
        // 详情信息
        List<CommonUIBean> paramsList = new ArrayList<>();
        for (int i = 0; i < paramTitle.length; i++) {
            CommonUIBean commonUI = new CommonUIBean();
            commonUI.setTitle(paramTitle[i]);
            paramsList.add(commonUI);
        }
        warinRe.setParamsList(paramsList);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_back:
                finish();
                break;
            case R.id.btn_replenish:
                // toast("补货");
                Intent intent = new Intent(this, WarnReplishenActivity.class);
                intent.putExtra("warnId", mWarnId);
                startActivity(intent);
                break;
        }
    }

    @SuppressLint("HandlerLeak")
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(@NonNull Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case DETAIL_SIZE:
                    if (TextUtils.isEmpty(warinRe.getImgPath())) {
                        Glide.with(getBaseContext()).load(R.drawable.icon_loadding_fail).into(image);
                    } else {
                        Glide.with(getBaseContext()).load(warinRe.getImgPath()).into(image);
                    }
                    name.setText(warinRe.getName());
                    status.setText(warinRe.getStatus());
                    // 商品信息
                    StoresListAdapter listAdapter = new StoresListAdapter(WarnRepertoryDetailActivity.this, warinRe.getCommonUIList());
                    goodsInfo.setAdapter(listAdapter);
                    // 参数规格
                    WranSizeAdapter sizeAdapter = new WranSizeAdapter(WarnRepertoryDetailActivity.this, warinRe.getSizeList());
                    params.setAdapter(sizeAdapter);
                    // 详情参数
                    StoresListAdapter adapter = new StoresListAdapter(WarnRepertoryDetailActivity.this, warinRe.getParamsList());
                    warnParams.setAdapter(adapter);
                    warnParams.setNumColumns(2);
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
                // 获取预警库存详情
                if (result.url().contains("wxapi/v1/stock.php?type=getWarnStockInfo")) {
                    //
                    Map<String, String> goodsInfo = JsonParser.parseJSONObject(JsonParser.parseJSONObject(body.get("data")).get("goodsInfo"));
                    warinRe.setImgPath(goodsInfo.get("ico"));           // 图片地址
                    warinRe.setName(goodsInfo.get("name"));                 // 商品名称
                    warinRe.setStatus(goodsInfo.get("state"));                 // 商品状态
                    for (int i = 0; i < ListUtils.getSize(warinRe.getCommonUIList()); i++) {
                        switch (i) {
                            case 0:
                                warinRe.getCommonUIList().get(i).setShowText(goodsInfo.get("model"));
                                break;
                            case 1:
                                warinRe.getCommonUIList().get(i).setShowText(goodsInfo.get("goodsType"));
                                break;
                            case 2:
                                warinRe.getCommonUIList().get(i).setShowText(goodsInfo.get("barCode"));
                                break;
                        }
                    }
                    for (int i = 0; i < ListUtils.getSize(warinRe.getParamsList()); i++) {
                        switch (i) {
                            case 0:
                                warinRe.getParamsList().get(i).setShowText(goodsInfo.get("tradePrice"));
                                break;
                            case 1:
                                warinRe.getParamsList().get(i).setShowText(goodsInfo.get("goodsUnit"));
                                break;
                            case 2:
                                warinRe.getParamsList().get(i).setShowText(goodsInfo.get("retailPrice"));
                                break;
                            case 3:
                                warinRe.getParamsList().get(i).setShowText(goodsInfo.get("taxRate"));
                                break;
                            case 4:
                                warinRe.getParamsList().get(i).setShowText(goodsInfo.get("isTax"));
                                break;
                            case 5:
                                warinRe.getParamsList().get(i).setShowText(goodsInfo.get("startNum"));
                                break;
                        }
                    }
                    List<SpecificationBean> beanList = JsonParser.parseJSONArray(SpecificationBean.class,
                            JsonParser.parseJSONObject(body.get("data")).get("goodsSkuInfo"));
                    warinRe.setSizeList(beanList);
                    Message msg = new Message();
                    msg.what = DETAIL_SIZE;
                    mHandler.sendMessage(msg);
                }
            } else {
                toast(body.get("data"));
            }
            dismissLoading();
        }
    }
}
