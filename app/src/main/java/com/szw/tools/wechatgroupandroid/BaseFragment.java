package com.szw.tools.wechatgroupandroid;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by SuZhiwei on 2017/9/21.
 */
public abstract class BaseFragment extends Fragment {
    private View view;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
         view = inflater.inflate(getLayout(), null);
        return view;
    }

    protected abstract int getLayout();

    public View findViewById(int id){
       return view.findViewById(id);
    }
}
