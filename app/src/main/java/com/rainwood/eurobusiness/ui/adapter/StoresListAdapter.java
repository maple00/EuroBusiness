package com.rainwood.eurobusiness.ui.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.rainwood.eurobusiness.R;
import com.rainwood.eurobusiness.domain.CommonUIBean;
import com.rainwood.tools.viewinject.ViewById;

import java.util.List;

/**
 * @Author: a797s
 * @Date: 2020/2/7
 * @Desc: 门店信息
 */
public class StoresListAdapter extends BaseAdapter {

    private Context mContext;
    private List<CommonUIBean> mList;

    public StoresListAdapter(Context mContext, List<CommonUIBean> mList) {
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
            convertView = LayoutInflater.from(mContext).inflate(R.layout.item_stores, parent, false);
            holder.tv_title = convertView.findViewById(R.id.tv_title);
            holder.tv_label = convertView.findViewById(R.id.tv_label);
            convertView.setTag(holder);
        }else {
            holder = (ViewHolder) convertView.getTag();
        }
        holder.tv_title.setText(getItem(position).getTitle());
        if (TextUtils.isEmpty(getItem(position).getShowText())){
            holder.tv_label.setText(getItem(position).getLabel());
        }else {
            holder.tv_label.setText(getItem(position).getShowText());
        }
        return convertView;
    }

    private class ViewHolder{
        private TextView tv_title, tv_label;
    }
}
