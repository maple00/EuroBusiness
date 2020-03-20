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
 * @Author: a797s
 * @Date: 2020/2/18
 * @Desc: 商品详情  ----- CommUIBean 格式
 */
public class PurchaseLabelsAdapter extends BaseAdapter {

    private Context mContext;
    private List<CommonUIBean> mList;

    public PurchaseLabelsAdapter(Context mContext, List<CommonUIBean> mList) {
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
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = LayoutInflater.from(mContext).inflate(R.layout.item_purchase_labels, parent, false);
            holder.tv_title = convertView.findViewById(R.id.tv_title);
            holder.tv_show_text = convertView.findViewById(R.id.tv_show_text);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        holder.tv_title.setText(getItem(position).getTitle());
        if (getItem(position).getTitle().equals("总计")){
            holder.tv_show_text.setTextSize(15);
            holder.tv_show_text.getPaint().setFakeBoldText(true);
        }else {
            holder.tv_show_text.setTextSize(13);
        }
        holder.tv_show_text.setText(getItem(position).getShowText());
        return convertView;
    }

    private class ViewHolder {
        private TextView tv_title, tv_show_text;
    }
}
