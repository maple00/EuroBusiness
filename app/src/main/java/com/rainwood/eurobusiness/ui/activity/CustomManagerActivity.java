package com.rainwood.eurobusiness.ui.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.rainwood.eurobusiness.R;
import com.rainwood.eurobusiness.base.BaseActivity;
import com.rainwood.eurobusiness.common.Contants;
import com.rainwood.eurobusiness.domain.ClientManagerBean;
import com.rainwood.eurobusiness.domain.ClientTypeBean;
import com.rainwood.eurobusiness.domain.ItemGridBean;
import com.rainwood.eurobusiness.json.JsonParser;
import com.rainwood.eurobusiness.okhttp.HttpResponse;
import com.rainwood.eurobusiness.okhttp.OnHttpListener;
import com.rainwood.eurobusiness.other.BaseDialog;
import com.rainwood.eurobusiness.request.RequestPost;
import com.rainwood.eurobusiness.ui.adapter.CustomAdapter;
import com.rainwood.eurobusiness.ui.adapter.LevelTypeAdapter;
import com.rainwood.eurobusiness.ui.dialog.MenuDialog;
import com.rainwood.tools.viewinject.ViewById;
import com.rainwood.tools.widget.MeasureListView;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @Author: a797s
 * @Date: 2020/2/22
 * @Desc: 客户管理
 */
public class CustomManagerActivity extends BaseActivity implements View.OnClickListener, OnHttpListener {
    @Override
    protected int getLayoutId() {
        return R.layout.activity_custom_manager;
    }

    @ViewById(R.id.iv_back)
    private ImageView pageBack;
    @ViewById(R.id.tv_manager_type)
    private TextView managerType;
    @ViewById(R.id.gv_custom_spinner)
    private GridView customSpinner;
    @ViewById(R.id.tv_all_money)
    private TextView allMoney;
    @ViewById(R.id.lv_content_list)
    private MeasureListView contentList;
    @ViewById(R.id.btn_new_custom)
    private Button newCustom;

    private final int INITIAL_SIZE = 0x101;
    private final int CLIENT_TYPE_SIZE = 0x102;

    // 订单总额
    private String totalCost;

    @Override
    protected void initView() {
        pageBack.setOnClickListener(this);
        managerType.setOnClickListener(this);
        newCustom.setOnClickListener(this);

        // request
        showLoading("loading");
        RequestPost.getClientList("", "", this);
    }

    @Override
    protected void initData() {
        super.initData();
        // 客户分类 Spinner
        customTypeList = new ArrayList<>();
        for (int i = 0; i < types.length && Contants.CHOOSE_MODEL_SIZE == 10; i++) {
            ItemGridBean itemGrid = new ItemGridBean();
            itemGrid.setItemName(types[i]);
            customTypeList.add(itemGrid);
        }

        for (int i = 0; i < topTypes.length && Contants.CHOOSE_MODEL_SIZE == 107; i++) {
            ItemGridBean itemGrid = new ItemGridBean();
            itemGrid.setItemName(topTypes[i]);
            customTypeList.add(itemGrid);
        }

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_back:
                finish();
                break;
            case R.id.tv_manager_type:              // 管理分类
                openActivity(CustomTypeActivity.class);
                break;
            case R.id.btn_new_custom:               // 新增客户
                openActivity(CustomNewActivity.class);
                break;
        }
    }

    @SuppressLint("HandlerLeak")
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(@NonNull Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case INITIAL_SIZE:
                    allMoney.setText(Html.fromHtml("<font color=" + getResources().getColor(R.color.fontColor) + " size='12px'>订单总额：</font>"
                            + "<font color=" + getResources().getColor(R.color.red30) + " size='15px'>" + (totalCost == null ? 0 : totalCost) + "€</font>"));
                    // 门店端
                    if (Contants.CHOOSE_MODEL_SIZE == 10) {
                        // 客户分类
                        LevelTypeAdapter typeAdapter = new LevelTypeAdapter(CustomManagerActivity.this, customTypeList);
                        customSpinner.setAdapter(typeAdapter);
                        customSpinner.setNumColumns(GridView.AUTO_FIT);
                        typeAdapter.setOnClickItem(position -> {
                            // request 客户分类列表
                            showLoading("loading");
                            RequestPost.getClientTypeList(CustomManagerActivity.this);
                        });
                        // content
                        CustomAdapter customAdapter = new CustomAdapter(CustomManagerActivity.this, mList);
                        contentList.setAdapter(customAdapter);
                        customAdapter.setOnClickContent(position -> {
                            // 查看详情
                            Log.d(TAG, "mList ---- " + mList.toString());
                            Intent intent = new Intent(CustomManagerActivity.this, CustomDetailActivity.class);
                            intent.putExtra("customId", mList.get(position).getId());
                            startActivity(intent);
                        });
                    }
                    // 批发商端
                    if (Contants.CHOOSE_MODEL_SIZE == 107) {
                        // 客户分类
                        LevelTypeAdapter typeAdapter = new LevelTypeAdapter(CustomManagerActivity.this, customTypeList);
                        customSpinner.setAdapter(typeAdapter);
                        customSpinner.setNumColumns(GridView.AUTO_FIT);
                        typeAdapter.setOnClickItem(new LevelTypeAdapter.OnClickItem() {
                            @Override
                            public void onClickItem(int position) {
                                toast(customTypeList.get(position).getItemName());
                            }
                        });
                        // content
                        CustomAdapter customAdapter = new CustomAdapter(CustomManagerActivity.this, mList);
                        contentList.setAdapter(customAdapter);
                        customAdapter.setOnClickContent(new CustomAdapter.OnClickContent() {
                            @Override
                            public void onClickContent(int position) {
                                openActivity(CustomDetailActivity.class);
                            }
                        });
                    }
                    break;
                case CLIENT_TYPE_SIZE:
                    List<String> typeList = new ArrayList<>();
                    for (ClientTypeBean clientTypeBean : mClientTypeList) {
                        typeList.add(clientTypeBean.getName());
                    }
                    new MenuDialog.Builder(CustomManagerActivity.this)
                            .setCancel(R.string.common_cancel)
                            .setAutoDismiss(false)
                            .setList(typeList)
                            .setCanceledOnTouchOutside(false)
                            .setListener(new MenuDialog.OnListener<String>() {
                                @Override
                                public void onSelected(BaseDialog dialog, int position, String text) {
                                    // request
                                    showLoading("loading");
                                    RequestPost.getClientList("",
                                            mClientTypeList.get(position).getId(), CustomManagerActivity.this);
                                    dialog.dismiss();
                                }

                                @Override
                                public void onCancel(BaseDialog dialog) {
                                    dialog.dismiss();
                                }
                            }).show();
                    break;
            }
        }
    };

    private List<ItemGridBean> customTypeList;
    private String[] types = {"客户分类"};
    private List<ClientManagerBean> mList;
    private List<ClientTypeBean> mClientTypeList;
    // 批发商
    private String[] topTypes = {"客户分类", "门店"};

    @Override
    public void onHttpFailure(HttpResponse result) {

    }

    @Override
    public void onHttpSucceed(HttpResponse result) {
        Map<String, String> body = JsonParser.parseJSONObject(result.body());
        if (body != null) {
            if ("1".equals(body.get("code"))) {
                // 获取客户列表
                if (result.url().contains("wxapi/v1/client.php?type=getClientlist")) {
                    mList = JsonParser.parseJSONArray(ClientManagerBean.class, JsonParser.parseJSONObject(body.get("data")).get("kehulist"));
                    totalCost = JsonParser.parseJSONObject(body.get("data")).get("info");

                    Message msg = new Message();
                    msg.what = INITIAL_SIZE;
                    mHandler.sendMessage(msg);
                }
                // 获取客户分类列表
                if (result.url().contains("wxapi/v1/client.php?type=getKehuTypelist")) {
                    mClientTypeList = JsonParser.parseJSONArray(ClientTypeBean.class, JsonParser.parseJSONObject(body.get("data")).get("kehuTypelist"));

                    Message msg = new Message();
                    msg.what = CLIENT_TYPE_SIZE;
                    mHandler.sendMessage(msg);
                }
            } else {
                toast(body.get("warn"));
            }
            dismissLoading();
        }
    }
}
