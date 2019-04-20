package com.snh.snhseller.ui.salesmanManagement.operationList;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

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
import com.snh.snhseller.utils.DialogUtils;
import com.snh.snhseller.utils.IsBang;
import com.snh.snhseller.utils.StrUtils;
import com.snh.snhseller.wediget.RecycleViewDivider;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

import static com.netease.nim.uikit.common.util.sys.NetworkUtil.isNetworkConnected;

/**
 * <p>desc：<p>
 * <p>author：DreamFly<p>
 * <p>creatTime：2019/3/4<p>
 * <p>changeTime：2019/3/4<p>
 * <p>version：1<p>
 */
public class OperationListFragment extends BaseFragment {
    @BindView(R.id.et_search)
    EditText etSearch;
    @BindView(R.id.layout_search)
    LinearLayout layoutSearch;
    @BindView(R.id.layout_location)
    LinearLayout layoutLocation;
    @BindView(R.id.rl_head)
    LinearLayout rlHead;
    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    @BindView(R.id.refreshLayout)
    SmartRefreshLayout refreshLayout;
    Unbinder unbinder;
    private int index = 1;
    private OperationAdapter adapter;
    private DialogUtils dialogUtils;
    private List<CommTenantBean> datas = new ArrayList<>();
    private String condition = "";
    private boolean isShow = false;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // TODO: inflate a fragment view
        View rootView = super.onCreateView(inflater, container, savedInstanceState);
        unbinder = ButterKnife.bind(this, rootView);
        return rootView;
    }
    @Override
    public int initContentView() {
        return R.layout.fragment_operationlist_layout;
    }

    @Override
    public void setUpViews(View view) {
        dialogUtils = new DialogUtils(getContext());
        ImmersionBar.setTitleBar(getActivity(),rlHead);
        IsBang.setImmerHeard(getContext(),rlHead,"");
        setRecyclerView();
    }

    @Override
    public void setUpLisener() {
        etSearch.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    hintKeyBoard();
                    isShow = true;
                    index = 1;
                    adapter.setEnableLoadMore(false);
                    if (!StrUtils.isEmpty(etSearch.getText().toString())) {
                        condition = etSearch.getText().toString().trim();
                    }else {
                        condition = "";
                    }
                    getData();
                    return true;
                }
                return false;
            }
        });

        refreshLayout.setEnableLoadMore(false);
        refreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                index = 1;
                isShow = false;
                adapter.setEnableLoadMore(false);
                getData();
            }
        });
        adapter.setOnLoadMoreListener(new BaseQuickAdapter.RequestLoadMoreListener() {
            @Override
            public void onLoadMoreRequested() {
                recyclerView.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        index += 1;
                        isShow = false;
                        adapter.setEnableLoadMore(false);
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
                    case R.id.btn_add:
                        add(datas.get(position).CommTenantId);
                        break;
                }
            }
        });
        adapter.setEnableLoadMore(false);
    }
    private void getData() {
        addSubscription(RequestClient.CommTenantList(index, condition,getContext(), new NetSubscriber<BaseResultBean<OperationBean>>(getContext(),isShow) {
            @Override
            public void onResultNext(BaseResultBean<OperationBean> model) {

                if(!adapter.isLoadMoreEnable()){
                    adapter.setEnableLoadMore(true);
                }
                refreshLayout.finishRefresh();

                if (index == 1) {
//                    adapter.setNewData(model.data.CommTenantList);
                    datas.clear();
                    if (model.data.CommTenantList.size() > 0) {
                        datas.addAll(model.data.CommTenantList);
                        adapter.loadMoreComplete();
                    } else {
                        adapter.setEmptyView(R.layout.empty1_layout,recyclerView);
                        adapter.loadMoreEnd();
                    }
                    adapter.setNewData(datas);
                } else {
                    if (model.data.CommTenantList.size() > 0) {
                        adapter.addData(model.data.CommTenantList);
                        adapter.loadMoreComplete();
                    } else {
                        adapter.loadMoreEnd();
                    }
                }
            }
        }));
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
    public void hintKeyBoard() {
        //拿到InputMethodManager
        InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        //如果window上view获取焦点 && view不为空
        if (imm.isActive() && getActivity().getCurrentFocus() != null) {
            //拿到view的token 不为空
            if (getActivity().getCurrentFocus().getWindowToken() != null) {
                //表示软键盘窗口总是隐藏，除非开始时以SHOW_FORCED显示。
                imm.hideSoftInputFromWindow(getActivity().getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
            }
        }
    }

    private void setRecyclerView() {
        adapter = new OperationAdapter(R.layout.item_operation_layout, null);
        RecycleViewDivider recycleViewDivider = new RecycleViewDivider(getContext(), LinearLayout.VERTICAL, 20, Color.parseColor("#f3f3f7"));
        recyclerView.addItemDecoration(recycleViewDivider);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(adapter);
        adapter.setType(false);

    }


    private void record(int id,String content) {
        addSubscription(RequestClient.RecordClockIn(id, content, 1, getContext(), new NetSubscriber<BaseResultBean>(getContext(), true) {
            @Override
            public void onResultNext(BaseResultBean model) {
                dialogUtils.dismissDialog();
                Toast.makeText(getContext(), "打卡成功", Toast.LENGTH_SHORT).show();
            }
        }));
    }
    private void add(int id){
        addSubscription(RequestClient.AddToMyCommtenant(id, getContext(), new NetSubscriber<BaseResultBean>(getContext(),true) {
            @Override
            public void onResultNext(BaseResultBean model) {
                index = 1;
                adapter.setEnableLoadMore(false);
                getData();
                dialogUtils.noBtnDialog("添加成功");
            }
        }));
    }

    @Override
    public void onResume() {
        super.onResume();
        getData();
    }
    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if (!hidden) {
            index =1;
            adapter.setEnableLoadMore(false);
            condition = "";
            getData();
        }
    }
}
