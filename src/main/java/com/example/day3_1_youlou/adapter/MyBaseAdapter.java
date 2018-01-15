package com.example.day3_1_youlou.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by mwg on 2017/12/2.
 */

public abstract class MyBaseAdapter<T> extends BaseAdapter {

    protected LayoutInflater layoutInflater;
    protected Context context;

    public MyBaseAdapter(Context context) {
        this.context = context;
        layoutInflater = LayoutInflater.from(context);

    }

    //定义一个泛型（类型参数化）
    private List<T> datas = new ArrayList<T>();


    /*
     * 为集合中添加数据
     */
    public void addDatas(List<T> list,boolean isClear){
        if(isClear){
            datas.clear();
        }
        if (list!=null){
            datas.addAll(list);
            //通知绑定适配器的UI进行数据更新
            notifyDataSetChanged();
        }
    }
    /*
     * 删除集合中的所有数据
     */
    public void removeDatas(){
        datas.clear();
        notifyDataSetChanged();
    }
    /*
     * 删除集合中指定数据
     */
    public void removeDatas(T t){
        datas.remove(t);
        notifyDataSetChanged();
    }
    /*
     * 查看（获得）集合中的数据
     */
    public List<T> getDatas(){
        return datas;
    }

    @Override
    public int getCount() {
        return datas.size();
    }

    @Override
    public T getItem(int position) {
        return datas.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public abstract View getView(int position, View convertView, ViewGroup parent);
}
