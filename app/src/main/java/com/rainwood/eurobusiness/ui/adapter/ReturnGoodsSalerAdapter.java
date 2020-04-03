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
import com.rainwood.eurobusiness.domain.RefundGoodsBean;
import com.rainwood.eurobusiness.domain.ReturnGoodsBean;

import java.util.List;

/**
 * @Author: a797s
 * @Date: 2020/2/21
 * @Desc: 退货管理 content
 */
public class ReturnGoodsSalerAdapter extends BaseAdapter {

    private Context mContext;
    private List<RefundGoodsBean> mList;

    public ReturnGoodsSalerAdapter(Context mContext, List<RefundGoodsBean> mList) {
        this.mContext = mContext;
        this.mList = mList;
    }

    @Override
    public int getCount() {
        return mList == null ? 0 : mList.size();
    }

    @Override
    public RefundGoodsBean getItem(int position) {
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
            convertView = LayoutInflater.from(mContext).inflate(R.layout.item_return_goods_sale, parent, false);
            holder.iv_img = convertView.findViewById(R.id.iv_img);
            holder.tv_name = convertView.findViewById(R.id.tv_name);
            holder.tv_params = convertView.findViewById(R.id.tv_params);
            holder.tv_return_num = convertView.findViewById(R.id.tv_return_num);
            holder.tv_return_money = convertView.findViewById(R.id.tv_return_money);
            holder.tv_status = convertView.findViewById(R.id.tv_status);
            holder.ll_item = convertView.findViewById(R.id.ll_item);
            holder.tv_in_storage = convertView.findViewById(R.id.tv_in_storage);
            holder.tv_scrap = convertView.findViewById(R.id.tv_scrap);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        if (TextUtils.isEmpty(getItem(position).getIco())) {
            Glide.with(convertView).load(R.drawable.icon_loadding_fail).into(holder.iv_img);
        } else {
            Glide.with(convertView).load(getItem(position).getIco()).into(holder.iv_img);
        }
        holder.tv_name.setText(getItem(position).getGoodsName());
        holder.tv_params.setText(getItem(position).getSkuName());
        holder.tv_return_num.setText(Html.fromHtml("<font color=" + mContext.getResources().getColor(R.color.textColor) + " size='13px'>退货：</font>"
                + "<font color=" + mContext.getResources().getColor(R.color.red30) + " size='13px'>" + getItem(position).getRefundNum() + "</font>"));
        holder.tv_return_money.setText(Html.fromHtml("<font color=" + mContext.getResources().getColor(R.color.fontColor) + " size='13px'>退款：</font>"
                + "<font color=" + mContext.getResources().getColor(R.color.fontColor) + " size='13px'>" + getItem(position).getRefundMoney() + "</font>"));
        if (getItem(position).getWorkFlow().equals("草稿")) {
            holder.tv_status.setTextColor(mContext.getResources().getColor(R.color.orange20));
        } else {
            holder.tv_status.setTextColor(mContext.getResources().getColor(R.color.blue5));
        }
        holder.tv_status.setText(getItem(position).getWorkFlow());
        if (getItem(position).getWorkFlow().equals("已完成")) {
            holder.tv_in_storage.setVisibility(View.VISIBLE);
            holder.tv_scrap.setVisibility(View.VISIBLE);
        } else {
            holder.tv_in_storage.setVisibility(View.GONE);
            holder.tv_scrap.setVisibility(View.GONE);
        }
        // 点击事件
        holder.ll_item.setOnClickListener(v -> onClickItem.onClickItem(position));
        // 报废
        holder.tv_scrap.setOnClickListener(v -> {
            onClickItem.onClickScrap(position);
            notifyDataSetChanged();
        });
        // 入库
        holder.tv_in_storage.setOnClickListener(v -> {
            onClickItem.onClickStorage(position);
            notifyDataSetChanged();
        });
        return convertView;
    }

    public interface OnClickItem {
        // 查看详情
        void onClickItem(int position);
        // 报废
        void onClickScrap(int position);
        // 入库
        void onClickStorage(int position);
    }

    private OnClickItem onClickItem;

    public void setOnClickItem(OnClickItem onClickItem) {
        this.onClickItem = onClickItem;
    }

    private class ViewHolder {
        private ImageView iv_img;
        private TextView tv_name, tv_params, tv_return_num, tv_return_money, tv_status, tv_in_storage, tv_scrap;
        private LinearLayout ll_item;
    }
}
