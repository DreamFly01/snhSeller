package com.snh.snhseller.ui.salesmanManagement.operation;

import android.Manifest;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.location.BDAbstractLocationListener;
import com.baidu.location.BDLocation;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.gyf.barlibrary.ImmersionBar;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.snh.snhseller.BaseFragment;
import com.snh.snhseller.R;
import com.snh.snhseller.bean.BaseResultBean;
import com.snh.snhseller.bean.salebean.CommTenantBean;
import com.snh.snhseller.bean.salebean.OperationBean;
import com.snh.snhseller.requestApi.NetSubscriber;
import com.snh.snhseller.requestApi.RequestClient;
import com.snh.snhseller.ui.salesmanManagement.adapter.OperationAdapter;
import com.snh.snhseller.utils.Contans;
import com.snh.snhseller.utils.DBManager;
import com.snh.snhseller.utils.DialogUtils;
import com.snh.snhseller.utils.ImageUtils;
import com.snh.snhseller.utils.IsBang;
import com.snh.snhseller.utils.JumpUtils;
import com.snh.snhseller.utils.SPUtils;
import com.snh.snhseller.utils.StrUtils;
import com.snh.snhseller.wediget.RecycleViewDivider;
import com.snh.snhseller.wediget.SlideRecyclerView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import pub.devrel.easypermissions.EasyPermissions;

import static com.netease.nim.uikit.common.util.sys.NetworkUtil.isNetworkConnected;

/**
 * <p>desc：业务管理页面<p>
 * <p>author：DreamFly<p>
 * <p>creatTime：2019/2/25<p>
 * <p>changeTime：2019/2/25<p>
 * <p>version：1<p>
 */
public class OperationFragment extends BaseFragment {
    @BindView(R.id.heard_back)
    LinearLayout heardBack;
    @BindView(R.id.heard_title)
    TextView heardTitle;
    @BindView(R.id.heard_menu)
    ImageView heardMenu;
    @BindView(R.id.heard_tv_menu)
    TextView heardTvMenu;
    @BindView(R.id.rl_head)
    LinearLayout rlHead;
    @BindView(R.id.iv_logo)
    ImageView ivLogo;
    @BindView(R.id.tv_nick)
    TextView tvNick;
    @BindView(R.id.tv_phone)
    TextView tvPhone;
    @BindView(R.id.recyclerView)
    SlideRecyclerView recyclerView;
    @BindView(R.id.refreshLayout)
    SmartRefreshLayout refreshLayout;
    Unbinder unbinder;

    private int index = 1;
    private OperationAdapter adapter;
    public LocationClient mLocationClient = null;
    private double latitude;
    private double longitude;
    private DialogUtils dialogUtils;


    @Override
    public int initContentView() {
        return R.layout.fragment_operation_layout;
    }

    @Override
    public void setUpViews(View view) {
        ImmersionBar.setTitleBar(getActivity(), rlHead);
        IsBang.setImmerHeard(getContext(), rlHead,"");
        dialogUtils = new DialogUtils(getContext());
        heardTitle.setText("业务员管理");
        heardBack.setVisibility(View.GONE);
        tvNick.setText(DBManager.getInstance(getContext()).getSaleInfo().NickName);
        tvPhone.setText(DBManager.getInstance(getContext()).getSaleInfo().PhoneNumber);
        ImageUtils.loadUrlCorners(getContext(), "http://cdn.duitang.com/uploads/item/201409/25/20140925003424_uzG3A.thumb.700_0.jpeg", ivLogo);
        setRecyclerView();
        checkPerm();
    }

    @Override
    public void setUpLisener() {
        refreshLayout.setEnableLoadMore(false);
        refreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                index = 1;
//                getData();
                checkPerm();
            }
        });
        adapter.setOnLoadMoreListener(new BaseQuickAdapter.RequestLoadMoreListener() {
            @Override
            public void onLoadMoreRequested() {
                recyclerView.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        index += 1;
                        if (isNetworkConnected(getContext())) {
                            getData();
                        } else {
                            dialogUtils.noBtnDialog("请打开网络！");
                            adapter.loadMoreEnd();
                        }
                    }
                }, 1000);
            }
        }, recyclerView);
        adapter.disableLoadMoreIfNotFullPage();
        adapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, final int position) {
                switch (view.getId()) {
                    case R.id.btn_dk:
                        dialogUtils.editeDialog(datas.get(position).CommTenantName, new DialogUtils.EditClickLisener() {
                            @Override
                            public void onCancelClick(View v) {
                                dialogUtils.dismissDialog();
                            }

                            @Override
                            public void onConfirmClick(View v, String content) {
                                if (!StrUtils.isEmpty(content)) {
                                    record(datas.get(position).CommTenantId, content);
                                }
                            }
                        }, false);
                        break;
                }
            }
        });

    }

    private void setRecyclerView() {
        adapter = new OperationAdapter(R.layout.item_operation_layout, null);
        RecycleViewDivider recycleViewDivider = new RecycleViewDivider(getContext(), LinearLayout.VERTICAL, 20, Color.parseColor("#f3f3f7"));
        recyclerView.addItemDecoration(recycleViewDivider);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(adapter);
        adapter.setType(true);
        adapter.setOnDeleteClickListener(new OperationAdapter.OnDeleteClickLister() {
            @Override
            public void onDeleteClick(View view, int position) {
                del(datas.get(position).CommTenantId);
            }
        });
        adapter.setEnableLoadMore(false);
        adapter.disableLoadMoreIfNotFullPage(recyclerView);
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

    private List<CommTenantBean> datas = new ArrayList<>();

    private void getData() {
        addSubscription(RequestClient.MyCommTenantList(index, getContext(), new NetSubscriber<BaseResultBean<OperationBean>>(getContext()) {
            @Override
            public void onResultNext(BaseResultBean<OperationBean> model) {
                if (!adapter.isLoadMoreEnable()) {
                    adapter.setEnableLoadMore(true);
                }
                refreshLayout.finishRefresh();

                if (index == 1) {
//                    adapter.setNewData(model.data.CommTenantList);
                    datas.clear();
                    if (model.data.MyCommTenantList.size() > 0) {
                        datas.addAll(model.data.MyCommTenantList);
                        adapter.loadMoreComplete();
                    } else {
                        adapter.setEmptyView(R.layout.empty1_layout,recyclerView);
                        adapter.loadMoreEnd();
                    }
                    adapter.setNewData(datas);
                } else {
                    if (model.data.MyCommTenantList.size() > 0) {
                        adapter.addData(model.data.MyCommTenantList);
                        adapter.loadMoreComplete();
                    } else {
                        adapter.loadMoreEnd();
                    }
                }
            }
        }));
    }

    private void initLocation() {

        LocationClientOption option = new LocationClientOption();
        option.setOpenGps(true);
        option.setLocationMode(LocationClientOption.LocationMode.Hight_Accuracy);
        option.setCoorType("bd09ll");
        option.setIsNeedAddress(true);
        mLocationClient = new LocationClient(getContext().getApplicationContext());
        mLocationClient.setLocOption(option);
        mLocationClient.start();

        //声明LocationClient类
        mLocationClient.registerLocationListener(new BDAbstractLocationListener() {
            @Override
            public void onReceiveLocation(BDLocation bdLocation) {
                if (null != bdLocation) {
                    float radius = bdLocation.getRadius();
                   String latitude = bdLocation.getLatitude()+"";
                   String longitude = bdLocation.getLongitude()+"";
                    SPUtils.getInstance(getContext()).saveData(Contans.LATITUDE, bdLocation.getLatitude() + "");
                    SPUtils.getInstance(getContext()).saveData(Contans.LONGITUDE, bdLocation.getLongitude() + "");
                    getNewData();
                }
            }
        });
        //注册监听函数



    }

    private void checkPerm() {
        String[] params = {Manifest.permission.ACCESS_FINE_LOCATION};
        if (EasyPermissions.hasPermissions(getContext(), params)) {
            initLocation();
        } else {
            EasyPermissions.requestPermissions(this, "需要定位权限,和相机权限", 100, params);
        }
    }

    private void record(int id, String content) {
        addSubscription(RequestClient.RecordClockIn(id, content, 1, getContext(), new NetSubscriber<BaseResultBean>(getContext(), true) {
            @Override
            public void onResultNext(BaseResultBean model) {
                dialogUtils.dismissDialog();
                Toast.makeText(getContext(), "打卡成功", Toast.LENGTH_SHORT).show();
            }
        }));
    }

    private void del(int id) {
        addSubscription(RequestClient.DelMyCommtenant(id, getContext(), new NetSubscriber<BaseResultBean>(getContext(), true) {
            @Override
            public void onResultNext(BaseResultBean model) {
                dialogUtils.noBtnDialog("删除成功");
                index = 1;
                getData();
                recyclerView.closeMenu();
            }
        }));
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if (!hidden) {
            getNewData();
        }
    }

    private void getNewData() {
        index = 1;
        adapter.setEnableLoadMore(false);
        adapter.disableLoadMoreIfNotFullPage();
        getData();
    }
}
