package com.rainwood.eurobusiness.ui.activity;

import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.rainwood.eurobusiness.R;
import com.rainwood.eurobusiness.base.BaseActivity;
import com.rainwood.eurobusiness.common.Contants;
import com.rainwood.eurobusiness.domain.CommonUIBean;
import com.rainwood.eurobusiness.ui.adapter.NewGoodsAddressAdapter;
import com.rainwood.tools.viewinject.ViewById;
import com.rainwood.tools.widget.MeasureListView;

import java.util.ArrayList;
import java.util.List;

/**
 * @Author: shearson
 * @Time: 2020/2/24 17:19
 * @Desc: 新增收货地址
 */
public class NewGoodsAddressActivity extends BaseActivity implements View.OnClickListener {
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
        }
        if (Contants.CHOOSE_MODEL_SIZE == 14) {
            pageTitle.setText("新增开票地址");
            defaultAddress.setVisibility(View.VISIBLE);
            save.setText(R.string.common_save);
        }
        if (Contants.CHOOSE_MODEL_SIZE == 16) {
            pageTitle.setText("新增分类");
            defaultAddress.setVisibility(View.GONE);
            save.setText(R.string.common_confirm);
        }
        if (Contants.CHOOSE_MODEL_SIZE == 17) {
            pageTitle.setText("编辑分类");
            defaultAddress.setVisibility(View.GONE);
            save.setText(R.string.common_confirm);
        }
        NewGoodsAddressAdapter addressAdapter = new NewGoodsAddressAdapter(this, mList);
        contentList.setAdapter(addressAdapter);
    }

    @Override
    protected void initData() {
        super.initData();
        mList = new ArrayList<>();
        if (Contants.CHOOSE_MODEL_SIZE == 13) {
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
        if (Contants.CHOOSE_MODEL_SIZE == 14) {
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
        if (Contants.CHOOSE_MODEL_SIZE == 16 || Contants.CHOOSE_MODEL_SIZE == 17) {
            for (int i = 0; i < typeTitles.length; i++) {
                CommonUIBean commonUI = new CommonUIBean();
                commonUI.setTitle(typeTitles[i]);
                commonUI.setLabel(typeLabel[i]);
                if (typeLabel[i].equals("请选择")) {
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
                toast(save.getText().toString());
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

    /*
    模拟数据
     */
    private List<CommonUIBean> mList;
    // 收货地址
    private String[] titles = {"公司名称", "收货人", "手机号", "所在地区", "详细地址"};
    private String[] labels = {"请输入", "请输入", "请输入收货人手机号", "请选择", "请输入"};
    // 开票地址
    private String[] taxTitles = {"公司名称", "公司税号（P.IVA）", "PEC邮箱/SDI", "手机号", "所在地区", "详细地址"};
    private String[] taxLabels = {"请输入", "请输入", "请输入", "请输入", "请选择", "请输入"};
    // 客户分类
    private String[] typeTitles = {"客户分类", "折扣(%)", "排序号"};
    private String[] typeLabel = {"请输入", "请输入", "请输入"};
}
