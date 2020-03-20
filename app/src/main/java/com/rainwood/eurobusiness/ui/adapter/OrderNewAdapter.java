package com.rainwood.eurobusiness.ui.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.rainwood.eurobusiness.R;
import com.rainwood.eurobusiness.domain.OrderBean;
import com.rainwood.tools.widget.MeasureListView;

import java.util.List;

/**
 * @Author: a797s
 * @Date: 2020/2/19
 * @Desc: 新建订单
 */
public class OrderNewAdapter extends BaseAdapter {

    private Context mContext;
    private List<OrderBean> mList;

    public OrderNewAdapter(Context mContext, List<OrderBean> mList) {
        this.mContext = mContext;
        this.mList = mList;
    }

    @Override
    public int getCount() {
        return mList == null ? 0 : mList.size();
    }

    @Override
    public OrderBean getItem(int position) {
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
            convertView = LayoutInflater.from(mContext).inflate(R.layout.item_order_new, parent, false);
            holder.tv_title = convertView.findViewById(R.id.tv_title);
            holder.lv_order_info = convertView.findViewById(R.id.lv_order_info);
            convertView.setTag(holder);
        }else {
            holder = (ViewHolder) convertView.getTag();
        }
        holder.tv_title.setText(getItem(position).getTitle());
        // 调用Adapter
        CommUIAdapter uiAdapter = new CommUIAdapter(mContext, getItem(position).getCommonList());
        holder.lv_order_info.setAdapter(uiAdapter);
        uiAdapter.setOnClickEditText(position, onClickEditText);
        return convertView;
    }

    private CommUIAdapter.OnClickEditText onClickEditText;

    public void setOnClickEditText(CommUIAdapter.OnClickEditText onClickEditText) {
        this.onClickEditText = onClickEditText;
    }

    private class ViewHolder{
        private TextView tv_title;
        private MeasureListView lv_order_info;
    }
}
