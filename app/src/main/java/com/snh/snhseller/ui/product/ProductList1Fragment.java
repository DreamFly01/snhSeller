package com.snh.snhseller.ui.product;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.snh.library_base.utils.Contans;
import com.snh.module_netapi.requestApi.BaseResultBean;
import com.snh.module_netapi.requestApi.NetSubscriber;
import com.snh.snhseller.BaseFragment;
import com.snh.snhseller.R;
import com.snh.snhseller.adapter.ProdcutAdapter;
import com.snh.snhseller.adapter.ScrollLeftAdapter;
import com.snh.snhseller.adapter.ScrollRightAdapter;
import com.snh.snhseller.bean.ProductBean;
import com.snh.snhseller.bean.RetailProductBean;
import com.snh.snhseller.bean.ScrollBean;
import com.snh.snhseller.requestApi.RequestClient;
import com.snh.snhseller.utils.DialogUtils;
import com.snh.snhseller.utils.JumpUtils;
import com.snh.snhseller.utils.SPUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * <p>desc：<p>
 * <p>author：DreamFly<p>
 * <p>creatTime：2019/5/20<p>
 * <p>changeTime：2019/5/20<p>
 * <p>version：1<p>
 */
public class ProductList1Fragment extends BaseFragment {

    Unbinder unbinder;
    @BindView(R.id.rec_left)
    RecyclerView recLeft;
    @BindView(R.id.rec_right)
    RecyclerView recRight;
    @BindView(R.id.right_title)
    TextView rightTitle;
    @BindView(R.id.refreshLayout)
    SmartRefreshLayout refreshLayout;
    private List<String> left = new ArrayList<>();
    private List<ScrollBean> right = new ArrayList<>();
    private ScrollLeftAdapter leftAdapter;
    private ScrollRightAdapter rightAdapter;
    private List<Integer> tPosition = new ArrayList<>();
    //title的高度
    private int tHeight;
    //记录右侧当前可见的第一个item的position
    private int first = 0;
    private LinearLayoutManager rightManager;
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
                getData();
                mIsDataInited = true;
            }
        }
    }

    private boolean myIsVisible;

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
//防止数据预加载, 只预加载View，不预加载数据
        myIsVisible = isVisibleToUser;
        if (isVisibleToUser && isVisible() && !mIsDataInited) {
            getData();
            mIsDataInited = true;
        }
        if (isVisibleToUser && SPUtils.getInstance(getContext()).getBoolean(Contans.PRODUCT_IS_FRESH)) {
            index = 1;
            getData();
        }
    }

    @Override
    public int initContentView() {
        return R.layout.fragment_productlist1_layout;
    }

    @Override
    public void setUpViews(View view) {
        dialogUtils = new DialogUtils(getContext());
        initRight();
        initLeft();
    }

    @Override
    public void setUpLisener() {
        refreshLayout.setEnableLoadMore(false);
        refreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                getData();
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

    private void getData() {
        addSubscription(RequestClient.GetSaleOfGoodsTwo(type, getContext(), new NetSubscriber<BaseResultBean<List<RetailProductBean>>>(getContext(), true) {
            @Override
            public void onResultNext(BaseResultBean<List<RetailProductBean>> model) {
                refreshLayout.finishRefresh();
                isClick = false;

                initData(model.data);
            }
        }));
    }

    private void initRight() {

        rightManager = new LinearLayoutManager(getContext());

        if (rightAdapter == null) {
            rightAdapter = new ScrollRightAdapter(R.layout.item_product_content, R.layout.layout_right_title, null);
            recRight.setLayoutManager(rightManager);
//            recRight.addItemDecoration(new RecyclerView.ItemDecoration() {
//                @Override
//                public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
//                    super.getItemOffsets(outRect, view, parent, state);
//                    outRect.set(dpToPx(getContext(), getDimens(getContext(), R.dimen.dp3))
//                            , 0
//                            , dpToPx(getContext(), getDimens(getContext(), R.dimen.dp3))
//                            , dpToPx(getContext(), getDimens(getContext(), R.dimen.dp3)));
//                }
//            });
            recRight.setAdapter(rightAdapter);
        } else {
            rightAdapter.notifyDataSetChanged();
        }

//        rightAdapter.setNewData(right);

        //设置右侧初始title
        if (right.size() > 0) {
            if (right.get(first).isHeader) {
                rightTitle.setText(right.get(first).header);
            }
        } else {
            rightTitle.setText("");
        }

        recRight.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                //获取右侧title的高度
                if(null!=rightTitle){
                    tHeight = rightTitle.getHeight();
                }
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

                //判断如果是header
                if (right.size() > 0) {
                    if (right.get(first).isHeader) {
                        //获取此组名item的view
                        View view = rightManager.findViewByPosition(first);
                        if (view != null) {
                            //如果此组名item顶部和父容器顶部距离大于等于title的高度,则设置偏移量
                            if (view.getTop() >= tHeight) {
                                rightTitle.setY(view.getTop() - tHeight);
                            } else {
                                //否则不设置
                                rightTitle.setY(0);
                            }
                        }
                    }
                }


                //因为每次滑动之后,右侧列表中可见的第一个item的position肯定会改变,并且右侧列表中可见的第一个item的position变换了之后,
                //才有可能改变右侧title的值,所以这个方法内的逻辑在右侧可见的第一个item的position改变之后一定会执行
                int firstPosition = rightManager.findFirstVisibleItemPosition();
                if (first != firstPosition && firstPosition >= 0) {
                    //给first赋值
                    first = firstPosition;
                    //不设置Y轴的偏移量
                    rightTitle.setY(0);

                    //判断如果右侧可见的第一个item是否是header,设置相应的值
                    if (right.get(first).isHeader) {
                        rightTitle.setText(right.get(first).header);
                    } else {
                        rightTitle.setText(right.get(first).t.type);
                    }
                }

                //遍历左边列表,列表对应的内容等于右边的title,则设置左侧对应item高亮
                if (left.size() > 0) {

                    for (int i = 0; i < left.size(); i++) {
                        if (left.get(i).equals(rightTitle.getText().toString())) {
                            leftAdapter.selectItem(i);
                        }
                    }
                }

                //如果右边最后一个完全显示的item的position,等于bean中最后一条数据的position(也就是右侧列表拉到底了),
                //则设置左侧列表最后一条item高亮
//                if (rightManager.findLastCompletelyVisibleItemPosition() == right.size() - 1) {
//                    leftAdapter.selectItem(left.size() - 1);
//                }
            }
        });

        rightAdapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, final int position) {
                switch (view.getId()) {
                    case R.id.tv_xj:
                        if (right.get(position).t.status == 1) {
                            dialogUtils.twoBtnDialog("是否确定下架商品", new DialogUtils.ChoseClickLisener() {
                                @Override
                                public void onConfirmClick(View v) {
                                    UpOrDownProduct(right.get(position).t.CommTenantId, 2, "下架成功");
                                    SPUtils.getInstance(getContext()).savaBoolean(Contans.PRODUCT_IS_FRESH, true).commit();
                                }

                                @Override
                                public void onCancelClick(View v) {
                                    dialogUtils.dismissDialog();
                                }
                            }, false);
                        } else if (right.get(position).t.status == 2) {
                            dialogUtils.twoBtnDialog("是否确定上架商品", new DialogUtils.ChoseClickLisener() {
                                @Override
                                public void onConfirmClick(View v) {
                                    UpOrDownProduct(right.get(position).t.CommTenantId, 1, "上架成功");
                                    SPUtils.getInstance(getContext()).savaBoolean(Contans.PRODUCT_IS_FRESH, true).commit();
                                }

                                @Override
                                public void onCancelClick(View v) {
                                    dialogUtils.dismissDialog();
                                }
                            }, false);
                        }
                        break;
                    case R.id.tv_edit:
                        ProductList1Fragment.isClick = true;
                        Bundle bundle = new Bundle();
                        bundle.putInt("type", 2);
                        bundle.putInt("isDel", right.get(position).t.status);
                        bundle.putString("CommTenantIcon", right.get(position).t.CommTenantIcon);
                        bundle.putString("CommTenantName", right.get(position).t.CommTenantName);
                        bundle.putString("CategoryName", right.get(position).t.CategoryName);
                        bundle.putString("UnitsTitle", right.get(position).t.UnitsTitle);
                        bundle.putInt("Inventory", (int) right.get(position).t.Inventory);
                        bundle.putDouble("Price", right.get(position).t.Price);
                        bundle.putDouble("MarketPrice", right.get(position).t.MarketPrice);
                        bundle.putInt("CommTenantId", right.get(position).t.CommTenantId);
                        JumpUtils.dataJump(getActivity(), EditProductActivity.class, bundle, false);
                        break;
                }

            }
        });
    }

    private void initLeft() {
        if (leftAdapter == null) {
            leftAdapter = new ScrollLeftAdapter(R.layout.scroll_left, null);
            recLeft.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
//            recLeft.addItemDecoration(new DividerItemDecoration(mContext, DividerItemDecoration.VERTICAL));
            recLeft.setAdapter(leftAdapter);
        } else {
            leftAdapter.notifyDataSetChanged();
        }

//        leftAdapter.setNewData(left);
        leftAdapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                switch (view.getId()) {
                    //点击左侧列表的相应item,右侧列表相应的title置顶显示
                    //(最后一组内容若不能填充右侧整个可见页面,则显示到右侧列表的最底端)
                    case R.id.item:
                        leftAdapter.selectItem(position);
                        rightManager.scrollToPositionWithOffset(tPosition.get(position), 0);
                        break;

                }
            }
        });
    }

    //获取数据(若请求服务端数据,请求到的列表需有序排列)
    private void initData(List<RetailProductBean> bean) {
        left.clear();
        right.clear();
        List<ScrollBean> list = new ArrayList<>();
        ScrollBean scrollBean = null;
        ScrollBean.ScrollItemBean scrollItemBean;
        if (bean.size() > 0) {
            for (int i = 0; i < bean.size(); i++) {
                left.add(bean.get(i).CategoryName);
                scrollBean = new ScrollBean(true, bean.get(i).CategoryName);
                scrollBean.SupplierId = bean.get(i).CategoryId;
                right.add(scrollBean);
                for (int j = 0; j < bean.get(i).CommTenantList.size(); j++) {
                    scrollItemBean = new ScrollBean.ScrollItemBean();
                    scrollItemBean.Inventory = bean.get(i).CommTenantList.get(j).Inventory;
                    scrollItemBean.type = bean.get(i).CategoryName;
                    scrollItemBean.CommTenantIcon = bean.get(i).CommTenantList.get(j).CommTenantIcon;
                    scrollItemBean.CommTenantId = bean.get(i).CommTenantList.get(j).CommTenantId;
                    scrollItemBean.CommTenantName = bean.get(i).CommTenantList.get(j).CommTenantName;
                    scrollItemBean.OnThePin = bean.get(i).CommTenantList.get(j).SalesVolume;
                    scrollItemBean.Price = bean.get(i).CommTenantList.get(j).Price;
                    scrollItemBean.UnitsTitle = bean.get(i).CommTenantList.get(j).UnitsTitle;
                    scrollItemBean.status = bean.get(i).CommTenantList.get(j).Status;
                    scrollItemBean.CategoryName = bean.get(i).CommTenantList.get(j).CategoryName;
                    scrollItemBean.MarketPrice = bean.get(i).CommTenantList.get(j).MarketPrice;

                    scrollBean.itemBean = scrollItemBean;
                    scrollBean = new ScrollBean(scrollItemBean);
                    right.add(scrollBean);
                }
            }

            for (int i = 0; i < right.size(); i++) {
                if (right.get(i).isHeader) {
                    //遍历右侧列表,判断如果是header,则将此header在右侧列表中所在的position添加到集合中
                    tPosition.add(i);
                }
                if (right.get(i).SupplierId != 0) {
                    list.add(right.get(i));
                }
            }
        }
        leftAdapter.cleanTv();
        rightAdapter.setNewData(right);
        leftAdapter.setNewData(left);

    }

    /**
     * 获得资源 dimens (dp)
     *
     * @param context
     * @param id      资源id
     * @return
     */
    public float getDimens(Context context, int id) {
        DisplayMetrics dm = context.getResources().getDisplayMetrics();
        float px = context.getResources().getDimension(id);
        return px / dm.density;
    }

    /**
     * dp转px
     *
     * @param context
     * @param dp
     * @return
     */
    public int dpToPx(Context context, float dp) {
        DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
        return (int) ((dp * displayMetrics.density) + 0.5f);
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

    public static boolean isClick = false;

    @Override
    public void onResume() {
        super.onResume();
        if (isClick && myIsVisible) {
            getData();
        }
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
}
