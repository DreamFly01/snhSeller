package com.snh.snhseller.ui.product;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.text.TextPaint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONObject;
import com.gyf.barlibrary.ImmersionBar;
import com.snh.snhseller.BaseFragment;
import com.snh.snhseller.R;
import com.snh.snhseller.db.DBManager;
import com.snh.snhseller.ui.home.set.ChangePswActivity;
import com.snh.snhseller.ui.merchantEntry.PerfectMyLocalActivity;
import com.snh.snhseller.utils.Contans;
import com.snh.snhseller.utils.DialogUtils;
import com.snh.snhseller.utils.IsBang;
import com.snh.snhseller.utils.JumpUtils;
import com.snh.snhseller.utils.SPUtils;
import com.snh.snhseller.utils.StrUtils;
import com.snh.snhseller.utils.UpdateAppHttpUtil;
import com.vector.update_app.UpdateAppBean;
import com.vector.update_app.UpdateAppManager;
import com.vector.update_app.UpdateCallback;
import com.vector.update_app.listener.ExceptionHandler;
import com.vector.update_app.utils.AppUpdateUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

/**
 * <p>desc：用户商品<p>
 * <p>author：DreamFly<p>
 * <p>creatTime：2019/2/22<p>
 * <p>changeTime：2019/2/22<p>
 * <p>version：1<p>
 */
public class ProductFragment extends BaseFragment {

    @BindView(R.id.tab_order)
    TabLayout tabOrder;
    @BindView(R.id.tab_vp)
    ViewPager tabVp;
    Unbinder unbinder;
    @BindView(R.id.fab)
    ImageView fab;
    @BindView(R.id.tv_01)
    TextView tv01;
    @BindView(R.id.ll_01)
    LinearLayout ll01;
    @BindView(R.id.tv_02)
    TextView tv02;
    @BindView(R.id.ll_02)
    LinearLayout ll02;
    @BindView(R.id.rl_head1)
    LinearLayout rlHead;
    @BindView(R.id.rl_search)
    RelativeLayout rlSearch;
    private String[] titles;
    private List<Fragment> list = new ArrayList<>();
    private Bundle bundle;
    private MyAdapter adapter;
    private List<String> listTab = new ArrayList<>();

    private DialogUtils dialogUtils;
    private List<String> data1 = new ArrayList<>();
    private List<Integer> data2 = new ArrayList<>();
    private int[] viewLocation = new int[2];
    private int type = 0;
    private TextPaint paint1, paint2;

    @Override
    public int initContentView() {
        return R.layout.fragment_product_layout;
    }

    @Override
    public void setUpViews(View view) {
//        IsBang.setImmerHeard(getContext(), rlHead);
//        ImmersionBar.setTitleBar(getActivity(), rlHead);
        ImmersionBar.with(getActivity()).statusBarColor(R.color.white).statusBarDarkFont(true).init();
        updataVersion();
        paint1 = tv01.getPaint();
        paint2 = tv02.getPaint();
        dialogUtils = new DialogUtils(getContext());
        setIndicator(tabOrder,40,40);
        setType(0);

    }

    @Override
    public void setUpLisener() {

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

    @OnClick({R.id.fab, R.id.ll_01, R.id.ll_02, R.id.rl_search})
    public void onClick(View view) {
        if (isFastClick()) {
            return;
        }
        switch (view.getId()) {
            case R.id.fab:
                if ("1".equals(SPUtils.getInstance(getContext()).getString(Contans.IS_FULL))) {

                    if (!StrUtils.isEmpty(DBManager.getInstance(getContext()).getUserInfo().suppType)) {
                        if (DBManager.getInstance(getContext()).getUserInfo().suppType.equals("6") && DBManager.getInstance(getContext()).getUserInfo().BusinessActivities.equals("1")) {
                            bundle = new Bundle();
                            bundle.putInt("type", 1);
                            ProductList1Fragment.isClick = true;
                            JumpUtils.dataJump(getActivity(), EditProductActivity.class, bundle, false);
                        } else {
                            Toast.makeText(getContext(), "请前往电脑端添加", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(getContext(), "请前往电脑端添加", Toast.LENGTH_SHORT).show();
                    }
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
            case R.id.heard_menu:
//                data1.clear();
//                data2.clear();
//                data1.add("批发商品");
//
//                data2.add(R.drawable.product2_bg);
//
//                dialogUtils.customDialog(new DialogUtils.OnItemClick() {
//                    @Override
//                    public void onItemClick(View v, int position) {
//                        dialogUtils.dismissDialog();
//                        JumpUtils.simpJump(getActivity(), BusinessProduct1Activity.class, false);
//                    }
//                }, viewLocation[1] + heardMenu.getMeasuredHeight() / 2, viewLocation[0], heardMenu.getWidth(), data1, data2);
                break;
            case R.id.ll_01:
                type = 0;
                setType(0);

                ll01.setEnabled(false);
                ll02.setEnabled(true);
                tv02.setTextColor(Color.parseColor("#6E6E6E"));
                tv01.setTextColor(Color.parseColor("#1e1e1e"));

                break;
            case R.id.ll_02:
                type = 1;

                setType(1);
                ll01.setEnabled(true);
                ll02.setEnabled(false);
                tv01.setTextColor(Color.parseColor("#6E6E6E"));
                tv02.setTextColor(Color.parseColor("#1e1e1e"));

                break;
            case R.id.rl_search:
                if (type == 0) {
                    ProductList1Fragment.isClick = true;
                    JumpUtils.simpJump(getActivity(), ProductAllActivity.class, false);
                } else if (type == 1) {
                    JumpUtils.simpJump(getActivity(), WholesaleAllActivity.class, false);
                }
                break;
        }
    }


    public class MyAdapter extends FragmentStatePagerAdapter {


        public MyAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            return list.get(position);
        }

        @Override
        public int getCount() {
            return list.size();
        }

        public View getTabView(int position) {
            View v = LayoutInflater.from(getContext()).inflate(R.layout.tab_custom, null);
            TextView mTv_Title = (TextView) v.findViewById(R.id.mTv_Title);
            ImageView mImg = (ImageView) v.findViewById(R.id.mImg);
            mTv_Title.setText(titles[position]);
//            mImg.setImageResource(images[position]);
            //添加一行，设置颜色
            mTv_Title.setTextColor(tabOrder.getTabTextColors());//
            return v;
        }

        @Override
        public int getItemPosition(@NonNull Object object) {
            return PagerAdapter.POSITION_NONE;
        }
    }


    private void setType(int orderType) {
        list.clear();

        switch (orderType) {
            case 0:
                paint1.setFakeBoldText(true);
                paint2.setFakeBoldText(false);
                titles = new String[]{"出售中", "审核中", "商品库"};
                fab.setVisibility(View.VISIBLE);
                for (int i = 0; i < titles.length; i++) {
                    ProductList1Fragment fragment = new ProductList1Fragment();
                    bundle = new Bundle();
                    switch (i) {
                        case 0:
                            bundle.putInt("type", 1);
                            break;
                        case 1:
                            bundle.putInt("type", 3);
                            break;
                        case 2:
                            bundle.putInt("type", 2);
                            break;
                    }

                    fragment.setArguments(bundle);
                    list.add(fragment);
                }
                break;
            case 1:
                paint1.setFakeBoldText(false);
                paint2.setFakeBoldText(true);
                titles = new String[]{"出售中", "商品库"};
                fab.setVisibility(View.GONE);
                BusinessProduct2Fragment businessProductFragment = new BusinessProduct2Fragment();
                BusinessProduct1Fragment businessProduct1Fragment = new BusinessProduct1Fragment();
                list.add(businessProductFragment);
                list.add(businessProduct1Fragment);
                break;
        }
        adapter = new MyAdapter(getChildFragmentManager());
        tabVp.setAdapter(adapter);
        tabOrder.setupWithViewPager(tabVp);
        for (int i = 0; i < tabOrder.getTabCount(); i++) {
            TabLayout.Tab tab = tabOrder.getTabAt(i);
            tab.setCustomView(adapter.getTabView(i));
        }
        for (int i = 0; i < listTab.size(); i++) {
            tabOrder.addTab(tabOrder.newTab().setText(listTab.get(i)));
        }

        tabOrder.post(new Runnable() {
            @Override
            public void run() {
                setIndicator(tabOrder, 20, 20);
            }
        });
        tabVp.setOffscreenPageLimit(titles.length);
    }

    private void updataVersion() {
        new UpdateAppManager.Builder()
                .setActivity(getActivity())
                .setHttpManager(new UpdateAppHttpUtil())
                .setUpdateUrl("http://shop.hnyunshang.com/api/webapi/Version/GetVersion?Channel=1&SourceSystem=2")
                .handleException(new ExceptionHandler() {
                    @Override
                    public void onException(Exception e) {
                        e.printStackTrace();
                    }
                })
                .hideDialogOnDownloading()
//                .setParams(params)设置自定义参数
//                .setTopPic(R.drawable.top_3)
//                .setThemeColor(R.color.app_red)
                .build()
                .checkNewApp(new UpdateCallback() {
                    /**
                     * 解析json,自定义协议
                     *
                     * @param json 服务器返回的json
                     * @return UpdateAppBean
                     */
                    @Override
                    protected UpdateAppBean parseJson(String json) {
                        UpdateAppBean updateAppBean = new UpdateAppBean();
                        String isUpdata = "No";
                        try {
                            JSONObject jsonObject = JSONObject.parseObject(json);
                            JSONObject data = jsonObject.getJSONObject("data");

                            if (AppUpdateUtils.getVersionCode(getContext()) < Integer.parseInt(data.getString("VersionCode"))) {
                                isUpdata = "Yes";
                            }
                            updateAppBean
                                    //是否更新Yes,No
                                    .setUpdate(isUpdata)
                                    //新版本号
                                    .setNewVersion(data.getString("VersionName"))
                                    //下载地址
                                    .setApkFileUrl(data.getString("DownloadPath"))
                                    //大小
//                                    .setTargetSize(data.getString("target_size"))
                                    //更新内容
                                    .setUpdateLog(jsonObject.getString("Describe"))
                                    //是否强制更新
                                    .setConstraint(data.getBoolean("IsCompel"));
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        return updateAppBean;
                    }

                    /**
                     * 有新版本
                     *
                     * @param updateApp        新版本信息
                     * @param updateAppManager app更新管理器
                     */
                    @Override
                    public void hasNewApp(UpdateAppBean updateApp, UpdateAppManager updateAppManager) {
//                        updateAppManager.showDialog();
                        updateAppManager.showDialogFragment();
                    }

                    /**
                     * 网络请求之前
                     */
                    @Override
                    public void onBefore() {
//                        CProgressDialogUtils.showProgressDialog(MainActivity.this);
                    }

                    /**
                     * 网路请求之后
                     */
                    @Override
                    public void onAfter() {
//                        CProgressDialogUtils.cancelProgressDialog(MainActivity.this);
                    }

                    @Override
                    protected void noNewApp(String error) {
                        super.noNewApp(error);
                    }
                });
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if(!hidden){
            ImmersionBar.with(getActivity()).statusBarColor(R.color.white).statusBarDarkFont(true).init();
        }
    }
}
