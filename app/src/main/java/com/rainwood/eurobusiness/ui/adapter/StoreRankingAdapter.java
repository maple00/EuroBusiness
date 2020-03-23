package com.rainwood.eurobusiness.ui.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.rainwood.eurobusiness.R;
import com.rainwood.eurobusiness.domain.StoreBean;

import java.util.List;

/**
 * @Author: a797s
 * @Date: 2020/3/23 9:34
 * @Desc: 销售排行榜 --- 门店统计和客户统计
 */
public class StoreRankingAdapter extends BaseAdapter {

    private Context mContext;
    private List<StoreBean> mList;

    public StoreRankingAdapter(Context context, List<StoreBean> list) {
        mContext = context;
        mList = list;
    }

    @Override
    public int getCount() {
        return mList == null ? 0 : mList.size();
    }

    @Override
    public StoreBean getItem(int position) {
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
            convertView = LayoutInflater.from(mContext).inflate(R.layout.item_store_client, parent, false);
            holder.tv_rankings = convertView.findViewById(R.id.tv_rankings);
            holder.tv_name = convertView.findViewById(R.id.tv_names);
            holder.tv_sale_money = convertView.findViewById(R.id.tv_sale_money);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        holder.tv_rankings.setText(getItem(position).getRanking());
        holder.tv_name.setText(getItem(position).getStoreName());
        holder.tv_sale_money.setText(getItem(position).getMoney());
        return convertView;
    }

    private class ViewHolder {
        private TextView tv_rankings, tv_name, tv_sale_money;
    }
}
