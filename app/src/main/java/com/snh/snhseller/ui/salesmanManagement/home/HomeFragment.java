package com.snh.snhseller.ui.salesmanManagement.home;

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
import com.snh.snhseller.ui.salesmanManagement.home.declaration.Declaration1Activity;
import com.snh.snhseller.ui.salesmanManagement.home.declaration.DeclarationActivity;
import com.snh.snhseller.ui.salesmanManagement.home.declaration.DeclarationListActivity;
import com.snh.snhseller.utils.IsBang;
import com.snh.snhseller.utils.JumpUtils;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

/**
 * <p>desc：<p>
 * <p>author：DreamFly<p>
 * <p>creatTime：2019/2/26<p>
 * <p>changeTime：2019/2/26<p>
 * <p>version：1<p>
 */
public class HomeFragment extends BaseFragment {
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
    @BindView(R.id.ll_01)
    LinearLayout ll01;
    @BindView(R.id.ll_02)
    LinearLayout ll02;
    @BindView(R.id.ll_03)
    LinearLayout ll03;
    Unbinder unbinder;
    @BindView(R.id.ll_04)
    LinearLayout ll04;

    @Override
    public int initContentView() {
        return R.layout.fragment_salehome_layout;
    }

    @Override
    public void setUpViews(View view) {
        heardTitle.setText("我的");
        heardBack.setVisibility(View.GONE);
        IsBang.setImmerHeard(getContext(), rlHead, "");
        ImmersionBar.setTitleBar(getActivity(), rlHead);
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

    @OnClick({R.id.ll_01, R.id.ll_02, R.id.ll_03,R.id.ll_04})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.ll_01:
                break;
            case R.id.ll_02:
                JumpUtils.simpJump(getActivity(), Declaration1Activity.class, false);
                break;
            case R.id.ll_03:
                JumpUtils.simpJump(getActivity(), SetActivity.class, false);
                break;
            case R.id.ll_04:
                JumpUtils.simpJump(getActivity(), DeclarationListActivity.class, false);
                break;
        }
    }
}
