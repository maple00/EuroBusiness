package com.rainwood.eurobusiness.ui.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.rainwood.eurobusiness.R;
import com.rainwood.eurobusiness.domain.ClientBean;

import java.util.List;

/**
 * @Author: a797s
 * @Date: 2020/3/23 9:34
 * @Desc: 销售排行榜 --- 门店统计和客户统计
 */
public class ClientRankingAdapter extends BaseAdapter {

    private Context mContext;
    private List<ClientBean> mList;

    public ClientRankingAdapter(Context context, List<ClientBean> list) {
        mContext = context;
        mList = list;
    }

    @Override
    public int getCount() {
        return mList == null ? 0 : mList.size();
    }

    @Override
    public ClientBean getItem(int position) {
        return mList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @SuppressLint("ViewHolder")
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null){
            holder = new ViewHolder();
            convertView = LayoutInflater.from(mContext).inflate(R.layout.item_store_client, parent, false);
            holder.tv_ranking = convertView.findViewById(R.id.tv_rankings);
            holder.tv_name = convertView.findViewById(R.id.tv_names);
            holder.tv_sale_money = convertView.findViewById(R.id.tv_sale_money);
            convertView.setTag(holder);
        }else {
            holder = (ViewHolder) convertView.getTag();
        }
        holder.tv_ranking.setText(getItem(position).getRanking());
        holder.tv_name.setText(getItem(position).getKehuName());
        holder.tv_sale_money.setText(getItem(position).getMoney());
        return convertView;
    }

    public class ViewHolder{
        private TextView tv_ranking, tv_name, tv_sale_money;
    }
}
