package com.rainwood.eurobusiness.ui.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.rainwood.eurobusiness.R;
import com.rainwood.eurobusiness.domain.ItemGridBean;

import java.util.List;

/**
 * @Author: a797s
 * @Date: 2020/2/18
 * @Desc:
 */
public class LevelTypeAdapter extends BaseAdapter {

    private Context mContext;
    private List<ItemGridBean> mList;

    public LevelTypeAdapter(Context mContext, List<ItemGridBean> mList) {
        this.mContext = mContext;
        this.mList = mList;
    }

    @Override
    public int getCount() {
        return mList == null ? 0 : mList.size();
    }

    @Override
    public ItemGridBean getItem(int position) {
        return mList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHodler hodler;
        if (convertView == null) {
            hodler = new ViewHodler();
            convertView = LayoutInflater.from(mContext).inflate(R.layout.item_level_type, parent, false);
            hodler.tv_level = convertView.findViewById(R.id.tv_level);
            hodler.iv_selector = convertView.findViewById(R.id.iv_selector);
            convertView.setTag(hodler);
        } else {
            hodler = (ViewHodler) convertView.getTag();
        }
        hodler.tv_level.setText(getItem(position).getItemName());
        hodler.iv_selector.setImageResource(R.drawable.ic_down_selector);
        return convertView;
    }

    private class ViewHodler {
        private TextView tv_level;
        private ImageView iv_selector;
    }
}
