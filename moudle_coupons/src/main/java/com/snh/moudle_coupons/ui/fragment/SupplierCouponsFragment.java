package com.snh.moudle_coupons.ui.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.snh.library_base.BaseFragment;
import com.snh.library_base.utils.JumpUtils;
import com.snh.library_base.wediget.RecycleViewDivider;
import com.snh.module_netapi.requestApi.BaseResultBean;
import com.snh.module_netapi.requestApi.NetSubscriber;
import com.snh.moudle_coupons.R;
import com.snh.moudle_coupons.R2;
import com.snh.moudle_coupons.adapter.StoreCouponsAdapter;
import com.snh.moudle_coupons.bean.CouponsBean;
import com.snh.moudle_coupons.netapi.RequestClient;
import com.snh.moudle_coupons.ui.activity.AppointProductActivity;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * <p>desc：<p>
 * <p>author：DreamFly<p>
 * <p>creatTime：2019/5/30<p>
 * <p>changeTime：2019/5/30<p>
 * <p>version：1<p>
 */
public class SupplierCouponsFragment extends BaseFragment {
    @BindView(R2.id.recyclerView)
    RecyclerView recyclerView;
    @BindView(R2.id.refreshLayout)
    SmartRefreshLayout refreshLayout;
    Unbinder unbinder;

    private boolean isShow = true;
    private int index = 1;
    private StoreCouponsAdapter adapter;
    List<CouponsBean> datas = new ArrayList<>();

    @Override
    public int initContentView() {
        return R.layout.coupons_fragment_coupons_layout;
    }

    @Override
    public void setUpViews(View view) {
        setRecyclerView();
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
        adapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                int i = view.getId();
               if(i == R.id.coupons_ll_on_off){
                    if(datas.get(position).IsAdd == 0){
                        datas.get(position).IsAdd = 1;
                    }else {
                        datas.get(position).IsAdd = 0;
                    }
                    setOnOff(datas.get(position).CouponId,datas.get(position).IsAdd,position);
               }else if(i == R.id.coupons_tv_03){
                   Bundle bundle = new Bundle();
                   bundle.putInt("couponsId",datas.get(position).CouponId);
                   JumpUtils.dataJump(getActivity(),AppointProductActivity.class,bundle,false);
               }
            }
        });
    }

    private void setRecyclerView() {
        adapter = new StoreCouponsAdapter(R.layout.coupons_item_storecoupons_layout, null);
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
    private void getData() {
        addSubscription(RequestClient.GetSupplierCouponsList(index, getContext(), new NetSubscriber<BaseResultBean<List<CouponsBean>>>(getContext(), isShow) {
            @Override
            public void onResultNext(BaseResultBean<List<CouponsBean>> model) {
                refreshLayout.finishRefresh();
                refreshLayout.finishLoadMore();
                if (index == 1) {
                    if (model.data.size() > 0) {
                        datas = model.data;
                        adapter.setNewData(datas);
                    } else {
                        adapter.setNewData(null);
                        adapter.setEmptyView(R.layout.empty_layout, recyclerView);
                    }
                } else {
                    if (model.data.size() > 0) {
                        datas.addAll(model.data);
                        adapter.setNewData(datas);
                    } else {
                        refreshLayout.finishLoadMoreWithNoMoreData();
                    }
                }
            }
        }));
    }

    private void setOnOff(int couponsId, int type, final int position){
        addSubscription(RequestClient.SetCouponsIsShow(couponsId, type, getContext(), new NetSubscriber<BaseResultBean>(getContext(),true) {
            @Override
            public void onResultNext(BaseResultBean model) {
                adapter.notifyItemChanged(position,datas.get(position));
            }
        }));
    }

    @Override
    public void onResume() {
        super.onResume();
        getData();

    }
}
