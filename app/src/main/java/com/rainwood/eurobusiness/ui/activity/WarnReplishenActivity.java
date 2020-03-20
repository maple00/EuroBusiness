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
import com.rainwood.eurobusiness.domain.CommonUIBean;
import com.rainwood.eurobusiness.other.BaseDialog;
import com.rainwood.eurobusiness.ui.adapter.WarnReplishAdapter;
import com.rainwood.eurobusiness.ui.dialog.DateDialog;
import com.rainwood.tools.viewinject.ViewById;
import com.rainwood.tools.widget.MeasureListView;

import java.util.ArrayList;
import java.util.List;

/**
 * @Author: a797s
 * @Date: 2020/2/21
 * @Desc: 预警库存 --- 补货
 */
public class WarnReplishenActivity extends BaseActivity implements View.OnClickListener {

    @Override
    protected int getLayoutId() {
        return R.layout.activity_warn_replish;
    }

    @ViewById(R.id.iv_back)
    private ImageView pageBack;
    @ViewById(R.id.tv_title)
    private TextView pageTitle;
    @ViewById(R.id.lv_content)
    private MeasureListView content;
    @ViewById(R.id.btn_confirm)
    private Button confirm;

    // handler
    private final int REPLISH_SIZE = 0x1124;

    @Override
    protected void initView() {
        pageBack.setOnClickListener(this);
        pageTitle.setText("补货");
        confirm.setOnClickListener(this);
        Message msg = new Message();
        msg.what = REPLISH_SIZE;
        mHandler.sendMessage(msg);

    }

    /**
     * 选择时间
     */
    private void toFinished(int position) {
        new DateDialog.Builder(this)
                .setTitle(getString(R.string.date_title))
                // 确定文本
                .setConfirm(getString(R.string.common_confirm))
                // 设置为null 时表示不显示取消按钮
                .setCancel(getString(R.string.common_cancel))
                // 设置日期(可支持2019-12-03， 20191203， 时间戳)
                // .setDate(20191203)
                // 设置年份
                //.setYear(2019)
                // 设置月份
                //.setMonth(12)
                // 设置天数
                //.setDay(3)
                // 不选择天数
                //.setIgnoreDay()
                .setListener(new DateDialog.OnListener() {
                    @Override
                    public void onSelected(BaseDialog dialog, int year, int month, int day) {
                        // toast(year + "-" + month + "-" + day);
                        mList.get(position).setShowText(year + "-" + month + "-" + day);
                        //
                        Message msg = new Message();
                        msg.what = REPLISH_SIZE;
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

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_back:
                finish();
                break;
            case R.id.btn_confirm:
                toast("提交");
                break;
        }
    }

    @SuppressLint("HandlerLeak")
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(@NonNull Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case REPLISH_SIZE:
                    WarnReplishAdapter replishAdapter = new WarnReplishAdapter(WarnReplishenActivity.this, mList);
                    content.setAdapter(replishAdapter);
                    replishAdapter.setOnClickLine(position -> {
                        // toast(mList.get(position).getTitle());
                        switch (mList.get(position).getTitle()) {
                            case "交货时间":
                                toFinished(position);
                                break;
                        }
                    });
                    break;
            }
        }
    };

    private List<CommonUIBean> mList;
    private String[] titles = {"交货时间", "采购数量", "备注"};
    private String[] labels = {"请选择", "请输入", "请输入"};

}
