package com.rainwood.eurobusiness.ui.fragment;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import androidx.annotation.NonNull;

import com.rainwood.eurobusiness.R;
import com.rainwood.eurobusiness.base.BaseFragment;
import com.rainwood.eurobusiness.domain.CommonUIBean;
import com.rainwood.eurobusiness.domain.GoodsBean;
import com.rainwood.eurobusiness.domain.PressBean;
import com.rainwood.eurobusiness.domain.SaleGoodsBean;
import com.rainwood.eurobusiness.json.JsonParser;
import com.rainwood.eurobusiness.okhttp.HttpResponse;
import com.rainwood.eurobusiness.okhttp.OnHttpListener;
import com.rainwood.eurobusiness.request.RequestPost;
import com.rainwood.eurobusiness.ui.activity.HomeActivity;
import com.rainwood.eurobusiness.ui.activity.NewShopActivity;
import com.rainwood.eurobusiness.ui.adapter.SaleGoodsAdapter;
import com.rainwood.eurobusiness.ui.adapter.TopTypeAdapter;
import com.rainwood.eurobusiness.utils.ListUtils;
import com.rainwood.tools.widget.MeasureGridView;
import com.rainwood.tools.widget.MeasureListView;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @Author: shearson
 * @Time: 2020/2/26 16:06
 * @Desc: 门店端 商品管理
 */
public class StoreCreateFragment extends BaseFragment implements View.OnClickListener, OnHttpListener {

    @Override
    protected int initLayout() {
        return R.layout.fragment_goods_manager;
    }

    private MeasureGridView topType;            // topType
    private MeasureListView contentList;        // contentList

    // mHandler Size
    private final int SALE_SIZE = 0x1124;           // 由门店创建
    private final int INITIAL_SIZE = 0x101;         // initial

    private List<PressBean> topTypeList;
    private String[] topTypes = {"全部", "在售中", "已下架"};
    // 批发商
    private List<SaleGoodsBean> mList;
    private String[] priceTitles = {"进", "批", "零"};

    @Override
    protected void initView(View view) {
        ImageView pageBack = view.findViewById(R.id.iv_back);
        pageBack.setOnClickListener(this);
        ImageView newGoods = view.findViewById(R.id.iv_new_shop);
        newGoods.setOnClickListener(this);
        topType = view.findViewById(R.id.gv_top_type);
        ImageView screening = view.findViewById(R.id.iv_screening);
        screening.setOnClickListener(this);
        contentList = view.findViewById(R.id.lv_content_list);
        Message msg = new Message();
        msg.what = SALE_SIZE;
        mHandler.sendMessage(msg);
        // request
        showLoading("");
        RequestPost.getGoodsList("store", "", "", new ArrayList<>(), this);
    }

    @Override
    protected void initData(Context mContext) {
        topTypeList = new ArrayList<>();
        for (int i = 0; i < topTypes.length; i++) {
            PressBean press = new PressBean();
            press.setTitle(topTypes[i]);
            if (i == 0) {
                press.setChoose(true);
            }
            topTypeList.add(press);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_back:
                startActivity(HomeActivity.class);
                break;
            case R.id.iv_new_shop:
                // toast("新建商品");
                startActivity(NewShopActivity.class);
                break;
            case R.id.iv_screening:
                toast("筛选");
                break;
        }
    }


    @SuppressLint("HandlerLeak")
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(@NonNull Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case SALE_SIZE:                     // 由门店创建
                    // 头部类型
                    TopTypeAdapter typeAdapter = new TopTypeAdapter(getActivity(), topTypeList);
                    topType.setAdapter(typeAdapter);
                    topType.setNumColumns(4);
                    typeAdapter.setOnClickItem(position -> {
                        for (PressBean pressBean : topTypeList) {
                            pressBean.setChoose(false);
                        }
                        topTypeList.get(position).setChoose(true);
                        String state;
                        switch (position) {
                            case 1:
                                state = "online";
                                break;
                            case 2:
                                state = "offline";
                                break;
                            default:
                                state = "";
                                break;
                        }
                        // request
                        showLoading("");
                        RequestPost.getGoodsList("mine", state, "", new ArrayList<>(), StoreCreateFragment.this);
                    });
                    break;
                case INITIAL_SIZE:
                    // content
                    SaleGoodsAdapter goodsAdapter = new SaleGoodsAdapter(getContext(), mList);
                    contentList.setAdapter(goodsAdapter);
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
                // 获取门店创建的商品列表
                if (result.url().contains("wxapi/v1/goods.php?type=getGoodsList")) {
                    List<GoodsBean> goodsList = JsonParser.parseJSONArray(GoodsBean.class, JsonParser.parseJSONObject(body.get("data")).get("goodslist"));
                    mList = new ArrayList<>();
                    for (int i = 0; i < ListUtils.getSize(goodsList); i++) {
                        SaleGoodsBean saleGoods = new SaleGoodsBean();
                        saleGoods.setType(0);
                        saleGoods.setImgPath(goodsList.get(i).getIco());
                        saleGoods.setStatus(goodsList.get(i).getState());
                        saleGoods.setName(goodsList.get(i).getName());
                        List<CommonUIBean> priceList = new ArrayList<>();
                        for (int j = 0; j < priceTitles.length; j++) {
                            CommonUIBean commonUI = new CommonUIBean();
                            commonUI.setTitle(priceTitles[j]);
                            if (j == 0) {        // 进价
                                commonUI.setShowText(goodsList.get(i).getPrice());
                            }
                            if (j == 1) {
                                commonUI.setShowText(goodsList.get(i).getTradePrice());
                            }
                            if (j == 2) {
                                commonUI.setShowText(goodsList.get(i).getRetailPrice());
                            }
                            priceList.add(commonUI);
                        }
                        saleGoods.setPriceList(priceList);
                        saleGoods.setStoreName(goodsList.get(i).getStore());
                        mList.add(saleGoods);
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
