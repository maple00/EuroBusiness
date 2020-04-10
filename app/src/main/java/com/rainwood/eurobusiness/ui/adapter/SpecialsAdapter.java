package com.rainwood.eurobusiness.ui.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.rainwood.eurobusiness.R;
import com.rainwood.eurobusiness.domain.SkuBean;
import com.rainwood.eurobusiness.domain.SpecialBean;

import java.util.List;

/**
 * @Author: a797s
 * @Date: 2020/2/15
 * @Desc: 规格参数 adapter
 */
public class SpecialsAdapter extends BaseAdapter {

    private Context mContext;
    private List<SkuBean> mList;

    public SpecialsAdapter(Context context, List<SkuBean> mList) {
        this.mContext = context;
        this.mList = mList;
    }

    @Override
    public int getCount() {
        return mList == null ? 0 : mList.size();
    }

    @Override
    public SkuBean getItem(int position) {
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
            convertView = LayoutInflater.from(mContext).inflate(R.layout.item_special_info, parent, false);
            holder.tv_color = convertView.findViewById(R.id.tv_color);
            holder.tv_size = convertView.findViewById(R.id.tv_size);
            holder.tv_price = convertView.findViewById(R.id.tv_price);
            holder.rl_item = convertView.findViewById(R.id.rl_item);
            holder.tv_num = convertView.findViewById(R.id.tv_num);
            holder.tv_store = convertView.findViewById(R.id.tv_store);
            holder.iv_garbage = convertView.findViewById(R.id.iv_garbage);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        if (position % 2 == 0){
            holder.rl_item.setBackgroundColor(mContext.getResources().getColor(R.color.white));
        }else {
            holder.rl_item.setBackgroundColor(mContext.getResources().getColor(R.color.gold05));
        }
        String[] nameAndSize = getItem(position).getName().split("/");
        holder.tv_color.setText(nameAndSize[0]);
        holder.tv_size.setText(nameAndSize[1]);
        holder.tv_price.setText(getItem(position).getTradePrice());
        holder.tv_num.setText(getItem(position).getNum());
        holder.tv_store.setText(getItem(position).getStoreName());
        // 点击事件
        holder.iv_garbage.setOnClickListener(v -> {
            mList.remove(position);
            notifyDataSetChanged();
        });
        return convertView;
    }
    private class ViewHolder {
        private TextView tv_color, tv_size, tv_price, tv_num, tv_store;
        private ImageView iv_garbage;
        private RelativeLayout rl_item;
    }
}
