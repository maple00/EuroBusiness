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
import com.rainwood.eurobusiness.domain.GoodsListBean;

import java.util.List;

/**
 * @Author: a797s
 * @Date: 2020/4/8 11:17
 * @Desc: 商品列表adapter
 */
public final class GoodsListAdapter extends BaseAdapter {

    private Context mContext;
    private List<GoodsListBean> mList;

    public GoodsListAdapter(Context context, List<GoodsListBean> list) {
        mContext = context;
        mList = list;
    }

    @Override
    public int getCount() {
        return mList == null ? 0 : mList.size();
    }

    @Override
    public GoodsListBean getItem(int position) {
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
            convertView = LayoutInflater.from(mContext).inflate(R.layout.item_goods_list, parent, false);
            holder.iv_selected = convertView.findViewById(R.id.iv_selected);
            holder.tv_name = convertView.findViewById(R.id.tv_name);
            holder.ll_item = convertView.findViewById(R.id.ll_item);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        holder.tv_name.setText(getItem(position).getName());
        if (getItem(position).isSelected()){
            holder.tv_name.setTextColor(mContext.getResources().getColor(R.color.red30));
            holder.iv_selected.setVisibility(View.VISIBLE);
        }else {
            holder.tv_name.setTextColor(mContext.getResources().getColor(R.color.textColor));
            holder.iv_selected.setVisibility(View.GONE);
        }
        // 点击事件
        holder.ll_item.setOnClickListener(v -> mOnClickGoods.onClickGoods(position));
        return convertView;
    }

    public interface OnClickGoods{
        void onClickGoods(int position);
    }

    private OnClickGoods mOnClickGoods;

    public void setOnClickGoods(OnClickGoods onClickGoods) {
        mOnClickGoods = onClickGoods;
    }

    private class ViewHolder {
        private TextView tv_name;
        private ImageView iv_selected;
        private LinearLayout ll_item;
    }
}
