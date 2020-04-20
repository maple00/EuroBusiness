package com.rainwood.eurobusiness.ui.activity;

import android.widget.Button;
import android.widget.ListView;

import com.rainwood.eurobusiness.R;
import com.rainwood.eurobusiness.base.BaseActivity;
import com.rainwood.eurobusiness.common.Contants;
import com.rainwood.eurobusiness.domain.PressBean;
import com.rainwood.eurobusiness.ui.adapter.IdentityListAdapter;
import com.rainwood.eurobusiness.utils.ListUtils;
import com.rainwood.tools.viewinject.ViewById;

import java.util.ArrayList;
import java.util.List;

/**
 * @Author: a797s
 * @Date: 2020/2/6
 * @Desc:
 */
public class IdentityActivity extends BaseActivity {

    @Override
    protected int getLayoutId() {
        return R.layout.activity_identity;
    }

    @ViewById(R.id.lv_identity)
    private ListView identityList;
    @ViewById(R.id.btn_next_step)
    private Button nextStep;


    private List<PressBean> mList;

    private String[] titleNames = {"供应商", "门店"};

    @Override
    protected void initView() {
        // 初始化
        final IdentityListAdapter adapter = new IdentityListAdapter(this, mList);
        identityList.setAdapter(adapter);
        // 选中
        adapter.setIdentity(position -> {
            if (mList.get(position).isChoose()) {        // 已经被选中
                toast("您当前的身份是：" + mList.get(position).getTitle());
                return;
            }
            // 全部置为false
            for (PressBean pressBean : mList) {
                pressBean.setChoose(false);
            }
            mList.get(position).setChoose(true);
        });

        // 下一步---进入登陆界面
        nextStep.setOnClickListener(v -> {
            // 获取选中的用户类型
            for (int i = 0; i < ListUtils.getSize(mList); i++) {
                if (mList.get(i).isChoose()) {
                    Contants.userType = i;
                    break;
                }
            }
            if (Contants.userType != -1) {               // 有选中的用户
                // toast("选中的身份是：" + mList.get(Contants.userType).getTitle());
                openActivity(LoginActivity.class);
            } else {
                toast("请选择您的身份");
            }
        });
    }

    @Override
    protected void initData() {
        mList = new ArrayList<>();
        for (int i = 0; i < titleNames.length; i++) {
            PressBean press = new PressBean();
            press.setChoose(false);
            press.setTitle(titleNames[i]);
            mList.add(press);
        }
    }

}
