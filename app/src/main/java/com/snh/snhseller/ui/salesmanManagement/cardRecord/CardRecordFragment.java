package com.snh.snhseller.ui.salesmanManagement.cardRecord;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.snh.snhseller.BaseFragment;
import com.snh.snhseller.R;
import com.snh.snhseller.ui.salesmanManagement.adapter.SimplePagerAdapter;
import com.test.tudou.library.WeekPager.adapter.WeekViewAdapter;
import com.test.tudou.library.WeekPager.view.WeekDayViewPager;
import com.test.tudou.library.WeekPager.view.WeekRecyclerView;
import com.test.tudou.library.model.CalendarDay;
import com.test.tudou.library.util.DayUtils;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * <p>desc：<p>
 * <p>author：DreamFly<p>
 * <p>creatTime：2019/2/26<p>
 * <p>changeTime：2019/2/26<p>
 * <p>version：1<p>
 */
public class CardRecordFragment extends BaseFragment implements WeekDayViewPager.DayScrollListener {

    private final static String TAG = "WeekPagerActivity";


    @BindView(R.id.heard_back)
    LinearLayout heardBack;
    @BindView(R.id.heard_title)
    TextView heardTitle;
    @BindView(R.id.heard_menu)
    ImageView heardMenu;
    @BindView(R.id.heard_tv_menu)
    TextView heardTvMenu;
    @BindView(R.id.rl_head)
    LinearLayout rlHead;
    @BindView(R.id.header_recycler_view)
    WeekRecyclerView mWeekRecyclerView;
    @BindView(R.id.text_day_label)
    TextView mTextView;
    @BindView(R.id.view_pager)
    WeekDayViewPager mViewPagerContent;
    Unbinder unbinder;

    private SimplePagerAdapter mPagerAdapter;
    private WeekViewAdapter mWeekViewAdapter;
    private static final int OFFSCREEN_PAGE_LIMIT = 1;

    @Override
    public int initContentView() {
        return R.layout.fragment_cardrecord_layout;
    }

    @Override
    public void setUpViews(View view) {
        setUpPager();
        setUpData();
    }

    @Override
    public void setUpLisener() {

    }

    private void setUpPager() {
        mPagerAdapter = new SimplePagerAdapter(getActivity().getSupportFragmentManager());
        mViewPagerContent.setOffscreenPageLimit(OFFSCREEN_PAGE_LIMIT);
        mViewPagerContent.setAdapter(mPagerAdapter);
        mViewPagerContent.setWeekRecyclerView(mWeekRecyclerView);
        mViewPagerContent.setDayScrollListener(this);
        mWeekViewAdapter = new WeekViewAdapter(getContext(), mViewPagerContent);
        mWeekViewAdapter.setTextNormalColor(getResources().getColor(android.R.color.darker_gray));
        mWeekRecyclerView.setAdapter(mWeekViewAdapter);
    }

    private void setUpData() {
        ArrayList<CalendarDay> reachAbleDays = new ArrayList<>();
        reachAbleDays.add(new CalendarDay(2015, 5, 1));
        reachAbleDays.add(new CalendarDay(2015, 5, 4));
        reachAbleDays.add(new CalendarDay(2015, 5, 6));
        reachAbleDays.add(new CalendarDay(2015, 5, 20));
        mWeekViewAdapter.setData(reachAbleDays.get(0), reachAbleDays.get(reachAbleDays.size() - 1), null);
        mPagerAdapter.setData(reachAbleDays.get(0), reachAbleDays.get(reachAbleDays.size() - 1));
//        mViewPagerContent.set(DayUtils.calculateDayPosition(mWeekViewAdapter.getItemCount(), new CalendarDay(2015, 5, 6)));
    }

    @Override
    public void onDayPageScrolled(int position, float positionOffset,
                                  int positionOffsetPixels) {
        mTextView.setText(DayUtils.formatEnglishTime(mPagerAdapter.getDatas().get(position).getTime()));
    }

    @Override
    public void onDayPageSelected(int position) {
    }

    @Override
    public void onDayPageScrollStateChanged(int state) {

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // TODO: inflate a fragment view
        View rootView = super.onCreateView(inflater, container, savedInstanceState);
        unbinder = ButterKnife.bind(this, rootView);
        return rootView;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
}
