package com.snh.snhseller.ui.salesmanManagement.cardRecord;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.gyf.barlibrary.ImmersionBar;
import com.haibin.calendarview.Calendar;
import com.haibin.calendarview.CalendarLayout;
import com.haibin.calendarview.CalendarView;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.DefaultRefreshFooterCreator;
import com.scwang.smartrefresh.layout.api.RefreshFooter;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.footer.ClassicsFooter;
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.snh.snhseller.BaseFragment;
import com.snh.snhseller.R;
import com.snh.snhseller.bean.BaseResultBean;
import com.snh.snhseller.bean.salebean.RecordBean;
import com.snh.snhseller.requestApi.NetSubscriber;
import com.snh.snhseller.requestApi.RequestClient;
import com.snh.snhseller.ui.salesmanManagement.adapter.RecordAdapter;
import com.snh.snhseller.utils.IsBang;
import com.snh.snhseller.utils.TimeUtils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;


/**
 * <p>desc：<p>
 * <p>author：DreamFly<p>
 * <p>creatTime：2019/3/4<p>
 * <p>changeTime：2019/3/4<p>
 * <p>version：1<p>
 */
public class RecordFragment extends BaseFragment implements CalendarView.OnCalendarSelectListener, CalendarView.OnYearChangeListener, CalendarView.OnCalendarInterceptListener {
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
    @BindView(R.id.calendarView)
    CalendarView calendarView;
    @BindView(R.id.calendarLayout)
    CalendarLayout calendarLayout;
    Unbinder unbinder;
    @BindView(R.id.tv_time)
    TextView tvTime;
    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    @BindView(R.id.refreshLayout)
    SmartRefreshLayout refreshLayout;
    private int mYear;
    private int index = 1;
    private RecordAdapter adapter;
    private boolean isShow = false;
    private String time;

    @Override
    public int initContentView() {
        return R.layout.fragment_record_layout;
    }

    @Override
    public void setUpViews(View view) {
        ImmersionBar.setTitleBar(getActivity(), rlHead);
        IsBang.setImmerHeard(getContext(), rlHead,"#2E8AFF");
        heardBack.setVisibility(View.GONE);
        heardTitle.setText("拜访记录");
        heardTvMenu.setText("今天");
        heardTvMenu.setTextColor(Color.WHITE);
        calendarView.setOnCalendarSelectListener(this);
        calendarView.setOnYearChangeListener(this);
        calendarView.setOnCalendarInterceptListener(this);
        setRecyclerView();
        time = TimeUtils.getDateString(System.currentTimeMillis());
        getData(time);
        tvTime.setText(calendarView.getCurYear() + "年" + calendarView.getCurMonth() + "月" + calendarView.getCurDay());
        refreshLayout.setEnableRefresh(false);
    }

    private void setRecyclerView() {
        adapter = new RecordAdapter(R.layout.item_record_layout, null);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(adapter);
    }

    @Override
    public void setUpLisener() {
        refreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                index = 1;
                getData(time);
            }
        });
        refreshLayout.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
                index += 1;
                getData(time);
            }
        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // TODO: inflate a fragment view
        View rootView = super.onCreateView(inflater, container, savedInstanceState);
        unbinder = ButterKnife.bind(this, rootView);
        return rootView;
    }


    //屏蔽某些日期
    @Override
    public boolean onCalendarIntercept(Calendar calendar) {
        String datastr = calendar.toString();
        return checkCalendar(datastr);
    }


    @Override
    public void onCalendarInterceptClick(Calendar calendar, boolean isClick) {

    }

    @Override
    public void onCalendarOutOfRange(Calendar calendar) {

    }

    //日历选择了某个日期
    @Override
    public void onCalendarSelect(Calendar calendar, boolean isClick) {
        isShow = true;
        time = calendar.getYear() + "-" + (calendar.getMonth() < 10 ? "0" + calendar.getMonth() : calendar.getMonth()) + "-" + (calendar.getDay() < 10 ? "0" + calendar.getDay() : calendar.getDay());
        index = 1;
        getData(time);
        tvTime.setText(calendar.getYear() + "年" + (calendar.getMonth() < 10 ? "0" + calendar.getMonth() : calendar.getMonth()) + "月" + (calendar.getDay() < 10 ? "0" + calendar.getDay() : calendar.getDay()));
    }

    @Override
    public void onYearChange(int year) {

    }

    private boolean checkCalendar(String datastr) {
        java.util.Calendar calendar1 = java.util.Calendar.getInstance();
        SimpleDateFormat format = new SimpleDateFormat("yyyyMMdd");
        java.util.Calendar calendar2 = java.util.Calendar.getInstance();

        try {
            Date data = format.parse(datastr);
            calendar2.setTime(data);
            return calendar1.before(calendar2);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return true;
    }

    @OnClick(R.id.heard_tv_menu)
    public void onClick() {
        calendarView.scrollToCurrent();
        isShow = true;
    }

    private void getData(String time) {
        addSubscription(RequestClient.GetClockInRecord(index, time, getContext(), new NetSubscriber<BaseResultBean<List<RecordBean>>>(getContext(), isShow) {
            @Override
            public void onResultNext(BaseResultBean<List<RecordBean>> model) {
                isShow = false;
                if (index == 1) {
                    refreshLayout.finishRefresh();
                    if (model.data.size() > 0) {
                        adapter.setNewData(model.data);
                    } else {
                        adapter.setNewData(null);
                        adapter.setEmptyView(R.layout.empty1_layout, recyclerView);
                    }
                } else {
                    if (model.data.size() > 0) {
                        adapter.addData(model.data);
                    } else {
                        refreshLayout.finishLoadMore();
                    }
                }
            }
        }));
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if (!hidden) {
            getData(TimeUtils.getDateString(System.currentTimeMillis()));
        }
    }
}
