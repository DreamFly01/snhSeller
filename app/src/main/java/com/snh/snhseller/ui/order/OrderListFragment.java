package com.snh.snhseller.ui.order;

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
import com.snh.library_base.utils.Contans;
import com.snh.module_netapi.requestApi.BaseResultBean;
import com.snh.module_netapi.requestApi.NetSubscriber;
import com.snh.moudle_coupons.bean.MsgEventBean;
import com.snh.snhseller.BaseFragment;
import com.snh.snhseller.MainActivity;
import com.snh.snhseller.R;
import com.snh.snhseller.adapter.OrderAdapter;
import com.snh.snhseller.bean.NoticeNumBean;
import com.snh.snhseller.bean.OrderBean;
import com.snh.snhseller.requestApi.RequestClient;
import com.snh.snhseller.utils.JumpUtils;
import com.snh.snhseller.utils.SPUtils;
import com.snh.snhseller.wediget.RecycleViewDivider;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

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
    private int type;//订单状态
    private int index = 1;
    private OrderAdapter adapter;
    private int orderType;//订单类型 0 :我的订单 1:出库订单 2：进货订单
    List<OrderBean> datas = new ArrayList<>();
    private Bundle bundle;

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        type = getArguments().getInt("type");
        orderType = getArguments().getInt("orderType");
        if (!mIsDataInited) {
            if (getUserVisibleHint()) {
                setView();
                getCount();
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

    public void setView() {
        setRecyclerView();
    }

    private void setRecyclerView() {
        adapter = new OrderAdapter(R.layout.item_order_layout, null);
        adapter.setType(orderType);
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
                        isShow = false;
                        getData();
                    }
                }, 1000);
            }
        }, recyclerView);
        adapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                if (orderType != 0) {
                    bundle = new Bundle();
                    bundle.putInt("orderId", datas.get(position).OrderId);
                    bundle.putInt("type", datas.get(position).OrderStates);
                    bundle.putInt("orderType", orderType);
                    JumpUtils.dataJump(getActivity(), OrderDetailsActivity.class, bundle, false);
                } else {
                    bundle = new Bundle();
                    bundle.putString("orderid", datas1.get(position).OrderId + "");
                    JumpUtils.dataJump(getActivity(), MyOrderDetailsActivity.class, bundle, false);
                }
            }
        });
    }

    @Override
    public void setUpLisener() {
        refreshLayout.setEnableLoadMore(false);
        refreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                index = 1;
                isShow = true;
                getCount();
            }
        });

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // TODO: inflate a fragment view
        View rootView = super.onCreateView(inflater, container, savedInstanceState);
        unbinder = ButterKnife.bind(this, rootView);
        EventBus.getDefault().register(this);
        return rootView;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    private List<OrderBean> datas1 = new ArrayList<>();

    private void getData() {
//        isFrist = false;
        SPUtils.getInstance(getContext()).savaBoolean(Contans.ORDER_IS_FRESH, false).commit();
        switch (orderType) {
            case 0:
                if (type == 0) {
                    addSubscription(RequestClient.getOrderList("", "", type + "", index, getContext(), new NetSubscriber<BaseResultBean<List<OrderBean>>>(getContext(), isShow) {
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
                } else {
                    addSubscription(RequestClient.getOrderList("", type + "", "", index, getContext(), new NetSubscriber<BaseResultBean<List<OrderBean>>>(getContext(), isShow) {
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
                break;
            case 1:
                addSubscription(RequestClient.MyShipmentOrderList(type, index, "", getContext(), new NetSubscriber<BaseResultBean<List<OrderBean>>>(getContext(), isShow) {
                    @Override
                    public void onResultNext(BaseResultBean<List<OrderBean>> model) {
                        if (index == 1) {
                            datas = model.data;
                            if (model.data.size() > 0) {
                                adapter.setNewData(model.data);
                            } else {
                                adapter.setNewData(null);
                                adapter.setEmptyView(R.layout.empty_layout, recyclerView);
                            }
                        } else {
                            datas.addAll(model.data);
                            if (model.data.size() > 0) {
                                adapter.setNewData(datas);
                                adapter.loadMoreComplete();
                            } else {
                                adapter.loadMoreEnd();
                            }
                        }
                        refreshLayout.finishRefresh();
                    }
                }));
                break;

            case 2:
                addSubscription(RequestClient.MyStockOrderList(type, index, "", getContext(), new NetSubscriber<BaseResultBean<List<OrderBean>>>(getContext(), isShow) {
                    @Override
                    public void onResultNext(BaseResultBean<List<OrderBean>> model) {
                        if (index == 1) {
                            datas = model.data;
                            if (model.data.size() > 0) {
                                adapter.setNewData(model.data);
                            } else {
                                adapter.setNewData(null);
                                adapter.setEmptyView(R.layout.empty_layout, recyclerView);
                            }
                        } else {
                            datas.addAll(model.data);
                            if (model.data.size() > 0) {
                                adapter.setNewData(datas);
                                adapter.loadMoreComplete();
                            } else {
                                adapter.loadMoreEnd();
                            }
                        }
                        refreshLayout.finishRefresh();
                    }
                }));
                break;
        }

    }


    private void getCount() {
        RequestClient.GetSupplierNoticeUnreadCount(getContext(), new NetSubscriber<BaseResultBean<NoticeNumBean>>(getContext()) {
            @Override
            public void onResultNext(BaseResultBean<NoticeNumBean> model) {
                int sumNum2 = model.data.UserDFH + model.data.CKDFH + model.data.JHDSH + model.data.JHDZF + model.data.UserDZF + model.data.CKDZF;
                if (orderType == 0) {
                    if (type == 0) {
                        OrderFragment.UserDZF = model.data.UserDZF;
                        OrderFragment.updataView(0, 1);
                    }
                    if (type == 2) {
                        OrderFragment.UserDFH = model.data.UserDFH;
                        OrderFragment.updataView(0, 2);
                    }
                }
                if (orderType == 1) {
                    if (type == 1) {

                        OrderFragment.CKDZF = model.data.CKDZF;
                        OrderFragment.updataView(1, 1);
                    }
                    if (type == 2) {
                        OrderFragment.CKDFH = model.data.CKDFH;
                        OrderFragment.updataView(1, 2);
                    }
                }
                if (orderType == 2) {
                    if (type == 1) {
                        OrderFragment.JHDZF = model.data.JHDZF;
                        OrderFragment.updataView(2, 1);
                    }
                    if (type == 3) {
                        OrderFragment.JHDSH = model.data.JHDSH;
                        OrderFragment.updataView(2, 3);
                    }
                }

                MainActivity.tvNum2.setVisibility(View.VISIBLE);
                if (sumNum2 > 99) {
                    MainActivity.tvNum2.setText("99+");
                } else if (sumNum2 <= 0) {
                    MainActivity.tvNum2.setVisibility(View.GONE);
                } else {
                    MainActivity.tvNum2.setText(sumNum2 + "");
                }
                getData();
            }
        });
    }

    public static boolean isFrist = true;

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        //不预加载数据
        if (isVisibleToUser && isVisible() && !mIsDataInited) {
            setView();
            getCount();
            mIsDataInited = true;
        }
        if (isVisibleToUser && SPUtils.getInstance(getContext()).getBoolean(Contans.ORDER_IS_FRESH)) {
            index = 1;
            getCount();
        }
    }

    private boolean hidden;

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        this.hidden = hidden;
    }

    private boolean isShow = true;

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void refreshData(MsgEventBean bean){
        if(bean.getMsg().equals("refresh")&&getUserVisibleHint()){
            index = 1;
            isShow = true;
            getCount();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

}
