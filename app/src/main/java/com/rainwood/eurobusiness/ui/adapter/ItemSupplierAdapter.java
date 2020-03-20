package com.rainwood.eurobusiness.ui.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.rainwood.eurobusiness.R;
import com.rainwood.eurobusiness.domain.SuppierBean;
import com.rainwood.tools.common.FontDisplayUtil;

import java.util.List;

/**
 * @Author: shearson
 * @Time: 2020/2/24 21:59
 * @Desc: 供应商管理 adapter
 */
public class ItemSupplierAdapter extends BaseAdapter {

    private Context mContext;
    private List<SuppierBean> mList;

    public ItemSupplierAdapter(Context mContext, List<SuppierBean> mList) {
        this.mContext = mContext;
        this.mList = mList;
    }

    @Override
    public int getCount() {
        return mList == null ? 0 : mList.size();
    }

    @Override
    public SuppierBean getItem(int position) {
        return mList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @SuppressLint("SetTextI18n")
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = LayoutInflater.from(mContext).inflate(R.layout.item_supplier, parent, false);
            holder.tv_name = convertView.findViewById(R.id.tv_name);
            holder.tv_all_money = convertView.findViewById(R.id.tv_all_money);
            holder.tv_id = convertView.findViewById(R.id.tv_id);
            holder.ll_item = convertView.findViewById(R.id.ll_item);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        holder.tv_id.setText(getItem(position).getId());
        holder.tv_name.setText(getItem(position).getName());
        holder.tv_all_money.setText(Html.fromHtml("<font color='" + R.color.fontColor
                + "' size='" + FontDisplayUtil.dip2px(mContext, 12) + "'>累计应付款：</font>"
                + "<font color='" + R.color.textColor + "'size='" + FontDisplayUtil.dip2px(mContext, 15)
                + "'><b>" + getItem(position).getAllMoney() + "</b></font>"));
        // 点击事件
        holder.ll_item.setOnClickListener(v -> onClickItem.onClickItem(position));
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
        private TextView tv_name, tv_id, tv_all_money;
        private LinearLayout ll_item;
    }
}
