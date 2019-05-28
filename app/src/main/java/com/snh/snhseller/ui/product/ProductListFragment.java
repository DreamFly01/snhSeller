package com.snh.snhseller.ui.product;

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
import com.snh.snhseller.adapter.ProdcutAdapter;
import com.snh.snhseller.bean.BaseResultBean;
import com.snh.snhseller.bean.ProductBean;
import com.snh.snhseller.requestApi.NetSubscriber;
import com.snh.snhseller.requestApi.RequestClient;
import com.snh.snhseller.ui.merchantEntry.PerfectMyLocalActivity;
import com.snh.snhseller.utils.Contans;
import com.snh.snhseller.utils.DialogUtils;
import com.snh.snhseller.utils.JumpUtils;
import com.snh.snhseller.utils.SPUtils;
import com.snh.snhseller.wediget.RecycleViewDivider;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * <p>desc：<p>
 * <p>author：DreamFly<p>
 * <p>creatTime：2019/2/22<p>
 * <p>changeTime：2019/2/22<p>
 * <p>version：1<p>
 */
public class ProductListFragment extends BaseFragment {
    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    @BindView(R.id.refreshLayout)
    SmartRefreshLayout refreshLayout;
    Unbinder unbinder;
    private boolean mIsDataInited;
    private int type = 1;
    private int index = 1;
    private ProdcutAdapter adapter;
    private DialogUtils dialogUtils;
    private List<ProductBean> datas = new ArrayList<>();

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
        return R.layout.fragment_productlist_layout;
    }

    @Override
    public void setUpViews(View view) {
        dialogUtils = new DialogUtils(getContext());
    }

    @Override
    public void setUpLisener() {
        refreshLayout.setEnableLoadMore(false);
        refreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                index = 1;
                isShow = true;
                getData();
            }
        });
    }

    private void setView() {
        setRecyclerView();
    }

    private void setRecyclerView() {
        adapter = new ProdcutAdapter(R.layout.item_product_content, null);
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
            public void onItemChildClick(BaseQuickAdapter adapter, View view, final int position) {
                switch (view.getId()) {
                    //下架商品
                    case R.id.ll_01:
                        dialogUtils.twoBtnDialog("是否确定下架商品", new DialogUtils.ChoseClickLisener() {
                            @Override
                            public void onConfirmClick(View v) {
                                UpOrDownProduct(datas.get(position).CommTenantId, 2, "下架成功");
                                SPUtils.getInstance(getContext()).savaBoolean(Contans.PRODUCT_IS_FRESH, true).commit();
                            }

                            @Override
                            public void onCancelClick(View v) {
                                dialogUtils.dismissDialog();
                            }
                        }, false);
                        break;
                    //编辑商品
                    case R.id.ll_item:
                        if (datas.get(position).IsAuditing == 2) {
                            Bundle bundle = new Bundle();
                            bundle.putParcelable("data", datas.get(position));
                            bundle.putInt("type", 2);
                            JumpUtils.dataJump(getActivity(), EditProduct1Activity.class, bundle, false);
                        } else if (type == 2) {
                            Bundle bundle = new Bundle();
                            bundle.putParcelable("data", datas.get(position));
                            bundle.putInt("type", 2);
                            JumpUtils.dataJump(getActivity(), EditProduct1Activity.class, bundle, false);
                        } else if (datas.get(position).IsAuditing == 0) {
//                            dialogUtils.noBtnDialog("商品待审核，不可编辑");
                        }
                        break;
                    case R.id.ll_02:
                        dialogUtils.twoBtnDialog("是否确定删除商品", new DialogUtils.ChoseClickLisener() {
                            @Override
                            public void onConfirmClick(View v) {
                                delProduct(datas.get(position).CommTenantId, "删除商品成功");
                                SPUtils.getInstance(getContext()).savaBoolean(Contans.PRODUCT_IS_FRESH, true).commit();
                                getData();
                            }

                            @Override
                            public void onCancelClick(View v) {
                                dialogUtils.dismissDialog();
                            }
                        }, false);
                        break;
                    //删除商品
                    case R.id.ll_delete:
                        dialogUtils.twoBtnDialog("是否确定删除商品", new DialogUtils.ChoseClickLisener() {
                            @Override
                            public void onConfirmClick(View v) {
                                delProduct(datas.get(position).CommTenantId, "删除商品成功");
                                SPUtils.getInstance(getContext()).savaBoolean(Contans.PRODUCT_IS_FRESH, true).commit();
                                getData();
                            }

                            @Override
                            public void onCancelClick(View v) {
                                dialogUtils.dismissDialog();
                            }
                        }, false);
                        break;
                    //上架商品
                    case R.id.ll_up:
                        if ("1".equals(SPUtils.getInstance(getContext()).getString(Contans.IS_FULL))) {
                            dialogUtils.twoBtnDialog("是否确定上架商品", new DialogUtils.ChoseClickLisener() {
                                @Override
                                public void onConfirmClick(View v) {
                                    UpOrDownProduct(datas.get(position).CommTenantId, 1, "上架成功");
                                    SPUtils.getInstance(getContext()).savaBoolean(Contans.PRODUCT_IS_FRESH, true).commit();
                                }

                                @Override
                                public void onCancelClick(View v) {
                                    dialogUtils.dismissDialog();
                                }
                            }, false);
                        } else if ("0".equals(SPUtils.getInstance(getContext()).getString(Contans.IS_FULL))) {
                            dialogUtils.twoBtnDialog("是否马上完善店铺信息", new DialogUtils.ChoseClickLisener() {
                                @Override
                                public void onConfirmClick(View v) {
                                    dialogUtils.dismissDialog();
                                    JumpUtils.simpJump(getActivity(), PerfectMyLocalActivity.class, false);
                                }

                                @Override
                                public void onCancelClick(View v) {
                                    dialogUtils.dismissDialog();
                                }
                            }, true);
                        }

                        break;
                }
            }
        });
    }

    private void getData() {
        addSubscription(RequestClient.GetSaleOfGoods(type, index, "", getContext(), new NetSubscriber<BaseResultBean<List<ProductBean>>>(getContext(), isShow) {
            @Override
            public void onResultNext(BaseResultBean<List<ProductBean>> model) {
//                SPUtils.getInstance(getContext()).savaBoolean(Contans.PRODUCT_IS_FRESH,false).commit();
                SPUtils.getInstance(getContext()).savaBoolean(Contans.FRESH, false).commit();
                for (int i = 0; i < model.data.size(); i++) {
                    model.data.get(i).state = type;
                }
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

    private boolean myIsVisible;

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
//防止数据预加载, 只预加载View，不预加载数据
        myIsVisible = isVisibleToUser;
        if (isVisibleToUser && isVisible() && !mIsDataInited) {
            setView();
            getData();
            mIsDataInited = true;
        }
        if (isVisibleToUser && SPUtils.getInstance(getContext()).getBoolean(Contans.PRODUCT_IS_FRESH)) {
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

    private void UpOrDownProduct(int commtenanId, int type, final String content) {
        index = 1;
        addSubscription(RequestClient.UpOrDownProduct(commtenanId, type, getContext(), new NetSubscriber<BaseResultBean>(getContext(), true) {
            @Override
            public void onResultNext(BaseResultBean model) {
                dialogUtils.dismissDialog();
                Toast.makeText(getContext(), content, Toast.LENGTH_SHORT).show();
                getData();
            }
        }));
    }

    private void delProduct(int commtenanId, final String content) {
        index = 1;
        addSubscription(RequestClient.DelProduct(commtenanId, getContext(), new NetSubscriber<BaseResultBean>(getContext()) {
            @Override
            public void onResultNext(BaseResultBean model) {
                dialogUtils.dismissDialog();
                Toast.makeText(getContext(), content, Toast.LENGTH_SHORT).show();
                getData();
            }
        }));
    }

    private boolean isShow = true;

    @Override
    public void onResume() {
        super.onResume();
        if (myIsVisible && SPUtils.getInstance(getContext()).getBoolean(Contans.FRESH)) {
            index = 1;
            isShow = true;
            getData();
        }
    }

    //更新数据
    public void updateArguments(int pageType) {
        this.type = pageType;
        Bundle args = getArguments();
        if (args != null) {
//            setView();
            args.putInt("type", pageType);
//            getData();
        }
    }

}
