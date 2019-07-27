package com.snh.snhseller;

import android.Manifest;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.snh.library_base.db.DBManager;
import com.snh.library_base.utils.Contans;
import com.snh.snhseller.ui.loging.LogingActivity;
import com.snh.snhseller.ui.salesmanManagement.SalesmanMainActivity;
import com.snh.snhseller.utils.JumpUtils;
import com.snh.snhseller.utils.SPUtils;
import com.tencent.bugly.crashreport.CrashReport;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import pub.devrel.easypermissions.AfterPermissionGranted;
import pub.devrel.easypermissions.EasyPermissions;

/**
 * <p>desc：<p>
 * <p>author：DreamFly<p>
 * <p>creatTime：2019/2/19<p>
 * <p>changeTime：2019/2/19<p>
 * <p>version：1<p>
 */
public class WelcomActivity extends BaseActivity {

    private static int[] imagIds = {R.drawable.welcome_page_1, R.drawable.welcome_page_2,
            R.drawable.welcome_page_3};
    @BindView(R.id.tv_jump)
    TextView tvJump;
    @BindView(R.id.iv_welcome_bg)
    ImageView ivWelcomeBg;
    @BindView(R.id.activity_welcome_pager)
    ViewPager activityWelcomePager;
    public static WelcomActivity instans;

    @Override
    protected void onStart() {
        super.onStart();
        System.out.println("welcomOnStart-->"+System.currentTimeMillis());
    }

    @Override
    protected void initContentView(Bundle savedInstanceState) {
        System.out.println("welcomOncreat-->"+System.currentTimeMillis());
        setImm(false);
        instans = this;
        setContentView(R.layout.activity_welcom_layout);
    }

    private Timer timer = new Timer();
    private int num = 2;
    TimerTask task = new TimerTask() {
        @Override
        public void run() {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    num--;
//                    tvJump.setText("跳过(" + num + ")");
                    if (num < 1) {
                        timer.cancel();
                        checkPerm();
                    }
                }
            });
        }
    };

    @Override
    public void setUpViews() {
        if (SPUtils.getInstance(this).getBoolean(Contans.LAUNCH_FRIST)) {
            ivWelcomeBg.setVisibility(View.GONE);
            activityWelcomePager.setVisibility(View.VISIBLE);
            SPUtils.getInstance(this).saveData(Contans.SP_HOSt,Contans.HOST);

            initWelcom();
        } else {
            ivWelcomeBg.setVisibility(View.VISIBLE);
            activityWelcomePager.setVisibility(View.GONE);
            timer.schedule(task, 0, 1000);
//            Bundle bundle = new Bundle();
//            if (null != DBManager.getInstance(this).getUserInfo()) {
//                CrashReport.setUserId(DBManager.getInstance(this).getUseId()+"");
//                JumpUtils.dataJump(WelcomActivity.this, MainActivity.class, bundle, true);
//            } else if(null != DBManager.getInstance(this).getSaleInfo()){
//                JumpUtils.dataJump(WelcomActivity.this, SalesmanMainActivity.class, bundle, true);
//            }else {
//                JumpUtils.dataJump(WelcomActivity.this, LogingActivity.class, bundle, true);
//            }
        }
    }

    @Override
    public void setUpLisener() {

    }

    private void initWelcom() {
        List<ImageView> datas = new ArrayList<>();
        ivWelcomeBg.setVisibility(View.GONE);
        for (int i = 0; i < imagIds.length; i++) {
            ImageView iv = new ImageView(this);
            iv.setScaleType(ImageView.ScaleType.FIT_XY);
//            iv.setImageResource(imagIds[i]);
            Glide.with(this).load(imagIds[i]).into(iv);
            datas.add(iv);

        }
        MyViewPagerAdapor myViewPagerAdapor = new MyViewPagerAdapor(datas);
        activityWelcomePager.setAdapter(myViewPagerAdapor);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);


    }

    @OnClick({R.id.tv_jump})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_jump:
                timer.cancel();
                checkPerm();
                break;
        }
    }


    private class MyViewPagerAdapor extends PagerAdapter {

        private List<ImageView> mList = new ArrayList<>();

        public MyViewPagerAdapor(List<ImageView> data) {
            mList = data;
        }

        @Override
        public int getCount() {
            return mList.size();
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            if (position == 2) {
                mList.get(2).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        checkPerm();
                    }
                });
            }
            container.addView(mList.get(position));
            return mList.get(position);
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView(mList.get(position));
        }
    }

    @AfterPermissionGranted(100)
    private void checkPerm() {
        String[] params = {Manifest.permission.WRITE_EXTERNAL_STORAGE,Manifest.permission.ACCESS_FINE_LOCATION};
            if (SPUtils.getInstance(this).getBoolean(Contans.LAUNCH_FRIST)) {
                SPUtils.getInstance(this).savaBoolean(Contans.LAUNCH_FRIST, false);
            }
            Bundle bundle = new Bundle();
            if (null != DBManager.getInstance(this).getUserInfo()) {
                CrashReport.setUserId(DBManager.getInstance(this).getUseId()+"");
                JumpUtils.dataJump(WelcomActivity.this, MainActivity.class, bundle, true);
            } else if(null != com.snh.snhseller.db.DBManager.getInstance(this).getSaleInfo()){
                JumpUtils.dataJump(WelcomActivity.this, SalesmanMainActivity.class, bundle, true);
            }else  {
                JumpUtils.dataJump(WelcomActivity.this, LogingActivity.class, bundle, true);
            }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        timer.cancel();
    }

    @Override
    protected void onStop() {
        super.onStop();
        timer.cancel();
    }
}
