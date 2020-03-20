package com.rainwood.eurobusiness.ui.activity;

import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.rainwood.eurobusiness.R;
import com.rainwood.eurobusiness.base.BaseActivity;
import com.rainwood.eurobusiness.domain.CommonUIBean;
import com.rainwood.eurobusiness.domain.StoresBean;
import com.rainwood.eurobusiness.ui.adapter.StoresListAdapter;
import com.rainwood.eurobusiness.ui.fragment.PersonalFragment;
import com.rainwood.tools.viewinject.ViewById;

import java.util.ArrayList;
import java.util.List;

/**
 * @Author: a797s
 * @Date: 2020/2/7
 * @Desc: 门店信息
 */
public class StoresActivity extends BaseActivity implements View.OnClickListener {

    @Override
    protected int getLayoutId() {
        return R.layout.activity_stores;
    }

    @ViewById(R.id.iv_back)
    private ImageView pageBack;
    @ViewById(R.id.tv_title)
    private TextView pageTitle;
    @ViewById(R.id.lv_stores)
    private ListView storesInfos;

    @Override
    protected void initView() {
        pageBack.setOnClickListener(this);
        pageTitle.setText("门店信息");

        StoresListAdapter adapter = new StoresListAdapter(this, mList);
        storesInfos.setAdapter(adapter);

    }

    @Override
    protected void initData() {
        mList = new ArrayList<>();
        for (int i = 0; i < titles.length; i++) {
            CommonUIBean commonUI = new CommonUIBean();
            commonUI.setTitle(titles[i]);
            commonUI.setLabel(labels[i]);
            mList.add(commonUI);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_back:
                //openActivity(PersonalFragment.class);
                finish();
                break;
        }
    }

    /*
    模拟数据
     */
    private List<CommonUIBean> mList;

    private String[] titles = {"门店名称", "联系人", "邮箱", "电话", "所在地区", "详细地址", "税号(P.IVA)", "税号(C.F)", "备注"};
    private String[] labels = {"来福士门店", "谢婷治", "1565921@163.com", "+86 13504151227",
        "中国-重庆", "重庆市南岸区弹子石腾龙大道国际商务大厦A座22-2", "5301127134076", "5301127134076",
        "这里是备注文字内容这里是备注文字内容这里是备注文字内容"};
}
