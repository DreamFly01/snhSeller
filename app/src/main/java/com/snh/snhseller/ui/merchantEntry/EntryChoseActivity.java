package com.snh.snhseller.ui.merchantEntry;

import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.snh.snhseller.adapter.EntryCompanyAdapter;
import com.snh.snhseller.bean.BaseResultBean;
import com.snh.snhseller.bean.CompanyBean;
import com.snh.snhseller.bean.InSwitchBean;
import com.snh.snhseller.requestApi.NetSubscriber;
import com.snh.snhseller.requestApi.RequestClient;
import com.snh.snhseller.utils.DialogUtils;
import com.snh.snhseller.utils.IsBang;
import com.snh.snhseller.utils.JumpUtils;
import com.gyf.barlibrary.ImmersionBar;
import com.snh.snhseller.R;
import com.zhy.adapter.recyclerview.MultiItemTypeAdapter;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import me.weyye.hipermission.HiPermission;
import me.weyye.hipermission.PermissionActivity;
import me.weyye.hipermission.PermissionCallback;
import me.weyye.hipermission.PermissionItem;
import pub.devrel.easypermissions.EasyPermissions;

/**
 * <p>desc：<p>
 * <p>author：DreamFly<p>
 * <p>creatTime：2019/1/22<p>
 * <p>changeTime：2019/1/22<p>
 * <p>version：1<p>
 */
public class EntryChoseActivity extends AppCompatActivity {
    @BindView(R.id.heard_back)
    LinearLayout heardBack;
    @BindView(R.id.heard_title)
    TextView heardTitle;
    @BindView(R.id.heard_menu)
    ImageView heardMenu;
    @BindView(R.id.heard_tv_menu)
    TextView heardTvMenu;
    @BindView(R.id.tv_01)
    TextView tv01;
    @BindView(R.id.iv_01)
    ImageView iv01;
    @BindView(R.id.ll_01)
    LinearLayout ll01;
    @BindView(R.id.tv_02)
    TextView tv02;
    @BindView(R.id.iv_02)
    ImageView iv02;
    @BindView(R.id.ll_02)
    LinearLayout ll02;
    @BindView(R.id.tv_03)
    TextView tv03;
    @BindView(R.id.iv_03)
    ImageView iv03;
    @BindView(R.id.ll_03)
    LinearLayout ll03;
    @BindView(R.id.tv_personOrAer)
    TextView tvPersonOrAer;
    @BindView(R.id.rl_entry_person)
    RelativeLayout rlEntryPerson;
    @BindView(R.id.recyclerView_company)
    RecyclerView recyclerViewCompany;
    @BindView(R.id.rl_entry_company)
    RelativeLayout rlEntryCompany;
    @BindView(R.id.tv_commit)
    TextView tvCommit;
    @BindView(R.id.radio_button_1)
    ImageView radioButton1;
    @BindView(R.id.tv_xieyi)
    TextView tvXieyi;
    @BindView(R.id.rl_heard)
    LinearLayout rlHeard;
    @BindView(R.id.tv_look)
    TextView tvLook;

    private EntryCompanyAdapter adapter;
    private List<CompanyBean> dadtas = new ArrayList<>();
    private CompanyBean bean, bean1, bean2, bean3;
    private boolean isClick = true;
    private ImmersionBar immersionBar;
    private DialogUtils dialogUtils;
    private Bundle bundle;
    private String phone;
    private String typeStr;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_entrychose_layout);
        ButterKnife.bind(this);
        setUpViews();
        dialogUtils = new DialogUtils(this);
        bundle = getIntent().getExtras();
        if (null != bundle) {
            phone = bundle.getString("phone");
        }
    }

    public void setUpViews() {
        immersionBar = ImmersionBar.with(this);
        immersionBar.statusBarColor("#77a2f9");
        immersionBar.titleBar(rlHeard);
        immersionBar.init();
        IsBang.setImmerHeard(this, rlHeard, "#77a2f9");
        heardTitle.setText("选择店铺类型");
        getInSwich();
        setBtn(tv03,iv03);
        type = 3;
        rlEntryPerson.setVisibility(View.VISIBLE);
        rlEntryCompany.setVisibility(View.GONE);
        tvPersonOrAer.setText(R.string.tip_aer);
    }

    private void setDatas() {
        bean = new CompanyBean();
        bean1 = new CompanyBean();
        bean2 = new CompanyBean();
        bean3 = new CompanyBean();
        bean.id = "1";
        bean.name = "旗舰店";
        bean.introcude = "经营1个自有品牌或者1级授权品品牌旗舰店";
        bean1.id = "2";
        bean1.name = "专卖店";
        bean1.introcude = "经营1个自有品牌或者授权销售专卖店（不超过2级）";
        bean2.id = "3";
        bean2.name = "专营店";
        bean2.introcude = "经营1个或多个自有/他人品牌的专营店";
        bean3.id = "4";
        bean3.name = "普通店";
        bean3.introcude = "普通企业店铺";
        dadtas.add(bean);
        dadtas.add(bean1);
        dadtas.add(bean2);
        dadtas.add(bean3);

        adapter = new EntryCompanyAdapter(this, R.layout.item_company_layout, dadtas);
        recyclerViewCompany.setLayoutManager(new LinearLayoutManager(this));
        recyclerViewCompany.setNestedScrollingEnabled(false);
        recyclerViewCompany.setAdapter(adapter);

        adapter.setOnItemClickListener(new MultiItemTypeAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, RecyclerView.ViewHolder holder, int position) {

                for (int i = 0; i < dadtas.size(); i++) {
                    if (i == position) {
                        dadtas.get(i).isChose = true;
                        typeStr = dadtas.get(i).id;
                    } else {
                        dadtas.get(i).isChose = false;
                    }
                }
                adapter.updata(dadtas);

            }

            @Override
            public boolean onItemLongClick(View view, RecyclerView.ViewHolder holder, int position) {
                return false;
            }
        });
    }

    private void setBtn(TextView tv, ImageView iv) {
        tv01.setTextColor(Color.BLACK);
        tv02.setTextColor(Color.BLACK);
        tv03.setTextColor(Color.BLACK);
        iv01.setVisibility(View.GONE);
        iv02.setVisibility(View.GONE);
        iv03.setVisibility(View.GONE);
        tv.setTextColor(Color.parseColor("#fc1a4e"));
        iv.setVisibility(View.VISIBLE);
    }

    private int type = 1;

    @OnClick({R.id.heard_back, R.id.ll_01, R.id.ll_02, R.id.ll_03, R.id.tv_commit, R.id.radio_button_1, R.id.tv_xieyi, R.id.tv_look})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.heard_back:
                this.finish();
                break;
            case R.id.ll_01:
                type = 1;
                setBtn(tv01, iv01);
                rlEntryPerson.setVisibility(View.VISIBLE);
                rlEntryCompany.setVisibility(View.GONE);
                tvPersonOrAer.setText(R.string.tip_person);
                break;
            case R.id.ll_02:
                type = 2;
                dadtas.clear();
                setBtn(tv02, iv02);
                rlEntryPerson.setVisibility(View.GONE);
                rlEntryCompany.setVisibility(View.VISIBLE);
                setDatas();
                break;
            case R.id.ll_03:
                type = 3;
                setBtn(tv03, iv03);
                rlEntryPerson.setVisibility(View.VISIBLE);
                rlEntryCompany.setVisibility(View.GONE);
                tvPersonOrAer.setText(R.string.tip_aer);
                break;
            case R.id.tv_commit:
                bundle = new Bundle();
                bundle.putString("phone", phone);
                if (check()) {
                    if (type == 1) {
                        if (null != data && "1".equals(data.inPersonal)) {
                            bundle.putString("shopType", "3");
                            JumpUtils.dataJump(this, PerfectPersonActivity.class, bundle, false);
                        } else {
                            dialogUtils.noBtnDialog("暂未开放此入驻");
                        }


                    } else if (type == 2) {
                        if (null != data && "1".equals(data.inEnterprise)) {
                            bundle.putString("ShopCategoryType", typeStr);
                            bundle.putString("shopType", "2");
                            JumpUtils.dataJump(this, PerfectCompanyActivity.class, bundle, false);
                        } else {
                            dialogUtils.noBtnDialog("暂未开放此入驻");
                        }

                    } else if (type == 3) {
                        if (null != data && "1".equals(data.inLocal)) {
                            bundle = new Bundle();
                            bundle.putString("shopType", "1");
                            bundle.putInt("flag", type);
                            bundle.putString("phone", phone);
                            requestPermision();
                        } else {
                            dialogUtils.noBtnDialog("暂未开放此入驻");
                        }


                    }
                }


                break;
            case R.id.radio_button_1:
                if (isClick) {
                    radioButton1.setBackgroundResource(R.drawable.xiyi_2);
                    isClick = false;
                } else {
                    radioButton1.setBackgroundResource(R.drawable.xiyi_1);
                    isClick = true;
                }
                break;
            case R.id.tv_xieyi:
                Bundle bundle = new Bundle();
                bundle.putInt("flag", 4);
                JumpUtils.simpJump(this, ProtocolActivity.class, false);
                break;
            case R.id.tv_look:
                JumpUtils.simpJump(this, ImageViewActivity.class, false);
                break;
        }
    }

    private boolean isChose = false;

    private boolean check() {
        if (type == 2) {
            for (int i = 0; i < dadtas.size(); i++) {
                isChose = isChose | dadtas.get(i).isChose;
            }
            if (!isChose) {
                dialogUtils.noBtnDialog("请选择店铺类型");
                return false;
            }
        }
        if (!isClick) {
            dialogUtils.noBtnDialog("请同意协议");
            return false;
        }
        return true;
    }

    private InSwitchBean data;

    private void getInSwich() {
        RequestClient.GetInSwitch(this, new NetSubscriber<BaseResultBean<InSwitchBean>>(this, true) {
            @Override
            public void onResultNext(BaseResultBean<InSwitchBean> model) {
                data = model.data;
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (null != immersionBar) {
            immersionBar.destroy();
        }
    }

    private void requestPermision() {

        String[] params = {Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.CAMERA,Manifest.permission.WRITE_EXTERNAL_STORAGE};
        if (!EasyPermissions.hasPermissions(this, params)) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                ActivityCompat.requestPermissions(this, params, 10010);
            }

        } else {
            JumpUtils.dataJump(EntryChoseActivity.this, PerfectLocalActivity.class, bundle, false);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        boolean a = false;
        if(requestCode == 10010){
            for (int i = 0; i < grantResults.length; i++) {
                if(grantResults[i]==-1){
                    a = true;
                    break;
                }
            }
        }
        if (requestCode == 10010) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                if (a) {
                    boolean b = shouldShowRequestPermissionRationale(permissions[0]);
                    boolean b1 = shouldShowRequestPermissionRationale(permissions[1]);
                    boolean b2 = shouldShowRequestPermissionRationale(permissions[2]);
                    if (b) {
                        Toast.makeText(this, "请授予位置权限", Toast.LENGTH_SHORT).show();
                    }
                    if (b1) {
                        Toast.makeText(this, "请授予相机权限", Toast.LENGTH_SHORT).show();
                    }
                    if(b2){
                        Toast.makeText(this, "请授予读写权限", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    JumpUtils.dataJump(EntryChoseActivity.this, PerfectLocalActivity.class, bundle, false);
                }
            }
        }
    }
}
