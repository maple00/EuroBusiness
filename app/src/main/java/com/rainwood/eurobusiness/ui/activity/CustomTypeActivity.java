package com.rainwood.eurobusiness.ui.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
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
import com.rainwood.eurobusiness.json.JsonParser;
import com.rainwood.eurobusiness.okhttp.HttpResponse;
import com.rainwood.eurobusiness.okhttp.OnHttpListener;
import com.rainwood.eurobusiness.other.BaseDialog;
import com.rainwood.eurobusiness.request.RequestPost;
import com.rainwood.eurobusiness.ui.adapter.CustomTypeAdapter;
import com.rainwood.eurobusiness.ui.dialog.MenuDialog;
import com.rainwood.eurobusiness.utils.ListUtils;
import com.rainwood.tools.viewinject.ViewById;
import com.rainwood.tools.widget.MeasureListView;

import java.util.List;
import java.util.Map;

/**
 * @Author: shearson
 * @Time: 2020/2/24 18:19
 * @Desc: 客户分类
 */
public class CustomTypeActivity extends BaseActivity implements View.OnClickListener, OnHttpListener {

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
    private List<CustomTypeBean> mList;
    private String[] menuLabels = {"编辑", "删除"};

    @Override
    protected void initView() {
        pageBack.setOnClickListener(this);
        newType.setOnClickListener(this);
        pageTitle.setText("客户分类");
    }

    @Override
    protected void onResume() {
        super.onResume();
        // request
        showLoading("loading");
        RequestPost.getClientManagerTypeList(this);
    }

    /**
     * 点击编辑/删除
     */
    private void getMenuContent(int pos) {
        new MenuDialog.Builder(this)
                .setCancel(R.string.common_cancel)
                .setAutoDismiss(false)
                .setCanceledOnTouchOutside(false)
                .setList(menuLabels)
                .setListener(new MenuDialog.OnListener<String>() {
                    @Override
                    public void onSelected(BaseDialog dialog, int position, String text) {
                        // toast("位置：" + position + ", 文本：" + text);
                        switch (position) {
                            case 0:
                                //toast("编辑客户分类");  --- 分类实体
                                Contants.CHOOSE_MODEL_SIZE = 17;
                                Intent intent = new Intent(CustomTypeActivity.this, CustomCreateTypeActivity.class);
                                intent.putExtra("typeId", mList.get(pos).getId());
                                startActivity(intent);
                                break;
                            case 1:         // 删除客户分类
                                // request
                                showLoading("loading");
                                RequestPost.clearCustomType(mList.get(pos).getId(), CustomTypeActivity.this);
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
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_back:
                finish();
                break;
            case R.id.btn_new_type:
                // toast("新增分类");
                openActivity(CustomCreateTypeActivity.class);
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

    @Override
    public void onHttpFailure(HttpResponse result) {

    }

    @Override
    public void onHttpSucceed(HttpResponse result) {
        Map<String, String> body = JsonParser.parseJSONObject(result.body());
        if (body != null) {
            if ("1".equals(body.get("code"))) {
                // 获取客户列表
                if (result.url().contains("wxapi/v1/client.php?type=getKehuTypelist")) {
                    mList = JsonParser.parseJSONArray(CustomTypeBean.class, JsonParser.parseJSONObject(body.get("data")).get("kehuTypelist"));
                    for (int i = 0; i < ListUtils.getSize(mList); i++) {
                        mList.get(i).setOrder(String.valueOf(i + 1));
                    }
                    Message msg = new Message();
                    msg.what = DEFAULT_SIZE;
                    mHandler.sendMessage(msg);
                }
                // 删除客户分类
                if (result.url().contains("wxapi/v1/client.php?type=delKehuType")){
                    toast("删除成功");

                }
            } else {
                toast(body.get("data"));
            }
            dismissLoading();
        }
    }
}
