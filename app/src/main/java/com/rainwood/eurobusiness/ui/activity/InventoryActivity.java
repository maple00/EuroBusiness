package com.rainwood.eurobusiness.ui.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.rainwood.eurobusiness.R;
import com.rainwood.eurobusiness.base.BaseActivity;
import com.rainwood.eurobusiness.common.Contants;
import com.rainwood.eurobusiness.domain.ItemGridBean;
import com.rainwood.eurobusiness.domain.ShopBean;
import com.rainwood.eurobusiness.ui.adapter.InventoryAdapter;
import com.rainwood.eurobusiness.ui.adapter.LevelTypeAdapter;
import com.rainwood.tools.permission.OnPermission;
import com.rainwood.tools.permission.Permission;
import com.rainwood.tools.permission.XXPermissions;
import com.rainwood.tools.viewinject.ViewById;
import com.rainwood.tools.widget.MeasureGridView;
import com.rainwood.tools.widget.MeasureListView;
import com.rainwood.zxingqrc.android.QRCodeCaptureActivity;

import java.util.ArrayList;
import java.util.List;

/**
 * @Author: a797s
 * @Date: 2020/2/18
 * @Desc: 库存商品首页
 */
public class InventoryActivity extends BaseActivity implements View.OnClickListener {

    @Override
    protected int getLayoutId() {
        return R.layout.activity_inventory;
    }

    @ViewById(R.id.iv_back)
    private ImageView pageBack;
    @ViewById(R.id.tv_screening)
    private TextView screening;
    @ViewById(R.id.tv_all_num)
    private TextView allNum;
    @ViewById(R.id.tv_all_money)
    private TextView allMoney;
    @ViewById(R.id.lv_content)
    private MeasureListView contentList;
    @ViewById(R.id.gv_level_type)
    private MeasureGridView levelType;


    @SuppressLint("SetTextI18n")
    @Override
    protected void initView() {
        pageBack.setOnClickListener(this);
        screening.setOnClickListener(this);

        allNum.setText("45622");
        allMoney.setText("45622");

        // level
        LevelTypeAdapter typeAdapter = new LevelTypeAdapter(this, levelList);
        levelType.setAdapter(typeAdapter);
        // content
        InventoryAdapter inventoryAdapter = new InventoryAdapter(this, mList);
        contentList.setAdapter(inventoryAdapter);
        inventoryAdapter.setOnClickItem(position -> {
            // toast("点击了： " + position);
            openActivity(InventoryGoodsActivity.class);
        });
    }

    @Override
    protected void initData() {
        super.initData();
        mList = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            ShopBean shopBean = new ShopBean();
            shopBean.setImgPath(R.drawable.icon_loadding_fail);
            shopBean.setName("西装外套式系缀扣连衣裙");
            shopBean.setModel("XDF-256165");
            shopBean.setInvenNum("库存582件");
            shopBean.setWholesalePrice("136.00€");
            shopBean.setRetailPrice("136.00€");
            mList.add(shopBean);
        }

        levelList = new ArrayList<>();
        for (String level : levels) {
            ItemGridBean itemGrid = new ItemGridBean();
            itemGrid.setItemName(level);
            itemGrid.setImgId(R.drawable.ic_down_selector);
            levelList.add(itemGrid);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_back:
                finish();
                break;
            case R.id.tv_screening:
                // toast("扫一扫");
                openScreening();
                break;
        }
    }

    /**
     * 打开扫一扫
     */
    private void openScreening() {
        // 先获取相机权限
        XXPermissions.with(getActivity())
                // 可设置被拒绝后继续申请，直到用户授权或永久拒绝
                .constantRequest()
                // 不指定权限则自定获取订单中的危险权限
                .permission(Permission.CAMERA)
                .request(new OnPermission() {
                    @Override
                    public void hasPermission(List<String> granted, boolean isAll) {
                        if (isAll) {
                            // 去扫码
                            Intent intent = new Intent(InventoryActivity.this, QRCodeCaptureActivity.class);
                            // 设置标题栏的颜色
                            intent.putExtra(QRCodeCaptureActivity.STATUS_BAR_COLOR, Color.parseColor("#99000000"));
                            startActivityForResult(intent, Contants.SCANCHECKCODE);
                        } else {
                            toast("获取权限成功，部分权限未正常授予");
                        }
                    }

                    @Override
                    public void noPermission(List<String> denied, boolean quick) {
                        if (quick) {
                            toast("被永久拒绝授权，请手动授予权限");
                            //如果是被永久拒绝就跳转到应用权限系统设置页面
                            XXPermissions.gotoPermissionSettings(getActivity());
                        } else {
                            toast("获取权限失败");
                        }
                    }
                });
    }

    /*
    模拟数据
    */
    private List<ShopBean> mList;
    // levele
    private List<ItemGridBean> levelList;
    private String[] levels = {"一级分类", "二级分类"};
}
