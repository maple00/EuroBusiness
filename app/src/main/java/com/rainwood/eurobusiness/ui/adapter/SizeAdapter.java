package com.rainwood.eurobusiness.ui.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.rainwood.eurobusiness.R;
import com.rainwood.eurobusiness.domain.SpecialBean;

import java.util.List;

/**
 * @Author: a797s
 * @Date: 2020/2/15
 * @Desc: 尺寸 adapter
 */
public class SizeAdapter extends BaseAdapter {

    private Context mContext;
    private List<SpecialBean> mList;
    private String status;

    public SizeAdapter(Context mContext, List<SpecialBean> mList, String status) {
        this.mContext = mContext;
        this.mList = mList;
        this.status = status;
    }

    @Override
    public int getCount() {
        return mList == null ? 0 : mList.size();
    }

    @Override
    public SpecialBean getItem(int position) {
        return mList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @SuppressLint("SetTextI18n")
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = LayoutInflater.from(mContext).inflate(R.layout.item_size, parent, false);
            holder.tv_color = convertView.findViewById(R.id.tv_color);
            holder.tv_size = convertView.findViewById(R.id.tv_size);
            holder.tv_below = convertView.findViewById(R.id.tv_below);
            holder.tv_price = convertView.findViewById(R.id.tv_price);
            holder.tv_repertory = convertView.findViewById(R.id.tv_repertory);
            holder.rl_item = convertView.findViewById(R.id.rl_item);
            holder.tv_refunds = convertView.findViewById(R.id.tv_refunds);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        if (position % 2 == 0) {
            holder.rl_item.setBackgroundColor(mContext.getResources().getColor(R.color.white));
        } else {
            holder.rl_item.setBackgroundColor(mContext.getResources().getColor(R.color.gold05));
        }
        holder.tv_color.setText(getItem(position).getGoodsColor());
        if (getItem(position).getGoodsSize() == null) {
            holder.tv_size.setText(getItem(position).getGodosSize());
        } else {
            holder.tv_size.setText(getItem(position).getGoodsSize());
        }
        if (TextUtils.isEmpty(getItem(position).getIowerLimit())) {
            holder.tv_below.setVisibility(View.GONE);
        } else {
            holder.tv_below.setText(getItem(position).getIowerLimit());
        }
        if (getItem(position).getTradePrice() == null) {
            holder.tv_price.setText(getItem(position).getPrice() + "$");
        } else {
            holder.tv_price.setText(getItem(position).getTradePrice() + "$");
        }
        if (getItem(position).getStock() == null) {
            holder.tv_repertory.setText(getItem(position).getNum());
        } else {
            holder.tv_repertory.setText(getItem(position).getStock());
        }
        // 如果内容为空，则表示可以退货 -- 只有门店端管理的订单完成才显示退货
        if ("".equals(getItem(position).getRefundState())) {         // 如果状态为空，则表示可以退货
            holder.tv_refunds.setFocusable(true);
            holder.tv_refunds.setText(getItem(position).getRefundText());
            holder.tv_refunds.setTextColor(mContext.getResources().getColor(R.color.textColor));
            holder.tv_refunds.setBackgroundResource(R.drawable.shape_radius_gray_16);
            holder.tv_refunds.setOnClickListener(v -> {
                mOnClickRefunds.onClickrefunds(position);
            });
        } else {                 // 显示退货进度
            if ("complete".equals(getItem(position).getRefundState())) {
                holder.tv_refunds.setTextColor(mContext.getResources().getColor(R.color.blue5));
            } else {
                holder.tv_refunds.setTextColor(mContext.getResources().getColor(R.color.yellow05));
            }
            holder.tv_refunds.setFocusable(false);
            holder.tv_refunds.setText(getItem(position).getRefundText());
            holder.tv_refunds.setBackgroundResource(R.drawable.selector_transparent);
        }
        return convertView;
    }

    public interface OnClickRefunds {
        // 退货
        void onClickrefunds(int position);
    }

    private OnClickRefunds mOnClickRefunds;

    public void setOnClickRefunds(OnClickRefunds onClickRefunds) {
        mOnClickRefunds = onClickRefunds;
    }

    private class ViewHolder {
        private TextView tv_color, tv_size, tv_below, tv_price, tv_repertory, tv_refunds;
        private RelativeLayout rl_item;
    }
}
