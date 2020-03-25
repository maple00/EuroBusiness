package com.rainwood.eurobusiness.ui.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.rainwood.eurobusiness.R;
import com.rainwood.eurobusiness.base.BaseActivity;
import com.rainwood.eurobusiness.common.Contants;
import com.rainwood.eurobusiness.domain.ClientBaseBean;
import com.rainwood.eurobusiness.domain.CommonUIBean;
import com.rainwood.eurobusiness.domain.InvoiceDetailBean;
import com.rainwood.eurobusiness.domain.OrderBean;
import com.rainwood.eurobusiness.domain.TakeGoodsBean;
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
 * @Date: 2020/2/23
 * @Desc: 客户详情
 */
public class CustomDetailActivity extends BaseActivity implements View.OnClickListener, OnHttpListener {
    @Override
    protected int getLayoutId() {
        return R.layout.activity_custom_detail;
    }

    @ViewById(R.id.iv_back)
    private ImageView pageBack;
    @ViewById(R.id.tv_title)
    private TextView pageTitle;
    @ViewById(R.id.tv_right_text)
    private TextView rightText;
    @ViewById(R.id.lv_content_list)
    private MeasureListView contentList;

    private final int INITIAL_SIZE = 0x101;

    private List<OrderBean> mList;
    private String[] titles = {"基本资料", "开票信息", "收货信息", "支付信息"};
    // 基本资料
    private String[] baseTitle = {"公司名称", "联系人", "手机号", "邮箱"};
    // 开票信息
    private String[] invoiceTitle = {"公司名称", "税号(P.IVA)", "PEC邮箱/SDI", "手机号", "所在地区", "详细地址"};
    // 收货信息
    private String[] goodsTitle = {"公司名称", "手机号", "所在地区", "详细地址"};
    // 支付信息
    private String[] payTitle = {"客户类型", "支付方式", "支付年限", "备注"};

    // 编辑对象
    private ClientEditDetailBean mClientEditDetail;

    @Override
    protected void initView() {
        pageBack.setOnClickListener(this);
        pageTitle.setText("客户详情");
        rightText.setText("编辑");
        rightText.setTextColor(getResources().getColor(R.color.black));
        rightText.setOnClickListener(this);

        // request
        showLoading("loading");
        String customId = getIntent().getStringExtra("customId");
        RequestPost.getClientDetail(customId, this);
    }

    @Override
    protected void initData() {
        super.initData();
        mList = new ArrayList<>();
        for (int i = 0; i < titles.length; i++) {
            OrderBean order = new OrderBean();
            order.setTitle(titles[i]);
            List<CommonUIBean> commonUIList = new ArrayList<>();
            // 基本资料
            for (int j = 0; j < baseTitle.length && i == 0; j++) {
                CommonUIBean commonUI = new CommonUIBean();
                commonUI.setTitle(baseTitle[j]);
                commonUIList.add(commonUI);
            }
            // 开票信息
            for (int j = 0; j < invoiceTitle.length && i == 1; j++) {
                CommonUIBean commonUI = new CommonUIBean();
                commonUI.setTitle(invoiceTitle[j]);
                commonUIList.add(commonUI);
            }
            // 收货信息
            for (int j = 0; j < goodsTitle.length && i == 2; j++) {
                CommonUIBean commonUI = new CommonUIBean();
                commonUI.setTitle(goodsTitle[j]);
                commonUI.setShowText(goodsTitle[j]);
                commonUIList.add(commonUI);
            }
            // 支付信息
            for (int j = 0; j < payTitle.length && i == 3; j++) {
                CommonUIBean commonUI = new CommonUIBean();
                commonUI.setTitle(payTitle[j]);
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
            case R.id.tv_right_text:
                // toast("编辑");
                Contants.CHOOSE_MODEL_SIZE = 15;
                Intent intent = new Intent(this, CustomNewActivity.class);
                intent.putExtra("editClient", mClientEditDetail);
                startActivity(intent);
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
                    OutBoundDetailAdapter boundAdapter = new OutBoundDetailAdapter(CustomDetailActivity.this, mList);
                    contentList.setAdapter(boundAdapter);
                    break;
            }
        }
    };

    @Override
    public void onHttpFailure(HttpResponse result) {

    }

    @Override
    public void onHttpSucceed(HttpResponse result) {
        Map<String, String> body = JsonParser.parseJSONObject(result.body());
        if (body != null) {
            if ("1".equals(body.get("code"))) {
                // 获取客户详情
                if (result.url().contains("wxapi/v1/client.php?type=getClientInfo")) {
                    Map<String, String> data = JsonParser.parseJSONObject(body.get("data"));
                    Log.d(TAG, " --- data ---" + body.get("data"));
                    // 基本资料
                    ClientBaseBean kehuInfo = JsonParser.parseJSONObject(ClientBaseBean.class, data.get("kehuInfo"));
                    // 开票信息
                    InvoiceDetailBean kehuInvoice = JsonParser.parseJSONObject(InvoiceDetailBean.class, data.get("kehuInvoice"));
                    // 收货信息
                    TakeGoodsBean kehuAddress = JsonParser.parseJSONObject(TakeGoodsBean.class, data.get("kehuAddress"));
                    // 支付信息
                    ClientBaseBean payInfos = JsonParser.parseJSONObject(ClientBaseBean.class, data.get("kehuInfo"));

                    // 设置编辑对象
                    mClientEditDetail = new ClientEditDetailBean();
                    mClientEditDetail.setClientBase(kehuInfo);
                    mClientEditDetail.setInvoiceDetai(kehuInvoice);
                    mClientEditDetail.setTakeGoods(kehuAddress);

                    for (int i = 0; i < mList.size(); i++) {
                        switch (i) {
                            case 0:             // 基本信息
                                if (kehuInfo != null)
                                    setValue(i, kehuInfo.getCompany(), kehuInfo.getName(), kehuInfo.getTel(), kehuInfo.getEmail());
                                break;
                            case 1:             // 开票信息
                                for (int j = 0; j < ListUtils.getSize(mList.get(i).getCommonList()); j++) {
                                    if (kehuInvoice != null)
                                        switch (j) {
                                            case 0:
                                                mList.get(i).getCommonList().get(j).setShowText(kehuInvoice.getName());
                                                break;
                                            case 1:
                                                mList.get(i).getCommonList().get(j).setShowText(kehuInvoice.getParagraph());
                                                break;
                                            case 2:
                                                mList.get(i).getCommonList().get(j).setShowText(kehuInvoice.getEmail());
                                                break;
                                            case 3:
                                                mList.get(i).getCommonList().get(j).setShowText(kehuInvoice.getConsigneeTel());
                                                break;
                                            case 4:
                                                mList.get(i).getCommonList().get(j).setShowText(kehuInvoice.getRegion());
                                                break;
                                            case 5:
                                                mList.get(i).getCommonList().get(j).setShowText(kehuInvoice.getAddressMx());
                                                break;
                                        }
                                }
                                break;
                            case 2:             // 收货信息
                                if (kehuAddress != null)
                                    setValue(i, kehuAddress.getCompanyName(), kehuAddress.getContactTel(), kehuAddress.getRegion(), kehuAddress.getAddressMx());
                                break;
                            case 3:             // 开票信息
                                if (payInfos != null)
                                    setValue(i, payInfos.getKehuTypeName(), payInfos.getPayType(), payInfos.getPayTerm(), payInfos.getText());
                                break;
                        }
                    }

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

    /**
     * 设置值
     *
     * @param i
     * @param params1
     * @param params2
     * @param params3
     * @param params4
     */
    private void setValue(int i, String params1, String params2, String params3, String params4) {
        for (int j = 0; j < ListUtils.getSize(mList.get(i).getCommonList()); j++) {
            switch (j) {
                case 0:
                    mList.get(i).getCommonList().get(j).setShowText(params1);
                    break;
                case 1:
                    mList.get(i).getCommonList().get(j).setShowText(params2);
                    break;
                case 2:
                    mList.get(i).getCommonList().get(j).setShowText(params3);
                    break;
                case 3:
                    mList.get(i).getCommonList().get(j).setShowText(params4);
                    break;
            }

        }
    }
}
