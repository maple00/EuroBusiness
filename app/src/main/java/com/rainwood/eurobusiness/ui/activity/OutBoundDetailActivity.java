package com.rainwood.eurobusiness.ui.activity;

import android.annotation.SuppressLint;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.rainwood.eurobusiness.R;
import com.rainwood.eurobusiness.base.BaseActivity;
import com.rainwood.eurobusiness.common.Contants;
import com.rainwood.eurobusiness.domain.CommonUIBean;
import com.rainwood.eurobusiness.domain.OrderBean;
import com.rainwood.eurobusiness.json.JsonParser;
import com.rainwood.eurobusiness.okhttp.HttpResponse;
import com.rainwood.eurobusiness.okhttp.OnHttpListener;
import com.rainwood.eurobusiness.request.RequestPost;
import com.rainwood.eurobusiness.ui.adapter.OutBoundDetailAdapter;
import com.rainwood.eurobusiness.utils.ListUtils;
import com.rainwood.tools.viewinject.ViewById;
import com.rainwood.tools.widget.MeasureListView;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @Author: a797s
 * @Date: 2020/2/20
 * @Desc: 出库详情
 */
public class OutBoundDetailActivity extends BaseActivity implements View.OnClickListener, OnHttpListener {

    @Override
    protected int getLayoutId() {
        return R.layout.activity_bound_detail;
    }

    @ViewById(R.id.iv_back)
    private ImageView pageBack;
    @ViewById(R.id.tv_title)
    private TextView pageTitle;
    @ViewById(R.id.lv_content_list)
    private MeasureListView contentList;

    private final int INITIAL_SIZE = 0x101;

    @Override
    protected void initView() {
        pageBack.setOnClickListener(this);
        if (Contants.CHOOSE_MODEL_SIZE == 111 || Contants.CHOOSE_MODEL_SIZE == 6) {
            pageTitle.setText("入库详情");
        }
        if (Contants.CHOOSE_MODEL_SIZE == 5 || Contants.CHOOSE_MODEL_SIZE == 110) {
            pageTitle.setText("出库详情");
        }
        // 出入库商品id
        String inventoryId = getIntent().getStringExtra("inventoryId");
        showLoading("loading");
        if (inventoryId != null) {
            RequestPost.getStockChecklist(inventoryId, this);
        }else {
            toast("异常错误");
            dismissLoading();
        }
    }

    @Override
    protected void initData() {
        super.initData();
        mList = new ArrayList<>();
        for (int i = 0; i < titles.length; i++) {
            OrderBean order = new OrderBean();
            order.setTitle(titles[i]);
            List<CommonUIBean> commonUIList = new ArrayList<>();
            for (int j = 0; j < goodsTitle.length && i == 0; j++) {
                CommonUIBean commonUI = new CommonUIBean();
                commonUI.setTitle(goodsTitle[j]);
                commonUIList.add(commonUI);
            }
            for (int j = 0; j < boundTitle.length && i == 1; j++) {
                CommonUIBean commonUI = new CommonUIBean();
                commonUI.setTitle(boundTitle[j]);
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

    @SuppressLint("HandlerLeak")
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(@NonNull Message msg) {
            switch (msg.what) {
                case INITIAL_SIZE:
                    OutBoundDetailAdapter boundAdapter = new OutBoundDetailAdapter(OutBoundDetailActivity.this, mList);
                    contentList.setAdapter(boundAdapter);
                    break;
            }
        }
    };

    private List<OrderBean> mList;
    private String[] titles = {"商品信息", "出库信息"};
    private String[] goodsTitle = {"商品名称", "商品型号", "条码", "商品分类", "规格"};
    private String[] boundTitle = {"出库类型", "订单号", "数量", "操作人", "出库时间"};

    @Override
    public void onHttpFailure(HttpResponse result) {

    }

    @Override
    public void onHttpSucceed(HttpResponse result) {
        Map<String, String> body = JsonParser.parseJSONObject(result.body());
        if (body != null) {
            if ("1".equals(body.get("code"))) {
                // 出入库记录详情
                if (result.url().contains("wxapi/v1/stock.php?type=getInventoryMxInfo")) {
                    Map<String, String> info = JsonParser.parseJSONObject(JsonParser.parseJSONObject(body.get("data")).get("info"));
                    for (int i = 0; i < ListUtils.getSize(mList); i++) {
                        switch (i){
                            case 0:
                                getValues(info, i, "goodsName", "model", "barCode", "goodsType", "skuName");
                                break;
                            case 1:
                                getValues(info, i, "workFlow", "orderNo", "num", "adName", "time");
                                break;
                        }
                    }

                    Message msg = new Message();
                    msg.what = INITIAL_SIZE;
                    mHandler.sendMessage(msg);
                }
            }else {
                toast(body.get("warn"));
            }
            dismissLoading();
        }
    }

    /**
     * 设置值
     * @param info
     * @param i
     * @param workFlow
     * @param orderNo
     * @param num
     * @param adName
     * @param time
     */
    private void getValues(Map<String, String> info, int i, String workFlow, String orderNo, String num, String adName, String time) {
        for (int j = 0; j < ListUtils.getSize(mList.get(i).getCommonList()); j++) {
            switch (j) {
                case 0:
                    mList.get(i).getCommonList().get(j).setShowText(info.get(workFlow));
                    break;
                case 1:
                    mList.get(i).getCommonList().get(j).setShowText(info.get(orderNo));
                    break;
                case 2:
                    mList.get(i).getCommonList().get(j).setShowText(info.get(num));
                    break;
                case 3:
                    mList.get(i).getCommonList().get(j).setShowText(info.get(adName));
                    break;
                case 4:
                    mList.get(i).getCommonList().get(j).setShowText(info.get(time));
                    break;
            }
        }
    }
}
