package com.rainwood.eurobusiness.ui.fragment;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.rainwood.eurobusiness.R;
import com.rainwood.eurobusiness.base.BaseFragment;
import com.rainwood.eurobusiness.domain.ClassifyBean;
import com.rainwood.eurobusiness.json.JsonParser;
import com.rainwood.eurobusiness.okhttp.HttpResponse;
import com.rainwood.eurobusiness.okhttp.OnHttpListener;
import com.rainwood.eurobusiness.other.BaseDialog;
import com.rainwood.eurobusiness.request.RequestPost;
import com.rainwood.eurobusiness.ui.adapter.SaleClassifyAdapter;
import com.rainwood.eurobusiness.ui.dialog.InputDialog;
import com.rainwood.eurobusiness.ui.dialog.MenuDialog;
import com.rainwood.eurobusiness.ui.dialog.MessageDialog;
import com.rainwood.eurobusiness.utils.ListUtils;
import com.rainwood.eurobusiness.utils.RecyclerViewSpacesItemDecoration;
import com.rainwood.tools.common.FontDisplayUtil;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * @Author: shearson
 * @Time: 2020/2/26 16:44
 * @Desc: 批发商 ---  商品管理(商品分类)
 */
public class GoodsClassifyFragment extends BaseFragment implements View.OnClickListener, OnHttpListener {
    @Override
    protected int initLayout() {
        return R.layout.fragment_goods_classify;
    }

    private RecyclerView contentList;

    private final int CLASSIFY_SIZE = 0x1124;

    private List<ClassifyBean> mList;
    // 底部弹窗
    private String[] parentDialog = {"新增子分类", "重命名", "停用分类", "删除"};
    private String[] subDialog = {"重命名", "停用分类", "删除"};

    @Override
    protected void initView(View view) {
        TextView newType = view.findViewById(R.id.tv_new_type);
        newType.setOnClickListener(this);
        newType.setText("新增首级分类");
        contentList = view.findViewById(R.id.rlv_content_list);
    }

    @Override
    public void onResume() {
        super.onResume();
        // request
        showLoading("");
        RequestPost.getAllGoodsType(this);
    }

    @Override
    protected void initData(Context mContext) {

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_new_type:
                new InputDialog.Builder(getActivity())
                        .setTitle("新增首级分类")
                        .setHint("请输入分类名称")
                        .setConfirm(R.string.common_confirm)
                        .setCancel(R.string.common_cancel)
                        .setAutoDismiss(false)               // 设置点击按钮后关不闭弹窗
                        .setCanceledOnTouchOutside(false)
                        .setListener(new InputDialog.OnListener() {
                            @Override
                            public void onConfirm(BaseDialog dialog, String content) {
                                if (TextUtils.isEmpty(content)) {
                                    toast("请输入分类名称");
                                } else {
                                    dialog.dismiss();
                                    // request
                                    showLoading("");
                                    RequestPost.newGoodsTypeOne("", content, GoodsClassifyFragment.this);
                                }
                            }

                            @Override
                            public void onCancel(BaseDialog dialog) {
                                dialog.dismiss();
                            }
                        }).show();
                break;
        }
    }

    @SuppressLint("HandlerLeak")
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(@NonNull Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case CLASSIFY_SIZE:
                    SaleClassifyAdapter adapter = new SaleClassifyAdapter(getContext());
                    LinearLayoutManager managerVertical = new LinearLayoutManager(getContext());
                    managerVertical.setOrientation(LinearLayoutManager.VERTICAL);       // 纵向
                    // 设置Item之间的间距
                    if (count == 0) {
                        HashMap<String, Integer> stringIntegerHashMap = new HashMap<>();
                        stringIntegerHashMap.put(RecyclerViewSpacesItemDecoration.BOTTOM_DECORATION, FontDisplayUtil.dip2px(Objects.requireNonNull(getContext()), 20)); // 下间距
                        contentList.addItemDecoration(new RecyclerViewSpacesItemDecoration(stringIntegerHashMap));
                        contentList.setLayoutManager(managerVertical);
                        contentList.setHasFixedSize(true);
                        count++;
                    }
                    contentList.setAdapter(adapter);
                    adapter.setmList(mList);
                    // 点击事件 --- 父类的底部弹窗
                    adapter.setOnClickItem(position -> setParentDialog(position));
                    // 点击事件 --- 子类的底部弹窗
                    adapter.setOnClickPoint((parentPos, position) -> setSubDialog(parentPos, position));
                    break;
            }
        }
    };

    private static int count = 0;

    /**
     * 子项的弹窗
     *
     * @param parentPos
     * @param pos
     */
    private void setSubDialog(int parentPos, int pos) {
        new MenuDialog.Builder(getActivity())
                .setCancel(R.string.common_cancel)
                .setAutoDismiss(false)
                .setList(subDialog)
                .setCanceledOnTouchOutside(false)
                .setListener(new MenuDialog.OnListener<String>() {
                    @Override
                    public void onSelected(BaseDialog dialog, int position, String text) {
                        dialog.dismiss();
                        if (text.equals("重命名")) {
                            new InputDialog.Builder(getActivity())
                                    .setTitle("重命名")
                                    .setHint("请输入")
                                    .setConfirm(R.string.common_confirm)
                                    .setCancel(R.string.common_cancel)
                                    .setAutoDismiss(false)               // 设置点击按钮后关不闭弹窗
                                    .setCanceledOnTouchOutside(false)
                                    .setListener(new InputDialog.OnListener() {
                                        @Override
                                        public void onConfirm(BaseDialog dialog, String content) {
                                            if (TextUtils.isEmpty(content)){
                                                toast("请输入分类名称");
                                                return;
                                            }
                                            // request
                                            showLoading("");
                                            RequestPost.newGoodsTypeChild("update",
                                                    mList.get(parentPos).getGoodsTypeTwolist().get(pos).getGoodsTypeTwoId(),
                                                    content, GoodsClassifyFragment.this);
                                        }

                                        @Override
                                        public void onCancel(BaseDialog dialog) {
                                            dialog.dismiss();
                                        }
                                    }).show();
                        } else if (text.equals("停用分类")) {
                            new MessageDialog.Builder(getActivity())
                                    .setTitle("确定停用该分类？")
                                    .setMessage("停用后该分类下所有商品强制下架")
                                    .setConfirm(getString(R.string.common_confirm))
                                    .setCancel(getString(R.string.common_cancel))
                                    .setAutoDismiss(false)
                                    .setCanceledOnTouchOutside(false)
                                    .setListener(new MessageDialog.OnListener() {
                                        @Override
                                        public void onConfirm(BaseDialog dialog) {
                                            dialog.dismiss();
                                            // request
                                            showLoading("");
                                            RequestPost.changeGoodsType(mList.get(parentPos).getGoodsTypeTwolist().get(pos).getGoodsTypeTwoId(),
                                                    GoodsClassifyFragment.this);
                                        }

                                        @Override
                                        public void onCancel(BaseDialog dialog) {
                                            dialog.dismiss();
                                        }
                                    }).show();
                        } else {
                            new MessageDialog.Builder(getActivity())
                                    .setMessage("确定删除该分类？")
                                    .setConfirm(getString(R.string.common_confirm))
                                    .setCancel(getString(R.string.common_cancel))
                                    .setAutoDismiss(false)
                                    .setCanceledOnTouchOutside(false)
                                    .setListener(new MessageDialog.OnListener() {
                                        @Override
                                        public void onConfirm(BaseDialog dialog) {
                                            dialog.dismiss();
                                            // 点击了删除
                                           /* if (mList.get(parentPos).getGoodsTypeTwolist() == null && mList.get(parentPos).getGoodsTypeTwolist().size() == 0) {
                                                mList.remove(pos);
                                                Message msg = new Message();
                                                msg.what = CLASSIFY_SIZE;
                                                mHandler.sendMessage(msg);
                                            }*/
                                            // request
                                            showLoading("");
                                            RequestPost.delGoodsType(mList.get(parentPos).getGoodsTypeTwolist().get(pos).getGoodsTypeTwoId(),
                                                    GoodsClassifyFragment.this);
                                        }

                                        @Override
                                        public void onCancel(BaseDialog dialog) {
                                            dialog.dismiss();
                                        }
                                    }).show();
                        }
                    }

                    @Override
                    public void onCancel(BaseDialog dialog) {
                        dialog.dismiss();
                    }
                }).show();

    }

    /**
     * 父类的弹窗
     *
     * @param pos
     */
    private void setParentDialog(int pos) {
        new MenuDialog.Builder(getActivity())
                .setCancel(R.string.common_cancel)
                .setAutoDismiss(false)
                .setList(parentDialog)
                .setCanceledOnTouchOutside(false)
                .setListener(new MenuDialog.OnListener<String>() {
                    @Override
                    public void onSelected(BaseDialog dialog, int position, String text) {
                        dialog.dismiss();
                        if (text.equals("删除")) {                // 父类删除必须满足没有子类的情况下删除
                            new MessageDialog.Builder(getActivity())
                                    .setMessage("确定删除该分类？")
                                    .setConfirm(getString(R.string.common_confirm))
                                    .setCancel(getString(R.string.common_cancel))
                                    .setAutoDismiss(false)
                                    .setCanceledOnTouchOutside(false)
                                    .setListener(new MessageDialog.OnListener() {
                                        @Override
                                        public void onConfirm(BaseDialog dialog) {
                                            dialog.dismiss();
                                            // 点击了删除
                                            if (ListUtils.getSize(mList.get(pos).getGoodsTypeTwolist()) == 0) {
                                                // request
                                                showLoading("");
                                                RequestPost.delGoodsType(mList.get(pos).getGoodsTypeOneId(), GoodsClassifyFragment.this);
                                            } else {
                                                new MessageDialog.Builder(getActivity())
                                                        .setTitle("该分类下还有商品，不可删除")
                                                        .setMessage("您可以暂时停用该分类，停用后分类下所有商品强制下架")
                                                        .setConfirm(getString(R.string.common_disable))
                                                        .setCancel(getString(R.string.common_cancel))
                                                        .setAutoDismiss(false)
                                                        .setCanceledOnTouchOutside(false)
                                                        .setListener(new MessageDialog.OnListener() {
                                                            @Override
                                                            public void onConfirm(BaseDialog dialog) {
                                                                dialog.dismiss();
                                                                // request
                                                                showLoading("");
                                                                RequestPost.changeGoodsType(mList.get(pos).getGoodsTypeOneId(), GoodsClassifyFragment.this);
                                                            }

                                                            @Override
                                                            public void onCancel(BaseDialog dialog) {
                                                                dialog.dismiss();
                                                            }
                                                        }).show();
                                            }

                                        }

                                        @Override
                                        public void onCancel(BaseDialog dialog) {
                                            dialog.dismiss();
                                        }
                                    }).show();
                        } else if (text.equals("新增子分类")) {
                            new InputDialog.Builder(getActivity())
                                    .setTitle("新增子分类")
                                    .setHint("请输入分类名称")
                                    .setConfirm(R.string.common_confirm)
                                    .setCancel(R.string.common_cancel)
                                    .setAutoDismiss(false)               // 设置点击按钮后关不闭弹窗
                                    .setCanceledOnTouchOutside(false)
                                    .setListener(new InputDialog.OnListener() {
                                        @Override
                                        public void onConfirm(BaseDialog dialog, String content) {
                                            if (TextUtils.isEmpty(content)) {
                                                toast("请输入分类名称");
                                                return;
                                            }
                                            // request
                                            dialog.dismiss();
                                            showLoading("");
                                            RequestPost.newGoodsTypeChild("new", mList.get(pos).getGoodsTypeOneId(), content, GoodsClassifyFragment.this);
                                        }

                                        @Override
                                        public void onCancel(BaseDialog dialog) {
                                            dialog.dismiss();
                                        }
                                    }).show();
                        } else if (text.equals("重命名")) {
                            new InputDialog.Builder(getActivity())
                                    .setTitle("重命名")
                                    .setHint("请输入")
                                    .setConfirm(R.string.common_confirm)
                                    .setCancel(R.string.common_cancel)
                                    .setAutoDismiss(false)               // 设置点击按钮后关不闭弹窗
                                    .setCanceledOnTouchOutside(false)
                                    .setListener(new InputDialog.OnListener() {
                                        @Override
                                        public void onConfirm(BaseDialog dialog, String content) {
                                            if (TextUtils.isEmpty(content)) {
                                                toast("请输入分类名称");
                                                return;
                                            }
                                            // request
                                            dialog.dismiss();
                                            showLoading("");
                                            RequestPost.newGoodsTypeOne(mList.get(pos).getGoodsTypeOneId(), content, GoodsClassifyFragment.this);
                                        }

                                        @Override
                                        public void onCancel(BaseDialog dialog) {
                                            dialog.dismiss();
                                        }
                                    }).show();
                        } else {
                            new MessageDialog.Builder(getActivity())
                                    .setTitle("确定停用该分类？")
                                    .setMessage("停用后该分类下所有商品强制下架")
                                    .setConfirm(getString(R.string.common_confirm))
                                    .setCancel(getString(R.string.common_cancel))
                                    .setAutoDismiss(false)
                                    .setCanceledOnTouchOutside(false)
                                    .setListener(new MessageDialog.OnListener() {
                                        @Override
                                        public void onConfirm(BaseDialog dialog) {
                                            dialog.dismiss();
                                            // request
                                            showLoading("");
                                            RequestPost.changeGoodsType(mList.get(pos).getGoodsTypeOneId(), GoodsClassifyFragment.this);
                                        }

                                        @Override
                                        public void onCancel(BaseDialog dialog) {
                                            dialog.dismiss();
                                        }
                                    }).show();
                        }
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
        Log.d(TAG, " ======== result ======== " + result);
        Map<String, String> body = JsonParser.parseJSONObject(result.body());
        if (body != null) {
            if ("1".equals(body.get("code"))) {
                // 获取商品的一级二级分类
                if (result.url().contains("wxapi/v1/goods.php?type=getGoodsType")) {
                    mList = JsonParser.parseJSONArray(ClassifyBean.class, JsonParser.parseJSONObject(body.get("data")).get("goodsTypelist"));

                    Message msg = new Message();
                    msg.what = CLASSIFY_SIZE;
                    mHandler.sendMessage(msg);
                }

                // 新增或更新一级分类
                if (result.url().contains("wxapi/v1/goods.php?type=newGoodsTypeOne")) {
                    toast(body.get("warn"));
                    // request
                    RequestPost.getAllGoodsType(this);
                }

                // 新增或者更新二级分类
                if (result.url().contains("wxapi/v1/goods.php?type=newGoodsTypeChild")) {
                    toast(body.get("warn"));
                    // request
                    RequestPost.getAllGoodsType(this);
                }

                // 停用或启用
                if (result.url().contains("wxapi/v1/goods.php?type=changeGoodsType")){
                    toast(body.get("warn"));
                    // request
                    RequestPost.getAllGoodsType(this);
                }

                // 删除分类
                if (result.url().contains("wxapi/v1/goods.php?type=delGoodsType")){
                    toast(body.get("warn"));
                    // request
                    RequestPost.getAllGoodsType(this);
                }
            } else {
                toast(body.get("warn"));
            }
            dismissLoading();
        }
    }
}
