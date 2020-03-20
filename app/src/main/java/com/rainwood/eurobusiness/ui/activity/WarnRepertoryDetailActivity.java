package com.rainwood.eurobusiness.ui.activity;

import android.annotation.SuppressLint;
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
import com.rainwood.eurobusiness.domain.SizeBean;
import com.rainwood.eurobusiness.domain.WarinReBean;
import com.rainwood.eurobusiness.ui.adapter.SizeAdapter;
import com.rainwood.eurobusiness.ui.adapter.StoresListAdapter;
import com.rainwood.tools.viewinject.ViewById;
import com.rainwood.tools.widget.MeasureGridView;
import com.rainwood.tools.widget.MeasureListView;

import java.util.ArrayList;
import java.util.List;

/**
 * @Author: a797s
 * @Date: 2020/2/21
 * @Desc:
 */
public class WarnRepertoryDetailActivity extends BaseActivity implements View.OnClickListener {

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

    private final int DETAIL_SIZE = 0x1124;

    @Override
    protected void initView() {
        pageBack.setOnClickListener(this);
        pageTitle.setText("库存详情");
        replenish.setOnClickListener(this);

        //
        Message msg = new Message();
        msg.what = DETAIL_SIZE;
        mHandler.sendMessage(msg);
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
            commonUI.setShowText(goodsLabel[i]);
            goodsList.add(commonUI);
        }
        warinRe.setCommonUIList(goodsList);
        // 商品规格参数
        List<SizeBean> sizeList = new ArrayList<>();
        for (int i = 0; i < 3; i++) {
            SizeBean size = new SizeBean();
            size.setColor("枫叶红");
            size.setSize("S");
            size.setRepertoryBelow("10");
            size.setWholsePrice("120.00€");
            size.setRepertory("10");
            sizeList.add(size);
        }
        warinRe.setSizeList(sizeList);
        // 详情信息
        List<CommonUIBean> paramsList = new ArrayList<>();
        for (int i = 0; i < paramTitle.length; i++) {
            CommonUIBean commonUI = new CommonUIBean();
            commonUI.setTitle(paramTitle[i]);
            commonUI.setShowText(paramLabel[i]);
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
                openActivity(WarnReplishenActivity.class);
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
                    SizeAdapter sizeAdapter = new SizeAdapter(WarnRepertoryDetailActivity.this, warinRe.getSizeList());
                    params.setAdapter(sizeAdapter);
                    // 详情参数
                    StoresListAdapter adapter = new StoresListAdapter(WarnRepertoryDetailActivity.this, warinRe.getParamsList());
                    warnParams.setAdapter(adapter);
                    warnParams.setNumColumns(2);
                    break;
            }
        }
    };

    /*
    模拟数据集
     */
    private WarinReBean warinRe;
    private String[] goodsTitles = {"商品型号", "商品分类", "条码"};
    private String[] goodsLabel = {"XDF-226023", "女士时装-连衣裙", "6920584471071"};
    private String[] paramTitle = {"批发价", "商品规格", "零售价", "税率", "增值税", "最小起订量"};
    private String[] paramLabel = {"98.00€", "箱", "120.00€", "16%", "含税", "500"};
}
