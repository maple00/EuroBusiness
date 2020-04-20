package com.rainwood.eurobusiness.ui.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.rainwood.eurobusiness.R;
import com.rainwood.eurobusiness.domain.TitleAndLabelBean;

import java.util.List;

/**
 * @Author: a797s
 * @Date: 2020/4/14 13:37
 * @Desc: 补货参数
 */
public final class ReplementParamsAdapter extends BaseAdapter {

    private Context mContext;
    private List<TitleAndLabelBean> mList;

    public ReplementParamsAdapter(Context context, List<TitleAndLabelBean> list) {
        mContext = context;
        mList = list;
    }

    @Override

    public int getCount() {
        return mList == null ? 0 : mList.size();
    }

    @Override
    public TitleAndLabelBean getItem(int position) {
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
            convertView = LayoutInflater.from(mContext).inflate(R.layout.item_common_label, parent, false);
            holder.tv_title = convertView.findViewById(R.id.tv_title);
            holder.tv_label = convertView.findViewById(R.id.tv_label);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        holder.tv_label.setText(getItem(position).getLabel());
        holder.tv_title.setText(getItem(position).getTitle());
        holder.tv_title.setTextSize(getItem(position).getFontSize());
        holder.tv_label.setTextSize(getItem(position).getFontSize());
        holder.tv_label.setTextColor(getItem(position).getLabelColorSize());
        return convertView;
    }

    private class ViewHolder {
        private TextView tv_title, tv_label;
    }
}
