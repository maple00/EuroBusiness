package com.rainwood.eurobusiness.ui.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestOptions;
import com.rainwood.eurobusiness.R;
import com.rainwood.eurobusiness.domain.CommImgBean;

import java.util.List;

/**
 * @Author: a797s
 * @Date: 2020/2/11
 * @Desc:
 */
public class ItemImgGridAdapter extends BaseAdapter {

    private Context mContext;
    private List<CommImgBean> mList;

    public ItemImgGridAdapter(Context mContext, List<CommImgBean> mList) {
        this.mContext = mContext;
        this.mList = mList;
    }

    @Override
    public int getCount() {
        return mList == null ? 0 : mList.size();
    }

    @Override
    public CommImgBean getItem(int position) {
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
            convertView = LayoutInflater.from(mContext).inflate(R.layout.item_shop_image, parent, false);
            holder.rl_rectangle = convertView.findViewById(R.id.rl_rectangle);
            holder.iv_add_img = convertView.findViewById(R.id.iv_add_img);
            holder.iv_delete = convertView.findViewById(R.id.iv_delete);
            holder.tv_desc = convertView.findViewById(R.id.tv_desc);
            holder.iv_img = convertView.findViewById(R.id.iv_img);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        if (position == 0){
            holder.iv_delete.setVisibility(View.GONE);
            holder.iv_add_img.setVisibility(View.VISIBLE);
            holder.tv_desc.setVisibility(View.VISIBLE);
            holder.rl_rectangle.setOnClickListener(v -> onImgListener.onClickAdd());
        }else {
            holder.rl_rectangle.setClickable(false);                // 设置不可点击，或者之后更新成查看大图
            Glide.with(mContext).load(getItem(position).getImgPath())
                    .apply(RequestOptions.bitmapTransform(new RoundedCorners(4)).override(80, 80))
                    .into(holder.iv_img);
            holder.iv_add_img.setVisibility(View.GONE);
            holder.tv_desc.setVisibility(View.GONE);
            holder.iv_delete.setVisibility(View.VISIBLE);
        }

        // 点击删除
        holder.iv_delete.setOnClickListener(v -> {
            onImgListener.onClickDelete(position);
            notifyDataSetChanged();
        });
        return convertView;
    }

    public interface OnImgListener{
        // 点击删除
        void onClickDelete(int position);
        // 点击添加
        void onClickAdd();
    }

    private OnImgListener onImgListener;

    public void setOnImgListener(OnImgListener onImgListener) {
        this.onImgListener = onImgListener;
    }

    private class ViewHolder {
        private RelativeLayout rl_rectangle;
        private ImageView iv_add_img, iv_delete, iv_img;
        private TextView tv_desc;
    }

}
