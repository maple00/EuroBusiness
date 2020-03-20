package com.rainwood.eurobusiness.ui.adapter;

import android.content.Context;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.rainwood.eurobusiness.R;
import com.rainwood.eurobusiness.domain.OrderContentBean;
import com.rainwood.tools.widget.MeasureListView;

import java.util.List;

/**
 * @Author: a797s
 * @Date: 2020/2/19
 * @Desc: 订单管理 -- 订单列表
 */
public class OrderContentAdapter extends BaseAdapter {

    private Context mContext;
    private List<OrderContentBean> mList;

    public OrderContentAdapter(Context mContext, List<OrderContentBean> mList) {
        this.mContext = mContext;
        this.mList = mList;
    }

    @Override
    public int getCount() {
        return mList == null ? 0 : mList.size();
    }

    @Override
    public OrderContentBean getItem(int position) {
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
            convertView = LayoutInflater.from(mContext).inflate(R.layout.item_order_manager, parent, false);
            holder.tv_method = convertView.findViewById(R.id.tv_method);
            holder.tv_num = convertView.findViewById(R.id.tv_num);
            holder.tv_status = convertView.findViewById(R.id.tv_status);
            holder.lv_label = convertView.findViewById(R.id.lv_label);
            holder.ll_item = convertView.findViewById(R.id.ll_item);
            convertView.setTag(holder);
        }else {
            holder = (ViewHolder) convertView.getTag();
        }
        holder.tv_method.setText(getItem(position).getMethod());
        holder.tv_num.setText(Html.fromHtml("<font color=" + mContext.getResources().getColor(R.color.textColor)
                + " size='13px'>订单号：</font>" + "<font color=" + mContext.getResources().getColor(R.color.textColor)
                + " size='13px'>" + getItem(position).getNum() + "</font>"));
        holder.tv_status.setText(getItem(position).getStatus());
        // CommUIBean  格式
        SubGoodsDetailAdapter labelAdapter = new SubGoodsDetailAdapter(mContext, getItem(position).getManagerList());
        holder.lv_label.setAdapter(labelAdapter);
        // 点击事件
        holder.ll_item.setOnClickListener(v -> onClickItem.onClickItem(position));

        return convertView;
    }

    public interface OnClickItem{
        void onClickItem(int position);
    }

    private OnClickItem onClickItem;

    public void setOnClickItem(OnClickItem onClickItem) {
        this.onClickItem = onClickItem;
    }

    private class ViewHolder{
        private TextView tv_method, tv_num, tv_status;
        private MeasureListView lv_label;
        private LinearLayout ll_item;
    }
}
