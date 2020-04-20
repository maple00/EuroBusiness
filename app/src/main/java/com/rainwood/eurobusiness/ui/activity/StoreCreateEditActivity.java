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
import com.rainwood.eurobusiness.domain.StoreDetailBean;
import com.rainwood.eurobusiness.domain.SupplierBean;
import com.rainwood.eurobusiness.json.JsonParser;
import com.rainwood.eurobusiness.okhttp.HttpResponse;
import com.rainwood.eurobusiness.okhttp.OnHttpListener;
import com.rainwood.eurobusiness.other.BaseDialog;
import com.rainwood.eurobusiness.request.RequestPost;
import com.rainwood.eurobusiness.ui.dialog.MenuDialog;
import com.rainwood.eurobusiness.utils.ListUtils;
import com.rainwood.tools.view.ClearEditText;
import com.rainwood.tools.view.PasswordEditText;
import com.rainwood.tools.viewinject.ViewById;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @Author: a797s
 * @Date: 2020/4/14 16:39
 * @Desc: 新建门店创建编辑
 */
public final class StoreCreateEditActivity extends BaseActivity implements View.OnClickListener, OnHttpListener {

    @ViewById(R.id.iv_back)
    private ImageView mPageBack;
    @ViewById(R.id.tv_title)
    private TextView mPageTitle;
    @ViewById(R.id.tv_right_text)
    private TextView rightText;
    @ViewById(R.id.cet_store_name)
    private ClearEditText storeName;
    @ViewById(R.id.cet_tel)
    private ClearEditText tel;
    @ViewById(R.id.cet_email)
    private ClearEditText email;
    @ViewById(R.id.tv_region)
    private TextView region;
    @ViewById(R.id.cet_address)
    private ClearEditText address;
    @ViewById(R.id.cet_tax_piva)
    private ClearEditText taxPIVA;
    @ViewById(R.id.cet_tax_cf)
    private ClearEditText taxCF;
    @ViewById(R.id.cet_contact)
    private ClearEditText contact;
    @ViewById(R.id.cet_store_desc)
    private ClearEditText storeDesc;
    @ViewById(R.id.cet_account)
    private ClearEditText account;
    @ViewById(R.id.cet_contact_tel)
    private ClearEditText contactTel;
    @ViewById(R.id.pet_pwd)
    private PasswordEditText pwd;
    @ViewById(R.id.tv_permission)
    private TextView permission;
    @ViewById(R.id.btn_confirm)
    private Button confirm;

    // 地区选择
    private List<String> regionList;
    // 门店id
    private String mStoreId;
    // 门店权限选择
    private List<SupplierBean> mStoreList;

    //
    private final int INITIAL_SIZE = 0x101;
    private StoreDetailBean mStoreDetail;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_create_store;
    }

    @Override
    protected void initView() {
        initEvents();
        mStoreId = getIntent().getStringExtra("storeId");
        showLoading("");
        if (mStoreId == null) {      // 新建门店信息
            mPageTitle.setText("新建门店");
            rightText.setVisibility(View.GONE);
            // TODO：新建门店页面数据
            RequestPost.newStorePage(this);
        } else {         // 更新/编辑门店信息
            mPageTitle.setText("门店详情");
            rightText.setVisibility(View.VISIBLE);
            rightText.setText("删除");
            rightText.setOnClickListener(this);
            RequestPost.getStoreDetail(mStoreId, this);
        }
    }

    private void initEvents() {
        mPageBack.setOnClickListener(this);
        region.setOnClickListener(this);
        permission.setOnClickListener(this);
        confirm.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_back:
                finish();
                break;
            case R.id.tv_right_text:            // 删除门店
                // TODO: 删除门店
                showLoading("");
                RequestPost.delStore(mStoreId, this);
                break;
            case R.id.tv_region:            // 地区选择
                setMenuDialogValue(regionList, "当前无选择方式", region);
                break;
            case R.id.tv_permission:        // 门店权限设置
                List<String> copyStoreList = new ArrayList<>();
                for (SupplierBean bean : mStoreList) {
                    copyStoreList.add(bean.getName());
                }
                setMenuDialogValue(copyStoreList, "当前无门店选择", permission);
                break;
            case R.id.btn_confirm:
                if (TextUtils.isEmpty(storeName.getText())) {
                    toast("请输入门店名称");
                    return;
                }

                if (TextUtils.isEmpty(tel.getText())) {
                    toast("请输入电话");
                    return;
                }

                if (TextUtils.isEmpty(email.getText())) {
                    toast("请输入邮箱");
                    return;
                }

                if (TextUtils.isEmpty(region.getText())) {
                    toast("请选择地区");
                    return;
                }

                if (TextUtils.isEmpty(address.getText())) {
                    toast("请填写详细地址");
                    return;
                }

                if (TextUtils.isEmpty(taxPIVA.getText())) {
                    toast("请输入税号(P.IVA)");
                    return;
                }

                if (TextUtils.isEmpty(taxCF.getText())) {
                    toast("请输入税号(C.f)");
                    return;
                }

                if (TextUtils.isEmpty(contact.getText())) {
                    toast("请输入联系人");
                    return;
                }

                if (TextUtils.isEmpty(storeDesc.getText())) {
                    toast("请输入门店描述");
                    return;
                }

                if (TextUtils.isEmpty(account.getText())) {
                    toast("请设置账号");
                    return;
                }

                if (TextUtils.isEmpty(pwd.getText())) {
                    toast("请设置密码");
                    return;
                }
                if (TextUtils.isEmpty(contactTel.getText())) {
                    toast("请设置电话");
                    return;
                }
                if (TextUtils.isEmpty(permission.getText())) {
                    toast("请选择权限");
                    return;
                }

                // TODO: 新增/编辑门店
                showLoading("");
                RequestPost.storerEdit(mStoreId, storeName.getText().toString().trim(), mStoreList.get(permissionPos).getId(),
                        account.getText().toString().trim(), contactTel.getText().toString().trim(),
                        pwd.getText().toString().trim(), email.getText().toString().trim(),
                        tel.getText().toString().trim(), region.getText().toString().trim(),
                        address.getText().toString().trim(), taxPIVA.getText().toString().trim(),
                        taxCF.getText().toString().trim(), contact.getText().toString().trim(), storeDesc.getText().toString().trim(), this);
                break;
        }
    }

    private int permissionPos = 0;

    /**
     * @param copyStoreList
     * @param tips
     * @param permission
     */
    private void setMenuDialogValue(List<String> copyStoreList, String tips, TextView permission) {
        if (ListUtils.getSize(copyStoreList) == 0) {
            toast(tips);
            return;
        }
        new MenuDialog.Builder(this)
                .setCancel(R.string.common_cancel)
                .setAutoDismiss(false)
                .setList(copyStoreList)
                .setCanceledOnTouchOutside(false)
                .setListener(new MenuDialog.OnListener<String>() {
                    @Override
                    public void onSelected(BaseDialog dialog, int position, String text) {
                        dialog.dismiss();
                        permissionPos = position;
                        permission.setText(text);
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
                    storeName.setText(mStoreDetail.getName());
                    tel.setText(mStoreDetail.getTel());
                    email.setText(mStoreDetail.getEmail());
                    region.setText(mStoreDetail.getRegion());
                    address.setText(mStoreDetail.getAddress());
                    taxPIVA.setText(mStoreDetail.getComapnyTaxNum());
                    taxCF.setText(mStoreDetail.getTaxNum());
                    contact.setText(mStoreDetail.getContactName());
                    contactTel.setText(mStoreDetail.getLoginTel());
                    storeDesc.setText(mStoreDetail.getText());
                    pwd.setText(mStoreDetail.getPassword());
                    permission.setText(mStoreDetail.getAdDutyName());
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
                // 新建门店页面数据
                if (result.url().contains("wxapi/v1/store.php?type=newStorePage")) {
                    // 门店id
                    mStoreId = JsonParser.parseJSONObject(JsonParser.parseJSONObject(body.get("data")).get("info")).get("id");
                    // 权限门店列表
                    mStoreList = JsonParser.parseJSONArray(SupplierBean.class, JsonParser.parseJSONObject(body.get("data")).get("adDutylist"));
                    // 国家/地区选择
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

                // 新增门店
                if (result.url().contains("wxapi/v1/store.php?type=storerEdit")) {
                    toast(body.get("warn"));
                    postDelayed(this::finish, 500);
                }

                // 删除门店
                if (result.url().contains("wxapi/v1/store.php?type=delStore")){
                    toast(body.get("warn"));
                    postDelayed(this::finish, 500);
                }

                // 编辑门店信息
                if (result.url().contains("wxapi/v1/store.php?type=getStoreInfo")) {
                    // 权限门店列表
                    mStoreList = JsonParser.parseJSONArray(SupplierBean.class, JsonParser.parseJSONObject(body.get("data")).get("adDutylist"));
                    // 国家/地区选择
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
                    // 门店信息
                    mStoreDetail = JsonParser.parseJSONObject(StoreDetailBean.class, JsonParser.parseJSONObject(body.get("data")).get("info"));

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
