package com.rainwood.eurobusiness.ui.activity;

import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.rainwood.eurobusiness.R;
import com.rainwood.eurobusiness.base.BaseActivity;
import com.rainwood.eurobusiness.common.Contants;
import com.rainwood.eurobusiness.domain.CommonUIBean;
import com.rainwood.eurobusiness.ui.adapter.CommUIDirectAdapter;
import com.rainwood.tools.viewinject.ViewById;
import com.rainwood.tools.widget.MeasureListView;

import java.util.ArrayList;
import java.util.List;

/**
 * @Author: shearson
 * @Time: 2020/2/24 22:36
 * @Desc: 新建供应商
 */
public class NewSupplierActivity extends BaseActivity implements View.OnClickListener {
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

    @Override
    protected void initView() {
        pageBack.setOnClickListener(this);
        confirm.setOnClickListener(this);
        if (Contants.CHOOSE_MODEL_SIZE == 102) {
            pageTitle.setText("新建供应商");
            CommUIDirectAdapter directAdapter = new CommUIDirectAdapter(this, mList);
            contentList.setAdapter(directAdapter);
            directAdapter.setOnClickEditText(position -> {
                // toast("sxs --- " + position);
                switch (mList.get(position).getTitle()) {
                    case "支付方式":
                        toast("支付方式");
                        break;
                    case "所在地区":
                        toast("所在地区");
                        break;
                }
            });
        }

        if (Contants.CHOOSE_MODEL_SIZE == 103) {
            pageTitle.setText("新建门店");
            CommUIDirectAdapter directAdapter = new CommUIDirectAdapter(this, mList);
            contentList.setAdapter(directAdapter);
            directAdapter.setOnClickEditText(position -> {
                if (mList.get(position).getTitle().equals("所在地区")) {
                    toast("所在地区");
                }
            });
        }
    }

    @Override
    protected void initData() {
        super.initData();
        mList = new ArrayList<>();
        if (Contants.CHOOSE_MODEL_SIZE == 102) {
            setData(titles, labels);
        }
        if (Contants.CHOOSE_MODEL_SIZE == 103) {
            setData(storeTitle, storeLabel);
        }
    }

    private void setData(String[] storeTitle, String[] storeLabel) {
        for (int i = 0; i < storeTitle.length; i++) {
            CommonUIBean commonUI = new CommonUIBean();
            commonUI.setTitle(storeTitle[i]);
            if (storeTitle[i].equals("供应商")){
                commonUI.setFillIn(true);
            }
            if (storeTitle[i].equals("门店名称")){
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
                toast("确定");
                break;
        }
    }


    /*
    模拟数据
     */
    private List<CommonUIBean> mList;
    private String[] titles = {"供应商", "电话", "负责人", "支付方式", "银行账号", "邮箱", "所在地区", "详细地址"};
    private String[] labels = {"请输入", "请输入", "请输入", "请选择", "请输入", "请输入", "请选择", "请输入"};
    // 门店
    private String[] storeTitle = {"门店名称", "电话", "邮箱", "所在地区", "详细地址", "税号(P.IVA)",
            "税号(C.F)", "联系人", "门店描述", "设置账号", "设置密码"};
    private String[] storeLabel = {"请输入", "请输入", "请输入", "请选择", "请输入", "请输入", "请输入",
            "请输入", "请输入", "请输入", "请输入"};

}
