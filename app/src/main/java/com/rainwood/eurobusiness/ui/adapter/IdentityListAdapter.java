package com.rainwood.eurobusiness.ui.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;

import com.rainwood.eurobusiness.R;
import com.rainwood.eurobusiness.domain.PressBean;

import java.util.List;

/**
 * @Author: a797s
 * @Date: 2020/2/6
 * @Desc: 身份选择列表ListView
 */
public class IdentityListAdapter extends BaseAdapter {

    private Context mContext;
    private List<PressBean> mList;

    @SuppressLint("LongLogTag")
    public IdentityListAdapter(Context mContext, List<PressBean> mList) {
        this.mContext = mContext;
        this.mList = mList;
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

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = LayoutInflater.from(mContext).inflate(R.layout.item_identity, parent, false);
            holder.tv_title = convertView.findViewById(R.id.tv_title);
            holder.iv_choose = convertView.findViewById(R.id.iv_choose);
            holder.ll_identity_item = convertView.findViewById(R.id.ll_identity_item);
            holder.rb_choose = convertView.findViewById(R.id.rb_choose);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        holder.tv_title.setText(getItem(position).getTitle());

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            if (getItem(position).isChoose()) {      // 如果处于选中状态
                holder.ll_identity_item.setBackground(mContext.getDrawable(R.drawable.shape_text_red30));
                holder.rb_choose.setVisibility(View.GONE);
                holder.iv_choose.setVisibility(View.VISIBLE);
                holder.iv_choose.setBackground(mContext.getDrawable(R.drawable.ic_choose));
            } else {                     // 未选中状态时，置为默认状态
                holder.ll_identity_item.setBackground(mContext.getDrawable(R.drawable.shape_identity_white));
                holder.rb_choose.setVisibility(View.VISIBLE);
                holder.iv_choose.setVisibility(View.GONE);
            }
        }
        // 选中状态
        holder.ll_identity_item.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                identity.itemClick(position);
                notifyDataSetChanged();
            }
        });
        return convertView;
    }

    public interface IdentityItem{
        void itemClick(int position);
    }

    private IdentityItem identity;

    public void setIdentity(IdentityItem identity) {
        this.identity = identity;
    }

    private class ViewHolder {
        private TextView tv_title;
        private ImageView iv_choose;
        private LinearLayout ll_identity_item;
        private RadioButton rb_choose;
    }
}
