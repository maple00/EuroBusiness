package com.rainwood.eurobusiness.ui.activity;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.rainwood.eurobusiness.R;
import com.rainwood.eurobusiness.base.BaseActivity;
import com.rainwood.eurobusiness.domain.CommonUIBean;
import com.rainwood.eurobusiness.domain.GoodsDetailBean;
import com.rainwood.eurobusiness.domain.ImageBean;
import com.rainwood.eurobusiness.domain.SizeBean;
import com.rainwood.eurobusiness.ui.adapter.ItemGoodsDetailAdapter;
import com.rainwood.tools.viewinject.ViewById;
import com.rainwood.tools.widget.MeasureListView;

import java.util.ArrayList;
import java.util.List;

/**
 * @Author: a797s
 * @Date: 2020/2/17
 * @Desc: 商品详情 --- 下架，在售
 */
public class GoodsDetailActivity extends BaseActivity implements View.OnClickListener {

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

    @Override
    protected void initView() {
        pageBack.setOnClickListener(this);
        pageTitle.setText("商品详情");

        ItemGoodsDetailAdapter detailAdapter = new ItemGoodsDetailAdapter(this, mList);
        infosList.setAdapter(detailAdapter);
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
            List<SizeBean> sizeBeanList = new ArrayList<>();
            for (int j = 0; j < colors.length && i == 2; j++) {             // 商品规格
                SizeBean sizeBean = new SizeBean();
                sizeBean.setColor(colors[j]);
                sizeBean.setSize(sizes[j]);
                sizeBean.setRepertoryBelow(repertoryBelow[j]);
                sizeBean.setWholsePrice(wholsale[j]);
                sizeBean.setRepertory(repertory[j]);
                sizeBeanList.add(sizeBean);
            }
            goodsDetail.setParamsList(sizeBeanList);
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
            for (int j = 0; j < promoTitles.length && i == 4; j++) {
                CommonUIBean commonUI = new CommonUIBean();
                commonUI.setTitle(promoTitles[j]);
                commonUI.setShowText(promoInfos[j]);
                commLists.add(commonUI);
            }
            goodsDetail.setCommList(commLists);
            mList.add(goodsDetail);
        }
    }

    /*
    模拟数据
     */
    private List<GoodsDetailBean> mList;
    private String[] mainTitles = {"基本信息", "商品定价", "规格参数", "商品图片", "促销信息"};
    // 基本信息
    private String[] baseTitle = {"商品名称", "商品型号", "商品分类", "条码", "创建者"};
    private String[] baseInfos = {"西装外套式系缀扣连衣裙连衣裙裙...", "XDF-226023", "女士时装-连衣裙",
                "6920584471071", "批发商"};
    // 商品定价
    private String[] priceTitles = {"批发价", "零售价", "增值税"};
    private String[] priceInfos = {"98.00€", "98.00€", "含税"};
    private String[] colors = {"枫叶红", "枫叶红", "粉色", "粉色", "粉色"};
    private String[] sizes = {"S", "M", "S", "S", "S"};
    private String[] repertoryBelow = {"10", "10", "8", "8", "8"};
    private String[] wholsale = {"120.00€", "120.00€", "120.00€", "120.00€", "120.00€"};
    private String[] repertory = {"900", "900", "900", "900", "900"};
    private String[] specialTitles = {"商品尺码", "商品规格", "税率", "最小起订量"};
    private String[] specialInfos = {"有尺码", "箱", "16%", "500"};
    // 促销信息
    private String[] promoTitles = {null, "开始时间", "结束时间", "促销价", "折扣"};
    private String[] promoInfos = {"正在促销", "2019年12月23日 15:00", "2019年12月23日 15:00", "128.00€", "15%"};
}
