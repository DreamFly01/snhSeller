package com.snh.snhseller.ui.home.costApply;

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
import com.snh.snhseller.BaseFragment;
import com.snh.snhseller.R;
import com.snh.snhseller.adapter.CostApplyAdapter;
import com.snh.snhseller.adapter.ProdcutAdapter;
import com.snh.snhseller.bean.BaseResultBean;
import com.snh.snhseller.bean.CostApplyBean;
import com.snh.snhseller.bean.ProductBean;
import com.snh.snhseller.requestApi.NetSubscriber;
import com.snh.snhseller.requestApi.RequestClient;
import com.snh.snhseller.utils.DialogUtils;
import com.snh.snhseller.utils.IsBang;
import com.snh.snhseller.utils.JumpUtils;
import com.snh.snhseller.wediget.LoadingDialog;
import com.snh.snhseller.wediget.RecycleViewDivider;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * <p>desc：<p>
 * <p>author：DreamFly<p>
 * <p>creatTime：2019/3/14<p>
 * <p>changeTime：2019/3/14<p>
 * <p>version：1<p>
 */
public class CostApplyFragment extends BaseFragment {
    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    @BindView(R.id.refreshLayout)
    SmartRefreshLayout refreshLayout;
    Unbinder unbinder;


    private boolean mIsDataInited;
    private int type;
    private LoadingDialog loadingDialog;
    private int index = 1;
    private CostApplyAdapter adapter;
    private DialogUtils dialogUtils;
    private List<CostApplyBean> datas = new ArrayList<>();
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
        return R.layout.fragment_costapply_layout;
    }

    @Override
    public void setUpViews(View view) {
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
    private void setView() {
        loadingDialog = LoadingDialog.getInstance(getContext());
        loadingDialog.show();
        setRecyclerView();
    }
    private void setRecyclerView() {
        adapter = new CostApplyAdapter(R.layout.item_delaration_layout, null);
        recyclerView.addItemDecoration(new RecycleViewDivider(getContext(), LinearLayoutManager.VERTICAL, R.drawable.line));
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(adapter);
        adapter.setOnLoadMoreListener(new BaseQuickAdapter.RequestLoadMoreListener() {
            @Override
            public void onLoadMoreRequested() {
                recyclerView.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        index += 1;
                        getData();
                    }
                }, 1000);
            }
        }, recyclerView);
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
        },recyclerView);
        adapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                Bundle bundle = new Bundle();
                isFrist= false;
                bundle.putParcelable("data",datas.get(position));
                JumpUtils.dataJump(getActivity(),CostApplyDetailsActivity.class,bundle,false);
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
        addSubscription(RequestClient.GetSalesmanCostList(type, index, getContext(), new NetSubscriber<BaseResultBean<List<CostApplyBean>>>(getContext()) {
            @Override
            public void onResultNext(BaseResultBean<List<CostApplyBean>> model) {
                refreshLayout.finishRefresh();

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
                loadingDialog.dismiss();
            }
        }));
    }
    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
//防止数据预加载, 只预加载View，不预加载数据
        if (isVisibleToUser && isVisible() && !mIsDataInited) {
            setView();
            getData();
            mIsDataInited = true;
        }

    }

    private boolean isFrist = true;
    @Override
    public void onResume() {
        super.onResume();
        if (!isFrist && mIsDataInited) {
            index = 1;
            getData();
        }
    }

}
