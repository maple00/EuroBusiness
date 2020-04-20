package com.rainwood.eurobusiness.ui.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.rainwood.eurobusiness.R;
import com.rainwood.eurobusiness.domain.ImageBean;

import java.util.List;

/**
 * @Author: a797s
 * @Date: 2020/3/10 10:44
 * @Desc: 照片上传
 */
public final class UploadImgAdapter extends BaseAdapter {

    private Context mContext;
    private List<ImageBean> mList;

    public UploadImgAdapter(Context context, List<ImageBean> list) {
        this.mContext = context;
        this.mList = list;
    }

    @Override
    public int getCount() {
        return mList == null ? 0 : mList.size();
    }

    @Override
    public ImageBean getItem(int position) {
        return mList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = LayoutInflater.from(mContext).inflate(R.layout.item_upload_image, parent, false);
            holder.iv_image = convertView.findViewById(R.id.iv_image);
            holder.iv_delete = convertView.findViewById(R.id.iv_delete);
            holder.fl_item = convertView.findViewById(R.id.fl_item);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        if (getItem(position).isHasAdd()) {
            holder.iv_delete.setVisibility(View.GONE);
            holder.fl_item.setOnClickListener(v -> mOnClickImage.onClickUpload());
            Glide.with(mContext).load(R.drawable.img_add)
                    //.apply(RequestOptions.bitmapTransform(new CircleCrop()).circleCrop())
                    .into(holder.iv_image);
        } else {
            holder.iv_delete.setVisibility(View.VISIBLE);
            holder.fl_item.setClickable(false);
            holder.fl_item.setFocusable(false);
            holder.fl_item.setFocusableInTouchMode(false);

            Glide.with(mContext)
                    .load(getItem(position).getPath() == null ? getItem(position).getSrc() : getItem(position).getPath())
                    .error(R.drawable.icon_loadding_fail)        //异常时候显示的图片
                    .placeholder(R.drawable.icon_loadding_fail) //加载成功前显示的图片
                    .fallback(R.drawable.icon_loadding_fail)  //url为空的时候,显示的图片
                    .into(holder.iv_image);
            // holder.iv_image.setImageURI(Uri.fromFile(new File(getItem(position).getPath())));
        }
        // 点击事件
        holder.iv_delete.setOnClickListener(v -> {
            mList.remove(position);
            notifyDataSetChanged();
        });
        return convertView;
    }

    public interface OnClickImage {
        // 上传照片
        void onClickUpload();
    }

    private OnClickImage mOnClickImage;

    public void setOnClickImage(OnClickImage onClickImage) {
        mOnClickImage = onClickImage;
    }

    private class ViewHolder {
        private ImageView iv_image, iv_delete;
        private FrameLayout fl_item;
    }
}
