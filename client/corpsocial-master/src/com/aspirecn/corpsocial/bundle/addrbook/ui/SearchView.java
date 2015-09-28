package com.aspirecn.corpsocial.bundle.addrbook.ui;

import android.content.Context;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;

import com.aspiren.corpsocial.R;

/**
 * Created by yinghuihong on 15/5/26.
 * <p/>
 * 自定义搜索栏
 */
public class SearchView extends LinearLayout {

    private EditText etSearch;

    private ImageButton imgBtnDelete;
    private OnTextChangedListener mListener;

    public SearchView(Context context) {
        super(context);
        init(context);
    }

    public SearchView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public SearchView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(context);
    }

    private void init(Context context) {
        View view = LayoutInflater.from(context).inflate(R.layout.search_view, this);
        etSearch = (EditText) view.findViewById(R.id.et_search);
        imgBtnDelete = (ImageButton) view.findViewById(R.id.img_btn_delete);

        imgBtnDelete.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                etSearch.setText(null);
            }
        });

        etSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (TextUtils.isEmpty(charSequence)) {
                    imgBtnDelete.setVisibility(View.GONE);
                } else {
                    imgBtnDelete.setVisibility(View.VISIBLE);
                }

                if (mListener != null) {
                    mListener.onTextChanged(charSequence);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
    }

    public void setOnTextChangedListener(OnTextChangedListener listener) {
        mListener = listener;
    }

    public interface OnTextChangedListener {
        void onTextChanged(CharSequence charSequence);
    }


}
