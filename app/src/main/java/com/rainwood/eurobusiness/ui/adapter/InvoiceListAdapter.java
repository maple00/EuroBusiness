package com.rainwood.eurobusiness.ui.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.rainwood.eurobusiness.R;
import com.rainwood.eurobusiness.domain.InvoiceBean;

import java.util.List;

/**
 * @Author: a797s
 * @Date: 2020/2/7
 * @Desc:
 */
public class InvoiceListAdapter extends BaseAdapter {

    private Context mContext;
    private List<InvoiceBean> mList;

    public InvoiceListAdapter(Context mContext, List<InvoiceBean> mList) {
        this.mContext = mContext;
        this.mList = mList;
    }

    @Override
    public int getCount() {
        return mList == null ? 0 : mList.size();
    }

    @Override
    public InvoiceBean getItem(int position) {
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
            convertView = LayoutInflater.from(mContext).inflate(R.layout.item_invoice, parent, false);
            holder.tv_company = convertView.findViewById(R.id.tv_company);
            holder.tv_tax_num = convertView.findViewById(R.id.tv_tax_num);
            holder.tv_copy_tax = convertView.findViewById(R.id.tv_copy_tax);
            holder.tv_location = convertView.findViewById(R.id.tv_location);
            holder.tv_address = convertView.findViewById(R.id.tv_address);
            holder.tv_tel = convertView.findViewById(R.id.tv_tel);
            holder.tv_copy_all_tax = convertView.findViewById(R.id.tv_copy_all_tax);
            convertView.setTag(holder);
        }else {
            holder = (ViewHolder) convertView.getTag();
        }
        holder.tv_company.setText(getItem(position).getCompany());
        holder.tv_tax_num.setText(getItem(position).getTaxP());
        holder.tv_location.setText(getItem(position).getLocation());
        holder.tv_address.setText(getItem(position).getAddress());
        holder.tv_tel.setText(getItem(position).getTel());
        // 复制税号
        holder.tv_copy_tax.setOnClickListener(v -> iCopyContext2Board.OnCopyTaxNum(position));

        // 复制开票单上所有的信息
        holder.tv_copy_all_tax.setOnClickListener(v -> iCopyContext2Board.OnCopyAllContext(position));

        return convertView;
    }

    public interface ICopyContext2Board{
        // 复制税号
        void OnCopyTaxNum(int position);
        // 复制所有信息
        void  OnCopyAllContext(int position);
    }

    private ICopyContext2Board iCopyContext2Board;

    public void setiCopyContext2Board(ICopyContext2Board iCopyContext2Board) {
        this.iCopyContext2Board = iCopyContext2Board;
    }

    private class ViewHolder {
        private TextView tv_company, tv_tax_num, tv_copy_tax,
                tv_location, tv_address, tv_tel, tv_copy_all_tax;
    }
}
