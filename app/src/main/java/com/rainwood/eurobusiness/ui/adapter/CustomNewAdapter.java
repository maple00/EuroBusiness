package com.rainwood.eurobusiness.ui.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.rainwood.eurobusiness.R;
import com.rainwood.eurobusiness.domain.NewCommUIBean;
import com.rainwood.tools.widget.MeasureListView;

import java.util.List;

/**
 * @Author: a797s
 * @Date: 2020/2/23
 * @Desc: 新建客户
 */
public class CustomNewAdapter extends BaseAdapter {

    private Context mContext;
    private List<NewCommUIBean> mList;

    public CustomNewAdapter(Context mContext, List<NewCommUIBean> mList) {
        this.mContext = mContext;
        this.mList = mList;
    }

    @Override
    public int getCount() {
        return mList == null ? 0 : mList.size();
    }

    @Override
    public NewCommUIBean getItem(int position) {
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
            convertView = LayoutInflater.from(mContext).inflate(R.layout.item_new_commui, parent, false);
            holder.tv_title = convertView.findViewById(R.id.tv_title);
            holder.lv_list = convertView.findViewById(R.id.lv_list);
            convertView.setTag(holder);
        }else {
            holder = (ViewHolder) convertView.getTag();
        }
        if (TextUtils.isEmpty(getItem(position).getTitle())){
            holder.tv_title.setVisibility(View.GONE);
        }else {
            holder.tv_title.setVisibility(View.VISIBLE);
            holder.tv_title.setText(getItem(position).getTitle());
        }
        // commUi
        CommUIAdapter uiAdapter = new CommUIAdapter(mContext, getItem(position).getCommonUIList());
        holder.lv_list.setAdapter(uiAdapter);
        uiAdapter.setOnClickEditText(position, onClickEditText);
        return convertView;
    }

    private CommUIAdapter.OnClickEditText onClickEditText;

    public void setOnClickEditText(CommUIAdapter.OnClickEditText onClickEditText) {
        this.onClickEditText = onClickEditText;
    }

    private class ViewHolder{
        private TextView tv_title;
        private MeasureListView lv_list;
    }
}
