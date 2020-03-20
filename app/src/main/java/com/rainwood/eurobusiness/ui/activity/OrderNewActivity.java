package com.rainwood.eurobusiness.ui.activity;

import android.annotation.SuppressLint;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.rainwood.eurobusiness.R;
import com.rainwood.eurobusiness.base.BaseActivity;
import com.rainwood.eurobusiness.common.Contants;
import com.rainwood.eurobusiness.domain.CommonUIBean;
import com.rainwood.eurobusiness.domain.OrderBean;
import com.rainwood.eurobusiness.other.BaseDialog;
import com.rainwood.eurobusiness.ui.adapter.OrderNewAdapter;
import com.rainwood.eurobusiness.ui.dialog.DateDialog;
import com.rainwood.tools.viewinject.ViewById;
import com.rainwood.tools.widget.MeasureListView;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * @Author: a797s
 * @Date: 2020/2/19
 * @Desc: 新建订单 --- 新建采购单
 */
public class OrderNewActivity extends BaseActivity implements View.OnClickListener {

    @Override
    protected int getLayoutId() {
        return R.layout.activity_order_new;
    }

    @ViewById(R.id.iv_back)
    private ImageView pageBack;
    @ViewById(R.id.tv_title)
    private TextView pageTitle;
    @ViewById(R.id.lv_content_list)
    private MeasureListView contentList;
    @ViewById(R.id.rl_add_shop)
    private RelativeLayout addShop;                 // 添加商品
    @ViewById(R.id.iv_add_img)
    private ImageView addImg;                       // 添加商品img
    @ViewById(R.id.tv_add_text)
    private TextView addText;                       // 添加商品 text

    // handler Size
    private final int ORDER_TIME_SIZE = 0x1124;

    @Override
    protected void initView() {
        pageBack.setOnClickListener(this);
        addShop.setOnClickListener(this);
        if (Contants.CHOOSE_MODEL_SIZE == 4) {
            pageTitle.setText("新建订单");
            Message msg = new Message();
            msg.what = ORDER_TIME_SIZE;
            mHandler.sendMessage(msg);
        }

        if (Contants.CHOOSE_MODEL_SIZE == 109) {
            pageTitle.setText("新建采购单");
            Message msg = new Message();
            msg.what = ORDER_TIME_SIZE;
            mHandler.sendMessage(msg);
        }
    }

    /**
     * 时间控件
     */
    private void getDate(int parentPos, int position) {
        // 日期选择对话框
        new DateDialog.Builder(this)
                .setTitle(getString(R.string.date_title))
                // 确定文本
                .setConfirm(getString(R.string.common_confirm))
                // 设置为null 时表示不显示取消按钮
                .setCancel(getString(R.string.common_cancel))
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
                    @Override
                    public void onSelected(BaseDialog dialog, int year, int month, int day) {
                        // toast(year + "-" + "-" + month + "-" + day);
                        mList.get(parentPos).getCommonList().get(position).setShowText(year + "年" + month + "月" + day + "日");

                        Message msg = new Message();
                        msg.what = ORDER_TIME_SIZE;
                        mHandler.sendMessage(msg);

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
                        //toast("取消了");
                        dialog.dismiss();
                    }
                }).show();
    }

    @Override
    protected void initData() {
        super.initData();
        mList = new ArrayList<>();
        for (int i = 0; i < titles.length && Contants.CHOOSE_MODEL_SIZE == 4; i++) {
            OrderBean order = new OrderBean();
            order.setTitle(titles[i]);
            // 订单信息
            List<CommonUIBean> orderInfoList = new ArrayList<>();
            for (int j = 0; j < orderTitle.length && i == 0; j++) {
                CommonUIBean commonUI = new CommonUIBean();
                commonUI.setTitle(orderTitle[j]);
                commonUI.setLabel(orderLabel[j]);
                if (orderLabel[j].equals("请选择")) {
                    commonUI.setArrowType(1);
                } else {
                    commonUI.setArrowType(0);
                }
                orderInfoList.add(commonUI);
            }
            order.setCommonList(orderInfoList);
            mList.add(order);
        }

        // 新建采购单
        for (int i = 0; i < salerTitel.length && Contants.CHOOSE_MODEL_SIZE == 109; i++) {
            OrderBean order = new OrderBean();
            order.setTitle(titles[i]);
            // 订单信息
            List<CommonUIBean> orderInfoList = new ArrayList<>();
            for (int j = 0; j < baseTitles.length && i == 0; j++) {
                CommonUIBean commonUI = new CommonUIBean();
                commonUI.setTitle(baseTitles[j]);
                commonUI.setLabel(labels[j]);
                if (labels[j].equals("请选择")) {
                    commonUI.setArrowType(1);
                } else {
                    commonUI.setArrowType(0);
                }
                orderInfoList.add(commonUI);
            }
            for (int j = 0; j < goodsTitles.length && i == 1; j++) {
                CommonUIBean commonUI = new CommonUIBean();
                commonUI.setTitle(baseTitles[j]);
                commonUI.setLabel(labels[j]);
                if (labels[j].equals("请选择")) {
                    commonUI.setArrowType(1);
                } else {
                    commonUI.setArrowType(0);
                }
                orderInfoList.add(commonUI);
            }
            order.setCommonList(orderInfoList);
            mList.add(order);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_back:
                finish();
                break;
            case R.id.rl_add_shop:
                // toast("添加商品");
                if (Contants.CHOOSE_MODEL_SIZE == 4) {
                    openActivity(OrderAddShopActivity.class);
                }
                if (Contants.CHOOSE_MODEL_SIZE == 109) {
                    toast("添加规格");
                }
                break;
        }
    }


    @SuppressLint("HandlerLeak")
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(@NonNull Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case ORDER_TIME_SIZE:
                    // Log.d("sxs", "---" + mList.toString());
                    OrderNewAdapter newAdapter = new OrderNewAdapter(OrderNewActivity.this, mList);
                    contentList.setAdapter(newAdapter);
                    newAdapter.setOnClickEditText((parentPos, position) -> {
                        // toast(mList.get(parentPos).getCommonList().get(position).getTitle());
                        switch (mList.get(parentPos).getCommonList().get(position).getTitle()) {
                            case "收货地址":
                                toast("待开发");
                                break;
                            case "交货时间":
                                if (Contants.CHOOSE_MODEL_SIZE == 4) {
                                    getDate(parentPos, position);
                                }
                                break;
                            case "供应商":
                                toast("选择供应商");
                                break;
                            case "商品分类":
                                toast("选择商品分类");
                                break;
                            case "商品名称":
                                toast("选择商品名称");
                                break;
                        }
                    });
                    break;
            }
        }
    };

    /*
    模拟数据
     */
    private List<OrderBean> mList;
    private String[] titles = {"订单信息", "商品信息"};
    private String[] orderTitle = {"公司名称", "收货地址", "交货时间", "备注"};
    private String[] orderLabel = {"请输入", "请选择", "请选择", "请输入"};
    // 新建采购单
    private String[] salerTitel = {"基本信息", "商品信息"};
    private String[] baseTitles = {"供应商", "交货时间"};
    private String[] goodsTitles = {"商品分类", "商品名称"};
    private String[] labels = {"请选择", "请选择"};
}
