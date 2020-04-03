package com.rainwood.eurobusiness.ui.adapter;

import android.content.Context;
import android.os.Build;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.rainwood.eurobusiness.R;
import com.rainwood.eurobusiness.domain.OrderContentBean;
import com.rainwood.eurobusiness.domain.OrderListBean;
import com.rainwood.tools.widget.MeasureListView;

import java.util.List;

/**
 * @Author: a797s
 * @Date: 2020/2/19
 * @Desc: 订单管理 -- 订单列表
 */
public class OrderContentAdapter extends BaseAdapter {

    private Context mContext;
    private List<OrderListBean> mList;

    public OrderContentAdapter(Context mContext, List<OrderListBean> mList) {
        this.mContext = mContext;
        this.mList = mList;
    }

    @Override
    public int getCount() {
        return mList == null ? 0 : mList.size();
    }

    @Override
    public OrderListBean getItem(int position) {
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
            //holder.lv_label = convertView.findViewById(R.id.lv_label);
            holder.ll_item = convertView.findViewById(R.id.ll_item);
            holder.tv_title_num = convertView.findViewById(R.id.tv_title_num);
            holder.tv_show_num = convertView.findViewById(R.id.tv_show_num);
            holder.tv_title_total = convertView.findViewById(R.id.tv_title_total);
            holder.tv_show_total = convertView.findViewById(R.id.tv_show_total);
            holder.tv_title_method = convertView.findViewById(R.id.tv_title_method);
            holder.tv_show_method = convertView.findViewById(R.id.tv_show_method);
            convertView.setTag(holder);
        }else {
            holder = (ViewHolder) convertView.getTag();
        }
        if ("线下".equals(getItem(position).getType())){
            holder.tv_method.setTextColor(mContext.getResources().getColor(R.color.purple));
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                holder.tv_method.setBackground(mContext.getDrawable(R.drawable.shape_purple));
            }
        }else {
            holder.tv_method.setTextColor(mContext.getResources().getColor(R.color.red100));
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                holder.tv_method.setBackground(mContext.getDrawable(R.drawable.shape_text_red100));
            }
        }
        holder.tv_method.setText(getItem(position).getType());
        holder.tv_num.setText(Html.fromHtml("<font color=" + mContext.getResources().getColor(R.color.textColor)
                + " size='13px'>订单号：</font>" + "<font color=" + mContext.getResources().getColor(R.color.textColor)
                + " size='13px'>" + getItem(position).getId() + "</font>"));
        holder.tv_status.setText(getItem(position).getState());
        holder.tv_title_num.setText("商品数量");
        holder.tv_show_num.setText(getItem(position).getNum());
        holder.tv_title_total.setText("合计");
        holder.tv_show_total.setText(getItem(position).getMoney());
        holder.tv_title_method.setText("支付方式");
        holder.tv_show_method.setText(getItem(position).getPayType());
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
        private TextView tv_method, tv_num, tv_status,
                tv_title_num, tv_show_num,
                tv_title_total, tv_show_total,
                tv_title_method, tv_show_method;
        private LinearLayout ll_item;
    }
}
