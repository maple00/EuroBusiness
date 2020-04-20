package com.rainwood.eurobusiness.ui.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.rainwood.eurobusiness.R;
import com.rainwood.eurobusiness.base.BaseActivity;
import com.rainwood.eurobusiness.common.Contants;
import com.rainwood.eurobusiness.domain.EditSpecialBean;
import com.rainwood.eurobusiness.domain.GoodsDetailsBean;
import com.rainwood.eurobusiness.domain.ImageBean;
import com.rainwood.eurobusiness.domain.PromotionBean;
import com.rainwood.eurobusiness.io.IOUtils;
import com.rainwood.eurobusiness.json.JsonParser;
import com.rainwood.eurobusiness.okhttp.HttpResponse;
import com.rainwood.eurobusiness.okhttp.OnHttpListener;
import com.rainwood.eurobusiness.other.BaseDialog;
import com.rainwood.eurobusiness.request.RequestPost;
import com.rainwood.eurobusiness.ui.adapter.UploadImgAdapter;
import com.rainwood.eurobusiness.ui.dialog.DateDialog;
import com.rainwood.eurobusiness.ui.dialog.MenuDialog;
import com.rainwood.eurobusiness.ui.dialog.TimeDialog;
import com.rainwood.eurobusiness.utils.CameraUtil;
import com.rainwood.eurobusiness.utils.ListUtils;
import com.rainwood.tools.permission.OnPermission;
import com.rainwood.tools.permission.Permission;
import com.rainwood.tools.permission.XXPermissions;
import com.rainwood.tools.view.ClearEditText;
import com.rainwood.tools.viewinject.ViewById;
import com.rainwood.tools.widget.MeasureGridView;
import com.rainwood.tools.widget.SwitchButton;
import com.rainwood.zxingqrc.android.QRCodeCaptureActivity;

import org.json.JSONArray;
import org.json.JSONException;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import static com.rainwood.eurobusiness.common.Contants.GOODS_REQUEST;
import static com.rainwood.eurobusiness.utils.CameraUtil.PHOTO_REQUEST_CAREMA;
import static com.rainwood.eurobusiness.utils.CameraUtil.RESULT_CAMERA_IMAGE;
import static com.rainwood.eurobusiness.utils.CameraUtil.uri_;

/**
 * @Author: a797s
 * @Date: 2020/2/8
 * @Desc: 新建商品
 */
public class NewShopActivity extends BaseActivity implements View.OnClickListener, OnHttpListener {

    private String mGoodsId;
    private GoodsDetailsBean mGoodsDetails;
    private List<EditSpecialBean> mEditSpecialList;
    private PromotionBean mPromotion;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_new_shop;
    }

    @ViewById(R.id.iv_back)
    private ImageView pageBack;
    @ViewById(R.id.tv_title)
    private TextView pageTitle;
    @ViewById(R.id.btn_save_draft)
    private Button saveDraft;
    @ViewById(R.id.btn_commit)
    private Button commit;
    // content
    @ViewById(R.id.cet_goods_name)      // 商品名称
    private ClearEditText goodsName;
    @ViewById(R.id.cet_goods_model)     // 商品型号
    private ClearEditText goodsModel;
    @ViewById(R.id.cet_goods_type)      // 商品分类
    private ClearEditText goodsType;
    @ViewById(R.id.cet_goods_codebar)       // 条码
    private ClearEditText goodsCodeBar;
    @ViewById(R.id.iv_scan)
    private ImageView scan;
    @ViewById(R.id.cet_wholesale_price)     // 批发价
    private ClearEditText salePrice;
    @ViewById(R.id.cet_retail_price)        // 零售价
    private ClearEditText retailPrice;
    @ViewById(R.id.cet_goods_special)       // 商品规格
    private ClearEditText goodsSpecial;
    @ViewById(R.id.cet_tax_rate)            // 税率
    private ClearEditText taxRate;
    @ViewById(R.id.cet_min_book)            // 最小起订量
    private ClearEditText minBook;
    @ViewById(R.id.cet_start_time)          // 开始时间
    private ClearEditText startTime;
    @ViewById(R.id.cet_end_time)            // 结束时间
    private ClearEditText endTime;
    @ViewById(R.id.cet_promotion_price)     // 促销价
    private ClearEditText promotionPrice;
    @ViewById(R.id.cet_discount)            // 折扣
    private ClearEditText discount;
    @ViewById(R.id.iv_tax_checked)          // 增值税
    private ImageView ivTaxChecked;
    @ViewById(R.id.tv_tax_checked)
    private TextView tvTaxChecked;
    @ViewById(R.id.iv_tax_unchecked)
    private ImageView ivTaxUnchecked;
    @ViewById(R.id.tv_tax_unchecked)
    private TextView tvTaxUnchecnked;
    @ViewById(R.id.iv_not_size)
    private ImageView iv_not_size;          // 商品尺码
    @ViewById(R.id.tv_not_size)
    private TextView tv_not_size;
    @ViewById(R.id.iv_has_size)
    private ImageView iv_has_size;
    @ViewById(R.id.tv_has_size)
    private TextView tv_has_size;
    @ViewById(R.id.ll_add_size)
    private LinearLayout llAddSize;         // 去添加尺码
    @ViewById(R.id.cet_add_size)
    private ClearEditText cetAddSize;
    @ViewById(R.id.mgv_goods_img)           // 商品图片
    private MeasureGridView goodsImg;
    @ViewById(R.id.sb_switch)               // 设置促销商品
    private SwitchButton sb_switch;

    private List<String> goodsFications;
    private String[] selectors = {"相机", "相册"};
    private final int GOODS_IMAGE = 0x101;
    private final int GOODS_EDIT_SIZE = 0x102;  // 商品编辑

    // 尺码选择请求码
    public static final int SIZE_REQUEST = 0x1003;

    //
    private boolean hasTax;         // 是否含税-- 默认不含税
    private boolean hasSize;        // 是否有尺码 -- 默认混装
    private String subTypeId;       // 二级商品id
    private List<File> goodsImageList = new ArrayList<>(); // 商品图片
    private List<ImageBean> mImageList = new ArrayList<>();

    @Override

    protected void initView() {
        initEvents();
        mGoodsId = getIntent().getStringExtra("goodsId");
        showLoading("");
        if (mGoodsId == null) {           // 新建商品
            pageTitle.setText("新建商品");
            // TODO:
            RequestPost.getNewGoodsPage(this);
        } else {                     // 编辑商品
            pageTitle.setText("编辑商品");
            // TODO: 获取详情页数据
            RequestPost.getGoodsDetail(mGoodsId, this);
        }
    }

    private void initEvents() {
        pageBack.setOnClickListener(this);
        saveDraft.setOnClickListener(this);
        commit.setOnClickListener(this);
        goodsType.setOnClickListener(this);
        goodsType.setFocusable(false);
        goodsType.setFocusableInTouchMode(false);
        scan.setOnClickListener(this);
        ivTaxChecked.setOnClickListener(this);
        tvTaxChecked.setOnClickListener(this);
        ivTaxUnchecked.setOnClickListener(this);
        tvTaxUnchecnked.setOnClickListener(this);
        iv_not_size.setOnClickListener(this);
        tv_not_size.setOnClickListener(this);
        iv_has_size.setOnClickListener(this);
        tv_has_size.setOnClickListener(this);
        cetAddSize.setOnClickListener(this);
        cetAddSize.setFocusableInTouchMode(false);
        cetAddSize.setFocusable(false);
        goodsSpecial.setOnClickListener(this);
        goodsSpecial.setFocusable(false);
        goodsSpecial.setFocusableInTouchMode(false);
        startTime.setOnClickListener(this);
        startTime.setFocusableInTouchMode(false);
        startTime.setFocusable(false);
        endTime.setOnClickListener(this);
        endTime.setFocusable(false);
        endTime.setFocusableInTouchMode(false);
    }

    @Override
    protected void initData() {
        Message msg = new Message();
        msg.what = GOODS_IMAGE;
        mHandler.sendMessage(msg);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_back:
                finish();
                break;
            case R.id.cet_goods_type:
                //toast("商品分类");
                //openActivity(GoodsTypeActivity.class);
                Intent intent = new Intent(NewShopActivity.this, GoodsTypeActivity.class);
                startActivityForResult(intent, GOODS_REQUEST);
                break;
            case R.id.iv_scan:
                // toast("扫一扫");
                openScan();
                break;
            case R.id.iv_tax_checked:
            case R.id.tv_tax_checked:
                hasTax = !hasTax;
                ivTaxUnchecked.setImageResource(R.drawable.radio_uncheck_shape);
                ivTaxChecked.setImageResource(R.drawable.radio_checked_shape);
                break;
            case R.id.iv_tax_unchecked:
            case R.id.tv_tax_unchecked:
                hasTax = !hasTax;
                ivTaxUnchecked.setImageResource(R.drawable.radio_checked_shape);
                ivTaxChecked.setImageResource(R.drawable.radio_uncheck_shape);
                break;
            case R.id.iv_not_size:
            case R.id.tv_not_size:
                //toast("混装");
                hasSize = !hasSize;
                llAddSize.setVisibility(View.GONE);
                iv_not_size.setImageResource(R.drawable.radio_checked_shape);
                iv_has_size.setImageResource(R.drawable.radio_uncheck_shape);
                break;
            case R.id.iv_has_size:
            case R.id.tv_has_size:
                //toast("有尺码");
                hasSize = !hasSize;
                llAddSize.setVisibility(View.VISIBLE);
                iv_not_size.setImageResource(R.drawable.radio_uncheck_shape);
                iv_has_size.setImageResource(R.drawable.radio_checked_shape);
                break;
            case R.id.cet_add_size:
                // toast("去添加尺码");
                Intent sizeIntent = new Intent(NewShopActivity.this, AppendSizeActivity.class);
                sizeIntent.putExtra("goodsId", mGoodsId);
                startActivityForResult(sizeIntent, SIZE_REQUEST);
                break;
            case R.id.cet_goods_special:
                //toast("商品规格");
                setSpecification();
                break;
            case R.id.cet_start_time:
                // toast("开始时间");
                setDateAndTime(0);
                break;
            case R.id.cet_end_time:
                // toast("结束时间");
                setDateAndTime(1);
                break;
            case R.id.btn_commit:
                //toast("提交");
                if (TextUtils.isEmpty(goodsName.getText())) {
                    toast("请填写商品名称");
                    return;
                }
                if (TextUtils.isEmpty(goodsModel.getText())) {
                    toast("请填写商品型号");
                    return;
                }
                if (TextUtils.isEmpty(goodsType.getText())) {
                    toast("请填写商品分类");
                    return;
                }
                if (TextUtils.isEmpty(salePrice.getText())) {
                    toast("请填写批发价");
                    return;
                }
                if (TextUtils.isEmpty(retailPrice.getText())) {
                    toast("请填写零售价");
                    return;
                }
                if (TextUtils.isEmpty(goodsSpecial.getText())) {
                    toast("请选择商品规格");
                    return;
                }
                if (TextUtils.isEmpty(taxRate.getText())) {
                    toast("请填写税率");
                    return;
                }
                if (TextUtils.isEmpty(minBook.getText())) {
                    toast("请填写最小起订量");
                    return;
                }
                if (TextUtils.isEmpty(startTime.getText())) {
                    toast("请填写开始时间");
                    return;
                }
                if (TextUtils.isEmpty(endTime.getText())) {
                    toast("请填写结束时间");
                    return;
                }
                if (TextUtils.isEmpty(promotionPrice.getText())) {
                    toast("请填写促销价");
                    return;
                }
                if (TextUtils.isEmpty(discount.getText())) {
                    toast("请填写折扣价");
                    return;
                }
                // request
                showLoading("");
                RequestPost.goodsInfoEdit(mGoodsId, "commit", goodsName.getText().toString().trim(), subTypeId, goodsModel.getText().toString().trim(),
                        goodsCodeBar.getText().toString().trim(), goodsSpecial.getText().toString().trim(), hasSize ? "有尺码" : "混装", salePrice.getText().toString().trim(),
                        retailPrice.getText().toString().trim(), minBook.getText().toString().trim(), hasTax ? "1" : "0", sb_switch.isChecked() ? "是" : "否",
                        startTime.getText().toString().trim(), endTime.getText().toString().trim(), promotionPrice.getText().toString().trim(),
                        discount.getText().toString().trim(), goodsImageList, this);
                break;
            case R.id.btn_save_draft:
                // toast("保存为草稿");
                if (TextUtils.isEmpty(goodsName.getText())) {
                    toast("请填写商品名称");
                    return;
                }
                // request
                showLoading("");
                RequestPost.goodsInfoEdit(mGoodsId, "save", goodsName.getText().toString().trim(), subTypeId, goodsModel.getText().toString().trim(),
                        goodsCodeBar.getText().toString().trim(), goodsSpecial.getText().toString().trim(), hasSize ? "有尺码" : "混装", salePrice.getText().toString().trim(),
                        retailPrice.getText().toString().trim(), minBook.getText().toString().trim(), hasTax ? "1" : "0", sb_switch.isChecked() ? "是" : "否",
                        startTime.getText().toString().trim(), endTime.getText().toString().trim(), promotionPrice.getText().toString().trim(),
                        discount.getText().toString().trim(), goodsImageList, this);
                break;
        }
    }

    @SuppressLint("HandlerLeak")
    private Handler mHandler = new Handler() {
        @SuppressLint("SetTextI18n")
        @Override
        public void handleMessage(@NonNull Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case GOODS_IMAGE:
                    // 默认的第一项
                    if (ListUtils.getSize(mImageList) == 0) {
                        ImageBean image = new ImageBean();
                        image.setHasAdd(true);
                        image.setPath("");
                        mImageList.add(image);
                    }
                    UploadImgAdapter imgAdapter = new UploadImgAdapter(NewShopActivity.this, mImageList);
                    goodsImg.setAdapter(imgAdapter);
                    goodsImg.setNumColumns(4);
                    imgAdapter.setOnClickImage(() -> {
                        //toast("添加图片");
                        imageSelector();
                    });
                    break;
                case GOODS_EDIT_SIZE:
                    Log.d(TAG, "" + mGoodsDetails);
                    // setValue
                    goodsName.setText(mGoodsDetails.getName());
                    goodsModel.setText(mGoodsDetails.getModel());
                    goodsModel.setText(mGoodsDetails.getGoodsTypeOne() + "-" + mGoodsDetails.getGoodsTypeTwo());
                    goodsCodeBar.setText(mGoodsDetails.getBarCode());
                    salePrice.setText(mGoodsDetails.getTradePrice());
                    retailPrice.setText(mGoodsDetails.getRetailPrice());
                    goodsSpecial.setText(mGoodsDetails.getGoodsUnit());
                    taxRate.setText(mGoodsDetails.getTaxRate());
                    minBook.setText(mGoodsDetails.getStartNum());
                    // 是否含税
                    if ("1".equals(mGoodsDetails.getIsTax())){      // 含有增值税
                        ivTaxChecked.setImageResource(R.drawable.radio_checked_shape);
                        ivTaxUnchecked.setImageResource(R.drawable.radio_uncheck_shape);
                    }else {
                        ivTaxChecked.setImageResource(R.drawable.radio_uncheck_shape);
                        ivTaxUnchecked.setImageResource(R.drawable.radio_checked_shape);
                    }
                    // 商品尺码
                    if (ListUtils.getSize(mEditSpecialList) == 0){      // 没有尺码
                        hasSize = !hasSize;
                        llAddSize.setVisibility(View.GONE);
                        iv_not_size.setImageResource(R.drawable.radio_checked_shape);
                        iv_has_size.setImageResource(R.drawable.radio_uncheck_shape);
                    }else {                                             // 有尺码
                        hasSize = !hasSize;
                        llAddSize.setVisibility(View.VISIBLE);
                        iv_not_size.setImageResource(R.drawable.radio_uncheck_shape);
                        iv_has_size.setImageResource(R.drawable.radio_checked_shape);
                        cetAddSize.setText("共添加了" + ListUtils.getSize(mEditSpecialList) + "种规格");
                    }
                    // 图片列表
                    Message imgMsg = new Message();
                    imgMsg.what = GOODS_IMAGE;
                    mHandler.sendMessage(imgMsg);
                    // 促销信息
                    if ("是".equals(mPromotion.getState())) {
                        sb_switch.setChecked(true);
                    }else {
                        sb_switch.setChecked(false);
                    }
                    startTime.setText(mPromotion.getStartTime());
                    endTime.setText(mPromotion.getEndTime());
                    promotionPrice.setText(mPromotion.getPrice());
                    discount.setText(mPromotion.getDiscount());
                    break;
            }
        }
    };

    // 设置商品规格
    private void setSpecification() {
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
                        dialog.dismiss();
                        goodsSpecial.setText(text);
                    }

                    @Override
                    public void onCancel(BaseDialog dialog) {
                        dialog.dismiss();
                    }
                }).show();
    }

    // 设置 日期和时间
    private void setDateAndTime(int flag) {
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
                        setTime(flag, year, month, day);
                    }

                    @Override
                    public void onCancel(BaseDialog dialog) {
                        dialog.dismiss();
                    }
                }).show();
    }

    // 设置时间
    private void setTime(int flag, int year, int month, int day) {
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
                        String dateTime = year + "-" + (month < 10 ? "0" + month : month) + "-"
                                + (day < 10 ? "0" + day : day) + " " + hour + ":" + minute;
                        // toast(dateTime);
                        if (flag == 0) {     // 开始时间
                            startTime.setText(dateTime);
                        }
                        if (flag == 1) {     // 结束时间
                            endTime.setText(dateTime);
                        }
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

    // 添加商品图片
    private void imageSelector() {
        List<String> data = new ArrayList<>(Arrays.asList(selectors));
        // 先权限检查
        XXPermissions.with(getActivity())
                .constantRequest()
                .permission(Permission.Group.STORAGE)       // 读写权限
                .permission(Permission.CAMERA)              // 相机权限
                .request(new OnPermission() {
                    @Override
                    public void hasPermission(List<String> granted, boolean isAll) {
                        if (isAll) {
                            new MenuDialog.Builder(getActivity())
                                    // 设置null 表示不显示取消按钮
                                    .setCancel(R.string.common_cancel)
                                    // 设置点击按钮后不关闭弹窗
                                    .setAutoDismiss(false)
                                    // 显示的数据
                                    .setList(data)
                                    .setCanceledOnTouchOutside(false)
                                    .setListener(new MenuDialog.OnListener<String>() {
                                        @Override
                                        public void onSelected(BaseDialog dialog, int position, String text) {
                                            dialog.dismiss();
                                            switch (position) {
                                                case 0:         // 拍照
                                                    // toast("相机");
                                                    CameraUtil.openCamera(NewShopActivity.this);
                                                    break;
                                                case 1:         // 相册
                                                    // toast("相册");
                                                    gallery();
                                                    break;
                                            }
                                        }

                                        @Override
                                        public void onCancel(BaseDialog dialog) {
                                            dialog.dismiss();
                                        }
                                    }).show();
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

    /**
     * 从相册获取
     */
    public void gallery() {
        // 激活系统图库，选择一张图片
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        // 开启一个带有返回值的Activity，请求码为PHOTO_REQUEST_GALLERY
        startActivityForResult(intent, RESULT_CAMERA_IMAGE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case PHOTO_REQUEST_CAREMA:
                    // toast("相机");
                    showImage(uri_);
                    break;
                case RESULT_CAMERA_IMAGE:
                    // 得到图片的全路径
                    // toast("相册");
                    Uri uri = data.getData();
                    showImage(uri);
                    break;

            }
        }
        if (resultCode == GOODS_REQUEST) {
            switch (requestCode) {
                case GOODS_REQUEST:             // 商品分类
                    goodsType.setText(data.getStringExtra("goodsType"));
                    subTypeId = data.getStringExtra("subTypeId");
                    break;
            }
        }
        if (requestCode == SIZE_REQUEST && resultCode == SIZE_REQUEST) {
            cetAddSize.setText(data.getStringExtra("sizeValue"));
        }
        // 获取识别的内容
        if (requestCode == Contants.SCANCHECKCODE && resultCode == RESULT_OK) {
            Bundle bundle = data.getExtras();
            String result = null;
            if (bundle != null) {
                result = bundle.getString(QRCodeCaptureActivity.CODE_CONTENT);
            }
            if (TextUtils.isEmpty(result)) {
                toast("未识别到任何内容");
                return;
            }
            goodsCodeBar.setText(result);
        }
    }

    /**
     * 商品图片
     *
     * @param uri
     */
    private void showImage(Uri uri) {
        String path = CameraUtil.getPath(this, uri);
        // 添加图片到后台
        File file = IOUtils.decodeUri(this, uri);

        goodsImageList.add(file);
        ImageBean image = new ImageBean();
        image.setPath(file.getAbsolutePath());
        image.setHasAdd(false);
        mImageList.add(image);

        Message msg = new Message();
        msg.what = GOODS_IMAGE;
        mHandler.sendMessage(msg);
    }

    @Override
    public void onHttpFailure(HttpResponse result) {

    }

    @Override
    public void onHttpSucceed(HttpResponse result) {
        Map<String, String> body = JsonParser.parseJSONObject(result.body());
        if (body != null) {
            if ("1".equals(body.get("code"))) {
                // 新建商品页面
                if (result.url().contains("wxapi/v1/goods.php?type=getNewGoodsPage")) {
                    JSONArray option = JsonParser.parseJSONArrayString(JsonParser.parseJSONObject(body.get("data")).get("unitOption"));
                    goodsFications = new ArrayList<>();
                    for (int i = 0; i < option.length(); i++) {
                        try {
                            goodsFications.add(option.getString(i));
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                    mGoodsId = JsonParser.parseJSONObject(body.get("data")).get("goodsId");
                }
                // 新增或更新商品
                if (result.url().contains("wxapi/v1/goods.php?type=goodsInfoEdit")) {
                    // Log.d(TAG, " ======== " + body.get("data"));
                    toast(body.get("warn"));
                    postDelayed(this::finish, 1000);
                }

                // 商品详情数据 -- 商品数据编辑
                if (result.url().contains("wxapi/v1/goods.php?type=getGoodsInfo")){
                    mGoodsDetails = JsonParser.parseJSONObject(GoodsDetailsBean.class, JsonParser.parseJSONObject(body.get("data")).get("goodsInfo"));
                    // 规格列表
                    mEditSpecialList = JsonParser.parseJSONArray(EditSpecialBean.class, JsonParser.parseJSONObject(body.get("data")).get("goodsSkulist"));
                    // 图片列表
                    List<ImageBean> imageList = JsonParser.parseJSONArray(ImageBean.class, JsonParser.parseJSONObject(body.get("data")).get("goodsWinlist"));
                    // mImageList
                    if (ListUtils.getSize(mImageList) == 0) {
                        ImageBean image = new ImageBean();
                        image.setHasAdd(true);
                        image.setPath("");
                        mImageList.add(image);
                    }
                    if (imageList != null) {
                        mImageList.addAll(imageList);
                    }
                    // 促销信息
                    mPromotion = JsonParser.parseJSONObject(PromotionBean.class, JsonParser.parseJSONObject(body.get("data")).get("goodsPromotion"));

                    Message msg = new Message();
                    msg.what = GOODS_EDIT_SIZE;
                    mHandler.sendMessage(msg);
                }
            } else {
                toast(body.get("warn"));
            }
            dismissLoading();
        }
    }
}
