package com.rainwood.eurobusiness.ui.activity;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.rainwood.eurobusiness.R;
import com.rainwood.eurobusiness.base.BaseActivity;
import com.rainwood.eurobusiness.domain.InvoiceBean;
import com.rainwood.eurobusiness.ui.adapter.InvoiceListAdapter;
import com.rainwood.tools.viewinject.ViewById;
import com.rainwood.tools.widget.MeasureListView;

import java.util.ArrayList;
import java.util.List;

/**
 * @Author: a797s
 * @Date: 2020/2/7
 * @Desc: 开票信息
 */
public class InvoiceActivity extends BaseActivity implements View.OnClickListener {

    @Override
    protected int getLayoutId() {
        return R.layout.activity_invoice;
    }

    @ViewById(R.id.iv_back)
    private ImageView pageBack;
    @ViewById(R.id.tv_title)
    private TextView pageTitle;
    @ViewById(R.id.lv_invoice)
    private MeasureListView invoiceList;

    @Override
    protected void initView() {
        pageBack.setOnClickListener(this);
        pageBack.setImageResource(R.drawable.icon_white_page_back);
        pageTitle.setText("开票信息");
        pageTitle.setTextColor(getResources().getColor(R.color.white));

        InvoiceListAdapter adapter = new InvoiceListAdapter(this, mList);
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
    }

    @Override
    protected void initData() {
        mList = new ArrayList<>();
        for (int i = 0; i < companys.length; i++) {
            InvoiceBean invoice = new InvoiceBean();
            invoice.setCompany(companys[i]);
            invoice.setTaxP("2250 6250 4870 8620");
            invoice.setLocation("中国-重庆");
            invoice.setAddress("重庆市南岸区弹子石腾龙大道国际商务大厦A座22-2");
            invoice.setTel("+86 13512270415");
            mList.add(invoice);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_back:
                finish();
                break;

            default:
                break;
        }
    }

    /*
    模拟数据
     */
    private List<InvoiceBean> mList;

    private String[] companys = {"重庆双木衣馆服饰有限公司"};
}
