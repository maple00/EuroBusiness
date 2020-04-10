package com.rainwood.eurobusiness.ui.adapter;

import android.content.Context;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.TextView;

import com.rainwood.eurobusiness.R;
import com.rainwood.eurobusiness.domain.CommonUIBean;
import com.rainwood.tools.toast.ToastUtils;

import java.util.List;

/**
 * @Author: a797s
 * @Date: 2020/4/8 17:00
 * @Desc: 折扣计算
 */
public final class DiscountAdapter extends BaseAdapter {

    private Context mContext;
    private List<CommonUIBean> mList;
    private final int VALUE = 50000;

    public DiscountAdapter(Context context, List<CommonUIBean> list) {
        mContext = context;
        mList = list;
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
            convertView = LayoutInflater.from(mContext).inflate(R.layout.item_discount, parent, false);
            holder.tv_title = convertView.findViewById(R.id.tv_title);
            holder.et_value = convertView.findViewById(R.id.et_value);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        holder.tv_title.setText(getItem(position).getTitle());
        holder.et_value.setTag(position + VALUE);
        if (TextUtils.isEmpty(getItem(position).getShowText()) || Double.parseDouble(getItem(position).getShowText()) <= 0.0) {
            //holder.et_value.setHint(mContext.getResources().getString(R.string.common_input));
            holder.et_value.setText("0");
        } else {
            holder.et_value.setHint(getItem(position).getShowText());
        }
        // 设置回调监听
        holder.et_value.setFocusable(true);
        holder.et_value.addTextChangedListener(new TextChangerListener(this, position, holder));
        return convertView;
    }

    public interface OnClickEdit {
        // 回调监听
        void onTextWatcher(DiscountAdapter adapter, int position);
    }

    private OnClickEdit mOnClickEdit;

    public void setOnClickEdit(OnClickEdit onClickEdit) {
        mOnClickEdit = onClickEdit;
    }

    private class TextChangerListener implements TextWatcher {

        private DiscountAdapter adapter;
        private int position;
        private ViewHolder holder;

        public TextChangerListener(DiscountAdapter adapter, int position, ViewHolder holder) {
            this.adapter = adapter;
            this.position = position;
            this.holder = holder;
        }

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            int tag = (int) holder.et_value.getTag();
            if (tag == position + VALUE) {
                adapter.getItem(position).setShowText(s.toString());
            }
        }

        @Override
        public void afterTextChanged(Editable s) {
            mOnClickEdit.onTextWatcher(adapter, position);
        }
    }

    class ViewHolder {
        private TextView tv_title;
        private EditText et_value;
    }
}
