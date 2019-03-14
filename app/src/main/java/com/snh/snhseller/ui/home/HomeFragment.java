package com.snh.snhseller.ui.home;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.gyf.barlibrary.ImmersionBar;
import com.snh.snhseller.BaseFragment;
import com.snh.snhseller.R;
import com.snh.snhseller.ui.home.account.ShopInfoActivity;
import com.snh.snhseller.ui.home.accoutData.DataStatisticsActivity;
import com.snh.snhseller.ui.home.costApply.CostApplyActivity;
import com.snh.snhseller.ui.home.salesManagement.SalesManagementActivity;
import com.snh.snhseller.ui.home.set.QRcodeActivity;
import com.snh.snhseller.ui.home.set.SetActivity;
import com.snh.snhseller.ui.home.supplier.MySupplierActivity;
import com.snh.snhseller.utils.DBManager;
import com.snh.snhseller.utils.ImageUtils;
import com.snh.snhseller.utils.IsBang;
import com.snh.snhseller.utils.JumpUtils;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

/**
 * <p>desc：<p>
 * <p>author：DreamFly<p>
 * <p>creatTime：2019/2/22<p>
 * <p>changeTime：2019/2/22<p>
 * <p>version：1<p>
 */
public class HomeFragment extends BaseFragment {
    @BindView(R.id.iv_heard)
    ImageView ivHeard;
    @BindView(R.id.tv_name)
    TextView tvName;
    @BindView(R.id.ll_heard)
    LinearLayout llHeard;
    @BindView(R.id.ll_01)
    LinearLayout ll01;
    @BindView(R.id.ll_02)
    LinearLayout ll02;
    @BindView(R.id.ll_03)
    LinearLayout ll03;
    @BindView(R.id.ll_04)
    LinearLayout ll04;
    Unbinder unbinder;
    @BindView(R.id.ll_05)
    LinearLayout ll05;
    @BindView(R.id.ll_06)
    LinearLayout ll06;
    @BindView(R.id.ll_07)
    LinearLayout ll07;
    @BindView(R.id.ll_08)
    LinearLayout ll08;

    private Bundle bundle;

    @Override
    public int initContentView() {
        return R.layout.fragment_home_layout;
    }

    @Override
    public void setUpViews(View view) {
        ImmersionBar.setTitleBar(getActivity(), llHeard);
        IsBang.setImmerHeard(getContext(), llHeard);
        ImageUtils.loadUrlCorners(getContext(), DBManager.getInstance(getContext()).getUserInfo().Logo, ivHeard);
        tvName.setText(DBManager.getInstance(getContext()).getUserInfo().ShopName);

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

    @OnClick({R.id.ll_heard, R.id.ll_01, R.id.ll_02, R.id.ll_03, R.id.ll_04, R.id.ll_05, R.id.ll_06, R.id.ll_07,R.id.ll_08})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.ll_heard:
                bundle = new Bundle();
                JumpUtils.dataJump(getActivity(), ShopInfoActivity.class, bundle, false);
                break;
            case R.id.ll_01:
                break;
            case R.id.ll_02:
                JumpUtils.simpJump(getActivity(), DataStatisticsActivity.class, false);
                break;
            case R.id.ll_03:
                JumpUtils.simpJump(getActivity(), QRcodeActivity.class, false);
                break;
            case R.id.ll_04:
                JumpUtils.simpJump(getActivity(), SalesManagementActivity.class, false);
                break;
            case R.id.ll_05:
                bundle = new Bundle();
                bundle.putInt("type", 1);
                JumpUtils.dataJump(getActivity(), MySupplierActivity.class, bundle, false);
                break;
            case R.id.ll_06:
                bundle = new Bundle();
                bundle.putInt("type", 2);
                JumpUtils.dataJump(getActivity(), MySupplierActivity.class, bundle, false);
                break;

            case R.id.ll_07:
                JumpUtils.simpJump(getActivity(), SetActivity.class, false);

                break;
            case R.id.ll_08:
                JumpUtils.simpJump(getActivity(), CostApplyActivity.class, false);
                break;
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if (!this.isHidden()) {
            ImageUtils.loadUrlCorners(getContext(), DBManager.getInstance(getContext()).getUserInfo().Logo, ivHeard);
            System.out.println(DBManager.getInstance(getContext()).getUserInfo().Logo);
            tvName.setText(DBManager.getInstance(getContext()).getUserInfo().ShopName);
        }
    }
}
