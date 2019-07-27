package com.snh.snhseller.ui.order;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.snh.module_netapi.requestApi.BaseResultBean;
import com.snh.module_netapi.requestApi.NetSubscriber;
import com.snh.snhseller.BaseActivity;
import com.snh.snhseller.R;
import com.snh.snhseller.adapter.OrderAdapter;
import com.snh.snhseller.bean.OrderBean;
import com.snh.snhseller.requestApi.RequestClient;
import com.snh.snhseller.utils.IsBang;
import com.snh.snhseller.utils.JumpUtils;
import com.snh.snhseller.utils.KeyBoardUtils;
import com.snh.snhseller.utils.StrUtils;
import com.snh.snhseller.wediget.RecycleViewDivider;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * <p>desc：<p>
 * <p>author：DreamFly<p>
 * <p>creatTime：2019/5/23<p>
 * <p>changeTime：2019/5/23<p>
 * <p>version：1<p>
 */
public class BToCOrderAllActivity extends BaseActivity {
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
    @BindView(R.id.et_search)
    EditText etSearch;

    private int index = 1;
    private OrderAdapter adapter;
    List<OrderBean> datas = new ArrayList<>();
    private Bundle bundle;
    private boolean isShow = true;
    private String condition = "";
    @Override
    protected void initContentView(Bundle savedInstanceState) {
        setContentView(R.layout.activity_btocorderall_layout);
        bundle = getIntent().getExtras();

    }

    @Override
    public void setUpViews() {
        IsBang.setImmerHeard(this,rlHead,"#ffffff");
        heardTitle.setText("订单搜索");
        setRecyclerView();
    }

    @Override
    public void setUpLisener() {
        refreshLayout.setEnableLoadMore(false);
        refreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                index = 1;
                isShow = true;
            }
        });

        etSearch.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    KeyBoardUtils.hintKeyBoard(BToCOrderAllActivity.this);
                    isShow = true;
                    index = 1;
                    if (!StrUtils.isEmpty(etSearch.getText().toString())) {
                        condition = etSearch.getText().toString().trim();
                    } else {
                        condition = "";
                    }
                    adapter.setNewData(null);
                    getData();
                    return true;
                }
                return false;
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
        adapter = new OrderAdapter(R.layout.item_order_layout, null);
        adapter.setType(0);
        recyclerView.addItemDecoration(new RecycleViewDivider(this, LinearLayoutManager.VERTICAL, R.drawable.line));
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);
        adapter.setOnLoadMoreListener(new BaseQuickAdapter.RequestLoadMoreListener() {
            @Override
            public void onLoadMoreRequested() {
                recyclerView.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        index += 1;
                        isShow = false;
                        getData();
                    }
                }, 1000);
            }
        }, recyclerView);
        adapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                bundle = new Bundle();
                bundle.putString("orderid", datas1.get(position).OrderId + "");
                JumpUtils.dataJump(BToCOrderAllActivity.this, MyOrderDetailsActivity.class, bundle, false);
            }
        });
    }

    private List<OrderBean> datas1 = new ArrayList<>();

    private void getData() {
        addSubscription(RequestClient.getOrderList(condition, "",  "", index, BToCOrderAllActivity.this, new NetSubscriber<BaseResultBean<List<OrderBean>>>(BToCOrderAllActivity.this, isShow) {
            @Override
            public void onResultNext(BaseResultBean<List<OrderBean>> model) {

                if (index == 1) {
                    if (model.data.size() > 0) {
                        datas1 = model.data;
                        adapter.setNewData(model.data);
                    } else {
                        adapter.setNewData(null);
                        adapter.setEmptyView(R.layout.empty_layout, recyclerView);
                    }
                } else {
                    if (model.data.size() > 0) {
                        datas1.addAll(model.data);
                        adapter.setNewData(datas1);
                        adapter.loadMoreComplete();
                    } else {
                        adapter.loadMoreEnd();
                    }
                }
                refreshLayout.finishRefresh();
            }
        }));
    }

    @OnClick(R.id.heard_back)
    public void onClick() {
        this.finish();
    }
}
