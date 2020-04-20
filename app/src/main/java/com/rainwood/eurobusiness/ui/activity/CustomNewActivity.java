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
import com.rainwood.eurobusiness.domain.CommonUIBean;
import com.rainwood.eurobusiness.domain.NewCommUIBean;
import com.rainwood.eurobusiness.json.JsonParser;
import com.rainwood.eurobusiness.okhttp.HttpResponse;
import com.rainwood.eurobusiness.okhttp.OnHttpListener;
import com.rainwood.eurobusiness.other.BaseDialog;
import com.rainwood.eurobusiness.ui.adapter.CustomNewAdapter;
import com.rainwood.eurobusiness.ui.dialog.MenuDialog;
import com.rainwood.eurobusiness.utils.ListUtils;
import com.rainwood.tools.viewinject.ViewById;
import com.rainwood.tools.widget.MeasureListView;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @Author: a797s
 * @Date: 2020/2/22
 * @Desc: 编辑
 */
public class CustomNewActivity extends BaseActivity implements View.OnClickListener, OnHttpListener {

    private ClientEditDetailBean mEditClient;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_custom_new;
    }

    @ViewById(R.id.iv_back)
    private ImageView pageBack;
    @ViewById(R.id.tv_title)
    private TextView pageTitle;
    @ViewById(R.id.lv_content_list)
    private MeasureListView contentLit;
    @ViewById(R.id.btn_confirm)
    private Button confirm;
    private final int INITIAL_SIZE = 0x101;

    @Override
    protected void initView() {
        pageBack.setOnClickListener(this);
        confirm.setOnClickListener(this);
        if (Contants.CHOOSE_MODEL_SIZE == 15) {         // 门店端
            pageTitle.setText("客户详情");
            mEditClient = (ClientEditDetailBean) getIntent().getSerializableExtra("editClient");
            if (mEditClient != null) {
                Log.d(TAG, "mEditClient -- " + mEditClient.toString());
                for (int i = 0; i < ListUtils.getSize(mList); i++) {
                    switch (i) {
                        case 0:                 // 基本资料
                            for (int j = 0; j < ListUtils.getSize(mList.get(i).getCommonUIList()); j++) {
                                switch (j) {
                                    case 0:         // 公司名称
                                        mList.get(i).getCommonUIList().get(j).setShowText(mEditClient.getClientBase().getCompany());
                                        break;
                                    case 1:         // 联系人
                                        mList.get(i).getCommonUIList().get(j).setShowText(mEditClient.getClientBase().getName());
                                        break;
                                    case 2:         // 手机号
                                        mList.get(i).getCommonUIList().get(j).setShowText(mEditClient.getClientBase().getTel());
                                        break;
                                    case 3:         // 邮箱
                                        mList.get(i).getCommonUIList().get(j).setShowText(mEditClient.getClientBase().getEmail());
                                        break;
                                    case 4:         // 发票信息
                                        if (mEditClient.getInvoiceDetai() != null)
                                            mList.get(i).getCommonUIList().get(j).setShowText(mEditClient.getInvoiceDetai().getName());
                                        break;
                                    case 5:         // 收货地址
                                        if (mEditClient.getTakeGoods() != null)
                                            mList.get(i).getCommonUIList().get(j).setShowText(mEditClient.getTakeGoods().getAddressMx());
                                        break;
                                }
                            }
                            break;
                        case 1:                 // 支付信息
                            for (int j = 0; j < ListUtils.getSize(mList.get(i).getCommonUIList()); j++) {
                                switch (j) {
                                    case 0:         // 客户类型
                                        mList.get(i).getCommonUIList().get(j).setShowText(mEditClient.getClientBase().getKehuTypeName());
                                        break;
                                    case 1:         // 支付方式
                                        mList.get(i).getCommonUIList().get(j).setShowText(mEditClient.getClientBase().getPayType());
                                        break;
                                    case 2:         // 支付期限
                                        mList.get(i).getCommonUIList().get(j).setShowText(mEditClient.getClientBase().getPayTerm());
                                        break;
                                    case 3:         // 请输入天数
                                        mList.get(i).getCommonUIList().get(j).setShowText(mEditClient.getClientBase().getPayTerm());
                                        break;
                                    case 4:         // 备注
                                        mList.get(i).getCommonUIList().get(j).setShowText(mEditClient.getClientBase().getText());
                                        break;
                                }
                            }
                            break;
                    }
                }
                Message msg = new Message();
                msg.what = INITIAL_SIZE;
                mHandler.sendMessage(msg);
            }
        } else {            // 批发商端
            pageTitle.setText("新增客户");
            Message msg = new Message();
            msg.what = INITIAL_SIZE;
            mHandler.sendMessage(msg);
        }

    }

    @Override
    protected void initData() {
        super.initData();
        mList = new ArrayList<>();
        for (int i = 0; i < titles.length; i++) {
            NewCommUIBean commUI = new NewCommUIBean();
            commUI.setTitle(titles[i]);
            List<CommonUIBean> commonUIList = new ArrayList<>();
            for (int j = 0; j < baseTitles.length && i == 0; j++) {     // 基本信息
                CommonUIBean commonUI = new CommonUIBean();
                commonUI.setTitle(baseTitles[j]);
                commonUI.setLabel(baseLabels[j]);
                if (baseTitles[j].equals("公司名称")) {
                    commonUI.setFillIn(true);
                }
                if (baseTitles[j].equals("发票信息")) {
                    commonUI.setArrowType(1);
                }
                if (baseTitles[j].equals("收货地址")) {
                    commonUI.setArrowType(1);
                }
                commonUIList.add(commonUI);
            }
            for (int j = 0; j < payTitles.length && i == 1; j++) {            // 支付信息
                CommonUIBean commonUI = new CommonUIBean();
                commonUI.setTitle(payTitles[j]);
                commonUI.setLabel(payLabels[j]);
                if (payTitles[j].equals("客户类型")) {
                    commonUI.setFillIn(true);
                    commonUI.setArrowType(1);
                }
                if (payTitles[j].equals("支付方式")) {
                    commonUI.setArrowType(1);
                }
                if (payTitles[j].equals("支付期限")) {
                    commonUI.setArrowType(1);
                }
                commonUIList.add(commonUI);
            }
            commUI.setCommonUIList(commonUIList);
            mList.add(commUI);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_back:
                finish();
                break;
            case R.id.btn_confirm:
                toast("确定");
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
                    CustomNewAdapter customNewAdapter = new CustomNewAdapter(CustomNewActivity.this, mList);
                    contentLit.setAdapter(customNewAdapter);
                    customNewAdapter.setOnClickEditText((parentPos, position) -> {
                        //toast(mList.get(parentPos).getCommonUIList().get(position).getTitle());
                        switch (mList.get(parentPos).getCommonUIList().get(position).getTitle()) {
                            case "发票信息":
                                // toast("去填写发票地址");
                                Contants.CHOOSE_MODEL_SIZE = 14;
                                Intent intent = new Intent(CustomNewActivity.this, GoodsAddressActivity.class);
                                intent.putExtra("customId", mEditClient.getClientBase().getId());
                                startActivity(intent);
                                break;
                            case "收货地址":
                                // toast("选择收货地址");
                                Contants.CHOOSE_MODEL_SIZE = 13;
                                Intent intent1 = new Intent(CustomNewActivity.this, GoodsAddressActivity.class);
                                intent1.putExtra("customId", mEditClient.getClientBase().getId());
                                startActivity(intent1);
                                break;
                            case "客户类型":
                                setText(parentPos, position, customType);
                                break;
                            case "支付方式":
                                setText(parentPos, position, payMethod);
                                break;
                            case "支付期限":
                                setText(parentPos, position, payTimeLimited);
                                break;
                        }
                    });
                    break;
            }
        }
    };

    /**
     * 设置Text
     *
     * @param payTimeLimited
     */
    private void setText(int parentPos, int pos, String[] payTimeLimited) {
        new MenuDialog.Builder(getActivity())
                // 设置null 表示不显示取消按钮
                .setCancel(R.string.common_cancel)
                // 设置点击按钮后不关闭弹窗
                .setAutoDismiss(false)
                // 显示的数据
                .setList(payTimeLimited)
                //.setCanceledOnTouchOutside(false)
                .setListener(new MenuDialog.OnListener<String>() {
                    @Override
                    public void onSelected(BaseDialog dialog, int position, String text) {
                        // toast(text);
                        dialog.dismiss();
                        mList.get(parentPos).getCommonUIList().get(pos).setShowText(text);

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

    private List<NewCommUIBean> mList;
    private String[] titles = {"基本资料", "支付信息"};
    // 基本资料
    private String[] baseTitles = {"公司名称", "联系人", "手机号", "邮箱", "发票信息", "收货地址"};
    private String[] baseLabels = {"请输入", "请输入联系人姓名", "手机号前面加国家固定代码", "请输入联系人邮箱",
            "去填写", "去填写"};
    // 支付信息
    private String[] payTitles = {"客户类型", "支付方式", "支付期限", "天数", "备注"};
    private String[] payLabels = {"请选择", "请选择", "请选择", "请输入天数", "请输入"};
    // 支付方式
    private String[] payMethod = {"现金支票", "现金汇款", "日期支票", "其它"};
    // 支付期限
    private String[] payTimeLimited = {"按周付", "按月付", "按季度付", "按年付"};
    // 客户类型
    private String[] customType = {"VIP会员", "普通会员", "普通客户", "其它"};

    @Override
    public void onHttpFailure(HttpResponse result) {

    }

    @Override
    public void onHttpSucceed(HttpResponse result) {
        Map<String, String> body = JsonParser.parseJSONObject(result.body());
        if (body != null) {
            if ("1".equals(body.get("code"))) {
                // 编辑客户详情
                if (result.url().contains("wxapi/v1/client.php?type=getClientInfo")) {

                }
            } else {

            }
            dismissLoading();
        }
    }
}
