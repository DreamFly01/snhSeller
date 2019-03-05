package com.snh.snhseller.ui.salesmanManagement.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;

import com.snh.snhseller.ui.salesmanManagement.cardRecord.SimpleFragment;
import com.test.tudou.library.WeekPager.adapter.WeekPagerAdapter;

/**
 * <p>desc：<p>
 * <p>author：DreamFly<p>
 * <p>creatTime：2019/3/4<p>
 * <p>changeTime：2019/3/4<p>
 * <p>version：1<p>
 */
public class SimplePagerAdapter extends WeekPagerAdapter {

    public SimplePagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override protected Fragment createFragmentPager(int position) {
        return SimpleFragment.newInstance(mDays.get(position));
    }
}
