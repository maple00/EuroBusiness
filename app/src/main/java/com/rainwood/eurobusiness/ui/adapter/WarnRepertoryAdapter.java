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
import com.rainwood.eurobusiness.domain.WarinReBean;
import com.rainwood.eurobusiness.domain.WarnStockBean;

import java.util.List;

/**
 * @Author: a797s
 * @Date: 2020/2/20
 * @Desc: 库存预警
 */
public class WarnRepertoryAdapter extends BaseAdapter {

    private Context mContext;
    private List<WarnStockBean> mList;

    public WarnRepertoryAdapter(Context mContext, List<WarnStockBean> mList) {
        this.mContext = mContext;
        this.mList = mList;
    }

    @Override
    public int getCount() {
        return mList == null ? 0 : mList.size();
    }

    @Override
    public WarnStockBean getItem(int position) {
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
            convertView = LayoutInflater.from(mContext).inflate(R.layout.item_warn_repertory, parent, false);
            holder.iv_img = convertView.findViewById(R.id.iv_img);
            holder.tv_name = convertView.findViewById(R.id.tv_name);
            holder.tv_model = convertView.findViewById(R.id.tv_model);
            holder.tv_params = convertView.findViewById(R.id.tv_params);
            holder.tv_ven_num = convertView.findViewById(R.id.tv_ven_num);
            holder.tv_replenish_num = convertView.findViewById(R.id.tv_replenish_num);
            holder.tv_replish = convertView.findViewById(R.id.tv_replish);
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
        holder.tv_model.setText(getItem(position).getModel());
        holder.tv_params.setText(getItem(position).getSkuName());
        holder.tv_ven_num.setText(Html.fromHtml("<font color=" + mContext.getResources().getColor(R.color.fontColor) + " size='12px'>库存：</font>"
                + "<font color=" + mContext.getResources().getColor(R.color.red100) + " size='12px'>" + getItem(position).getStock() + "</font>"));
        holder.tv_replenish_num.setText(Html.fromHtml("<font color=" + mContext.getResources().getColor(R.color.fontColor) + " size='12px'>补货中：</font>"
                + "<font color=" + mContext.getResources().getColor(R.color.blue5) + " size='12px'>" + getItem(position).getChargeNum() + "</font>"));
        // 点击事件
        holder.tv_replish.setOnClickListener(v -> onClickItem.onClickItem(position));
        return convertView;
    }

    public interface OnClickItem {
        void onClickItem(int position);
    }

    private OnClickItem onClickItem;

    public void setOnClickItem(OnClickItem onClickItem) {
        this.onClickItem = onClickItem;
    }

    private class ViewHolder {
        private ImageView iv_img;
        private TextView tv_name, tv_model, tv_params, tv_ven_num, tv_replenish_num, tv_replish;
    }
}
