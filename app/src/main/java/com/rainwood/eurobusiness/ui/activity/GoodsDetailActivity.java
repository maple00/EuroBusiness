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
import com.rainwood.eurobusiness.domain.CommonUIBean;
import com.rainwood.eurobusiness.domain.GoodBean;
import com.rainwood.eurobusiness.domain.GoodsDetailBean;
import com.rainwood.eurobusiness.domain.GoodsPromotionBean;
import com.rainwood.eurobusiness.domain.ImagesBean;
import com.rainwood.eurobusiness.domain.SpecialBean;
import com.rainwood.eurobusiness.json.JsonParser;
import com.rainwood.eurobusiness.okhttp.HttpResponse;
import com.rainwood.eurobusiness.okhttp.OnHttpListener;
import com.rainwood.eurobusiness.request.RequestPost;
import com.rainwood.eurobusiness.ui.adapter.ItemGoodsDetailAdapter;
import com.rainwood.eurobusiness.utils.ListUtils;
import com.rainwood.tools.viewinject.ViewById;
import com.rainwood.tools.widget.MeasureListView;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @Author: a797s
 * @Date: 2020/2/17
 * @Desc: 商品详情 --- 下架，在售
 */
public class GoodsDetailActivity extends BaseActivity implements View.OnClickListener, OnHttpListener {

    @Override
    protected int getLayoutId() {
        return R.layout.activity_goods_detail;
    }

    @ViewById(R.id.iv_back)
    private ImageView pageBack;
    @ViewById(R.id.tv_title)
    private TextView pageTitle;
    @ViewById(R.id.tv_right_text)
    private TextView rightText;
    @ViewById(R.id.lv_info_list)
    private MeasureListView infosList;

    private final int INITIAL_SIZE = 0x101;

    @Override
    protected void initView() {
        pageBack.setOnClickListener(this);
        pageTitle.setText("商品详情");


        // request
        String goodsId = getIntent().getStringExtra("goodsId");
        showLoading("loading");
        RequestPost.getGoodsDetail(goodsId, this);
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
    protected void initData() {
        super.initData();
        mList = new ArrayList<>();
        for (int i = 0; i < mainTitles.length; i++) {
            GoodsDetailBean goodsDetail = new GoodsDetailBean();
            goodsDetail.setTitle(mainTitles[i]);
            goodsDetail.setType(i);
            List<CommonUIBean> commLists = new ArrayList<>();
            for (int j = 0; j < baseTitle.length && i == 0; j++) {      // 基本信息
                CommonUIBean commonUI = new CommonUIBean();
                commonUI.setTitle(baseTitle[j]);
                commLists.add(commonUI);
            }
            for (int j = 0; j < priceTitles.length && i == 1; j++) {        // 商品定价
                CommonUIBean commonUI = new CommonUIBean();
                commonUI.setTitle(priceTitles[j]);
                commLists.add(commonUI);
            }
            // 规格参数
            for (int j = 0; j < specialTitles.length && i == 2; j++) {
                CommonUIBean commonUI = new CommonUIBean();
                commonUI.setTitle(specialTitles[j]);
                commLists.add(commonUI);
            }
            for (int j = 0; j < promoTitles.length && i == 4; j++) {
                CommonUIBean commonUI = new CommonUIBean();
                commonUI.setTitle(promoTitles[j]);
                commLists.add(commonUI);
            }
            goodsDetail.setCommList(commLists);
            mList.add(goodsDetail);
        }
    }

    private List<GoodsDetailBean> mList;
    private String[] mainTitles = {"基本信息", "商品定价", "规格参数", "商品图片", "促销信息"};
    // 基本信息
    private String[] baseTitle = {"商品名称", "商品型号", "商品分类", "条码"};
    // 商品定价
    private String[] priceTitles = {"批发价", "零售价", "增值税"};
    private String[] specialTitles = {"商品尺码", "商品规格", "最小起订量"};
    // 促销信息
    private String[] promoTitles = {null, "开始时间", "结束时间", "促销价", "折扣"};

    @SuppressLint("HandlerLeak")
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(@NonNull Message msg) {
            switch (msg.what) {
                case INITIAL_SIZE:
                    ItemGoodsDetailAdapter detailAdapter = new ItemGoodsDetailAdapter(GoodsDetailActivity.this, mList);
                    infosList.setAdapter(detailAdapter);
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
                // 查询商品详情
                if (result.url().contains("wxapi/v1/goods.php?type=getGoodsInfo")) {
                    Log.d(TAG, "result ---- " + result);
                    // 规格参数
                    List<SpecialBean> specialList = JsonParser.parseJSONArray(SpecialBean.class, JsonParser.parseJSONObject(body.get("data")).get("goodsSkulist"));
                    // 图片
                    List<ImagesBean> imageList = JsonParser.parseJSONArray(ImagesBean.class, JsonParser.parseJSONObject(body.get("data")).get("goodsWinlist"));
                    // 促销信息
                    GoodsPromotionBean goodsPromotion = JsonParser.parseJSONObject(GoodsPromotionBean.class, JsonParser.parseJSONObject(body.get("data")).get("goodsPromotion"));
                    // 基本信息
                    GoodBean goodsInfo = JsonParser.parseJSONObject(GoodBean.class, JsonParser.parseJSONObject(body.get("data")).get("goodsInfo"));
                    // 赋值 -- common 格式
                    for (int i = 0; i < ListUtils.getSize(mList); i++) {
                        switch (i) {
                            case 0:         // 基本信息
                                for (int j = 0; j < ListUtils.getSize(mList.get(i).getCommList()); j++) {
                                    switch (j) {
                                        case 0:
                                            mList.get(i).getCommList().get(j).setShowText(goodsInfo.getName());
                                            break;
                                        case 1:
                                            mList.get(i).getCommList().get(j).setShowText(goodsInfo.getModel());
                                            break;
                                        case 2:
                                            mList.get(i).getCommList().get(j).setShowText(goodsInfo.getGoodsTypeOne() + "-" + goodsInfo.getGoodsTypeTwo());
                                            break;
                                        case 3:
                                            mList.get(i).getCommList().get(j).setShowText(goodsInfo.getBarCode());
                                            break;
                                    }
                                }
                                break;
                            case 1:         // 商品定价
                                for (int j = 0; j < ListUtils.getSize(mList.get(i).getCommList()); j++) {
                                    switch (j) {
                                        case 0:
                                            mList.get(i).getCommList().get(j).setShowText(goodsInfo.getTradePrice());
                                            break;
                                        case 1:
                                            mList.get(i).getCommList().get(j).setShowText(goodsInfo.getRetailPrice());
                                            break;
                                        case 2:
                                            mList.get(i).getCommList().get(j).setShowText("0".equals(goodsInfo.getIsTax()) ? "不含税" : "含税");
                                            break;
                                    }
                                }
                                break;
                            case 2:         // 规格参数
                                mList.get(i).setParamsList(specialList);
                                break;
                            case 3:         // 商品图片
                                mList.get(i).setImgList(imageList);
                                break;
                            case 4:         // 促销信息
                                if (goodsPromotion != null)
                                    for (int j = 0; j < ListUtils.getSize(mList.get(i).getCommList()); j++) {
                                        switch (j) {
                                            case 0:
                                                mList.get(i).getCommList().get(j).setShowText(goodsPromotion.getStartTime());
                                                break;
                                            case 1:
                                                mList.get(i).getCommList().get(j).setShowText(goodsPromotion.getEndTime());
                                                break;
                                            case 2:
                                                mList.get(i).getCommList().get(j).setShowText(goodsPromotion.getPrice());
                                                break;
                                            case 3:
                                                mList.get(i).getCommList().get(j).setShowText(goodsPromotion.getDiscount());
                                                break;
                                        }
                                    }
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
