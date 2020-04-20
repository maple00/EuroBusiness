package com.rainwood.eurobusiness.ui.activity;

import android.annotation.SuppressLint;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.rainwood.eurobusiness.R;
import com.rainwood.eurobusiness.base.BaseActivity;
import com.rainwood.eurobusiness.common.Contants;
import com.rainwood.eurobusiness.domain.CommonUIBean;
import com.rainwood.eurobusiness.domain.CustomTypeBean;
import com.rainwood.eurobusiness.domain.InvoicesBean;
import com.rainwood.eurobusiness.json.JsonParser;
import com.rainwood.eurobusiness.okhttp.HttpResponse;
import com.rainwood.eurobusiness.okhttp.OnHttpListener;
import com.rainwood.eurobusiness.request.RequestPost;
import com.rainwood.eurobusiness.ui.adapter.NewGoodsAddressAdapter;
import com.rainwood.eurobusiness.utils.ListUtils;
import com.rainwood.tools.viewinject.ViewById;
import com.rainwood.tools.widget.MeasureListView;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @Author: shearson
 * @Time: 2020/2/24 17:19
 * @Desc: 新增/编辑收货地址
 */
public class NewGoodsAddressActivity extends BaseActivity implements View.OnClickListener, OnHttpListener {
    @Override
    protected int getLayoutId() {
        return R.layout.activity_new_address;
    }

    @ViewById(R.id.iv_back)
    private ImageView pageBack;
    @ViewById(R.id.tv_title)
    private TextView pageTitle;
    @ViewById(R.id.tv_right_text)
    private TextView rightText;
    @ViewById(R.id.lv_content_list)
    private MeasureListView contentList;
    @ViewById(R.id.btn_save)
    private Button save;
    // 设置默认地址
    @ViewById(R.id.ll_default)
    private LinearLayout defaultAddress;
    @ViewById(R.id.iv_checked)
    private ImageView checked;
    @ViewById(R.id.tv_default)
    private TextView defaultText;
    private static int DEFAULT_ADDRESS_SIZE = 1;     // 设置为默认地址标志

    private final int INITIAL_SIZE = 0x101;
    private List<CommonUIBean> mList;
    // 收货地址
    private String[] titles = {"公司名称", "收货人", "手机号", "所在地区", "详细地址"};
    private String[] labels = {"请输入", "请输入", "请输入收货人手机号", "请选择", "请输入"};
    // 开票地址
    private String[] taxTitles = {"公司名称", "公司税号（P.IVA）", "PEC邮箱/SDI", "手机号", "所在地区", "详细地址"};
    private String[] taxLabels = {"请输入", "请输入", "请输入", "请输入", "请选择", "请输入"};

    @Override
    protected void initView() {
        pageBack.setOnClickListener(this);
        save.setOnClickListener(this);
        defaultAddress.setOnClickListener(this);
        defaultText.setText("设置为默认地址");
        if (Contants.CHOOSE_MODEL_SIZE == 13) {
            pageTitle.setText("新增收货地址");
            defaultAddress.setVisibility(View.VISIBLE);
            save.setText(R.string.common_save);

            Message msg = new Message();
            msg.what = INITIAL_SIZE;
            mHandler.sendMessage(msg);
        }
        if (Contants.CHOOSE_MODEL_SIZE == 14) {
            pageTitle.setText("新增开票地址");
            defaultAddress.setVisibility(View.VISIBLE);
            save.setText(R.string.common_save);

            Message msg = new Message();
            msg.what = INITIAL_SIZE;
            mHandler.sendMessage(msg);
        }
        if (Contants.CHOOSE_MODEL_SIZE == 18) {
            pageTitle.setText("修改开票地址");
            defaultAddress.setVisibility(View.GONE);
            save.setText(R.string.common_save);
            //
            InvoicesBean invoice = (InvoicesBean) getIntent().getSerializableExtra("invoice");
            if (invoice != null)
                for (int i = 0; i < mList.size(); i++) {
                    switch (i) {
                        case 0:
                            mList.get(i).setShowText(invoice.getName());
                            break;
                        case 1:
                            mList.get(i).setShowText(invoice.getParagraph());
                            break;
                        case 2:
                            mList.get(i).setShowText(invoice.getEmail());
                            break;
                        case 3:
                            mList.get(i).setShowText(invoice.getConsigneeTel());
                            break;
                        case 4:
                            mList.get(i).setShowText(invoice.getRegion());
                            break;
                        case 5:
                            mList.get(i).setShowText(invoice.getAddressMx());
                            break;
                    }
                }
            Message msg = new Message();
            msg.what = INITIAL_SIZE;
            mHandler.sendMessage(msg);
        }
        if (Contants.CHOOSE_MODEL_SIZE == 19) {
            pageTitle.setText("修改收货地址");
            defaultAddress.setVisibility(View.GONE);
            save.setText(R.string.common_save);

            Message msg = new Message();
            msg.what = INITIAL_SIZE;
            mHandler.sendMessage(msg);
        }
    }

    @Override
    protected void initData() {
        super.initData();
        mList = new ArrayList<>();
        if (Contants.CHOOSE_MODEL_SIZE == 13 || Contants.CHOOSE_MODEL_SIZE == 19) {
            for (int i = 0; i < titles.length; i++) {
                CommonUIBean commonUI = new CommonUIBean();
                commonUI.setTitle(titles[i]);
                commonUI.setLabel(labels[i]);
                if (labels[i].equals("请选择")) {
                    commonUI.setArrowType(1);
                } else {
                    commonUI.setArrowType(0);
                }
                mList.add(commonUI);
            }
        }
        if (Contants.CHOOSE_MODEL_SIZE == 14 || Contants.CHOOSE_MODEL_SIZE == 18) {
            for (int i = 0; i < taxTitles.length; i++) {
                CommonUIBean commonUI = new CommonUIBean();
                commonUI.setTitle(taxTitles[i]);
                commonUI.setLabel(taxLabels[i]);
                if (taxLabels[i].equals("请选择")) {
                    commonUI.setArrowType(1);
                } else {
                    commonUI.setArrowType(0);
                }
                mList.add(commonUI);
            }
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_back:
                finish();
                break;
            case R.id.btn_save:
                // 客户分类编辑
                if (Contants.CHOOSE_MODEL_SIZE == 17){
                    // request
                    //RequestPost.clientTypeEdit();
                }
                break;
            case R.id.ll_default:
                // toast("设置为默认地址");
                if (DEFAULT_ADDRESS_SIZE % 2 == 1) {
                    checked.setImageResource(R.drawable.radio_checked_shape);
                    defaultText.setText("取消设置为默认地址");
                } else {
                    checked.setImageResource(R.drawable.radio_uncheck_shape);
                    defaultText.setText("设置为默认地址");
                }
                DEFAULT_ADDRESS_SIZE++;
                break;
        }
    }

    @SuppressLint("HandlerLeak")
    private Handler mHandler = new Handler(){
        @Override
        public void handleMessage(@NonNull Message msg) {
            switch (msg.what){
                case INITIAL_SIZE:
                    NewGoodsAddressAdapter addressAdapter = new NewGoodsAddressAdapter(NewGoodsAddressActivity.this, mList);
                    contentList.setAdapter(addressAdapter);
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
                // 获取客户分类详情
                if (result.url().contains("wxapi/v1/client.php?type=getKehuTypeInfo")) {
                    CustomTypeBean customType = JsonParser.parseJSONObject(CustomTypeBean.class, JsonParser.parseJSONObject(body.get("data")).get("info"));
                    // 展示客户分类详情
                    if (Contants.CHOOSE_MODEL_SIZE == 17 && customType != null){
                        for (int i = 0; i < ListUtils.getSize(mList); i++) {
                            switch (i){
                                case 0:
                                    mList.get(i).setShowText(customType.getName());
                                    break;
                                case 1:
                                    mList.get(i).setShowText(customType.getDiscount());
                                    break;
                                case 2:
                                    mList.get(i).setShowText(customType.getList());
                                    break;
                            }
                        }
                    }
                    Message msg = new Message();
                    msg.what = INITIAL_SIZE;
                    mHandler.sendMessage(msg);
                }
            } else {
                toast(body.get("data"));
            }
            dismissLoading();
        }
    }
}
