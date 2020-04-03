package com.rainwood.eurobusiness.ui.activity;

import android.annotation.SuppressLint;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.rainwood.eurobusiness.R;
import com.rainwood.eurobusiness.base.BaseActivity;
import com.rainwood.eurobusiness.common.Contants;
import com.rainwood.eurobusiness.domain.CommonUIBean;
import com.rainwood.eurobusiness.domain.StoreDetailBean;
import com.rainwood.eurobusiness.domain.StorePermisionBean;
import com.rainwood.eurobusiness.domain.SuppierBean;
import com.rainwood.eurobusiness.json.JsonParser;
import com.rainwood.eurobusiness.okhttp.HttpResponse;
import com.rainwood.eurobusiness.okhttp.OnHttpListener;
import com.rainwood.eurobusiness.other.BaseDialog;
import com.rainwood.eurobusiness.request.RequestPost;
import com.rainwood.eurobusiness.ui.adapter.CommUIDirectAdapter;
import com.rainwood.eurobusiness.ui.dialog.MenuDialog;
import com.rainwood.eurobusiness.ui.dialog.MessageDialog;
import com.rainwood.tools.viewinject.ViewById;
import com.rainwood.tools.widget.MeasureListView;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @Author: shearson
 * @Time: 2020/2/24 22:36
 * @Desc: 新建供应商
 */
public class NewSupplierActivity extends BaseActivity implements View.OnClickListener, OnHttpListener {

    private List<StorePermisionBean> mStorePermissionList;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_new_spuulier;
    }

    @ViewById(R.id.iv_back)
    private ImageView pageBack;
    @ViewById(R.id.tv_title)
    private TextView pageTitle;
    @ViewById(R.id.tv_right_text)
    private TextView rightText;
    @ViewById(R.id.lv_content_list)
    private MeasureListView contentList;
    @ViewById(R.id.btn_confirm)
    private Button confirm;
    private List<CommonUIBean> mList;
    private String[] titles = {"供应商", "电话", "负责人", "支付方式", "银行账号", "邮箱", "所在地区", "详细地址"};
    private String[] labels = {"请输入", "请输入", "请输入", "请选择", "请输入", "请输入", "请选择", "请输入"};

    // 门店
    private String[] storeTitle = {"门店名称", "电话", "邮箱", "所在地区", "详细地址", "税号(P.IVA)",
            "税号(C.F)", "联系人", "门店描述", "设置账号", "设置电话", "设置密码", "权限"};
    private String[] storeLabel = {"请输入", "请输入", "请输入", "请选择", "请输入", "请输入", "请输入",
            "请输入", "请输入", "请输入", "请输入", "请输入", "请选择"};
    // 所选地区
    private List<String> mReginList;
    // 权限列表
    private List<String> mPermissionList;
    // mHandler
    private final int INITIAL_SIZE = 0x101;
    // 位置记录
    private static int positionFlag;
    // 供应商id
    private String supperId;

    @Override
    protected void initView() {
        pageBack.setOnClickListener(this);
        confirm.setOnClickListener(this);

    }

    /**
     * 获取所选地区或者支付方式
     *
     * @param valueList
     */
    private void getValue(List<String> valueList, int pos) {
        new MenuDialog.Builder(this)
                .setCancel(R.string.common_cancel)
                .setAutoDismiss(false)
                .setList(valueList)
                .setCanceledOnTouchOutside(false)
                .setAnimStyle(R.style.IOSAnimStyle)
                .setListener(new MenuDialog.OnListener<String>() {
                    @Override
                    public void onSelected(BaseDialog dialog, int position, String text) {
                        dialog.dismiss();
                        // 更改数据
                        mList.get(pos).setShowText(text);

                        Message msg = new Message();
                        msg.what = INITIAL_SIZE;
                        mHandler.sendMessage(msg);
                    }

                    @Override
                    public void onCancel(BaseDialog dialog) {
                        dialog.dismiss();
                    }
                }).show();
    }

    @Override
    protected void initData() {
        super.initData();
        mList = new ArrayList<>();
        if (Contants.CHOOSE_MODEL_SIZE == 102) {
            pageTitle.setText("新建供应商");
            setData(titles, labels);

            Message msg = new Message();
            msg.what = INITIAL_SIZE;
            mHandler.sendMessage(msg);
        }
        // 查看&编辑供应商
        if (Contants.CHOOSE_MODEL_SIZE == 0x1001) {
            pageTitle.setText("供应商详情");
            setData(titles, labels);
            rightText.setText("删除");
            String supplyId = getIntent().getStringExtra("supplyId");
            supperId = supplyId;
            // request
            showLoading("");
            RequestPost.getSupplierDetail(supplyId, this);

            // 删除供应商
            // 删除记录
            rightText.setOnClickListener(v -> new MessageDialog.Builder(getActivity())
                    .setMessage("确认删除？")
                    .setConfirm(getString(R.string.common_confirm))
                    .setCancel(getString(R.string.common_cancel))
                    .setAutoDismiss(false)
                    .setCanceledOnTouchOutside(false)
                    .setListener(new MessageDialog.OnListener() {
                        @Override
                        public void onConfirm(BaseDialog dialog) {
                            dialog.dismiss();
                            // request
                            showLoading("");
                            RequestPost.delSupplier(supperId, NewSupplierActivity.this);
                        }

                        @Override
                        public void onCancel(BaseDialog dialog) {
                            dialog.dismiss();
                        }
                    }).show());
        }

        if (Contants.CHOOSE_MODEL_SIZE == 103) {
            pageTitle.setText("新建门店");
            setData(storeTitle, storeLabel);
            // request
            showLoading("");
            RequestPost.newStorePage(this);
        }

        // 查看& 编辑门店信息
        if (Contants.CHOOSE_MODEL_SIZE == 0x1003) {
            pageTitle.setText("门店详情");
            setData(storeTitle, storeLabel);
            rightText.setText("删除");
            String storeId = getIntent().getStringExtra("storeId");
            supperId = storeId;
            // request
            showLoading("");
            RequestPost.getStoreDetail(storeId, this);

            // 删除门店
            rightText.setOnClickListener(v -> new MessageDialog.Builder(getActivity())
                    .setMessage("确认删除？")
                    .setConfirm(getString(R.string.common_confirm))
                    .setCancel(getString(R.string.common_cancel))
                    .setAutoDismiss(false)
                    .setCanceledOnTouchOutside(false)
                    .setListener(new MessageDialog.OnListener() {
                        @Override
                        public void onConfirm(BaseDialog dialog) {
                            dialog.dismiss();
                            // request
                            showLoading("");
                            RequestPost.delStore(supperId, NewSupplierActivity.this);
                        }

                        @Override
                        public void onCancel(BaseDialog dialog) {
                            dialog.dismiss();
                        }
                    }).show());
        }
    }

    /**
     * initial
     *
     * @param storeTitle title
     * @param storeLabel label
     */
    private void setData(String[] storeTitle, String[] storeLabel) {
        for (int i = 0; i < storeTitle.length; i++) {
            CommonUIBean commonUI = new CommonUIBean();
            commonUI.setTitle(storeTitle[i]);
            if (storeTitle[i].equals("供应商")) {
                commonUI.setFillIn(true);
            }
            if (storeTitle[i].equals("门店名称")) {
                commonUI.setFillIn(true);
            }
            if (storeLabel[i].equals("请选择")) {
                commonUI.setArrowType(1);
            } else {
                commonUI.setArrowType(0);
            }
            commonUI.setLabel(storeLabel[i]);
            mList.add(commonUI);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_back:
                finish();
                break;
            case R.id.btn_confirm:
                // 新增或者编辑供应商
                if (Contants.CHOOSE_MODEL_SIZE == 102 || Contants.CHOOSE_MODEL_SIZE == 0x1001) {
                    String name = "";
                    String tel = "";
                    String chargeName = "";
                    String bankNum = "";
                    String email = "";
                    String area = "";
                    String address = "";
                    String payType = "";
                    for (int i = 0; i < mList.size(); i++) {
                        if ("供应商".equals(mList.get(i).getTitle()) && TextUtils.isEmpty(mList.get(i).getShowText())) {
                            toast("请输入供应商名称");
                            return;
                        }
                        switch (mList.get(i).getTitle()) {
                            case "供应商":
                                name = mList.get(i).getShowText();
                                break;
                            case "电话":
                                tel = mList.get(i).getShowText();
                                break;
                            case "负责人":
                                chargeName = mList.get(i).getShowText();
                                break;
                            case "银行账号":
                                bankNum = mList.get(i).getShowText();
                                break;
                            case "邮箱":
                                email = mList.get(i).getShowText();
                                break;
                            case "所在地区":
                                area = mList.get(i).getShowText();
                                break;
                            case "详细地址":
                                address = mList.get(i).getShowText();
                                break;
                            case "支付方式":
                                payType = mList.get(i).getShowText();
                                break;
                        }
                    }
                    // request
                    showLoading("");
                    RequestPost.supplierEdit(supperId, name, tel, chargeName, bankNum, email, area, address, payType, this);
                }

                // 新增或编辑门店
                if (Contants.CHOOSE_MODEL_SIZE == 103 || Contants.CHOOSE_MODEL_SIZE == 0x1003) {
                    String storeName = "";
                    String tel = "";
                    String email = "";
                    String regoin = "";
                    String address = "";
                    String taxPiva = "";
                    String taxCf = "";
                    String contact = "";
                    String storeDesc = "";
                    String account = "";
                    String accountTel = "";
                    String accountPwd = "";
                    String permission = "";

                    for (int i = 0; i < mList.size(); i++) {
                        switch (mList.get(i).getTitle()) {
                            case "门店名称":
                                storeName = mList.get(i).getShowText();
                                break;
                            case "电话":
                                tel = mList.get(i).getShowText();
                                break;
                            case "邮箱":
                                email = mList.get(i).getShowText();
                                break;
                            case "所在地区":
                                regoin = mList.get(i).getShowText();
                                break;
                            case "详细地址":
                                address = mList.get(i).getShowText();
                                break;
                            case "税号(P.IVA)":
                                taxPiva = mList.get(i).getShowText();
                                break;
                            case "税号(C.F)":
                                taxCf = mList.get(i).getShowText();
                                break;
                            case "联系人":
                                contact = mList.get(i).getShowText();
                                break;
                            case "门店描述":
                                storeDesc = mList.get(i).getShowText();
                                break;
                            case "设置账号":
                                account = mList.get(i).getShowText();
                                break;
                            case "设置电话":
                                accountTel = mList.get(i).getShowText();
                                break;
                            case "设置密码":
                                accountPwd = mList.get(i).getShowText();
                                break;
                            case "权限":
                                permission = mList.get(i).getShowText();
                                break;
                        }
                    }
                    // 获取权限id
                    for (StorePermisionBean permisionBean : mStorePermissionList) {
                        if (permission.equals(permisionBean.getName())) {
                            permission = permisionBean.getId();
                            break;
                        }
                    }

                    // request
                    showLoading("");
                    RequestPost.storerEdit(supperId, storeName, permission, account, accountTel, accountPwd, email, tel, regoin,
                            address, taxPiva, taxCf, contact, storeDesc, this);
                }
                break;
        }
    }

    @SuppressLint("HandlerLeak")
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(@NonNull Message msg) {
            switch (msg.what) {
                case INITIAL_SIZE:
                    if (Contants.CHOOSE_MODEL_SIZE == 102 || Contants.CHOOSE_MODEL_SIZE == 0x1001) {
                        CommUIDirectAdapter directAdapter = new CommUIDirectAdapter(NewSupplierActivity.this, mList);
                        contentList.setAdapter(directAdapter);
                        directAdapter.setOnClickEditText(new CommUIDirectAdapter.OnClickEditText() {
                            @Override
                            public void onClickText(int position) {
                                switch (mList.get(position).getTitle()) {
                                    case "支付方式":
                                    case "所在地区":
                                        // request
                                        showLoading("");
                                        RequestPost.newSupplierPage(NewSupplierActivity.this);
                                        positionFlag = position;
                                        break;
                                }
                            }

                            @Override
                            public void onTextWatcher(CommUIDirectAdapter adapter, int position, String result) {
                                mList.get(position).setShowText(result);
                            }
                        });
                    }

                    // 新建&编辑门店信息
                    if (Contants.CHOOSE_MODEL_SIZE == 103 || Contants.CHOOSE_MODEL_SIZE == 0x1003) {
                        CommUIDirectAdapter directAdapter = new CommUIDirectAdapter(NewSupplierActivity.this, mList);
                        contentList.setAdapter(directAdapter);
                        directAdapter.setOnClickEditText(new CommUIDirectAdapter.OnClickEditText() {
                            @Override
                            public void onClickText(int position) {
                                switch (mList.get(position).getTitle()) {
                                    case "所在地区":
                                        getValue(mReginList, position);
                                        break;
                                    case "权限":
                                        getValue(mPermissionList, position);
                                        break;
                                }
                            }

                            @Override
                            public void onTextWatcher(CommUIDirectAdapter adapter, int position, String result) {
                                Log.d(TAG, "===== 新建门店== textChange ======= " + position);
                            }
                        });
                    }
                    break;
            }
        }
    };

    @Override
    public void onHttpFailure(HttpResponse result) {

    }

    @Override
    public void onHttpSucceed(HttpResponse result) {
        Log.d(TAG, " --- result ---- " + result);
        Map<String, String> body = JsonParser.parseJSONObject(result.body());
        if (body != null) {
            if ("1".equals(body.get("code"))) {
                // 获取新建供应商的支付方式和所在地区
                if (result.url().contains("wxapi/v1/supplier.php?type=newSupplierPage")) {
                    // 支付方式
                    JSONArray payMethods = JsonParser.parseJSONArrayString(JsonParser.parseJSONObject(body.get("data")).get("payWay"));
                    // 支付方式
                    List<String> payMethodList = new ArrayList<>();
                    for (int i = 0; i < payMethods.length(); i++) {
                        try {
                            payMethodList.add(payMethods.getString(i));
                        } catch (JSONException e) {
                            toast("异常数据");
                        }
                    }
                    // 国家/地区
                    JSONArray regions = JsonParser.parseJSONArrayString(JsonParser.parseJSONObject(body.get("data")).get("regionlist"));
                    mReginList = new ArrayList<>();
                    for (int i = 0; i < regions.length(); i++) {
                        try {
                            mReginList.add(regions.getString(i));
                        } catch (JSONException e) {
                            toast("数据异常");
                        }
                    }
                    // 供应商id
                    supperId = JsonParser.parseJSONObject(JsonParser.parseJSONObject(body.get("data")).get("info")).get("id");
                    if ("支付方式".equals(mList.get(positionFlag).getTitle())) {
                        getValue(payMethodList, positionFlag);
                    }

                    if ("所在地区".equals(mList.get(positionFlag).getTitle())) {
                        getValue(mReginList, positionFlag);
                    }

                }

                // 供应商详情
                if (result.url().contains("wxapi/v1/supplier.php?type=getSupplierInfo")) {
                    SuppierBean suppier = JsonParser.parseJSONObject(SuppierBean.class, JsonParser.parseJSONObject(body.get("data")).get("info"));
                    if (suppier != null)
                        for (int i = 0; i < mList.size(); i++) {
                            switch (i) {
                                case 0:
                                    mList.get(i).setShowText(suppier.getName());
                                    break;
                                case 1:
                                    mList.get(i).setShowText(suppier.getTel());
                                    break;
                                case 2:
                                    mList.get(i).setShowText(suppier.getChargeName());
                                    break;
                                case 3:
                                    mList.get(i).setShowText(suppier.getPayType());
                                    break;
                                case 4:
                                    mList.get(i).setShowText(suppier.getBankNum());
                                    break;
                                case 5:
                                    mList.get(i).setShowText(suppier.getEmail());
                                    break;
                                case 6:
                                    mList.get(i).setShowText(suppier.getRegionId());
                                    break;
                                case 7:
                                    mList.get(i).setShowText(suppier.getAddress());
                                    break;
                            }
                        }

                    Message msg = new Message();
                    msg.what = INITIAL_SIZE;
                    mHandler.sendMessage(msg);
                }

                // 新增或编辑供应商数据
                if (result.url().contains("wxapi/v1/supplier.php?type=supplierEdit")) {
                    toast(body.get("warn"));
                    Contants.CHOOSE_MODEL_SIZE = 102;
                    postDelayed(this::finish, 500);
                }

                // 删除供应商
                if (result.url().contains("wxapi/v1/supplier.php?type=delSupplier")) {
                    toast(body.get("warn"));
                    Contants.CHOOSE_MODEL_SIZE = 102;
                    postDelayed(this::finish, 500);
                }

                // 门店详情
                if (result.url().contains("wxapi/v1/store.php?type=getStoreInfo")) {
                    // Log.d(TAG, "==== 门店详情 ===== " + body.get("data"));
                    StoreDetailBean storeDetail = JsonParser.parseJSONObject(StoreDetailBean.class,
                            JsonParser.parseJSONObject(body.get("data")).get("info"));
                    if (storeDetail != null)
                        for (int i = 0; i < mList.size(); i++) {
                            switch (mList.get(i).getTitle()) {
                                case "门店名称":
                                    mList.get(i).setShowText(storeDetail.getName());
                                    break;
                                case "电话":
                                    mList.get(i).setShowText(storeDetail.getTel());
                                    break;
                                case "邮箱":
                                    mList.get(i).setShowText(storeDetail.getEmail());
                                    break;
                                case "所在地区":
                                    mList.get(i).setShowText(storeDetail.getRegion());
                                    break;
                                case "详细地址":
                                    mList.get(i).setShowText(storeDetail.getAddress());
                                    break;
                                case "税号(P.IVA)":
                                    mList.get(i).setShowText(storeDetail.getComapnyTaxNum());
                                    break;
                                case "税号(C.F)":
                                    mList.get(i).setShowText(storeDetail.getTaxNum());
                                    break;
                                case "联系人":
                                    mList.get(i).setShowText(storeDetail.getContactName());
                                    break;
                                case "门店描述":
                                    mList.get(i).setShowText(storeDetail.getText());
                                    break;
                                case "设置账号":
                                    mList.get(i).setShowText(storeDetail.getAdLoginName());
                                    break;
                                case "设置电话":
                                    mList.get(i).setShowText(storeDetail.getLoginTel());
                                    break;
                                case "权限":
                                    mList.get(i).setShowText(storeDetail.getAdDutyName());
                                    break;
                            }
                        }

                    // 地区列表
                    JSONArray regoinList = JsonParser.parseJSONArrayString(JsonParser.parseJSONObject(body.get("data")).get("regionlist"));
                    mReginList = new ArrayList<>();
                    for (int i = 0; i < regoinList.length(); i++) {
                        try {
                            mReginList.add(regoinList.getString(i));
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    // 权限列表
                    mStorePermissionList = JsonParser.parseJSONArray(StorePermisionBean.class,
                            JsonParser.parseJSONObject(body.get("data")).get("adDutylist"));
                    mPermissionList = new ArrayList<>();
                    if (mStorePermissionList != null) {
                        for (StorePermisionBean permisionBean : mStorePermissionList) {
                            mPermissionList.add(permisionBean.getName());
                        }
                    }

                    Message msg = new Message();
                    msg.what = INITIAL_SIZE;
                    mHandler.sendMessage(msg);
                }

                // 删除门店
                if (result.url().contains("wxapi/v1/store.php?type=delStore")) {
                    Contants.CHOOSE_MODEL_SIZE = 103;
                    postDelayed(this::finish, 500);
                }

                // 新增或者更新门店信息
                if (result.url().contains("wxapi/v1/store.php?type=storerEdit")) {
                    Contants.CHOOSE_MODEL_SIZE = 103;
                    postDelayed(this::finish, 500);
                }

                // 获取门店中的地区列表和权限列表
                if (result.url().contains("wxapi/v1/store.php?type=newStorePage")) {
                    // 地区列表
                    JSONArray regoinList = JsonParser.parseJSONArrayString(JsonParser.parseJSONObject(body.get("data")).get("regionlist"));
                    mReginList = new ArrayList<>();
                    for (int i = 0; i < regoinList.length(); i++) {
                        try {
                            mReginList.add(regoinList.getString(i));
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                    // 权限列表
                    mStorePermissionList = JsonParser.parseJSONArray(StorePermisionBean.class, JsonParser.parseJSONObject(body.get("data")).get("adDutylist"));
                    mPermissionList = new ArrayList<>();
                    if (mStorePermissionList != null) {
                        for (StorePermisionBean permisionBean : mStorePermissionList) {
                            mPermissionList.add(permisionBean.getName());
                        }
                    }

                    // 新建id
                    supperId = JsonParser.parseJSONObject(JsonParser.parseJSONObject(body.get("data")).get("info")).get("id");

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
}
