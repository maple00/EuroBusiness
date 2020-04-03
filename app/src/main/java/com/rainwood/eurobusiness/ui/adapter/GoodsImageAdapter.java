package com.rainwood.eurobusiness.ui.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestOptions;
import com.rainwood.eurobusiness.R;
import com.rainwood.eurobusiness.domain.ImageBean;
import com.rainwood.eurobusiness.domain.ImagesBean;

import java.util.List;

/**
 * @Author: a797s
 * @Date: 2020/2/18
 * @Desc: 商品展示图片
 */
public class GoodsImageAdapter extends BaseAdapter {

    private Context mContext;
    private List<ImagesBean> mList;

    public GoodsImageAdapter(Context mContext, List<ImagesBean> mList) {
        this.mContext = mContext;
        this.mList = mList;
        Log.d("Sxs", " ===== mList ==== " + mList.toString());
    }

    @Override
    public int getCount() {
        return mList == null ? 0 : mList.size();
    }

    @Override
    public ImagesBean getItem(int position) {
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
            convertView = LayoutInflater.from(mContext).inflate(R.layout.sub_item_goods_image, parent, false);
            holder.iv_img = convertView.findViewById(R.id.iv_img);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        Glide.with(convertView).load(getItem(position).getSrc())
                .error(mContext.getResources().getDrawable(R.drawable.icon_loadding_fail))
                .apply(RequestOptions.bitmapTransform(new RoundedCorners(4)).override(80, 80))
                .into(holder.iv_img);
        return convertView;
    }

    private class ViewHolder {
        private ImageView iv_img;
    }
}
