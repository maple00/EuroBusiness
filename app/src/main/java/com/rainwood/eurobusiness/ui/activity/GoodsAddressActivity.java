package com.rainwood.eurobusiness.ui.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.rainwood.eurobusiness.R;
import com.rainwood.eurobusiness.base.BaseActivity;
import com.rainwood.eurobusiness.common.Contants;
import com.rainwood.eurobusiness.domain.AddressBean;
import com.rainwood.eurobusiness.domain.InvoicesBean;
import com.rainwood.eurobusiness.json.JsonParser;
import com.rainwood.eurobusiness.okhttp.HttpResponse;
import com.rainwood.eurobusiness.okhttp.OnHttpListener;
import com.rainwood.eurobusiness.request.RequestPost;
import com.rainwood.eurobusiness.ui.adapter.GoodsAddressAdapter;
import com.rainwood.eurobusiness.utils.ListUtils;
import com.rainwood.tools.viewinject.ViewById;
import com.rainwood.tools.widget.MeasureListView;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @Author: shearson
 * @Time: 2020/2/24 15:49
 * @Desc: 地址
 */
public class GoodsAddressActivity extends BaseActivity implements View.OnClickListener, OnHttpListener {

    /**
     * 开票地址
     */
    private List<InvoicesBean> mInvoiceList;

    /**
     * 收货地址
     */
    private List<ClientAddressBean> mClientAddressList;
    private String mCustomId;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_goods_address;
    }

    @ViewById(R.id.iv_back)
    private ImageView pageBack;
    @ViewById(R.id.tv_title)
    private TextView pageTitle;
    @ViewById(R.id.lv_content_list)
    private MeasureListView contentList;
    @ViewById(R.id.btn_new_address)
    private Button newAddress;

    private List<AddressBean> mList;

    private final int INITIAL_SIZE = 0x101;
    // 记录点击默认地址的位置
    private int defaultPos = -1;

    @Override
    protected void initView() {
        pageBack.setOnClickListener(this);

        mCustomId = getIntent().getStringExtra("customId");
        if (Contants.CHOOSE_MODEL_SIZE == 13) {          // 收货地址
            setDefaultUI("收货地址", "新增收货地址");
            // request
            showLoading("loading");
            RequestPost.getClientAddresslist(mCustomId, this);
        }

        if (Contants.CHOOSE_MODEL_SIZE == 14) {              // 开票地址
            setDefaultUI("开票地址", "新增开票地址");
            // request
            showLoading("loading");
            RequestPost.getClientInvoicelist(mCustomId, this);
        }
    }

    private void setDefaultUI(String title, String bottomText) {
        newAddress.setOnClickListener(this);
        pageTitle.setText(title);
        newAddress.setText(bottomText);
    }

    @Override
    protected void initData() {
        super.initData();
        mList = new ArrayList<>();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_back:
                finish();
                break;
            case R.id.btn_new_address:
                if (Contants.CHOOSE_MODEL_SIZE == 13) {              // 新增收货地址
                    openActivity(NewGoodsAddressActivity.class);
                }
                if (Contants.CHOOSE_MODEL_SIZE == 14) {              // 新增开票地址
                    openActivity(NewGoodsAddressActivity.class);
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
                case INITIAL_SIZE:
                    GoodsAddressAdapter addressAdapter = new GoodsAddressAdapter(GoodsAddressActivity.this, mList);
                    contentList.setAdapter(addressAdapter);
                    addressAdapter.setOnClickItem(new GoodsAddressAdapter.OnClickItem() {
                        @Override
                        public void onClickDefault(int position) {
                            defaultPos = position;
                            if (mList.get(position).isChecked()) {
                                toast("已经是默认地址,不用更改");
                                return;
                            }
                            for (AddressBean addressBean : mList) {
                                addressBean.setChecked(false);
                            }
                            mList.get(defaultPos).setChecked(true);
                            // 设置为默认地址 request
                            String addressType = "";
                            if (Contants.CHOOSE_MODEL_SIZE == 14) {
                                addressType = "invoice";
                            }
                            if (Contants.CHOOSE_MODEL_SIZE == 13) {
                                addressType = "address";
                            }
                            showLoading("loading");
                            RequestPost.setDefaultAddress(addressType, mList.get(position).getId(), GoodsAddressActivity.this);
                        }

                        @Override
                        public void onClickEdit(int position) {
                            // 开票地址
                            if (Contants.CHOOSE_MODEL_SIZE == 14) {
                                Contants.CHOOSE_MODEL_SIZE = 18;
                                Intent intent = new Intent(GoodsAddressActivity.this, NewGoodsAddressActivity.class);
                                intent.putExtra("invoice", mInvoiceList.get(position));
                                startActivity(intent);
                            }
                            // 收货地址
                            if (Contants.CHOOSE_MODEL_SIZE == 13) {
                                Contants.CHOOSE_MODEL_SIZE = 19;
                                Intent intent = new Intent(GoodsAddressActivity.this, NewGoodsAddressActivity.class);
                                intent.putExtra("invoice", mClientAddressList.get(position));
                                startActivity(intent);
                            }
                        }

                        @Override
                        public void onClickDelete(int position) {
                            // request
                            Log.d(TAG, " --- id -" + mList.get(position).getId());
                            if (Contants.CHOOSE_MODEL_SIZE == 14) {     // 删除发票地址
                                showLoading("loading");
                                RequestPost.clearInvoice(mList.get(position).getId(), GoodsAddressActivity.this);
                            }
                            if (Contants.CHOOSE_MODEL_SIZE == 13) {          // 删除收货地址
                                showLoading("loading");
                                RequestPost.clearClientAddress(mList.get(position).getId(), GoodsAddressActivity.this);
                            }
                        }

                        @Override
                        public void onClickItem(int position) {
                            // 开票地址
                            if (Contants.CHOOSE_MODEL_SIZE == 14) {
                                Contants.CHOOSE_MODEL_SIZE = 18;
                                Intent intent = new Intent(GoodsAddressActivity.this, NewGoodsAddressActivity.class);
                                intent.putExtra("invoice", mInvoiceList.get(position));
                                startActivity(intent);
                            }
                            // 收货地址
                            if (Contants.CHOOSE_MODEL_SIZE == 13) {
                                Contants.CHOOSE_MODEL_SIZE = 19;
                                Intent intent = new Intent(GoodsAddressActivity.this, NewGoodsAddressActivity.class);
                                intent.putExtra("invoice", mClientAddressList.get(position));
                                startActivity(intent);
                            }
                        }
                    });
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
                // 获取发票列表
                if (result.url().contains("wxapi/v1/client.php?type=getClientInvoicelist")) {
                    mInvoiceList = JsonParser.parseJSONArray(InvoicesBean.class, JsonParser.parseJSONObject(body.get("data")).get("list"));
                    for (int i = 0; i < ListUtils.getSize(mInvoiceList); i++) {
                        AddressBean address = new AddressBean();
                        address.setId(mInvoiceList.get(i).getId());
                        address.setAddress(mInvoiceList.get(i).getAddressMx());
                        address.setName(mInvoiceList.get(i).getName());
                        address.setChecked("1".equals(mInvoiceList.get(i).getIsDefault()));
                        mList.add(address);
                    }
                    Message msg = new Message();
                    msg.what = INITIAL_SIZE;
                    mHandler.sendMessage(msg);
                }
                // 收货地址列表
                if (result.url().contains("wxapi/v1/client.php?type=getClientAddresslist")) {
                    Log.d(TAG, " body ======= " + JsonParser.parseJSONObject(body.get("data")));
                    mClientAddressList = JsonParser.parseJSONArray(ClientAddressBean.class, JsonParser.parseJSONObject(body.get("data")).get("list"));
                    Log.d(TAG, " ============= " + mClientAddressList.toString());
                    for (int i = 0; i < ListUtils.getSize(mClientAddressList); i++) {
                        AddressBean address = new AddressBean();
                        address.setId(mClientAddressList.get(i).getId());
                        address.setAddress(mClientAddressList.get(i).getAddressMx());
                        address.setName(mClientAddressList.get(i).getCompanyName());
                        address.setChecked("1".equals(mClientAddressList.get(i).getIsDefault()));
                        mList.add(address);
                    }
                    Message msg = new Message();
                    msg.what = INITIAL_SIZE;
                    mHandler.sendMessage(msg);
                }

                // 删除发票地址 wxapi/v1/client.php?type=delClientInvoice
                if (result.url().contains("wxapi/v1/client.php?type=delClientInvoice")) {
                    toast(body.get("warn"));
                    showLoading("loading");
                    if (Contants.CHOOSE_MODEL_SIZE == 14) {
                        RequestPost.getClientInvoicelist(mCustomId, this);
                    }
                    if (Contants.CHOOSE_MODEL_SIZE == 13) {
                        RequestPost.getClientAddresslist(mCustomId, this);
                    }
                }

                // 设置默认地址
                if (result.url().contains("wxapi/v1/client.php?type=setDefaultAddress")) {
                    // toast(body.get("warn"));
                }
            } else {
                toast(body.get("warn"));
            }
            dismissLoading();
        }
    }
}
