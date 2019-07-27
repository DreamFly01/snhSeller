package com.snh.moudle_coupons.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.snh.library_base.BaseActivity;
import com.snh.library_base.wediget.RecycleViewDivider;
import com.snh.module_netapi.requestApi.BaseResultBean;
import com.snh.module_netapi.requestApi.NetSubscriber;
import com.snh.moudle_coupons.R;
import com.snh.moudle_coupons.R2;
import com.snh.moudle_coupons.adapter.ScrollRightAdapter;
import com.snh.moudle_coupons.bean.CouponsProductIdBean;
import com.snh.moudle_coupons.bean.RetailProductBean;
import com.snh.moudle_coupons.bean.ScrollBean;
import com.snh.moudle_coupons.netapi.RequestClient;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * <p>desc：<p>
 * <p>author：DreamFly<p>
 * <p>creatTime：2019/6/3<p>
 * <p>changeTime：2019/6/3<p>
 * <p>version：1<p>
 */
public class ProductActivity extends BaseActivity {
    @BindView(R2.id.heard_back)
    LinearLayout heardBack;
    @BindView(R2.id.heard_title)
    TextView heardTitle;
    @BindView(R2.id.heard_menu)
    ImageView heardMenu;
    @BindView(R2.id.heard_tv_menu)
    TextView heardTvMenu;
    @BindView(R2.id.rl_menu)
    RelativeLayout rlMenu;
    @BindView(R2.id.rl_head)
    LinearLayout rlHead;
    @BindView(R2.id.coupons_rec_right)
    RecyclerView recRight;
    @BindView(R2.id.coupons_right_title)
    TextView rightTitle;
    //title的高度
    private int tHeight;
    //记录右侧当前可见的第一个item的position
    private int first = 0;
    private LinearLayoutManager rightManager;
    private List<ScrollBean> right = new ArrayList<>();
    private ScrollRightAdapter rightAdapter;

    private List<CouponsProductIdBean> idBeanList = new ArrayList<>();

    private Bundle bundle;

    @Override
    protected void initContentView(Bundle savedInstanceState) {
        setContentView(R.layout.coupons_activity_product_layout);
        bundle = getIntent().getExtras();
        if (null != bundle) {
            idBeanList = bundle.getParcelableArrayList("idList");
        }
    }

    @Override
    public void setUpViews() {
        heardTitle.setText("设置优惠产品");
        heardTvMenu.setText("完成");
        getData();
    }

    @Override
    public void setUpLisener() {

    }

    List<RetailProductBean> bean = new ArrayList<>();

    private void getData() {
        addSubscription(RequestClient.GetGoodsBySupplierId(this, new NetSubscriber<BaseResultBean<List<RetailProductBean>>>(this, true) {
            @Override
            public void onResultNext(BaseResultBean<List<RetailProductBean>> model) {
                if (idBeanList.size() > 0 && model.data.size() > 0) {
                    for (int i = 0; i < model.data.size(); i++) {
                        for (int j = 0; j < model.data.get(i).CommTenantList.size(); j++) {
                            for (int k = 0; k < idBeanList.size(); k++) {
                                if (idBeanList.get(k).GoodsId == model.data.get(i).CommTenantList.get(j).ShopgoodsId) {
                                    model.data.get(i).CommTenantList.get(j).isSelect = true;
                                }
                            }
                        }
                    }
                }
                bean = model.data;

                initData(model.data);

            }
        }));
    }

    //获取数据(若请求服务端数据,请求到的列表需有序排列)
    private void initData(List<RetailProductBean> bean) {
        right.clear();
        ScrollBean scrollBean = null;
        ScrollBean.ScrollItemBean scrollItemBean;
        if (bean.size() > 0) {
            for (int i = 0; i < bean.size(); i++) {
                scrollBean = new ScrollBean(true, bean.get(i).CategoryName);
                scrollBean.SupplierId = bean.get(i).CategoryId;
                right.add(scrollBean);
                for (int j = 0; j < bean.get(i).CommTenantList.size(); j++) {
                    scrollItemBean = new ScrollBean.ScrollItemBean();
                    scrollItemBean.Inventory = bean.get(i).CommTenantList.get(j).Inventory;
                    scrollItemBean.type = bean.get(i).CategoryName;
                    scrollItemBean.CommodityIconUrl = bean.get(i).CommTenantList.get(j).CommodityIconUrl;
                    scrollItemBean.ShopgoodsId = bean.get(i).CommTenantList.get(j).ShopgoodsId;
                    scrollItemBean.CommodityName = bean.get(i).CommTenantList.get(j).CommodityName;
                    scrollItemBean.isSelect = bean.get(i).CommTenantList.get(j).isSelect;
                    scrollBean.itemBean = scrollItemBean;

                    scrollBean = new ScrollBean(scrollItemBean);
                    right.add(scrollBean);
                }
            }
        }
        initRight();

    }

    private void initRight() {
        rightManager = new LinearLayoutManager(this);
        if (rightAdapter == null) {
            rightAdapter = new ScrollRightAdapter(R.layout.coupons_item_product_content, R.layout.layout_right_title, right);
            recRight.setLayoutManager(rightManager);
            recRight.addItemDecoration(new RecycleViewDivider(this,LinearLayout.VERTICAL,R.drawable.line1));
            recRight.setAdapter(rightAdapter);
        } else {
            rightAdapter.notifyDataSetChanged();
        }


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
                tHeight = rightTitle.getHeight();
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


                //如果右边最后一个完全显示的item的position,等于bean中最后一条数据的position(也就是右侧列表拉到底了),
                //则设置左侧列表最后一条item高亮
//                if (rightManager.findLastCompletelyVisibleItemPosition() == right.size() - 1) {
//                    leftAdapter.selectItem(left.size() - 1);
//                }
            }
        });
        rightAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                if (right.get(position - 1).itemBean.isSelect) {
                    right.get(position - 1).itemBean.isSelect = false;
                } else {
                    right.get(position - 1).itemBean.isSelect = true;
                }
                rightAdapter.notifyItemChanged(position, right.get(position - 1));
//                showShortToast("position：" + (position - 1));
                rightAdapter.notifyDataSetChanged();
            }
        });
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);
    }

    @OnClick({R2.id.heard_tv_menu, R2.id.heard_back})
    public void onClick(View view) {
        if (view.getId() == R.id.heard_tv_menu) {
            idBeanList.clear();
            for (int i = 0; i < right.size(); i++) {
                if (!right.get(i).isHeader) {
                    if (right.get(i).t.isSelect) {
                        CouponsProductIdBean idBean = new CouponsProductIdBean();
                        idBean.GoodsId = right.get(i).t.ShopgoodsId;
                        idBean.GoodsImg = right.get(i).t.CommodityIconUrl;
                        idBeanList.add(idBean);
                    }
                }

            }
            if (idBeanList.size() > 0) {
                Intent intent = new Intent();
                intent.putParcelableArrayListExtra("idList", (ArrayList<? extends Parcelable>) idBeanList);
                ProductActivity.this.setResult(100, intent);
                ProductActivity.this.finish();
            } else {
                showShortToast("请选择商品");
            }
        } else if (view.getId() == R.id.heard_back) {
            this.finish();
        }
    }
}
