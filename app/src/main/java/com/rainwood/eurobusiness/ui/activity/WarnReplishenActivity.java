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
import com.rainwood.eurobusiness.json.JsonParser;
import com.rainwood.eurobusiness.okhttp.HttpResponse;
import com.rainwood.eurobusiness.okhttp.OnHttpListener;
import com.rainwood.eurobusiness.other.BaseDialog;
import com.rainwood.eurobusiness.request.RequestPost;
import com.rainwood.eurobusiness.ui.dialog.DateDialog;
import com.rainwood.eurobusiness.utils.DateTimeUtils;
import com.rainwood.tools.view.ClearEditText;
import com.rainwood.tools.viewinject.ViewById;

import java.util.Map;

/**
 * @Author: a797s
 * @Date: 2020/2/21
 * @Desc: 预警库存 --- 补货
 */
public class WarnReplishenActivity extends BaseActivity implements View.OnClickListener, OnHttpListener {

    @Override
    protected int getLayoutId() {
        return R.layout.activity_warn_replish;
    }

    @ViewById(R.id.iv_back)
    private ImageView pageBack;
    @ViewById(R.id.tv_title)
    private TextView pageTitle;
    /*@ViewById(R.id.lv_content)
    private MeasureListView content;*/
    @ViewById(R.id.btn_confirm)
    private Button confirm;
    //
    @ViewById(R.id.tv_time)
    private TextView time;
    @ViewById(R.id.cet_purchase_num)
    private ClearEditText purchaseNum;
    @ViewById(R.id.cet_purchase_note)
    private ClearEditText purchaseNote;

    // handler
    private final int REPLISH_SIZE = 0x1124;

//    private List<CommonUIBean> mList;
//    private String[] titles = {"交货时间", "采购数量", "备注"};
//    private String[] labels = {"请选择", "请输入", "请输入"};

    @Override
    protected void initView() {
        pageBack.setOnClickListener(this);
        pageTitle.setText("补货");
        confirm.setOnClickListener(this);
        time.setOnClickListener(this);
    }

    @Override
    protected void initData() {
        super.initData();

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_back:
                finish();
                break;
            case R.id.tv_time:
                toFinished();
                break;
            case R.id.btn_confirm:
                if (TextUtils.isEmpty(time.getText())) {
                    toast("请选择时间");
                    return;
                }
                if (TextUtils.isEmpty(purchaseNum.getText())) {
                    toast("请输入采购数量");
                    return;
                }
                String warnId = getIntent().getStringExtra("warnId");
                // TODO
                RequestPost.commitChargeOrder(warnId, purchaseNum.getText().toString().trim(),
                        purchaseNote.getText().toString().trim(), time.getText().toString().trim(), this);
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

                    break;
            }
        }
    };

    /**
     * 选择时间
     */
    private void toFinished() {
        new DateDialog.Builder(this)
                .setTitle(getString(R.string.date_title))
                .setConfirm(getString(R.string.common_confirm))
                .setCancel(getString(R.string.common_cancel))
                .setYear(DateTimeUtils.getNowYear())
                .setMonth(DateTimeUtils.getNowMonth())
                .setDay(DateTimeUtils.getNowDay())
                // 不选择天数
                //.setIgnoreDay()
                .setListener(new DateDialog.OnListener() {
                    @SuppressLint("SetTextI18n")
                    @Override
                    public void onSelected(BaseDialog dialog, int year, int month, int day) {
                        dialog.dismiss();
                        time.setText(year + "-" + month + "-" + day);
                    }

                    @Override
                    public void onCancel(BaseDialog dialog) {
                        dialog.dismiss();
                    }
                }).show();
    }

    @Override
    public void onHttpFailure(HttpResponse result) {

    }

    @Override
    public void onHttpSucceed(HttpResponse result) {
        Map<String, String> body = JsonParser.parseJSONObject(result.body());
        if (body != null) {
            if ("1".equals(body.get("code"))) {
                // 预警补货
                if (result.url().contains("wxapi/v1/stock.php?type=commitChargeOrder")) {
                    toast(body.get("warn"));
                    postDelayed(this::finish, 1000);
                }
            } else {
                toast(body.get("data"));
            }
            if (getDialog() != null)
                dismissLoading();
        }
    }
}
