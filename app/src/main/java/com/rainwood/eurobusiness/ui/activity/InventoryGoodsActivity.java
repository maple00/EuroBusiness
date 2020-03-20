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
import com.rainwood.eurobusiness.domain.SizeBean;
import com.rainwood.eurobusiness.ui.adapter.ItemGoodsDetailAdapter;
import com.rainwood.eurobusiness.ui.adapter.SizeAdapter;
import com.rainwood.tools.viewinject.ViewById;
import com.rainwood.tools.widget.MeasureListView;

import java.util.ArrayList;
import java.util.List;

/**
 * @Author: a797s
 * @Date: 2020/2/18
 * @Desc: 库存商品 ---- 库存信息、商品信息
 */
public class InventoryGoodsActivity extends BaseActivity implements View.OnClickListener {

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
        // 默认加载库存信息
        Message msg = new Message();
        msg.what = INVENTORY_SIZE;
        mHandler.sendMessage(msg);
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
                commonUI.setShowText(baseInfos[j]);
                commLists.add(commonUI);
            }
            for (int j = 0; j < priceTitles.length && i == 1; j++) {        // 商品定价
                CommonUIBean commonUI = new CommonUIBean();
                commonUI.setTitle(priceTitles[j]);
                commonUI.setShowText(priceInfos[j]);
                commLists.add(commonUI);
            }
            // 规格参数
            for (int j = 0; j < specialTitles.length && i == 2; j++) {
                CommonUIBean commonUI = new CommonUIBean();
                commonUI.setTitle(specialTitles[j]);
                commonUI.setShowText(specialInfos[j]);
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
        // 库存信息
        sizeLists = new ArrayList<>();
        for (int j = 0; j < colors.length; j++) {             // 商品规格
            SizeBean sizeBean = new SizeBean();
            sizeBean.setColor(colors[j]);
            sizeBean.setSize(sizes[j]);
            sizeBean.setRepertoryBelow(repertoryBelow[j]);
            sizeBean.setWholsePrice(wholsale[j]);
            sizeBean.setRepertory(repertory[j]);
            sizeLists.add(sizeBean);
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
                    SizeAdapter sizeAdapter = new SizeAdapter(InventoryGoodsActivity.this, sizeLists);
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

    /*
    模拟数据
    */
    private List<GoodsDetailBean> mList;
    private String[] mainTitles = {"基本信息", "商品定价", "规格参数", "商品图片"};
    // 基本信息
    private String[] baseTitle = {"商品名称", "商品型号", "商品分类", "条码", "创建者"};
    private String[] baseInfos = {"西装外套式系缀扣连衣裙连衣裙裙...", "XDF-226023", "女士时装-连衣裙",
            "6920584471071", "批发商"};
    // 商品定价
    private String[] priceTitles = {"批发价", "零售价", "增值税"};
    private String[] priceInfos = {"98.00€", "98.00€", "含税"};
    // 规格参数
    private String[] specialTitles = {"商品尺码", "商品规格", "税率", "最小起订量"};
    private String[] specialInfos = {"有尺码", "箱", "16%", "500"};
    // 库存信息
    private List<SizeBean> sizeLists;
    private String[] colors = {"枫叶红", "枫叶红", "粉色", "粉色", "粉色"};
    private String[] sizes = {"S", "M", "S", "S", "S"};
    private String[] repertoryBelow = {"10", "10", "8", "8", "8"};
    private String[] wholsale = {"120.00€", "120.00€", "120.00€", "120.00€", "120.00€"};
    private String[] repertory = {"900", "900", "900", "900", "900"};

}
