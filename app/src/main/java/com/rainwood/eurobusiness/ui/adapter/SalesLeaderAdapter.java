package com.rainwood.eurobusiness.ui.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.rainwood.eurobusiness.R;
import com.rainwood.eurobusiness.domain.GoodsRankingBean;
import com.rainwood.eurobusiness.domain.SalesLeaderBean;

import java.util.List;

/**
 * @Author: a797s
 * @Date: 2020/2/13
 * @Desc: 商品销售排行榜
 */
public class SalesLeaderAdapter extends BaseAdapter {

    private Context mContext;
    private List<GoodsRankingBean> mList;

    public SalesLeaderAdapter(Context mContext, List<GoodsRankingBean> mList) {
        this.mContext = mContext;
        this.mList = mList;
    }

    @Override
    public int getCount() {
        return mList == null ? 0 : mList.size();
    }

    @Override
    public GoodsRankingBean getItem(int position) {
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
            convertView = LayoutInflater.from(mContext).inflate(R.layout.item_sales_leaders, parent, false);
            holder.tv_ranking = convertView.findViewById(R.id.tv_ranking);
            holder.tv_name = convertView.findViewById(R.id.tv_name);
            holder.tv_special = convertView.findViewById(R.id.tv_special);
            holder.tv_sale_num = convertView.findViewById(R.id.tv_sale_num);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        holder.tv_ranking.setText(getItem(position).getRaking());
        holder.tv_name.setText(getItem(position).getGoodsName());
        holder.tv_special.setText(getItem(position).getGoodsSkuName());
        holder.tv_sale_num.setText(getItem(position).getNum());
        return convertView;
    }

    private class ViewHolder {
        private TextView tv_ranking, tv_name, tv_special, tv_sale_num;
    }
}
