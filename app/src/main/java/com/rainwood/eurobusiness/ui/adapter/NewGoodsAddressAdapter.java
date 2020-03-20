package com.rainwood.eurobusiness.ui.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.rainwood.eurobusiness.R;
import com.rainwood.eurobusiness.domain.CommonUIBean;

import java.util.List;

/**
 * @Author: shearson
 * @Time: 2020/2/24 17:29
 * @Desc: 新增收货地址
 */
public class NewGoodsAddressAdapter extends BaseAdapter {

    private Context mContext;
    private List<CommonUIBean> mList;

    public NewGoodsAddressAdapter(Context mContext, List<CommonUIBean> mList) {
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
        ViewHoler holer;
        if (convertView == null) {
            holer = new ViewHoler();
            convertView = LayoutInflater.from(mContext).inflate(R.layout.item_new_goods_address, parent, false);
            holer.tv_title = convertView.findViewById(R.id.tv_title);
            holer.et_hint = convertView.findViewById(R.id.et_hint);
            holer.iv_right_arrow = convertView.findViewById(R.id.iv_right_arrow);
            convertView.setTag(holer);
        } else {
            holer = (ViewHoler) convertView.getTag();
        }
        if (getItem(position).getArrowType() == 1) {     // 请选择之类
            holer.iv_right_arrow.setVisibility(View.VISIBLE);
        } else {
            holer.iv_right_arrow.setVisibility(View.GONE);
        }

        if (TextUtils.isEmpty(getItem(position).getShowText())) {
            holer.et_hint.setHint(getItem(position).getLabel());
        } else {
            holer.et_hint.setText(getItem(position).getShowText());
        }
        holer.tv_title.setText(getItem(position).getTitle());
        return convertView;
    }

    private class ViewHoler {
        private TextView tv_title;
        private EditText et_hint;
        private ImageView iv_right_arrow;
    }
}
