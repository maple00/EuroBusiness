package com.rainwood.eurobusiness.ui.activity;

import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.rainwood.eurobusiness.R;
import com.rainwood.eurobusiness.base.BaseActivity;
import com.rainwood.eurobusiness.common.Contants;
import com.rainwood.eurobusiness.domain.CommonUIBean;
import com.rainwood.eurobusiness.domain.NewCommUIBean;
import com.rainwood.eurobusiness.other.BaseDialog;
import com.rainwood.eurobusiness.ui.adapter.CustomNewAdapter;
import com.rainwood.eurobusiness.ui.dialog.MenuDialog;
import com.rainwood.tools.viewinject.ViewById;
import com.rainwood.tools.widget.MeasureListView;

import java.util.ArrayList;
import java.util.List;

/**
 * @Author: a797s
 * @Date: 2020/2/22
 * @Desc: 新增
 */
public class CustomNewActivity extends BaseActivity implements View.OnClickListener {

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

    @Override
    protected void initView() {
        pageBack.setOnClickListener(this);
        confirm.setOnClickListener(this);
        if (Contants.CHOOSE_MODEL_SIZE == 15) {
            pageTitle.setText("客户详情");
        } else {
            pageTitle.setText("新增客户");
        }

        CustomNewAdapter customNewAdapter = new CustomNewAdapter(this, mList);
        contentLit.setAdapter(customNewAdapter);
        customNewAdapter.setOnClickEditText((parentPos, position) -> {
            //toast(mList.get(parentPos).getCommonUIList().get(position).getTitle());
            switch (mList.get(parentPos).getCommonUIList().get(position).getTitle()) {
                case "发票信息":
                    // toast("去填写发票地址");
                    Contants.CHOOSE_MODEL_SIZE = 14;
                    openActivity(GoodsAddressActivity.class);
                    break;
                case "收货地址":
                    // toast("选择收货地址");
                    Contants.CHOOSE_MODEL_SIZE = 13;
                    openActivity(GoodsAddressActivity.class);
                    break;
                case "客户类型":
                    setText(customType);
                    break;
                case "支付方式":
                    setText(payMethod);
                    break;
                case "支付期限":
                    setText(payTimeLimited);
                    break;
            }
        });
    }

    /**
     * 设置Text
     *
     * @param payTimeLimited
     */
    private void setText(String[] payTimeLimited) {
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
                        toast(text);
                        dialog.dismiss();
                    }

                    //
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

    /*
    模拟数据
     */
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

}
