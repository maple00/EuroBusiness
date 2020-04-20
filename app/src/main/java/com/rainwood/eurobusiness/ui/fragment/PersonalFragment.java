package com.rainwood.eurobusiness.ui.fragment;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CircleCrop;
import com.bumptech.glide.request.RequestOptions;
import com.rainwood.eurobusiness.R;
import com.rainwood.eurobusiness.base.BaseFragment;
import com.rainwood.eurobusiness.common.Contants;
import com.rainwood.eurobusiness.domain.PersonalListBean;
import com.rainwood.eurobusiness.io.IOUtils;
import com.rainwood.eurobusiness.json.JsonParser;
import com.rainwood.eurobusiness.okhttp.HttpResponse;
import com.rainwood.eurobusiness.okhttp.OnHttpListener;
import com.rainwood.eurobusiness.other.BaseDialog;
import com.rainwood.eurobusiness.request.RequestPost;
import com.rainwood.eurobusiness.ui.activity.FeedBackActivity;
import com.rainwood.eurobusiness.ui.activity.HelperActivity;
import com.rainwood.eurobusiness.ui.activity.IdentityActivity;
import com.rainwood.eurobusiness.ui.activity.InvoiceActivity;
import com.rainwood.eurobusiness.ui.activity.MessageActivity;
import com.rainwood.eurobusiness.ui.activity.ModifyPwdActivity;
import com.rainwood.eurobusiness.ui.activity.StoresActivity;
import com.rainwood.eurobusiness.ui.adapter.PersonaListAdapter;
import com.rainwood.eurobusiness.ui.dialog.InputDialog;
import com.rainwood.eurobusiness.ui.dialog.MenuDialog;
import com.rainwood.eurobusiness.ui.dialog.MessageDialog;
import com.rainwood.eurobusiness.ui.dialog.UpdateDialog;
import com.rainwood.eurobusiness.utils.CacheManagerUtils;
import com.rainwood.eurobusiness.utils.CameraAlbumUtils;
import com.rainwood.eurobusiness.utils.CameraUtil;
import com.rainwood.tools.permission.OnPermission;
import com.rainwood.tools.permission.Permission;
import com.rainwood.tools.permission.XXPermissions;
import com.rainwood.tools.statusbar.StatusBarUtil;
import com.rainwood.tools.widget.MeasureListView;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import static android.app.Activity.RESULT_OK;
import static com.rainwood.eurobusiness.utils.CameraUtil.PHOTO_REQUEST_CAREMA;
import static com.rainwood.eurobusiness.utils.CameraUtil.RESULT_CAMERA_IMAGE;
import static com.rainwood.eurobusiness.utils.CameraUtil.uri_;

/**
 * @Author: a797s
 * @Date: 2020/2/7
 * @Desc: 个人中心
 */
public class PersonalFragment extends BaseFragment implements View.OnClickListener, OnHttpListener {

    private Map<String, String> mCustomInfo;
    private TextView mMsgDesc;

    @Override
    protected int initLayout() {
        return R.layout.fragment_personal;
    }

    private MeasureListView personalList;
    private ImageView edit, headPhoto;
    private TextView userName, location;
    private Button logout;

    private List<PersonalListBean> mList;

    private String[] moduleName = {"门店信息", "开票信息", "账号设置", "帮助中心", "意见反馈",
            "清理缓存", "版本更新"};
    private int[] moduleIcon = {R.drawable.ic_icon_me_store, R.drawable.ic_icon_me_invoice,
            R.drawable.ic_icon_me_set_up, R.drawable.ic_icon_me_help,
            R.drawable.ic_icon_me_feedback, R.drawable.ic_icon_me_clear,
            R.drawable.ic_icon_me_version_update};
    private String[] addPictures = {"相机", "相册"};

    private final int INITIAL_SIZE = 0x101;

    @Override
    protected void initView(View view) {
        // 消息
        FrameLayout msg = view.findViewById(R.id.fl_message);
        msg.setOnClickListener(this);
        mMsgDesc = view.findViewById(R.id.tv_desc);
        mMsgDesc.setVisibility(View.GONE);
        // 个人信息
        headPhoto = view.findViewById(R.id.iv_head_photo);
        headPhoto.setOnClickListener(this);
        userName = view.findViewById(R.id.tv_user_name);
        location = view.findViewById(R.id.tv_location);     // 门店位置
        edit = view.findViewById(R.id.iv_edit);             // 编辑个人信息
        edit.setOnClickListener(this);
        TextView callPolice = view.findViewById(R.id.tv_call_police);
        if (Contants.userType == 0) {        // 批发商
            StatusBarUtil.setStatusBarDarkTheme(getActivity(), false);
            callPolice.setVisibility(View.VISIBLE);
            callPolice.setText("一键报警");
            callPolice.setBackground(getContext().getResources().getDrawable(R.drawable.shape_radius_red_full_14));
            callPolice.setOnClickListener(v -> new InputDialog.Builder(getActivity())
                    .setTitle("确认后报警生效，请谨慎操作！")
                    .setHint("请输入您的密码")
                    .setConfirm(R.string.personal_confirm_call)
                    .setCancel(R.string.common_cancel)
                    .setAutoDismiss(false)               // 设置点击按钮后关不闭弹窗
                    .setCanceledOnTouchOutside(false)
                    .setListener(new InputDialog.OnListener() {
                        @Override
                        public void onConfirm(BaseDialog dialog, String content) {
                            if (TextUtils.isEmpty(content)) {
                                toast("请输入密码");
                                return;
                            }
                            dialog.dismiss();
                            //  request
                            showLoading("");
                            RequestPost.callPlat(content, PersonalFragment.this);
                            callPolice.setClickable(false);
                            callPolice.setText("正在处理报警");
                            callPolice.setBackground(getContext().getResources().getDrawable(R.drawable.shape_radius_gray_white_14));
                        }

                        @Override
                        public void onCancel(BaseDialog dialog) {
                            dialog.dismiss();
                        }
                    }).show());

        }
        // 退出登陆
        logout = view.findViewById(R.id.btn_logout);
        logout.setOnClickListener(this);
        // 赋值
        userName.setText("");
        location.setText("");
        // 圆形头像
        Glide.with(view)
                .load(R.drawable.icon_logo_2x)
                .apply(RequestOptions.bitmapTransform(new CircleCrop()).circleCrop())
                .into(headPhoto);
        // 个人中心列表
        personalList = view.findViewById(R.id.lv_personal);
        PersonaListAdapter adapter = new PersonaListAdapter(getActivity(), mList);
        personalList.setAdapter(adapter);
        adapter.setItemClick(position -> {
            switch (position) {
                case 0:         // 门店信息
                    startActivity(StoresActivity.class);
                    break;
                case 1:         // 开票信息
                    startActivity(InvoiceActivity.class);
                    break;
                case 2:         // 账号设置
                    startActivity(ModifyPwdActivity.class);
                    break;
                case 3:         // 帮助中心
                    startActivity(HelperActivity.class);
                    break;
                case 4:         // 意见反馈
                    startActivity(FeedBackActivity.class);
                    break;
                case 5:         // 清理缓存
                    new MessageDialog.Builder(getActivity())
                            .setMessage("是否清理缓存？")
                            .setConfirm(getString(R.string.common_confirm))
                            .setCancel(getString(R.string.common_cancel))
                            .setAutoDismiss(false)
                            .setListener(new MessageDialog.OnListener() {
                                @Override
                                public void onConfirm(BaseDialog dialog) {
                                    CacheManagerUtils.clearAllCache(Objects.requireNonNull(getContext()));
                                    PersonaListAdapter adapter = new PersonaListAdapter(getActivity(), mList);
                                    personalList.setAdapter(adapter);
                                    dialog.dismiss();
                                }

                                @Override
                                public void onCancel(BaseDialog dialog) {
                                    dialog.dismiss();
                                }
                            }).show();
                    break;
                case 6:         // 版本更新
                    String updateUrl = "aaa";
                    // 升级更新对话框
                    new UpdateDialog.Builder(getActivity())
                            // 版本名
                            .setVersionName("v 2.0")
                            // 文件大小
                            .setFileSize("10 M")
                            // 是否强制更新
                            .setForceUpdate(false)
                            // 更新日志
                            .setUpdateLog("到底更新了啥\n更新了啥\n更新了啥\n更新了啥")
                            // 下载地址
                            .setDownloadUrl(updateUrl)
                            .show();
                    break;
                default:
                    break;
            }
        });
    }

    @Override
    protected void initData(Context mContext) {
        // 个人中心列表
        mList = new ArrayList<>();
        for (int i = 0; i < moduleName.length; i++) {
            PersonalListBean personal = new PersonalListBean();
            personal.setImgPath(moduleIcon[i]);
            personal.setName(moduleName[i]);
            mList.add(personal);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        // request
        showLoading("loading");
        RequestPost.getPersonalInfo(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_head_photo:        // 头像更改
                XXPermissions.with(getActivity())
                        .constantRequest()
                        .permission(Permission.CAMERA)
                        .request(new OnPermission() {
                            @Override
                            public void hasPermission(List<String> granted, boolean isAll) {
                                if (isAll) {
                                    getSelectButton();
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
                break;
            case R.id.iv_edit:               // 修改昵称--- 此处可优化成用户输入的时候就限制，省去输入完成之后再更改的必要
                new InputDialog.Builder(getActivity())
                        .setTitle("修改昵称")
                        .setHint("请输入2-8位昵称")
                        .setConfirm(R.string.common_confirm)
                        .setCancel(R.string.common_cancel)
                        .setAutoDismiss(false)               // 设置点击按钮后关不闭弹窗
                        .setCanceledOnTouchOutside(false)
                        .setListener(new InputDialog.OnListener() {
                            @Override
                            public void onConfirm(BaseDialog dialog, String content) {
                                if (content.length() < 2 || content.length() > 8) {      // 输入限制
                                    toast("请输入2-8位昵称");
                                } else {
                                    postDelayed(() -> {
                                        dialog.dismiss();
                                        userName.setText(content);
                                        // TODO:
                                        RequestPost.changeNickName(content, PersonalFragment.this);
                                    }, 500);
                                }
                            }

                            @Override
                            public void onCancel(BaseDialog dialog) {
                                dialog.dismiss();
                            }
                        }).show();
                break;
            case R.id.fl_message:               // 消息
                // toast("消息列表");
                startActivity(MessageActivity.class);
                break;
            case R.id.btn_logout:           // 退出登陆
                new MessageDialog.Builder(getActivity())
                        .setMessage("确认退出登陆？")
                        .setConfirm(getString(R.string.common_confirm))
                        .setCancel(getString(R.string.common_cancel))
                        .setAutoDismiss(false)
                        .setCanceledOnTouchOutside(false)
                        .setListener(new MessageDialog.OnListener() {
                            @Override
                            public void onConfirm(BaseDialog dialog) {
                                // TODO
                                showLoading("");
                                RequestPost.loginOut(PersonalFragment.this);
                            }

                            @Override
                            public void onCancel(BaseDialog dialog) {
                                dialog.dismiss();
                            }
                        }).show();
                break;
            default:
                break;
        }
    }

    // 指定存储位置
    private File picSavePath = new File(Environment.getExternalStorageDirectory().getPath()
            + "/" + System.currentTimeMillis() + ".jpg");

    /**
     * 底部选择框
     */
    private void getSelectButton() {
        new MenuDialog.Builder(getActivity())
                .setCancel(R.string.common_cancel)
                .setAutoDismiss(false)
                .setList(addPictures)
                .setCanceledOnTouchOutside(false)
                .setListener(new MenuDialog.OnListener<String>() {
                    @Override
                    public void onSelected(BaseDialog dialog, int position, String text) {
                        if (position == 0) {     // 相机
                            CameraUtil.openCamera(PersonalFragment.this);
                        }
                        if (position == 1) {     // 相册
                            gallery();
                        }
                        dialog.dismiss();
                    }

                    @Override
                    public void onCancel(BaseDialog dialog) {
                        dialog.dismiss();
                    }
                }).show();
    }

    /**
     * 从相册获取
     */
    public void gallery() {
        // 激活系统图库，选择一张图片
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
        // 开启一个带有返回值的Activity，请求码为PHOTO_REQUEST_GALLERY
        startActivityForResult(intent, RESULT_CAMERA_IMAGE);
    }

    /**
     * 获取相机相册的返回值
     *
     * @param requestCode 请求码
     * @param resultCode  结果
     * @param data        当data为null时：参考
     *                    https://blog.csdn.net/zimo2013/article/details/16916279
     */
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        String photoPath;
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case PHOTO_REQUEST_CAREMA:
                    if (data != null) {      //可能尚未指定intent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
                        if (data.hasExtra("data")) {                                    //返回有缩略图
                            Bitmap bitmap = data.getParcelableExtra("data");
                            //得到bitmap后的操作，如压缩处理...
                            Glide.with(getmContext())
                                    .load(bitmap)
                                    .apply(RequestOptions.bitmapTransform(new CircleCrop()).circleCrop())
                                    .into(headPhoto);
                        } else {         // 如果返回的不是缩略图，则直接获取地址
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                                photoPath = String.valueOf(picSavePath);
                            } else {
                                photoPath = data.getData().getEncodedPath();
                            }
                            Glide.with(getmContext())
                                    .load(photoPath)
                                    .apply(RequestOptions.bitmapTransform(new CircleCrop()).circleCrop())
                                    .into(headPhoto);
                            File file = IOUtils.decodeUri(getActivity(), uri_);
                            RequestPost.changeTouxiang(file, this);
                        }
                    }
                    break;
                case RESULT_CAMERA_IMAGE:
                    if (data != null) {
                        // 得到图片的全路径
                        Uri uri = data.getData();
                        // String path = CameraUtil.getPath(getContext(), uri);
                        // uploadImg(path);
                        File file = IOUtils.decodeUri(getActivity(), uri);
                        RequestPost.changeTouxiang(file, this);
//                        File file1 = CameraUtil.getFileFromUri(uri, getContext());
//                        RequestPost.UploadFile(file1, this);
                    }
                    photoPath = CameraAlbumUtils.getRealPathFromUri(getContext(), data.getData());
                    Glide.with(PersonalFragment.this)
                            .load(photoPath)
                            .apply(RequestOptions.bitmapTransform(new CircleCrop()).circleCrop())
                            .into(headPhoto);
                    break;
            }
        }
    }

    @SuppressLint("HandlerLeak")
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(@NonNull Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case INITIAL_SIZE:
                    Glide.with(Objects.requireNonNull(getActivity()))
                            .load(mCustomInfo.get("ico"))
                            .apply(RequestOptions.bitmapTransform(new CircleCrop()).circleCrop())
                            .error(R.drawable.icon_logo_2x)        //异常时候显示的图片
                            .placeholder(R.drawable.icon_logo_2x) //加载成功前显示的图片
                            .fallback(R.drawable.icon_logo_2x)  //url为空的时候,显示的图片
                            .into(headPhoto);
                    location.setText(mCustomInfo.get("storeName"));
                    userName.setText(mCustomInfo.get("name"));
                    break;

            }
        }
    };

    @Override
    public void onHttpFailure(HttpResponse result) {
        Log.d(TAG, "上传失败");
    }

    @Override
    public void onHttpSucceed(HttpResponse result) {
        Log.d(TAG, "result -- " + result);
        Map<String, String> body = JsonParser.parseJSONObject(result.body());
        if (body != null) {
            if ("1".equals(body.get("code"))) {
                if (result.url().contains("wxapi/v1/index.php?type=getPerson")) {           // 个人中心
                    mCustomInfo = JsonParser.parseJSONObject(JsonParser.parseJSONObject(body.get("data")).get("info"));
                    if (mCustomInfo != null) {
                        if (!TextUtils.isEmpty(mCustomInfo.get("messageNum"))){
                            mMsgDesc.setVisibility(View.VISIBLE);
                            mMsgDesc.setText(mCustomInfo.get("messageNum"));
                        }
                    }
                    Message msg = new Message();
                    msg.what = INITIAL_SIZE;
                    mHandler.sendMessage(msg);
                }

                // 一键报警
                if (result.url().contains("wxapi/v1/index.php?type=callPlat")) {
                    toast(body.get("warn"));
                }

                // 退出登录
                if (result.url().contains("wxapi/v1/login.php?type=loginOut")) {
                    toast(body.get("warn"));
                    postDelayed(() -> startActivity(IdentityActivity.class), 500);
                }

                // 修改头像
                if (result.url().contains("wxapi/v1/index.php?type=changeTouxiang")) {
                    toast(body.get("warn"));
                }

                // 修改昵称
                if (result.url().contains("wxapi/v1/index.php?type=changeNickName")) {
                    toast(body.get("warn"));
                }

            } else {
                toast(body.get("warn"));
            }
            dismissLoading();
        }
    }
}
