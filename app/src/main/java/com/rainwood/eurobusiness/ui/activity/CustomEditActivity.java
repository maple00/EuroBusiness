package com.rainwood.eurobusiness.ui.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.rainwood.eurobusiness.R;
import com.rainwood.eurobusiness.base.BaseActivity;
import com.rainwood.eurobusiness.common.Contants;
import com.rainwood.eurobusiness.domain.AddressBean;
import com.rainwood.eurobusiness.domain.AddressDetailBean;
import com.rainwood.eurobusiness.domain.CustomDetailBean;
import com.rainwood.eurobusiness.domain.InvoiceDetailBean;
import com.rainwood.eurobusiness.domain.SupplierBean;
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

import static com.rainwood.eurobusiness.common.Contants.ADDRESS_REQUEST_SIZE;
import static com.rainwood.eurobusiness.common.Contants.INVOICE_ADDRESS_REQUEST;

/**
 * @Author: a797s
 * @Date: 2020/4/14 8:58
 * @Desc: 客户新增以及编辑
 */
public final class CustomEditActivity extends BaseActivity implements View.OnClickListener, OnHttpListener {

    @ViewById(R.id.iv_back)
    private ImageView mPageBack;
    @ViewById(R.id.tv_title)
    private TextView mPageTitle;
    @ViewById(R.id.cet_company)
    private ClearEditText companyName;
    @ViewById(R.id.cet_contact)
    private ClearEditText contact;
    @ViewById(R.id.cet_tel)
    private ClearEditText tel;
    @ViewById(R.id.cet_email)
    private ClearEditText email;
    @ViewById(R.id.tv_invoice)
    private TextView invoice;
    @ViewById(R.id.tv_ship_address)
    private TextView shipAddress;
    @ViewById(R.id.tv_custom_type)
    private TextView customType;
    @ViewById(R.id.tv_pay_method)
    private TextView payMethod;
    @ViewById(R.id.tv_pay_limit)
    private TextView payLimit;
    @ViewById(R.id.cet_day_limit)
    private ClearEditText dayLimit;
    @ViewById(R.id.cet_note)
    private ClearEditText note;
    @ViewById(R.id.btn_confirm)
    private Button confirm;

    // 客户id
    private String mCustomId;
    // 支付方式
    private List<String> payMethodList;
    // 支付期限
    private List<String> payLimitList;
    // 客户分类
    private List<SupplierBean> mCustomList;

    private final int INITIAL_SIZE = 0x101;
    private CustomDetailBean mCustomDetailBean;
    private InvoiceDetailBean mInvoiceDetailBean;
    private AddressDetailBean mAddressDetailBean;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_custom_edit;
    }

    @Override
    protected void initView() {
        initEvents();
        mCustomId = getIntent().getStringExtra("customId");
        showLoading("");
        if (mCustomId != null) {     // 客户详情
            mPageTitle.setText("客户详情");
            RequestPost.getClientDetail(mCustomId, this);
        } else {             // 新增客户
            mPageTitle.setText("新增客户");
            // TODO:  新增客户页面数据
            RequestPost.newClientPage(this);
        }
    }

    private void initEvents() {
        mPageBack.setOnClickListener(this);
        invoice.setOnClickListener(this);
        shipAddress.setOnClickListener(this);
        customType.setOnClickListener(this);
        payMethod.setOnClickListener(this);
        payLimit.setOnClickListener(this);
        confirm.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_back:
                finish();
                break;
            case R.id.tv_invoice:       // 发票地址
                Contants.CHOOSE_MODEL_SIZE = 14;
                Intent intent = new Intent(this, GoodsAddressActivity.class);
                intent.putExtra("customId", mCustomId);
                startActivityForResult(intent, INVOICE_ADDRESS_REQUEST);
                break;
            case R.id.tv_ship_address:      // 收货地址
                Contants.CHOOSE_MODEL_SIZE = 13;
                Intent intent1 = new Intent(this, GoodsAddressActivity.class);
                intent1.putExtra("customId", mCustomId);
                startActivityForResult(intent1, ADDRESS_REQUEST_SIZE);
                break;
            case R.id.tv_custom_type:       // 客户类型
                List<String> cusTypeList = new ArrayList<>();
                for (SupplierBean bean : mCustomList) {
                    cusTypeList.add(bean.getName());
                }
                setMenuDialogValue(cusTypeList, customType);
                break;
            case R.id.tv_pay_method:        // 支付方式
                setMenuDialogValue(payMethodList, payMethod);
                break;
            case R.id.tv_pay_limit:         // 支付期限
                setMenuDialogValue(payLimitList, payLimit);
                break;
            case R.id.btn_confirm:
                if (TextUtils.isEmpty(companyName.getText())) {
                    toast("请输入公司名称");
                    return;
                }
                if (TextUtils.isEmpty(contact.getText())) {
                    toast("请输入联系人");
                    return;
                }
                if (TextUtils.isEmpty(tel.getText())) {
                    toast("请输入联系电话");
                    return;
                }
                if (TextUtils.isEmpty(customType.getText())) {
                    toast("请选择客户类型");
                    return;
                }
                if (TextUtils.isEmpty(payMethod.getText())) {
                    toast("请选择支付类型");
                    return;
                }
                String cusId = "";
                for (SupplierBean bean : mCustomList) {
                    if (customType.getText().toString().trim().equals(bean.getName())) {
                        cusId = bean.getId();
                    }
                }
                // TODO：新增/编辑客户
                showLoading("");
                RequestPost.clientEdit(mCustomId, contact.getText().toString().trim(), tel.getText().toString().trim(), payMethod.getText().toString().trim(),
                        TextUtils.isEmpty(payLimit.getText()) ? (TextUtils.isEmpty(dayLimit.getText()) ? "" : "") : payLimit.getText().toString().trim(),
                        companyName.getText().toString().trim(), cusId, TextUtils.isEmpty(note.getText()) ? "" : note.getText().toString().trim(),
                        TextUtils.isEmpty(email.getText()) ? "" : email.getText().toString().trim(), this);
                break;
        }
    }

    /**
     * setValue
     *
     * @param optionList
     * @param textView
     */
    private void setMenuDialogValue(List<String> optionList, TextView textView) {
        new MenuDialog.Builder(this)
                .setCancel(R.string.common_cancel)
                .setAutoDismiss(false)
                .setList(optionList)
                .setCanceledOnTouchOutside(false)
                .setListener(new MenuDialog.OnListener<String>() {
                    @Override
                    public void onSelected(BaseDialog dialog, int position, String text) {
                        // toast("位置：" + position + ", 文本：" + text);
                        dialog.dismiss();
                        textView.setText(text);
                    }

                    @Override
                    public void onCancel(BaseDialog dialog) {
                        dialog.dismiss();
                    }
                }).show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // 发票地址
        if (requestCode == INVOICE_ADDRESS_REQUEST && resultCode == INVOICE_ADDRESS_REQUEST) {
            AddressBean address = (AddressBean) data.getSerializableExtra("address");
            invoice.setText(address.getAddress());
            invoice.setHint(address.getId());
        }
        // 收货地址
        if (requestCode == ADDRESS_REQUEST_SIZE && resultCode == ADDRESS_REQUEST_SIZE) {
            AddressBean address = (AddressBean) data.getSerializableExtra("address");
            shipAddress.setText(address.getAddress());
            shipAddress.setHint(address.getId());
        }
    }

    @SuppressLint("HandlerLeak")
    private Handler mHandler = new Handler(){
        @Override
        public void handleMessage(@NonNull Message msg) {
            switch (msg.what){
                case INITIAL_SIZE:
                    companyName.setText(mCustomDetailBean.getCompanyName());
                    contact.setText(mCustomDetailBean.getName());
                    tel.setText(mCustomDetailBean.getTel());
                    email.setText(mCustomDetailBean.getEmail());
                    invoice.setText(mInvoiceDetailBean.getAddressMx());
                    shipAddress.setText(mAddressDetailBean.getAddressMx());
                    customType.setText(mCustomDetailBean.getKehuTypeName());
                    payMethod.setText(mCustomDetailBean.getPayType());
                    payLimit.setText(mCustomDetailBean.getPayTerm());
                    note.setText(mCustomDetailBean.getText());
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
                // 新增客户页面数据
                if (result.url().contains("wxapi/v1/client.php?type=newClientPage")) {
                    mCustomId = JsonParser.parseJSONObject(JsonParser.parseJSONObject(body.get("data")).get("info")).get("id");
                    // 客户分类
                    mCustomList = JsonParser.parseJSONArray(SupplierBean.class, JsonParser.parseJSONObject(body.get("data")).get("kehuTypelist"));
                    // 支付方式
                    JSONArray payWayArray = JsonParser.parseJSONArrayString(JsonParser.parseJSONObject(body.get("data")).get("payWay"));
                    payMethodList = new ArrayList<>();
                    if (payWayArray != null) {
                        for (int i = 0; i < payWayArray.length(); i++) {
                            try {
                                payMethodList.add(payWayArray.getString(i));
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                    // 支付期限
                    JSONArray dayLimitArray = JsonParser.parseJSONArrayString(JsonParser.parseJSONObject(body.get("data")).get("payDayLimit"));
                    payLimitList = new ArrayList<>();
                    if (dayLimitArray != null) {
                        for (int i = 0; i < dayLimitArray.length(); i++) {
                            try {
                                payLimitList.add(dayLimitArray.getString(i));
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                }

                // 客户详情
                if (result.url().contains("wxapi/v1/client.php?type=getClientInfo")) {
                    // 客户分类
                    mCustomList = JsonParser.parseJSONArray(SupplierBean.class, JsonParser.parseJSONObject(body.get("data")).get("kehuTypelist"));
                    // 支付方式
                    JSONArray payWayArray = JsonParser.parseJSONArrayString(JsonParser.parseJSONObject(body.get("data")).get("payWay"));
                    payMethodList = new ArrayList<>();
                    if (payWayArray != null) {
                        for (int i = 0; i < payWayArray.length(); i++) {
                            try {
                                payMethodList.add(payWayArray.getString(i));
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                    // 支付期限
                    JSONArray dayLimitArray = JsonParser.parseJSONArrayString(JsonParser.parseJSONObject(body.get("data")).get("payDayLimit"));
                    payLimitList = new ArrayList<>();
                    if (dayLimitArray != null) {
                        for (int i = 0; i < dayLimitArray.length(); i++) {
                            try {
                                payLimitList.add(dayLimitArray.getString(i));
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                    // 客户信息
                    mCustomDetailBean = JsonParser.parseJSONObject(CustomDetailBean.class, JsonParser.parseJSONObject(body.get("data")).get("kehuInfo"));
                    mInvoiceDetailBean = JsonParser.parseJSONObject(InvoiceDetailBean.class, JsonParser.parseJSONObject(body.get("data")).get("kehuInvoice"));
                    mAddressDetailBean = JsonParser.parseJSONObject(AddressDetailBean.class, JsonParser.parseJSONObject(body.get("data")).get("kehuAddress"));

                    Message msg = new Message();
                    msg.what = INITIAL_SIZE;
                    mHandler.sendMessage(msg);
                }

                // 新增/编辑客户
                if (result.url().contains("wxapi/v1/client.php?type=clientEdit")) {
                    toast(body.get("warn"));
                    postDelayed(this::finish, 500);
                }
            } else {
                toast(body.get("warn"));
            }
            if (getDialog() != null)
                dismissLoading();
        }
    }
}
