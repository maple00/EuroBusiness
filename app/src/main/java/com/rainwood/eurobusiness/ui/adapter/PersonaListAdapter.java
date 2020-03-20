package com.rainwood.eurobusiness.ui.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.rainwood.eurobusiness.R;
import com.rainwood.eurobusiness.domain.PersonalListBean;
import com.rainwood.eurobusiness.utils.CacheManagerUtils;

import java.util.List;
import java.util.Objects;

/**
 * @Author: a797s
 * @Date: 2020/2/7
 * @Desc: 个人中心Adapter
 */
public class PersonaListAdapter extends BaseAdapter {

    private Context mContext;
    private List<PersonalListBean> mList;

    public PersonaListAdapter(Context mContext, List<PersonalListBean> mList) {
        this.mContext = mContext;
        this.mList = mList;
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return mList == null ? 0 : mList.size();
    }

    @Override
    public PersonalListBean getItem(int position) {
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
            convertView = LayoutInflater.from(mContext).inflate(R.layout.item_personal, parent, false);
            holder.iv_icon = convertView.findViewById(R.id.iv_icon);
            holder.tv_module = convertView.findViewById(R.id.tv_module);
            holder.tv_note = convertView.findViewById(R.id.tv_note);
            holder.ll_item = convertView.findViewById(R.id.ll_item);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        holder.iv_icon.setBackgroundResource(getItem(position).getImgPath());
        holder.tv_module.setText(getItem(position).getName());
        // 加载缓存大小
        if (getItem(position).getName().equals("清理缓存")) {
            try {
                String totalCacheSize = CacheManagerUtils.getTotalCacheSize((mContext));
                holder.tv_note.setText(totalCacheSize);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }else {
            holder.tv_note.setText("");
        }

        holder.ll_item.setOnClickListener(v -> {
            itemClick.OnItemClick(position);
            notifyDataSetChanged();
        });
        return convertView;
    }

    public interface IOnItemClick {
        void OnItemClick(int position);
    }

    private IOnItemClick itemClick;

    public void setItemClick(IOnItemClick itemClick) {
        this.itemClick = itemClick;
    }

    private class ViewHolder {
        private ImageView iv_icon;
        private TextView tv_module, tv_note;
        private LinearLayout ll_item;
    }
}
