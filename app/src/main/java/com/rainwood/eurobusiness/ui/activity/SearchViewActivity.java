package com.rainwood.eurobusiness.ui.activity;

import android.content.Context;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.rainwood.eurobusiness.R;
import com.rainwood.eurobusiness.base.BaseActivity;
import com.rainwood.eurobusiness.helper.DbDao;
import com.rainwood.eurobusiness.ui.adapter.SeachRecordAdapter;
import com.rainwood.tools.viewinject.ViewById;

/**
 * @Author: a797s
 * @Date: 2019/12/5 11:44
 * @Desc: 含历史记录的搜索框
 */
public class SearchViewActivity extends BaseActivity implements View.OnClickListener {


    @Override
    protected int getLayoutId() {
        return R.layout.activity_searchview;
    }

    @ViewById(R.id.btn_search)
    private Button mbtn_search;
    @ViewById(R.id.et_search)
    private EditText met_search;

    @ViewById(R.id.btn_back)
    private ImageView backBtn;

    @ViewById(R.id.mRecyclerView)
    private RecyclerView mRecyclerView;
    @ViewById(R.id.tv_deleteAll)
    private TextView mtv_deleteAll;
    private SeachRecordAdapter mAdapter;

    // 数据库
    private DbDao mDbDao;

    @Override
    protected void initView() {
        mDbDao = new DbDao(this);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mAdapter = new SeachRecordAdapter(mDbDao.queryData(""), this);
        // 查询历史记录
        mAdapter.setRvItemOnclickListener(position -> {
            mDbDao.delete(mDbDao.queryData("").get(position));
            mAdapter.updata(mDbDao.queryData(""));
        });
        mRecyclerView.setAdapter(mAdapter);
        // 历史记录点击
        mAdapter.setOnClickRecord(new SeachRecordAdapter.OnClickRecord() {
            @Override
            public void onClickRecord(String text) {
                // toast(text);
                met_search.setText(text);
            }
        });
        // 事件监听
        mbtn_search.setOnClickListener(this);
        backBtn.setOnClickListener(this);
        mtv_deleteAll.setOnClickListener(this);
        // EditText自动聚焦
        showSoftInputFromWindow(met_search);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_back:
                finish();
                break;
            case R.id.tv_deleteAll:
                mDbDao.deleteData();
                mAdapter.updata(mDbDao.queryData(""));
                break;
            case R.id.btn_search:
                //事件监听
                if (met_search.getText().toString().trim().length() != 0) {
                    // 查询数据库历史记录
                    boolean hasData = mDbDao.hasData(met_search.getText().toString().trim());
                    if (!hasData) {
                        mDbDao.insertData(met_search.getText().toString().trim());
                    } else {
                        toast("该内容已在历史记录中");
                    }
                    mAdapter.updata(mDbDao.queryData(""));
                    /*
                    去查询的地方
                     */

                } else {
                    toast("请输入内容");
                }
                break;
        }
    }

    /**
     * EditText 自动聚焦且弹出软键盘
     */
    public void showSoftInputFromWindow(EditText editText) {
        editText.setFocusable(true);
        editText.setFocusableInTouchMode(true);
        editText.requestFocus();
        InputMethodManager inputManager =
                (InputMethodManager) editText.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        inputManager.showSoftInput(editText, 0);
    }


}
