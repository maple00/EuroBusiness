package com.rainwood.eurobusiness.ui.activity;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.rainwood.eurobusiness.R;
import com.rainwood.eurobusiness.base.BaseActivity;
import com.rainwood.eurobusiness.domain.CommonUIBean;
import com.rainwood.eurobusiness.domain.OrderBean;
import com.rainwood.eurobusiness.domain.PressBean;
import com.rainwood.eurobusiness.ui.adapter.NewStockAdapter;
import com.rainwood.tools.viewinject.ViewById;
import com.rainwood.tools.widget.MeasureListView;

import java.util.ArrayList;
import java.util.List;

/**
 * @Author: a797s
 * @Date: 2020/2/20
 * @Desc: 新建盘点
 */
public class NewStockActivity extends BaseActivity implements View.OnClickListener {

    @Override
    protected int getLayoutId() {
        return R.layout.activity_new_stock;
    }

    @ViewById(R.id.iv_back)
    private ImageView pageBack;
    @ViewById(R.id.tv_title)
    private TextView pageTitle;
    @ViewById(R.id.lv_content_list)
    private MeasureListView contentList;

    @Override
    protected void initView() {
        pageBack.setOnClickListener(this);
        pageTitle.setText("新建盘点");

        NewStockAdapter stockAdapter = new NewStockAdapter(this, mList);
        contentList.setAdapter(stockAdapter);
        stockAdapter.setOnClickEditText((parentPos, position) -> {          // 商品信息
            // toast(mList.get(parentPos).getCommonList().get(position).getTitle());
            switch (mList.get(parentPos).getCommonList().get(position).getTitle()) {
                case "商品分类":
                    openActivity(GoodsTypeActivity.class);
                    break;
                case "商品名称":
                    // toast("商品名称");
                    openActivity(SearchViewActivity.class);
                    break;
            }
        });
        stockAdapter.setOnClickItem((parentPos, position) -> {
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
            for (int j = 0; j < goodsTitle.length && i == 0; j++) {
                CommonUIBean commonUI = new CommonUIBean();
                commonUI.setTitle(goodsTitle[j]);
                commonUI.setLabel(goodsLabel[j]);
                if (goodsLabel[j].equals("请选择")) {
                    commonUI.setArrowType(1);
                } else {
                    commonUI.setArrowType(0);
                }
                commonUIList.add(commonUI);
            }
            List<PressBean> paramsList = new ArrayList<>();
            for (int j = 0; j < params.length && i == 0; j++) {
                PressBean press = new PressBean();
                press.setTitle(params[j]);
                press.setChoose(false);
                paramsList.add(press);
            }
            order.setPressList(paramsList);
            for (int j = 0; j < stockTitle.length && i == 1; j++) {
                CommonUIBean commonUI = new CommonUIBean();
                commonUI.setTitle(stockTitle[j]);
                commonUI.setLabel(stockLael[j]);
                if (stockLael[j].equals("请选择")) {
                    commonUI.setArrowType(1);
                } else {
                    commonUI.setArrowType(0);
                }
                commonUIList.add(commonUI);
            }
            order.setCommonList(commonUIList);
            mList.add(order);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_back:
                finish();
                break;
        }
    }

    /*
    模拟数据
     */
    private List<OrderBean> mList;
    private String[] titles = {"商品信息", "库存信息"};
    // 商品信息
    private String[] goodsTitle = {"商品分类", "商品名称"};
    private String[] goodsLabel = {"请选择", "请选择"};
    private String[] params = {"红色/S", "红色/M", "红色/L", "红色/XL", "枫叶红/XS", "枫叶红/XXL",
            "枫叶红/XXXL", "雾霾蓝/XXXL", "雾霾蓝/XXXL"};
    // 库存信息
    private String[] stockTitle = {"库存数量", "盘存数量", "备注"};
    private String[] stockLael = {"请输入", "请输入", "请输入"};
}
