package com.rainwood.eurobusiness.ui.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.rainwood.eurobusiness.R;
import com.rainwood.eurobusiness.domain.CommonUIBean;

import java.util.List;

/**
 * @Author: a797s
 * @Date: 2020/2/11
 * @Desc: 商品定价
 */
public class ShopPriceAdapter extends BaseAdapter {

    private Context mContext;
    private List<CommonUIBean> mList;

    public ShopPriceAdapter(Context mContext, List<CommonUIBean> mList) {
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
            convertView = LayoutInflater.from(mContext).inflate(R.layout.item_sub_price_params,
                    parent, false);
            holder = new ViewHolder();
            holder.tv_title = convertView.findViewById(R.id.tv_title);
            holder.et_hint = convertView.findViewById(R.id.et_hint);
            holder.rg_choose = convertView.findViewById(R.id.rg_choose);
            holder.rb_text1 = convertView.findViewById(R.id.rb_text1);
            holder.rb_text2 = convertView.findViewById(R.id.rb_text2);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        holder.tv_title.setText(getItem(position).getTitle());
        if (getItem(position).getTitle().equals("增值税")) {
            holder.et_hint.setVisibility(View.GONE);
            holder.rg_choose.setVisibility(View.VISIBLE);
            holder.rb_text1.setText("不含税");
            holder.rb_text2.setText("含税");
        } else {
            holder.et_hint.setVisibility(View.VISIBLE);
            holder.rg_choose.setVisibility(View.GONE);
            holder.et_hint.setHint(getItem(position).getLabel());
        }
        return convertView;
    }

    private class ViewHolder {
        private TextView tv_title;
        private EditText et_hint;
        private RadioGroup rg_choose;
        private RadioButton rb_text1, rb_text2;
    }
}
