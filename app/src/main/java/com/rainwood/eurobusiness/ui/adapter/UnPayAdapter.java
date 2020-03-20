package com.rainwood.eurobusiness.ui.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.rainwood.eurobusiness.R;
import com.rainwood.eurobusiness.domain.CommonUIBean;

import java.util.List;

/**
 * @Author: shearson
 * @Time: 2020/2/24 21:43
 * @Desc: 未付款和未支付
 */
public class UnPayAdapter extends BaseAdapter {

    private Context mContext;
    private List<CommonUIBean> mList;

    public UnPayAdapter(Context mContext, List<CommonUIBean> mList) {
        this.mContext = mContext;
        this.mList = mList;
    }

    @Override
    public int getCount() {
        return mList == null ? 0 : mList.size();
    }

    @Override
    public CommonUIBean getItem(int position) {
        return mList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null){
            holder = new ViewHolder();
            convertView = LayoutInflater.from(mContext).inflate(R.layout.item_un_payment, parent, false);
            holder.tv_name = convertView.findViewById(R.id.tv_name);
            holder.tv_money = convertView.findViewById(R.id.tv_money);
            convertView.setTag(holder);
        }else {
            holder = (ViewHolder) convertView.getTag();
        }
        holder.tv_name.setText(getItem(position).getTitle());
        holder.tv_money.setText(getItem(position).getShowText());
        return convertView;
    }

    private class ViewHolder{
        private TextView tv_name,tv_money;
    }
}
