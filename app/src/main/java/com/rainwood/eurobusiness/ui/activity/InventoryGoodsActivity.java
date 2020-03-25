package com.rainwood.eurobusiness.ui.activity;

import android.annotation.SuppressLint;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.rainwood.eurobusiness.R;
import com.rainwood.eurobusiness.base.BaseActivity;
import com.rainwood.eurobusiness.domain.CommonUIBean;
import com.rainwood.eurobusiness.domain.GoodsDetailBean;
import com.rainwood.eurobusiness.domain.ImageBean;
import com.rainwood.eurobusiness.domain.SpecialBean;
import com.rainwood.eurobusiness.json.JsonParser;
import com.rainwood.eurobusiness.okhttp.HttpResponse;
import com.rainwood.eurobusiness.okhttp.OnHttpListener;
import com.rainwood.eurobusiness.request.RequestPost;
import com.rainwood.eurobusiness.ui.adapter.ItemGoodsDetailAdapter;
import com.rainwood.eurobusiness.ui.adapter.SpecialAdapter;
import com.rainwood.eurobusiness.utils.ListUtils;
import com.rainwood.tools.viewinject.ViewById;
import com.rainwood.tools.widget.MeasureListView;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @Author: a797s
 * @Date: 2020/2/18
 * @Desc: 库存商品 ---- 库存信息、商品信息
 */
public class InventoryGoodsActivity extends BaseActivity implements View.OnClickListener, OnHttpListener {

    @Override
    protected int getLayoutId() {
        return R.layout.activity_inventory_goods;
    }

    @ViewById(R.id.iv_back)
    private ImageView pageBack;
    @ViewById(R.id.tv_inventory)
    private TextView inventory;         // 库存信息
    @ViewById(R.id.tv_goods)
    private TextView goods;             // 商品信息
    @ViewById(R.id.lv_content)
    private MeasureListView contentList;

    @ViewById(R.id.lv_size_list)
    private MeasureListView sizeList;       // 库存信息
    @ViewById(R.id.ll_inventory)
    private LinearLayout inventorySize;     // 库存信息标题

    private final int INVENTORY_SIZE = 0x1124;        // 默认码(库存信息码)
    private final int GOODS_SIZE = 0x0929;          // 商品信息码

    @Override
    protected void initView() {
        pageBack.setOnClickListener(this);
        inventory.setOnClickListener(this);
        goods.setOnClickListener(this);

        // request
        String stockId = getIntent().getStringExtra("stockId");
        if (stockId != null) {
            showLoading("loading");
            RequestPost.getStockDetail(stockId, "", this);
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
            goodsDetail.setLoadType(101);
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
            List<ImageBean> imgList = new ArrayList<>();    // 商品图片
            for (int j = 0; j < 10 && i == 3; j++) {
                ImageBean image = new ImageBean();
                image.setImgPath(String.valueOf(R.drawable.icon_loadding_fail));
                imgList.add(image);
            }
            goodsDetail.setImgList(imgList);
            goodsDetail.setCommList(commLists);
            mList.add(goodsDetail);
        }
    }

    @Override
    public void onClick(View v) {
        Message msg = new Message();
        switch (v.getId()) {
            case R.id.iv_back:
                finish();
                break;
            case R.id.tv_inventory:         // 库存信息
                inventory.setBackgroundResource(R.drawable.shape_radius_red_left_4);
                inventory.setTextColor(getResources().getColor(R.color.white));
                goods.setBackgroundResource(R.drawable.shape_radius_red_right_empty_4);
                goods.setTextColor(getResources().getColor(R.color.red30));
                // 隐藏商品信息
                msg.what = INVENTORY_SIZE;
                mHandler.sendMessage(msg);
                break;
            case R.id.tv_goods:             // 商品信息
                inventory.setBackgroundResource(R.drawable.shape_radius_red_left_empty_4);
                inventory.setTextColor(getResources().getColor(R.color.red30));
                goods.setBackgroundResource(R.drawable.shape_radius_red_right_4);
                goods.setTextColor(getResources().getColor(R.color.white));
                // 隐藏库存信息
                msg.what = GOODS_SIZE;
                mHandler.sendMessage(msg);
                break;
        }
    }


    @SuppressLint("HandlerLeak")
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(@NonNull Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case INVENTORY_SIZE:                // 默认状态
                    contentList.setVisibility(View.GONE);
                    sizeList.setVisibility(View.VISIBLE);
                    inventorySize.setVisibility(View.VISIBLE);
                    // 库存信息
                    SpecialAdapter sizeAdapter = new SpecialAdapter(InventoryGoodsActivity.this, mSpeciaList);
                    sizeList.setAdapter(sizeAdapter);
                    break;
                case GOODS_SIZE:                // 商品信息
                    contentList.setVisibility(View.VISIBLE);
                    sizeList.setVisibility(View.GONE);
                    inventorySize.setVisibility(View.GONE);
                    // 商品信息
                    ItemGoodsDetailAdapter detailAdapter = new ItemGoodsDetailAdapter(InventoryGoodsActivity.this, mList);
                    contentList.setAdapter(detailAdapter);
                    break;
            }
        }
    };

    private List<GoodsDetailBean> mList;
    private String[] mainTitles = {"基本信息", "商品定价", "规格参数", "商品图片"};  //， "创建者"
    // 基本信息
    private String[] baseTitle = {"商品名称", "商品型号", "商品分类", "条码"};
    // 商品定价
    private String[] priceTitles = {"批发价", "零售价", "增值税"};
    // 规格参数
    private String[] specialTitles = {"商品尺码", "商品规格", "最小起订量", "税率"};
    // 库存信息
    private List<SpecialBean> mSpeciaList;

    @Override
    public void onHttpFailure(HttpResponse result) {

    }

    @Override
    public void onHttpSucceed(HttpResponse result) {
        Map<String, String> body = JsonParser.parseJSONObject(result.body());
        if (body != null) {
            if ("1".equals(body.get("code"))) {
                // 获取库存详情
                if (result.url().contains("wxapi/v1/stock.php?type=getStockInfo")) {
                    // 规格参数
                    mSpeciaList = JsonParser.parseJSONArray(SpecialBean.class,
                            JsonParser.parseJSONObject(body.get("data")).get("skulist"));
                    // 基本信息
                    Map<String, String> goodsInfo = JsonParser.parseJSONObject(JsonParser.parseJSONObject(body.get("data")).get("goodsInfo"));
                    // 商品信息
                    for (int i = 0; i < ListUtils.getSize(mList); i++) {
                        switch (i) {
                            case 0:         // 基本信息
                                for (int j = 0; j < ListUtils.getSize(mList.get(i).getCommList()); j++) {
                                    switch (j) {
                                        case 0:
                                            mList.get(i).getCommList().get(j).setShowText(goodsInfo.get("name"));
                                            break;
                                        case 1:
                                            mList.get(i).getCommList().get(j).setShowText(goodsInfo.get("model"));
                                            break;
                                        case 2:
                                            mList.get(i).getCommList().get(j).setShowText(goodsInfo.get("goodsType"));
                                            break;
                                        case 3:
                                            mList.get(i).getCommList().get(j).setShowText(goodsInfo.get("barCode"));
                                            break;
//                                        case 4:
//                                            mList.get(i).getCommList().get(j).setShowText(goodsInfo.get("name"));
//                                            break;
                                    }
                                }
                                break;
                            case 1:         // 商品定价
                                for (int j = 0; j < ListUtils.getSize(mList.get(i).getCommList()); j++) {
                                    switch (j) {
                                        case 0:
                                            mList.get(i).getCommList().get(j).setShowText(goodsInfo.get("tradePrice"));
                                            break;
                                        case 1:
                                            mList.get(i).getCommList().get(j).setShowText(goodsInfo.get("retailPrice"));
                                            break;
                                        case 2:
                                            mList.get(i).getCommList().get(j).setShowText(goodsInfo.get("isTax"));
                                            break;
                                    }
                                }
                                break;
                            case 2:         // 规格参数
                                for (int j = 0; j < ListUtils.getSize(mList.get(i).getCommList()); j++) {
                                    switch (j) {
                                        case 0:
                                            mList.get(i).getCommList().get(j).setShowText(goodsInfo.get("goodsOption"));
                                            break;
                                        case 1:
                                            mList.get(i).getCommList().get(j).setShowText(goodsInfo.get("goodsUnit"));
                                            break;
                                        case 2:
                                            mList.get(i).getCommList().get(j).setShowText(goodsInfo.get("startNum"));
                                            break;
                                    }
                                }
                                break;
                            case 3:         // 商品图片
                                List<ImageBean> imageList = JsonParser.parseJSONArray(ImageBean.class,
                                        JsonParser.parseJSONObject(body.get("data")).get("icolist"));
                                mList.get(i).setImgList(imageList);
                                break;
                        }
                    }
                    Message msg = new Message();
                    msg.what = INVENTORY_SIZE;
                    mHandler.sendMessage(msg);
                }
            } else {
                toast(body.get("warn"));
            }
            dismissLoading();
        }
    }
}
