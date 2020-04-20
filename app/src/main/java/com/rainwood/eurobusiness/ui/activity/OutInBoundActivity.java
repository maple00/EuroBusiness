package com.rainwood.eurobusiness.ui.activity;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.rainwood.eurobusiness.R;
import com.rainwood.eurobusiness.base.BaseActivity;
import com.rainwood.eurobusiness.common.Contants;
import com.rainwood.eurobusiness.domain.InventoryOutBean;
import com.rainwood.eurobusiness.domain.ItemGridBean;
import com.rainwood.eurobusiness.domain.PressBean;
import com.rainwood.eurobusiness.domain.SupplierBean;
import com.rainwood.eurobusiness.json.JsonParser;
import com.rainwood.eurobusiness.okhttp.HttpResponse;
import com.rainwood.eurobusiness.okhttp.OnHttpListener;
import com.rainwood.eurobusiness.other.BaseDialog;
import com.rainwood.eurobusiness.request.RequestPost;
import com.rainwood.eurobusiness.ui.adapter.ItemOutBoundAdapter;
import com.rainwood.eurobusiness.ui.adapter.LevelTypeAdapter;
import com.rainwood.eurobusiness.ui.adapter.TopTypeAdapter;
import com.rainwood.eurobusiness.ui.dialog.DateDialog;
import com.rainwood.eurobusiness.ui.dialog.MenuDialog;
import com.rainwood.eurobusiness.utils.ListUtils;
import com.rainwood.tools.refresh.DaisyRefreshLayout;
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

    private String mStoreId;

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

    //    @ViewById(R.id.ll_search)
//    private LinearLayout search;
    @ViewById(R.id.ll_search_1)
    private LinearLayout search1;
    //    @ViewById(R.id.iv_new_found)
//    private ImageView newFound;
    @ViewById(R.id.iv_screening)
    private ImageView screening;

    private List<PressBean> topList;
    private List<InventoryOutBean> mCopyList = new ArrayList<>();
    // 入库记录
    private List<SupplierBean> mStoreList;
    // 门店列表
    private List<SupplierBean> mTopStoreList;
    @ViewById(R.id.drl_refresh)
    private DaisyRefreshLayout mRefreshLayout;
    /*
    门店
     */
    // 出库
    private String[] tops = {"全部", "线上订单", "线下订单", "退货订单"};
    // 订单类型
    private List<String> orderTypeList;
    // 入库
    private String[] inTops = {"全部", "订单退货", "采购入库"};
    /*
    批发商
     */
    private List<ItemGridBean> levelTypeList;
    // 入库
    private String[] salerInTops = {"订单类型", "门店"};
    private final int INITIAL_SIZE = 0x101;
    private final int REFRESH_SIZE = 0x102;
    private int pageCount = 0;
    private String mType = "";
    private boolean hasLoadMore;        // 是否是加载，默认不是。

    @Override
    protected void initView() {
        pageBack.setOnClickListener(this);
        screening.setOnClickListener(this);

        Message msg = new Message();
        msg.what = REFRESH_SIZE;
        mHandler.sendMessage(msg);
    }

    @Override
    protected void initData() {
        super.initData();
        topList = new ArrayList<>();
        // 门店端
        if (Contants.CHOOSE_MODEL_SIZE == 5) {              // 出库
            setTopType(4);
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
            setTopType(3);
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
     */
    private void OutInRecord() {
        // content
        ItemOutBoundAdapter boundAdapter = new ItemOutBoundAdapter(this, mCopyList);
        contentList.setAdapter(boundAdapter);
        boundAdapter.setOnClickItem(position -> {
            // toast("详情");
            Intent intent = new Intent(OutInBoundActivity.this, OutBoundDetailActivity.class);
            intent.putExtra("inventoryId", mCopyList.get(position).getId());
            startActivity(intent);
        });
    }

    private void setTopType(int i) {
        TopTypeAdapter typeAdapter = new TopTypeAdapter(this, topList);
        topType.setAdapter(typeAdapter);
        topType.setNumColumns(i);
        typeAdapter.setOnClickItem(position -> {
            for (PressBean pressBean : topList) {
                pressBean.setChoose(false);
            }
            mCopyList = new ArrayList<>();
            pageCount = 0;
            topList.get(position).setChoose(true);
            // request -- change type
            if (Contants.CHOOSE_MODEL_SIZE == 5) {       // 出库
                // saleOut：线下订单 saleOnlineOut：线上订单 returnOut：退货订单
                switch (position) {
                    case 0:
                        mType = "saleOnlineOut";
                        break;
                    case 1:
                        mType = "saleOut";
                        break;
                    case 2:
                        mType = "returnOut";
                        break;
                }
                showLoading("loading");
                mCopyList = new ArrayList<>();
                RequestPost.getInventoryOut(String.valueOf(pageCount), mType, "", "", "", "", this);
            }
            if (Contants.CHOOSE_MODEL_SIZE == 6) {        // 入库
                // 订单类型 buyIn：采购入库 returnIn：退货订单
                switch (position) {
                    case 0:
                        mType = "returnIn";
                        break;
                    case 1:
                        mType = "buyIn";
                        break;
                }
                showLoading("loading");
                mCopyList = new ArrayList<>();
                RequestPost.getInventoryIn(String.valueOf(pageCount), mType, "", "", "", "", this);
            }
        });
    }

    /**
     * 批发商出入库记录
     * flag : 0 -- 入库
     * 1 -- 出库
     */
    private void wholesalersOutInBounds(int flag) {
        LevelTypeAdapter typeAdapter = new LevelTypeAdapter(this, levelTypeList);
        topType.setAdapter(typeAdapter);
        topType.setNumColumns(4);
        typeAdapter.setOnClickItem(position -> {
            switch (position) {
                case 0:         // 订单类型
                    // toast(levelTypeList.get(position).getItemName() + "还没有数据");
                    new MenuDialog.Builder(OutInBoundActivity.this)
                            // 设置null 表示不显示取消按钮
                            .setCancel(R.string.common_cancel)
                            // 设置点击按钮后不关闭弹窗
                            .setAutoDismiss(false)
                            // 显示的数据
                            .setList(orderTypeList)
                            .setListener(new MenuDialog.OnListener<String>() {
                                @Override
                                public void onSelected(BaseDialog dialog, int position, String text) {
                                    dialog.dismiss();
                                    pageCount = 0;
                                    mCopyList = new ArrayList<>();
                                    //request
                                    showLoading("");
                                    if (flag == 1) {             // 出库 saleOut：线下订单 saleOnlineOut：线上订单 returnOut：退货订单
                                        switch (position) {
                                            case 1:
                                                mType = "returnOut";
                                                break;
                                            case 2:
                                                mType = "saleOnlineOut";
                                                break;
                                            default:
                                                mType = "saleOut";
                                                break;
                                        }
                                        RequestPost.getInventoryOut(String.valueOf(pageCount), mType,
                                                "", "", "", "", OutInBoundActivity.this);
                                    }
                                    if (flag == 0) {         // 入库记录
                                        // 订单类型 buyIn：采购入库 returnIn：退货订单
                                        switch (position) {
                                            case 1:
                                                mType = "buyIn";
                                                break;
                                            default:
                                                mType = "returnIn";
                                                break;
                                        }
                                        RequestPost.getInventoryIn(String.valueOf(pageCount), mType,
                                                "", "", "", "", OutInBoundActivity.this);
                                    }
                                }

                                @Override
                                public void onCancel(BaseDialog dialog) {
                                    dialog.dismiss();
                                }
                            }).show();
                    break;
                case 1:             // 门店
                    // toast(levelTypeList.get(position).getItemName() + "还没有数据");
                    List<String> strList = new ArrayList<>();
                    for (SupplierBean bean : mTopStoreList) {
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
                                    pageCount = 0;
                                    mCopyList = new ArrayList<>();
                                    showLoading("");
                                    mStoreId = mTopStoreList.get(position).getId();
                                    if (flag == 1) {         // 出库门店列表
                                        RequestPost.getInventoryOut(String.valueOf(pageCount), "",
                                                "", mStoreId, "", "", OutInBoundActivity.this);
                                    }
                                    if (flag == 0) {         // 入库门店列表
                                        RequestPost.getInventoryIn(String.valueOf(pageCount), "",
                                                "", mStoreId, "", "", OutInBoundActivity.this);
                                    }
                                }

                                @Override
                                public void onCancel(BaseDialog dialog) {
                                    dialog.dismiss();
                                }
                            }).show();
                    break;
            }
        });
        ItemOutBoundAdapter boundAdapter = new ItemOutBoundAdapter(this, mCopyList);
        contentList.setAdapter(boundAdapter);
        boundAdapter.setOnClickItem(position -> {
            // toast("详情");
            Intent intent = new Intent(OutInBoundActivity.this, OutBoundDetailActivity.class);
            intent.putExtra("inventoryId", mCopyList.get(position).getId());
            startActivity(intent);
        });
    }

    /**
     * 选择时间段 Dialog
     */
    private void getCusTomDialog() {
        Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_period);
        Window dialogWindow = dialog.getWindow();
        dialogWindow.setGravity(Gravity.CENTER);
        dialogWindow.getDecorView().setPadding(0, 30, 0, 30);
        WindowManager.LayoutParams lp = dialogWindow.getAttributes();
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        dialogWindow.setAttributes(lp);
        dialog.show();

        TextView startTime = dialog.findViewById(R.id.tv_start_time);
        startTime.setFocusable(true);
        startTime.setOnClickListener(v -> {
            // 日期选择对话框
            getDateDialog(startTime);
        });
        TextView endTime = dialog.findViewById(R.id.tv_end_time);
        endTime.setOnClickListener(v -> getDateDialog(endTime));
        // 监听
        TextView clear = dialog.findViewById(R.id.tv_clear_screening);
        clear.setOnClickListener(v -> {
            startTime.setText("");
            endTime.setText("");
            postAtTime(() -> toast("清除完成!"), 500);
        });
        TextView confirm = dialog.findViewById(R.id.tv_confirm);
        confirm.setOnClickListener(v -> {
            if (TextUtils.isEmpty(startTime.getText()) || TextUtils.isEmpty(endTime.getText())) {
                toast("选择时间范围不完整!");
                return;
            }

            // request
            showLoading("loading");
            dialog.dismiss();
            pageCount = 0;
            mCopyList = new ArrayList<>();
            if (Contants.CHOOSE_MODEL_SIZE == 5)
                RequestPost.getInventoryOut(String.valueOf(pageCount), mType, "", "",
                        startTime.getText().toString().trim(), endTime.getText().toString().trim(), OutInBoundActivity.this);
            if (Contants.CHOOSE_MODEL_SIZE == 6) {
                RequestPost.getInventoryIn(String.valueOf(pageCount), mType, "", "",
                        startTime.getText().toString().trim(), endTime.getText().toString().trim(), this);
            }
        });
        // dialog  dismiss 监听
        dialog.setOnDismissListener(DialogInterface::dismiss);
    }

    /**
     * 选择时间范围
     *
     * @param time TextView
     */
    private void getDateDialog(TextView time) {
        new DateDialog.Builder(this)
                .setTitle(getString(R.string.date_title))
                .setConfirm(getString(R.string.common_confirm))
                .setCancel(null)
                //.setIgnoreDay()
                .setListener(new DateDialog.OnListener() {
                    @SuppressLint("SetTextI18n")
                    @Override
                    public void onSelected(BaseDialog dialog, int year, int month, int day) {
                        // toast(year + "-" + "-" + month + "-" + day);
                        time.setText(year + "-" + (month < 10 ? ("0" + month) : month) + "-" + (day < 10 ? ("0" + day) : day));
                    }

                    @Override
                    public void onCancel(BaseDialog dialog) {
                        dialog.dismiss();
                    }
                }).show();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_back:
                finish();
                break;
            case R.id.iv_screening:
                getCusTomDialog();
                break;
        }
    }

    @SuppressLint("HandlerLeak")
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(@NonNull Message msg) {
            switch (msg.what) {
                case INITIAL_SIZE:
                    mRefreshLayout.setRefreshing(false);
                    mRefreshLayout.setLoadMore(false);
                    // 加载完成之后，使之不加载
                    hasLoadMore = false;
                    /**
                     * 门店端
                     */
                    if (Contants.CHOOSE_MODEL_SIZE == 5) {
                        OutInRecord();
                    }
                    if (Contants.CHOOSE_MODEL_SIZE == 6) {
                        OutInRecord();
                    }

                    /**
                     * 批发商端
                     */
                    // 入库记录
                    if (Contants.CHOOSE_MODEL_SIZE == 111) {
                        wholesalersOutInBounds(0);
                    }
                    // 出库记录
                    if (Contants.CHOOSE_MODEL_SIZE == 110) {
                        wholesalersOutInBounds(1);
                    }
                    break;
                case REFRESH_SIZE:
                    //上拉加载
                    mRefreshLayout.setOnLoadMoreListener(() -> {
                        hasLoadMore = true;
                        pageCount++;
                        /**
                         * 门店端
                         */
                        if (Contants.CHOOSE_MODEL_SIZE == 5) {           // 出库记录
                            OutInRecord();
                            RequestPost.getInventoryOut(String.valueOf(pageCount), mType, "", mStoreId, "", "", OutInBoundActivity.this);
                        }
                        if (Contants.CHOOSE_MODEL_SIZE == 6) {           // 入库记录
                            OutInRecord();
                            RequestPost.getInventoryIn(String.valueOf(pageCount), mType, "", mStoreId, "", "", OutInBoundActivity.this);
                        }
                        // 批发商
                        if (Contants.CHOOSE_MODEL_SIZE == 110) {        // 出库记录
                            wholesalersOutInBounds(1);
                            RequestPost.getInventoryOut(String.valueOf(pageCount), mType, "", mStoreId, "", "", OutInBoundActivity.this);
                        }
                        if (Contants.CHOOSE_MODEL_SIZE == 111) {         // 入库记录
                            wholesalersOutInBounds(0);
                            RequestPost.getInventoryIn(String.valueOf(pageCount), mType, "", mStoreId, "", "", OutInBoundActivity.this);
                        }
                    });
                    // 下拉刷新
                    mRefreshLayout.setOnRefreshListener(() -> {
                        pageCount = 0;
                        /**
                         * 门店端
                         */
                        if (Contants.CHOOSE_MODEL_SIZE == 5) {           // 出库记录
                            OutInRecord();
                            mCopyList = new ArrayList<>();
                            RequestPost.getInventoryOut(String.valueOf(pageCount), "", "", "", "", "", OutInBoundActivity.this);
                        }
                        if (Contants.CHOOSE_MODEL_SIZE == 6) {           // 入库记录
                            OutInRecord();
                            mCopyList = new ArrayList<>();
                            RequestPost.getInventoryIn(String.valueOf(pageCount), "", "", "", "", "", OutInBoundActivity.this);
                        }
                        // 批发商
                        if (Contants.CHOOSE_MODEL_SIZE == 110) {        // 出库记录
                            wholesalersOutInBounds(1);

                        }
                        if (Contants.CHOOSE_MODEL_SIZE == 111) {         // 入库记录
                            wholesalersOutInBounds(0);
                        }
                    });
                    // 第一次进来的时候刷新
                    mRefreshLayout.setOnAutoLoadListener(() -> {
                        // default query new --
                        /**
                         * 门店端
                         */
                        if (Contants.CHOOSE_MODEL_SIZE == 5) {           // 出库记录
                            OutInRecord();
                            // request -- default query all
                            RequestPost.getInventoryOut(String.valueOf(pageCount), mType, "", "", "", "", OutInBoundActivity.this);
                        }
                        if (Contants.CHOOSE_MODEL_SIZE == 6) {           // 入库记录
                            OutInRecord();
                            // request -- default query all
                            RequestPost.getInventoryIn(String.valueOf(pageCount), mType, "", "", "", "", OutInBoundActivity.this);
                        }
                        // 批发商
                        if (Contants.CHOOSE_MODEL_SIZE == 110) {        // 出库记录
                            wholesalersOutInBounds(1);
                            RequestPost.getInventoryOut(String.valueOf(pageCount), mType, "", "", "", "", OutInBoundActivity.this);
                        }
                        if (Contants.CHOOSE_MODEL_SIZE == 111) {         // 入库记录
                            wholesalersOutInBounds(0);
                            RequestPost.getInventoryIn(String.valueOf(pageCount), mType, "", "", "", "", OutInBoundActivity.this);
                        }
                    });
                    mRefreshLayout.autoRefresh();
                    break;
            }
        }
    };

    @Override
    public void onHttpFailure(HttpResponse result) {

    }

    @Override
    public void onHttpSucceed(HttpResponse result) {
        Map<String, String> body = null;
        try {
            body = JsonParser.parseJSONObject(result.body());
        } catch (Exception e) {
            toast("数据异常");
        }
        if (body != null) {
            if ("1".equals(body.get("code"))) {
                // 出库记录
                if (result.url().contains("wxapi/v1/stock.php?type=getInventoryOut")) {
                    // 出库记录列表
                    List<InventoryOutBean> outList = JsonParser.parseJSONArray(InventoryOutBean.class, JsonParser.parseJSONObject(body.get("data")).get("stocklist"));
                    // 出库门店列表
                    List<SupplierBean> supplierList = JsonParser.parseJSONArray(SupplierBean.class, JsonParser.parseJSONObject(body.get("data")).get("storelist"));
                    if (supplierList != null) {
                        mTopStoreList = new ArrayList<>();
                        mTopStoreList.addAll(supplierList);
                    }
                    // 出库记录订单类型
                    Map<String, String> map = JsonParser.parseJSONObject(JsonParser.parseJSONObject(body.get("data")).get("orderType"));
                    orderTypeList = new ArrayList<>();
                    for (Map.Entry<String, String> entry : map.entrySet()) {
                        orderTypeList.add(entry.getValue());
                    }
                    if (outList != null && ListUtils.getSize(outList) != 0) {
                        mCopyList.addAll(outList);
                    } else {
                        if (!hasLoadMore)
                            mCopyList = new ArrayList<>();
                    }
                    Message msg = new Message();
                    msg.what = INITIAL_SIZE;
                    mHandler.sendMessage(msg);
                }
                // 入库记录
                if (result.url().contains("wxapi/v1/stock.php?type=getInventoryIn")) {
                    // 出库记录列表
                    List<InventoryOutBean> inList = JsonParser.parseJSONArray(InventoryOutBean.class, JsonParser.parseJSONObject(body.get("data")).get("stocklist"));
                    if (inList != null) {
                        mCopyList.addAll(inList);
                    }else {
                        if (!hasLoadMore)
                            mCopyList = new ArrayList<>();
                    }
                    // 门店列表
                    List<SupplierBean> supplierList = JsonParser.parseJSONArray(SupplierBean.class, JsonParser.parseJSONObject(body.get("data")).get("storelist"));
                    if (supplierList != null) {
                        mTopStoreList = new ArrayList<>();
                        mTopStoreList.addAll(supplierList);
                    }
                    // 订单类型
                    Map<String, String> map = JsonParser.parseJSONObject(JsonParser.parseJSONObject(body.get("data")).get("orderType"));
                    orderTypeList = new ArrayList<>();
                    for (Map.Entry<String, String> entry : map.entrySet()) {
                        orderTypeList.add(entry.getValue());
                    }

                    Message msg = new Message();
                    msg.what = INITIAL_SIZE;
                    mHandler.sendMessage(msg);
                }
            } else {
                toast(body.get("warn"));
            }
            if (getDialog() != null)
                dismissLoading();
        }
    }
}
