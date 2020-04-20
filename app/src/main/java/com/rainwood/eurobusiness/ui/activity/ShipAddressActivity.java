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
import com.rainwood.eurobusiness.domain.AddressDetailBean;
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
 * @Date: 2020/4/15 11:18
 * @Desc: 新增/编辑地址
 */
public final class ShipAddressActivity extends BaseActivity implements View.OnClickListener, OnHttpListener {

    @ViewById(R.id.iv_back)
    private ImageView mPageBack;
    @ViewById(R.id.tv_title)
    private TextView mPageTitle;
    @ViewById(R.id.cet_company)
    private ClearEditText company;
    @ViewById(R.id.cet_ship_man)
    private ClearEditText shipMan;
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
    private ClientAddressBean mInvoice;

    private final int INITIAL_SIZE = 0x101;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_ship_address;
    }

    @Override
    protected void initView() {
        initEvents();
        mCustomId = getIntent().getStringExtra("customId");
        mInvoice = (ClientAddressBean) getIntent().getSerializableExtra("invoice");
        if (mCustomId != null && mInvoice != null) {         // 编辑
            mPageTitle.setText("修改收货地址");
            Message msg = new Message();
            msg.what = INITIAL_SIZE;
            mHandler.sendMessage(msg);
        } else {                                        // 新增
            mPageTitle.setText("新增收货地址");
        }
        // TODO: 获取国家地区列表
        showLoading("");
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
            case R.id.ll_default:               // 设为默认地址
                hasDefault = !hasDefault;
                if (hasDefault) {
                    defaultImg.setImageResource(R.drawable.radio_checked_shape);
                } else {
                    defaultImg.setImageResource(R.drawable.radio_uncheck_shape);
                }
                break;
            case R.id.tv_region:            // 选择国家/地区
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
            case R.id.btn_save:
                if (TextUtils.isEmpty(company.getText())) {
                    toast("请输入公司名称");
                    return;
                }
                if (TextUtils.isEmpty(shipMan.getText())) {
                    toast("请输入收件人");
                    return;
                }
                if (TextUtils.isEmpty(mobile.getText())) {
                    toast("请输入手机号");
                    return;
                }
                if (TextUtils.isEmpty(region.getText())) {
                    toast("请选择国家/地区");
                    return;
                }
                if (TextUtils.isEmpty(address.getText())) {
                    toast("请输入详细地址");
                    return;
                }
                // TODO: 新增/编辑收货地址
                showLoading("");
                RequestPost.clientAddressEdit(mInvoice == null ? "" : mInvoice.getId(), mCustomId, company.getText().toString().trim(),
                        shipMan.getText().toString().trim(), mobile.getText().toString().trim(),
                        region.getText().toString().trim(), address.getText().toString().trim(),
                        hasDefault ? "1" : "0", this);
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
                    shipMan.setText(mInvoice.getContactName());
                    mobile.setText(mInvoice.getContactTel());
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

                // 新增/编辑收货地址
                if (result.url().contains("wxapi/v1/client.php?type=clientAddressEdit")) {
                    toast(body.get("warn"));
                    postDelayed(this::finish, 1000);
                }
            } else {
                toast(body.get("warn"));
            }
            if (getDialog() != null)
                dismissLoading();
        }
    }
}
