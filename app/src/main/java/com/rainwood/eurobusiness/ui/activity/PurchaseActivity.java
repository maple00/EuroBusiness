package com.rainwood.eurobusiness.ui.activity;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
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
import com.rainwood.eurobusiness.domain.PressBean;
import com.rainwood.eurobusiness.domain.PurchasesBean;
import com.rainwood.eurobusiness.json.JsonParser;
import com.rainwood.eurobusiness.okhttp.HttpResponse;
import com.rainwood.eurobusiness.okhttp.OnHttpListener;
import com.rainwood.eurobusiness.other.BaseDialog;
import com.rainwood.eurobusiness.request.RequestPost;
import com.rainwood.eurobusiness.ui.adapter.GoodsStatusAdapter;
import com.rainwood.eurobusiness.ui.adapter.PurchaseGoodsAdapter;
import com.rainwood.eurobusiness.ui.adapter.TopTypeAdapter;
import com.rainwood.eurobusiness.ui.dialog.DateDialog;
import com.rainwood.tools.viewinject.ViewById;
import com.rainwood.tools.widget.MeasureGridView;
import com.rainwood.tools.widget.MeasureListView;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Map;

/**
 * @Author: a797s
 * @Date: 2020/2/16
 * @Desc: 采购记录
 */
public class PurchaseActivity extends BaseActivity implements View.OnClickListener, OnHttpListener {


    @Override
    protected int getLayoutId() {
        return R.layout.activity_purchase;
    }

    @ViewById(R.id.iv_back)
    private ImageView pageBack;
    @ViewById(R.id.gv_top_type)
    private MeasureGridView topType;
    @ViewById(R.id.gv_goods_status)
    private MeasureGridView goodsStatus;
    @ViewById(R.id.ll_search)
    private LinearLayout search;
    @ViewById(R.id.ll_search_1)
    private LinearLayout search1;
    @ViewById(R.id.iv_new_found)
    private ImageView newFound;
    @ViewById(R.id.lv_goods_list)
    private MeasureListView goodsLists;
    @ViewById(R.id.iv_screening)
    private ImageView screening;            // 筛选

    // handler 码
    private final int STATUS_SIZE = 0x1124;
    private final int INITIAL_SIZE = 0x1123;
    // 头部选中位置标记
    private static int posFalg;

    @Override
    protected void initView() {
        pageBack.setOnClickListener(this);
        screening.setOnClickListener(this);
        newFound.setOnClickListener(this);

        // default query new -- 默认查询供应商采购订单的全部
        showLoading("loading");
        RequestPost.getPurchaseOrderlist("new", "", "", "", "",
                "", this);
    }

    @Override
    protected void initData() {
        super.initData();
        // 头部
        mTopList = new ArrayList<>();
        for (int i = 0; i < topTitles.length && Contants.CHOOSE_MODEL_SIZE == 2; i++) {
            search1.setVisibility(View.GONE);
            newFound.setVisibility(View.GONE);
            PressBean press = new PressBean();
            press.setTitle(topTitles[i]);
            press.setChoose(false);
            if (i == 0) {
                press.setChoose(true);
            }
            mTopList.add(press);
        }
        for (int i = 0; i < salerStatus.length && Contants.CHOOSE_MODEL_SIZE == 109; i++) {
            search.setVisibility(View.GONE);
            screening.setVisibility(View.GONE);
            PressBean press = new PressBean();
            press.setTitle(salerStatus[i]);
            press.setChoose(false);
            if (i == 0) {
                press.setChoose(true);
            }
            mTopList.add(press);
        }

        // 商品状态
        statuList = new ArrayList<>();
        for (int i = 0; i < status.length; i++) {
            PressBean press = new PressBean();
            press.setChoose(false);
            press.setTitle(status[i]);
            if (status[i].equals("全部")) {
                press.setChoose(true);
            }
            statuList.add(press);
        }
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
            case R.id.iv_new_found:
                openActivity(OrderNewActivity.class);
                break;
            default:
                throw new IllegalStateException("Unexpected value: " + v.getId());
        }
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
            // 通过时间查询订单列表 -- 默认查询当前选中的类型的全部状态
            showLoading("loading");
            String type = "";
            switch (posFalg){
                case 0:             // 采购订单
                    type = "new";
                    break;
                case 1:             // 补货订单
                    type = "charge";
                    break;
            }
            RequestPost.getPurchaseOrderlist(type, "", "", "", startTime.getText().toString().trim(),
                    endTime.getText().toString().trim(), this);
            //toast("您选择了：" + startTime.getText().toString() + "至" + endTime.getText().toString());
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
                // 确定文本
                .setConfirm(getString(R.string.common_confirm))
                // 设置为null 时表示不显示取消按钮
                // .setCancel(getString(R.string.common_clear_screening))
                .setCancel(null)
                // 设置日期(可支持2019-12-03， 20191203， 时间戳)
                // .setDate(20191203)
                // 设置年份
                //.setYear(2019)
                // 设置月份
                //.setMonth(12)
                // 设置天数
                //.setDay(3)
                // 不选择天数
                //.setIgnoreDay()
                .setListener(new DateDialog.OnListener() {
                    @SuppressLint("SetTextI18n")
                    @Override
                    public void onSelected(BaseDialog dialog, int year, int month, int day) {
                        // toast(year + "-" + "-" + month + "-" + day);
                        time.setText(year + "-" + "-" + month + "-" + day);

                        // 如果不指定时分秒则默认为现在的时间
                        Calendar calendar = Calendar.getInstance();
                        calendar.set(Calendar.YEAR, year);
                        // 月份从零开始，所以需要减 1
                        calendar.set(Calendar.MONTH, month - 1);
                        calendar.set(Calendar.DAY_OF_MONTH, day);
                        // toast("时间戳：" + calendar.getTimeInMillis());
                        //toast(new SimpleDateFormat("yyyy年MM月dd日 kk:mm:ss").format(calendar.getTime()));
                    }

                    @Override
                    public void onCancel(BaseDialog dialog) {
                        toast("取消了");
                    }
                }).show();
    }

    @SuppressLint("HandlerLeak")
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(@NonNull Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case INITIAL_SIZE:
                    // 门店端
                    if (Contants.CHOOSE_MODEL_SIZE == 2) {
                        setValues();
                    }
                    // 批发商端
                    if (Contants.CHOOSE_MODEL_SIZE == 109) {
                        setValues();
                    }
                    break;
                case STATUS_SIZE:                   // 查询不同入库的情况
                    GoodsStatusAdapter statusAdapter = new GoodsStatusAdapter(PurchaseActivity.this, statuList);
                    goodsStatus.setAdapter(statusAdapter);
                    statusAdapter.setOnClickItem(position -> {
                        for (PressBean pressBean : statuList) {
                            pressBean.setChoose(false);
                        }
                        statuList.get(position).setChoose(true);
                        // query 不同的类型的不同状态
                        String topType = "";
                        String status;
                        switch (posFalg) {
                            case 0:
                                topType = "";
                                break;
                            case 1:
                                topType = "charge";
                                break;
                        }
                        switch (position) {
                            case 1:
                                status = "waitIn";
                                break;
                            case 2:
                                status = "complete";
                                break;
                            default:
                                status = "";
                                break;
                        }
                        // request
                        showLoading("loading");
                        RequestPost.getPurchaseOrderlist(topType, "", "", status, "",
                                "", PurchaseActivity.this);

                    });
                    break;
            }
        }
    };

    /**
     * 设置UI
     */
    private void setValues() {
        TopTypeAdapter typeAdapter = new TopTypeAdapter(PurchaseActivity.this, mTopList);
        topType.setAdapter(typeAdapter);
        topType.setNumColumns(2);
        typeAdapter.setOnClickItem(position -> {
            for (PressBean pressBean : mTopList) {
                pressBean.setChoose(false);
            }
            mTopList.get(position).setChoose(true);
            for (PressBean pressBean : statuList) {
                pressBean.setChoose(false);
            }
            statuList.get(0).setChoose(true);
            String topType;
            if (position == 1) {
                topType = "charge";
            } else {
                topType = "";
            }
            posFalg = position;
            // request -- default query all
            showLoading("loading");
            RequestPost.getPurchaseOrderlist(topType, "", "", "", "",
                    "", PurchaseActivity.this);
        });
        // 商品状态
        Message statusMsg = new Message();
        statusMsg.what = STATUS_SIZE;
        mHandler.sendMessage(statusMsg);
        //  商品列表
        PurchaseGoodsAdapter goodsAdapter = new PurchaseGoodsAdapter(PurchaseActivity.this, goodsList);
        goodsLists.setAdapter(goodsAdapter);
        goodsAdapter.setOnClickItem(position -> openActivity(PurchaseDetailActivity.class));
    }

    private List<PressBean> mTopList;
    private String[] topTitles = {"供应商采购订单", "申请补货订单"};
    // 商品状态
    private List<PressBean> statuList;
    private String[] status = {"全部", "待入库", "已完成"};
    private String[] salerStatus = {"采购订单", "补货订单"};
    // 商品列表
    private List<PurchasesBean> goodsList;

    @Override
    public void onHttpFailure(HttpResponse result) {

    }

    @Override
    public void onHttpSucceed(HttpResponse result) {
        Log.d(TAG, " --- result --- " + result);
        Map<String, String> body = JsonParser.parseJSONObject(result.body());
        if (body != null) {
            if ("1".equals(body.get("code"))) {
                // 获取采购记录列表
                if (result.url().contains("wxapi/v1/order.php?type=getPurchaseOrderlist")) {
                    goodsList = JsonParser.parseJSONArray(PurchasesBean.class, JsonParser.parseJSONObject(body.get("data")).get("datalist"));

                    Message msg = new Message();
                    msg.what = INITIAL_SIZE;
                    mHandler.sendMessage(msg);
                }
            } else {
                toast(body.get("data"));
            }
            dismissLoading();
        }
    }
}
