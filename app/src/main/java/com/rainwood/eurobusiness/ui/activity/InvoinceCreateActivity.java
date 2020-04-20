package com.rainwood.eurobusiness.ui.activity;

import android.annotation.SuppressLint;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.rainwood.eurobusiness.R;
import com.rainwood.eurobusiness.base.BaseActivity;
import com.rainwood.eurobusiness.domain.InvoicesBean;
import com.rainwood.eurobusiness.json.JsonParser;
import com.rainwood.eurobusiness.okhttp.HttpResponse;
import com.rainwood.eurobusiness.okhttp.OnHttpListener;
import com.rainwood.eurobusiness.other.BaseDialog;
import com.rainwood.eurobusiness.request.RequestPost;
import com.rainwood.eurobusiness.ui.dialog.MenuDialog;
import com.rainwood.tools.view.ClearEditText;
import com.rainwood.tools.viewinject.ViewById;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @Author: a797s
 * @Date: 2020/4/15 10:03
 * @Desc: 发票地址创建
 */
public final class InvoinceCreateActivity extends BaseActivity implements View.OnClickListener, OnHttpListener {

    @ViewById(R.id.iv_back)
    private ImageView mPageBack;
    @ViewById(R.id.tv_title)
    private TextView mPageTitle;
    @ViewById(R.id.cet_company)
    private ClearEditText company;
    @ViewById(R.id.cet_tax_num)
    private ClearEditText taxNum;
    @ViewById(R.id.cet_email)
    private ClearEditText email;
    @ViewById(R.id.cet_mobile)
    private ClearEditText mobile;
    @ViewById(R.id.tv_region)
    private TextView region;
    @ViewById(R.id.cet_address)
    private ClearEditText address;
    @ViewById(R.id.ll_default)
    private LinearLayout setDefault;
    @ViewById(R.id.iv_default)
    private ImageView defaultImg;
    @ViewById(R.id.btn_save)
    private Button save;

    // 国家地区列表
    private List<String> regionList;
    // 设置默认地址开关
    private boolean hasDefault;     // 默认不是默认地址
    //客户id
    private String mCustomId;
    private InvoicesBean mInvoice;

    private final int INITIAL_SIZE = 0x101;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_invoice_addres;
    }

    @Override
    protected void initView() {
        initEvents();
        mCustomId = getIntent().getStringExtra("customId");
        mInvoice = (InvoicesBean) getIntent().getSerializableExtra("invoice");
        if (mCustomId != null && mInvoice != null) {         // 编辑
            mPageTitle.setText("修改开票地址");
            Message msg = new Message();
            msg.what = INITIAL_SIZE;
            mHandler.sendMessage(msg);
           // RequestPost.getClientInvoice(mInvoice.getId(), this);
        } else {                // 新增
            mPageTitle.setText("新增开票地址");
        }
        showLoading("");
        // TODO: 获取国家地区列表
        RequestPost.getRegion(this);
    }

    private void initEvents() {
        mPageBack.setOnClickListener(this);
        region.setOnClickListener(this);
        setDefault.setOnClickListener(this);
        save.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_back:
                finish();
                break;
            case R.id.tv_region:                // 选择国家/地区
                new MenuDialog.Builder(this)
                        .setCancel(R.string.common_cancel)
                        .setAutoDismiss(false)
                        .setList(regionList)
                        .setCanceledOnTouchOutside(false)
                        .setListener(new MenuDialog.OnListener<String>() {
                            @Override
                            public void onSelected(BaseDialog dialog, int position, String text) {
                                // toast("位置：" + position + ", 文本：" + text);
                                dialog.dismiss();
                                region.setText(text);
                            }

                            @Override
                            public void onCancel(BaseDialog dialog) {
                                dialog.dismiss();
                            }
                        }).show();
                break;
            case R.id.ll_default:               // 设为默认地址
                hasDefault = !hasDefault;
                if (hasDefault) {
                    defaultImg.setImageResource(R.drawable.radio_checked_shape);
                } else {
                    defaultImg.setImageResource(R.drawable.radio_uncheck_shape);
                }
                break;
            case R.id.btn_save:
                if (TextUtils.isEmpty(company.getText())) {
                    toast("请填写公司名称");
                    return;
                }
                if (TextUtils.isEmpty(taxNum.getText())) {
                    toast("请填写税号");
                    return;
                }
                if (TextUtils.isEmpty(mobile.getText())) {
                    toast("请填写手机号");
                    return;
                }
                if (TextUtils.isEmpty(region.getText())) {
                    toast("请选择国家/地区");
                    return;
                }
                if (TextUtils.isEmpty(address.getText())) {
                    toast("请填写收货地址");
                    return;
                }
                // TODO: 新增/编辑开票地址
                showLoading("");
                RequestPost.clientInvoiceEdit(mInvoice == null ? "" : mInvoice.getId(), mCustomId, company.getText().toString().trim(),
                        taxNum.getText().toString().trim(), TextUtils.isEmpty(email.getText()) ? "" : email.getText().toString().trim(),
                        company.getText().toString().trim(), mobile.getText().toString().trim(), region.getText().toString().trim(),
                        address.getText().toString().trim(), hasDefault ? "1" : "0", this);
                break;
        }
    }

    @SuppressLint("HandlerLeak")
    private Handler mHandler = new Handler(){
        @Override
        public void handleMessage(@NonNull Message msg) {
            switch (msg.what){
                case INITIAL_SIZE:
                    company.setText(mInvoice.getCompanyName());
                    taxNum.setText(mInvoice.getParagraph());
                    email.setText(mInvoice.getEmail());
                    mobile.setText(mInvoice.getConsigneeTel());
                    region.setText(mInvoice.getRegion());
                    address.setText(mInvoice.getAddressMx());
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
                // 获取国家地区列表
                if (result.url().contains("wxapi/v1/client.php?type=getRegion")) {
                    JSONArray regionArray = JsonParser.parseJSONArrayString(JsonParser.parseJSONObject(body.get("data")).get("regionlist"));
                    regionList = new ArrayList<>();
                    if (regionArray != null) {
                        for (int i = 0; i < regionArray.length(); i++) {
                            try {
                                regionList.add(regionArray.getString(i));
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                }

                // 新增/编辑开票地址
                if (result.url().contains("wxapi/v1/client.php?type=clientInvoiceEdit")) {
                    toast(body.get("warn"));
                    postDelayed(this::finish, 1000);
                }

                // 发票地址详情
//                if (result.url().contains("wxapi/v1/client.php?type=getClientInvoice")){
//
//                }

            } else {
                toast(body.get("warn"));
            }
            if (getDialog() != null)
                dismissLoading();
        }
    }
}
