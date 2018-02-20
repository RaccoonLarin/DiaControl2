package com.example.salima.diacontrol;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Salima on 14.01.2018.
 */

public class SectionsStatePagerAdapter extends FragmentPagerAdapter {

    private Map<Integer,String> mFragmentTags;
    private  FragmentManager mFragmentManager;
    private Context mContext;

    private final List<Fragment> mFragmentList =  new ArrayList<>();
    private final List<Fragment> mFragmentTitleList =  new ArrayList<>();

    public SectionsStatePagerAdapter(FragmentManager fm, Context ct) {
        super(fm);
        mFragmentManager=fm;
        mFragmentTags=new HashMap<Integer, String>();
        mContext= ct;
    }

    public void addFragment(Fragment fragment){
        mFragmentList.add(fragment);
    }

    @Override
    public Fragment getItem(int position) {
        switch (position){
            case 0: return new DiaryFragment();
        }
        return null;

    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        Object obj=super.instantiateItem(container, position);
        if(obj instanceof Fragment){
            Fragment f = (Fragment) obj;
            String tag=f.getTag();
            mFragmentTags.put(position,tag);
        }
        return obj;
    }

    public Fragment getFragment(int position){
        String tag = mFragmentTags.get(position);
        if (tag==null)
            return null;
        return  mFragmentManager.findFragmentByTag(tag);
    }

    @Override
    public int getCount() {
        return mFragmentList.size();
    }
}
