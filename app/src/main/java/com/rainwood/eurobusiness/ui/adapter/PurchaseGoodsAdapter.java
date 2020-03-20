package com.rainwood.eurobusiness.ui.adapter;

import android.content.Context;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.rainwood.eurobusiness.R;
import com.rainwood.eurobusiness.domain.PurchaseBean;

import java.util.List;

/**
 * @Author: a797s
 * @Date: 2020/2/16
 * @Desc: 采购记录 --- 商品列表
 */
public class PurchaseGoodsAdapter extends BaseAdapter {

    private Context mContext;
    private List<PurchaseBean> mList;

    public PurchaseGoodsAdapter(Context mContext, List<PurchaseBean> mList) {
        this.mContext = mContext;
        this.mList = mList;
    }

    @Override
    public int getCount() {
        return mList == null ? 0 : mList.size();
    }

    @Override
    public PurchaseBean getItem(int position) {
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
            convertView = LayoutInflater.from(mContext).inflate(R.layout.item_purchase, parent, false);
            holder.iv_img = convertView.findViewById(R.id.iv_img);
            holder.tv_name = convertView.findViewById(R.id.tv_name);
            holder.tv_purchase = convertView.findViewById(R.id.tv_purchase);
            holder.tv_in_storage = convertView.findViewById(R.id.tv_in_storage);
            holder.tv_status = convertView.findViewById(R.id.tv_status);
            holder.ll_item = convertView.findViewById(R.id.ll_item);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        if (getItem(position).getImgPath() == null){
            Glide.with(convertView).load(mContext.getResources().getDrawable(R.drawable.icon_loadding_fail)).into(holder.iv_img);
        }else {
            Glide.with(convertView).load(getItem(position).getImgPath()).into(holder.iv_img);
        }
        holder.tv_name.setText(getItem(position).getName());
        // 采购
        holder.tv_purchase.setText(Html.fromHtml("<font color=" + mContext.getResources().getColor(R.color.fontColor) + " size='13px'>采购：</font>"
                + "<font color=" + mContext.getResources().getColor(R.color.textColor) + " size='13px'>" + getItem(position).getNum() + "</font>"));
        // 入库
        holder.tv_in_storage.setText(Html.fromHtml("<font color=" + mContext.getResources().getColor(R.color.fontColor) + " size='13px'>入库：</font>"
                + "<font color=" + mContext.getResources().getColor(R.color.red30) + " size='13px'>" + getItem(position).getInNum() + "</font>"));
        holder.tv_status.setText(getItem(position).getStatus());
        // 点击事件
        holder.ll_item.setOnClickListener(v -> onClickItem.onClickItem(position));
        return convertView;
    }

    public interface OnClickItem{
        void onClickItem(int position);
    }

    private OnClickItem onClickItem;

    public void setOnClickItem(OnClickItem onClickItem) {
        this.onClickItem = onClickItem;
    }

    private class ViewHolder {
        private ImageView iv_img;
        private TextView tv_name, tv_purchase, tv_in_storage, tv_status;
        private LinearLayout ll_item;
    }

}
