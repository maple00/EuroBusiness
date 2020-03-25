package com.rainwood.eurobusiness.ui.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.rainwood.eurobusiness.R;
import com.rainwood.eurobusiness.base.BaseActivity;
import com.rainwood.eurobusiness.common.Contants;
import com.rainwood.eurobusiness.domain.ItemGridBean;
import com.rainwood.eurobusiness.domain.StockBean;
import com.rainwood.eurobusiness.json.JsonParser;
import com.rainwood.eurobusiness.okhttp.HttpResponse;
import com.rainwood.eurobusiness.okhttp.OnHttpListener;
import com.rainwood.eurobusiness.request.RequestPost;
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
import java.util.Map;

/**
 * @Author: a797s
 * @Date: 2020/2/18
 * @Desc: 库存商品首页
 */
public class InventoryActivity extends BaseActivity implements View.OnClickListener, OnHttpListener {

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

    private List<StockBean> mList;
    // levele
    private List<ItemGridBean> levelList;
    private String[] levels = {"一级分类", "二级分类"};

    private final int INITIAL_SIZE = 0x101;

    @SuppressLint("SetTextI18n")
    @Override
    protected void initView() {
        pageBack.setOnClickListener(this);
        screening.setOnClickListener(this);

    }

    @Override
    protected void onResume() {
        super.onResume();
        // request
        showLoading("loading");
        RequestPost.getAllStock("", "", this);
    }

    @Override
    protected void initData() {
        super.initData();
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

    @SuppressLint("HandlerLeak")
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(@NonNull Message msg) {
            switch (msg.what) {
                case INITIAL_SIZE:
                    // level
                    LevelTypeAdapter typeAdapter = new LevelTypeAdapter(InventoryActivity.this, levelList);
                    levelType.setAdapter(typeAdapter);
                    typeAdapter.setOnClickItem(new LevelTypeAdapter.OnClickItem() {
                        @Override
                        public void onClickItem(int position) {
                            toast(levelList.get(position).getItemName());
                        }
                    });
                    // content
                    InventoryAdapter inventoryAdapter = new InventoryAdapter(InventoryActivity.this, mList);
                    contentList.setAdapter(inventoryAdapter);
                    inventoryAdapter.setOnClickItem(position -> {
                        // toast("点击了： " + position);
                        Intent intent = new Intent(InventoryActivity.this, InventoryGoodsActivity.class);
                        intent.putExtra("stockId", mList.get(position).getId());
                        startActivity(intent);
                    });
                    break;
            }
        }
    };

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

    @Override
    public void onHttpFailure(HttpResponse result) {

    }

    @Override
    public void onHttpSucceed(HttpResponse result) {
        Map<String, String> body = JsonParser.parseJSONObject(result.body());
        if (body != null) {
            if ("1".equals(body.get("code"))) {
                // 获取库存列表
                if (result.url().contains("wxapi/v1/stock.php?type=getAllStock")) {
                    mList = JsonParser.parseJSONArray(StockBean.class, JsonParser.parseJSONObject(body.get("data")).get("stocklist"));

                    allNum.setText(JsonParser.parseJSONObject(JsonParser
                            .parseJSONObject(body.get("data")).get("info")).get("totalStock"));            // 总库存
                    allMoney.setText(JsonParser.parseJSONObject(JsonParser
                            .parseJSONObject(body.get("data")).get("info")).get("totalMoney"));          // 总金额

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
