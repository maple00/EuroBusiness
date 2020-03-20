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
import com.rainwood.eurobusiness.domain.SaleGoodsBean;
import com.rainwood.eurobusiness.ui.activity.HomeActivity;
import com.rainwood.eurobusiness.ui.activity.NewShopActivity;
import com.rainwood.eurobusiness.ui.adapter.DraftAdapter;
import com.rainwood.tools.widget.MeasureListView;

import java.util.ArrayList;
import java.util.List;

/**
 * @Author: shearson
 * @Time: 2020/2/26 16:10
 * @Desc: 草稿
 */
public class DraftFragment extends BaseFragment implements View.OnClickListener {

    @Override
    protected int initLayout() {
        return R.layout.fragment_draft_goods;
    }

    private MeasureListView contentList;        // contentList

    // mHandler Size
    private final int SALE_SIZE = 0x1124;           // 由批发商创建

    @Override
    protected void initView(View view) {
        ImageView newFound = view.findViewById(R.id.iv_new_shop);
        newFound.setOnClickListener(this);
        ImageView pageBack = view.findViewById(R.id.iv_back);
        pageBack.setOnClickListener(this);
        contentList = view.findViewById(R.id.lv_content_list);

        // content
        Message msg = new Message();
        msg.what = SALE_SIZE;
        mHandler.sendMessage(msg);
    }

    @Override
    protected void initData(Context mContext) {
        // 批发商Content
        mList = new ArrayList<>();
        for (int i = 0; i < 2; i++) {
            SaleGoodsBean goods = new SaleGoodsBean();
            goods.setName("西装外套式系缀扣连衣裙");
            List<CommonUIBean> commonUIList = new ArrayList<>();
            for (int j = 0; j < priceTitles.length; j++) {
                CommonUIBean commonUI = new CommonUIBean();
                commonUI.setTitle(priceTitles[j]);
                commonUI.setShowText("104.00€");
                commonUIList.add(commonUI);
            }
            goods.setPriceList(commonUIList);
            mList.add(goods);
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
        }
    }


    @SuppressLint("HandlerLeak")
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(@NonNull Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case SALE_SIZE:                     // 由批发商创建
                    // content
                    DraftAdapter draftAdapter = new DraftAdapter(getContext(), mList);
                    contentList.setAdapter(draftAdapter);
                    draftAdapter.setOnClickItem(new DraftAdapter.OnClickItem() {
                        @Override
                        public void onClickDetail(int position) {           // 查看详情
                            toast("查看详情");
                        }

                        @Override
                        public void onClickEdit(int position) {             // 编辑
                            toast("编辑");
                        }

                        @Override
                        public void onClickCommit(int position) {           // 提交
                            toast("提交");
                        }
                    });
                    break;
            }
        }
    };

    /*
    模拟数据
     */
    // 批发商
    private List<SaleGoodsBean> mList;
    private String[] priceTitles = {"进", "批", "零"};

}
