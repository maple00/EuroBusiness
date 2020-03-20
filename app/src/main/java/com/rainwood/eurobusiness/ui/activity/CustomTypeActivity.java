package com.rainwood.eurobusiness.ui.activity;

import android.annotation.SuppressLint;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.rainwood.eurobusiness.R;
import com.rainwood.eurobusiness.base.BaseActivity;
import com.rainwood.eurobusiness.common.Contants;
import com.rainwood.eurobusiness.domain.CustomTypeBean;
import com.rainwood.eurobusiness.other.BaseDialog;
import com.rainwood.eurobusiness.ui.adapter.CustomTypeAdapter;
import com.rainwood.eurobusiness.ui.dialog.MenuDialog;
import com.rainwood.tools.viewinject.ViewById;
import com.rainwood.tools.widget.MeasureListView;

import java.util.ArrayList;
import java.util.List;

/**
 * @Author: shearson
 * @Time: 2020/2/24 18:19
 * @Desc: 客户分类
 */
public class CustomTypeActivity extends BaseActivity implements View.OnClickListener {

    @Override
    protected int getLayoutId() {
        return R.layout.activity_custom_type;
    }

    @ViewById(R.id.iv_back)
    private ImageView pageBack;
    @ViewById(R.id.tv_title)
    private TextView pageTitle;
    @ViewById(R.id.btn_new_type)
    private Button newType;
    @ViewById(R.id.lv_content_list)
    private MeasureListView contentList;
    // handler
    private final int DEFAULT_SIZE = 0x1124;

    @Override
    protected void initView() {
        pageBack.setOnClickListener(this);
        newType.setOnClickListener(this);
        pageTitle.setText("客户分类");

        // content
        Message msg = new Message();
        msg.what = DEFAULT_SIZE;
        mHandler.sendMessage(msg);

    }

    /**
     * 点击编辑/删除
     */
    private void getMenuContent(int pos) {
        // 底部选择框
        new MenuDialog.Builder(this)
                // 设置null 表示不显示取消按钮
                .setCancel(R.string.common_cancel)
                // 设置点击按钮后不关闭弹窗
                .setAutoDismiss(false)
                // 设置不可点击
                .setCanceledOnTouchOutside(false)
                // 显示的数据
                .setList(menuLabels)
                .setListener(new MenuDialog.OnListener<String>() {
                    @Override
                    public void onSelected(BaseDialog dialog, int position, String text) {
                        // toast("位置：" + position + ", 文本：" + text);
                        switch (position) {
                            case 0:
                                 toast("编辑客户分类");
                                Contants.CHOOSE_MODEL_SIZE = 17;
                                openActivity(NewGoodsAddressActivity.class);
                                break;
                            case 1:
                                // toast("删除");
                                mList.remove(pos);
                                Message msg = new Message();
                                msg.what = DEFAULT_SIZE;
                                mHandler.sendMessage(msg);
                                break;
                        }
                        dialog.dismiss();
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
        for (int i = 0; i < tyeps.length; i++) {
            CustomTypeBean type = new CustomTypeBean();
            type.setId(String.valueOf(i + 1));
            type.setType(tyeps[i]);
            type.setPercent(percent[i]);
            mList.add(type);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_back:
                finish();
                break;
            case R.id.btn_new_type:
                // toast("新增分类");
                Contants.CHOOSE_MODEL_SIZE = 16;
                openActivity(NewGoodsAddressActivity.class);
                break;
        }
    }

    @SuppressLint("HandlerLeak")
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(@NonNull Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case DEFAULT_SIZE:
                    CustomTypeAdapter typeAdapter = new CustomTypeAdapter(CustomTypeActivity.this, mList);
                    contentList.setAdapter(typeAdapter);
                    typeAdapter.setOnClickItem(position -> getMenuContent(position));
                    break;
            }
        }
    };

    /*
    模拟数据
     */
    private List<CustomTypeBean> mList;
    private String[] tyeps = {"普通客户", "VIP客户", "黄金会员", "铂金会员", "钻石会员"};
    private String[] percent = {"10%", "15%", "25%", "30%", "35%"};
    private String[] menuLabels = {"编辑", "删除"};
}
