package com.snh.snhseller.ui.product;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.snh.module_netapi.requestApi.BaseResultBean;
import com.snh.module_netapi.requestApi.NetSubscriber;
import com.snh.snhseller.BaseFragment;
import com.snh.snhseller.R;
import com.snh.snhseller.adapter.BusinessAdapter;
import com.snh.snhseller.bean.BusinessBean;
import com.snh.snhseller.requestApi.RequestClient;
import com.snh.snhseller.utils.JumpUtils;
import com.snh.snhseller.wediget.RecycleViewDivider;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * <p>desc：<p>
 * <p>author：DreamFly<p>
 * <p>creatTime：2019/4/9<p>
 * <p>changeTime：2019/4/9<p>
 * <p>version：1<p>
 */
public class BusinessProduct1Fragment extends BaseFragment {

    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    @BindView(R.id.refreshLayout)
    SmartRefreshLayout refreshLayout;
    @BindView(R.id.btn_commit)
    Button btnCommit;
    Unbinder unbinder;
    private BusinessAdapter adapter;
    private int index = 1;
    private String condition = "";
    private int type = 2;
    private boolean isShow = true;
    private List<BusinessBean> datas = new ArrayList<>();
    public boolean isFrist = true;

    @Override
    public int initContentView() {
        return R.layout.fragment_business_layout;
    }

    @Override
    public void setUpViews(View view) {
        btnCommit.setVisibility(View.GONE);
        setRecyclerView();
        getData();
    }

    @Override
    public void setUpLisener() {
            refreshLayout.setOnLoadMoreListener(new OnLoadMoreListener() {
                @Override
                public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
                    index += 1;
                    isShow = false;
                    getData();
                }
            });
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
                isFrist = false;
                Bundle bundle = new Bundle();
                bundle.putInt("goodsId", datas.get(position).ShopgoodsId);
                if (datas.get(position).IsSetNorm) {
                    JumpUtils.dataJump(getActivity(), SkuList1Activity.class, bundle, false);
                } else {
                    JumpUtils.dataJump(getActivity(), AddSkuActivity.class, bundle, false);
                }
            }
        });
    }

    private void setRecyclerView() {
        adapter = new BusinessAdapter(R.layout.item_business_layout, null);
        recyclerView.addItemDecoration(new RecycleViewDivider(getContext(), LinearLayoutManager.VERTICAL, R.drawable.line));
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(adapter);
    }

    private void getData() {
        RequestClient.MyCommodityList(index, condition, type, getContext(), new NetSubscriber<BaseResultBean<List<BusinessBean>>>(getContext(), isShow) {
            @Override
            public void onResultNext(BaseResultBean<List<BusinessBean>> model) {
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
//                        adapter.addData(model.data);
                        adapter.setNewData(datas);
                    }
                }
                refreshLayout.finishRefresh();
                refreshLayout.finishLoadMore();
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        if (!isFrist) {
            index = 1;
            getData();
        }
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
