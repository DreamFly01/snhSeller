package com.snh.snhseller.ui.order;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.snh.snhseller.BaseFragment;
import com.snh.snhseller.R;
import com.snh.snhseller.adapter.OrderAdapter;
import com.snh.snhseller.bean.BaseResultBean;
import com.snh.snhseller.bean.OrderBean;
import com.snh.snhseller.requestApi.NetSubscriber;
import com.snh.snhseller.requestApi.RequestClient;
import com.snh.snhseller.utils.DBManager;
import com.snh.snhseller.wediget.LoadingDialog;
import com.snh.snhseller.wediget.RecycleViewDivider;

import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * <p>desc：<p>
 * <p>author：DreamFly<p>
 * <p>creatTime：2019/2/21<p>
 * <p>changeTime：2019/2/21<p>
 * <p>version：1<p>
 */
public class OrderListFragment extends BaseFragment {

    private boolean mIsDataInited;
    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    @BindView(R.id.refreshLayout)
    SmartRefreshLayout refreshLayout;
    Unbinder unbinder;
    private int type;
    private int index = 1;
    private OrderAdapter adapter;
    private LoadingDialog loadingDialog;
    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        type = getArguments().getInt("type");
        if (!mIsDataInited) {
            if (getUserVisibleHint()) {
                setView();
                getData();
                mIsDataInited = true;
            }
        }
    }

    @Override
    public int initContentView() {
        return R.layout.fragment_orderlist_layout;
    }

    @Override
    public void setUpViews(View view) {

    }
    public void setView(){
        loadingDialog = LoadingDialog.getInstance(getContext());
        loadingDialog.show();
        setRecyclerView();
    }
    private void setRecyclerView(){
        adapter = new OrderAdapter(R.layout.item_order_layout, null);
        recyclerView.addItemDecoration(new RecycleViewDivider(getContext(),LinearLayoutManager.VERTICAL,R.drawable.line));
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(adapter);
        adapter.setOnLoadMoreListener(new BaseQuickAdapter.RequestLoadMoreListener() {
            @Override
            public void onLoadMoreRequested() {
                recyclerView.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        index+=1;
                        getData();
                    }
                },1000);
            }
        }, recyclerView);
    }
    @Override
    public void setUpLisener() {
        refreshLayout.setEnableLoadMore(false);
        refreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                index = 1;
                getData();
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

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    private void getData() {
        isFrist = false;
        if (type == 0) {
            addSubscription(RequestClient.getOrderList("", type + "", index, getContext(), new NetSubscriber<BaseResultBean<List<OrderBean>>>(getContext()) {
                @Override
                public void onResultNext(BaseResultBean<List<OrderBean>> model) {

                    if (index == 1) {
                        if (model.data.size() > 0) {
                            adapter.setNewData(model.data);
                        }else {
                            adapter.setEmptyView(R.layout.empty_layout);
                        }
                    } else {
                        if (model.data.size() > 0) {
                            adapter.addData(model.data);
                            adapter.loadMoreComplete();
                        } else {
                            adapter.loadMoreEnd();
                        }
                    }
                    loadingDialog.dismiss();
                    refreshLayout.finishRefresh();
                }
            }));
        } else {
            addSubscription(RequestClient.getOrderList(type + "", "", index, getContext(), new NetSubscriber<BaseResultBean<List<OrderBean>>>(getContext()) {
                @Override
                public void onResultNext(BaseResultBean<List<OrderBean>> model) {
                    if (index == 1) {
                        if (model.data.size() > 0) {
                            adapter.setNewData(model.data);
                        }else {
                            adapter.setNewData(null);
                            adapter.setEmptyView(R.layout.empty_layout);
                        }
                    } else {
                        if (model.data.size() > 0) {
                            adapter.addData(model.data);
                            adapter.loadMoreComplete();
                        } else {
                            adapter.loadMoreEnd();
                        }
                    }
                    loadingDialog.dismiss();
                    refreshLayout.finishRefresh();
                }
            }));
        }
    }
    private boolean isFrist = true;
    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
//不预加载数据
        if (isVisibleToUser && isVisible() && !mIsDataInited) {
            setView();
            getData();
            mIsDataInited = true;
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if (!isFrist && mIsDataInited) {
            index = 1;
            getData();
        }
    }

}
