package com.snh.snhseller.ui.msg;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.snh.module_netapi.requestApi.BaseResultBean;
import com.snh.module_netapi.requestApi.NetSubscriber;
import com.snh.snhseller.BaseFragment;
import com.snh.snhseller.R;
import com.snh.snhseller.adapter.MyMsgAdapter;
import com.snh.snhseller.bean.NoticeBean;
import com.snh.snhseller.requestApi.RequestClient;
import com.snh.snhseller.utils.DialogUtils;
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
public class SystemNoticeFragment extends BaseFragment {
    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    @BindView(R.id.refreshLayout)
    SmartRefreshLayout refreshLayout;
    Unbinder unbinder;


    private int index = 1;
    private boolean isShow = true;
    private MyMsgAdapter adapter;
    private DialogUtils dialogUtils;
    @Override
    public int initContentView() {
        return R.layout.fragment_applynotice_layout;
    }

    @Override
    public void setUpViews(View view) {
        dialogUtils = new DialogUtils(getContext());
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
                index+=1;
                isShow = false;
                getData();
            }
        });
    }
    private void setRecyclerView() {
        adapter = new MyMsgAdapter(R.layout.item_mynotice_layout, null);
        recyclerView.addItemDecoration(new RecycleViewDivider(getContext(), LinearLayout.VERTICAL, R.drawable.line));
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(adapter);
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
    private List<NoticeBean> datas = new ArrayList<>();
//    private void getData() {
//        addSubscription(RequestClient.MyMsg(index, getContext(), new NetSubscriber<BaseResultBean<List<MyMsgBean>>>(getContext(), isShow) {
//            @Override
//            public void onResultNext(BaseResultBean<List<MyMsgBean>> model) {
//                refreshLayout.finishLoadMore();
//                refreshLayout.finishRefresh();
//                if (index == 1 && model.data.size() > 0) {
//                    datas = model.data;
//                    adapter.setNewData(model.data);
//                } else {
//                    if (index == 1 && model.data.size() <= 0) {
//                        adapter.setNewData(null);
//                        adapter.setEmptyView(R.layout.empty_layout, recyclerView);
//                    } else {
//                        if (model.data.size() > 0) {
//                            datas.addAll(model.data);
//                            adapter.setNewData(datas);
//                        }
//                    }
//
//                }
//            }
//        }));
//    }

    private void getData() {
        addSubscription(RequestClient.GetSupplierNotice(5, index, "", getContext(), new NetSubscriber<BaseResultBean<List<NoticeBean>>>(getContext(), isShow) {
            @Override
            public void onResultNext(BaseResultBean<List<NoticeBean>> model) {
                if (index == 1) {
                    if (model.data.size() > 0) {
                        datas = model.data;
                        adapter.setNewData(model.data);
                    } else {
                        adapter.setNewData(null);
                        adapter.setEmptyView(R.layout.empty_layout,recyclerView);
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
}
