package com.rainwood.eurobusiness.ui.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;

import com.rainwood.eurobusiness.R;
import com.rainwood.eurobusiness.base.BaseActivity;
import com.rainwood.eurobusiness.common.Contants;
import com.rainwood.eurobusiness.domain.InventoryOutBean;
import com.rainwood.eurobusiness.domain.ItemGridBean;
import com.rainwood.eurobusiness.domain.PressBean;
import com.rainwood.eurobusiness.domain.StorePermisionBean;
import com.rainwood.eurobusiness.json.JsonParser;
import com.rainwood.eurobusiness.okhttp.HttpResponse;
import com.rainwood.eurobusiness.okhttp.OnHttpListener;
import com.rainwood.eurobusiness.other.BaseDialog;
import com.rainwood.eurobusiness.request.RequestPost;
import com.rainwood.eurobusiness.ui.adapter.ItemOutBoundAdapter;
import com.rainwood.eurobusiness.ui.adapter.LevelTypeAdapter;
import com.rainwood.eurobusiness.ui.adapter.TopTypeAdapter;
import com.rainwood.eurobusiness.ui.dialog.MenuDialog;
import com.rainwood.tools.viewinject.ViewById;
import com.rainwood.tools.widget.MeasureGridView;
import com.rainwood.tools.widget.MeasureListView;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @Author: a797s
 * @Date: 2020/2/20
 * @Desc: 出库记录
 */
public class OutInBoundActivity extends BaseActivity implements View.OnClickListener, OnHttpListener {

    @Override
    protected int getLayoutId() {
        return R.layout.activity_out_bound;
    }

    @ViewById(R.id.iv_back)
    private ImageView pageBack;
    @ViewById(R.id.gv_top_type)
    private MeasureGridView topType;
    @ViewById(R.id.lv_content_list)
    private MeasureListView contentList;        // content

    @ViewById(R.id.ll_search)
    private LinearLayout search;
    @ViewById(R.id.ll_search_1)
    private LinearLayout search1;
    @ViewById(R.id.iv_new_found)
    private ImageView newFound;
    @ViewById(R.id.iv_screening)
    private ImageView screening;

    private List<PressBean> topList;
    private List<InventoryOutBean> mList;
    private List<StorePermisionBean> mStoreList;
    /*
    门店
     */
    // 出库
    private String[] tops = {"全部", "线上订单", "线下订单", "退货订单"};
    // 入库
    private String[] inTops = {"全部", "订单退货", "采购入库"};
    /*
    批发商
     */
    private List<ItemGridBean> levelTypeList;
    // 入库
    private String[] salerInTops = {"订单类型", "门店"};

    private final int INITIAL_SIZE = 0x101;

    @Override
    protected void initView() {
        pageBack.setOnClickListener(this);
        /**
         * 门店端
         */
        if (Contants.CHOOSE_MODEL_SIZE == 5) {           // 出库记录
            OutInRecord(4);
            search1.setVisibility(View.GONE);
            screening.setVisibility(View.GONE);
        }
        if (Contants.CHOOSE_MODEL_SIZE == 6) {           // 入库记录
            search1.setVisibility(View.GONE);
            screening.setVisibility(View.GONE);
            OutInRecord(3);
        }
        // 批发商
        if (Contants.CHOOSE_MODEL_SIZE == 110) {        // 出库记录
            wholesalersOutInBounds();
        }
        if (Contants.CHOOSE_MODEL_SIZE == 111) {         // 入库记录
            wholesalersOutInBounds();
        }
        // request -- default query all
        showLoading("loading");
        RequestPost.getInventoryOut("", "", "", this);
    }

    @Override
    protected void initData() {
        super.initData();
        topList = new ArrayList<>();
        if (Contants.CHOOSE_MODEL_SIZE == 5) {              // 出库
            for (int i = 0; i < tops.length; i++) {
                PressBean press = new PressBean();
                if (i == 0) {
                    press.setChoose(true);
                } else {
                    press.setChoose(false);
                }
                press.setTitle(tops[i]);
                topList.add(press);
            }
        }
        if (Contants.CHOOSE_MODEL_SIZE == 6) {           // 入库
            for (int i = 0; i < inTops.length; i++) {
                PressBean press = new PressBean();
                if (i == 0) {
                    press.setChoose(true);
                } else {
                    press.setChoose(false);
                }
                press.setTitle(inTops[i]);
                topList.add(press);
            }
        }

        levelTypeList = new ArrayList<>();
        if (Contants.CHOOSE_MODEL_SIZE == 111 || Contants.CHOOSE_MODEL_SIZE == 110) {
            for (String salerInTop : salerInTops) {
                ItemGridBean itemGrid = new ItemGridBean();
                itemGrid.setItemName(salerInTop);
                levelTypeList.add(itemGrid);
            }
        }
    }

    /**
     * 门店出入库记录
     *
     * @param i
     */
    private void OutInRecord(int i) {
        TopTypeAdapter typeAdapter = new TopTypeAdapter(this, topList);
        topType.setAdapter(typeAdapter);
        topType.setNumColumns(i);
        typeAdapter.setOnClickItem(position -> {
            for (PressBean pressBean : topList) {
                pressBean.setChoose(false);
            }
            topList.get(position).setChoose(true);
            // request -- change type
            if (Contants.CHOOSE_MODEL_SIZE == 5) {       // 出库
                // saleOut：线下订单 saleOnlineOut：线上订单 returnOut：退货订单
                String type = "";
                switch (position) {
                    case 0:
                        type = "";
                        break;
                    case 1:
                        type = "saleOnlineOut";
                        break;
                    case 2:
                        type = "saleOut";
                        break;
                    case 3:
                        type = "returnOut";
                        break;
                }
                showLoading("loading");
                RequestPost.getInventoryOut(type, "", "", this);
            }
            if (Contants.CHOOSE_MODEL_SIZE == 6) {        // 入库
                // 订单类型 buyIn：采购入库 returnIn：退货订单
                String type = "";
                switch (position) {
                    case 0:
                        type = "";
                        break;
                    case 1:
                        type = "returnIn";
                        break;
                    case 2:
                        type = "buyIn";
                        break;
                }
                showLoading("loading");
                RequestPost.getInventoryIn(type, "", "", this);
            }
        });

        // content
        ItemOutBoundAdapter boundAdapter = new ItemOutBoundAdapter(this, mList);
        contentList.setAdapter(boundAdapter);
        boundAdapter.setOnClickItem(position -> {
            // toast("详情");
            Intent intent = new Intent(OutInBoundActivity.this, OutBoundDetailActivity.class);
            intent.putExtra("inventoryId", mList.get(position).getId());
            startActivity(intent);
        });
    }

    /**
     * 批发商出入库记录
     */
    private void wholesalersOutInBounds() {
        search.setVisibility(View.GONE);
        newFound.setVisibility(View.GONE);

        LevelTypeAdapter typeAdapter = new LevelTypeAdapter(this, levelTypeList);
        topType.setAdapter(typeAdapter);
        topType.setNumColumns(4);
        typeAdapter.setOnClickItem(position -> {
            switch (position) {
                case 0:         // 订单类型
                    toast(levelTypeList.get(position).getItemName() + "还没有数据");
                    break;
                case 1:             // 门店
                    toast(levelTypeList.get(position).getItemName() + "还没有数据");
                   /* List<String> strList = new ArrayList<>();
                    for (StorePermisionBean bean : mStoreList) {
                        strList.add(bean.getName());
                    }
                    new MenuDialog.Builder(OutInBoundActivity.this)
                            // 设置null 表示不显示取消按钮
                            .setCancel(R.string.common_cancel)
                            // 设置点击按钮后不关闭弹窗
                            .setAutoDismiss(false)
                            // 显示的数据
                            .setList(strList)
                            .setListener(new MenuDialog.OnListener<String>() {
                                @Override
                                public void onSelected(BaseDialog dialog, int position, String text) {
                                    dialog.dismiss();
                                    //toast("位置：" + position + ", 文本：" + text);
                                    //request
                                    showLoading("");
                                    RequestPost.getInventoryOut("", "", mStoreList.get(position).getId(), OutInBoundActivity.this);
                                }

                                @Override
                                public void onCancel(BaseDialog dialog) {
                                    dialog.dismiss();
                                }
                            }).show();*/
                    break;
            }
        });
        ItemOutBoundAdapter boundAdapter = new ItemOutBoundAdapter(this, mList);
        contentList.setAdapter(boundAdapter);
        boundAdapter.setOnClickItem(position -> {
            // toast("详情");
            Intent intent = new Intent(OutInBoundActivity.this, OutBoundDetailActivity.class);
            intent.putExtra("inventoryId", mList.get(position).getId());
            startActivity(intent);
            //openActivity(OutBoundDetailActivity.class);
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_back:
                finish();
                break;
        }
    }

    @SuppressLint("HandlerLeak")
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(@NonNull Message msg) {
            switch (msg.what) {
                case INITIAL_SIZE:
                    /**
                     * 门店端
                     */
                    if (Contants.CHOOSE_MODEL_SIZE == 5) {
                        OutInRecord(4);
                    }
                    if (Contants.CHOOSE_MODEL_SIZE == 6) {
                        OutInRecord(3);
                    }

                    /**
                     * 批发商端
                     */
                    // 入库记录
                    if (Contants.CHOOSE_MODEL_SIZE == 111) {
                        wholesalersOutInBounds();
                    }
                    // 出库记录
                    if (Contants.CHOOSE_MODEL_SIZE == 110) {
                        wholesalersOutInBounds();
                    }
                    break;
            }
        }
    };

    @Override
    public void onHttpFailure(HttpResponse result) {

    }

    @Override
    public void onHttpSucceed(HttpResponse result) {
        Log.d(TAG, " ======== result ======= " + result);
        Map<String, String> body = JsonParser.parseJSONObject(result.body());
        if (body != null) {
            if ("1".equals(body.get("code"))) {
                // 出库记录
                if (result.url().contains("wxapi/v1/stock.php?type=getInventoryOut")) {
                    mList = JsonParser.parseJSONArray(InventoryOutBean.class, JsonParser.parseJSONObject(body.get("data")).get("stocklist"));

                    Message msg = new Message();
                    msg.what = INITIAL_SIZE;
                    mHandler.sendMessage(msg);
                }
                // 入库记录
                if (result.url().contains("wxapi/v1/stock.php?type=getInventoryIn")) {
                    mList = JsonParser.parseJSONArray(InventoryOutBean.class, JsonParser.parseJSONObject(body.get("data")).get("stocklist"));
                    Log.d(TAG, " ======== " + JsonParser.parseJSONObject(body.get("data")).get("storelist"));
                    mStoreList = JsonParser.parseJSONArray(StorePermisionBean.class, JsonParser.parseJSONObject(body.get("data")).get("storelist"));
                    Log.d(TAG, " ========= " + mStoreList.toString());
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
