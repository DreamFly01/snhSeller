package com.snh.snhseller.ui.msg;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.snh.module_netapi.requestApi.BaseResultBean;
import com.snh.module_netapi.requestApi.NetSubscriber;
import com.snh.snhseller.BaseFragment;
import com.snh.snhseller.R;
import com.snh.snhseller.adapter.OrderNoticeAdapter;
import com.snh.snhseller.bean.NoticeBean;
import com.snh.snhseller.requestApi.RequestClient;
import com.snh.snhseller.wediget.RecycleViewDivider;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * <p>desc：<p>
 * <p>author：DreamFly<p>
 * <p>creatTime：2019/3/12<p>
 * <p>changeTime：2019/3/12<p>
 * <p>version：1<p>
 */
public class OrderNoticeFragment extends BaseFragment {
    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    @BindView(R.id.refreshLayout)
    SmartRefreshLayout refreshLayout;
    Unbinder unbinder;
    private int type = 1;
    private int index = 1;
    private boolean isShow = true;
    private OrderNoticeAdapter adapter;
    private List<NoticeBean> datas = new ArrayList<>();
    private Bundle bundle;
    private boolean mIsDataInited;
    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        type = getArguments().getInt("type");
        if (!mIsDataInited) {
            if (getUserVisibleHint()) {
                getData();
                mIsDataInited = true;
            }
        }
    }
    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        //不预加载数据
        if (isVisibleToUser && isVisible() && !mIsDataInited) {
           getData();
            mIsDataInited = true;
        }
    }
    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if (!hidden) {
            getData();
        }
    }
    @Override
    public int initContentView() {
        return R.layout.fragment_applynotice_layout;
    }

    @Override
    public void setUpViews(View view) {
        refreshLayout.setEnableLoadMore(false);
        setRecyclerView();
    }

    @Override
    public void setUpLisener() {
        refreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                index = 1;
                isShow = true;
                getData();
            }
        });
        adapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                if (type == 2) {
                    OrderNoticeActivity.updataView(type, 1);
                    bundle = new Bundle();
                    bundle.putInt("orderId", datas.get(position).OrderId);
//                    bundle.putInt("type",datas.get(position).OrderStates);
//                    bundle.putInt("orderType", orderType);
//                    JumpUtils.dataJump(getActivity(), OrderDetailsActivity.class, bundle, false);
                } else if (type == 1) {
                    bundle = new Bundle();
                    OrderNoticeActivity.updataView(type, 1);
//                    bundle.putString("orderid", datas.get(position).OrderId + "");
//                    JumpUtils.dataJump(getActivity(), MyOrderDetailsActivity.class, bundle, false);
                }
            }
        });
    }

    private void setRecyclerView() {
        adapter = new OrderNoticeAdapter(R.layout.item_notice_layout, null);
        recyclerView.addItemDecoration(new RecycleViewDivider(getContext(), LinearLayoutManager.VERTICAL, R.drawable.line_2_gray));
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
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
        addSubscription(RequestClient.GetSupplierNotice(type, index, "", getContext(), new NetSubscriber<BaseResultBean<List<NoticeBean>>>(getContext(), isShow) {
            @Override
            public void onResultNext(BaseResultBean<List<NoticeBean>> model) {
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
                        adapter.loadMoreComplete();
                    } else {
                        adapter.loadMoreEnd();
                    }
                }
                refreshLayout.finishRefresh();
            }
        }));
    }

    @Override
    public void onResume() {
        super.onResume();
//        getData();
    }
}
