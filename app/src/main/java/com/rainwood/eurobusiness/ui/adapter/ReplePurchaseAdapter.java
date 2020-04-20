package com.rainwood.eurobusiness.ui.adapter;

import android.content.Context;
import android.text.Html;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.rainwood.eurobusiness.R;
import com.rainwood.eurobusiness.domain.ReplePurchaseBean;

import java.util.List;

/**
 * @Author: a797s
 * @Date: 2020/4/2 17:24
 * @Desc: 补货订单
 */
public final class ReplePurchaseAdapter extends BaseAdapter {

    private Context mContext;
    private List<ReplePurchaseBean> mList;

    public ReplePurchaseAdapter(Context context, List<ReplePurchaseBean> list) {
        mContext = context;
        mList = list;
    }

    @Override
    public int getCount() {
        return mList == null ? 0 : mList.size();
    }

    @Override
    public ReplePurchaseBean getItem(int position) {
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
            convertView = LayoutInflater.from(mContext).inflate(R.layout.item_reple_purchase, parent, false);
            holder.iv_img = convertView.findViewById(R.id.iv_img);
            holder.tv_name = convertView.findViewById(R.id.tv_name);
            holder.tv_special = convertView.findViewById(R.id.tv_special);
            holder.tv_purchase_num = convertView.findViewById(R.id.tv_purchase_num);
            holder.tv_storage_num = convertView.findViewById(R.id.tv_storage_num);
            holder.tv_status = convertView.findViewById(R.id.tv_status);
            holder.ll_item = convertView.findViewById(R.id.ll_item);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        if (TextUtils.isEmpty(getItem(position).getIco())) {
            Glide.with(convertView).load(R.drawable.icon_loadding_fail).into(holder.iv_img);
        } else {
            Glide.with(convertView).load(getItem(position).getIco()).into(holder.iv_img);
        }
        holder.tv_name.setText(getItem(position).getName());
        holder.tv_special.setText(getItem(position).getSkuName());
        holder.tv_purchase_num.setText(Html.fromHtml("<font color="
                + mContext.getResources().getColor(R.color.fontColor) + " size='12px'>采购：</font>"
                + "<font color=" + mContext.getResources().getColor(R.color.textColor) + " size='12px'>"
                + getItem(position).getBuyTotal() + "</font>"));
        holder.tv_storage_num.setText(Html.fromHtml("<font color="
                + mContext.getResources().getColor(R.color.fontColor) + " size='12px'>入库：</font>"
                + "<font color=" + mContext.getResources().getColor(R.color.red30) + " size='12px'>"
                + getItem(position).getInTotal() + "</font>"));
        holder.tv_status.setText(getItem(position).getWorkFlow());
        // 点击事件
        holder.ll_item.setOnClickListener(v -> mOnClickItem.onClickItem(position));
        return convertView;
    }

    public interface OnClickItem {
        void onClickItem(int position);
    }

    private OnClickItem mOnClickItem;

    public void setOnClickItem(OnClickItem onClickItem) {
        mOnClickItem = onClickItem;
    }

    class ViewHolder {
        private ImageView iv_img;
        private TextView tv_name, tv_special, tv_purchase_num, tv_storage_num, tv_status;
        private LinearLayout ll_item;
    }
}
