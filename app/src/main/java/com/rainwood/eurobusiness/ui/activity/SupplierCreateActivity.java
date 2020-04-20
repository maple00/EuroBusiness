package com.rainwood.eurobusiness.ui.activity;

import android.annotation.SuppressLint;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.rainwood.eurobusiness.R;
import com.rainwood.eurobusiness.base.BaseActivity;
import com.rainwood.eurobusiness.domain.SuppierBean;
import com.rainwood.eurobusiness.json.JsonParser;
import com.rainwood.eurobusiness.okhttp.HttpResponse;
import com.rainwood.eurobusiness.okhttp.OnHttpListener;
import com.rainwood.eurobusiness.other.BaseDialog;
import com.rainwood.eurobusiness.request.RequestPost;
import com.rainwood.eurobusiness.ui.dialog.MenuDialog;
import com.rainwood.eurobusiness.utils.ListUtils;
import com.rainwood.tools.view.ClearEditText;
import com.rainwood.tools.viewinject.ViewById;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @Author: a797s
 * @Date: 2020/4/14 15:29
 * @Desc: 新建供应商
 */
public final class SupplierCreateActivity extends BaseActivity implements View.OnClickListener, OnHttpListener {

    @ViewById(R.id.iv_back)
    private ImageView mPageBack;
    @ViewById(R.id.tv_title)
    private TextView mPageTitle;
    @ViewById(R.id.tv_right_text)
    private TextView rightText;
    @ViewById(R.id.cet_supplier)
    private ClearEditText supplier;
    @ViewById(R.id.cet_tel)
    private ClearEditText tel;
    @ViewById(R.id.cet_head_man)
    private ClearEditText headMan;
    @ViewById(R.id.tv_pay_method)
    private TextView payMethod;
    @ViewById(R.id.cet_bank_card)
    private ClearEditText bankCard;
    @ViewById(R.id.cet_email)
    private ClearEditText email;
    @ViewById(R.id.tv_region)
    private TextView region;
    @ViewById(R.id.cet_address)
    private ClearEditText address;
    @ViewById(R.id.btn_confirm)
    private Button confirm;

    // 供应商id
    private String mSupplierId;
    // 支付方式
    private List<String> payMethodList;
    // 国家地区
    private List<String> regionList;

    private final int INITIAL_SIZE = 0x101;
    private SuppierBean mSuppierBean;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_create_supplier;
    }

    @Override
    protected void initView() {
        mSupplierId = getIntent().getStringExtra("supplyId");
        if (mSupplierId == null || "".equals(mSupplierId)) {         // 新建供应商
            mPageTitle.setText("新建供应商");
            rightText.setVisibility(View.GONE);
            // TODO:获取新建供应商页面数据
            showLoading("");
            RequestPost.newSupplierPage(this);
        } else {
            mPageTitle.setText("供应商详情");
            rightText.setText("删除");
            rightText.setVisibility(View.VISIBLE);
            // TODO: 编辑供应商详情
            showLoading("");
            RequestPost.getSupplierDetail(mSupplierId, this);
        }
        //
        initEvents();
    }

    private void initEvents() {
        mPageBack.setOnClickListener(this);
        rightText.setOnClickListener(this);
        payMethod.setOnClickListener(this);
        region.setOnClickListener(this);
        confirm.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_back:
                finish();
                break;
            case R.id.tv_pay_method:            // 选择支付方式
                setMenuDialogValue(payMethodList, payMethod);
                break;
            case R.id.tv_region:                // 选择地区
                setMenuDialogValue(regionList, region);
                break;
            case R.id.tv_right_text:            // 删除供应商
                // TODO： 删除供应商
                showLoading("");
                RequestPost.delSupplier(mSupplierId, this);
                break;
            case R.id.btn_confirm:
                if (TextUtils.isEmpty(supplier.getText())) {
                    toast("请输入供应商");
                    return;
                }
                if (TextUtils.isEmpty(tel.getText())) {
                    toast("请输入电话");
                    return;
                }
                // TODO
                showLoading("");
                RequestPost.supplierEdit(mSupplierId, supplier.getText().toString().trim(), tel.getText().toString().trim(),
                        TextUtils.isEmpty(headMan.getText()) ? "" : headMan.getText().toString().trim(),
                        TextUtils.isEmpty(bankCard.getText()) ? "" : bankCard.getText().toString().trim(),
                        TextUtils.isEmpty(email.getText()) ? "" : email.getText().toString().trim(),
                        TextUtils.isEmpty(region.getText()) ? "" : region.getText().toString().trim(),
                        TextUtils.isEmpty(address.getText()) ? "" : address.getText().toString().trim(),
                        TextUtils.isEmpty(payMethod.getText()) ? "" : payMethod.getText().toString().trim(),
                        this);
                break;
        }
    }

    /**
     * 选择设置值
     *
     * @param optionList
     * @param textView
     */
    private void setMenuDialogValue(List<String> optionList, TextView textView) {
        if (ListUtils.getSize(optionList) == 0) {
            toast("当前无选择方式");
            return;
        }
        new MenuDialog.Builder(this)
                .setCancel(R.string.common_cancel)
                .setAutoDismiss(false)
                .setList(optionList)
                .setCanceledOnTouchOutside(false)
                .setListener(new MenuDialog.OnListener<String>() {
                    @Override
                    public void onSelected(BaseDialog dialog, int position, String text) {
                        dialog.dismiss();
                        textView.setText(text);
                    }

                    @Override
                    public void onCancel(BaseDialog dialog) {
                        dialog.dismiss();
                    }
                }).show();
    }


    @SuppressLint("HandlerLeak")
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(@NonNull Message msg) {
            switch (msg.what) {
                case INITIAL_SIZE:
                    supplier.setText(mSuppierBean.getName());
                    tel.setText(mSuppierBean.getTel());
                    headMan.setText(mSuppierBean.getChargeName());
                    payMethod.setText(mSuppierBean.getPayType());
                    bankCard.setText(mSuppierBean.getBankNum());
                    email.setText(mSuppierBean.getEmail());
                    region.setText(mSuppierBean.getRegionId());
                    address.setText(mSuppierBean.getAddress());
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
                // 新建供应页面数据
                if (result.url().contains("wxapi/v1/supplier.php?type=newSupplierPage")) {
                    // 供应商id
                    mSupplierId = JsonParser.parseJSONObject(JsonParser.parseJSONObject(body.get("data")).get("info")).get("id");
                    // 支付方式
                    JSONArray payMethodArray = JsonParser.parseJSONArrayString(JsonParser.parseJSONObject(body.get("data")).get("payWay"));
                    payMethodList = new ArrayList<>();
                    if (payMethodArray != null) {
                        for (int i = 0; i < payMethodArray.length(); i++) {
                            try {
                                payMethodList.add(payMethodArray.getString(i));
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                    // 国家地区选择
                    JSONArray regionListArray = JsonParser.parseJSONArrayString(JsonParser.parseJSONObject(body.get("data")).get("regionlist"));
                    regionList = new ArrayList<>();
                    if (regionListArray != null) {
                        for (int i = 0; i < regionListArray.length(); i++) {
                            try {
                                regionList.add(regionListArray.getString(i));
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                }

                // 新建或者编辑供应商信息
                if (result.url().contains("wxapi/v1/supplier.php?type=supplierEdit")) {
                    toast(body.get("warn"));
                    postDelayed(this::finish, 599);
                }

                // 供应商详情数据
                if (result.url().contains("wxapi/v1/supplier.php?type=getSupplierInfo")) {
                    mSuppierBean = JsonParser.parseJSONObject(SuppierBean.class, JsonParser.parseJSONObject(body.get("data")).get("info"));

                    Message msg = new Message();
                    msg.what = INITIAL_SIZE;
                    mHandler.sendMessage(msg);
                }

                // 删除供应商
                if (result.url().contains("wxapi/v1/supplier.php?type=delSupplier")){
                    toast(body.get("warn"));
                    postDelayed(this::finish, 599);
                }
            } else {
                toast(body.get("warn"));
            }
            if (getDialog() != null)
                dismissLoading();
        }
    }
}
