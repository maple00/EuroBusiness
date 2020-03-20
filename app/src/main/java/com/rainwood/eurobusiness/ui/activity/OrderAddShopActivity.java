package com.rainwood.eurobusiness.ui.activity;

import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.rainwood.eurobusiness.R;
import com.rainwood.eurobusiness.base.BaseActivity;
import com.rainwood.eurobusiness.domain.CommonUIBean;
import com.rainwood.eurobusiness.domain.OrderBean;
import com.rainwood.eurobusiness.domain.PressBean;
import com.rainwood.eurobusiness.ui.adapter.CommUIAdapter;
import com.rainwood.eurobusiness.ui.adapter.ItemOrderAddAdapter;
import com.rainwood.eurobusiness.ui.adapter.SubChooseParamsAdapter;
import com.rainwood.tools.viewinject.ViewById;
import com.rainwood.tools.widget.MeasureListView;

import java.util.ArrayList;
import java.util.List;

/**
 * @Author: a797s
 * @Date: 2020/2/19
 * @Desc: 订单添加商品页面
 */
public class OrderAddShopActivity extends BaseActivity implements View.OnClickListener {

    @Override
    protected int getLayoutId() {
        return R.layout.activity_order_add_shop;
    }

    @ViewById(R.id.iv_back)
    private ImageView pageBack;
    @ViewById(R.id.tv_title)
    private TextView pageTitle;
    @ViewById(R.id.lv_content_list)
    private MeasureListView contentList;
    @ViewById(R.id.btn_confirm)
    private Button confirm;

    @Override
    protected void initView() {
        pageBack.setOnClickListener(this);
        confirm.setOnClickListener(this);
        pageTitle.setText("添加商品");

        ItemOrderAddAdapter orderAddAdapter = new ItemOrderAddAdapter(this, mList);
        contentList.setAdapter(orderAddAdapter);
        orderAddAdapter.setOnClickEditText((parentPos, position) -> {                   // 商品信息
            // toast(mList.get(parentPos).getCommonList().get(position).getTitle());
            switch (mList.get(parentPos).getCommonList().get(position).getTitle()) {
                case "选择分类":
                    openActivity(GoodsTypeActivity.class);
                    break;
                case "商品名称":
                    // toast("商品名称");
                    openActivity(SearchViewActivity.class);
                    break;
            }
        });

        orderAddAdapter.setOnClickItem((parentPos, position) -> {                   // 选择规格
            // toast(mList.get(parentPos).getPressList().get(position).getTitle());
            boolean choose = mList.get(parentPos).getPressList().get(position).isChoose();
            if (choose) {
                mList.get(parentPos).getPressList().get(position).setChoose(false);
            } else {
                mList.get(parentPos).getPressList().get(position).setChoose(true);
            }
        });
    }

    @Override
    protected void initData() {
        super.initData();
        mList = new ArrayList<>();
        for (int i = 0; i < titles.length; i++) {
            OrderBean order = new OrderBean();
            order.setTitle(titles[i]);
            order.setItemType(i);
            List<CommonUIBean> commonUIList = new ArrayList<>();
            for (int j = 0; j < goodsTitles.length && i == 0; j++) {        // 商品信息
                CommonUIBean commonUI = new CommonUIBean();
                commonUI.setTitle(goodsTitles[j]);
                commonUI.setLabel(goodsLabels[j]);
                commonUI.setArrowType(1);
                commonUIList.add(commonUI);
            }
            order.setCommonList(commonUIList);
            List<PressBean> pressList = new ArrayList<>();
            for (int j = 0; j < params.length && i == 1; j++) {             // 规格参数
                PressBean press = new PressBean();
                press.setTitle(params[j]);
                press.setChoose(false);
                pressList.add(press);
            }
            order.setPressList(pressList);
            mList.add(order);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_back:
                finish();
                break;
            case R.id.btn_confirm:
                 toast("确定");
                break;
        }
    }

    /*
    模拟数据
     */
    private List<OrderBean> mList;
    private String[] titles = {"商品信息", "选择规格"};
    private String[] goodsTitles = {"选择分类", "商品名称"};
    private String[] goodsLabels = {"请选择", "请选择"};
    private String[] params = {"红色/S", "红色/M", "红色/L", "红色/XL", "枫叶红/XS", "枫叶红/XL",
                "枫叶红/XXL", "雾霾蓝/XXXL", "雾霾蓝/XXXL"};
}
