package com.snh.snhseller.ui.home.salesManagement;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.haibin.calendarview.Calendar;
import com.haibin.calendarview.CalendarLayout;
import com.haibin.calendarview.CalendarView;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.snh.snhseller.BaseActivity;
import com.snh.snhseller.R;
import com.snh.snhseller.adapter.SalestCountAdapter;
import com.snh.snhseller.bean.BaseResultBean;
import com.snh.snhseller.bean.salesBean.SalesCountBean;
import com.snh.snhseller.requestApi.NetSubscriber;
import com.snh.snhseller.requestApi.RequestClient;
import com.snh.snhseller.utils.IsBang;
import com.snh.snhseller.utils.JumpUtils;
import com.snh.snhseller.utils.TimeUtils;
import com.snh.snhseller.wediget.RecycleViewDivider;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * <p>desc：<p>
 * <p>author：DreamFly<p>
 * <p>creatTime：2019/3/13<p>
 * <p>changeTime：2019/3/13<p>
 * <p>version：1<p>
 */
public class SalesCountActivity extends BaseActivity implements CalendarView.OnCalendarSelectListener, CalendarView.OnYearChangeListener, CalendarView.OnCalendarInterceptListener {
    @BindView(R.id.heard_back)
    LinearLayout heardBack;
    @BindView(R.id.heard_title)
    TextView heardTitle;
    @BindView(R.id.heard_menu)
    ImageView heardMenu;
    @BindView(R.id.heard_tv_menu)
    TextView heardTvMenu;
    @BindView(R.id.rl_menu)
    RelativeLayout rlMenu;
    @BindView(R.id.rl_head)
    LinearLayout rlHead;
    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    @BindView(R.id.refreshLayout)
    SmartRefreshLayout refreshLayout;
    @BindView(R.id.calendarView)
    CalendarView calendarView;
    @BindView(R.id.calendarLayout)
    CalendarLayout calendarLayout;
    @BindView(R.id.tv_name)
    TextView tvName;

    private SalestCountAdapter adapter;
    private int index = 1;
    private boolean isShow = true;
    private String time = "";

    @Override
    protected void initContentView(Bundle savedInstanceState) {
        setContentView(R.layout.activity_salesrecode_layout);
    }

    @Override
    public void setUpViews() {
        IsBang.setImmerHeard(this, rlHead);
        heardTitle.setText("今天");
        tvName.setVisibility(View.GONE);
        heardMenu.setBackgroundResource(R.drawable.clander_bg);
        time = TimeUtils.getDateString(System.currentTimeMillis());
        calendarView.setOnCalendarSelectListener(this);
        calendarView.setOnYearChangeListener(this);
        calendarView.setOnCalendarInterceptListener(this);
        setRecyclerView();
        getData();
    }

    @Override
    public void setUpLisener() {
        refreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                index = 1;
                isShow = false;
                getData();
            }
        });
        refreshLayout.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
                index += 1;
                isShow = false;
                getData();
            }
        });
        adapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                Bundle bundle = new Bundle();
                bundle.putInt("id", datas.get(position).SalesmanId);
                bundle.putString("name", datas.get(position).SalesmanName);
                JumpUtils.dataJump(SalesCountActivity.this, SalesRecodeActivity.class, bundle, false);
            }
        });
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);
    }


    private void setRecyclerView() {
        adapter = new SalestCountAdapter(R.layout.item_sales_count_layout, null);
        recyclerView.addItemDecoration(new RecycleViewDivider(this, LinearLayoutManager.VERTICAL, R.drawable.line));
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);
    }

    private List<SalesCountBean> datas = new ArrayList<>();

    private void getData() {
        addSubscription(RequestClient.SalesClockInRecord(time, index, this, new NetSubscriber<BaseResultBean<List<SalesCountBean>>>(this, isShow) {
            @Override
            public void onResultNext(BaseResultBean<List<SalesCountBean>> model) {
                refreshLayout.finishRefresh();
                refreshLayout.finishLoadMore();
                if (index == 1) {
                    if (model.data.size() > 0) {
                        datas = model.data;
                        adapter.setNewData(model.data);
                    } else {
                        adapter.setNewData(null);
                        adapter.setEmptyView(R.layout.empty_layout, recyclerView);
                    }
                } else {
                    if (model.data.size() > 0) {
                        datas.addAll(model.data);
                        adapter.setNewData(datas);
                    }
                }
            }
        }));
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
        isShow = true;
        getData();
        heardTitle.setText(calendar.getYear() + "年" + (calendar.getMonth() < 10 ? "0" + calendar.getMonth() : calendar.getMonth()) + "月" + (calendar.getDay() < 10 ? "0" + calendar.getDay() : calendar.getDay()));
        calendarLayout.setVisibility(View.GONE);
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


    @OnClick({R.id.heard_back, R.id.heard_menu})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.heard_back:
                this.finish();
                break;
            case R.id.heard_menu:
                calendarLayout.setVisibility(View.VISIBLE);
                break;
        }
    }
}
