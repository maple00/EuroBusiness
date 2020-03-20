package com.rainwood.eurobusiness.ui.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.rainwood.eurobusiness.R;
import com.rainwood.eurobusiness.domain.AddressBean;

import java.util.List;

/**
 * @Author: shearson
 * @Time: 2020/2/24 16:02
 * @Desc: java类作用描述
 */
public class GoodsAddressAdapter extends BaseAdapter {

    private Context mContext;
    private List<AddressBean> mList;

    public GoodsAddressAdapter(Context mContext, List<AddressBean> mList) {
        this.mContext = mContext;
        this.mList = mList;
    }

    @Override
    public int getCount() {
        return mList == null ? 0 : mList.size();
    }

    @Override
    public AddressBean getItem(int position) {
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
            convertView = LayoutInflater.from(mContext).inflate(R.layout.item_goods_address, parent, false);
            holder.tv_checked = convertView.findViewById(R.id.tv_checked);
            holder.tv_name = convertView.findViewById(R.id.tv_name);
            holder.tv_address = convertView.findViewById(R.id.tv_address);
            holder.iv_default = convertView.findViewById(R.id.iv_default);
            holder.ll_default = convertView.findViewById(R.id.ll_default);
            holder.ll_edit = convertView.findViewById(R.id.ll_edit);
            holder.ll_delete = convertView.findViewById(R.id.ll_delete);
            holder.ll_item = convertView.findViewById(R.id.ll_item);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        if (getItem(position).isChecked()) {             // 如果是默认地址
            holder.iv_default.setImageResource(R.drawable.radio_checked_shape);
            holder.tv_checked.setVisibility(View.VISIBLE);
        } else {
            holder.iv_default.setImageResource(R.drawable.radio_uncheck_shape);
            holder.tv_checked.setVisibility(View.GONE);
        }
        holder.tv_name.setText(getItem(position).getName());
        holder.tv_address.setText(getItem(position).getAddress());
        // 设置为默认值
        holder.ll_default.setOnClickListener(v -> {
            onClickItem.onClickDefault(position);
            notifyDataSetChanged();
        });
        // 编辑
        holder.ll_edit.setOnClickListener(v -> onClickItem.onClickEdit(position));
        // 删除
        holder.ll_delete.setOnClickListener(v -> {
            onClickItem.onClickDelete(position);
            notifyDataSetChanged();
        });
        // 选择地址
        holder.ll_item.setOnClickListener(v -> onClickItem.onClickItem(position));
        return convertView;
    }

    public interface OnClickItem {
        // 选择默认方式
        void onClickDefault(int position);

        // 编辑
        void onClickEdit(int position);

        // 删除
        void onClickDelete(int position);

        // 选择地址
        void onClickItem(int position);
    }

    private OnClickItem onClickItem;

    public void setOnClickItem(OnClickItem onClickItem) {
        this.onClickItem = onClickItem;
    }

    private class ViewHolder {
        private TextView tv_checked, tv_name, tv_address;
        private LinearLayout ll_default, ll_edit, ll_delete, ll_item;
        private ImageView iv_default;
    }
}
