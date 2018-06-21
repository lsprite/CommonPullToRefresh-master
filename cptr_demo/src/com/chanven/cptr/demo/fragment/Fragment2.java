package com.chanven.cptr.demo.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.chanven.cptr.demo.BaseFragment;
import com.chanven.cptr.demo.R;

/**
 * Created by Administrator on 2018/6/15.
 */

public class Fragment2 extends BaseFragment {
    private View view;
    //
    //
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        view = inflater.inflate(R.layout.fragment2, container, false);
        initView();
        return view;
    }

    private void initView() {
        // TODO Auto-generated method stub

    }
}
