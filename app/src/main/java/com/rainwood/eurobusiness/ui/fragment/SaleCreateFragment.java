package com.rainwood.eurobusiness.ui.fragment;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.ImageView;

import androidx.annotation.NonNull;

import com.rainwood.eurobusiness.R;
import com.rainwood.eurobusiness.base.BaseFragment;
import com.rainwood.eurobusiness.domain.CommonUIBean;
import com.rainwood.eurobusiness.domain.PressBean;
import com.rainwood.eurobusiness.domain.SaleGoodsBean;
import com.rainwood.eurobusiness.ui.activity.HomeActivity;
import com.rainwood.eurobusiness.ui.activity.NewShopActivity;
import com.rainwood.eurobusiness.ui.adapter.SaleGoodsAdapter;
import com.rainwood.eurobusiness.ui.adapter.TopTypeAdapter;
import com.rainwood.tools.widget.MeasureGridView;
import com.rainwood.tools.widget.MeasureListView;

import java.util.ArrayList;
import java.util.List;

/**
 * @Author: shearson
 * @Time: 2020/2/26 11:55
 * @Desc: 批发商端创建的商品
 */
public class SaleCreateFragment extends BaseFragment implements View.OnClickListener {

    @Override
    protected int initLayout() {
        return R.layout.fragment_goods_manager;
    }

    private MeasureGridView topType;            // topType
    private MeasureListView contentList;        // contentList

    // mHandler Size
    private final int SALE_SIZE = 0x1124;           // 由批发商创建

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
        // 批发商Content
        mList = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            SaleGoodsBean saleGoods = new SaleGoodsBean();
            saleGoods.setType(0);
            saleGoods.setImgPath(String.valueOf(R.drawable.icon_loadding_fail));
            if (i == 2) {
                saleGoods.setStatus("");
            } else if (i == 3) {
                saleGoods.setStatus("已下架");
            } else {
                saleGoods.setStatus("在售中");
            }
            saleGoods.setName("真皮雪地靴短筒靴加厚棉鞋短靴子...");
            List<CommonUIBean> priceList = new ArrayList<>();
            for (int j = 0; j < priceTitles.length; j++) {
                CommonUIBean commonUI = new CommonUIBean();
                commonUI.setTitle(priceTitles[j]);
                commonUI.setShowText("104.00€");
                priceList.add(commonUI);
            }
            saleGoods.setPriceList(priceList);
            saleGoods.setStoreName("双木衣馆门店");
            mList.add(saleGoods);
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
                case SALE_SIZE:                     // 由批发商创建
                    // 头部类型
                    TopTypeAdapter typeAdapter = new TopTypeAdapter(getActivity(), topTypeList);
                    topType.setAdapter(typeAdapter);
                    topType.setNumColumns(4);
                    typeAdapter.setOnClickItem(position -> {
                        for (PressBean pressBean : topTypeList) {
                            pressBean.setChoose(false);
                        }
                        topTypeList.get(position).setChoose(true);
                    });
                    // content
                    SaleGoodsAdapter goodsAdapter = new SaleGoodsAdapter(getContext(), mList);
                    contentList.setAdapter(goodsAdapter);

                    break;
            }
        }
    };

    /*
    模拟数据
     */
    private List<PressBean> topTypeList;
    private String[] topTypes = {"全部", "在售中", "已下架"};
    // 批发商
    private List<SaleGoodsBean> mList;
    private String[] priceTitles = {"进", "批", "零"};

}
