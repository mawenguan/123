package com.example.day3_1_youlou.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by mwg on 2017/11/30.
 */

public class MyFragmentPagerAdapter extends FragmentPagerAdapter {
    public MyFragmentPagerAdapter(FragmentManager fm) {
        super(fm);
    }
    //封装一个集合，用来保存要适配的Fragment
    private List<Fragment> fragments = new ArrayList<Fragment>();
    //获得一个Fragment添加到集合中
    public void addFragment(Fragment fragment){
        if(fragment!=null){
            fragments.add(fragment);
            notifyDataSetChanged();
        }
    }
    @Override
    public Fragment getItem(int position) {
        return fragments.get(position);
    }

    @Override
    public int getCount() {
        return fragments.size();
    }
}
