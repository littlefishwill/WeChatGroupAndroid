package com.szw.tools.wechatgroupandroid.view.adapter;

/**
 * Created by SuZhiwei on 2017/9/21.
 */
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zhy on 16/4/9.
 */
public abstract class CommonAdapter<T> extends RecyclerView.Adapter<CommonViewHolder>
{
    protected Context mContext;
    protected int mLayoutId;
    protected List<T> mDatas;
    protected LayoutInflater mInflater;

    public void addDoamin(T object){
        if(mDatas==null){
            mDatas = new ArrayList<>();
        }
        mDatas.add(0,object);
        notifyDataSetChanged();
    }

    public void changeData(List<T> datas){
        mDatas = datas;
        notifyDataSetChanged();
    }

    public CommonAdapter(Context context, int layoutId, List<T> datas)
    {
        mContext = context;
        mInflater = LayoutInflater.from(context);
        mLayoutId = layoutId;
        mDatas = datas;
    }

    public List<T> getmDatas(){
        return mDatas;
    }

    @Override
    public CommonViewHolder onCreateViewHolder(final ViewGroup parent, int viewType)
    {
        CommonViewHolder viewHolder = CommonViewHolder.get(mContext, parent, mLayoutId);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(CommonViewHolder holder, int position)
    {
        convert(holder, mDatas.get(position),position);
    }

    public abstract void convert(CommonViewHolder holder, T t,int posation);

    @Override
    public int getItemCount()
    {
        return mDatas.size();
    }
}