package com.rainwood.eurobusiness.ui.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.rainwood.eurobusiness.R;
import com.rainwood.eurobusiness.base.BaseActivity;
import com.rainwood.eurobusiness.common.Contants;
import com.rainwood.eurobusiness.domain.CommImgBean;
import com.rainwood.eurobusiness.domain.CommonUIBean;
import com.rainwood.eurobusiness.domain.NewShopBean;
import com.rainwood.eurobusiness.other.BaseDialog;
import com.rainwood.eurobusiness.ui.adapter.ItemImgGridAdapter;
import com.rainwood.eurobusiness.ui.adapter.ItemNewShopBaseAdapter;
import com.rainwood.eurobusiness.ui.adapter.NewShopAdapter;
import com.rainwood.eurobusiness.ui.adapter.ShopParamsAdapter;
import com.rainwood.eurobusiness.ui.dialog.DateDialog;
import com.rainwood.eurobusiness.ui.dialog.MenuDialog;
import com.rainwood.eurobusiness.ui.dialog.TimeDialog;
import com.rainwood.eurobusiness.utils.CameraAlbumUtils;
import com.rainwood.tools.permission.OnPermission;
import com.rainwood.tools.permission.Permission;
import com.rainwood.tools.permission.XXPermissions;
import com.rainwood.tools.viewinject.ViewById;
import com.rainwood.tools.widget.MeasureListView;
import com.rainwood.zxingqrc.android.QRCodeCaptureActivity;

import java.io.File;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * @Author: a797s
 * @Date: 2020/2/8
 * @Desc:
 */
public class NewShopActivity extends BaseActivity implements View.OnClickListener {

    @Override
    protected int getLayoutId() {
        return R.layout.activity_new_shop;
    }

    @ViewById(R.id.iv_back)
    private ImageView pageBack;
    @ViewById(R.id.tv_title)
    private TextView pageTitle;
    @ViewById(R.id.lv_shop_params)
    private MeasureListView shopParams;
    @ViewById(R.id.btn_save_draft)
    private Button saveDraft;
    @ViewById(R.id.btn_commit)
    private Button commit;

    // 图片列表
    List<CommImgBean> imgList = new ArrayList<>();

    @Override
    protected void initView() {
        pageBack.setOnClickListener(this);
        pageTitle.setText("新建商品");
        saveDraft.setOnClickListener(this);
        commit.setOnClickListener(this);

        // 初始化UI
        Message msg = new Message();
        msg.what = 1124;
        mHandler.sendMessage(msg);
    }

    @Override
    protected void initData() {
        mList = new ArrayList<>();
        for (int i = 0; i < moduleNames.length; i++) {
            NewShopBean newShop = new NewShopBean();
            newShop.setTitle(moduleNames[i]);
            newShop.setType(i);
            List<CommonUIBean> moduleList = new ArrayList<>();
            if (i == 0) {        // 基本信息
                for (int j = 0; j < baseInfos.length; j++) {
                    CommonUIBean commonUI = new CommonUIBean();
                    commonUI.setTitle(baseInfos[j]);
                    commonUI.setLabel(baseLabels[j]);
                    moduleList.add(commonUI);
                }
            } else if (i == 1) {      // 商品定价
                for (int j = 0; j < priceNames.length; j++) {
                    CommonUIBean commonUI = new CommonUIBean();
                    commonUI.setTitle(priceNames[j]);
                    commonUI.setLabel(priceLabels[j]);
                    moduleList.add(commonUI);
                }
            } else if (i == 2) {      // 规格参数
                for (int j = 0; j < paramsNames.length; j++) {
                    CommonUIBean commonUI = new CommonUIBean();
                    commonUI.setTitle(paramsNames[j]);
                    commonUI.setLabel(paramsLabels[j]);
                    moduleList.add(commonUI);
                }
            } else if (i == 3) {      // 商品图片
                moduleList.add(null);
                imgList.add(null);
                newShop.setImgList(imgList);
            } else {                 // 促销信息
                for (int j = 0; j < promotionNames.length; j++) {
                    CommonUIBean commonUI = new CommonUIBean();
                    commonUI.setTitle(promotionNames[j]);
                    commonUI.setLabel(promotionLabels[j]);
                    moduleList.add(commonUI);
                }
            }
            newShop.setInfosList(moduleList);
            mList.add(newShop);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_back:
                finish();
                break;
            case R.id.btn_save_draft:
                toast("保存为草稿");
                break;
            case R.id.btn_commit:
                toast("提交");
                break;
        }
    }


    /**
     * 申请权限
     */
    private void getPermission() {
        // 先申请权限
        XXPermissions.with(getActivity())
                // 可设置被拒绝后继续申请，直到用户授权或者永久拒绝
                .constantRequest()
                // 不指定权限则自动获取清单中的危险权限
                .permission(Permission.CAMERA)
                .request(new OnPermission() {
                    @Override
                    public void hasPermission(List<String> granted, boolean isAll) {
                        if (isAll) {
                            // 底部选择框
                            getSelectButton();
                            // toast("获取权限成功");
                        } else {
                            toast("获取权限成功，部分权限未正常授予");
                        }
                    }

                    @Override
                    public void noPermission(List<String> denied, boolean quick) {
                        if (quick) {
                            toast("被永久拒绝授权，请手动授予权限");
                            //如果是被永久拒绝就跳转到应用权限系统设置页面
                            XXPermissions.gotoPermissionSettings(getActivity());
                        } else {
                            toast("获取权限失败");
                        }
                    }
                });
    }

    // 文件打开地址
    File imgPath = new File(Environment.getExternalStorageDirectory().getPath()
            + File.separator + "/temp.jpg");

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case CameraAlbumUtils.OPEN_CAMERA_SIZE:
                    toast("相机");
                    break;
                case CameraAlbumUtils.OPEN_ALBUM_SIZE:
                    String realPath = CameraAlbumUtils.getRealPathFromUri(this, data.getData());
                    showImg(realPath);
                    break;
            }
        }
    }

    // 显示图片
    private void showImg(String imgPath) {
        CommImgBean imgBean = new CommImgBean();
        imgBean.setImgPath(imgPath);
        imgList.add(imgBean);

        // 更新局部UI
        Message msg = new Message();
        msg.what = 1124;
        mHandler.sendMessage(msg);

    }

    @SuppressLint("HandlerLeak")
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(@NonNull Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 1124:
                    NewShopAdapter shopAdapter = new NewShopAdapter(NewShopActivity.this, mList);
                    shopParams.setAdapter(shopAdapter);
                    // 基本信息的点击事件
                    shopAdapter.setBaseListener(new ItemNewShopBaseAdapter.OnItemLabelListener() {

                        @Override
                        public void onClickShopType(int parentPosition, int position) {
                            // 选择分类
                            openActivity(GoodsTypeActivity.class);
                        }

                        @Override
                        public void onClickScan() {
                            // 打开扫一扫
                            openScan();
                        }
                    });

                    // 选择商品规格
                    shopAdapter.setParamsListener(new ShopParamsAdapter.OnClickParamsListener() {
                        @Override
                        public void onClickParams(int parentPosition, int position) {
                            setSpecification(parentPosition, position);
                        }

                        @Override
                        public void onClickSize() {
                            openActivity(AppendSizeActivity.class);
                        }
                    });

                    // 添加图片
                    shopAdapter.setOnImgListener(new ItemImgGridAdapter.OnImgListener() {
                        @Override
                        public void onClickDelete(int position) {
                            imgList.remove(position);
                            postDelayed(() -> toast("删除成功"), 500);
                        }

                        @Override
                        public void onClickAdd() {
                            getPermission();
                        }
                    });

                    // 添加时间控件
                    shopAdapter.setDateListener((parentPosition, position) -> {
                        // toast("点击了： " + mList.get(parentPosition).getInfosList().get(position).getTitle());
                        setDateAndTime(parentPosition, position);
                    });
                    break;
            }
        }
    };

    // 设置商品规格
    private void setSpecification(int parentPosition, int subPosition) {
        new MenuDialog.Builder(getActivity())
                // 设置null 表示不显示取消按钮
                .setCancel(R.string.common_cancel)
                // 设置点击按钮后不关闭弹窗
                .setAutoDismiss(false)
                // 显示的数据
                .setList(goodsFications)
                .setCanceledOnTouchOutside(false)
                .setListener(new MenuDialog.OnListener<String>() {
                    @Override
                    public void onSelected(BaseDialog dialog, int position, String text) {
                        // toast("商品规格: ---- " + text);
                        mList.get(parentPosition).getInfosList().get(subPosition).setShowText(text);
                        dialog.dismiss();

                        // 更新局部UI
                        Message msg = new Message();
                        msg.what = 1124;
                        mHandler.sendMessage(msg);
                    }

                    @Override
                    public void onCancel(BaseDialog dialog) {
                        dialog.dismiss();
                    }
                }).show();
    }

    // 添加图片
    private void getSelectButton() {
        new MenuDialog.Builder(getActivity())
                // 设置null 表示不显示取消按钮
                .setCancel(R.string.common_cancel)
                // 设置点击按钮后不关闭弹窗
                .setAutoDismiss(false)
                // 显示的数据
                .setList(addPictures)
                //.setCanceledOnTouchOutside(false)
                .setListener(new MenuDialog.OnListener<String>() {
                    @Override
                    public void onSelected(BaseDialog dialog, int position, String text) {
                        if (position == 0) {     // 相机, 6.0, 7.0 , 8.0
//                            CameraAlbumUtils.openCamera(NewShopActivity.this,
//                                    CameraAlbumUtils.getSaveUriAll(NewShopActivity.this, imgPath), CameraAlbumUtils.OPEN_CAMERA_SIZE);
                        }
                        if (position == 1) {     // 相册
                            // CameraAlbumUtils.openAlbum(NewShopActivity.this, CameraAlbumUtils.OPEN_ALBUM_SIZE);
                        }
                        dialog.dismiss();
                    }

                    //
                    @Override
                    public void onCancel(BaseDialog dialog) {
                        dialog.dismiss();
                    }
                }).show();
    }

    // 设置 日期和时间
    private void setDateAndTime(int parentPosition, int position) {
        // 选择日期
        new DateDialog.Builder(this)
                .setTitle(getString(R.string.date_title))
                // 确定文本
                .setConfirm(getString(R.string.common_confirm))
                // 设置为null 时表示不显示取消按钮
                .setCancel(getString(R.string.common_cancel))
                // 不选择天数
                //.setIgnoreDay()
                .setListener(new DateDialog.OnListener() {
                    @Override
                    public void onSelected(BaseDialog dialog, int year, int month, int day) {
                        // toast(year + "-" + "-" + month + "-" + day);
                        // 设置时间、忽略秒
                        setTime(parentPosition, position, year, month, day);
                    }

                    @Override
                    public void onCancel(BaseDialog dialog) {
                        dialog.dismiss();
                    }
                }).show();
    }

    // 设置时间
    private void setTime(int parentPosition, int position, int year, int month, int day) {
        // 时间选择对话框
        new TimeDialog.Builder(this)
                .setTitle(getString(R.string.time_title))
                // 确定文本按钮
                .setConfirm(getString(R.string.common_confirm))
                // 设置 为 null 的时候表示不显示取消按钮
                .setCancel(getString(R.string.common_cancel))
                // 不选择秒数
                .setIgnoreSecond()
                .setListener(new TimeDialog.OnListener() {
                    @Override
                    public void onSelected(BaseDialog dialog, int hour, int minute, int second) {
                        // toast(year + "年" + month + "月"+ day + "日\u3000" + hour + ":" + minute);
                        String dateTime = year + "年" + month + "月" + day + "日\u3000" + hour + ":" + minute;
                        mList.get(parentPosition).getInfosList().get(position).setShowText(dateTime);
                        // toast(dateTime);
                        Message msg = new Message();
                        msg.what = 1124;
                        mHandler.sendMessage(msg);
                    }

                    @Override
                    public void onCancel(BaseDialog dialog) {
                        dialog.dismiss();
                    }
                }).show();
    }

    // 打开扫一扫
    private void openScan() {
        XXPermissions.with(getActivity())
                // 可设置被拒绝后继续申请，直到用户授权或永久拒绝
                .constantRequest()
                // 不指定权限则自定获取订单中的危险权限
                .permission(Permission.CAMERA)
                .request(new OnPermission() {
                    @Override
                    public void hasPermission(List<String> granted, boolean isAll) {
                        if (isAll) {
                            // 去扫码
                            Intent intent = new Intent(NewShopActivity.this, QRCodeCaptureActivity.class);
                            // 设置标题栏的颜色
                            intent.putExtra(QRCodeCaptureActivity.STATUS_BAR_COLOR, Color.parseColor("#99000000"));
                            startActivityForResult(intent, Contants.SCANCHECKCODE);
                        } else {
                            toast("获取权限成功，部分权限未正常授予");
                        }
                    }

                    @Override
                    public void noPermission(List<String> denied, boolean quick) {
                        if (quick) {
                            toast("被永久拒绝授权，请手动授予权限");
                            //如果是被永久拒绝就跳转到应用权限系统设置页面
                            XXPermissions.gotoPermissionSettings(getActivity());
                        } else {
                            toast("获取权限失败");
                        }
                    }
                });
    }


    /*
    模拟数据
    */
    private List<NewShopBean> mList;
    private String[] moduleNames = {"基本信息", "商品定价", "规格参数", "商品图片", "促销信息"};
    // 基本信息
    private String[] baseInfos = {"商品名称", "商品型号", "商品分类", "条码"};
    private String[] baseLabels = {"请输入", "请输入", "请选择", "请输入"};
    // 商品定价
    private String[] priceNames = {"批发价", "零售价", "增值税"};
    private String[] priceLabels = {"请输入", "请输入", ""};
    // 规格参数
    private String[] paramsNames = {"商品尺码", "库存", "库存下限", "商品规格", "税率", "最小起订量"};
    private String[] paramsLabels = {"", "请输入", "请输入", "请选择", "请输入", "请输入"};
    //商品图片   GridView
    // 促销信息
    private String[] promotionNames = {"设为促销商品", "开始时间", "结束时间", "批发价", "促销价", "折扣"};
    private String[] promotionLabels = {"是", "请选择", "请选择", "请输入", "请输入", "请输入"};

    private String[] addPictures = {"相机", "相册"};

    private String[] goodsFications = {"箱", "包", "件", "其他"};
}
