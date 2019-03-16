package com.snh.snhseller.ui.product;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.snh.snhseller.R;
import com.snh.snhseller.adapter.BusinessAdapter;
import com.snh.snhseller.adapter.SkuListAdapter;
import com.snh.snhseller.bean.BaseResultBean;
import com.snh.snhseller.bean.BusinessBean;
import com.snh.snhseller.requestApi.NetSubscriber;
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
 * <p>creatTime：2019/3/11<p>
 * <p>changeTime：2019/3/11<p>
 * <p>version：1<p>
 */
public class BusinessProductFragment extends Fragment {


    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    @BindView(R.id.refreshLayout)
    SmartRefreshLayout refreshLayout;
    Unbinder unbinder;
    private BusinessAdapter adapter;
    private int index = 1;
    private String condition = "";
    private int type;
    private boolean isShow = true;
    private List<BusinessBean> datas = new ArrayList<>();
    //是否可见
    public boolean isVisible = false;
    //是否初始化完成
    public boolean isInit = false;
    //是否已经加载过
    public boolean isLoadOver = false;

    public boolean isFrist = true;
    private View view;

    //界面可见时再加载数据(该方法在onCreate()方法之前执行。)

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        this.isVisible = isVisibleToUser;
        setParam();
    }

    private void setParam() {
        if (isInit && !isLoadOver && isVisible) {
            isLoadOver = true;
//            setRecyclerView();
            getData();
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        type = getArguments().getInt("type");
        if (view == null) {
                view = View.inflate(getActivity(), R.layout.fragment_business_layout, null);
            isInit = true;
            setParam();
        }
        unbinder = ButterKnife.bind(this, view);

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setRecyclerView();
        setUpLisener();
    }

    public void setUpLisener() {
        if (type == 0) {
            refreshLayout.setOnLoadMoreListener(new OnLoadMoreListener() {
                @Override
                public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
                    index += 1;
                    isShow = false;
                    getData();
                }
            });
        }else {
            refreshLayout.setEnableLoadMore(false);
        }
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
                    JumpUtils.dataJump(getActivity(), SkuListActivity.class, bundle, false);
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
        if (!isFrist && isLoadOver) {
            index = 1;
            getData();
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
}
