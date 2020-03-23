package com.rainwood.eurobusiness.ui.activity;

import android.annotation.SuppressLint;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.rainwood.eurobusiness.R;
import com.rainwood.eurobusiness.base.BaseActivity;
import com.rainwood.eurobusiness.domain.InvoiceBean;
import com.rainwood.eurobusiness.json.JsonParser;
import com.rainwood.eurobusiness.okhttp.HttpResponse;
import com.rainwood.eurobusiness.okhttp.OnHttpListener;
import com.rainwood.eurobusiness.request.RequestPost;
import com.rainwood.eurobusiness.ui.adapter.InvoiceListAdapter;
import com.rainwood.eurobusiness.utils.ListUtils;
import com.rainwood.tools.viewinject.ViewById;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @Author: a797s
 * @Date: 2020/2/7
 * @Desc: 开票信息
 */
public class InvoiceActivity extends BaseActivity implements View.OnClickListener, OnHttpListener {

    @Override
    protected int getLayoutId() {
        return R.layout.activity_invoice;
    }

    @ViewById(R.id.iv_back)
    private ImageView pageBack;
    @ViewById(R.id.tv_title)
    private TextView pageTitle;
    @ViewById(R.id.lv_invoice)
    private ListView invoiceList;

    private final int INITIAL_SIZE = 0x101;

    private List<InvoiceBean> mList;

    private String[] companys = {"重庆双木衣馆服饰有限公司", "重庆双木衣馆服饰有限公司", "重庆双木衣馆服饰有限公司"};


    @Override
    protected void initView() {
        pageBack.setOnClickListener(this);
        pageBack.setImageResource(R.drawable.icon_white_page_back);
        pageTitle.setText("开票信息");
        pageTitle.setTextColor(getResources().getColor(R.color.white));

        // request
        showLoading("loading");
        RequestPost.storeOrInvoice(this);
    }

    @Override
    protected void initData() {
        mList = new ArrayList<>();
        /*for (String company : companys) {
            InvoiceBean invoice = new InvoiceBean();
            invoice.setCompany(company);
            invoice.setTaxP("2250 6250 4870 8620");
            invoice.setLocation("中国-重庆");
            invoice.setAddress("重庆市南岸区弹子石腾龙大道国际商务大厦A座22-2");
            invoice.setTel("+86 13512270415");
            mList.add(invoice);
        }*/
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.iv_back) {
            finish();
        }
    }

    @SuppressLint("HandlerLeak")
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(@NonNull Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case INITIAL_SIZE:
                    InvoiceListAdapter adapter = new InvoiceListAdapter(InvoiceActivity.this, mList);
                    invoiceList.setAdapter(adapter);

                    // 复制内容到剪贴板
                    adapter.setiCopyContext2Board(new InvoiceListAdapter.ICopyContext2Board() {
                        // 获取到剪贴板对象
                        ClipboardManager clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);

                        @Override
                        public void OnCopyTaxNum(int position) {            // 复制税号
                            //第一个参数只是一个标记，随便传入。
                            //第二个参数是要复制到剪贴版的内容
                            ClipData clip = ClipData.newPlainText("tax_num", mList.get(position).getTaxP());
                            // 传入剪贴板对象
                            clipboard.setPrimaryClip(clip);
                            toast("已成功复制到剪贴板");
                        }

                        @Override
                        public void OnCopyAllContext(int position) {
                            // 开票信息格式
                            String formatTax = "【开票公司：" + mList.get(position).getCompany() + "，税号："
                                    + mList.get(position).getTaxP() + "，地址：" + mList.get(position).getLocation() + "-"
                                    + mList.get(position).getAddress() + "，电话：" + mList.get(position).getTel() + "】";
                            ClipData clip = ClipData.newPlainText("tax_info", formatTax);
                            // 传入剪贴板对象
                            clipboard.setPrimaryClip(clip);
                            toast("已成功复制到剪贴板");
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
                // 门店或开票信息
                if (result.url().contains("wxapi/v1/index.php?type=getStoreInfo")) {
                    Map<String, String> data = JsonParser.parseJSONObject(JsonParser.parseJSONObject(body.get("data")).get("info"));
                    /*if (data != null)
                        for (int i = 0; i < ListUtils.getSize(mList); i++) {
                            switch (i) {
                                case 0:
                                    mList.get(i).setCompany(data.get("id"));
                                    break;
                                case 1:
                                    break;
                                case 2:
                                    break;
                                case 3:
                                    break;
                                case 4:
                                    break;
                                case 5:
                                    break;
                                case 6:
                                    break;
                                case 7:
                                    break;
                                case 8:
                                    break;
                            }
                        }*/
                    InvoiceBean invoice = new InvoiceBean();
                    invoice.setCompany(data.get("id"));
                    invoice.setTaxP(data.get("companyTax"));
                    invoice.setLocation(data.get("region"));
                    invoice.setAddress(data.get("address"));
                    invoice.setTel(data.get("tel"));

                    mList.add(invoice);

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
