package com.rainwood.eurobusiness.ui.adapter;

import android.content.Context;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.RequiresApi;

import com.rainwood.eurobusiness.R;
import com.rainwood.eurobusiness.domain.PressBean;

import java.util.List;

/**
 * @Author: a797s
 * @Date: 2020/3/31 17:39
 * @Desc: 付款方式adapter
 */
public final class PayMethodAdapter extends BaseAdapter {

    private Context mContext;
    private List<PressBean> mList;

    public PayMethodAdapter(Context context, List<PressBean> list) {
        mContext = context;
        mList = list;
    }

    @Override
    public int getCount() {
        return mList == null ? 0 : mList.size();
    }

    @Override
    public PressBean getItem(int position) {
        return mList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = LayoutInflater.from(mContext).inflate(R.layout.item_pay_methods, parent, false);
            holder.ll_item = convertView.findViewById(R.id.ll_item);
            holder.iv_checked = convertView.findViewById(R.id.iv_checked);
            holder.tv_method = convertView.findViewById(R.id.tv_method);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        if (getItem(position).isChoose()) {
            holder.iv_checked.setImageResource(R.drawable.radio_checked_shape);
        } else {
            holder.iv_checked.setImageResource(R.drawable.radio_uncheck_shape);
        }
        holder.tv_method.setText(getItem(position).getTitle());
        // 点击事件
        holder.ll_item.setOnClickListener(v -> {
            mOnClickItem.onClickItem(position);
            notifyDataSetChanged();
        });
        return convertView;
    }

    public interface OnClickItem {
        void onClickItem(int position);
    }

    private OnClickItem mOnClickItem;

    public void setOnClickItem(OnClickItem onClickItem) {
        mOnClickItem = onClickItem;
    }

    private class ViewHolder {
        LinearLayout ll_item;
        ImageView iv_checked;
        TextView tv_method;
    }
}
